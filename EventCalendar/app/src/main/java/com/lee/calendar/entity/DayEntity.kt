package com.lee.calendar.entity

import androidx.annotation.IntDef

/**
 * @author jv.lee
 * @date 2020/10/26
 * @description
 */
data class DayEntity(
    val isToMonth: Boolean = true,
    val isSelected: Boolean = false,
    @DayStatus val dayStatus: Int = DayStatus.DEFAULT_STATUS,
    val year: Int,
    val month: Int,
    val day: Int
)

@IntDef(DayStatus.DEFAULT_STATUS, DayStatus.UPDATE_STATUS, DayStatus.OVER_UPDATE_STATUS)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class DayStatus {
    companion object {
        const val DEFAULT_STATUS = 0x000
        const val UPDATE_STATUS = 0x001
        const val OVER_UPDATE_STATUS = 0x002
    }
}