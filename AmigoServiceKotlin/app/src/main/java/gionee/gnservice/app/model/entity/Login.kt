package gionee.gnservice.app.model.entity

/**
 * @author jv.lee
 * @date 2019/8/22.
 * @description 登陆用户信息
 */
data class Login(
    val isNew: Int,
    val isShowAct: Int,
    val redPacketInfo: RedPacketInfo,
    val sessionKey: String,
    val userInfo: UserInfo
)

data class UserInfo(
    val avatar: String,
    val gender: String,
    val id: String,
    val jl_uid: String,
    val nick: String,
    val phone: String,
    val register_ts: String,
    val status: String,
    val wx_openid: String,
    val wx_unionid: String
)

data class RedPacketInfo(
    val isOpenLogin: Int,
    val isOpenTime: Int,
    val nextDT: Int
)