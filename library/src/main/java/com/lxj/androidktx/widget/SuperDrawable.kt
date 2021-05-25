package com.lxj.androidktx.widget

import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ResourceUtils
import kotlin.math.max

/**
 * 支持设置渐变，背景，文字，leftDrawable和rightDrawable的drawable；好处是可以放到span中与文字一起换行
 */
class SuperDrawable : Drawable() {
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var bgRadius = 0f
    private var bgColor = Color.WHITE
    private var gradientColors: IntArray? = null
    private var mGradientOrientation = GradientDrawable.Orientation.LEFT_RIGHT

    private var bgDrawableWidth = 0
    private var bgDrawableHeight = 0
    private var bgDrawable: Drawable? = null

    private var textColor = Color.BLACK
    private var textSize = ConvertUtils.sp2px(16f).toFloat()
    private var text: String = ""
    private var bold: Boolean = false
    private var typeface: Typeface? = null

    private var paddingLeft = 0
    private var paddingTop = 0
    private var paddingRight = 0
    private var paddingBottom = 0

    private var leftDrawableWidth = 0
    private var leftDrawableHeight = 0
    private var leftDrawablePadding = 0
    private var leftDrawable: Drawable? = null

    private var rightDrawableWidth = 0
    private var rightDrawableHeight = 0
    private var rightDrawablePadding = 0
    private var rightDrawable: Drawable? = null

    init {
        textPaint.color = textColor
        textPaint.textSize = textSize
        textPaint.textAlign = Paint.Align.LEFT
        if(bold) textPaint.isFakeBoldText = true
        if(typeface!=null) textPaint.typeface = typeface

        bgPaint.color = bgColor
        bgPaint.style = Paint.Style.FILL
    }

    private fun getTextBounds(): Rect {
        val rect = Rect()
        textPaint.getTextBounds(text, 0, text.length, rect)
        return rect
    }

    //文字图标垂直居中
    override fun draw(canvas: Canvas) {
        //1.draw bg
        canvas.clipRect(bounds)
        canvas.save()
        if(bgDrawable!=null){
            bgDrawable?.setBounds(0, 0, bgDrawableWidth, bgDrawableHeight)
            bgDrawable?.draw(canvas)
        }else{
            val mShader = getShaders()
            if (mShader != null) bgPaint.shader = mShader
            canvas.drawRoundRect(
                RectF(0f, 0f, bounds.right.toFloat(), bounds.bottom.toFloat()),
                bgRadius, bgRadius, bgPaint
            )
        }

        //2. draw leftDrawable
        if (leftDrawable != null) {
            val offsetX = paddingLeft.toFloat()
            val offsetY = bounds.height() / 2f - leftDrawableHeight / 2f
            canvas.translate(offsetX, offsetY)
            leftDrawable?.setBounds(0, 0, leftDrawableWidth, leftDrawableHeight)
            leftDrawable?.draw(canvas)
            canvas.translate(-offsetX, -offsetY)
        }

        //3. draw Text
        val r: Rect = getTextBounds()
        val x = paddingLeft + leftDrawableWidth + leftDrawablePadding
        val fm = textPaint.fontMetrics
//        canvas.drawText(
//            text, x.toFloat(), bounds.height() / 2f + r.height() / 2 ,
//            textPaint
//        )
        //让文字垂直居中
        canvas.drawText(
            text, x.toFloat(), (bounds.bottom + bounds.top - fm.ascent - fm.descent) / 2,
            textPaint
        )

        //4. draw rightDrawable
        if (rightDrawable != null) {
            val offsetX = x + r.width() + rightDrawablePadding.toFloat()
            val offsetY = bounds.height() / 2f - rightDrawableHeight / 2f
            canvas.translate(offsetX, offsetY)
            rightDrawable?.setBounds(0, 0, rightDrawableWidth, rightDrawableHeight)
            rightDrawable?.draw(canvas)
            canvas.translate(-offsetX, -offsetY)
        }
        canvas.restore()
    }

