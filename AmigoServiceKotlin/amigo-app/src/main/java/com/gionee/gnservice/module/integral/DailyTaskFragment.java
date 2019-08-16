package com.gionee.gnservice.module.integral;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gionee.gnservice.R;
import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.imageloader.ImageConfig;
import com.gionee.gnservice.common.imageloader.ImageLoaderImpl;
import com.gionee.gnservice.entity.IntegralTask;
import com.gionee.gnservice.entity.IntegralTaskResult;
import com.gionee.gnservice.module.main.WebViewActivity;
import com.gionee.gnservice.sdk.AmigoServiceSdk;
import com.gionee.gnservice.sdk.integral.TextUpDownScroll;
import com.gionee.gnservice.sdk.integral.mvp.IMemberIntegralMakeContract;
import com.gionee.gnservice.sdk.integral.mvp.MemberIntegralMakePresenter;
import com.gionee.gnservice.statistics.StatisticsEvents;
import com.gionee.gnservice.statistics.StatisticsUtil;
import com.gionee.gnservice.utils.ImageOOMUtil;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.NetworkUtil;
import com.gionee.gnservice.utils.ResourceUtil;
import com.gionee.gnservice.utils.SdkUtil;
import com.gionee.gnservice.utils.ToastUtil;
import com.gionee.gnservice.widget.AutoVerticalScrollTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.gionee.gnservice.utils.ResourceUtil.getLayoutId;
import static com.gionee.gnservice.utils.ResourceUtil.getWidgetId;

/**
 * Created by borney on 9/8/15.
 */
