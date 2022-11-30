package com.G3.kalendar.ui.kanban

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.G3.kalendar.Globals
import com.G3.kalendar.R
import com.G3.kalendar.database.DatabaseViewModelFactory
import com.G3.kalendar.database.story.StoryViewModel

class inprogressFragment : Fragment() {
    private lateinit var list: ListView
    private lateinit var arrayAdapter:KanbanEntryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_kanban_inprogress, container, false)


        list = view.findViewById(R.id.inProgressList)

        loadDBEntries()


        return view
    }

    override fun onResume() {
        super.onResume()
        loadDBEntries()
        parentFragmentManager.beginTransaction().detach(this).attach(this).commit()
    }



    private fun loadDBEntries(){

        arrayAdapter = KanbanEntryAdapter(requireActivity(), ArrayList())
        list.adapter = arrayAdapter

        // Set Up
        var sharedPref = requireActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        var factory = DatabaseViewModelFactory(sharedPref.getString("id", "")!!)
        var viewModel = ViewModelProvider(
            requireActivity(),
            factory.storyViewModelFactory
        ).get(StoryViewModel::class.java)

        viewModel.getAllByStatus(Globals.IN_PROGRESS_STATUS)

        viewModel.stories.observe(requireActivity(), {
            arrayAdapter.replace(it)
            arrayAdapter.notifyDataSetChanged()
        })
    }
}