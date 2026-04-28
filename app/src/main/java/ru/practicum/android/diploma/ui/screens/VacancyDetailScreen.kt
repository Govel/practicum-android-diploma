package ru.practicum.android.diploma.ui.screens

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
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.koin.compose.koinInject
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.navigation.ActionBack
import ru.practicum.android.diploma.ui.navigation.ActionFavorites
import ru.practicum.android.diploma.ui.navigation.ActionShare
import ru.practicum.android.diploma.ui.navigation.AppBarTop
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.LightGray
import ru.practicum.android.diploma.ui.theme.WhiteUniversal


@Composable
fun VacancyDetailScreen(
    vacancy: VacancyDetail,
    onBack: () -> Unit = {},
) {
    val state = 1
    Column {
        AppBarTop(
            title = "Вакансия",
            back = ActionBack(
                isView = true,
                onClick = onBack
            ),
            share = ActionShare(
                isView = true,
                onClick = {}
            ),
            favorites = ActionFavorites(
                isView = true,
                onClick = {},
                isActive = vacancy().isFavorite
            )
        )
        when(state) {
            1 -> LoadingVacancy()
            2 -> ContentVacancy(vacancy)
            3 -> ErrorVacancy(
                painter = painterResource(R.drawable.server_error_cat),
                text = stringResource(R.string.server_error))
            4 -> ErrorVacancy(
                painter = painterResource(R.drawable.fiery_rock),
                text = stringResource(R.string.vacancy_not_found))
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
        text = vacancy.salary,
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
                        .data("https://upload.wikimedia.org/wikipedia/commons/6/69/Netflix_logo.svg")
                        .crossfade(true).build(),
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.ic_placeholder_32),
                    error = painterResource(R.drawable.ic_placeholder_32),
                    contentScale = ContentScale.Crop,
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
                Text(
                    text = vacancy.area,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
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
    Text(
        text = "${vacancy.employment}, ${vacancy.schedule}",
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

@Composable
private fun ContentVacancyContacts(vacancy: VacancyDetail) {
    Text(
        text = stringResource(R.string.contacts),
        style = MaterialTheme.typography.titleLarge
    )
    if (vacancy.contacts != null) {
        Text(
            text = vacancy.contacts.name,
            style = MaterialTheme.typography.bodyMedium
        )
        if (vacancy.contacts.email != "") {
            Text(
                text = "E-mail: ${vacancy.contacts.email}",
                style = MaterialTheme.typography.bodyLarge,
                color = Blue,
                modifier = Modifier.clickable(onClick = {})
            )
        }
        if (vacancy.contacts.phones?.isNotEmpty() ?: false) {
            for (phone in vacancy.contacts.phones) {
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
            }
        }
    }
}

@Composable
private fun LoadingVacancy() {
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

@Composable
private fun ErrorVacancy(
    painter: Painter,
    text: String
) {
    Column (
        modifier = Modifier.fillMaxSize(),
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

data class VacancyDetail(
    val id: String,
    val name: String,
    val description: String,
    val salary: String,
    val address: AddressEmployer?,
    val experience: String?,
    val schedule: String?,
    val employment: String?,
    val contacts: ContactsEmployer?,
    val employer: Employer,
    val area: String,
    val skills: List<String>,
    val url: String,
    val industry: String,
    val isFavorite: Boolean
)

data class AddressEmployer(
    val city: String,
    val street: String,
    val building: String,
    val raw: String
)

data class ContactsEmployer(
    val name: String,
    val email: String,
    val phones: List<Phone>?
)

data class Phone(
    val comment: String?,
    val formatted: String
)

data class Employer(
    val name: String,
    val logo: String
)

fun vacancy(): VacancyDetail = VacancyDetail(
    id = "000aa9c3-18c4-357a-8aaa-6def258d5601",
    name = "Frontend-разработчик",
    description = "<h3>Условия</h3><ul><li>Продуктовые задачи с понятным влиянием на результат.</li><li>Удаленная работа или гибридный формат.</li><li>Технические обсуждения без избыточного менеджмента.</li></ul></section>",
    salary = "От 18000000 UZS",
    address = AddressEmployer(
        city = "Ростов-на-Дону",
        street = "Садовая",
        building = "10",
        raw = "Ростов-на-Дону, Садовая, 10"
    ),
    experience = "От 1 года до 3 лет",
    schedule = "Удаленная работа",
    employment = "Полная занятость",
    contacts = ContactsEmployer(
        name = "Кузнецов Сергей Петрович",
        email = "example@ya.ru",
        phones = listOf(
            Phone(
                comment = "work",
                formatted = "+7 (999) 567-89-01"
            ), Phone(
                comment = null,
                formatted = "+7 (999) 543-21-09"
            )
        )
    ),
    employer = Employer(
        name = "Netflix",
        logo = "https://upload.wikimedia.org/wikipedia/commons/6/69/Netflix_logo.svg"
    ),
    area = "Ростов-на-Дону",
    skills = listOf("TypeScript", "React", "HTML", "CSS", "REST API"),
    url = "cek6h0n4ffe7ur.cluster-czz5s0kz4scl.eu-west-1.rds.amazonaws.com/vacancies/000aa9c3-18c4-357a-8aaa-6def258d5601",
    industry = "Информационные технологии, системная интеграция, интернет",
    isFavorite = true
)


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewVacancyDetail() {
    VacancyDetailScreen(vacancy())
}
