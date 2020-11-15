package com.lee.calendar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.lee.calendar.R
import com.lee.calendar.entity.DateEntity
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.utils.CalendarUtils
import com.lee.calendar.widget.MonthView
import java.util.*

/**
 * @author jv.lee
 * @date 2020/11/15
 * @description
 */
abstract class BaseCalendarPageAdapter2(private val data:ArrayList<DateEntity>) : RecyclerView.Adapter<BaseCalendarPageAdapter2.BaseCalendar2ViewHolder>(){

    private var mChangeDataListener:OnChangeDataListener?= null
    private var mPager:ViewPager2? = null

    private var rowIndex = 0
    private var currentSelectIndex = 0
    private var currentDay: DayEntity? = null

    fun getRowIndex() = rowIndex
    fun getData() = data

    //viewPager绑定adapter 进行初始化view渲染
    fun bindPager(pager: ViewPager2) {
        this.mPager = pager

        pager.adapter = this
        pager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                updateCurrentSelected(data[position])
                mChangeDataListener?.onPageChangeDate(position,data[position])
            }
        })
        initStartPage(data.size / 2)
    }

    //初始化搜起始页面 - 默认为当月
    private fun initStartPage(position: Int) {
        val index = getInitPosition(position)
        mPager?.setCurrentItem(index, false)
    }

    //初始化起始页下标 - 根据month / week 模式计算
    private fun getInitPosition(position: Int): Int {
        return if (monthMode() == MonthView.MonthMode.MODE_MONTH) {
            position
        } else {
            val dayEntity = data[position].dayList[0]
            val weekDiffCount = CalendarUtils.getDiffWeekCount(
                Calendar.getInstance().also {
                    it.set(Calendar.DATE, 1)
                }, Calendar.getInstance().also {
                    it.set(dayEntity.year, dayEntity.month, dayEntity.day)
                })
            position + weekDiffCount
        }
    }

    //更新当前选中行坐标 外部动画需要行坐标
    private fun updateSelectRowIndex(position: Int, entity: DayEntity) {
        if (entity.isSelected) {
            rowIndex = position / 7
        }
    }

    //更新当前选中的日期数据 - 修改成员变量
    private fun updateCurrentSelected(monthEntity: DateEntity) {
        for ((index, item) in monthEntity.dayList.withIndex()) {
            if (item.isSelected) {
                currentSelectIndex = index
                currentDay = item
                mChangeDataListener?.onDayChangeDate(index,item)
                updateSelectRowIndex(index,item)
            }
        }
    }

    //更新当权选中的下标数据 - 修改实际数据源通知更新
    private fun updateSelectPosition(entity:DayEntity,index:Int) {
        for ((position, day) in data[index].dayList.withIndex()) {
            day.isSelected = false
            if (day.day == entity.day && day.isToMonth) {
                day.isSelected = true
                currentDay = day
                currentSelectIndex = position
                updateSelectRowIndex(position,day)
            }
        }
        notifyDataSetChanged()
    }

    //双列表互相通知对方同步页面跟随
    fun synchronizeSelectItem(entity: DayEntity) {
        if (monthMode() == MonthView.MonthMode.MODE_MONTH) selectMonthItem(entity) else selectWeekItem(entity)
    }

    private fun selectMonthItem(entity: DayEntity) {
        currentDay ?: return
        mPager ?: return

        val diffMonthCount = CalendarUtils.getDiffMonthCount(
            Calendar.getInstance().also {
                it.set(entity.year, entity.month, entity.day)
            }, Calendar.getInstance().also {
                it.set(currentDay?.year!!, currentDay?.month!!, currentDay?.day!!)
            })

        val index = mPager?.currentItem!! + diffMonthCount
        mPager?.setCurrentItem(index, true)

        updateSelectPosition(entity,index)
    }

    private fun selectWeekItem(entity: DayEntity) {
        currentDay ?: return
        mPager ?: return

        val diffWeekCount = CalendarUtils.getDiffWeekCount(
            Calendar.getInstance().also {
                it.set(entity.year, entity.month, entity.day)
            }, Calendar.getInstance().also {
                it.set(currentDay?.year!!, currentDay?.month!!, currentDay?.day!!)
            })

        val index = mPager?.currentItem!! + diffWeekCount
        mPager?.setCurrentItem(index, true)

        updateSelectPosition(entity,index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseCalendar2ViewHolder {
        return BaseCalendar2ViewHolder(
            LayoutInflater.from(parent.context).inflate(getItemLayout(), parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: BaseCalendar2ViewHolder, position: Int) {
        holder.bindView(data[position])
    }

    inner class  BaseCalendar2ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bindView(entity: DateEntity) {
            itemView.findViewById<MonthView>(R.id.month_view).also {
                it.setOnDaySelectedListener(object:MonthView.OnDaySelectedListener{
                    override fun onDayClick(position: Int, entity: DayEntity) {
                        currentDay = entity
                        mChangeDataListener?.onDayChangeDate(position,entity)
                        updateSelectRowIndex(position,entity)
                    }
                })
                it.setMode(monthMode())
                it.bindData(entity.dayList)
            }

        }
    }

    fun setOnChangeDataListener(onChangeDataListener: OnChangeDataListener) {
        this.mChangeDataListener = onChangeDataListener
    }

    interface OnChangeDataListener {
        fun onPageChangeDate(position: Int, entity: DateEntity)
        fun onDayChangeDate(position: Int, entity: DayEntity)
    }

    abstract fun monthMode():Int

    abstract fun getItemLayout(): Int

}