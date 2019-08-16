package com.gionee.gnservice.config;

import android.text.TextUtils;

public class AppConfig {
    public static final boolean DEBUG = true;
    public static final boolean TRACE_DEBUG = false;

    public static final class URL {
        //获取延保信息
        public static final String URL_GET_YAN_BAO = "http://www.datatransetl.com:8087/dc/app/getYanbaoJson.json";

        //提交延保信息
        public static final String URL_UPLOAD_YAN_BAO = "http://www.datatransetl.com:8087/dc/app/addYanbaoJson.json";

        //会员活动
        private static final String URL_MEMBER_ACTIVITY = "http://mceapi.gionee.com/mcenter-api/service/activity.do";
        private static final String URL_MEMBER_ACTIVITY_TEST = "http://t-mcenter.gionee.com/mcenter-api/service/activity.do";

        public static String getMemberActionUrl() {
            return EnvConfig.isProEnv() ? URL_MEMBER_ACTIVITY : URL_MEMBER_ACTIVITY_TEST;
        }

        private static final String URL_PRIVILEGE_ACTION = "https://mceapi.gionee.com/ucs-api/service/banner.do";
        private static final String URL_PRIVILEGE_ACTION_TEST = "https://t-mcenter.gionee.com/ucs-api/service/banner.do";

        public static String getPrivilegeActionUrl() {
            return EnvConfig.isProEnv() ? URL_PRIVILEGE_ACTION : URL_PRIVILEGE_ACTION_TEST;
        }

        //A币中心余额
        private static final String URL_A_COIN_BALANCE = "https://pay.gionee.com/proxy/user/balance";
        private static final String URL_A_COIN_BALANCE_TEST = "http://test3.gionee.com/pay/proxy/user/balance";

        public static String getACoinBalanceUrl() {
            return EnvConfig.isProEnv() ? URL_A_COIN_BALANCE : URL_A_COIN_BALANCE_TEST;
        }

        //优惠券
        private static final String URL_A_COUPON = "https://pay.gionee.com/coupon/user";
        private static final String URL_A_COUPON_TEST = "http://test3.gionee.com/pay/coupon/user";

        public static String getCouponUrl() {
            return EnvConfig.isProEnv() ? URL_A_COUPON : URL_A_COUPON_TEST;
        }


        //bbs
        private static final String URL_BBS = "http://bbs.amigo.cn";
        private static final String URL_BBS_TEST = "http://t-bbs.amigo.cn";

        public static String getBbsUrl() {
            return EnvConfig.isProEnv() ? URL_BBS : URL_BBS_TEST;
        }

        //中奖记录
        private static final String URL_MEMBER_INTEGRAL_PRIZE = "https://mceapi.gionee.com/ucs-api/point/record.do";
        public static final String URL_MEMBER_INTEGRAL_PRIZE_TEST = "https://t-mcenter.gionee.com/ucs-api/point/record.do";

        public static String getMemberIntegralPrizeUrl() {
            return EnvConfig.isProEnv() ? URL_MEMBER_INTEGRAL_PRIZE : URL_MEMBER_INTEGRAL_PRIZE_TEST;
        }

        //每日抽奖
        public static final String URL_MEMBER_INTEGRAL_LOTTERY = "https://mceapi.gionee.com/ucs-api/auth/drawact.do";
        public static final String URL_MEMBER_INTEGRAL_LOTTERY_TEST = "https://t-mcenter.gionee.com/ucs-api/auth/drawact.do";

        public static String getMemberIntegralLotteryUrl() {
            return EnvConfig.isProEnv() ? URL_MEMBER_INTEGRAL_LOTTERY : URL_MEMBER_INTEGRAL_LOTTERY_TEST;
        }

        //每日抽奖剩余次数
        public static final String URL_MEMBER_INTEGRAL_LOTTERY_TIMES = "https://mceapi.gionee.com/ucs-api/auth/drawacttimes.do";
        public static final String URL_MEMBER_INTEGRAL_LOTTERY_TIMES_TEST = "https://t-mcenter.gionee.com/ucs-api/auth/drawacttimes.do";

