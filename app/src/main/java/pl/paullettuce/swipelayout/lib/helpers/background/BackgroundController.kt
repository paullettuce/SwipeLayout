package pl.paullettuce.swipelayout.lib.helpers.background

import pl.paullettuce.swipelayout.lib.SwipeLayout

class BackgroundController(
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