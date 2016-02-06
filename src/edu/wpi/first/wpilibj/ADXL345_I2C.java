/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tInstances;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * @author dtjones
 */
@objid ("15644110-9eb1-4ce6-9789-f0de15ffd112")
public class ADXL345_I2C extends SensorBase implements Accelerometer, LiveWindowSendable {
    @objid ("b707fcbf-c28b-4508-a186-58fd9134bddb")
    private static final byte kAddress = 0x1D;

    @objid ("3e70427d-bb9e-449c-844f-2011933b66f7")
    private static final byte kPowerCtlRegister = 0x2D;

    @objid ("e8af2cb9-6515-43c2-9894-bbd6045d8018")
    private static final byte kDataFormatRegister = 0x31;

    @objid ("98673883-ea37-4339-a481-27dc3d0688d4")
    private static final byte kDataRegister = 0x32;

    @objid ("6436a0bd-f3f5-438c-970c-9e7fba98ac76")
    private static final double kGsPerLSB = 0.00390625;

    @objid ("101a8e5b-bed3-4a8f-83e0-b1c3d3d8d225")
    private static final byte kPowerCtl_Link = 0x20;

    @objid ("68f29009-d0dd-4d57-9843-269066b2c994")
    private static final byte kPowerCtl_AutoSleep = 0x10;

    @objid ("b167da29-be81-4a75-af2e-4dc09b579a4f")
    private static final byte kPowerCtl_Measure = 0x08;

    @objid ("99a51817-766d-46d2-a335-d877bd62375c")
    private static final byte kPowerCtl_Sleep = 0x04;

    @objid ("529e3ba4-4f62-447f-8c55-9860caf12bdd")
    private static final byte kDataFormat_SelfTest = (byte) 0x80;

    @objid ("1983141b-670a-47dc-91ab-43577f0c0c99")
    private static final byte kDataFormat_SPI = 0x40;

    @objid ("a6949b95-94e0-408c-98f0-58bce5370deb")
    private static final byte kDataFormat_IntInvert = 0x20;

    @objid ("375e89f0-6ad0-4a9c-8df4-8cebfeea6627")
    private static final byte kDataFormat_FullRes = 0x08;

    @objid ("bf5abeaf-2b60-4cc0-a630-c290d714a91f")
    private static final byte kDataFormat_Justify = 0x04;

    @objid ("cb644790-c162-4f34-8e2e-2468d6a6992d")
    private ITable m_table;

    @objid ("021200da-06ec-4157-a9f3-104f05393987")
    private I2C m_i2c;

    /**
     * Constructor.
     * @param port The I2C port the accelerometer is attached to
     * @param range The range (+ or -) that the accelerometer will measure.
     */
    @objid ("fef4997e-1991-49fe-816d-d8cc21285f64")
    public ADXL345_I2C(edu.wpi.first.wpilibj.I2C.Port port, Range range) {
        m_i2c = new I2C(port, kAddress);
        
        // Turn on the measurements
        m_i2c.write(kPowerCtlRegister, kPowerCtl_Measure);
        
        setRange(range);
        
        UsageReporting.report(tResourceType.kResourceType_ADXL345, tInstances.kADXL345_I2C);
        LiveWindow.addSensor("ADXL345_I2C", port.getValue(), this);
    }

    /**
     * {inheritdoc}
     */
    @objid ("1e535312-9ce2-4778-a070-06c7188cd12f")
    @Override
    public void setRange(Range range) {
        byte value = 0;
        
        switch(range) {
        case k2G:
            value = 0;
            break;
        case k4G:
            value = 1;
            break;
        case k8G:
            value = 2;
            break;
        case k16G:
            value = 3;
            break;
        }
        
        // Specify the data format to read
        m_i2c.write(kDataFormatRegister, kDataFormat_FullRes | value);
    }

    /**
     * {@inheritDoc}
     */
    @objid ("ac4d0559-e9ca-4bef-9925-9f25f7255e05")
    @Override
    public double getX() {
        return getAcceleration(Axes.kX);
    }

    /**
     * {@inheritDoc}
     */
    @objid ("20c30b8a-b8ff-42fe-8c7e-6667b56e90ab")
    @Override
    public double getY() {
        return getAcceleration(Axes.kY);
    }

