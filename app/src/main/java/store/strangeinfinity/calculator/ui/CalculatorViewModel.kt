package store.strangeinfinity.calculator.ui

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import store.strangeinfinity.calculator.data.history.CalculatorDatabase
import store.strangeinfinity.calculator.data.history.CalculationEntity
import store.strangeinfinity.calculator.data.history.HistoryRepository
import store.strangeinfinity.calculator.data.settings.SettingsManager
import store.strangeinfinity.calculator.data.settings.ThemeMode
import store.strangeinfinity.calculator.domain.calculator.AngleUnit
import store.strangeinfinity.calculator.domain.calculator.CalculationResult
import store.strangeinfinity.calculator.domain.calculator.ExpressionEvaluator

data class CalculatorUiState(
    val expression: String = "",
    val previewResult: String = "",
    val errorMessage: String? = null,
    val lastEvaluated: Boolean = false,
    val isScientificExpanded: Boolean = false,
    val angleUnit: AngleUnit = AngleUnit.DEGREE,
    val isHistoryOpen: Boolean = false,
    val isConverterOpen: Boolean = false,
    val isAboutOpen: Boolean = false,
    val showClearHistoryDialog: Boolean = false
)

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {

    private val database = CalculatorDatabase.getDatabase(application)
    private val historyRepository = HistoryRepository(database.calculationDao())
    val settingsManager = SettingsManager(application)
    private val evaluator = ExpressionEvaluator()

    private val _uiState = MutableStateFlow(
        CalculatorUiState(
            angleUnit = settingsManager.angleUnit.value
        )
    )
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    val historyList: StateFlow<List<CalculationEntity>> = historyRepository.allCalculations.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val themeMode: StateFlow<ThemeMode> = settingsManager.themeMode
    val hapticEnabled: StateFlow<Boolean> = settingsManager.hapticEnabled

    fun onDigit(digit: String) {
        val current = _uiState.value
        val newExpr = if (current.lastEvaluated) {
            digit
        } else {
            current.expression + digit
        }
        updateExpressionAndPreview(newExpr, lastEvaluated = false)
    }

    fun onDecimal() {
        val current = _uiState.value
        if (current.lastEvaluated) {
            updateExpressionAndPreview("0.", lastEvaluated = false)
            return
        }

        val expr = current.expression
        if (expr.isEmpty() || endsWithOperator(expr) || expr.endsWith("(")) {
            updateExpressionAndPreview(expr + "0.", lastEvaluated = false)
            return
        }

        // Find current number token
        val lastNumber = extractLastNumber(expr)
        if (!lastNumber.contains(".")) {
            updateExpressionAndPreview(expr + ".", lastEvaluated = false)
        }
    }

    fun onOperator(op: String) {
        val current = _uiState.value
        var expr = current.expression

        if (current.lastEvaluated && current.previewResult.isNotEmpty()) {
            expr = current.previewResult
        }

        if (expr.isEmpty()) {
            if (op == "−" || op == "-") {
                updateExpressionAndPreview("−", lastEvaluated = false)
            }
            return
        }

        // If ends with an operator, replace it
        val trimmed = expr.trimEnd()
        if (endsWithOperator(trimmed)) {
            val replaced = replaceLastOperator(trimmed, op)
            updateExpressionAndPreview(replaced, lastEvaluated = false)
        } else {
            updateExpressionAndPreview("$trimmed $op ", lastEvaluated = false)
        }
    }

    fun onPercentage() {
        val current = _uiState.value
        val expr = current.expression
        if (expr.isNotEmpty() && !endsWithOperator(expr.trimEnd()) && !expr.endsWith("(")) {
            updateExpressionAndPreview(expr + "%", lastEvaluated = false)
        }
    }

    fun onParentheses() {
        val current = _uiState.value
        val expr = current.expression.trimEnd()

        if (current.lastEvaluated) {
            updateExpressionAndPreview("(", lastEvaluated = false)
            return
        }

        if (expr.isEmpty() || endsWithOperator(expr) || expr.endsWith("(")) {
            updateExpressionAndPreview(expr + "(", lastEvaluated = false)
            return
        }

        // Check open vs closed parentheses count
        val openCount = expr.count { it == '(' }
        val closeCount = expr.count { it == ')' }

        if (openCount > closeCount) {
            updateExpressionAndPreview(expr + ")", lastEvaluated = false)
        } else {
            updateExpressionAndPreview(expr + " × (", lastEvaluated = false)
        }
    }

    fun onPlusMinus() {
        val current = _uiState.value
        val expr = current.expression
        if (expr.isEmpty()) {
            updateExpressionAndPreview("−", lastEvaluated = false)
            return
        }

        // Toggle sign on last number
        val lastNum = extractLastNumber(expr)
        if (lastNum.isNotEmpty()) {
            val prefix = expr.dropLast(lastNum.length)
            val toggled = if (prefix.endsWith("−") || prefix.endsWith("-")) {
                prefix.dropLast(1) + lastNum
            } else if (prefix.endsWith("(−")) {
                prefix.dropLast(2) + lastNum
            } else {
                prefix + "(−" + lastNum + ")"
            }
            updateExpressionAndPreview(toggled, lastEvaluated = false)
        }
    }

    fun onScientificFunction(fn: String) {
        val current = _uiState.value
        var expr = if (current.lastEvaluated) "" else current.expression

        val newExpr = when (fn) {
            "x²" -> if (expr.isNotEmpty() && !endsWithOperator(expr.trimEnd())) "$expr^2" else expr
            "x³" -> if (expr.isNotEmpty() && !endsWithOperator(expr.trimEnd())) "$expr^3" else expr
            "xʸ" -> if (expr.isNotEmpty() && !endsWithOperator(expr.trimEnd())) "$expr^" else expr
            "√" -> if (expr.isNotEmpty() && !endsWithOperator(expr.trimEnd())) "$expr × √( " else "${expr}√( "
            "∛" -> if (expr.isNotEmpty() && !endsWithOperator(expr.trimEnd())) "$expr × ∛( " else "${expr}∛( "
            "1/x" -> if (expr.isNotEmpty() && !endsWithOperator(expr.trimEnd())) "1 ÷ ($expr)" else "1 ÷ ("
            "x!" -> if (expr.isNotEmpty() && !endsWithOperator(expr.trimEnd())) "$expr!" else expr
            "π" -> if (expr.isNotEmpty() && (expr.last().isDigit() || expr.last() == ')')) "$expr × π" else "${expr}π"
            "e" -> if (expr.isNotEmpty() && (expr.last().isDigit() || expr.last() == ')')) "$expr × e" else "${expr}e"
            "|x|" -> if (expr.isNotEmpty() && !endsWithOperator(expr.trimEnd())) "|($expr)|" else "${expr}|"
            "10ˣ" -> if (expr.isNotEmpty() && !endsWithOperator(expr.trimEnd())) "$expr × 10^" else "${expr}10^"
            "eˣ" -> if (expr.isNotEmpty() && !endsWithOperator(expr.trimEnd())) "$expr × e^" else "${expr}e^"
            else -> {
                // Trigonometric & logs: sin, cos, tan, ln, log, asin, acos, atan
                if (expr.isNotEmpty() && (expr.last().isDigit() || expr.last() == ')')) {
                    "$expr × $fn( "
                } else {
                    "$expr$fn( "
                }
            }
        }
        updateExpressionAndPreview(newExpr, lastEvaluated = false)
    }

    fun onBackspace() {
        val current = _uiState.value
        if (current.lastEvaluated) {
            onClear()
            return
        }

        var expr = current.expression.trimEnd()
        if (expr.isEmpty()) return

        // Smart delete multi-char tokens: e.g. "sin( ", "cos( ", "sqrt( "
        val tokens = listOf("asin( ", "acos( ", "atan( ", "sinh( ", "cosh( ", "tanh( ", "sin( ", "cos( ", "tan( ", "log( ", "ln( ", "√( ", "∛( ", " × ", " ÷ ", " + ", " − ")
        var matched = false
        for (token in tokens) {
            if (expr.endsWith(token)) {
                expr = expr.dropLast(token.length)
                matched = true
                break
            }
        }

        if (!matched) {
            expr = expr.dropLast(1)
        }

        updateExpressionAndPreview(expr, lastEvaluated = false)
    }

    fun onClear() {
        _uiState.value = _uiState.value.copy(
            expression = "",
            previewResult = "",
            errorMessage = null,
            lastEvaluated = false
        )
    }

    fun onEquals() {
        val current = _uiState.value
        val expr = current.expression.trim()
        if (expr.isEmpty()) return

        when (val res = evaluator.evaluate(expr, current.angleUnit, isFinalCalculation = true)) {
            is CalculationResult.Success -> {
                // Save to Room database history
                viewModelScope.launch {
                    historyRepository.saveCalculation(expr, res.value)
                }
                _uiState.value = current.copy(
                    expression = expr,
                    previewResult = res.value,
                    errorMessage = null,
                    lastEvaluated = true
                )
            }
            is CalculationResult.Error -> {
                _uiState.value = current.copy(
                    errorMessage = res.message,
                    lastEvaluated = false
                )
            }
        }
    }

    fun toggleScientific() {
        _uiState.value = _uiState.value.copy(
            isScientificExpanded = !_uiState.value.isScientificExpanded
        )
    }

    fun toggleAngleUnit() {
        val nextUnit = if (_uiState.value.angleUnit == AngleUnit.DEGREE) AngleUnit.RADIAN else AngleUnit.DEGREE
        settingsManager.setAngleUnit(nextUnit)
        _uiState.value = _uiState.value.copy(angleUnit = nextUnit)
        // Refresh preview with new angle unit
        updateExpressionAndPreview(_uiState.value.expression, _uiState.value.lastEvaluated)
    }

    fun setHistoryOpen(open: Boolean) {
        _uiState.value = _uiState.value.copy(isHistoryOpen = open)
    }

    fun setConverterOpen(open: Boolean) {
        _uiState.value = _uiState.value.copy(isConverterOpen = open)
    }

    fun setAboutOpen(open: Boolean) {
        _uiState.value = _uiState.value.copy(isAboutOpen = open)
    }

    fun setShowClearHistoryDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showClearHistoryDialog = show)
    }

    fun clearHistory() {
        viewModelScope.launch {
            historyRepository.clearHistory()
            _uiState.value = _uiState.value.copy(showClearHistoryDialog = false)
        }
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch {
            historyRepository.delete(id)
        }
    }

    fun recallHistoryItem(item: CalculationEntity) {
        _uiState.value = _uiState.value.copy(
            expression = item.expression,
            previewResult = item.result,
            lastEvaluated = true,
            isHistoryOpen = false,
            errorMessage = null
        )
    }

    fun copyToClipboard(text: String) {
        val clipboard = getApplication<Application>().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Calculator Result", text)
        clipboard.setPrimaryClip(clip)
    }

    fun pasteFromClipboard() {
        val clipboard = getApplication<Application>().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = clipboard.primaryClip
        if (clip != null && clip.itemCount > 0) {
            val text = clip.getItemAt(0).text?.toString() ?: ""
            // Sanitize pasted text: only numbers, operators, decimals
            val sanitized = text.replace(Regex("[^0-9.+\\-*/×÷(),^%eπ ]"), "").trim()
            if (sanitized.isNotEmpty()) {
                val current = _uiState.value
                val newExpr = if (current.lastEvaluated) sanitized else current.expression + sanitized
                updateExpressionAndPreview(newExpr, lastEvaluated = false)
            }
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        settingsManager.setThemeMode(mode)
    }

    fun setHapticEnabled(enabled: Boolean) {
        settingsManager.setHapticEnabled(enabled)
    }

    private fun updateExpressionAndPreview(newExpr: String, lastEvaluated: Boolean) {
        if (newExpr.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                expression = "",
                previewResult = "",
                errorMessage = null,
                lastEvaluated = false
            )
            return
        }

        val angleUnit = _uiState.value.angleUnit
        val preview = when (val res = evaluator.evaluate(newExpr, angleUnit, isFinalCalculation = false)) {
            is CalculationResult.Success -> res.value
            is CalculationResult.Error -> ""
        }

        _uiState.value = _uiState.value.copy(
            expression = newExpr,
            previewResult = preview,
            errorMessage = null,
            lastEvaluated = lastEvaluated
        )
    }

    private fun endsWithOperator(expr: String): Boolean {
        val trimmed = expr.trimEnd()
        return trimmed.endsWith("+") || trimmed.endsWith("−") || trimmed.endsWith("-") ||
                trimmed.endsWith("×") || trimmed.endsWith("*") || trimmed.endsWith("÷") ||
                trimmed.endsWith("/") || trimmed.endsWith("^")
    }

    private fun replaceLastOperator(expr: String, newOp: String): String {
        var s = expr.trimEnd()
        while (endsWithOperator(s)) {
            s = if (s.endsWith(" +") || s.endsWith(" −") || s.endsWith(" ×") || s.endsWith(" ÷")) {
                s.dropLast(2).trimEnd()
            } else {
                s.dropLast(1).trimEnd()
            }
        }
        return "$s $newOp "
    }

    private fun extractLastNumber(expr: String): String {
        var i = expr.length - 1
        while (i >= 0 && (expr[i].isDigit() || expr[i] == '.')) {
            i--
        }
        return expr.substring(i + 1)
    }
}
