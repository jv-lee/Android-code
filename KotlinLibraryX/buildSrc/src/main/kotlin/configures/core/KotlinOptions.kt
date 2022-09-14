package configures.core

val freeCompilerArgs = mutableListOf(
    "-Xskip-prerelease-check",
    "-Xjvm-default=all",
    "-opt-in=kotlin.RequiresOptIn",
    "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
    "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
    "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
    "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
    "-opt-in=androidx.paging.ExperimentalPagingApi",
    "-opt-in=com.google.accompanist.pager.ExperimentalPagerApi",
    "-opt-in=coil.annotation.ExperimentalCoilApi",
)