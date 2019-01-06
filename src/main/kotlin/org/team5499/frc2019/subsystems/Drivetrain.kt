package org.team5499.frc2019.subsystems

import com.ctre.phoenix.motorcontrol.can.VictorSPX
import com.ctre.phoenix.sensors.PigeonIMU
import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode

import org.team5499.frc2019.Constants

import org.team5499.monkeyLib.Subsystem
import org.team5499.monkeyLib.hardware.LazyTalonSRX
import org.team5499.monkeyLib.math.Position
import org.team5499.monkeyLib.math.geometry.Vector2
import org.team5499.monkeyLib.math.geometry.Rotation2d
import org.team5499.monkeyLib.math.geometry.Pose2d
import org.team5499.monkeyLib.util.Utils

public class Drivetrain : Subsystem() {

    private enum class DriveMode {
        OPEN_LOOP,
        POSITION,
        VELOCITY,
        TURN
    }

    // hardware
    private val mLeftMaster: LazyTalonSRX
    private val mLeftSlave1: VictorSPX
    private val mLeftSlave2: VictorSPX

    private val mRightMaster: LazyTalonSRX
    private val mRightSlave1: VictorSPX
    private val mRightSlave2: VictorSPX

    private val mGyro: PigeonIMU

    // drive variables
    private var mDriveMode: DriveMode = DriveMode.OPEN_LOOP
        set(value) {
            if (field == value) return
            when (value) {
                DriveMode.OPEN_LOOP -> {
                }
                DriveMode.POSITION -> {
                }
                DriveMode.VELOCITY -> {
                }
                DriveMode.TURN -> {
                }
            }
            field = value
        }

    public var brakeMode: Boolean = false
        set(value) {
            if (value == field) return
            val mode = if (value) NeutralMode.Brake else NeutralMode.Coast
            mLeftMaster.setNeutralMode(mode)
            mLeftSlave1.setNeutralMode(mode)
            mLeftSlave2.setNeutralMode(mode)

            mRightMaster.setNeutralMode(mode)
            mRightSlave1.setNeutralMode(mode)
            mRightSlave2.setNeutralMode(mode)
            field = value
        }

    private val mPosition = Position()

    public fun setPosition(vector: Vector2) {
        mPosition.positionVector = vector
    }

    private var mGyroOffset: Rotation2d = Rotation2d.identity
        set(value) { field = value }

    public var heading: Rotation2d
        get() {
            return Rotation2d.fromDegrees(mGyro.getFusedHeading()).rotateBy(mGyroOffset)
        }
        set(value) {
            println("SET HEADING: ${heading.degrees}")
            mGyroOffset = value.rotateBy(Rotation2d.fromDegrees(mGyro.getFusedHeading()).inverse())
            println("Gyro offset: ${mGyroOffset.degrees}")
        }

    public val angularVelocity: Double
        get() {
            val xyz = doubleArrayOf(0.0, 0.0, 0.0)
            mGyro.getRawGyro(xyz)
            return xyz[1]
        }

    public val pose: Pose2d
        get() = Pose2d(mPosition.positionVector, heading)

    public var leftDistance: Double
        get() {
            return Utils.encoderTicksToInches(
                Constants.Units.ENCODER_TICKS_PER_ROTATION,
                Constants.Dimensions.WHEEL_CIR,
                mLeftMaster.sensorCollection.quadraturePosition
            )
        }
        set(inches) {
            mLeftMaster.sensorCollection.setQuadraturePosition(
                Utils.inchesToEncoderTicks(Constants.Units.ENCODER_TICKS_PER_ROTATION,
                Constants.Dimensions.WHEEL_CIR,
                inches), 0)
        }

    public var rightDistance: Double
        get() {
            return Utils.encoderTicksToInches(
                Constants.Units.ENCODER_TICKS_PER_ROTATION,
                Constants.Dimensions.WHEEL_CIR,
                mRightMaster.sensorCollection.quadraturePosition
            )
        }
        set(inches) {
            mRightMaster.sensorCollection.setQuadraturePosition(
                Utils.inchesToEncoderTicks(Constants.Units.ENCODER_TICKS_PER_ROTATION,
                Constants.Dimensions.WHEEL_CIR,
                inches), 0)
        }

