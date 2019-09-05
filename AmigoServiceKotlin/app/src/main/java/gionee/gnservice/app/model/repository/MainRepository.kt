package gionee.gnservice.app.model.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.lee.library.mvvm.IModel
import com.lee.library.utils.LogUtil
import gionee.gnservice.app.constants.ServerConstants
import gionee.gnservice.app.model.entity.*
import gionee.gnservice.app.model.entity.base.Data
import gionee.gnservice.app.model.server.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author jv.lee
 * @date 2019/8/30.
 * @description 主页业务数据
 */
class MainRepository : IModel {

    fun getConfig(): LiveData<Config> {
        val data = MutableLiveData<Config>()

        RetrofitUtils.instance.getApi()
            .config(ServerConstants.ACT_CONFIG)
            .enqueue(object : Callback<Data<Config>> {
                override fun onFailure(call: Call<Data<Config>>, t: Throwable) {
                    LogUtil.e("getConfig ${t.message}")
                }

                override fun onResponse(call: Call<Data<Config>>, response: Response<Data<Config>>) {
                    if (response.body()?.code == 1) {
                        data.value = response.body()?.data
                    }
                }

            })
        return data
    }

    fun getPushAward(map: HashMap<String, Any>): LiveData<Push> {
        val data = MutableLiveData<Push>()

        RetrofitUtils.instance.getApi()
            .pushAward(ServerConstants.ACT_PUSH_AWARD, map)
            .enqueue(object : Callback<Data<Push>> {
                override fun onFailure(call: Call<Data<Push>>, t: Throwable) {
                    LogUtil.e("getPushAward ${t.message}")
                }

                override fun onResponse(call: Call<Data<Push>>, response: Response<Data<Push>>) {
                    if (response.body()?.code == 1) {
                        data.value = response.body()?.data
                    }
                }
            })
        return data
    }

    fun getRedPoint(): LiveData<RedPoint> {
        val data = MutableLiveData<RedPoint>()

        RetrofitUtils.instance.getApi()
            .redPoint(ServerConstants.ACT_RED_POINT)
            .enqueue(object : Callback<Data<RedPoint>> {
                override fun onFailure(call: Call<Data<RedPoint>>, t: Throwable) {
                    LogUtil.e("getRedPoint ${t.message}")
                }

                override fun onResponse(call: Call<Data<RedPoint>>, response: Response<Data<RedPoint>>) {
                    if (response.body()?.code == 1) {
                        data.value = response.body()?.data
                    }
                }

            })
        return data
    }

    fun getMagnet(): LiveData<Magnet> {
        val data = MutableLiveData<Magnet>()

        RetrofitUtils.instance.getApi()
            .magnetActive(ServerConstants.ACT_MAGNET)
            .enqueue(object : Callback<Data<Magnet>> {
                override fun onFailure(call: Call<Data<Magnet>>, t: Throwable) {
                    LogUtil.e("getMagnet ${t.message}")
                }

                override fun onResponse(call: Call<Data<Magnet>>, response: Response<Data<Magnet>>) {
                    if (response.body()?.code == 1) {
                        data.value = response.body()?.data
                    }
                }
            })
        return data
    }

    fun noviceAward(): LiveData<NoviceAward> {
        val data = MutableLiveData<NoviceAward>()

        RetrofitUtils.instance.getApi()
            .noviceAward(ServerConstants.ACT_NOVICE_AWARD, "1")
            .enqueue(object : Callback<Data<NoviceAward>> {
                override fun onFailure(call: Call<Data<NoviceAward>>, t: Throwable) {
                    LogUtil.e("noviceAward failure :$t.message")
                    data.value = null
                }

                override fun onResponse(call: Call<Data<NoviceAward>>, response: Response<Data<NoviceAward>>) {
                    if (response.body()?.code == 1) {
                        data.value = response.body()?.data
                    }
                }

            })
        return data
    }

    fun tabIndex(isSub: String): LiveData<Menu> {
        val data = MutableLiveData<Menu>()

        RetrofitUtils.instance.getApi()
            .tabIndex(ServerConstants.ACT_TAB_INDEX, isSub)
            .enqueue(object : Callback<Data<Menu>> {
                override fun onFailure(call: Call<Data<Menu>>, t: Throwable) {
                    LogUtil.e("tabIndex failure :${t.message}")
                    data.value = null
                }

                override fun onResponse(call: Call<Data<Menu>>, response: Response<Data<Menu>>) {
                    if (response.body()?.code == 1) {
                        data.value = response.body()?.data
                    }
                }
            })

        return data
    }

}