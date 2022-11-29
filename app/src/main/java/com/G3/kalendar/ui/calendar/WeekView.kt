package com.G3.kalendar.ui.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.G3.kalendar.MainActivity
import com.G3.kalendar.R

class WeekView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs) {

    private var columnWidth = (rootView.width/8).toFloat()
    private var rowLength = (rootView.height/24).toFloat()

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
        println("bebug week $columnWidth $rowLength")

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
    }

    fun addStory(rect: Rect) {
//        columnWidth = (rootView.width/8).toFloat()
//        rowLength = (rootView.height/24).toFloat()

    }


}