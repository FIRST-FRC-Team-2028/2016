package com.PhantomMentalists.Stronghold;

import com.PhantomMentalists.Stronghold.Parameters;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * This class encapsulates the state (attributes) and logic (methods) required to control
 * Telepath's shooter to score points by hurling a boulder into the high goal.
 * 
 * @author Nazhere and Moose
 */
@objid ("e7c0967b-bd2b-41d7-a528-2d3f463133d3")
public class Shooter {
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
	
	/**
	 * Internal variable that indicates the shooter is in the process of shooting.
	 * This attribute is set by the shoot() method and remains true until the ball
	 * is away.
	 */
	protected boolean shooting;
	
	/**
	 * Internal variable that indicates the shooter is in the process of reloading.
	 * This attribute is set by the shoot() method and remains true until we sense
	 * a ball in the shooter (or it is cancelled by the driver).  
	 */
	protected boolean reloading;

    @objid ("e8c46368-8549-4701-acd1-6a7a1b073c83")
    public Shooter() {
    	rightPitchingMotor = new CANTalon(Parameters.kRightShooterPitcherMotorCanId);
    	leftPitchingMotor = new CANTalon(Parameters.kLeftShooterPitcherMotorCanId);
    	tiltMotor = new CANTalon(Parameters.kShooterAngleMotorCanId);
    	
    	rightPitchingMotor.enableBrakeMode(true);
    	leftPitchingMotor.enableBrakeMode(true);
    	tiltMotor.enableBrakeMode(true);
    	ballShooter = new Solenoid(Parameters.kShooterBallShooterSolenoidChanel);
    	ballShooter.set(false);
    	dink = new Solenoid(Parameters.kShooterDinkSolenoidChanel);
    	dink.set(true);
    	position = ShooterPosition.kUnknown;
    	enablePitchingMachineSpeedControl();
    	// Disable tilt position control until we've reached the home position
    	disableTiltPositionControl();
    	shooting = false;
    	reloading = false;
    }

    /**
     * 
     */
    public void disableTiltPositionControl() {
    	tiltMotor.disable();
    	tiltMotor.changeControlMode(CANTalon.ControlMode.PercentVbus);
    	tiltMotor.enable();
    	tiltMotor.set(0);
    }
    
    /**
     * 
     */
    public void enableTiltPositionControl() {
    	tiltMotor.disable();
    	tiltMotor.changeControlMode(CANTalon.ControlMode.Position);
    	tiltMotor.setFeedbackDevice(device);
    	tiltMotor.setPID(Parameters.kShootTiltPositionControlProportional, 
			 	Parameters.kShootTiltPositionControlIntegral, 
			 	Parameters.kShootTiltPositionControlDifferential, 
			 	Parameters.kShootTiltPositionControlThrottle);
    	tiltMotor.enable();
    }
    
    /**
     * 
     */
    public void disablePitchingMachineSpeedControl() {
    	rightPitchingMotor.disable();
    	leftPitchingMotor.disable();
    	rightPitchingMotor.changeControlMode(CANTalon.ControlMode.PercentVbus);
    	leftPitchingMotor.changeControlMode(CANTalon.ControlMode.PercentVbus);
    	rightPitchingMotor.enable();    	
    	leftPitchingMotor.enable();
    }
    
    /**
     * 
     */
    public void enablePitchingMachineSpeedControl() {
    	rightPitchingMotor.disable();
    	leftPitchingMotor.disable();    	
    	rightPitchingMotor.changeControlMode(CANTalon.ControlMode.Speed);
    	rightPitchingMotor.setFeedbackDevice(device);
    	rightPitchingMotor.setPID(Parameters.kShootPitchPositionControlProportional, 
			 	Parameters.kShootPitchPositionControlIntegral, 
			 	Parameters.kShootPitchPositionControlDifferential, 
			 	Parameters.kShootPitchPositionControlThrottle);    	
    	leftPitchingMotor.changeControlMode(CANTalon.ControlMode.Speed);
    	leftPitchingMotor.setFeedbackDevice(device);
    	leftPitchingMotor.setPID(Parameters.kShootPitchPositionControlProportional, 
			 	Parameters.kShootPitchPositionControlIntegral, 
			 	Parameters.kShootPitchPositionControlDifferential, 
			 	Parameters.kShootPitchPositionControlThrottle);
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
        	leftPitchingMotor.set(-1.0 * Parameters.kShooterShootPitchingMachineSpeed);
        	shooting = true;
    }

    public void reload() {
    	rightPitchingMotor.set(-1.0 * Parameters.kShooterReloadPitchingMachineSpeed);
    	leftPitchingMotor.set(Parameters.kShooterReloadPitchingMachineSpeed);
    	reloading = true;
    }
    
    /**
     * Tells if a ball is in the shooter.
     * 
     * @return true if a ball is in the shooter, false otherwise
     */
    @objid ("91e9f52a-d664-4f68-ba8b-4e5503fe8eda")
    public boolean isBallLoaded() {
    	return ballSensor.get();
    	//
    }

    /**
     * This method tells the shooter tilt angle to move to one of its pre-defined setpoints
     * 
     * @param shooterPosition the pre-defined shooter setpoint to move to
     */
    @objid ("f1761ce3-8d39-43f8-9b3c-6adaedaec7ad")
    public void setShootAngle(ShooterPosition shooterPosition) {
    	position = shooterPosition;
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
    	leftPitchingMotor.set(-1.0 * value);
    }

    @objid ("17dd7815-19f9-4e9f-80bb-74ef735538d4")
    public boolean isUpToSpeed() {
//    	if(rightPitchingMotor && leftPitchingMotor){
//    		return true;
//    	}
//    	else{
//    		return false;
//    	}
//        //if motor is up to speed return true else return false.
//    	//This is not correct but it is a shot
//        
//       
        return false;
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
    public boolean isPositionAtSetpoint() {
    	return false;
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
    }
    
    /**
     * 
     * @return
     */
    public boolean isPitchingMachineSpeedControlEnabled() {
    	return (leftPitchingMotor.getControlMode() == CANTalon.ControlMode.Speed);
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
    
    @objid ("08844018-94d8-4017-af1b-f834faa0871b")
    public enum ShooterPosition {
         /**
         * This setpoint puts the Shooter tilt angle at the home position
         */
        kHome,
        
        /**
         * This represents the shooter tilt angle is in an unknown position.  This is the initial state of the robot until it is homed.
         */
        kUnknown,
        
        /**
         * This setpoint holds the tilt angle of the shooter for shooting at the goal from the batter position.
         */
        kShootBatter,
        
        /**
         * This setpoint indicates the shooter tilt angle is correct for shooting when the robot is lined up on the tape in the center of the courtyard.
         */
        kShootTape,
        
        /**
         * This setpoint aligns the shooter tilt angle to take a shot from the couryard just inside the defense.
         */
        kShootDefense,
        
        /**
         * This setpoint puts the shooter tilt angle at the full-down position to clear the low bar.
         */
        kLowBar,
        
        /**
         * This setpoint holds the shooter tilt angle to the full-down position and runs the pitching machine motors in reverse until a ball is loaded.
         */
        kReload;
    }

}