package ru.practicum.android.diploma.ui.screens.filter.workplace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.filter.ui.screen.SelectField
import ru.practicum.android.diploma.ui.navigation.ActionBack
import ru.practicum.android.diploma.ui.navigation.AppBarTop

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkPlaceScreen(
    onBack: () -> Unit = {},
    onCountry: () -> Unit = {},
    onRegion: () -> Unit = {},
    onApply: () -> Unit = {}
) {
    var countryPlaceText by remember { mutableStateOf("") }
    var regionPlaceText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        AppBarTop(
            title = stringResource(R.string.select_work_place),
            back = ActionBack(
                isView = true,
                onClick = onBack
            )
        )

        Column(modifier = Modifier.padding(top = 16.dp)) {
            SelectField(
                label = stringResource(R.string.country),
                value = countryPlaceText,
                onClear = { countryPlaceText = "" },
                onNavigate = { onCountry() }
            )

            SelectField(
                label = stringResource(R.string.region),
                value = regionPlaceText,
                onClear = { regionPlaceText = "" },
                onNavigate = { onRegion() }
            )
        }

        if (countryPlaceText.isNotEmpty() || regionPlaceText.isNotEmpty()) {
            ButtonApply(
                onApply = {
                    onApply()
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp)
            )
        }
    }
}

@Composable
private fun SelectField(
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        onClick = onNavigate
    ) {
    }
}

@Composable
fun ButtonApply(
    onApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(59.dp),
            onClick = onApply,
            enabled = true,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            content = {
                Text(
                    text = stringResource(R.string.apply),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun WorkPlaceScreenPreview() {
    WorkPlaceScreen(
        onBack = {},
        onCountry = {},
        onRegion = {},
        onApply = {}
    )
}
