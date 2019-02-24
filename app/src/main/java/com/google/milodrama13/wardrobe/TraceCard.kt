package com.google.milodrama13.wardrobe

import android.view.View
import android.widget.TextView
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.*

class TraceCard(view: View) {
    var trace_timestamp: TextView
    var trace_severity : TextView
    var trace_message: TextView
    var trace_id:TextView

    init {
        trace_timestamp = view.findViewById(R.id.timestamp)
        trace_message = view.findViewById(R.id.message)
        trace_severity = view.findViewById(R.id.severity)
        trace_id = view.findViewById(R.id.trace_id)
    }

    fun setTrace(trace:TraceItem){
        trace_id.text = trace.id.toString()
        trace_message.text = trace.message
        trace_severity.text = trace.level.toString()

        trace_timestamp.text =  SimpleDateFormat("EEE dd/MM/yyyy\nHH:mm:ss", Locale.FRANCE).format(Date.from(trace.timestamp))
    }

}