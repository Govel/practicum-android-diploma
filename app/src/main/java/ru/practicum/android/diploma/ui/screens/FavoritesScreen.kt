package ru.practicum.android.diploma.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R

@Composable
fun FavoritesScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.favorites),
            style = MaterialTheme.typography.titleLarge
        )
    }
}
