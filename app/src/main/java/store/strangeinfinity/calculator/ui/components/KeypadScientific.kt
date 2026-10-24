package store.strangeinfinity.calculator.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import store.strangeinfinity.calculator.domain.calculator.AngleUnit

@Composable
fun KeypadScientificPortrait(
    hapticEnabled: Boolean,
    onScientificFunction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // Row 1: sin, cos, tan, log
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CalculatorButton(
                text = "sin",
                contentDesc = "Sine",
                type = ButtonType.FUNCTION,
                fontSize = 17.sp,
                aspectRatio = 1.6f,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("sin") }
            )
            CalculatorButton(
                text = "cos",
                contentDesc = "Cosine",
                type = ButtonType.FUNCTION,
                fontSize = 17.sp,
                aspectRatio = 1.6f,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("cos") }
            )
            CalculatorButton(
                text = "tan",
                contentDesc = "Tangent",
                type = ButtonType.FUNCTION,
                fontSize = 17.sp,
                aspectRatio = 1.6f,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("tan") }
            )
            CalculatorButton(
                text = "log",
                contentDesc = "Log base 10",
                type = ButtonType.FUNCTION,
                fontSize = 17.sp,
                aspectRatio = 1.6f,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("log") }
            )
        }

        // Row 2: ln, √, x², xʸ
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CalculatorButton(
                text = "ln",
                contentDesc = "Natural log",
                type = ButtonType.FUNCTION,
                fontSize = 17.sp,
                aspectRatio = 1.6f,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("ln") }
            )
            CalculatorButton(
                text = "√",
                contentDesc = "Square root",
                type = ButtonType.FUNCTION,
                fontSize = 17.sp,
                aspectRatio = 1.6f,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("√") }
            )
            CalculatorButton(
                text = "x²",
                contentDesc = "Square",
                type = ButtonType.FUNCTION,
                fontSize = 17.sp,
                aspectRatio = 1.6f,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("x²") }
            )
            CalculatorButton(
                text = "xʸ",
                contentDesc = "Power",
                type = ButtonType.FUNCTION,
                fontSize = 17.sp,
                aspectRatio = 1.6f,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("xʸ") }
            )
        }

        // Row 3: 1/x, π, e, x!
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CalculatorButton(
                text = "1/x",
                contentDesc = "Reciprocal",
                type = ButtonType.FUNCTION,
                fontSize = 17.sp,
                aspectRatio = 1.6f,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("1/x") }
            )
            CalculatorButton(
                text = "π",
                contentDesc = "Pi",
                type = ButtonType.FUNCTION,
                fontSize = 17.sp,
                aspectRatio = 1.6f,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("π") }
            )
            CalculatorButton(
                text = "e",
                contentDesc = "Euler's number",
                type = ButtonType.FUNCTION,
                fontSize = 17.sp,
                aspectRatio = 1.6f,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("e") }
            )
            CalculatorButton(
                text = "x!",
                contentDesc = "Factorial",
                type = ButtonType.FUNCTION,
                fontSize = 17.sp,
                aspectRatio = 1.6f,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("x!") }
            )
        }
    }
}

