package com.xiamo.marqueelib.g

import com.xiamo.marqueelib.gong.OnItemClickListener

import android.view.animation.Animation

import androidx.annotation.AnimRes

import android.content.Context

import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View

import android.widget.ViewFlipper
import androidx.annotation.Nullable
import com.xiamo.marqueelib.R
import com.xiamo.marqueelib.gong.AnimationListenerAdapter
import com.xiamo.marqueelib.gong.MarqueeFactory
import java.lang.UnsupportedOperationException
import java.util.*


open class MarqueeViewGong<T : View, E>(context: Context?, attrs: AttributeSet?) :
    ViewFlipper(context, attrs), Observer {
    protected var factory: MarqueeFactory<T, E>? = null
    private val DEFAULT_ANIM_RES_IN: Int = R.anim.anim_bottom_in
    private val DEFAULT_ANIM_RES_OUT: Int = R.anim.anim_top_out

    constructor(context: Context?) : this(context, null) {}

    private fun init(attrs: AttributeSet?) {
        if (inAnimation == null || outAnimation == null) {
            setInAnimation(context, DEFAULT_ANIM_RES_IN)
            setOutAnimation(context, DEFAULT_ANIM_RES_OUT)
        }
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.MarqueeViewGong)
        if (a.hasValue(R.styleable.MarqueeViewGong_marqueeAnimDuration)) {
            val animDuration = a.getInt(R.styleable.MarqueeViewGong_marqueeAnimDuration, -1)
            inAnimation.duration = animDuration.toLong()
            outAnimation.duration = animDuration.toLong()
        }
        a.recycle()
        setOnClickListener(onClickListener)
    }

    fun setAnimDuration(animDuration: Long) {
        if (inAnimation != null) {
            inAnimation.duration = animDuration
        }
        if (outAnimation != null) {
            outAnimation.duration = animDuration
        }
    }

    fun setInAndOutAnim(inAnimation: Animation?, outAnimation: Animation?) {
        setInAnimation(inAnimation)
        setOutAnimation(outAnimation)
    }

    fun setInAndOutAnim(@AnimRes inResId: Int, @AnimRes outResId: Int) {
        setInAnimation(context, inResId)
        setOutAnimation(context, outResId)
    }

    fun setMarqueeFactory(factory: MarqueeFactory<T, E>) {
        this.factory = factory
        factory.attachedToMarqueeView(this)
        refreshChildViews()
    }

    protected open fun refreshChildViews() {
        if (childCount > 0) {
            removeAllViews()
        }
        val mViews: List<T> = factory!!.getMarqueeViews()
        for (i in mViews.indices) {
            addView(mViews[i])
        }
    }

    override fun update(o: Observable?, arg: Any?) {
        if (arg == null) return
        if (MarqueeFactory.COMMAND_UPDATE_DATA.equals(arg.toString())) {
            val animation = inAnimation
            if (animation != null && animation.hasStarted()) {
                animation.setAnimationListener(object : AnimationListenerAdapter() {
                    override fun onAnimationEnd(animation: Animation) {
                        refreshChildViews()
                        if (animation != null) {
                            animation.setAnimationListener(null)
                        }
                    }
                })
            } else {
                refreshChildViews()
            }
        }
    }

    // <editor-fold desc="点击事件模块">
    private var onItemClickListener: OnItemClickListener<T?, E>? = null
    private var isJustOnceFlag = true
    private val onClickListener = OnClickListener {
        if (onItemClickListener != null) {
            if (factory == null || (factory!!.data)?.isEmpty() == true || childCount == 0) {
                //onItemClickListener!!.onItemClickListener(null, null, -1)
                return@OnClickListener
            }
            val displayedChild = displayedChild
            val mData: E = (factory!!.data)!![displayedChild]
            onItemClickListener!!.onItemClickListener(currentView as T, mData, displayedChild)
        }
    }

    override fun setOnClickListener(@Nullable l: OnClickListener?) {
        isJustOnceFlag = if (isJustOnceFlag) {
            super.setOnClickListener(l)
            false
        } else {
            throw UnsupportedOperationException("The setOnClickListener method is not supported,please use setOnItemClickListener method.")
        }
    }

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener<T?, E>?) {
        onItemClickListener = mOnItemClickListener
    } // </editor-fold>

    init {
        init(attrs)
    }
}