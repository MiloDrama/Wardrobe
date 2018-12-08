package com.google.milodrama13.wardrobe

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class RecyclerAdapter(private var _data:List<Cloth>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return _data.size
    }

    fun update(cloths:List<Cloth>){
        _data = cloths
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cloth = _data[position]
        holder.card.setCloth(cloth)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cloth_card, parent, false)
        return ViewHolder(view)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        internal var card:ClothCard

        init {
            card = ClothCard(itemView)
        }
    }
}