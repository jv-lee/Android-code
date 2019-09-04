package gionee.gnservice.app.tool;

import android.app.Activity;
import android.util.Log;
import com.gionee.gnservice.statistics.StatisticsUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.lee.library.utils.LogUtil;
import com.mobgi.MobgiInterstitialAd;
import com.mobgi.MobgiVideoAd;
import gionee.gnservice.app.BuildConfig;
import gionee.gnservice.app.Config;

public class AManager {
    private static AManager mInstance;
    private MobgiVideoAd adVideoView;
    private MobgiInterstitialAd adScreenView;
    private boolean adReady;
    private boolean googleReady;
    private ACallback mACallback;

    private RewardedVideoAd mRewardedVideoAd;
    private boolean isRewarded;

    private AManager() {
    }

    public static AManager getInstance() {
        synchronized (AManager.class) {
            if (mInstance == null) {
                mInstance = new AManager();
            }
        }
        return mInstance;
    }

    public void init(Activity activity) {
        initGoogleAd(activity);
        registerVideoAd(activity);
        registerScreen(activity);
    }

    public void initGoogleAd(Activity activity) {
        //初始化谷歌广告
        MobileAds.initialize(activity.getApplicationContext(), BuildConfig.googleAppId);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity.getApplicationContext());
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                googleReady = true;
                LogUtil.i("google>>>onRewardedVideoAdLoaded");
            }

            @Override
            public void onRewardedVideoAdOpened() {
                Config.Companion.setPlayVideo(true);
//                StatisticsUtil.onEvent(activity.getApplicationContext(), EventConstant.Video_Ad_Show, "admob");
                googleReady = false;
                LogUtil.i("google>>>onRewardedVideoAdOpened");
            }

            @Override
            public void onRewardedVideoStarted() {
                LogUtil.i("google>>>onRewardedVideoStarted");
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Config.Companion.setPlayVideo(false);
                LogUtil.i("google>>>onRewardedVideoAdClosed");
                if (!isRewarded) {
//                    StatisticsUtil.onEvent(activity.getApplicationContext(), EventConstant.Video_Ad_User_Break, "谷歌广告主动关闭");
                    mACallback.onResponse(false);
                }
                isRewarded = false;
                loadRewardedVideoAd(activity);
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                LogUtil.i("google>>>onRewarded");
//                StatisticsUtil.onEvent(activity.getApplicationContext(), EventConstant.Video_Ad_User_Finish, "admob");
                isRewarded = true;
                mACallback.onResponse(true);
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                LogUtil.i("google>>>onRewardedVideoAdLeftApplication");
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Config.Companion.setPlayVideo(false);
//                StatisticsUtil.onEvent(activity, EventConstant.Video_Ad_User_Break, "谷歌广告拉取失败");
                LogUtil.i("google>>>onRewardedVideoAdFailedToLoad:" + i);
            }

            @Override
            public void onRewardedVideoCompleted() {
                LogUtil.i("google>>>onRewardedVideoCompleted");
            }
        });
        loadRewardedVideoAd(activity);
    }

    private void loadRewardedVideoAd(Activity activity) {
//        StatisticsUtil.onEvent(activity.getApplicationContext(), EventConstant.Video_Ad_Fetch_Times, "admob");
        mRewardedVideoAd.loadAd(BuildConfig.googleVideoId, new AdRequest.Builder().build());
    }

    /**
     * 初始化激励视频广告
     */
    public void registerVideoAd(Activity activity) {
        LogUtil.i("mobgi>>>registerVideoAd: ");
        adVideoView = new MobgiVideoAd(activity, new MobgiVideoAd.AdListener() {
            @Override
            public void onAdLoaded() {
                LogUtil.i("mobgi>>>onAdLoaded");
                adReady = true;
            }

            @Override
            public void onAdLoadFailed(int i, String s) {
                LogUtil.i("mobgi>>>onAdLoadFailed: code->" + i + " msg->" + s);
                adReady = false;
//                StatisticsUtil.onEvent(activity.getApplicationContext(), EventConstant.Video_Ad_User_Break, "乐逗广告拉取失败");
            }

            @Override
            public void onAdDisplayed(String s) {
                LogUtil.i("mobgi>>>onAdDisplayed: " + s);
                Config.Companion.setPlayVideo(true);
//                StatisticsUtil.onEvent(activity.getApplicationContext(), EventConstant.Video_Ad_Show, "乐逗");
            }

            @Override
            public void onAdClicked(String s) {
                LogUtil.i("mobgi>>>onAdClicked: " + s);
//                StatisticsUtil.onEvent(activity.getApplicationContext(), EventConstant.Video_Ad_Click, "乐逗");
            }

            @Override
            public void onAdDismissed(String s, boolean b) {
                LogUtil.i("mobgi>>>onAdDismissed:" + s + " flag:" + b);
                Config.Companion.setPlayVideo(false);
                if (mACallback != null && adReady) {
//                    StatisticsUtil.onEvent(activity.getApplicationContext(), EventConstant.Video_Ad_User_Finish, "乐逗");
                    mACallback.onResponse(b);
                }
            }

            @Override
            public void onAdError(String s, int i, String s1) {
                LogUtil.i("mobgi>>>onAdError: code->" + i + " msg->" + s + " Exception->" + s1);
                Config.Companion.setPlayVideo(false);
//                StatisticsUtil.onEvent(activity.getApplicationContext(), EventConstant.Video_Ad_User_Break, "乐逗广告拉取失败");
            }
        });

    }

    public void registerScreen(Activity activity) {
        StatisticsUtil.onEvent(activity.getApplicationContext(), "Inter_Ad_Fetch_Times");
        adScreenView = new MobgiInterstitialAd(activity);
    }

    public void showScreen(String adID, Activity activity, ACallback callback) {
        adScreenView = new MobgiInterstitialAd(activity);
        if (adScreenView.isReady(adID)) {
            adScreenView.show(activity, adID, new MobgiInterstitialAd.AdInteractionListener() {
                @Override
                public void onAdClicked() {
                    LogUtil.i("screen onAdClicked: ");
//                    StatisticsUtil.onEvent(activity.getApplicationContext(), EventConstant.Inter_Ad_Result, "曝光展示");
                }

                @Override
                public void onAdDisplayed() {
                    LogUtil.i("screen onAdDisplayed: ");
//                    StatisticsUtil.onEvent(activity.getApplicationContext(), EventConstant.Inter_Ad_Result, "曝光展示");
                }

                @Override
                public void onAdDismissed() {
                    LogUtil.i("screen onAdDismissed: ");
                    if (callback != null) {
                        callback.onResponse(true);
                    }
                }

                @Override
                public void onAdError(int i, String s) {
                    LogUtil.i("screen onAdError: " + i + "  - " + s);
//                    StatisticsUtil.onEvent(activity.getApplicationContext(), EventConstant.Inter_Ad_Result, "拉取失败");
                    if (callback != null) {
                        callback.onResponse(true);
                    }
                }
            });
            LogUtil.i("screen true :ID" + adID);
        } else {
//            StatisticsUtil.onEvent(activity.getApplicationContext(), EventConstant.Inter_Ad_Result, "拉取失败");
            LogUtil.i("screen false :ID" + adID);
        }
    }

    boolean showFlag;

    /**
     * 通用广告调用方法
     *
     * @param callback
     * @param videoId
     */
    public void showVideoAD(Activity activity, String videoId, ACallback callback) {
        if (callback != null) {
            mACallback = callback;
        }
        if (!showFlag) {
            showFlag = true;
            showMobgiVideoAd(activity, videoId);

        } else {
            showFlag = false;
            if (googleReady) {
                showGoogleVideoAd();
                return;
            }
            loadRewardedVideoAd(activity);
            showMobgiVideoAd(activity, videoId);
        }

    }

    /**
     * 显示谷歌视频广告
     */
    private void showGoogleVideoAd() {
        if (mRewardedVideoAd != null) {
            if (mRewardedVideoAd.isLoaded()) {
                mRewardedVideoAd.show();
            } else {
                mACallback.onResponse(false);
            }
        }
    }

    private void showMobgiVideoAd(Activity activity, String videoId) {
        if (adVideoView != null && adReady) {
//            StatisticsUtil.onEvent(activity.getApplicationContext(), EventConstant.Video_Ad_Fetch_Times, "乐逗");
            if (adVideoView.isReady(videoId)) {
                adVideoView.show(activity, videoId);
            } else {
                mACallback.onResponse(false);
            }
        } else {
            mACallback.onResponse(false);
        }
    }

}
