package pl.paullettuce.swipelayout_lib.helpers.drag

import android.view.MotionEvent
import pl.paullettuce.SwipeLayout
import pl.paullettuce.swipelayout_lib.helpers.AllowedSwipeDirectionState

class DragHelper(
    private val mainLayoutController: SwipeLayout,
    private val allowedSwipeDirection: AllowedSwipeDirectionState,
    private val swipeConfirmedThreshold: Float = 0.5f
) {
    private var originalX: Float = 0f
    private var lastTouchX = 0f
    private var actionDownX = 0F
    private var actionDownY = 0F
    private var touchable: Boolean = true

    fun onAttachedToWindow() {
        originalX = mainLayoutController.getDraggableView().x
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        if (!touchable) return true // consume touch event without action
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
        mainLayoutController.getDraggableView().x = originalX
        touchable = true
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
            mainLayoutController.getDraggableView().x += diff
            lastTouchX = x
            mainLayoutController.onMove(actionDownX, x)
        }
    }

    private fun actionUp(event: MotionEvent) {
        if (!checkIfCommittedASwipe(event)) {
            mainLayoutController.reset()
        }
    }

    private fun checkIfCommittedASwipe(event: MotionEvent): Boolean {
        val travelled = event.x() - actionDownX
        if (!allowedSwipeDirection.isDragValid(travelled)) return false

        val minDistanceToTreatAsSwipe = getMinDistanceToTreatAsSwipe()
        return when {
            travelled < -minDistanceToTreatAsSwipe -> {
                touchable = false
                mainLayoutController.swipeToLeft()
                true
            }
            travelled > minDistanceToTreatAsSwipe -> {
                touchable = false
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

    private fun MotionEvent.x() = getX(actionIndex)
    private fun MotionEvent.y() = getY(actionIndex)
}