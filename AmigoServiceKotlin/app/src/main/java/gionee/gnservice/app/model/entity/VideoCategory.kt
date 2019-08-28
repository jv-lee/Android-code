package gionee.gnservice.app.model.entity

/**
 * @author jv.lee
 * @date 2019/8/28.
 * @description
 */
data class VideoCategory(val cList: List<Category>)

data class Category(val cid: String, val name: String)