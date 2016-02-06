/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.can;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Exception indicating that the CAN driver layer has not been initialized.
 * This happens when an entry-point is called before a CAN driver plugin
 * has been installed.
 */
@objid ("a0788150-aff7-4936-b5f5-8999e5f2c54d")
public class CANNotInitializedException extends RuntimeException {
    @objid ("6d1dacc2-ff3d-428d-8b17-d36d66cac8ad")
    public CANNotInitializedException() {
        super();
    }

}
