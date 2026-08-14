package com.mhd_07.courtly.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.AVFoundation.AVAuthorizationStatus
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.Foundation.NSURL
import platform.Photos.PHAuthorizationStatus
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue


actual class PermissionManager actual constructor(private val listener: PermissionResultListener) :
    PermissionHandler {
    @Composable
    actual override fun requestPermission(
        permissionType: PermissionType,
    ) {
        when (permissionType) {
            PermissionType.CAMERA -> {
                val status =
                    remember { AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo) }
                requestCameraPermission(status, listener)
            }

            PermissionType.GALLERY -> {
                val status = remember { PHPhotoLibrary.authorizationStatus() }
                requestGalleryPermission(status, listener)
            }
        }
    }

    private fun requestCameraPermission(
        status: AVAuthorizationStatus,
        listener: PermissionResultListener
    ) {
        when (status) {
            AVAuthorizationStatusAuthorized -> listener.onPermissionResult(
                PermissionType.CAMERA,
                PermissionStatus.GRANTED
            )

            AVAuthorizationStatusNotDetermined -> {
                return AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                    if (granted) {
                        listener.onPermissionResult(PermissionType.CAMERA, PermissionStatus.GRANTED)
                    } else {
                        listener.onPermissionResult(PermissionType.CAMERA, PermissionStatus.DENIED)
                    }
                }
            }

            AVAuthorizationStatusDenied -> listener.onPermissionResult(
                PermissionType.CAMERA,
                PermissionStatus.DENIED
            )

            else -> error("Unknown status")
        }
    }

    private fun requestGalleryPermission(
        status: PHAuthorizationStatus,
        listener: PermissionResultListener
    ) {
        when (status) {
            PHAuthorizationStatusAuthorized -> listener.onPermissionResult(
                PermissionType.GALLERY,
                PermissionStatus.GRANTED
            )

            PHAuthorizationStatusNotDetermined -> PHPhotoLibrary.requestAuthorization { granted ->
                requestGalleryPermission(granted, listener)
            }

            PHAuthorizationStatusDenied -> listener.onPermissionResult(
                PermissionType.GALLERY,
                PermissionStatus.DENIED
            )

            else -> error("Unknown status")
        }
    }


    @Composable
    actual override fun checkPermission(permissionType: PermissionType): PermissionStatus {
        return when(permissionType) {
            PermissionType.CAMERA -> remember { AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo) }.let { status ->
                when (status) {
                    AVAuthorizationStatusAuthorized -> PermissionStatus.GRANTED
                    AVAuthorizationStatusDenied -> PermissionStatus.DENIED
                    else -> PermissionStatus.DENIED // Handle other cases as needed
                }
            }
            PermissionType.GALLERY -> remember { PHPhotoLibrary.authorizationStatus() }.let { status ->
                when (status) {
                    PHAuthorizationStatusAuthorized -> PermissionStatus.GRANTED
                    PHAuthorizationStatusDenied -> PermissionStatus.DENIED
                    else -> PermissionStatus.DENIED // Handle other cases as needed
                }
            }
        }
    }

    @Composable
    actual override fun launchPermissionSettings() {
        dispatch_async(dispatch_get_main_queue()){
            NSURL.URLWithString(UIApplicationOpenSettingsURLString)?.let {
                UIApplication.sharedApplication.openURL(it)
            }
        }
    }
}