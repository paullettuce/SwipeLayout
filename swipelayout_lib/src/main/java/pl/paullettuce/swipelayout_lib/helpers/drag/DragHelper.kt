package pl.paullettuce.swipelayout_lib.helpers.drag

import android.view.MotionEvent
import pl.paullettuce.SwipeLayout
import pl.paullettuce.swipelayout_lib.helpers.*
import pl.paullettuce.swipelayout_lib.helpers.AllowedSwipeDirectionState
import pl.paullettuce.swipelayout_lib.helpers.xDiffTo
import pl.paullettuce.swipelayout_lib.helpers.yDiffTo
import kotlin.math.absoluteValue

internal class DragHelper(
    private val mainLayoutController: SwipeLayout,
    private val allowedSwipeDirection: AllowedSwipeDirectionState,
    private val swipeConfirmedThreshold: Float = 0.5f
) {

    private val coords = CoordsStore()
    private var viewState: ViewState = Still()

    fun onAttachedToWindow() {
        coords.onAttachedToWindow(mainLayoutController.getDraggableView())
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        if (!viewState.shouldHandleTouchItself()) return false // notify parent view that touch wasn't consumed nor handled, so parent view can handle it itself
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                actionDown(event)
            }
            MotionEvent.ACTION_MOVE -> {
                actionMove(event)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                actionUp(event)
            }
        }
        return true
    }

    fun isDragAction(event: MotionEvent?): Boolean {
        event ?: return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                actionDown(event)
            }
            MotionEvent.ACTION_MOVE -> {
                return isMoving(event)
            }
        }
        return false
    }

    fun onReset() {
        coords.bringToOriginalPosition(mainLayoutController.getDraggableView())
        viewState = Still()
    }

    fun swipedAway() {
        viewState = SwipedAway()
    }

    private fun isMoving(event: MotionEvent) = coords.isMoving(event)

    private fun actionDown(event: MotionEvent) {
        coords.actionDown(event)
    }

    private fun actionMove(event: MotionEvent) {
        viewState.actionMove(event)
    }

    private fun actionUp(event: MotionEvent) {
        viewState.actionUp(event)
    }

    /**
     * Move draggable view by ~1px step
     * @return true if view has moved, false if not
     */
    private fun moveView(event: MotionEvent): Boolean {
        val travelledX = event.xDiffTo(coords.actionDownX)
        if (!allowedSwipeDirection.isDragValid(travelledX)) return false

        if (coords.moveView(mainLayoutController.getDraggableView(), event)) {
            mainLayoutController.onMove(travelledX)
            return true
        }
        return false
    }

    abstract class ViewState {
        abstract fun shouldHandleTouchItself(): Boolean
        abstract fun actionUp(event: MotionEvent)
        abstract fun actionMove(event: MotionEvent)
    }

    inner class ScrollingVertically : ViewState() {
        override fun actionUp(event: MotionEvent) {
            mainLayoutController.reset()
        }

        override fun actionMove(event: MotionEvent) {}

        override fun shouldHandleTouchItself() = false
    }

    inner class DraggingHorizontally : ViewState() {
        override fun actionUp(event: MotionEvent) {
            if (checkIfCommittedASwipe(event)) { // if drag is a swipe
                viewState = SwipedAway()
            } else {
                mainLayoutController.reset()
            }
        }

        override fun actionMove(event: MotionEvent) {
            moveView(event)
        }

        override fun shouldHandleTouchItself() = true

        private fun checkIfCommittedASwipe(event: MotionEvent): Boolean {
            val travelled = event.xDiffTo(coords.actionDownX)
            if (!allowedSwipeDirection.isDragValid(travelled)) return false

            val minDistanceToTreatAsSwipe = getMinDistanceToTreatAsSwipe()
            return when {
                travelled < -minDistanceToTreatAsSwipe -> {
                    mainLayoutController.swipeToLeft()
                    true
                }
                travelled > minDistanceToTreatAsSwipe -> {
                    mainLayoutController.swipeToRight()
                    true
                }
                else -> {
                    false
                }
            }
        }

        private fun getMinDistanceToTreatAsSwipe() =
            mainLayoutController.getDraggableView().width * swipeConfirmedThreshold
    }

    inner class SwipedAway : ViewState() {
        override fun actionUp(event: MotionEvent) {}
        override fun actionMove(event: MotionEvent) {}
        override fun shouldHandleTouchItself() = false
    }

    inner class Still : ViewState() {
        override fun actionUp(event: MotionEvent) {}
        override fun actionMove(event: MotionEvent) {
            if (isScrollingVertically(event)) {
                viewState = ScrollingVertically()
            } else if (moveView(event)) {
                viewState = DraggingHorizontally()
            }
        }

        override fun shouldHandleTouchItself() = true

        private fun isScrollingVertically(event: MotionEvent): Boolean {
            val diffX = event.xDiffTo(coords.actionDownX).absoluteValue
            val diffY = event.yDiffTo(coords.actionDownY).absoluteValue
            return diffY > diffX
        }
    }
}
