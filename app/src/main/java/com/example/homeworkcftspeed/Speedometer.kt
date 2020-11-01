package com.example.homeworkcftspeed

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import kotlin.math.pow


class Speedometer(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val typeface: Typeface? = null

    private var value = 0
    private val text = "km/h"
    private val color = -0x670050
    private val textColor = -0x6f5f01
    private val markRange = 10
    private val maxSpeed=120


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        val aspect = width / height.toFloat()
        val normalAspect = 2f / 1f
        if (aspect > normalAspect) {
            if (widthMode != MeasureSpec.EXACTLY) {
                width = Math.round(normalAspect * height)
            }
        }
        if (aspect < normalAspect) {
            if (heightMode != MeasureSpec.EXACTLY) {
                height = Math.round(width / normalAspect)
            }
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var width = width.toFloat()
        var height = height.toFloat()
        val aspect = width / height
        val normalAspect = 2f / 1f
        if (aspect > normalAspect) {
            width = normalAspect * height
        }
        if (aspect < normalAspect) {
            height = width / normalAspect
        }
        canvas.save()
        canvas.translate(width / 2, height)
        canvas.scale(.5f * width, -1f * height)
        drawCircles(canvas)
        drawMarkers(canvas)

        canvas.save()
        //drawNumbers(canvas)
        canvas.restore()
        canvas.save()
        drawCursor(canvas)
        canvas.restore()

        }
        fun drawCircles(canvas: Canvas)
        {

            paint.color = 0xFF1C1B1B.toInt()
            paint.style = Paint.Style.FILL
            paint.setTypeface(typeface)
            canvas.drawCircle(0f, 0f, 1f, paint)
            paint.color = 0xFF5A5555.toInt()
            canvas.drawCircle(0f, 0f, 0.6f, paint)


        }
        fun drawMarkers(canvas: Canvas)
        {

            paint.color=0xFFFFFFFF.toInt()
            paint.style=Paint.Style.STROKE
            paint.strokeWidth=0.005f


            val scale=0.9f

            val step=Math.PI/ maxSpeed

            for(i in 0..maxSpeed)
            {
                var x1= Math.cos(Math.PI - step * i).toFloat()
                var y1=Math.sin(Math.PI - step * i).toFloat()
                var x2:Float
                var y2:Float
                if(i%20==0)
                {
                    x2=x1*scale.pow(2)
                    y2=y1*scale.pow(2)
                }
                else
                {
                    x2=x1*scale
                    y2=y1*scale
                }
                canvas.drawLine(x1, y1, x2, y2, paint)

            }
        }
    fun drawCursor(canvas: Canvas)
    {

        canvas.rotate(90 - 180.toFloat() * (value / maxSpeed.toFloat()))
        paint.color=0xFFC54C4C.toInt()
        paint.strokeWidth=0.02f
        canvas.drawLine(0.01f, 0f, 0f, 1f, paint)
        canvas.drawLine(-0.01f, 0f, 0f, 1f, paint)

        paint.style = Paint.Style.FILL
        paint.color = 0xFF1C1B1B.toInt()
        canvas.drawCircle(0f, 0f, 0.05f, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                for (i in 0..maxSpeed) {
                    if (i <= 20)
                        setValueAnimated(i, 5000)
                    if (i in 21..40)
                        setValueAnimated(i, 10000)
                    if (i in 41..60)
                        setValueAnimated(i, 15000)
                    if (i in 61..80)
                        setValueAnimated(i, 20000)
                    if (i in 81..100)
                        setValueAnimated(i, 25000)
                    else {
                        setValueAnimated(i, 50000)
                    }


                }
                true
            }
            MotionEvent.ACTION_MOVE -> {
                true
            }
            MotionEvent.ACTION_UP -> true
            else -> super.onTouchEvent(event)
        }
    }
    var objectAnimator: ObjectAnimator? = null
    fun setValueAnimated(value: Int, speed: Int) {
        if (objectAnimator != null) {
            objectAnimator!!.cancel()
        }
        objectAnimator = ObjectAnimator.ofInt(this, "value", this.value, value)
        objectAnimator?.setDuration(speed + Math.abs(this.value - value) * 5.toLong())
        objectAnimator?.setInterpolator(DecelerateInterpolator())
        objectAnimator?.start()
    }
    fun setValue(value: Int) {
        this.value = Math.min(value, maxSpeed)
        invalidate()
    }


}