package com.G3.kalendar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.G3.kalendar.database.DatabaseViewModelFactory
import com.G3.kalendar.database.epic.Epic
import com.G3.kalendar.database.epic.EpicViewModel
import com.G3.kalendar.database.story.Story
import com.G3.kalendar.database.story.StoryViewModel
import com.G3.kalendar.ui.calendar.StoryView
import java.util.*

class AddEpicActivity : AppCompatActivity() {

    private lateinit var epic:Epic

    private lateinit var epicET:EditText

    private lateinit var epicViewModel:EpicViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_epic)

        epicET = findViewById(R.id.epic_name)

        val sharedPref = this.getSharedPreferences("UserInfo", MODE_PRIVATE)
        val userID = sharedPref.getString("id", "")!!
        val factory = DatabaseViewModelFactory(sharedPref.getString("id", "")!!)
        epicViewModel = ViewModelProvider(this, factory.epicViewModelFactory)[EpicViewModel::class.java]

        epic = Epic()
        epic.userId = userID
    }


    fun onSaveClicked(view: View) {
        epic.title = epicET.text.toString()
        epicViewModel.insert(epic)
        onBackPressed()
    }

    fun onCancelClicked(view: View) {
        onBackPressed()
    }
}