package com.lee.calendar.widget.calendar.entity

import androidx.annotation.IntDef
import com.lee.calendar.widget.calendar.MonthView

/**
 * @author jv.lee
 * @date 2020/10/26
 * @description
 */
data class DateEntity(
    val year: Int,
    val month: Int,
    val dayList: ArrayList<DayEntity>,
    val startIndex: Int
) {
    override fun toString(): String {
        return "{${year}-${month}-${dayList}}\n\n"
    }
}

data class DayEntity(
    val isToMonth: Boolean = true, //是否为当月数据
    var isSelected: Boolean = false, //是否为选中状态
    @DayStatus var dayStatus: Int = DayStatus.EMPTY_STATUS,
    @MonthView.DayBackgroundStatus var backgroundStatus:Int = MonthView.DayBackgroundStatus.STATUS_GONE,
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

@IntDef(
    DayStatus.EMPTY_STATUS,
    DayStatus.UPDATE_STATUS,
    DayStatus.OVER_UPDATE_STATUS,
    DayStatus.DELAY_UPDATE_STATUS
)
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