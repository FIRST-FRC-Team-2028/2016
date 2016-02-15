package com.PhantomMentalists.Stronghold.Autopilot;

import com.PhantomMentalists.Stronghold.ClimbingArm;
import com.PhantomMentalists.Stronghold.Parameters;
import com.PhantomMentalists.Stronghold.PusherArm;
import com.PhantomMentalists.Stronghold.Shooter;
import com.PhantomMentalists.Stronghold.WestCoastDrive;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("2e7b2583-3a04-4188-beec-544aff2708ac")
public class AutoMoat extends Autopilot {
    /**
     * <Enter note text here>
     */
    @objid ("e1bf47bf-543a-45f6-b369-e5a8c5869185")
    public AutoMoat(WestCoastDrive drive, Shooter shooter, PusherArm pusherArm, ClimbingArm climbingArm) {
        super(drive, shooter, pusherArm, climbingArm);
    }

    @objid ("46ef2765-db09-4fc1-a9a8-04e9fb7c4ff9")
    public void process() {
    }

}
