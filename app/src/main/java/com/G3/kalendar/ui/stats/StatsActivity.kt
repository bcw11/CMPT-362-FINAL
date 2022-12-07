package com.G3.kalendar.ui.stats

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.G3.kalendar.database.DatabaseViewModelFactory
import com.G3.kalendar.database.epic.*
import com.G3.kalendar.database.story.StoryDao
import com.G3.kalendar.database.story.StoryRepository
import com.G3.kalendar.database.story.StoryViewModel
import com.G3.kalendar.database.story.StoryViewModelFactory
import com.G3.kalendar.databinding.ActivityStatsBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import lecho.lib.hellocharts.model.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt


class StatsActivity: AppCompatActivity() {
    public val label = "Stats"
    private var _binding: ActivityStatsBinding? = null
    private val binding get() = _binding!!


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val root: View = binding.root
        val sharedPref = this.getSharedPreferences("UserInfo", AppCompatActivity.MODE_PRIVATE)
        val userId = sharedPref.getString("id", "")
        println("Debug: $userId")



        /////Database setup/////////////////////
        val factory = DatabaseViewModelFactory(userId!!)
        val viewModelStory = ViewModelProvider(
            this,
            factory.storyViewModelFactory
        ).get(StoryViewModel::class.java)

        val viewModelEpic = ViewModelProvider(
            this,
            factory.epicViewModelFactory
        ).get(EpicViewModel::class.java)


        //iterating through the stories get the epic titles
        val epics = ArrayList<Epic>()
        val xAxisLabel: ArrayList<String> = ArrayList()
        viewModelEpic.epics.observe(this) {
            epics.clear()
            for (epic in it) {
                epics.add(epic)
                xAxisLabel.add(epic.title)
            }
        }

        viewModelStory.stories.observe(this) {

            var i = 1
            var timeSpent = 0.0f
            var totalTimeSpent = 0.0f
            //for every epic, go through each story to determine values for bar chart...
            val barEntries: ArrayList<BarEntry> = ArrayList()
            for(epic in epics){
                totalTimeSpent =0.0f
                for(story in it){
                    println("Debug: title is " + epic.title)
                    println("Debug: story.epicId is " + story.epicId)
                    if(story.epicId == epic.id){
                        timeSpent = story.timeSpent.toFloat()
                        totalTimeSpent += timeSpent
                        println("Debug: match")
                    } else {
                        println("Debug: ${story.epicId} vs ${epic.id}")
                    }
                }

                barEntries.add(BarEntry(i.toFloat(), totalTimeSpent*0.00027778f))
                i += 1
            }

            val barDataSet = BarDataSet(barEntries, "")
            barDataSet.valueTextSize=15f
            barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

            val data = BarData(barDataSet)
            _binding!!.myBarChart.data=data

            val xAxis: XAxis = _binding!!.myBarChart.xAxis
            xAxis.setLabelCount(Integer.MAX_VALUE,true)
            xAxis.valueFormatter = FixedIndexAxisValueFormatter(1f,xAxisLabel)
            //xAxis.textSize = 8f

            println("Debug: xAxisLabel is " + xAxisLabel)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            _binding!!.myBarChart.axisLeft.setDrawGridLines(true)
            xAxis.setDrawGridLines(false)
            xAxis.setDrawAxisLine(false)

            _binding!!.myBarChart.axisRight.isEnabled = false

            _binding!!.myBarChart.setViewPortOffsets(60f, 0f, 50f, 60f)
            //remove legend
            _binding!!.myBarChart.legend.isEnabled = false

            //remove description label
            _binding!!.myBarChart.description.isEnabled = false
            //disable zooming.
            _binding!!.myBarChart.setScaleEnabled(false)
            //add animation
            _binding!!.myBarChart.animateY(2500)
            //refresh the chart
            _binding!!.myBarChart.invalidate()
        }
        viewModelStory.getAllById()

    }

}

//source:https://stackoverflow.com/questions/48824793/indexaxisvalueformatter-not-working-as-expected
class FixedIndexAxisValueFormatter(private val offset: Float, private val labels: List<String>) : ValueFormatter()
{
    override fun getFormattedValue(value: Float): String {
        val x = value - offset
        val index = x.roundToInt()
        return if( index < 0 || index >= labels.size || abs(index - x) > 0.01f) ""
        else labels[index]
    }
}