package store.strangeinfinity.calculator.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun KeypadStandard(
    hapticEnabled: Boolean,
    onDigit: (String) -> Unit,
    onOperator: (String) -> Unit,
    onClear: () -> Unit,
    onParentheses: () -> Unit,
    onPercent: () -> Unit,
    onDecimal: () -> Unit,
    onPlusMinus: () -> Unit,
    onEquals: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Row 1: C, ( ), %, ÷
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CalculatorButton(
                text = "C",
                contentDesc = "Clear",
                type = ButtonType.CLEAR,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = onClear
            )
            CalculatorButton(
                text = "( )",
                contentDesc = "Parentheses",
                type = ButtonType.FUNCTION,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = onParentheses
            )
            CalculatorButton(
                text = "%",
                contentDesc = "Percentage",
                type = ButtonType.FUNCTION,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = onPercent
            )
            CalculatorButton(
                text = "÷",
                contentDesc = "Divide",
                type = ButtonType.OPERATOR,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onOperator("÷") }
            )
        }

        // Row 2: 7, 8, 9, ×
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
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
                text = "×",
                contentDesc = "Multiply",
                type = ButtonType.OPERATOR,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onOperator("×") }
            )
        }

        // Row 3: 4, 5, 6, −
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
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
                text = "−",
                contentDesc = "Minus",
                type = ButtonType.OPERATOR,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onOperator("−") }
            )
        }

        // Row 4: 1, 2, 3, +
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
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
                text = "+",
                contentDesc = "Plus",
                type = ButtonType.OPERATOR,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onOperator("+") }
            )
        }

        // Row 5: +/−, 0, ., =
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CalculatorButton(
                text = "+/−",
                contentDesc = "Plus minus",
                type = ButtonType.FUNCTION,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = onPlusMinus
            )
            CalculatorButton(
                text = "0",
                contentDesc = "0",
                type = ButtonType.NUMBER,
                modifier = Modifier.weight(1f),
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
                text = "=",
                contentDesc = "Equals",
                type = ButtonType.EQUALS,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = onEquals
            )
        }
    }
}
