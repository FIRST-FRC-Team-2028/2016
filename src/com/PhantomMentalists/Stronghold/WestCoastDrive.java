package com.PhantomMentalists.Stronghold;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.PIDOutput;

@objid ("bfe07845-b9da-40e9-a047-c801ee86b209")
public class WestCoastDrive implements PIDOutput {
    /**
     * <Enter note text here>
     */
    @objid ("994a7329-7dde-4b3a-af65-88988500092f")
    public double turnSetpoint;

    @objid ("b8a106fc-3334-47d0-b500-cf128b91b6c0")
    protected DriveUnit leftSide;

    @objid ("d0db5703-a6c1-4909-baf4-37eb81a88730")
    protected DriveUnit rightSide;

    @objid ("06e23f53-7280-4c29-b319-50a651aca613")
    public WestCoastDrive() {
    }

    /**
     */
    @objid ("a6904588-3865-4548-9fb7-1075a02226d8")
    public void SetSuspension(SuspensionState suspensionState, WheelLocation wheel) {
    }

    /**
     * Set the output to the value calculated by PIDController
     * @param output the value calculated by PIDController
     */
    @objid ("fc5f1ae8-f7bf-4800-924d-f54e5ee2a661")
    public void pidWrite(double output) {
    }

    /**
     * This enumeration assigns human readable names to the suspension positions for each wheel.
     */
    @objid ("16b50431-1206-4647-ae61-5a474135b8b3")
    public enum SuspensionState {
        kRaised,
        kLowered;
    }

    /**
     * This enumeration assigns human readable names to the wheel positions.
     */
    @objid ("521d40fa-84c2-458c-9b1a-63c2d24a9a74")
    public enum WheelLocation {
        kFrontRight,
        kFrontLeft,
        kRearRight,
        kRearLeft,
        kMiddleRight,
        kMiddleLeft;
    }

}
