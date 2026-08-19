package com.mhd_07.courtly.feature_profile.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
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
import com.mhd_07.courtly.feature_match.presentation.components.EnsureDialog
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.bio
import courtly.shared.generated.resources.camera
import courtly.shared.generated.resources.camera_outline
import courtly.shared.generated.resources.cancel
import courtly.shared.generated.resources.edit_profile
import courtly.shared.generated.resources.gallery
import courtly.shared.generated.resources.gallery_wide_outline
import courtly.shared.generated.resources.handle
import courtly.shared.generated.resources.name
import courtly.shared.generated.resources.name_error
import courtly.shared.generated.resources.open_settings
import courtly.shared.generated.resources.permission_camera_rational
import courtly.shared.generated.resources.permission_camera_rational_description
import courtly.shared.generated.resources.profile
import courtly.shared.generated.resources.save
import courtly.shared.generated.resources.select_source
import courtly.shared.generated.resources.user_bold
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private enum class ImageTarget { AVATAR, COVER }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navBack: () -> Unit,
    save: () -> Unit,
    saveEnabled: Boolean,
    avatar: Any?,
    changeAvatar: (ByteArray) -> Unit,
    name: String,
    onNameChange: (String) -> Unit,
    handle: String,
    onHandleChange: (String) -> Unit,
    handleErrorMessage: String?,
    bio: String,
    onBioChange: (String) -> Unit,
    result: RemoteResult?,
    cover: String?,
    onCoverChange: (ByteArray) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val dimensions = LocalDimensions.current
    val density = LocalDensity.current
    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var activeImageTarget by remember { mutableStateOf(ImageTarget.AVATAR) }
    var avatarSize by remember { mutableStateOf(IntSize.Zero) }
    var showRational by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    val onImageCaptured: (ByteArray) -> Unit = { bytes ->
        when (activeImageTarget) {
            ImageTarget.AVATAR -> changeAvatar(bytes)
            ImageTarget.COVER -> onCoverChange(bytes)
        }
    }

    val cameraManager = rememberCameraManager { res ->
        res.toByteArray()?.let(onImageCaptured)
    }
    val galleryManager = rememberGalleryManager { res ->
        res.toByteArray()?.let(onImageCaptured)
    }

    val permissionManager = rememberPermissionManager { permission, status ->
        if (status == PermissionStatus.Granted) {
            when (permission) {
                Permission.Camera -> cameraManager.launch()
                Permission.Gallery -> galleryManager.launch()
            }
        }
        if (status == PermissionStatus.ShowRational && !showRational && permission == Permission.Camera) {
            showRational = true
        }
    }

    fun openImagePicker(target: ImageTarget) {
        activeImageTarget = target
        scope.launch { sheetState.show() }
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
                    TextButton(onClick = save, enabled = saveEnabled) {
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
                                if (permissionManager.isGranted(Permission.Camera)) {
                                    cameraManager.launch()
                                } else {
                                    permissionManager.checkAndRequest(Permission.Camera)
                                }
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
                                if (permissionManager.isGranted(Permission.Gallery)) {
                                    galleryManager.launch()
                                } else {
                                    permissionManager.checkAndRequest(Permission.Gallery)
                                }
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

        EnsureDialog(
            visible = showRational,
            title = stringResource(Res.string.permission_camera_rational),
            description = stringResource(Res.string.permission_camera_rational_description),
            confirmText = stringResource(Res.string.open_settings),
            cancelText = stringResource(Res.string.cancel),
            onDismiss = { showRational = false },
            onConfirm = {
                permissionManager.launchSettings()
                showRational = false
            }
        )
        if (result is RemoteResult.Loading) {
            Loading()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Section: Cover + Overlapping Avatar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f)
            ) {
                // Cover Image
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { openImagePicker(ImageTarget.COVER) }
                ) {
                    SubcomposeAsyncImage(
                        model = cover,
                        contentDescription = stringResource(Res.string.profile),
                        contentScale = ContentScale.Crop,
                        error = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.DarkGray)
                            )
                        },
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.DarkGray)
                            )
                        },
                        modifier = Modifier.fillMaxSize().drawWithContent {
                            drawContent()
                            drawRect(
                                color = Color.Black.copy(alpha = 0.5f),
                                topLeft = Offset(0f, 0f),
                                size = size
                            )
                        }
                    )

                    // Edit Icon Badge for Cover
                    Icon(
                        painter = painterResource(Res.drawable.camera_outline),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(dimensions.large).align(Alignment.Center)
                    )
                }

                // Avatar Image (Positioned at Bottom Start, hanging 50% below the cover box)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = dimensions.small)
                        .offset { IntOffset(x = 0, y = avatarSize.height / 2) }
                        .fillMaxWidth(0.3f)
                        .aspectRatio(1f)
                        .onSizeChanged { avatarSize = it }
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(
                            width = dimensions.xxSmall,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                        .clickable { openImagePicker(ImageTarget.AVATAR) }
                ) {
                    SubcomposeAsyncImage(
                        model = avatar,
                        contentDescription = stringResource(Res.string.profile),
                        contentScale = ContentScale.Crop,
                        error = {
                            Box(modifier = Modifier.fillMaxSize().background(Color.LightGray)) {
                                Icon(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(dimensions.small),
                                    painter = painterResource(Res.drawable.user_bold),
                                    tint = Color.DarkGray,
                                    contentDescription = null
                                )
                            }
                        },
                        loading = {
                            Box(modifier = Modifier.fillMaxSize().background(Color.LightGray)) {
                                Icon(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(dimensions.small),
                                    painter = painterResource(Res.drawable.user_bold),
                                    tint = Color.DarkGray,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxSize().drawWithContent {
                            drawContent()
                            drawRect(
                                color = Color.Black.copy(alpha = 0.5f),
                                topLeft = Offset(0f, 0f),
                                size = size
                            )
                        })
                    Icon(
                        painter = painterResource(Res.drawable.camera_outline),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(dimensions.large).align(Alignment.Center)
                    )
                }
            }

            // Spacer to compensate for the avatar hanging 50% below the cover box
            Spacer(modifier = Modifier.height(with(density) { (avatarSize.height / 2).toDp() } + dimensions.small))

            // Form Fields Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensions.small),
                verticalArrangement = Arrangement.spacedBy(dimensions.small)
            ) {
                // Name Input
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

                // Handle Input
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

                // Bio Input
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
}

@Preview
@Composable
fun EditProfilePreview() {
    CourtlyTheme(darkTheme = true) {
        EditProfileScreen(
            navBack = {},
            save = {},
            saveEnabled = true,
            avatar = null,
            name = "Mohammed Hazem Dawood",
            onNameChange = {},
            handle = "_mhd_07",
            onHandleChange = {},
            handleErrorMessage = null,
            bio = "Cooked Math, Physics and CS Student",
            onBioChange = {},
            changeAvatar = {},
            result = RemoteResult.Success,
            cover = "",
            onCoverChange = {}
        )
    }
}