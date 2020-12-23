package pl.paullettuce.swipelayout.lib.helpers.drag

import android.view.View

interface DraggableView {
    fun getDraggableView(): View
    fun onMove(touchPointX: Float, currentX: Float)
    fun onPositionReset()
    fun swipedToLeft()
    fun swipedToRight()
}