package com.PhantomMentalists.Stronghold;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

/**
 * 
 * This is the class for the climber and controls the extending and retracting 
 * the climbing arm as well as changing the control modes of the climbing arm motors.
 *  
 * @author Ricky
 */
@objid ("899c26b9-be06-403c-b6de-a1d9d8a5322b")

public class ClimbingArm {
    /**
     * This is the motor that extends and retracts the climber arm.
     */
    @objid ("de0098a0-c9b9-42cb-8012-212eec71543c")
    protected CANTalon extendRetractMotor;
	
    /**
     * This is the motor that retracts the climber arm on the left side
     */
	protected CANTalon leftWenchMotor; 
	
    /**
     * This is the motor that retracts the climber arm on the right side
     */
	protected CANTalon rightWenchMotor;
	
    /**
     * This is the motor that raises and lowers the climbing arm.
     */	
    @objid ("94a77f41-a9e3-4f3b-b9c4-58e16c9e1898")
    protected CANTalon raiseLowerMotor;

    /**
     * this has all of the states of the climber arm.
     */
    protected ClimberPositions climberState;
    
    /**
     * true or false value dependent on if the autopilot mode is enabled.
     */	
    protected boolean autopilotEnabled;
    
    /**
     * <Enter note text here>
     */	
    @objid ("c215a8ac-a925-4c23-aab7-ec75ee354734")
    public ClimbingArm()
    {
    	raiseLowerMotor = new CANTalon(Parameters.kClimberAngleMotorCanId);
    	raiseLowerMotor.configEncoderCodesPerRev(Parameters.kEncoderCodesPerRev);
    	disableTiltPositionControl();
		extendRetractMotor = new CANTalon(Parameters.kClimberExtendMotorCanId);
		extendRetractMotor.configEncoderCodesPerRev(Parameters.kEncoderCodesPerRev);		
		disableExtendRetractPositionControl();
		rightWenchMotor = new CANTalon(Parameters.kClimberRightWinchMotorCanId);
		leftWenchMotor = new CANTalon(Parameters.kClimberLeftWinchMotorCanId);
		
    	//winch motors are running in percent Vbus.
		rightWenchMotor.disable();
		rightWenchMotor.changeControlMode(TalonControlMode.PercentVbus);
		rightWenchMotor.enable();
		rightWenchMotor.set(0);
		
		leftWenchMotor.disable();
		leftWenchMotor.changeControlMode(TalonControlMode.PercentVbus);
		leftWenchMotor.enable();
		leftWenchMotor.set(0);		
    	autopilotEnabled = false;
    	climberState = ClimberPositions.kUnknown;
    }
    
    /**
     * This method enables the climbing arm's Raise/lower motor's position control mode.
     */	
    public void enableTiltPositionControl() {
    	raiseLowerMotor.disable();
    	raiseLowerMotor.changeControlMode(TalonControlMode.Position);
    	raiseLowerMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	raiseLowerMotor.setPID(Parameters.kClimbTiltPositionControlProportional, 
			 	Parameters.kPusherClimbTiltPositionControlIntegral, 
			 	Parameters.kPusherClimbTiltPositionControlDifferential);
    	raiseLowerMotor.setF(Parameters.kPusherClimbTiltPositionControlThrottle);
    	raiseLowerMotor.enable();
    }
    
    /**
     * This method disables the climbing arm's Raise/lower motor's position
     * control mode. It changes the control mode to PercentVbus.
     */	
    public void disableTiltPositionControl() {
    	raiseLowerMotor.disable();
    	raiseLowerMotor.changeControlMode(TalonControlMode.PercentVbus);
    	raiseLowerMotor.enable();
    	raiseLowerMotor.set(0);
    	autopilotEnabled = false;    	
    }
    
    /**
     * This method enables the position control mode on the extend/retract motor.
     */	
    public void enableExtendRetractPositionControl() {
    	extendRetractMotor.disable();
    	extendRetractMotor.changeControlMode(TalonControlMode.Position);
    	extendRetractMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	extendRetractMotor.setPID(Parameters.kClimbExtendPositionControlProportional,
    			Parameters.kPusherClimbExtendPositionControlIntegral, 
			 	Parameters.kPusherClimbExtendPositionControlDifferential);
    	extendRetractMotor.setF(Parameters.kPusherClimbExtendPositionControlThrottle);
    	extendRetractMotor.enable();    	
    }
    
    /**
     * This method disables the position control mode on the extend/retract motor.
     * Then it switches the control mode to PercentVbus.
     */	
    public void disableExtendRetractPositionControl() {
    	extendRetractMotor.disable();
    	extendRetractMotor.changeControlMode(TalonControlMode.PercentVbus);
    	extendRetractMotor.enable();
    	extendRetractMotor.set(0);
    	autopilotEnabled = false;
    }
    
    /**
     * This method will switch the control mode for the extend/retract motor
     * to percent Vbus, disable autoPilot, and pass the supplied joystick power
     * value to the motor.
     * 
     * @param power
     */
    public void manualSetTilt(double power) {
    	if (isTiltPositionControlEnabled()) {
    		disableTiltPositionControl();
    	}
    	extendRetractMotor.set(power);    	
    }

