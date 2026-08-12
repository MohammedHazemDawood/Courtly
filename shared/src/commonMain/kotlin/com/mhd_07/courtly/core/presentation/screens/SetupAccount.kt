package com.mhd_07.courtly.core.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.mhd_07.courtly.core.presentation.components.CourtlyAppBar
import com.mhd_07.courtly.core.presentation.components.Loading
import com.mhd_07.courtly.core.presentation.model.RemoteResult
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.buttonTextStyle
import com.mhd_07.courtly.core.util.BackHandler
import com.mhd_07.courtly.core.util.PermissionResultListener
import com.mhd_07.courtly.core.util.PermissionStatus
import com.mhd_07.courtly.core.util.PermissionType
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
import courtly.shared.generated.resources.setup_account
import courtly.shared.generated.resources.user_outline


import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupAccountScreen(
//    navBack: () -> Unit,
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
    val scaffoldState = rememberBottomSheetScaffoldState(sheetState)
    var cameraPermitted by remember { mutableStateOf(false) }
    var galleryPermitted by remember { mutableStateOf(false) }
    var requiredPermission by remember { mutableStateOf<PermissionType?>(null) }
    val cameraManager = rememberCameraManager { res ->
        res.toByteArray()?.let {
            println("Camera image selected ${it.size}")
            changeAvatar(it)
        }
    }
    val galleryManager = rememberGalleryManager { res ->
        res.toByteArray()?.let {
            println("Gallery image selected ${it.size}")
            changeAvatar(it)
        }
    }
    val permissionManager = rememberPermissionManager(object : PermissionResultListener {
        override fun onPermissionResult(permissionType: PermissionType, status: PermissionStatus) {
            println("Permission Result: $permissionType $status")
            when (permissionType) {
                PermissionType.CAMERA -> {
                    cameraPermitted = status == PermissionStatus.GRANTED
                    if (status == PermissionStatus.GRANTED)
                        cameraManager.launch()
                }

                PermissionType.GALLERY -> {
                    galleryPermitted = status == PermissionStatus.GRANTED
                    if (status == PermissionStatus.GRANTED)
                        galleryManager.launch()
                }
            }
            requiredPermission = null
        }
    })
    cameraPermitted =
        permissionManager.checkPermission(PermissionType.CAMERA) == PermissionStatus.GRANTED
    galleryPermitted =
        permissionManager.checkPermission(PermissionType.GALLERY) == PermissionStatus.GRANTED
    requiredPermission?.let { permissionType ->
        permissionManager.requestPermission(permissionType)
    }
    LaunchedEffect(result) {
        if (result is RemoteResult.Error)
            snackbarHostState.showSnackbar(getString(result.error.message))
    }
    BackHandler(scope) {
        if (sheetState.currentValue != SheetValue.Hidden)
            sheetState.hide()
//        else
//            navBack()
    }
    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CourtlyAppBar(
                title = stringResource(Res.string.setup_account),
//                onBackClick = navBack,
//                backVisible = true,
            )
        },
        snackbarHost = {
            SnackbarHost(
                snackbarHostState,
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        sheetContent = {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensions.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = dimensions.medium)
                    .padding(bottom = dimensions.medium)
            ) {
                Text(text = stringResource(Res.string.select_source))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(dimensions.small)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable {
                            if (cameraPermitted)
                                cameraManager.launch()
                            else requiredPermission = PermissionType.CAMERA
                            scope.launch { sheetState.hide() }
                        },
                        horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                    ) {
                        Icon(painterResource(Res.drawable.camera_outline), contentDescription = null)
                        Text(text = stringResource(Res.string.camera))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable {
                            if (galleryPermitted)
                                galleryManager.launch()
                            else requiredPermission = PermissionType.GALLERY
                            scope.launch { sheetState.hide() }
                        },
                        horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                    ) {
                        Icon(painterResource(Res.drawable.gallery_wide_outline), contentDescription = null)
                        Text(text = stringResource(Res.string.gallery))
                    }
                }
            }
        },
        scaffoldState = scaffoldState,
    ) {
        if (result is RemoteResult.Loading)
            Loading()
        Column(
            modifier = Modifier.fillMaxSize().padding(it)
                .padding(WindowInsets.ime.asPaddingValues())
                .padding(horizontal = dimensions.medium, vertical = dimensions.small)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensions.small)
            ) {
                Box {
                    SubcomposeAsyncImage(
                        model = avatar,
                        contentDescription = stringResource(Res.string.profile),
//                        placeholder = rememberVectorPainter(TablerIcons.Filled.User.apply),
                        contentScale = ContentScale.Crop,
                        error = {
                            Icon(
                                painterResource(Res.drawable.user_outline),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.fillMaxSize().padding(dimensions.xSmall)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(0.5f).aspectRatio(1f).clip(CircleShape)
                            .border(
                                dimensions.xxSmall,
                                MaterialTheme.colorScheme.primary,
                                CircleShape
                            )
                            .align(Alignment.Center)
                    )
                    Card(
                        onClick = { scope.launch { sheetState.expand() } },
                        modifier = Modifier.align(Alignment.BottomEnd).align(Alignment.BottomEnd),
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
//                    label = { Text(text = stringResource(Res.string.name)) },
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
//                    label = { Text(text = stringResource(Res.string.handle)) },
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
//                    label = { Text(text = stringResource(Res.string.bio)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
//                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.)
                    )
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = save,
                enabled = saveEnables,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = stringResource(Res.string.save),
                    modifier = Modifier.padding(dimensions.xxSmall),
                    style = buttonTextStyle
                )
            }
        }
    }
}