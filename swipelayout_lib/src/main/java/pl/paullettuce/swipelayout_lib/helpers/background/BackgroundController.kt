package pl.paullettuce.swipelayout_lib.helpers.background

import pl.paullettuce.SwipeLayout

internal class BackgroundController(
    private val mainLayoutController: SwipeLayout,
    private val startingMoveThresholdPx: Float
) {
    fun onMove(touchPointX: Float, currentX: Float) {
        when {
            currentX > touchPointX + startingMoveThresholdPx -> {
                mainLayoutController.showLeftBGView()
            }
            currentX < touchPointX - startingMoveThresholdPx -> {
                mainLayoutController.showRightBGView()
            }
            else -> {
                onReset()
            }
        }
    }

    fun onReset() {
        mainLayoutController.hideLeftBGView()
        mainLayoutController.hideRightBGView()
    }
}