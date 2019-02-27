package org.team5499.frc2019

object Utils {

    @Suppress("MagicNumber")
    fun nanoToSec(nanos: Long): Double {
        var stringValue = ""
        if (nanos.toString().length < 9) {
            for (i in 0..(9 - nanos.toString().length)) {
                stringValue += "0"
            }
            stringValue += "."
        } else {
            stringValue += nanos.toString().substring(0, nanos.toString().length - 9)
            stringValue += "."
            stringValue += nanos.toString().substring(nanos.toString().length - 9)
        }
        return stringValue.toDouble()
    }
}
