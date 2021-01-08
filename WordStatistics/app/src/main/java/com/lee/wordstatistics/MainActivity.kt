package com.lee.wordstatistics

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val wordCounter = WordCounter(applicationContext)
        val count = wordCounter.getWordCount("A B C D E F G")
        Toast.makeText(this, "count:$count", Toast.LENGTH_SHORT).show()

        val limitContent = wordCounter.getWordLimitContent("A B C D E F G H ", 3)
        Toast.makeText(this, "limitContent:$limitContent", Toast.LENGTH_SHORT).show()
    }
}