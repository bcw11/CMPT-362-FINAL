package com.G3.kalendar.ui.calendar

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalendarViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply { value = "This is calendar Fragment" }
    val text: LiveData<String> = _text

//     calendar
//    val calendar: Calendar = Calendar(binding)


//        requireActivity().setContentView(calendar)

}