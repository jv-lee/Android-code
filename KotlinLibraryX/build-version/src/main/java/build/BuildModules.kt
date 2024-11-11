package build

/**
 * 项目模块配置信息
 * @author jv.lee
 * @date 2021/10/1
 */
object BuildModules {
    const val APP = ":app"
    const val LIBRARY = ":library"

    fun String.name(): String {
        return this.substring(this.lastIndexOf(":") + 1, this.length)
    }

}

