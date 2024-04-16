/*
 *
 *   Created by Ved Prakash on 4/16/24, 3:06 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 4/16/24, 3:06 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.utils

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.res.ResourcesCompat
import com.infinity8.mvvm_clean_base.R

class CustomCircularProgressBar : View {

    private var progress = 0f
    private var maxProgress = 100f
    private var strokeWidth = 20f
    private var startAngle = -90f
    private var progressColor = Color.GREEN
    private var backgroundColor = Color.GRAY
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()
    private lateinit var progressAnimator: ValueAnimator
    private var drawableResId: Int = R.drawable.baseline_density_medium_24

    fun setDrawableResource(drawableResId: Int) {
        this.drawableResId = drawableResId
        invalidate()
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.CustomCircularProgressBar)
            progressColor =
                typedArray.getColor(
                    R.styleable.CustomCircularProgressBar_progressColor,
                    Color.GREEN
                )
            backgroundColor =
                typedArray.getColor(
                    R.styleable.CustomCircularProgressBar_backgroundColor,
                    Color.GRAY
                )
            strokeWidth =
                typedArray.getDimension(R.styleable.CustomCircularProgressBar_strokeWidth, 20f)
            drawableResId =
                typedArray.getResourceId(
                    R.styleable.CustomCircularProgressBar_centerImage,
                    R.drawable.baseline_density_medium_24
                )
            progress = typedArray.getFloat(R.styleable.CustomCircularProgressBar_progress, 0F)
            maxProgress =
                typedArray.getFloat(R.styleable.CustomCircularProgressBar_maxProgress, 100F)
            typedArray.recycle()

        }
        paint.strokeWidth = strokeWidth
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (width - strokeWidth) / 2f

        // Draw background circle
        paint.color = backgroundColor
        canvas.drawCircle(centerX, centerY, radius, paint)

        // Draw progress arc
        paint.color = progressColor
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        canvas.drawArc(rectF, startAngle, 360 * (progress / maxProgress), false, paint)

        // Draw image at the center
        val drawable = ResourcesCompat.getDrawable(resources, drawableResId, null)
        drawable?.let {
            val drawableWidth = drawable.intrinsicWidth
            val drawableHeight = drawable.intrinsicHeight
            val left = centerX - drawableWidth / 2f
            val top = centerY - drawableHeight / 2f
            drawable.setBounds(
                left.toInt(),
                top.toInt(),
                (left + drawableWidth).toInt(),
                (top + drawableHeight).toInt()
            )
            drawable.draw(canvas)
        }
    }


    fun setProgress(progress: Float) {
        progressAnimator = ValueAnimator.ofFloat(this.progress, progress)
        progressAnimator.duration = 1000 // Animation duration in milliseconds
        progressAnimator.interpolator = DecelerateInterpolator() // Decelerate the animation
        progressAnimator.addUpdateListener { animation ->
            this.progress = animation.animatedValue as Float
            invalidate()
        }
        progressAnimator.start()
    }

    fun setMaxProgress(maxProgress: Float) {
        this.maxProgress = maxProgress
    }

    fun setProgressColor(color: Int) {
        progressColor = color
        invalidate()
    }

    fun setBackgroundColorUI(color: Int) {
        backgroundColor = color
        invalidate()
    }
}
