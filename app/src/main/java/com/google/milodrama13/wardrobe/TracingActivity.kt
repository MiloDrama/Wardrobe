package com.google.milodrama13.wardrobe

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import java.time.Instant

class TracingActivity : AppCompatActivity() {

    private var _recycler: RecyclerView? = null
    private var _adapter: TraceRecyclerAdapter? = null
    private var _viewModel: TraceViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracing)

        val dao = MyDatabase.getInstance(this).getDao()
        _viewModel = ViewModelProviders.of(this).get(TraceViewModel::class.java) as TraceViewModel

        _recycler = findViewById(R.id.recycler)
        _recycler!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val observer = android.arch.lifecycle.Observer<List<TraceItem>> { list ->
            if (_recycler!!.adapter == null) {
                _adapter = TraceRecyclerAdapter(list!!)
                _recycler!!.adapter = _adapter
            } else {
                _adapter!!.update(list!!)
            }
        }
        _viewModel!!.getTraces(dao, Instant.MIN).observe(this, observer)

    }
}
