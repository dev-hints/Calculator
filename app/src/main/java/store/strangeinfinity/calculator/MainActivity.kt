package store.strangeinfinity.calculator

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import store.strangeinfinity.calculator.ui.CalculatorScreen
import store.strangeinfinity.calculator.ui.CalculatorViewModel
import store.strangeinfinity.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {

    private val calculatorViewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeMode by calculatorViewModel.themeMode.collectAsState()
            CalculatorTheme(themeMode = themeMode) {
                CalculatorScreen(calculatorViewModel = calculatorViewModel)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event == null) return super.onKeyDown(keyCode, event)

        // Handle hardware keyboard input
        when (keyCode) {
            KeyEvent.KEYCODE_0, KeyEvent.KEYCODE_NUMPAD_0 -> {
                calculatorViewModel.onDigit("0")
                return true
            }
            KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_NUMPAD_1 -> {
                calculatorViewModel.onDigit("1")
                return true
            }
            KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_NUMPAD_2 -> {
                calculatorViewModel.onDigit("2")
                return true
            }
            KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_NUMPAD_3 -> {
                calculatorViewModel.onDigit("3")
                return true
            }
            KeyEvent.KEYCODE_4, KeyEvent.KEYCODE_NUMPAD_4 -> {
                calculatorViewModel.onDigit("4")
                return true
            }
            KeyEvent.KEYCODE_5, KeyEvent.KEYCODE_NUMPAD_5 -> {
                if (event.isShiftPressed) calculatorViewModel.onPercentage()
                else calculatorViewModel.onDigit("5")
                return true
            }
            KeyEvent.KEYCODE_6, KeyEvent.KEYCODE_NUMPAD_6 -> {
                if (event.isShiftPressed) calculatorViewModel.onScientificFunction("xʸ")
                else calculatorViewModel.onDigit("6")
                return true
            }
            KeyEvent.KEYCODE_7, KeyEvent.KEYCODE_NUMPAD_7 -> {
                calculatorViewModel.onDigit("7")
                return true
            }
            KeyEvent.KEYCODE_8, KeyEvent.KEYCODE_NUMPAD_8 -> {
                if (event.isShiftPressed) calculatorViewModel.onOperator("×")
                else calculatorViewModel.onDigit("8")
                return true
            }
            KeyEvent.KEYCODE_9, KeyEvent.KEYCODE_NUMPAD_9 -> {
                if (event.isShiftPressed) calculatorViewModel.onParentheses()
                else calculatorViewModel.onDigit("9")
                return true
            }
            KeyEvent.KEYCODE_PERIOD, KeyEvent.KEYCODE_NUMPAD_DOT -> {
                calculatorViewModel.onDecimal()
                return true
            }
            KeyEvent.KEYCODE_PLUS, KeyEvent.KEYCODE_NUMPAD_ADD -> {
                calculatorViewModel.onOperator("+")
                return true
            }
            KeyEvent.KEYCODE_MINUS, KeyEvent.KEYCODE_NUMPAD_SUBTRACT -> {
                calculatorViewModel.onOperator("−")
                return true
            }
            KeyEvent.KEYCODE_NUMPAD_MULTIPLY, KeyEvent.KEYCODE_STAR -> {
                calculatorViewModel.onOperator("×")
                return true
            }
            KeyEvent.KEYCODE_SLASH, KeyEvent.KEYCODE_NUMPAD_DIVIDE -> {
                calculatorViewModel.onOperator("÷")
                return true
            }
            KeyEvent.KEYCODE_EQUALS, KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_NUMPAD_ENTER -> {
                if (event.isShiftPressed && keyCode == KeyEvent.KEYCODE_EQUALS) {
                    calculatorViewModel.onOperator("+")
                } else {
                    calculatorViewModel.onEquals()
                }
                return true
            }
            KeyEvent.KEYCODE_DEL -> {
                calculatorViewModel.onBackspace()
                return true
            }
            KeyEvent.KEYCODE_ESCAPE -> {
                calculatorViewModel.onClear()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
