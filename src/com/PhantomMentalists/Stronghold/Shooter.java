package com.PhantomMentalists.Stronghold;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * This class encapsulates the state (attributes) and logic (methods) required to control Telepath's shooter to score points by hurling a boulder into the high goal.
 */
@objid ("e7c0967b-bd2b-41d7-a528-2d3f463133d3")
public class Shooter {
    @objid ("6684d714-fbb6-42f7-91d5-a80c4aa7b92e")
    protected DigitalInput ballSensor;

    @objid ("75dc4e42-0ff3-473e-ba0e-7afcd800dd26")
    public List<CANTalon> pitchingMachineMotors = new ArrayList<CANTalon> ();

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

    @objid ("d1df8542-0d68-4c46-a1e7-cd12eb759c65")
    public void manualRunShooterHood() {
    }

    @objid ("eba4d1df-2aa2-4f39-b3a7-f0ca4f9b6626")
    public void manualRunShooter() {
    }

    @objid ("08844018-94d8-4017-af1b-f834faa0871b")
    public enum ShooterPosition {
        kLow,
        kMedium,
        kHigh;
    }

}
