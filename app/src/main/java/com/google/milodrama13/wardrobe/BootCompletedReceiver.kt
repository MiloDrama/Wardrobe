package com.google.milodrama13.wardrobe

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootCompletedReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!=null && intent.action == "android.intent.action.BOOT_COMPLETE"){

        }
    }
}