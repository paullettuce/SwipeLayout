package pl.paullettuce.swipelayout_lib.helpers

import android.view.MotionEvent
import android.view.View
import kotlin.math.absoluteValue

class CoordsStore {
    private var originalX = 0f
    private var lastTouchX = 0f
    var actionDownX = 0F
        private set
    var actionDownY = 0F
        private set

    fun onAttachedToWindow(view: View) {
        originalX = view.x
    }

    fun bringToOriginalPosition(view: View) {
        view.x = originalX
    }

    fun actionDown(event: MotionEvent) {
        actionDownX = event.x()
        actionDownY = event.y()
        lastTouchX = actionDownX
    }

    /**
     * Move draggable view by ~1px step
     * @return true if view has moved, false if not
     */
    fun moveView(view: View, event: MotionEvent): Boolean {
        val xDiff = event.xDiffTo(lastTouchX)
        val currentX = event.x()
        if (xDiff.absoluteValue > 1f) {
            lastTouchX = currentX
            view.x += xDiff
            return true
        }
        return false
    }

    fun isMoving(event: MotionEvent) = event.x() != actionDownX || event.y() != actionDownY
}