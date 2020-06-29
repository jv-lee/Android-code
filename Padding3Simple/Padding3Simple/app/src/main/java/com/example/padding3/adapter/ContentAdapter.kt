package com.example.padding3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.padding3.R
import com.example.padding3.model.entity.Content

class ContentAdapter : PagingDataAdapter<Content, ContentViewHolder>(POST_COMPARATOR) {

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Content>() {
            override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean =
                oldItem._id == newItem._id
        }
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.tvText.text = getItem(position)?.title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        return ContentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false)
        )
    }

}

class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvText: TextView = itemView.findViewById(R.id.tv_text)
}
