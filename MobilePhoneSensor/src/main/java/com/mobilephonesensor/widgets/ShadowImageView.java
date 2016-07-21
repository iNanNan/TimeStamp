package com.mobilephonesensor.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.mobilephonesensor.R;

/**
 * Created by heng on 16-6-1.
 */
public class ShadowImageView extends ImageView {

    public enum Shape {
        RECT(0),

        OVAL(1),

        CIRCLE(2);

        final int value;

        Shape(int value) {
            this.value = value;
        }

        public static Shape fromValue(int shape) {
            Shape result;
            switch (shape) {
                case 1:
                    result = Shape.OVAL;
                    break;
                case 2:
                    result = Shape.CIRCLE;
                    break;
                default:
                    result = Shape.RECT;
            }
            return result;
        }
    }

    private static final int DEFAULT_SHADOW_START_COLOR = 0x4D000000;

    private static final int DEFAULT_SHADOW_END_COLOR = 0x00FFFFFF;

    /**
     * View的形状
     *
     * @see Shape
     */
    public Shape mShape;

    /* 圆角半径 */
    private int mCornerRadius;
    /* 左上角半径 */
    private int mCornerTLRadius;
    /* 右上角半径 */
    private int mCornerTRRadius;
    /* 左下角半径 */
    private int mCornerBLRadius;
    /* 右下角半径 */
    private int mCornerBRRadius;

    /* 边框宽度 */
    private int mBorderWidth;
    /* 边框颜色 */
    private int mBorderColor;
    /* 阴影宽度 */
    private int mShadowWidth;
    /* 阴影开始颜色 */
    private int mShadowStartColor;
    /* 阴影结束颜色 */
    private int mShadowEndColor;
    /* 绘制阴影的路径 */
    private Path mShadowPath;

    private Paint mPaint;
    /* 最后显示在控件上的资源 */
    private Drawable mDrawable;
    /* 显示的类型 */
    private ScaleType mScaleType = ScaleType.MATRIX;

    private int mRes = 0;

    public Shape getShape() {
        return mShape;
    }

    public int getCornerRadius() {
        return mCornerRadius;
    }

    public void setCornerRadius(int mCornerRadius) {
        this.mCornerRadius = mCornerRadius;
        invalidate();
    }

    public int getCornerTLRadius() {
        return mCornerTLRadius;
    }

    public void setCornerTLRadius(int mCornerTLRadius) {
        this.mCornerTLRadius = mCornerTLRadius;
        invalidate();
    }

    public int getCornerTRRadius() {
        return mCornerTRRadius;
    }

    public void setCornerTRRadius(int mCornerTRRadius) {
        this.mCornerTRRadius = mCornerTRRadius;
        invalidate();
    }

    public int getCornerBLRadius() {
        return mCornerBLRadius;
    }

    public void setCornerBLRadius(int mCornerBLRadius) {
        this.mCornerBLRadius = mCornerBLRadius;
        invalidate();
    }

    public int getCornerBRRadius() {
        return mCornerBRRadius;
    }

    public void setCornerBRRadius(int mCornerBRRadius) {
        this.mCornerBRRadius = mCornerBRRadius;
        invalidate();
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int mBorderWidth) {
        this.mBorderWidth = mBorderWidth;
        invalidate();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int mBorderColor) {
        this.mBorderColor = mBorderColor;
        invalidate();
    }

    public int getShadowWidth() {
        return mShadowWidth;
    }

    public void setShadowWidth(int mShadowWidth) {
        this.mShadowWidth = mShadowWidth;
        invalidate();
    }

    public int getShadowStartColor() {
        return mShadowStartColor;
    }

