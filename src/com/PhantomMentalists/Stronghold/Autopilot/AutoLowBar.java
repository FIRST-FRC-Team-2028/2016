package com.PhantomMentalists.Stronghold.Autopilot;

import com.PhantomMentalists.Stronghold.ClimbingArm;
import com.PhantomMentalists.Stronghold.PusherArm;
import com.PhantomMentalists.Stronghold.Shooter;
import com.PhantomMentalists.Stronghold.WestCoastDrive;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import edu.wpi.first.wpilibj.DigitalInput;

@objid ("6e68fcd3-ac51-46b8-9089-679dbdd5f934")
public class AutoLowBar extends Autopilot {
    /**
     * <Enter note text here>
     */
    @objid ("d7c7102f-72dd-4034-acef-1c3a8027b04e")
    public AutoLowBar(WestCoastDrive drive, Shooter shooter, PusherArm pusherArm, ClimbingArm climbingArm,DigitalInput lTape,DigitalInput rTape) 
    {
    	super(drive,shooter,pusherArm,climbingArm,lTape,rTape);
    }

    @objid ("48bf2e81-2671-4b6d-bc5e-7c79022f61d0")
    public void process() 
    {
    	
    }

}
