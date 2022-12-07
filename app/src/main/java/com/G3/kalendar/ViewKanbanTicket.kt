package com.G3.kalendar

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
import java.util.*
import java.util.ResourceBundle.getBundle

class ViewKanbanTicket : AppCompatActivity() {
    private val statusArray = arrayOf(Globals.TO_DO_STATUS,Globals.IN_PROGRESS_STATUS,Globals.DONE_STATUS)

    // current story and its epic
    private var story: Story? = null
    private var epic: Epic? = null

    // spinners
    private lateinit var epicSpinner: AutoCompleteTextView
    private lateinit var statusSpinner: AutoCompleteTextView

    // view model
    private lateinit var storyViewModel:StoryViewModel

    // edit text
    private lateinit var storyET : EditText


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ticket_menu,menu)
        println("bebug menu1")
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete){
            storyViewModel.delete(story!!)
            onBackPressed()
        }
        println("bebug menu2")
        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewticket)

        // getting story id from intent extra
        story = intent.getParcelableExtra("story_id")

        // setting story edit text
        storyET = findViewById(R.id.storyET)
        storyET.setText(story!!.name)

        // initializing filter drop down
        val arrayAdapter = ArrayAdapter(this,R.layout.filter_item,statusArray)
        statusSpinner = findViewById(R.id.statusAC)
        statusSpinner.setText(story!!.status)
        statusSpinner.setAdapter(arrayAdapter)

        // initializing due date
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = story!!.dueDate

        // initializing epic spinner
        val sharedPref = getSharedPreferences("UserInfo", MODE_PRIVATE)
        val factory = DatabaseViewModelFactory(sharedPref.getString("id", "")!!)
        var epicViewModel = ViewModelProvider(this, factory.epicViewModelFactory)[EpicViewModel::class.java]
        storyViewModel = ViewModelProvider(this, factory.storyViewModelFactory)[StoryViewModel::class.java]
        epicViewModel.epics.observe(this){
            var epicNames:Array<String> = arrayOf()
            for(e in it) {
                epicNames += e.title
                if(e.id == story!!.epicId){
                    epic = e
                }
            }
            val arrayAdapter = ArrayAdapter(this,R.layout.filter_item,epicNames)
            epicSpinner = findViewById(R.id.epicAC)
            epicSpinner.setText(epic!!.title)
            epicSpinner.setAdapter(arrayAdapter)
        }

    }


    fun onSaveClicked(view:View){
        story!!.name = storyET.text.toString()
        story!!.epicId = epic!!.id
        story!!.status = statusSpinner.text.toString()

        storyViewModel.update(story!!)

        onBackPressed()
    }

    fun onCancelClicked(view:View){
        onBackPressed()
    }
}