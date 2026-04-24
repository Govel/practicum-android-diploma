package ru.practicum.android.diploma.ui.screens

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.navigation.AppBarTop

@Composable
fun FavoritesScreen() {
    val state: FavoritesState = FavoritesState.Content(vacancyCard = getListFavorites())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary),
    ) {
        AppBarTop(title = stringResource(R.string.favorites))

        Spacer(modifier = Modifier.padding(top = 16.dp))

        when (state) {
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
                    items(state.vacancyCard) { vacancy ->
                        ItemVacancyDetails(vacancy, onClick = {})
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

@Composable
private fun ItemVacancyDetails(
    vacancy: TempVacancyCard,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(vacancy.logo.toString())
                .crossfade(true)
                .listener(
                    onError = { _, result ->
                        Log.e("IMAGE_ERROR", result.throwable.toString())
                    }
                )
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            placeholder = painterResource(R.drawable.ic_placeholder_32),
            error = painterResource(R.drawable.ic_placeholder_32),
            fallback = painterResource(R.drawable.ic_placeholder_32),
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp)),

        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${vacancy.name}, ${vacancy.city}",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 3
            )

            Text(
                text = vacancy.company ?: "",
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1
            )

            Text(
                text = vacancy.salary ?: "Зарплата не указана",
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun FavoritesScreenPreview() {
    FavoritesScreen()
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ItemVacancyDetailsPreview() {
    val vacancy = TempVacancyCard(
        id = 1,
        name = "Android-developer",
        company = "Едадил",
        city = "Moscow",
        salary = "от 100 000 руб",
        logo = "",
    )

    ItemVacancyDetails(
        vacancy = vacancy,
        onClick = {},
    )
}

// для теста
data class TempVacancyCard(
    val id: Int,
    val name: String,
    val company: String?,
    val city: String,
    val salary: String?,
    val logo: String?
)

private fun getListFavorites(): List<TempVacancyCard> {
    val vacancyOne = TempVacancyCard(
        id = 1,
        name = "Android-разработчик",
        company = "Еда",
        city = "Москва",
        salary = "от 100 000 руб",
        logo = "",
    )
    val vacancyTwo = TempVacancyCard(
        id = 2,
        name = "Разработчик на С++ в комаанду внутренних сервисов",
        company = "Авто.ру",
        city = "Москва",
        salary = "от 40 000 до 80 000 руб",
        logo = "",
    )

    return listOf(vacancyOne, vacancyTwo)
}

sealed interface FavoritesState {
    data class Content(val vacancyCard: List<TempVacancyCard>) : FavoritesState
    data object Error : FavoritesState
    data object Empty : FavoritesState
}
