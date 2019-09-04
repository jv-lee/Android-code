package gionee.gnservice.app.tool

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.provider.Settings
import android.telephony.TelephonyManager
import android.webkit.JavascriptInterface
import com.gionee.gnservice.statistics.StatisticsUtil
import gionee.gnservice.app.App
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author jv.lee
 * @date 2019/8/28.
 * @description 项目基础通用 工具方法
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

        /**
         * 向上取整 精确到小数，保留后一位
         *
         * @param value
         * @return
         */
        fun commandCeil(value: String): String {
            val newValue = java.lang.Double.valueOf(value) / 10000
            val newValue2 = BigDecimal(newValue).setScale(1, BigDecimal.ROUND_UP).toDouble()
            return newValue2.toString()
        }

        /**
         * 红包雨下次时间换算
         */
        fun nextRedPackageTime(time: Long): String {
            @SuppressLint("SimpleDateFormat") val hh = SimpleDateFormat("HH")
            return hh.format(Date(time))
        }

        /**
         * 换算秒
         *
         * @param time
         * @return
         */
        fun second(time: Int): Int {
            return time * 1000
        }

        /**
         * 换算分钟
         *
         * @param time
         * @return
         */
        fun minute(time: Int): Int {
            return time * 60000
        }

        fun event(time: Long) {
            if (time > second(1) && time < second(30)) {
                StatisticsUtil.onEvent(App.instance, "Warm_Start", "0 - 30 秒")
            } else if (time > second(30) && time <= minute(1)) {
                StatisticsUtil.onEvent(App.instance, "Warm_Start", "30 秒 - 1 分钟")
            } else if (time > minute(1) && time <= minute(2)) {
                StatisticsUtil.onEvent(App.instance, "Warm_Start", "1 分钟 - 2 分钟")
            } else if (time > minute(2) && time <= minute(3)) {
                StatisticsUtil.onEvent(App.instance, "Warm_Start", "2 分钟 - 3分钟")
            } else if (time > minute(3) && time <= minute(5)) {
                StatisticsUtil.onEvent(App.instance, "Warm_Start", "3 分钟 - 5 分钟")
            } else if (time > minute(5) && time <= minute(10)) {
                StatisticsUtil.onEvent(App.instance, "Warm_Start", "5 分钟 - 10 分钟")
            } else if (time > minute(10) && time <= minute(30)) {
                StatisticsUtil.onEvent(App.instance, "Warm_Start", "10 分钟 - 30 分钟")
            } else if (time > minute(30)) {
                StatisticsUtil.onEvent(App.instance, "Warm_Start", "30 分钟以上")
            }
        }

        fun getTime(HOUR_OF_DAY: Int, MINUTE: Int): Long {
            //系统当前时间
            val systemTime = System.currentTimeMillis()

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.timeZone = TimeZone.getTimeZone("GMT+8")
            //设置时间
            calendar.set(Calendar.HOUR_OF_DAY, HOUR_OF_DAY)
            calendar.set(Calendar.MINUTE, MINUTE)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            // 选择的定时时间
            var selectTime = calendar.timeInMillis
            // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
            if (systemTime > selectTime) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
                selectTime = calendar.timeInMillis
            }
            return selectTime
        }

        /**
         * 检测当前主要服务是否为运行状态
         *
         * @param context
         * @return
         */
        fun thisServiceHasRun(context: Context, serviceName: String): Boolean {
            val manager = context.getSystemService(Service.ACTIVITY_SERVICE) as ActivityManager
            //如果当前服务处于运行状态 就不再启动服务
            for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
                if (service.service.className.contains(serviceName)) {
                    return true
                }
            }
            return false
        }

    }

}