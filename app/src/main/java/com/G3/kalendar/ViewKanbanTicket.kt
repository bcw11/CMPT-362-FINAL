package com.G3.kalendar

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.G3.kalendar.database.DatabaseViewModelFactory
import com.G3.kalendar.database.story.StoryViewModel

class ViewKanbanTicket : AppCompatActivity() {

    private lateinit var TVstoryName : TextView
    private lateinit var TVepicName : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewticket)

        TVstoryName = findViewById(R.id.TVstoryName)
        TVepicName = findViewById(R.id.TVepicName)

        // Set Up
        var sharedPref = this.getSharedPreferences("UserInfo", MODE_PRIVATE)
        var factory = DatabaseViewModelFactory(sharedPref.getString("id", "")!!)
        var viewModel = ViewModelProvider(
            this,
            factory.storyViewModelFactory
        ).get(StoryViewModel::class.java)


    }

    fun onCancelClicked(view: View){
        this.onBackPressed()
    }
}