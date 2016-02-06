/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.buttons;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * This class is intended to be used within a program.  The programmer can manually set its value.
 * Also includes a setting for whether or not it should invert its value.
 * 
 * @author Joe
 */
@objid ("402d9ec5-762b-4239-9ed8-a8515a6c187c")
public class InternalButton extends Button {
    @objid ("dcd1b566-121d-44ac-b6e7-c9187ba56f1b")
     boolean pressed;

    @objid ("4bd6c438-c637-4d19-a897-9a90f67faecd")
     boolean inverted;

    /**
     * Creates an InternalButton that is not inverted.
     */
    @objid ("4f4a1c45-6a23-4f72-a1c4-f7fb77cac25b")
    public InternalButton() {
        this(false);
    }

    /**
     * Creates an InternalButton which is inverted depending on the input.
     * @param inverted if false, then this button is pressed when set to true, otherwise it is pressed when set to false.
     */
    @objid ("131e29d0-c4a2-408c-be66-0d34b2373f35")
    public InternalButton(boolean inverted) {
        this.pressed = this.inverted = inverted;
    }

    @objid ("8a6eb6e8-c233-4e84-9f6f-b2eefd4cd9d1")
    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    @objid ("578551a1-20ec-4f6f-b398-9c6d9c233abb")
    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    @objid ("d28aa162-4e7a-4a78-9d91-e5b657f2e263")
    public boolean get() {
        return pressed ^ inverted;
    }

}