        public static String getMemberIntegralLotteryTimesUrl() {
            return EnvConfig.isProEnv() ? URL_MEMBER_INTEGRAL_LOTTERY_TIMES : URL_MEMBER_INTEGRAL_LOTTERY_TIMES_TEST;
        }

        //赚积分
        public static final String URL_MEMBER_INTEGRAL_MAKE = "https://mceapi.gionee.com/ucs-api/auth/ptask.do";
        public static final String URL_MEMBER_INTEGRAL_MAKE_TEST = "https://t-mcenter.gionee.com/ucs-api/auth/ptask.do";

        public static String getMemberIntegralMakeUrl() {
            return EnvConfig.isProEnv() ? URL_MEMBER_INTEGRAL_MAKE : URL_MEMBER_INTEGRAL_MAKE_TEST;
        }

        // 上传积分任务
        public static final String URL_MEMBER_INTEGRAL_TASK_UPLOAD = "https://mceapi.gionee.com/ucs-api/auth/ptask/click.do";
        public static final String URL_MEMBER_INTEGRAL_TASK_UPLOAD_TEST = "https://t-mcenter.gionee.com/ucs-api/auth/ptask/click.do";

        public static String getMemberIntegralTaskUploadUrl() {
            return EnvConfig.isProEnv() ? URL_MEMBER_INTEGRAL_TASK_UPLOAD : URL_MEMBER_INTEGRAL_TASK_UPLOAD_TEST;
        }

        //积分商城
        public static final String URL_MEMBER_INTEGRAL_MALL = "https://mceapi.gionee.com/ucs-api/auth/pshop.do";
        public static final String URL_MEMBER_INTEGRAL_MALL_TEST = "https://t-mcenter.gionee.com/ucs-api/auth/pshop.do";

        public static String getMemberIntegralMallUrl() {
            return EnvConfig.isProEnv() ? URL_MEMBER_INTEGRAL_MALL : URL_MEMBER_INTEGRAL_MALL_TEST;
        }

        //我的积分
        public static final String URL_MEMBER_INTEGRAL_USER = "https://mceapi.gionee.com/ucs-api/auth/point.do";
        public static final String URL_MEMBER_INTEGRAL_USER_TEST = "https://t-mcenter.gionee.com/ucs-api/auth/point.do";

        public static String getMemberIntegralUserUrl() {
            return EnvConfig.isProEnv() ? URL_MEMBER_INTEGRAL_USER : URL_MEMBER_INTEGRAL_USER_TEST;
        }

        //积分记录
        public static final String URL_MEMBER_INTEGRAL_RECORD = "https://mceapi.gionee.com/ucs-api/auth/point/myrecord.do";
        public static final String URL_MEMBER_INTEGRAL_RECORD_TEST = "https://t-mcenter.gionee.com/ucs-api/auth/point/myrecord.do";

        public static String getMemberIntegralRecordUrl() {
            return EnvConfig.isProEnv() ? URL_MEMBER_INTEGRAL_RECORD : URL_MEMBER_INTEGRAL_RECORD_TEST;
        }

        //积分细则
        public static final String URL_MEMBER_INTEGRAL_RULE = "https://mceapi.gionee.com/ucs-api/desc/scorerule.html";
        public static final String URL_MEMBER_INTEGRAL_RULE_TEST = "https://t-mcenter.gionee.com/ucs-api/desc/scorerule.html";

        public static String getMemberIntegralRuleUrl() {
            return EnvConfig.isProEnv() ? URL_MEMBER_INTEGRAL_RULE : URL_MEMBER_INTEGRAL_RULE_TEST;
        }

        //会员特权
        private static final String URL_MEMBER_PRIVILEGE_NEW = "https://mceapi.gionee.com/ucs-api/service/privilege.do";
        private static final String URL_MEMBER_PRIVILEGE_NEW_TEST = "https://t-mcenter.gionee.com/ucs-api/service/privilege.do";

