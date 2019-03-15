package org.team5499.frc2019

import org.tinylog.Logger
import edu.wpi.first.wpilibj.PowerDistributionPanel
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.RobotController

import org.team5499.monkeyLib.hardware.LazyTalonSRX
import org.team5499.monkeyLib.hardware.LazyVictorSPX

import org.team5499.frc2019.input.ControlBoard

import org.team5499.frc2019.subsystems.SubsystemsManager

import java.util.concurrent.LinkedBlockingDeque

@SuppressWarnings("MagicNumber", "TooManyFunctions", "LongParameterList")
object Logging {
    enum class LoggingType {
        ITERATIVE, // iterate through each different set of data(faster)
        DUMP // dump all the data each update loop
    }

    enum class LoggingState {
        PDP,
        PDP_CHANNELS,
        ROBOT_CONTROLLER,
        DRIVER_STATION,
        DRIVETRAIN,
        DRIVER,
        CODRIVER,
        OTHER_SUBSYSTEMS
    }

    @Suppress("ObjectPropertyNaming")
    private val LOGGING_TYPE = LoggingType.ITERATIVE
    @Suppress("ObjectPropertyNaming")
    private var LOGGING_STATE = LoggingState.PDP

    private val logQueue = LinkedBlockingDeque<Pair<String, Any>>(2000)
    private val logValues = HashMap<String, Any>()
    private val loggingThread: Thread

    init {
        loggingThread = Thread(LoggingThread())
        loggingThread.start()
    }

    fun update(
        subsystems: SubsystemsManager,
        pdp: PowerDistributionPanel,
        controlBoard: ControlBoard,
        leftMaster: LazyTalonSRX,
        leftSlave1: LazyVictorSPX,
        leftSlave2: LazyVictorSPX,
        rightMaster: LazyTalonSRX,
        rightSlave1: LazyVictorSPX,
        rightSlave2: LazyVictorSPX,
        elevatorMaster: LazyTalonSRX,
        elevatorSlave: LazyTalonSRX,
        intake: LazyTalonSRX,
        hatch: LazyTalonSRX
    ) {
        when (LOGGING_TYPE) {
            LoggingType.ITERATIVE -> { iterate(subsystems,
                pdp,
                controlBoard,
                leftMaster,
                leftSlave1,
                leftSlave2,
                rightMaster,
                rightSlave1,
                rightSlave2,
                elevatorMaster,
                elevatorSlave,
                intake,
                hatch)
                iterate(subsystems,
                pdp,
                controlBoard,
                leftMaster,
                leftSlave1,
                leftSlave2,
                rightMaster,
                rightSlave1,
                rightSlave2,
                elevatorMaster,
                elevatorSlave,
                intake,
                hatch) }
            LoggingType.DUMP -> dump(subsystems,
                pdp,
                controlBoard,
                leftMaster,
                leftSlave1,
                leftSlave2,
                rightMaster,
                rightSlave1,
                rightSlave2,
                elevatorMaster,
                elevatorSlave,
                intake,
                hatch)
        }
        alwaysLog(subsystems, pdp, controlBoard)
    }

    fun alwaysLog(subsystems: SubsystemsManager, pdp: PowerDistributionPanel, controlBoard: ControlBoard) {
    }

