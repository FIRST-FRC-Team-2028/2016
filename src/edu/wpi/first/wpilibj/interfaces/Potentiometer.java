/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.interfaces;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.PIDSource;

/**
 * @author alex
 */
@objid ("2175ba04-8a49-4e74-97fb-7586d3912568")
public interface Potentiometer extends PIDSource {
    @objid ("7b543c8b-9471-472a-8490-dcdf4e7e57c8")
    double get();

}
