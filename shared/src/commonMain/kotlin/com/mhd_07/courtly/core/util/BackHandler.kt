package com.mhd_07.courtly.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

@Composable
fun BackHandler(scope: CoroutineScope = rememberCoroutineScope(), onBack: suspend () -> Unit) {
    val navigationState = rememberNavigationEventState(currentInfo = NavigationEventInfo.None)
    val mutex = remember { Mutex() }
    NavigationBackHandler(navigationState) {
        scope.launch {
            if (mutex.tryLock()) {
                try {
                    onBack()
                } finally {
                    mutex.unlock()
                }
            }
        }
    }
}