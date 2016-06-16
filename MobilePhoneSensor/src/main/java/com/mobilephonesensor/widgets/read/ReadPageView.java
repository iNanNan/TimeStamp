package com.mobilephonesensor.widgets.read;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by heng on 16-5-24.
 */
public class ReadPageView extends ViewGroup {

    private Bitmap mPrevPage;

    private Bitmap mCurrPage;

    private Bitmap mNextPage;

    private HFParent mHeaderContainer;

    private HFParent mFooterContainer;

    private ReadMode mMode = ReadMode.getDefault();

    public void addHeader(View header) {
        mHeaderContainer.addView(header);
        invalidate();
    }

    public void addFooter(View footer) {
        mFooterContainer.addView(footer);
        invalidate();
    }

    public View getHeader() {
        if (mHeaderContainer.getChildCount() == 0) {
            return null;
        }
        return mHeaderContainer.getChildAt(0);
    }

    public View getFooter() {
        if (mFooterContainer.getChildCount() == 0) {
            return null;
        }
        return mFooterContainer.getChildAt(0);
    }

    public ReadMode getMode() {
        return mMode;
    }

    public void setMode(ReadMode mMode) {
        this.mMode = mMode;
    }

    public ReadPageView(Context context) {
        super(context);
    }

    public ReadPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReadPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderAndFooter(context);
    }

    private void initHeaderAndFooter(Context ctx) {
        mHeaderContainer = buildContainer(ctx);
        mFooterContainer = buildContainer(ctx);
    }

    private HFParent buildContainer(Context ctx) {
        HFParent container = new HFParent(ctx);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(params);
        return container;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.UNSPECIFIED) {
            throw new IllegalStateException("Must measure with an exact size");
        }

        final int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        measureChild(mHeaderContainer, widthMeasureSpec, heightMeasureSpec);
        measureChild(mFooterContainer, widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(parentWidth, parentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;
        final int height = b - t;
        mHeaderContainer.layout(0, 0, width, mHeaderContainer.getMeasuredHeight());
        mFooterContainer.layout(0, height - mFooterContainer.getMeasuredHeight(), width, height);
    }
}
