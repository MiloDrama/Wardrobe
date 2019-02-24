package com.google.milodrama13.wardrobe

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import java.time.Instant

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var dao = MyDatabase.getInstance(context!!).getDao();
        dao.trace(TraceItem(Instant.now(), TracingLevel.Verbose, "Alarm received"))

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
        }
        if (calendar.isWeekend)
            MainActivity.checkDirtyClothes(context!!)
        else
            MainActivity.notifyClothChoice(context!!)

    }
}