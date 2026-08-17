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
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

internal class IOSPermissionDelegate : PermissionDelegate {
    override fun permissionRequired(permission: Permission): Boolean = true
    override fun checkStatus(permission: Permission): PermissionStatus {
        return when (permission) {
            Permission.Camera -> checkCameraPermission()
            Permission.Gallery -> checkGalleryPermission()
        }
    }

    override fun launchPermissionSettings() {
        dispatch_async(dispatch_get_main_queue()) {
            NSURL.URLWithString(UIApplicationOpenSettingsURLString)?.let { url ->
                if (UIApplication.sharedApplication.canOpenURL(url)) {
                    UIApplication.sharedApplication.openURL(url)
                }
            }
        }
    }

    private fun checkCameraPermission(): PermissionStatus {
        val status = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
        return if (status == AVAuthorizationStatusAuthorized) PermissionStatus.Granted else PermissionStatus.Denied
    }

    private fun checkGalleryPermission(): PermissionStatus {
        val status = PHPhotoLibrary.authorizationStatus()
        return if (status == PHAuthorizationStatusAuthorized || status == PHAuthorizationStatusLimited) {
            PermissionStatus.Granted
        } else {
            PermissionStatus.Denied
        }
    }
}
@Composable
actual fun rememberPermissionManager(onPermissionResult: (permission: Permission, status: PermissionStatus) -> Unit): PermissionManager {
    val delegate = remember { IOSPermissionDelegate() }
    return remember(onPermissionResult) {
        PermissionManagerImpl(delegate) { permission ->
            when (permission) {
                Permission.Camera -> {
                    val status = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
                    requestCameraPermission(status)
                }

                Permission.Gallery -> {
                    val status = PHPhotoLibrary.authorizationStatus()
                    requestGalleryPermission(status)
                }
            }
        }
    }
}

private fun requestCameraPermission(status: AVAuthorizationStatus) : PermissionStatus{
    return when (status) {
        AVAuthorizationStatusAuthorized -> PermissionStatus.Granted

        AVAuthorizationStatusNotDetermined -> {
            lateinit var status : PermissionStatus
            AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                dispatch_async(dispatch_get_main_queue()) {
                    val resultStatus =
                        if (granted) PermissionStatus.Granted else PermissionStatus.Denied
                    status =  resultStatus
                }
            }
            status
        }

        AVAuthorizationStatusDenied ->  PermissionStatus.Denied

        else ->  PermissionStatus.Denied
    }
}

private fun requestGalleryPermission(status : PHAuthorizationStatus) : PermissionStatus {
    return  when (status) {
        PHAuthorizationStatusAuthorized, PHAuthorizationStatusLimited -> {
            PermissionStatus.Granted
        }

        PHAuthorizationStatusNotDetermined -> {
            lateinit var status : PermissionStatus
            PHPhotoLibrary.requestAuthorization { newStatus ->
                dispatch_async(dispatch_get_main_queue()) {
                    val resultStatus = if (
                        newStatus == PHAuthorizationStatusAuthorized ||
                        newStatus == PHAuthorizationStatusLimited
                    ) {
                        PermissionStatus.Granted
                    } else {
                        PermissionStatus.Denied
                    }
                    status =  resultStatus
                }
            }
            status
        }

        PHAuthorizationStatusDenied -> {
            PermissionStatus.Denied
        }

        else -> {
            PermissionStatus.Denied
        }
    }
}