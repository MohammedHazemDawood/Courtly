package com.mhd_07.courtly.core.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import java.io.ByteArrayOutputStream
import java.io.File
actual class SharedImage(private val bimap: Bitmap?) {
    actual fun toByteArray(): ByteArray? = bimap?.let {
        val stream = ByteArrayOutputStream()
        it.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        stream.toByteArray()
    }

    actual fun toImageBitmap(): ImageBitmap? = bimap?.asImageBitmap()
}

@Composable
actual fun rememberCameraManager(
    onResult: (SharedImage) -> Unit
): CameraManager {

    val context = LocalContext.current

    var photoUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->

            if (!success) {
                Log.d("Camera", "Photo capture failed/cancelled")
                photoUri = Uri.EMPTY
                return@rememberLauncherForActivityResult
            }

            photoUri.let { uri ->

                val bitmap = uriToBitmap(
                    uri = uri,
                    contentResolver = context.contentResolver
                )

                Log.d("Camera", "Bitmap: $bitmap")

                if (bitmap != null) {
                    onResult(SharedImage(bitmap))
                }

                photoUri = Uri.EMPTY
            }
        }
    )

    return remember {
        CameraManager {

            val directory = File(
                context.cacheDir,
                "camera"
            ).apply {
                mkdirs()
            }

            val file = File(
                directory,
                "photo_${System.currentTimeMillis()}.jpg"
            )

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            photoUri = uri

            cameraLauncher.launch(uri)
        }
    }
}

fun uriToBitmap(uri: Uri, contentResolver: ContentResolver): Bitmap? = try {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    } else
        contentResolver
            .openInputStream(uri)
            ?.use { stream ->
                BitmapFactory.decodeStream(stream)
            }
} catch (e: Exception) {
//    e.printStackTrace()
    println("Error Making Bitmap: ${e.message}")
    null
}

private fun isUriValidAndNotEmpty(contentResolver: ContentResolver, uri: Uri): Boolean {
    return try {
        contentResolver.openFileDescriptor(uri, "r")?.use { pfd ->
            pfd.statSize > 0
        } ?: false
    } catch (e: Exception) {
        false
    }
}