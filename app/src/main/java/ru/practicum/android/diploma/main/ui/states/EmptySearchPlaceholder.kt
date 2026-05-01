package ru.practicum.android.diploma.main.ui.states

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R

@Composable
fun EmptySearchPlaceholder() {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.Center,
            painter = painterResource(R.drawable.job_search),
            contentDescription = "Ищу работу"
        )
    }
}
