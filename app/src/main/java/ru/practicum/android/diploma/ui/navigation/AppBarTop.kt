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
    val onClick: (() -> Unit)? = null,
    val isActive: Boolean = false,
)

data class ActionFavorites(
    val isView: Boolean = false,
    val onClick: (() -> Unit)? = null,
    val isActive: Boolean = false
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
            .height(64.dp)
            .background(MaterialTheme.colorScheme.onPrimary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppButtonBack(
            isViewIcon = back.isView,
            onClick = back.onClick
        )

        AppTitle(title = title, hasBackButton = back.isView)

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
                .padding(horizontal = 16.dp)
                .clickable { onClick?.invoke() },
        )
    }
}

@Composable
private fun AppTitle(
    title: String,
    hasBackButton: Boolean = false
) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = if (hasBackButton) 4.dp else 16.dp)
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
        FilterIcon(filter)

        ShareIcon(share)

        FavoritesIcon(favorites)
    }
}

@Composable
private fun FilterIcon(
    filter: ActionFilter
) {
    if (!filter.isView) return

    val iconActive = if (filter.isActive) {
        R.drawable.ic_filter_on__24
    } else {
        R.drawable.ic_filter_off__24
    }

    val colorActive = if (filter.isActive) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Icon(
        painter = painterResource(id = iconActive),
        contentDescription = stringResource(R.string.filter_settings),
        tint = Color.Unspecified,
        modifier = Modifier
            .padding(end = 12.dp)
            .clickable { filter.onClick?.invoke() },
    )
}

@Composable
private fun ShareIcon(
    share: ActionShare
) {
    if (!share.isView) return

    Icon(
        painter = painterResource(id = R.drawable.ic_sharing_24),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = Modifier
            .padding(end = 20.dp)
            .clickable { share.onClick?.invoke() },
    )
}

@Composable
private fun FavoritesIcon(
    favorites: ActionFavorites
) {
    if (!favorites.isView) return

    val iconActive = if (favorites.isActive) {
        R.drawable.ic_favorites_on__24
    } else {
        R.drawable.ic_favorites_off__24
    }

    Icon(
        painter = painterResource(id = iconActive),
        contentDescription = stringResource(R.string.favorites),
        tint = Color.Unspecified,
        modifier = Modifier
            .padding(end = 20.dp)
            .clickable { favorites.onClick?.invoke() },
    )
}
