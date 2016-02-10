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
    	position = ShooterPosition.kUnkown;
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
    	position = ShooterPosition.kLow;
    	rightPitchingMotor.set(-1);
    	leftPitchingMotor.set(1);
    }

    @objid ("eba4d1df-2aa2-4f39-b3a7-f0ca4f9b6626")
    public void manualRunShooter() {
    	position = ShooterPosition.kHigh;
    	rightPitchingMotor.set(1);
    	leftPitchingMotor.set(-1);
    }

    @objid ("17dd7815-19f9-4e9f-80bb-74ef735538d4")
    public boolean isUpToSpeed() {
    	if(rightPitchingMotor && leftPitchingMotor.isUpToSpeed){
    		return true;
    	}
    	else{
    		return false;
    	}
        //if motor is up to speed return true else return false.
    	//This is not correct but it is a shot
        
       
    }

    @objid ("08844018-94d8-4017-af1b-f834faa0871b")
    public enum ShooterPosition {
        kLow,
        kMedium,
        kUnkown,
        kHigh;
    	//Do we need a kHome and kReload 
    }

}
