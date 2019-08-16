package com.gionee.gnservice.module.main;

import android.content.Context;
import android.content.res.Resources;
import android.util.Xml;
import com.gionee.gnservice.entity.ServiceInfo;
import com.gionee.gnservice.utils.LogUtil;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ModulesXmlHelper {
    private Context mContext;

    public ModulesXmlHelper(Context context) {
        mContext = context;
    }

    public ArrayList<ServiceInfo> getDefaultFromAsset(String fileName) {
        ArrayList<ServiceInfo> tagslist = new ArrayList<ServiceInfo>();
        Resources res = mContext.getResources();
        InputStream inStream = null;
        try {
            inStream = mContext.getAssets().open(fileName);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inStream, "UTF-8");
            int eventType = parser.getEventType();
            ServiceInfo info = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        if (name.equalsIgnoreCase("tag")) {
                            info = new ServiceInfo();
                        }
                        if (name.equalsIgnoreCase("tagproperty") && info != null) {
                            String proKey = parser.getAttributeValue(0);
                            String value = parser.nextText();
                            if (proKey.equalsIgnoreCase("serviceOrder")) {
                                info.setOrder(value);
                            } else if (proKey.equalsIgnoreCase("serviceName")) {
                                info.setName(getStringResource(value, res));
                            } else if (proKey.equalsIgnoreCase("serviceIcon")) {
                                info.setIcon(getImageResourceId(value, res));
                            } else if (proKey.equalsIgnoreCase("serviceIsApk")) {
                                info.setApk(Boolean.valueOf(value));
                            } else if (proKey.equalsIgnoreCase("serviceIsIntentAction")) {
                                info.setAction(Boolean.valueOf(value));
                            } else if (proKey.equalsIgnoreCase("serviceTargetPackage")) {
                                info.setTargetPackage(value);
                            } else if (proKey.equalsIgnoreCase("serviceIsNeedTnLogin")) {
                                info.setNeedTnLogin(Boolean.valueOf(value));
                            } else if (proKey.equalsIgnoreCase("needShowTrafficAlertFirst")) {
                                info.setNeedShowTrafficAlertFirst(Boolean.valueOf(value));
                            } else if (proKey.equalsIgnoreCase("serviceBigDisplay")) {
                                info.setBigDisplay(Boolean.valueOf(value));
                            } else if (proKey.equalsIgnoreCase("serviceTarget")) {
                                info.setTarget(value);
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (name.equalsIgnoreCase("tag")) {
                            tagslist.add(info);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (Exception e) {
        } finally {
            try {
                if (null != inStream) {
                    inStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return tagslist;
    }

    private int getImageResourceId(String name, Resources res) {
        if (null != name) {
            return res.getIdentifier(name, "drawable", mContext.getPackageName());
        }
        return -1;
    }

    private String getStringResource(String name, Resources res) {
        int resId = -1;
        if (null != name) {
            resId = res.getIdentifier(name, "string", mContext.getPackageName());
        }
        if (resId != -1) {
            return res.getString(resId);
        }
        return null;
    }

}
