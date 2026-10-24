package store.strangeinfinity.calculator.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import store.strangeinfinity.calculator.data.settings.ThemeMode
import store.strangeinfinity.calculator.domain.calculator.AngleUnit
import store.strangeinfinity.calculator.ui.theme.LocalCalculatorPalette
import store.strangeinfinity.calculator.ui.theme.SamsungGreen

@Composable
fun ToolbarHandle(
    isScientificActive: Boolean,
    angleUnit: AngleUnit,
    currentTheme: ThemeMode,
    hapticEnabled: Boolean,
    onHistoryClick: () -> Unit,
    onConverterClick: () -> Unit,
    onScientificToggle: () -> Unit,
    onAngleUnitToggle: () -> Unit,
    onBackspaceClick: () -> Unit,
    onThemeSelect: (ThemeMode) -> Unit,
    onHapticToggle: (Boolean) -> Unit,
    onAboutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = LocalCalculatorPalette.current
    var showMenu by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(
            thickness = 0.5.dp,
            color = palette.divider,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Group
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onHistoryClick,
                    modifier = Modifier.semantics { contentDescription = "History" }
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "History",
                        tint = palette.iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = onConverterClick,
                    modifier = Modifier.semantics { contentDescription = "Unit converter" }
                ) {
                    Icon(
                        imageVector = Icons.Default.Straighten,
                        contentDescription = "Unit converter",
                        tint = palette.iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = onScientificToggle,
                    modifier = Modifier.semantics { contentDescription = "Scientific mode" }
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = "Scientific mode",
                        tint = if (isScientificActive) SamsungGreen else palette.iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }

                if (isScientificActive) {
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = onAngleUnitToggle,
                        modifier = Modifier.semantics {
                            contentDescription = "Switch angle unit. Currently ${angleUnit.name}"
                        }
                    ) {
                        Text(
                            text = if (angleUnit == AngleUnit.DEGREE) "Deg" else "Rad",
                            color = SamsungGreen,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            // Right Group: Backspace and More
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBackspaceClick,
                    modifier = Modifier.semantics { contentDescription = "Backspace" }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Backspace,
                        contentDescription = "Backspace",
                        tint = palette.iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.semantics { contentDescription = "More options" }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = palette.iconTint,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Theme: ${currentTheme.name.lowercase().replaceFirstChar { it.uppercase() }}") },
                            onClick = {
                                val next = when (currentTheme) {
                                    ThemeMode.SYSTEM -> ThemeMode.LIGHT
                                    ThemeMode.LIGHT -> ThemeMode.DARK
                                    ThemeMode.DARK -> ThemeMode.SYSTEM
                                }
                                onThemeSelect(next)
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(if (hapticEnabled) "Haptic Feedback: On" else "Haptic Feedback: Off") },
                            onClick = {
                                onHapticToggle(!hapticEnabled)
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("About StrangeInfinity") },
                            onClick = {
                                onAboutClick()
                                showMenu = false
                            }
                        )
                    }
                }
            }
        }
    }
}
