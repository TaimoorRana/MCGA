package com.concordia.mcga.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.concordia.mcga.activities.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.Vector;

import static android.content.ContentValues.TAG;


public class BuildingBottomSheetInfo<V extends View> extends CoordinatorLayout.Behavior<V> {

    /**
     * Callback for monitoring events about bottom sheets.
     */
    public abstract static class BottomSheetCallback {

        /**
         * Called when the bottom sheet changes its state.
         *
         * @param bottomSheet The bottom sheet view.
         * @param newState    The new state. This will be one of {@link #STATE_DRAGGING},
         *                    {@link #STATE_SETTLING}, {@link #STATE_ANCHOR_POINT},
         *                    {@link #STATE_EXPANDED},
         *                    {@link #STATE_COLLAPSED}, or {@link #STATE_HIDDEN}.
         */
        public abstract void onStateChanged(@NonNull View bottomSheet, @State int newState);

        /**
         * Called when the bottom sheet is being dragged.
         *
         * @param bottomSheet The bottom sheet view.
         * @param slideOffset The new offset of this bottom sheet within its range, from 0 to 1
         *                    when it is moving upward, and from 0 to -1 when it moving downward.
         */
        public abstract void onSlide(@NonNull View bottomSheet, float slideOffset);
    }

    /**
     * The bottom sheet is dragging.
     */
    public static final int STATE_DRAGGING = 1;

    /**
     * The bottom sheet is settling.
     */
    public static final int STATE_SETTLING = 2;

    /**
     * The bottom sheet is expanded_half_way.
     */
    public static final int STATE_ANCHOR_POINT = 3;

    /**
     * The bottom sheet is expanded.
     */
    public static final int STATE_EXPANDED = 4;

    /**
     * The bottom sheet is collapsed.
     */
    public static final int STATE_COLLAPSED = 5;

    /**
     * The bottom sheet is hidden.
     */
    public static final int STATE_HIDDEN = 6;

