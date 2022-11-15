package com.G3.kalendar.ui.calendar

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.G3.kalendar.R
import java.text.SimpleDateFormat
import java.util.*


class CalendarFragment : Fragment(R.layout.fragment_calendar) {
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
                println("debug ${c.get(Calendar.DAY_OF_MONTH) }")
                dayOfMonthTV.setTextColor(Color.BLUE)
                dayOfWeekTV.setTextColor(Color.BLUE)
            }

            // getting next day
            c.add(Calendar.DAY_OF_YEAR, 1)
        }
    }

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
    private var listviewArray:ArrayList<String> = ArrayList<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for(i in 0..23){
            val string = "$i:00"
            listviewArray.add(string)
        }

        val listview: ListView = view.findViewById(R.id.listview)
        val listviewAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireActivity(), android.R.layout
            .simple_list_item_1, listviewArray)
        listview.adapter = listviewAdapter

    }
}