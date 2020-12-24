package pl.paullettuce.swipelayout.lib.helpers.background

class BackgroundController(
    private val backgroundViews: BackgroundViewsVisibilityController,
    private val startingMoveThresholdPx: Float
) {
    fun onMove(touchPointX: Float, currentX: Float) {
        when {
            currentX > touchPointX + startingMoveThresholdPx -> {
                backgroundViews.showLeftSide()
            }
            currentX < touchPointX - startingMoveThresholdPx -> {
                backgroundViews.showRightSide()
            }
            else -> {
                onReset()
            }
        }
    }

    fun onReset() {
        backgroundViews.hideLeftSide()
        backgroundViews.hideRightSide()
    }
}