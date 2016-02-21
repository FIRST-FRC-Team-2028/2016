	package com.PhantomMentalists.Stronghold;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * This class encapsulates the state (attributes) and logic (methods) required to control
 * Telepath's shooter to score points by hurling a boulder into the high goal.
 * 
 * @author Nazhere and Moose
 */
@objid ("e7c0967b-bd2b-41d7-a528-2d3f463133d3")
public class Shooter {
	public double p = 0,i=0,d =0;
	public Preferences prefs;
	public boolean tilting = false;
	/**
	 * <Enter note text here>
	 */
	protected ShooterPosition position;

    /**
     * <Enter note text here>
     */
    @objid ("54c45f11-335d-432c-93c6-f1a8354239b9")
    protected DigitalInput ballSensor;

    /**
     * Controls the hardware for the motor driving the left-hand pitching motor
     */
    @objid ("fde5fcfa-15dc-4eae-a052-65112042635a")
    protected CANTalon leftPitchingMotor;

    /**
     * Controls the hardware for the motor driving the right-hand pitching motor
     */
    @objid ("e22e0db7-a4cb-46a2-85aa-b7a1de718fc6")
    protected CANTalon rightPitchingMotor;

    /**
     * Controls the hardware for the motor driving the shooter tilt angle
     */
    @objid ("01c41837-6db2-47af-ba2f-e34cc6166b56")
    protected CANTalon tiltMotor;

    /**
     * <Enter note text here>
     */
    @objid ("2c653aa8-a80f-4797-975e-a6799cc7f9aa")
    protected Solenoid ballShooter;

    /**
     * <Enter note text here>
     */
    @objid ("2c653aa8-a80f-4797-975e-a6799cc7f9aa")
    protected Solenoid dink;

    /**
     * <Enter note text here>
     */
	private double tiltSetpoint;
	private double tiltPosition;
	
	/**
	 * Internal variable that indicates the shooter is in the process of shooting.
	 * This attribute is set by the shoot() method and remains true until the ball
	 * is away.
	 */
	private double tiltAngle;
	protected boolean shooting;
	
	/**
	 * Internal variable that indicates the shooter is in the process of reloading.
	 * This attribute is set by the shoot() method and remains true until we sense
	 * a ball in the shooter (or it is cancelled by the driver).  
	 */
	protected boolean reloading;
	
	protected boolean autopilotEnabled;
	
    @objid ("e8c46368-8549-4701-acd1-6a7a1b073c83")
    public Shooter() {
    	prefs = Preferences.getInstance();
    	prefs.putDouble("Tilt P", p);
    	prefs.putDouble("Tilt I", i);
    	prefs.putDouble("Tilt D", d);
    	rightPitchingMotor = new CANTalon(Parameters.kRightShooterPitcherMotorCanId);
    	leftPitchingMotor = new CANTalon(Parameters.kLeftShooterPitcherMotorCanId);
    	tiltMotor = new CANTalon(Parameters.kShooterAngleMotorCanId);
    	
    	rightPitchingMotor.enableBrakeMode(true);
    	leftPitchingMotor.enableBrakeMode(true);
    	tiltMotor.enableBrakeMode(true);
//    	tiltMotor.configMaxOutputVoltage(Parameters.kShooterTiltMaxVolt);
    	ballShooter = new Solenoid(Parameters.kShooterBallShooterSolenoidChanel);
    	ballShooter.set(false);
//    	dink = new Solenoid(Parameters.kShooterDinkSolenoidChanel);
//    	dink.set(true);
//    	tiltMotor.setPosition(0);
    	position = ShooterPosition.kUnknown;
//    	enablePitchingMachineSpeedControl();
    	
    	// Disable tilt position control until we've reached the home position
    	disablePitchingMachineSpeedControl();
//    	enableTiltPositionControl();
    	disableTiltPositionControl();
    	shooting = false;
    	reloading = false;
    	autopilotEnabled = true;
    }

    /**
     * 
     */
    public void disableTiltPositionControl() {
    	tiltMotor.disable();
    	tiltMotor.changeControlMode(TalonControlMode.PercentVbus);
    	tiltMotor.enable();       	
    }
    
    /**
     * 
     */
    public void enableTiltPositionControl() {
    	System.out.println("here");
    	tiltMotor.disable();
    	tiltMotor.changeControlMode(TalonControlMode.Position);
    	tiltMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
    	tiltMotor.setPID(Parameters.kShootTiltPositionControlProportional, 
			 	Parameters.kShootTiltPositionControlIntegral, 
			 	Parameters.kShootTiltPositionControlDifferential);
    	tiltMotor.enable();
    }
    
