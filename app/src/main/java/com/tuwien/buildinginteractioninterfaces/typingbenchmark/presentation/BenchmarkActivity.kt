package com.tuwien.buildinginteractioninterfaces.typingbenchmark.presentation

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.R
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.BenchmarkModel
import kotlinx.android.synthetic.main.activity_benchmark.*

class BenchmarkActivity: AppCompatActivity() {
    lateinit var benchmark:BenchmarkModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_benchmark)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        benchmark = intent.extras["BENCHMARK"] as BenchmarkModel
        supportActionBar!!.title = benchmark.uid.toString()

        val benchmarkText = findViewById<TextView>(R.id.benchmark)


        benchmarkText.text=benchmark.toString()
    }

    override fun onSupportNavigateUp(): Boolean{
        onBackPressed()
        return true
    }

    @SuppressLint("ShowToast")
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_copy-> {

            val benchmarkText = findViewById<TextView>(R.id.benchmark)
            val text = benchmarkText.text
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("benchmark uid: " + benchmark.uid, text)
            clipboard.primaryClip = clip

            Toast.makeText(this,R.string.benchmark_to_clipboard,Toast.LENGTH_SHORT).show()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.benchmark_menu, menu)
        return true
    }
}