    public void setShadowStartColor(int mShadowStartColor) {
        this.mShadowStartColor = mShadowStartColor;
        invalidate();
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
        final int shape = ta.getInt(R.styleable.ShadowImageView_siv_shape, Shape.RECT.value);
        mShape = Shape.fromValue(shape);
        mCornerRadius = ta.getDimensionPixelOffset(R.styleable.ShadowImageView_siv_cornerRadius, 0);
        mCornerTLRadius = ta.getDimensionPixelOffset(R.styleable.ShadowImageView_siv_cornerTLRadius, mCornerRadius);
        mCornerTRRadius = ta.getDimensionPixelOffset(R.styleable.ShadowImageView_siv_cornerTRRadius, mCornerRadius);
        mCornerBLRadius = ta.getDimensionPixelOffset(R.styleable.ShadowImageView_siv_cornerBLRadius, mCornerRadius);
        mCornerBRRadius = ta.getDimensionPixelOffset(R.styleable.ShadowImageView_siv_cornerBRRadius, mCornerRadius);

        mBorderWidth = ta.getDimensionPixelOffset(R.styleable.ShadowImageView_siv_borderWidth, 0);
        mBorderColor = ta.getColor(R.styleable.ShadowImageView_siv_borderColor, DEFAULT_SHADOW_END_COLOR);

        mShadowWidth = ta.getDimensionPixelOffset(R.styleable.ShadowImageView_siv_shadowWidth, 0);
        mShadowStartColor = ta.getColor(R.styleable.ShadowImageView_siv_shadowStartColor, DEFAULT_SHADOW_START_COLOR);
        mShadowEndColor = ta.getColor(R.styleable.ShadowImageView_siv_shadowEndColor, DEFAULT_SHADOW_END_COLOR);
        ta.recycle();

        mShadowPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        updateCornerDrawable();
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(scaleType);
        if (scaleType != null && mScaleType != scaleType) {
            updateCornerDrawable();
        }
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setImageDrawable(getDrawable());
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        setImageDrawable(CornerDrawable.obtainCornerDrawable(bm, getResources()));
    }

    @Override
    public void setImageResource(int resId) {
        setImageDrawable(resolveResource(resId));
    }

    /**
     * 为了处理圆角，所有设置的图片资源最终都统一
     * 在这里处理成CornerDrawable
     *
     * @param drawable
     */
    @Override
    public void setImageDrawable(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        if (drawable instanceof CornerDrawable) {
            mDrawable = drawable;
        } else {
            mDrawable = CornerDrawable.obtainCornerDrawable(drawable2Bitmap(drawable), getResources());
        }
        super.setImageDrawable(mDrawable);
        updateCornerDrawable();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /* ***********************************
         * 这里并没有调用super.onDraw方法所以此控
         * 件对android:cropToPadding属性是无效的
         * */
        if (mDrawable == null) {
            return;
        }
        //设置padding(由于没有重写super.onDraw所以padding需要单独处理)
        ((CornerDrawable) mDrawable).setPadding(
                getPaddingLeft() + mShadowWidth,
                getPaddingTop() + mShadowWidth,
                getPaddingRight() + mShadowWidth,
                getPaddingBottom() + mShadowWidth);
        mDrawable.draw(canvas);
        drawShadow(canvas, ((CornerDrawable) mDrawable).getRealRect());
    }

    private void updateCornerDrawable() {
        if (mDrawable == null) {
            return;
        }
        this.mScaleType = getScaleType();
        ((CornerDrawable) mDrawable)
                .updateValues(mCornerTLRadius
                        , mCornerTRRadius
                        , mCornerBRRadius
                        , mCornerBLRadius
                        , mBorderWidth
                        , mBorderColor
                        , mScaleType
                        , mShape);
    }

