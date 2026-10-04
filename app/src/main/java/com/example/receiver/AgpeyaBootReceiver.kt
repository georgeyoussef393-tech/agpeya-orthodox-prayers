package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.service.AgpeyaTimezoneService

class AgpeyaBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.d("AgpeyaBootReceiver", "Received broadcast action: $action - detecting timezone and adjusting Agpeya alarms")
        
        when (action) {
            Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_DATE_CHANGED -> {
                AgpeyaTimezoneService.detectAndAdjustTimezone(context, isSystemBroadcast = true, forceReschedule = true)
            }
            else -> {
                AgpeyaTimezoneService.detectAndAdjustTimezone(context, isSystemBroadcast = false, forceReschedule = true)
            }
        }
    }
}
