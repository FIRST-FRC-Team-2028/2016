package com.PhantomMentalists.Stronghold;

import java.util.Timer;
import java.util.TimerTask;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class encapsulates the state (attributes) and logic (methods) required to control
 * Telepath's shooter to score points by hurling a boulder into the high goal.
 * 
 * @author Nazhere and Moose
 */
@objid ("e7c0967b-bd2b-41d7-a528-2d3f463133d3")
public class Shooter extends TimerTask{
	/**
	 * Timer used for the timing operations
	 */
	private Timer timer;
	private Timer timer2;
	
	/**
	 * True if the the pitching machine is accelerating and if the pitching machine is not accelerating
	 */
	private boolean shootAccel = false;
	
	/**
	 * True if shooter is up to speed and false if not up to speed
	 */
	private boolean shootUpToSpeed = false;
	
	/**
	 * Predefined shooting tilt setpoints
	 */
	protected ShooterState tiltSetpoint;
	
	/**
	 * Current prefdefined shooting tilt position
	 */
	protected ShooterState currentPosition;
	
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
     * Custom memory to store a shooter tilt setpoint
     */
	private double tiltMemSetpoint;
	
	private boolean memSetpointSet = false;
		
	/**
	 * Internal variable that indicates the shooter is in the process of shooting.
	 * This attribute is set by the shoot() method and remains true until the ball
	 * is away.
	 */
	protected boolean shooting;
	
	/**
	 * True if the shooter is allowed move itself
	 */
	protected boolean autopilotEnabled;
	
//	protected boolean 
	
	class shooter2 extends TimerTask
	{
		public void run()
		{
//			System.out.println("Shoot2 timer");
			if(!shootUpToSpeed)
	    	{
	    		shootAccel = true;
	    		shootUpToSpeed = true;
	    		timer = null;
	    		pushBall(true);
	    		timer2 = new Timer();
	    		timer2.schedule(new shooter2(), Parameters.kShooterPitchingMachineAccelTimeout);
	    	}
	    	else
	    	{
	    		shootAccel = false;
	    		shooting = false;
	    		shootUpToSpeed = false;
	    		manualRunPitchingMachine(0);
	    		pushBall(false);
	    		tiltSetpoint = ShooterState.kLowBar;
	    	}
		}
	}
	
	public void setAutoPilot(boolean val)
	{
		autopilotEnabled = val;
	}
	
	public void shoot2()
	{
		if(!shooting)
		{
//			System.out.println("Shooter 2");
			rightPitchingMotor.set(Parameters.kShooterShootPitchingMachineSpeed);
	    	leftPitchingMotor.set(-Parameters.kShooterShootPitchingMachineSpeed);
	    	shooting = true;
	    	shootAccel = true;
	    	shootUpToSpeed = false;
	    	timer = new Timer();
	    	long timeout = Parameters.kShooterPitchingMachineAccelTimeout;
	    	timer.schedule(new shooter2(),timeout);
		}
	}
	
    @objid ("e8c46368-8549-4701-acd1-6a7a1b073c83")
    public Shooter() {
    	rightPitchingMotor = new CANTalon(Parameters.kRightShooterPitcherMotorCanId);
    	leftPitchingMotor = new CANTalon(Parameters.kLeftShooterPitcherMotorCanId);
    	tiltMotor = new CANTalon(Parameters.kShooterAngleMotorCanId);
    	//TODO: Check motor voltage to direction
    	tiltMotor.enableLimitSwitch(true, false);
    	
    	rightPitchingMotor.enableBrakeMode(true);
    	leftPitchingMotor.enableBrakeMode(true);
    	tiltMotor.enableBrakeMode(true);
    	
    	tiltMotor.configMaxOutputVoltage(Parameters.kShooterTiltMaxVolt);
    	
    	ballShooter = new Solenoid(Parameters.kShooterBallShooterSolenoidChanel);
    	ballShooter.set(false);
    	
    	tiltSetpoint = ShooterState.kUnknown;
    	
    	// Disable tilt position control until we've reached the home position
    	disablePitchingMachineSpeedControl();
    	disableTiltPositionControl();
    	shooting = false;
    	autopilotEnabled = false;
    }

