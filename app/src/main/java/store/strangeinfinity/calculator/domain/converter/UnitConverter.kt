package store.strangeinfinity.calculator.domain.converter

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

enum class UnitCategory(val displayName: String) {
    LENGTH("Length"),
    AREA("Area"),
    VOLUME("Volume"),
    MASS("Mass"),
    TEMPERATURE("Temperature"),
    SPEED("Speed"),
    TIME("Time"),
    DATA("Data");
}

data class ConversionUnit(
    val id: String,
    val name: String,
    val symbol: String,
    val factorToBase: Double = 1.0 // For linear conversions: valueInBase = value * factorToBase
)

object UnitConverter {
    private val MC = MathContext(16, RoundingMode.HALF_UP)

    val unitsMap: Map<UnitCategory, List<ConversionUnit>> = mapOf(
        UnitCategory.LENGTH to listOf(
            ConversionUnit("m", "Meter", "m", 1.0),
            ConversionUnit("km", "Kilometer", "km", 1000.0),
            ConversionUnit("cm", "Centimeter", "cm", 0.01),
            ConversionUnit("mm", "Millimeter", "mm", 0.001),
            ConversionUnit("mi", "Mile", "mi", 1609.344),
            ConversionUnit("yd", "Yard", "yd", 0.9144),
            ConversionUnit("ft", "Foot", "ft", 0.3048),
            ConversionUnit("in", "Inch", "in", 0.0254),
            ConversionUnit("nmi", "Nautical Mile", "NM", 1852.0)
        ),
        UnitCategory.AREA to listOf(
            ConversionUnit("m2", "Square Meter", "m²", 1.0),
            ConversionUnit("km2", "Square Kilometer", "km²", 1_000_000.0),
            ConversionUnit("ha", "Hectare", "ha", 10_000.0),
            ConversionUnit("ac", "Acre", "ac", 4046.8564224),
            ConversionUnit("sqmi", "Square Mile", "mi²", 2_589_988.110336),
            ConversionUnit("sqyd", "Square Yard", "yd²", 0.83612736),
            ConversionUnit("sqft", "Square Foot", "ft²", 0.09290304),
            ConversionUnit("sqin", "Square Inch", "in²", 0.00064516)
        ),
        UnitCategory.VOLUME to listOf(
            ConversionUnit("l", "Liter", "L", 1.0),
            ConversionUnit("ml", "Milliliter", "mL", 0.001),
            ConversionUnit("m3", "Cubic Meter", "m³", 1000.0),
            ConversionUnit("gal", "Gallon (US)", "gal", 3.785411784),
            ConversionUnit("qt", "Quart (US)", "qt", 0.946352946),
            ConversionUnit("pt", "Pint (US)", "pt", 0.473176473),
            ConversionUnit("cup", "Cup (US)", "cup", 0.2365882365),
            ConversionUnit("floz", "Fluid Ounce (US)", "fl oz", 0.0295735295625)
        ),
        UnitCategory.MASS to listOf(
            ConversionUnit("kg", "Kilogram", "kg", 1.0),
            ConversionUnit("g", "Gram", "g", 0.001),
            ConversionUnit("mg", "Milligram", "mg", 1e-6),
            ConversionUnit("t", "Metric Ton", "t", 1000.0),
            ConversionUnit("lb", "Pound", "lb", 0.45359237),
            ConversionUnit("oz", "Ounce", "oz", 0.028349523125),
            ConversionUnit("ct", "Carat", "ct", 0.0002)
        ),
        UnitCategory.TEMPERATURE to listOf(
            ConversionUnit("c", "Celsius", "°C"),
            ConversionUnit("f", "Fahrenheit", "°F"),
            ConversionUnit("k", "Kelvin", "K")
        ),
        UnitCategory.SPEED to listOf(
            ConversionUnit("mps", "Meter/second", "m/s", 1.0),
            ConversionUnit("kmh", "Kilometer/hour", "km/h", 1.0 / 3.6),
            ConversionUnit("mph", "Mile/hour", "mph", 0.44704),
            ConversionUnit("kn", "Knot", "kn", 0.514444444),
            ConversionUnit("fps", "Foot/second", "ft/s", 0.3048)
        ),
        UnitCategory.TIME to listOf(
            ConversionUnit("s", "Second", "s", 1.0),
            ConversionUnit("ms", "Millisecond", "ms", 0.001),
            ConversionUnit("min", "Minute", "min", 60.0),
            ConversionUnit("h", "Hour", "h", 3600.0),
            ConversionUnit("d", "Day", "d", 86400.0),
            ConversionUnit("wk", "Week", "wk", 604800.0),
            ConversionUnit("yr", "Year (365d)", "yr", 31536000.0)
        ),
        UnitCategory.DATA to listOf(
            ConversionUnit("b", "Byte", "B", 1.0),
            ConversionUnit("kb", "Kilobyte", "KB", 1024.0),
            ConversionUnit("mb", "Megabyte", "MB", 1024.0 * 1024.0),
            ConversionUnit("gb", "Gigabyte", "GB", 1024.0 * 1024.0 * 1024.0),
            ConversionUnit("tb", "Terabyte", "TB", 1024.0 * 1024.0 * 1024.0 * 1024.0),
            ConversionUnit("pb", "Petabyte", "PB", 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0)
        )
    )

    fun convert(
        category: UnitCategory,
        fromUnit: ConversionUnit,
        toUnit: ConversionUnit,
        value: Double
    ): Double {
        if (fromUnit.id == toUnit.id) return value

        if (category == UnitCategory.TEMPERATURE) {
            return convertTemperature(fromUnit.id, toUnit.id, value)
        }

        // Linear conversion via base unit
        val valueInBase = value * fromUnit.factorToBase
        return valueInBase / toUnit.factorToBase
    }

    private fun convertTemperature(fromId: String, toId: String, value: Double): Double {
        // Convert to Celsius first
        val celsius = when (fromId) {
            "c" -> value
            "f" -> (value - 32.0) * 5.0 / 9.0
            "k" -> value - 273.15
            else -> value
        }

        // Convert Celsius to target
        return when (toId) {
            "c" -> celsius
            "f" -> (celsius * 9.0 / 5.0) + 32.0
            "k" -> celsius + 273.15
            else -> celsius
        }
    }

    fun formatConvertedValue(value: Double): String {
        if (value.isNaN()) return "0"
        if (value.isInfinite()) return if (value > 0) "Infinity" else "-Infinity"
        val bd = try {
            BigDecimal.valueOf(value).round(MC).stripTrailingZeros()
        } catch (e: Exception) {
            BigDecimal(value.toString())
        }

        // Avoid scientific notation for normal range numbers
        val absVal = bd.abs()
        return if (absVal >= BigDecimal("1e12") || (absVal < BigDecimal("1e-6") && absVal > BigDecimal.ZERO)) {
            val df = java.text.DecimalFormat("0.######E0")
            df.format(bd).replace("E", "×10^")
        } else {
            val df = java.text.DecimalFormat("#,##0.######")
            df.format(bd)
        }
    }
}