    /**
     * 开始绘制阴影
     *
     * @param canvas
     * @param rect
     */
    private void drawShadow(Canvas canvas, Rect rect) {

        if (mShadowWidth <= 0) {
            return;
        }

        final int top = rect.top;
        final int left = rect.left;
        final int right = rect.right;
        final int bottom = rect.bottom;
        mPaint.setStyle(Paint.Style.FILL);
        drawTopShadow(top, left, right, canvas);
        drawLeftShadow(top, left, bottom, canvas);
        drawRightShadow(top, right, bottom, canvas);
        drawBottomShadow(left, right, bottom, canvas);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        mShadowColorGroup = makeGradientGroup(mShadowStartColor, mShadowEndColor, mShadowWidth, new int[mShadowWidth]);
        drawTLShadow(canvas, top, left);
        drawTRShadow(canvas, top, right);
        drawBRShadow(canvas, right, bottom);
        drawBLShadow(canvas, left, bottom);
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

    /**
     * 主要绘制线性阴影
     *
     * @param top         阴影起点Y坐标
     * @param left        起点X坐标
     * @param right       终点X坐标
     * @param bottom      终点Y坐标
     * @param orientation 阴影的渐变方向 (参考 @see android.graphics.drawable.GradientDrawable.Orientation)
     * @param canvas
     */
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
        canvas.drawPath(mShadowPath, mPaint);
        canvas.restore();
        mPaint.setShader(null);
    }

    private RectF mTLRect;

    private void drawTLShadow(Canvas canvas, int top, int left) {
        if (mCornerTLRadius <= 0) {
            return;
        }
        final float diameter = mCornerTLRadius * 2;
        final float oLeft = left - mShadowWidth / 2;
        final float oTop = top - mShadowWidth / 2;
        final float oRight = diameter + oLeft;
        final float oBottom = diameter + top;
        if (mTLRect == null) {
            mTLRect = new RectF(oLeft, oTop, oRight, oBottom);
        }
        drawArcShadow(canvas, mTLRect, 0);
    }

    private RectF mTRRect;

    private void drawTRShadow(Canvas canvas, int top, int right) {
        if (mCornerTRRadius <= 0) {
            return;
        }
        final float diameter = (mCornerTRRadius + mShadowWidth / 2) * 2;
        final float oTop = top - mShadowWidth / 2;
        final float oRight = right + mShadowWidth / 2;
        final float oBottom = diameter + oTop;
        final float oLeft = oRight - diameter;
        if (mTRRect == null) {
            mTRRect = new RectF(oLeft, oTop, oRight, oBottom);
        }
        drawArcShadow(canvas, mTRRect, 1);
    }

    private RectF mBRRect;

    private void drawBRShadow(Canvas canvas, int right, int bottom) {
        if (mCornerBRRadius <= 0) {
            return;
        }
        final float diameter = (mCornerBRRadius + mShadowWidth / 2) * 2;
        final float oRight = right + mShadowWidth / 2;
        final float oBottom = bottom + mShadowWidth / 2;
        final float oLeft = oRight - diameter;
        final float oTop = oBottom - diameter;
        if (mBRRect == null) {
            mBRRect = new RectF(oLeft, oTop, oRight, oBottom);
        }
        drawArcShadow(canvas, mBRRect, 2);
    }

    private RectF mBLRect;

    private void drawBLShadow(Canvas canvas, int left, int bottom) {
        if (mCornerBLRadius <= 0) {
            return;
        }
        final float diameter = (mCornerBLRadius + mShadowWidth / 2) * 2;
        final float oLeft = left - mShadowWidth / 2;
        final float oBottom = bottom + mShadowWidth / 2;
        final float oTop = oBottom - diameter;
        final float oRight = oLeft + diameter;
        if (mBLRect == null) {
            mBLRect = new RectF(oLeft, oTop, oRight, oBottom);
        }
        drawArcShadow(canvas, mBLRect, 3);
    }

    private void drawArcShadow(Canvas canvas, RectF oval, int orientation) {
        final float top = oval.top + mShadowWidth / 2;
        final float left = oval.left + mShadowWidth / 2;
        final float right = oval.right - mShadowWidth / 2;
        final float bottom = oval.bottom - mShadowWidth / 2;
        float start = 0;
        final float sweep = 90;
        switch (orientation) {
            case 0://左上
                start = 180;
                break;
            case 1://右上
                start = 270;
                break;
            case 2://右下
                start = 0;
                break;
            case 3://左下
                start = 90;
                break;
        }
        canvas.save();
        int[] group = makeGradientGroup(mShadowStartColor, mShadowEndColor, mShadowWidth, new int[mShadowWidth]);
        for (int i = 1; i < mShadowWidth + 1; i++) {
            mPaint.setColor(group[i - 1]);
            final RectF rectF = new RectF(left - i, top - i, right + i, bottom + i);
            canvas.drawArc(rectF, start, sweep, false, mPaint);
        }
        canvas.restore();
    }

