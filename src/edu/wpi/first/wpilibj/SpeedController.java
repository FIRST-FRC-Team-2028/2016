/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Interface for speed controlling devices.
 */
@objid ("d30a575e-c104-4b3c-af32-423b359b867d")
public interface SpeedController extends PIDOutput {
    /**
     * Common interface for getting the current set speed of a speed controller.
     * @return The current set speed.  Value is between -1.0 and 1.0.
     */
    @objid ("d2572763-534e-4ebe-a1a9-f37e0854d39e")
    double get();

    /**
     * Common interface for setting the speed of a speed controller.
     * @param speed The speed to set.  Value should be between -1.0 and 1.0.
     * @param syncGroup The update group to add this Set() to, pending UpdateSyncGroup().  If 0, update immediately.
     */
    @objid ("bc60918a-8772-4b73-bb79-69838504f2cd")
    void set(double speed, byte syncGroup);

    /**
     * Common interface for setting the speed of a speed controller.
     * @param speed The speed to set.  Value should be between -1.0 and 1.0.
     */
    @objid ("cddb5e0c-2d01-4ceb-b56a-f376e0d78999")
    void set(double speed);

    /**
     * Disable the speed controller
     */
    @objid ("6c3e00fa-2127-46d2-a9d7-6eddf25db4ac")
    void disable();

}
