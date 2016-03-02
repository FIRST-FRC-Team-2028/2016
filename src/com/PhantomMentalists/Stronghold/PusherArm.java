package com.PhantomMentalists.Stronghold;

import com.PhantomMentalists.Stronghold.Parameters;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * <p>Author: Ryan</p>
 */
@objid ("a38fcf5c-bc25-4155-8afc-05a8335fee44")
public class PusherArm {
    /**
     * ONly motor of the PusherArm.
     */
	
    @objid ("b1de0831-3bc0-40f1-9fd9-0add4a6d68db")
    protected CANTalon tiltMotor;
    
    /**
     * true or false value dependent on if the autopilot mode is enabled.
     */	
    protected boolean autopilotEnabled;
    
    
    /**
     * It stores the position set point.
     */
    protected Position positionSetpoint;
    
    /**
     * Position which the motor is on.
     * 
     */
    protected Position currentPosition;
    
    @objid ("ef54315f-71a9-48b1-8840-4441e6e9db2c")
    public PusherArm() {
    	tiltMotor = new CANTalon(Parameters.kPusherArmMotorCanId);
    	tiltMotor.enableLimitSwitch(true, false);
    	disableTiltPositionControl();
    	positionSetpoint = Position.kHome;
    	currentPosition = Position.kUnknown;
    	
    }
    
    /**
     * This method enables the climbing arm's Raise/lower motor's position control mode.
     */	
    public void enableTiltPositionControl() {
    	tiltMotor.disable();
    	tiltMotor.changeControlMode(TalonControlMode.Position);
    	tiltMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	tiltMotor.setPID(Parameters.kPusherArmPositionControlProportional, 
			 	Parameters.kPusherArmPositionControlIntegral, 
			 	Parameters.kPusherArmPositionControlDifferential);
    	tiltMotor.setF(Parameters.kPusherArmPositionControlThrottle);
    	tiltMotor.enable();
    }
    
    /**
     * This method disables the climbing arm's Raise/lower motor's position
     * control mode. It changes the control mode to PercentVbus.
     */	
    public void disableTiltPositionControl() {
    	tiltMotor.disable();
    	tiltMotor.changeControlMode(TalonControlMode.PercentVbus);
    	tiltMotor.enable();
    	tiltMotor.set(0);
    	autopilotEnabled = false;    	
    }
    
    /**
     *  Returns true if the motor is in speed control mode.
     * 
     * 
     * @return
     */
    public boolean isTiltMotorPositionControlEnabled()
    {
    	boolean returnCode = false;
    	if (tiltMotor.getControlMode() == CANTalon.TalonControlMode.Position)
    	{
    		returnCode = true;
    	}
    	return returnCode;
    }
    
    /**
     * It sets the autopilot true or false status when it is called.
     * 
     * @param enabled
     */
    public void setAutopilot(boolean enabled) {
    	autopilotEnabled = enabled;
    }
    
    /**
     * It sets the motor to voltage control mode (calling disableTiltPositionControl()) an it sets the threshold given.
     * 
     * @param value
     */
    @objid ("59a12327-d8c3-4b2c-8521-285b79fef074")
    public void manualSetTilt(double value)
	{
    	disableTiltPositionControl();
		tiltMotor.set(value);
    }

    /**
     * It sets the position (home or down) given by the master class.
     * 
     * @param newPosition
     */
    public void setTiltPosition(Position newPosition) {
    	
    	this.positionSetpoint = newPosition;
    	
    }
    
    /**
     * This method is called once every iteration through the robot's main loop, in both
     * autonomous and operatorControl.  It is responsible for controlling the pusher arm's
     * movements based on its state, sending updated telemetry values to the smart 
     * dashboard, and catching any error conditions (such as motors being over their 
     * current limit).
     */
    public void process()
    {
    	SmartDashboard.putNumber("PusherArm Voltage: ", tiltMotor.getOutputVoltage());
    	SmartDashboard.putNumber("PusherArm Current: ", tiltMotor.getOutputCurrent());
    	SmartDashboard.putNumber("PusherArm Position: ", tiltMotor.getPosition());
    	
    	if (tiltMotor.getOutputCurrent() > Parameters.kPusherArmMaxMotorCurrent)
    	{
    		disableTiltPositionControl();
    	}
    	
    	if (autopilotEnabled)
    	{
    		if (currentPosition == Position.kUnknown)
    		{
    			manualSetTilt(Parameters.kPusherArmHomeMotorPower);
    		}
    		else if (positionSetpoint == Position.kHome)
    		{
    			if (!isTiltMotorPositionControlEnabled())
    			{
    				enableTiltPositionControl();
    			}
    			tiltMotor.set(Parameters.kPusherArmHomeSetPoint);
    		}
    		else if (positionSetpoint == Position.kDown)
    		{
    			if (!isTiltMotorPositionControlEnabled())
    			{
    				enableTiltPositionControl();
    			}
    			tiltMotor.set(Parameters.kPusherArmDownSetPoint);
    		}
    		
    	}
    	
    	if (tiltMotor.isRevLimitSwitchClosed())
    	{
    		currentPosition = Position.kHome;
    		tiltMotor.setPosition(0);
    		tiltMotor.set(0);
    	}
    }

    /**
     * This method returns if the pusher arm is in a known position.
     * 
     * The shooter uses an encoder to count ticks as it moves.  When the robot is initially
     * powered on, the pusher arm angle is in an unknown state until it reaches a limit
     * switch at a known position.  If the motor is ever shutoff for an over current condition
     * it is reset to an unknown position (since that should never happen).
     * 
     * @return boolean - true if pusher arm tilt angle has reached the home limit switch since
     *         being powered on, false otherwise.
     */
    public boolean isKnownPosition() {
    	boolean returnCode = true;
    	if (currentPosition == Position.kUnknown)
    	{
    		returnCode = false;
    	}
    	return returnCode;
    }

    /**
     * Returns true if the motor is at setpoint, else false.
     * 
     * @return boolean
     */
    public boolean isPositionAtSetpoint() {
    	if (currentPosition == Position.kUnknown)
    	{
    		return false;
    	}
    	if (currentPosition != positionSetpoint)
    	{
    		return false;
    	}
    	return true;
    }
    
    /**
     * Returns true if motor is moving, else false.
     * 
     * @return boolean
     */
    public boolean isMoving() {
    	double voltage = tiltMotor.getOutputVoltage();
    	if (voltage <= Parameters.kMotorVoltageDeadband || voltage >= (-1.0 * Parameters.kMotorVoltageDeadband))
    	{
    		return false;
    	}
    	return true;
    }
    
    /**
     * It enumerates the different positions of the motor (unknown, home, down).
     */
    public enum Position
    {
        /**
         * unknown
         */
    	kUnknown, 
        
    	/**
         * Up
         */
    	kHome, 

        /**
         * down
         */
    	kDown
    }

}