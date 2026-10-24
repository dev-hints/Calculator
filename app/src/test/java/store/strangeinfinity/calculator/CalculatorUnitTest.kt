package store.strangeinfinity.calculator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import store.strangeinfinity.calculator.domain.calculator.AngleUnit
import store.strangeinfinity.calculator.domain.calculator.CalculationResult
import store.strangeinfinity.calculator.domain.calculator.ExpressionEvaluator
import store.strangeinfinity.calculator.domain.calculator.NumberFormatter

class CalculatorUnitTest {

    private val evaluator = ExpressionEvaluator()

    private fun eval(expr: String, angleUnit: AngleUnit = AngleUnit.DEGREE): String {
        return when (val res = evaluator.evaluate(expr, angleUnit, isFinalCalculation = true)) {
            is CalculationResult.Success -> res.value
            is CalculationResult.Error -> res.message
        }
    }

    @Test
    fun testBasicArithmetic() {
        assertEquals("5", eval("2 + 3"))
        assertEquals("6", eval("10 - 4"))
        assertEquals("42", eval("6 × 7"))
        assertEquals("5", eval("15 ÷ 3"))
    }

    @Test
    fun testOperatorPrecedence() {
        assertEquals("14", eval("2 + 3 × 4"))
        assertEquals("20", eval("(2 + 3) × 4"))
        assertEquals("11", eval("2 × 4 + 3"))
        assertEquals("14", eval("2 + 4 × 3"))
        assertEquals("10", eval("20 - 5 × 2"))
    }

    @Test
    fun testFloatingPointPrecision() {
        // Must NEVER display 0.30000000000000004
        val result = eval("0.1 + 0.2")
        assertEquals("0.3", result)

        val result2 = eval("1 - 0.9")
        assertEquals("0.1", result2)

        val result3 = eval("0.7 + 0.1")
        assertEquals("0.8", result3)
    }

    @Test
    fun testNegativeNumbersAndUnary() {
        assertEquals("-3", eval("-5 + 2"))
        assertEquals("-10", eval("5 × -2"))
        assertEquals("7", eval("5 - -2"))
        assertEquals("-7", eval("-5 - 2"))
    }

    @Test
    fun testPercentageRules() {
        // Standard calculator rules
        assertEquals("0.25", eval("25%"))
        assertEquals("125", eval("100 + 25%"))
        assertEquals("180", eval("200 - 10%"))
        assertEquals("10", eval("50 × 20%"))
        assertEquals("200", eval("50 ÷ 25%"))
    }

    @Test
    fun testDivisionByZero() {
        val result = eval("10 ÷ 0")
        assertEquals("Cannot divide by zero", result)
    }

    @Test
    fun testParentheses() {
        assertEquals("100", eval("((2 + 3) × 4) × 5"))
        // Auto-balanced during evaluation
        val autoClose = evaluator.evaluate("(2 + 3) × (4 + 1", isFinalCalculation = false)
        assertTrue(autoClose is CalculationResult.Success)
        assertEquals("25", (autoClose as CalculationResult.Success).value)
    }

    @Test
    fun testScientificPowersAndRoots() {
        assertEquals("4", eval("2^2"))
        assertEquals("1,024", eval("2^10"))
        assertEquals("9", eval("sqrt(81)"))
        assertEquals("3", eval("cbrt(27)"))
        assertEquals("Invalid input", eval("sqrt(-16)"))
    }

    @Test
    fun testTrigonometricFunctions() {
        // DEGREE mode
        assertEquals("0.5", eval("sin(30)", AngleUnit.DEGREE))
        assertEquals("0.5", eval("cos(60)", AngleUnit.DEGREE))
        assertEquals("1", eval("tan(45)", AngleUnit.DEGREE))
        assertEquals("Invalid input", eval("tan(90)", AngleUnit.DEGREE))

        // RADIAN mode
        assertEquals("0.5", eval("sin(PI / 6)", AngleUnit.RADIAN))
        assertEquals("0.5", eval("cos(PI / 3)", AngleUnit.RADIAN))
    }

    @Test
    fun testLogarithmsAndExponents() {
        assertEquals("2", eval("log(100)"))
        assertEquals("3", eval("log10(1000)"))
        assertEquals("1", eval("ln(e)"))
        assertEquals("Invalid input", eval("ln(0)"))
        assertEquals("Invalid input", eval("log(-5)"))
    }

    @Test
    fun testFactorial() {
        assertEquals("1", eval("0!"))
        assertEquals("1", eval("1!"))
        assertEquals("120", eval("5!"))
        assertEquals("720", eval("6!"))
        assertEquals("3,628,800", eval("10!"))
    }

    @Test
    fun testChainedCalculationsAndImplicitMultiplication() {
        assertEquals("12", eval("3(4)"))
        assertEquals("15", eval("(3 + 2)(3)"))
    }
}
