package com.PhantomMentalists.Stronghold;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("bfe07845-b9da-40e9-a047-c801ee86b209")
public class WestCoastDrive {
    @objid ("06e23f53-7280-4c29-b319-50a651aca613")
    public WestCoastDrive() {
    }

    /**
     */
    @objid ("a6904588-3865-4548-9fb7-1075a02226d8")
    public void SetSuspension(SuspensionState suspensionState, WheelLocation wheel) {
    }

    /**
     * This enumeration assigns human readable names to the suspension positions for each wheel.
     */
    @objid ("16b50431-1206-4647-ae61-5a474135b8b3")
    public enum SuspensionState {
        Raised,
        Lowered;
    }

    /**
     * This enumeration assigns human readable names to the wheel positions.
     */
    @objid ("521d40fa-84c2-458c-9b1a-63c2d24a9a74")
    public enum WheelLocation {
        FrontRight,
        FrontLeft,
        RearRight,
        RearLeft,
        MiddleRight,
        MiddleLeft;
    }

}
