/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in $(WIND_BASE)/WPILib.  */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.hal.AccelerometerJNI;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * Built-in accelerometer.
 * 
 * This class allows access to the RoboRIO's internal accelerometer.
 */
@objid ("e52d87ad-9d93-4e1a-8782-87e4537f050d")
public class BuiltInAccelerometer implements Accelerometer, LiveWindowSendable {
    @objid ("a938ef7b-a7cd-4801-8b5a-9cea0bd0fb34")
    private ITable m_table;

    /**
     * Constructor.
     * @param range The range the accelerometer will measure
     */
    @objid ("4d393b7e-ab74-4bcb-b752-4bb25edb8e1c")
    public BuiltInAccelerometer(Range range) {
        setRange(range);
        UsageReporting.report(tResourceType.kResourceType_Accelerometer, 0, 0, "Built-in accelerometer");
        LiveWindow.addSensor("BuiltInAccel", 0, this);
    }

    /**
     * Constructor.
     * The accelerometer will measure +/-8 g-forces
     */
    @objid ("e242e4ea-3af2-4500-a7aa-691eeb3f2356")
    public BuiltInAccelerometer() {
        this(Range.k8G);
    }

    /**
     * {inheritdoc}
     */
    @objid ("e407d162-ccd3-4057-9515-31fed98221ab")
    @Override
    public void setRange(Range range) {
        AccelerometerJNI.setAccelerometerActive(false);
        
        switch(range) {
        case k2G:
            AccelerometerJNI.setAccelerometerRange(0);
            break;
        case k4G:
            AccelerometerJNI.setAccelerometerRange(1);
            break;
        case k8G:
            AccelerometerJNI.setAccelerometerRange(2);
            break;
        case k16G:
            throw new RuntimeException("16G range not supported (use k2G, k4G, or k8G)");
        }
        
        AccelerometerJNI.setAccelerometerActive(true);
    }

    /**
     * @return The acceleration of the RoboRIO along the X axis in g-forces
     */
    @objid ("2cde37f7-b58c-4a34-9f7c-20e91875cd75")
    @Override
    public double getX() {
        return AccelerometerJNI.getAccelerometerX();
    }

    /**
     * @return The acceleration of the RoboRIO along the Y axis in g-forces
     */
    @objid ("6a1ac895-2b5b-4586-94d6-cb1f85ddd3d6")
    @Override
    public double getY() {
        return AccelerometerJNI.getAccelerometerY();
    }

    /**
     * @return The acceleration of the RoboRIO along the Z axis in g-forces
     */
    @objid ("8db1907b-eb61-478f-abc4-7b4c9260715c")
    @Override
    public double getZ() {
        return AccelerometerJNI.getAccelerometerZ();
    }

    @objid ("85e07aa8-9074-4c42-95c5-4646c865c8c2")
    public String getSmartDashboardType() {
        return "3AxisAccelerometer";
    }

    /**
     * {@inheritDoc}
     */
    @objid ("e2c6ce84-cfa6-41cb-9c7a-6bec46d1e790")
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @objid ("6bcf2a6e-e81c-413a-a148-7d72a5029632")
    public void updateTable() {
        if (m_table != null) {
            m_table.putNumber("X", getX());
            m_table.putNumber("Y", getY());
            m_table.putNumber("Z", getZ());
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("808f9afb-5e51-4a72-a6ba-b71ccc0813a8")
    public ITable getTable() {
        return m_table;
    }

    @objid ("cb44dc04-3c5a-426c-a43a-686b059e0b6a")
    public void startLiveWindowMode() {
    }

    @objid ("f2b8849e-9fb8-4a7e-a51d-355f947167e8")
    public void stopLiveWindowMode() {
    }

}
