package com.tnm.android.core.ui.view.widgets

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tnm.android.core.ui.view.extensions.formatWithComma
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun BalanceUsageProgressBar(
    balance: BigDecimal,
    maxLimit: BigDecimal,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    var showCreditUsed by remember { mutableStateOf(false) }
    val safeCreditLimit = maxLimit.coerceAtLeast(BigDecimal.ONE)

    val availableCredit = (maxLimit - balance).coerceAtLeast(BigDecimal.ZERO)
    val creditUsed = balance.coerceAtLeast(BigDecimal.ZERO)

    val progressFraction: BigDecimal = if (safeCreditLimit.compareTo(BigDecimal.ZERO) == 0) {
        BigDecimal.ZERO
    } else if (showCreditUsed) {
        creditUsed.divide(safeCreditLimit, 4, RoundingMode.HALF_UP)
    } else {
        availableCredit.divide(safeCreditLimit, 4, RoundingMode.HALF_UP)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction.toFloat().coerceIn(0f, 1f),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "credit_usage_progress_animation"
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showCreditUsed = !showCreditUsed },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = (if (showCreditUsed) creditUsed else availableCredit).formatWithComma(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = maxLimit.formatWithComma(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }

        LinearProgressIndicator(
            modifier = modifier,
            progress = { animatedProgress }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewBalanceUsageProgressBar() {
    Surface(color = MaterialTheme.colorScheme.surfaceVariant) {
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BalanceUsageProgressBar(
                    balance = BigDecimal("25000.00"),
                    maxLimit = BigDecimal("30000.00"),
                )
            }
        }
    }
}