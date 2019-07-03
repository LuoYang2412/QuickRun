package com.luoyang.quickrun.ui.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.luoyang.quickrun.R

class RadiusButton(context: Context, attrs: AttributeSet, defStyLeAttr: Int) :
    CardView(context, attrs, defStyLeAttr) {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    var textView: TextView = TextView(context)
        private set

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.RadiusButton)
        if (attributes != null) {
            textView.text = attributes.getString(R.styleable.RadiusButton_text) ?: "Radius Button"
            textView.setTextColor(attributes.getColor(R.styleable.RadiusButton_textColor, textView.currentTextColor))
            isEnabled = attributes.getBoolean(R.styleable.RadiusButton_enabled, true)
            if (isEnabled) {
                setCardBackgroundColor(cardBackgroundColor)
                textView.setTextColor(textView.currentTextColor)
                cardElevation = 4F
            } else {
                setCardBackgroundColor(Color.parseColor("#FFD6D7D7"))
                textView.setTextColor(Color.parseColor("#FFA0A0A0"))
                cardElevation = 0F
            }
        }
        attributes.recycle()
        val layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.CENTER
        layoutParams.topMargin = 40
        layoutParams.bottomMargin = 40
        addView(textView, layoutParams)
    }

}