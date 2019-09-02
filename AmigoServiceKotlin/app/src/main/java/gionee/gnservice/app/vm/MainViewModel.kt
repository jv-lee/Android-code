package gionee.gnservice.app.vm

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import com.gionee.gnservice.module.setting.push.PushIntentUtils
import com.gionee.gnservice.module.setting.push.PushReceiver
import com.gionee.gnservice.statistics.StatisticsUtil
import com.lee.library.mvvm.BaseViewModel
import com.lee.library.utils.SPUtil
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.constants.StatisticsConstants
import gionee.gnservice.app.model.entity.Magnet
import gionee.gnservice.app.model.entity.Push
import gionee.gnservice.app.model.entity.RedPoint
import gionee.gnservice.app.model.repository.MainRepository

/**
 * @author jv.lee
 * @date 2019/8/30.
 * @description
 */
class MainViewModel(application: Application) : BaseViewModel(application) {

    val model by lazy { MainRepository() }
    val type by lazy { MutableLiveData<Int>() }
    val push by lazy { MutableLiveData<Push>() }
    val redPoint by lazy { MutableLiveData<RedPoint>() }
    val magnet by lazy { MutableLiveData<Magnet>() }

    fun getConfig() {
        model.getConfig().observeForever {
            SPUtil.save("web_url", it?.data?.value?.user_web_url)
        }
    }

    fun initEvent(intent: Intent) {
        val notification = intent.getIntExtra(Constants.NOTIFICATION_LABLE, 0)
        if (notification == 1) {
            StatisticsUtil.onEvent(getApplication(), StatisticsConstants.Client_Notification, "点击")
        }

        var lable = "桌面"
        if (intent.getStringExtra("k1") != null) {
            lable = "推送"
        } else if (intent.data?.getQueryParameter("ch") != null) {
            lable = intent.data?.getQueryParameter("ch")!!
        }
        StatisticsUtil.onEvent(getApplication(), StatisticsConstants.Cold_Start, lable)
    }

    fun initPush(intent: Intent) {
        val k1 = intent.getStringExtra("k1")
        val vtype = intent.getIntExtra("vtype", 0)

        if (vtype != 0) {//通过用户中心推送消息打开
            getPushAward(intent)
            type.value = vtype
        } else if (k1 != null) {//通过系统推送打开
            getPushAward(intent)
            type.value = PushIntentUtils.getEntity(k1).data.vtype
        }
        PushReceiver.startSaveServer(getApplication(), intent.getStringExtra(PushReceiver.REGISTERATION_KEY_MESSAGE_ID))
    }

    private fun getPushAward(intent: Intent) {
        val map = HashMap<String, Any>()
        map.put("mid", intent.getStringExtra(PushReceiver.REGISTERATION_KEY_MESSAGE_ID))
        map.put("push_ts", intent.getStringExtra(PushReceiver.REGISTERATION_KEY_CREATE_TIME))
        map.put("title", intent.getStringExtra(PushReceiver.REGISTERATION_KEY_TITLE))
        model.getPushAward(map).observeForever {
            push.value = it
        }
    }

    fun initRedPoint() {
        model.getRedPoint().observeForever {
            redPoint.value = it
        }
    }

    fun initMagnetActive() {
        model.getMagnet().observeForever {
            magnet.value = it
        }
    }

}