@Composable
fun KeypadLandscape(
    hapticEnabled: Boolean,
    angleUnit: AngleUnit,
    onAngleToggle: () -> Unit,
    onDigit: (String) -> Unit,
    onOperator: (String) -> Unit,
    onScientificFunction: (String) -> Unit,
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
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Row 1: Rad/Deg, √, ∛ | C, ( ), %, ÷
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CalculatorButton(
                text = if (angleUnit == AngleUnit.DEGREE) "Deg" else "Rad",
                contentDesc = "Angle mode",
                type = ButtonType.FUNCTION,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = onAngleToggle
            )
            CalculatorButton(
                text = "√",
                contentDesc = "Square root",
                type = ButtonType.FUNCTION,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("√") }
            )
            CalculatorButton(
                text = "∛",
                contentDesc = "Cube root",
                type = ButtonType.FUNCTION,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("∛") }
            )
            CalculatorButton(
                text = "C",
                contentDesc = "Clear",
                type = ButtonType.CLEAR,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = onClear
            )
            CalculatorButton(
                text = "( )",
                contentDesc = "Parentheses",
                type = ButtonType.FUNCTION,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = onParentheses
            )
            CalculatorButton(
                text = "%",
                contentDesc = "Percentage",
                type = ButtonType.FUNCTION,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = onPercent
            )
            CalculatorButton(
                text = "÷",
                contentDesc = "Divide",
                type = ButtonType.OPERATOR,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onOperator("÷") }
            )
        }

        // Row 2: sin, cos, tan | 7, 8, 9, ×
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CalculatorButton(
                text = "sin",
                contentDesc = "Sine",
                type = ButtonType.FUNCTION,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("sin") }
            )
            CalculatorButton(
                text = "cos",
                contentDesc = "Cosine",
                type = ButtonType.FUNCTION,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("cos") }
            )
            CalculatorButton(
                text = "tan",
                contentDesc = "Tangent",
                type = ButtonType.FUNCTION,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("tan") }
            )
            CalculatorButton(
                text = "7",
                contentDesc = "7",
                type = ButtonType.NUMBER,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onDigit("7") }
            )
            CalculatorButton(
                text = "8",
                contentDesc = "8",
                type = ButtonType.NUMBER,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onDigit("8") }
            )
            CalculatorButton(
                text = "9",
                contentDesc = "9",
                type = ButtonType.NUMBER,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onDigit("9") }
            )
            CalculatorButton(
                text = "×",
                contentDesc = "Multiply",
                type = ButtonType.OPERATOR,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onOperator("×") }
            )
        }

        // Row 3: ln, log, 1/x | 4, 5, 6, −
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CalculatorButton(
                text = "ln",
                contentDesc = "Natural log",
                type = ButtonType.FUNCTION,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("ln") }
            )
            CalculatorButton(
                text = "log",
                contentDesc = "Log 10",
                type = ButtonType.FUNCTION,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("log") }
            )
            CalculatorButton(
                text = "1/x",
                contentDesc = "Reciprocal",
                type = ButtonType.FUNCTION,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("1/x") }
            )
            CalculatorButton(
                text = "4",
                contentDesc = "4",
                type = ButtonType.NUMBER,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onDigit("4") }
            )
            CalculatorButton(
                text = "5",
                contentDesc = "5",
                type = ButtonType.NUMBER,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onDigit("5") }
            )
            CalculatorButton(
                text = "6",
                contentDesc = "6",
                type = ButtonType.NUMBER,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onDigit("6") }
            )
            CalculatorButton(
                text = "−",
                contentDesc = "Minus",
                type = ButtonType.OPERATOR,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onOperator("−") }
            )
        }

        // Row 4: eˣ, x², xʸ | 1, 2, 3, +
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CalculatorButton(
                text = "eˣ",
                contentDesc = "e to power",
                type = ButtonType.FUNCTION,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("eˣ") }
            )
            CalculatorButton(
                text = "x²",
                contentDesc = "Square",
                type = ButtonType.FUNCTION,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("x²") }
            )
            CalculatorButton(
                text = "xʸ",
                contentDesc = "Power",
                type = ButtonType.FUNCTION,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("xʸ") }
            )
            CalculatorButton(
                text = "1",
                contentDesc = "1",
                type = ButtonType.NUMBER,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onDigit("1") }
            )
            CalculatorButton(
                text = "2",
                contentDesc = "2",
                type = ButtonType.NUMBER,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onDigit("2") }
            )
            CalculatorButton(
                text = "3",
                contentDesc = "3",
                type = ButtonType.NUMBER,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onDigit("3") }
            )
            CalculatorButton(
                text = "+",
                contentDesc = "Plus",
                type = ButtonType.OPERATOR,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onOperator("+") }
            )
        }

        // Row 5: |x|, π, e | +/−, 0, ., =
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CalculatorButton(
                text = "|x|",
                contentDesc = "Absolute value",
                type = ButtonType.FUNCTION,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("|x|") }
            )
            CalculatorButton(
                text = "π",
                contentDesc = "Pi",
                type = ButtonType.FUNCTION,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("π") }
            )
            CalculatorButton(
                text = "e",
                contentDesc = "Euler's number",
                type = ButtonType.FUNCTION,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onScientificFunction("e") }
            )
            CalculatorButton(
                text = "+/−",
                contentDesc = "Plus minus",
                type = ButtonType.FUNCTION,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = onPlusMinus
            )
            CalculatorButton(
                text = "0",
                contentDesc = "0",
                type = ButtonType.NUMBER,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = { onDigit("0") }
            )
            CalculatorButton(
                text = ".",
                contentDesc = "Decimal point",
                type = ButtonType.NUMBER,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = onDecimal
            )
            CalculatorButton(
                text = "=",
                contentDesc = "Equals",
                type = ButtonType.EQUALS,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f),
                hapticEnabled = hapticEnabled,
                onClick = onEquals
            )
        }
    }
}
