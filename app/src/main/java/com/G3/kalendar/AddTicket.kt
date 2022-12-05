package com.G3.kalendar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.G3.kalendar.database.DatabaseViewModelFactory
import com.G3.kalendar.database.story.Story
import com.G3.kalendar.database.story.StoryViewModel
import java.util.Calendar


class AddTicket : AppCompatActivity() {
    private lateinit var ETstoryName: EditText
    private lateinit var ETepicName: EditText

    private lateinit var SpinnerStatus: Spinner
    private lateinit var SpinnerStatus_adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_addticket)

        ETstoryName = findViewById(R.id.ETstoryName)
        ETepicName = findViewById(R.id.ETepicName)

        val spinnerInput = resources.getStringArray(R.array.statusList)
        SpinnerStatus = findViewById(R.id.statusSpinner)
        SpinnerStatus_adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerInput)
        SpinnerStatus.adapter = SpinnerStatus_adapter


    }

    fun onSaveClicked(view: View) {
        val entry = Story()

        val sharedPref = this.getSharedPreferences("UserInfo", MODE_PRIVATE)
        val userID = sharedPref.getString("id", "")!!
        entry.userId = userID

        val storyName = ETstoryName.text
        entry.name = storyName.toString()

        val epicId = ETepicName.text
        entry.epicId = epicId.toString()

        val status = SpinnerStatus.selectedItem.toString()
        entry.status = status

        val current_time = arrayListOf<Long>(Calendar.getInstance().timeInMillis + 7000)
        //current_time.add(Calendar.getInstance().timeInMillis + 7000 * 3)
        entry.calendarTimes = current_time

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

}
