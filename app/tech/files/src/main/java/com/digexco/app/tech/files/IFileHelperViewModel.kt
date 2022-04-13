package com.digexco.app.tech.files

interface IFileHelperViewModel {
    fun onSelectedFileUnsupported(file: FileObject)
    fun filesListChanged(files: List<FileObject>)
    fun getFileHelper() : FileHelper
}
