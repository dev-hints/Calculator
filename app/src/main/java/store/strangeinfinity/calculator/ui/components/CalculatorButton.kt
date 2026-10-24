package store.strangeinfinity.calculator.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import store.strangeinfinity.calculator.ui.theme.LocalCalculatorPalette

enum class ButtonType {
    NUMBER,
    FUNCTION,
    OPERATOR,
    CLEAR,
    EQUALS
}

@Composable
fun CalculatorButton(
    text: String,
    contentDesc: String,
    type: ButtonType,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 26.sp,
    hapticEnabled: Boolean = true,
    aspectRatio: Float = 1.15f,
    customBgColor: Color? = null,
    customTextColor: Color? = null,
    onClick: () -> Unit
) {
    val palette = LocalCalculatorPalette.current
    val view = LocalView.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val (bgColor, pressedColor, textColor) = when (type) {
        ButtonType.NUMBER -> Triple(
            customBgColor ?: palette.numberKey,
            palette.numberKeyPressed,
            customTextColor ?: palette.numberKeyText
        )
        ButtonType.FUNCTION -> Triple(
            customBgColor ?: palette.functionKey,
            palette.functionKeyPressed,
            customTextColor ?: palette.functionKeyText
        )
        ButtonType.OPERATOR, ButtonType.EQUALS -> Triple(
            customBgColor ?: palette.operatorKey,
            palette.operatorKey.copy(alpha = 0.85f),
            customTextColor ?: palette.operatorKeyText
        )
        ButtonType.CLEAR -> Triple(
            customBgColor ?: palette.functionKey,
            palette.functionKeyPressed,
            customTextColor ?: palette.clearKeyText
        )
    }

    val currentBg = if (isPressed) pressedColor else bgColor

    Box(
        modifier = modifier
            .padding(3.dp)
            .aspectRatio(aspectRatio)
            .clip(CircleShape)
            .background(currentBg)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    if (hapticEnabled) {
                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                    }
                    onClick()
                }
            )
            .semantics {
                this.contentDescription = contentDesc
                this.role = Role.Button
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = fontSize,
            fontWeight = if (type == ButtonType.NUMBER) FontWeight.Normal else FontWeight.Medium
        )
    }
}
