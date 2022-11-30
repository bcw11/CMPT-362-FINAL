package com.G3.kalendar.ui.kanban


// DB imports
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.G3.kalendar.Globals
import com.G3.kalendar.R
import com.G3.kalendar.ViewKanbanTicket
import com.G3.kalendar.database.DatabaseViewModelFactory
import com.G3.kalendar.database.story.StoryViewModel


class todoFragment : Fragment() {
    private lateinit var list: ListView
    private lateinit var arrayAdapter:KanbanEntryAdapter

    private lateinit var viewModel : StoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_kanban_todo, container, false)

        list = view.findViewById(R.id.toDoList)

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
        var sharedPref = requireActivity().getSharedPreferences("UserInfo", MODE_PRIVATE)
        var factory = DatabaseViewModelFactory(sharedPref.getString("id", "")!!)
        viewModel = ViewModelProvider(
            requireActivity(),
            factory.storyViewModelFactory
        ).get(StoryViewModel::class.java)

        viewModel.getAllByStatus(Globals.TO_DO_STATUS)

        viewModel.stories.observe(requireActivity(), {
            arrayAdapter.replace(it)
            arrayAdapter.notifyDataSetChanged()
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.setOnItemClickListener() { parent: AdapterView<*>, view: View, position: Int,
                                        id: Long ->


            val intent: Intent = Intent(activity, ViewKanbanTicket::class.java)
            intent.putExtra("position", position)
            startActivity(intent)

        }

    }
}