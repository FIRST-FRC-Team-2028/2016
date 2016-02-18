package com.PhantomMentalists.Stronghold.Autopilot;

import com.PhantomMentalists.Stronghold.ClimbingArm;
import com.PhantomMentalists.Stronghold.Parameters;
import com.PhantomMentalists.Stronghold.PusherArm;
import com.PhantomMentalists.Stronghold.Shooter;
import com.PhantomMentalists.Stronghold.WestCoastDrive;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * This is an abstract base class.  It is used as the base class for a hierarchy of
 * concrete classes, each designed to traverse a single defense or perform a specific
 * action (such as aiming and shooting the boulder).
 */
@objid ("51808c75-6bd6-4f90-975a-784b714c5fba")
public abstract class Autopilot {
    /**
     * <Enter note text here>
     */
    @objid ("4fdd9cb6-a7ec-4b08-b502-67f8c957d9be")
    protected boolean enabled;

    /**
     * <Enter note text here>
     */
    @objid ("d74f2d0e-ec97-42d2-84ae-1b25814a0283")
    protected WestCoastDrive drive;

    /**
     * <Enter note text here>
     */
    @objid ("d4fa75ce-c898-41d5-a904-6dac0d12343c")
    protected PusherArm pusherArm;

    /**
     * <Enter note text here>
     */
    @objid ("0e3784c5-5226-470d-b800-ddab465fb900")
    protected Shooter shooter;

    /**
     * <Enter note text here>
     */
    @objid ("09cd8f95-bffd-403e-8326-2ee2956eab31")
    protected ClimbingArm climbingArm;

    /**
     * <Enter note text here>
     */
    @objid ("35e9e305-e9fe-407b-b2d3-d11900535076")
    public abstract void process();

    /**
     * <Enter note text here>
     */
    @objid ("c0750894-6c97-460b-a3be-801d31733962")
    public Autopilot(WestCoastDrive drive, Shooter shooter, PusherArm pusherArm, ClimbingArm climberArm) {
    }

    /**
     * <Enter note text here>
     */
    @objid ("63e39f0e-afb4-40ba-a73a-0998c5997f41")
    public boolean isEnabled() {
        return false;
    }

    /**
     * <Enter note text here>
     */
    @objid ("fd1d5c3f-9dc1-4bf4-8f73-d86fa21874d2")
    public void setEnabled(boolean value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.enabled = value;
    }

}
