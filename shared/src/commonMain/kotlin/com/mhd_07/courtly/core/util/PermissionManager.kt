package com.mhd_07.courtly.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
expect fun rememberPermissionManager(onPermissionResult: (permission: Permission, status: PermissionStatus) -> Unit) : PermissionManager

sealed interface Permission {
    data object Camera : Permission
    data object Gallery : Permission
}

sealed interface PermissionStatus {
    data object Granted : PermissionStatus
    data object Denied : PermissionStatus
    data object ShowRational : PermissionStatus
}

internal interface PermissionDelegate {
    fun permissionRequired(permission: Permission) : Boolean
    fun checkStatus(permission: Permission): PermissionStatus

    fun launchPermissionSettings()
}

interface PermissionManager {
    fun isGranted(permission: Permission): Boolean
    fun shouldShowRational(permission: Permission): Boolean
    fun checkAndRequest(permission: Permission)
    fun launchSettings()
}

internal class PermissionManagerImpl(
    private val delegate: PermissionDelegate,
    private val onLaunch: (Permission) -> Unit
) : PermissionManager {
    override fun isGranted(permission: Permission): Boolean {
        return delegate.checkStatus(permission) == PermissionStatus.Granted
    }

    override fun shouldShowRational(permission: Permission): Boolean {
        return delegate.checkStatus(permission) == PermissionStatus.ShowRational
    }

    override fun checkAndRequest(permission: Permission) {
        if (isGranted(permission)) return
        onLaunch(permission)
    }

    override fun launchSettings() {
        delegate.launchPermissionSettings()
    }
}