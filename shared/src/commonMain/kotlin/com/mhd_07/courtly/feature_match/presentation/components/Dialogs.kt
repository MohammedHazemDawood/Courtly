package com.mhd_07.courtly.feature_match.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
// unused import removed
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
// unused import removed
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.titleTextStyle
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.cancel
import courtly.shared.generated.resources.ensure_quit
import courtly.shared.generated.resources.ensure_quit_title
import courtly.shared.generated.resources.quit
import courtly.shared.generated.resources.select_player
import courtly.shared.generated.resources.select_player_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun EnsureDialog(
    visible: Boolean,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    description: String,
    confirmText: String,
    cancelText: String,
    additionalActionText: String? = null,
    additionalAction: (() -> Unit) = {}
) {
    val dimensions = LocalDimensions.current
    if (visible)
        Dialog(onDismissRequest = onDismiss) {
            Box(
                modifier = modifier.background(
                    MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.medium
                ), contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = dimensions.small, bottom = dimensions.xxSmall),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensions.small)
                ) {
                    Text(
                        text = title,
                        style = titleTextStyle,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = dimensions.small),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = description,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = dimensions.small),
                        textAlign = TextAlign.Center
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        additionalActionText?.let {
                            HorizontalDivider()
                            TextButton(
                                onClick = additionalAction,
                                shape = RectangleShape,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = additionalActionText,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        HorizontalDivider()
                        TextButton(
                            onClick = onConfirm,
                            shape = RectangleShape,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = confirmText,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                        HorizontalDivider()
                        TextButton(
                            onClick = onDismiss,
                            shape = RectangleShape,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = cancelText,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
//                    Spacer(modifier = Modifier)
                    }
                }
            }
        }
}

@Preview
@Composable
fun EnsureDialogPreview() {
    CourtlyTheme(darkTheme = true) {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            EnsureDialog(
//                modifier = Modifier.fillMaxWidth().padding(16.dp),
                onDismiss = {},
                onConfirm = {},
                visible = true,
                title = "",
                description = "",
                confirmText = "",
                cancelText = ""
            )
        }
    }
}

/**************************************************************************/

@Composable
fun PlayerSelectDialog(
    visible: Boolean,
    modifier: Modifier = Modifier,
    p1: Player?,
    p2: Player?,
    select: (Player) -> Unit,
    cancel: () -> Unit
) {
    val dimensions = LocalDimensions.current
    if (visible)
        Dialog(onDismissRequest = { }) {
            Box(
                modifier = modifier.background(
                    MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.medium
                ),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth().padding(/*top =*/ dimensions.small),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensions.small)
                ) {
                    Text(
                        text = stringResource(Res.string.select_player),
                        style = titleTextStyle,
//                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(Res.string.select_player_description),
//                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Column(modifier = Modifier.fillMaxWidth()) {
                        p1?.let {
                            HorizontalDivider()
                            PlayerRow(
                                avatar = p1.avatar + "?v=" + p1.avatarVersion,
                                name = p1.name,
                                handle = p1.handle ?: "",
                                select = { select(p1) },
                                modifier = Modifier.fillMaxWidth()//.height(dimensions.xxLarge)
                            )
                        }
                        p2?.let {
                            HorizontalDivider()
                            PlayerRow(
                                avatar = p2.avatar + "?v=" + p2.avatarVersion,
                                name = p2.name,
                                handle = p2.handle ?: "",
                                select = { select(p2) },
                                modifier = Modifier.fillMaxWidth()//.height(dimensions.xxLarge)
                            )
                        }
                        HorizontalDivider()
                        TextButton(
                            onClick = cancel,
                            shape = RectangleShape,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(Res.string.cancel))
                        }
                    }
                }
            }
        }
}

@Composable
fun PlayerRow(
    avatar: String,
    name: String,
    handle: String,
    modifier: Modifier = Modifier,
    select: (() -> Unit)? = null
) {
    val dimensions = LocalDimensions.current
    Box(
        modifier = modifier
            .clickable(enabled = select != null) { select?.invoke() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(dimensions.xSmall),
            horizontalArrangement = Arrangement.spacedBy(dimensions.small),
        ) {
            PlayerAvatar(
                modifier = Modifier.height(dimensions.xxLarge),
                name = name,
                avatar = avatar,
//                borderWidth = dimensions.default/*, modifier = Modifier.weight(1f)*/
            )
            Column(verticalArrangement = Arrangement.spacedBy(dimensions.xxSmall)) {
                Text(text = name, maxLines = 1)
                if (handle.isNotEmpty())
                    Text(text = "@$handle", style = notesTextStyle)
            }
        }
    }
}

@Preview
@Composable
fun PlayerSelectionDialogPreviewDialog() {
    CourtlyTheme(darkTheme = true) {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            PlayerSelectDialog(
                p1 = Player(
                    handle = "_mhd_07",
                    name = "Mohammad Hazem",
                    avatar = "https://gcflppntjrgnbvsuoxkr.supabase.co/storage/v1/object/public/avatar/cdfdb848-cefe-4a0d-bb73-835a57f37823/pfp.jpg",
                    avatarVersion = 0,
                    location = "Egypt",
                    bio = "",
                    cover = null,
                    coverVersion = 0
                ),
                p2 = Player(
                    handle = "_sj_55",
                    name = "Steve Jobs",
                    avatar = "https://static.wikia.nocookie.net/ipod/images/c/cb/Jobs_hero20110329.png/revision/latest?cb=20200202110213",
                    avatarVersion = 0,
                    location = "Egypt",
                    bio = "",
                    cover = null,
                    coverVersion = 0
                ),
                select = { throw NotImplementedError("Select not implemented in preview") },
                visible = true,
                cancel = {}
            )
        }
    }
}