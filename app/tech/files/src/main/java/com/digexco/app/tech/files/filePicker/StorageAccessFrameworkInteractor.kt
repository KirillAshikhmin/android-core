package com.digexco.app.tech.files.filePicker

import androidx.activity.result.ActivityResultLauncher

//from https://www.holdapp.com/blog/how-to-access-files-in-android-storage-access-framework-and-activity-result-api
class StorageAccessFrameworkInteractor(private val fileSelectionEntryPoint: FileSelectionEntryPoint) {

    private val selectFileLauncher: ActivityResultLauncher<SelectFileParams> =
        fileSelectionEntryPoint.fileSelectionOwner
            .registerForActivityResult(SelectFileResultContract()) { uriList ->
                fileSelectionEntryPoint.onFileSelected(uriList)
            }

    fun beginSelectingFile(selectFileParams: SelectFileParams) =
        selectFileLauncher.launch(selectFileParams)
}
