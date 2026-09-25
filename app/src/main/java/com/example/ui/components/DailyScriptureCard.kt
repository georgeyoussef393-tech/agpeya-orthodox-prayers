package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.ui.graphics.graphicsLayer
import com.example.ui.animation.AgpeyaMotion
import com.example.ui.animation.pressBounce
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyVerseMeditation
import com.example.data.model.PrayerId
import com.example.localization.AgpeyaStrings
import com.example.localization.AppLanguage
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.BurgundyLight
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary

/**
 * Daily Orthodox Scripture Verse & Patristic Reflection Card.
 * Adheres strictly to the canonical Holy Bible recognized in the Coptic Orthodox Church in Egypt.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DailyScriptureCard(
    dailyVerse: DailyVerseMeditation,
    language: AppLanguage,
    onPrayerSelected: (PrayerId) -> Unit,
    onRefreshToday: () -> Unit,
    onReadPrayer: (PrayerId) -> Unit,
    modifier: Modifier = Modifier
) {
    var isReflectionExpanded by remember { mutableStateOf(true) }
    var showHourSelector by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(
                width = 1.dp,
                color = GoldPrimary.copy(alpha = 0.35f),
                shape = RoundedCornerShape(20.dp)
            )
            .testTag("daily_scripture_card")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header: Title with Coptic Cross motif
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(BurgundyDeep, BurgundyLight)
                                )
                            )
                    ) {
                        CopticCrossCanvas(
                            color = GoldLight,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = AgpeyaStrings.dailyVerseTitle(language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = AgpeyaStrings.orthodoxBibleEgypt(language),
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldPrimary
                        )
                    }
                }

                IconButton(
                    onClick = onRefreshToday,
                    modifier = Modifier.testTag("refresh_daily_verse")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh Verse",
                        tint = GoldPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Liturgical Connection Badge (Shows link to canonical prayer hour)
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = GoldPrimary.copy(alpha = 0.12f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = BurgundyDeep,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = dailyVerse.getLiturgicalTheme(language),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = if (showHourSelector) "✕" else if (language == AppLanguage.ARABIC) "تغيير الساعة ▾" else "Switch Hour ▾",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = BurgundyDeep,
                        modifier = Modifier
                            .clickable { showHourSelector = !showHourSelector }
                            .padding(4.dp)
                    )
                }
            }

            // Optional hour picker to view verse tied to any canonical hour
            AnimatedVisibility(
                visible = showHourSelector,
                enter = AgpeyaMotion.expandSpring(),
                exit = AgpeyaMotion.shrinkSpring()
            ) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "اختر الساعة لعرض آيتها وتأملها:" else "Choose prayer hour for its scripture:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PrayerId.canonicalPrayers.filter { it != PrayerId.VEIL }.forEach { prayer ->
                            val isSelected = dailyVerse.associatedPrayer == prayer
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    onPrayerSelected(prayer)
                                    showHourSelector = false
                                },
                                label = {
                                    Text(
                                        text = prayer.getDisplayName(language),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BurgundyDeep,
                                    selectedLabelColor = GoldLight
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // The Scripture Verse Quote Box
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(14.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Verse text
                    Text(
                        text = dailyVerse.getVerse(language),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            lineHeight = 26.sp,
                            fontStyle = FontStyle.Italic
                        ),
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Reference citation
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "— ${dailyVerse.getReference(language)}",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Collapsible / Expandable Patristic Reflection
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = BurgundyDeep.copy(alpha = 0.05f),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isReflectionExpanded = !isReflectionExpanded }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = BurgundyDeep,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AgpeyaStrings.spiritualExplanation(language),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = BurgundyDeep
                            )
                        }

                        val chevronRotation by animateFloatAsState(
                            targetValue = if (isReflectionExpanded) 180f else 0f,
                            animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing),
                            label = "chevron_rotation"
                        )
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = if (isReflectionExpanded) "Collapse" else "Expand",
                            tint = BurgundyDeep,
                            modifier = Modifier.graphicsLayer { rotationZ = chevronRotation }
                        )
                    }

                    AnimatedVisibility(
                        visible = isReflectionExpanded,
                        enter = AgpeyaMotion.expandSpring(),
                        exit = AgpeyaMotion.shrinkSpring()
                    ) {
                        Column(modifier = Modifier.padding(top = 8.dp)) {
                            Text(
                                text = dailyVerse.getExplanation(language),
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Quick link button to read the associated prayer
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                AssistChip(
                                    onClick = { onReadPrayer(dailyVerse.associatedPrayer) },
                                    label = {
                                        Text(
                                            text = if (language == AppLanguage.ARABIC)
                                                "صلاة ${dailyVerse.associatedPrayer.getDisplayName(language)} 📖"
                                            else
                                                "Read ${dailyVerse.associatedPrayer.getDisplayName(language)} 📖",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.MenuBook,
                                            contentDescription = null,
                                            tint = BurgundyDeep,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = GoldPrimary.copy(alpha = 0.18f),
                                        labelColor = BurgundyDeep
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
