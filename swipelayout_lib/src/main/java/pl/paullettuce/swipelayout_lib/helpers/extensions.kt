package pl.paullettuce.swipelayout_lib.helpers

import android.view.MotionEvent
import android.view.View
import androidx.core.view.isVisible

internal fun View.show() {
    if (!isVisible) isVisible = true
}

internal fun View.hide() {
    if (isVisible) isVisible = false
}

internal fun MotionEvent.xDiffTo(startX: Float) = x() - startX
internal fun MotionEvent.yDiffTo(startY: Float) = y() - startY

internal fun MotionEvent.x() = getX(actionIndex)
internal fun MotionEvent.y() = getY(actionIndex)