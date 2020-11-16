package com.lee.calendar.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.calendar.entity.DateData
import com.lee.calendar.entity.DayStatus
import com.lee.calendar.utils.CalendarUtils
import com.lee.calendar.widget.MonthView
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

    data class DateObserver(val position: Int, val data: ArrayList<DateData>)

    val monthLiveData by lazy { MutableLiveData<DateObserver>() }
    val weekLiveData by lazy { MutableLiveData<DateObserver>() }

    fun getMonthData(position: Int, year: Int, month: Int) {
        val disposable = Observable.create<ArrayList<DateData>> {
            it.onNext(createMonthData(year, month))
        }.map {
            setBackgroundStatus(it,year,month)
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
            setBackgroundStatus(it,year,month)
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
                1-> monthArray.add(DateData(index,DayStatus.UPDATE_STATUS))
                2-> monthArray.add(DateData(index,DayStatus.OVER_UPDATE_STATUS))
                3-> monthArray.add(DateData(index,DayStatus.UPDATE_STATUS))
                4-> monthArray.add(DateData(index,DayStatus.OVER_UPDATE_STATUS))
                5-> monthArray.add(DateData(index,DayStatus.UPDATE_STATUS))
                6-> monthArray.add(DateData(index,DayStatus.OVER_UPDATE_STATUS))
                7-> monthArray.add(DateData(index,DayStatus.OVER_UPDATE_STATUS))
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

    private fun setBackgroundStatus(monthData: ArrayList<DateData>, year: Int, month: Int): ArrayList<DateData> {
        for ((index, item) in monthData.withIndex()) {
            paresBackgroundStatus(index,item,monthData,year,month)
        }
        return monthData
    }

    private fun paresBackgroundStatus(
        position: Int,
        dayEntity: DateData,
        arrayList: ArrayList<DateData>,
        year:Int,
        month:Int
    ) {
        val isDrawGone = isDrawGone(dayEntity)
        if (isDrawGone) {
            dayEntity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_GONE
            return
        }

        val isDrawSingle = isDrawSingle(position, dayEntity, arrayList)
        if (isDrawSingle) {
            dayEntity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_SINGLE
            return
        }

        isDrawLocation(position, dayEntity, arrayList,year,month)
    }

    private fun isDrawGone(entity: DateData): Boolean {
        return entity.status != DayStatus.UPDATE_STATUS && entity.status != DayStatus.OVER_UPDATE_STATUS
    }

    private fun isDrawSingle(
        position: Int,
        entity: DateData,
        arrayList: ArrayList<DateData>
    ): Boolean {
        val prevEntity: DateData? = if (position == 0) null else arrayList[position - 1]
        val nextEntity: DateData? =
            if (position == arrayList.size - 1) null else arrayList[position + 1]
        if (prevEntity == null) {
            return entity.status != nextEntity?.status
        }
        if (nextEntity == null) {
            return entity.status != prevEntity.status
        }
        return entity.status != prevEntity.status && entity.status != nextEntity.status
    }

    private fun isDrawLocation(position: Int, entity: DateData, arrayList: ArrayList<DateData>, year:Int, month: Int) {
        val prevEntity: DateData? = if (position == 0) null else arrayList[position - 1]
        val nextEntity: DateData? =
            if (position == arrayList.size - 1) null else arrayList[position + 1]

        //当月第一天
        if (prevEntity == null && entity.status == nextEntity?.status) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_START
            return
        }

        //当月最后一天
        if (nextEntity == null && entity.status == prevEntity?.status) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_END
            return
        }

        if (entity.status != prevEntity?.status && entity.status == nextEntity?.status) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_START
        }

        if (entity.status == prevEntity?.status && entity.status == nextEntity?.status) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_CENTER
        }

        if (entity.status == prevEntity?.status && entity.status != nextEntity?.status) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_END
        }

        //周天 / 周六切割
        val week = CalendarUtils.getDayOfWeek(year, month, entity.day)
        if (week == 1 && entity.status == nextEntity?.status) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_START
        }
        if (week == 1 && entity.status != nextEntity?.status) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_SINGLE
        }
        if (week == 7 && entity.status == prevEntity?.status) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_END
        }
        if (week == 7 && entity.status != prevEntity?.status) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_SINGLE
        }
    }

}