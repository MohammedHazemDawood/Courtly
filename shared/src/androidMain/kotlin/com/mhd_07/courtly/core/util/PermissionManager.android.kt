package com.mhd_07.courtly.core.util

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

internal class AndroidPermissionDelegate(private val activity: Activity) : PermissionDelegate {
    override fun permissionRequired(permission: Permission): Boolean {
        return permission != Permission.Gallery
    }

    override fun checkStatus(permission: Permission): PermissionStatus {
        if (!permissionRequired(permission)) return PermissionStatus.Granted
        return if (ContextCompat.checkSelfPermission(
                activity,
                permission.android()
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            PermissionStatus.Granted
        } else {
            PermissionStatus.Denied
        }
    }

    override fun launchPermissionSettings() {
        val uri = Uri.fromParts("package", activity.packageName, null)
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }
}


fun Permission.android() = when (this) {
    Permission.Camera -> Manifest.permission.CAMERA
    Permission.Gallery -> ""
}

@Composable
actual fun rememberPermissionManager(onPermissionResult: (permission: Permission, status: PermissionStatus) -> Unit): PermissionManager {
    val activity = requireNotNull(LocalActivity.current) {
        "No Activity found"
    }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                onPermissionResult(Permission.Camera, PermissionStatus.Granted)
            } else {
                onPermissionResult(Permission.Camera, PermissionStatus.Denied)
            }
        }
    val delegate = remember { AndroidPermissionDelegate(activity) }
    return remember(onPermissionResult, launcher, activity) {
        PermissionManagerImpl(delegate) { permission ->
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.android()))
                onPermissionResult(permission, PermissionStatus.ShowRational)
            else
                launcher.launch(permission.android())
        }
    }
}