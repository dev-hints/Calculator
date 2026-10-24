package store.strangeinfinity.calculator.domain.calculator

enum class AngleUnit {
    DEGREE,
    RADIAN
}

sealed class CalculationResult {
    data class Success(val value: String, val rawValue: Double? = null) : CalculationResult()
    data class Error(val message: String) : CalculationResult()
}
