package com.xiamo.marqueelib.sun

import android.content.Context
import android.graphics.Typeface
import android.view.animation.Animation
import androidx.annotation.AnimRes
import android.widget.TextView
import android.text.TextUtils
import android.view.Gravity
import android.os.Build
import androidx.annotation.FontRes
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.AnimationUtils
import android.widget.ViewFlipper
import androidx.core.content.res.ResourcesCompat
import com.xiamo.marqueelib.R
import com.xiamo.marqueelib.ScreenUtil
import java.lang.RuntimeException


class MarqueeViewSun<T>(context: Context, attrs: AttributeSet?) :
    ViewFlipper(context, attrs) {
    private var interval = 3000
    private var hasSetAnimDuration = false
    private var animDuration = 1000
    private var textSize = 14
    private var textColor = -0x1000000
    private var singleLine = false
    private var gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
    private var direction = DIRECTION_BOTTOM_TO_TOP
    private var typeface: Typeface? = null

    @AnimRes
    private var inAnimResId: Int = R.anim.anim_bottom_in

    @AnimRes
    private var outAnimResId: Int = R.anim.anim_top_out
    private var position = 0
    private var messages: MutableList<T>? = ArrayList()
    private var onItemClickListener: OnItemClickListener? = null

    constructor(context: Context) : this(context, null) {}

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.MarqueeViewStyleSun, defStyleAttr, 0)
        interval = typedArray.getInteger(R.styleable.MarqueeViewStyleSun_mvInterval, interval)
        hasSetAnimDuration = typedArray.hasValue(R.styleable.MarqueeViewStyleSun_mvAnimDuration)
        animDuration =
            typedArray.getInteger(R.styleable.MarqueeViewStyleSun_mvAnimDuration, animDuration)
        singleLine = typedArray.getBoolean(R.styleable.MarqueeViewStyleSun_mvSingleLine, false)
        if (typedArray.hasValue(R.styleable.MarqueeViewStyleSun_mvTextSize)) {
            textSize =
                typedArray.getDimension(R.styleable.MarqueeViewStyleSun_mvTextSize, textSize.toFloat())
                    .toInt()
            textSize = ScreenUtil.px2sp(context, textSize.toFloat())
        }
        textColor = typedArray.getColor(R.styleable.MarqueeViewStyleSun_mvTextColor, textColor)
        @FontRes val fontRes = typedArray.getResourceId(R.styleable.MarqueeViewStyleSun_mvFont, 0)
        if (fontRes != 0) {
            typeface = ResourcesCompat.getFont(context, fontRes)
        }
        when (typedArray.getInt(R.styleable.MarqueeViewStyleSun_mvGravity, GRAVITY_LEFT)) {
            GRAVITY_LEFT -> gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
            GRAVITY_CENTER -> gravity = Gravity.CENTER
            GRAVITY_RIGHT -> gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
        }
        if (typedArray.hasValue(R.styleable.MarqueeViewStyleSun_mvDirection)) {
            direction = typedArray.getInt(R.styleable.MarqueeViewStyleSun_mvDirection, direction)
            when (direction) {
                DIRECTION_BOTTOM_TO_TOP -> {
                    inAnimResId = R.anim.anim_bottom_in
                    outAnimResId = R.anim.anim_top_out
                }
                DIRECTION_TOP_TO_BOTTOM -> {
                    inAnimResId = R.anim.anim_top_in
                    outAnimResId = R.anim.anim_bottom_out
                }
                DIRECTION_RIGHT_TO_LEFT -> {
                    inAnimResId = R.anim.anim_right_in
                    outAnimResId = R.anim.anim_left_out
                }
                DIRECTION_LEFT_TO_RIGHT -> {
                    inAnimResId = R.anim.anim_left_in
                    outAnimResId = R.anim.anim_right_out
                }
            }
        } else {
            inAnimResId = R.anim.anim_bottom_in
            outAnimResId = R.anim.anim_top_out
        }
        typedArray.recycle()
        flipInterval = interval
    }
    /**
     * 根据字符串，启动翻页公告
     *
     * @param message      字符串
     * @param inAnimResId  进入动画的resID
     * @param outAnimResID 离开动画的resID
     */
    /**
     * 根据字符串，启动翻页公告
     *
     * @param message 字符串
     */
    @JvmOverloads
    fun startWithText(
        message: String, @AnimRes inAnimResId: Int = this.inAnimResId,
        @AnimRes outAnimResID: Int = outAnimResId
    ) {
        if (TextUtils.isEmpty(message)) return
        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                } else {
                    viewTreeObserver.removeGlobalOnLayoutListener(this)
                }
                startWithFixedWidth(message, inAnimResId, outAnimResID)
            }
        })
    }

    /**
     * 根据字符串和宽度，启动翻页公告
     *
     * @param message 字符串
     */
    private fun startWithFixedWidth(
        message: String, @AnimRes inAnimResId: Int,
        @AnimRes outAnimResID: Int
    ) {
        val messageLength = message.length
        val width: Int = ScreenUtil.px2dip(context, width.toFloat())
        if (width == 0) {
            throw RuntimeException("Please set the width of MarqueeView !")
        }
        val limit = width / textSize
        val list: MutableList<String> = ArrayList()
        if (messageLength <= limit) {
            list.add(message)
        } else {
            val size = messageLength / limit + if (messageLength % limit != 0) 1 else 0
            for (i in 0 until size) {
                val startIndex = i * limit
                val endIndex =
                    if ((i + 1) * limit >= messageLength) messageLength else (i + 1) * limit
                list.add(message.substring(startIndex, endIndex))
            }
        }
        if (messages == null) {
            messages = ArrayList()
        }
        messages!!.clear()
        messages!!.addAll(list as Collection<T>)
        postStart(inAnimResId, outAnimResID)
    }
    /**
     * 根据字符串列表，启动翻页公告
     *
     * @param messages     字符串列表
     * @param inAnimResId  进入动画的resID
     * @param outAnimResID 离开动画的resID
     */
    /**
     * 根据字符串列表，启动翻页公告
     *
     * @param messages 字符串列表
     */
    @JvmOverloads
    fun startWithList(
        messages: Any,
        @AnimRes inAnimResId: Int = this.inAnimResId,
        @AnimRes outAnimResID: Int = outAnimResId
    ) {
        messages as MutableList<T>?
        if(messages==null)return
        if (messages.isEmpty()) return
        setMessages(messages)
        postStart(inAnimResId, outAnimResID)
    }

    private fun postStart(@AnimRes inAnimResId: Int, @AnimRes outAnimResID: Int) {
        post { start(inAnimResId, outAnimResID) }
    }

    private var isAnimStart = false
    private fun start(@AnimRes inAnimResId: Int, @AnimRes outAnimResID: Int) {
        removeAllViews()
        clearAnimation()
        // 检测数据源
        if (messages == null || messages!!.isEmpty()) {
            throw RuntimeException("The messages cannot be empty!")
        }
        position = 0
        addView(createTextView(messages!![position]))
        if (messages!!.size > 1) {
            setInAndOutAnimation(inAnimResId, outAnimResID)
            startFlipping()
        }
        if (inAnimation != null) {
            inAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    if (isAnimStart) {
                        animation.cancel()
                    }
                    isAnimStart = true
                }

                override fun onAnimationEnd(animation: Animation) {
                    position++
                    if (position >= messages!!.size) {
                        position = 0
                    }
                    val view = createTextView(messages!![position])
                    if (view.parent == null) {
                        addView(view)
                    }
                    isAnimStart = false
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
    }

    private fun createTextView(marqueeItem: T): TextView {
        var textView = getChildAt((displayedChild + 1) % 3)
        if (textView == null) {
            textView = TextView(context)
            textView.gravity = gravity or Gravity.CENTER_VERTICAL
            textView.setTextColor(textColor)
            textView.textSize = textSize.toFloat()
            textView.includeFontPadding = true
            textView.isSingleLine = singleLine
            if (singleLine) {
                textView.maxLines = 1
                textView.ellipsize = TextUtils.TruncateAt.END
            }
            if (typeface != null) {
                textView.typeface = typeface
            }
            textView.setOnClickListener { v ->
                if (onItemClickListener != null) {
                    onItemClickListener!!.onItemClick(getPosition(), v as TextView)
                }
            }
        }
        var message: CharSequence = ""
        if (marqueeItem is CharSequence) {
            message = marqueeItem
        } else if (marqueeItem is IMarqueeItem) {
            message = (marqueeItem as IMarqueeItem).marqueeMessage()
        }

         textView as TextView
        textView.text = message
        textView.tag = position
        return textView
    }

    fun getPosition(): Int {
        return currentView.tag as Int
    }

    fun getMessages(): List<T>? {
        return messages
    }

    fun setMessages(messages: MutableList<T>?) {
        this.messages = messages
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, textView: TextView?)
    }

    /**
     * 设置进入动画和离开动画
     *
     * @param inAnimResId  进入动画的resID
     * @param outAnimResID 离开动画的resID
     */
    private fun setInAndOutAnimation(@AnimRes inAnimResId: Int, @AnimRes outAnimResID: Int) {
        val inAnim: Animation = AnimationUtils.loadAnimation(context, inAnimResId)
        if (hasSetAnimDuration) inAnim.duration = animDuration.toLong()
        inAnimation = inAnim
        val outAnim: Animation = AnimationUtils.loadAnimation(context, outAnimResID)
        if (hasSetAnimDuration) outAnim.duration = animDuration.toLong()
        outAnimation = outAnim
    }

    fun setTypeface(typeface: Typeface?) {
        this.typeface = typeface
    }

    companion object {
        private const val GRAVITY_LEFT = 0
        private const val GRAVITY_CENTER = 1
        private const val GRAVITY_RIGHT = 2
        private const val DIRECTION_BOTTOM_TO_TOP = 0
        private const val DIRECTION_TOP_TO_BOTTOM = 1
        private const val DIRECTION_RIGHT_TO_LEFT = 2
        private const val DIRECTION_LEFT_TO_RIGHT = 3
    }

    init {
        init(context, attrs, 0)
    }
}