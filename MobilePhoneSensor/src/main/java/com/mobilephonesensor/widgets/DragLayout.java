package com.mobilephonesensor.widgets;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.mobilephonesensor.R;

/**
 * Created by heng on 16-4-19.
 */
public class DragLayout extends ViewGroup {

    private final static int A_NEGATIVE = -1;

    public final static int MODE_FOR_WINDOW = 0;

    public final static int MODE_FOR_CONTENT = 1;

    public static DragLayout attach(Activity activity, int mode) {

        DragLayout menu = new DragLayout(activity);

        ViewGroup layout;

        switch (mode) {
            case MODE_FOR_WINDOW:
                layout = (ViewGroup) activity.getWindow().getDecorView();
                View content = layout.getChildAt(0);
                layout.removeAllViews();
                menu.setContentView(content);
                layout.addView(menu, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                break;
            case MODE_FOR_CONTENT:
                layout = (ViewGroup) activity.findViewById(android.R.id.content);
                layout.removeAllViews();
                layout.addView(menu, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                break;
            default:
                throw new RuntimeException("Unknown DragLayout Mode: " + mode);
        }

        return menu;
    }

    public enum MenuState {
        OPEN, CLOSE, SLIDING, NONE
    }

    private MenuState mMenuState = MenuState.CLOSE;

    private boolean mDragging = false;

    private View mMenuView;

    private View mContentView;

    private float mOffsetPixels;

    private float mLastTouchX = -1;

    private float mLastTouchY = -1;

    private int mSlop;

    private int mMaxSpeed;

    private int mMenuWidth;

    private int mActivePointerId = A_NEGATIVE;

    private Scroller mScroller;

    private VelocityTracker mVelocityTracker;

    public MenuState getMenuState() {
        return mMenuState;
    }

    public View getMenuView() {
        return mMenuView;
    }

    public void setMenuView(View menuView) {
        this.mMenuView = menuView;
        removeView(mMenuView);
        addView(mMenuView, 0);
    }

    public View getContentView() {
        return mContentView;
    }

    public void setContentView(View contentView) {
        this.mContentView = contentView;
        removeView(mContentView);
        addView(mContentView);
    }

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context, new LinearInterpolator());
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mSlop = configuration.getScaledTouchSlop();
        mMaxSpeed = configuration.getScaledMaximumFlingVelocity();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int childCount = getChildCount();
        if (childCount > 2) {
            throw new IllegalStateException("Menu and content view added in DragLayout");
        }

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == View.MeasureSpec.UNSPECIFIED || heightMode == View.MeasureSpec.UNSPECIFIED) {
            throw new IllegalStateException("Must measure with an exact size");
        }

        final int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        for (int i = 0; i < childCount; i++) {
            if (i == 0) {
                measureChild(mMenuView, widthMeasureSpec, heightMeasureSpec);
            } else {
                measureChild(mContentView, widthMeasureSpec, heightMeasureSpec);
            }
        }