    /**
     * This method will switch the control mode for the extend/retract motor
     * to percent Vbus, disable autoPilot, and pass the supplied joystick power
     * value to the motor.
     * <p>
     * When extending, we need to apply the supplied power parameter to the 
     * extend/retract motor, and also power the winch motors slowly enough to
     * just play out cable without allowing the cable to be slack.
     * <p>
     * When retracting, we need to apply the supplied power parameter to both
     * of the two winch motors, and also retract the extend/retract motor slowly
     * to keep the cable taught.
     * 
     * @param power - In the range (-1.0 .. 0 .. 1.0) where negative values
     *                retract the arm and positive values extend the arm
     */
    public void manualSetExtendRetract(double power) {
    	if (isExtendRetractPositionControlEnabled()) {
    		disableExtendRetractPositionControl();
    	}
    	
    	if (power < 0.0)
    	{
    		// Retracting
    		leftWenchMotor.set(power);
    		rightWenchMotor.set(power);
    		// Calculate a new power value for the extend/retract motor
    		extendRetractMotor.set(newValue);
    	}
    	else
    	{
    		// Extending
    		extendRetractMotor.set(power);
    		// Calculate a new power value for the two winch motors
    	
    		leftWenchMotor.set(newValue);
    		rightWenchMotor.set(newValue);
    	}
    }
    
    /**
     * sets the position setpoint
     * 
     * @param ClimberPositions
     */
    public void setPositionSetpoint(ClimberPositions newState) 
    {
    	
    }
    
    /**
     * This method checks if the arm is in a known position, if it is known
     * it will return true, if it isn't it will return false.
     * 
     * @return boolean - true if arm is in a known position, False if it is not.
     */
    public boolean isKnownPosition()
    {
    	return false;
    	 
    }
    
    /**
     * returns true or false if raise/lower motor is in position control.
     * 
     * @return boolean
     */
    public boolean isTiltPositionControlEnabled() {
    	return false;
    }
    
    /**
     * returns true or false if the extend/retract motor is in position control or not.
     * 
     * @return boolean
     */
    public boolean isExtendRetractPositionControlEnabled() {
    	return false;
    }
    
    /**
     * returns true or false if the climber arm is at the home point
     * 
     * @return boolean
     */
    public boolean isAtSetpoint() {
    	return false;
    }
    
    /**
     * this method returns true or false if the climber is moving in any way.
     * 
     * @return
     */
    public boolean isClimberMoving()
    {
    	return true;
    }
    
    /**
     * This method checks if stage two is fully extended on the right side.  
     * If it is fully extended, it will return true. If it is not fully
     * extended it will return false.
     * 
     * @return true if stage two is extended on right side, False if it is not.
     */
    public boolean isExtended()
   {	
    	if (extendRetractMotor.getPosition() < Parameters.kClimberFullyExtendedPositionSetpoint)
    	{
    		return false;
    	}
    	return true;
    }
    
    /**
     * This method checks if the climber arm is fully retracted. If it is retracted, 
     * it will return true.  If it is not retracted it will return false.
     * 
     * @return true if climber is retracted, False if it is not.
     */
    public boolean isRetracted()		
    {
    	if(extendRetractMotor.isRevLimitSwitchClosed())
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    /**
     * This method gets the angle position.  The value will be in full revolutions
     * of the motor's gearbox shaft from the home position (e.g., 0.0 is the home
     * position and 1024.25 is 1024 and a quarter revolutions of the gearbox shaft).
     * 
     * @return double - value of tilt ange in motor gearbox revolutions.
     */ 
    public double getTiltPosition()
    {
    	return raiseLowerMotor.getPosition();
    }
    
    /**
     * This method assigns a new setpoint for the climber while in autopilot
     * control.
     * 
     * @param newState - a new enum for the climber to move to in autopilot
     */
    public void autoSetClimberState(ClimberPositions newState)
    {
    	climberState = newState;
    }
    
    
    /**
     * This method is called once every iteration through the robot's main loop, 
     * in both autonomous and operatorControl.  It is responsible for controlling
     * the climbing arm's movements based on its state, sending updated telemetry 
     * values to the smart dashboard, and catching any error conditions (such as 
     * motors being over their current limit).
     */
    public void process()
    {
    	if(autopilotEnabled == true)
    	{
    		switch (climberState) 
    		{
    		case kUnknown:
    			disableTiltPositionControl();
    			raiseLowerMotor.set(Parameters.kRaiseLowerMoterHomeSpeed);
    			if(raiseLowerMotor.isRevLimitSwitchClosed())
    			{
    				climberState = ClimberPositions.kHome;
    				raiseLowerMotor.set(0.0);
    				enableTiltPositionControl();
    			}
    			break;
    			
    		case kHome:
    			
    			
    			break;
    			
    		case kLowBar:
    			
    			
    			break;
    			
    		case kDeployHook:
    			
    			
    			break;
    			
    		case kClimb:
    			
    			
    			break;
    			
    		case kRaised:
    			
    			
    			break;
    			
    		case kDrawBridge:
    			
    			
    			break;
    		}
    	}
    }
    
    
    
    /**
     * these are the positions of the climber arm. (e.g. home state: home
     * state is the state where the climber arm is touching the home limit switch)
     */
    public enum ClimberPositions
    {
        /**
         * This is when the climber arm is in an unknown state.
         */
    	kUnknown, 
        
    	/**
         * this is when the climber arm is at it's home position
         */
    	kHome, 

        /**
         * this is when the climber arm is low enough that it
         * can get under the low bar.
         */
    	kLowBar,
    	
        /**
         * this is the when the hook of the climber arm is fully deployed
         */
    	kDeployHook,
    	
        /**
         * this is the state of the climber arm when the arm is already attatched
         * to the rung and is in the process of climbing.
         */
    	kClimb,
    	
        /**
         * This is when the climber arm is positioned vertically.
         */
    	kRaised,
    	
        /**
         * This is the state of the climber arm when it brings down the drawbridge.
         */
    	kDrawBridge
    }
    
}