package com.PhantomMentalists.Stronghold;

import com.PhantomMentalists.Stronghold.Parameters;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

/**
 * <Enter note text here>
 * 
 * <p>Author: Ryan</p>
 */
@objid ("a38fcf5c-bc25-4155-8afc-05a8335fee44")
public class PusherArm {
    /**
     * <Enter note text here>
     */
    @objid ("b1de0831-3bc0-40f1-9fd9-0add4a6d68db")
    protected CANTalon tiltMotor;

    /**
     * <Enter note text here>
     */
    protected boolean autopilotEnabled;
    
    /**
     * <Enter note text here>
     */
    protected Position positionSetpoint;
    
    /**
     * <Enter note text here>
     */
    @objid ("ef54315f-71a9-48b1-8840-4441e6e9db2c")
    public PusherArm() {
    	tiltMotor = new CANTalon(Parameters.kPusherArmMotorCanId);
    	tiltMotor.configEncoderCodesPerRev(Parameters.kEncoderCodesPerRev);    	
    	tiltMotor.enableBrakeMode(true);
    	disablePositionControl();
    	autopilotEnabled = false;
    	positionSetpoint = kUnknown;
    }

    /**
     * <Enter note text here>
     * 
     * @param value
     */
    public void enablePositionControl() {
    	tiltMotor.disable();
    	tiltMotor.changeControlMode(TalonControlMode.Position);
    	tiltMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	tiltMotor.setPID(Parameters.kShootTiltPositionControlProportional, 
			 	Parameters.kPusherTiltPositionControlIntegral, 
			 	Parameters.kPusherTiltPositionControlDifferential, 
			 	Parameters.kPusherTiltPositionControlThrottle);
    	tiltMotor.enable();
    }
    
    /**
     * <Enter note text here>
     * 
     * @param value
     */
    public void disablePositionControl() {
    	tiltMotor.disable();
    	tiltMotor.changeControlMode(CANTalon.ControlMode.PercentVbus);
    	tiltMotor.enable();
    	tiltMotor.set(0);
    	autopilotEnabled = false;
    }
    
    
    /**
     * <Enter note text here>
     * 
     * @param enabled
     */
    public void setAutopilot(boolean enabled) {
    	autopilotEnabled = enabled;
    }
    
    /**
     * <Enter note text here>
     * 
     * @param value
     */
    @objid ("59a12327-d8c3-4b2c-8521-285b79fef074")
    public void manualSetTilt(double value)
	{
    	disablePositionControl();
		tiltMotor.set(value);
    }

    /**
     * <Enter note text here>
     * 
     * @param newPosition
     */
    public void setTiltPosition(Position newPosition) {
    	
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

    }

    /**
     * <Enter note text here>
     * 
     * @return boolean
     */
    public boolean isPositionAtSetpoint() {
    	
    }
    
    /**
     * <Enter note text here>
     * 
     * @return boolean
     */
    public boolean isMoving() {
    	
    }
    
    /**
     * <Enter note text here>
     */
    public enum Position
    {
        /**
         * <Enter note text here>
         */
    	kUnknown, 
        
    	/**
         * <Enter note text here>
         */
    	kHome, 

        /**
         * <Enter note text here>
         */
    	kDown
    }

}