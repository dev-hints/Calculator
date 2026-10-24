# Calculator — StrangeInfinity

[![Android](https://img.shields.io/badge/Platform-Android%208.0%2B%20(API%2026%2B)-3DDC84.svg?style=flat&logo=android)](https://developer.android.com)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-34%20(Android%2014)-34A853.svg?style=flat)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7F52FF.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20%2B%20Material%203-4285F4.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Room](https://img.shields.io/badge/Persistence-Room%20SQLite%202.6.1-F4B400.svg?style=flat)](https://developer.android.com/training/data-storage/room)
[![Offline](https://img.shields.io/badge/Network-100%25%20Offline%20(Zero%20Permissions)-00C853.svg?style=flat)](https://github.com/dev-hints/Calculator)

A precision-crafted native Android calculator application developed under the **StrangeInfinity** brand, inspired by the familiar design language and interaction patterns of Samsung Calculator. Built from the ground up using **Kotlin**, **Jetpack Compose**, **Material 3**, and a custom **high-precision mathematical engine**.

---

## 📸 Screenshots

| Default Calculator | Calculation (`125 × 8 = 1,000`) | Scientific Mode |
| :---: | :---: | :---: |
| <img src="screenshots/calculator-main.png" width="240" /> | <img src="screenshots/calculator-calculation.png" width="240" /> | <img src="screenshots/calculator-scientific.png" width="240" /> |

| Calculation History | Unit Converter | AMOLED Dark Theme |
| :---: | :---: | :---: |
| <img src="screenshots/calculator-history.png" width="240" /> | <img src="screenshots/calculator-conversion.png" width="240" /> | <img src="screenshots/calculator-dark.png" width="240" /> |

| Landscape Layout (7 Columns) | About StrangeInfinity |
| :---: | :---: |
| <img src="screenshots/calculator-landscape.png" width="360" /> | <img src="screenshots/calculator-about.png" width="240" /> |

---

## ✨ Features

### 1. Mathematical Correctness & Precision Engine
- **High-Precision Decimal Arithmetic**: Powered by `BigDecimal` with `MathContext.DECIMAL128` (34 decimal digits of precision). Eliminates binary floating-point artifacts:
  - `0.1 + 0.2 = 0.3` (never `0.30000000000000004`).
- **Comprehensive Percentage Rules**:
  - Additive: `100 + 25% = 125`
  - Subtractive: `200 - 10% = 180`
  - Multiplicative: `50 × 20% = 10`
  - Divisive: `50 ÷ 25% = 200`
  - Standalone: `25% = 0.25`
- **Real-Time Live Preview**: Calculates intermediate results dynamically as you type.
- **Continuous Chaining**: Tapping an operator after `=` automatically continues from the previous calculation result.

### 2. Scientific Calculator (Portrait & Landscape)
- **Responsive Modes**:
  - *Portrait*: Smooth expandable panel toggled from the handle toolbar.
  - *Landscape*: Full 7-column layout (3 scientific columns on the left, 4 standard columns on the right).
- **Functions**:
  - Trigonometric: `sin`, `cos`, `tan`, `asin`, `acos`, `atan`, `sinh`, `cosh`, `tanh`.
  - Angle Modes: Switchable `Deg` (Degrees) and `Rad` (Radians) with instant recalculation.
  - Exponents & Roots: `x²`, `x³`, `xʸ`, `√`, `∛`, `10ˣ`, `eˣ`.
  - Logarithms: Natural log (`ln`), common log (`log` / `log10`).
  - Core Math: Reciprocal (`1/x`), absolute value (`|x|`), and factorial (`x!`) with overflow protection up to $n=1000$.
  - Constants: $\pi$ and $e$.
- **Robust Domain Protections**: Safely handles negative square roots, logarithm domain boundaries, tangent singularities, and division by zero with clean, non-crashing inline alerts.

### 3. Persistent Calculation History
- **Room SQLite Persistence**: Saves expressions, formatted results, and timestamps across application restarts.
- **Interactive History Sheet**: Tap any entry to reuse the expression or result.
- **Entry Management**: Delete individual entries or clear all history with a confirmation dialog.

### 4. Offline Unit Converter (8 Categories)
- **100% Offline Execution**: Completely self-contained conversion suite with zero network requirements.
- **Categories**:
  - **Length**: Meter, Kilometer, Centimeter, Millimeter, Micrometer, Nanometer, Mile, Yard, Foot, Inch, Nautical Mile.
  - **Area**: Square Meter, Square Kilometer, Hectare, Acre, Square Mile, Square Yard, Square Foot, Square Inch.
  - **Volume**: Liter, Milliliter, Cubic Meter, Gallon (US), Quart, Pint, Cup, Fluid Ounce.
  - **Mass**: Kilogram, Gram, Milligram, Metric Ton, Pound, Ounce, Carat.
  - **Temperature**: Celsius (°C), Fahrenheit (°F), Kelvin (K) with exact offset conversions.
  - **Speed**: m/s, km/h, mph, knot, ft/s.
  - **Time**: Second, Millisecond, Minute, Hour, Day, Week, Year.
  - **Data Storage**: Byte, Kilobyte (1024 B), Megabyte (1024 KB), Gigabyte (1024 MB), Terabyte, Petabyte.
- **Custom Keypad & Reversible Swap**: Dedicated numeric keypad with instant swap ($\rightleftarrows$) and one-tap clipboard copy.

### 5. Security & Privacy by Design
- **Zero Network Permissions**: `android.permission.INTERNET` is **not requested**. The OS physically prevents all network activity.
- **No Arbitrary Code Execution**: Custom Recursive Descent Parser evaluates math safely without `eval()`, JavaScript runtimes, or dynamic reflection.
- **Sandbox Storage**: Calculation records stay strictly inside the private app sandbox.
- **Sanitized Clipboard**: Pasted strings are filtered through a strict whitelist (`[0-9.+\-*/×÷(),^%eπ ]`).
- **Zero Telemetry**: No analytics SDKs, advertising trackers, or background listeners.

---

## 🏛️ Architecture

The codebase follows Clean Android Architecture with strict separation between Presentation, Domain, and Data layers:

```
app/src/main/java/store/strangeinfinity/calculator/
├── domain/
│   ├── calculator/
│   │   ├── ExpressionEvaluator.kt   # Recursive Descent Parser & Math Engine
│   │   ├── NumberFormatter.kt       # Decimal128 & thousand separators
│   │   └── CalculationResult.kt     # Sealed class for Success & Error states
│   └── converter/
│       └── UnitConverter.kt         # Conversion algorithms & unit definitions
├── data/
│   ├── history/
│   │   ├── CalculationEntity.kt     # Room database entity
│   │   ├── CalculationDao.kt        # SQLite Data Access Object
│   │   ├── CalculatorDatabase.kt    # Room database instance
│   │   └── HistoryRepository.kt     # Flow-based repository
│   └── settings/
│       └── SettingsManager.kt       # Theme and angle unit preferences
├── ui/
│   ├── components/
│   │   ├── CalculatorButton.kt      # Rounded keys with haptic feedback
│   │   ├── CalculatorDisplay.kt     # Auto-scaling expression & result area
│   │   ├── KeypadStandard.kt        # 4x5 standard arithmetic grid
│   │   ├── KeypadScientific.kt      # Portrait slide-down & Landscape 7-column
│   │   ├── ToolbarHandle.kt         # Middle tool row & more options menu
│   │   ├── HistorySheet.kt          # Modal bottom sheet for history
│   │   ├── UnitConverterScreen.kt   # Dedicated converter screen & keypad
│   │   └── AboutDialog.kt           # StrangeInfinity branding dialog
│   ├── theme/
│   │   ├── Color.kt                 # Samsung-inspired palette (AMOLED Dark & Clean Light)
│   │   ├── Type.kt                  # Typography hierarchy
│   │   └── Theme.kt                 # Material 3 & custom palette composition
│   ├── CalculatorScreen.kt          # Root responsive screen
│   ├── CalculatorViewModel.kt       # UI state management & event coordination
│   └── ConverterViewModel.kt        # Unit converter state management
└── MainActivity.kt                  # Edge-to-edge container & hardware key handling
```

---

## 🛠️ Building & Running

### Prerequisites
- **JDK**: Java 17 or Java 21 (e.g. JetBrains Runtime `jbr-21`)
- **Android SDK**: Build Tools `34.0.0`, Platforms `android-34`
- **Gradle**: 8.10 (Gradle wrapper included)

### Build Tasks

```bash
# Run automated unit test suite (17/17 tests)
./gradlew test

# Build debug APK
./gradlew assembleDebug

# Build release APK with Lint-Vital checks
./gradlew assembleRelease
```

### Generated APK Artifacts
- **Signed Release APK**: [`app-release-signed.apk`](app-release-signed.apk) (11 MB)
- **Debug APK**: `app/build/outputs/apk/debug/app-debug.apk` (16 MB)

### Installation via ADB

```bash
# Install to connected Android device
adb install -r app-release-signed.apk

# Launch Calculator
adb shell am start -n store.strangeinfinity.calculator/.MainActivity
```

---

## 🧪 Automated Testing

Run the automated test suite with:

```bash
./gradlew testDebugUnitTest
```

Verified test coverage includes:
- Basic operations (`+`, `−`, `×`, `÷`) and operator precedence
- Floating-point precision validation (`0.1 + 0.2 = 0.3`)
- Percentage rules across addition, subtraction, multiplication, and division
- Trigonometric functions in Degree and Radian modes
- Logarithms, powers, roots, factorials, and division-by-zero protection
- Unit converter formulas across all 8 supported measurement categories

---

## 📄 License & Attribution

Developed and published under the **StrangeInfinity** brand.  
Copyright © 2026 StrangeInfinity. All rights reserved.
