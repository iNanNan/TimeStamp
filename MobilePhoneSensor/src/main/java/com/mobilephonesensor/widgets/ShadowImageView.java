package com.mobilephonesensor.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.mobilephonesensor.R;

/**
 * Created by heng on 16-6-1.
 */
public class ShadowImageView extends ImageView {

    private static final int DEFAULT_SHADOW_START_COLOR = 0x4D000000;

    private static final int DEFAULT_SHADOW_END_COLOR = 0x00FFFFFF;

    private boolean isOval;

    private int mCornerRadius;

    private int mCornerTLRadius;

    private int mCornerTRRadius;

    private int mCornerBLRadius;

    private int mCornerBRRadius;

    private int mShadowWidth;

    private int mShadowStartColor;

    private int mShadowEndColor;

    private Path mRadiusPath;

    private Path mShadowPath;

    private Paint mPaint;

    public int getShadowWidth() {
        return mShadowWidth;
    }

    public void setShadowWidth(int mShadowWidth) {
        this.mShadowWidth = mShadowWidth;
    }

    public int getShadowStartColor() {
        return mShadowStartColor;
    }

    public void setShadowStartColor(int mShadowStartColor) {
        this.mShadowStartColor = mShadowStartColor;
    }

    public int getShadowEndColor() {
        return mShadowEndColor;
    }

    public void setShadowEndColor(int mShadowEndColor) {
        this.mShadowEndColor = mShadowEndColor;
    }
    public ShadowImageView(Context context) {
        this(context, null);
    }

