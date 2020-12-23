package pl.paullettuce.swipelayout.lib.helpers.background

class BackgroundController(
    private val backgroundViews: BackgroundViewsVisibilityController,
    private val initialMoveThreshold: Float
) {
    fun onMove(touchPointX: Float, currentX: Float) {
        when {
            currentX > touchPointX + initialMoveThreshold -> {
                backgroundViews.showLeftSide()
            }
            currentX < touchPointX - initialMoveThreshold -> {
                backgroundViews.showRightSide()
            }
            else -> {
                reset()
            }
        }
    }

    fun reset() {
        backgroundViews.hideLeftSide()
        backgroundViews.hideRightSide()
    }
}