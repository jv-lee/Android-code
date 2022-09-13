package configures.core

val freeCompilerArgs = mutableListOf(
    "-Xallow-jvm-ir-dependencies",
    "-Xskip-prerelease-check",
    "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
    "-Xuse-experimental=androidx.compose.animation.ExperimentalAnimationApi",
    "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi",
    "-Xopt-in=com.google.accompanist.pager.ExperimentalPagerApi",
    "-Xopt-in=kotlin.RequiresOptIn",
    "-Xjvm-default=all"
)