    private fun getShaders(): LinearGradient? {
        if (gradientColors == null) return null
        val r = bounds
        val x0: Float
        val x1: Float
        val y0: Float
        val y1: Float
        val level = 1f
        when (mGradientOrientation) {
            GradientDrawable.Orientation.TOP_BOTTOM -> {
                x0 = r.left.toFloat()
                y0 = r.top.toFloat()
                x1 = x0
                y1 = level * r.bottom.toFloat()
            }
            GradientDrawable.Orientation.TR_BL -> {
                x0 = r.right.toFloat()
                y0 = r.top.toFloat()
                x1 = level * r.left
                y1 = level * r.bottom
            }
            GradientDrawable.Orientation.RIGHT_LEFT -> {
                x0 = r.right.toFloat()
                y0 = r.top.toFloat()
                x1 = level * r.left
                y1 = y0
            }
            GradientDrawable.Orientation.BR_TL -> {
                x0 = r.right.toFloat()
                y0 = r.bottom.toFloat()
                x1 = level * r.left
                y1 = level * r.top
            }
            GradientDrawable.Orientation.BOTTOM_TOP -> {
                x0 = r.left.toFloat()
                y0 = r.bottom.toFloat()
                x1 = x0
                y1 = level * r.top
            }
            GradientDrawable.Orientation.BL_TR -> {
                x0 = r.left.toFloat()
                y0 = r.bottom.toFloat()
                x1 = level * r.right
                y1 = level * r.top
            }
            GradientDrawable.Orientation.LEFT_RIGHT -> {
                x0 = r.left.toFloat()
                y0 = r.top.toFloat()
                x1 = level * r.right
                y1 = y0
            }
            else -> {
                x0 = r.left.toFloat()
                y0 = r.top.toFloat()
                x1 = level * r.right
                y1 = level * r.bottom
            }
        }
        return LinearGradient(x0, y0, x1, y1, gradientColors!!, null, Shader.TileMode.CLAMP)
    }

    override fun setAlpha(alpha: Int) {}

    override fun setColorFilter(cf: ColorFilter?) {}

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun getIntrinsicWidth(): Int {
        if(bgDrawable!=null) return bgDrawableWidth
        return getTextBounds().width() +
                paddingLeft + paddingRight + leftDrawableWidth + leftDrawablePadding +
                rightDrawableWidth + rightDrawablePadding
    }

    override fun getIntrinsicHeight(): Int {
        if(bgDrawable!=null) return bgDrawableHeight
        val max = max(leftDrawableHeight, rightDrawableHeight)
        return max(getTextBounds().height(), max)+ paddingTop + paddingBottom
    }

    fun setText(text: String, textColor: Int? = null, textSize: Float? = null,
        bold: Boolean? = null, typeface: Typeface? = null): SuperDrawable {
        this.text = text
        if (textColor != null) {
            this.textColor = textColor
            textPaint.color = this.textColor
        }
        if (textSize != null) {
            this.textSize = textSize
            textPaint.textSize = this.textSize
        }
        if (bold != null) {
            this.bold = bold
            textPaint.isFakeBoldText = this.bold
        }
        if (typeface != null) {
            this.typeface = typeface
            textPaint.typeface = this.typeface
        }
        return this
    }

    fun setBg(
        bgColor: Int? = null, bgRadius: Float? = null, gradientColors: IntArray? = null,
        gradientOrientation: GradientDrawable.Orientation? = null
    ): SuperDrawable {
        if (bgColor != null) {
            this.bgColor = bgColor
            bgPaint.color = this.bgColor
        }
        if (bgRadius != null) this.bgRadius = bgRadius
        if (gradientColors != null) this.gradientColors = gradientColors
        if (gradientOrientation != null) this.mGradientOrientation = gradientOrientation
        return this
    }

    fun setLeftDrawable(
        resId: Int, drawableWidth: Int? = null, drawableHeight: Int? = null,
        drawablePadding: Int? = null
    ): SuperDrawable {
        this.leftDrawable = ResourceUtils.getDrawable(resId)
        if (drawableWidth != null) leftDrawableWidth = drawableWidth
        if (drawableHeight != null) leftDrawableHeight = drawableHeight
        if (drawablePadding != null) leftDrawablePadding = drawablePadding
        return this
    }

    fun setRightDrawable(
        resId: Int, drawableWidth: Int? = null, drawableHeight: Int? = null,
        drawablePadding: Int? = null
    ): SuperDrawable {
        this.rightDrawable = ResourceUtils.getDrawable(resId)
        if (drawableWidth != null) rightDrawableWidth = drawableWidth
        if (drawableHeight != null) rightDrawableHeight = drawableHeight
        if (drawablePadding != null) rightDrawablePadding = drawablePadding
        return this
    }

    fun setBgDrawable(
        resId: Int, drawableWidth: Int? = null, drawableHeight: Int? = null
    ): SuperDrawable {
        this.bgDrawable = ResourceUtils.getDrawable(resId)
        if (drawableWidth != null) bgDrawableWidth = drawableWidth
        if (drawableHeight != null) bgDrawableHeight = drawableHeight
        return this
    }

    fun setPadding(
        left: Int? = 0,
        top: Int? = 0,
        right: Int? = 0,
        bottom: Int? = 0,
    ): SuperDrawable {
        if (left != null) this.paddingLeft = left
        if (top != null) this.paddingTop = top
        if (right != null) this.paddingRight = right
        if (bottom != null) this.paddingBottom = bottom
        return this
    }
}