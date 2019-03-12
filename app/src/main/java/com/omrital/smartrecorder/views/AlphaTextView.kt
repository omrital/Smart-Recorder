package com.omrital.smartrecorder.views

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.MotionEvent

class AlphaTextView : AppCompatTextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                alpha = 0.5f
            }
            MotionEvent.ACTION_UP -> {
                alpha = 1.0f
                callOnClick()
            }
            else -> {
                alpha = 1.0f
            }
        }
        return true
    }
}
