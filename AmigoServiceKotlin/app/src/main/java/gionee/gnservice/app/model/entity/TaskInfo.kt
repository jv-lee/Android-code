package gionee.gnservice.app.model.entity

/**
 * @author jv.lee
 * @date 2019/9/4.
 * @description
 */
data class TaskInfo(val time_sum: Int, val type: Int, val info: Info, val taskOverList: List<TaskOver>)

data class Info(val next_timeLimit: Int, val getYD: Int, val isHave: Int, val this_gtimeSum: Int, val gid: Int)

data class TaskOver(val taskType: Int, val tid: Int, val taskCount: Int, val taskName: String, val redList: Red)

data class Red(val t1: Int, val t2: Int, val t3: Int, val t4: Int, val t5: Int)