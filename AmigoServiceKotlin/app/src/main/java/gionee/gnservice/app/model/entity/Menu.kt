package gionee.gnservice.app.model.entity

/**
 * @author jv.lee
 * @date 2019/9/5.
 * @description
 */
data class Menu(val menu: MenuInfo)

data class MenuInfo(val index: Int, val info: List<String>)