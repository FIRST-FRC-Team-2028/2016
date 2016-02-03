package com.PhantomMentalists.Stronghold;

import com.PhantomMentalists.Stronghold.Parameters;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * This class encapsulates the state (attributes) and logic (methods) required to control Telepath's shooter to score points by hurling a boulder into the high goal.
 * 
 * @author Nazhere and Moose
 */
@objid ("e7c0967b-bd2b-41d7-a528-2d3f463133d3")
public class Shooter {
    @objid ("871f3fd5-0de0-4432-868a-82369fcf07de")
    public Solenoid Kicker;

    @objid ("6684d714-fbb6-42f7-91d5-a80c4aa7b92e")
    protected DigitalInput ballSensor;

    @objid ("75dc4e42-0ff3-473e-ba0e-7afcd800dd26")
    protected CANTalon leftPitchingMotor;

    @objid ("17010cd2-3e92-4a35-ac2c-24b4698847ce")
    protected CANTalon rightPitchingMotor;

    @objid ("154896da-7aff-4686-82b6-6b3a73c3e144")
    protected CANTalon tiltMotor;

    @objid ("6e459665-5e6d-4d0f-b774-334e966614b6")
    protected Solenoid ballShooter;

    @objid ("e8c46368-8549-4701-acd1-6a7a1b073c83")
    public Shooter() {
    }

    @objid ("af588d71-18eb-4098-95a6-d69164a77e41")
    public void shoot() {
    }

    /**
     */
    @objid ("91e9f52a-d664-4f68-ba8b-4e5503fe8eda")
    public boolean isBallLoaded() {
    }

    @objid ("f1761ce3-8d39-43f8-9b3c-6adaedaec7ad")
    public void setShootAngle(ShooterPosition shooterPosition) {
    }

    @objid ("f0ee0c60-695b-452c-b734-44653ea8b4fd")
    public void manualRunBallFeeder() {
    }

    @objid ("eba4d1df-2aa2-4f39-b3a7-f0ca4f9b6626")
    public void manualRunShooter() {
    }

    /**
     */
    @objid ("17dd7815-19f9-4e9f-80bb-74ef735538d4")
    public boolean isUpToSpeed() {
    }

    @objid ("08844018-94d8-4017-af1b-f834faa0871b")
    public enum ShooterPosition {
        kLow,
        kMedium,
        kHigh;
    }

}
