package me.turkergoksu.lib

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.res.ResourcesCompat
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin

/**
 * Created by turkergoksu on 01-Sep-20
 */

class PercentageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // CONSTANTS
    private val START_ANGLE_OF_PERCENTAGE_BAR = 45.0
    private val END_ANGLE_OF_PERCENTAGE_BAR = 315.0

    // BAR
    private var rectF: RectF? = null
    private var centerPaint = Paint()
    private var progressPaint = Paint()
    private var progressBackgroundPaint = Paint()
    private var isSmooth = true

    // TEXT
    private var percentage = 33
    private var percentageTextPaint = TextPaint()

    // ANIMATION
    private var percentageStartAngle: Float = 135f
    private var percentageSweepAngle: Float = 270f
    private var percentageWidth: Float = 50f

    private var animator: ValueAnimator? = null
    private var animationDuration = 1000
    private var currentSweepAngle = 0
    private var currentPercentage = 0f

    private var typedArray: TypedArray? = null

    init {
        typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PercentageView,
            0,
            0
        )
        initAttributes()
        startPercentageBarAnimation()
    }

    private fun initAttributes() {
        typedArray?.apply {
            // FINAL PERCENTAGE VALUE
            percentage = abs(getInt(R.styleable.PercentageView_percentage, 0))
            percentageSweepAngle = (percentage * 270f) / 100

            // ANIMATION DURATION
            animationDuration =
                abs(getInt(R.styleable.PercentageView_animDuration, 1000))

            // PERCENTAGE WIDTH
            percentageWidth =
                abs(getFloat(R.styleable.PercentageView_percentageWidth, 50f))

            // CENTER COLOR
            centerPaint.color = getColor(
                R.styleable.PercentageView_centerColor,
                Color.WHITE
            )
            // TODO: 13-Nov-20 get rootview background color as default value

            // PROGRESS COLOR
            progressPaint.color = getColor(
                R.styleable.PercentageView_progressColor,
                Color.BLACK
            )

            // PROGRESS BACKGROUND COLOR
            progressBackgroundPaint.color = getColor(
                R.styleable.PercentageView_progressBackgroundColor,
                Color.GRAY
            )

            // PERCENTAGE TEXT COLOR
            // Default color value should be same with progressPaint
            percentageTextPaint.color = getColor(
                R.styleable.PercentageView_textColor,
                Color.BLACK
            )

            // PERCENTAGE TEXT SIZE
            percentageTextPaint.textSize = resources.getDimension(R.dimen.default_text_size)
            percentageTextPaint.textSize =
                getDimensionPixelSize(R.styleable.PercentageView_textSize, 32).toFloat()

            // PERCENTAGE TEXT FONT
            val fontId = getResourceId(R.styleable.PercentageView_android_fontFamily, 0)
            if (fontId != 0) {
                percentageTextPaint.typeface = ResourcesCompat.getFont(context, fontId)
            }

            // PERCENTAGE SMOOTHNESS
            isSmooth = getBoolean(
                R.styleable.PercentageView_softness,
                true
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        rectF = RectF(
            0f,
            0f,
            width.coerceAtMost(height).toFloat(),
            width.coerceAtMost(height).toFloat()
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val centerX = (measuredWidth / 2).toFloat()
        val centerY = (measuredHeight / 2).toFloat()
        val radius = centerX.coerceAtMost(centerY)

        // Percentage bar background
        canvas?.drawArc(
            rectF!!,
            percentageStartAngle,
            270f, // FIXME: 13-Nov-20 create constant later
            true,
            progressBackgroundPaint
        )

        // Percentage bar fill animation
        canvas?.drawArc(
            rectF!!,
            percentageStartAngle,
            currentSweepAngle.toFloat(),
            true,
            progressPaint
        )

        // Draw for fill animation
        if (isSmooth)
            drawFill(canvas, radius)

        // Center circle of percentage bar
        canvas?.drawCircle(
            rectF!!.centerX(),
            rectF!!.centerY(),
            radius - percentageWidth,
            centerPaint
        )

        // Draw Text
        val textSizeWidth =
            percentageTextPaint.measureText("%s%%".format(currentPercentage.toInt()))
        canvas?.drawText(
            "%s%%".format(currentPercentage.toInt()),
            rectF!!.centerX() - textSizeWidth / 2,
            rectF!!.centerY() + percentageTextPaint.textSize / 2,
            percentageTextPaint
        )

        // TODO: 13-Nov-20 Add shining particle effect from bottom
    }

    private fun drawFill(canvas: Canvas?, radius: Float) {
        val fillCircleRadius = percentageWidth / 2

        /**
         * distBetweenCenterAndFillCircle is the distance between center of percentage bar and
         * center of fill circle. Later in this function by multiplying with sin and cos function
         * we will get X and Y coordinate values over percentage bar circle.
         */
        val distBetweenCenterAndFillCircle = radius - fillCircleRadius

        /**
         * Temporary paint object to fix a visual bug while "percentage" value equals to 0.
         * The visual bug, shows one circle at the start of percentage bar like i even though percentage
         * equals to 0. To fix this if "percentage" value equals to 0 then draw this circle with
         * background color of percentage bar.
         */
        val tempPaint = Paint(progressPaint)
        if (percentage == 0)
            tempPaint.color = progressBackgroundPaint.color

        // Fixed circle at the start
        canvas?.drawCircle(
            rectF!!.centerX() - distBetweenCenterAndFillCircle *
                    sin(Math.toRadians(START_ANGLE_OF_PERCENTAGE_BAR)).toFloat(),
            rectF!!.centerY() + distBetweenCenterAndFillCircle *
                    cos(Math.toRadians(START_ANGLE_OF_PERCENTAGE_BAR)).toFloat(),
            fillCircleRadius,
            tempPaint
        )

        // Fixed circle at the end
        canvas?.drawCircle(
            rectF!!.centerX() - distBetweenCenterAndFillCircle *
                    sin(Math.toRadians(END_ANGLE_OF_PERCENTAGE_BAR)).toFloat(),
            rectF!!.centerY() + distBetweenCenterAndFillCircle *
                    cos(Math.toRadians(END_ANGLE_OF_PERCENTAGE_BAR)).toFloat(),
            fillCircleRadius,
            progressBackgroundPaint
        )

        // Endpoint circle of fill animation
        canvas?.drawCircle(
            rectF!!.centerX() - distBetweenCenterAndFillCircle *
                    sin(Math.toRadians(currentSweepAngle + 45.0)).toFloat(),
            rectF!!.centerY() + distBetweenCenterAndFillCircle *
                    cos(Math.toRadians(currentSweepAngle + 45.0)).toFloat(),
            fillCircleRadius,
            tempPaint
        )
    }

    private fun startPercentageBarAnimation() {
        animator?.cancel()
        animator =
            ValueAnimator.ofInt(0, percentageSweepAngle.toInt()).apply {
                duration = animationDuration.toLong()
                interpolator = LinearInterpolator()
                addUpdateListener { valueAnimator ->
                    currentSweepAngle = valueAnimator.animatedValue as Int
                    currentPercentage =
                        ceil((valueAnimator.animatedValue as Int * (percentage.toFloat() / percentageSweepAngle)))
                    invalidate()
                }
            }
        animator?.start()
    }

    fun setPercentage(percentage: Int) {
        this.percentage = percentage
        percentageSweepAngle = (percentage * 270f) / 100
        startPercentageBarAnimation()
    }
}