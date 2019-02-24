package com.google.milodrama13.wardrobe

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.time.Instant

class BootCompletedReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!=null && intent.action == "android.intent.action.BOOT_COMPLETE"){
            var dao = MyDatabase.getInstance(context!!).getDao();
            dao.trace(TraceItem(Instant.now(), TracingLevel.Verbose, "BootCompleted"))

            MainActivity.createNotificationChannel(context!!)
            MainActivity.createAlarms(context!!)
        }
    }
}