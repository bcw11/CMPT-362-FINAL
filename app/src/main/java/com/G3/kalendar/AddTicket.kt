package com.G3.kalendar

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.G3.kalendar.database.DatabaseViewModelFactory
import com.G3.kalendar.database.epic.Epic
import com.G3.kalendar.database.epic.EpicViewModel
import com.G3.kalendar.database.story.Story
import com.G3.kalendar.database.story.StoryViewModel
import java.util.Calendar


class AddTicket : AppCompatActivity(), DatePickerDialog.OnDateSetListener,  TimePickerDialog.OnTimeSetListener {
    private lateinit var ETstoryName: EditText
    private lateinit var TVdueDate: TextView
    private lateinit var TVStartworkTime: TextView

    private lateinit var SpinnerEpic: Spinner
    private lateinit var SpinnerStatus: Spinner
    private lateinit var SpinnerColor: Spinner

    private lateinit var SpinnerStatus_adapter: ArrayAdapter<String>
    private lateinit var SpinnerEpic_adapter: ArrayAdapter<String>

    private lateinit var possibleEpics_epic : ArrayList<Epic>
    private lateinit var possibleEpics_title : ArrayList<String>
    //private lateinit var SpinnerColor_adapter: ArrayAdapter<String>
    //private lateinit var SpinnerStatus_adapter: ArrayAdapter<String>

    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var timePickerDialog: TimePickerDialog
    private var calendar_duedate = Calendar.getInstance()
    private var calendar_startWork = Calendar.getInstance()

    private lateinit var workTimesArray : ArrayList<Calendar>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_addticket)

        ETstoryName = findViewById(R.id.ETstoryName)
        TVdueDate = findViewById(R.id.TVdueDate)
        TVStartworkTime = findViewById(R.id.TVstartworkTimes)

        // Set Up
        possibleEpics_epic = ArrayList()
        possibleEpics_title = ArrayList()
        workTimesArray = ArrayList()

        val sharedPref = this.getSharedPreferences("UserInfo", MODE_PRIVATE)
        var factory_epic = DatabaseViewModelFactory(sharedPref.getString("id", "")!!)
        var viewModel_epic = ViewModelProvider(
            this,
            factory_epic.epicViewModelFactory
        ).get(EpicViewModel::class.java)

        viewModel_epic.epics.observe(this, {
            possibleEpics_epic.clear()
            possibleEpics_title.clear()
            for (epic in it) {
                possibleEpics_epic.add(epic)
                possibleEpics_title.add(epic.title)
            }
            SpinnerEpic_adapter.notifyDataSetChanged()
        })


        SpinnerEpic = findViewById(R.id.epicSpinner)
        SpinnerEpic_adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, possibleEpics_title)
        SpinnerEpic.adapter = SpinnerEpic_adapter




        val statusSpinnerInput = resources.getStringArray(R.array.statusList)
        SpinnerStatus = findViewById(R.id.statusSpinner)
        SpinnerStatus_adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusSpinnerInput)
        SpinnerStatus.adapter = SpinnerStatus_adapter

