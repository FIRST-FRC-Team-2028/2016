/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * Handle operation of an analog accelerometer.
 * The accelerometer reads acceleration directly through the sensor. Many sensors have
 * multiple axis and can be treated as multiple devices. Each is calibrated by finding
 * the center value over a period of time.
 */
@objid ("529bd188-843e-43c7-9a60-ff7f50758442")
public class AnalogAccelerometer extends SensorBase implements PIDSource, LiveWindowSendable {
    @objid ("45adbcf7-22e0-4ab3-ad09-1834fb5aa211")
    private double m_voltsPerG = 1.0;

    @objid ("ce848317-8ec3-4265-9f64-b92874afef83")
    private double m_zeroGVoltage = 2.5;

    @objid ("1b88f1d6-83cf-40e8-bb66-f4d181d9613b")
    private boolean m_allocatedChannel;

/*
     * Live Window code, only does anything if live window is activated.
     */
    @objid ("9708abaa-69f2-4ff1-801c-c97adb487fa2")
    private ITable m_table;

    @objid ("964079ee-34b5-4f4b-b147-3d165f812d7a")
    private AnalogInput m_analogChannel;

    /**
     * Common initialization
     */
    @objid ("34282a6e-eb46-487d-afdb-f885d7c91d5c")
    private void initAccelerometer() {
        UsageReporting.report(tResourceType.kResourceType_Accelerometer, m_analogChannel.getChannel());
        LiveWindow.addSensor("Accelerometer", m_analogChannel.getChannel(), this);
    }

    /**
     * Create a new instance of an accelerometer.
     * 
     * The constructor allocates desired analog channel.
     * @param channel The channel number for the analog input the accelerometer is connected to
     */
    @objid ("d0191825-52e4-470b-8193-68d2a7b0bec3")
    public AnalogAccelerometer(final int channel) {
        m_allocatedChannel = true;
        m_analogChannel = new AnalogInput(channel);
        initAccelerometer();
    }

    /**
     * Create a new instance of Accelerometer from an existing AnalogChannel.
     * Make a new instance of accelerometer given an AnalogChannel. This is particularly
     * useful if the port is going to be read as an analog channel as well as through
     * the Accelerometer class.
     * @param channel The existing AnalogInput object for the analog input the accelerometer is connected to
     */
    @objid ("7a45e51b-f9e0-44dc-be8a-0474c958ca7c")
    public AnalogAccelerometer(AnalogInput channel) {
        m_allocatedChannel = false;
        if (channel == null)
            throw new NullPointerException("Analog Channel given was null");
        m_analogChannel = channel;
        initAccelerometer();
    }

    /**
     * Delete the analog components used for the accelerometer.
     */
    @objid ("97b8c6ec-f928-4d3c-a625-4f5359e56b5a")
    public void free() {
        if (m_analogChannel != null && m_allocatedChannel) {
            m_analogChannel.free();
        }
        m_analogChannel = null;
    }

    /**
     * Return the acceleration in Gs.
     * 
     * The acceleration is returned units of Gs.
     * @return The current acceleration of the sensor in Gs.
     */
    @objid ("903667cf-6df1-438c-b9a4-a72ea06eb669")
    public double getAcceleration() {
        return (m_analogChannel.getAverageVoltage() - m_zeroGVoltage) / m_voltsPerG;
    }

    /**
     * Set the accelerometer sensitivity.
     * 
     * This sets the sensitivity of the accelerometer used for calculating the acceleration.
     * The sensitivity varies by accelerometer model. There are constants defined for various models.
     * @param sensitivity The sensitivity of accelerometer in Volts per G.
     */
    @objid ("e24530f2-eac5-447e-8ecc-0f67845cb1b4")
    public void setSensitivity(double sensitivity) {
        m_voltsPerG = sensitivity;
    }

    /**
     * Set the voltage that corresponds to 0 G.
     * 
     * The zero G voltage varies by accelerometer model. There are constants defined for various models.
     * @param zero The zero G voltage.
     */
    @objid ("78cd2411-ee7c-4111-a69d-35a9147b5cb8")
    public void setZero(double zero) {
        m_zeroGVoltage = zero;
    }

    /**
     * Get the result to use in PIDController
     * @return the result to use in PIDController
     */
    @objid ("a1774360-e6da-47eb-bf77-075779b8fff9")
    public double pidGet() {
        return getAcceleration();
    }

    @objid ("f7019225-b5a3-4c98-b27e-ec2e7e6dfd25")
    public String getSmartDashboardType() {
        return "Accelerometer";
    }

    /**
     * {@inheritDoc}
     */
    @objid ("995795ec-ec17-4c51-a2ac-14448c9439dc")
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @objid ("1e48831a-e6e1-462d-ac44-a4da8703e570")
    public ITable getTable() {
        return m_table;
    }

    /**
     * {@inheritDoc}
     */
    @objid ("beb7fc9b-e3ea-469f-a144-5e9cb8c713cf")
    public void updateTable() {
        if (m_table != null) {
            m_table.putNumber("Value", getAcceleration());
        }
    }

    /**
     * Analog Channels don't have to do anything special when entering the LiveWindow.
     * {@inheritDoc}
     */
    @objid ("a4a373b6-ea8a-4a7f-a899-fb8081110e79")
    public void startLiveWindowMode() {
    }

    /**
     * Analog Channels don't have to do anything special when exiting the LiveWindow.
     * {@inheritDoc}
     */
    @objid ("2c30198d-bffb-49c2-87f0-c315f61176c6")
    public void stopLiveWindowMode() {
    }

}
