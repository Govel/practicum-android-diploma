package ru.practicum.android.diploma.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import okhttp3.OkHttpClient
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.LightGray
import ru.practicum.android.diploma.ui.theme.WhiteUniversal


@Composable
fun VacancyDetailScreen(vacancy: VacancyDetail) {
    val context = LocalContext.current

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "Mozilla/5.0")
                .build()
            chain.proceed(request)
        }
        .build()

    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
            add(OkHttpNetworkFetcherFactory(
                callFactory = okHttpClient))
        }
        .build()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
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
                    modifier = Modifier.size(48.dp)
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
                Column() {
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
        Text(
            text = "${vacancy.employment}, ${vacancy.schedule}",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = AnnotatedString.fromHtml(vacancy.description.trimIndent()),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.key_skills),
            style = MaterialTheme.typography.titleLarge
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp)
        ) {
            for (skill in vacancy.skills) {
                Text(
                    text = "• $skill",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
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
    val industry: String
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
    val phones: List<String>
)

data class Employer(
    val name: String,
    val logo: String
)

fun vacancy1(): VacancyDetail {
    return VacancyDetail(
        id = "000aa9c3-18c4-357a-8aaa-6def258d5601",
        name = "Frontend-разработчик",
        description = "<h2>Описание вакансии</h2><p>Ищем frontend-разработчика для работы над пользовательскими кабинетами и публичными интерфейсами.</p><p>Нужен инженер, который одинаково внимательно относится к UX, производительности и поддерживаемости кода.</p><section><h3>Обязанности</h3><ul><li>Разрабатывать интерфейсы на React и TypeScript.</li><li>Поддерживать дизайн-систему и повторно используемые компоненты.</li><li>Оптимизировать загрузку страниц и стабильность клиентской части.</li></ul></section><section><h3>Требования</h3><ul><li>Уверенное знание JavaScript или TypeScript.</li><li>Опыт работы с React, HTML и CSS.</li><li>Понимание адаптивной верстки, accessibility и REST API.</li></ul></section><section><h3>Условия</h3><ul><li>Прямое взаимодействие с дизайнерами и продуктом.</li><li>Регулярный пересмотр технических решений и места для рефакторинга.</li><li>Гибкий график и современный стек разработки.</li></ul></section>",
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
            email = "",
            phones = listOf("+7 (999) 567-89-01", "+7 (999) 543-21-09")
        ),
        employer = Employer(
            name = "Netflix",
            logo = "https://upload.wikimedia.org/wikipedia/commons/6/69/Netflix_logo.svg"
        ),
        area = "Ростов-на-Дону",
        skills = listOf("TypeScript", "React", "HTML", "CSS", "REST API"),
        url = "cek6h0n4ffe7ur.cluster-czz5s0kz4scl.eu-west-1.rds.amazonaws.com/vacancies/000aa9c3-18c4-357a-8aaa-6def258d5601",
        industry = "Информационные технологии, системная интеграция, интернет"
    )
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewVacancyDetail() {
    VacancyDetailScreen(vacancy1())
}