    /** @hide */
    @IntDef({STATE_EXPANDED, STATE_COLLAPSED, STATE_DRAGGING, STATE_ANCHOR_POINT, STATE_SETTLING, STATE_HIDDEN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {}

    private static final float HIDE_THRESHOLD = 0.5f;
    private static final float HIDE_FRICTION = 0.1f;

    private float mMinimumVelocity;

    private int mPeekHeight;

    private int mMinOffset;
    private int mMaxOffset;

    private static final int DEFAULT_ANCHOR_POINT = 700;
    private int mAnchorPoint;

    private boolean mHideable;

    @State
    private int mState = STATE_ANCHOR_POINT;
    @State
    private int mLastStableState = STATE_ANCHOR_POINT;

    private ViewDragHelper mViewDragHelper;

    private boolean mIgnoreEvents;

    private boolean mNestedScrolled;

    private int mParentHeight;

    private int customOffSet = -1;

    private WeakReference<V> mViewRef;

    private WeakReference<View> mNestedScrollingChildRef;

    private Vector<BottomSheetCallback> mCallback;

    private int mActivePointerId;

    private int mInitialY;

    private boolean mTouchingScrollingChild;

    private String mType = null;



    /**
     * Default constructor for instantiating BottomSheetBehaviors.
     */
    public BuildingBottomSheetInfo() { }

    /**
     * Default constructor for inflating BottomSheetBehaviors from layout.
     *
     * @param context The {@link Context}.
     * @param attrs   The {@link AttributeSet}.
     */
    public BuildingBottomSheetInfo(Context context, AttributeSet attrs ) {
        super( context, attrs );
        TypedArray a = context.obtainStyledAttributes(attrs,
                android.support.design.R.styleable.BottomSheetBehavior_Layout);
        try {
            setPeekHeight(a.getDimensionPixelSize(android.support.design.R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, 0));
            setHideable(a.getBoolean(android.support.design.R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
            a.recycle();

            /**
             * Getting the anchorPoint...
             */
            mAnchorPoint = DEFAULT_ANCHOR_POINT;
            a = context.obtainStyledAttributes(attrs, R.styleable.CustomBottomSheetBehavior);
            if (attrs != null)
                mAnchorPoint = (int) a.getDimension( R.styleable.CustomBottomSheetBehavior_anchorPoint, 0);
            a.recycle();
        }
        catch(Exception e){
            setPeekHeight(1000);
            setHideable(true);
            mAnchorPoint = 200;
        }


        ViewConfiguration configuration = ViewConfiguration.get(context);
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
    }

    /**
     * Will return the saved/previous state that the BottomSheet was found in
     * @param parent Coordinator Layout surrounding the BottomSheet
     * @param child ListView of the BottomSheet
     * @return The saved state of the BottomSheet
     */
    @Override
    public Parcelable onSaveInstanceState(CoordinatorLayout parent, V child ) {
        return new SavedState(super.onSaveInstanceState(parent, child), mState);
    }

    /**
     * When restoring the instance, the saved state is then generated
     * If the state wasn't set to a static point such as 'expanded' or 'collapsed'
     * then the default state will be set to 'collapsed'
     * @param parent Coordinator Layout surrounding the BottomSheet
     * @param child ListView of the BottomSheet
     * @param state The saved state of the BottomSheet
     */
    @Override
    public void onRestoreInstanceState(CoordinatorLayout parent, V child, Parcelable state ) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(parent, child, ss.getSuperState());
        // Intermediate states are restored as collapsed state
        if (ss.state == STATE_DRAGGING || ss.state == STATE_SETTLING) {
            mState = STATE_COLLAPSED;
        } else {
            mState = ss.state;
        }

        mLastStableState = mState;
    }

    /**
     * Sets the type of bottomsheet attributes to set. The onCreate method
     * will look at this variable to decide which attributable makeup
     * the bottomsheet will have to use
     * @param type A string dictating which kind of BottomSheet configuration will be used
     */
    public void setmType(String type){
        mType = type;
    }

    /**
     * Method configures the childlayout of the bottomsheet
     * This layout represents the list contained within the
     * Coordinator Layout
     * Maximum displaceable height and retraction are set here
     * Anchor points are also defined if necessary
     * @param parent
     * @param child
     * @param layoutDirection
     * @return True if The views are not null
     */
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection ) {
        // First let the parent lay it out
        if (mState != STATE_DRAGGING && mState != STATE_SETTLING) {
            if (ViewCompat.getFitsSystemWindows(parent) &&
                    !ViewCompat.getFitsSystemWindows(child)) {
                ViewCompat.setFitsSystemWindows(child, true);
            }
            parent.onLayoutChild(child, layoutDirection);
        }
        // Offset the bottom sheet
        mParentHeight = parent.getHeight();

        if (mType == "building_navigation") {
            mMinOffset = mParentHeight - child.getHeight() / 2 - child.getHeight() / 5 - child.getHeight() / 10;
            mMaxOffset = mParentHeight - parent.getHeight() / 6;
        }
        else if(mType == "building_information"){
            mMinOffset = mParentHeight - child.getHeight() / 2;
            mMaxOffset = mParentHeight - parent.getHeight() / 6;
        }
        else {
            mMinOffset = mParentHeight - child.getHeight() / 2;
            mMaxOffset = mParentHeight - parent.getHeight() / 4;
        }

        /**
         * New behavior
         */
        if (mState == STATE_ANCHOR_POINT) {
            ViewCompat.offsetTopAndBottom(child, mAnchorPoint);
        } else if (mState == STATE_EXPANDED) {
            ViewCompat.offsetTopAndBottom(child, mMinOffset);
        } else if (mHideable && mState == STATE_HIDDEN) {
            ViewCompat.offsetTopAndBottom(child, mParentHeight);
        } else if (mState == STATE_COLLAPSED) {
            ViewCompat.offsetTopAndBottom(child, mMaxOffset);
        }
        try {
            if (mViewDragHelper == null) {
                mViewDragHelper = ViewDragHelper.create(parent, mDragCallback);
            }
        }
        catch(Exception e){
            Log.e(TAG, "Exception: " + Log.getStackTraceString(e));
        }
        mViewRef = new WeakReference<>(child);
        mNestedScrollingChildRef = new WeakReference<>( findScrollingChild( child ) );
        return true;
    }

    /**
     * The onInterceptTouchEvent() method is called whenever a
     * touch event is detected on the surface of a ViewGroup,
     * including on the surface of its children. If onInterceptTouchEvent()
     * returns true, the MotionEvent is intercepted, meaning it
     * will be not be passed on to the child, but rather
     * to the onTouchEvent() method of the parent.
     * @param parent coordinator Layout
     * @param child ListView
     * @param event MotionEvent
     * @return true is the MotionEvent is intercepted
     */
    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, V child, MotionEvent event ) {
        if ( ! child.isShown() ) {
            return false;
        }

        int action = MotionEventCompat.getActionMasked( event );
        if ( action == MotionEvent.ACTION_DOWN ) {
            reset();
        }

        switch ( action ) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mTouchingScrollingChild = false;
                mActivePointerId = MotionEvent.INVALID_POINTER_ID;
                // Reset the ignore flag
                if (mIgnoreEvents) {
                    mIgnoreEvents = false;
                    return false;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                int initialX = (int) event.getX();
                mInitialY = (int) event.getY();
                if(mState == STATE_ANCHOR_POINT){
                    mActivePointerId = event.getPointerId(event.getActionIndex());
                    mTouchingScrollingChild = true;
                }else {
                    View scroll = mNestedScrollingChildRef.get();
                    if (scroll != null && parent.isPointInChildBounds(scroll, initialX, mInitialY)) {
                        mActivePointerId = event.getPointerId(event.getActionIndex());
                        mTouchingScrollingChild = true;
                    }
                }
                mIgnoreEvents = mActivePointerId == MotionEvent.INVALID_POINTER_ID &&
                        !parent.isPointInChildBounds(child, initialX, mInitialY);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }

        if ( action == MotionEvent.ACTION_CANCEL ) {
            // We don't want to trigger a BottomSheet fling as a result of a Cancel MotionEvent (e.g., parent horizontal scroll view taking over touch events)
            mScrollVelocityTracker.clear();
        }

        if ( ! mIgnoreEvents  &&  mViewDragHelper.shouldInterceptTouchEvent( event ) ) {
            return true;
        }
        // We have to handle cases that the ViewDragHelper does not capture the bottom sheet because
        // it is not the top most view of its parent. This is not necessary when the touch event is
        // happening over the scrolling content as nested scrolling logic handles that case.
        View scroll = mNestedScrollingChildRef.get();
        boolean ret = action == MotionEvent.ACTION_MOVE && scroll != null &&
                !mIgnoreEvents && mState != STATE_DRAGGING &&
                !parent.isPointInChildBounds(scroll, (int) event.getX(), (int) event.getY()) &&
                Math.abs(mInitialY - event.getY()) > mViewDragHelper.getTouchSlop();
        return ret;
    }

    /**
     * If this method is used to detect click actions,
     * it is recommended that the actions be performed by implementing and
     * calling performClick().
     * @param parent
     * @param child
     * @param event
     * @return True if an Action was detected and the Child is visible on the phone screen
     */
    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, V child, MotionEvent event ) {
        if ( ! child.isShown() ) {
            return false;
        }

        int action = MotionEventCompat.getActionMasked( event );
        if ( mState == STATE_DRAGGING  &&  action == MotionEvent.ACTION_DOWN ) {
            return true;
        }

        mViewDragHelper.processTouchEvent( event );

        if ( action == MotionEvent.ACTION_DOWN ) {
            reset();
        }

        // The ViewDragHelper tries to capture only the top-most View. We have to explicitly tell it
        // to capture the bottom sheet in case it is not captured and the touch slop is passed.
        if ( action == MotionEvent.ACTION_MOVE  &&  ! mIgnoreEvents ) {
            if ( Math.abs(mInitialY - event.getY()) > mViewDragHelper.getTouchSlop() ) {
                mViewDragHelper.captureChildView( child, event.getPointerId(event.getActionIndex()) );
            }
        }
        return ! mIgnoreEvents;
    }

