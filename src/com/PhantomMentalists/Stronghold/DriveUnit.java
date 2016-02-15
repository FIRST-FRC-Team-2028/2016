package com.PhantomMentalists.Stronghold;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;

/**
 * DriveUnit encapsulates all the hardware that makes one side of the 
 * robot drive. There will always be two instances of DriveUnit, one 
 * controlling the left side and one controlling the right side. 
 * Each DriveUnit has two Talon Motor Controllers, configured as 
 * master/follower as well as a Solenoid to change gears in the gearbox. 
 * Each gearbox has a quadrature encoder that measures the speed of the 
 * gearbox which can be used to give the DriveUnit's motors a speed setpoint 
 * (as opposed to a percentage of bus voltage output).
 * 
 * @author Diego
 */
@objid ("69d74b75-df80-4243-808f-74bb65f3aa0a")
public class DriveUnit {
    /**
     * speedSetpoint is the speed the drive motors should be running in the 
     * range of -1.0 .. 0 .. 1.0 where zero is stopped, negative numbers are
     * reverse (up to 100% at -1.0) and positive numbers are forward (up to 
     * 100% at 1.0).
     */
    @objid ("24edc69c-f818-4b29-93f7-f288887f5a0c")
    protected double speedSetpoint;

    /**
     * 
     */
    protected double turnSetpoint;
    
    /**
     * This attribute identifies where the DriveUnit is mounted.  
     * It is used to format telemetry values for the SmartDashboard.
     */
    @objid ("7303330e-6bf8-4f1f-bb7e-b8ad6117e9ca")
    protected final Placement placement;

    /**
     * Master Talon Speed Controller.  This device controls the output 
     * voltage either in speed control mode (i.e., getting a setpoint 
     * and using the quadrature encoder to maintain that speed) or voltage 
     * control (i.e., setting the output to a percentage of the bus voltage).
     */
    @objid ("93e44ffb-90ea-4f63-9dda-bf5923393a19")
    protected CANTalon masterMotor;

    /**
     * Motor controller for the second motor on the gearbox.  This 
     * motor controller will be set in follower mode and always output 
     * the same as the master motor controller.
     */
    @objid ("4e8a45ac-4494-4cb2-9042-fed3e7f97224")
    protected CANTalon followerMotor;

    /**
     * DriveUnit constructor.  This method initializes all attributes 
     * of the DriveUnit to a known state.  It instantiates all hardware 
     * attributes (both Talon Motor Controllers and the gear Solenoid) using 
     * values from Parameters.  it sets the master Talon Motor Controller 
     * in Voltage control mode and sets the follower Talon Motor Controller 
     * in follower mode.  It sets the gearbox to low gear.
     * @param placement - Identifies if this DriveUnit is mounted on the 
     * left or right side of the robot.
     */
    @objid ("72aba1e2-59da-49cd-914f-a0aba060f665")
    public DriveUnit(Placement placements) 
    {
        placement = placements;
        if (placement == Placement.Right) 
        {
        	masterMotor = new CANTalon(Parameters.kRightMasterDriveMotorCanId);
        	followerMotor = new CANTalon(Parameters.kRightFollowerDriveMotorCanId);
        	followerMotor.changeControlMode(CANTalon.ControlMode.Follower);
        	followerMotor.set(Parameters.kRightMasterDriveMotorCanId);
        }
        else if (placement == Placement.Left)
        {
        	masterMotor = new CANTalon(Parameters.kLeftMasterDriveMotorCanId);
        	followerMotor = new CANTalon(Parameters.kLeftFollowerDriveMotorCanId);
        	followerMotor.changeControlMode(CANTalon.ControlMode.Follower);
        	followerMotor.set(Parameters.kLeftMasterDriveMotorCanId);
        	
        }
        masterMotor.changeControlMode(CANTalon.ControlMode.PercentVbus);
    	masterMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	turnSetpoint = 0.0;
    	speedSetpoint = 0.0;
    }

