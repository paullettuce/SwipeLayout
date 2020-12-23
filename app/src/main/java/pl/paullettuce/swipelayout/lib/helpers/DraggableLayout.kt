package pl.paullettuce.swipelayout.lib.helpers

import android.view.View

interface DraggableLayout {
    fun getDraggableView(): View
    fun onMove(touchPointX: Float, currentX: Float)
    fun onPositionReset()
    fun swipedToLeft()
    fun swipedToRight()
}