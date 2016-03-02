package com.PhantomMentalists.Stronghold;

import java.util.Date;

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
    public static final int kLeftMasterDriveMotorCanId = 11;

    /**
     * CAN ID for the second motor powering the Left Drive Unit
     */
    @objid ("1421bb0d-9370-4be8-8702-e551cebf019f")
    public static final int kLeftFollowerDriveMotorCanId = 12;

    /**
     * <Enter note text here>
     */
    @objid ("d59d9116-a49e-40ed-a68d-7ae8dc108c5e")
    public static final int kRightMasterDriveMotorCanId = 21;

    /**
     * <Enter note text here>
     */
    @objid ("440bfe17-9a55-44dc-b53e-96e5a0be2911")
    public static final int kRightFollowerDriveMotorCanId = 22;

    /**
     * <Enter note text here>
     */
    @objid ("2a05fc7b-925f-4bec-a68e-8ead1eada820")
    public static final int kPusherArmMotorCanId = 51;

    @objid ("2deeb224-65ff-4963-8cac-4c00697bb3b9")
    public static final int kShooterAngleMotorCanId = 33;

    @objid ("ba76d02f-5cb3-45d4-87d4-7c0f38f7372d")
    public static final int kLeftShooterPitcherMotorCanId = 42;

    @objid ("02b639b3-8099-4c45-83ab-463996000324")
    public static final int kRightShooterPitcherMotorCanId = 41;

    /**
     * <Enter note text here>
     */
    @objid ("fe6208ba-4d13-4928-b0c0-dea88626ef4e")
    public static final int kClimberAngleMotorCanId = 43;

    /**
     * <Enter note text here>
     */
    @objid ("ad953a2c-2244-4439-a2aa-0e86cd05d5c5")
    public static final int kClimberLeftWinchMotorCanId = 31;

    /**
     * <Enter note text here>
     */
    @objid ("2b176629-4eaf-473e-9106-e8f7f32633de")
    public static final int kClimberRightWinchMotorCanId = 32;

    @objid ("48388446-73d2-468b-9b56-780f9bd7f5e7")
    public static final int kClimberExtendMotorCanId = 44;

    /**
     * <Enter note text here>
     */
    @objid ("84c6a947-3dd9-40ea-a483-650ecea1dbfb")
    public static final String kCameraIpAddress = "10.20.28.11";
    
    public static final int kGyroAnalogPort = 0;
    public static final int kGyroFanAnalogPort = 7;
    
    public static final int kUltraSonicAnalogPort = 1;

    public static final int kTapeSensorLeftDigitalPort = 0;
    public static final int kTapeSensorRightDigitalPort = 1;
    /**
     * <Enter note text here>
     */
    @objid ("7845afef-ef6b-4ac8-af0a-df9c3c2140db")
    public static final double delay = 0.1;

    /**
     * 
     */
    public static final double kDriveMotorDownshiftCurrentThreshold = 20.0;
    
    /**
     * The chanel on the pneumatics module that's wired to the solenoid for drive's low gear
     * <p>
     * If a negative value, it must be replaced with the actual chanel once it has been wired.
     */
    public static final int kLowGearSolenoidChanel = 1;


    /**
     * The chanel on the pneumatics module that's wired to the solenoid for drive's high gear
     * <p>
     * If a negative value, it must be replaced with the actual chanel once it has been wired.
     */

    public static final int kHighGearSolenoidChanel = 0;
    
    /**
     * 
     */
    public static final double kDriveSpeedControlProportional = 0;
    
    /**
     * 
     */
    public static final double kDriveSpeedControlIntegral = 0;
    
    /**
     * 
     */
    public static final double kDriveSpeedControlDifferential = 0;
    
    /**
     * 
     */
    public static final double kDriveSpeedControlThrottle = 0;
    
    /**
     * 
     */
    public static final int kDriveSpeedControlIZone = 0;
    
    /**
     * 
     */
    public static final double kDriveControlCloseLoopRampRate = -1;
    
    /**
     * 48.5 degrees, 38.2 degrees
     */
    public static final int kDriveControlProfile = 0;
	public static final double kShooterShootPitchingMachineSpeed = 0.8;
	public static final double kShooterShootBatterSpeed = 0.65;
	public static final double kShooterTiltMaxVolt = 12*0.6;

	public static final double kShooterReloadPitchingMachineSpeed = -.6;

	public static final double kShootPitchPositionControlProportional = 0;

	public static final double kShootPitchPositionControlIntegral = 0;

	public static final double kShootPitchPositionControlDifferential = 0;

	public static final double kShootTiltPositionControlProportional = 0;

	public static final double kShootTiltPositionControlIntegral = 0;

	public static final double kShootTiltPositionControlDifferential = 0;

	public static final int kShooterBallShooterSolenoidChanel = 2;

	public static final int kShooterDinkSolenoidChanel = 0;

    
    /**
     * 
     */
     
    public static final double kMaxVelocity = 20.0;
	public static final double kShooterTiltShootTapePositionEncoderSetpoint = 1.125;

	public static final double kShooterTiltShootDefensePositionEncoderSetpoint = 1.600;

	public static final double kShooterTiltShootBatterPositionEncoderSetpoint = 0;

	public static final double kShooterTiltReloadPositionEncoderSetpoint = 0;

	public static final double kShooterTiltLowBarPositionEncoderSetpoint = 0;

	public static final double kShooterTiltHomePositionEncoderSetpoint = -17389;
	
	public static final double kShooterTiltPowerDown = -0.35;
	
	public static final double kShooterTiltPowerUp = 0.4;

	public static final double kShooterSeekHomePower = 0.35;
	
	public static final double kShooterSeekTolerance = 0.035;

	public static final int kEncoderCodesPerRev = 1024;


	public static final double kClimbTiltPositionControlProportional = 0;

	public static final double kClimbTiltPositionControlIntegral = 0;

	public static final double kClimbTiltPositionControlDifferential = 0;

	public static final double kClimbTiltPositionControlThrottle = 0;

	public static final double kClimbExtendPositionControlProportional = 0;

	public static final double kClimbExtendPositionControlIntegral = 0;

	public static final double kClimbExtendPositionControlDifferential = 0;

	public static final double kClimbExtendPositionControlThrottle = 0;

	public static final double kClimberFullyExtendedPositionSetpoint = 0;

	public static final double kRaiseLowerMoterHomeSpeed = 0;

	public static final double kClimberTiltHomePositionEncSetpoint = 0;

	public static final double kClimberTiltLowBarPositionEncSetPoint = 0;

	public static final double kClimberRaisedPositionSetPoint = 0;

	public static final double kClimberDrawBridgeSetPoint = 0;
	
	public static final double kClimberTiltPower = 0.35;
	
	public static final double kClimberExtendPower = 0.45;
	
	
	public static final int kRock = 0;
	public static final int kRough =0;
	public static final int kDraw =0;
	public static final int kSally =0;
	public static final int kRamp = 0;
	public static final int kMoat =0;
	public static final int kPort =0;
	public static final int kCheval =0;
	public static final int kLowBar =0;
	// pusher arm pid
	public static final double kPusherArmPositionControlProportional = 0;
	public static final double kPusherArmPositionControlIntegral = 0;
	public static final double kPusherArmPositionControlDifferential = 0;
	public static final double kPusherArmPositionControlThrottle = 0.0;
	//pusher arm 
	public static final double kPusherArmMaxMotorCurrent = 20.0;
	public static final double kPusherArmHomeMotorPower = -0.4;
	public static final double kMotorVoltageDeadband = 0.05;
	
	public static final double kPusherArmHomeSetPoint = 0.0;
	public static final double kPusherArmDownSetPoint = 0.25;

	public static final double kShooterTiltMaxCurrent = 25.0;

	public static final long kShooterPitchingMachineAccelTimeout = 2500;

	public static final long kShooterPitchingMachinePushBallTimeout = 250;
	
	public static final double kGoalHeight = 85;//height from camera to middle of goal, camera is 12 inches high
	
	public static final double kShooterOffSetFromCamera = 7;

}
