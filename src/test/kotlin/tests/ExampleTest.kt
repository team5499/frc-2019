package tests

import org.junit.jupiter.api.Test

import org.team5499.frc2019.Utils

public class ExampleTest {
    @Test
    fun example_test() {
        val time = System.nanoTime()
        println(time)
        println(Utils.nanoToSec(time))
        println(123456789)
        println(Utils.nanoToSec(123456789))
        println(1234567890)
        println(Utils.nanoToSec(1234567890))
    }
}
