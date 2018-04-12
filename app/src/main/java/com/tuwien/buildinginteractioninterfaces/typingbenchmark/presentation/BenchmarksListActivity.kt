package com.tuwien.buildinginteractioninterfaces.typingbenchmark.presentation

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonArray
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.R
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.local.room.RoomDatabase
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.BenchmarkModel

class BenchmarksListActivity: AppCompatActivity() {
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: RecyclerView.Adapter<BenchmarkAdapter.ViewHolder>
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    lateinit var benchmarks: List<BenchmarkModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_benchmarks)
        supportActionBar!!.title = "Benchmark History"
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

        this.benchmarks = benchmarks
        runOnUiThread(object: Runnable {
            override fun run() {
                mAdapter = BenchmarkAdapter(benchmarks.asReversed().toTypedArray())
                mRecyclerView.adapter = mAdapter
            }

        })
    }

    @SuppressLint("ShowToast")
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_copy-> {

            val array= JsonArray()
            for(benchmark in benchmarks){
                array.add(benchmark.toJson())
            }

            val str = array.toString()

            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("benchmark history", str)
            clipboard.primaryClip = clip

            Toast.makeText(this,getString(R.string.all_benchmarks_to_clipboard),Toast.LENGTH_SHORT).show()
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