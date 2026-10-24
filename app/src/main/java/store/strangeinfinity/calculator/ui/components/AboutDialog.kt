package store.strangeinfinity.calculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import store.strangeinfinity.calculator.ui.theme.LocalCalculatorPalette
import store.strangeinfinity.calculator.ui.theme.SamsungGreen

@Composable
fun AboutDialog(
    onDismissRequest: () -> Unit
) {
    val palette = LocalCalculatorPalette.current

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = onDismissRequest,
                colors = ButtonDefaults.buttonColors(containerColor = SamsungGreen)
            ) {
                Text("Close", color = palette.operatorKeyText)
            }
        },
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(SamsungGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = null,
                        tint = palette.operatorKeyText,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Calculator",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.resultText
                )

                Text(
                    text = "by StrangeInfinity",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = SamsungGreen
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Version 1.0.1",
                    fontSize = 13.sp,
                    color = palette.expressionText
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = palette.divider, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "A precision-crafted everyday and scientific calculator featuring high accuracy decimal arithmetic, full scientific functions, and instant offline unit conversions.",
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    color = palette.resultText
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Package: store.strangeinfinity.calculator\n100% Offline • Private & Secure",
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Center,
                    color = palette.expressionText
                )
            }
        },
        containerColor = palette.surface,
        shape = RoundedCornerShape(24.dp)
    )
}
