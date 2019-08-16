package com.gionee.gnservice.domain.model;

import android.os.Build;
import android.text.TextUtils;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.http.HttpParam;
import com.gionee.gnservice.common.http.IHttpHelper;
import com.gionee.gnservice.common.http.IHttpResponse;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.entity.MemberLevel;
import com.gionee.gnservice.entity.MemberPrivilege;
import com.gionee.gnservice.entity.MemberPrivilegeContent;
import com.gionee.gnservice.exception.NetWorkException;
import com.gionee.gnservice.sdk.member.database.DatabaseManager;
import com.gionee.gnservice.sdk.member.database.IMemberPrivilegeDatabase;
import com.gionee.gnservice.utils.GNDecodeUtils;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.PhoneUtil;
import com.gionee.gnservice.utils.PreconditionsUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caocong on 2/27/17.
 */

public class MemberPrivilegeModel extends BaseModel {
    private static final String TAG = MemberPrivilegeModel.class.getSimpleName();
    private static final long UPDATE_DIFF_TIME = 3 * 24 * 60 * 60 * 1000L;

    private IMemberPrivilegeDatabase mDbManager;
    private MemberLevel mMemberLevel;

    public MemberPrivilegeModel(IAppContext appContext) {
        super(appContext);
        mDbManager = new DatabaseManager(mAppContext.application());
    }

