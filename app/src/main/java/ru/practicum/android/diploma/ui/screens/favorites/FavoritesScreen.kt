package ru.practicum.android.diploma.ui.screens.favorites

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.favorite.FavoritesState
import ru.practicum.android.diploma.favorite.FavoritesViewModel
import ru.practicum.android.diploma.main.ui.VacancyCard
import ru.practicum.android.diploma.ui.navigation.AppBarTop

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary),
    ) {
        AppBarTop(title = stringResource(R.string.favorites))

        Spacer(modifier = Modifier.padding(top = 16.dp))

        when (state) {
            is FavoritesState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(44.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            is FavoritesState.Empty -> {
                PlaceholderNothingAndError(
                    title = R.string.empty_list,
                    image = R.drawable.loupe
                )
            }

            is FavoritesState.Error -> {
                PlaceholderNothingAndError(
                    title = R.string.failed_to_get_vacancies,
                    image = R.drawable.empty_cat
                )
            }

            is FavoritesState.Content -> {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(state.vacancyCard, key = { it.id }) { vacancy ->
                        VacancyCard(
                            vacancy = vacancy,
                            onClick = { "vacancy/${vacancy.id}" }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceholderNothingAndError(
    @StringRes title: Int = 0,
    @DrawableRes image: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(224.dp),
            painter = painterResource(image),
            contentDescription = null
        )

        if (title != 0) {
            Text(
                text = stringResource(title),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 30.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun FavoritesScreenPreview() {
    FavoritesScreen(
        viewModel = koinViewModel()
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PlaceholderNothingPreview() {
    PlaceholderNothingAndError(
        title = R.string.empty_list,
        image = R.drawable.loupe
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PlaceholderErrorPreview() {
    PlaceholderNothingAndError(
        title = R.string.failed_to_get_vacancies,
        image = R.drawable.empty_cat
    )
}
