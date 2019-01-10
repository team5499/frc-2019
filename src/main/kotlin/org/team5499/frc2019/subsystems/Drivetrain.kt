package org.team5499.frc2019.subsystems

import com.ctre.phoenix.sensors.PigeonIMU
import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced
import com.ctre.phoenix.motorcontrol.DemandType
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.SensorTerm
import com.ctre.phoenix.motorcontrol.RemoteSensorSource
import com.ctre.phoenix.motorcontrol.FollowerType
import com.ctre.phoenix.motorcontrol.can.SlotConfiguration
import com.ctre.phoenix.motorcontrol.InvertType
import com.ctre.phoenix.ParamEnum

import org.team5499.frc2019.Constants

import org.team5499.monkeyLib.Subsystem
import org.team5499.monkeyLib.hardware.LazyTalonSRX
import org.team5499.monkeyLib.hardware.LazyVictorSPX
import org.team5499.monkeyLib.math.Position
import org.team5499.monkeyLib.math.geometry.Vector2
import org.team5499.monkeyLib.math.geometry.Rotation2d
import org.team5499.monkeyLib.math.geometry.Pose2d
import org.team5499.monkeyLib.util.Utils
import org.team5499.monkeyLib.input.DriveSignal

@Suppress("LargeClass", "TooManyFunctions")
public class Drivetrain : Subsystem() {

    companion object {
        private const val kPrimaryPIDSlot = 0
        private const val kSecondaryPIDSlot = 1
    }

    private enum class DriveMode {
        OPEN_LOOP,
        POSITION,
        VELOCITY,
        TURN
    }

    // hardware
    private val mLeftMaster: LazyTalonSRX
    private val mLeftSlave1: LazyVictorSPX
    private val mLeftSlave2: LazyVictorSPX

    private val mRightMaster: LazyTalonSRX
    private val mRightSlave1: LazyVictorSPX
    private val mRightSlave2: LazyVictorSPX

    private val mGyro: PigeonIMU

    private val mPositionPIDConfig: SlotConfiguration
    private val mAnglePIDConfig: SlotConfiguration

    private val mVelocityPIDConfig: SlotConfiguration

    private val mTurnPIDConfig: SlotConfiguration
    private val mFixedPIDConfig: SlotConfiguration
    // maybe use these at some point

