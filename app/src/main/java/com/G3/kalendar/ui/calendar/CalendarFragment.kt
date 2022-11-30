package com.G3.kalendar.ui.calendar

import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.G3.kalendar.Globals
import com.G3.kalendar.R
import com.G3.kalendar.database.DatabaseViewModelFactory
import com.G3.kalendar.database.epic.Epic
import com.G3.kalendar.database.epic.EpicViewModel
import com.G3.kalendar.database.story.Story
import com.G3.kalendar.database.story.StoryViewModel
import java.text.SimpleDateFormat
import java.util.*


class CalendarFragment : Fragment(R.layout.fragment_calendar) {
    val label = "Calendar"

    private val dayofWeek = arrayOf(R.id.sunday_text,R.id.monday_text,R.id.tuesday_text, R.id.wednesday_text,
                                    R.id.thursday_text,R.id.friday_text,R.id.saturday_text)
    private val dayOfMonth = arrayOf(R.id.sunday_num,R.id.monday_num,R.id.tuesday_num, R.id.wednesday_num,
                                        R.id.thursday_num,R.id.friday_num,R.id.saturday_num)

    private lateinit var monthText: TextView
    private var calendar: Calendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        insertNestedFragment()

        // setting month
        setMonthText(view)

        // setting days of month
        setDaysOfMonth(view)

        // getting epic database
        val sharedPref = requireActivity().getSharedPreferences("UserInfo", MODE_PRIVATE)
        val factory = DatabaseViewModelFactory(sharedPref.getString("id", "")!!)
        val viewModel = ViewModelProvider(requireActivity(), factory.epicViewModelFactory)[EpicViewModel::class.java]
        val epics = viewModel.epics.value

        // getting epic names
        var epicNames:Array<String> = arrayOf()
        if (epics != null) {
            for(epic in epics)
                epicNames += epic.title
        }

        // initializing filter drop down
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.filter_item,epicNames)
        val autoCompleteTV:AutoCompleteTextView = view.findViewById(R.id.auto_compelete_TV)
        autoCompleteTV.setAdapter(arrayAdapter)
        autoCompleteTV.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            if(epics != null){
                ChildFragment().populateStories(epics[i])
            }
        }
    }

    private fun setDaysOfMonth(view: View) {
        val c = Calendar.getInstance()

        // setting day to first of the week
        val monthDay = c.get(Calendar.DAY_OF_WEEK_IN_MONTH) - 1
        c.add(Calendar.DAY_OF_YEAR, -monthDay)
        calendar.get(Calendar.DAY_OF_MONTH)

        // updating day of month text views
        for(i in dayOfMonth.indices){
            val dayOfMonthTV: TextView = view.findViewById(dayOfMonth[i])
            val dayOfWeekTV: TextView = view.findViewById(dayofWeek[i])
            val dayOfMonth = c.get(Calendar.DAY_OF_MONTH)
            dayOfMonthTV.text = "$dayOfMonth"

            // current day's colour to BLUE
            if(c.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)){
                dayOfMonthTV.setTextColor(Color.RED)
                dayOfMonthTV.setTypeface(null, Typeface.BOLD)
                dayOfWeekTV.setTextColor(Color.RED)
                dayOfWeekTV.setTypeface(null, Typeface.BOLD)
            }

            // getting next day
            c.add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    // getting correct month and setting it in view
    private fun setMonthText(view: View){
        monthText = view.findViewById(R.id.month_text)
        val month = SimpleDateFormat("MMMM").format(calendar.time)
        monthText.text = "$month"
    }

    // adapted from https://guides.codepath.com/android/Creating-and-Using-Fragments
    // Embeds the child fragment dynamically
    private fun insertNestedFragment() {
        val childFragment: Fragment = ChildFragment()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.child_fragment_container, childFragment).commit()
    }

}

// Child fragment that will be dynamically embedded in the parent
class ChildFragment : Fragment(R.layout.fragment_calendar_child) {

    // week view
    private lateinit var weekView:WeekView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initializing week view
        weekView = view.findViewById(R.id.week_view)

        populateStories(null)
    }


    fun populateStories(epic: Epic?){
        // getting user's story database
        val sharedPref = requireActivity().getSharedPreferences("UserInfo", MODE_PRIVATE)
        val factory = DatabaseViewModelFactory(sharedPref.getString("id", "")!!)
        val viewModel = ViewModelProvider(requireActivity(), factory.storyViewModelFactory)[StoryViewModel::class.java]

        val c = Calendar.getInstance().timeInMillis
        val g:List<Long> = listOf(c)
        val story = Story("","","","CMPT 413",0L, Globals.TO_DO_STATUS,g,0)
        val stories = listOf(story)
        weekView.populateStories(stories)

//        if(epic != null)
//            viewModel.getAllByEpicId(epic.id)
//        else
//            weekView.populateStories(viewModel.stories.value)
//
//        viewModel.stories.observe(requireActivity()){
//            weekView.populateStories(it)
//        }
    }
}