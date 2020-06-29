package com.example.padding3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.padding3.R

class NetworkStateItemViewHolder(
    parent: ViewGroup,
    private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.lee_item_load, parent, false)
) {
    private val loadingLayout = itemView.findViewById<ConstraintLayout>(R.id.const_item_loadMore)
    private val endLayout = itemView.findViewById<ConstraintLayout>(R.id.const_item_loadEnd)
    private val retryLayout = itemView.findViewById<ConstraintLayout>(R.id.const_item_loadError)
    private val tvRetryError = itemView.findViewById<TextView>(R.id.tv_error_text)
        .also {
            it.setOnClickListener { retryCallback() }
        }

    fun bindTo(loadState: LoadState) {
        loadingLayout.isVisible = loadState is LoadState.Loading
        endLayout.isVisible = loadState is LoadState.NotLoading
        retryLayout.isVisible = loadState is LoadState.Error
    }
}