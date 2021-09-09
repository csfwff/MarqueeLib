package com.xiamo.marqueelib.gong

import android.content.Context
import android.view.View
import android.widget.TextView


class SimpleMF<E : CharSequence?>(mContext: Context) :
    MarqueeFactory<View, E>(mContext) {
    public override fun generateMarqueeItemView(data: E): TextView {
        val mView = TextView(mContext)
        mView.text = data
        return mView
    }
}