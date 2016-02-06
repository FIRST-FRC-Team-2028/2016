package com.PhantomMentalists.Stronghold.Autopilot;

import com.PhantomMentalists.Stronghold.ClimbingArm;
import com.PhantomMentalists.Stronghold.DriveUnit;
import com.PhantomMentalists.Stronghold.Parameters;
import com.PhantomMentalists.Stronghold.PusherArm;
import com.PhantomMentalists.Stronghold.Shooter;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("faf35b12-f052-4aeb-b323-6c3b10671387")
public class AutoDrive extends Autopilot {
    @objid ("13eec206-2a84-4912-8c67-28d062557de8")
    public AutoDrive(DriveUnit drive, Shooter shooter, PusherArm pusherArm, ClimbingArm climbingArm) {
        super(drive, shooter, pusherArm, climbingArm);
    }

    @objid ("730f3e5c-3b87-46f6-934e-79aa04f30461")
    public void process() {
    }

}
