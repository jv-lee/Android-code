package gionee.gnservice.app.tool

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Service
import android.content.*
import android.provider.Settings
import android.telephony.TelephonyManager
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.android.droi.books.utils.Constant
import com.gionee.gnservice.statistics.StatisticsUtil
import gionee.gnservice.app.App
import gionee.gnservice.app.constants.Constants
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

        /**
         * 跳转到微信
         */
        fun getWeChatApi(context: Context) {
            try {
                val intent = Intent(Intent.ACTION_MAIN)
                val cmp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.component = cmp
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // TODO: handle exception
                Toast.makeText(context, "请安装微信", Toast.LENGTH_SHORT).show()
            }

        }

        /**
         * 复制内容到剪切板
         *
         * @param context
         * @param copyStr
         * @return
         */
        fun copy(context: Context, copyStr: String): Boolean {
            try {
                //获取剪贴板管理器
                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                // 创建普通字符型ClipData
                val mClipData = ClipData.newPlainText("Label", copyStr)
                // 将ClipData内容放到系统剪贴板里。
                cm.primaryClip = mClipData
                return true
            } catch (e: Exception) {
                return false
            }

        }

        /**
         * 获取剪切板内容
         *
         * @param context
         * @param copyStr
         * @return
         */
        fun paste(context: Context, copyStr: String): String? {
            try {
                //获取剪贴板管理器
                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                // 将ClipData内容放到系统剪贴板里。
                val primaryClip = cm.primaryClip
                val itemAt = primaryClip!!.getItemAt(0)
                return itemAt.text.toString()
            } catch (e: Exception) {
                return null
            }

        }

        /**
         * 转换推送跳转码
         */
        fun pushTransformCode(code: Int): Int {
            when (code) {
                1 -> return Constants.TAB_NOVEL
                2 -> return Constants.TAB_GAME
                3 -> return Constants.TAB_NEWS
                4 -> return Constants.TAB_WALLET
                5 -> return Constants.TAB_WALLET_TASK
            }
            return 0
        }

        /**
         * 转换活动跳转码
         */
        fun activeTransformCode(code: Int): Int {
            when (code) {
                Constants.MAGNET_VIDEO -> return Constants.TAB_VIDEO
                Constants.MAGNET_NEWS -> return Constants.TAB_NEWS
                Constants.MAGNET_NOVEL -> return Constants.TAB_NOVEL
                Constants.MAGNET_GAME -> return Constants.TAB_GAME
                Constants.MAGNET_WALLET -> return Constants.TAB_WALLET
                Constants.MAGNET_WALLET_TASK -> return Constants.TAB_WALLET_TASK
                Constants.MAGNET_WALLET_WECHAT -> return Constants.TAB_WALLET_WECHAT
            }
            return 0
        }

        /**
         * 将hclw定义的label转为有据label
         *
         * @param eventId
         * @param label
         * @return
         */
        fun convertYoujuLabel(eventId: String, label: String?): String {
            var youJuLabel = ""
            if (label != null) {
                when (eventId) {
                    "FTButton_Click" -> when (label) {
                        "0" -> youJuLabel = "我的鱼蛋"
                        "1" -> youJuLabel = "今日鱼蛋"
                        "2" -> youJuLabel = "用户签到"
                        "3" -> youJuLabel = "提现兑换"
                        "4" -> youJuLabel = "消息中心"
                        "5" -> youJuLabel = "设置"
                        else -> {
                        }
                    }
                    "User_WithdrawalsClick" -> when (label) {
                        "0" -> youJuLabel = "1元"
                        "1" -> youJuLabel = "3元"
                        "2" -> youJuLabel = "5元"
                        "3" -> youJuLabel = "10元"
                        "4" -> youJuLabel = "30元"
                        "5" -> youJuLabel = "50元"
                        "6" -> youJuLabel = "100元"
                        else -> {
                        }
                    }
                    "Message_Click" -> when (label) {
                        "0" -> youJuLabel = "通知"
                        "1" -> youJuLabel = "公告"
                        "2" -> youJuLabel = "清空"
                        else -> {
                        }
                    }
                    "Novice_Click", "Novice_Finish" -> when (label) {
                        "0" -> youJuLabel = "阅读资讯"
                        "1" -> youJuLabel = "阅读小说"
                        "2" -> youJuLabel = "查看阅读收益"
                        "3" -> youJuLabel = "查看我的钱包"
                        "4" -> youJuLabel = "天天领红包"
                        else -> {
                        }
                    }
                    "Usual_Click", "Usual_Finish" -> when (label) {
                        "0" -> youJuLabel = "阅读资讯5"
                        "1" -> youJuLabel = "阅读资讯10"
                        "2" -> youJuLabel = "阅读资讯20分钟"
                        "3" -> youJuLabel = "体验游戏10"
                        "4" -> youJuLabel = "体验游戏30分钟"
                        "5" -> youJuLabel = "玩指定游戏10分钟"
                        "6" -> youJuLabel = "阅读小说10"
                        "7" -> youJuLabel = "阅读小说30"
                        "8" -> youJuLabel = "阅读小说60"
                        "9" -> youJuLabel = "阅读小说120"
                        "10" -> youJuLabel = "观看视频任务"
                        else -> {
                        }
                    }
                    "Games_skip", "Message_Games_skip", "Game_Games_skip" -> when (label) {
                        "2" -> youJuLabel = "小鸡冲冲冲"
                        "3" -> youJuLabel = "大咖斗地主"
                        else -> {
                        }
                    }
                    "GameButton_Click" -> when (label) {
                        "0" -> youJuLabel = "转盘观看视频"
                        "1" -> youJuLabel = "我的奖品"
                        "2" -> youJuLabel = "规则"
                        "3" -> youJuLabel = "小鸡冲冲冲"
                        "4" -> youJuLabel = "大咖斗地主"
                        else -> {
                        }
                    }
                    "User_TagsClick" -> when (label) {
                        "0" -> youJuLabel = "视频"
                        "1" -> youJuLabel = "资讯"
                        "2" -> youJuLabel = "小说"
                        "3" -> youJuLabel = "钱包"
                        "4" -> youJuLabel = "游戏"
                        else -> {
                        }
                    }
                    "Points_for_Fish_Eggs_click" -> when (label) {
                        "0" -> youJuLabel = "积分兑换鱼蛋点击"
                        else -> {
                        }
                    }
                    "Time_Page" -> when (label) {
                        "0" -> youJuLabel = "用户中心"
                        "1" -> youJuLabel = "资讯"
                        "2" -> youJuLabel = "小说"
                        "3" -> youJuLabel = "钱包"
                        "4" -> youJuLabel = "游戏"
                        else -> {
                        }
                    }
                    "ActivityButton_Click" -> when (label) {
                        "0" -> youJuLabel = "按钮1"
                        "1" -> youJuLabel = "按钮2"
                        "2" -> youJuLabel = "按钮3"
                    }
                    "Books_glide", "Books_Click", "GamePrize_Click", "Exposure_Page", "Magnet_Click" -> {
                    }
                    else -> youJuLabel = label
                }
            }
            return youJuLabel
        }

    }

}