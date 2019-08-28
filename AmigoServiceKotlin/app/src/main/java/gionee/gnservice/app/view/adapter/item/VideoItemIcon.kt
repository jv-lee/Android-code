package gionee.gnservice.app.view.adapter.item

import android.widget.ImageView
import android.widget.TextView
import com.lee.library.adapter.LeeViewHolder
import com.lee.library.adapter.listener.LeeViewItem
import gionee.gnservice.app.R
import gionee.gnservice.app.model.entity.VideoData
import gionee.gnservice.app.tool.CommonTool
import gionee.gnservice.app.tool.GlideTool

/**
 * @author jv.lee
 * @date 2019/8/16.
 * @description
 */
class VideoItemIcon : LeeViewItem<VideoData> {

    override fun getItemLayout(): Int {
        return R.layout.item_video_icon
    }

    override fun openClick(): Boolean {
        return true
    }

    override fun openShake(): Boolean {
        return true
    }

    override fun isItemView(entity: VideoData?, position: Int): Boolean {
        return entity != null
    }

    override fun convert(holder: LeeViewHolder?, entity: VideoData?, position: Int) {
        val ivIcon = holder?.getView(R.id.iv_icon) as ImageView
        val tvTitle = holder.getView(R.id.tv_title) as TextView
        val tvAuthor = holder.getView(R.id.tv_author) as TextView
        val tvTime = holder.getView(R.id.tv_time) as TextView

        GlideTool.loadImage(entity?.videoImage!!, ivIcon)
        tvTitle.text = entity.title
        tvTime.text = CommonTool.videoTotalTime(Integer.valueOf(entity.videoDuration))
        tvAuthor.text = entity.origin
    }

}
