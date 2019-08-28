package gionee.gnservice.app.model.entity

/**
 * @author jv.lee
 * @date 2019/8/28.
 * @description
 */
data class Video(val sumLimit: Int, val cid: String, val name: String, val page: Int, val list: List<VideoData>)

data class VideoData(
    val messageId: String,
    val messageType: String,
    val title: String,
    val summary: String,
    val origin: String,
    val shareLink: String,
    val video: String,
    val videoImage: String,
    val videoDuration: String,
    var publishTime: Long = 0,
    var createTime: Long = 0,
    val tags: List<String>
)