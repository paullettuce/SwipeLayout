package pl.paullettuce.swipelayout_lib.helpers.animation

import android.animation.ObjectAnimator
import android.view.View
import kotlin.math.absoluteValue

/**
 * @param animationSlowness: the higher value, the slower animation goes
 */
internal class SwipeAnimator(
    var animationSlowness: Long = 500L
) {

    fun animateToLeft(view: View) {
        ObjectAnimator.ofFloat(view, "translationX", -view.width.toFloat()).apply {
            duration = calculateDuration(view)
            start()
        }
    }

    fun animateToRight(view: View) {
        ObjectAnimator.ofFloat(view, "translationX", view.width.toFloat()).apply {
            duration = calculateDuration(view)
            start()
        }
    }

    private fun calculateDuration(view: View): Long {
        val positiveX = view.x.absoluteValue
        val width = view.width
        val duration = ((width - positiveX) * animationSlowness / width).toLong()
        return if (duration > 0) duration else 0L
    }
}