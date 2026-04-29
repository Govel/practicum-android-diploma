package ru.practicum.android.diploma.vacancy.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.navigation.ActionBack
import ru.practicum.android.diploma.ui.navigation.ActionFavorites
import ru.practicum.android.diploma.ui.navigation.ActionShare
import ru.practicum.android.diploma.ui.navigation.AppBarTop
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.LightGray
import ru.practicum.android.diploma.ui.theme.WhiteUniversal
import ru.practicum.android.diploma.vacancy.domain.models.AddressEmployer
import ru.practicum.android.diploma.vacancy.domain.models.ContactsEmployer
import ru.practicum.android.diploma.vacancy.domain.models.Employer
import ru.practicum.android.diploma.vacancy.domain.models.Phone
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetail
import ru.practicum.android.diploma.vacancy.ui.VacancyState
import ru.practicum.android.diploma.vacancy.ui.VacancyViewModel

@Composable
fun VacancyDetailScreen(
    vacancyId: String,
    onBack: () -> Unit = {},
    viewModel: VacancyDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()

    viewModel.currentVacancyId = vacancyId
    val vacancy = viewModel.currentVacancy

    LaunchedEffect(Unit) {
        viewModel.loadVacancyDetail()
    }
    Column {
        AppBarTop(
            title = "Вакансия",
            back = ActionBack(
                isView = true,
                onClick = onBack
            ),
            share = ActionShare(
                isView = true,
                onClick = { viewModel.shareLink(vacancy().url) }
            ),
            favorites = ActionFavorites(
                isView = true,
                onClick = { viewModel.onFavoriteClicked(vacancy()) },
                isActive = isFavorite
            )
        )
        when (state) {
            is VacancyState.Loading -> LoadingVacancy()
            is VacancyState.Content -> ContentVacancy(vacancy!!)
            is VacancyState.Error -> ErrorVacancy(
                painter = painterResource(R.drawable.server_error_cat),
                text = stringResource(R.string.server_error)
            )
            is VacancyState.Empty -> ErrorVacancy(
                painter = painterResource(R.drawable.fiery_rock),
                text = stringResource(R.string.vacancy_not_found)
            )
        }
    }
}

@Composable
fun ContentVacancy(vacancy: VacancyDetail) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        ContentVacancyHeader(vacancy)
        ContentVacancyCard(vacancy)
        ContentVacancyExperience(vacancy)
        ContentVacancySchedule(vacancy)
        ContentVacancyDescription(vacancy)
        ContentVacancySkills(vacancy)
        ContentVacancyContacts(vacancy)
    }
}

@Composable
private fun ContentVacancyHeader(vacancy: VacancyDetail) {
    Text(
        text = vacancy.name,
        style = MaterialTheme.typography.displayLarge,
        modifier = Modifier.padding(top = 24.dp)
    )
    Text(
        text = vacancy.salary ?: stringResource(R.string.salary_not_specified),
        style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
private fun ContentVacancyCard(
    vacancy: VacancyDetail,
    imageLoader: ImageLoader = koinInject()
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(WhiteUniversal)
                    .border(1.dp, LightGray, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(vacancy.employer.logo)
                        .crossfade(true).build(),
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.ic_placeholder_32),
                    error = painterResource(R.drawable.ic_placeholder_32),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.size(48.dp),
                    imageLoader = imageLoader,
                    onError = {
                        Log.e("Coil", "Error loading image", it.result.throwable)
                    }
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = vacancy.employer.name,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleLarge
                )
                if (vacancy.address?.raw?.isNotEmpty() ?: false) {
                    Text(
                        text = vacancy.address.raw,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    Text(
                        text = vacancy.area,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
private fun ContentVacancyExperience(vacancy: VacancyDetail) {
    Text(
        text = stringResource(R.string.required_experience),
        style = MaterialTheme.typography.bodyMedium
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = vacancy.experience ?: "",
        style = MaterialTheme.typography.bodyLarge
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun ContentVacancySchedule(vacancy: VacancyDetail) {
    val text = if ((vacancy.employment?.isNotEmpty() ?: false) and (vacancy.schedule?.isNotEmpty() ?: false)) {
        "${vacancy.employment}, ${vacancy.schedule}"
    } else if ((vacancy.employment?.isNotEmpty() ?: false) and (vacancy.schedule?.isEmpty() ?: true)) {
        "${vacancy.employment}"
    } else {
        "${vacancy.schedule}"
    }
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge
    )
    Spacer(modifier = Modifier.height(32.dp))
}

@Composable
private fun ContentVacancyDescription(vacancy: VacancyDetail) {
    Text(
        text = AnnotatedString.fromHtml(vacancy.description.trimIndent()),
        style = MaterialTheme.typography.bodyLarge
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun ContentVacancySkills(vacancy: VacancyDetail) {
    if (vacancy.skills.isNotEmpty()) {
        Text(
            text = stringResource(R.string.key_skills),
            style = MaterialTheme.typography.titleLarge
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
        ) {
            for (skill in vacancy.skills) {
                Text(
                    text = "\u2022\t $skill",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun ContentVacancyContacts(vacancy: VacancyDetail) {
    if (vacancy.contacts != null) {
        Text(
            text = stringResource(R.string.contacts),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(4.dp))
        ContentVacancyContact(vacancy.contacts)
    }
}

@Composable
private fun ContentVacancyContact(contact: ContactsEmployer) {
    Text(
        text = contact.name,
        style = MaterialTheme.typography.bodyMedium
    )
    if (contact.email.isNotEmpty()) {
        Text(
            text = "E-mail: ${contact.email}",
            style = MaterialTheme.typography.bodyLarge,
            color = Blue,
            modifier = Modifier.clickable(onClick = {})
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
    contact.phones?.forEach { phone ->
        var comment = ""
        if (phone.comment?.isNotEmpty() ?: false) {
            comment = "${phone.comment}: "
        }
        Text(
            text = "$comment${phone.formatted}",
            style = MaterialTheme.typography.bodyLarge,
            color = Blue,
            modifier = Modifier.clickable(onClick = {})
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun LoadingVacancy() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun ErrorVacancy(
    painter: Painter,
    text: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.height(224.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge
        )
    }
}
