package com.lee.calendar.entity

import androidx.annotation.IntDef
import com.lee.calendar.widget.DayView

/**
 * @author jv.lee
 * @date 2020/11/3
 * @description
 */
data class CalendarData(
    val months: ArrayList<DateData>, //月列表
    val full_attendance: FullAttendance, //全勤相关信息
    val reward_issue: Int //全勤奖励发放，非0时进行弹窗提醒(单位：分)
)

data class DateData(
    val day: Int,
    @DayStatus val status: Int, //0已更新/1断更/2无数据/3定时更新
    @DayView.DayBackgroundStatus var backgroundStatus: Int = DayView.DayBackgroundStatus.STATUS_GONE
)

data class FullAttendance(
    @RewardStatus val status: Int,//0正常/1书籍已签约但签约类型无全勤奖/2书籍未签约
    val data: AttendanceData? = null
)

data class AttendanceData(
    val completed_days: Int, //已完成天数
    val completed_word_count: Int //已完成字数
)

@IntDef(RewardStatus.REWARD_STATUS, RewardStatus.NOT_REWARD_STATUS, RewardStatus.NOT_SIGN_STATUS)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class RewardStatus {
    companion object {
        const val REWARD_STATUS = 0 //0正常
        const val NOT_REWARD_STATUS = 1 //1书籍已签约但签约类型无全勤奖
        const val NOT_SIGN_STATUS = 2 //2书籍未签约
    }
}