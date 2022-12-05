package com.G3.kalendar

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.G3.kalendar.database.story.StoryDao
import com.G3.kalendar.database.story.StoryRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class RescheduleBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val STORY_ID = "story_id"
        const val TIME_ARRAY = "time_array"
        const val TIME = "time"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val storyId = intent.getStringExtra(STORY_ID)!!
        val time = intent.getLongExtra(TIME, 0)

        val alarmManagement = AlarmManagement(context)
        alarmManagement.cancelAlarm(time, storyId)

        val activityIntent = Intent(context, ViewKanbanTicket::class.java)
        activityIntent.putExtra(STORY_ID, storyId)
        activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(activityIntent)
    }
}