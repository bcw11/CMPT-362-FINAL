package com.G3.kalendar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.G3.kalendar.database.DatabaseViewModelFactory
import com.G3.kalendar.database.epic.Epic
import com.G3.kalendar.database.epic.EpicViewModel
import com.G3.kalendar.database.story.Story
import com.G3.kalendar.database.story.StoryViewModel
import com.G3.kalendar.ui.calendar.ChildFragment
import com.G3.kalendar.ui.calendar.StoryView
import java.text.SimpleDateFormat
import java.util.*
import java.util.ResourceBundle.getBundle
import kotlin.collections.ArrayList

class ViewKanbanTicket : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private val statusArray =
        arrayOf(Globals.TO_DO_STATUS, Globals.IN_PROGRESS_STATUS, Globals.DONE_STATUS)

    // current story and its epic
    private var story: Story? = null
    private var epic: Epic? = null

    // spinners
    private lateinit var epicSpinner: AutoCompleteTextView
    private lateinit var statusSpinner: AutoCompleteTextView

    private lateinit var dueDateTV: TextView
    private lateinit var workTimeTV: TextView
    private lateinit var newworkTimeTV: TextView

    // view model
    private lateinit var storyViewModel: StoryViewModel

    // edit text
    private lateinit var storyET: EditText

    //Calendar
    private lateinit var datePickerDialog: DatePickerDialog
    private var calendar = Calendar.getInstance()

    // new Work Times
    private lateinit var oldWorkTimesArray: List<Long>
    private lateinit var workTimesArray: ArrayList<Calendar>
    private lateinit var timePickerDialog: TimePickerDialog
    private var calendar_duedate = Calendar.getInstance()
    private var calendar_startWork = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewticket)

        // getting story id from intent extra
        story = intent.getParcelableExtra("story_id")

        // setting story edit text
        storyET = findViewById(R.id.storyET)
        storyET.setText(story!!.name)

        // initializing filter drop down
        val arrayAdapter = ArrayAdapter(this, R.layout.filter_item, statusArray)
        statusSpinner = findViewById(R.id.statusAC)
        statusSpinner.setText(story!!.status)
        statusSpinner.setAdapter(arrayAdapter)

        // initializing due date
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = story!!.dueDate

        // Due Date
        dueDateTV = findViewById(R.id.dueDateTV)
        dueDateTV.setText("Due Date: " + convert2Calendar(story!!.dueDate))

        // Work Times
        workTimesArray = ArrayList()
        workTimeTV = findViewById(R.id.workTimeTV)
        newworkTimeTV = findViewById(R.id.newworkTimeTV)
        oldWorkTimesArray = story!!.calendarTimes
        var oldDateString: String = ""
        for (i in story!!.calendarTimes) {
            oldDateString += "Work Time: " + convert2Calendar(i) + "\n"
//                    println("DEBUG: " + i.timeInMillis)
        }
        workTimeTV.setText("Old List: \n" + oldDateString)

        // initializing epic spinner
        val sharedPref = getSharedPreferences("UserInfo", MODE_PRIVATE)
        val factory = DatabaseViewModelFactory(sharedPref.getString("id", "")!!)
        var epicViewModel =
            ViewModelProvider(this, factory.epicViewModelFactory)[EpicViewModel::class.java]
        storyViewModel =
            ViewModelProvider(this, factory.storyViewModelFactory)[StoryViewModel::class.java]
        epicViewModel.epics.observe(this) {
            var epicNames: Array<String> = arrayOf()
            for (e in it) {
                epicNames += e.title
                if (e.id == story!!.epicId) {
                    epic = e
                }
            }
            val arrayAdapter = ArrayAdapter(this, R.layout.filter_item, epicNames)
            epicSpinner = findViewById(R.id.epicAC)
            epicSpinner.setText(epic!!.title)
            epicSpinner.setAdapter(arrayAdapter)
        }

    }

    fun onDeleteClicked(view: View) {
        storyViewModel.delete(story!!)
        onBackPressed()
    }

    fun onSaveClicked(view: View) {
        story!!.name = storyET.text.toString()
        story!!.epicId = epic!!.id
        story!!.status = statusSpinner.text.toString()
        story!!.dueDate = calendar.timeInMillis

        if (workTimesArray.size != 0) {
            val alarmManagement = AlarmManagement(this)
            for (time in oldWorkTimesArray) {
                alarmManagement.cancelAlarm(time, story!!.id)
            }
            val current_time = ArrayList<Long>()
            for (i in workTimesArray) {
                current_time.add(i.timeInMillis)
                alarmManagement.scheduleAlarm(i.timeInMillis, story!!.id)
            }
            story!!.calendarTimes = current_time
        }

        storyViewModel.update(story!!)

        onBackPressed()
    }

    fun onCancelClicked(view: View) {
        onBackPressed()
    }

    fun onChangeDueDateBtn(view: View) {
        datePickerDialog = DatePickerDialog(
            this,
            { view, year, month, day ->
                calendar.set(year, month, day)
                dueDateTV.setText("Due Date: " + parseTime())
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)

        )

        datePickerDialog.show()
    }

    fun onChangeWorkTimeBtn(view: View) {
        datePickerDialog = DatePickerDialog(
            this,
            { view, year, month, day ->
                calendar_startWork.set(year, month, day)

                workTimesArray.add(calendar_startWork)
                var oldDateString: String = ""

                for (i in workTimesArray) {
                    oldDateString += "Work Time: " + parseDateTime(i) + "\n"
//                    println("DEBUG: " + i.timeInMillis)
                }
                newworkTimeTV.setText(oldDateString)

            },
            calendar_startWork.get(Calendar.YEAR),
            calendar_startWork.get(Calendar.MONTH),
            calendar_startWork.get(Calendar.DAY_OF_MONTH)
        )

        timePickerDialog = TimePickerDialog(
            this, { view, hour, minute ->
                calendar_startWork = Calendar.getInstance()
                calendar_startWork.set(Calendar.HOUR_OF_DAY, hour)
                calendar_startWork.set(Calendar.MINUTE, minute)
                datePickerDialog.show()
            },
            calendar_startWork.get(Calendar.HOUR_OF_DAY),
            calendar_startWork.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
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

    private fun convert2Calendar(timeinMillis: Long): String {
        var formatter = SimpleDateFormat("dd/MM/yyyy")
        var dateString: String = formatter.format(Date(timeinMillis))
        return dateString
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        // do nothing
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        // do nothing
    }


}