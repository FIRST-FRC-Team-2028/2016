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
    public static final int kLeftMasterDriveMotorCanId = 21;

    /**
     * CAN ID for the second motor powering the Left Drive Unit
     */
    @objid ("1421bb0d-9370-4be8-8702-e551cebf019f")
    public static final int kLeftFollowerDriveMotorCanId = 22;

    /**
     * <Enter note text here>
     */
    @objid ("d59d9116-a49e-40ed-a68d-7ae8dc108c5e")
    public static final int kRightMasterDriveMotorCanId = 11;

    /**
     * <Enter note text here>
     */
    @objid ("440bfe17-9a55-44dc-b53e-96e5a0be2911")
    public static final int kRightFollowerDriveMotorCanId = 12;

    /**
     * <Enter note text here>
     */
    @objid ("2a05fc7b-925f-4bec-a68e-8ead1eada820")
    public static final int kPusherArmMotorCanId = 51;

    @objid ("2deeb224-65ff-4963-8cac-4c00697bb3b9")
    public static final int kShooterAngleMotorCanId = 33;

    @objid ("ba76d02f-5cb3-45d4-87d4-7c0f38f7372d")
    public static final int kLeftShooterPitcherMotorCanId = 32;

    @objid ("02b639b3-8099-4c45-83ab-463996000324")
    public static final int kRightShooterPitcherMotorCanId = 31;

    /**
     * <Enter note text here>
     */
    @objid ("fe6208ba-4d13-4928-b0c0-dea88626ef4e")
    public static final String kClimberAngleMotorCanId = 43;

    /**
     * <Enter note text here>
     */
    @objid ("ad953a2c-2244-4439-a2aa-0e86cd05d5c5")
    public static final int kClimberLeftWinchMotorCanId = 42;

    /**
     * <Enter note text here>
     */
    @objid ("2b176629-4eaf-473e-9106-e8f7f32633de")
    public static final int kClimberRightWinchMotorCanId = 41;

    @objid ("48388446-73d2-468b-9b56-780f9bd7f5e7")
    public static final int kClimberExtendMotorCanId = 44;

    /**
     * <Enter note text here>
     */
    @objid ("84c6a947-3dd9-40ea-a483-650ecea1dbfb")
    public static final String kCameraIpAddress = 10.20.28.11;

    /**
     * <Enter note text here>
     */
    @objid ("7845afef-ef6b-4ac8-af0a-df9c3c2140db")
    public static final double delay = 0.05;

}
