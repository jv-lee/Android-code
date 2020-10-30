package com.lee.calendar.entity

import androidx.annotation.IntDef

/**
 * @author jv.lee
 * @date 2020/10/26
 * @description
 */
data class DayEntity(
    val isToMonth: Boolean = true, //是否为当月数据
    var isSelected: Boolean = false, //是否为选中状态
    @DayStatus var dayStatus: Int = DayStatus.DEFAULT_STATUS,
    val year: Int,
    val month: Int,
    val day: Int,
    val startIndex:Int
)

@IntDef(DayStatus.DEFAULT_STATUS, DayStatus.UPDATE_STATUS, DayStatus.OVER_UPDATE_STATUS,DayStatus.TIMING_UPDATE_STATUS)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class DayStatus {
    companion object {
        const val DEFAULT_STATUS = 0x000 //默认状态
        const val UPDATE_STATUS = 0x001 //已更新状态
        const val OVER_UPDATE_STATUS = 0x002 //断更状态
        const val TIMING_UPDATE_STATUS = 0x003 //定时更新状态
    }
}