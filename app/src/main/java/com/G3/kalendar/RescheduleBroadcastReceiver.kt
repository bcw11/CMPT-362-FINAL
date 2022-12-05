package com.G3.kalendar

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.Calendar

class RescheduleBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val STORY_ID = "story_id"
        const val TIME_ARRAY = "time_array"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val storyId = intent.getStringExtra(STORY_ID)!!
        val times = intent.getLongArrayExtra(TIME_ARRAY)!!
        val alarmManagement = AlarmManagement(context)
        for (time in times) {
            if (time > Calendar.getInstance().timeInMillis) {
                alarmManagement.cancelAlarm(time, storyId)
            }
        }

        val activityIntent = Intent(context, ViewKanbanTicket::class.java)
        activityIntent.putExtra(STORY_ID, storyId)
        activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(activityIntent)
    }
}