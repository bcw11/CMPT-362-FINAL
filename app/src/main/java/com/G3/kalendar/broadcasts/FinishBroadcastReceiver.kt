package com.G3.kalendar.broadcasts

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.G3.kalendar.Globals
import com.G3.kalendar.database.story.StoryDao
import com.G3.kalendar.database.story.StoryRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class FinishBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val storyId = intent.getStringExtra(Globals.BROADCAST_STORY_ID).toString()
        val timeCounter = TimerBroadcastReceiver.counter

        val sharedPref = context.getSharedPreferences("UserInfo", AppCompatActivity.MODE_PRIVATE)
        val userId = sharedPref.getString("id", "")!!

        val db = Firebase.firestore
        val storyDao = StoryDao(db)
        val repository = StoryRepository(storyDao)
        CoroutineScope(IO).launch {
            val stories = repository.getAllByUserId(userId)
            for (story in stories) {
                if (story.id == storyId) {
                    story.timeSpent += timeCounter
                    repository.update(story)
                }
            }
        }

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
        notificationManager.cancel(TimerBroadcastReceiver.NOTIFY_ID)
        TimerBroadcastReceiver.timer.cancel()
    }
}