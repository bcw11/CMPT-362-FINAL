package com.G3.kalendar

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class AlarmManagement(val context: Context) {

    companion object {
        const val STORY_NAME = "story_name"
    }

    fun scheduleAlarm(time: Long, requestCode: Int, storyName: String) {
        val pendingIntent = createPendingIntent(requestCode, storyName)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    fun cancelAlarm(requestCode: Int, storyName: String) {
        val pendingIntent = createPendingIntent(requestCode, storyName)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    private fun createPendingIntent(requestCode: Int, storyName: String): PendingIntent {
        val intent = Intent(context, ExactAlarmBroadcastReceiver::class.java).apply {
            putExtra(STORY_NAME, storyName)
        }
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}