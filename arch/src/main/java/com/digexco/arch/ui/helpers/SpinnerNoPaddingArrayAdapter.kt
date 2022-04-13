package com.digexco.arch.ui.helpers

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.ArrayRes
import androidx.annotation.LayoutRes

open class NoPaddingArrayAdapter(context: Context, layoutId: Int, items: List<String>) :
    ArrayAdapter<String>(context, layoutId, items) {

    constructor(context: Context, @LayoutRes layoutId: Int, @ArrayRes textArrayResId: Int) :
            this(
                context,
                layoutId,
                context.resources.getTextArray(textArrayResId).map { it.toString() }.toList()
            )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        view.setPadding(0, view.paddingTop, view.paddingRight, view.paddingBottom)
        return view
    }
}


fun Spinner.setNoPaddingAdapter(
    @ArrayRes items: Int,
    @LayoutRes simpleSpinnerItem: Int,
    @LayoutRes simpleSpinnerDropdownItem: Int
) {

    val adapter = NoPaddingArrayAdapter(
        context,
        simpleSpinnerItem,
        items,
    )
    adapter.setDropDownViewResource(simpleSpinnerDropdownItem)
    this.adapter = adapter
}
fun Spinner.setNoPaddingAdapter(
    items : List<String>,
    @LayoutRes simpleSpinnerItem: Int,
    @LayoutRes simpleSpinnerDropdownItem: Int
) {

    val adapter = NoPaddingArrayAdapter(
        context,
        simpleSpinnerItem,
        items,
    )
    adapter.setDropDownViewResource(simpleSpinnerDropdownItem)
    this.adapter = adapter
}
