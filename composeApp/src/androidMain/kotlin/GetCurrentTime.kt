import java.time.LocalTime
import java.time.format.DateTimeFormatter

actual fun getCurrentTime(): String {
    return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
}