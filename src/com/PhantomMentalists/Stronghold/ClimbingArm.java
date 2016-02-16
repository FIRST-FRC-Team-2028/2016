package com.PhantomMentalists.Stronghold;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

/**
 * 
 * <Enter note text here>
 * 
 * @author Ricky
 */
@objid ("899c26b9-be06-403c-b6de-a1d9d8a5322b")

public class ClimbingArm {
    /**
     * <Enter note text here>
     */
    @objid ("de0098a0-c9b9-42cb-8012-212eec71543c")
    protected CANTalon extendRetractMotor;
	
    /**
     * <Enter note text here>
     */
	protected CANTalon leftWenchMotor; 
	
    /**
     * <Enter note text here>
     */
	protected CANTalon rightWenchMotor;
	
    /**
     * <Enter note text here>
     */	
    @objid ("94a77f41-a9e3-4f3b-b9c4-58e16c9e1898")
    protected CANTalon raiseLowerMotor;

    /**
     * <Enter note text here>
     */
    protected ClimberPositions climberState;
    
    /**
     * <Enter note text here>
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
		
    	// For now, assume winch motors are running in percent Vbus.
    	// TO-DO:  FIND OUT IF THIS IS CORRECT!!!
		rightWenchMotor.disable();
		rightWenchMotor.changeControlMode(CANTalon.ControlMode.PercentVbus);
		rightWenchMotor.enable();
		rightWenchMotor.set(0);
		
		leftWenchMotor.disable();
		leftWenchMotor.changeControlMode(CANTalon.ControlMode.PercentVbus);
		leftWenchMotor.enable();
		leftWenchMotor.set(0);		
    	autopilotEnabled = false;
    	climberState = ClimberPositions.kUnknown;
    }
    
    /**
     * <Enter note text here>
     */	
    public void enableTiltPositionControl() {
    	raiseLowerMotor.disable();
    	raiseLowerMotor.changeControlMode(TalonControlMode.Position);
    	raiseLowerMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	raiseLowerMotor.setPID(Parameters.kClimbTiltPositionControlProportional, 
			 	Parameters.kPusherClimbTiltPositionControlIntegral, 
			 	Parameters.kPusherClimbTiltPositionControlDifferential, 
			 	Parameters.kPusherClimbTiltPositionControlThrottle);
    	raiseLowerMotor.enable();
    }
    
    /**
     * <Enter note text here>
     */	
    public void disableTiltPositionControl() {
    	raiseLowerMotor.disable();
    	raiseLowerMotor.changeControlMode(CANTalon.ControlMode.PercentVbus);
    	raiseLowerMotor.enable();
    	raiseLowerMotor.set(0);
    	autopilotEnabled = false;    	
    }
    
    /**
     * <Enter note text here>
     */	
    public void enableExtendRetractPositionControl() {
    	extendRetractMotor.disable();
    	extendRetractMotor.changeControlMode(TalonControlMode.Position);
    	extendRetractMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	extendRetractMotor.setPID(Parameters.kClimbExtendPositionControlProportional, 
			 	Parameters.kPusherClimbExtendPositionControlIntegral, 
			 	Parameters.kPusherClimbExtendPositionControlDifferential, 
			 	Parameters.kPusherClimbExtendPositionControlThrottle);
    	extendRetractMotor.enable();    	
    }
    
    /**
     * <Enter note text here>
     */	
    public void disableExtendRetractPositionControl() {
    	extendRetractMotor.disable();
    	extendRetractMotor.changeControlMode(CANTalon.ControlMode.PercentVbus);
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
     * just play out cable without allowing the cable to go slack.
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
     * <Enter note text here>
     * 
     * @param ClimberPositions
     */
    public void setPositionSetpoint(ClimberPositions newState) {
    	
    }
    
    /**
     * This method checks if the arm is in a known position, if it is known
     * it will return true, if it isn't it will return false.
     * 
     * @return boolean - true if arm is in a known position, False if it is not.
     */
    public boolean isKnownPosition()
    {
    	
    }
    
    /**
     * <Enter note text here>
     * 
     * @return boolean
     */
    public boolean isTiltPositionControlEnabled() {
    	
    }
    
    /**
     * <Enter note text here>
     * 
     * @return boolean
     */
    public boolean isExtendRetractPositionControlEnabled() {
    	
    }
    
    /**
     * <Enter note text here>
     * 
     * @return boolean
     */
    public boolean isAtSetpoint() {
    	
    }
    
    /**
     * <Enter note text here>
     * 
     * @return
     */
    public boolean isClimberMoving() {
    	
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
    	if (extend.getPosition() < Parameters.kClimberFullyExtendedPositionSetpoint)
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
    public void process() {
    	
    }
    
    /**
     * <Enter note text here>
     */
    public enum ClimberPositions
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
    	kLowBar,
    	
        /**
         * <Enter note text here>
         */
    	kDeployHook,
    	
        /**
         * <Enter note text here>
         */
    	kClimb,
    	
        /**
         * <Enter note text here>
         */
    	kRaised,
    	
        /**
         * <Enter note text here>
         */
    	kDrawBridge
    }
    
}