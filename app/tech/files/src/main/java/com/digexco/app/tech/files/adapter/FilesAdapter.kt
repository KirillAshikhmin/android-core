package com.digexco.app.tech.files.adapter

import coil.load
import com.digexco.app.tech.files.FileObject
import com.digexco.app.tech.files.IFileHelperViewModel
import com.digexco.app.tech.files.R
import me.ibrahimyilmaz.kiel.adapterOf
import kotlin.reflect.KProperty0

object FilesAdapter {
    fun get(
        fileHelper: KProperty0<IFileHelperViewModel>,
    ) = adapterOf<FileObject> {
        register(
            layoutResource = R.layout.item_file,
            viewHolder = ::FileViewHolder,
            onBindViewHolder = { vh, _, model ->
                vh.binding.delete.setOnClickListener {
                    fileHelper.get().getFileHelper().deleteFile(model)
                }
                vh.binding.nameView.text = model.name ?: "Unknown name"
                vh.binding.preview.load(model.file)
            }
        )
    }
}
