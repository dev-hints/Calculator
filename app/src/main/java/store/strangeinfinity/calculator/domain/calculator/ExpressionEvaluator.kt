package store.strangeinfinity.calculator.domain.calculator

import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.*

class ExpressionEvaluator {

    companion object {
        private val MC = MathContext(34, RoundingMode.HALF_UP)
        private const val PI_DOUBLE = Math.PI
        private const val E_DOUBLE = Math.E
        private val BD_PI = BigDecimal("3.1415926535897932384626433832795028841971")
        private val BD_E = BigDecimal("2.7182818284590452353602874713526624977572")
    }

    /**
     * Evaluates a mathematical expression string.
     * @param expression The expression to evaluate.
     * @param angleUnit DEGREE or RADIAN for trigonometric functions.
     * @param isFinalCalculation true if triggered by '=', false for real-time live preview.
     */
    fun evaluate(
        expression: String,
        angleUnit: AngleUnit = AngleUnit.DEGREE,
        isFinalCalculation: Boolean = false
    ): CalculationResult {
        if (expression.isBlank()) {
            return CalculationResult.Success("0", 0.0)
        }

        // Clean up expression
        var sanitized = expression
            .replace(",", "") // remove formatting commas
            .replace("×", "*")
            .replace("÷", "/")
            .replace("−", "-")
            .replace("–", "-")
            .replace("π", "PI")
            .replace("√", "sqrt")
            .replace("∛", "cbrt")
            .trim()

        if (sanitized.isEmpty()) {
            return CalculationResult.Success("0", 0.0)
        }

        // If it's a live preview and ends with an operator, strip trailing operators for evaluation
        if (!isFinalCalculation) {
            sanitized = stripTrailingOperators(sanitized)
            if (sanitized.isEmpty()) return CalculationResult.Success("0", 0.0)
        }

        // Balance trailing unclosed parentheses
        sanitized = balanceParentheses(sanitized)

        return try {
            val parser = Parser(sanitized, angleUnit)
            val result = parser.parseExpression()
            val formatted = NumberFormatter.formatResult(result)
            CalculationResult.Success(formatted, result.toDouble())
        } catch (e: ArithmeticException) {
            CalculationResult.Error(e.message ?: "Calculation error")
        } catch (e: EvaluationException) {
            CalculationResult.Error(e.message ?: "Invalid format")
        } catch (e: Exception) {
            CalculationResult.Error("Invalid format")
        }
    }

    private fun stripTrailingOperators(expr: String): String {
        var s = expr.trim()
        while (s.isNotEmpty()) {
            val last = s.last()
            if (last in "+-*/^(" || s.endsWith("sqrt") || s.endsWith("sin") || s.endsWith("cos") || s.endsWith("tan") || s.endsWith("ln") || s.endsWith("log")) {
                s = when {
                    s.endsWith("sqrt") -> s.dropLast(4).trim()
                    s.endsWith("cbrt") -> s.dropLast(4).trim()
                    s.endsWith("asin") -> s.dropLast(4).trim()
                    s.endsWith("acos") -> s.dropLast(4).trim()
                    s.endsWith("atan") -> s.dropLast(4).trim()
                    s.endsWith("sinh") -> s.dropLast(4).trim()
                    s.endsWith("cosh") -> s.dropLast(4).trim()
                    s.endsWith("tanh") -> s.dropLast(4).trim()
                    s.endsWith("sin") -> s.dropLast(3).trim()
                    s.endsWith("cos") -> s.dropLast(3).trim()
                    s.endsWith("tan") -> s.dropLast(3).trim()
                    s.endsWith("log") -> s.dropLast(3).trim()
                    s.endsWith("ln") -> s.dropLast(2).trim()
                    else -> s.dropLast(1).trim()
                }
            } else {
                break
            }
        }
        return s
    }

    private fun balanceParentheses(expr: String): String {
        var open = 0
        for (c in expr) {
            if (c == '(') open++
            else if (c == ')') open--
        }
        val sb = StringBuilder(expr)
        while (open > 0) {
            sb.append(')')
            open--
        }
        return sb.toString()
    }

