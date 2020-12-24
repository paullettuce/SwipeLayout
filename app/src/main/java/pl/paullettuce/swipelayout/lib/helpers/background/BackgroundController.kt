package pl.paullettuce.swipelayout.lib.helpers.background

class BackgroundController(
    private val backgroundViews: BackgroundViewsVisibilityController,
    private val startingMoveThresholdPx: Float
) {
    fun onMove(touchPointX: Float, currentX: Float) {
        when {
            currentX > touchPointX + startingMoveThresholdPx -> {
                backgroundViews.onLeftUnderViewRevealed()
            }
            currentX < touchPointX - startingMoveThresholdPx -> {
                backgroundViews.onRightUnderViewRevealed()
            }
            else -> {
                onReset()
            }
        }
    }

    fun onReset() {
        backgroundViews.hideLeftUnderView()
        backgroundViews.hideRightUnderView()
    }
}