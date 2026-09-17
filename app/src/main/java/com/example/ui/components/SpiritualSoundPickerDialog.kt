package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SpiritualSound
import com.example.data.model.PrayerId
import com.example.localization.AgpeyaStrings
import com.example.localization.AppLanguage
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary

/**
 * Dialog for previewing and choosing spiritual sounds & hymns for Agpeya prayer alarms.
 */
@Composable
fun SpiritualSoundPickerDialog(
    currentSoundId: String,
    prayerId: PrayerId?,
    language: AppLanguage,
    playingSoundId: String?,
    onPlayPreview: (SpiritualSound) -> Unit,
    onStopPreview: () -> Unit,
    onSelectSound: (SpiritualSound) -> Unit,
    onDismiss: () -> Unit
) {
    DisposableEffect(Unit) {
        onDispose {
            onStopPreview()
        }
    }

    AlertDialog(
        onDismissRequest = {
            onStopPreview()
            onDismiss()
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(GoldPrimary.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = AgpeyaStrings.selectAlarmTone(language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (prayerId != null) {
                        Text(
                            text = prayerId.getDisplayName(language),
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldPrimary
                        )
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (language == AppLanguage.ARABIC)
                        "استمع لمعاينة الأصوات الروحية والألحان الكنسية قبل اختيارها لتنبيه صلواتك:"
                    else
                        "Preview and choose sacred liturgical tones or chimes for your prayer alarm:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                SpiritualSound.entries.forEach { sound ->
                    val isSelected = sound.id.equals(currentSoundId, ignoreCase = true)
                    val isPlaying = playingSoundId == sound.id

                    val containerColor by animateColorAsState(
                        targetValue = when {
                            isPlaying -> GoldPrimary.copy(alpha = 0.18f)
                            isSelected -> MaterialTheme.colorScheme.surfaceVariant
                            else -> MaterialTheme.colorScheme.surface
                        },
                        label = "cardColor"
                    )

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = containerColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .border(
                                width = if (isSelected || isPlaying) 1.5.dp else 0.5.dp,
                                color = if (isPlaying) GoldPrimary else if (isSelected) BurgundyDeep else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable {
                                onSelectSound(sound)
                            }
                            .testTag("sound_item_${sound.id}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onSelectSound(sound) },
                                colors = RadioButtonDefaults.colors(selectedColor = GoldPrimary)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = sound.getName(language),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = sound.getDescription(language),
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 15.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Play / Stop Preview button
                            IconButton(
                                onClick = {
                                    if (isPlaying) {
                                        onStopPreview()
                                    } else {
                                        onPlayPreview(sound)
                                    }
                                },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (isPlaying) BurgundyDeep else GoldPrimary.copy(alpha = 0.15f))
                                    .size(36.dp)
                                    .testTag("preview_${sound.id}")
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "Stop" else "Play",
                                    tint = if (isPlaying) GoldLight else GoldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onStopPreview()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = BurgundyDeep)
            ) {
                Text(
                    text = AgpeyaStrings.save(language),
                    color = GoldLight,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onStopPreview()
                    onDismiss()
                }
            ) {
                Text(
                    text = AgpeyaStrings.cancel(language),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}
