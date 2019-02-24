package com.google.milodrama13.wardrobe

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class TraceRecyclerAdapter(private var _data:List<TraceItem>) : RecyclerView.Adapter<TraceRecyclerAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return _data.size
    }

    fun update(traces:List<TraceItem>){
        _data = traces
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trace = _data[position]
        holder.card.setTrace(trace)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_trace, parent, false)
        return ViewHolder(view)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        internal var card:TraceCard

        init {
            card = TraceCard(itemView)
        }
    }
}