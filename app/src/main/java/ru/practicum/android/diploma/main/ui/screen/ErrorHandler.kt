package ru.practicum.android.diploma.main.ui.screen

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

object ErrorHandler {

    fun getErrorType(errorMessage: String): Boolean {
        return errorMessage.contains("интернет", ignoreCase = true) ||
            errorMessage.contains("network", ignoreCase = true) ||
            errorMessage.contains("connection", ignoreCase = true)
    }
}

@Composable
fun ErrorScreen(
    isNetworkError: Boolean
) {
    val imageRes = if (isNetworkError) {
        R.drawable.no_internet
    } else {
        R.drawable.server_error
    }

    val titleRes = if (isNetworkError) {
        R.string.no_internet
    } else {
        R.string.server_error
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
