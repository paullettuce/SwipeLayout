package pl.paullettuce.swipelayout_lib.helpers.drag

import android.view.MotionEvent
import pl.paullettuce.SwipeLayout
import pl.paullettuce.swipelayout_lib.helpers.AllowedSwipeDirectionState
import kotlin.math.absoluteValue

internal class DragHelper(
    private val mainLayoutController: SwipeLayout,
    private val allowedSwipeDirection: AllowedSwipeDirectionState,
    private val swipeConfirmedThreshold: Float = 0.5f
) {
    private var originalX: Float = 0f
    private var lastTouchX = 0f
    private var actionDownX = 0F
    private var actionDownY = 0F
    private var blockTouchesUntilReset: Boolean = false
    private var isCurrentlyDraggedHorizontally = false

    fun onAttachedToWindow() {
        originalX = mainLayoutController.getDraggableView().x
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        if (blockTouchesUntilReset) return true // consume touch event without action
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
        blockTouchesUntilReset = false
        isCurrentlyDraggedHorizontally = false
    }

    private fun isMoving(event: MotionEvent) = event.x() != actionDownX || event.y() != actionDownY

    private fun actionDown(event: MotionEvent) {
        actionDownX = event.x()
        actionDownY = event.y()
        lastTouchX = actionDownX
    }

    private fun actionMove(event: MotionEvent) {
        if (blockTouchesIfMoveIsVertical(event)) return
        if (!allowedSwipeDirection.isDragValid(event.xDiffTo(actionDownX))) return

        moveView(event)
    }

    private fun actionUp(event: MotionEvent) {
        if (!checkIfCommittedASwipe(event)) { // if drag is not a swipe
            mainLayoutController.reset()
        }
    }

    private fun blockTouchesIfMoveIsVertical(event: MotionEvent): Boolean {
        val diffX = event.xDiffTo(actionDownX).absoluteValue
        val diffY = event.yDiffTo(actionDownY).absoluteValue
        if (diffY > diffX && !isCurrentlyDraggedHorizontally) {
            blockTouchesUntilReset = true
        }
        return blockTouchesUntilReset
    }

    /**
     * Move draggable view by ~1px step
     */
    private fun moveView(event: MotionEvent) {
        val xDiff = event.xDiffTo(lastTouchX)
        val currentX = event.x()
        if (xDiff.absoluteValue > 1f) {
            isCurrentlyDraggedHorizontally = true
            mainLayoutController.getDraggableView().x += xDiff
            lastTouchX = currentX
            mainLayoutController.onMove(actionDownX, currentX)
        }
    }

    private fun checkIfCommittedASwipe(event: MotionEvent): Boolean {
        val travelled = event.xDiffTo(actionDownX)
        if (!allowedSwipeDirection.isDragValid(travelled)) return false

        val minDistanceToTreatAsSwipe = getMinDistanceToTreatAsSwipe()
        return when {
            travelled < -minDistanceToTreatAsSwipe -> {
                blockTouchesUntilReset = true
                mainLayoutController.swipeToLeft()
                true
            }
            travelled > minDistanceToTreatAsSwipe -> {
                blockTouchesUntilReset = true
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

    private fun MotionEvent.xDiffTo(startX: Float) = x() - startX
    private fun MotionEvent.yDiffTo(startY: Float) = y() - startY

    private fun MotionEvent.x() = getX(actionIndex)
    private fun MotionEvent.y() = getY(actionIndex)
}