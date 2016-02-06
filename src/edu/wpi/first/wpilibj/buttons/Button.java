/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.buttons;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.command.Command;

/**
 * This class provides an easy way to link commands to OI inputs.
 * 
 * It is very easy to link a button to a command.  For instance, you could
 * link the trigger button of a joystick to a "score" command.
 * 
 * This class represents a subclass of Trigger that is specifically aimed at
 * buttons on an operator interface as a common use case of the more generalized
 * Trigger objects. This is a simple wrapper around Trigger with the method names
 * renamed to fit the Button object use.
 * 
 * @author brad
 */
@objid ("26ac7af5-46c1-4b8e-a93c-af36d1560ee8")
public abstract class Button extends Trigger {
    /**
     * Starts the given command whenever the button is newly pressed.
     * @param command the command to start
     */
    @objid ("95fb0d82-54af-4168-a705-3017e0b246cc")
    public void whenPressed(final Command command) {
        whenActive(command);
    }

    /**
     * Constantly starts the given command while the button is held.
     * 
     * {@link Command#start()} will be called repeatedly while the button is held,
     * and will be canceled when the button is released.
     * @param command the command to start
     */
    @objid ("b3e44ec4-601b-4e62-a68b-afde66be2630")
    public void whileHeld(final Command command) {
        whileActive(command);
    }

    /**
     * Starts the command when the button is released
     * @param command the command to start
     */
    @objid ("373ec912-8162-45c7-876c-519fcaf71eb8")
    public void whenReleased(final Command command) {
        whenInactive(command);
    }

    /**
     * Toggles the command whenever the button is pressed (on then off then on)
     * @param command the command to start
     */
    @objid ("bf1fd124-6e09-437d-96a0-7a41be7606aa")
    public void toggleWhenPressed(final Command command) {
        toggleWhenActive(command);
    }

    /**
     * Cancel the command when the button is pressed
     * @param command the command to start
     */
    @objid ("609fb292-cad4-4d51-bfcb-c3413afec9cc")
    public void cancelWhenPressed(final Command command) {
        cancelWhenActive(command);
    }

}