    /**
     * 
     */
    public void disablePitchingMachineSpeedControl() {
    	rightPitchingMotor.disable();
    	leftPitchingMotor.disable();
    	rightPitchingMotor.changeControlMode(TalonControlMode.PercentVbus);
    	leftPitchingMotor.changeControlMode(TalonControlMode.PercentVbus);
    	rightPitchingMotor.enable();    	
    	leftPitchingMotor.enable();
    }
    
    /**
     * 
     */
    public void enablePitchingMachineSpeedControl() {
    	rightPitchingMotor.disable();
    	leftPitchingMotor.disable();    	
    	rightPitchingMotor.changeControlMode(TalonControlMode.Speed);
    	rightPitchingMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
    	rightPitchingMotor.setPID(Parameters.kShootPitchPositionControlProportional, 
			 	Parameters.kShootPitchPositionControlIntegral, 
			 	Parameters.kShootPitchPositionControlDifferential);    	
    	leftPitchingMotor.changeControlMode(TalonControlMode.Speed);
    	leftPitchingMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
    	leftPitchingMotor.setPID(Parameters.kShootPitchPositionControlProportional, 
			 	Parameters.kShootPitchPositionControlIntegral, 
			 	Parameters.kShootPitchPositionControlDifferential);
    	rightPitchingMotor.enable();    	
    	leftPitchingMotor.enable();    	
    }
    
    /**
     * This method shoots the shooter.  It assumes the robot is already aimed at the target
     * (i.e., the robot is aimed at the target and the shooter tilt angle is already at the
     * correct tilt angle).  It first turns on the two pitching machine motors, and once up
     * to speed it retracts the dink and lastly triggers the ball shooter solenoid.
     */
    @objid ("af588d71-18eb-4098-95a6-d69164a77e41")
    public void shoot() {
    	
    		rightPitchingMotor.set(Parameters.kShooterShootPitchingMachineSpeed);
        	leftPitchingMotor.set(-Parameters.kShooterShootPitchingMachineSpeed);
        	shooting = true;
//        	if(isPitchingMachineUpToSpeed()){
//        		dink.set(false);
//        		ballShooter.set(true);
//        	}
//        	else{
//        		return;
//        	}
    }

    public void reload() {
    	rightPitchingMotor.set(-Parameters.kShooterReloadPitchingMachineSpeed);
    	leftPitchingMotor.set(Parameters.kShooterReloadPitchingMachineSpeed);
    	reloading = true;
//    	if(isPitchingMachineUpToSpeed()){
//    		dink.set(true);
//    		ballShooter.set(false);
//    	}
//    	else{
//    		return;
//    	}
    }
    
    /**
     * Tells if a ball is in the shooter.
     * 
     * @return true if a ball is in the shooter, false otherwise
     */
    @objid ("91e9f52a-d664-4f68-ba8b-4e5503fe8eda")
    public boolean isBallLoaded() {
    	return ballSensor.get();
    	// may change depending on if the sensor is analog of digital.
    }

    /**
     * This method tells the shooter tilt angle to move to one of its pre-defined setpoints
     * 
     * @param shooterPosition the pre-defined shooter setpoint to move to
     */
    @objid ("f1761ce3-8d39-43f8-9b3c-6adaedaec7ad")
    public void setShootAngle(ShooterPosition shooterPosition) {
//    	enableTiltPositionControl();
    	System.out.println("here");
    	tiltMotor.enableControl();
    	p = prefs.getDouble("Tilt P",p);
    	i = prefs.getDouble("Tilt I", i);
    	d = prefs.getDouble("Tilt D",d);
    	position = shooterPosition;
    	tiltMotor.set(getDirection());
    }
    public double getDirection()
    {
    	if(isTiltAngleAtSetpoint())
    	{
    		return 0;
    	}
    	else if(tiltPosition > position.getPosition())
    	{
    		tilting = true;
    		return Parameters.kShooterTiltPowerUp;
    	}
    	else if(tiltPosition < position.getPosition())
    	{
    		tilting = true;
    		return Parameters.kShooterTiltPowerDown;
    	}
    	return 0;
    }
    public void resetTiltAngle()
    {
    	tiltMotor.setPosition(0);
    }
    public void manualRunTiltMotor(double value)
    {
    	disableTiltPositionControl();
    	tiltMotor.enableControl();
//    	System.out.println("here2");
    	if(tiltMotor.getControlMode() == TalonControlMode.Position)
    	{
    		tiltMotor.set(tiltMotor.getPosition()+value);
    	}
    	else
    	{
    		tiltMotor.set(value);
    	}
    }
    
