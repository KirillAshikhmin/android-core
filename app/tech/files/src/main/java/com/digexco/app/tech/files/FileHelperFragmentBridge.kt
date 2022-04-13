package com.digexco.app.tech.files

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.digexco.app.tech.files.filePicker.FileSelectionEntryPoint
import com.digexco.app.tech.files.filePicker.SelectFileParams
import com.digexco.app.tech.files.filePicker.StorageAccessFrameworkInteractor
import com.digexco.arch.helpers.fileName
import com.digexco.arch.helpers.getOriginalFileName
import com.digexco.arch.helpers.isGranted
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FileHelperFragmentBridge(val fragment: Fragment) : FileSelectionEntryPoint {

    companion object {
        const val FILE_NAME_PREFIX = "doc_"
        private const val filesDirName = "attachments"
        private const val fileNameDateFormat = "yyyyMMdd_HHmmss"
        private const val fileJpgExt = ".jpg"
        fun getAttachmentsDir(context: Context): String =
            "${context.cacheDir.absolutePath}${File.separator}$filesDirName"
    }


    var helper: FileHelper? = null

    var filePath: String = ""

    var isMultipleFilesChoice = false

    override val fileSelectionOwner: Fragment = fragment

    private val fileSelectionInteractor: StorageAccessFrameworkInteractor =
        StorageAccessFrameworkInteractor(this)

    fun startCamera() {
        if (
            fragment.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
            fragment.isGranted(Manifest.permission.CAMERA)
        )
            startCameraProcess()
        else
            cameraPermission.launch(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
            )
    }

    fun selectFile() {
        if (fragment.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE))
            selectFileProcess()
        else
            readStoragePermission.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
    }

    fun generateFilePath(): String {
        val filePath =
            getAttachmentsDir(fragment.requireContext()) + File.separator + UUID.randomUUID() + File.separator

        val path = File(filePath)
        if (!path.exists()) path.mkdirs()
        return filePath
    }


    private fun startCameraProcess() {
        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        val file = createImageFile()

        val imageUri = FileProvider.getUriForFile(
            fragment.requireContext(),
            "com.arrival.traceability.provider",
            file
        )

        _cameraImageFile = file
        _cameraImageUri = imageUri
        camera.launch(imageUri)
    }

    private var _cameraImageFile: File? = null
    private var _cameraImageUri: Uri? = null

    private val camera =
        fragment.registerForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
            if (ok) onCameraResult()
        }

    private fun selectFileProcess() {
        fileSelectionInteractor.beginSelectingFile(SelectFileParams(null, isMultipleFilesChoice))
    }

    override fun onFileSelected(uriList: List<Uri>?) {
        if (uriList == null) return
        helper?.onFilesSelected(
            uriList.toMutableList().map {
                FileObject(null, it).apply {
                    name = it.getOriginalFileName(fragment.requireContext())
                }
            }
        )
    }

    private fun onCameraResult() {
        if (_cameraImageFile == null || _cameraImageUri == null) return
        val file = FileObject(_cameraImageFile, _cameraImageUri)
        helper?.onFilesSelected(listOf(file))
    }


    private val cameraPermission =
        fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { granted ->
            val anyNotGranted = granted.values.contains(false)
            if (!anyNotGranted) startCameraProcess()
        }

    private val readStoragePermission =
        fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { granted ->
            val anyNotGranted = granted.values.contains(false)
            if (!anyNotGranted) selectFileProcess()
        }


    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat(fileNameDateFormat).format(Date())
        val imageFileName = FILE_NAME_PREFIX + timeStamp + "_"

        val storageDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            fragment.requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        else
            @Suppress("DEPRECATION")
            (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES))

        return File.createTempFile(
            imageFileName, // prefix
            fileJpgExt, // suffix
            storageDir      // directory
        )
    }

    fun getFileName(file: FileObject) =
        file.uri?.getOriginalFileName(fragment.requireContext()) ?: file.file?.fileName
}
