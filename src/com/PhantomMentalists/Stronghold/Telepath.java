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
    public DefenceSelection defence;
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
    
    protected FlashLight light;
    
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
//    	gyro.calibrate();
//    	gyro.
    	Autonomous auto = new Autonomous(this);
    	auto.setLane(getLaneFromJoyStick());
    	this.setDefenceConfig();
    	auto.setDefence(defence);
    	auto.setEnabled(true);
        while (isAutonomous() && isEnabled()) {
        	SmartDashboard.putNumber("Gyro Absolute Angle", gyro.getAngle());
        	auto.process();
            Timer.delay(Parameters.delay);
        }
    }

    /**
     * <Enter note text here>
     */
    @objid ("a9a437aa-9281-40cd-a90f-1fa3eec18b3c")
    public void operatorControl() {
    	compressor.start();
    	double leftval = 0,rightval = 0;
    	fan.set(true);
    	P = prefs.getDouble("Turn P", P);
    	I = prefs.getDouble("Turn I", I);
    	D = prefs.getDouble("Turn D", D);
    	turncont.setPID(P, I, D);
    	boolean isCameraMovingManually = true;
    	
        while (isEnabled() && isOperatorControl()) {
        	setDefenceConfig();
            SmartDashboard.putNumber("Configure",defence.getNum());
            SmartDashboard.putNumber("Lane",getLaneFromJoyStick());
            SmartDashboard.putNumber("Shooter Pos", getShootPos());
            leftval = newJoystickValue(leftstick.getY());
        	rightval = newJoystickValue(rightstick.getY());
//        	System.out.println("Gyro Angle: "+gyro.getAngle());
        	SmartDashboard.putNumber("Gyro Relative Anagle",gyro.getRelativeAngle());
        	SmartDashboard.putNumber("Gyro Absolute Angle", gyro.getAngle());
        	SmartDashboard.putNumber("Shoot Position",shootangle);
        	SmartDashboard.putBoolean("Left Tape",tapeSensorLeft.get());
        	SmartDashboard.putBoolean("Right Tape",tapeSensorRight.get());
        	
        	// Manually control shooter pitching machine
        	if(buttonstick3.getRawButton(ButtonStick3Values.kShooterShoot.getValue()) || leftstick.getRawButton(5))
        	{
        		light.turnOn();
        		shooter.manualRunPitchingMachine(Parameters.kShooterShootPitchingMachineSpeed);
//        		shooter.shoot2();
        	}
        	else if(buttonstick2.getRawButton(ButtonStick3Values.kShooterInfeed.getValue()) || leftstick.getRawButton(4))
        	{
        		shooter.manualRunPitchingMachine(Parameters.kShooterReloadPitchingMachineSpeed);
        	}
        	else
        	{
        		light.turnOff();
        		shooter.manualRunPitchingMachine(0);
        	}
        	
        	// Shooter Control
        	if(buttonstick3.getRawButton(ButtonStick3Values.kShooterUp.getValue()) || leftstick.getRawButton(3))
        	{
        		shooter.manualRunTiltMotor(Parameters.kShooterTiltPowerUp);
        	}
        	else if(buttonstick2.getRawButton(ButtonStick3Values.kShooterDown.getValue()) || leftstick.getRawButton(2))
        	{
        		shooter.manualRunTiltMotor(Parameters.kShooterTiltPowerDown);
        	}
        	else if (buttonstick3.getRawButton(ButtonStick2Values.kFindTarget.getValue()))
        	{	
        		if(isCameraMovingManually)
        		{
        			cam.getImage();
            		cam.centerTarget(gyro.getAngle());
            		turncont.enable();
        		}
        		isCameraMovingManually = false;
        		shooter.setShooterAngle(cam.getCameraAngle()+16);
        		turncont.setSetpoint(gyro.getAngle()+cam.getAngleToMoveFromCamera());
        		if(!shooter.isTiltAngleAtSetpoint())
        		{
        			shooter.setShooterAngle(cam.getCameraAngle()+16);
        		}
        		else
        		{
        			shooter.manualRunTiltMotor(0);

        		}
        	}
        	else if(buttonstick2.getRawButton(ButtonStick2Values.kGoTo.getValue()))
        	{
        		if(!shooter.isTiltAngleAtSetpoint())
        		{
        			switch(getShootPos())
        			{
        			case 1:
        				System.out.println("here");
        				shooter.setShooterAngleSetpoint(70);
        			case 2:
        				shooter.setShooterAngleSetpoint(48);
        			}
        		}
        		else
        		{
        			shooter.manualRunTiltMotor(0);
        		}
        	}
        	else
        	{
        		isCameraMovingManually = true;
        		shooter.manualRunTiltMotor(0);
        	}
        	//FlashLight
        	if(buttonstick3.getRawButton(ButtonStick2Values.kSpare.getValue()) )
        	{
        		light.turnOn();
        	}

        	
        	// Manually control dink
        	if(buttonstick2.getRawButton(ButtonStick3Values.kKick.getValue()))
        	{
        		shooter.pushBall(true);
        	}
        	else
        	{
        		shooter.pushBall(false);
        	}

        	//DRIVE
        	if(rightstick.getRawButton(2))
        	{
        		westCoastDrive.setGear(Gear.kLowGear);
        	}
        	else if(rightstick.getRawButton(3))
        	{
        		westCoastDrive.setGear(Gear.kHighGear);
        	}
        	if(leftstick.getRawButton(11))
        	{
        		westCoastDrive.setSpeedSetpoint(leftval, leftval);
        	}
        	else
        	{
        		westCoastDrive.setSpeedSetpoint(leftval, rightval);
        	}
        	
        	//GYRO
        	
        	if(buttonstick3.getRawButton(ButtonStick2Values.kGotoZero.getValue()))
        	{
        		turncont.enable();
        		turncont.setSetpoint(0);
        	}
        	else if(buttonstick3.getRawButton(ButtonStick2Values.kGotoTarget.getValue()))
        	{
        		turncont.enable();
        		turncont.setSetpoint(tangle);
        	}
        	else if(!buttonstick3.getRawButton(ButtonStick2Values.kGotoTarget.getValue()) && !buttonstick3.getRawButton(ButtonStick2Values.kSpare.getValue()) && !buttonstick3.getRawButton(ButtonStick2Values.kGotoZero.getValue()) && !buttonstick3.getRawButton(ButtonStick2Values.kFindTarget.getValue()) && turncont.isEnabled())
        	{
        		turncont.disable();
        		westCoastDrive.setTurnSetpoint(0);
        	}
        	if(buttonstick3.getRawButton(ButtonStick2Values.kSetTarget.getValue()))
        	{
        		tangle = gyro.getAngle();
        	}

        	//DUCK BARS
        	
        	if(buttonstick2.getRawButton(ButtonStick3Values.kPusherUp.getValue()))
        	{
        		pusherArm.manualSetTilt(Parameters.kPusherArmMotorPowerUp);
        	}
        	else if(buttonstick2.getRawButton(ButtonStick3Values.kPusherDown.getValue()))
        	{
        		pusherArm.manualSetTilt(-Parameters.kPusherArmHomeMotorPower);
        	}
        	else
        	{
        		pusherArm.manualSetTilt(0);
        	}
        	
        	
        	//CAMERA MOVEMENT
        	
        	if (isCameraMovingManually)
        	{
        		cam.setCam(-1, (analogstick.getRawAxis(3)+1)/2);
        	}
        	
        	westCoastDrive.process();
        	shooter.process();
        	cam.process();
//        	System.out.println("Turn Cont: "+turncont.isEnabled());
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
    	compressor = new Compressor();
    	ultrasonic= new UltrasonicSensor(Parameters.kUltraSonicAnalogPort);
    	gyro = new Gyro(Parameters.kGyroAnalogPort);
    	gyro.initGyro();
    	gyro.calibrate();
    	fan = new Solenoid(Parameters.kGyroFanAnalogPort);
    	turncont = new PIDController(P,I,D,gyro,westCoastDrive);
    	light = new FlashLight();
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
    
    public boolean getLeftTape()
	{
		return tapeSensorLeft.get();
	}

	public PIDController getTurnController()
    {
    	return turncont;
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
    
    public int getLaneFromJoyStick()
    {
    	double val = analogstick.getY();
    	if(val <= -0.9)
    	{
    		val = 1;
    	}
    	else if(val <= -0.5 && val >= -0.7)
    	{
    		val = 2;
    	}
    	else if(val <= -0.1 && val >= -0.3)
    	{
    		val = 3;
    	}
    	else if(val <= 0.3 && val >= 0.1)
    	{
    		val = 4;
    	}
    	else if(val <= 0.7 && val >= 0.5)
    	{
    		val = 5;
    	}
    	else
    	{
    		val = -1;
    	}
    	return (int)val;
    }
 
    public void setDefenceConfig()
    {
    	double val = analogstick.getX();
    	if(val <= -0.925)
    	{
    		defence = DefenceSelection.kLowBar;
    	}
    	else if(val >= -0.875 && val <= -0.725)
    	{
    		defence = DefenceSelection.kPort;
    	}
    	else if(val >= -0.675 && val <= -0.525)
    	{
    		defence = DefenceSelection.kCheval;
    	}
    	else if(val >= -0.475 && val <= -0.325)
    	{
    		defence = DefenceSelection.kMoat;
    	}
    	else if(val >= -0.275 && val <= -0.125)
    	{
    		defence = DefenceSelection.kRamp;
    	}
    	else if(val >= 0.125 && val <= 0.275)
    	{
    		defence = DefenceSelection.kDraw;
    	}
    	else if(val >= 0.325 && val <= 0.475)
    	{
    		defence = DefenceSelection.kSally;
    	}
    	else if(val >= 0.525 && val <= 0.675)
    	{
    		defence = DefenceSelection.kRock;
    	}
    	else if(val >= 0.725 && val <= 0.875)
    	{
    		defence = DefenceSelection.kRough;
    	}
    	else if(val >=0.925)
    	{
    		defence = DefenceSelection.kClimb;
    	}
    }
   
    public int getShootPos()
    {
    	double val = analogstick.getRawAxis(2);
    	int rc = 0;
    	if(val < -0.9)
    	{
    		rc = 1; 
    	}
    	else if(val < -0.65 && val > -0.85)
    	{
    		rc = 2;
    	}
    	else if(val < -0.45 && val > -0.65)
    	{
    		rc = 3;
    	}
    	else if(val < -0.25 && val > -0.45)
    	{
    		rc = 4;
    	}
    	else if(val < 0 && val > -0.25)
    	{
    		rc = 5;
    	}
    	else if(val < 0.2 && val > 0)
    	{
    		rc = 6;
    	}
    	else if(val >0.2)
    	{
    		rc = 7;
    	}
    	return rc;
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
    	kGoTo(4),
    	
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