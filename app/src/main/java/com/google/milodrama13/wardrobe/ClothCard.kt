package com.google.milodrama13.wardrobe

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.*

class ClothCard(view: View) {
    var cloth_picture: ImageView
    var cloth_use_count: TextView
    var cloth_last_wear : TextView
    var cloth_last_washed: TextView
    var cloth_id:TextView
    var notYetText: String
    var height: Int

    init {
        cloth_picture = view.findViewById(R.id.cloth_picture)
        cloth_use_count = view.findViewById(R.id.cloth_use_count)
        cloth_last_washed = view.findViewById(R.id.cloth_last_washed)
        cloth_last_wear = view.findViewById(R.id.cloth_last_wear)
        cloth_id = view.findViewById(R.id.cloth_id)
        notYetText = view.context.getString(R.string.not_yet)
        height = R.dimen.cloth_card_height
    }

    fun setCloth(cloth:Cloth){
        cloth_id.text = cloth.id.toString()
        cloth_use_count.text = cloth.useCount.toString()

        if (cloth.lastWashed == null)
            cloth_last_washed.text = notYetText
        else{
            val days = Duration.between(cloth.lastWashed,Instant.now()).toDays()
            cloth_last_washed.text =  SimpleDateFormat("EEE dd/MM/yyyy", Locale.FRANCE).format(Date.from(cloth.lastWashed)) + " (" + days + " days ago)"
        }
        if (cloth.lastWear == null)
            cloth_last_wear.text = notYetText
        else{
            cloth_last_wear.text =  SimpleDateFormat("EEE dd/MM/yyyy", Locale.FRANCE).format(Date.from(cloth.lastWear))
        }

        val picture = cloth.geBitmap(height)
        cloth_picture.setImageBitmap(picture)
    }

}