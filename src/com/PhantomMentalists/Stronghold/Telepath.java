package com.PhantomMentalists.Stronghold;

import com.PhantomMentalists.Stronghold.WestCoastDrive.Gear;
import com.PhantomMentalists.Stronghold.Autopilot.Autonomous;
import com.PhantomMentalists.Stronghold.Autopilot.Autopilot;
import com.PhantomMentalists.Stronghold.Autopilot.DefenceSelection;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

@objid ("839537d8-f1f4-49a5-ac60-eaa7467a9f20")
public class Telepath extends SampleRobot {
    public Joystick leftstick;
    public Joystick rightstick;
    public Joystick buttonstick2;
    public Joystick buttonstick3;
    public Joystick analogstick;
    public PIDController turncont;
    public double P =0.07,I = 0.0001,D= 0.005;
    public double tangle = 0;
    public double shootangle = 0;
    public Preferences prefs;
    DigitalInput tapeSensorLeft = new DigitalInput(Parameters.kTapeSensorLeftDigitalPort);
    DigitalInput tapeSensorRight = new DigitalInput(Parameters.kTapeSensorRightDigitalPort);
	/**
     * <Enter note text here>
     */
    @objid ("f4bbb171-d580-4f7e-858a-1f36d93dabaa")
    public boolean autopilotEnabled;

    /**
     * <Enter note text here>
     */
    @objid ("f6c31ec5-102f-4066-8abb-a43eefb32659")
    protected PusherArm pusherArm;

    /**
     * <Enter note text here>
     */
    @objid ("3228d03b-2213-4992-8a56-ff4eebca5015")
    protected WestCoastDrive westCoastDrive;

    @objid ("a955160a-7027-417b-a92e-41236325ab4c")
    protected Shooter shooter;

    @objid ("434ab641-6873-4ddc-a690-48168db127aa")
    protected ClimbingArm climbingArm;
    
    /**
     * Camera used for aiming
     */
    protected Camera cam;

    @objid ("352325f0-e7b8-4746-8690-a318ffdc697c")
    protected Autopilot autopilot;

    /**
     * <Enter note text here>
     */
    @objid ("2e3ccbe3-294d-43e9-a432-a5e5a2404953")
    protected Compressor compressor;

    /**
     * <Enter note text here>
     */
    @objid ("32e1d7a9-9c5f-40cb-beaa-5698d7dbfb75")
    protected UltrasonicSensor ultrasonic;

    /**
     * <Enter note text here>
     */
    @objid ("819d3be2-a72e-4178-9fd5-0d5727789bcf")
    protected Gyro gyro;
    protected Solenoid fan;
    /**
     * <Enter note text here>
     */
    @objid ("6538383a-6e25-4552-ad07-03d18282baf2")
    public void autonomous() {
    	Autonomous auto = new Autonomous(this);
    	auto.setLane(4);
    	auto.setDefence(DefenceSelection.kRough.getNum());
    	auto.setEnabled(true);
        while (isAutonomous() && isEnabled()) {
        	auto.process();
            Timer.delay(Parameters.delay);
        }
    }

