package com.G3.kalendar.ui.calendar

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import javax.annotation.RegEx

class StoryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    private var story:RectF? = null

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        textAlign = Paint.Align.LEFT
        textSize = 35.0f
        typeface = Typeface.create("", Typeface.NORMAL)
    }

    init{
        isClickable = true
    }
    override fun performClick(): Boolean {
        if(return super.performClick()) return true
        println("bebug on performclick")

        invalidate()
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        super.onSizeChanged(w, h, oldw, oldh)
    }

    fun setRect(rect:Rect){
        story = RectF(rect)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = Color.DKGRAY

        if(story != null)
            canvas.drawRoundRect(story!!,20f,20f,paint)

    }
}