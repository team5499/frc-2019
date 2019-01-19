package tests

import org.junit.jupiter.api.Test

import com.ctre.phoenix.motorcontrol.FeedbackDevice

public class ExampleTest {
    @Test
    fun example_test() {
        println(FeedbackDevice.Analog.value)
        println(FeedbackDevice.CTRE_MagEncoder_Absolute.value)
        println(FeedbackDevice.CTRE_MagEncoder_Relative.value)
        println(FeedbackDevice.PulseWidthEncodedPosition.value)
        println(FeedbackDevice.QuadEncoder.value)
        println(FeedbackDevice.RemoteSensor0.value)
        println(FeedbackDevice.RemoteSensor1.value)
        println(FeedbackDevice.SensorDifference.value)
        println(FeedbackDevice.SoftwareEmulatedSensor.value)
        println(FeedbackDevice.Tachometer.value)
    }
}