    public ShadowImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShadowImageView);

        mCornerRadius = ta.getDimensionPixelOffset(R.styleable.ShadowImageView_siv_cornerRadius, 0);
        mCornerTLRadius = ta.getDimensionPixelOffset(R.styleable.ShadowImageView_siv_cornerTLRadius, mCornerRadius);
        mCornerTRRadius = ta.getDimensionPixelOffset(R.styleable.ShadowImageView_siv_cornerTRRadius, mCornerRadius);
        mCornerBLRadius = ta.getDimensionPixelOffset(R.styleable.ShadowImageView_siv_cornerBLRadius, mCornerRadius);
        mCornerBRRadius = ta.getDimensionPixelOffset(R.styleable.ShadowImageView_siv_cornerBRRadius, mCornerRadius);

        mShadowWidth = ta.getDimensionPixelOffset(R.styleable.ShadowImageView_siv_shadowWidth, 0);
        mShadowStartColor = ta.getColor(R.styleable.ShadowImageView_siv_shadowStartColor, DEFAULT_SHADOW_START_COLOR);
        mShadowEndColor = ta.getColor(R.styleable.ShadowImageView_siv_shadowEndColor, DEFAULT_SHADOW_END_COLOR);
        ta.recycle();

        mRadiusPath = new Path();
        mShadowPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        resetDrawableSizeAndLocation();
        super.onDraw(canvas);
        final Rect bounds = getDrawable().getBounds();
        setMaxRadius();
        drawRadius(canvas, bounds);
        drawShadow(canvas, bounds);
    }

    private void resetDrawableSizeAndLocation() {
        if (mShadowWidth <= 0) {
            return;
        }
        final Rect tempRect = getDrawable().copyBounds();
        getDrawable().getBounds().set(
                tempRect.left + mShadowWidth,
                tempRect.top + mShadowWidth,
                tempRect.right - mShadowWidth,
                tempRect.bottom - mShadowWidth);
    }

    private void drawRadius(Canvas canvas, Rect rect) {

        final int top = rect.top;
        final int left = rect.left;
        final int right = rect.right;
        final int bottom = rect.bottom;

        mPaint.setColor(Color.WHITE);

        if (mCornerTLRadius > 0) {
            mRadiusPath.reset();
            mRadiusPath.moveTo(left, top);
            mRadiusPath.lineTo(left + mCornerTLRadius, top);
            mRadiusPath.quadTo(left, top, left, top + mCornerTLRadius);
            mRadiusPath.lineTo(left, top + mCornerTLRadius);
            mRadiusPath.lineTo(left, top);
            mRadiusPath.close();
            canvas.save();
            canvas.drawPath(mRadiusPath, mPaint);
            canvas.restore();
        }

        if (mCornerTRRadius > 0) {
            mRadiusPath.reset();
            mRadiusPath.moveTo(right, top);
            mRadiusPath.lineTo(right, top + mCornerTRRadius);
            mRadiusPath.quadTo(right, top, right - mCornerTRRadius, top);
            mRadiusPath.lineTo(right - mCornerTRRadius, top);
            mRadiusPath.lineTo(right, top);
            mRadiusPath.close();
            canvas.save();
            canvas.drawPath(mRadiusPath, mPaint);
            canvas.restore();
        }

        if (mCornerBLRadius > 0) {
            mRadiusPath.reset();
            mRadiusPath.moveTo(left, bottom);
            mRadiusPath.lineTo(left, bottom - mCornerBLRadius);
            mRadiusPath.quadTo(left, bottom, left + mCornerBLRadius, bottom);
            mRadiusPath.lineTo(left + mCornerBLRadius, bottom);
            mRadiusPath.lineTo(left, bottom);
            mRadiusPath.close();
            canvas.save();
            canvas.drawPath(mRadiusPath, mPaint);
            canvas.restore();
        }

        if (mCornerBRRadius > 0) {
            mRadiusPath.reset();
            mRadiusPath.moveTo(right, bottom);
            mRadiusPath.lineTo(right, bottom - mCornerBRRadius);
            mRadiusPath.quadTo(right, bottom, right - mCornerBRRadius, bottom);
            mRadiusPath.lineTo(right - mCornerBRRadius, bottom);
            mRadiusPath.lineTo(right, bottom);
            mRadiusPath.close();
            canvas.save();
            canvas.drawPath(mRadiusPath, mPaint);
            canvas.restore();
        }

    }

    private void setMaxRadius() {
        final int max = getMaxRadii();
        if (mCornerTLRadius > max) {
            mCornerTLRadius = max;
        }

        if (mCornerTRRadius > max) {
            mCornerTRRadius = max;
        }

        if (mCornerBLRadius > max) {
            mCornerBLRadius = max;
        }

        if (mCornerBRRadius > max) {
            mCornerBRRadius = max;
        }
    }

    private final int getMaxRadii() {
        final int r = getWidth() / 2;
        final int r2 = getHeight() / 2;
        return r < r2 ? r : r2;
    }

    private void drawShadow(Canvas canvas, Rect rect) {

        if (mShadowWidth <= 0) {
            return;
        }

        final int top = rect.top;
        final int left = rect.left;
        final int right = rect.right;
        final int bottom = rect.bottom;
        drawTopShadow(top, left, right, canvas);
        drawLeftShadow(top, left, bottom, canvas);
        drawRightShadow(top, right, bottom, canvas);
        drawBottomShadow(left, right, bottom, canvas);
    }

    private void drawTopShadow(int top, int left, int right, Canvas canvas) {
        final int showTop = top - mShadowWidth;
        final int showLeft = left + mCornerTLRadius;
        final int showRight = right - mCornerTRRadius;
        final int showBottom = top;
        drawLinearShadow(showTop, showLeft, showRight, showBottom, GradientDrawable.Orientation.BOTTOM_TOP, canvas);
    }

    private void drawLeftShadow(int top, int left, int bottom, Canvas canvas) {
        final int showTop = top + mCornerTLRadius;
        final int showLeft = left - mShadowWidth;
        final int showRight = left;
        final int showBottom = bottom - mCornerBLRadius;
        drawLinearShadow(showTop, showLeft, showRight, showBottom, GradientDrawable.Orientation.RIGHT_LEFT, canvas);
    }

    private void drawRightShadow(int top, int right, int bottom, Canvas canvas) {
        final int showTop = top + mCornerTRRadius;
        final int showLeft = right;
        final int showRight = right + mShadowWidth;
        final int showBottom = bottom - mCornerBRRadius;
        drawLinearShadow(showTop, showLeft, showRight, showBottom, GradientDrawable.Orientation.LEFT_RIGHT, canvas);
    }

    private void drawBottomShadow(int left, int right, int bottom, Canvas canvas) {
        final int showTop = bottom;
        final int showLeft = left + mCornerBLRadius;
        final int showRight = right - mCornerBRRadius;
        final int showBottom = bottom + mShadowWidth;
        drawLinearShadow(showTop, showLeft, showRight, showBottom, GradientDrawable.Orientation.TOP_BOTTOM, canvas);
    }

    private void drawLinearShadow(int top, int left, int right, int bottom, GradientDrawable.Orientation orientation, Canvas canvas) {

        if ((left == right) || (top == bottom)) {
            return;
        }

        LinearGradient gradient;
        mShadowPath.reset();
        switch (orientation) {
            case BOTTOM_TOP:
                mShadowPath.moveTo(mCornerTLRadius > 0 ? left : left - mShadowWidth, top);
                mShadowPath.lineTo(mCornerTRRadius > 0 ? right : right + mShadowWidth, top);
                mShadowPath.lineTo(right, bottom);
                mShadowPath.lineTo(left, bottom);
                gradient = new LinearGradient(left, bottom, left, top, mShadowStartColor, mShadowEndColor, Shader.TileMode.CLAMP);
                break;
            case RIGHT_LEFT:
                mShadowPath.moveTo(left, mCornerTLRadius > 0 ? top : top - mShadowWidth);
                mShadowPath.lineTo(left, mCornerBLRadius > 0 ? bottom : bottom + mShadowWidth);
                mShadowPath.lineTo(right, bottom);
                mShadowPath.lineTo(right, top);
                gradient = new LinearGradient(right, top, left, top, mShadowStartColor, mShadowEndColor, Shader.TileMode.CLAMP);
                break;
            case LEFT_RIGHT:
                mShadowPath.moveTo(right, mCornerTRRadius > 0 ? top : top - mShadowWidth);
                mShadowPath.lineTo(right, mCornerBRRadius > 0 ? bottom : bottom + mShadowWidth);
                mShadowPath.lineTo(left, bottom);
                mShadowPath.lineTo(left, top);
                gradient = new LinearGradient(left, top, right, top, mShadowStartColor, mShadowEndColor, Shader.TileMode.CLAMP);
                break;
            case TOP_BOTTOM:
                mShadowPath.moveTo(mCornerBLRadius > 0 ? left : left - mShadowWidth, bottom);
                mShadowPath.lineTo(mCornerBRRadius > 0 ? right : right + mShadowWidth, bottom);
                mShadowPath.lineTo(right, top);
                mShadowPath.lineTo(left, top);
                gradient = new LinearGradient(left, top, left, bottom, mShadowStartColor, mShadowEndColor, Shader.TileMode.CLAMP);
                break;
            default:
                gradient = null;
        }
        mShadowPath.close();
        canvas.save();
        mPaint.setShader(gradient);
        canvas.translate(getPaddingLeft(), getPaddingTop());
        canvas.drawPath(mShadowPath, mPaint);
        canvas.restore();
        mPaint.setShader(null);
    }

    private void drawTLShadow() {
        if (mCornerTLRadius <= 0) {
            return;
        }
    }

    private void drawTRShadow() {
        if (mCornerTRRadius <= 0) {
            return;
        }
    }

    private void drawBLShadow() {
        if (mCornerBLRadius <= 0) {
            return;
        }
    }

    private void drawBRShadow() {
        if (mCornerBRRadius <= 0) {
            return;
        }
    }

}