    /**
     * <Enter note text here>
     */
    @objid ("a9a437aa-9281-40cd-a90f-1fa3eec18b3c")
    public void operatorControl() {
    	double leftval = 0,rightval = 0;
    	fan.set(true);
    	P = prefs.getDouble("Turn P", P);
    	I = prefs.getDouble("Turn I", I);
    	D = prefs.getDouble("Turn D", D);
    	turncont.setPID(P, I, D);
    	boolean isCameraMovingManually = true;
    	
        while (isEnabled() && isOperatorControl()) {
            System.out.println("Configure: "+analogstick.getX());
            System.out.println("Lane: "+analogstick.getY());
        	System.out.println("Shooter Pos: "+analogstick.getZ());
        	System.out.println("Camera Pos: "+analogstick.getRawAxis(3));
            leftval = newJoystickValue(leftstick.getY());
        	rightval = newJoystickValue(rightstick.getY());
        	SmartDashboard.putNumber("Gyro Anagle",gyro.getAngle());
        	SmartDashboard.putNumber("Shoot Position",shootangle);
        	SmartDashboard.putBoolean("Left Tape",tapeSensorLeft.get());
        	SmartDashboard.putBoolean("Right Tape",tapeSensorRight.get());
        	// Manually control shooter pitching machine
        	if(buttonstick3.getRawButton(ButtonStick3Values.kShooterShoot.getValue()))
        	{
//        		shooter.manualRunPitchingMachine(Parameters.kShooterShootPitchingMachineSpeed);
        		shooter.shoot2();
        	}
        	else if(buttonstick2.getRawButton(ButtonStick3Values.kShooterInfeed.getValue()))
        	{
        		shooter.manualRunPitchingMachine(Parameters.kShooterReloadPitchingMachineSpeed);
        	}
        	else
        	{
        		shooter.manualRunPitchingMachine(0);
        	}
//        	else if(buttonstick1.getRawButton(kShooterSpare.getValue()))
//        	{
//        		shooter.manualRunPitchingMachine(Parameters.kShooterShootBatterSpeed);
//        	}
        	
        	// Manually control dink
        	if(buttonstick2.getRawButton(ButtonStick3Values.kKick.getValue()))
        	{
        		shooter.pushBall(true);
        	}
        	else
        	{
        		shooter.pushBall(false);
        	}
        	
        	// Manually control shooter angle
        	if(buttonstick3.getRawButton(ButtonStick3Values.kShooterUp.getValue()))
        	{
        		shooter.manualRunTiltMotor(Parameters.kShooterTiltPowerUp);
        	}
        	else if(buttonstick2.getRawButton(ButtonStick3Values.kShooterDown.getValue()))
        	{
        		shooter.manualRunTiltMotor(Parameters.kShooterTiltPowerDown);
        	}
        	else if(rightstick.getRawButton(11))
        	{
//        		System.out.println("here2");
//        		shooter.setShootAngle(ShooterPosition.kHome);
        		shooter.setTiltMemSetpoint(shootangle);
//        		cam.setCam(-1,(0.335*shooter.getTiltAngle()/3.9)+0.665);
        	}
        	else if(rightstick.getRawButton(10))
        	{
        		shootangle = shooter.getTiltAngle();
        	}
        	else
        	{
        		shooter.manualRunTiltMotor(0);
        	}
        	if(rightstick.getRawButton(9))
        	{
        		tangle = gyro.getAngle();
        	}
        	if(rightstick.getRawButton(8))
        	{
        		gyro.calibrate();
        	}
        	if(rightstick.getRawButton(2))
        	{
        		westCoastDrive.setGear(Gear.kLowGear);
        	}
        	if(rightstick.getRawButton(3))
        	{
        		westCoastDrive.setGear(Gear.kHighGear);
        	}
        	
        	if(leftstick.getRawButton(11))
        	{
        		westCoastDrive.setSpeedSetpoint(leftval, leftval);
        	}
        	else
        	{
//        		System.out.println("here3");
        		westCoastDrive.setSpeedSetpoint(leftval, rightval);
        	}
        	if(leftstick.getRawButton(8) && !turncont.isEnabled())
        	{
//        		System.out.println("here2");
        		turncont.setSetpoint(0);
        		turncont.enable();

        	}
        	else if(leftstick.getRawButton(9) && !turncont.isEnabled())
        	{
//        		System.out.println("here2");
        		turncont.setSetpoint(tangle);
        		turncont.enable();

        	}
        	else if(!leftstick.getRawButton(9) && !leftstick.getRawButton(8) && turncont.isEnabled())
        	{
//        		System.out.println("here");
        		turncont.disable();
        		westCoastDrive.setTurnSetpoint(0);
        	}
        	if(rightstick.getTrigger())
        	{
        		System.out.println("Cam angle: "+cam.getCameraAngle());
        		shooter.setShooterAngle(cam.getCameraAngle());
        	}
//        	if(leftstick.getTrigger())
//        	{
//        		pusherArm.manualSetTilt(-Parameters.kPusherArmHomeMotorPower);
//        	}
//        	else if(rightstick.getTrigger())
//        	{
//        		pusherArm.manualSetTilt(Parameters.kPusherArmHomeMotorPower);
//        	}
//        	else
//        	{
//        		pusherArm.manualSetTilt(0);
//        	}
//        	if(buttonstick3.getRawButton(ButtonStick2Values.kClimberOut.getValue()))
//        	{
//        		//
//        		// TODO:  We cannot extend the climber without at least putting the
//        		//        winch in "coast" mode, or preferably slowly playing out cord 
//        		//  
//        		climbingArm.manualSetExtendRetract(Parameters.kClimberExtendPower);
//        	}
//        	else if(buttonstick2.getRawButton(ButtonStick3Values.kClimberIn.getValue()))
//        	{
//        		// 
//        		// TODO:  We cannot pull the climber in without also realing in the 
//        		//        winch cord
//        		//
//        		climbingArm.manualSetExtendRetract(-Parameters.kClimberExtendPower);
//        	}
//        	else
//        	{
//        		climbingArm.manualSetExtendRetract(0);
//        	}
//        	if(buttonstick2.getRawButton(ButtonStick3Values.kClimberUp.getValue()))
//        	{
//        		climbingArm.manualSetTilt(Parameters.kClimberTiltPower);
//        	}
//        	else if(buttonstick2.getRawButton(ButtonStick3Values.kClimberDown.getValue()))
//        	{
//        		climbingArm.manualSetTilt(-Parameters.kClimberTiltPower*.5);
//        	}
//        	else
//        	{
//        		climbingArm.manualSetTilt(0);
//        	}
        	
        	if(buttonstick2.getRawButton(ButtonStick3Values.kPusherUp.getValue()))
        	{
        		pusherArm.manualSetTilt(Parameters.kPusherArmHomeMotorPower);
        	}
        	else if(buttonstick2.getRawButton(ButtonStick3Values.kPusherDown.getValue()))
        	{
        		pusherArm.manualSetTilt(-Parameters.kPusherArmHomeMotorPower);
        	}
        	else
        	{
        		pusherArm.manualSetTilt(0);
        	}
        	
        	if (buttonstick3.getRawButton(ButtonStick2Values.kFindTarget.getValue()) && isCameraMovingManually)
        	{	
        		isCameraMovingManually = false;
        		cam.getImage();
        		cam.centerTarget();
        		turncont.setSetpoint(gyro.getAbsoluteAngleFromRelative(cam.getPosx()));
        		turncont.enable();
        	}
        	else if(!buttonstick3.getRawButton(ButtonStick2Values.kFindTarget.getValue()))
        	{
        		isCameraMovingManually = true;
        		turncont.disable();
        	}
        	
        	
        	if (isCameraMovingManually)
        	{
        		cam.setCam(-1, (analogstick.getRawAxis(3)+1)/2);
        	}
        	
        	westCoastDrive.process();
        	shooter.process();
        	cam.process();
        	
            Timer.delay(Parameters.delay);
        }
        fan.set(false);
    }

