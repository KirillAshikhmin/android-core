package com.digexco.arch.ui.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.digexco.arch.R

class LoadingIndicator : View {

    companion object {
        const val DEFAULT_ITEM_COUNT = 12
        const val DEFAULT_ITEM_RADIUS = 6f
        const val DEFAULT_COLOR = Color.BLACK
        const val DEFAULT_DOT_ALPHA = 0.1f
        const val DEFAULT_ANIMATION_DURATION = 150
    }

    private var animationValue: Float = 0f
    private var animator: ValueAnimator? = null
    private var paint: Paint = Paint()
    private var paintProgress: Paint = Paint()
    private var itemsCount: Int = DEFAULT_ITEM_COUNT
    private var itemRadius: Float = DEFAULT_ITEM_RADIUS
    private var color: Int = DEFAULT_COLOR
    private var dotAlpha: Float = DEFAULT_DOT_ALPHA
    private var animationDuration: Int = DEFAULT_ANIMATION_DURATION

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingIndicator,
            defStyleAttr, defStyleRes
        ).apply {
            try {
                itemRadius = getDimension(R.styleable.LoadingIndicator_android_radius, itemRadius)
                color = getColor(R.styleable.LoadingIndicator_android_color, color)
                itemsCount = getInteger(R.styleable.LoadingIndicator_count, itemsCount)
                dotAlpha = getFloat(R.styleable.LoadingIndicator_alpha, dotAlpha)
                animationDuration = getInteger(R.styleable.LoadingIndicator_animation_duration, animationDuration)
            } finally {
                recycle()
            }
        }

        paint.color = color
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.alpha = (dotAlpha * 255).toInt()
        paint.isAntiAlias = true
        paintProgress.color = color
        paintProgress.style = Paint.Style.FILL_AND_STROKE
        paintProgress.isAntiAlias = true
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }

    private fun startAnimation() {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0f, 2f * itemsCount).apply {
            duration = (animationDuration * itemsCount).toLong()
            interpolator = android.view.animation.LinearInterpolator()
            addUpdateListener { valueAnimator ->
                animationValue = valueAnimator.animatedValue as Float
                invalidate()
            }
        }
        animator?.repeatCount = ValueAnimator.INFINITE
        animator?.repeatMode = ValueAnimator.REVERSE
        animator?.start()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (canvas == null) return
        val radius = itemRadius
        val d = itemRadius*2
        val cy = height / 2f

        val allWidth = d * itemsCount + d * (itemsCount - 1)
        val start = (width - allWidth) / 2f

        var x = start
        for (i in 0 until itemsCount) {
            canvas.drawCircle(x + radius, cy, radius, paint)
            x += d + d
        }

        val item = (animationValue / 2f).toInt()
        val progress = animationValue % 2f

        val animatedDotX = (start + item * d + d * item).coerceAtLeast(0f)

        if (progress <= 1) paintProgress.alpha = (progress * 255).toInt()
        else paintProgress.alpha = ((1 - progress - 1) * 255).toInt()
        canvas.drawCircle(animatedDotX + radius, cy, radius, paintProgress)
    }

}
