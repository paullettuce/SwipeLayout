package pl.paullettuce.swipelayout.lib.helpers.drag

import android.view.View

interface MainLayoutInteractor {
    fun getDraggableView(): View
    fun onMove(touchPointX: Float, currentX: Float)
    fun reset()
    fun swipedToLeft()
    fun swipedToRight()
}