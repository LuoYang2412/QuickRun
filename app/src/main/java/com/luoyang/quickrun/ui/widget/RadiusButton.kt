package com.luoyang.quickrun.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.luoyang.quickrun.R

class RadiusButton(context: Context, attrs: AttributeSet, defStyLeAttr: Int) :
    LinearLayout(context, attrs, defStyLeAttr) {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    private lateinit var cardView: CardView
    private var cardColor: Int = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_radius_btn, this, true)
        cardView = findViewById<CardView>(R.id.radius_btn_cardView)
        val textView = findViewById<TextView>(R.id.radius_btn_content_textView14)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.RadiusButton)
        if (attributes != null) {
            cardView.isEnabled = attributes.getBoolean(R.styleable.RadiusButton_btnEnable, true)
            cardView.radius = attributes.getDimension(R.styleable.RadiusButton_btnRadius, 2F)
            cardColor = attributes.getColor(
                R.styleable.RadiusButton_btnBackground,
                resources.getColor(R.color.colorPrimary)
            )
            if (cardView.isEnabled) {
                cardView.setCardBackgroundColor(cardColor)
            } else {
                cardView.setCardBackgroundColor(resources.getColor(android.R.color.holo_blue_dark))
            }
            textView.text = attributes.getString(R.styleable.RadiusButton_btnText)
            textView.setTextColor(
                attributes.getColor(R.styleable.RadiusButton_btnTextColor, resources.getColor(android.R.color.white))
            )
        }
        attributes.recycle()
    }

    fun setBtnEnable(boolean: Boolean) {
        cardView.isEnabled = boolean
        if (boolean) {
            cardView.setCardBackgroundColor(cardColor)
        } else {
            cardView.setCardBackgroundColor(resources.getColor(android.R.color.holo_blue_dark))
        }
    }

    override fun setOnClickListener(listener: OnClickListener) {
        cardView.setOnClickListener(listener)
    }
}