package com.xiamo.marqueelib.gong

import android.widget.TextView



import android.content.res.ColorStateList

import androidx.annotation.ColorInt


import android.content.Context

import android.content.res.TypedArray
import android.text.TextUtils.TruncateAt
import android.util.AttributeSet

import android.view.Gravity
import android.view.View
import com.xiamo.marqueelib.R
import com.xiamo.marqueelib.ScreenUtil
import com.xiamo.marqueelib.g.MarqueeViewGong
import java.lang.UnsupportedOperationException


class SimpleMarqueeViewGong<E>(context: Context?, attrs: AttributeSet?) :
    MarqueeViewGong<View, E>(context, attrs) {
    private var smvTextColor: ColorStateList? = null
    private var smvTextSize = 15f
    private var smvTextGravity = Gravity.NO_GRAVITY
    private var smvTextSingleLine = false
    private var smvTextEllipsize: TruncateAt? = null

    constructor(context: Context?) : this(context, null) {}

    private fun init(attrs: AttributeSet?) {
        var ellipsize = -1
        if (attrs != null) {
            val a: TypedArray =
                getContext().obtainStyledAttributes(attrs, R.styleable.SimpleMarqueeViewGong, 0, 0)
            smvTextColor = a.getColorStateList(R.styleable.SimpleMarqueeViewGong_smvTextColor)
            if (a.hasValue(R.styleable.SimpleMarqueeViewGong_smvTextSize)) {
                smvTextSize = a.getDimension(R.styleable.SimpleMarqueeViewGong_smvTextSize, smvTextSize)
                smvTextSize = ScreenUtil.px2sp(context, smvTextSize).toFloat()
            }
            smvTextGravity = a.getInt(R.styleable.SimpleMarqueeViewGong_smvTextGravity, smvTextGravity)
            smvTextSingleLine =
                a.getBoolean(R.styleable.SimpleMarqueeViewGong_smvTextSingleLine, smvTextSingleLine)
            ellipsize = a.getInt(R.styleable.SimpleMarqueeViewGong_smvTextEllipsize, ellipsize)
            a.recycle()
        }
        if (smvTextSingleLine && ellipsize < 0) {
            ellipsize = 3 // END
        }
        when (ellipsize) {
            1 -> smvTextEllipsize = TruncateAt.START
            2 -> smvTextEllipsize = TruncateAt.MIDDLE
            3 -> smvTextEllipsize = TruncateAt.END
        }
    }

     override fun refreshChildViews() {
        super.refreshChildViews()
        val views: List<TextView> = factory!!.getMarqueeViews() as List<TextView>
        for (textView in views) {
            textView.textSize = smvTextSize
            textView.gravity = smvTextGravity
            if (smvTextColor != null) {
                textView.setTextColor(smvTextColor)
            }
            textView.isSingleLine = smvTextSingleLine
            textView.ellipsize = smvTextEllipsize
        }
    }

    fun setTextSize(textSize: Float) {
        smvTextSize = textSize
        if (factory != null) {
            for (textView in factory!!.getMarqueeViews()) {
                textView as TextView
                textView.textSize = textSize
            }
        }
    }

    fun setTextColor(@ColorInt color: Int) {
        setTextColor(ColorStateList.valueOf(color))
    }

    fun setTextColor(colorStateList: ColorStateList?) {
        smvTextColor = colorStateList
        if (factory != null) {
            for (textView in factory!!.getMarqueeViews()) {
                textView as TextView
                textView.setTextColor(smvTextColor)
            }
        }
    }

    fun setTextGravity(gravity: Int) {
        smvTextGravity = gravity
        if (factory != null) {
            for (textView in factory!!.getMarqueeViews()) {
                textView as TextView
                textView.gravity = smvTextGravity
            }
        }
    }

    fun setTextSingleLine(singleLine: Boolean) {
        smvTextSingleLine = singleLine
        if (factory != null) {
            for (textView in factory!!.getMarqueeViews()) {
                textView as TextView
                textView.isSingleLine = smvTextSingleLine
            }
        }
    }

    fun setTextEllipsize(where: TruncateAt) {
        if (where == TruncateAt.MARQUEE) {
            throw UnsupportedOperationException("The type MARQUEE is not supported!")
        }
        smvTextEllipsize = where
        if (factory != null) {
            for (textView in factory!!.getMarqueeViews()) {
                textView as TextView
                textView.ellipsize = where
            }
        }
    }

    init {
        init(attrs)
    }
}