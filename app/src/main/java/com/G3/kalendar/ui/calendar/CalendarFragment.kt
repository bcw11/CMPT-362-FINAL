package com.G3.kalendar.ui.calendar

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.G3.kalendar.R
import java.text.SimpleDateFormat
import java.util.*


class CalendarFragment : Fragment(R.layout.fragment_calendar) {
    public val label = "Calendar"

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
                dayOfMonthTV.setTextColor(Color.BLUE)
                dayOfWeekTV.setTextColor(Color.BLUE)
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
    private lateinit var weekView:WeekView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weekView = view.findViewById(R.id.week_view)

        val weekView: WeekView = view.findViewById(R.id.week_view)
        val columnWidth = (weekView.rootView.width/8)
        val rowLength = 3*(weekView.rootView.height/24)
        println("bebug $columnWidth $rowLength")
        val rect = Rect(columnWidth,rowLength,columnWidth,rowLength)
//        weekView.addStory(rect)

        var button:Button = Button(context)
        button.layoutParams = LinearLayout.LayoutParams(
            columnWidth.toInt(),
            rowLength.toInt())
        button.text = "YOYOYO"
        button.setOnClickListener{println("bebug YOYOYO")}
        button.x = columnWidth.toFloat()
        button.y = rowLength.toFloat()

        weekView.addView(button)

        button = Button(context)
        button.layoutParams = LinearLayout.LayoutParams(
            columnWidth.toInt(),
            rowLength.toInt())
        button.text = "BABABA"
        button.setOnClickListener{println("bebug BABABA")}
        button.x = 2*columnWidth.toFloat()
        button.y = rowLength.toFloat()
//        button2.setBackgroundColor(Color.CYAN)
        weekView.addView(button)
    }


}