    private void drawShadowLayer(Canvas canvas) {
        float[] radii = new float[]{mCornerTLRadius, mCornerTLRadius
                , mCornerTRRadius, mCornerTRRadius
                , mCornerBRRadius, mCornerBRRadius
                , mCornerBLRadius, mCornerBLRadius};
        final RectF rf = ((CornerDrawable) mDrawable).getRealRectF();
        RectF copyF = new RectF(rf.left, rf.top, rf.right, rf.bottom);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        final int[] group = makeGradientGroup(mShadowStartColor, mShadowEndColor, mShadowWidth, new int[mShadowWidth]);
        for (int i = 0; i < mShadowWidth; i++) {
            mShadowPath.reset();
            mPaint.setColor(group[i]);
//            mShadowPath.addRoundRect(copyF, radii, Path.Direction.CW);
//            canvas.drawPath(mShadowPath, mPaint);
            mShadowPath.close();

            copyF.set(copyF.left - i, copyF.top - i, copyF.right + i, copyF.bottom + i);

            radii[0] = radii[1] = radii[0] + i;
            radii[2] = radii[3] = radii[2] + i;
            radii[4] = radii[5] = radii[4] + i;
            radii[6] = radii[7] = radii[6] + i;
        }
    }

    private int[] mShadowColorGroup;

    private int[] makeGradientGroup(int from, int to, int n, int[] group) {
        int r = getR32(from) << 23;
        int g = getG32(from) << 23;
        int b = getB32(from) << 23;
        int a = getA32(from) << 23;
        int dr = ((getR32(to) << 23) - r) / (n - 1);
        int dg = ((getG32(to) << 23) - g) / (n - 1);
        int db = ((getB32(to) << 23) - b) / (n - 1);
        int da = ((getA32(to) << 23) - a) / (n - 1);

        for (int i = 0; i < n; i++) {
            final int newColor = newColorInt(r >> 23, g >> 23, b >> 23, a >> 23);
            group[i] = newColor;
            r += dr;
            g += dg;
            b += db;
            a += da;
        }

        return group;
    }

    // 访问从进行颜色的红色分量
    private  int getR32(int c) {
        return (c >>  0) & 0xFF;
    }

    private  int getG32(int c) {
        return (c >>  8) & 0xFF;
    }

    private  int getB32(int c) {
        return (c >> 16) & 0xFF;
    }

    private  int getA32(int c) {
        return (c >> 24) & 0xFF;
    }

    private int newColorInt(int r, int g, int b, int a) {
        return (r << 0) | ( g << 8) | (b << 16) | (a << 24);
    }

    private Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap;
        int width = Math.max(drawable.getIntrinsicWidth(), 2);
        int height = Math.max(drawable.getIntrinsicHeight(), 2);
        try {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }

    private Drawable resolveResource(int resId) {
        if (mRes != resId) {
            mRes = resId;
        }

        if (mRes == 0) {
            return null;
        }

        Drawable d = null;

        try {
            Drawable temp = getResources().getDrawable(mRes);
            d = CornerDrawable.obtainCornerDrawable(drawable2Bitmap(temp), getResources());
        } catch (Resources.NotFoundException e) {
            //
        }
        return d;
    }

    /**
     * 自定义Drawable实现圆角及边框的处理
     */
    static class CornerDrawable extends Drawable {

        private float[] mRadiusRadii = new float[]{0, 0, 0, 0, 0, 0, 0, 0};

        private float[] mBorderRadii = new float[]{0, 0, 0, 0, 0, 0, 0, 0};

        private Shape mShape;

        private ScaleType mScaleType = null;

        /* 绘制图片的边界 */
        private final RectF mFillBounds = new RectF();
        /* 绘制边框的边界 */
        private final RectF mBorderBounds = new RectF();

