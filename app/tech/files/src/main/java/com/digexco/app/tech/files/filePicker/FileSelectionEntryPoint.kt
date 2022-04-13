package com.digexco.app.tech.files.filePicker

import android.net.Uri
import androidx.fragment.app.Fragment

interface FileSelectionEntryPoint {

    val fileSelectionOwner: Fragment

    fun onFileSelected(uriList: List<Uri>?)
}
