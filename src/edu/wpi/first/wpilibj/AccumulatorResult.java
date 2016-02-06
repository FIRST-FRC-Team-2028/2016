/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Structure for holding the values stored in an accumulator
 * @author brad
 */
@objid ("00123288-4c04-4b92-8b4d-09aeeb6d21b8")
public class AccumulatorResult {
    /**
     * The total value accumulated
     */
    @objid ("cc29f763-c09e-4f65-8d71-8359f8ff43ff")
    public long value;

    /**
     * The number of sample vaule was accumulated over
     */
    @objid ("4bd9905f-5d76-487b-9134-f9bf05a74199")
    public long count;

}
