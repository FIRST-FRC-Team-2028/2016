/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in $(WIND_BASE)/WPILib.  */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.interfaces;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Interface for 3-axis accelerometers
 */
@objid ("f3b2e2fe-c992-4dc4-90cb-44806011554a")
public interface Accelerometer {
    /**
     * Common interface for setting the measuring range of an accelerometer.
     * @param range The maximum acceleration, positive or negative, that the
     * accelerometer will measure.  Not all accelerometers support all ranges.
     */
    @objid ("839142fd-1d6f-4db0-bddf-a71ddd8e4a73")
    void setRange(Range range);

    /**
     * Common interface for getting the x axis acceleration
     * @return The acceleration along the x axis in g-forces
     */
    @objid ("54088fdd-4d8f-4c84-aef4-3f26bb8823c4")
    double getX();

    /**
     * Common interface for getting the y axis acceleration
     * @return The acceleration along the y axis in g-forces
     */
    @objid ("194b3388-5f98-4aba-a6ee-1aed9d927c44")
    double getY();

    /**
     * Common interface for getting the z axis acceleration
     * @return The acceleration along the z axis in g-forces
     */
    @objid ("d160df2a-ce31-4fb7-8ab9-f9da754a6247")
    double getZ();

    @objid ("bbaabe6f-d153-454a-a441-7c12a6f189dc")
    public enum Range {
        k2G,
        k4G,
        k8G,
        k16G;
    }

}
