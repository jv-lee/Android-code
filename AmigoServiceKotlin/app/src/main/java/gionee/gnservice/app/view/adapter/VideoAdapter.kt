package gionee.gnservice.app.view.adapter

import android.content.Context
import com.lee.library.adapter.LeeViewAdapter
import gionee.gnservice.app.model.entity.VideoData
import gionee.gnservice.app.view.adapter.item.VideoItemIcon

/**
 * @author jv.lee
 * @date 2019/8/16.
 * @description
 */
class VideoAdapter(context: Context, data: List<VideoData>) :
    LeeViewAdapter<VideoData>(context, data) {

    init {
        addItemStyles(VideoItemIcon())
    }
}
