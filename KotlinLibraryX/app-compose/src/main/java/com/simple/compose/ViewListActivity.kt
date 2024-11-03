package com.simple.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.lee.library.extensions.binding
import com.simple.compose.databinding.ActivityMainBinding
import com.simple.compose.databinding.ItemMessageBinding
import com.simple.compose.source.Message
import com.simple.compose.viewmodel.ListDataViewModel
import kotlinx.coroutines.launch

/**
 *
 * @author jv.lee
 * @date 2022/1/6
 */
class ViewListActivity : ComponentActivity() {

    private val binding by binding { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<ListDataViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.messagesFlow.collect {
                    binding.rvContainer.adapter = MessagesAdapter(it)
                }
            }
        }
    }

    private class MessagesAdapter(private val messages: List<Message>) :
        RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                ItemMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindView(messages[position])
        }

        override fun getItemCount() = messages.size

        private inner class ViewHolder(private val binding: ItemMessageBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bindView(message: Message) {
                binding.tvTitle.text = message.author
                binding.tvBody.text = message.body
            }
        }
    }
}