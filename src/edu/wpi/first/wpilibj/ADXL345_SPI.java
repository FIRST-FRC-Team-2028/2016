/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
 * @author mwills
 */
@objid ("6c5662a7-689b-41d1-8992-fcc94a5a5833")
public class ADXL345_SPI extends SensorBase implements Accelerometer, LiveWindowSendable {
    @objid ("4cad0f14-e58e-46f1-985c-3bab1987103d")
    private static final int kPowerCtlRegister = 0x2D;

    @objid ("98f97ec2-b630-4f29-b78c-31cb12ae641f")
    private static final int kDataFormatRegister = 0x31;

    @objid ("053a3d72-4149-4f2a-999e-59cee8394c74")
    private static final int kDataRegister = 0x32;

    @objid ("3babc1ce-53cc-4b27-86bc-0211d117087a")
    private static final double kGsPerLSB = 0.00390625;

    @objid ("0c4be30c-bb82-4190-a7cf-140d0af341b7")
    private static final int kAddress_Read = 0x80;

    @objid ("a2d96ac1-f133-4d75-af94-9b9ea78f827b")
    private static final int kAddress_MultiByte = 0x40;

    @objid ("99f8e996-90db-4e18-9b70-5891eec53349")
    private static final int kPowerCtl_Link = 0x20;

    @objid ("91497b84-3047-4135-9893-bbe0db3c19d0")
    private static final int kPowerCtl_AutoSleep = 0x10;

    @objid ("0c1ba501-d6b0-43d0-87ef-812994eb0682")
    private static final int kPowerCtl_Measure = 0x08;

    @objid ("c842345e-263b-41d0-a673-430d061c1495")
    private static final int kPowerCtl_Sleep = 0x04;

    @objid ("70c53e88-6192-4d08-876a-811069255dfa")
    private static final int kDataFormat_SelfTest = 0x80;

    @objid ("6c14e801-323a-48fb-8bfb-969672fc50c3")
    private static final int kDataFormat_SPI = 0x40;

    @objid ("ade54635-3519-411f-a6b9-2a944d788832")
    private static final int kDataFormat_IntInvert = 0x20;

    @objid ("ce8bb8b9-6db8-46db-80f5-dff550e11238")
    private static final int kDataFormat_FullRes = 0x08;

    @objid ("2bfd7f29-d553-4c53-98ef-9cbb1b223ef7")
    private static final int kDataFormat_Justify = 0x04;

    @objid ("40b0046b-ed7d-4150-8e0a-517bfc743c8b")
    private ITable m_table;

    @objid ("2b07a2b1-c918-4d12-9206-a86399a6d86d")
    private SPI m_spi;

    /**
     * Constructor.
     * @param port The SPI port that the accelerometer is connected to
     * @param range The range (+ or -) that the accelerometer will measure.
     */
    @objid ("28e62b79-7697-4e9e-b52e-d5a5850e882e")
    public ADXL345_SPI(edu.wpi.first.wpilibj.SPI.Port port, Range range) {
        m_spi = new SPI(port);
        init(range);
        LiveWindow.addSensor("ADXL345_SPI", port.getValue(), this);
    }

    @objid ("c9d49535-1d92-4c34-bcee-fea1c3b0a3c3")
    public void free() {
        m_spi.free();
    }

    /**
     * Set SPI bus parameters, bring device out of sleep and set format
     * @param range The range (+ or -) that the accelerometer will measure.
     */
    @objid ("f9bda7ad-182d-4bf6-8398-e264f6e57e2a")
    private void init(Range range) {
        m_spi.setClockRate(500000);
        m_spi.setMSBFirst();
        m_spi.setSampleDataOnFalling();
        m_spi.setClockActiveLow();
        m_spi.setChipSelectActiveHigh();
        
        // Turn on the measurements
        byte[] commands = new byte[2];
        commands[0] = kPowerCtlRegister;
        commands[1] = kPowerCtl_Measure;
        m_spi.write(commands, 2);
        
        setRange(range);
        
        UsageReporting.report(tResourceType.kResourceType_ADXL345, tInstances.kADXL345_SPI);
    }

