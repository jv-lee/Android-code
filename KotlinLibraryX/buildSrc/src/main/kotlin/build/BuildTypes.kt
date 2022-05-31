package build

/**
 * 项目编译类型配置信息
 * @author jv.lee
 * @date 2021/10/1
 */
interface BuildTypes {

    companion object {
        const val DEBUG = "debug"
        const val RELEASE = "release"
    }

    val isMinifyEnabled: Boolean
    val zipAlignEnabled: Boolean
}

object BuildDebug : BuildTypes {
    override val isMinifyEnabled = false
    override val zipAlignEnabled = false
}

object BuildRelease : BuildTypes {
    override val isMinifyEnabled = true
    override val zipAlignEnabled = true
}
