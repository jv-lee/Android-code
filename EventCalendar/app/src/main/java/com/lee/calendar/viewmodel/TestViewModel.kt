package com.lee.calendar.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.calendar.entity.*
import com.lee.calendar.utils.CalendarUtils

/**
 * @author jv.lee
 * @date 2020/11/3
 * @description
 */
class TestViewModel : ViewModel() {

    data class MonthObserver(val position: Int,val data:ArrayList<MonthData>)
    val monthLiveData by lazy { MutableLiveData<MonthObserver>() }

    fun getMonthData(position:Int,year: Int, month: Int) {
        Thread(Runnable {
            Thread.sleep(1000)
            val monthMaxDay = CalendarUtils.getMonthMaxDay(year, month)
            val monthArray = arrayListOf<MonthData>()
            for (index in 1..monthMaxDay) {
                when (index) {
                    1->monthArray.add(MonthData(index, DayStatus.UPDATE_STATUS))
                    2->monthArray.add(MonthData(index, DayStatus.OVER_UPDATE_STATUS))
                    3->monthArray.add(MonthData(index, DayStatus.UPDATE_STATUS))
                    4->monthArray.add(MonthData(index, DayStatus.OVER_UPDATE_STATUS))
                    5->monthArray.add(MonthData(index, DayStatus.UPDATE_STATUS))
                    6->monthArray.add(MonthData(index, DayStatus.UPDATE_STATUS))
                    7->monthArray.add(MonthData(index, DayStatus.UPDATE_STATUS))
                    8->monthArray.add(MonthData(index, DayStatus.UPDATE_STATUS))
                    9->monthArray.add(MonthData(index, DayStatus.UPDATE_STATUS))
                    10->monthArray.add(MonthData(index, DayStatus.UPDATE_STATUS))
                    11->monthArray.add(MonthData(index, DayStatus.UPDATE_STATUS))
                    12->monthArray.add(MonthData(index, DayStatus.UPDATE_STATUS))
                    13->monthArray.add(MonthData(index, DayStatus.UPDATE_STATUS))
                    14->monthArray.add(MonthData(index, DayStatus.UPDATE_STATUS))
                    15->monthArray.add(MonthData(index, DayStatus.UPDATE_STATUS))
                    16->monthArray.add(MonthData(index, DayStatus.UPDATE_STATUS))
                    17->monthArray.add(MonthData(index, DayStatus.UPDATE_STATUS))
                    18->monthArray.add(MonthData(index, DayStatus.OVER_UPDATE_STATUS))
                    19->monthArray.add(MonthData(index, DayStatus.OVER_UPDATE_STATUS))
                    20->monthArray.add(MonthData(index, DayStatus.OVER_UPDATE_STATUS))
                    21->monthArray.add(MonthData(index, DayStatus.EMPTY_STATUS))
                    22->monthArray.add(MonthData(index, DayStatus.EMPTY_STATUS))
                    23->monthArray.add(MonthData(index, DayStatus.DELAY_UPDATE_STATUS))
                    24->monthArray.add(MonthData(index, DayStatus.EMPTY_STATUS))
                    25->monthArray.add(MonthData(index, DayStatus.DELAY_UPDATE_STATUS))
                    26->monthArray.add(MonthData(index, DayStatus.EMPTY_STATUS))
                    27->monthArray.add(MonthData(index, DayStatus.DELAY_UPDATE_STATUS))
                    28->monthArray.add(MonthData(index, DayStatus.EMPTY_STATUS))
                    else -> monthArray.add(MonthData(index, DayStatus.EMPTY_STATUS))
                }
            }
            monthLiveData.postValue(MonthObserver(position,monthArray))
        }).start()

//        return CalendarData(
//            monthArray,
//            FullAttendance(RewardStatus.REWARD_STATUS, AttendanceData(10, 1000)),
//            0
//        )
    }

}