//        val colorSpinnerInput = resources.getStringArray(R.array.statusList)
//        SpinnerColor = findViewById(R.id.colorSpinner)
//        SpinnerColor_adapter =
//            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusSpinnerInput)
//        SpinnerColor.adapter = SpinnerColor_adapter



    }

    fun onSaveClicked(view: View) {
        val entry = Story()

        val sharedPref = this.getSharedPreferences("UserInfo", MODE_PRIVATE)
        val userID = sharedPref.getString("id", "")!!
        entry.userId = userID

        val storyName = ETstoryName.text
        entry.name = storyName.toString()

        val epicTitlePos = SpinnerEpic.selectedItemPosition
        val epicTitleID = possibleEpics_epic.get(epicTitlePos);
        entry.epicId = epicTitleID.id

        entry.epicName = SpinnerEpic.selectedItem.toString()

        val status = SpinnerStatus.selectedItem.toString()
        entry.status = status

        val current_time = ArrayList<Long>()

        for (i in workTimesArray) {
            current_time.add(i.timeInMillis)
        }
        entry.calendarTimes = current_time

        entry.dueDate = calendar_duedate.timeInMillis

        val factory = DatabaseViewModelFactory(userID)
        val viewModel = ViewModelProvider(
            this,
            factory.storyViewModelFactory
        ).get(StoryViewModel::class.java)

        viewModel.insert(entry)

        val alarmManagement = AlarmManagement(this)
        viewModel.stories.observe(this) {
            for (story in it) {
                if (story.name == entry.name && story.userId == entry.userId) {
                    for (time in story.calendarTimes) {
                        alarmManagement.scheduleAlarm(time, story.id)
                    }
                }
            }
        }

        val refresh = Intent(this, MainActivity::class.java)
        startActivity(refresh)
        finish()
    }

    fun onCancelClicked(view: View) {
        this.onBackPressed()
    }

    fun setDueDate(view: View) {
        datePickerDialog = DatePickerDialog(
            this,
            { view, year, month, day ->
                calendar_duedate.set(year, month, day)
                TVdueDate.setText("Due Date: " + parseTime())
            },
            calendar_duedate.get(Calendar.YEAR),
            calendar_duedate.get(Calendar.MONTH),
            calendar_duedate.get(Calendar.DAY_OF_MONTH)

        )

        datePickerDialog.show()
    }

    fun setWorkTime(view: View) {
        datePickerDialog = DatePickerDialog(
            this,
            { view, year, month, day ->
                calendar_startWork.set(year, month, day)

                workTimesArray.add(calendar_startWork)
                var oldDateString : String = ""

                for (i in workTimesArray) {
                    oldDateString += "Work Time: " + parseDateTime(i) + "\n"
//                    println("DEBUG: " + i.timeInMillis)
                }
                TVStartworkTime.setText(oldDateString)

            },
            calendar_startWork.get(Calendar.YEAR),
            calendar_startWork.get(Calendar.MONTH),
            calendar_startWork.get(Calendar.DAY_OF_MONTH)
        )

        timePickerDialog = TimePickerDialog(
            this, {
                view, hour, minute ->
                calendar_startWork = Calendar.getInstance()
                calendar_startWork.set( Calendar.HOUR_OF_DAY, hour )
                calendar_startWork.set( Calendar.MINUTE, minute )
                datePickerDialog.show()
            },
            calendar_startWork.get(Calendar.HOUR_OF_DAY),
            calendar_startWork.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()



    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        // do nothing
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        // do nothing
    }

    fun parseTime(): String {
        val month = calendar_duedate.get(Calendar.MONTH)
        val day = calendar_duedate.get(Calendar.DAY_OF_MONTH)
        val year = calendar_duedate.get(Calendar.YEAR)

        return "${parseMonth(month + 1)} $day $year"
    }

    private fun parseMonth(value: Int): String {
        when (value) {
            1 -> return "Jan"
            2 -> return "Feb"
            3 -> return "Mar"
            4 -> return "Apr"
            5 -> return "May"
            6 -> return "June"
            7 -> return "July"
            8 -> return "Aug"
            9 -> return "Sept"
            10 -> return "Oct"
            11 -> return "Nov"
            12 -> return "Dec"
        }
        return ""
    }

    fun parseDateTime(calendar: Calendar): String {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        var hourStr = "${hour}"
        if (hour < 10) hourStr = "0${hour}"

        val minute = calendar.get(Calendar.MINUTE)
        var minuteStr = "${minute}"
        if (minute < 10) minuteStr = "0${minute}"

        val seconds = calendar.get(Calendar.SECOND)
        var secondStr = "${seconds}"
        if (seconds < 10) secondStr = "0${seconds}"

        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val year = calendar.get(Calendar.YEAR)

        return "$hourStr:$minuteStr:$secondStr ${parseMonth(month + 1)} $day $year"
    }

}
