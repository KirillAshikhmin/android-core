package com.digexco.app.tech.files.filePicker

data class SelectFileParams(
    val fileMimeType: String?,
    val isMultipleChoice: Boolean = false
)