        private Path mPath;

        private Paint mFillPaint;

        private Paint mBorderPaint;

        private Bitmap mBitmap;

        private BitmapShader mShader;

        private int mBitmapWidth;

        private int mBitmapHeight;

        private int mPaddingTop = 0;

        private int mPaddingLeft = 0;

        private int mPaddingRight = 0;

        private int mPaddingBottom = 0;

        private int mBorderWidth = 0;

        private int mBorderColor = 0;

        public static Drawable obtainCornerDrawable(Bitmap bitmap, Resources r) {
            if (bitmap == null)
                return null;
            else
                return new CornerDrawable(bitmap, r);
        }

        public void updateValues(int rTL, int rTR, int rBR, int rBL, int borderWidth, int borderColor, ScaleType scaleType, Shape shape) {
            mShape = shape;
            mScaleType = scaleType;
            mRadiusRadii[0] = mRadiusRadii[1] = rTL;
            mRadiusRadii[2] = mRadiusRadii[3] = rTR;
            mRadiusRadii[4] = mRadiusRadii[5] = rBR;
            mRadiusRadii[6] = mRadiusRadii[7] = rBL;
            if (borderWidth > 0) {
                mBorderWidth = borderWidth;
                mBorderColor = borderColor;
                for (int i = 0; i < 8; i++) {
                    mBorderRadii[i] = mRadiusRadii[i];
                    mRadiusRadii[i] = mBorderRadii[i] - mBorderWidth;
                }
                setBorderInternal(mBorderWidth, mBorderColor, 0, 0, false);
            }
            invalidateSelf();
        }

        public void setBorder(int width, int color, float dashWidth, float dashGap) {
            setBorderInternal(width, color, dashWidth, dashGap, true);
        }

        public void setPadding(int left, int top, int right, int bottom) {
            this.mPaddingLeft = left;
            this.mPaddingTop = top;
            this.mPaddingRight = right;
            this.mPaddingBottom = bottom;
        }

        public CornerDrawable(Bitmap bitmap, Resources r) {
            if (bitmap == null) {
                mBitmapWidth = mBitmapHeight = -1;
                return;
            }
            this.mBitmap = bitmap;
            if (r != null) {
                mBitmapWidth = bitmap.getScaledWidth(r.getDisplayMetrics());
                mBitmapHeight = bitmap.getScaledHeight(r.getDisplayMetrics());
            } else {
                mBitmapWidth = bitmap.getWidth();
                mBitmapHeight = bitmap.getHeight();
            }
            this.mPath = new Path();
            this.mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            this.mFillPaint.setStyle(Paint.Style.FILL);
        }

        @Override
        public void draw(Canvas canvas) {
            if (mBitmapWidth == -1 || mBitmapHeight == -1) {
                return;
            }
            configureImageBounds(canvas);
            canvas.save();

            final boolean isBorder = mBorderPaint != null && mBorderPaint.getAlpha() > 0 && mBorderPaint.getStrokeWidth() > 0;

            mPath.reset();
            if (isBorder) {
                adjustFillBounds();
            }
            switch (mShape) {
                case OVAL://oval
                    canvas.drawOval(mFillBounds, mFillPaint);
                    if (isBorder) {
                        canvas.drawOval(mFillBounds, mBorderPaint);
                    }
                    break;
                case CIRCLE://circle
                    break;
                default://rect
                    mPath.addRoundRect(mFillBounds, mRadiusRadii, Path.Direction.CW);
                    canvas.drawPath(mPath, mFillPaint);
                    if (isBorder) {
                        mPath.reset();
                        mPath.addRoundRect(mFillBounds, mBorderRadii, Path.Direction.CW);
                        canvas.drawPath(mPath, mBorderPaint);
                    }
            }
            mPath.close();
            canvas.restore();
        }

        @Override
        public void setAlpha(int alpha) {
            mFillPaint.setAlpha(alpha);
            invalidateSelf();
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
            mFillPaint.setColorFilter(colorFilter);
            invalidateSelf();
        }

