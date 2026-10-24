package store.strangeinfinity.calculator.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import store.strangeinfinity.calculator.domain.converter.ConversionUnit
import store.strangeinfinity.calculator.domain.converter.UnitCategory
import store.strangeinfinity.calculator.domain.converter.UnitConverter
import store.strangeinfinity.calculator.ui.ConverterViewModel
import store.strangeinfinity.calculator.ui.theme.LocalCalculatorPalette
import store.strangeinfinity.calculator.ui.theme.SamsungGreen

@Composable
fun UnitConverterScreen(
    viewModel: ConverterViewModel,
    hapticEnabled: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = LocalCalculatorPalette.current
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val categoryScrollState = rememberScrollState()

    var showFromDropdown by remember { mutableStateOf(false) }
    var showToDropdown by remember { mutableStateOf(false) }

    val currentUnits = UnitConverter.unitsMap[state.category] ?: emptyList()

    Scaffold(
        containerColor = palette.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to calculator",
                        tint = palette.resultText
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Unit Converter",
                    color = palette.resultText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Category Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(categoryScrollState),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    UnitCategory.entries.forEach { category ->
                        val isSelected = category == state.category
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectCategory(category) },
                            label = { Text(category.displayName) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SamsungGreen,
                                selectedLabelColor = palette.operatorKeyText,
                                containerColor = palette.surface,
                                labelColor = palette.resultText
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // From Unit Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = palette.surface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showFromDropdown = true },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${state.fromUnit.name} (${state.fromUnit.symbol})",
                                color = SamsungGreen,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Select unit",
                                tint = SamsungGreen
                            )
                            DropdownMenu(
                                expanded = showFromDropdown,
                                onDismissRequest = { showFromDropdown = false }
                            ) {
                                currentUnits.forEach { unit ->
                                    DropdownMenuItem(
                                        text = { Text("${unit.name} (${unit.symbol})") },
                                        onClick = {
                                            viewModel.selectFromUnit(unit)
                                            showFromDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = state.fromValueText,
                            color = palette.resultText,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Swap Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { viewModel.swapUnits() },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(palette.surface)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = "Swap units",
                            tint = SamsungGreen
                        )
                    }
                }

                // To Unit Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = palette.surface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showToDropdown = true },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${state.toUnit.name} (${state.toUnit.symbol})",
                                color = SamsungGreen,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Select unit",
                                tint = SamsungGreen
                            )
                            DropdownMenu(
                                expanded = showToDropdown,
                                onDismissRequest = { showToDropdown = false }
                            ) {
                                currentUnits.forEach { unit ->
                                    DropdownMenuItem(
                                        text = { Text("${unit.name} (${unit.symbol})") },
                                        onClick = {
                                            viewModel.selectToUnit(unit)
                                            showToDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = state.toValueText,
                                color = palette.resultText,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = {
                                viewModel.copyConvertedValue()
                                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy result",
                                    tint = palette.iconTint
                                )
                            }
                        }
                    }
                }
            }

            // Dedicated Numeric Keypad for Unit Converter
            ConverterKeypad(
                hapticEnabled = hapticEnabled,
                onDigit = { viewModel.onDigit(it) },
                onDecimal = { viewModel.onDecimal() },
                onBackspace = { viewModel.onBackspace() },
                onClear = { viewModel.onClear() },
                onToggleSign = { viewModel.onToggleSign() }
            )
        }
    }
}

@Composable
private fun ConverterKeypad(
    hapticEnabled: Boolean,
    onDigit: (String) -> Unit,
    onDecimal: () -> Unit,
    onBackspace: () -> Unit,
    onClear: () -> Unit,
    onToggleSign: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Row 1: 7, 8, 9, C
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CalculatorButton(
                text = "7",
                contentDesc = "7",
                type = ButtonType.NUMBER,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onDigit("7") }
            )
            CalculatorButton(
                text = "8",
                contentDesc = "8",
                type = ButtonType.NUMBER,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onDigit("8") }
            )
            CalculatorButton(
                text = "9",
                contentDesc = "9",
                type = ButtonType.NUMBER,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onDigit("9") }
            )
            CalculatorButton(
                text = "C",
                contentDesc = "Clear",
                type = ButtonType.CLEAR,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = onClear
            )
        }

        // Row 2: 4, 5, 6, Backspace
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CalculatorButton(
                text = "4",
                contentDesc = "4",
                type = ButtonType.NUMBER,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onDigit("4") }
            )
            CalculatorButton(
                text = "5",
                contentDesc = "5",
                type = ButtonType.NUMBER,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onDigit("5") }
            )
            CalculatorButton(
                text = "6",
                contentDesc = "6",
                type = ButtonType.NUMBER,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onDigit("6") }
            )
            CalculatorButton(
                text = "⌫",
                contentDesc = "Backspace",
                type = ButtonType.FUNCTION,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = onBackspace
            )
        }

        // Row 3: 1, 2, 3, +/-
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CalculatorButton(
                text = "1",
                contentDesc = "1",
                type = ButtonType.NUMBER,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onDigit("1") }
            )
            CalculatorButton(
                text = "2",
                contentDesc = "2",
                type = ButtonType.NUMBER,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onDigit("2") }
            )
            CalculatorButton(
                text = "3",
                contentDesc = "3",
                type = ButtonType.NUMBER,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onDigit("3") }
            )
            CalculatorButton(
                text = "+/−",
                contentDesc = "Toggle sign",
                type = ButtonType.FUNCTION,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = onToggleSign
            )
        }

        // Row 4: 0, .
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CalculatorButton(
                text = "0",
                contentDesc = "0",
                type = ButtonType.NUMBER,
                modifier = Modifier.weight(2f),
                hapticEnabled = hapticEnabled,
                onClick = { onDigit("0") }
            )
            CalculatorButton(
                text = ".",
                contentDesc = "Decimal point",
                type = ButtonType.NUMBER,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = onDecimal
            )
            CalculatorButton(
                text = "✓",
                contentDesc = "Done",
                type = ButtonType.OPERATOR,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = onBackspace
            )
        }
    }
}
