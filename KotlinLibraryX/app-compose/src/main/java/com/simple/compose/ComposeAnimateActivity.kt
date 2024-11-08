package com.simple.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.simple.compose.ui.theme.Green300
import com.simple.compose.ui.theme.Purple100
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 *
 * @author jv.lee
 * @date 2022/1/7
 */
class ComposeAnimateActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            UiScreen()
            UiTextAnimateScreen()
        }
    }

    enum class ColorState {
        PURPLE, GREEN
    }

    enum class VisibleState {
        VISIBLE, GONE
    }

    class AnimateViewModel : ViewModel() {
        private val _colorState = MutableStateFlow(VisibleState.VISIBLE.ordinal)
        val colorState: StateFlow<Int> = _colorState

        fun onColorStateChange() {
            _colorState.value = if (_colorState.value == ColorState.PURPLE.ordinal) {
                ColorState.GREEN.ordinal 
            }else ColorState.PURPLE.ordinal
        }

        private val _visibleState = MutableStateFlow(VisibleState.GONE.ordinal)
        val visibleState: StateFlow<Int> = _visibleState

        fun onVisibleStateChange() {
            _visibleState.value =
                if (_visibleState.value == VisibleState.VISIBLE.ordinal) {
                    VisibleState.GONE.ordinal 
                }else VisibleState.VISIBLE.ordinal
        }
    }

    @Composable
    fun UiScreen(viewModel: AnimateViewModel = viewModel()) {
        val colorState: Int by viewModel.colorState.collectAsState(ColorState.PURPLE.ordinal)

        // 为背景添加动画状态监听
        val backgroundColor by animateColorAsState(
            if (colorState == ColorState.PURPLE.ordinal) Purple100 else Green300, label = ""
        )

        val visibleState: Int by viewModel.visibleState.collectAsState(VisibleState.GONE.ordinal)

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
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

    @Preview(name = "UiTextAnimateScreen")
    @Composable
    fun UiTextAnimateScreen() {
        var expandedTopic by remember { mutableStateOf<String?>(null) }
        val topic = "this is title"
        TopicRow(topic = topic, expanded = expandedTopic == topic) {
            expandedTopic = if (expandedTopic == topic) null else topic
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun TopicRow(topic: String, expanded: Boolean, onClick: () -> Unit) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            elevation = 2.dp,
            onClick = onClick
        ) {
            Column(
                modifier = Modifier
                    .background(Color.Blue)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .animateContentSize()
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = topic,
                        style = MaterialTheme.typography.body1
                    )
                }
                AnimatedVisibility(visible = expanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "this content text.",
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }
}