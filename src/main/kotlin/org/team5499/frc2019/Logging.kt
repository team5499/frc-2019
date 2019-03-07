package org.team5499.frc2019

import org.tinylog.Logger
import edu.wpi.first.wpilibj.PowerDistributionPanel
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.RobotController

import org.team5499.frc2019.input.ControlBoard

import org.team5499.frc2019.subsystems.SubsystemsManager

@SuppressWarnings("MagicNumber", "TooManyFunctions")
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
        CODRIVER
    }

    @Suppress("ObjectPropertyNaming")
    private val LOGGING_TYPE = LoggingType.ITERATIVE
    @Suppress("ObjectPropertyNaming")
    private var LOGGING_STATE = LoggingState.PDP

    fun update(subsystems: SubsystemsManager, pdp: PowerDistributionPanel, controlBoard: ControlBoard) {
        when (LOGGING_TYPE) {
            LoggingType.ITERATIVE -> iterate(subsystems, pdp, controlBoard)
            LoggingType.DUMP -> dump(subsystems, pdp, controlBoard)
        }
        alwaysLog(subsystems, pdp)
    }

    fun alwaysLog(subsystems: SubsystemsManager, pdp: PowerDistributionPanel, controlBoard: ControlBoard) {
    }

    private fun iterate(subsystems: SubsystemsManager, pdp: PowerDistributionPanel, controlBoard: ControlBoard) {
        LOGGING_STATE = when (LOGGING_STATE) {
            LoggingState.PDP -> { logPDP(pdp); LoggingState.PDP_CHANNELS }
            LoggingState.PDP_CHANNELS -> { logPDPChannels(pdp); LoggingState.ROBOT_CONTROLLER }
            LoggingState.ROBOT_CONTROLLER -> { logRobotController(); LoggingState.DRIVER_STATION }
            LoggingState.DRIVER_STATION -> { logDriverStation(); LoggingState.DRIVETRAIN }
            LoggingState.DRIVETRAIN -> { logDrivetrain(subsystems); LoggingState.DRIVER }
            LoggingState.DRIVER -> { logDriver(controlBoard); LoggingState.CODRIVER }
            LoggingState.CODRIVER -> { logCodriver(controlBoard); LoggingState.PDP }
        }
    }

    private fun dump(subsystems: SubsystemsManager, pdp: PowerDistributionPanel, controlBoard: ControlBoard) {
        logPDP(pdp)
        logPDPChannels(pdp)
        logRobotController()
        logDriverStation()
        logDrivetrain(subsystems)
        logDriver(controlBoard)
        logCodriver(controlBoard)
    }

    private fun logPDP(pdp: PowerDistributionPanel) {
        Logger.tag("TOTAL_CURRENT").trace(pdp.totalCurrent)
        Logger.tag("TOTAL_VOLTAGE").trace(pdp.voltage)
        Logger.tag("TOTAL_POWER").trace(pdp.totalPower)
    }

    private fun logPDPChannels(pdp: PowerDistributionPanel) {
        Logger.tag("PDP0").trace(pdp.getCurrent(0))
        Logger.tag("PDP1").trace(pdp.getCurrent(1))
        Logger.tag("PDP2").trace(pdp.getCurrent(2))
        Logger.tag("PDP3").trace(pdp.getCurrent(3))
        Logger.tag("PDP4").trace(pdp.getCurrent(4))
        Logger.tag("PDP5").trace(pdp.getCurrent(5))
        Logger.tag("PDP6").trace(pdp.getCurrent(6))
        Logger.tag("PDP7").trace(pdp.getCurrent(7))
        Logger.tag("PDP8").trace(pdp.getCurrent(8))
        Logger.tag("PDP9").trace(pdp.getCurrent(9))
        Logger.tag("PDP10").trace(pdp.getCurrent(10))
        Logger.tag("PDP11").trace(pdp.getCurrent(11))
        Logger.tag("PDP12").trace(pdp.getCurrent(12))
        Logger.tag("PDP13").trace(pdp.getCurrent(13))
        Logger.tag("PDP14").trace(pdp.getCurrent(14))
        Logger.tag("PDP15").trace(pdp.getCurrent(15))
    }

    private fun logRobotController() {
        Logger.tag("BROWNED_OUT").trace(RobotController.isBrownedOut())
        Logger.tag("CAN_USAGE").trace(RobotController.getCANStatus().percentBusUtilization)
        Logger.tag("SYS_ACTIVE").trace(RobotController.isSysActive())
    }

    private fun logDriverStation() {
        Logger.tag("ALLIANCE").trace(DriverStation.getInstance().alliance)
        Logger.tag("DS_ATTACHED").trace(DriverStation.getInstance().isDSAttached())
        Logger.tag("ENABLED").trace(DriverStation.getInstance().isEnabled())
        Logger.tag("EVENT_NAME").trace(DriverStation.getInstance().eventName as Any)
        Logger.tag("FMS_ATTACHED").trace(DriverStation.getInstance().isFMSAttached())
        Logger.tag("GAME_MESSAGE").trace(DriverStation.getInstance().gameSpecificMessage as Any)
        Logger.tag("DRIVER_LOCATION").trace(DriverStation.getInstance().location)
        Logger.tag("MATCH_NUMBER").trace(DriverStation.getInstance().matchNumber)
        Logger.tag("MATCH_TIME").trace(DriverStation.getInstance().matchTime)
        Logger.tag("MATCH_TYPE").trace(DriverStation.getInstance().matchType)
    }

    private fun logDrivetrain(subsystems: SubsystemsManager) {
        Logger.tag("POSITION").trace(subsystems.drivetrain.pose.toString() as Any)
        Logger.tag("LVEL").trace(subsystems.drivetrain.leftVelocity.toString() as Any)
        Logger.tag("RVEL").trace(subsystems.drivetrain.rightVelocity.toString() as Any)
        Logger.tag("LVEL_ERR").trace(subsystems.drivetrain.leftVelocityError.toString() as Any)
        Logger.tag("RVEL_ERR").trace(subsystems.drivetrain.leftVelocityError.toString() as Any)
    }

    private fun logDriver(controlBoard: ControlBoard) {
        Logger.tag("DRIVER_STATE").trace(controlBoard.driverControls.getState() as Any)
    }

    private fun logCodriver(controlBoard: ControlBoard) {
        Logger.tag("CODRIVER_STATE").trace(controlBoard.codriverControls.getState() as Any)
    }
}