    /**
     * {@inheritDoc}
     */
    @objid ("6d3c4426-bcd6-43ea-8853-453354ba214b")
    @Override
    public double getZ() {
        return getAcceleration(Axes.kZ);
    }

    /**
     * Get the acceleration of one axis in Gs.
     * @param axis The axis to read from.
     * @return Acceleration of the ADXL345 in Gs.
     */
    @objid ("8deec62f-07ac-4d1b-909b-521e452dc769")
    public double getAcceleration(Axes axis) {
        byte[] rawAccel = new byte[2];
        m_i2c.read(kDataRegister + axis.value, rawAccel.length, rawAccel);
        
        // Sensor is little endian... swap bytes
        return accelFromBytes(rawAccel[0], rawAccel[1]);
    }

    @objid ("b2f615d8-cb19-45dd-9348-a58c2972227c")
    private double accelFromBytes(byte first, byte second) {
        short tempLow = (short) (first & 0xff);
        short tempHigh = (short) ((second << 8) & 0xff00);
        return (tempLow | tempHigh) * kGsPerLSB;
    }

    /**
     * Get the acceleration of all axes in Gs.
     * @return An object containing the acceleration measured on each axis of the ADXL345 in Gs.
     */
    @objid ("1fbe7878-c0a4-4eb1-b6e1-0c827f4b2be5")
    public AllAxes getAccelerations() {
        AllAxes data = new AllAxes();
        byte[] rawData = new byte[6];
        m_i2c.read(kDataRegister, rawData.length, rawData);
        
        // Sensor is little endian... swap bytes
        data.XAxis = accelFromBytes(rawData[0], rawData[1]);
        data.YAxis = accelFromBytes(rawData[2], rawData[3]);
        data.ZAxis = accelFromBytes(rawData[4], rawData[5]);
        return data;
    }

    @objid ("95d6dda2-39f6-4b4d-a614-dfe3aca294f4")
    public String getSmartDashboardType() {
        return "3AxisAccelerometer";
    }

    /**
     * {@inheritDoc}
     */
    @objid ("849a785b-c31b-4091-ac2c-f7ecaf6dc6a6")
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @objid ("a80c9f66-753e-4725-971d-e9359cb2a21e")
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
    @objid ("397af925-f51e-4bef-b894-df062d08123c")
    public ITable getTable() {
        return m_table;
    }

    @objid ("4e1e4c16-af34-4491-817c-b7106aac74ee")
    public void startLiveWindowMode() {
    }

    @objid ("b280997d-324a-4690-b524-74c651e5c416")
    public void stopLiveWindowMode() {
    }

    @objid ("e41a78ce-f605-434d-812a-35c97d2bf4b5")
    public static class Axes {
        /**
         * The integer value representing this enumeration
         */
        @objid ("b0bbf7f1-3c94-49fd-95a0-1bdee3677856")
        public final byte value;

        @objid ("0e125689-9e73-4cb1-8d5d-91a578ff4bab")
         static final byte kX_val = 0x00;

        @objid ("1d9e1c86-997a-4bea-9319-1d875e6717a3")
         static final byte kY_val = 0x02;

        @objid ("2279ac78-c823-4d8b-9451-b2d7a3118247")
         static final byte kZ_val = 0x04;

        @objid ("7b15204c-38e9-4edb-81d8-ae9c66a0e857")
        public static final Axes kX = new Axes(kX_val);

        @objid ("c053e04e-5335-41cf-92bb-c55159b9c930")
        public static final Axes kY = new Axes(kY_val);

        @objid ("cc84b115-ac1b-497e-a961-39b88b683806")
        public static final Axes kZ = new Axes(kZ_val);

        @objid ("d20c5789-be48-4c98-b4ed-25711f45694d")
        private Axes(byte value) {
            this.value = value;
        }

    }

    @objid ("c3c2ea2b-36d4-4b5f-aa11-b4030dcba051")
    public static class AllAxes {
        @objid ("a2e0e6b7-f862-4a8e-bd5b-1c85ebfec8c6")
        public double XAxis;

        @objid ("b559af6e-ff37-4415-8492-5204698d1dfe")
        public double YAxis;

        @objid ("e4d2e620-d98d-4efa-be4c-2e7dae5bbb8e")
        public double ZAxis;

    }

}