    /**
     * {inheritdoc}
     */
    @objid ("973db0c5-459a-4c7c-923d-3cc3cbc47393")
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
        byte[] commands = new byte[]{
            kDataFormatRegister,
            (byte)(kDataFormat_FullRes | value)
        };
        m_spi.write(commands, commands.length);
    }

    /**
     * {@inheritDoc}
     */
    @objid ("d28d5462-6757-4a5d-95fa-e1b91bbd2b0c")
    @Override
    public double getX() {
        return getAcceleration(Axes.kX);
    }

    /**
     * {@inheritDoc}
     */
    @objid ("00c58ef5-426c-4897-989e-2d0653b22cb7")
    @Override
    public double getY() {
        return getAcceleration(Axes.kY);
    }

    /**
     * {@inheritDoc}
     */
    @objid ("88bac19c-0e35-4e20-a9fa-4e5a9bba2b63")
    @Override
    public double getZ() {
        return getAcceleration(Axes.kZ);
    }

    /**
     * Get the acceleration of one axis in Gs.
     * @param axis The axis to read from.
     * @return Acceleration of the ADXL345 in Gs.
     */
    @objid ("03c28d1f-76a0-43e2-afe7-40413b624ee5")
    public double getAcceleration(edu.wpi.first.wpilibj.ADXL345_SPI.Axes axis) {
        byte[] transferBuffer = new byte[3];
        transferBuffer[0] = (byte)((kAddress_Read | kAddress_MultiByte | kDataRegister) + axis.value);
        m_spi.transaction(transferBuffer, transferBuffer, 3);
        ByteBuffer rawAccel = ByteBuffer.wrap(transferBuffer, 1, 2);
        //Sensor is little endian
        rawAccel.order(ByteOrder.LITTLE_ENDIAN);
        return rawAccel.getShort() * kGsPerLSB;
    }

    /**
     * Get the acceleration of all axes in Gs.
     * @return An object containing the acceleration measured on each axis of the ADXL345 in Gs.
     */
    @objid ("3f009d27-a3c7-42f5-a6e1-cb43b21dfde7")
    public edu.wpi.first.wpilibj.ADXL345_SPI.AllAxes getAccelerations() {
        ADXL345_SPI.AllAxes data = new ADXL345_SPI.AllAxes();
        byte dataBuffer[] = new byte[7];
        if (m_spi != null)
        {
            // Select the data address.
            dataBuffer[0] = (byte)(kAddress_Read | kAddress_MultiByte | kDataRegister);
            m_spi.transaction(dataBuffer, dataBuffer, 7);
            ByteBuffer rawData = ByteBuffer.wrap(dataBuffer, 1, 6);
            //Sensor is little endian... swap bytes
            rawData.order(ByteOrder.LITTLE_ENDIAN);
        
            data.XAxis = rawData.getShort() * kGsPerLSB;
            data.YAxis = rawData.getShort() * kGsPerLSB;
            data.ZAxis = rawData.getShort() * kGsPerLSB;
        }
        return data;
    }

    @objid ("c8687f08-3161-4d8b-b19e-a835f965db45")
    public String getSmartDashboardType() {
        return "3AxisAccelerometer";
    }

    /**
     * {@inheritDoc}
     */
    @objid ("88280fca-722e-40e7-8059-2bdb07824d9f")
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @objid ("6d4c16dd-37a0-4cbe-a01e-09e8aa5a983e")
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
    @objid ("56687e10-ca93-48bb-a558-b05c1b5f9395")
    public ITable getTable() {
        return m_table;
    }

    @objid ("8d6f3a29-c6c3-4885-a0d9-02c344ee6ea8")
    public void startLiveWindowMode() {
    }

    @objid ("80bc01c4-3847-4b83-ba28-0e43d8c6a8b2")
    public void stopLiveWindowMode() {
    }

    @objid ("63eb814c-7f8b-4b58-ad0f-37a67c08dbba")
    public static class Axes {
        /**
         * The integer value representing this enumeration
         */
        @objid ("1efa6dd9-9c20-43b4-8da5-7b6943511c82")
        public final byte value;

        @objid ("4ea927e6-77ff-45cb-9e1e-de2fabc3c675")
         static final byte kX_val = 0x00;

        @objid ("30679def-cda4-4fd9-9575-86526006b779")
         static final byte kY_val = 0x02;

        @objid ("71bd8853-2f07-4a9a-97a0-5d96af479563")
         static final byte kZ_val = 0x04;

        @objid ("d59afe20-b1f5-4eea-9b1f-42ee01773617")
        public static final edu.wpi.first.wpilibj.ADXL345_SPI.Axes kX = new ADXL345_SPI.Axes(kX_val);

        @objid ("6d736bf1-d250-4662-ba29-e548d6bb8b8f")
        public static final edu.wpi.first.wpilibj.ADXL345_SPI.Axes kY = new ADXL345_SPI.Axes(kY_val);

        @objid ("b95f04ba-bab1-40b1-8b8d-b387aee6a6cc")
        public static final edu.wpi.first.wpilibj.ADXL345_SPI.Axes kZ = new ADXL345_SPI.Axes(kZ_val);

        @objid ("fec92b05-1ce5-4965-b2ce-a27154c0283e")
        private Axes(byte value) {
            this.value = value;
        }

    }

    @objid ("2407692e-1dcb-404c-97cf-d83e44c95b3b")
    public static class AllAxes {
        @objid ("79f05635-80bc-4041-bd7b-10bd20b4052a")
        public double XAxis;

        @objid ("d1d70be8-4536-43eb-9fa3-60cdd76ca922")
        public double YAxis;

        @objid ("8cd3b953-7bb4-4943-adc8-8374fabd329e")
        public double ZAxis;

    }

}
