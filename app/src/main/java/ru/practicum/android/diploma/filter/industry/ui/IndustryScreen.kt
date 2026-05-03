package ru.practicum.android.diploma.filter.industry.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.filter.industry.domain.models.FilterIndustry
import ru.practicum.android.diploma.filter.industry.domain.models.IndustriesState
import ru.practicum.android.diploma.ui.navigation.ActionBack
import ru.practicum.android.diploma.ui.navigation.AppBarTop

@Composable
fun IndustryScreen(
    onBack: () -> Unit = {},
    viewModel: IndustryViewModel = koinViewModel()
) {
    var isSelected: Boolean? by remember { mutableStateOf(false) }
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadIndustries()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        AppBarTop(
            title = stringResource(R.string.select_industry),
            back = ActionBack(
                isView = true,
                onClick = onBack
            ),
        )
        Render(state = state, viewModel = viewModel)
        if (isSelected ?: false) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 16.dp),
                onClick = { },
                enabled = true,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                content = {
                    Text(
                        text = stringResource(R.string.select),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun IndustriesResult(
    industries: List<FilterIndustry>,
    viewModel: IndustryViewModel
) {
    var searchText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val selectedId by viewModel.selectedId.collectAsStateWithLifecycle()
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
                searchText = newText
                // viewModel.onSearchTextChanged(newText)
            },
            placeholder = {
                Text(
                    stringResource(R.string.enter_industry),
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
                        searchText = ""
                        focusManager.clearFocus()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close_24),
                            contentDescription = "Clear",
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
    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.onPrimary),
    ) {
        items(
            items = industries,
            key = { it.id }
        ) { industry ->
            IndustryItem(
                item = industry,
                isSelected = selectedId == industry.id,
                onSelect = { viewModel.selectIndustry(industry.id) }
            )
        }
    }
}

@Composable
fun IndustryItem(
    item: FilterIndustry,
    isSelected: Boolean,
    onSelect: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(onClick = onSelect),
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
        CustomRadioButton(
            selected = isSelected,
            onSelectionChanged = onSelect,
            enabled = true,
            modifier = Modifier.padding(start = 28.dp, end = 16.dp)
        )
    }
}

@Composable
fun CustomRadioButton(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onSelectionChanged: () -> Unit = {},
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .toggleable(
                value = selected,
                onValueChange = { onSelectionChanged() },
                enabled = enabled,
                role = Role.RadioButton,
                interactionSource = interactionSource,
                indication = null,
            )
            .size(24.dp),
    ) {
        if (selected) SelectedImage() else UnselectedImage()
    }
}

@Composable
fun SelectedImage() {
    Image(
        painter = painterResource(R.drawable.ic_radio_button_on__24),
        contentDescription = null,
        modifier = Modifier.size(24.dp)
    )
}

@Composable
fun UnselectedImage() {
    Image(
        painter = painterResource(R.drawable.ic_radio_button_off__24),
        contentDescription = null,
        modifier = Modifier.size(24.dp)
    )
}

@Composable
fun Render(state: IndustriesState, viewModel: IndustryViewModel) {
    when (state) {
        is IndustriesState.Loading -> LoadingIndustry()
        is IndustriesState.Error -> ErrorIndustry(
            painter = painterResource(R.drawable.no_internet),
            text = stringResource(R.string.no_internet)
        )
        is IndustriesState.Empty -> ErrorIndustry(
            painter = painterResource(R.drawable.empty_cat),
            text = stringResource(R.string.failed_to_get_list)
        )
        is IndustriesState.Content -> IndustriesResult(industries = state.industries, viewModel = viewModel)
    }
}

@Composable
private fun LoadingIndustry() {
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
private fun ErrorIndustry(
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

@Preview(showSystemUi = false)
@Composable
fun PreviewIndustryScreen() {
    IndustryScreen()
}
