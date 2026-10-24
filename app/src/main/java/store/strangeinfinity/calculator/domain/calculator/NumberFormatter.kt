package store.strangeinfinity.calculator.domain.calculator

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object NumberFormatter {

    private val symbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ','
        decimalSeparator = '.'
    }

    /**
     * Formats a BigDecimal for user-facing calculator display.
     */
    fun formatResult(value: BigDecimal): String {
        // Strip trailing zeros first
        val stripped = value.stripTrailingZeros()

        // Handle zero explicitly to avoid 0.0 or 0E+1
        if (stripped.compareTo(BigDecimal.ZERO) == 0) {
            return "0"
        }

        val absVal = stripped.abs()
        val scale = stripped.scale()

        // Use scientific notation for very large or very small non-zero numbers
        if (absVal >= BigDecimal("1e15") || (absVal < BigDecimal("1e-6") && absVal > BigDecimal.ZERO)) {
            val df = DecimalFormat("0.######E0", symbols)
            return df.format(stripped).replace("E", "×10^")
        }

        // Standard number formatting with thousands separators
        val df = DecimalFormat("#,##0.##########", symbols)
        return df.format(stripped)
    }

    /**
     * Formats a Double result safely.
     */
    fun formatDouble(value: Double): String {
        if (value.isNaN()) return "Invalid input"
        if (value.isInfinite()) {
            return if (value > 0) "Infinity" else "-Infinity"
        }

        // Avoid -0.0
        val sanitized = if (value == -0.0) 0.0 else value

        // Convert to BigDecimal with high precision
        val bd = try {
            BigDecimal.valueOf(sanitized).round(MathContext.DECIMAL64)
        } catch (e: Exception) {
            BigDecimal(sanitized.toString())
        }

        return formatResult(bd)
    }

    /**
     * Formats numbers inside an expression with commas for easier reading
     * while preserving operators, parentheses, and decimals.
     */
    fun formatExpression(expression: String): String {
        if (expression.isEmpty()) return ""
        val sb = StringBuilder()
        var i = 0
        while (i < expression.length) {
            val c = expression[i]
            if (c.isDigit() || c == '.') {
                val numStart = i
                while (i < expression.length && (expression[i].isDigit() || expression[i] == '.')) {
                    i++
                }
                val rawNum = expression.substring(numStart, i)
                sb.append(formatNumberInExpression(rawNum))
            } else {
                sb.append(c)
                i++
            }
        }
        return sb.toString()
    }

    private fun formatNumberInExpression(numStr: String): String {
        if (numStr.isEmpty()) return ""
        val parts = numStr.split(".")
        val intPart = parts[0]

        val formattedInt = if (intPart.isNotEmpty()) {
            try {
                val parsed = java.math.BigInteger(intPart)
                val df = DecimalFormat("#,###", symbols)
                df.format(parsed)
            } catch (e: Exception) {
                intPart
            }
        } else {
            ""
        }

        return if (parts.size > 1) {
            "$formattedInt.${parts[1]}"
        } else if (numStr.endsWith(".")) {
            "$formattedInt."
        } else {
            formattedInt
        }
    }
}
