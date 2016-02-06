/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.can;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Exception indicating that a CAN driver library entry-point
 * was passed an invalid buffer.  Typically, this is due to a
 * buffer being too small to include the needed safety token.
 */
@objid ("338a0143-f6e4-47ec-9f57-15c2da7642a1")
public class CANInvalidBufferException extends RuntimeException {
    @objid ("1f830924-0a58-4484-b297-9994ad2cc519")
    public CANInvalidBufferException() {
        super();
    }

}
