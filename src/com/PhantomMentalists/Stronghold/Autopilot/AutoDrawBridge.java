package com.PhantomMentalists.Stronghold.Autopilot;

import com.PhantomMentalists.Stronghold.ClimbingArm;
import com.PhantomMentalists.Stronghold.PusherArm;
import com.PhantomMentalists.Stronghold.Shooter;
import com.PhantomMentalists.Stronghold.WestCoastDrive;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import edu.wpi.first.wpilibj.DigitalInput;

@objid ("38a40f7b-cbe0-4b5c-be22-96454d76cbba")
public class AutoDrawBridge extends Autopilot {
    @objid ("0dd09066-9924-467d-a9c3-807c4574c413")
    public AutoDrawBridge(WestCoastDrive drive, Shooter shooter, PusherArm pusherArm, ClimbingArm climbingArm, DigitalInput lTape, DigitalInput rTape) {
        super(drive, shooter, pusherArm, climbingArm, lTape, rTape);
    }

    @objid ("092d8c5b-fd87-474b-831e-47f3b45b35b0")
    public void process() {
    }

}
