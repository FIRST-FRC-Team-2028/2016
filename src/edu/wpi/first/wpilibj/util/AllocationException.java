/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.util;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Exception indicating that the resource is already allocated
 * @author dtjones
 */
@objid ("bd125fc6-0d13-4d10-8ff5-3d3a4af30565")
public class AllocationException extends RuntimeException {
    /**
     * Create a new AllocationException
     * @param msg the message to attach to the exception
     */
    @objid ("601a4fab-57ab-42ea-83c7-479d54e279c5")
    public AllocationException(String msg) {
        super(msg);
    }

}