    public val leftVelocity: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(
            Constants.Units.ENCODER_TICKS_PER_ROTATION,
            Constants.Dimensions.WHEEL_CIR,
            mLeftMaster.sensorCollection.quadratureVelocity
        )

    public val rightVelocity: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(
            Constants.Units.ENCODER_TICKS_PER_ROTATION,
            Constants.Dimensions.WHEEL_CIR,
            mRightMaster.sensorCollection.quadratureVelocity
        )

    public val averageVelocity: Double
        get() = (leftVelocity + rightVelocity) / 2.0

    public val positionError: Double
        get() = Utils.encoderTicksToInches(
            Constants.Units.ENCODER_TICKS_PER_ROTATION,
            Constants.Dimensions.WHEEL_CIR,
            mRightMaster.getClosedLoopError(0)
        )

    val leftVelocityError: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(
            Constants.Units.ENCODER_TICKS_PER_ROTATION,
            Constants.Dimensions.WHEEL_CIR,
            mLeftMaster.getClosedLoopError(0)
        )

    val rightVelocityError: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(
            Constants.Units.ENCODER_TICKS_PER_ROTATION,
            Constants.Dimensions.WHEEL_CIR,
            mRightMaster.getClosedLoopError(0)
        )

    val averageVelocityError: Double
        get() = (leftVelocityError + rightVelocityError) / 2.0

    val anglePositionError: Double
        get() = Utils.talonAngleToDegrees(
            Constants.Units.TURN_UNITS_PER_ROTATION,
            mRightMaster.getClosedLoopError(1)
        )

    val turnError: Double
        get() = Utils.talonAngleToDegrees(
            Constants.Units.TURN_UNITS_PER_ROTATION,
            mRightMaster.getClosedLoopError(0)
        )

    val turnFixedError: Double
        get() = Utils.encoderTicksToInches(
            Constants.Units.ENCODER_TICKS_PER_ROTATION,
            Constants.Dimensions.WHEEL_CIR,
            mRightMaster.getClosedLoopError(1)
        )

    // configuration methods

    // set methods

    init {
        mLeftMaster = LazyTalonSRX(Constants.HardwarePorts.LEFT_DRIVE_MASTER)
        mLeftSlave1 = VictorSPX(Constants.HardwarePorts.LEFT_DRIVE_SLAVE1)
        mLeftSlave2 = VictorSPX(Constants.HardwarePorts.LEFT_DRIVE_SLAVE2)

        mLeftSlave1.follow(mLeftMaster)
        mLeftSlave2.follow(mLeftMaster)

        mLeftMaster.setInverted(false)

        mRightMaster = LazyTalonSRX(Constants.HardwarePorts.RIGHT_DRIVE_MASTER)
        mRightSlave1 = VictorSPX(Constants.HardwarePorts.RIGHT_DRIVE_SLAVE1)
        mRightSlave2 = VictorSPX(Constants.HardwarePorts.RIGHT_DRIVE_SLAVE2)

        mRightSlave1.follow(mRightMaster)
        mRightSlave2.follow(mRightMaster)

        mRightMaster.setInverted(true)

        mGyro = PigeonIMU(Constants.HardwarePorts.GYRO_PORT)
    }

    public fun setPercent(left: Double, right: Double) {
        mLeftMaster.set(ControlMode.PercentOutput, left)
        mRightMaster.set(ControlMode.PercentOutput, right)
    }

    public override fun update() {
        mPosition.update(leftDistance, rightDistance, heading.degrees)
    }

    public override fun stop() {
        leftDistance = 0.0
        rightDistance = 0.0
        mPosition.reset()
        brakeMode = false
    }

    public override fun reset() {
        setPercent(0.0, 0.0)
        mLeftMaster.neutralOutput()
        mRightMaster.neutralOutput()
        brakeMode = false
    }
}
