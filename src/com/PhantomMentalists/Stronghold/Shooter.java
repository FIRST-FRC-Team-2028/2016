package com.PhantomMentalists.Stronghold;

import com.PhantomMentalists.Stronghold.Parameters;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * This class encapsulates the state (attributes) and logic (methods) required to control Telepath's shooter to score points by hurling a boulder into the high goal.
 * 
 * @author Nazhere and Moose
 */
@objid ("e7c0967b-bd2b-41d7-a528-2d3f463133d3")
public class Shooter {
	protected ShooterPosition position;
	
	
    @objid ("871f3fd5-0de0-4432-868a-82369fcf07de")
    public Solenoid Kicker;

    /**
     * <Enter note text here>
     */
    @objid ("54c45f11-335d-432c-93c6-f1a8354239b9")
    protected DigitalInput ballSensor;

    /**
     * <Enter note text here>
     */
    @objid ("fde5fcfa-15dc-4eae-a052-65112042635a")
    protected CANTalon leftPitchingMotor;

    /**
     * <Enter note text here>
     */
    @objid ("e22e0db7-a4cb-46a2-85aa-b7a1de718fc6")
    protected CANTalon rightPitchingMotor;

    @objid ("01c41837-6db2-47af-ba2f-e34cc6166b56")
    protected CANTalon tiltMotor;

    @objid ("2c653aa8-a80f-4797-975e-a6799cc7f9aa")
    protected Solenoid ballShooter;


	private double tiltSetpoint;

    @objid ("e8c46368-8549-4701-acd1-6a7a1b073c83")
    public Shooter() {
    	rightPitchingMotor = new CANTalon(Parameters.kRightShooterPitcherMotorCanId);
    	leftPitchingMotor = new CANTalon(Parameters.kLeftShooterPitcherMotorCanId);
    	tiltMotor = new CANTalon(Parameters.kShooterAngleMotorCanId);
    	
    	rightPitchingMotor.enableBrakeMode(true);
    	leftPitchingMotor.enableBrakeMode(true);
    	tiltMotor.enableBrakeMode(true);
    	ballShooter.set(false);
    	ballSensor.get();
    	Kicker.set(false);
    	position = ShooterPosition.kUnknown;
    	rightPitchingMotor.changeControlMode(TalonControlMode.Speed);
    	leftPitchingMotor.changeControlMode(TalonControlMode.Speed);
    	tiltMotor.changeControlMode(TalonControlMode.Position);
    	
    	
    }

    @objid ("af588d71-18eb-4098-95a6-d69164a77e41")
    public void shoot() {
    	
    		rightPitchingMotor.set(1);
        	leftPitchingMotor.set(-1);
        	// this needs kicker after up to speed
    	
    }

    @objid ("91e9f52a-d664-4f68-ba8b-4e5503fe8eda")
    public boolean isBallLoaded() {
    	return ballSensor.get();
    	//
    }

    @objid ("f1761ce3-8d39-43f8-9b3c-6adaedaec7ad")
    public void setShootAngle(ShooterPosition shooterPosition) {
    	tiltMotor.getEncPosition();
    	tiltMotor.set(0);
    	//
    }

    @objid ("f0ee0c60-695b-452c-b734-44653ea8b4fd")
    public void manualRunBallFeeder() {
    	position = ShooterPosition.kReload;
    	rightPitchingMotor.set(-1);
    	leftPitchingMotor.set(1);
    }

    @objid ("eba4d1df-2aa2-4f39-b3a7-f0ca4f9b6626")
    public void manualRunShooter() {
    	position = ShooterPosition.kShootTape;
    	rightPitchingMotor.set(1);
    	leftPitchingMotor.set(-1);
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
    }

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
     */
    @objid ("f6797d96-b58a-47ee-a41a-4ea7709c5062")
    public boolean isPitchingMachineOn() {
    	return false;
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
