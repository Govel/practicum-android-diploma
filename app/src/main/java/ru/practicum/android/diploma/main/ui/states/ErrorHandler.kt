package ru.practicum.android.diploma.main.ui.states

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R

@Composable
fun ErrorScreen(
    errorType: ErrorType
) {
    val (imageRes, titleRes) = when (errorType) {
        is ErrorType.Network -> R.drawable.no_internet to R.string.no_internet
        is ErrorType.Server -> R.drawable.server_error to R.string.server_error
        is ErrorType.Unknown -> R.drawable.server_error to R.string.server_error
    }

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
            painter = painterResource(id = imageRes),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(horizontal = 48.dp),
            text = stringResource(titleRes),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}
