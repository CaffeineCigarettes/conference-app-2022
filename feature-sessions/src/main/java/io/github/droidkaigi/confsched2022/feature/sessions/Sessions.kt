package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched2022.feature.sessions.SessionsUiModel.ScheduleState.Loaded
import io.github.droidkaigi.confsched2022.feature.sessions.SessionsUiModel.ScheduleState.Loading
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.orEmptyContents

@Composable
fun SessionsScreenRoot(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {}
) {
    val viewModel = hiltViewModel<SessionsViewModel>()
    val state: SessionsUiModel by viewModel.uiModel

    var tabState by remember { mutableStateOf(0) }

    Sessions(
        uiModel = state,
        modifier = modifier,
        selectedTab = tabState,
        onTimetableClick = {},
        onTabClicked = { index -> tabState = index },
        onToggleFilter = { viewModel.onToggleFilter() },
        onFavoriteClick = { timetableItemId, isFavorite ->
            viewModel.onFavoriteToggle(timetableItemId, isFavorite)
        },
        onNavigationIconClick = onNavigationIconClick,
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Sessions(
    uiModel: SessionsUiModel,
    modifier: Modifier = Modifier,
    selectedTab: Int,
    onNavigationIconClick: () -> Unit,
    onTimetableClick: (timetableItemId: TimetableItemId) -> Unit,
    onTabClicked: (index: Int) -> Unit,
    onToggleFilter: () -> Unit,
    onFavoriteClick: (TimetableItemId, Boolean) -> Unit
) {
    KaigiScaffold(onNavigationIconClick = onNavigationIconClick) {
        val scheduleState = uiModel.scheduleState
        if (scheduleState !is Loaded) {
            CircularProgressIndicator()
            return@KaigiScaffold
        }
        val days = scheduleState.schedule.days
        Column(
            modifier = modifier
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                )
        ) {
            val pagerState = rememberPagerState()
            TabRow(
                selectedTabIndex = selectedTab,
                indicator = {},
                divider = {},
            ) {
                days.forEachIndexed { index, day ->
                    SessionDayTab(
                        index = index,
                        day = day,
                        selectedTab = selectedTab,
                        onTabClicked = onTabClicked
                    )
                }
            }
            Text(
                text = "Filter is ${if (uiModel.isFilterOn) "ON" else "OFF"}",
                modifier = Modifier.clickable(onClick = onToggleFilter)
            )
            HorizontalPager(
                count = days.size,
                state = pagerState
            ) { dayIndex ->
                val day = days[dayIndex]
                val timetable = scheduleState.schedule.dayToTimetable[day].orEmptyContents()
                Timetable(timetable) { timetableItem, isFavorited ->
                    TimetableItem(
                        timetableItem = timetableItem,
                        isFavorited = isFavorited,
                        modifier = Modifier
                            .clickable(
                                onClick = { onTimetableClick(timetableItem.id) }
                            ),
                        onFavoriteClick = onFavoriteClick
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SessionsPreview() {
    KaigiTheme {
        Sessions(
            uiModel = SessionsUiModel(scheduleState = Loading, isFilterOn = false),
            selectedTab = 0,
            onNavigationIconClick = {},
            onFavoriteClick = { _, _ -> },
            onTabClicked = {},
            onTimetableClick = {},
            onToggleFilter = {}
        )
    }
}
