package com.digexco.arch.ui.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.faltenreich.skeletonlayout.*

private const val LIST_ITEM_COUNT_DEFAULT = 3

fun RecyclerView.applyCustomSkeleton(
    @LayoutRes listItemLayoutResId: Int,
    itemCount: Int = LIST_ITEM_COUNT_DEFAULT,
    config: SkeletonConfig = SkeletonConfig.default(context),
    onCreateView: (View) -> Unit
): Skeleton = CustomSkeletonRecyclerView(this, listItemLayoutResId, itemCount, config, onCreateView)


internal class CustomSkeletonRecyclerView(
    private val recyclerView: RecyclerView,
    @LayoutRes layoutResId: Int,
    itemCount: Int,
    config: SkeletonConfig,
    onCreateView: (View) -> Unit
) : Skeleton, SkeletonStyle by config {

    private val originalAdapter = recyclerView.adapter
    private var skeletonAdapter =
        CustomSkeletonRecyclerViewAdapter(layoutResId, itemCount, config, onCreateView)

    init {
        config.addValueObserver { skeletonAdapter.notifyDataSetChanged() }
    }

    override fun showOriginal() {
        recyclerView.adapter = originalAdapter
    }

    override fun showSkeleton() {
        recyclerView.adapter = skeletonAdapter
    }

    override fun isSkeleton(): Boolean {
        return recyclerView.adapter == skeletonAdapter
    }
}

internal class CustomSkeletonRecyclerViewHolder(itemView: SkeletonLayout) :
    RecyclerView.ViewHolder(itemView)

internal class CustomSkeletonRecyclerViewAdapter(
    @LayoutRes private val layoutResId: Int,
    private val itemCount: Int,
    private val config: SkeletonConfig,
    private val onCreateView: (View) -> Unit
) : RecyclerView.Adapter<CustomSkeletonRecyclerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomSkeletonRecyclerViewHolder {
        val originView = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        onCreateView(originView)
        val skeleton = originView.createSkeleton(config) as SkeletonLayout
        skeleton.layoutParams = originView.layoutParams
        skeleton.showSkeleton()
        return CustomSkeletonRecyclerViewHolder(skeleton)
    }

    override fun onBindViewHolder(holder: CustomSkeletonRecyclerViewHolder, position: Int) = Unit

    override fun getItemCount() = itemCount
}