    /**
     * Returns the speed setpoint.
     * 
     * @returns double - setpoint of the drive motor controller as a 
     *                   percentage in the range -1.0 .. 0 .. 1.0 where negative values 
     *                   indicate reverse and positive values indicate forward.
     */
    @objid ("5dad8b50-8973-4186-80f9-5dd883b14e9d")
    public double getSpeedSetpoint() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.speedSetpoint;
    }

    /**
     * Sets the motor controller speed setpoint.
     * 
     * @param value - Percentage of the motor's output in the range 
     *                -1.0 .. 0 .. 1.0 where negative values indicate reverse and 
     *                positive values indicate forward.
     */
    @objid ("6b1aafd1-f349-430d-8d23-f281df820075")
    public void setSpeedSetpoint(double value) {
        // Automatically generated method. Please delete this comment before entering specific code.
    	if (getSpeedSetpoint() != value)
    	{
    		this.speedSetpoint = value;
    	}
        
    }

    /**
     * This method is called on every iteration through the main loop.  
     * This method is responsible for updating the internal state of the 
     * DriveUnit based on it's attributes (which may have been changed since 
     * the last call to process().  It changes Talon Motor Controller 
     * setpoints, sends telemetry values to the Smart Dashboard.
     */
    @objid ("de5d82d9-c9f3-4739-b5a8-eeb4984e075c")
    public void process() 
    {
    	// Set the power to the master motor.  NOTE:  We need to calculate
    	// the power based on the speed setpoint modified by the turn setpoint.
    	// If we're driving forward and turning, the inside wheels must
    	// turn slower than the outside wheels.  If our speed setpoint is zero
    	// and we have a non-zero turn setpoint (i.e., we're spinning in place)
    	// we want the inside wheels to run in reverse and the outside wheels to
    	// run forward.
		double setpoint = speedSetpoint;
		double maxVelocity = Parameters.kMaxVelocity;
		if (turnSetpoint != 0.0) 
		{
			// We are turning
			if (speedSetpoint == 0.0)
			{
				// Priority #1
				// We're spinning in place
				if (turnSetpoint < 0.0)
				{
					// We're turning to the left
					if (placement == Placement.Left) 
					{
						setpoint = -(maxVelocity * turnSetpoint);
					}
					else
					{
						setpoint = maxVelocity * turnSetpoint;
					}
									
				}
				else
				{
					// We're turning to the right
					if (placement == Placement.Right) 
					{
						setpoint = -(maxVelocity * turnSetpoint);
					} 
					else
					{
						setpoint = maxVelocity * turnSetpoint;
					}
									
				}
			}
			else
			{
				// Priority #2
				// We're turning while moving forward/backwards, subtract the turn setpoint
				// percentage from the inside set of wheels
				if (turnSetpoint < 0.0)
				{
					
				// We're turning to the left
					if (placement == Placement.Left) 
					{
						setpoint = speedSetpoint * (1 - turnSetpoint);
					} 
					
				}
				else
				{
				// We're turning to the right
					
					if (placement == Placement.Right)
					{
						setpoint = speedSetpoint * (1 - turnSetpoint);
					}
				}
			}
		}
   		masterMotor.set(setpoint);
    }
    
    /**
     * Returns if a certain threshold of the current of the motors is exceeded.
     * Returns true if exceeded, returns false if the motors have not got to the threshold set.	
     * 
     * @return true if current of either the master or follower motor exceeds its threshold, false otherwise
     */
    public boolean isCurrentThresholdExceeded()
    {
    	if (masterMotor.getOutputCurrent() >= Parameters.kDriveMotorDownshiftCurrentThreshold || followerMotor.getOutputCurrent() >= Parameters.kDriveMotorDownshiftCurrentThreshold)
    	{
    		return true;
    	}
    	return false;
    }

    /**
     * Queries if the masterMotor is in speed control mode
     * @returns boolean - true if Talon Speed Controller is in speed 
     * control mode, false otherwise.
     */
    @objid ("ff98472c-d4a4-4aa7-815a-ae058c161375")
    public boolean isSpeedControlEnabled() 
    {
    	if (masterMotor.getControlMode() == CANTalon.ControlMode.Speed) 
    	{
    		return true;
    	}       	
    	return false;
    	
    }

    /**
     * Enables or disables the Talon Motor Controller's speed control.  
     * Note:  This method must check the current state of the masterMotor's 
     * speed control setting before changing it.
     * @param speedControlEnabled - true enables speed control (if it is 
     * not in speed control mode), false enables voltage control 
     * (only if it is not in voltage control mode already).
     */
    @objid ("c990d0bb-d6b3-4333-a041-3a38b70461a9")
    public void setSpeedControl(boolean speedControlEnabled) 
    {
    	if (speedControlEnabled != isSpeedControlEnabled())
    	{
    		if (speedControlEnabled)
        	{
        		masterMotor.changeControlMode(CANTalon.ControlMode.Speed);
        		masterMotor.setPID(Parameters.kDriveSpeedControlProportional, 
        						 	Parameters.kDriveSpeedControlIntegral, 
        						 	Parameters.kDriveSpeedControlDifferential, 
        						 	Parameters.kDriveSpeedControlThrottle, 
        						 	Parameters.kDriveSpeedControlIZone, 
        						 	Parameters.kDriveControlCloseLoopRampRate, 
        						 	Parameters.kDriveControlProfile);
        	}
        	else 
        	{
        		masterMotor.changeControlMode(CANTalon.ControlMode.PercentVbus);
        	}
    	}
    	
    }

    /**
     * Returns the placement
     * @returns Placement - The location of the DriveUnit
     */
    @objid ("9f636d22-f55f-43bf-b998-1f88f80adbc2")
    public Placement getPlacement() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.placement;
    }

    /**
     * 
     * @param value
     */
    public void setTurnSetpoint(double value)
    {
    	turnSetpoint = value;
    }
    
    /**
     * 
     * @return
     */
    public double getTurnSetpoint()
    {
    	return this.turnSetpoint;
    }
    
    @objid ("b87fda8c-c9ff-4309-987b-05d2794dfab0")
    public enum Placement {
        Left,
        Right;
    }

}