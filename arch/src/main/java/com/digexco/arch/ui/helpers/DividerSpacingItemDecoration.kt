package com.digexco.arch.ui.helpers

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DimenRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


/**
 * Creates a divider [RecyclerView.ItemDecoration] that can be used with a
 * [LinearLayoutManager].
 *
 * @param context Current context, it will be used to access resources.
 * @param orientation Divider orientation. Should be [.HORIZONTAL] or [.VERTICAL].
 * @param size Divider size. Should be dimen.
 */
class DividerSpacingItemDecoration(context: Context, @LinearLayoutCompat.OrientationMode orientation: Int, @DimenRes size: Int) :
    ItemDecoration() {

    init {
        setSize(context.resources.getDimensionPixelSize(size))
        setOrientation(orientation)
    }

    /**
     * Current orientation. Either [.HORIZONTAL] or [.VERTICAL].
     */
    private var orientation = 0
    private var sizePx: Int = 0

    /**
     * Sets the orientation for this divider. This should be called if
     * [RecyclerView.LayoutManager] changes orientation.
     *
     * @param orientation [.HORIZONTAL] or [.VERTICAL]
     */
    fun setOrientation(@LinearLayoutCompat.OrientationMode orientation: Int) {
        this.orientation = orientation
    }

    /**
     * Sets the size for this divider.
     *
     * @param sizePx in pixels
     */
    fun setSize(sizePx: Int) {
        this.sizePx = sizePx
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) = if (orientation == LinearLayout.VERTICAL)
        outRect[0, 0, 0] = sizePx
    else
        outRect[0, 0, sizePx] = 0
}

fun RecyclerView.addDividerSpacingItemDecoration(
    orientation: Int,
    @DimenRes size: Int
) {
    val marginDecorator = DividerSpacingItemDecoration(context, orientation, size)
    this.addItemDecoration(marginDecorator)
}
