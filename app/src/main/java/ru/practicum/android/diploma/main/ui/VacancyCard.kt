@file:Suppress("MagicNumber")

package ru.practicum.android.diploma.main.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import org.koin.compose.koinInject
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.main.domain.models.VacancyCard

@Composable
fun VacancyCard(
    vacancy: VacancyCard,
    onVacancyClick: (String) -> Unit,
    imageLoader: ImageLoader = koinInject()
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onVacancyClick(vacancy.id)
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(vacancy.logo)
                .diskCachePolicy(CachePolicy.DISABLED)
                .memoryCachePolicy(CachePolicy.DISABLED)
                .build(),
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
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 1
            )
            Text(
                text = if (!vacancy.salary.isNullOrEmpty()) {
                    vacancy.salary
                } else {
                    stringResource(R.string.salary_not_specified)
                },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 1
            )
        }
    }
}
