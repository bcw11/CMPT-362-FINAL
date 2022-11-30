package com.G3.kalendar.ui.kanban

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.G3.kalendar.R
import com.G3.kalendar.databinding.FragmentKanbanBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.ArrayList

class KanbanFragment : Fragment() {
    public val label = "Kanban"

    private var _binding: FragmentKanbanBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var toDo: todoFragment
    private lateinit var inProgress: inprogressFragment
    private lateinit var done: doneFragment
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var myMyFragmentStateAdapter: MyFragmentStateAdapter
    private lateinit var fragments: ArrayList<Fragment>
    private val tabTitles = arrayOf("To Do", "In Progress", "Done") //Tab titles
    private lateinit var tabConfigurationStrategy: TabLayoutMediator.TabConfigurationStrategy
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentKanbanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewPager2 = binding.viewpager
        tabLayout = binding.tab

        toDo = todoFragment()
        inProgress = inprogressFragment()
        done = doneFragment()

        fragments = ArrayList()
        fragments.add(toDo)
        fragments.add(inProgress)
        fragments.add(done)

        myMyFragmentStateAdapter = MyFragmentStateAdapter(this, fragments)
        viewPager2.adapter = myMyFragmentStateAdapter

        tabConfigurationStrategy =
            TabLayoutMediator.TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
                tab.text = tabTitles[position]
            }
        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2, tabConfigurationStrategy)
        tabLayoutMediator.attach()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}