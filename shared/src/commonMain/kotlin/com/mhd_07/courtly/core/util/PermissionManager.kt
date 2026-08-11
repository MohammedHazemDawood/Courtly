package com.mhd_07.courtly.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberPermissionManager(listener: PermissionResultListener) =
    remember { PermissionManager(listener)   }

expect class PermissionManager(listener: PermissionResultListener) : PermissionHandler {
    @Composable
    override fun requestPermission(
        permissionType: PermissionType,
    )

    @Composable
    override fun checkPermission(permissionType: PermissionType): PermissionStatus

    @Composable
    override fun launchPermissionSettings()
}


enum class PermissionType {
    CAMERA,
    GALLERY
}

enum class PermissionStatus {
    GRANTED,
    DENIED,
    SHOW_RATIONAL
//    PERMANENTLY_DENIED
}

interface PermissionResultListener {
    fun onPermissionResult(permissionType: PermissionType, status: PermissionStatus)
}

interface PermissionHandler {

    @Composable
    fun requestPermission(permissionType: PermissionType)

    @Composable
    fun checkPermission(permissionType: PermissionType): PermissionStatus

    @Composable
    fun launchPermissionSettings()
}