    private fun iterate(
        subsystems: SubsystemsManager,
        pdp: PowerDistributionPanel,
        controlBoard: ControlBoard,
        leftMaster: LazyTalonSRX,
        leftSlave1: LazyVictorSPX,
        leftSlave2: LazyVictorSPX,
        rightMaster: LazyTalonSRX,
        rightSlave1: LazyVictorSPX,
        rightSlave2: LazyVictorSPX,
        elevatorMaster: LazyTalonSRX,
        elevatorSlave: LazyTalonSRX,
        intake: LazyTalonSRX,
        hatch: LazyTalonSRX
    ) {
        LOGGING_STATE = when (LOGGING_STATE) {
            LoggingState.PDP -> { logPDP(pdp); LoggingState.PDP_CHANNELS }
            LoggingState.PDP_CHANNELS -> { logVoltages(pdp,
                                                        leftMaster,
                                                        leftSlave1,
                                                        leftSlave2,
                                                        rightMaster,
                                                        rightSlave1,
                                                        rightSlave2,
                                                        elevatorMaster,
                                                        elevatorSlave,
                                                        intake,
                                                        hatch); LoggingState.ROBOT_CONTROLLER }
            LoggingState.ROBOT_CONTROLLER -> { logRobotController(); LoggingState.DRIVER_STATION }
            LoggingState.DRIVER_STATION -> { logDriverStation(); LoggingState.DRIVETRAIN }
            LoggingState.DRIVETRAIN -> { logDrivetrain(subsystems); LoggingState.DRIVER }
            LoggingState.DRIVER -> { logDriver(controlBoard); LoggingState.CODRIVER }
            LoggingState.CODRIVER -> { logCodriver(controlBoard); LoggingState.OTHER_SUBSYSTEMS }
            LoggingState.OTHER_SUBSYSTEMS -> { logOtherSubsystems(subsystems); LoggingState.PDP }
        }
    }

    private fun dump(
        subsystems: SubsystemsManager,
        pdp: PowerDistributionPanel,
        controlBoard: ControlBoard,
        leftMaster: LazyTalonSRX,
        leftSlave1: LazyVictorSPX,
        leftSlave2: LazyVictorSPX,
        rightMaster: LazyTalonSRX,
        rightSlave1: LazyVictorSPX,
        rightSlave2: LazyVictorSPX,
        elevatorMaster: LazyTalonSRX,
        elevatorSlave: LazyTalonSRX,
        intake: LazyTalonSRX,
        hatch: LazyTalonSRX
    ) {
        logPDP(pdp)
        logVoltages(pdp,
                    leftMaster,
                    leftSlave1,
                    leftSlave2,
                    rightMaster,
                    rightSlave1,
                    rightSlave2,
                    elevatorMaster,
                    elevatorSlave,
                    intake,
                    hatch)
        logRobotController()
        logDriverStation()
        logDrivetrain(subsystems)
        logDriver(controlBoard)
        logCodriver(controlBoard)
        logOtherSubsystems(subsystems)
    }

    private fun logPDP(pdp: PowerDistributionPanel) {
        log("TOTAL_CURRENT", pdp.totalCurrent)
        log("TOTAL_VOLTAGE", pdp.voltage)
    }

    private fun logVoltages(
        pdp: PowerDistributionPanel,
        leftMaster: LazyTalonSRX,
        leftSlave1: LazyVictorSPX,
        leftSlave2: LazyVictorSPX,
        rightMaster: LazyTalonSRX,
        rightSlave1: LazyVictorSPX,
        rightSlave2: LazyVictorSPX,
        elevatorMaster: LazyTalonSRX,
        elevatorSlave: LazyTalonSRX,
        intake: LazyTalonSRX,
        hatch: LazyTalonSRX
    ) {
        val volts = StringBuilder()
        val voltsString = volts.append("LEFT_MASTER_VOLTAGE: ${leftMaster.motorOutputVoltage} "
        ).append("LEFT_SLAVE1_VOLTAGE: ${leftSlave1.motorOutputVoltage} "
        ).append("LEFT_SLAVE2_VOLTAGE: ${leftSlave2.motorOutputVoltage} "
        ).append("RIGHT_MASTER_VOLTAGE: ${rightMaster.motorOutputVoltage} "
        ).append("RIGHT_SLAVE1_VOLTAGE: ${rightSlave1.motorOutputVoltage} "
        ).append("RIGHT_SLAVE2_VOLTAGE: ${rightSlave2.motorOutputVoltage} "
        ).append("ELEVATOR_MASTER_VOLTAGE: ${elevatorMaster.motorOutputVoltage} "
        ).append("ELEVATOR_SLAVE_VOLTAGE: ${elevatorSlave.motorOutputVoltage} "
        ).append("INTAKE_VOLTAGE: ${intake.motorOutputVoltage} "
        ).append("HATCH_VOLTAGE: ${hatch.motorOutputVoltage}"
        ).toString()
        log("VOLTAGE", voltsString)

        val amps = StringBuilder()
        val ampsString = amps.append("PDP0: ${pdp.getCurrent(0)} "
        ).append("PDP1: ${pdp.getCurrent(1)} "
        ).append("PDP2: ${pdp.getCurrent(2)} "
        ).append("PDP3: ${pdp.getCurrent(3)} "
        ).append("PDP4: ${pdp.getCurrent(4)} "
        ).append("PDP5: ${pdp.getCurrent(5)} "
        ).append("PDP6: ${pdp.getCurrent(6)} "
        ).append("PDP7: ${pdp.getCurrent(7)} "
        ).append("PDP8: ${pdp.getCurrent(8)} "
        ).append("PDP9: ${pdp.getCurrent(9)} "
        ).append("PDP10: ${pdp.getCurrent(10)} "
        ).append("PDP11: ${pdp.getCurrent(11)} "
        ).append("PDP12: ${pdp.getCurrent(12)} "
        ).append("PDP13: ${pdp.getCurrent(13)} "
        ).append("PDP14: ${pdp.getCurrent(14)} "
        ).append("PDP15: ${pdp.getCurrent(15)} "
        ).toString()
        log("AMPERAGE", ampsString)
    }

