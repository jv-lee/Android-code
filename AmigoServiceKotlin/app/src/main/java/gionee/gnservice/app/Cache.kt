package gionee.gnservice.app

import gionee.gnservice.app.model.entity.Login

/**
 * @author jv.lee
 * @date 2019/8/29.
 * @description
 */
class Cache {

    companion object {
        const val limit: Int = 20

        var isNew: Int = 0
        var isOpenTime: Int = 0
        var isOpenLogin: Int = 0
        var nextDT: Int = 0
        var ydCount: Int = 0

        /**
         * 设置红包雨参数
         */
        fun saveRedPackageParameter(login: Login) {
            isNew = login.isNew
            isOpenTime = login.redPacketInfo.isOpenTime
            isOpenLogin = login.redPacketInfo.isOpenLogin
            nextDT = login.redPacketInfo.nextDT
        }

        var isPlayVideo: Boolean = false
        var isPlayGame: Boolean = false

        var playGameId: String = ""
    }

}