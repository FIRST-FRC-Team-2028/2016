package com.PhantomMentalists.Stronghold;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.CANTalon;

@objid ("a38fcf5c-bc25-4155-8afc-05a8335fee44")
public class TankTread {
    /**
     * <Enter note text here>
     */
    @objid ("38a1efee-fa70-49db-b68c-adb8fbbd8233")
    protected CANTalon tiltMotor;

    /**
     * May become a Spike if no speed control is required (and Spikes save weight and/or space compared to Talon)
     */
    @objid ("1d94eac0-d4b2-4a8a-a1e4-6c0b611adf80")
    protected CANTalon driveMotor;

    @objid ("ef54315f-71a9-48b1-8840-4441e6e9db2c")
    public TankTread() {
    }

    @objid ("59a12327-d8c3-4b2c-8521-285b79fef074")
    public void manualRaise() {
    }

    @objid ("bea74f7c-38ea-4673-bb5d-d27ab80f34de")
    public void manualLower() {
    }

    @objid ("84011ff3-a38d-40cd-b74e-f92f6824adaa")
    public void manualRun() {
    }

    @objid ("08cddce2-8b6f-4b33-aaab-0e3e90c8fed4")
    public void manualStop() {
    }

}
