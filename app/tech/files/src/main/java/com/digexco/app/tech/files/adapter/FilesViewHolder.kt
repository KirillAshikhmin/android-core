package com.digexco.app.tech.files.adapter

import android.view.View
import com.digexco.app.tech.files.FileObject
import com.digexco.app.tech.files.databinding.ItemFileBinding
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder

class FileViewHolder(view: View) : RecyclerViewHolder<FileObject>(view) {
    val binding = ItemFileBinding.bind(view)
}
