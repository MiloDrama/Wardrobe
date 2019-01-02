package com.google.milodrama13.wardrobe

import android.support.v4.view.LayoutInflaterCompat
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class GridAdapter(private var _data:List<Cloth>) : BaseAdapter() {

    fun update(cloths:List<Cloth>){
        _data = cloths
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Any {
        return _data[position]
    }

    override fun getItemId(position: Int): Long {
        return _data[position].id.toLong()
    }

    override fun getCount(): Int {
        return _data.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val cloth = _data[position]
        var layoutView = convertView
        if (layoutView == null){
            layoutView = LayoutInflater.from(parent!!.context).inflate(R.layout.grid_item_cloth, parent, false)!!
        }

        val imageView = layoutView.findViewById<ImageView>(R.id.cloth_picture)
        val textView = layoutView.findViewById<TextView>(R.id.cloth_id)

        textView.text = cloth.id.toString()
        val bitmap = cloth.geBitmap(R.dimen.grid_item_height)
        imageView.setImageBitmap(bitmap)
        when(cloth.useCount){
            0 -> layoutView.setBackgroundColor(parent!!.context.getColor(R.color.colorGreen))
            1 -> layoutView.setBackgroundColor(parent!!.context.getColor(R.color.colorOrange))
            else  -> layoutView.setBackgroundColor(parent!!.context.getColor(R.color.colorRed))
        }
        return layoutView
    }
}