package com.G3.kalendar.ui.calendar

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.G3.kalendar.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // text view
        val textView: TextView = binding.textCalendar
        calendarViewModel.text.observe(viewLifecycleOwner) { textView.text = it }

        // calendar
        val calendar: Calendar = Calendar(requireActivity())
//        calendar.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
//        requireActivity().setContentView(calendar)


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}