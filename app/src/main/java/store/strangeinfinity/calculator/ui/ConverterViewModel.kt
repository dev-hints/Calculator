package store.strangeinfinity.calculator.ui

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import store.strangeinfinity.calculator.domain.converter.ConversionUnit
import store.strangeinfinity.calculator.domain.converter.UnitCategory
import store.strangeinfinity.calculator.domain.converter.UnitConverter

data class ConverterUiState(
    val category: UnitCategory = UnitCategory.LENGTH,
    val fromUnit: ConversionUnit = UnitConverter.unitsMap[UnitCategory.LENGTH]!![0], // Meter
    val toUnit: ConversionUnit = UnitConverter.unitsMap[UnitCategory.LENGTH]!![6],   // Foot
    val fromValueText: String = "1",
    val toValueText: String = ""
)

class ConverterViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ConverterUiState())
    val uiState: StateFlow<ConverterUiState> = _uiState.asStateFlow()

    init {
        recalculate()
    }

    fun selectCategory(category: UnitCategory) {
        val units = UnitConverter.unitsMap[category] ?: return
        val from = units[0]
        val to = if (units.size > 1) units[1] else units[0]
        _uiState.value = _uiState.value.copy(
            category = category,
            fromUnit = from,
            toUnit = to
        )
        recalculate()
    }

    fun selectFromUnit(unit: ConversionUnit) {
        _uiState.value = _uiState.value.copy(fromUnit = unit)
        recalculate()
    }

    fun selectToUnit(unit: ConversionUnit) {
        _uiState.value = _uiState.value.copy(toUnit = unit)
        recalculate()
    }

    fun swapUnits() {
        val current = _uiState.value
        _uiState.value = current.copy(
            fromUnit = current.toUnit,
            toUnit = current.fromUnit
        )
        recalculate()
    }

    fun onDigit(digit: String) {
        val current = _uiState.value
        val newText = if (current.fromValueText == "0") digit else current.fromValueText + digit
        _uiState.value = current.copy(fromValueText = newText)
        recalculate()
    }

    fun onDecimal() {
        val current = _uiState.value
        if (!current.fromValueText.contains(".")) {
            val newText = if (current.fromValueText.isEmpty()) "0." else current.fromValueText + "."
            _uiState.value = current.copy(fromValueText = newText)
            recalculate()
        }
    }

    fun onBackspace() {
        val current = _uiState.value
        val newText = if (current.fromValueText.length <= 1) "0" else current.fromValueText.dropLast(1)
        _uiState.value = current.copy(fromValueText = newText)
        recalculate()
    }

    fun onClear() {
        _uiState.value = _uiState.value.copy(fromValueText = "0")
        recalculate()
    }

    fun onToggleSign() {
        val current = _uiState.value
        val text = current.fromValueText
        if (text == "0" || text.isEmpty()) return
        val newText = if (text.startsWith("-")) text.drop(1) else "-$text"
        _uiState.value = current.copy(fromValueText = newText)
        recalculate()
    }

    private fun recalculate() {
        val current = _uiState.value
        val rawNum = current.fromValueText.toDoubleOrNull() ?: 0.0
        val converted = UnitConverter.convert(
            category = current.category,
            fromUnit = current.fromUnit,
            toUnit = current.toUnit,
            value = rawNum
        )
        val formatted = UnitConverter.formatConvertedValue(converted)
        _uiState.value = _uiState.value.copy(toValueText = formatted)
    }

    fun copyConvertedValue() {
        val clipboard = getApplication<Application>().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Converted Unit", _uiState.value.toValueText)
        clipboard.setPrimaryClip(clip)
    }
}
