package com.digexco.app.tech.files

import android.net.Uri
import java.io.File

data class FileObject(val file: File?, val uri: Uri?) {
    var name: String? = null
}
