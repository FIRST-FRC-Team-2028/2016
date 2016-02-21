package com.PhantomMentalists.Stronghold.Autopilot;

import com.PhantomMentalists.Stronghold.ClimbingArm;
import com.PhantomMentalists.Stronghold.Parameters;
import com.PhantomMentalists.Stronghold.PusherArm;
import com.PhantomMentalists.Stronghold.Shooter;
import com.PhantomMentalists.Stronghold.WestCoastDrive;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * This class controls the Telepath robot during the 15 second autonomous period.  It instantiates its own Autopilot classes to control traversing the defense, driving to the goal and shooting.
 */
@objid ("d5c53723-495a-4d0d-9cc0-37fc89facee4")
public class Autonomous extends Autopilot {
    /**
     * <Enter note text here>
     */
    @objid ("81e544d6-b681-4b60-8197-0654c188c97b")
    protected Autopilot autopilotMode;

    @objid ("89eb6fde-adee-4a71-8d26-0f72ceaaea93")
    public void process() {
    	
    }

    /**
     * Constructor
     * 
     * This method initializes the local references to Telepath's major components.
     */
    @objid ("a472f866-0ec6-4c52-bc30-c893eb1086b3")
    public Autonomous(WestCoastDrive drive, Shooter shooter, PusherArm pusherArm, ClimbingArm climbingArm) {
        super(drive, shooter, pusherArm, climbingArm);
    }

}
