@file:Suppress("MagicNumber")

package ru.practicum.android.diploma.ui.screens.filter.workplace

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.filter.workplace.country.domain.models.FilterCountry
import ru.practicum.android.diploma.ui.navigation.ActionBack
import ru.practicum.android.diploma.ui.navigation.AppBarTop

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryScreen(
    onBack: () -> Unit = {}
) {
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
                CountriesResult()
            }
        }
    )
}

@Composable
private fun CountriesResult() {
    val countries = getListCountries()

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
                isSelectedObject = null,
                onSelect = {}
            )
        }
    }
}

@Composable
private fun CountryItem(
    item: FilterCountry,
    isSelectedObject: FilterCountry?,
    onSelect: (FilterCountry) -> Unit = {}
) {
    val isSelected = isSelectedObject?.id == item.id

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

@Composable
private fun getListCountries(): List<FilterCountry> {
    return listOf(
        FilterCountry(1, "Россия"),
        FilterCountry(2, "Ураина"),
        FilterCountry(3, "Казахстан"),
        FilterCountry(4, "Азербайджан"),
        FilterCountry(5, "Беларусь"),
        FilterCountry(6, "Грузия"),
        FilterCountry(7, "Кыргызстан"),
        FilterCountry(8, "Узбекистан"),
        FilterCountry(9, "Другие регионы")
    )
}