public class DailyTaskFragment extends Fragment implements IMemberIntegralMakeContract.View {
    private static final String TAG = DailyTaskFragment.class.getSimpleName();
    private ListView mListMenu;
    private List<IntegralTask> mIntegralTasks;
    private ItemAdapter mAdapter;
    private View mLoadingView;
    private View mEmptyView;
    private View mLayoutScoll;
    private AutoVerticalScrollTextView mTxtPrize;
    private View mLayoutNum;
    private TextView mTxtNum;
    private TextUpDownScroll mPrizeScroll;
    private View mLayoutTask;
    private IMemberIntegralMakeContract.Presenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, "onCreate");
        mIntegralTasks = new ArrayList<IntegralTask>();
        mPresenter = new MemberIntegralMakePresenter(appContext(), this);
    }

    protected IAppContext appContext() {
        IAppContext mAppContext;
        if (SdkUtil.isCallBySdk(getActivity())) {
            mAppContext = AmigoServiceSdk.getInstance().appContext();
        } else {
            mAppContext = (IAppContext) getActivity().getApplication();
        }
        return mAppContext;
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getView(View view, int id) {
        return (T) view.findViewById(id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutResId(), null);
        mLoadingView = getView(rootView, getWidgetId(getActivity(), "progress_member_integral_make_daily"));
        mEmptyView = getView(rootView, getWidgetId(getActivity(), "layout_member_integral_daily_task_empty"));
        showLoadStartView();
        mListMenu = getView(rootView, getWidgetId(getActivity(), "listview_member_integral_make_daily"));
        mAdapter = new ItemAdapter(getActivity(), mIntegralTasks);
        mListMenu.setAdapter(mAdapter);
        mTxtPrize = getView(rootView, getWidgetId(getActivity(), "txt_member_integral_task_prize"));
        mLayoutNum = getView(rootView, getWidgetId(getActivity(), "layout_member_integral_task_num"));
        mLayoutNum.setBackground(new BitmapDrawable(getResources(),
                ImageOOMUtil.getBitmap(getContext(), ResourceUtil.getDrawableId(getContext(), "uc_bg_member_integral_make_top"))));
        mTxtNum = getView(rootView, getWidgetId(getActivity(), "txt_member_integral_task_num"));
        mLayoutTask = getView(rootView, getWidgetId(getActivity(), "layout_member_integral_task"));
        mLayoutScoll = getView(rootView, getWidgetId(getActivity(), "layout_member_integral_task_prize"));
        return rootView;
    }

    private void onItemClick(int position) {
        if (ToastUtil.showNetWorkErrorToast(getContext())) {
            return;
        }
        IntegralTask task = mIntegralTasks.get(position);
        if (task.getStatus() == IntegralTask.STATUS_DONE) {
            return;
        }
        boolean needUpload = true;

        Intent intent = new Intent();
        switch (task.getType()) {
            case IntegralTask.TYPE_WEB:
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("url", task.getContent());
                startActivity(intent);
                break;
            case IntegralTask.TYPE_APP:
                String content = task.getContent();
                if (isJson(content)) {
                    String packageName = null;
                    String action = null;
                    String data = null;
                    try {
                        JSONObject jo = new JSONObject(content);
                        action = jo.getString("action");
                        packageName = jo.getString("package");
                        data = jo.getString("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    LogUtil.d(TAG, "item click,content is:" + content + "packagename is:" + packageName + ";action is:" + action + ", data:" + data);
                    if (!TextUtils.isEmpty(packageName)) {
                        intent.setPackage(packageName);
                    }
                    if (!TextUtils.isEmpty(action)) {
                        intent.setAction(action);
                    }
                    if (!TextUtils.isEmpty(data)) {
                        intent.setData(Uri.parse(data));
                    }
                } else {
                    intent.setAction(content);
                }

                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(intent);
                    StatisticsUtil.onEvent(getContext(), task.getId() + "-" + task.getName(), StatisticsEvents.Label.DAILY_MISSION);
                } else {
                    LogUtil.e(TAG, "Activity is not exist");
                    needUpload = false;
                }
                break;
            case IntegralTask.TYPE_NONE:
                break;
            default:
                break;
        }
        if (needUpload) {
            mPresenter.uploadIntegralTask(task.getId());
        }
    }

    private boolean isJson(String string) {
        return string.startsWith("{") && string.endsWith("}");
    }

    private int getLayoutResId() {
        return ResourceUtil.getLayoutId(getActivity(), "uc_fragment_member_integral_task");
    }

    @Override
    public void showLoadStartView() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showIntegralTaskView(List<IntegralTask> integralTasks) {
        if (!isAdded()) {
            return;
        }
        if (integralTasks == null) {
            return;
        }

        LogUtil.d(TAG, "task number: " + integralTasks.size());
        integralTasks = filterData(integralTasks);
        if (integralTasks == null || integralTasks.isEmpty()) {
            mEmptyView.setVisibility(View.VISIBLE);
            mLoadingView.setVisibility(View.GONE);
            mLayoutTask.setVisibility(View.GONE);
        } else {
            mLayoutTask.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            mLoadingView.setVisibility(View.GONE);
            mIntegralTasks = integralTasks;
            mAdapter.setData(mIntegralTasks);
            mAdapter.notifyDataSetChanged();
        }
        showTaskNumView(integralTasks);
    }

    private void showTaskNumView(List<IntegralTask> integralTasks) {
        if (integralTasks.size() > 0 && isAdded()) {
            int size = 0;
            int num = 0;
            for (IntegralTask task : integralTasks) {
                if (task.getStatus() == IntegralTask.STATUS_NOT_DONE) {
                    num += task.getValue();
                    size++;
                }
            }
            mLayoutNum.setVisibility(View.VISIBLE);
            String value = String.format(getString(R.string.uc_user_center_member_integral_task_description), String.valueOf(size), String.valueOf(num));
            mTxtNum.setText(formatSpannableString(value, String.valueOf(size), String.valueOf(num)));
        }
    }

    private CharSequence formatSpannableString(String value, String number1, String number2) {
        int start1 = value.indexOf(number1);
        int end1 = start1 + number1.length();
        int start2 = value.lastIndexOf(number2);
        int end2 = start2 + number2.length();
        SpannableStringBuilder style = new SpannableStringBuilder(value);
        int color = getResources().getColor(R.color.uc_user_center_card_integral_num);
        style.setSpan(new ForegroundColorSpan(color), start1, end1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new AbsoluteSizeSpan((int) getContext().getResources().getDimension(R.dimen.uc_user_center_card_integral_task_num)), start1, end1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new ForegroundColorSpan(color), start2, end2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new AbsoluteSizeSpan((int) getContext().getResources().getDimension(R.dimen.uc_user_center_card_integral_task_num)), start2, end2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return style;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private List<IntegralTask> filterData(List<IntegralTask> integralTasks) {
        List<IntegralTask> tasks = new ArrayList<IntegralTask>();
        for (IntegralTask task : integralTasks) {
            if (task.getIntegralType() == IntegralTask.PTYPE_DAILY) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    @Override
    public void showLoadFailView() {
        if (!isAdded()) {
            LogUtil.e(TAG, "not added to Activity");
            return;
        }

        mLoadingView.setVisibility(View.GONE);
        if (!NetworkUtil.isConnected(appContext().application())) {
            ToastUtil.showLong(appContext().application(), ResourceUtil.getString(getActivity(), "uc_network_exception"));
        } else {
            ToastUtil.showLong(appContext().application(), ResourceUtil.getString(getActivity(), "uc_user_center_member_fetch_integral_fail"));
        }
    }

    @Override
    public void showPrizeView(List<String> prizeList) {
        if (!isAdded()) {
            return;
        }
        if (prizeList == null) {
            return;
        }
        if (prizeList.isEmpty()) {
            return;
        }

        mLayoutScoll.setVisibility(View.VISIBLE);
        mPrizeScroll = new TextUpDownScroll(mTxtPrize, prizeList);
        mPrizeScroll.startScroll();
    }

    @Override
    public void showUploadIntegralTaskView(IntegralTaskResult result) {
        if (!isAdded() && result.getId() == -1 || findIntegralTaskById(result.getId()) == null) {
            return;
        }

        int code = result.getCode();
        if (code == IntegralTaskResult.CODE_GET_ERROR) {
            ToastUtil.showLong(appContext().application(), ResourceUtil.getString(appContext().application(), "uc_user_center_member_integral_make_beyond_max"));
        } else if (code == IntegralTaskResult.CODE_SCUCESS) {
            IntegralTask integralTask = findIntegralTaskById(result.getId());
            ToastUtil.showLong(appContext().application(), String.format(ResourceUtil.getString(appContext().application(), "uc_user_center_member_integral_make_success"), String.valueOf(integralTask.getValue())));
            integralTask.setStatus(IntegralTask.STATUS_DONE);
            showTaskNumView(mIntegralTasks);
            mAdapter.notifyDataSetChanged();
        }
    }

    public IntegralTask findIntegralTaskById(int id) {
        if (mIntegralTasks == null || mIntegralTasks.isEmpty()) {
            return null;
        }
        for (IntegralTask task : mIntegralTasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }


    private class ItemAdapter extends BaseAdapter {
        private List<IntegralTask> mIntegralTasks;
        private Context mContext;

        public ItemAdapter(Context context, List<IntegralTask> integralTasks) {
            this.mContext = context;
            setData(integralTasks);
        }

        public void setData(List<IntegralTask> integralTasks) {
            if (integralTasks != null) {
                mIntegralTasks = integralTasks;
            } else {
                mIntegralTasks = new ArrayList<IntegralTask>();
            }
        }

        @Override
        public int getCount() {
            return mIntegralTasks.size();
        }

        @Override
        public IntegralTask getItem(int position) {
            return mIntegralTasks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(getLayoutId(mContext, "uc_list_item_member_integral_make"), null);
                viewHolder = new ViewHolder();
                viewHolder.statusLayout = getView(convertView, getWidgetId(mContext, "txt_list_item_member_integral_status_layout"));
                viewHolder.imgIcon = getView(convertView, getWidgetId(mContext, "img_list_item_member_integral_make"));
                viewHolder.txtNumber = getView(convertView, getWidgetId(mContext, "txt_list_item_member_integral_make_number"));
                viewHolder.txtDescription = getView(convertView, getWidgetId(mContext, "txt_list_item_member_integral_make_duration"));
                viewHolder.txtName = getView(convertView, getWidgetId(mContext, "txt_list_item_member_integral_make_name"));
                viewHolder.btnAction = getView(convertView, getWidgetId(mContext, "btn_list_item_member_integral_make_action"));
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            IntegralTask integralTask = getItem(position);
            int value = integralTask.getValue();
            if (value >= 0) {
                viewHolder.txtNumber.setText("+" + String.valueOf(value));
            } else {
                viewHolder.txtNumber.setText("-" + String.valueOf(value));
            }
            if (!TextUtils.isEmpty(integralTask.getIconUrl())) {
                ImageConfig.Builder builder = new ImageConfig.Builder();
                builder.setUrl(integralTask.getIconUrl())
                        .setImageView(viewHolder.imgIcon)
                        .setPlaceholder(ResourceUtil.getDrawableId(getContext(), "uc_ic_member_integral_make_default"))
                        .setErrorPic(ResourceUtil.getDrawableId(getContext(), "uc_ic_member_integral_make_default"))
                        .setCacheInDisk(true)
                        .setCacheInMemory(true);
                boolean setGray = integralTask.getStatus() == IntegralTask.STATUS_DONE;
                if (setGray) {
                    builder.setListener(new ImageTintListener());
                }
                ImageLoaderImpl.create(getContext()).loadImage(builder.build());
            }
            viewHolder.txtDescription.setText(integralTask.getDescription());
            viewHolder.txtName.setText(integralTask.getName());
            switch (integralTask.getStatus()) {
                case IntegralTask.STATUS_DONE:
                    viewHolder.statusLayout.setBackgroundColor(0x1a828282);
                    viewHolder.txtName.setTextColor(ResourceUtil.getColor(getContext(), "uc_integral_task_title_done"));
                    viewHolder.txtDescription.setTextColor(ResourceUtil.getColor(getContext(), "uc_integral_task_summary_done"));
                    viewHolder.btnAction.setText(ResourceUtil.getString(getContext(), "uc_txt_member_integral_task_done"));
                    viewHolder.btnAction.setTextColor(ResourceUtil.getColor(getContext(), "uc_btn_integral_task_done"));
                    viewHolder.btnAction.setBackground(ResourceUtil.getDrawable(getContext(), "uc_member_integral_task_done_shape"));
                    viewHolder.txtNumber.setTextColor(ResourceUtil.getColor(getContext(), "uc_txt_integral_task_done"));
                    break;
                case IntegralTask.STATUS_NOT_DONE:
                    viewHolder.statusLayout.setBackgroundColor(0x1accbe7a);
                    viewHolder.txtName.setTextColor(ResourceUtil.getColor(getContext(), "uc_integral_task_title_not_done"));
                    viewHolder.txtDescription.setTextColor(ResourceUtil.getColor(getContext(), "uc_integral_task_summary_not_done"));
                    viewHolder.btnAction.setText(ResourceUtil.getString(getContext(), "uc_txt_member_integral_task_not_done"));
                    viewHolder.btnAction.setTextColor(ResourceUtil.getColor(getContext(), "uc_btn_integral_task_not_done"));
                    viewHolder.btnAction.setBackground(ResourceUtil.getDrawable(getContext(), "uc_member_integral_task_not_done_shape"));
                    viewHolder.txtNumber.setTextColor(ResourceUtil.getColor(getContext(), "uc_txt_integral_task_not_done"));
                    break;
                default:
                    break;
            }
            viewHolder.btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(position);
                }
            });

            return convertView;

        }

        <T extends View> T getView(View view, int id) {
            return (T) view.findViewById(id);
        }

        private class ViewHolder {
            LinearLayout statusLayout;
            TextView txtName, txtDescription, txtNumber;
            ImageView imgIcon;
            Button btnAction;
        }
    }

}