        public static String getMemberPrivilegeUrl() {
            return EnvConfig.isProEnv() ? URL_MEMBER_PRIVILEGE_NEW : URL_MEMBER_PRIVILEGE_NEW_TEST;
        }

        private static final String URL_MEMBER_CARD_VIEW_IMAGE = "https://update.gionee.com/synth/open/app/conf.do";
        private static final String URL_MEMBER_CARD_VIEW_IMAGE_TEST = "https://t-api.gionee.com/synth/open/app/conf.do";

        public static String getMemberCardViewImageUrl() {
            return EnvConfig.isProEnv() ? URL_MEMBER_CARD_VIEW_IMAGE : URL_MEMBER_CARD_VIEW_IMAGE_TEST;
        }
    }

    public static final class Account {
        //account AppId
        public static final String AMIGO_SERVICE_APP_ID = "F7CEB1412D0A419689CFEB52C1317B48";
        public static final String AMIGO_SERVICE_APP_ID_TEST = "D198B6F9FB42450EBC0A6ED0BA39AA7F";

        private static String mAppId, mAppIdTest;

        public static String getAccountAppID() {
            if (TextUtils.isEmpty(mAppId) || TextUtils.isEmpty(mAppIdTest)) {
                return getAmigoServiceAppId();
            }
            return EnvConfig.isProEnv() ? mAppId : mAppIdTest;
        }

        public static String getAmigoServiceAppId() {
            return EnvConfig.isProEnv() ? AMIGO_SERVICE_APP_ID : AMIGO_SERVICE_APP_ID_TEST;
        }

        public static void setAppID(String appId, String appIdTest) {
            mAppId = appId;
            mAppIdTest = appIdTest;
        }

    }

    public static final class Warranty {
        private static final String APP_ID = "EC728CA2CD55443E9F3323A9402540E3";
        private static final String APP_ID_TEST = "3660D7AC021A475289ADA2658B2ECA5A";

        public static String getWarrantyAppID() {
            return EnvConfig.isProEnv() ? APP_ID : APP_ID_TEST;
        }
    }

    public static final class Bbs {
        private static final String APP_ID = "AC9ABF21CD604450B27DF05D921643D2";
        private static final String APP_ID_TEST = "1FD1C1449C69470FACB49DDD82821D69";

        public static String getBbsAppID() {
            return EnvConfig.isProEnv() ? APP_ID : APP_ID_TEST;
        }
    }

    public static final class CacheHelper {
        //Cache 缓存文件夹名
        public static final String CACHE_FILE_NAME = "cache_user_center";
        //缓存最大size
        public static final long CACHE_MAX_SIZE = 50 * 1024 * 1024;
        //缓存最大文件数量
        public static final int CACHE_MAX_COUNT = 100;
    }

    public static final class ImageLoader {
        //本地缓存图片最大size
        public static final int CACHE_IMAGE_DISK_MAX_SIZE = 10 * 1024 * 1024;
        //本地缓存图片最大数量
        public static final int CACHE_IMAGE_DISK_MAX_COUNT = 30;
        //内存缓存图片最大size
        public static final int CACHE_IMAGE_MEMORY_MAX_SIZE = 2 * 1024 * 1024;
        //Cache 缓存文件夹名
        public static final String CACHE_DIRECTORY_NAME = "images_user_center";
        //线程池size
        public static final int THREAD_POOL_SIZE = 3;
    }

    public static final class HttpHelper {
        //网络链接超时，秒
        public static final int CONNECT_TIMEOUT_SECOND = 10;
        //读超时，秒
        public static final int READ_TIMEOUT_SECOND = 10;
        //写超时，秒
        public static final int WRITE_TIMEOUT_SECOND = 10;
        //缓存文件名
        public static final String CACHE_FILE_NAME = "cache_http";
        // 缓存最大文件数量
        public static final int CACHE_MAX_SIZE = 1024 * 1024 * 10;

    }

}
