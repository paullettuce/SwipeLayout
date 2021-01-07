package pl.paullettuce.swipelayout_lib.helpers.background

import pl.paullettuce.SwipeLayout

internal class BackgroundController(
    private val mainLayoutController: SwipeLayout,
    private val startingMoveThresholdPx: Float
) {
    fun onMove(travelledX: Float) {
        when {
            travelledX > startingMoveThresholdPx -> {
                mainLayoutController.showLeftBGView()
            }
            travelledX < -startingMoveThresholdPx -> {
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