package com.PhantomMentalists.Stronghold;

import com.PhantomMentalists.Stronghold.Parameters;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

/**
 * <p>Author: Ryan & Austin</p>
 */
@objid ("a38fcf5c-bc25-4155-8afc-05a8335fee44")
public class PusherArm
{
    /**
     * Setpoint for the pusher arm tilt.  It is measured in "ticks" per revolution (typically 4096 per revolution of the motor). 
     * While in distance control mode, the PID Controller will hold the motor as the desired number of ticks.
     */
    @objid ("df266bd7-9e98-431b-9e8c-715f107364dc")
    public double tiltSetpoint;

    @objid ("89ce3a76-eda0-444f-be92-555662aedc38")
    public PusherArmPositions positionSetpoint;

    /**
     * <Enter note text here>
     */
    @objid ("4db7315e-daa2-48ed-a50e-6c2ebb928eab")
    protected CANTalon tiltMotor;

    @objid ("ef54315f-71a9-48b1-8840-4441e6e9db2c")
    public PusherArm()
    {
    	tiltMotor = new CANTalon(0);
		tiltMotor.changeControlMode(TalonControlMode.PercentVbus);
		tiltMotor.enableBrakeMode(true);
		tiltMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		tiltMotor.setPID(0, 0, 0);
		tiltMotor.enable();
    }

    /**
     * This method powers the tilt motor.  This method can only be used when autopilot is disabled and is intended for troubleshooting and debugging.
     * 
     * @param power Power to apply to the tilt motor as a percentage in the range of -1.0 .. 0 1.0 where -1.0 is 100% reverse and 1.0 is 100% forward.
     */
    @objid ("59a12327-d8c3-4b2c-8521-285b79fef074")
    public void manualSetTiltPower(double power)
    {
    	tiltMotor.set(power);
    }

    /**
     * This method returns if the pusher arm is in a known position.
     * 
     * The pusher arm uses an encoder to count ticks as it moves.
     * When the robot is initially powered on, the pusher arm tilt angle is in an unknown state until it reaches a limit switch at a known position.
     * If the motor is ever shut off for an over current condition it is reset to an unknown position (since that should never happen).
     * 
     * @return true if pusher arm tilt angle has reached the home limit switch since being powered on, false otherwise.
     */
    @objid ("aa54b10a-1ca9-498f-8cb8-506906f9bd51")
    public boolean isKnownPosition()
    {
    	return false;
    }

    @objid ("500c5ab5-ea57-4da4-be48-8df56fbced03")
    public double getTiltSetpoint()
    {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.tiltSetpoint;
    }

    @objid ("6aaf28b4-2c3f-410c-ba0f-d31a7634ff58")
    public void setTiltSetpoint(double value)
    {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.tiltSetpoint = value;
    }

    @objid ("c189a01d-9bbd-4dfe-8412-5b13d3a439f1")
    public PusherArmPositions getPositionSetpoint()
    {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.positionSetpoint;
    }

    @objid ("3ed02eae-49ef-4d5d-a4e5-015002f2bad0")
    public void setPositionSetpoint(PusherArmPositions value)
    {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.positionSetpoint = value;
    }

    /**
     * <Enter note text here>
     */
    @objid ("ec445282-355a-4c32-847c-3f12cc077733")
    public boolean isPositionAtSetpoint()
    {
    	return false;
    }

    @objid ("59979124-6f97-4353-a791-92fc6ed425b0")
    public enum PusherArmPositions
    {
        kUnknown,
        kHome,
        kLowBar;
    }

}