    /**
     * Called when a descendant of the CoordinatorLayout attempts to initiate a nested scroll.
     * Any Behavior associated with any direct child of the CoordinatorLayout may respond to
     * this event and return true to indicate that the CoordinatorLayout should act as a nested
     * scrolling parent for this scroll. Only Behaviors that return true from this method will receive
     * subsequent nested scroll events.
     * @param coordinatorLayout
     * @param child
     * @param directTargetChild
     * @param target
     * @param nestedScrollAxes
     * @return true if Scrolling
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V child, View directTargetChild, View target, int nestedScrollAxes ) {
        mNestedScrolled = false;
        return ( nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL ) != 0;
    }



    private ScrollVelocityTracker mScrollVelocityTracker = new ScrollVelocityTracker();
    private class ScrollVelocityTracker {
        private long  mPreviousScrollTime = 0;
        private float mScrollVelocity     = 0;

        /**
         * Records scroll time of the user
         * Velocity of the scroll is a function of time
         * @param dy
         */
        public void recordScroll( int dy ) {
            long now = System.currentTimeMillis();

            if ( mPreviousScrollTime != 0 ) {
                long elapsed = now - mPreviousScrollTime;
                mScrollVelocity = (float) dy / elapsed * 1000; // pixels per sec
            }

            mPreviousScrollTime = now;
        }

