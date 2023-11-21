import kotlinx.cinterop.ExperimentalForeignApi
import wiringPi.HIGH
import wiringPi.delay
import wiringPi.digitalWrite
import wiringPi.wiringPiSetup

@OptIn(ExperimentalForeignApi::class)
fun main() {
    println("Hello, Kotlin/Native!")
}