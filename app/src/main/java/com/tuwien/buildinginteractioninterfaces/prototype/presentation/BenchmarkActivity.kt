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

        val benchmark = intent.extras["BENCHMARK"] as BenchmarkModel?

//        val benchmark = BenchmarkModel((OptionsModel(OptionsModel.TypeGame.NO_END,true, true, OptionsModel.Source.TWELVE_DICTS)))

        val benchmarkText = findViewById<TextView>(R.id.benchmark)

        benchmarkText.text=benchmark.toString()
    }
}