package com.G3.kalendar.ui.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.RoundRectShape
import android.graphics.drawable.shapes.Shape
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import com.G3.kalendar.Globals
import com.G3.kalendar.MainActivity
import com.G3.kalendar.R
import com.G3.kalendar.ViewKanbanTicket
import com.G3.kalendar.database.story.Story
import com.G3.kalendar.ui.home.LoginActivity
import java.util.*
import java.util.Collections.max

class WeekView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs) {

    private var columnWidth = (rootView.width/8).toFloat()
    private var rowLength = (rootView.height/24).toFloat()
    private var buttons:ArrayList<Button> = ArrayList()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        textAlign = Paint.Align.RIGHT
        textSize = 40.0f
        typeface = Typeface.create("",Typeface.BOLD)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        paint.color = Color.DKGRAY

        columnWidth = (width/8).toFloat()
        rowLength = (height/24).toFloat()

        // drawing horizontal grid lines
        for(i in 1..8){
            canvas.drawLine((i*columnWidth),0f,i*columnWidth,(height).toFloat(), paint)
        }

        // drawing vertical grid lines
        for(i in 1..23){
            canvas.drawLine(columnWidth-10,i*rowLength,(height).toFloat(),i*rowLength,paint)

            // drawing hours text
            val bounds = Rect()
            paint.getTextBounds("$i:00",0,"$i:00".length,bounds)
            canvas.drawText("$i:00",
                columnWidth-10,
                (i*rowLength)+(bounds.height()/2), paint)
        }

        // scroll to correct position on screen
        val scrollView: ScrollView = rootView.findViewById(R.id.scroll_view)
        val c = Math.max(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - 2,0)
        scrollView.scrollTo(0, c*rowLength.toInt())
    }

    fun populateStories(stories:List<Story>?){
        if(stories == null)
            return

        columnWidth = (rootView.width/8).toFloat()
        rowLength = 3*(rootView.height/24).toFloat()

        // remove previous buttons
        for(i in buttons.indices){
            val button = buttons[i]
            button.visibility = GONE
            buttons.drop(i)
        }

        // adding new buttons
        for(story in stories){
            for(time in story.calendarTimes){

                // getting story time
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = time
                var dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                if(dayOfWeek > 7){ dayOfWeek -= 7 }
                var hour = calendar.get(Calendar.HOUR_OF_DAY).toFloat()
                val min = calendar.get(Calendar.MINUTE).toFloat()/60
                hour += min

                // setting button location
                var button = Button(context)
                button.layoutParams = LinearLayout.LayoutParams(
                    columnWidth.toInt(),
                    rowLength.toInt()/2)
                button.gravity = Gravity.TOP
                button.text = story.name
                button.setPadding(15,15,15,15)
                button.textSize = 10f
                button.setTextColor(Color.WHITE)
                button.x = dayOfWeek*columnWidth
                button.y = hour*rowLength

                val floatArray = FloatArray(8)
                for(i in 0..7)
                    floatArray[i] = 10f

                var statusColor = 0
                when(story.status){
                    Globals.TO_DO_STATUS -> statusColor = resources.getColor(R.color.todo)
                    Globals.IN_PROGRESS_STATUS -> statusColor = resources.getColor(R.color.in_progress)
                    Globals.DONE_STATUS -> statusColor = resources.getColor(R.color.done)
                }

                // setting button colour
                var boarder = ShapeDrawable()
                boarder.shape = RoundRectShape(floatArray,null,null)
                boarder.paint.strokeWidth = 10f
                boarder.paint.style = Paint.Style.STROKE
                boarder.paint.color = statusColor

                var fill = ShapeDrawable()
                fill.shape = RoundRectShape(floatArray,null,null)
                fill.paint.style = Paint.Style.FILL_AND_STROKE
                fill.paint.color = Color.GRAY
//                fill.paint.color = story.color

                var composite = LayerDrawable(arrayOf(fill,boarder))
                button.background = composite

                val intent = Intent(context, ViewKanbanTicket::class.java)
                button.setOnClickListener{context.startActivity(intent)}
                addView(button)

                buttons.add(button)
            }
        }
    }

//            button.setBackgroundColor(resources.getColor(R.color.gray))
//            button.setBackgroundResource(R.drawable.button_layout)
//            button.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_status_complete,0, 0,0)

}