    class EvaluationException(message: String) : RuntimeException(message)

    /**
     * Recursive Descent Parser for robust expression evaluation.
     */
    private class Parser(private val input: String, private val angleUnit: AngleUnit) {
        private var pos = 0
        private val length = input.length

        fun parseExpression(): BigDecimal {
            val result = parseAdditive()
            skipWhitespace()
            if (pos < length) {
                throw EvaluationException("Unexpected character at $pos: '${input[pos]}'")
            }
            return result
        }

        // Additive: + and -
        private fun parseAdditive(): BigDecimal {
            var left = parseMultiplicative(allowPercent = true)

            while (true) {
                skipWhitespace()
                if (pos >= length) break
                val op = input[pos]
                if (op != '+' && op != '-') break
                pos++ // consume op

                // Parse right with allowPercent = false so additive percentage rule applies
                var right = parseMultiplicative(allowPercent = false)

                skipWhitespace()
                if (pos < length && input[pos] == '%') {
                    pos++ // consume '%'
                    // Percentage rule: A + B% = A + (A * B / 100)
                    // A - B% = A - (A * B / 100)
                    val percentFactor = right.divide(BigDecimal("100"), MC)
                    val delta = left.multiply(percentFactor, MC)
                    left = if (op == '+') left.add(delta, MC) else left.subtract(delta, MC)
                } else {
                    left = if (op == '+') left.add(right, MC) else left.subtract(right, MC)
                }
            }
            return left
        }

        // Multiplicative: *, /, % (modulo / isolated percent)
        private fun parseMultiplicative(): BigDecimal {
            var left = parseUnary()

            while (true) {
                skipWhitespace()
                if (pos >= length) break
                val op = input[pos]
                if (op != '*' && op != '/' && op != '%') {
                    // Check for implicit multiplication: e.g. 2(3) or 2sin(30) or 2PI
                    if (canStartFactor(op)) {
                        val right = parseUnary()
                        left = left.multiply(right, MC)
                        continue
                    }
                    break
                }
                pos++ // consume op

                if (op == '%') {
                    // Postfix percent: left% -> left / 100
                    left = left.divide(BigDecimal("100"), MC)
                    continue
                }

                var right = parseUnary()

                if (peekPercent()) {
                    pos++ // consume '%'
                    // A * B% = A * (B / 100)
                    // A / B% = A / (B / 100)
                    val percentFactor = right.divide(BigDecimal("100"), MC)
                    left = if (op == '*') {
                        left.multiply(percentFactor, MC)
                    } else {
                        if (percentFactor.compareTo(BigDecimal.ZERO) == 0) {
                            throw ArithmeticException("Cannot divide by zero")
                        }
                        left.divide(percentFactor, MC)
                    }
                } else {
                    if (op == '*') {
                        left = left.multiply(right, MC)
                    } else {
                        if (right.compareTo(BigDecimal.ZERO) == 0) {
                            throw ArithmeticException("Cannot divide by zero")
                        }
                        left = left.divide(right, MC)
                    }
                }
            }
            return left
        }

        private fun peekPercent(): Boolean {
            skipWhitespace()
            return pos < length && input[pos] == '%'
        }

        // Unary: +, -, functions (sin, cos, etc.)
        private fun parseUnary(): BigDecimal {
            skipWhitespace()
            if (pos >= length) throw EvaluationException("Unexpected end of expression")

            if (input[pos] == '+') {
                pos++
                return parseUnary()
            }
            if (input[pos] == '-') {
                pos++
                return parseUnary().negate(MC)
            }

            return parsePower()
        }

        // Power: ^
        private fun parsePower(): BigDecimal {
            var base = parsePostfix()

            skipWhitespace()
            if (pos < length && input[pos] == '^') {
                pos++
                val exponent = parseUnary() // right-associative power
                base = powerBigDecimal(base, exponent)
            }
            return base
        }

