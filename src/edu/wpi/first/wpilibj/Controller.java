/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * An interface for controllers. Controllers run control loops, the most command
 * are PID controllers and there variants, but this includes anything that is
 * controlling an actuator in a separate thread.
 * 
 * @author alex
 */
@objid ("4d8e5413-e48e-4b4a-81b3-0011cc9a6ff8")
interface Controller {
    /**
     * Allows the control loop to run.
     */
    @objid ("ddc68fd1-f35c-4c01-a749-67edd0b317f7")
    void enable();

    /**
     * Stops the control loop from running until explicitly re-enabled by calling
     * {@link enable()}.
     */
    @objid ("70be2d4b-aa36-4492-937a-49a9cd022649")
    void disable();

}