        @Override
        public void setFilterBitmap(boolean filter) {
            mFillPaint.setFilterBitmap(filter);
            invalidateSelf();
        }

        @Override
        public int getIntrinsicWidth() {
            final int superSize = super.getIntrinsicWidth();
            if (superSize <= 0) {
                return mBitmapWidth;
            }
            return superSize;
        }

        @Override
        public int getIntrinsicHeight() {
            final int superSize = super.getIntrinsicHeight();
            if (superSize <= 0) {
                return mBitmapHeight;
            }
            return superSize;
        }

        @Override
        public int getOpacity() {
            return mFillPaint.getAlpha() > 0 ? PixelFormat.TRANSLUCENT : PixelFormat.OPAQUE;
        }

        public Rect getRealRect() {
            if (mBorderWidth <= 0)
                return new Rect((int) mFillBounds.left, (int)mFillBounds.top, (int) mFillBounds.right, (int) mFillBounds.bottom);
            else
                return new Rect((int) mBorderBounds.left, (int)mBorderBounds.top, (int) mBorderBounds.right, (int) mBorderBounds.bottom);
        }

        public RectF getRealRectF() {
            if (mBorderWidth <= 0)
                return mFillBounds;
            else
                return mBorderBounds;
        }

        /**
         * 这里对部分朋友可能不好理解！噢噢~~
         * 想知道为什么去参考ImageView的ScaleType配置
         * 详情在ImageView源码configureBounds的方法中
         *
         * @param canvas 这里不同于ImageView的地方在于小于控件的图片都
         *               进行了放大，扩大到视图的边界，不然圆角没有任何意义
         * @see android.widget.ImageView.ScaleType
         */
        private void configureImageBounds(Canvas canvas) {
            final Rect canvasBounds = canvas.getClipBounds();
            final int cTop = canvasBounds.top;
            final int cLeft = canvasBounds.left;
            final int cRight = canvasBounds.right;
            final int cBottom = canvasBounds.bottom;
            final int canvasW = cRight - cLeft;
            final int canvasH = cBottom - cTop;
            mFillBounds.set(cLeft + mPaddingLeft + mBorderWidth
                    , cTop + mPaddingTop + mBorderWidth
                    , cRight - mPaddingRight - mBorderWidth
                    , cBottom - mPaddingBottom - mBorderWidth);
            boolean fits = (mBitmapWidth < 0 || canvasW == mBitmapWidth) &&
                    (mBitmapHeight < 0 || canvasH == mBitmapHeight);
            boolean crop = mBitmapWidth > canvasW && mBitmapHeight > canvasH;
            Bitmap temp = null;
            if (fits) {//原图和ImageView一样大不需要做任何处理
                temp = mBitmap;
            } else if (ScaleType.MATRIX == mScaleType ||
                    ScaleType.FIT_START == mScaleType) {
                    /* 从ImageView的左上角开始绘制原图，
                    原图超过ImageView的部分作裁剪处理，
                    如果原图小于ImageView等比例放大 */
                if (!crop) {
                    float scale;
                    if (mBitmapWidth * canvasH > mBitmapHeight * canvasW) {//高比较接近画布边缘
                        scale = (float) canvasH / (float) mBitmapHeight;
                    } else {
                        scale = (float) canvasW / (float) mBitmapWidth;
                    }
                    temp = scaleBitmap((int) (scale * mBitmapWidth), (int) (scale * mBitmapHeight));
                }
            } else if (ScaleType.CENTER == mScaleType ||
                    ScaleType.FIT_CENTER == mScaleType ||
                    ScaleType.CENTER_CROP == mScaleType) {
                    /* 将原图中心对准ImageView的中心，
                    超出部分作裁剪处理，如果原图小于ImageView
                    等比例放大原图 */
                if (crop) {
                    temp = cropBitmap(canvasW, canvasH, 1);
                } else {
                    float scale;
                    if (mBitmapWidth * canvasH > mBitmapHeight * canvasW) {//高比较接近画布边缘
                        scale = (float) canvasH / (float) mBitmapHeight;
                    } else {
                        scale = (float) canvasW / (float) mBitmapWidth;
                    }
                    final int scaleW = (int) (scale * mBitmapWidth);
                    final int scaleH = (int) (scale * mBitmapHeight);
                    if (mBitmapWidth * canvasH == mBitmapHeight * canvasW) {
                        temp = scaleBitmap(scaleW, scaleH);
                    } else {
                        temp = cropBitmap(scaleBitmap(scaleW, scaleH), canvasW, canvasH, 1);
                    }
                }
            } else if (ScaleType.CENTER_INSIDE == mScaleType ||
                    ScaleType.FIT_XY == mScaleType) {
                    /* 通过拉伸或缩小完全显示原图到ImageView上 */
                temp = scaleBitmap(canvasW, canvasH);
            } else if (ScaleType.FIT_END == mScaleType) {
                    /* 从ImageView的右下角开始绘制原图，
                    原图超过ImageView的部分作裁剪处理，
                    如果原图小于ImageView等比例放大 */
                if (crop) {
                    temp = cropBitmap(canvasW, canvasH, 2);
                } else {
                    float scale;
                    if (mBitmapWidth * canvasH > mBitmapHeight * canvasW) {//高比较接近画布边缘
                        scale = (float) canvasH / (float) mBitmapHeight;
                    } else {
                        scale = (float) canvasW / (float) mBitmapWidth;
                    }
                    final int scaleW = (int) (scale * mBitmapWidth);
                    final int scaleH = (int) (scale * mBitmapHeight);
                    if (mBitmapWidth * canvasH == mBitmapHeight * canvasW) {
                        temp = scaleBitmap(scaleW, scaleH);
                    } else {
                        temp = cropBitmap(scaleBitmap(scaleW, scaleH), canvasW, canvasH, 2);
                    }
                }
            } else {
                //unknown
                temp = mBitmap;
            }
            mShader = new BitmapShader(temp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mFillPaint.setShader(mShader);
        }

        private void adjustFillBounds() {

        }

        private void setBorderInternal(int width, int color, float dashWidth, float dashGap, boolean invalidate) {
            if (mBorderPaint == null) {
                mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mBorderPaint.setStyle(Paint.Style.STROKE);
            }
            mBorderPaint.setStrokeWidth(width);
            mBorderPaint.setColor(color);

            DashPathEffect e = null;
            if (dashWidth > 0) {
                e = new DashPathEffect(new float[]{dashWidth, dashGap}, 0);
            }
            mBorderPaint.setPathEffect(e);
            if (invalidate) {
                invalidateSelf();
            }
        }

        /**
         * 裁剪位图
         *
         * @param src
         * @param newWidth
         * @param newHeight
         * @param type      三种模式：0裁剪右下角新图取左上角，1裁剪边缘新图取中间，2裁剪左下角新图取右上角
         * @return
         */
        private Bitmap cropBitmap(Bitmap src, int newWidth, int newHeight, int type) {
            if (src == null) {
                return null;
            }
            final int srcW = src.getWidth();
            final int srcH = src.getHeight();
            int sx;
            int sy;
            switch (type) {
                case 1:
                    sx = (srcW - newWidth) / 2;
                    sy = (srcH - newHeight) / 2;
                    break;
                case 2:
                    sx = srcW - newWidth;
                    sy = srcH - newHeight;
                    break;
                default:
                    sx = 0;
                    sy = 0;
            }
            Bitmap result;
            result = Bitmap.createBitmap(src, sx, sy, newWidth, newHeight);
            return result;
        }

        private Bitmap cropBitmap(int newWidth, int newHeight, int type) {
            return this.cropBitmap(mBitmap, newWidth, newHeight, type);
        }

        /**
         * 缩放位图
         *
         * @param newWidth
         * @param newHeight
         * @return
         */
        private Bitmap scaleBitmap(int newWidth, int newHeight) {
            if (mBitmap == null) {
                return null;
            }
            return Bitmap.createScaledBitmap(mBitmap, newWidth, newHeight, false);
        }
    }
}
