package store.strangeinfinity.calculator.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import store.strangeinfinity.calculator.ui.components.AboutDialog
import store.strangeinfinity.calculator.ui.components.CalculatorDisplay
import store.strangeinfinity.calculator.ui.components.HistorySheet
import store.strangeinfinity.calculator.ui.components.KeypadLandscape
import store.strangeinfinity.calculator.ui.components.KeypadScientificPortrait
import store.strangeinfinity.calculator.ui.components.KeypadStandard
import store.strangeinfinity.calculator.ui.components.ToolbarHandle
import store.strangeinfinity.calculator.ui.components.UnitConverterScreen
import store.strangeinfinity.calculator.ui.theme.LocalCalculatorPalette

@Composable
fun CalculatorScreen(
    calculatorViewModel: CalculatorViewModel = viewModel(),
    converterViewModel: ConverterViewModel = viewModel()
) {
    val palette = LocalCalculatorPalette.current
    val uiState by calculatorViewModel.uiState.collectAsState()
    val historyList by calculatorViewModel.historyList.collectAsState()
    val themeMode by calculatorViewModel.themeMode.collectAsState()
    val hapticEnabled by calculatorViewModel.hapticEnabled.collectAsState()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        if (uiState.isConverterOpen) {
            UnitConverterScreen(
                viewModel = converterViewModel,
                hapticEnabled = hapticEnabled,
                onBackClick = { calculatorViewModel.setConverterOpen(false) }
            )
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Display Area
                CalculatorDisplay(
                    expression = uiState.expression,
                    previewResult = uiState.previewResult,
                    errorMessage = uiState.errorMessage,
                    lastEvaluated = uiState.lastEvaluated,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(if (isLandscape) 0.6f else 1f)
                        .defaultMinSize(minHeight = 120.dp),
                    onCopyResult = { calculatorViewModel.copyToClipboard(it) }
                )

                // Middle Toolbar
                ToolbarHandle(
                    isScientificActive = uiState.isScientificExpanded || isLandscape,
                    angleUnit = uiState.angleUnit,
                    currentTheme = themeMode,
                    hapticEnabled = hapticEnabled,
                    onHistoryClick = { calculatorViewModel.setHistoryOpen(true) },
                    onConverterClick = { calculatorViewModel.setConverterOpen(true) },
                    onScientificToggle = { calculatorViewModel.toggleScientific() },
                    onAngleUnitToggle = { calculatorViewModel.toggleAngleUnit() },
                    onBackspaceClick = { calculatorViewModel.onBackspace() },
                    onThemeSelect = { calculatorViewModel.setThemeMode(it) },
                    onHapticToggle = { calculatorViewModel.setHapticEnabled(it) },
                    onAboutClick = { calculatorViewModel.setAboutOpen(true) }
                )

                // Keypad Area
                if (isLandscape) {
                    KeypadLandscape(
                        hapticEnabled = hapticEnabled,
                        angleUnit = uiState.angleUnit,
                        onAngleToggle = { calculatorViewModel.toggleAngleUnit() },
                        onDigit = { calculatorViewModel.onDigit(it) },
                        onOperator = { calculatorViewModel.onOperator(it) },
                        onScientificFunction = { calculatorViewModel.onScientificFunction(it) },
                        onClear = { calculatorViewModel.onClear() },
                        onParentheses = { calculatorViewModel.onParentheses() },
                        onPercent = { calculatorViewModel.onPercentage() },
                        onDecimal = { calculatorViewModel.onDecimal() },
                        onPlusMinus = { calculatorViewModel.onPlusMinus() },
                        onEquals = { calculatorViewModel.onEquals() }
                    )
                } else {
                    // Scientific keypad expansion in portrait
                    AnimatedVisibility(
                        visible = uiState.isScientificExpanded,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        KeypadScientificPortrait(
                            hapticEnabled = hapticEnabled,
                            onScientificFunction = { calculatorViewModel.onScientificFunction(it) }
                        )
                    }

                    // Standard Keypad
                    KeypadStandard(
                        hapticEnabled = hapticEnabled,
                        onDigit = { calculatorViewModel.onDigit(it) },
                        onOperator = { calculatorViewModel.onOperator(it) },
                        onClear = { calculatorViewModel.onClear() },
                        onParentheses = { calculatorViewModel.onParentheses() },
                        onPercent = { calculatorViewModel.onPercentage() },
                        onDecimal = { calculatorViewModel.onDecimal() },
                        onPlusMinus = { calculatorViewModel.onPlusMinus() },
                        onEquals = { calculatorViewModel.onEquals() }
                    )
                }
            }
        }

        // History Modal Sheet
        if (uiState.isHistoryOpen) {
            HistorySheet(
                historyList = historyList,
                showClearDialog = uiState.showClearHistoryDialog,
                onDismissRequest = { calculatorViewModel.setHistoryOpen(false) },
                onItemClick = { calculatorViewModel.recallHistoryItem(it) },
                onDeleteItem = { calculatorViewModel.deleteHistoryItem(it) },
                onClearHistoryRequest = { calculatorViewModel.setShowClearHistoryDialog(true) },
                onConfirmClearHistory = { calculatorViewModel.clearHistory() },
                onDismissClearDialog = { calculatorViewModel.setShowClearHistoryDialog(false) }
            )
        }

        // About Dialog
        if (uiState.isAboutOpen) {
            AboutDialog(
                onDismissRequest = { calculatorViewModel.setAboutOpen(false) }
            )
        }
    }
}
