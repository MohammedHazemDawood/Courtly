package com.mhd_07.courtly.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.reinterpret
import org.jetbrains.skia.Image
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerCameraCaptureMode
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@Composable
actual fun rememberCameraManager(onResult: (SharedImage) -> Unit): CameraManager {
    val imagePicker = UIImagePickerController()
    val cameraDelegate = remember {
        object : NSObject(), UIImagePickerControllerDelegateProtocol,
            UINavigationControllerDelegateProtocol {
            override fun imagePickerController(
                picker: UIImagePickerController, didFinishPickingMediaWithInfo: Map<Any?, *>
            ) {
                val image =
                    didFinishPickingMediaWithInfo.getValue(UIImagePickerControllerEditedImage) as? UIImage
                        ?: didFinishPickingMediaWithInfo.getValue(
                            UIImagePickerControllerOriginalImage
                        ) as? UIImage
                onResult.invoke(SharedImage(image))
                picker.dismissViewControllerAnimated(true, null)
            }
        }
    }
    return remember {
        CameraManager {
            dispatch_async(dispatch_get_main_queue()) {
                val cameraSource =
                    UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera

                if (!UIImagePickerController.isSourceTypeAvailable(cameraSource)) {
                    println("Camera source is not available on this device")
                    return@dispatch_async
                }

                imagePicker.setSourceType(cameraSource)
                imagePicker.setAllowsEditing(true)
                imagePicker.setCameraCaptureMode(UIImagePickerControllerCameraCaptureMode.UIImagePickerControllerCameraCaptureModePhoto)
                imagePicker.setDelegate(cameraDelegate)
                UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                    imagePicker, true, null
                )
            }
        }
    }
}
actual class SharedImage(private val image: UIImage?) {
    @OptIn(ExperimentalForeignApi::class)
    actual fun toByteArray(): ByteArray? = image?.let {
        val imageData = UIImageJPEGRepresentation(it, QUALITY)
            ?: throw IllegalStateException("Failed to convert UIImage to JPEG data")
        val bytes = imageData.bytes
            ?: throw IllegalStateException("Failed to get bytes from UIImageJPEGRepresentation")
        val length = imageData.length.toInt()

        val data: CPointer<ByteVar> = bytes.reinterpret()

        ByteArray(length) { index -> data[index.toLong()] }
    }

    actual fun toImageBitmap(): ImageBitmap? = image?.let {
        toByteArray()?.let { Image.makeFromEncoded(it).toComposeImageBitmap() }
    }

    companion object {
        const val QUALITY = 0.99
    }
}