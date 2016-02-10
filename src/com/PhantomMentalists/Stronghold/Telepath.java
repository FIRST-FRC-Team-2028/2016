package com.PhantomMentalists.Stronghold;

import com.PhantomMentalists.Stronghold.Autopilot.Autopilot;
import com.PhantomMentalists.Stronghold.DriveUnit;
import com.PhantomMentalists.Stronghold.Parameters;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;

@objid ("839537d8-f1f4-49a5-ac60-eaa7467a9f20")
public class Telepath extends SampleRobot {
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
        while (isEnabled() && isOperatorControl()) {
            Timer.delay(Parameters.delay);
        }
    }

    @objid ("bc395b50-2496-4ddf-b4ca-1891ed75cbb4")
    public Telepath() {
    	pusherArm = new PusherArm();
    	westCoastDrive = new WestCoastDrive();
    	shooter = new Shooter();
    	climbingArm = new ClimbingArm();
    	compressor = new Compressor();
    	ultrasonic= new UltrasonicSensor(Parameters.kUltraSonicAnalogPort);
    	gyro = new AnalogGyro(Parameters.kGyroAnalogPort);
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
