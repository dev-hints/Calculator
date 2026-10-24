package store.strangeinfinity.calculator

import org.junit.Assert.assertEquals
import org.junit.Test
import store.strangeinfinity.calculator.domain.converter.UnitCategory
import store.strangeinfinity.calculator.domain.converter.UnitConverter

class UnitConverterTest {

    @Test
    fun testLengthConversion() {
        val lengthUnits = UnitConverter.unitsMap[UnitCategory.LENGTH]!!
        val m = lengthUnits.first { it.id == "m" }
        val km = lengthUnits.first { it.id == "km" }
        val cm = lengthUnits.first { it.id == "cm" }
        val ft = lengthUnits.first { it.id == "ft" }

        // 1000 m = 1 km
        assertEquals(1.0, UnitConverter.convert(UnitCategory.LENGTH, m, km, 1000.0), 0.0001)
        // 1 m = 100 cm
        assertEquals(100.0, UnitConverter.convert(UnitCategory.LENGTH, m, cm, 1.0), 0.0001)
        // 1 ft = 0.3048 m
        assertEquals(0.3048, UnitConverter.convert(UnitCategory.LENGTH, ft, m, 1.0), 0.0001)
    }

    @Test
    fun testTemperatureConversion() {
        val tempUnits = UnitConverter.unitsMap[UnitCategory.TEMPERATURE]!!
        val c = tempUnits.first { it.id == "c" }
        val f = tempUnits.first { it.id == "f" }
        val k = tempUnits.first { it.id == "k" }

        // 0 °C = 32 °F
        assertEquals(32.0, UnitConverter.convert(UnitCategory.TEMPERATURE, c, f, 0.0), 0.001)
        // 100 °C = 212 °F
        assertEquals(212.0, UnitConverter.convert(UnitCategory.TEMPERATURE, c, f, 100.0), 0.001)
        // -40 °C = -40 °F
        assertEquals(-40.0, UnitConverter.convert(UnitCategory.TEMPERATURE, c, f, -40.0), 0.001)
        // 0 °C = 273.15 K
        assertEquals(273.15, UnitConverter.convert(UnitCategory.TEMPERATURE, c, k, 0.0), 0.001)
        // 32 °F = 273.15 K
        assertEquals(273.15, UnitConverter.convert(UnitCategory.TEMPERATURE, f, k, 32.0), 0.001)
    }

    @Test
    fun testMassConversion() {
        val massUnits = UnitConverter.unitsMap[UnitCategory.MASS]!!
        val kg = massUnits.first { it.id == "kg" }
        val g = massUnits.first { it.id == "g" }
        val lb = massUnits.first { it.id == "lb" }

        // 1 kg = 1000 g
        assertEquals(1000.0, UnitConverter.convert(UnitCategory.MASS, kg, g, 1.0), 0.0001)
        // 1 lb ~ 0.453592 kg
        assertEquals(0.45359237, UnitConverter.convert(UnitCategory.MASS, lb, kg, 1.0), 0.0001)
    }

    @Test
    fun testDataStorageConversion() {
        val dataUnits = UnitConverter.unitsMap[UnitCategory.DATA]!!
        val b = dataUnits.first { it.id == "b" }
        val kb = dataUnits.first { it.id == "kb" }
        val mb = dataUnits.first { it.id == "mb" }
        val gb = dataUnits.first { it.id == "gb" }

        // 1024 B = 1 KB
        assertEquals(1.0, UnitConverter.convert(UnitCategory.DATA, b, kb, 1024.0), 0.0001)
        // 1024 KB = 1 MB
        assertEquals(1.0, UnitConverter.convert(UnitCategory.DATA, kb, mb, 1024.0), 0.0001)
        // 1 GB = 1024 MB
        assertEquals(1024.0, UnitConverter.convert(UnitCategory.DATA, gb, mb, 1.0), 0.0001)
    }

    @Test
    fun testSpeedConversion() {
        val speedUnits = UnitConverter.unitsMap[UnitCategory.SPEED]!!
        val mps = speedUnits.first { it.id == "mps" }
        val kmh = speedUnits.first { it.id == "kmh" }

        // 10 m/s = 36 km/h
        assertEquals(36.0, UnitConverter.convert(UnitCategory.SPEED, mps, kmh, 10.0), 0.001)
    }
}
