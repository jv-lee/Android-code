package gionee.gnservice.app.model.entity

/**
 * @author jv.lee
 * @date 2019/9/6.
 * @description
 */
data class WXInfo(
    val wx_openid: String,
    val wx_unionid: String,
    val gender: String,
    val nick: String,
    val avatar: String
)