    public com.gionee.gnservice.domain.Observable<List<MemberPrivilege>> getMemberPivileges(MemberLevel memberLevel) {
        //PreconditionsUtil.checkNotNull(memberLevel);
        if (memberLevel == null) {
            memberLevel = MemberLevel.GOLD;
        }
        mMemberLevel = memberLevel;

        return new com.gionee.gnservice.domain.Observable<List<MemberPrivilege>>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    List<MemberPrivilege> cache = loadDataFromDb();
                    boolean isCacheExist = (cache != null && !cache.isEmpty());
                    boolean isOffLine = !isNetworkConnect();
                    boolean isCacheValid = !isCacheExpired();

                    if (isCacheExist) {
                        if (isCacheValid || isOffLine) {
                            publishNext(cache);
                            return null;
                        }

                        List<MemberPrivilege> privileges = loadDataFromNet();
                        publishNext(privileges);
                    } else {
                        if (isOffLine) {
                            publishError(new NetWorkException());
                            return null;
                        }

                        List<MemberPrivilege> privileges = loadDataFromNet();
                        publishNext(privileges);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    publishError(e);
                }

                return null;
            }
        };
    }

    public com.gionee.gnservice.domain.Observable<Integer> getMemberPivilegesSize(MemberLevel memberLevel) {
        PreconditionsUtil.checkNotNull(memberLevel);
        mMemberLevel = memberLevel;

        return new com.gionee.gnservice.domain.Observable<Integer>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    List<MemberPrivilege> cache = loadDataFromDb();
                    boolean isCacheExist = (cache != null && !cache.isEmpty());
                    LogUtil.d(TAG, "is privilege cache exists=" + isCacheExist);
                    boolean isNetWorkable = isNetworkConnect();
                    if (isCacheExist && isNetWorkable) {
                        if (!isCacheExpired()) {
                            LogUtil.d(TAG, "privilege cache is not expried");
                            publishNext(cache.size());
                            return null;
                        }
                        LogUtil.d(TAG, "privilege cache time is expired,should refresh");
                        List<MemberPrivilege> privileges = loadDataFromNet();
                        if (privileges != null && !privileges.isEmpty()) {
                            publishNext(privileges.size());

                        } else {
                            publishNext(cache.size());
                        }
                    } else if (isCacheExist) {
                        LogUtil.d(TAG, "privilege cache is exist,net work is not available");
                        publishNext(cache.size());
                    } else if (isNetWorkable) {
                        LogUtil.d(TAG, "cache is not exist,load data from net");
                        List<MemberPrivilege> datalist = loadDataFromNet();
                        publishNext(datalist.size());
                    } else {
                        throw new NetWorkException();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    publishError(e);
                }
                return null;
            }
        };
    }

    private List<MemberPrivilege> loadDataFromDb() {
        return mDbManager.queryMemberPrivilegesByMemberLevel(mMemberLevel);
    }

    private boolean isCacheExpired() {
        long lastUpdateTime = mDbManager.queryLastUpdateTime();
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastUpdateTime) >= UPDATE_DIFF_TIME;

    }

    private List<MemberPrivilege> loadDataFromNet() throws Exception {
        LogUtil.d(TAG, "load member privilege from net");
        IHttpHelper httpHelper = mAppContext.httpHelper();
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("v", "0");
        params.put("model", Build.MODEL);
        params.put("imei", GNDecodeUtils.get(PhoneUtil.getIMEI(mAppContext.application())));
        params.put("nt", "wifi");
        params.put("grade", String.valueOf(mMemberLevel.getValue()));
        LogUtil.d("memberActivity", "memberlevle is:" + mMemberLevel.getValue() + ";task post body=" + params.toString());
        HttpParam.Builder builder = new HttpParam.Builder();
        builder.setUrl(AppConfig.URL.getMemberPrivilegeUrl()).setParams(params);
        IHttpResponse response = httpHelper.get(builder.build());
        if (response == null) {
            return new ArrayList<MemberPrivilege>();
        }
        String responseInfo = httpHelper.get(builder.build()).getString();
        return parseJson(responseInfo);
    }

    private List<MemberPrivilege> parseJson(String json) throws JSONException {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        List<MemberPrivilege> memberPrivilegeList = new ArrayList<MemberPrivilege>();
        List<MemberPrivilegeContent> memberPrivilegeContentList = new ArrayList<MemberPrivilegeContent>();
        JSONObject jos = new JSONObject(json);
        int netVersion = jos.getInt("v");
        int currentVersion = mDbManager.queryVersion();
        LogUtil.d("currentVersion=" + currentVersion + ";netVersion=" + netVersion);
        JSONArray catesArray = jos.getJSONArray("cates");
        for (int i = 0; i < catesArray.length(); i++) {
            MemberPrivilege memberPrivilege = new MemberPrivilege();
            JSONObject jo = catesArray.getJSONObject(i);
            memberPrivilege.setId(getIntValueByKey("id", jo));
            memberPrivilege.setName(getValueByKey("name", jo));
            memberPrivilege.setDescription(getValueByKey("desc", jo));
            memberPrivilege.setIconUrl(getValueByKey("icon", jo));
            memberPrivilege.setIcon2Url(getValueByKey("icon2", jo));
            memberPrivilege.setImgUrl(getValueByKey("img", jo));
            memberPrivilege.setMemberLevel(mMemberLevel);
            memberPrivilegeList.add(memberPrivilege);
        }

        JSONArray dataArray = jos.getJSONArray("data");
        for (int j = 0; j < dataArray.length(); j++) {
            MemberPrivilegeContent mpc = new MemberPrivilegeContent();
            JSONObject jo = dataArray.getJSONObject(j);
            mpc.setId(jo.getInt("id"));
            mpc.setName(getValueByKey("name", jo));
            mpc.setCid(getIntValueByKey("cid", jo));
            mpc.setContent(getValueByKey("cn", jo));
            memberPrivilegeContentList.add(mpc);
        }
        sortPrivilege(memberPrivilegeList);
        buildMemberPrivileges(memberPrivilegeList, memberPrivilegeContentList);
        LogUtil.d(TAG, "get privilege list from net size is:" + memberPrivilegeList.size());
        mDbManager.saveLastUpdateTime(System.currentTimeMillis());
        mDbManager.saveMemberPrivileges(memberPrivilegeList);
        mDbManager.saveVersion(String.valueOf(currentVersion));
        return memberPrivilegeList;
    }

    private String getValueByKey(String key, JSONObject jos) throws JSONException {
        String value = "";
        if (jos.has(key)) {
            value = jos.getString(key);
        }
        return value;
    }

    private int getIntValueByKey(String key, JSONObject jos) throws JSONException {
        String value = getValueByKey(key, jos);
        if (TextUtils.isEmpty(value)) {
            return -1;
        } else {
            return Integer.parseInt(value);
        }
    }

    private void buildMemberPrivileges(List<MemberPrivilege> memberPrivilegeList,
                                       List<MemberPrivilegeContent> memberPrivilegeContentList) {
        for (MemberPrivilege mp : memberPrivilegeList) {
            int id = mp.getId();
            List<MemberPrivilegeContent> mpcList = new ArrayList<MemberPrivilegeContent>();
            for (MemberPrivilegeContent mpc : memberPrivilegeContentList) {
                if (mpc.getCid() == id) {
                    mpcList.add(mpc);
                }
            }
            sortPrivilegeContent(mpcList);
            mp.setContentParts(mpcList);
        }

    }

    private void sortPrivilege(List<MemberPrivilege> memberPrivilegeList) {
        Collections.sort(memberPrivilegeList, new Comparator<MemberPrivilege>() {
            @Override
            public int compare(MemberPrivilege o1, MemberPrivilege o2) {
                return o1.getId() - o2.getId();
            }
        });
        putMorePrivilegeAtLast(memberPrivilegeList);
    }

    //更多权限放到最后
    private void putMorePrivilegeAtLast(List<MemberPrivilege> memberPrivilegeList) {
        int index = -1;
        int N = memberPrivilegeList.size();
        for (int i = 0; i < N; i++) {
            if ((i != N - 1) && "please wait".equals(memberPrivilegeList.get(i).getDescription())) {
                index = i;
            }
        }
        if (index != -1) {
            LogUtil.d(TAG, "");
            memberPrivilegeList.add(memberPrivilegeList.remove(index));
        }
    }

    private void sortPrivilegeContent(List<MemberPrivilegeContent> memberPrivilegeContentList) {
        Collections.sort(memberPrivilegeContentList, new Comparator<MemberPrivilegeContent>() {
            @Override
            public int compare(MemberPrivilegeContent lhs, MemberPrivilegeContent rhs) {
                return lhs.getId() - rhs.getId();
            }
        });
    }

}
