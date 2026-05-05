@file:Suppress("MagicNumber", "CognitiveComplexMethod")

package ru.practicum.android.diploma.main.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.main.ui.SearchViewModel
import ru.practicum.android.diploma.main.ui.states.EmptyResultScreen
import ru.practicum.android.diploma.main.ui.states.EmptySearchPlaceholder
import ru.practicum.android.diploma.main.ui.states.ErrorScreen
import ru.practicum.android.diploma.main.ui.states.LoadingIndicator
import ru.practicum.android.diploma.main.ui.states.SearchState
import ru.practicum.android.diploma.main.ui.states.VacanciesSearchResult
import ru.practicum.android.diploma.ui.navigation.ActionBack
import ru.practicum.android.diploma.ui.navigation.ActionFilter
import ru.practicum.android.diploma.ui.navigation.AppBarTop

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onFilter: () -> Unit = {},
    viewModel: SearchViewModel = koinViewModel(),
    onVacancyClick: (String) -> Unit,
    navController: NavHostController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var searchText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val filterIsActive by viewModel.filterIsActive.collectAsState()

    HandleFilterResult(navController, state, viewModel)

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
                isActive = filterIsActive
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

        SearchContent(state, viewModel, onVacancyClick)
    }
}

@Composable
private fun HandleFilterResult(
    navController: NavHostController,
    state: SearchState,
    viewModel: SearchViewModel
) {
    LaunchedEffect(Unit) {
        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
        savedStateHandle?.get<Boolean>("filtersChanged")?.let { filtersChanged ->
            if (filtersChanged) {
                savedStateHandle.remove<Boolean>("filtersChanged")
                if (state.searchText.isNotEmpty()) {
                    viewModel.refreshSearch()
                }
            }
        }
        savedStateHandle?.get<Boolean>("filtersReset")?.let { filtersReset ->
            if (filtersReset) {
                savedStateHandle.remove<Boolean>("filtersReset")
                if (state.searchText.isNotEmpty()) {
                    viewModel.refreshSearch()
                }
            }
        }
    }
}

@Composable
private fun SearchContent(state: SearchState, viewModel: SearchViewModel, onVacancyClick: (String) -> Unit) {
    when {
        state.isLoading -> {
            LoadingIndicator()
        }

        state.isNetworkError -> {
            ErrorScreen(true)
        }

        state.isServerError -> {
            ErrorScreen(false)
        }

        state.isEmptyResult && state.searchText.isNotEmpty() -> {
            EmptyResultScreen()
        }

        state.vacancies.isNotEmpty() -> {
            VacanciesSearchResult(state, viewModel, onVacancyClick)
        }

        state.searchText.isEmpty() -> {
            EmptySearchPlaceholder()
        }
    }
}
