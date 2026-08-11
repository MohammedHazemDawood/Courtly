package com.mhd_07.courtly.core.util

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch

actual class PermissionManager actual constructor(private val listener: PermissionResultListener) :
    PermissionHandler {
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    actual override fun requestPermission(
        permissionType: PermissionType,
    ) {
        if (permissionType == PermissionType.CAMERA) {
            val lifecycleOwner = LocalLifecycleOwner.current
            val cameraState = rememberPermissionState(Manifest.permission.CAMERA)
            LaunchedEffect(Unit) {
                val status = cameraState.status
                if (status.isGranted) {
                    listener.onPermissionResult(PermissionType.CAMERA, PermissionStatus.GRANTED)
                } else {
                    if (status.shouldShowRationale)
                        listener.onPermissionResult(
                            PermissionType.CAMERA,
                            PermissionStatus.SHOW_RATIONAL
                        )
                    else
//                        lifecycleOwner.lifecycleScope.launch {
                            cameraState.launchPermissionRequest()
//                        }
                }
            }
        } else {
            listener.onPermissionResult(PermissionType.GALLERY, PermissionStatus.GRANTED)
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    actual override fun checkPermission(permissionType: PermissionType): PermissionStatus =
        when (permissionType) {
            PermissionType.CAMERA -> {
                val cameraState = rememberPermissionState(Manifest.permission.CAMERA)
                if (cameraState.status.isGranted) {
                    PermissionStatus.GRANTED
                } else {
                    PermissionStatus.DENIED
                }
            }

            PermissionType.GALLERY -> PermissionStatus.GRANTED
        }

    @Composable
    actual override fun launchPermissionSettings() {
        val context = LocalContext.current
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        ).also {
            context.startActivity(it)
        }
    }

}