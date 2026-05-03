package ru.practicum.android.diploma.ui.screens.filter.industry

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.navigation.ActionBack
import ru.practicum.android.diploma.ui.navigation.AppBarTop

@Composable
fun IndustryScreen() {
    var selectedIndustryId: String? by remember { mutableStateOf(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppBarTop(
            title = stringResource(R.string.select_industries),
            back = ActionBack(isView = true),
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary),
        ) {
            val items: List<String> = listOf(
                "IT-сфера",
                "Финансы",
                "Маркетинг"
            )
            items(items = items) { industryId ->
                IndustryItem(
                    item = industryId,
                    isSelected = selectedIndustryId == industryId,
                    onSelect = { selectedIndustryId = industryId }
                )
            }
        }
    }
}

@Composable
fun IndustryItem(
    item: String,
    isSelected: Boolean,
    onSelect: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item,
            modifier = Modifier.weight(1f)
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

@Preview(showSystemUi = false)
@Composable
fun PreviewIndustryScreen() {
    IndustryScreen()
}
