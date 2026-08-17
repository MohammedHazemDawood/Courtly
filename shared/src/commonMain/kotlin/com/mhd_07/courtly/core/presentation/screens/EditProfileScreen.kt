package com.mhd_07.courtly.core.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.SubcomposeAsyncImage
import com.mhd_07.courtly.core.presentation.components.AnimatedBottomSheet
import com.mhd_07.courtly.core.presentation.components.CourtlyAppBar
import com.mhd_07.courtly.core.presentation.components.Loading
import com.mhd_07.courtly.core.presentation.model.RemoteResult
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.util.BackHandler
import com.mhd_07.courtly.core.util.Permission
import com.mhd_07.courtly.core.util.PermissionStatus
import com.mhd_07.courtly.core.util.rememberCameraManager
import com.mhd_07.courtly.core.util.rememberGalleryManager
import com.mhd_07.courtly.core.util.rememberPermissionManager
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.bio
import courtly.shared.generated.resources.camera
import courtly.shared.generated.resources.camera_outline
import courtly.shared.generated.resources.edit_avatar
import courtly.shared.generated.resources.edit_profile
import courtly.shared.generated.resources.gallery
import courtly.shared.generated.resources.gallery_wide_outline
import courtly.shared.generated.resources.handle
import courtly.shared.generated.resources.name
import courtly.shared.generated.resources.name_error
import courtly.shared.generated.resources.pen_new_square_outline
import courtly.shared.generated.resources.profile
import courtly.shared.generated.resources.save
import courtly.shared.generated.resources.select_source
import courtly.shared.generated.resources.user_outline
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navBack: () -> Unit,
    save: () -> Unit,
    saveEnables: Boolean,
    avatar: Any?,
    changeAvatar: (ByteArray) -> Unit,
    name: String,
    onNameChange: (String) -> Unit,
    handle: String,
    onHandleChange: (String) -> Unit,
    handleErrorMessage: String?,
    bio: String,
    onBioChange: (String) -> Unit,
    result: RemoteResult?
) {
    val scope = rememberCoroutineScope()
    val dimensions = LocalDimensions.current
    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val cameraManager = rememberCameraManager { res ->
        res.toByteArray()?.let { changeAvatar(it) }
    }
    val galleryManager = rememberGalleryManager { res ->
        res.toByteArray()?.let { changeAvatar(it) }
    }

    val permissionManager = rememberPermissionManager { permission, status ->
        if (status == PermissionStatus.Granted) {
            when (permission) {
                Permission.Camera -> cameraManager.launch()
                Permission.Gallery -> galleryManager.launch()
            }
        }
    }

    LaunchedEffect(result) {
        if (result is RemoteResult.Error) {
            snackbarHostState.showSnackbar(getString(result.error.message))
        }
    }

    BackHandler(scope) {
        if (sheetState.isVisible) {
            scope.launch { sheetState.hide() }
        } else {
            navBack()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CourtlyAppBar(
                title = stringResource(Res.string.edit_profile),
                onBackClick = navBack,
                backVisible = true,
                trailing = {
                    TextButton(onClick = save, enabled = saveEnables) {
                        Text(text = stringResource(Res.string.save))
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                snackbarHostState,
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        AnimatedBottomSheet(
            isVisible = sheetState.isVisible,
            onDismissRequest = { scope.launch { sheetState.hide() } },
            sheetState = sheetState
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensions.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensions.medium)
                    .padding(bottom = dimensions.medium)
            ) {
                Text(text = stringResource(Res.string.select_source))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(dimensions.small)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch { sheetState.hide() }
                               if(permissionManager.isGranted(Permission.Camera))
                                   cameraManager.launch()
                                else
                                    permissionManager.checkAndRequest(Permission.Camera)
                            },
                        horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                    ) {
                        Icon(
                            painterResource(Res.drawable.camera_outline),
                            contentDescription = null
                        )
                        Text(text = stringResource(Res.string.camera))
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch { sheetState.hide() }
                                if (permissionManager.isGranted(Permission.Gallery))
                                    galleryManager.launch()
                                else
                                    permissionManager.checkAndRequest(Permission.Gallery)
                            },
                        horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                    ) {
                        Icon(
                            painterResource(Res.drawable.gallery_wide_outline),
                            contentDescription = null
                        )
                        Text(text = stringResource(Res.string.gallery))
                    }
                }
            }
        }

        if (result is RemoteResult.Loading) {
            Loading()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(WindowInsets.ime.asPaddingValues())
                .padding(horizontal = dimensions.medium, vertical = dimensions.small)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.small)
        ) {
            Box {
                SubcomposeAsyncImage(
                    model = avatar,
                    contentDescription = stringResource(Res.string.profile),
                    contentScale = ContentScale.Crop,
                    error = {
                        Icon(
                            painter = painterResource(Res.drawable.user_outline),
                            contentDescription = stringResource(Res.string.profile),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(dimensions.xSmall)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .border(dimensions.xxSmall, MaterialTheme.colorScheme.primary, CircleShape)
                        .align(Alignment.Center)
                )
                Card(
                    onClick = { scope.launch { sheetState.expand() } },
                    modifier = Modifier.align(Alignment.BottomEnd),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = CircleShape,
                    border = BorderStroke(dimensions.xxSmall, MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.pen_new_square_outline),
                        contentDescription = stringResource(Res.string.edit_avatar),
                        modifier = Modifier.padding(dimensions.xSmall)
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
            ) {
                Text(text = stringResource(Res.string.name))
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = name.isEmpty(),
                    supportingText = {
                        if (name.isEmpty()) {
                            Text(
                                text = stringResource(Res.string.name_error),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
            ) {
                Text(text = stringResource(Res.string.handle))
                OutlinedTextField(
                    value = handle,
                    onValueChange = onHandleChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = handleErrorMessage != null,
                    supportingText = {
                        if (handleErrorMessage != null) {
                            Text(
                                text = handleErrorMessage,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    prefix = { Text(text = "@") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
            ) {
                Text(text = stringResource(Res.string.bio))
                OutlinedTextField(
                    value = bio,
                    onValueChange = onBioChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.None)
                )
            }
        }
    }
}

@Preview
@Composable
fun EditProfilePreview() {
    CourtlyTheme(darkTheme = true) {
        EditProfileScreen(
            navBack = {},
            save = {},
            saveEnables = true,
            avatar = null,
            name = "Mohammed Hazem Dawood",
            onNameChange = {},
            handle = "_mhd_07",
            onHandleChange = {},
            handleErrorMessage = null,
            bio = "Cooked Math, Physics and CS Student",
            onBioChange = {},
            changeAvatar = {},
            result = RemoteResult.Success
        )
    }
}