package ru.practicum.android.diploma.main.ui.states

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.main.ui.SearchViewModel
import ru.practicum.android.diploma.main.ui.VacancyCard

@Composable
fun VacanciesSearchResult(
    state: SearchState,
    viewModel: SearchViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CountChip(count = state.totalCount)

        LazyColumn {
            items(state.vacancies, key = { it.id }) { vacancy ->
                VacancyCard(
                    vacancy = vacancy,
                    onClick = { viewModel.onVacancyClick(vacancy.id) }
                )
                if (state.vacancies.lastOrNull() == vacancy && state.hasMorePages && !state.isLoading && !state.isLoadingMore) {
                    LaunchedEffect(Unit) {
                        viewModel.loadNextPage()
                    }
                }
            }

            if (state.isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CountChip(count: Int) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .wrapContentSize()
            .padding(top = 4.dp, bottom = 8.dp),
    ) {
        Text(
            text = pluralStringResource(
                id = R.plurals.vacancies_count,
                count = count,
                count
            ),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}
