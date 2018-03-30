package com.tuwien.buildinginteractioninterfaces.typingbenchmark.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.R
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.BenchmarkModel

class BenchmarkActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_benchmark)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val benchmark = intent.extras["BENCHMARK"] as BenchmarkModel
        supportActionBar!!.title = benchmark.uid.toString()

        val benchmarkText = findViewById<TextView>(R.id.benchmark)


        benchmarkText.text=benchmark.toString()
    }

    override fun onSupportNavigateUp(): Boolean{
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_copy-> {

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