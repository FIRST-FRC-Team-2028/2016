/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.can;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Exception indicating that a can message is not available from Network
 * Communications.  This usually just means we already have the most recent
 * value cached locally.
 */
@objid ("b1ae3701-39c7-4fad-ade5-c9efcabf9797")
public class CANMessageNotFoundException extends RuntimeException {
    @objid ("7ba12a58-078a-46d3-8a53-4415a366f959")
    public CANMessageNotFoundException() {
        super();
    }

}
