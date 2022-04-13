package com.digexco.app.main

import android.content.Intent
import android.net.Uri
import com.digexco.arch.ui.ScreensCore
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.ActivityScreen


@Suppress("FunctionName")
object Screens : ScreensCore() {
    fun FilePreview(fileName: String, fileUrl: String, contentType: String) =
        getActivityScreen("FilePreviewActivity") { intent ->
            intent.putExtra(Arguments.FILE_NAME, fileName)
            intent.putExtra(Arguments.FILE_URL, fileUrl)
            intent.putExtra(Arguments.FILE_CONTENT_TYPE, contentType)
        }

    fun OpenLink(link: String): Screen {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(link)
        return ActivityScreen { i }
    }

    object Arguments {
        const val FILE_NAME = "file_name"
        const val FILE_URL = "file_url"
        const val FILE_CONTENT_TYPE = "file_content_type"
    }

    object ResultKeys {
        const val SCAN_AGAIN = "scan_again"
    }

}
