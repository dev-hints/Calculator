package store.strangeinfinity.calculator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import store.strangeinfinity.calculator.data.settings.ThemeMode

@Immutable
data class CalculatorPalette(
    val background: Color,
    val surface: Color,
    val numberKey: Color,
    val numberKeyPressed: Color,
    val numberKeyText: Color,
    val functionKey: Color,
    val functionKeyPressed: Color,
    val functionKeyText: Color,
    val operatorKey: Color,
    val operatorKeyText: Color,
    val clearKeyText: Color,
    val expressionText: Color,
    val resultText: Color,
    val divider: Color,
    val iconTint: Color,
    val isDark: Boolean
)

val LocalCalculatorPalette = staticCompositionLocalOf {
    CalculatorPalette(
        background = DarkBackground,
        surface = DarkSurface,
        numberKey = DarkNumberKey,
        numberKeyPressed = DarkNumberKeyPressed,
        numberKeyText = DarkNumberKeyText,
        functionKey = DarkFunctionKey,
        functionKeyPressed = DarkFunctionKeyPressed,
        functionKeyText = DarkFunctionKeyText,
        operatorKey = DarkOperatorKey,
        operatorKeyText = DarkOperatorKeyText,
        clearKeyText = SamsungClearRed,
        expressionText = DarkExpressionText,
        resultText = DarkResultText,
        divider = DarkDivider,
        iconTint = DarkIconTint,
        isDark = true
    )
}

val DarkPalette = CalculatorPalette(
    background = DarkBackground,
    surface = DarkSurface,
    numberKey = DarkNumberKey,
    numberKeyPressed = DarkNumberKeyPressed,
    numberKeyText = DarkNumberKeyText,
    functionKey = DarkFunctionKey,
    functionKeyPressed = DarkFunctionKeyPressed,
    functionKeyText = DarkFunctionKeyText,
    operatorKey = DarkOperatorKey,
    operatorKeyText = DarkOperatorKeyText,
    clearKeyText = SamsungClearRed,
    expressionText = DarkExpressionText,
    resultText = DarkResultText,
    divider = DarkDivider,
    iconTint = DarkIconTint,
    isDark = true
)

val LightPalette = CalculatorPalette(
    background = LightBackground,
    surface = LightSurface,
    numberKey = LightNumberKey,
    numberKeyPressed = LightNumberKeyPressed,
    numberKeyText = LightNumberKeyText,
    functionKey = LightFunctionKey,
    functionKeyPressed = LightFunctionKeyPressed,
    functionKeyText = LightFunctionKeyText,
    operatorKey = LightOperatorKey,
    operatorKeyText = LightOperatorKeyText,
    clearKeyText = SamsungClearRed,
    expressionText = LightExpressionText,
    resultText = LightResultText,
    divider = LightDivider,
    iconTint = LightIconTint,
    isDark = false
)

private val M3DarkColorScheme = darkColorScheme(
    primary = SamsungGreen,
    surface = DarkSurface,
    background = DarkBackground
)

private val M3LightColorScheme = lightColorScheme(
    primary = SamsungGreen,
    surface = LightSurface,
    background = LightBackground
)

@Composable
fun CalculatorTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val palette = if (darkTheme) DarkPalette else LightPalette
    val colorScheme = if (darkTheme) M3DarkColorScheme else M3LightColorScheme

    CompositionLocalProvider(LocalCalculatorPalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
