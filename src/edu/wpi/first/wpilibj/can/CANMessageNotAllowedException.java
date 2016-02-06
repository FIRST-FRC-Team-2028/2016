/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.can;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Exception indicating that the Jaguar CAN Driver layer refused to send a
 * restricted message ID to the CAN bus.
 */
@objid ("51bec4f8-2a10-4d59-b1af-fe70173a11e9")
public class CANMessageNotAllowedException extends RuntimeException {
    @objid ("0c4ef731-9614-45f0-98ae-5e75894b735a")
    public CANMessageNotAllowedException(String msg) {
        super(msg);
    }

}
