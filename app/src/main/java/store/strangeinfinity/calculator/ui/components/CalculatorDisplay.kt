package store.strangeinfinity.calculator.ui.components

import android.widget.Toast
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import store.strangeinfinity.calculator.domain.calculator.NumberFormatter
import store.strangeinfinity.calculator.ui.theme.LocalCalculatorPalette
import store.strangeinfinity.calculator.ui.theme.SamsungClearRed

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun CalculatorDisplay(
    expression: String,
    previewResult: String,
    errorMessage: String?,
    lastEvaluated: Boolean,
    modifier: Modifier = Modifier,
    onCopyResult: (String) -> Unit
) {
    val palette = LocalCalculatorPalette.current
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Auto-scroll expression to the end when text updates
    LaunchedEffect(expression) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    val displayExpression = NumberFormatter.formatExpression(expression)

    // Calculate dynamic font size for result
    val resultText = when {
        errorMessage != null -> errorMessage
        lastEvaluated -> previewResult
        previewResult.isNotEmpty() -> previewResult
        expression.isNotEmpty() -> ""
        else -> "0"
    }

    val resultFontSize = when {
        resultText.length > 16 -> 28.sp
        resultText.length > 12 -> 34.sp
        resultText.length > 8 -> 40.sp
        else -> 48.sp
    }

    val resultColor = when {
        errorMessage != null -> SamsungClearRed
        lastEvaluated -> palette.resultText
        previewResult.isNotEmpty() -> palette.expressionText
        else -> palette.resultText.copy(alpha = 0.5f)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        // Expression Line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = displayExpression,
                color = palette.expressionText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.End,
                maxLines = 1,
                modifier = Modifier.semantics {
                    this.contentDescription = "Expression: $expression"
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Result / Preview Line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {},
                    onLongClick = {
                        if (resultText.isNotEmpty() && errorMessage == null && resultText != "0") {
                            onCopyResult(resultText)
                            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                        }
                    }
                ),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = resultText,
                color = resultColor,
                fontSize = resultFontSize,
                fontWeight = if (lastEvaluated) FontWeight.Medium else FontWeight.Normal,
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.semantics {
                    this.contentDescription = "Result: $resultText"
                }
            )
        }
    }
}