    @objid ("bc395b50-2496-4ddf-b4ca-1891ed75cbb4")
    public Telepath() {
    	prefs = Preferences.getInstance();
    	prefs.putDouble("Turn P", P);
    	prefs.putDouble("Turn I", I);
    	prefs.putDouble("Turn D", D);
    	cam = new Camera(Parameters.kCameraIpAddress);
    	leftstick = new Joystick(Parameters.kDriverStationLeftStick);
    	rightstick = new Joystick(Parameters.kDriverStationRightStick);
    	analogstick = new Joystick(Parameters.kDriverStationAnalogStick);
    	buttonstick2 = new Joystick(Parameters.kDriverStationButtonStick2);
    	buttonstick3 = new Joystick(Parameters.kDriverStationButtonStick3);
    	pusherArm = new PusherArm();
    	westCoastDrive = new WestCoastDrive();
    	shooter = new Shooter();
    	climbingArm = new ClimbingArm();
//    	compressor = new Compressor();
    	ultrasonic= new UltrasonicSensor(Parameters.kUltraSonicAnalogPort);
    	gyro = new Gyro(Parameters.kGyroAnalogPort);
    	gyro.initGyro();
    	gyro.calibrate();
    	fan = new Solenoid(Parameters.kGyroFanAnalogPort);
    	turncont = new PIDController(P,I,D,gyro,westCoastDrive);
    }
    public boolean isJoystickInDeadband(double val)
    {
    	return (val > -0.05 && val < 0.05);
    }
    
    public double newJoystickValue(double val)
    {
    	if (val > -0.05 && val < 0.05)
    	{
    		return 0;
    	}
    	else
    	{
    		return val;
    	}
    }
    
    public int getNumFromPot(double val)
    {
    	return -1;
    }
    
    /**
     * <Enter note text here>
     */
    @objid ("1b4fdd3a-dca2-4cca-93c8-4daa1f3253b4")
    public double getRangeToTarget() {
        return 0;
    }

    @objid ("92f9f534-aeba-40f2-849c-24d5ee83688c")
    public double getAngleToTarget() {
        return 0;
    }

