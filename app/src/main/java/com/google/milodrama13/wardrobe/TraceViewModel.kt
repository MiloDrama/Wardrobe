package com.google.milodrama13.wardrobe

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import java.time.Instant

class TraceViewModel : ViewModel() {
    private var _traces:LiveData<List<TraceItem>>? = null

    fun getTraces(dao:MyDao, reference: Instant) : LiveData<List<TraceItem>>{
            _traces = dao.getTraceItems(reference)
        return _traces!!
    }

    fun getTrace(position:Int) : TraceItem{
        return _traces!!.value!![position]
    }
}