        setMeasuredDimension(parentWidth, parentHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMenuWidth = mMenuView.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;
        final int height = b - t;
        mMenuView.layout(0, 0, mMenuWidth, height);
        mContentView.layout(0, 0, width, height);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View menu = findViewById(R.id.dragMenu);
        if (menu != null) {
            setMenuView(menu);
        }

        View content = findViewById(R.id.dragContent);
        if (content != null) {
            setContentView(content);
        }

        if (getChildCount() > 2) {
            throw new IllegalStateException("Menu and content view added in xml must have id's @id/dragMenu and @id/dragContent");
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        final int action = ev.getAction() & MotionEvent.ACTION_MASK;

        if (action != MotionEvent.ACTION_DOWN && mDragging) {
            return true;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchX = ev.getX();
                mLastTouchY = ev.getY();
                mActivePointerId = ev.getPointerId(0);

                if (mMenuState == MenuState.OPEN
                        && mOffsetPixels == mMenuWidth
                        && mLastTouchX > mMenuWidth) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final int moveIndex = ev.findPointerIndex(mActivePointerId);
                if (moveIndex == -1) {
                    mActivePointerId = A_NEGATIVE;
                    mDragging = false;
                    return false;
                }

                final float x = ev.getX(moveIndex);
                final float y = ev.getY(moveIndex);
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;
                if (checkTouchSlop(dx, dy)) {
                    if (mOnInterceptTouchEventListener != null) {
                        boolean scroll = false;
                        if (mMenuState == MenuState.OPEN) {
                            final int tempX = (int) (x - (mMenuView.getLeft() + mMenuView.getTranslationX()));
                            final int tempY = (int) (y - (mMenuView.getTop() + mMenuView.getTranslationY()));
                            scroll = checkChildScroll(mMenuView, tempX, tempY, (int) dx, (int) dy);
                        } else if (mMenuState == MenuState.CLOSE) {
                            final int tempX = (int) (x - (mContentView.getLeft() + mContentView.getTranslationX()));
                            final int tempY = (int) (y - (mContentView.getTop() + mContentView.getTranslationY()));
                            scroll = checkChildScroll(mContentView, tempX, tempY, (int) dx, (int) dy);
                        }
                        if (scroll) {
                            endDrag();
                            requestDisallowInterceptTouchEvent(true);
                            return false;
                        }
                    }
                    mDragging = true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mActivePointerId = A_NEGATIVE;
                endDrag();
                return false;
            case MotionEvent.ACTION_POINTER_UP:
                upPointer(ev);
                break;
        }

        addMovementToVelocityTracker(ev);

        return mDragging;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final int action = event.getAction() & MotionEvent.ACTION_MASK;

        addMovementToVelocityTracker(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchX = event.getX();
                mLastTouchY = event.getY();
                mActivePointerId = event.getPointerId(0);
                stopScrollAnim();
                break;
            case MotionEvent.ACTION_MOVE:
                final int moveIndex = event.findPointerIndex(mActivePointerId);
                if (moveIndex == -1) {
                    mActivePointerId = A_NEGATIVE;
                    mDragging = false;
                    return false;
                }

                if (!mDragging) {
                    final float x = event.getX(moveIndex);
                    final float y = event.getY(moveIndex);
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;
                    if (checkTouchSlop(dx, dy)) {
                        mDragging = true;
                    }
                }

                if (mDragging) {
                    final float x = event.getX(moveIndex);
                    final float y = event.getY(moveIndex);
                    final float dx = x - mLastTouchX;
                    mLastTouchX = x;
                    mLastTouchY = y;
                    transMenuAndContent(dx + mOffsetPixels);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                int upIndex = event.findPointerIndex(mActivePointerId);
                upIndex = upIndex == -1 ? 0 : upIndex;
                mLastTouchX = event.getX(upIndex);
                mLastTouchY = event.getY(upIndex);
                mVelocityTracker.computeCurrentVelocity(1000, mMaxSpeed);
                final int velocity = (int) mVelocityTracker.getXVelocity(mActivePointerId);
                startScrollAnim(velocity > 0 ? mMenuWidth : 0, velocity);
                mActivePointerId = A_NEGATIVE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                final int actionIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                mLastTouchX = event.getX(actionIndex);
                mActivePointerId = event.getPointerId(actionIndex);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                upPointer(event);
                break;
        }

        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            transMenuAndContent(mScroller.getCurrX());
            invalidate();
        }
    }

    private void upPointer(MotionEvent event) {
        final int pointerIndex = event.getActionIndex();
        final int pointerId = event.getPointerId(event.getActionIndex());
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastTouchX = event.getX(newPointerIndex);
            mActivePointerId = event.getPointerId(newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
        mLastTouchX = event.getX(event.findPointerIndex(mActivePointerId));
        mLastTouchY = event.getY(event.findPointerIndex(mActivePointerId));
    }

    private void openMenu() {
        mDragging = false;
    }

    private void closeMenu() {
        mDragging = false;
    }

    private void transMenuAndContent(float offset) {

        final int menuWidth = mMenuWidth;

        final float offsetPixels = offset;

        final float ratio = offsetPixels / menuWidth;

        final float menuOffset = -0.25f * (1.0f - ratio) * menuWidth;


        if (offsetPixels >= 0 && offsetPixels <= menuWidth) {
            mContentView.setTranslationX(offsetPixels);
            mMenuView.setTranslationX(menuOffset);
            mOffsetPixels = offsetPixels;
        }

        if (mOffsetPixels == mMenuWidth) {
            mMenuState = MenuState.OPEN;
        } else if (mOffsetPixels == 0) {
            mMenuState = MenuState.CLOSE;
        } else {
            mMenuState = MenuState.SLIDING;
        }
    }

    private void addMovementToVelocityTracker(MotionEvent ev) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
    }

    private boolean checkTouchSlop(float dx, float dy) {
        return Math.abs(dx) > mSlop && Math.abs(dx) > Math.abs(dy);
    }

    private void startScrollAnim(int critical, int velocity) {
        endDrag();
        final int startX = (int) mOffsetPixels;
        if (mDragging && Math.abs(velocity) > 1000) {
            final int dx = critical - startX;
            final float rate = velocity / 1000;
            final int duration = (int) (dx / rate);
            mScroller.startScroll(startX, 0, dx, 0, duration);
        } else {
            int dx;
            int duration;
            if (mMenuState == MenuState.OPEN
                    && mLastTouchX > mMenuWidth) {
                dx = -mMenuWidth;
                duration = 350;
            } else {
                if (startX > mMenuWidth / 2) {
                    dx = mMenuWidth - startX;
                } else {
                    dx = -startX;
                }
                final float rate = mMenuWidth / 350;
                duration = (int) (dx / rate);
            }
            mScroller.startScroll(startX, 0, dx, 0, duration);
        }
        invalidate();
    }

    private void stopScrollAnim() {
        mScroller.abortAnimation();
    }

    private void endDrag() {
        mDragging = false;
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private boolean checkChildScroll(View checkView, int x, int y, int dx, int dy) {

        if (checkView instanceof ViewGroup) {

            final ViewGroup viewGroup = (ViewGroup) checkView;

            final int cCount = viewGroup.getChildCount();

            for (int i = cCount - 1; i >= 0; i--) {

                final View child = viewGroup.getChildAt(i);
                final int transX = (int) child.getTranslationX();
                final int transY = (int) child.getTranslationY();
                final int top = child.getTop() + transY;
                final int left = child.getLeft() + transX;
                final int right = child.getRight() + transX;
                final int bottom = child.getBottom() + transY;

                if (x >= left && x < right && y >= top && y < bottom
                        && checkChildScroll(child, x - left, y - top, dx, dy)) {
                    return true;
                }
            }
        }

        return mOnInterceptTouchEventListener.onIntercept(checkView, x, y, dx, dy);
    }

    private OnInterceptTouchEventListener mOnInterceptTouchEventListener;

    public void setOnInterceptTouchEventListener(OnInterceptTouchEventListener listener) {
        this.mOnInterceptTouchEventListener = listener;
    }

    public interface OnInterceptTouchEventListener {

        boolean onIntercept(View v, int x, int y, int dx, int dy);
    }
}
