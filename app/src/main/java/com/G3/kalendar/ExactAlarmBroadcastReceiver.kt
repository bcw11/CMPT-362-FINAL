package com.G3.kalendar

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class ExactAlarmBroadcastReceiver : BroadcastReceiver() {
    private val CHANNEL_ID = "Kalendar Notifications"
    private val NOTIFY_ID = 15

    override fun onReceive(context: Context, intent: Intent) {
        val storyName = intent.getStringExtra(AlarmManagement.STORY_NAME)

        val testIntent = Intent()
        val testPendingIntent =
            PendingIntent.getBroadcast(context, 0, testIntent, PendingIntent.FLAG_IMMUTABLE)


        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSilent(false)
            .setContentText("Time for $storyName!")
            .setSmallIcon(R.drawable.ic_menu_camera)
            .addAction(R.drawable.ic_menu_gallery, "Start", testPendingIntent)
            .addAction(R.drawable.ic_menu_gallery, "Cancel", testPendingIntent)

        val notification = notificationBuilder.build()

        val notificationManager =
            context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT > 26) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "notification dot",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(NOTIFY_ID, notification)
    }
}