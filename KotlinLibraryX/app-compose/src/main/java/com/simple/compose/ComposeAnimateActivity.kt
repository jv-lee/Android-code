package com.simple.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.simple.compose.ui.theme.Green300
import com.simple.compose.ui.theme.Purple100

/**
 * @author jv.lee
 * @date 2022/1/7
 * @description
 */
class ComposeAnimateActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UiScreen()
        }
    }

    enum class ColorState {
        PURPLE, GREEN
    }

    enum class VisibleState {
        VISIBLE, GONE
    }

    class AnimateViewModel : ViewModel() {
        private val _colorState = MutableLiveData(VisibleState.VISIBLE.ordinal)
        val colorState: LiveData<Int> = _colorState

        fun onColorStateChange() {
            _colorState.value = if (_colorState.value == ColorState.PURPLE.ordinal)
                ColorState.GREEN.ordinal else ColorState.PURPLE.ordinal
        }

        private val _visibleState = MutableLiveData(VisibleState.GONE.ordinal)
        val visibleState: LiveData<Int> = _visibleState

        fun onVisibleStateChange() {
            _visibleState.value =
                if (_visibleState.value == VisibleState.VISIBLE.ordinal)
                    VisibleState.GONE.ordinal else VisibleState.VISIBLE.ordinal
        }

    }

    @Composable
    fun UiScreen(viewModel: AnimateViewModel = viewModel()) {
        val colorState: Int by viewModel.colorState.observeAsState(ColorState.PURPLE.ordinal)

        // 为背景添加动画状态监听
        val backgroundColor by animateColorAsState(if (colorState == ColorState.PURPLE.ordinal) Purple100 else Green300)

        val visibleState: Int by viewModel.visibleState.observeAsState(VisibleState.GONE.ordinal)

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {
            Column(
                modifier = Modifier.background(backgroundColor),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    viewModel.onColorStateChange()
                }) {
                    Text(text = "updateBackground")
                }
                FloatingActionButton(
                    onClick = { viewModel.onVisibleStateChange() },
                    Modifier.wrapContentWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            Modifier.padding(16.dp)
                        )

                        // 为可见性添加基础动画效果
                        AnimatedVisibility(visible = visibleState == VisibleState.VISIBLE.ordinal) {
                            Text("Edit", Modifier.padding(end = 16.dp))
                        }
                    }
                }
            }
        }

        // 为可见性添加自定义动画效果
        AnimatedVisibility(
            visible = visibleState == VisibleState.VISIBLE.ordinal,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> -fullHeight },
                animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> -fullHeight },
                animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
            )
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colors.secondary,
                elevation = 4.dp
            ) {
                Text(
                    text = "Edit feature is not supported",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

}