    private fun logRobotController() {
        log("BROWNED_OUT", RobotController.isBrownedOut())
        log("CAN_USAGE", RobotController.getCANStatus().percentBusUtilization)
        log("SYS_ACTIVE", RobotController.isSysActive())
    }

    private fun logDriverStation() {
        log("ALLIANCE", DriverStation.getInstance().alliance)
        log("DS_ATTACHED", DriverStation.getInstance().isDSAttached())
        log("ENABLED", DriverStation.getInstance().isEnabled())
        log("EVENT_NAME", DriverStation.getInstance().eventName)
        log("FMS_ATTACHED", DriverStation.getInstance().isFMSAttached())
        log("GAME_MESSAGE", DriverStation.getInstance().gameSpecificMessage)
        log("DRIVER_LOCATION", DriverStation.getInstance().location)
        log("MATCH_NUMBER", DriverStation.getInstance().matchNumber)
        log("MATCH_TIME", DriverStation.getInstance().matchTime)
        log("MATCH_TYPE", DriverStation.getInstance().matchType)
    }

    private fun logDrivetrain(subsystems: SubsystemsManager) {
        log("POSITION", subsystems.drivetrain.pose.toString())
        log("LVEL", subsystems.drivetrain.leftVelocity.toString())
        log("RVEL", subsystems.drivetrain.rightVelocity.toString())
        log("LVEL_ERR", subsystems.drivetrain.leftVelocityError.toString())
        log("RVEL_ERR", subsystems.drivetrain.leftVelocityError.toString())
    }

    private fun logOtherSubsystems(subsystems: SubsystemsManager) {
        log("ELEVATOR_HEIGHT", subsystems.lift.secondStagePositionInches)
        log("ELEVATOR_VELOCITY", subsystems.lift.secondStageVelocityInchesPerSecond)
        log("HATCH_POSITION", subsystems.hatchMech.selectedPosition)
        log("HATCH_VALUE", subsystems.hatchMech.positionRaw)
    }

    private fun logDriver(controlBoard: ControlBoard) {
        log("DRIVER_STATE", controlBoard.driverControls.getState())
    }

    private fun logCodriver(controlBoard: ControlBoard) {
        log("CODRIVER_STATE", controlBoard.codriverControls.getState())
    }

    private fun log(tag: String, value: Any): Boolean {
        if (logValues.get(tag) != value) {
            logValues.put(tag, value)
            return logQueue.offer(Pair(tag, value))
        } else {
            return true
        }
    }

    class LoggingThread : Runnable {
        override fun run() {
            Thread.currentThread().priority = 1
            while (true) {
                try {
                    val log = logQueue.take()
                    Logger.tag(log.first).trace(log.second)
                } catch (ie: InterruptedException) {
                    println("Logger thread stopped!")
                    Thread.currentThread().interrupt()
                    return
                }
            }
        }
    }
}
