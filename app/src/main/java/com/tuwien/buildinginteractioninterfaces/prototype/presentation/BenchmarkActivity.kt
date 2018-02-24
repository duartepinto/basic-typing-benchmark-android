package com.tuwien.buildinginteractioninterfaces.prototype.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.tuwien.buildinginteractioninterfaces.prototype.R
import com.tuwien.buildinginteractioninterfaces.prototype.domain.model.BenchmarkModel

class BenchmarkActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_benchmark)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        val benchmark = intent.extras["BENCHMARK"] as BenchmarkModel?

        val benchmarkText = findViewById<TextView>(R.id.benchmark)


        benchmarkText.text=benchmark.toString()
    }

    override fun onSupportNavigateUp(): Boolean{
        onBackPressed()
        return true
    }
}