package com.simple.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.simple.compose.source.Message
import com.simple.compose.viewmodel.ListDataViewModel

/**
 *
 * @author jv.lee
 * @date 2022/1/6
 */
class ComponseListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComponseListScreen()
        }
    }

    @Composable
    fun ComponseListScreen(viewModel: ListDataViewModel = viewModel()) {
        val messages: List<Message> by viewModel.messagesLive.observeAsState(initial = listOf())
        Surface(color = Color.White) {
            MessageList(messages = messages)
        }
    }

    @Composable
    fun MessageList(messages: List<Message>) {
        LazyColumn {
            items(messages) { item ->
                MessageItem(item)
            }
        }
    }

    @Composable
    fun MessageItem(message: Message) {
        Column(Modifier.padding(16.dp)) {
            Row {
                Text(
                    text = "Hello, ${message.author}!",
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.h6
                )
            }
            Row {
                Text(text = message.body, fontSize = 16.sp)
            }
        }
    }
}