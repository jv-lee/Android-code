package com.lee.wordstatistics

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val edit by lazy { findViewById<EditText>(R.id.edit) }
    private val tvLimit by lazy { findViewById<TextView>(R.id.tv_limit) }
    private val wordCounter by lazy { WordCounter(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initEditCounter()
    }

    private fun initEditCounter() {
        edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val count = wordCounter.getWordCount(s.toString())
                if (count > 10) {
                    val content = wordCounter.getWordLimitContent(s.toString(), 10)
                    edit.setText(content)
                    edit.setSelection(content.length)
                } else {
                    tvLimit.text = "limit:$count"
                }


            }

        })
    }

    private fun testWordCounter() {
        val wordCounter = WordCounter(applicationContext)
        val count = wordCounter.getWordCount("A B C D E F G")
        Toast.makeText(this, "count:$count", Toast.LENGTH_SHORT).show()

        val limitContent = wordCounter.getWordLimitContent("A B C D E F G H ", 3)
        Toast.makeText(this, "limitContent:$limitContent", Toast.LENGTH_SHORT).show()
    }

}