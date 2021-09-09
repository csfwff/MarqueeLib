package com.xiamo.marqueelib.gong

import android.content.Context
import android.view.View
import com.xiamo.marqueelib.g.MarqueeViewGong
import java.lang.IllegalStateException
import java.lang.String
import java.util.*
import kotlin.collections.ArrayList

abstract class MarqueeFactory<T : View, E>(mContext: Context) : Observable() {
    protected var mContext: Context = mContext
    protected var mViews: MutableList<T>? = null
    protected var dataList: List<E>? = null
    private var mMarqueeView: MarqueeViewGong<T, E>? = null
    protected abstract fun generateMarqueeItemView(data: E): T

    public var data: List<E>?
        get() = dataList
        set(dataList) {
            if (dataList == null) {
                return
            }
            this.dataList = dataList
            mViews = ArrayList<T>()
            for (i in dataList.indices) {
                val data = dataList[i]
                val mView = generateMarqueeItemView(data)
                (mViews)?.add(mView)
            }
            notifyDataChanged()
        }

    fun getMarqueeViews() :List<T> {
        return if (mViews != null) mViews!! else Collections.EMPTY_LIST as  MutableList<T>
    }
    private val isAttachedToMarqueeView: Boolean
        private get() = mMarqueeView != null

    fun attachedToMarqueeView(marqueeView: MarqueeViewGong<T,E>?) {
        if (!isAttachedToMarqueeView) {
            mMarqueeView = marqueeView
            this.addObserver(marqueeView)
            return
        }
        throw IllegalStateException(
            String.format(
                "The %s has been attached to the %s!",
                toString(),
                mMarqueeView.toString()
            )
        )
    }

    private fun notifyDataChanged() {
        if (isAttachedToMarqueeView) {
            setChanged()
            notifyObservers(COMMAND_UPDATE_DATA)
        }
    }

    companion object {
        const val COMMAND_UPDATE_DATA = "UPDATE_DATA"
    }

}