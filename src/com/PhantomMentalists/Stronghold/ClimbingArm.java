package com.PhantomMentalists.Stronghold;

import com.PhantomMentalists.Stronghold.Parameters;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.CANTalon;

@objid ("899c26b9-be06-403c-b6de-a1d9d8a5322b")
public class ClimbingArm {
    @objid ("534c37a6-0aae-48e4-9ccf-475ca53c092c")
    protected CANTalon extendRetractMotor;

    @objid ("c7d0df5b-dc61-45b7-bf18-207bbf5d74a8")
    protected CANTalon raiseLowerMotor;

    @objid ("c215a8ac-a925-4c23-aab7-ec75ee354734")
    public ClimbingArm() {
    }

    @objid ("aeb2632b-96b3-4001-8a1a-6655759a1229")
    public void manualMoveForward() {
    }

    @objid ("4d55bb48-6960-4de6-ad94-99c0e3b53226")
    public void manualMoveBackward() {
    }

    @objid ("fc35b51c-7c86-4ff7-a3d8-d45acbad3f4a")
    public void autoDeployArm() {
    }

    @objid ("c79c6bd4-bedc-408f-b152-b67ec1425b47")
    public void autoRetractArm() {
    }

    @objid ("17ccc502-6324-4a87-bbc3-e61847c4d098")
    public void autoExtendArm() {
    }

    @objid ("13e1033b-fb72-493a-8320-db6e610fdf26")
    public boolean isDeployed() {
    }

}
