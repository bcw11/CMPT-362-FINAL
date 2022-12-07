package com.G3.kalendar

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
    private lateinit var TVStartworkTimes: TextView

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
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_addticket)

        ETstoryName = findViewById(R.id.ETstoryName)
        TVdueDate = findViewById(R.id.TVdueDate)
        TVStartworkTimes = findViewById(R.id.TVstartworkTimes)

        // Set Up
        possibleEpics_epic = ArrayList()
        possibleEpics_title = ArrayList()
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

        val status = SpinnerStatus.selectedItem.toString()
        entry.status = status

        val current_time = arrayListOf<Long>(calendar.timeInMillis)
        entry.calendarTimes = current_time

        entry.dueDate = calendar.timeInMillis

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
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()

        TVdueDate.setText("Due Date: " + parseTime())
    }

    fun setWorkTime(view: View) {
        timePickerDialog = TimePickerDialog(
            this, this,
            calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
        )
        timePickerDialog.show()

        TVStartworkTimes.setText("Work Times: " + parseDateTime())
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        // do nothing
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        // do nothing
    }

    fun parseTime(): String {
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val year = calendar.get(Calendar.YEAR)

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

    fun parseDateTime(): String {
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