    // drive variables
    private var mDriveMode: DriveMode = DriveMode.OPEN_LOOP
        set(value) {
            if (field == value) return
            when (value) {
                DriveMode.OPEN_LOOP -> {
                    configureForPercent()
                }
                DriveMode.POSITION -> {
                    configureForPosition()
                }
                DriveMode.VELOCITY -> {
                    configureForVelocity()
                }
                DriveMode.TURN -> {
                    configureForTurn()
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

    public val leftVelocityError: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(
            Constants.Units.ENCODER_TICKS_PER_ROTATION,
            Constants.Dimensions.WHEEL_CIR,
            mLeftMaster.getClosedLoopError(0)
        )

    public val rightVelocityError: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(
            Constants.Units.ENCODER_TICKS_PER_ROTATION,
            Constants.Dimensions.WHEEL_CIR,
            mRightMaster.getClosedLoopError(0)
        )

    public val averageVelocityError: Double
        get() = (leftVelocityError + rightVelocityError) / 2.0

    public val anglePositionError: Double
        get() = Utils.talonAngleToDegrees(
            Constants.Units.TURN_UNITS_PER_ROTATION,
            mRightMaster.getClosedLoopError(1)
        )

    public val turnError: Double
        get() = Utils.talonAngleToDegrees(
            Constants.Units.TURN_UNITS_PER_ROTATION,
            mRightMaster.getClosedLoopError(0)
        )

    public val turnFixedError: Double
        get() = Utils.encoderTicksToInches(
            Constants.Units.ENCODER_TICKS_PER_ROTATION,
            Constants.Dimensions.WHEEL_CIR,
            mRightMaster.getClosedLoopError(1)
        )

    init {
        // initialze hardware
        mLeftMaster = LazyTalonSRX(Constants.HardwarePorts.LEFT_DRIVE_MASTER).apply {
            setInverted(false)
            setSensorPhase(false)
            setStatusFramePeriod(
                StatusFrameEnhanced.Status_3_Quadrature,
                Constants.TALON_UPDATE_PERIOD_MS,
                0
            )
        }
        mLeftSlave1 = LazyVictorSPX(Constants.HardwarePorts.LEFT_DRIVE_SLAVE1).apply {
            follow(mLeftMaster)
            setInverted(InvertType.FollowMaster)
        }
        mLeftSlave2 = LazyVictorSPX(Constants.HardwarePorts.LEFT_DRIVE_SLAVE2).apply {
            follow(mLeftMaster)
            setInverted(InvertType.FollowMaster)
        }

        mRightMaster = LazyTalonSRX(Constants.HardwarePorts.RIGHT_DRIVE_MASTER).apply {
            setInverted(true)
            setSensorPhase(false)
            setStatusFramePeriod(
                StatusFrameEnhanced.Status_3_Quadrature,
                Constants.TALON_UPDATE_PERIOD_MS,
                0
            )
        }
        mRightSlave1 = LazyVictorSPX(Constants.HardwarePorts.RIGHT_DRIVE_SLAVE1).apply {
            follow(mRightMaster)
            setInverted(InvertType.FollowMaster)
        }
        mRightSlave2 = LazyVictorSPX(Constants.HardwarePorts.RIGHT_DRIVE_SLAVE2).apply {
            follow(mRightMaster)
            setInverted(InvertType.FollowMaster)
        }

        mGyro = PigeonIMU(Constants.HardwarePorts.GYRO_PORT)

        // initialize PID
        mPositionPIDConfig = SlotConfiguration()
        mAnglePIDConfig = SlotConfiguration()
        mVelocityPIDConfig = SlotConfiguration()
        mTurnPIDConfig = SlotConfiguration()
        mFixedPIDConfig = SlotConfiguration()
        loadGains()
    }

    public fun setPercent(signal: DriveSignal) {
        setPercent(signal.left, signal.right, signal.brakeMode)
    }

    public fun setPercent(left: Double, right: Double, brakeMode: Boolean = false) {
        mDriveMode = DriveMode.OPEN_LOOP
        mLeftMaster.set(ControlMode.PercentOutput, left)
        mRightMaster.set(ControlMode.PercentOutput, right)
        this.brakeMode = brakeMode
    }

    public fun setPosition(distance: Double) {
        mDriveMode = DriveMode.POSITION
        val absDistance = Utils.inchesToEncoderTicks(
            Constants.Units.ENCODER_TICKS_PER_ROTATION,
            Constants.Dimensions.WHEEL_CIR,
            ((leftDistance + rightDistance) / 2.0) + distance
        )
        val angleTarget = mRightMaster.getSelectedSensorPosition(1)
        mRightMaster.set(ControlMode.Position, absDistance.toDouble(), DemandType.AuxPID, angleTarget.toDouble())
    }

    public fun setTurn(angle: Double) {
        mDriveMode = DriveMode.TURN
        val fixedDistance = Utils.inchesToEncoderTicks(
            Constants.Units.ENCODER_TICKS_PER_ROTATION,
            Constants.Dimensions.WHEEL_CIR,
            (leftDistance + rightDistance) / 2.0
        )
        val angleTarget = mRightMaster.getSelectedSensorPosition(1) +
            Utils.degreesToTalonAngle(Constants.Units.TURN_UNITS_PER_ROTATION, angle)
        mRightMaster.set(ControlMode.Position, angleTarget.toDouble(), DemandType.AuxPID, fixedDistance.toDouble())
    }

    public fun setVelocity(leftSpeed: Double, rightSpeed: Double) {
        mDriveMode = DriveMode.VELOCITY
        val left = Utils.limit(leftSpeed, Constants.PID.MAX_VELOCITY_SETPOINT)
        val right = Utils.limit(rightSpeed, Constants.PID.MAX_VELOCITY_SETPOINT)
        mLeftMaster.set(ControlMode.Velocity,
            Utils.inchesPerSecondToEncoderTicksPer100Ms(
                Constants.Units.ENCODER_TICKS_PER_ROTATION,
                Constants.Dimensions.WHEEL_CIR,
                left)
            )
        mRightMaster.set(ControlMode.Velocity,
            Utils.inchesPerSecondToEncoderTicksPer100Ms(
                Constants.Units.ENCODER_TICKS_PER_ROTATION,
                Constants.Dimensions.WHEEL_CIR,
                right)
            )
    }

    private fun configureForPercent() {
        brakeMode = false
        mLeftMaster.set(ControlMode.PercentOutput, 0.0) // make sure its not following right master
        mLeftMaster.apply {
            configNominalOutputForward(0.0, 0)
            configNominalOutputReverse(0.0, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            setInverted(false)
            setSensorPhase(false)
        }

        mRightMaster.apply {
            configNominalOutputForward(0.0, 0)
            configNominalOutputReverse(0.0, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            setInverted(true)
            setSensorPhase(false)
        }
    }

    private fun configureForVelocity() {
        brakeMode = true
        mLeftMaster.set(ControlMode.PercentOutput, 0.0) // make sure its not following rightMaster
        mLeftMaster.apply {
            setInverted(false)
            configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            setSensorPhase(false)
            config_kP(0, Constants.PID.VEL_KP, 0)
            config_kI(0, Constants.PID.VEL_KI, 0)
            config_kD(0, Constants.PID.VEL_KD, 0)
            config_kF(0, Constants.PID.VEL_KF, 0)
            config_kP(1, 0.0, 0)
            config_kI(1, 0.0, 0)
            config_kD(1, 0.0, 0)
            config_kF(1, 0.0, 0)
            config_IntegralZone(0, 0, 0)
            configClosedLoopPeakOutput(0, 1.0, 0)
            config_IntegralZone(1, 0, 0)
            configClosedLoopPeakOutput(1, 0.0, 0)
            selectProfileSlot(0, 0)
            selectProfileSlot(1, 1)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 0, 0)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 1, 0)
        }

        mRightMaster.apply {
            configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0)
            configRemoteFeedbackFilter(0x00, RemoteSensorSource.Off, 0, 0)
            configRemoteFeedbackFilter(0x00, RemoteSensorSource.Off, 1, 0)
            // configSensorTerm(SensorTerm.Sum0, FeedbackDevice.None, 0)
            // configSensorTerm(SensorTerm.Sum1, FeedbackDevice.None, 0)
            configSelectedFeedbackCoefficient(1.0, 1, 0)
            configSelectedFeedbackCoefficient(1.0, 0, 0)
            // configSelectedFeedbackSensor(FeedbackDevice.None, 1, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            setSensorPhase(true)
            config_kP(0, Constants.PID.VEL_KP, 0)
            config_kI(0, Constants.PID.VEL_KI, 0)
            config_kD(0, Constants.PID.VEL_KD, 0)
            config_kF(0, Constants.PID.VEL_KF, 0)
            config_kP(1, 0.0, 0)
            config_kI(1, 0.0, 0)
            config_kD(1, 0.0, 0)
            config_kF(1, 0.0, 0)
            config_IntegralZone(0, 0, 0)
            configClosedLoopPeakOutput(0, 1.0, 0)
            config_IntegralZone(1, 0, 0)
            configClosedLoopPeakOutput(1, 0.0, 0)
            selectProfileSlot(0, 0)
            selectProfileSlot(1, 1)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 0, 0)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 1, 0)
        }
    }

