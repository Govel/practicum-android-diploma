@file:Suppress("MagicNumber")

package ru.practicum.android.diploma.ui.screens.filter.workplace

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.filter.workplace.country.domain.models.CountriesState
import ru.practicum.android.diploma.filter.workplace.country.domain.models.Country
import ru.practicum.android.diploma.filter.workplace.country.ui.CountryViewModel
import ru.practicum.android.diploma.ui.navigation.ActionBack
import ru.practicum.android.diploma.ui.navigation.AppBarTop

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryScreen(
    onBack: () -> Unit = {},
    onCountrySelected: (Country) -> Unit = {},
    viewModel: CountryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AppBarTop(
                title = stringResource(R.string.select_country),
                back = ActionBack(isView = true, onClick = onBack),
            )
        },

        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.onPrimary)
            ) {
                when (state) {
                    is CountriesState.Loading -> {
                        LoadingIndicator()
                    }

                    is CountriesState.Error -> {
                        ErrorPlaceholder()
                    }

                    is CountriesState.Content -> {
                        val countries = (state as CountriesState.Content).countries
                        CountriesList(
                            countries = countries,
                            onCountryClick = { country ->
                                viewModel.selectCountry(country)
                                onCountrySelected(country)
                                onBack()
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun ErrorPlaceholder() {
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
            painter = painterResource(id = R.drawable.magic_carpet),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(horizontal = 48.dp),
            text = stringResource(R.string.failed_to_get_list),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Composable
private fun CountriesList(
    countries: List<Country>,
    onCountryClick: (Country) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.onPrimary),
    ) {
        items(
            items = countries,
            key = { it.id }
        ) { country ->
            CountryItem(
                item = country,
                onSelect = onCountryClick
            )
        }
    }
}

@Composable
private fun CountryItem(
    item: Country,
    onSelect: (Country) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup()
            .height(60.dp)
            .clickable(onClick = { onSelect(item) }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = item.name,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_forward_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(8.dp)
                .size(32.dp)
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun CountryScreenPreview() {
    CountryScreen()
}
