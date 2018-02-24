package com.tuwien.buildinginteractioninterfaces.prototype.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.tuwien.buildinginteractioninterfaces.prototype.R
import com.tuwien.buildinginteractioninterfaces.prototype.domain.model.BenchmarkModel
import com.tuwien.buildinginteractioninterfaces.prototype.domain.model.OptionsModel

class BenchmarksListActivity: AppCompatActivity() {
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_benchmarks)
        mRecyclerView = findViewById(R.id.benchmarks_recycler_view)

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true)

        // use a linear layout manager
        mLayoutManager = LinearLayoutManager(this)
        mRecyclerView.setLayoutManager(mLayoutManager)
        //Add line between each item
        mRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        startUpdate()
    }

    private fun startUpdate() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun updateBenchmarks(benchmarks :List<BenchmarkModel>){
        // specify an adapter (see also next example)
        mAdapter = BenchmarkAdapter(benchmarks.toTypedArray())
        mRecyclerView.adapter = mAdapter
    }
}