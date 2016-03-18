package com.mobilephonesensor.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobilephonesensor.R;
import com.mobilephonesensor.model.Tab;

import java.util.List;

public class ViewPagerIndicator extends LinearLayout {
    /**
     * 绘制指示器的画笔
     */
    private Paint mPaint;
    /**
     * 指示器宽度
     */
    private int mIndicatorWidth;
    /**
     * 指示器高度
     */
    private int mIndicatorHeight;
    /**
     * 指示器颜色
     */
    private int mIndicatorColor;
    /**
     * 初始时，指示器的偏移量
     */
    private int mInitTranslationX;
    /**
     * 手指滑动时的偏移量
     */
    private float mTranslationX;
    /**
     * tab数量
     */
    private int mTabVisibleCount;
    /**
     * 与之绑定的ViewPager
     */
    private ViewPager mViewPager;
    /**
     * 标题正常时的颜色
     */
    private static final int COLOR_TEXT_NORMAL = 0xFF999999;
    private int mTextNormalColor;
    /**
     * 标题选中时的颜色
     */
    private static final int COLOR_TEXT_HIGHLIGHT_COLOR = 0xFF18B0F4;
    private int mTextHighlightColor;
    /**
     * 是否设置指示器背景
     */
    private static final int DEFAULT_INDICATOR_BACKGROUND = 0xFFEEEEEE;
    private boolean isBackground;
    /**
     * 指示器背景颜色
     */
    private int backgroundColor;
    /**
     * 指示器距离底部的空白
     */
    private int bottomPadding;
    private static final int DEFAULT_BOTTOM_PADDING = 0;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        getScreenWidth();
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);

        mTabVisibleCount = a.getInt(R.styleable.ViewPagerIndicator_setShowTabs, 3);
        if (mTabVisibleCount < 0) {
            throw new IllegalArgumentException("show tabs is negative");
        }
        mIndicatorWidth = (int) a.getDimension(R.styleable.ViewPagerIndicator_setIndicatorWidth, 60);
        mIndicatorHeight = (int) a.getDimension(R.styleable.ViewPagerIndicator_setIndicatorHeight,6);
        mIndicatorColor = a.getColor(R.styleable.ViewPagerIndicator_setIndicatorColor,COLOR_TEXT_HIGHLIGHT_COLOR);
        mTextNormalColor = a.getColor(R.styleable.ViewPagerIndicator_setOffTabTextColor,COLOR_TEXT_NORMAL);
        mTextHighlightColor = a.getColor(R.styleable.ViewPagerIndicator_setOnTabTextColor,COLOR_TEXT_HIGHLIGHT_COLOR);
        isBackground = a.getBoolean(R.styleable.ViewPagerIndicator_indicatorBackground,false);
        if (isBackground) {
            backgroundColor = a.getColor(R.styleable.ViewPagerIndicator_setIndicatorBackground, DEFAULT_INDICATOR_BACKGROUND);
        }
        bottomPadding = (int) a.getDimension(R.styleable.ViewPagerIndicator_setIndicatorPaddingBottom, DEFAULT_BOTTOM_PADDING);
        if (bottomPadding < 0) {
            bottomPadding = DEFAULT_BOTTOM_PADDING;
        }
        a.recycle();
        // 初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mIndicatorColor);
        mPaint.setStrokeWidth(mIndicatorHeight);
        mPaint.setStyle(Style.FILL);
    }

    /**
     * 绘制指示器
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
        drawIndicatorBackground(canvas);
        // 画笔平移到正确的位置
        canvas.drawLine(mInitTranslationX + mTranslationX,
                getHeight()-bottomPadding,
                mInitTranslationX + mTranslationX +mIndicatorWidth,
                getHeight()-bottomPadding,
                mPaint);
        canvas.restore();
    }

    /**
     * 初始化指示器的宽度
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 初始时的偏移量
        mInitTranslationX = getWidth() / mTabVisibleCount / 2 - mIndicatorWidth / 2;
    }

    /**
     * 给指示器绘制背景
     */
    private void drawIndicatorBackground(Canvas canvas) {
        if (!isBackground) return;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(backgroundColor);
        paint.setStrokeWidth(mIndicatorHeight);
        paint.setStyle(Style.FILL);
        canvas.drawLine(0, getHeight() - bottomPadding, getWidth(), getHeight() - bottomPadding, paint);
    }

    public void setIndicatorWidth(int indicatorWidth) {
        this.mIndicatorWidth = indicatorWidth;
    }

    /**
     * 设置可见的tab的数量
     * @param count
     */
    public void setVisibleTabCount(int count) {
        this.mTabVisibleCount = count;
    }

    public void setTabs(List<Tab> tabs) {
        createTabs(tabs);
    }

    /**
     * 设置tab的标题内容
     */
    private void createTabs(List<Tab> tabs) {
        // 如果传入的list有值，则移除布局文件中设置的view
        if (tabs == null || tabs.size() == 0)
            return;
        this.removeAllViews();
        int maxWidth = screenWidth / mTabVisibleCount;
        if (mIndicatorWidth < 0 || mIndicatorWidth > maxWidth) {
            mIndicatorWidth = maxWidth;
        }
        for (Tab tab: tabs) {
            View tabView = tab.getTabView();
            addView(tabView);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
            params.width = maxWidth;
            tabView.setLayoutParams(params);
        }
        setItemClickEvent();
    }

    /**
     * 对外的ViewPager的回调接口
     */
    public interface PageChangeListener {
        void onPageScrolled(int position, float positionOffset,
                            int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }

    // 对外的ViewPager的回调接口
    private PageChangeListener onPageChangeListener;

    // 对外的ViewPager的回调接口的设置
    public void setOnPageChangeListener(PageChangeListener pageChangeListener) {
        this.onPageChangeListener = pageChangeListener;
    }

    // 设置关联的ViewPager
    public void setViewPager(ViewPager mViewPager, int pos) {
        this.mViewPager = mViewPager;

        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // 设置字体颜色高亮
                resetTextViewColor();
                highLightTextView(position);

                // 回调
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                // 滚动
                scroll(position, positionOffset);

                // 回调
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrolled(position,
                            positionOffset, positionOffsetPixels);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // 回调
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrollStateChanged(state);
                }

            }
        });
        // 设置当前页
        mViewPager.setCurrentItem(pos);
        // 高亮
        highLightTextView(pos);
    }

    /**
     * 高亮文本
     *
     * @param position
     */
    protected void highLightTextView(int position) {
        View view = getChildAt(position);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(mTextHighlightColor);
        }
    }

    /**
     * 重置文本颜色
     */
    private void resetTextViewColor() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(mTextNormalColor);
            }
        }
    }

    /**
     * 设置点击事件
     */
    public void setItemClickEvent() {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            final int j = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j, false);
                }
            });
        }
    }

    /**
     * 指示器跟随手指滚动，以及容器滚动
     *
     * @param position
     * @param offset
     */
    public void scroll(int position, float offset) {
        /**
         * <pre>
         *  0-1:position=0 ;1-0:postion=0;
         * </pre>
         */
        // 不断改变偏移量，invalidate
        mTranslationX = getWidth() / mTabVisibleCount * (position + offset);

        int tabWidth = getWidth() / mTabVisibleCount;

        // 容器滚动，当移动到倒数最后一个的时候，开始滚动
        if (offset > 0 && position >= (mTabVisibleCount - 2)
                && getChildCount() > mTabVisibleCount) {
            if (mTabVisibleCount != 1) {
                this.scrollTo((position - (mTabVisibleCount - 2)) * tabWidth
                        + (int) (tabWidth * offset), 0);
            } else
            // 为count为1时 的特殊处理
            {
                this.scrollTo(
                        position * tabWidth + (int) (tabWidth * offset), 0);
            }
        }
        invalidate();
    }

    /**
     * 设置布局中view的一些必要属性；如果设置了setTabTitles，布局中view则无效
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int cCount = getChildCount();

        if (cCount == 0)
            return;

        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            LayoutParams lp = (LayoutParams) view
                    .getLayoutParams();
            lp.height = LayoutParams.MATCH_PARENT;
            lp.weight = 1;
            lp.width = 0;
            view.setLayoutParams(lp);
        }
        // 设置点击事件
        setItemClickEvent();
    }

    /**
     * 获得屏幕的宽度
     *
     * @return
     */
    private static int screenWidth;
    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        return screenWidth;
    }
}
