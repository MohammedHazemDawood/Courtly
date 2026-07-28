package com.mhd_07.courtly.core.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.mhd_07.courtly.core.presentation.viewmodel.CoreViewmodel
import com.mhd_07.courtly.feature_nav.presentation.data.Graphs
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CoreUI(navToGameSetup: () -> Unit) {
    val backStack = rememberNavBackStack(SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(Graphs.Core.Home::class, Graphs.Core.Home.serializer())
                subclass(Graphs.Core.Settings::class, Graphs.Core.Settings.serializer())
            }
        }
    }, Graphs.Core.Home)
    val viewmodel = koinViewModel<CoreViewmodel>()
    NavDisplay(
        backStack = backStack,
        modifier = Modifier.fillMaxSize(),
        entryProvider = entryProvider {
            entry<Graphs.Core.Home> {
                HomeScreen(
                    navToGameSetup = navToGameSetup,
                    navToProfileScreen = { backStack.add(Graphs.Core.Settings) })
            }
            entry<Graphs.Core.Settings> {
                //TODO: Setup Profile Setting Screen
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Button(onClick = { viewmodel.logOut() }) {
                        Text("Logout") //TODO: Change to stringResource
                    }
                }
            }
        })
}