    /**
     * Manually run the pitching machine.
     * 
     * @param value percentage of power to run the pitching machine motors in the range of
     *        (-1.0 .. 0 .. 1.0) where 1.0 is 100% forward and -1.0 is 100% reverse.
     */
    @objid ("eba4d1df-2aa2-4f39-b3a7-f0ca4f9b6626")
    public void manualRunPitchingMachine(double value) {
//    	if (isPitchingMachineSpeedControlEnabled())
//    	{
//    		disablePitchingMachineSpeedControl();
//    	}
    	rightPitchingMotor.set(value);
    	leftPitchingMotor.set(-value);
    	autopilotEnabled = false;
    }

    @objid ("17dd7815-19f9-4e9f-80bb-74ef735538d4")
    public boolean isPitchingMachineUpToSpeed() {
    	if(rightPitchingMotor.getSpeed()==Parameters.kShooterShootPitchingMachineSpeed && 
    			leftPitchingMotor.getSpeed()==Parameters.kShooterShootPitchingMachineSpeed){
    		return true;
    	}
    	else{
    		return false;
    	}
       
    }

    /**
     * This method enables or disables the pneumatic ball pusher solenoid.
     * 
     * @param push True extends the penumatic ball thumper, false retracts it
     */
    @objid ("e0334a6b-b527-4a95-936a-51e68527f3ea")
    public void pushBall(boolean push) {
    	ballShooter.set(push);
    }

    public void stop()
    {
    	tiltMotor.disableControl();
    }
    
    /**
     * This method retracts or extends the dink
     * 
     * @param retract true retracts the dink, false extends it
     */
    public void setDink(boolean retract) {
    	dink.set(retract);
    }
    
