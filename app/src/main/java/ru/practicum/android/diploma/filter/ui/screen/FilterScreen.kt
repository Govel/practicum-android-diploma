package ru.practicum.android.diploma.filter.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.filter.ui.FilterViewModel
import ru.practicum.android.diploma.ui.navigation.ActionBack
import ru.practicum.android.diploma.ui.navigation.AppBarTop

@Composable
fun FilterScreen(
    onBack: () -> Unit = {},
    onIndustry: () -> Unit = {},
    onWorkPlace: () -> Unit = {},
    onApply: () -> Unit = {},
    onReset: () -> Unit = {},
    viewModel: FilterViewModel = koinViewModel()
) {
    var workPlaceText by remember { mutableStateOf("") }
    var industryText by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        AppBarTop(
            title = stringResource(R.string.filter_settings),
            back = ActionBack(
                isView = true,
                onClick = onBack
            )
        )

        Column(modifier = Modifier.padding(top = 16.dp)) {
            SelectField(
                label = stringResource(R.string.work_place),
                value = workPlaceText,
                onClear = { workPlaceText = "" },
                onNavigate = onWorkPlace
            )

            SelectField(
                label = stringResource(R.string.industry),
                value = industryText,
                onClear = { industryText = "" },
                onNavigate = onIndustry
            )
        }

        SalaryField(
            value = state.salary,
            onValueChange = { viewModel.updateSalary(it) },
            onClear = { viewModel.updateSalary("") }
        )

        Column(modifier = Modifier.padding(top = 24.dp)) {
            NoSalaryCheckbox(
                checked = state.onlyWithSalary,
                onCheckedChange = { viewModel.updateOnlyWithSalary(!state.onlyWithSalary) }
            )
        }

        if (state.hasAnyFilter) {
            FilterButtons(
                onApply = {
                    onApply()
                },
                onReset = {
                    viewModel.clearAllFilters()
                    onReset()
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
fun SelectField(
    label: String,
    value: String,
    onClear: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        onClick = onNavigate
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { },
            singleLine = true,
            enabled = false,
            label = {
                Text(
                    label,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconButton(onClick = onClear) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close_24),
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_forward_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(32.dp)
                    )

                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
                disabledTextColor = MaterialTheme.colorScheme.onBackground,
                disabledBorderColor = Color.Transparent,
                disabledLabelColor = MaterialTheme.colorScheme.secondary
            )
        )
    }
}

@Composable
fun SalaryField(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            SalaryLabel(isFocused = isFocused, value = value)
            SalaryInputRow(
                value = value,
                isFocused = isFocused,
                onValueChange = onValueChange,
                onFocusChange = { isFocused = it }
            )
        }
        SalaryClearButton(value = value, onClear = onClear)
    }
}

@Composable
private fun SalaryLabel(isFocused: Boolean, value: String) {
    Text(
        text = stringResource(R.string.expected_salary),
        style = MaterialTheme.typography.bodySmall,
        color = if (isFocused || value.isNotEmpty()) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.secondary
        },
        modifier = Modifier.padding(bottom = 2.dp)
    )
}

@Composable
private fun SalaryInputRow(
    value: String,
    isFocused: Boolean,
    onValueChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(22.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .onFocusEvent { event ->
                    onFocusChange(event.isFocused)
                },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                SalaryDecorationBox(
                    value = value,
                    isFocused = isFocused,
                    innerTextField = innerTextField
                )
            }
        )
    }
}

@Composable
private fun SalaryDecorationBox(
    value: String,
    isFocused: Boolean,
    innerTextField: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (value.isEmpty() && !isFocused) {
            Text(
                text = stringResource(R.string.enter_amount),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        innerTextField()
    }
}

@Composable
private fun SalaryClearButton(value: String, onClear: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        if (value.isNotEmpty()) {
            IconButton(
                onClick = onClear,
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close_24),
                    contentDescription = "Clear",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun NoSalaryCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.do_not_show_without_salary),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge
        )

        IconButton(onClick = { onCheckedChange(!checked) }) {
            Icon(
                painter = painterResource(
                    id = if (checked) {
                        R.drawable.ic_check_box_on__24
                    } else {
                        R.drawable.ic_check_box_off__24
                    }
                ),
                contentDescription = null,
                Modifier
                    .padding(8.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun FilterButtons(
    onApply: () -> Unit,
    onReset: () -> Unit,
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
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(59.dp)
                .padding(top = 8.dp),
            onClick = onReset,
            enabled = true,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimary),
            content = {
                Text(
                    text = stringResource(R.string.reset),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        )
    }
}

@Preview(showSystemUi = false, name = "test")
@Composable
fun TestFilter() {
    FilterScreen()
}
