/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.util;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Exception for bad status codes from the chip object
 * @author Brian
 */
@objid ("b28475d2-2165-4101-8b27-0eaa2fa0729e")
public final class UncleanStatusException extends IllegalStateException {
    @objid ("fa5e2d35-a1e7-417f-8a29-a24a641067d4")
    private final int statusCode;

    /**
     * Create a new UncleanStatusException
     * @param status the status code that caused the exception
     * @param message A message describing the exception
     */
    @objid ("0cf75345-7daf-4095-b987-23489e8c0031")
    public UncleanStatusException(int status, String message) {
        super(message);
        statusCode = status;
    }

    /**
     * Create a new UncleanStatusException
     * @param status the status code that caused the exception
     */
    @objid ("b8e72218-cb97-43e3-9cc3-ed2533059f50")
    public UncleanStatusException(int status) {
        this(status, "Status code was non-zero");
    }

    /**
     * Create a new UncleanStatusException
     * @param message a message describing the exception
     */
    @objid ("6c6a8745-4d63-4d9f-a0c0-bf5cd91d95d3")
    public UncleanStatusException(String message) {
        this(-1, message);
    }

    /**
     * Create a new UncleanStatusException
     */
    @objid ("3886d1ea-5f82-4707-8015-182d0e5e34c7")
    public UncleanStatusException() {
        this(-1, "Status code was non-zero");
    }

    /**
     * Create a new UncleanStatusException
     * @return the status code that caused the exception
     */
    @objid ("4a76685b-3c4c-4193-8188-d6578bb2afc5")
    public int getStatus() {
        return statusCode;
    }

}
