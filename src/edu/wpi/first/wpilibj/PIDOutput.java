/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * This interface allows PIDController to write it's results to its output.
 * @author dtjones
 */
@objid ("4b7a834f-81dc-4677-a847-1a306a2f02da")
public interface PIDOutput {
    /**
     * Set the output to the value calculated by PIDController
     * @param output the value calculated by PIDController
     */
    @objid ("22519dad-6260-4aae-9af6-ba7ffc029695")
    void pidWrite(double output);

}
