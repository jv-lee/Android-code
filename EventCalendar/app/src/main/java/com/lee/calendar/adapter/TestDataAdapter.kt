package com.lee.calendar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lee.calendar.R

/**
 * @author jv.lee
 * @date 2020/11/10
 * @description
 */
class TestDataAdapter(val data:ArrayList<String>) :RecyclerView.Adapter<TestDataAdapter.TestDataViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestDataViewHolder {
        return TestDataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_data,null,false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: TestDataViewHolder, position: Int) {
        holder.bindView(position,data[position])
    }

    class TestDataViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindView(position: Int, data: String) {
            itemView.findViewById<TextView>(R.id.tv_text).text = "this is position - $position"
        }
    }


}

