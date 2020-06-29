package com.example.padding3

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.padding3.adapter.ContentAdapter
import com.example.padding3.adapter.PostsLoadStateAdapter
import com.example.padding3.viewmodel.PagingViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(PagingViewModel::class.java) }
    private val adapter: ContentAdapter by lazy { ContentAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_container.layoutManager = LinearLayoutManager(this)
        rv_container.adapter = adapter.withLoadStateFooter(PostsLoadStateAdapter(adapter))
        viewModel.getContent().observe(this, Observer {
            Toast.makeText(this,it.toString(),Toast.LENGTH_LONG).show()
            lifecycleScope.launchWhenCreated {
                adapter.submitData(it)
            }
        })

    }

}