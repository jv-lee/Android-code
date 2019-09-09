package gionee.gnservice.app.model.server

import gionee.gnservice.app.constants.ServerConstants.Companion.BASE_ACT
import gionee.gnservice.app.constants.ServerConstants.Companion.BASE_PATH
import gionee.gnservice.app.model.entity.*
import gionee.gnservice.app.model.entity.base.Data
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * @author jv.lee
 * @date 2019/8/22.
 * @description 接口地址
 */
interface ApiServer {

    @GET(BASE_PATH)
    fun login(@Query(BASE_ACT) act: String, @QueryMap map: HashMap<String, Any>): Call<Data<Login>>

    @GET(BASE_PATH)
    fun config(@Query(BASE_ACT) act: String): Call<Data<Config>>

    @GET(BASE_PATH)
    fun pushAward(@Query(BASE_ACT) act: String, @QueryMap map: HashMap<String, Any>): Call<Data<Push>>

    @GET(BASE_PATH)
    fun redPoint(@Query(BASE_ACT) act: String): Call<Data<RedPoint>>

    @GET(BASE_PATH)
    fun magnetActive(@Query(BASE_ACT) act: String): Call<Data<Magnet>>

    @GET(BASE_PATH)
    fun versionInfo(@Query(BASE_ACT) act: String): Call<Data<VersionInfo>>

    @GET(BASE_PATH)
    fun tabIndex(@Query(BASE_ACT) act: String, @Query("isSub") isSub: String): Call<Data<Menu>>

    @GET(BASE_PATH)
    fun redPackageAward(@Query(BASE_ACT) act: String): Call<Data<RedPacketAward>>

    /**
     * 首次打开奖励
     * type 默认传1
     */
    @GET(BASE_PATH)
    fun noviceAward(@Query(BASE_ACT) act: String, @Query("type") type: String): Call<Data<NoviceAward>>

    /**
     * 时长上报
     * @param time    要累加的时长 单位/秒
     * @param type    类型 1资讯 2小说 3游戏 4视频
     * @param gid     当type=3时 游戏id
     */
    @GET(BASE_PATH)
    fun subAddTime(
        @Query(BASE_ACT) act: String,
        @Query("s") time: String,
        @Query("type") type: String,
        @Query("gid") gid: String?
    ): Call<Data<TaskInfo>>

    @GET(BASE_PATH)
    fun videoCategory(@Query(BASE_ACT) act: String): Call<Data<VideoCategory>>

    @GET(BASE_PATH)
    fun videoList(@Query(BASE_ACT) act: String, @QueryMap map: HashMap<String, Any>): Call<Data<Video>>

    @GET(BASE_PATH)
    fun bindWX(@Query(BASE_ACT) act: String, @Query("code") code: String): Call<Data<WXInfo>>

}