import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun App() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .statusBarsPadding()
        ){
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
    var autoUpdateEnabled by remember { mutableStateOf(false) }

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

    LaunchedEffect(autoUpdateEnabled) {
        if (autoUpdateEnabled) {
            while (true) {
                delay(60000)
                fetchData()
            }
        }
    }

    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = defaultColor)
            }
        }

        error != null -> {
            ErrorCard(error = error!!, onRetry = { fetchData() })
        }

        xrpData != null -> {
            XrpDataCard(
                data = xrpData!!,
                onRefresh = { fetchData() },
                lastUpdated = lastUpdated,
                autoUpdateEnabled = autoUpdateEnabled,
                onAutoUpdateToggle = {autoUpdateEnabled = it }
            )
        }
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

// Needs a diff implementation for each platform to work
expect fun getCurrentTime(): String
