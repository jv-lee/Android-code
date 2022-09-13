package configures.core

val freeCompilerArgs = mutableListOf(
    "-Xskip-prerelease-check",
    "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi",
    "-Xopt-in=com.google.accompanist.pager.ExperimentalPagerApi",
    "-Xopt-in=kotlin.RequiresOptIn",
    "-Xjvm-default=all"
)