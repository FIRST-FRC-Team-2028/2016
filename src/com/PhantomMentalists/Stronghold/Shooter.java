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
    @objid ("4ede4b6a-ff63-46f7-9714-da4101e49e75")
    public Solenoid Kicker;

    /**
     * <Enter note text here>
     */
    @objid ("63f2413a-d988-445a-ace3-67d34b261a43")
    protected DigitalInput ballSensor;

    /**
     * <Enter note text here>
     */
    @objid ("89cdc41c-1864-4459-8d64-5f9914675c79")
    protected CANTalon leftPitchingMotor;

    /**
     * <Enter note text here>
     */
    @objid ("3149120a-5cf8-4902-bee1-1e15cfb9d14b")
    protected CANTalon rightPitchingMotor;

    @objid ("ca52f5d5-e895-4f2b-b8eb-cf991ceae8ff")
    protected CANTalon tiltMotor;

    @objid ("c541cb16-62b1-4934-bb7c-a37d4bd8f851")
    protected Solenoid ballShooter;

    @objid ("e8c46368-8549-4701-acd1-6a7a1b073c83")
    public Shooter() {
    }

    @objid ("af588d71-18eb-4098-95a6-d69164a77e41")
    public void shoot() {
    }

    @objid ("91e9f52a-d664-4f68-ba8b-4e5503fe8eda")
    public boolean isBallLoaded() {
        return false;
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

    @objid ("17dd7815-19f9-4e9f-80bb-74ef735538d4")
    public boolean isUpToSpeed() {
        return false;
    }

    @objid ("08844018-94d8-4017-af1b-f834faa0871b")
    public enum ShooterPosition {
        kLow,
        kMedium,
        kHigh;
    }

}
