import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Color as AwtColor

fun main() =
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "XRP Pricer",
            state =
                rememberWindowState(
                    width = 600.dp,
                    height = 360.dp,
                ),
        ) {
            window.rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
            window.rootPane.putClientProperty("apple.awt.fullWindowContent", true)
            window.rootPane.putClientProperty("apple.awt.appearance", "NSAppearanceNameVibrantDark")
            window.background = AwtColor.BLACK
            App()
        }
    }
