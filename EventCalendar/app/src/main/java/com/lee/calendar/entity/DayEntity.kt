package com.lee.calendar.entity

import androidx.annotation.IntDef
import com.lee.calendar.widget.DayView

/**
 * @author jv.lee
 * @date 2020/10/26
 * @description
 */
data class DayEntity(
    val isToMonth: Boolean = true, //是否为当月数据
    var isSelected: Boolean = false, //是否为选中状态
    @DayStatus var dayStatus: Int = DayStatus.EMPTY_STATUS,
    @DayView.DayBackgroundStatus var backgroundStatus:Int = DayView.DayBackgroundStatus.STATUS_GONE,
    val year: Int,
    val month: Int,
    val day: Int,
    val startIndex:Int,
    val isToDay:Boolean = false
){

    override fun toString(): String {
        return "{${year}-${month}-${day}}"
    }
}

@IntDef(DayStatus.EMPTY_STATUS, DayStatus.UPDATE_STATUS, DayStatus.OVER_UPDATE_STATUS,DayStatus.DELAY_UPDATE_STATUS)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class DayStatus {
    companion object {
        const val UPDATE_STATUS = 0 //0已更新
        const val OVER_UPDATE_STATUS = 1 //1断更
        const val EMPTY_STATUS = 2 //2无数据
        const val DELAY_UPDATE_STATUS = 3 //3定时更新
    }
}