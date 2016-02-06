/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.util;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Exception indicating that the resource is already allocated
 * This is meant to be thrown by the resource class
 * @author dtjones
 */
@objid ("5d0035d3-d792-4150-b8b9-ef1f9e4ac735")
public class CheckedAllocationException extends Exception {
    /**
     * Create a new CheckedAllocationException
     * @param msg the message to attach to the exception
     */
    @objid ("203634b3-8f75-4125-bc6e-14f14d61b6e7")
    public CheckedAllocationException(String msg) {
        super(msg);
    }

}