    /**
     * 
     */
    public void disableTiltPositionControl() {
    	tiltMotor.disable();
    	tiltMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	tiltMotor.configEncoderCodesPerRev(Parameters.kEncoderCodesPerRev);
    	tiltMotor.changeControlMode(TalonControlMode.PercentVbus);
    	tiltMotor.enable();     
    	autopilotEnabled = false;
    }
    
    /**
     * 
     */
    public void enableTiltPositionControl() {
    	tiltMotor.disable();
    	tiltMotor.changeControlMode(TalonControlMode.Position);
    	tiltMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
    	tiltMotor.configEncoderCodesPerRev(Parameters.kEncoderCodesPerRev);
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
    	autopilotEnabled = false;
    }
    
    /**
     * 
     */
    public void enablePitchingMachineSpeedControl() {
    	rightPitchingMotor.disable();
    	leftPitchingMotor.disable();    	
    	
    	rightPitchingMotor.changeControlMode(TalonControlMode.Speed);
    	rightPitchingMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
    	
    	leftPitchingMotor.configEncoderCodesPerRev(Parameters.kEncoderCodesPerRev);
    	rightPitchingMotor.configEncoderCodesPerRev(Parameters.kEncoderCodesPerRev);
    	
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
    	shootAccel = true;
    	shootUpToSpeed = false;
    	timer = new Timer();
    	long timeout = Parameters.kShooterPitchingMachineAccelTimeout;
    	timer.schedule(this,timeout);
    	//TODO: Add java timer for shoot up to speed
    }
    /**
     * TODO: Should this be protected?
     */
    public void reload() {
    	rightPitchingMotor.set(-Parameters.kShooterReloadPitchingMachineSpeed);
    	leftPitchingMotor.set(Parameters.kShooterReloadPitchingMachineSpeed);
    }

    /**
     * This method tells the shooter tilt angle to move to one of its pre-defined setpoints
     * 
     * @param shooterPosition the pre-defined shooter setpoint to move to
     */
    @objid ("f1761ce3-8d39-43f8-9b3c-6adaedaec7ad")
    public void setShootAngle(ShooterState shooterPosition) {
    	tiltSetpoint = shooterPosition;
    }
    /**
     * TODO: If motor leads get swaped fix the logic
     * @return
     */
    public double getMotorPower()
    {
    	double rc = 0;
    	if(isTiltAngleAtSetpoint())
    	{
    		rc = 0;
    	}
    	else if(memSetpointSet)
    	{
    		
    		/**
    		 * 
    		 * 
    		 * Added the several if else statements to try to get the shooter faster when seeking the setpoint
    		 * 
    		 */
    		double differenceToCompare = tiltMotor.getPosition()-tiltMemSetpoint;
    	
    		if(tiltMotor.getPosition() > tiltMemSetpoint)
    		{
    			if (differenceToCompare < 0.1)
    			{
    				rc = Parameters.kShooterSeekHomePower * 0.20;
    			}
    			else if(differenceToCompare < 0.15)
    			{
    				rc = Parameters.kShooterSeekHomePower * 0.35;
    			}
    			else if (differenceToCompare < 0.25)
    			{
    				rc = Parameters.kShooterSeekHomePower * 0.5;
    			}
    			else
    			{
    				rc = Parameters.kShooterSeekHomePower;
    			}
    			
    		}
    		else if(tiltMotor.getPosition() < tiltMemSetpoint)
    		{
    			if(differenceToCompare > -0.1)
    			{
    				rc = -Parameters.kShooterSeekHomePower*0.1;
    			}
    			if(differenceToCompare > -0.15)
    			{
    				rc = -Parameters.kShooterSeekHomePower*0.2;
    			}
    			if(differenceToCompare > -0.25)
    			{
    				rc = -Parameters.kShooterSeekHomePower*0.35;
    			}
    			else
    			{
    				rc = -Parameters.kShooterSeekHomePower*0.65;
    			}
    		}
    	}
    	else if(tiltMotor.getPosition() > tiltSetpoint.getPosition())
    	{
    		rc = Parameters.kShooterSeekHomePower;
    	}
    	else if(tiltMotor.getPosition() < tiltSetpoint.getPosition())
    	{
    		rc = -Parameters.kShooterSeekHomePower*0.65;
    	}
    	return rc;
    }
    /**
     * Resets the tilt angle to zero "Homing"
     */
    public void resetTiltAngle()
    {
    	tiltMotor.setPosition(0);
    }
    /**
     * 
     * @param value
     */
    public void manualRunTiltMotor(double value)
    {
    	disableTiltPositionControl();
    	tiltMotor.set(value);
    }
    
