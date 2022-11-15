package com.G3.kalendar.ui.calendar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

class CalendarActivity(context: Context): View(context) {

    private lateinit var bitmap: Bitmap
    private lateinit var canvas: Canvas

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if(::bitmap.isInitialized)
            bitmap.recycle()

        bitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
    }

}