        /**
         * Resets scroll time and velocity
         */
        public void clear() {
            mPreviousScrollTime = 0;
            mScrollVelocity = 0;
        }

        /**
         * Returns scroll velocity
         * @return
         */
        public float getScrollVelocity() {
            return mScrollVelocity;
        }
    }

    /**
     * Called when a nested scroll in progress is about to update, before the target
     * has consumed any of the scrolled distance.
     * The CoordinatorLayout will report as consumed the maximum number of pixels in
     * either direction that any Behavior responding to the nested scroll reported as consumed.
     * @param coordinatorLayout Coordinator Layout
     * @param child ListView
     * @param target the descendant view of the CoordinatorLayout performing the nested scroll
     * @param dx the raw horizontal number of pixels that the user attempted to scroll
     * @param dy the raw vertical number of pixels that the user attempted to scroll
     * @param consumed Travelled distance
     */
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V child, View target, int dx, int dy, int[] consumed ) {

        try {
            View scrollingChild = mNestedScrollingChildRef.get();
            if (target != scrollingChild) {
                return;
            }
        }
        catch (Exception e){
            Log.e(TAG, "Exception: "+ Log.getStackTraceString(e));
        }

        mScrollVelocityTracker.recordScroll( dy );

        int currentTop = child.getTop();
        int newTop     = currentTop - dy;


        if ( dy > 0 ) { // Upward
            if ( newTop < mMinOffset ) {
                consumed[1] = currentTop - mMinOffset;
                ViewCompat.offsetTopAndBottom( child, -consumed[1] );
                setStateInternal( STATE_EXPANDED );
            } else {
                consumed[1] = dy;
                ViewCompat.offsetTopAndBottom( child, -dy );
                setStateInternal( STATE_DRAGGING );
            }
        }
        else
        if ( dy < 0 ) { // Downward
            if ( ! ViewCompat.canScrollVertically(target, -1) ) {
                if ( newTop <= mMaxOffset || mHideable ) {
                    consumed[1] = dy;
                    ViewCompat.offsetTopAndBottom(child, -dy);
                    setStateInternal(STATE_DRAGGING);
                } else {
                    consumed[1] = currentTop - mMaxOffset;
                    ViewCompat.offsetTopAndBottom(child, -consumed[1]);
                    setStateInternal(STATE_COLLAPSED);
                }
            }
        }
        try {
            dispatchOnSlide(child.getTop());
        }catch(Exception e){
            Log.e(TAG, "Exception: "+ Log.getStackTraceString(e));
        }
        mNestedScrolled = true;
    }

    /**
     * @return True if nested scrolling
     */
    public boolean getmNestedScrolled(){
        return mNestedScrolled;
    }

    /**
     *  Each Behavior that returned true will receive subsequent nested scroll events for that nested scroll.
     *  onStopNestedScroll marks the end of a single nested scroll event sequence.
     *  The override is necessary to define the State Machine
     * @param coordinatorLayout Coordinator Layout
     * @param child Listview
     * @param target the descendant view of the CoordinatorLayout performing the nested scroll
     */
    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V child, View target ) {
        if ( child.getTop() == mMinOffset ) {
            setStateInternal( STATE_EXPANDED );
            mLastStableState = STATE_EXPANDED;
            return;
        }
        if ( target != mNestedScrollingChildRef.get()  ||  ! mNestedScrolled ) {
            return;
        }
        int top;
        int targetState;

        // Are we flinging up?
        float scrollVelocity = mScrollVelocityTracker.getScrollVelocity();
        if ( scrollVelocity > mMinimumVelocity) {
            if ( mLastStableState == STATE_COLLAPSED ) {
                // Fling from collapsed to anchor
                top = mMinOffset;
                targetState = STATE_EXPANDED;
            }
            else
            if ( mLastStableState == STATE_ANCHOR_POINT ) {
                // Fling from anchor to expanded
                top = mMinOffset;
                targetState = STATE_EXPANDED;
            }
            else {
                // We are already expanded
                top = mMinOffset;
                targetState = STATE_EXPANDED;
            }
        }
        else
            // Are we flinging down?
            if ( scrollVelocity < -mMinimumVelocity ) {
                if ( mLastStableState == STATE_EXPANDED ) {
                    // Fling to from expanded to anchor
                    top = mMaxOffset;
                    targetState = STATE_COLLAPSED;
                }
                else
                if ( mLastStableState == STATE_ANCHOR_POINT ) {
                    // Fling from anchor to collapsed
                    top = mMaxOffset;
                    targetState = STATE_COLLAPSED;
                }
                else {
                    // We are already collapsed
                    top = mMaxOffset;
                    targetState = STATE_COLLAPSED;
                }
            }
            // Not flinging, just settle to the nearest state
            else {
                // Collapse?
                int currentTop = child.getTop();
                if ( currentTop > mAnchorPoint * 1.25 ) { // Multiply by 1.25 to account for parallax. The currentTop needs to be pulled down 50% of the anchor point before collapsing.
                    top = mMaxOffset;
                    targetState = STATE_COLLAPSED;
                }
                // Expand?
                else
                if ( currentTop < mAnchorPoint * 0.5 ) {
                    top = mMinOffset;
                    targetState = STATE_EXPANDED;
                }
                // Snap back to the anchor
                else {
                    top = mMinOffset;
                    targetState = STATE_EXPANDED;
                }
            }

        mLastStableState = targetState;
        if ( mViewDragHelper.smoothSlideViewTo( child, child.getLeft(), top ) ) {
            setStateInternal( STATE_SETTLING );
            ViewCompat.postOnAnimation( child, new SettleRunnable( child, targetState ) );
        } else {
            setStateInternal( targetState );
        }
        mNestedScrolled = false;
    }

    /**
     *  Each Behavior that returned true will receive subsequent nested scroll events for that nested scroll.
     * @param coordinatorLayout Coordinator Layout
     * @param child ListView
     * @param target the descendant view of the CoordinatorLayout performing the nested scroll
     * @param velocityX speed of the fling vertically
     * @param velocityY speed of the fling horizontally
     * @return
     */
    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, V child, View target,
                                    float velocityX, float velocityY) {
        return target == mNestedScrollingChildRef.get() &&
                (mState != STATE_EXPANDED ||
                        super.onNestedPreFling(coordinatorLayout, child, target,
                                velocityX, velocityY));
    }

    /**
     * Sets the height of the bottom sheet when it is collapsed.
     *
     * @param peekHeight The height of the collapsed bottom sheet in pixels.
     * @attr ref android.support.design.R.styleable#BottomSheetBehavior_Params_behavior_peekHeight
     */
    public final void setPeekHeight(int peekHeight) {
        mPeekHeight = Math.max(0, peekHeight);
        mMaxOffset = mParentHeight - peekHeight;
    }

    /**
     * @return maximum offset value of the bottomsheet
     */
    public int getmMaxOffset(){
        return mMaxOffset;
    }
    /**
     * Gets the height of the bottom sheet when it is collapsed.
     *
     * @return The height of the collapsed bottom sheet.
     * @attr ref android.support.design.R.styleable#BottomSheetBehavior_Params_behavior_peekHeight
     */
    public final int getPeekHeight() {
        return mPeekHeight;
    }

    public void setAnchorPoint(int anchorPoint) {
        mAnchorPoint = anchorPoint;
    }
    public int getAnchorPoint(){
        return mAnchorPoint;
    }

    /**
     * Sets whether this bottom sheet can hide when it is swiped down.
     *
     * @param hideable {@code true} to make this bottom sheet hideable.
     * @attr ref android.support.design.R.styleable#BottomSheetBehavior_Params_behavior_hideable
     */
    public void setHideable(boolean hideable) {
        mHideable = hideable;
    }

    /**
     * Gets whether this bottom sheet can hide when it is swiped down.
     *
     * @return {@code true} if this bottom sheet can hide.
     * @attr ref android.support.design.R.styleable#BottomSheetBehavior_Params_behavior_hideable
     */
    public boolean isHideable() {
        return mHideable;
    }

    /**
     * Adds a callback to be notified of bottom sheet events.
     *
     * @param callback The callback to notify when bottom sheet events occur.
     */
    public void addBottomSheetCallback(BottomSheetCallback callback) {
        if (mCallback == null)
            mCallback = new Vector<>();

        mCallback.add(callback);
    }

    /**
     *
     * @return Vector containing all callbacks
     */
    public Vector getmCallBack(){
        return mCallback;
    }

    /**
     *
     * @return last static state of the bottomsheet (not dragging)
     */
    public int getmLastStableState(){
        return mLastStableState;
    }

    private int top_position;

    /**
     * @return top position of bottomsheet
     */
    public int getTop_position(){
        return top_position;
    }

    /**
     *
     * @return minimum offset of the bottomsheet
     */
    public int getmMinOffset(){
        return mMinOffset;
    }
    /**
     * Sets the state of the bottom sheet. The bottom sheet will transition to that state with
     * animation.
     *
     * @param state One of {@link #STATE_COLLAPSED}, {@link #STATE_ANCHOR_POINT},
     *              {@link #STATE_EXPANDED} or {@link #STATE_HIDDEN}.
     */
    public final void setState( @State int state ) {
        if ( state == mState ) {
            return;
        }
        if ( mViewRef == null ) {
            // The view is not laid out yet; modify mState and let onLayoutChild handle it later
            /**
             * New behavior (added: state == STATE_ANCHOR_POINT ||)
             */
            if ( state == STATE_COLLAPSED || state == STATE_EXPANDED || state == STATE_ANCHOR_POINT ||
                    (mHideable && state == STATE_HIDDEN)) {
                mState = state;
                mLastStableState = state;
            }

            return;
        }


        V child = mViewRef.get();
        if (child == null) {
            return;
        }

        if (state == STATE_COLLAPSED) {
            top_position = mMaxOffset;
        }
        else
        if (state == STATE_ANCHOR_POINT) {
            top_position = mAnchorPoint;
        }
        else
        if (state == STATE_EXPANDED) {
            top_position = mMinOffset;
        }
        else
        if (mHideable && state == STATE_HIDDEN) {
            if (mType == "building_information") {
                top_position = mParentHeight;
            }else {
                top_position = mMaxOffset;
            }
        } else {
            throw new IllegalArgumentException("Illegal state argument: " + state);
        }
        setStateInternal(STATE_SETTLING);
        if (mViewDragHelper.smoothSlideViewTo(child, child.getLeft(), top_position)) {
            ViewCompat.postOnAnimation(child, new SettleRunnable(child, state));
        }
    }

    /**
     * Gets the current state of the bottom sheet.
     *
     * @return One of {@link #STATE_EXPANDED}, {@link #STATE_ANCHOR_POINT}, {@link #STATE_COLLAPSED},
     * {@link #STATE_DRAGGING}, and {@link #STATE_SETTLING}.
     */
    @State
    public final int getState() {
        return mState;
    }

    /**
     * Sets the state of the bottomsheet
     * @param state represented as an integer
     */
    private void setStateInternal(@State int state) {
        if (mState == state) {
            return;
        }
        View bottomSheet = null;
        mState = state;
        try {
            bottomSheet = mViewRef.get();
        }
        catch(Exception e){
            Log.e(TAG, "Exception: "+ Log.getStackTraceString(e));
        }
        if (bottomSheet != null && mCallback != null) {
//            mCallback.onStateChanged(bottomSheet, state);
            notifyStateChangedToListeners(bottomSheet, state);
        }
    }

    /**
     * Listens for any state changes of the bottomsheet
     * @param bottomSheet
     * @param newState
     */
    private void notifyStateChangedToListeners(@NonNull View bottomSheet, @State int newState) {
        for (BottomSheetCallback bottomSheetCallback:mCallback) {
            bottomSheetCallback.onStateChanged(bottomSheet, newState);
        }
    }

    /**
     * Listens for sliding movements
     * @param bottomSheet
     * @param slideOffset
     */
    private void notifyOnSlideToListeners(@NonNull View bottomSheet, float slideOffset) {
        for (BottomSheetCallback bottomSheetCallback:mCallback) {
            bottomSheetCallback.onSlide(bottomSheet, slideOffset);
        }
    }

    /**
     * Will reset the view to its basic configuration of the onChildLayout
     */
    private void reset() {
        mActivePointerId = ViewDragHelper.INVALID_POINTER;
    }

    /**
     * Depending on the current state of the bottomsheet, we can determine
     * if the user's intention was to hide it
     * @param child
     * @param yvel
     * @return true if we hide the bottomsheet at the bottom
     */
    private boolean shouldHide(View child, float yvel) {
        if (child.getTop() < mMaxOffset) {
            // It should not hide, but collapse.
            return false;
        }
        final float newTop = child.getTop() + yvel * HIDE_FRICTION;
        return Math.abs(newTop - mMaxOffset) / (float) mPeekHeight > HIDE_THRESHOLD;
    }

    /**
     * Iteratively find all children of the bottomsheet
     * @param view
     * @return view
     */
    private View findScrollingChild(View view) {
        if (view instanceof NestedScrollingChild) {
            return view;
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0, count = group.getChildCount(); i < count; i++) {
                View scrollingChild = findScrollingChild(group.getChildAt(i));
                if (scrollingChild != null) {
                    return scrollingChild;
                }
            }
        }
        return null;
    }


    public final ViewDragHelper.Callback mDragCallback = new ViewDragHelper.Callback() {

        /**
         * Called when the user's input indicates that they want to capture
         * the given child view with the pointer indicated by pointerId. The
         * callback should return true if the user is permitted to drag the
         * given view with the indicated pointer.
         * @param child
         * @param pointerId
         * @return true if the user is permitted to drag the given view with the indicated pointer.
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId ) {
            if ( mState == STATE_DRAGGING ) {
                return false;
            }
            if ( mTouchingScrollingChild ) {
                return false;
            }
            if ( mState == STATE_EXPANDED  &&  mActivePointerId == pointerId ) {
                View scroll = mNestedScrollingChildRef.get();
                if (scroll != null && ViewCompat.canScrollVertically(scroll, -1)) {
                    // Let the content scroll up
                    return false;
                }
            }
            return mViewRef != null && mViewRef.get() == child;
        }

        /**
         * Called when the captured view's position changes as the result of a drag or settle.
         * @param changedView
         * @param left
         * @param top
         * @param dx
         * @param dy
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy ) {
            dispatchOnSlide( top );
        }

        /**
         * Called when the drag state changes. See the STATE_* constants for more information.
         * @param state
         */
        @Override
        public void onViewDragStateChanged( int state ) {
            if ( state == ViewDragHelper.STATE_DRAGGING ) {
                setStateInternal( STATE_DRAGGING );
            }
        }

        /**
         * Called when the child view is no longer being actively dragged.
         * The fling velocity is also supplied, if relevant. The velocity
         * values may be clamped to system minimums or maximums.
         * @param releasedChild
         * @param xvel
         * @param yvel
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel ) {
            int top;
            @State int targetState;
            if ( yvel < 0 ) { // Moving up
                top = mMinOffset;
                targetState = STATE_EXPANDED;
            }
            else
            if ( mHideable  &&  shouldHide(releasedChild, yvel) ) {
                if (mType == "building_information"){
                    top = mParentHeight;
                    targetState = STATE_HIDDEN;
                }else {
                    top = mMaxOffset;
                    targetState = STATE_COLLAPSED;
                }
            }
            else
            if ( yvel == 0.f ) {
                int currentTop = releasedChild.getTop();
                if (Math.abs(currentTop - mMinOffset) < Math.abs(currentTop - mMaxOffset)) {
                    top = mMinOffset;
                    targetState = STATE_EXPANDED;
                } else {
                    top = mMaxOffset;
                    targetState = STATE_COLLAPSED;
                }
            } else {
                top = mMaxOffset;
                targetState = STATE_COLLAPSED;
            }
            if ( mViewDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top) ) {
                setStateInternal(STATE_SETTLING);
                ViewCompat.postOnAnimation(releasedChild,
                        new SettleRunnable(releasedChild, targetState));
            } else {
                setStateInternal(targetState);
            }
        }

        /**
         * Restrict the motion of the dragged child view along the vertical axis.
         * @param child
         * @param top
         * @param dy
         * @return The new clamped position for top
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return constrain(top, mMinOffset, mHideable ? mParentHeight : mMaxOffset);
        }
        int constrain(int amount, int low, int high) {
            return amount < low ? low : (amount > high ? high : amount);
        }

        /**
         * Restrict the motion of the dragged child view along the horizontal axis.
         * @param child
         * @param left
         * @param dx
         * @return The new clamped position for left
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return child.getLeft();
        }

        /**
         * Return the magnitude of a draggable child view's vertical range of motion in pixels.
         * @param child
         * @return This method should return 0 for views that cannot move vertically.
         */
        @Override
        public int getViewVerticalDragRange(View child) {
            if (mHideable) {
                return mParentHeight - mMinOffset;
            } else {
                return mMaxOffset - mMinOffset;
            }
        }
    };

    /**
     * Notify on slide listeners when there is a slide motion
     * @param top
     */
    private void dispatchOnSlide( int top ) {
        View bottomSheet = null;
        try{
            bottomSheet = mViewRef.get();
        }
        catch (Exception e){
            Log.e(TAG, "Exception: "+ Log.getStackTraceString(e));
        }

        if (bottomSheet != null && mCallback != null) {
            if (top > mMaxOffset) {
                notifyOnSlideToListeners(bottomSheet, (float) (mMaxOffset - top) / mPeekHeight);
            } else {
                notifyOnSlideToListeners(bottomSheet, (float) (mMaxOffset - top) / ((mMaxOffset - mMinOffset)));
            }
        }
    }

    private class SettleRunnable implements Runnable {

        private final View mView;

        @State
        private final int mTargetState;

        /**
         * constructor
         * @param view
         * @param targetState
         */
        SettleRunnable(View view, @State int targetState ) {
            mView = view;
            mTargetState = targetState;
        }

        /**
         * When an object implementing interface Runnable is used to create a thread,
         * starting the thread causes the object's run method to be called in that
         * separately executing thread.The general contract of the method run is that it
         * may take any action whatsoever.
         */
        @Override
        public void run() {
            if ( mViewDragHelper != null  &&  mViewDragHelper.continueSettling( true ) ) {
                ViewCompat.postOnAnimation( mView, this );
            } else {
                setStateInternal( mTargetState );
            }
        }
    }

    /**
     * Set minimum offset
     * @param offSet
     */
    public void setmMinOffset(int offSet){
        mMinOffset = offSet;
    }

    protected static class SavedState extends View.BaseSavedState {

        @State
        final int state;

        /**
         * Constructor
         * @param source
         */
        public SavedState( Parcel source ) {
            super( source );
            // noinspection ResourceType
            state = source.readInt();
        }

        /**
         * Saves state of the bottomsheet
         * @param superState
         * @param state
         */
        public SavedState(Parcelable superState, @State int state) {
            super(superState);
            this.state = state;
        }

        /**
         * Flatten this object in to a Parcel.
         * @param out
         * @param flags
         */
        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(state);
        }

        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    @Override
                    public SavedState createFromParcel(Parcel source) {
                        return new SavedState(source);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    /**
     * A utility function to get the {@link BuildingBottomSheetInfo} associated with the {@code view}.
     *
     * @param view The {@link View} with {@link BuildingBottomSheetInfo}.
     * @return The {@link BuildingBottomSheetInfo} associated with the {@code view}.
     */
    @SuppressWarnings("unchecked")
    public static <V extends View> BuildingBottomSheetInfo<V> from(V view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params)
                .getBehavior();
        if (!(behavior instanceof BuildingBottomSheetInfo)) {
            throw new IllegalArgumentException(
                    "The view is not associated with BuildingBottomSheetInfo");
        }
        return (BuildingBottomSheetInfo<V>) behavior;
    }

}