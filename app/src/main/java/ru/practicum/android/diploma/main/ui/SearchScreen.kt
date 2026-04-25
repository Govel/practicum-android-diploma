@file:Suppress("MagicNumber")

package ru.practicum.android.diploma.main.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.main.domain.models.VacancyCard
import ru.practicum.android.diploma.ui.navigation.ActionBack
import ru.practicum.android.diploma.ui.navigation.ActionFilter
import ru.practicum.android.diploma.ui.navigation.AppBarTop

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onFilter: () -> Unit = {},
    isFilterActive: Boolean = false,
    viewModel: SearchViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var searchText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(state.searchText) {
        if (searchText != state.searchText) {
            searchText = state.searchText
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary),
    ) {
        AppBarTop(
            title = stringResource(R.string.search_vacancies),
            back = ActionBack(isView = false),
            filter = ActionFilter(
                isView = true,
                onClick = onFilter,
                isActive = isFilterActive
            )
        )

        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { newText ->
                    searchText = newText
                    viewModel.onSearchTextChanged(newText)
                },
                placeholder = {
                    Text(
                        stringResource(R.string.enter_query),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                },
                trailingIcon = {
                    if (searchText.isEmpty()) {
                        Icon(
                            painter = painterResource(R.drawable.ic_search_24),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        IconButton(onClick = {
                            searchText = ""
                            viewModel.clearSearch()
                            focusManager.clearFocus()
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_close_24),
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },

                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    focusManager.clearFocus()
                }),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        SearchContent(state, viewModel)
    }
}

@Composable
private fun SearchContent(state: SearchState, viewModel: SearchViewModel) {
    when {
        state.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        state.isNetworkError -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(232.dp)
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    painter = painterResource(R.drawable.no_internet),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(horizontal = 48.dp),
                    text = stringResource(R.string.no_internet),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }

        state.isEmptyResult && state.searchText.isNotEmpty() -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 4.dp, bottom = 8.dp),
                ) {
                    Text(
                        text = stringResource(R.string.no_such_vacancies),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(0.3f))

                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(232.dp)
                        .padding(horizontal = 16.dp),
                    painter = painterResource(R.drawable.empty_cat),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 48.dp),
                    text = stringResource(R.string.failed_to_get_vacancies),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.weight(0.3f))
            }
        }

        state.vacancies.isNotEmpty() -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 4.dp, bottom = 8.dp),
                ) {
                    Text(
                        text = "Найдено ${state.totalCount} вакансий",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                LazyColumn {
                    items(state.vacancies, key = { it.id }) { vacancy ->
                        VacancyCard(
                            vacancy = vacancy,
                            onClick = { viewModel.onVacancyClick(vacancy.id) }
                        )
                        if (state.vacancies.lastOrNull() == vacancy && state.hasMorePages && !state.isLoading) {
                            LaunchedEffect(Unit) {
                                viewModel.loadNextPage()
                            }
                        }
                    }
                }
            }
        }

        state.searchText.isEmpty() -> {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    alignment = Alignment.Center,
                    painter = painterResource(R.drawable.job_search),
                    contentDescription = "Ищу работу"
                )
            }
        }
    }
}

@Composable
fun VacancyCard(
    vacancy: VacancyCard,
    onClick: () -> Unit,
    imageLoader: ImageLoader = koinInject()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        AsyncImage(
            model = vacancy.logo,
            contentDescription = "Логотип компании",
            imageLoader = imageLoader,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp)),
            placeholder = painterResource(R.drawable.ic_placeholder_32),
            error = painterResource(R.drawable.ic_placeholder_32)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = buildString {
                    append(vacancy.name)
                    vacancy.city?.let { append(", $it") }
                },
                style = MaterialTheme.typography.titleMedium,
                maxLines = 3
            )

            Text(
                text = vacancy.company ?: "",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
            Text(
                text = vacancy.salary ?: stringResource(R.string.salary_not_specified),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}
