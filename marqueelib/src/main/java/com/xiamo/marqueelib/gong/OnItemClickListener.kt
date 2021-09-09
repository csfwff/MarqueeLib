package com.xiamo.marqueelib.gong

import android.view.View

interface OnItemClickListener<V : View?, E> {
    fun onItemClickListener(mView: V, mData: E, mPosition: Int)
}