        // Postfix operators: ! (factorial), % (percent)
        private fun parsePostfix(): BigDecimal {
            var value = parsePrimary()

            while (true) {
                skipWhitespace()
                if (pos >= length) break
                val c = input[pos]
                if (c == '!') {
                    pos++
                    value = computeFactorial(value)
                } else if (c == '%') {
                    pos++
                    value = value.divide(BigDecimal("100"), MC)
                } else {
                    break
                }
            }
            return value
        }

        // Primary: numbers, parentheses, functions, constants
        private fun parsePrimary(): BigDecimal {
            skipWhitespace()
            if (pos >= length) throw EvaluationException("Unexpected end of expression")

            val c = input[pos]

            // Parentheses
            if (c == '(') {
                pos++
                val value = parseAdditive()
                skipWhitespace()
                if (pos < length && input[pos] == ')') {
                    pos++
                } else {
                    // Tolerated if at end during live preview
                }
                return value
            }

            // Absolute value |x|
            if (c == '|') {
                pos++
                val value = parseAdditive()
                skipWhitespace()
                if (pos < length && input[pos] == '|') {
                    pos++
                }
                return value.abs(MC)
            }

            // Numbers
            if (c.isDigit() || c == '.') {
                return parseNumber()
            }

            // Word tokens: functions or constants
            if (c.isLetter()) {
                val ident = parseIdentifier()
                return handleIdentifier(ident)
            }

            throw EvaluationException("Unexpected character '${c}' at index $pos")
        }

        private fun parseNumber(): BigDecimal {
            val start = pos
            var hasDot = false
            while (pos < length) {
                val ch = input[pos]
                if (ch.isDigit()) {
                    pos++
                } else if (ch == '.') {
                    if (hasDot) throw EvaluationException("Multiple decimal points in number")
                    hasDot = true
                    pos++
                } else {
                    break
                }
            }
            val numStr = input.substring(start, pos)
            if (numStr == ".") return BigDecimal.ZERO
            return try {
                BigDecimal(numStr, MC)
            } catch (e: Exception) {
                throw EvaluationException("Invalid number: $numStr")
            }
        }

        private fun parseIdentifier(): String {
            val start = pos
            while (pos < length && (input[pos].isLetter() || input[pos].isDigit())) {
                pos++
            }
            return input.substring(start, pos)
        }

        private fun handleIdentifier(name: String): BigDecimal {
            return when (name.lowercase()) {
                "pi" -> BD_PI
                "e" -> BD_E
                "sqrt" -> {
                    val arg = parseUnary()
                    if (arg < BigDecimal.ZERO) throw ArithmeticException("Invalid input")
                    BigDecimal(sqrt(arg.toDouble()), MC)
                }
                "cbrt" -> {
                    val arg = parseUnary()
                    BigDecimal(cbrt(arg.toDouble()), MC)
                }
                "sin" -> {
                    val rad = toRadians(parseUnary().toDouble())
                    // Fix exact zero for sin(180) etc.
                    val s = sin(rad)
                    val rounded = if (abs(s) < 1e-15) 0.0 else s
                    BigDecimal.valueOf(rounded).round(MC)
                }
                "cos" -> {
                    val rad = toRadians(parseUnary().toDouble())
                    val c = cos(rad)
                    val rounded = if (abs(c) < 1e-15) 0.0 else c
                    BigDecimal.valueOf(rounded).round(MC)
                }
                "tan" -> {
                    val angle = parseUnary().toDouble()
                    if (angleUnit == AngleUnit.DEGREE) {
                        val normalized = ((angle % 180) + 180) % 180
                        if (abs(normalized - 90.0) < 1e-10) {
                            throw ArithmeticException("Invalid input")
                        }
                    }
                    val rad = toRadians(angle)
                    val t = tan(rad)
                    BigDecimal.valueOf(t).round(MC)
                }
                "asin" -> {
                    val arg = parseUnary().toDouble()
                    if (arg < -1.0 || arg > 1.0) throw ArithmeticException("Invalid input")
                    val rad = asin(arg)
                    val res = fromRadians(rad)
                    BigDecimal.valueOf(res).round(MC)
                }
                "acos" -> {
                    val arg = parseUnary().toDouble()
                    if (arg < -1.0 || arg > 1.0) throw ArithmeticException("Invalid input")
                    val rad = acos(arg)
                    val res = fromRadians(rad)
                    BigDecimal.valueOf(res).round(MC)
                }
                "atan" -> {
                    val arg = parseUnary().toDouble()
                    val rad = atan(arg)
                    val res = fromRadians(rad)
                    BigDecimal.valueOf(res).round(MC)
                }
                "sinh" -> {
                    val arg = parseUnary().toDouble()
                    BigDecimal.valueOf(sinh(arg)).round(MC)
                }
                "cosh" -> {
                    val arg = parseUnary().toDouble()
                    BigDecimal.valueOf(cosh(arg)).round(MC)
                }
                "tanh" -> {
                    val arg = parseUnary().toDouble()
                    BigDecimal.valueOf(tanh(arg)).round(MC)
                }
                "ln" -> {
                    val arg = parseUnary()
                    if (arg <= BigDecimal.ZERO) throw ArithmeticException("Invalid input")
                    BigDecimal.valueOf(ln(arg.toDouble())).round(MC)
                }
                "log", "log10" -> {
                    val arg = parseUnary()
                    if (arg <= BigDecimal.ZERO) throw ArithmeticException("Invalid input")
                    BigDecimal.valueOf(log10(arg.toDouble())).round(MC)
                }
                "exp" -> {
                    val arg = parseUnary().toDouble()
                    BigDecimal.valueOf(exp(arg)).round(MC)
                }
                "abs" -> {
                    parseUnary().abs(MC)
                }
                else -> throw EvaluationException("Unknown function or identifier: $name")
            }
        }

