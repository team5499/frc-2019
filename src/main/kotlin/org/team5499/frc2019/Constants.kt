package org.team5499.frc2019

@SuppressWarnings("MagicNumber")
public object Constants {

    public val ROBOT_UPDATE_PERIOD = 0.005

    object Input {
        public const val DRIVER_PORT = 0
        public const val CODRIVER_PORT = 1
    }

    object HardwarePorts {
        public const val LEFT_DRIVE_MASTER = 1
        public const val LEFT_DRIVE_SLAVE1 = 2
        public const val LEFT_DRIVE_SLAVE2 = 3

        public const val RIGHT_DRIVE_MASTER = 4
        public const val RIGHT_DRIVE_SLAVE1 = 5
        public const val RIGHT_DRIVE_SLAVE2 = 6

        public const val LIFT_MASTER = 7
        public const val LIFT_SLAVE = 8

        public const val INTAKE_MASTER = 10

        public const val GYRO_PORT = 9
    }
}
