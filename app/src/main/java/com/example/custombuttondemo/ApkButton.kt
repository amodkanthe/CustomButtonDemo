package com.example.custombuttondemo


import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView.ScaleType
import androidx.core.content.ContextCompat


class ApkButton(context: Context, attrs: AttributeSet) : View(context, attrs),
    View.OnClickListener {

    var mDrawable: Drawable? = null
    var title: String? = null
    var subTitle: String? = null
    var titleTextSize: Int = 14
    var subTitleTextSize: Int = 12
    var textColor: Int? = R.color.white
    var backgroundColor: Int? = 0
    var textStyle: Int? = 0
    var textStyleSubTitle: Int? = 0
    var scaleType: ScaleType? = null
    val padding = resources.getDimensionPixelSize(R.dimen.padding_text).toFloat()
    val paddingDrawable = resources.getDimensionPixelSize(R.dimen.padding_icon)
    val iconSize = resources.getDimensionPixelSize(R.dimen.icon_size)
    var labelPaintSubtitle: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var labelPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    var radius = 0f;
    var drawAroundFinger = false
    var paintClick = Paint()
    var buttonHeightWithoutIcon =
        resources.getDimensionPixelSize(R.dimen.button_height_without_icon)

    var listener: OnClickListener? = null
    private val sScaleTypeArray = arrayOf(
        ScaleType.MATRIX,
        ScaleType.FIT_XY,
        ScaleType.FIT_START,
        ScaleType.FIT_CENTER,
        ScaleType.FIT_END,
        ScaleType.CENTER,
        ScaleType.CENTER_CROP,
        ScaleType.CENTER_INSIDE
    )


    init {
        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.ApkButton)
        title = attributeArray.getString(R.styleable.ApkButton_title)
        subTitle = attributeArray.getString(R.styleable.ApkButton_subTitle)
        titleTextSize =
            attributeArray.getDimensionPixelSize(R.styleable.ApkButton_titleTextSize, 14)
        subTitleTextSize =
            attributeArray.getDimensionPixelSize(R.styleable.ApkButton_subTitleTextSize, 12)
        textColor = attributeArray.getColor(
            R.styleable.ApkButton_textColor,
            ContextCompat.getColor(context, R.color.white)
        )
        backgroundColor = attributeArray.getColor(
            R.styleable.ApkButton_backgroundColor,
            ContextCompat.getColor(context, R.color.purple_700)
        )
        textStyle = attributeArray.getInt(R.styleable.ApkButton_textStyleTitle, 0)
        textStyleSubTitle = attributeArray.getInt(R.styleable.ApkButton_textStyleSubTitle, 0)
        mDrawable = attributeArray.getDrawable(R.styleable.ApkButton_icon)
        val index: Int = attributeArray.getInt(R.styleable.ApkButton_scaleType, -1)
        if (index >= 0) {
            scaleType = sScaleTypeArray.get(index)
        }

        paintClick.style = Paint.Style.FILL
        paintClick.color = ContextCompat.getColor(context, R.color.white_trans)

        attributeArray.recycle()

        labelPaint.textSize = titleTextSize.toFloat()
        textColor?.let { labelPaint.color = it }
        labelPaint.setTextAlign(Paint.Align.LEFT);
        labelPaint.setTypeface(Typeface.create(Typeface.DEFAULT, textStyle ?: 0));

        labelPaintSubtitle.textSize = subTitleTextSize.toFloat()
        textColor?.let { labelPaintSubtitle.color = it }
        labelPaintSubtitle.setTextAlign(Paint.Align.LEFT);
        labelPaintSubtitle.setTypeface(Typeface.create(Typeface.DEFAULT, textStyleSubTitle ?: 0));

        mDrawable?.setBounds(
            paddingDrawable,
            paddingDrawable,
            iconSize + paddingDrawable,
            iconSize + paddingDrawable
        )

        isClickable = true
        setOnClickListener(this)


    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec))
    }

    fun measureWidth(measureSpec: Int): Int {
        var size = paddingLeft + paddingRight
        var bounds = Rect()
        labelPaint.getTextBounds(title, 0, title?.length ?: 0, bounds)
        val titleWidth = bounds.width()
        var boundsSub = Rect()
        if (subTitle?.isEmpty() == false) {
            labelPaintSubtitle.getTextBounds(subTitle, 0, subTitle?.length ?: 0, boundsSub)
        }
        size += maxOf(titleWidth, boundsSub.width())


        size += mDrawable?.bounds?.width() ?: 0

        size += (2 * paddingDrawable)

        size += (2 * padding).toInt()

        return resolveSizeAndState(size, measureSpec, 0)
    }

    fun measureHeight(measureSpec: Int): Int {
        var size = paddingTop + paddingBottom
        var sizeText =
            labelPaint.fontSpacing.toInt() + labelPaintSubtitle.fontSpacing.toInt() + (2 * padding).toInt()
        var sizeIcon = (mDrawable?.bounds?.height() ?: 0) + (2 * paddingDrawable).toInt()
        if (mDrawable == null) {
            size += buttonHeightWithoutIcon;
        }
        size += maxOf(sizeText, sizeIcon)

        return resolveSizeAndState(size, measureSpec, 0)
    }

    override fun invalidateDrawable(dr: Drawable) {

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = backgroundColor ?: Color.GRAY
        val offset = 16
        val rectF = RectF(
            offset.toFloat(),  // left
            offset.toFloat(),  // top
            (canvas?.width ?: 0f - offset).toFloat(),  // right
            (canvas?.height ?: 0 - offset).toFloat() // bottom
        )
        val cornersRadius = 8

        canvas?.drawRoundRect(
            rectF,  // rect
            cornersRadius.toFloat(),  // rx
            cornersRadius.toFloat(),  // ry
            paint // Paint
        )

        drawFingerPrint(canvas)

        if (canvas != null) {
            mDrawable?.draw(canvas)
        }
        onDrawTitle(canvas)
        canvas?.save()
        canvas?.restore()


    }


    fun onDrawTitle(canvas: Canvas?) {
        var x: Float = 0f
        val bounds = Rect()
        labelPaint.getTextBounds(title, 0, title?.length ?: 0, bounds)
        if (mDrawable == null) {
            x = (width.toFloat() - paddingLeft.toFloat() - padding - bounds.width().toFloat()) / 2
        } else {
            x = (mDrawable?.bounds?.left ?: 0).toFloat() + (mDrawable?.bounds?.width()
                ?: 0.0f).toFloat() + paddingLeft.toFloat() + padding
        }
        var y: Float = 0f
        if (subTitle?.isEmpty() == false) {
            var labelPaintSubTitle = Paint(Paint.ANTI_ALIAS_FLAG);
            labelPaintSubTitle.textSize = subTitleTextSize.toFloat()
            labelPaintSubTitle.setTypeface(
                Typeface.create(
                    Typeface.DEFAULT,
                    textStyleSubTitle ?: 0
                )
            );
            val boundsSubTitle = Rect()
            labelPaint.getTextBounds(subTitle, 0, subTitle?.length ?: 0, boundsSubTitle)
            y = (paddingTop + bounds.height() + height - padding - boundsSubTitle.height()) / 2
        } else {
            y = (paddingTop + bounds.height() + height + padding) / 2.toFloat()
        }
        canvas?.drawText(title ?: "", x.toFloat() + padding, y, labelPaint);
        if (subTitle?.isEmpty() == false) {
            onDrawSubTitle(canvas, y + bounds.height().toFloat())
        }
    }

    fun onDrawSubTitle(canvas: Canvas?, titleBottomY: Float?) {
        val bounds = Rect()
        labelPaintSubtitle.getTextBounds(subTitle, 0, subTitle?.length ?: 0, bounds)
        var x = 0f
        if (mDrawable == null) {
            x = (width.toFloat() - paddingLeft.toFloat() - padding - bounds.width().toFloat()) / 2
        } else {
            x = (mDrawable?.bounds?.left ?: 0).toFloat() + (mDrawable?.bounds?.width()
                ?: 0.0f).toFloat() + paddingLeft.toFloat()
        }
        val y = (titleBottomY ?: 0f) + padding
        canvas?.drawText(subTitle ?: "", x.toFloat() + padding, y, labelPaintSubtitle);

    }

    private fun drawFingerPrint(canvas: Canvas?) {
        if (drawAroundFinger) {
            canvas?.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paintClick)
        }

        if (radius <= width / 2) {
            radius += 8F
            invalidate()
        } else {
            radius = 0F
            drawAroundFinger = false
            invalidate()
        }
    }

    //    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        super.onTouchEvent(event)
//        return true;
//    }
    override fun onClick(v: View?) {
        drawAroundFinger = true
        invalidate()
        listener?.onClick(v)
    }

    fun setClickListener(listener1: OnClickListener?) {
        listener = listener1
    }


}