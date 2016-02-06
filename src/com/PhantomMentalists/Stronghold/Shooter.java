package com.PhantomMentalists.Stronghold;

import com.PhantomMentalists.Stronghold.Parameters;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * This class encapsulates the state (attributes) and logic (methods) required to control Telepath's shooter to score points by hurling a boulder into the high goal.
 * 
 * @author Nazhere and Moose
 */
@objid ("e7c0967b-bd2b-41d7-a528-2d3f463133d3")
public class Shooter {
    /**
     * This attribute stores the angle the shooter will move to.
     * 
     * This method is used when the Shooter is in autopilot control.  It sets the position as the number of ticks (typically 4096 ticks per revolution) from the home position.  The Talon will use distance control to hold the Shooter angle at the specified position.
     */
    @objid ("2cba802d-0e00-4a6f-a589-c6ec5069db6f")
    protected double tiltSetpoint;

    @objid ("4ede4b6a-ff63-46f7-9714-da4101e49e75")
    public Solenoid Kicker;

    /**
     * <Enter note text here>
     */
    @objid ("63f2413a-d988-445a-ace3-67d34b261a43")
    protected DigitalInput ballSensor;

    /**
     * <Enter note text here>
     */
    @objid ("89cdc41c-1864-4459-8d64-5f9914675c79")
    protected CANTalon leftPitchingMotor;

    /**
     * <Enter note text here>
     */
    @objid ("3149120a-5cf8-4902-bee1-1e15cfb9d14b")
    protected CANTalon rightPitchingMotor;

    @objid ("ca52f5d5-e895-4f2b-b8eb-cf991ceae8ff")
    protected CANTalon tiltMotor;

    @objid ("c541cb16-62b1-4934-bb7c-a37d4bd8f851")
    protected Solenoid ballShooter;

    @objid ("e8c46368-8549-4701-acd1-6a7a1b073c83")
    public Shooter() {
    }

    @objid ("91e9f52a-d664-4f68-ba8b-4e5503fe8eda")
    public boolean isBallLoaded() {
        return false;
    }

    /**
     * This method powers both of the two picthing matchine motors.  
     * 
     * This method intended for use during autopilot when the two Talon motor controllers are in speed control mode.  It provides a setpoint in counts per 100 milisecond.  Both motors must spin their respective wheels in oposite directions.
     * 
     * @param power Power to apply to the tilt motor as a percentage in counts per 100 miliseconds where negative numbers run the motors in reverse and positive numbers run the motors forward
     */
    @objid ("f1761ce3-8d39-43f8-9b3c-6adaedaec7ad")
    public void setShootAngle(ShooterPosition shooterPosition) {
    }

    /**
     * This method powers both of the two picthing matchine motors.  
     * 
     * This method can only be used when autopilot is disabled and is intended for troubleshooting and debugging.  Both motors must spin their respective wheels in oposite directions.
     * 
     * @param power Power to apply to the tilt motor as a percentage in the range of -1.0 .. 0 1.0 where -1.0 is 100% reverse and 1.0 is 100% forward.
     */
    @objid ("f0ee0c60-695b-452c-b734-44653ea8b4fd")
    public void manualRunPichingMachine(double power) {
    }

    /**
     * This method powers the tilt motor.  This method can only be used when autopilot is disabled and is intended for troubleshooting and debugging.
     * 
     * @param power Power to apply to the tilt motor as a percentage in the range of -1.0 .. 0 1.0 where -1.0 is 100% reverse and 1.0 is 100% forward.
     */
    @objid ("eba4d1df-2aa2-4f39-b3a7-f0ca4f9b6626")
    public void manualShooterTiltPower(double power) {
    }

    @objid ("17dd7815-19f9-4e9f-80bb-74ef735538d4")
    public boolean isUpToSpeed() {
        return false;
    }

    /**
     * This method enables or disables the pneumatic ball pusher solenoid.
     * 
     * @param push True extends the penumatic ball thumper, false retracts it
     */
    @objid ("e0334a6b-b527-4a95-936a-51e68527f3ea")
    public void pushBall(boolean push) {
    }

    @objid ("bf56ebbc-3451-4f51-88e0-c944b48b9819")
    public double getTiltSetpoint() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.tiltSetpoint;
    }

    /**
     * <Enter note text here>
     */
    @objid ("89bd53b8-23c9-4a16-adb2-95ec7d912696")
    public void setTiltSetpoint(double value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.tiltSetpoint = value;
    }

    /**
     */
    @objid ("f6797d96-b58a-47ee-a41a-4ea7709c5062")
    public boolean isPitchingMachineOn() {
    }

    /**
     * This method returns if the shooter is in a known position.
     * 
     * The shooter uses an encoder to count ticks as it moves.  When the robot is initially powered on, the shooter angle is in an unknown state until it reaches a limit switch at a known position.  If the motor is ever shutoff for an over current condition it is reset to an unknown position (since that should never happen).
     * 
     * @return true if shooter tilt angle has reached the home limit switch since being powered on, false otherwise.
     */
    @objid ("2a8ebf57-a97b-499d-af56-80d785a4ffde")
    public boolean isKnownPosition() {
    }

    @objid ("08844018-94d8-4017-af1b-f834faa0871b")
    public enum ShooterPosition {
        kHome,
        kUnknown,
        kShoot,
        kLowBar;
    }

}