        private fun toRadians(angle: Double): Double {
            return if (angleUnit == AngleUnit.DEGREE) Math.toRadians(angle) else angle
        }

        private fun fromRadians(rad: Double): Double {
            return if (angleUnit == AngleUnit.DEGREE) Math.toDegrees(rad) else rad
        }

        private fun powerBigDecimal(base: BigDecimal, exponent: BigDecimal): BigDecimal {
            // Check integer power
            val scale = exponent.stripTrailingZeros().scale()
            if (scale <= 0) {
                val expInt = exponent.toBigInteger()
                if (expInt >= BigInteger.valueOf(Int.MIN_VALUE.toLong()) && expInt <= BigInteger.valueOf(Int.MAX_VALUE.toLong())) {
                    val p = expInt.toInt()
                    return if (p >= 0) {
                        base.pow(p, MC)
                    } else {
                        if (base.compareTo(BigDecimal.ZERO) == 0) throw ArithmeticException("Cannot divide by zero")
                        BigDecimal.ONE.divide(base.pow(-p, MC), MC)
                    }
                }
            }

            // Real power
            val b = base.toDouble()
            val e = exponent.toDouble()
            if (b < 0.0) throw ArithmeticException("Invalid input")
            val res = b.pow(e)
            if (res.isNaN() || res.isInfinite()) throw ArithmeticException("Calculation overflow")
            return BigDecimal.valueOf(res).round(MC)
        }

        private fun computeFactorial(n: BigDecimal): BigDecimal {
            if (n < BigDecimal.ZERO) throw ArithmeticException("Invalid input")
            val stripped = n.stripTrailingZeros()
            if (stripped.scale() > 0) throw ArithmeticException("Invalid input") // non-integer

            val intVal = stripped.toBigInteger()
            if (intVal > BigInteger.valueOf(1000)) {
                throw ArithmeticException("Calculation overflow")
            }

            var result = BigInteger.ONE
            var i = BigInteger.valueOf(2)
            while (i <= intVal) {
                result = result.multiply(i)
                i = i.add(BigInteger.ONE)
            }
            return BigDecimal(result)
        }

        private fun canStartFactor(c: Char): Boolean {
            return c.isLetter() || c == '(' || c == '|'
        }

        private fun skipWhitespace() {
            while (pos < length && input[pos].isWhitespace()) {
                pos++
            }
        }
    }
}
