package store.strangeinfinity.calculator.data.settings

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import store.strangeinfinity.calculator.domain.calculator.AngleUnit

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("calculator_settings", Context.MODE_PRIVATE)

    private val _themeMode = MutableStateFlow(loadThemeMode())
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _angleUnit = MutableStateFlow(loadAngleUnit())
    val angleUnit: StateFlow<AngleUnit> = _angleUnit.asStateFlow()

    private val _hapticEnabled = MutableStateFlow(loadHaptic())
    val hapticEnabled: StateFlow<Boolean> = _hapticEnabled.asStateFlow()

    private fun loadThemeMode(): ThemeMode {
        val name = prefs.getString("theme_mode", ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name
        return try {
            ThemeMode.valueOf(name)
        } catch (e: Exception) {
            ThemeMode.SYSTEM
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString("theme_mode", mode.name).apply()
        _themeMode.value = mode
    }

    private fun loadAngleUnit(): AngleUnit {
        val name = prefs.getString("angle_unit", AngleUnit.DEGREE.name) ?: AngleUnit.DEGREE.name
        return try {
            AngleUnit.valueOf(name)
        } catch (e: Exception) {
            AngleUnit.DEGREE
        }
    }

    fun setAngleUnit(unit: AngleUnit) {
        prefs.edit().putString("angle_unit", unit.name).apply()
        _angleUnit.value = unit
    }

    private fun loadHaptic(): Boolean {
        return prefs.getBoolean("haptic_enabled", true)
    }

    fun setHapticEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("haptic_enabled", enabled).apply()
        _hapticEnabled.value = enabled
    }
}
