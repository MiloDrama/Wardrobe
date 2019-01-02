package com.google.milodrama13.wardrobe

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
        }
        if (calendar.isWeekend)
            MainActivity.checkDirtyClothes(context!!)
        else
            MainActivity.notifyClothChoice(context!!)

    }
}