package gionee.gnservice.app.model.server

import gionee.gnservice.app.constants.ServerConstants.Companion.BASE_ACT
import gionee.gnservice.app.constants.ServerConstants.Companion.BASE_PATH
import gionee.gnservice.app.model.entity.Data
import gionee.gnservice.app.model.entity.Login
import gionee.gnservice.app.model.entity.Video
import gionee.gnservice.app.model.entity.VideoCategory
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
    fun videoCategory(@Query(BASE_ACT) act: String): Call<Data<VideoCategory>>

    @GET(BASE_PATH)
    fun videoList(@Query(BASE_ACT) act: String, @QueryMap map: HashMap<String, Any>): Call<Data<Video>>

}