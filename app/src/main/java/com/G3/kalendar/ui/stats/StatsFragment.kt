package com.G3.kalendar.ui.stats

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.G3.kalendar.databinding.FragmentStatsBinding
import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter
import lecho.lib.hellocharts.model.*
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.*


class StatsFragment: Fragment() {
    public val label = "Stats"
    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStatsBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val c = Calendar.getInstance()
        val yearInt = c.get(Calendar.YEAR)
        val monthInt = c.get(Calendar.MONTH)
        val dayInt = c.get(Calendar.DAY_OF_MONTH)
        val monthDate = SimpleDateFormat("MMM")
        val monthName: String = monthDate.format(c.getTime())
        val yearMonthObject: YearMonth = YearMonth.of(yearInt, monthInt)
        val daysInMonth: Int = yearMonthObject.lengthOfMonth()

        _binding!!.title.text = monthName
        val formatter = SimpleAxisValueFormatter()
        formatter.decimalDigitsNumber = 1
        var xAxis: Axis = Axis.generateAxisFromRange(0.0f, daysInMonth.toFloat(),1.0f)
        var yAxis: Axis = Axis.generateAxisFromRange(0.0f,24.0f,1.0f)
        xAxis.maxLabelChars=1
        xAxis.name="Days"
        xAxis.formatter=formatter
        yAxis.name="Hours"
        yAxis.formatter=formatter

        var values: MutableList<PointValue> = ArrayList()
        values.add(PointValue(0F, 2F))
        values.add(PointValue(1F, 4F))
        values.add(PointValue(2F, 5F))
        values.add(PointValue(3F, 4F))
        values.add(PointValue(4F, 8F))
        values.add(PointValue(5F, 15F))
        values.add(PointValue(6F, 10F))
        values.add(PointValue(7F, 16F))
        values.add(PointValue(8F, 12F))
        values.add(PointValue(9F, 24F))
        values.add(PointValue(10F, 13F))

        val line: Line = Line(values).setColor(Color.BLUE).setCubic(false)
        line.isFilled=false
        val lines: MutableList<Line> = ArrayList<Line>()
        lines.add(line)

        var values2: MutableList<PointValue> = ArrayList()
        val line2: Line = Line(values2).setColor(Color.GREEN).setCubic(false)
        line2.isFilled=false
        values2.add(PointValue(0F, 1F))
        values2.add(PointValue(1F, 5F))
        values2.add(PointValue(2F, 3F))
        values2.add(PointValue(3F, 2F))
        values2.add(PointValue(4F, 1F))
        values2.add(PointValue(5F, 5F))
        values2.add(PointValue(6F, 1F))
        values2.add(PointValue(7F, 8F))
        values2.add(PointValue(8F, 2F))
        values2.add(PointValue(9F, 3F))
        values2.add(PointValue(10F, 4F))
        lines.add(line2)

        val data = LineChartData()
        data.lines = lines
        data.axisYLeft = yAxis
        data.axisXBottom= xAxis
        val chart = _binding!!.chart

        val viewport = Viewport(chart.maximumViewport)
        viewport.top = 50f
        viewport.bottom=10f
        chart.maximumViewport = viewport
        chart.currentViewport = viewport

        chart.lineChartData = data
        chart.isViewportCalculationEnabled = false



        //add table entries
        val tableView = binding!!.table
        val newRow = TableRow(context)
        newRow.setPadding(0,0,0,20)

        val params: ViewGroup.LayoutParams =
        TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        val newEntry1 = TextView(context)
        newEntry1.setLayoutParams(params)
        newEntry1.setText("CMPT 362: ")

        val newEntry2 = TextView(context)
        newEntry2.setLayoutParams(params)
        newEntry2.setText("90000 Hrs")

        //add textview to the new row
        newRow.addView(newEntry1)
        newRow.addView(newEntry2)
        tableView.addView(newRow)

        val newRow2 = TableRow(context)
        val newEntry3 = TextView(context)
        val newEntry4 = TextView(context)
        newEntry3.setLayoutParams(params)
        newEntry4.setLayoutParams(params)
        newEntry3.setText("CMPT 310: ")
        newEntry4.setText("50 Hrs: ")
        newRow2.addView(newEntry3)
        newRow2.addView(newEntry4)
        tableView.addView(newRow2)
        return root
    }
}