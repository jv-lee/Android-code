package gionee.gnservice.app.tool

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.telephony.TelephonyManager
import android.webkit.JavascriptInterface

/**
 * @author jv.lee
 * @date 2019/8/28.
 * @description
 */
class CommonTool {

    companion object {

        /**
         * 获取IMEI
         *
         * @param context
         * @return
         */
        @SuppressLint("MissingPermission")
        @JavascriptInterface
        fun getIMEI(context: Context): String {
            try {
                val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val imei = telephonyManager.deviceId
                return imei ?: ""
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }

        /**
         * 获取设备id
         *
         * @param context
         * @return
         */
        @JavascriptInterface
        fun getEquipID(context: Context): String {
            try {
                val eqId = Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                return eqId ?: ""
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }


        /**
         * 获取视频总时长 换算分钟
         */
        fun videoTotalTime(time: Int): String {
            val builder = StringBuilder()
            if (time <= 60) {
                builder.append("00").append(":")
                if (time < 10) {
                    builder.append(0).append(time)
                } else {
                    builder.append(time)
                }
            } else {
                val fromat = if (time % 60 < 10) "0" + time % 60 else (time % 60).toString()
                if (time / 60 < 10) {
                    builder.append(0).append(time / 60).append(":").append(fromat)
                } else {
                    builder.append(time / 60).append(":").append(fromat)
                }
            }
            return builder.toString()
        }

    }

}