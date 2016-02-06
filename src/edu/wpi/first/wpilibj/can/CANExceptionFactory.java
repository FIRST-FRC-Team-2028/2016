/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.can;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.communication.NIRioStatus;
import edu.wpi.first.wpilibj.util.UncleanStatusException;

@objid ("651edf6e-88b9-4b1d-8462-a2fe6337310e")
public class CANExceptionFactory {
// FRC Error codes
    @objid ("5ee8217e-f72d-4b8d-94d5-7ddba0b91475")
     static final int ERR_CANSessionMux_InvalidBuffer = -44086;

    @objid ("17a17ee9-5e94-4ba8-b801-1ec615697313")
     static final int ERR_CANSessionMux_MessageNotFound = -44087;

    @objid ("9bc96501-0401-4d4e-8691-b0bdef692341")
     static final int ERR_CANSessionMux_NotAllowed = -44088;

    @objid ("ed472e79-5080-4d63-b978-328ccc8f5e1d")
     static final int ERR_CANSessionMux_NotInitialized = -44089;

    @objid ("5769c8d5-2fa7-43e9-9108-601b87ef980f")
    public static void checkStatus(int status, int messageID) throws CANNotInitializedException, CANMessageNotAllowedException, CANInvalidBufferException, UncleanStatusException {
        switch (status) {
        case NIRioStatus.kRioStatusSuccess:
            // Everything is ok... don't throw.
            return;
        case ERR_CANSessionMux_InvalidBuffer:
        case NIRioStatus.kRIOStatusBufferInvalidSize:
            throw new CANInvalidBufferException();
        case ERR_CANSessionMux_MessageNotFound:
        case NIRioStatus.kRIOStatusOperationTimedOut:
            throw new CANMessageNotFoundException();
        case ERR_CANSessionMux_NotAllowed:
        case NIRioStatus.kRIOStatusFeatureNotSupported:
            throw new CANMessageNotAllowedException("MessageID = " + Integer.toString(messageID));
        case ERR_CANSessionMux_NotInitialized:
        case NIRioStatus.kRIOStatusResourceNotInitialized:
            throw new CANNotInitializedException();
        default:
            throw new UncleanStatusException("Fatal status code detected:  " + Integer.toString(status));
        }
    }

}
