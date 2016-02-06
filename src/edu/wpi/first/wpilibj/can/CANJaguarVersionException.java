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
@objid ("df3c483f-59ee-4e86-b363-23316e99a2ca")
public class CANJaguarVersionException extends RuntimeException {
    @objid ("9dad9542-f16f-4aca-a425-3c8804104263")
    public static final int kMinLegalFIRSTFirmwareVersion = 101;

// 3330 was the first shipping RDK firmware version for the Jaguar
    @objid ("ecbf5f7a-783d-447a-afc0-181fb1d55048")
    public static final int kMinRDKFirmwareVersion = 3330;

    @objid ("0c017fc1-e038-4611-9397-0d789887b5a9")
    public CANJaguarVersionException(int deviceNumber, int fwVersion) {
        super(getString(deviceNumber, fwVersion));
        System.out.println("fwVersion[" + deviceNumber + "]: " + fwVersion);
    }

    @objid ("4d46f219-c3fb-44c4-9034-791ce5939b6b")
    static String getString(int deviceNumber, int fwVersion) {
        String msg;
        if (fwVersion < kMinRDKFirmwareVersion) {
            msg = "Jaguar " + deviceNumber +
                  " firmware is too old.  It must be updated to at least version " +
                  Integer.toString(kMinLegalFIRSTFirmwareVersion) +
                  " of the FIRST approved firmware!";
        } else {
            msg = "Jaguar " + deviceNumber +
                  " firmware is not FIRST approved.  It must be updated to at least version " +
                  Integer.toString(kMinLegalFIRSTFirmwareVersion) +
                  " of the FIRST approved firmware!";
        }
        return msg;
    }

}
