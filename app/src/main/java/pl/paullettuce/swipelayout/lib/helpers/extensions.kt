package pl.paullettuce.swipelayout.lib.helpers

import android.view.View
import androidx.core.view.isVisible

fun View.show() {
    if (!isVisible) isVisible = true
}

fun View.hide() {
    if (isVisible) isVisible = false
}