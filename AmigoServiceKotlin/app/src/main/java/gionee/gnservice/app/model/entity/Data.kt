package gionee.gnservice.app.model.entity

/**
 * @author jv.lee
 * @date 2019/8/22.
 * @description 服务器数据基类
 */
data class Data<T>(
    val code: Int,
    val msg: String,
    val data: T
)