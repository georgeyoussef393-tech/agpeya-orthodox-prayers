package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.ui.theme.GoldPrimary
import com.example.ui.viewmodel.AgpeyaViewModel
import kotlinx.coroutines.launch

@Composable
fun ReportsAndSettingsHostScreen(
    viewModel: AgpeyaViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val subPagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = subPagerState.currentPage != 0) {
        coroutineScope.launch { subPagerState.animateScrollToPage(0) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        TabRow(
            selectedTabIndex = subPagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = GoldPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[subPagerState.currentPage]),
                    color = GoldPrimary,
                    height = 3.dp
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = subPagerState.currentPage == 0,
                onClick = { coroutineScope.launch { subPagerState.animateScrollToPage(0) } },
                text = {
                    Text(
                        text = if (lang == AppLanguage.ARABIC) "📊 تقارير الصلوات والسلاسل" else "📊 Progress & Streaks",
                        fontWeight = if (subPagerState.currentPage == 0) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 13.sp
                    )
                },
                modifier = Modifier.testTag("subtab_reports")
            )

            Tab(
                selected = subPagerState.currentPage == 1,
                onClick = { coroutineScope.launch { subPagerState.animateScrollToPage(1) } },
                text = {
                    Text(
                        text = if (lang == AppLanguage.ARABIC) "✍️ المفكرة والاعتراف" else "✍️ Journal Notes",
                        fontWeight = if (subPagerState.currentPage == 1) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 13.sp
                    )
                },
                modifier = Modifier.testTag("subtab_journal")
            )
        }

        HorizontalPager(
            state = subPagerState,
            beyondViewportPageCount = 1,
            modifier = Modifier
                .fillMaxSize()
                .testTag("reports_journal_horizontal_pager")
        ) { tab ->
            when (tab) {
                0 -> ReportsScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                1 -> SpiritualJournalScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
            }
        }
    }
}
