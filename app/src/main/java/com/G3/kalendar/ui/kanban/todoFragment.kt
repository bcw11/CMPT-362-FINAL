package com.G3.kalendar.ui.kanban


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.G3.kalendar.R


// DB imports
import com.G3.kalendar.database.story.StoryViewModel
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.widget.AdapterView
import android.widget.ListView
import com.G3.kalendar.Globals
import com.G3.kalendar.database.DatabaseViewModelFactory

class todoFragment : Fragment() {
    private lateinit var list: ListView
    private lateinit var arrayAdapter:KanbanEntryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_kanban_todo, container, false)

        list = view.findViewById(R.id.toDoList)
        arrayAdapter = KanbanEntryAdapter(requireActivity(), ArrayList())
        list.adapter = arrayAdapter

        // Set Up
        val sharedPref = requireActivity().getSharedPreferences("UserInfo", MODE_PRIVATE)
        val factory = DatabaseViewModelFactory(sharedPref.getString("id", "")!!)
        val viewModel = ViewModelProvider(
            requireActivity(),
            factory.storyViewModelFactory
        ).get(StoryViewModel::class.java)

        //viewModel.getAllByStatus(Globals.TO_DO_STATUS)

        viewModel.stories.observe(requireActivity(), {
                arrayAdapter.replace(it)
                arrayAdapter.notifyDataSetChanged()
                })

        return view;
    }

    // onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}