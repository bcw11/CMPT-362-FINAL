package com.G3.kalendar.ui.kanban

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.G3.kalendar.R
import com.G3.kalendar.database.DatabaseViewModelFactory
import com.G3.kalendar.database.epic.EpicViewModel
import com.G3.kalendar.database.story.Story
import java.text.SimpleDateFormat
import java.util.*

class KanbanEntryAdapter(private val context: Context, private var entryList : List<Story>) : BaseAdapter()  {

    override fun getCount(): Int {
        return entryList.size
    }

    override fun getItem(position: Int): Any {
        return entryList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun replace(newList: List<Story>) {
        entryList = newList
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.kanbanticket_adapter,null)

        //Get database variables for the listView display
        val storyName = entryList[position].name


        val epicName = entryList[position].epicName
        val dueDate = getDate(entryList[position].dueDate)
        //val subTaskCount = entryList[position].duration

        //display
        val TVstoryName = view.findViewById(R.id.storyName) as TextView
        val TVepicName = view.findViewById(R.id.epicName) as TextView
        val TVdueDate = view.findViewById(R.id.dueDate) as TextView
        //val TVsubTaskCount = ""

        TVstoryName.text = storyName
        TVepicName.text = epicName
        TVdueDate.text = dueDate.toString()

        return view
    }

    private fun getDate(value: Long): String {
        val date = Date(`value`)
        val df2 = SimpleDateFormat("dd/MM/yy")
        val dateText: String = df2.format(date)
        return dateText
    }
}