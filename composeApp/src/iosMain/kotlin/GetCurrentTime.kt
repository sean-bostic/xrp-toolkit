import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter

actual fun getCurrentTime(): String {
    val formatter = NSDateFormatter()
    formatter.dateFormat = "HH:mm:ss"
    return formatter.stringFromDate(NSDate())
}