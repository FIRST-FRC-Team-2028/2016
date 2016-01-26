package com.PhantomMentalists.Stronghold;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
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

    @objid ("3228d03b-2213-4992-8a56-ff4eebca5015")
    protected WestCoastDrive westCoastDrive;

    /**
     * <Enter note text here>
     */
    @objid ("a955160a-7027-417b-a92e-41236325ab4c")
    protected Shooter shooter;

    /**
     * <Enter note text here>
     */
    @objid ("434ab641-6873-4ddc-a690-48168db127aa")
    protected ClimbingArm climbingArm;

    /**
     * <Enter note text here>
     */
    @objid ("3940dd9a-83a9-4d04-a3b6-2bddccc1f4c8")
    protected AxisCamera axisCamera;

    @objid ("6538383a-6e25-4552-ad07-03d18282baf2")
    public void autonomous() {
    }

    @objid ("a9a437aa-9281-40cd-a90f-1fa3eec18b3c")
    public void operatorControl() {
    }

    @objid ("bc395b50-2496-4ddf-b4ca-1891ed75cbb4")
    public Telepath() {
    }

}
