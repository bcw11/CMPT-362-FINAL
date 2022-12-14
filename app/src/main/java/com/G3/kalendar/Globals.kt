package com.G3.kalendar

object Globals {
    val TO_DO_STATUS = "To Do"
    val IN_PROGRESS_STATUS = "In Progress"
    val DONE_STATUS = "Done"

    // Table Names
    val EPIC_TABLE_NAME = "epics"
    val STORY_TABLE_NAME = "stories"
    val TASK_TABLE_NAME = "tasks"
    val USER_TABLE_NAME = "users"

    // Table Fields
    val NAME_FIELD = "name"
    val USER_ID_FIELD = "userId"
    val EPIC_ID_FIELD = "epicId"
    val EPIC_NAME_FIELD = "epicName"
    val STORY_ID_FIELD = "storyId"
    val TASK_ID_FIELD = "taskId"
    val TITLE_FIELD = "title"
    val DUE_DATE_FIELD = "dueDate"
    val STATUS_FIELD = "status"
    val CALENDAR_TIMES_FIELD = "calendarTimes"
    val EMAIL_FIELD = "email"
    val PASSWORD_FIELD = "password"
    val SALT_FIELD = "salt"
    val COLOR_FIELD = "color"
    val TIME_SPENT_FIELD = "timeSpent"

    // Timers
    val TWENTYFIVE_MINS_TO_SECS = 25 * 60
    val FIVE_MINS_TO_SECS = 5 * 60

    // Notifications
    val CHANNEL_ID = "Kalendar Notifications"
    val BROADCAST_STORY_ID = "broadcastStoryId"

}