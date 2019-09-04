package gionee.gnservice.app.view.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.TextView
import com.lee.library.base.BaseActivity
import com.lee.library.livedatabus.LiveDataBus
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.EventConstants
import gionee.gnservice.app.databinding.ActivityRedPackageBinding
import gionee.gnservice.app.model.entity.RedPacketInfo
import gionee.gnservice.app.model.server.RetrofitUtils
import gionee.gnservice.app.tool.DelayTimeTool
import gionee.gnservice.app.tool.CommonTool
import gionee.gnservice.app.view.alert.DialogEx
import gionee.gnservice.app.view.widget.RedRainView
import gionee.gnservice.app.vm.RedPackageViewModel

/**
 * @author jv.lee
 * @date 2019/9/3.
 * @description 红包雨窗口
 */
class RedPackageActivity : BaseActivity<ActivityRedPackageBinding, RedPackageViewModel>(
    R.layout.activity_red_package,
    RedPackageViewModel::class.java
) {

    var dialog: AlertDialog? = null

    @SuppressLint("SetTextI18n")
    override fun bindData(savedInstanceState: Bundle?) {
        viewModel.redPackageAward.observe(this, Observer {
            dialog = if (it == null || it.count == 0) {
                //过期提示
                DialogEx(
                    this,
                    DialogEx.ViewInterface {
                        val view = LayoutInflater.from(this@RedPackageActivity).inflate(R.layout.layout_red_out, null)
                        view.findViewById<TextView>(R.id.tv_button).setOnClickListener {
                            dialog?.dismiss()
                            useClose()
                        }
                        view
                    }).build()
            } else {
                //奖励提示
                DialogEx(
                    this,
                    DialogEx.ViewInterface {
                        val view =
                            LayoutInflater.from(this@RedPackageActivity).inflate(R.layout.layout_red_reward, null)
                        view.findViewById<TextView>(R.id.tv_yudan_count).text = "+" + it.count
                        view.findViewById<TextView>(R.id.tv_money_count).text =
                            getString(R.string.red_dialog_red_money, CommonTool.commandCeil(it.count.toString()))
                        view.findViewById<TextView>(R.id.tv_next_time).text = getString(
                            R.string.red_dialog_nexttime,
                            CommonTool.nextRedPackageTime(System.currentTimeMillis() + (it.nextDT * 1000))
                        )
                        view.findViewById<TextView>(R.id.tv_button).setOnClickListener {
                            dialog?.dismiss()
                            useClose()
                        }
                        view
                    }).build()
            }

            if (it != null) {
                //初始化下次红包时间配置
                val user = RetrofitUtils.instance.getUser()
                user?.redPacketInfo = RedPacketInfo(0, 0, it.nextDT)
                RetrofitUtils.instance.saveUser(user)
            }
        })
    }

    override fun bindView() {
        binding.redRain.setCallback(object : RedRainView.Callback {
            override fun buttonCall() {
                viewModel.getRedPackageAward()
            }

            override fun closeCall() {
                useClose()
            }

        })

        DelayTimeTool.timeChange(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                binding.redRain.startAnimators()
            }
        }, 100, 0, 1)
    }

    override fun onResume() {
        super.onResume()
        binding.redRain.resumeAnimators()
    }

    override fun onPause() {
        super.onPause()
        binding.redRain.pauseAnimators()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.redRain.destroyAnimators()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    fun useClose() {
        LiveDataBus.getInstance().getChannel(EventConstants.RED_PACKAGE_ACTIVITY).postValue(0)
        finish()
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out)
    }
}
