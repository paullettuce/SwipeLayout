package pl.paullettuce.swipelayout.lib

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.view.forEachIndexed
import androidx.core.view.isVisible
import pl.paullettuce.swipelayout.lib.helpers.*

class SwipeLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    DraggableLayout,
    BackgroundControllerI,
    View.OnTouchListener {

    private val allowedSwipeDirection: AllowedSwipeDirectionState = obtainSwipeAllowanceState(context, attrs)
    private val dragHelper = DragHelper(this, allowedSwipeDirection)
    private val backgroundController =
        BackgroundController(
            this,
            initialMoveThreshold = 5f
        )

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
        // TODO: 23.12.2020 animation to end goes here
        Toast.makeText(context, "swiped left", Toast.LENGTH_SHORT).show()
        dragHelper.reset()
    }

    override fun swipedToRight() {
        // TODO: 23.12.2020 animation to end goes here
        Toast.makeText(context, "swiped right", Toast.LENGTH_SHORT).show()
        dragHelper.reset()
    }

    override fun onPositionReset() {
        backgroundController.reset()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        Log.d("SwipeLayout", "onTouch: ${v}, event=$event")
        event ?: return false
        return dragHelper.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        return dragHelper.isDragAction(event)
    }

    override fun showLeftSide() {
        doOnLeftSide { visible() }
    }

    override fun hideLeftSide() {
        doOnLeftSide { gone() }
    }

    override fun showRightSide() {
        doOnRightSide { visible() }
    }

    override fun hideRightSide() {
        doOnRightSide { gone() }
    }

    private fun doOnLeftSide(action: View.() -> Unit) {
        val index = getLeftLayoutIndex()
        if (index < 0) return
        getChildAt(index).action()
    }

    private fun doOnRightSide(action: View.() -> Unit) {
        val index = getRightLayoutIndex()
        if (index < 0) return
        getChildAt(index).action()
    }

    private fun getLeftLayoutIndex(): Int {
        return allowedSwipeDirection.getLeftLayoutIndex(childCount)
    }

    private fun getRightLayoutIndex(): Int {
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