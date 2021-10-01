interface BuildType {

    companion object {
        const val DEBUG = "debug"
        const val RELEASE = "release"
    }

    val isMinifyEnabled: Boolean
    val zipAlignEnabled: Boolean
}

object BuildDebug : BuildType {
    override val isMinifyEnabled = false
    override val zipAlignEnabled = false
}

object BuildRelease : BuildType {
    override val isMinifyEnabled = true
    override val zipAlignEnabled = true
}
