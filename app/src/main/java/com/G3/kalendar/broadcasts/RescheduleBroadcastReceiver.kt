package com.G3.kalendar.broadcasts

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.G3.kalendar.AlarmManagement
import com.G3.kalendar.Globals
import com.G3.kalendar.ViewKanbanTicket

class RescheduleBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val TIME_ARRAY = "time_array"
        const val TIME = "time"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val storyId = intent.getStringExtra(Globals.BROADCAST_STORY_ID).toString()
        val time = intent.getLongExtra(TIME, 0)

        val notificationManager =
            context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT > 26) {
            val notificationChannel = NotificationChannel(
                Globals.CHANNEL_ID,
                "notification dot",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.cancel(ExactAlarmBroadcastReceiver.NOTIFY_ID)

        val alarmManagement = AlarmManagement(context)
        alarmManagement.cancelAlarm(time, storyId)

        val activityIntent = Intent(context, ViewKanbanTicket::class.java)
        activityIntent.putExtra(Globals.BROADCAST_STORY_ID, storyId)
        activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(activityIntent)
    }
}