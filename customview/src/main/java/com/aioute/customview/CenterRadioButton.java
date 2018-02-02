package com.aioute.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;

/**
 * Created by Administrator on 2016/5/7.
 */
public class CenterRadioButton extends RadioButton {

    //drawable 的宽度
    private int drawableWidth;
    // drawable 的高度
    private int drawableHeight;
    // 是否已经根据drawable的大小重新计算drawablepadding
    boolean isCalulate = false;
    // drawable 和 文字是否居中
    private boolean isCenter;

    private String TAG = "com.aioute.chargecloud";

    public CenterRadioButton(Context context) {
        super(context);
    }

    public CenterRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomDrawableTextView);
        drawableWidth = (int) a.getDimension(R.styleable.CustomDrawableTextView_drawableLeftWidth, 30);
        drawableHeight = (int) a.getDimension(R.styleable.CustomDrawableTextView_drawableLeftHeight, 30);
        isCenter = a.getBoolean(R.styleable.CustomDrawableTextView_drawableTextCenter, true);
        a.recycle();
    }

    public CenterRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isCalulate) {
            Drawable[] drawables = getCompoundDrawables();
            if (drawables != null) {
                if (drawables[0] != null) {
                    Drawable drawableLeft = drawables[0];
                    if (!isCalulate) {
                        isCalulate = true;
                        setCompoundDrawablePadding(drawableWidth - drawableLeft.getIntrinsicWidth() + getCompoundDrawablePadding());
                    }
                    float bodyWidth = getBodyWidth(drawableLeft);
                    if (isCenter)
                        setPadding((int) ((getWidth() - bodyWidth) / 2), 0, 0, 0);
                    setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    int offset = (drawableHeight - drawableLeft.getIntrinsicHeight()) / 2;
                    drawableLeft.setBounds(0, -offset, drawableWidth, drawableHeight - offset);
                } else if (drawables[2] != null) {
                    Drawable drawableRight = drawables[2];
                    if (!isCalulate) {
                        isCalulate = true;
                        setCompoundDrawablePadding(drawableWidth - drawableRight.getIntrinsicWidth() + getCompoundDrawablePadding());
                    }
                    float bodyWidth = getBodyWidth(drawableRight);
                    setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                    if (isCenter)
                        setPadding(0, 0, (int) ((getWidth() - bodyWidth) / 2), 0);
                    int offset = (drawableHeight - drawableRight.getIntrinsicHeight()) / 2;
                    drawableRight.setBounds(drawableRight.getIntrinsicWidth() - drawableWidth, -offset, drawableRight.getIntrinsicWidth(), drawableHeight - offset);
                } else if (drawables[1] != null) {
                    Drawable drawableTop = drawables[1];
                    if (!isCalulate) {
                        isCalulate = true;
                        setCompoundDrawablePadding(drawableHeight - drawableTop.getIntrinsicHeight() + getCompoundDrawablePadding());
                    }
                    float bodyHeight = getBodyHeight(drawableTop);
                    setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                    if (isCenter)
                        setPadding(0, (int) ((getHeight() - bodyHeight) / 2), 0, 0);
                    int offsetWdith = (drawableWidth - drawableTop.getIntrinsicWidth()) / 2;
                    int offsetHeight = (drawableHeight - drawableTop.getIntrinsicHeight()) / 10;
                    drawableTop.setBounds(-offsetWdith, -offsetHeight, drawableWidth - offsetWdith, drawableHeight - offsetHeight);
                } else if (drawables[3] != null) {
                    Drawable drawableBottom = drawables[3];
                    if (!isCalulate) {
                        isCalulate = true;
                        setCompoundDrawablePadding(drawableHeight - drawableBottom.getIntrinsicHeight() + getCompoundDrawablePadding());
                    }
                    float bodyHeight = getBodyHeight(drawableBottom);
                    setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                    if (isCenter)
                        setPadding(0, 0, 0, (int) ((getHeight() - bodyHeight) / 2));
                    int offsetWdith = (drawableWidth - drawableBottom.getIntrinsicWidth()) / 2;
                    drawableBottom.setBounds(-offsetWdith, drawableBottom.getIntrinsicHeight() - drawableHeight, drawableWidth - offsetWdith, drawableBottom.getIntrinsicHeight());
                } else {
                    setGravity(Gravity.CENTER);
                    setPadding(0, 0, 0, 0);
                }
            }
        }
        super.onDraw(canvas);
    }

    private float getBodyWidth(Drawable drawable) {
        float textWidth = measureWidth();
        int drawablePadding = getCompoundDrawablePadding();
        int drawableWidth = drawable.getIntrinsicWidth();
        return textWidth + drawableWidth + drawablePadding;
    }

    private float getBodyHeight(Drawable drawable) {
        float textHeight = measureHeight();
        int drawablePadding = getCompoundDrawablePadding();
        int drawableHeight = drawable.getIntrinsicHeight();
        return textHeight + drawableHeight + drawablePadding;
    }

    private float measureWidth() {
        Paint paint = new Paint();
        paint.setTextSize(getTextSize());
        if (!TextUtils.isEmpty(getText())) {
            return paint.measureText(getText().toString());
        } else {
            return 0;
        }
    }

    private float measureHeight() {
        Paint paint = new Paint();
        paint.setTextSize(getTextSize());
        if (!TextUtils.isEmpty(getText())) {
            return paint.measureText(getText().toString().substring(0, 1));
        } else {
            return 0;
        }
    }

}
