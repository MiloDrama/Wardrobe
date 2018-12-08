package com.google.milodrama13.wardrobe

import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.*

class ClothCard(view: View) {
    var cloth_type: ImageView
    var cloth_picture: ImageView
    var cloth_use_count: TextView
    var cloth_last_washed: TextView
    var cloth_id:TextView

    init {
        cloth_type = view.findViewById(R.id.cloth_type)
        cloth_picture = view.findViewById(R.id.cloth_picture)
        cloth_use_count = view.findViewById(R.id.cloth_use_count)
        cloth_last_washed = view.findViewById(R.id.cloth_last_washed)
        cloth_id = view.findViewById(R.id.cloth_id)
    }

    fun setCloth(cloth:Cloth){
        cloth_id.text = cloth.id.toString()
        cloth_use_count.text = cloth.useCount.toString()

        if (cloth.lastWashed == null)
            cloth_last_washed.text = "Never"
        else{
            val days = Duration.between(cloth.lastWashed,Instant.now()).toDays()
            cloth_last_washed.text =  SimpleDateFormat("EEE dd/MM/yyyy").format(Date.from(cloth.lastWashed)) + " (" + days + " days ago)"
        }

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(cloth.pictureFilePath, options)
        //val scale = options.outHeight/96
        //val scale = Math.min(options.outWidth/cloth_picture.measuredWidth, options.outHeight/cloth_picture.measuredHeight)
        //val scale = Math.min(4*options.outWidth/(cloth_picture.parent.parent as View).width, options.outHeight/(cloth_picture.parent.parent as View).height)
        var scale = 1
        val halfHeight = options.outHeight/2
        while (halfHeight/scale >= 96)
            scale *= 2
        options.inJustDecodeBounds = false
        options.inSampleSize = scale
        val picture = BitmapFactory.decodeFile(cloth.pictureFilePath, options)
        cloth_picture.setImageBitmap(picture)

        when(cloth.type) {
            ClothType.SUMMER_TOP -> cloth_type.setImageResource(R.drawable.ic_short_sleeved_shirt)
            ClothType.WINTER_TOP -> cloth_type.setImageResource(R.drawable.ic_shirt)
            ClothType.PANTS -> cloth_type.setImageResource(R.drawable.ic_pants)
        }
    }
}