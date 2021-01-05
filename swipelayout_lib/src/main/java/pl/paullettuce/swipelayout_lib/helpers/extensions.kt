package pl.paullettuce.swipelayout_lib.helpers

import android.view.View
import androidx.core.view.isVisible

internal fun View.show() {
    if (!isVisible) isVisible = true
}

internal fun View.hide() {
    if (isVisible) isVisible = false
}