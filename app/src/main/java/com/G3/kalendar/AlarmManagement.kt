package com.G3.kalendar

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.G3.kalendar.broadcasts.ExactAlarmBroadcastReceiver

class AlarmManagement(val context: Context) {

    companion object {
        const val TIME = "time"
    }

    fun scheduleAlarm(time: Long, storyId: String) {
        val pendingIntent = createPendingIntent(time.toInt(), storyId)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    fun cancelAlarm(time: Long, storyId: String) {
        val pendingIntent = createPendingIntent(time.toInt(), storyId)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    private fun createPendingIntent(requestCode: Int, storyId: String): PendingIntent {
        val intent = Intent(context, ExactAlarmBroadcastReceiver::class.java).apply {
            putExtra(Globals.BROADCAST_STORY_ID, storyId)
            putExtra(TIME, requestCode)
        }
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_MUTABLE
        )
    }
}