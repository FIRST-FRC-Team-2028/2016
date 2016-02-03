package com.PhantomMentalists.Stronghold;

import com.PhantomMentalists.Stronghold.Parameters;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * DriveUnit encapsulates all the hardware that makes one side of the robot drive.  There will always be two instances of DriveUnit, one controlling the left side and one controlling the right side.  Each DriveUnit has two Talon Motor Controllers, configured as master/follower as well as a Solenoid to change gears in the gearbox.  Each gearbox has a quadrature encoder that measures the speed of the gearbox which can be used to give the DriveUnit's motors a speed setpoint (as opposed to a percentage of bus voltage output).
 * 
 * @author Diego
 */
@objid ("69d74b75-df80-4243-808f-74bb65f3aa0a")
public class DriveUnit {
    /**
     * speedSetpoint is the speed the drive motors should be running in the range of -1.0 .. 0 .. 1.0 where zero is stopped, negative numbers are reverse (up to 100% at -1.0) and positive numbers are forward (up to 100% at 1.0).
     */
    @objid ("24edc69c-f818-4b29-93f7-f288887f5a0c")
    protected double speedSetpoint;

    /**
     * This attribute identifies where the DriveUnit is mounted.  It is used to format telemetry values for the SmartDashboard.
     */
    @objid ("7303330e-6bf8-4f1f-bb7e-b8ad6117e9ca")
    protected final Placement placement;

    /**
     * This attribute stores the gear the DriveUnit's gearbox should be in.
     */
    @objid ("68cc68bf-030b-4f27-aefb-dc90d58f4c1f")
    protected Gear gear;

    /**
     * Master Talon Speed Controller.  This device controls the output voltage either in speed control mode (i.e., getting a setpoint and using the quadrature encoder to maintain that speed) or voltage control (i.e., setting the output to a percentage of the bus voltage).
     */
    @objid ("8ebf763a-4c0e-4724-936a-e4bfc59091f5")
    protected CANTalon masterMotor;

    /**
     * Motor controller for the second motor on the gearbox.  This motor controller will be set in follower mode and always output the same as the master motor controller.
     */
    @objid ("f561482b-0252-461f-84e6-29dc422cce48")
    protected CANTalon followerMotor;

    /**
     * Controls the hardware that sets the gearbox to high gear.
     */
    @objid ("0fb4ba63-1470-4131-a627-451ab5778848")
    protected Solenoid highGear;

    /**
     * Controls the hardware that sets the gearbox to low gear.
     */
    @objid ("21ea24e8-f7b6-4d61-8baa-d4e1f3550a1d")
    protected Solenoid lowGear;

    /**
     * DriveUnit constructor.  This method initializes all attributes of the DriveUnit to a known state.  It instantiates all hardware attributes (both Talon Motor Controllers and the gear Solenoid) using values from Parameters.  it sets the master Talon Motor Controller in Voltage control mode and sets the follower Talon Motor Controller in follower mode.  It sets the gearbox to low gear.
     * 
     * @param placement - Identifies if this DriveUnit is mounted on the left or right side of the robot.
     */
    @objid ("72aba1e2-59da-49cd-914f-a0aba060f665")
    public DriveUnit(Placement placement) {
    }

    /**
     * Returns the setpoint.
     * 
     * @returns double - setpoint of the drive motor controller as a percentage in the range -1.0 .. 0 .. 1.0 where negative values indicate reverse and positive values indicate forward.  
     */
    @objid ("5dad8b50-8973-4186-80f9-5dd883b14e9d")
    public double getSpeedSetpoint() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.speedSetpoint;
    }

    /**
     * Sets the motor controller setpoint.
     * 
     * @param value - Percentage of the motor's output in the range -1.0 .. 0 .. 1.0 where negative values indicate reverse and positive values indicate forward.
     */
    @objid ("6b1aafd1-f349-430d-8d23-f281df820075")
    public void setSpeedSetpoint(double value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.speedSetpoint = value;
    }

    /**
     * This method is called on every iteration through the main loop.  This method is responsible for updating the internal state of the DriveUnit based on it's attributes (which may have been changed since the last call to process().  It changes Talon Motor Controller setpoints, sends telemetry values to the Smart Dashboard, and checks to see if either Motor controllers are exceeding their current threshold (and automatically downshifts the gearbox to low gear if they are).
     */
    @objid ("de5d82d9-c9f3-4739-b5a8-eeb4984e075c")
    public void process() {
    }

    /**
     * Queries if the masterMotor is in speed control mode
     * 
     * @returns boolean - true if Talon Speed Controller is in speed control mode, false otherwise.
     */
    @objid ("ff98472c-d4a4-4aa7-815a-ae058c161375")
    public boolean isSpeedControlEnabled() {
    }

    /**
     * Enables or disables the Talon Motor Controller's speed control.  Note:  This method must check the current state of the masterMotor's speed control setting before changing it.
     * 
     * @param speedControlEnabled - true enables speed control (if it is not in speed control mode), false enables voltage control (only if it is not in voltage control mode already).
     */
    @objid ("c990d0bb-d6b3-4333-a041-3a38b70461a9")
    public void setSpeedControl(boolean speedControlEnabled) {
    }

    /**
     * Returns the placement
     * 
     * @returns Placement - The location of the DriveUnit
     */
    @objid ("9f636d22-f55f-43bf-b998-1f88f80adbc2")
    public Placement getPlacement() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.placement;
    }

    /**
     * Sets the gearbox gear.
     * 
     * @param value - the gear (Low or High) to set the gearbox to.
     */
    @objid ("07a16abf-c1bb-4ebe-a0a0-9cee9c2b7b66")
    public void setGear(Gear value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.gear = value;
    }

    @objid ("e4914053-d1c5-48d3-9a8a-aeefb3c7c402")
    public Gear getGear() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.gear;
    }

    @objid ("b87fda8c-c9ff-4309-987b-05d2794dfab0")
    public enum Placement {
        Left,
        Right;
    }

    /**
     * <Enter note text here>
     */
    @objid ("d18d0297-82a7-46fd-b0cf-1bda39f0d7f9")
    public enum Gear {
        kLowGear,
        kHighGear;
    }

}
