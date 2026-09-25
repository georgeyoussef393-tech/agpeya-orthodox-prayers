package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AgpeyaStrings
import com.example.localization.AppLanguage
import com.example.ui.animation.AgpeyaMotion
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.GoldPrimary
import com.example.ui.viewmodel.AgpeyaViewModel

@Composable
fun ReportsAndSettingsHostScreen(
    viewModel: AgpeyaViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    var selectedSubTab by rememberSaveable { mutableIntStateOf(0) }

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = GoldPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]),
                    color = GoldPrimary,
                    height = 3.dp
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = {
                    Text(
                        text = if (lang == AppLanguage.ARABIC) "📊 تقارير الصلوات والسلاسل" else "📊 Progress & Streaks",
                        fontWeight = if (selectedSubTab == 0) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 13.sp
                    )
                }
            )

            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = {
                    Text(
                        text = if (lang == AppLanguage.ARABIC) "✍️ المفكرة والاعتراف" else "✍️ Journal Notes",
                        fontWeight = if (selectedSubTab == 1) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 13.sp
                    )
                }
            )
        }

        AnimatedContent(
            targetState = selectedSubTab,
            transitionSpec = {
                val isForward = if (lang.isRtl) (targetState < initialState) else (targetState > initialState)
                AgpeyaMotion.directionalSlide(forward = isForward)
            },
            label = "reports_host_tab_transition",
            modifier = Modifier.fillMaxSize()
        ) { tab ->
            when (tab) {
                0 -> ReportsScreen(viewModel = viewModel)
                1 -> SpiritualJournalScreen(viewModel = viewModel)
            }
        }
    }
}
