package com.PhantomMentalists.Stronghold;

import com.PhantomMentalists.Stronghold.WestCoastDrive.Gear;
import com.PhantomMentalists.Stronghold.Autopilot.Autopilot;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Compressor;
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
    public PIDController turncont;
    public double P =0.07,I = 0.0001,D= 0.005;
    public double tangle = 0;
    public Preferences prefs;
	/**
     * <Enter note text here>
     */
	protected Joystick lstick,rstick,buttonstick,analogstick;
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
    protected AnalogGyro gyro;
    protected Solenoid fan;
    /**
     * <Enter note text here>
     */
    @objid ("6538383a-6e25-4552-ad07-03d18282baf2")
    public void autonomous() {
        while (isAutonomous() && isEnabled()) {
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
        while (isEnabled() && isOperatorControl()) {
        	leftval = newJoystickValue(leftstick.getY());
        	rightval = newJoystickValue(rightstick.getY());
        	SmartDashboard.putNumber("Gyro Anagle",gyro.getAngle());
        	if(rightstick.getRawButton(2))
        	{
        		tangle = gyro.getAngle();
        	}
        	if(rightstick.getRawButton(3))
        	{
        		gyro.calibrate();
        	}
        	if(leftstick.getTrigger())
        	{
        		westCoastDrive.setGear(Gear.kLowGear);
        	}
        	if(rightstick.getTrigger())
        	{
        		westCoastDrive.setGear(Gear.kHighGear);
        	}
        	
        	if(leftstick.getRawButton(2))
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
        	westCoastDrive.process();
        	
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

    	leftstick = new Joystick(0);
    	rightstick = new Joystick(1);
//    	pusherArm = new PusherArm();
    	westCoastDrive = new WestCoastDrive();
//    	shooter = new Shooter();
//    	climbingArm = new ClimbingArm();
//    	compressor = new Compressor();
//    	ultrasonic= new UltrasonicSensor(Parameters.kUltraSonicAnalogPort);
    	gyro = new AnalogGyro(Parameters.kGyroAnalogPort);
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

}
