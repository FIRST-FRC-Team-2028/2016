package com.PhantomMentalists.Stronghold;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;

@objid ("69d74b75-df80-4243-808f-74bb65f3aa0a")
public class DriveUnit {
    /**
     * speedSetpoint is the speed the drive motors should be running in the range of -1.0 .. 0 .. 1.0 where zero is stopped, negative numbers are reverse (up to 100% at -1.0) and positive numbers are forward (up to 100% at 1.0).
     */
    @objid ("24edc69c-f818-4b29-93f7-f288887f5a0c")
    protected double speedSetpoint;

    @objid ("8ebf763a-4c0e-4724-936a-e4bfc59091f5")
    protected CANTalon motor1;

    @objid ("f561482b-0252-461f-84e6-29dc422cce48")
    protected CANTalon motor2;

    /**
     * <Enter note text here>
     */
    @objid ("0fb4ba63-1470-4131-a627-451ab5778848")
    protected Solenoid gearSolenoid;

    @objid ("72aba1e2-59da-49cd-914f-a0aba060f665")
    public DriveUnit() {
    }

    /**
     * <Enter note text here>
     */
    @objid ("587974b2-058b-4b50-be3e-0e3dc1aed6e0")
    public void setGear(GearSelection gear) {
    }

    /**
     */
    @objid ("d9f0f54c-5d22-482f-a3d1-09538f3b6c49")
    public void setSpeed(double speed) {
    }

    @objid ("5dad8b50-8973-4186-80f9-5dd883b14e9d")
    double getSpeedSetpoint() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.speedSetpoint;
    }

    @objid ("6b1aafd1-f349-430d-8d23-f281df820075")
    void setSpeedSetpoint(double value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.speedSetpoint = value;
    }

    @objid ("03f75dd6-ebef-46e3-af08-99fe8154fc8b")
    public enum GearSelection {
        LowGear,
        HighGear;
    }

}