    private fun configureForTurn() {
        brakeMode = true
        mLeftMaster.apply {
            configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, Constants.TALON_UPDATE_PERIOD_MS, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            follow(mRightMaster, FollowerType.AuxOutput1)
            setSensorPhase(false)
            setInverted(true)
        }

        mRightMaster.apply {
            configRemoteFeedbackFilter(mLeftMaster.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor, 0, 0)
            configRemoteFeedbackFilter(mGyro.getDeviceID(), RemoteSensorSource.Pigeon_Yaw, 1, 0)
            configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, 0)
            configSensorTerm(SensorTerm.Sum1, FeedbackDevice.QuadEncoder, 0)
            configSelectedFeedbackSensor(FeedbackDevice.SensorSum, 1, 0)
            @Suppress("MagicNumber")
            configSelectedFeedbackCoefficient(0.5, 1, 0)
            configSelectedFeedbackCoefficient(
                (Constants.Units.TURN_UNITS_PER_ROTATION / Constants.Units.PIGEON_UNITS_PER_ROTATION).toDouble(), 0, 0)
            configSelectedFeedbackSensor(FeedbackDevice.RemoteSensor1, 0, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_12_Feedback1, Constants.TALON_UPDATE_PERIOD_MS, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, Constants.TALON_UPDATE_PERIOD_MS, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_14_Turn_PIDF1, Constants.TALON_UPDATE_PERIOD_MS, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)

            config_kP(0, Constants.PID.TURN_KP, 0)
            config_kI(0, Constants.PID.TURN_KI, 0)
            config_kD(0, Constants.PID.TURN_KD, 0)
            config_kF(0, Constants.PID.TURN_KF, 0)

            config_kP(1, Constants.PID.FIXED_KP, 0)
            config_kI(1, Constants.PID.FIXED_KI, 0)
            config_kD(1, Constants.PID.FIXED_KD, 0)
            config_kF(1, Constants.PID.FIXED_KF, 0)

            config_IntegralZone(0, Constants.PID.TURN_IZONE, 0)
            configClosedLoopPeakOutput(0, Constants.PID.TURN_MAX_OUTPUT, 0)
            config_IntegralZone(1, Constants.PID.FIXED_IZONE, 0)
            configClosedLoopPeakOutput(1, Constants.PID.FIXED_MAX_OUTPUT, 0)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 0, 0)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 1, 0)
            configAuxPIDPolarity(!Constants.PID.INVERT_TURN_AUX_PIDF, 0)
            selectProfileSlot(0, 0)
            selectProfileSlot(1, 1)
            setSensorPhase(true)
        }
    }

    private fun configureForPosition() {
        brakeMode = true
        mLeftMaster.apply {
            configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, Constants.TALON_UPDATE_PERIOD_MS, 0)
            follow(mRightMaster, FollowerType.AuxOutput1)
            setSensorPhase(false)
            setInverted(false)
        }

        mRightMaster.apply {
            configRemoteFeedbackFilter(mLeftMaster.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor, 0, 0)
            configRemoteFeedbackFilter(mGyro.getDeviceID(), RemoteSensorSource.Pigeon_Yaw, 1, 0)
            configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, 0)
            configSensorTerm(SensorTerm.Sum1, FeedbackDevice.QuadEncoder, 0)
            configSelectedFeedbackSensor(FeedbackDevice.SensorSum, 0, 0)
            @Suppress("MagicNumber")
            configSelectedFeedbackCoefficient(0.5, 0, 0)
            configSelectedFeedbackCoefficient(
                (Constants.Units.TURN_UNITS_PER_ROTATION / Constants.Units.PIGEON_UNITS_PER_ROTATION).toDouble(), 1, 0)
            configSelectedFeedbackSensor(FeedbackDevice.RemoteSensor1, 1, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_12_Feedback1, Constants.TALON_UPDATE_PERIOD_MS, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, Constants.TALON_UPDATE_PERIOD_MS, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_14_Turn_PIDF1, Constants.TALON_UPDATE_PERIOD_MS, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, Constants.TALON_UPDATE_PERIOD_MS, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            config_kP(0, Constants.PID.POS_KP, 0)
            config_kI(0, Constants.PID.POS_KI, 0)
            config_kD(0, Constants.PID.POS_KD, 0)
            config_kF(0, Constants.PID.POS_KF, 0)
            config_kP(1, Constants.PID.ANGLE_KP, 0)
            config_kI(1, Constants.PID.ANGLE_KI, 0)
            config_kD(1, Constants.PID.ANGLE_KF, 0)
            config_kF(1, Constants.PID.ANGLE_KF, 0)
            config_IntegralZone(0, Constants.PID.POS_IZONE, 0)
            configClosedLoopPeakOutput(0, Constants.PID.POS_MAX_OUTPUT, 0)
            config_IntegralZone(1, Constants.PID.ANGLE_IZONE, 0)
            configClosedLoopPeakOutput(1, Constants.PID.ANGLE_MAX_OUTPUT, 0)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 0, 0)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 1, 0)
            selectProfileSlot(0, 0)
            selectProfileSlot(1, 1)
            configAuxPIDPolarity(!Constants.PID.INVERT_ANGLE_AUX_PIDF, 0)
            setSensorPhase(true)
        }
    }

    /**
    * laods the pid gains from the constants file and eventually the dashboard
    */
    @Suppress("LongMethod")
    public fun loadGains() {
        mPositionPIDConfig.kP = Constants.PID.POS_KP
        mPositionPIDConfig.kI = Constants.PID.POS_KI
        mPositionPIDConfig.kD = Constants.PID.POS_KD
        mPositionPIDConfig.kF = Constants.PID.POS_KF
        mPositionPIDConfig.integralZone = Constants.PID.POS_IZONE
        mPositionPIDConfig.closedLoopPeakOutput = Constants.PID.POS_MAX_OUTPUT

        mAnglePIDConfig.kP = Constants.PID.ANGLE_KP
        mAnglePIDConfig.kI = Constants.PID.ANGLE_KI
        mAnglePIDConfig.kD = Constants.PID.ANGLE_KD
        mAnglePIDConfig.kF = Constants.PID.ANGLE_KF
        mAnglePIDConfig.integralZone = Constants.PID.ANGLE_IZONE
        mAnglePIDConfig.closedLoopPeakOutput = Constants.PID.ANGLE_MAX_OUTPUT

        mVelocityPIDConfig.kP = Constants.PID.VEL_KP
        mVelocityPIDConfig.kI = Constants.PID.VEL_KI
        mVelocityPIDConfig.kD = Constants.PID.VEL_KD
        mVelocityPIDConfig.kF = Constants.PID.VEL_KF
        mVelocityPIDConfig.integralZone = Constants.PID.VEL_IZONE
        mVelocityPIDConfig.closedLoopPeakOutput = Constants.PID.VEL_MAX_OUTPUT

        mTurnPIDConfig.kP = Constants.PID.TURN_KP
        mTurnPIDConfig.kI = Constants.PID.TURN_KI
        mTurnPIDConfig.kD = Constants.PID.TURN_KD
        mTurnPIDConfig.kF = Constants.PID.TURN_KF
        mTurnPIDConfig.integralZone = Constants.PID.TURN_IZONE
        mTurnPIDConfig.closedLoopPeakOutput = Constants.PID.TURN_MAX_OUTPUT

        mFixedPIDConfig.kP = Constants.PID.FIXED_KP
        mFixedPIDConfig.kI = Constants.PID.FIXED_KI
        mFixedPIDConfig.kD = Constants.PID.FIXED_KD
        mFixedPIDConfig.kF = Constants.PID.FIXED_KF
        mFixedPIDConfig.integralZone = Constants.PID.FIXED_IZONE
        mFixedPIDConfig.closedLoopPeakOutput = Constants.PID.FIXED_MAX_OUTPUT
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
        loadGains()
    }
}
