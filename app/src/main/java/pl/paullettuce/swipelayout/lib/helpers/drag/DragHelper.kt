package pl.paullettuce.swipelayout.lib.helpers.drag

import android.view.MotionEvent
import pl.paullettuce.swipelayout.lib.helpers.AllowedSwipeDirectionState

class DragHelper(
    private val draggableView: DraggableView,
    private val allowedSwipeDirection: AllowedSwipeDirectionState,
    private val swipeConfirmedThreshold: Float = 0.5f
) {
    private var originalX: Float = 0f
    private var lastTouchX = 0f
    private var actionDownX = 0F
    private var actionDownY = 0F

    fun onAttachedToWindow() {
        originalX = draggableView.getDraggableView().x
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
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

    private fun isMoving(event: MotionEvent) = event.x() != actionDownX || event.y() != actionDownY

    private fun actionDown(event: MotionEvent) {
        actionDownX = event.x()
        actionDownY = event.y()
        lastTouchX = actionDownX
    }

    private fun actionMove(event: MotionEvent) {
        val x = event.x()
        if (!allowedSwipeDirection.isDragValid(x - actionDownX)) return

        val diff = x - lastTouchX
        if (diff > 1f || diff < 1f) {
            draggableView.getDraggableView().x += diff
            lastTouchX = x
            draggableView.onMove(actionDownX, x)
        }
    }

    private fun actionUp(event: MotionEvent) {
        if (!checkIfCommittedASwipe(event)) {
            reset()
        }
    }

    private fun checkIfCommittedASwipe(event: MotionEvent): Boolean {
        val travelled = event.x() - actionDownX
        if (!allowedSwipeDirection.isDragValid(travelled)) return false

        val minDistanceToTreatAsSwipe = getMinDistanceToTreatAsSwipe()
        return when {
            travelled < -minDistanceToTreatAsSwipe -> {
                draggableView.swipedToLeft()
                true
            }
            travelled > minDistanceToTreatAsSwipe -> {
                draggableView.swipedToRight()
                true
            }
            else -> {
                false
            }
        }
    }

    private fun getMinDistanceToTreatAsSwipe() =
        draggableView.getDraggableView().width * swipeConfirmedThreshold

    private fun reset() {
        draggableView.getDraggableView().x = originalX
        draggableView.onPositionReset()
    }

    private fun MotionEvent.x() = getX(actionIndex)
    private fun MotionEvent.y() = getY(actionIndex)
}