package ru.practicum.android.diploma.ui.screens.filter.workplace

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.filter.workplace.region.RegionViewModel
import ru.practicum.android.diploma.filter.workplace.region.domain.models.FilterRegion
import ru.practicum.android.diploma.filter.workplace.region.domain.models.RegionsState
import ru.practicum.android.diploma.ui.navigation.ActionBack
import ru.practicum.android.diploma.ui.navigation.AppBarTop

@Composable
fun RegionScreen(
    onBack: () -> Unit = {},
    viewModel: RegionViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadRegiones()
    }

    Scaffold(
        topBar = {
            AppBarTop(
                title = stringResource(R.string.select_region),
                back = ActionBack(isView = true, onClick = onBack),
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.onPrimary)
            ) {
                SearchTextField(viewModel = viewModel)

                Box(modifier = Modifier.weight(1f)) {
                    RenderState(state = state, viewModel = viewModel)
                }
            }
        }
    )
}

@Composable
private fun SearchTextField(
    viewModel: RegionViewModel
) {
    val searchText by viewModel.query.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { newText ->
                viewModel.onSearchTextChanged(newText)
            },
            placeholder = {
                Text(
                    stringResource(R.string.enter_region),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            trailingIcon = {
                if (searchText.isEmpty()) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    IconButton(onClick = {
                        viewModel.onSearchTextChanged("")
                        focusManager.clearFocus()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close_24),
                            contentDescription = stringResource(R.string.clear),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
private fun RenderState(
    state: RegionsState,
    viewModel: RegionViewModel
) {
    when (state) {
        is RegionsState.Loading -> Loading()
        is RegionsState.Error -> EmptyErrorRegion(
            painter = painterResource(R.drawable.magic_carpet),
            text = stringResource(R.string.region_not_found)
        )
        is RegionsState.Empty -> EmptyErrorRegion(
            painter = painterResource(R.drawable.magic_carpet),
            text = stringResource(R.string.failed_to_get_list)
        )
        is RegionsState.Content -> RegionsResult(viewModel = viewModel)
    }
}

@Composable
private fun Loading() {
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
private fun EmptyErrorRegion(
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

@Composable
private fun RegionsResult(
    viewModel: RegionViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state is RegionsState.Content) {
        val regions = (state as RegionsState.Content).regions
        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onPrimary),
        ) {
            items(
                items = regions,
                key = { it.id }
            ) { region ->
                RegionItem(
                    item = region,
                    onSelect = viewModel::selectRegion
                )
            }
        }
    }
}

@Composable
private fun RegionItem(
    item: FilterRegion,
    onSelect: (FilterRegion) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup()
            .height(60.dp)
            .clickable(onClick = { onSelect(item) }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = item.name,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_forward_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(8.dp)
                .size(32.dp)
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun RegionScreenPreview() {
    RegionScreen()
}
