package com.PhantomMentalists.Stronghold;

import com.PhantomMentalists.Stronghold.Parameters;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.CANTalon;

/**
 * <p>Author: Ricky</p>
 */
@objid ("899c26b9-be06-403c-b6de-a1d9d8a5322b")
public class ClimbingArm {
    @objid ("534c37a6-0aae-48e4-9ccf-475ca53c092c")
    protected CANTalon extendRetractMotor;

    @objid ("c7d0df5b-dc61-45b7-bf18-207bbf5d74a8")
    protected CANTalon raiseLowerMotor;

    @objid ("c215a8ac-a925-4c23-aab7-ec75ee354734")
    public ClimbingArm() {
    }

    @objid ("a661b8a2-befb-4ed3-95cc-e445670fa559")
    public double getPositionOfStageOne() {
    }

    @objid ("6de04ff3-23df-472a-9c94-0e666ba14075")
    public void setPositionOfStageOne(double position) {
    }

    @objid ("a46baf37-02e4-43cf-bd92-42521e614a74")
    public boolean isStageTwoExtended() {
    }

    @objid ("5caadcfa-d288-4f21-999d-8c9b7150dba7")
    public boolean isStageTwoRetracted() {
    }

    @objid ("5447b3c0-a7a2-45f6-8b5a-ec97ee145ea1")
    public void extendStageTwo() {
    }

    @objid ("0053d6a3-1878-46e1-b726-12de04b65c1d")
    public void retractStageTwo() {
    }

    @objid ("b1bdbdec-2f99-4692-89bb-a109403a4565")
    public void extendHook() {
    }

    @objid ("e9b32a28-ba8d-4bf9-bd50-c926ba81ac19")
    public void retractHook() {
    }

}