    @objid ("b2f54c9f-b426-4269-aabb-1604a583d152")
    public void shoot() {
    }

    public enum ButtonStick3Values
    {
    	/**
    	 * Runs the shooter's pitching machine motors in reverse slowly to load a ball
    	 */
    	kShooterInfeed(1),
    	
    	/**
   		 * Button that engages the Shooter's dink
   		 */
    	kKick(2),
    	
    	/**
    	 * Button that runs the shooter's tilt motor forward to lower the shooter angle
    	 */
    	kShooterDown(3),
    	
    	/**
    	 * Button that runs the shooter's tilt motor in reverse to raise the shooter angle
    	 */
    	kClimberOut(4),
    	
    	/**
    	 * Button to run the Climber arm's winch motors slowly in reverse to play out cable
    	 */
    	kClimberRelease(5),
    	
    	/**
    	 * Button to run the Climber arm's extend/retract motor forward to extend/raise
    	 * the climbing arm.
    	 */
    	kClimberIn(6),
    	
    	/**
    	 * Button to run the Climber arm's tilt motor forward to angle the climber arm
    	 * towards the front of the robot (towards the shooter)
    	 */
    	kClimberUp(7),
    	
    	/**
    	 * Button to run the Climber arm's tilt motor in reverse to angle the climber arm
    	 * toward the back of the robot (towards the pusher/duck bar)
    	 */
    	kClimberDown(8),
    	
    	/**
    	 * Button to run the Pusher arm (a.k.a., the "duck" bar) in reverse to raise the
    	 * pusher arm towards it home/stoweed position
    	 */
    	kPusherUp(9),
    	
    	/**
    	 * Button to run the Pusher arm (a.k.a., the "duck" bar) forward to lower the
    	 * pusher arm towards the bumper
    	 */
    	kPusherDown(10),
    	
    	/**
    	 * Button to run the Shooter's tilt motor in reverse to raise the shooter tilt
    	 * angle (towards it's home/stowed position)
    	 */
    	kShooterUp(11),
    	
    	/**
    	 * Button to run the Shooter's pitching machine motors full-speed forward to 
    	 * propell the boulder towards the tower.
    	 */
    	kShooterShoot(12);
    	
    	private int value;
    	
    	private ButtonStick3Values(int value)
    	{
    		this.value = value;
    	}
    	
    	public int getValue()
    	{
    		return value;
    	}
    }
    
    public WestCoastDrive getDrive()
    {
    	return westCoastDrive;
    }
    
    public Shooter getShooter()
    {
    	return shooter;
    }
    
    public PusherArm getPusherArm()
    {
    	return pusherArm;
    }
    
    public Camera getCamera()
    {
    	return cam;
    }
    
    public ClimbingArm getClimberArm()
    {
    	return climbingArm;
    }
    
    public PIDController getTurnController()
    {
    	return turncont;
    }
    
    public boolean getLeftTape()
    {
    	return tapeSensorLeft.get();
    }
    
    public boolean getRightTape()
    {
    	return tapeSensorRight.get();
    }
    
    public Gyro getGyro()
    {
    	return gyro;
    }
    
    public UltrasonicSensor getUltrasonic()
    {
    	return ultrasonic;
    }
    
    public enum ButtonStick2Values
    {
    	/**
    	 * 
    	 */
    	kOn(1),
    	
    	/**
    	 * 
    	 */
    	kOff(2),
    	
    	/**
    	 * 
    	 */
    	kSpare(3),
    	
    	/**
    	 *
    	 */
    	kClimberOut(4),
    	
    	/**
    	 * 
    	 */
    	kSetZero(5),
    	
    	/**
    	 * 
    	 */
    	kGotoZero(6),
    	
    	/**
    	 * 
    	 */
    	kSetTarget(7),
    	
    	/**
    	 * 
    	 */
    	kGotoTarget(8),
    	
    	/**
    	 * 
    	 */
    	kClimberWindUp(9),
    	
    	/**
    	 * 
    	 */
    	kFindTarget(10),
    	
    	/**
    	 * 
    	 */
    	kShooterUp(11),
    	
    	/**
    	 * 
    	 */
    	kShooterShoot(12);
    	
    	private int value;
    	
    	private ButtonStick2Values(int value)
    	{
    		this.value = value;
    	}
    	
    	public int getValue()
    	{
    		return value;
    	}
    }
}