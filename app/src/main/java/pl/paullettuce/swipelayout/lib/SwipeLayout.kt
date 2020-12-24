package pl.paullettuce.swipelayout.lib

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.forEachIndexed
import androidx.core.view.isVisible
import pl.paullettuce.swipelayout.lib.helpers.AllowedSwipeDirectionState
import pl.paullettuce.swipelayout.lib.helpers.SwipeBothSides
import pl.paullettuce.swipelayout.lib.helpers.animation.SwipeAnimator
import pl.paullettuce.swipelayout.lib.helpers.background.BackgroundController
import pl.paullettuce.swipelayout.lib.helpers.background.BackgroundViewsVisibilityController
import pl.paullettuce.swipelayout.lib.helpers.drag.DragHelper
import pl.paullettuce.swipelayout.lib.helpers.drag.MainLayoutInteractor
import pl.paullettuce.swipelayout.lib.helpers.obtainSwipeAllowanceState

class SwipeLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    MainLayoutInteractor,
    BackgroundViewsVisibilityController,
    View.OnTouchListener {

    private val allowedSwipeDirection: AllowedSwipeDirectionState = obtainSwipeAllowanceState(context, attrs)
    private val dragHelper =
        DragHelper(
            this,
            allowedSwipeDirection
        )
    private val backgroundController =
        BackgroundController(
            this,
            startingMoveThresholdPx = 5f
        )
    private val swipeAnimator = SwipeAnimator()
    var swipeListener: SwipeListener? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        throwExceptionIfNotBuildProperly()
        dragHelper.onAttachedToWindow()
        hideAllButDraggableViews()
        setOnTouchListener(this)
    }

    override fun getDraggableView(): View = getChildAt(draggableViewIndex())

    override fun onMove(touchPointX: Float, currentX: Float) {
        backgroundController.onMove(touchPointX, currentX)
    }

    override fun swipedToLeft() {
        swipeAnimator.animateToLeft(getDraggableView())
        swipeListener?.swipedToLeft()
    }

    override fun swipedToRight() {
        swipeAnimator.animateToRight(getDraggableView())
        swipeListener?.swipedToRight()
    }

    override fun reset() {
        backgroundController.onReset()
        dragHelper.onReset()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        Log.d("SwipeLayout", "onTouch: ${v}, event=$event")
        event ?: return false
        return dragHelper.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        return dragHelper.isDragAction(event)
    }

    override fun onLeftUnderViewRevealed() {
        doOnLeftSideBGView { visible() }
    }

    override fun hideLeftUnderView() {
        doOnLeftSideBGView { gone() }
    }

    override fun onRightUnderViewRevealed() {
        doOnRightSideBGView { visible() }
    }

    override fun hideRightUnderView() {
        doOnRightSideBGView { gone() }
    }

    private fun doOnLeftSideBGView(action: View.() -> Unit) {
        val index = getLeftUnderlyingLayoutIndex()
        if (index < 0) return
        getChildAt(index).action()
    }

    private fun doOnRightSideBGView(action: View.() -> Unit) {
        val index = getRighUnderlyingLayoutIndex()
        if (index < 0) return
        getChildAt(index).action()
    }

    private fun getLeftUnderlyingLayoutIndex(): Int {
        return allowedSwipeDirection.getLeftLayoutIndex(childCount)
    }

    private fun getRighUnderlyingLayoutIndex(): Int {
        return allowedSwipeDirection.getRightLayoutIndex(childCount)
    }

    private fun hideAllButDraggableViews() {
        forEachIndexed { index, view ->
            if (index != draggableViewIndex()) view.gone()
        }
    }

    private fun draggableViewIndex() = childCount - 1

    private fun View.visible() {
        if (!isVisible) visibility = View.VISIBLE
    }

    private fun View.gone() {
        if (isVisible) visibility = View.GONE
    }

    private fun throwExceptionIfNotBuildProperly() {
        if (childCount == 0 || childCount > 3 || childCount == 2 && allowedSwipeDirection is SwipeBothSides) {
            throw LayoutNotBuiltProperlyException()
        }
    }

    interface SwipeListener {
        fun swipedToLeft()
        fun swipedToRight()
    }

    inner class LayoutNotBuiltProperlyException : Exception() {
        override val message: String?
            get() {
                return if (childCount == 2) {
                    "You have to specify swipe direction side explicitly. You can do that using swipeEnabled xml attribute"
                } else {
                    "This layout must have one, two or three children, current children count=$childCount."
                }
            }
    }
}