package com.PhantomMentalists.Stronghold;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * This class stores all constant variables for team 2028.  Everything in this class should be a static public final.  There will be no instances of this class and no class will hold a reference to any instance.
 */
@objid ("a5f86b3a-a108-4159-9a04-9f7eb8bd5490")
public class Parameters {
    /**
     * CAN ID for the first (primary) motor on the left-side Drive Unit.
     */
    @objid ("a46d1d4c-8185-43c1-abd5-648d21706f50")
    public static final int kLeftMotor1CanId = 21;

    /**
     * CAN ID for the second motor powering the Left Drive Unit
     */
    @objid ("1421bb0d-9370-4be8-8702-e551cebf019f")
    public static final int kLeftMotor2CanId = 12;

    /**
     * This value stores the IP address to the Axis Camera for team 2028.
     */
    @objid ("a8706581-4b3f-426e-9cb9-43165cb99074")
    public static final String kCameraIpAddress = "10.20.28.11";

}