    /**
     * Manually run the pitching machine.
     * 
     * @param value percentage of power to run the pitching machine motors in the range of
     *        (-1.0 .. 0 .. 1.0) where 1.0 is 100% forward and -1.0 is 100% reverse.
     */
    @objid ("eba4d1df-2aa2-4f39-b3a7-f0ca4f9b6626")
    public void manualRunPitchingMachine(double value) {
    	if (isPitchingMachineSpeedControlEnabled())
    	{
    		disablePitchingMachineSpeedControl();
    	}
    	rightPitchingMotor.set(value);
    	leftPitchingMotor.set(-value);
    	autopilotEnabled = false;
    }

    /**
     * 
     * @return
     */
    @objid ("17dd7815-19f9-4e9f-80bb-74ef735538d4")
    public boolean isPitchingMachineUpToSpeed() {
    	if(shootAccel && shootUpToSpeed){
    		return true;
    	}
    	else
    	{
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
    
    /**
     * 
     * @return  The tilt Memory setpoint
     */
    @objid ("bf56ebbc-3451-4f51-88e0-c944b48b9819")
    public double getTiltMemSetpoint() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.tiltMemSetpoint;
    }
    
    /**
     * 
     * @return Shooter state
     */
    public ShooterState getTiltSetpoint()
    {
    	return tiltSetpoint;
    }
    
    /**
     * 
     * @return Current tilt motor position
     */
    public double getTiltAngle(){
    	return tiltMotor.getPosition();
    }

    /**
     * <Enter note text here>
     */
    @objid ("89bd53b8-23c9-4a16-adb2-95ec7d912696")
    public void setTiltMemSetpoint(double value) {
    	autopilotEnabled = true;
    	memSetpointSet = true;
        tiltMemSetpoint = value;
    }
    
    /**
     * 
     * @return boolean - true if pitching machine motors are on, false otherwise
     */
    @objid ("f6797d96-b58a-47ee-a41a-4ea7709c5062")
    public boolean isPitchingMachineOn() {
    	double voltage = leftPitchingMotor.getOutputVoltage();
    	if (voltage <= Parameters.kMotorVoltageDeadband || voltage >= (-1.0 * Parameters.kMotorVoltageDeadband))
    	{
    		return false;
    	}
    	return true;
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
    	if(tiltSetpoint == ShooterState.kUnknown)
    	{
    		return false;
    	}
    	return true;
    }

    /**
     * <Enter note text here>
     */
    @objid ("4d7bd443-0329-4985-8729-3ec742465875")
    public boolean isTiltAngleAtSetpoint() {
    	// Figure out if tilt angle is "close enough" to setpoint
    	
    	if(memSetpointSet)
    	{
    		if(getTiltAngle() >= tiltMemSetpoint-Parameters.kShooterSeekTolerance && getTiltAngle() <=tiltMemSetpoint+Parameters.kShooterSeekTolerance)
    		{
    			return true;
    		}
    	}
    	else 
    	{
    		if(getTiltAngle() >= tiltSetpoint.getPosition()-Parameters.kShooterSeekTolerance && getTiltAngle() <= tiltSetpoint.getPosition()+Parameters.kShooterSeekTolerance)
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean isShooterHome()
    {
    	return (currentPosition == ShooterState.kHome);
    }
    
    public void setShooterAngle(double angleSetpoint)
    {
    	double setpoint = Math.toRadians(angleSetpoint);    	
    	double tan = Math.tan(setpoint);
    	
//    	double constant = Parameters.kGoalHeight/Parameters.kShooterOffSetFromCamera;
    	double h = Parameters.kGoalHeight;   	
    	double d = Parameters.kShooterOffSetFromCameraZ;
    	double hgd = Parameters.kHeightGoalDifference;
    	
    	double angleInRads = Math.atan((h+hgd)/((h/tan)+d));
    	double newAngle = Math.toDegrees(angleInRads);
//    	double offset = (-0.0284*newAngle)+2.139;
//    	offset = offset/12;
//    	angleInRads = Math.atan((h+offset)/((h/tan)+d));
//    	newAngle = Math.toDegrees(angleInRads);
//    	newAngle -= 5;
    	double newPosition = (-3.7*newAngle/68.5)+3.7;
//    	System.out.println("Setpoint: "+setpoint);
//    	System.out.println("tan: "+tan);
//    	System.out.println("constant: "+constant);
//    	System.out.println("angle in rads: "+angleInRads);
//    	System.out.println("new angle: "+newAngle);
//    	System.out.println("New position: "+newPosition);
    	setTiltMemSetpoint(newPosition);
    }
    
    public double getShooterAngle()
    {
    	return ((3.7-tiltMotor.getPosition())*68.5/3.7);
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
    	SmartDashboard.putNumber("Tilt Voltage", tiltMotor.getOutputVoltage());
    	SmartDashboard.putNumber("Tilt Current", tiltMotor.getOutputCurrent());
    	SmartDashboard.putNumber("L Volt", leftPitchingMotor.getOutputVoltage());
    	SmartDashboard.putNumber("L Current", leftPitchingMotor.getOutputCurrent());
    	SmartDashboard.putNumber("R Volt", rightPitchingMotor.getOutputVoltage());
    	SmartDashboard.putNumber("R Current",rightPitchingMotor.getOutputCurrent());
    	SmartDashboard.putBoolean("Tilt Up Limit",tiltMotor.isFwdLimitSwitchClosed());
    	SmartDashboard.putBoolean("Tilt Down Limit",tiltMotor.isRevLimitSwitchClosed());
    	SmartDashboard.putNumber("Tilt Pos", tiltMotor.getPosition());
    	SmartDashboard.putNumber("Shooter Tilt Angle",((3.7-tiltMotor.getPosition())*68.5/3.7));
    	SmartDashboard.putNumber("MemSetpoint", tiltMemSetpoint);
    	if(currentPosition != null)
    		SmartDashboard.putString("Shooter State", currentPosition.name());
    	else
    		SmartDashboard.putString("Shooter State", "UNKNOWN");
//    	double tiltPosition = tiltMotor.getPosition();
    	SmartDashboard.putBoolean("Is at setpoint", isTiltAngleAtSetpoint());
    	//TODO: Check for fwd or rev if motor power swapped
    	if(tiltMotor.isFwdLimitSwitchClosed())
    	{
    		tiltMotor.setPosition(0);
    		currentPosition = ShooterState.kHome;
    		memSetpointSet = false;
    	}
    	//PitchingMachine method just retracts kicker, extends dink and turns motor off
    	if(tiltMotor.getOutputCurrent() > Parameters.kShooterTiltMaxCurrent)
    	{
    		disableTiltPositionControl();
    		currentPosition = ShooterState.kUnknown;
    		tiltMotor.set(0);
    		autopilotEnabled = false;
    	}
   	 	if(isAutoPilotEnabled())
   	 	{
	 		if(currentPosition == ShooterState.kUnknown)
	 		{
	 			// We don't know the shooter's tilt angle, run the shooter in reverse at a slow
	   			// constant speed until we reach the reverse limit switch.
	   			disableTiltPositionControl();
	   			tiltMotor.set(Parameters.kShooterSeekHomePower);
	   			if (tiltMotor.isFwdLimitSwitchClosed());
	   			{
	   				currentPosition = ShooterState.kHome;
	   				tiltMotor.set(0.0);
	   				enableTiltPositionControl();
	   			}
	   			
	 		}
	 		else if (!memSetpointSet)
	 		{
		   		switch (tiltSetpoint) 
		   		{
			   		case kUnknown:
			   			System.out.println("SHOULD NEVER HAPPEN IN SHOOTER");
			   			break;
			   		
			   		case kHome:
			   		case kLowBar:
			   			manualRunPitchingMachine(0.0);
			   			if(isTiltAngleAtSetpoint())
			   			{
			   				currentPosition = tiltSetpoint;
			   				tiltMotor.set(0.0);
			   			}
			   			else
			   			{
			   				tiltMotor.set(getMotorPower());
			   			}
			   			break;
			   			
			   		case kReload:
			   			if(isTiltAngleAtSetpoint())
			   			{
			   				currentPosition = tiltSetpoint;
			   				tiltMotor.set(0.0);
			   				manualRunPitchingMachine(Parameters.kShooterReloadPitchingMachineSpeed);
			   			}
			   			else
			   			{
			   				tiltMotor.set(getMotorPower());
			   			}
			   			break;
			   			
			   		case kShootBatter:
			   		case kShootTape:
			   		case kShootDefense:
			   			if(isTiltAngleAtSetpoint())
			   			{
			   				currentPosition = tiltSetpoint;
			   				tiltMotor.set(0.0);
			   				if(!shootAccel && !shootUpToSpeed && !shooting)
			   				{
			   					shoot();			   					
			   				}
			   				else if(shooting && isPitchingMachineUpToSpeed())
			   				{
			   					pushBall(true);
			   					if(timer != null)
			   					{
			   						timer = null;
			   					}
			   					timer = new Timer();
			   					timer.schedule(this, Parameters.kShooterPitchingMachinePushBallTimeout);
			   					shooting = false;
			   				}
			   			}
			   			else
			   			{
			   				tiltMotor.set(getMotorPower());
			   			}

			   			break;
		   		}
	 		}
	 		else
	 		{
	 			//We are moving to a custom setpoint
	   			if(isTiltAngleAtSetpoint())
	   			{
	   				tiltMotor.set(0.0);
	   				memSetpointSet = false;
	   			}
	   			else
	   			{
	   				tiltMotor.set(getMotorPower());
	   			}
	 		}
   	 	}
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
     * 
     * @return returns weather autopilot is enabled or disabled
     */
    public boolean isAutoPilotEnabled(){
    	return autopilotEnabled;
    }
//    
//    class pushShooter extends TimerTask
//    {
//		@Override
//		public void run() {
//			shootUpToSpeed = true;
//		}
//    	
//    }
//    
//    class stopPitching extends TimerTask
//    {
//    	public void run()
//    	{
//    		manualRunPitchingMachine(0);
//    	}
//    }
//    
    /**
     * Called when timer task expires
     */
    public void run()
    {
    	if(!shootUpToSpeed)
    	{
    		shootAccel = true;
    		shootUpToSpeed = true;
    	}
    	else
    	{
    		shootAccel = false;
    		shooting = false;
    		shootUpToSpeed = false;
    		manualRunPitchingMachine(0);
    		tiltSetpoint = ShooterState.kLowBar;
    	}
    }
    
    /**
     * 
     * @author Robotics
     */
    @objid ("08844018-94d8-4017-af1b-f834faa0871b")
    public enum ShooterState {

         /**
         * This setpoint puts the Shooter tilt angle at the home position
         */
        kHome(Parameters.kShooterTiltHomePositionEncoderSetpoint),
        
        /**
         * This represents the shooter tilt angle is in an unknown position.  This is the initial state of the robot until it is homed.
         */
        kUnknown(null),
        /**
         * 
         */
        kShoot(-1),
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
    	ShooterState(double pos)
    	{
    		position = pos;
    	}
    	ShooterState(Object obj)
    	{	
    	}
    	public double getPosition()
    	{
    		return position;
    	}
    }

}