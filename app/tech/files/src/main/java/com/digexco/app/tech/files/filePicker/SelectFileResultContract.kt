package com.digexco.app.tech.files.filePicker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class SelectFileResultContract : ActivityResultContract<SelectFileParams, List<Uri>?>() {

    override fun createIntent(context: Context, data: SelectFileParams): Intent =
        Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            if(data.isMultipleChoice) putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            addCategory(Intent.CATEGORY_OPENABLE)
            setTypeAndNormalize(data.fileMimeType ?: "*/*")
        }

    override fun parseResult(resultCode: Int, intent: Intent?): List<Uri>? = when (resultCode) {
        Activity.RESULT_OK -> mutableListOf<Uri>().apply {
            intent?.data?.let { add(it) }
            intent?.clipData?.let {
                for(i in 0 until it.itemCount) {
                    add(it.getItemAt(i).uri)
                }
            }
        }
        else -> null
    }
}