    /**
     * 
     * @return
     */
    @objid ("bf56ebbc-3451-4f51-88e0-c944b48b9819")
    public double getTiltSetpoint() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.tiltSetpoint;
    }
    public double getTiltAngle(){
    	return this.tiltAngle;
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
     * 
     * @return boolean - true if pitching machine motors are on, false otherwise
     */
    @objid ("f6797d96-b58a-47ee-a41a-4ea7709c5062")
    public boolean isPitchingMachineOn() {
    	boolean rc = false;
    	if (leftPitchingMotor.getOutputVoltage() != 0.0)
    	{
    		rc = true;
    	}
    	return rc;
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
    	return false;
    }

    /**
     * <Enter note text here>
     */
    @objid ("4d7bd443-0329-4985-8729-3ec742465875")
    public boolean isTiltAngleAtSetpoint() {
    	// Figure out if tilt angle is "close enough" to setpoint
    	if(tiltPosition >= position.getPosition()-250 && tiltPosition <= position.getPosition()+250)
    	{
    		return true;
    	}
    	return false;
    }
    
    /**
     * 
     * @param on
     */
    public void setPitchingMachine(boolean on) {
    	if (leftPitchingMotor.equals(false) && rightPitchingMotor.equals(false) ) {
    		leftPitchingMotor.enable(); 
    		rightPitchingMotor.enable();
    	}
    	if ( isTiltAngleAtSetpoint() ) {
    		if ( isPitchingMachineUpToSpeed() ) {
    			if ( dink.equals(true) ) {
    				ballShooter.equals(true);
    			}
    			else
    			{
    				// Dink is not (yet) retracted
    				dink.set(true);
    			}
    		}
    	}
    }

    /**
     * This method is called once every iteration through the robot's main loop, in both
     * autonomous and operatorControl.  It is responsible for controlling the shooter's
     * movements based on its state, sending updated telemetry values to the smart 
     * dashboard, and catching any error conditions (such as motors being over their 
     * current limit).
     */
    public void process()
    {
//    	SmartDashboard.putNumber("Tilt Voltage", tiltMotor.getOutputVoltage());
//    	SmartDashboard.putNumber("Tilt Current", tiltMotor.getOutputCurrent());
//    	SmartDashboard.putNumber("L Volt", leftPitchingMotor.getOutputVoltage());
//    	SmartDashboard.putNumber("L Current", leftPitchingMotor.getOutputCurrent());
//    	SmartDashboard.putNumber("R Volt", rightPitchingMotor.getOutputVoltage());
//    	SmartDashboard.putNumber("R Current",rightPitchingMotor.getOutputCurrent());
    	SmartDashboard.putNumber("Tilt Pos", tiltMotor.getPosition());
    	tiltPosition = tiltMotor.getPosition();
    	p = prefs.getDouble("Tilt P", p);
    	i = prefs.getDouble("Turn I", i);
    	d = prefs.getDouble("Turn D",d);
    	if(tilting)
    	{
    		if(isTiltAngleAtSetpoint())
    		{
    			tiltMotor.set(0);
    			tilting = false;
    		}
    	}
    	//PitchingMachine method just retracts kicker, extends dink and turns motor off
   	 	if(isAutoPilotEnabled())
   	 	{
	 		
	   		switch (position) 
	   		{
		   		case kUnknown:
		   			// We don't know the shooter's tilt angle, run the shooter in reverse at a slow
		   			// constant speed until we reach the reverse limit switch.
		   			disableTiltPositionControl();
		   			tiltMotor.set(Parameters.kShooterSeekHomePower);
		   			if (tiltMotor.isRevLimitSwitchClosed());
		   			{
		   				position = ShooterPosition.kHome;
		   				tiltMotor.set(0.0);
		   				enableTiltPositionControl();
		   			}
		   			break;
		   		
		   		case kHome:
		   			tiltMotor.set(Parameters.kShooterTiltHomePositionEncoderSetpoint);
		   			if (isPichingMachineIsOn())
		   			{
		   				setPitchingMachine(false);
		   			}
		   			break;
		   			
		   		case kLowBar:
		   			tiltMotor.set(Parameters.kShooterTiltLowBarPositionEncoderSetpoint);
		   			if (isPichingMachineIsOn())
		   			{
		   				setPitchingMachine(false);
		   			}
		   			break;
		   			
		   		case kReload:
		   			tiltMotor.set(Parameters.kShooterTiltReloadPositionEncoderSetpoint);
		   			if (isPichingMachineIsOn())
		   			{
		   				setPitchingMachine(true);
		   			}
		   			break;
		   			
		   		case kShootBatter:
		   			tiltMotor.set(Parameters.kShooterTiltShootBatterPositionEncoderSetpoint);
		   			if (isPichingMachineIsOn())
		   			{
		   				setPitchingMachine(true);
		   			}
		   			break;
		   			
		   		case kShootTape:
		   			tiltMotor.set(Parameters.kShooterTiltShootTapePositionEncoderSetpoint);
		   			if (isPichingMachineIsOn())
		   			{
		   				setPitchingMachine(true);
		   			}
		   			break;
		   			
		   		case kShootDefense:
		   			tiltMotor.set(Parameters.kShooterTiltShootDefensePositionEncoderSetpoint);
		   			if (isPichingMachineIsOn())
		   			{
		   				setPitchingMachine(true);
		   			}
		   			break;
	   		}
   		
   	 	}
    }

    /**
     * 
     * @return sets pitching machine off at base due to orientation of cases in process()
     */
    public boolean isPichingMachineIsOn() {
		return false;
	}

	/**
     * 
     * @return true if speed control for pitching machine is enabled. returns false otherwise.
     */
    public boolean isPitchingMachineSpeedControlEnabled() {
    	return (leftPitchingMotor.getControlMode() == TalonControlMode.Speed);
    }
    
    /**
     * Interogates the shooter to see if it is currently shooting
     * 
     * @return true if in the process of shooting, false otherwise
     */
    public boolean isShooting() {
    	return shooting;
    }
    
    /**
     * Interogates the shooter to see if it is curently reloading
     * 
     * @return true if in the process of reloading, false otherwise
     */
    public boolean isReloading() {
    	return reloading;
    }
    public boolean isAutoPilotEnabled(){
    	return false;
    }
    @objid ("08844018-94d8-4017-af1b-f834faa0871b")
    public enum ShooterPosition {

         /**
         * This setpoint puts the Shooter tilt angle at the home position
         */
        kHome(Parameters.kShooterTiltHomePositionEncoderSetpoint),
        
        /**
         * This represents the shooter tilt angle is in an unknown position.  This is the initial state of the robot until it is homed.
         */
        kUnknown(null),
        
        /**
         * This setpoint holds the tilt angle of the shooter for shooting at the goal from the batter position.
         */
        kShootBatter(Parameters.kShooterTiltShootBatterPositionEncoderSetpoint),
        
        /**
         * This setpoint indicates the shooter tilt angle is correct for shooting when the robot is lined up on the tape in the center of the courtyard.
         */
        kShootTape(Parameters.kShooterTiltShootTapePositionEncoderSetpoint),
        
        /**
         * This setpoint aligns the shooter tilt angle to take a shot from the couryard just inside the defense.
         */
        kShootDefense(Parameters.kShooterTiltShootDefensePositionEncoderSetpoint),
        
        /**
         * This setpoint puts the shooter tilt angle at the horizontal position to clear the low bar.
         */
        kLowBar(Parameters.kShooterTiltLowBarPositionEncoderSetpoint),
        
        /**
         * This setpoint holds the shooter tilt angle to the full-down position and runs the pitching machine motors in reverse until a ball is loaded.
         */
        kReload(Parameters.kShooterTiltReloadPositionEncoderSetpoint);
    	private double position;
    	ShooterPosition(double pos)
    	{
    		position = pos;
    	}
    	ShooterPosition(Object obj)
    	{	
    	}
    	public double getPosition()
    	{
    		return position;
    	}
    }

}