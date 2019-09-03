package gionee.gnservice.app.model.entity

/**
 * @author jv.lee
 * @date 2019/8/30.
 * @description 磁铁类
 */
data class Magnet(val list: MagnetData)

data class MagnetData(
    val icon: String,
    val isActive: Int,
    val skip: Int,
    val skipurl: String,
    val game: Game,
    val way: Int,
    val first_login: Int,
    val name: String,
    val start: String,
    val end: String
)

data class Game(
    val id: String,
    val type: String,
    val name: String,
    val explain: String,
    val icon: String,
    val sort: String,
    val status: String,
    val linkurl: String,
    val landspace: String
)


