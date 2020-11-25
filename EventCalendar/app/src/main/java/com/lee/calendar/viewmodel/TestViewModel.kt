package com.lee.calendar.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.calendar.entity.DateData
import com.lee.calendar.widget.calendar.entity.DayStatus
import com.lee.calendar.utils.AttendanceDataUtils
import com.lee.calendar.widget.calendar.utils.CalendarUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @author jv.lee
 * @date 2020/11/3
 * @description
 */
class TestViewModel : ViewModel() {

    data class DateObserver(val position: Int, val data: List<DateData>)

    val monthLiveData by lazy { MutableLiveData<DateObserver>() }
    val weekLiveData by lazy { MutableLiveData<DateObserver>() }

    fun getMonthData(position: Int, year: Int, month: Int) {
        val disposable = Observable.create<ArrayList<DateData>> {
            it.onNext(createMonthData(year, month))
        }.map {
            AttendanceDataUtils.setBackgroundStatus(it,year,month)
        }.map {
            DateObserver(position, it)
        }.delay(500,TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                monthLiveData.value = it
            }
    }

    fun getWeekData(position: Int, year: Int, month: Int,day:Int) {
        val disposable = Observable.create<ArrayList<DateData>> {
            it.onNext(createWeekData(year, month,day))
        }.map {
            AttendanceDataUtils.setBackgroundStatus(it,year,month)
        }.map {
            DateObserver(position, it)
        }.delay(500,TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                weekLiveData.value = it
            }
    }

    private fun createWeekData(year:Int,month:Int,day:Int):ArrayList<DateData>{
        val monthArray = arrayListOf<DateData>()
        for (index in 1..7) {
            when (index) {
                1-> monthArray.add(DateData(index,
                    DayStatus.UPDATE_STATUS))
                2-> monthArray.add(DateData(index,
                    DayStatus.OVER_UPDATE_STATUS))
                3-> monthArray.add(DateData(index,
                    DayStatus.UPDATE_STATUS))
                4-> monthArray.add(DateData(index,
                    DayStatus.OVER_UPDATE_STATUS))
                5-> monthArray.add(DateData(index,
                    DayStatus.UPDATE_STATUS))
                6-> monthArray.add(DateData(index,
                    DayStatus.OVER_UPDATE_STATUS))
                7-> monthArray.add(DateData(index,
                    DayStatus.OVER_UPDATE_STATUS))
            }
        }
        return monthArray
    }

    private fun createMonthData(year: Int, month: Int): ArrayList<DateData> {
        val monthMaxDay = CalendarUtils.getMaxDayCountByMonth(year, month)
        val monthArray = arrayListOf<DateData>()
        for (index in 1..monthMaxDay) {
            when (index) {
                1 -> monthArray.add(DateData(index, DayStatus.UPDATE_STATUS))
                2 -> monthArray.add(DateData(index, DayStatus.OVER_UPDATE_STATUS))
                3 -> monthArray.add(DateData(index, DayStatus.UPDATE_STATUS))
                4 -> monthArray.add(DateData(index, DayStatus.OVER_UPDATE_STATUS))
                5 -> monthArray.add(DateData(index, DayStatus.UPDATE_STATUS))
                6 -> monthArray.add(DateData(index, DayStatus.OVER_UPDATE_STATUS))
                7 -> monthArray.add(DateData(index, DayStatus.OVER_UPDATE_STATUS))
                8 -> monthArray.add(DateData(index, DayStatus.UPDATE_STATUS))
                9 -> monthArray.add(DateData(index, DayStatus.UPDATE_STATUS))
                10 -> monthArray.add(DateData(index, DayStatus.UPDATE_STATUS))
                11 -> monthArray.add(DateData(index, DayStatus.UPDATE_STATUS))
                12 -> monthArray.add(DateData(index, DayStatus.UPDATE_STATUS))
                13 -> monthArray.add(DateData(index, DayStatus.UPDATE_STATUS))
                14 -> monthArray.add(DateData(index, DayStatus.UPDATE_STATUS))
                15 -> monthArray.add(DateData(index, DayStatus.UPDATE_STATUS))
                16 -> monthArray.add(DateData(index, DayStatus.UPDATE_STATUS))
                17 -> monthArray.add(DateData(index, DayStatus.UPDATE_STATUS))
                18 -> monthArray.add(DateData(index, DayStatus.OVER_UPDATE_STATUS))
                19 -> monthArray.add(DateData(index, DayStatus.OVER_UPDATE_STATUS))
                20 -> monthArray.add(DateData(index, DayStatus.OVER_UPDATE_STATUS))
                21 -> monthArray.add(DateData(index, DayStatus.EMPTY_STATUS))
                22 -> monthArray.add(DateData(index, DayStatus.EMPTY_STATUS))
                23 -> monthArray.add(DateData(index, DayStatus.DELAY_UPDATE_STATUS))
                24 -> monthArray.add(DateData(index, DayStatus.EMPTY_STATUS))
                25 -> monthArray.add(DateData(index, DayStatus.DELAY_UPDATE_STATUS))
                26 -> monthArray.add(DateData(index, DayStatus.EMPTY_STATUS))
                27 -> monthArray.add(DateData(index, DayStatus.DELAY_UPDATE_STATUS))
                28 -> monthArray.add(DateData(index, DayStatus.EMPTY_STATUS))
                else -> monthArray.add(DateData(index, DayStatus.EMPTY_STATUS))
            }
        }
        return monthArray
    }

}