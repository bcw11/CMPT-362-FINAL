package com.G3.kalendar.broadcasts

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import com.G3.kalendar.*
import com.G3.kalendar.database.story.Story
import com.G3.kalendar.database.story.StoryDao
import com.G3.kalendar.database.story.StoryRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class ExactAlarmBroadcastReceiver : BroadcastReceiver(), LifecycleOwner {
    private lateinit var lifecycleRegistry: LifecycleRegistry

    companion object {
        var RESCHEDULE_REQUEST_CODE = 2
        var TIMER_REQUEST_CODE = 4
        const val NOTIFY_ID = 15
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    override fun onReceive(context: Context, intent: Intent) {
        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.markState(Lifecycle.State.CREATED)

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

        val sharedPref = context.getSharedPreferences("UserInfo", AppCompatActivity.MODE_PRIVATE)
        val id = sharedPref.getString("id", "")!!
        val storyId = intent.getStringExtra(Globals.BROADCAST_STORY_ID).toString()
        val time = intent.getIntExtra(AlarmManagement.TIME, 0)

        val db = Firebase.firestore
        val storyDao = StoryDao(db)
        val storyRepository = StoryRepository(storyDao)

        CoroutineScope(IO).launch {
            val stories = storyRepository.getAllByUserId(id)
            for (story in stories) {
                if (story.id == storyId) {

                    val notificationBuilder = NotificationCompat.Builder(context,
                        Globals.CHANNEL_ID
                    )
                        .setSilent(false)
                        .setContentText("Time to start ${story.name}")
                        .setSmallIcon(R.drawable.ic_menu_calendar)
                        .addAction(
                            R.drawable.ic_menu_kanban,
                            "Start",
                            timerPendingIntent(context, story)
                        )
                        .addAction(
                            R.drawable.ic_menu_kanban,
                            "Reschedule",
                            reschedulePendingIntent(context, story, time)
                        )

                    val notification = notificationBuilder.build()
                    notificationManager.notify(NOTIFY_ID, notification)
                }
            }
        }
    }

    private fun timerPendingIntent(context: Context, story: Story): PendingIntent {
        val intent = Intent(context, TimerBroadcastReceiver::class.java).apply {
            putExtra(Globals.BROADCAST_STORY_ID, story.id)
            putExtra(TimerBroadcastReceiver.COUNTDOWN, Globals.TWENTYFIVE_MINS_TO_SECS)
        }
        return PendingIntent.getBroadcast(
            context,
            TIMER_REQUEST_CODE++,
            intent,
            PendingIntent.FLAG_MUTABLE
        )
    }

    private fun reschedulePendingIntent(context: Context, story: Story, time: Int): PendingIntent {
        val bundle = Bundle()
        bundle.putString(Globals.BROADCAST_STORY_ID, story.id)
        bundle.putLongArray(RescheduleBroadcastReceiver.TIME_ARRAY, generateLongArray(story))
        bundle.putLong(RescheduleBroadcastReceiver.TIME, time.toLong())
        val intent = Intent(context, RescheduleBroadcastReceiver::class.java)
        intent.putExtras(bundle)
        return PendingIntent.getBroadcast(
            context,
            RESCHEDULE_REQUEST_CODE++,
            intent,
            PendingIntent.FLAG_MUTABLE
        )
    }

    private fun generateLongArray(story: Story): LongArray {
        val timesSize = story.calendarTimes.size
        val times = LongArray(timesSize)
        for (i in 0 until timesSize) {
            times[i] = story.calendarTimes[i]
        }
        return times
    }
}