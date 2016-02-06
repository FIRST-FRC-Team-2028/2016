/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.buttons;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.GenericHID;

/**
 * @author bradmiller
 */
@objid ("be3d9b82-8e4e-429e-a3af-88b1ac18b245")
public class JoystickButton extends Button {
    @objid ("8852a28f-b2d6-40c9-bc4c-b26978f6453a")
     int m_buttonNumber;

    @objid ("a6012b5d-251d-498d-b1d2-170f1259d543")
     GenericHID m_joystick;

    /**
     * Create a joystick button for triggering commands
     * @param joystick The GenericHID object that has the button (e.g. Joystick, KinectStick, etc)
     * @param buttonNumber The button number (see {@link GenericHID#getRawButton(int) }
     */
    @objid ("3a80be9a-c602-4107-8f6b-9d89feb49214")
    public JoystickButton(GenericHID joystick, int buttonNumber) {
        m_joystick = joystick;
        m_buttonNumber = buttonNumber;
    }

    /**
     * Gets the value of the joystick button
     * @return The value of the joystick button
     */
    @objid ("d43264b7-a8f9-42f2-a01c-437ea722d4ac")
    public boolean get() {
        return m_joystick.getRawButton(m_buttonNumber);
    }

}
