package gionee.gnservice.app.model.server

import gionee.gnservice.app.constants.ServerConstants.Companion.BASE_PATH
import gionee.gnservice.app.model.entity.Data
import gionee.gnservice.app.model.entity.Login
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * @author jv.lee
 * @date 2019/8/22.
 * @description 接口地址
 */
interface ApiServer {

    @GET(BASE_PATH)
    fun login(
        @QueryMap map: HashMap<String, Any>
    ): Call<Response<Data<Login>>>

}