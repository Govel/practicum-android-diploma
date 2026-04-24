package ru.practicum.android.diploma.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R

data class ActionBack(
    val isView: Boolean = false,
    val onClick: (() -> Unit)? = null
)

data class ActionFilter(
    val isView: Boolean = false,
    val onClick: (() -> Unit)? = null
)

data class ActionFavorites(
    val isView: Boolean = false,
    val onClick: (() -> Unit)? = null,
    val isFavorites: Boolean = false
)

data class ActionShare(
    val isView: Boolean = false,
    val onClick: (() -> Unit)? = null
)

const val WEIGHT_COLUMN = 0.5f
@Composable
fun AppBarTop(
    title: String,
    back: ActionBack = ActionBack(),
    filter: ActionFilter = ActionFilter(),
    share: ActionShare = ActionShare(),
    favorites: ActionFavorites = ActionFavorites(),
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .height(64.dp)
            .background(MaterialTheme.colorScheme.onPrimary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppButtonBack(
            isViewIcon = back.isView,
            onClick = back.onClick
        )

        AppTitle(title = title)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(WEIGHT_COLUMN)
                .padding(end = 8.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            AppButtonAction(
                filter,
                share,
                favorites
            )
        }
    }
}

@Composable
private fun AppButtonBack(
    isViewIcon: Boolean,
    onClick: (() -> Unit)?
) {
    if (isViewIcon) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_back_24),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .clickable { onClick?.invoke() },
        )
    }
}

@Composable
private fun AppTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 16.dp)
            .padding(vertical = 20.dp),
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
private fun AppButtonAction(
    filter: ActionFilter = ActionFilter(),
    share: ActionShare = ActionShare(),
    favorites: ActionFavorites = ActionFavorites(),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    ) {
        if (filter.isView) {
            Icon(
                painter = painterResource(id = R.drawable.ic_filter_off__24),
                contentDescription = stringResource(R.string.filter_settings),
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .clickable { filter.onClick?.invoke() },
            )
        }

        if (share.isView) {
            Icon(
                painter = painterResource(id = R.drawable.ic_sharing_24),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .clickable { share.onClick?.invoke() },
            )
        }

        if (favorites.isView) {
            Icon(
                painter = painterResource(id = R.drawable.ic_favorites_off__24),
                contentDescription = stringResource(R.string.favorites),
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .clickable { favorites.onClick?.invoke() },
            )
        }
    }
}
