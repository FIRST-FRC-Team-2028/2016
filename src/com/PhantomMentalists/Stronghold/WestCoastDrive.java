package com.PhantomMentalists.Stronghold;

import com.PhantomMentalists.Stronghold.DriveUnit.Gear;
import com.PhantomMentalists.Stronghold.DriveUnit;
import com.PhantomMentalists.Stronghold.Parameters;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.PIDOutput;

/**
 * <p>Author: Zavion</p>
 */
@objid ("bfe07845-b9da-40e9-a047-c801ee86b209")
public class WestCoastDrive implements PIDOutput {
    /**
     * <Enter note text here>
     */
    @objid ("994a7329-7dde-4b3a-af65-88988500092f")
    public double turnSetpoint;

    /**
     * <Enter note text here>
     */
    @objid ("28bc5bec-15fd-4924-b01c-5f725341e189")
    protected Gear gear = Gear.kLowGear;

    @objid ("b8a106fc-3334-47d0-b500-cf128b91b6c0")
    protected DriveUnit leftSide;

    @objid ("d0db5703-a6c1-4909-baf4-37eb81a88730")
    protected DriveUnit rightSide;

    @objid ("06e23f53-7280-4c29-b319-50a651aca613")
    public WestCoastDrive() {
    }

    /**
     * Set the output to the value calculated by PIDController
     * @param output the value calculated by PIDController
     */
    @objid ("fc5f1ae8-f7bf-4800-924d-f54e5ee2a661")
    public void pidWrite(double output) {
    }

    @objid ("efa356b3-f44c-44ee-a418-1ba6be974be5")
    public void setSetpoint(double setPoint) {
    }

    @objid ("ea714300-5ea7-4831-acb5-2503084baa47")
    public double getSetpoint() {
    	return 0;
    }

    @objid ("77501865-67e2-4889-b074-f047bd3616a6")
    Gear getGear() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.gear;
    }

    @objid ("3e825af2-8a08-4402-9058-dd7ae4aa2260")
    void setGear(Gear value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.gear = value;
    }

}
