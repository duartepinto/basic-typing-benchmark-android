package com.tuwien.buildinginteractioninterfaces.typingbenchmark.presentation

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.R
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.local.room.RoomDatabase
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.BenchmarkModel

class BenchmarksListActivity: AppCompatActivity() {
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: RecyclerView.Adapter<BenchmarkAdapter.ViewHolder>
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
        AsyncTask.execute(object: Runnable {
            override fun run() {
                val list = RoomDatabase.instance.getDatabase(applicationContext).benchmarkDao().all
                updateBenchmarks(list)
            }
        })
    }

    fun updateBenchmarks(benchmarks :List<BenchmarkModel>){

        runOnUiThread(object: Runnable {
            override fun run() {
                mAdapter = BenchmarkAdapter(benchmarks.asReversed().toTypedArray())
                mRecyclerView.adapter = mAdapter
            }

        })
    }
}