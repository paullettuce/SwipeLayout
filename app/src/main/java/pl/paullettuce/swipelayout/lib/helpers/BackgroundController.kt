package pl.paullettuce.swipelayout.lib.helpers

class BackgroundController(
    private val background: BackgroundControllerI,
    private val initialMoveThreshold: Float
) {
    fun onMove(touchPointX: Float, currentX: Float) {
        when {
            currentX > touchPointX + initialMoveThreshold -> {
                background.showLeftSide()
            }
            currentX < touchPointX - initialMoveThreshold -> {
                background.showRightSide()
            }
            else -> {
                reset()
            }
        }
    }

    fun reset() {
        background.hideLeftSide()
        background.hideRightSide()
    }
}