package pl.paullettuce.swipelayout_lib.helpers

import android.content.Context
import android.util.AttributeSet
import pl.paullettuce.SwipeLayout
import pl.paullettuce.swipelayout_lib.R

fun SwipeLayout.obtainSwipeAllowanceState(
    context: Context,
    attrs: AttributeSet?
): AllowedSwipeDirectionState {
    context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.SwipeLayout, 0, 0
    ).apply {
        return when (getInt(
            R.styleable.SwipeLayout_swipeEnabled,
            SwipeDirectionEnum.BOTH_SIDES.value
        )) {
            SwipeDirectionEnum.TO_LEFT.value -> {
                SwipeToLeftOnly()
            }
            SwipeDirectionEnum.TO_RIGHT.value -> {
                SwipeToRightOnly()
            }
            else -> SwipeBothSides()
        }
    }
}

interface AllowedSwipeDirectionState {
    fun getLeftLayoutIndex(childCount: Int): Int
    fun getRightLayoutIndex(childCount: Int): Int
    fun isDragValid(travelledX: Float): Boolean
}

/**
 * When only swipe to left <-- is enabled , the right side of layout gets revealed
 */
class SwipeToLeftOnly :
    AllowedSwipeDirectionState {
    override fun getLeftLayoutIndex(childCount: Int) = -1

    override fun getRightLayoutIndex(childCount: Int): Int {
        return when (childCount) {
            3 -> 1
            2 -> 0
            else -> -1
        }
    }

    /**
     * If @param positionDiffToActionDownX is < 0f, it means that layout is dragged to left
     * Making it less than -1f, also skips too-small-to-notice drags
     */
    override fun isDragValid(travelledX: Float): Boolean {
        return travelledX < -1f
    }
}

/**
 * When only swipe to right --> is enabled , the left side of layout gets revealed
 */
class SwipeToRightOnly :
    AllowedSwipeDirectionState {
    override fun getLeftLayoutIndex(childCount: Int): Int {
        return when (childCount) {
            3, 2 -> 0
            else -> -1
        }
    }

    override fun getRightLayoutIndex(childCount: Int) = -1

    /**
     * If @param positionDiffToActionDownX is > 0f, it means that layout is dragged to right
     * Making it higher than 1f, also skips too-small-to-notice drags
     */
    override fun isDragValid(travelledX: Float): Boolean {
        return travelledX > 1f
    }
}

/**
 * Layout moves horizontally both ways
 */
class SwipeBothSides :
    AllowedSwipeDirectionState {
    override fun getLeftLayoutIndex(childCount: Int): Int {
        return when (childCount) {
            3 -> 0
            else -> -1
        }
    }

    override fun getRightLayoutIndex(childCount: Int): Int {
        return when (childCount) {
            3 -> 1
            else -> -1
        }
    }

    /**
     * Allows movement if it's significant
     */
    override fun isDragValid(travelledX: Float): Boolean {
        return travelledX > 1f || travelledX < 1f
    }
}