package com.G3.kalendar.broadcasts

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.G3.kalendar.Globals
import com.G3.kalendar.MainActivity
import com.G3.kalendar.R
import com.G3.kalendar.ViewKanbanTicket
import com.G3.kalendar.database.story.Story
import com.G3.kalendar.database.story.StoryDao
import com.G3.kalendar.database.story.StoryRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*

class TimerBroadcastReceiver : BroadcastReceiver() {
    private lateinit var myTask: MyTask

    private lateinit var storyId: String
    private lateinit var repository: StoryRepository
    private lateinit var userId: String

    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder

    companion object {
        const val COUNTDOWN = "countdown"
        const val NOTIFY_ID = 55
        var REQUEST_CODE = 19
        var counter = 0
        var countdown = 0
        var isWorkTimer = true
        lateinit var timer: Timer
    }

    override fun onReceive(context: Context, intent: Intent) {

        // Launch kalendar app
        val activityIntent = Intent(context, MainActivity::class.java)
        activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(activityIntent)

        // Set up and initialization
        counter = 0
        storyId = intent.getStringExtra(Globals.BROADCAST_STORY_ID).toString()
        countdown = intent.getIntExtra(COUNTDOWN, Globals.TWENTYFIVE_MINS_TO_SECS)

        notificationManager =
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
        userId = sharedPref.getString("id", "")!!

        val db = Firebase.firestore
        val storyDao = StoryDao(db)
        repository = StoryRepository(storyDao)

        // Remove old notification
        notificationManager.cancel(ExactAlarmBroadcastReceiver.NOTIFY_ID)

        // Build new notification
        notificationBuilder = NotificationCompat.Builder(context, Globals.CHANNEL_ID)
            .setSilent(false)
            .setOngoing(true)
            .setContentText("${getNotificationStartString()} ${convertSecondsToDisplay()} time left")
            .setSmallIcon(R.drawable.ic_menu_calendar)
            .setProgress(countdown, counter, false)
            .addAction(
                R.drawable.ic_menu_kanban,
                "Finish",
                getFinishReceiverPendingIntent(context)
            )

        val notification = notificationBuilder.build()
        notificationManager.notify(NOTIFY_ID, notification)

        myTask = MyTask(countdown, context)
        timer = Timer()
        timer.scheduleAtFixedRate(myTask, 0, 1000L)
    }

    // Calculates time left and formats for display
    private fun convertSecondsToDisplay(): String {
        val timeLeft = countdown - counter
        val minutes = timeLeft / 60
        val seconds = timeLeft - minutes * 60
        var minutesStr = "$minutes"
        var secondsStr = "$seconds"
        if (minutes < 10) {
            minutesStr = "0$minutes"
        }
        if (seconds < 10) {
            secondsStr = "0$seconds"
        }
        return "$minutesStr:$secondsStr"
    }

    private fun findTimerType() {
        if (countdown == Globals.TWENTYFIVE_MINS_TO_SECS) {
            isWorkTimer = true
        } else if (countdown == Globals.FIVE_MINS_TO_SECS) {
            isWorkTimer = false
        }
    }

    private fun sendNextBroadcastIntent(context: Context) {
        findTimerType()
        val intent: Intent = if (isWorkTimer) { // ending work phase
            Intent(context, TimerBroadcastReceiver::class.java).apply {
                putExtra(Globals.BROADCAST_STORY_ID, storyId)
                putExtra(COUNTDOWN, Globals.FIVE_MINS_TO_SECS)
            }
        } else { // ending break
            Intent(context, TimerBroadcastReceiver::class.java).apply {
                putExtra(Globals.BROADCAST_STORY_ID, storyId)
                putExtra(COUNTDOWN, Globals.TWENTYFIVE_MINS_TO_SECS)
            }
        }
        context.sendBroadcast(intent)
    }


    private fun getNotificationStartString(): String {
        findTimerType()
        return if (isWorkTimer) {
            "Time to work!"
        } else {
            "Time for a break!"
        }
    }

    private fun getIntentToUpdateStory(context: Context): Intent {
        return Intent(context, FinishBroadcastReceiver::class.java).apply {
            putExtra(Globals.BROADCAST_STORY_ID, storyId)
        }
    }

    private fun getFinishReceiverPendingIntent(context: Context): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE++,
            getIntentToUpdateStory(context),
            PendingIntent.FLAG_MUTABLE
        )
    }

    inner class MyTask(var time: Int, var context: Context) : TimerTask() {
        override fun run() {
            try {
                counter += 1
                notificationBuilder.setContentText("${getNotificationStartString()} ${convertSecondsToDisplay()} time left")
                    .setProgress(countdown, counter, false)
                val notification = notificationBuilder.build()
                notificationManager.notify(NOTIFY_ID, notification)

                // If timer finishes, cancel it and update the story
                if (counter % time == 0) {
                    context.sendBroadcast(getIntentToUpdateStory(context))
                    notificationManager.cancel(NOTIFY_ID)
                    sendNextBroadcastIntent(context)
                    timer.cancel()
                }
            } catch (t: Throwable) {
                println("debug: Timer Tick Failed. $t")
            }
        }
    }

}