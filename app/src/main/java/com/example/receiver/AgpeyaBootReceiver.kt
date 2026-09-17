package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.alarm.AgpeyaAlarmScheduler

class AgpeyaBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.d("AgpeyaBootReceiver", "Received broadcast action: $action - rescheduling alarms")
        AgpeyaAlarmScheduler.rescheduleAllActiveAlarms(context)
    }
}
