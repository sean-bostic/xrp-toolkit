import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xrptools.toolkit.XrpClient
import com.xrptools.toolkit.models.XrpSummary
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun App() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            XrpTrackerScreen()
        }
    }
}

@Composable
fun XrpTrackerScreen() {
    var xrpData by remember { mutableStateOf<XrpSummary?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var lastUpdated by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val client = remember { XrpClient() }

    fun fetchData() {
        scope.launch {
            isLoading = true
            error = null

            client.getXrpData()
                .onSuccess {
                    xrpData = it
                    lastUpdated = getCurrentTime()
                }
                .onFailure {
                    error = it.message ?: "Unknown error"
                }

            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        fetchData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "XRP Toolkit",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp
            ),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))


        Spacer(modifier = Modifier.height(32.dp))

        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Loading XRP data...")
            }

            error != null -> {
                ErrorCard(error = error!!, onRetry = { fetchData() })
            }

            xrpData != null -> {
                XrpDataCard(data = xrpData!!)

                Spacer(modifier = Modifier.height(16.dp))

                if (lastUpdated.isNotEmpty()) {
                    Text(
                        "Last updated: $lastUpdated",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { fetchData() },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(48.dp)
        ) {
            Text(
                if (isLoading) "Loading..." else "Refresh Data",
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun XrpDataCard(data: XrpSummary) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "${data.priceUsd.formatDecimal(4)}",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 48.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            TrendDisplay(changePercent = data.priceChangePercentage24h)

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Market Cap",
                    value = formatLargeNumber(data.marketCapUsd),
                    modifier = Modifier.weight(1f)
                )

                StatItem(
                    label = "24h Volume",
                    value = formatLargeNumber(data.volume24hUsd),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "24h High",
                    value = "${data.high24hUsd.formatDecimal(4)}",
                    modifier = Modifier.weight(1f)
                )

                StatItem(
                    label = "24h Low",
                    value = "${data.low24hUsd.formatDecimal(4)}",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun TrendDisplay(changePercent: Double) {
    val emojiCount = (abs(changePercent) / 5.0).toInt()
    val emoji = if (changePercent > 0) "🚀" else "💩"
    val emojiString = emoji.repeat(emojiCount)

    var pumpDumpMessage = if (emojiCount > 0) {
        if (changePercent > 0) "Pump Status" else "Dump Status: "
    } else {
        "24h Trend: "
    }

    if(emojiString.isEmpty()) {
        pumpDumpMessage = "$pumpDumpMessage $emojiString"
    }

    val trendText = if (changePercent > 0) "UP" else "DOWN"
    val trendColor = if (changePercent > 0) Color(0xFF10b981) else Color(0xFFef4444)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            pumpDumpMessage,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (emojiString.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                emojiString,
                fontSize = 32.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                trendText,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = trendColor
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                "${if (changePercent > 0) "+" else ""}${changePercent.formatDecimal(2)}%",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = trendColor
            )
        }
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            value,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
fun ErrorCard(error: String, onRetry: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "⚠️",
                fontSize = 48.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Error Loading Data",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Try Again")
            }
        }
    }
}

fun formatLargeNumber(num: Long): String {
    return when {
        num >= 1_000_000_000 -> "${(num / 1_000_000_000.0).formatDecimal(2)}B"
        num >= 1_000_000 -> "${(num / 1_000_000.0).formatDecimal(2)}M"
        num >= 1_000 -> "${(num / 1_000.0).formatDecimal(2)}K"
        else -> "$num"
    }
}

fun Double.formatDecimal(decimals: Int): String {
    val multiplier = when(decimals) {
        2 -> 100.0
        4 -> 10000.0
        else -> 1.0
    }
    val rounded = (this * multiplier).toLong() / multiplier
    return rounded.toString()
}


// Needs a diff implementation for each platform to work
expect fun getCurrentTime(): String