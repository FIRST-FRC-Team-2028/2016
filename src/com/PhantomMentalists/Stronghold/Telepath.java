package com.PhantomMentalists.Stronghold;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.vision.AxisCamera;

@objid ("839537d8-f1f4-49a5-ac60-eaa7467a9f20")
public class Telepath extends SampleRobot {
    /**
     * <Enter note text here>
     */
    @objid ("f4bbb171-d580-4f7e-858a-1f36d93dabaa")
    public boolean autopilotEnabled;

    @objid ("f6c31ec5-102f-4066-8abb-a43eefb32659")
    protected TankTread tankTread;

    /**
     * <Enter note text here>
     */
    @objid ("3228d03b-2213-4992-8a56-ff4eebca5015")
    protected WestCoastDrive westCoastDrive;

    @objid ("a955160a-7027-417b-a92e-41236325ab4c")
    protected Shooter shooter;

    @objid ("434ab641-6873-4ddc-a690-48168db127aa")
    protected ClimbingArm climbingArm;

    @objid ("3940dd9a-83a9-4d04-a3b6-2bddccc1f4c8")
    protected AxisCamera axisCamera;

    /**
     * <p>The turn controller takes over steering the robot, rotating it in place to point directly at the target we&#39;re going to shoot at. &nbsp;It gets the angle to turn to by using the pidGet() method (realized from the PIDSource interface).</p>
     */
    @objid ("14ee1a17-2513-4710-b3e0-3f157c1733d9")
    protected PIDController turnPidController;

    /**
     * <Enter note text here>
     */
    @objid ("d13607f0-e64f-4d43-8cb5-52ab7f6a9113")
    protected Gyro gyro;

    @objid ("6538383a-6e25-4552-ad07-03d18282baf2")
    public void autonomous() {
    }

    @objid ("a9a437aa-9281-40cd-a90f-1fa3eec18b3c")
    public void operatorControl() {
    }

    @objid ("bc395b50-2496-4ddf-b4ca-1891ed75cbb4")
    public Telepath() {
    }

    /**
     * <Enter note text here>
     */
    @objid ("1b4fdd3a-dca2-4cca-93c8-4daa1f3253b4")
    public double getRangeToTarget() {
    }

    @objid ("92f9f534-aeba-40f2-849c-24d5ee83688c")
    public double getAngleToTarget() {
    }

    /**
     * Get the result to use in PIDController
     * @return the result to use in PIDController
     */
    @objid ("e92caffb-11f7-41f4-acdc-b40ce1d43e94")
    public double pidGet() {
    }

    @objid ("b2f54c9f-b426-4269-aabb-1604a583d152")
    public void shoot() {
    }

}
