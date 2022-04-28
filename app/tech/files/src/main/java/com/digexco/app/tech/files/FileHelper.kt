package com.digexco.app.tech.files

import android.content.ContentResolver
import android.content.Context
import com.digexco.arch.helpers.writeInputString
import com.digexco.common.Logger
import kotlinx.coroutines.*
import java.io.File
import java.io.InputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class FileHelper(
    private val supportTypes: List<String>,
    val vm: IFileHelperViewModel,
    val scope: CoroutineScope
) {


    companion object {
        const val MODE_MULTIPLE_CHOICE = true
        private const val TAG = "FileHelper"

        fun clearOldFiles(context: Context) {
            val dir = FileHelperFragmentBridge.getAttachmentsDir(context)
            val fileDir = File(dir)
            if (!fileDir.exists() || !fileDir.isDirectory) return
            MainScope().launch(Dispatchers.IO) {
                fileDir.deleteRecursively()
            }
        }
    }


    private val filesMutableList = mutableListOf<FileObject>()
    private var fragmentBridge: FileHelperFragmentBridge? = null

    private var filePath: String = ""

    fun setFragmentBridge(fragmentBridge: FileHelperFragmentBridge) {
        this.fragmentBridge = fragmentBridge
        filePath = if (filePath.isEmpty()) fragmentBridge.generateFilePath() else filePath
        fragmentBridge.filePath = filePath
        fragmentBridge.helper = this
    }


    fun startCamera() {
        fragmentBridge?.startCamera()
    }

    fun selectFile(isMultipleChoice: Boolean = false) {
        fragmentBridge?.isMultipleFilesChoice = isMultipleChoice
        fragmentBridge?.selectFile()
    }

    fun clear() {
        MainScope().launch(Dispatchers.IO) {
            if (filePath.isNotEmpty()) File(filePath).deleteRecursively()
        }
        filesMutableList.clear()
        fragmentBridge = null
    }

    private fun saveAndAdd(file: FileObject) {
        if (file.file != null) {
            addToFileList(file)
        } else {
            scope.launch(Dispatchers.Main) {
                val saved = saveFileToFs(file)
                saved?.let(::addToFileList)
            }
        }
    }

    private suspend fun saveFileToFs(file: FileObject) = suspendCoroutine<FileObject?> { cont ->
        val bridge = fragmentBridge
        if (bridge == null) {
            cont.resume(null)
            return@suspendCoroutine
        }

        scope.launch(Dispatchers.IO) {
            val uri = file.uri!!
            val fileName = file.name ?: fragmentBridge?.getFileName(file)
            val newFile = File(filePath + fileName)
            try {
                val cr: ContentResolver = bridge.fragment.requireContext().contentResolver
                val inputStream: InputStream? = cr.openInputStream(uri)
                newFile.writeInputString(inputStream!!)
                cont.resume(FileObject(newFile, uri))
            } catch (e: Exception) {
                Logger.e(TAG, e, "Unable save file")
                e.printStackTrace()
                cont.resume(null)
            }
        }
    }


    internal fun addToFileList(file: FileObject) {
        filesMutableList.add(file)

        if (file.name.isNullOrEmpty())
            file.name = fragmentBridge?.getFileName(file)

        vm.filesListChanged(filesMutableList.toList())
    }

    private fun removeFileFromList(file: FileObject) {
        filesMutableList.remove(file)
        vm.filesListChanged(filesMutableList.toList())
    }


    fun deleteFile(fileObject: FileObject) {
        scope.launch(Dispatchers.IO) {
            fileObject.file?.delete()
            delay(250)
            scope.launch {
                removeFileFromList(fileObject)
            }
        }
    }

    private fun isSupportFileType(file: FileObject, ext: String): Boolean {
        return if (!supportTypes.contains(ext)) {
            vm.onSelectedFileUnsupported(file)
            false
        } else
            true
    }

    fun onFilesSelected(filesList: List<FileObject>) {
        for (f in filesList) {
            val uri = f.uri ?: return

            val fileName = f.name ?: uri.path!!
            val ext = fileName.substringAfterLast('.', "").lowercase()
            if (isSupportFileType(f, ext))
                saveAndAdd(f)
        }
    }
}
