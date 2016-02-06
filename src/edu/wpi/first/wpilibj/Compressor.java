package edu.wpi.first.wpilibj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.hal.CompressorJNI;
import edu.wpi.first.wpilibj.hal.HALUtil;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * Class for operating the PCM (Pneumatics compressor module)
 * The PCM automatically will run in close-loop mode by default whenever a Solenoid object is
 * created. For most cases the Compressor object does not need to be
 * instantiated or used in a robot program.
 * 
 * This class is only required in cases where more detailed status or to enable/disable
 * closed loop control. Note: you cannot operate the compressor directly from this class as
 * doing so would circumvent the safety provided in using the pressure switch and closed
 * loop control. You can only turn off closed loop control, thereby stopping the compressor
 * from operating.
 */
@objid ("ab260faf-a885-40fa-a45a-dafb31aed422")
public class Compressor extends SensorBase implements LiveWindowSendable {
    @objid ("e217ed7e-201f-4f2b-a04d-ccf433555f86")
    private ByteBuffer m_pcm;

    @objid ("a677ea23-a670-462a-bb87-89ace836f0eb")
    private ITable m_table;

    /**
     * Create an instance of the Compressor
     * @param pcmId The PCM CAN device ID. Most robots that use PCM will have a single module. Use this
     * for supporting a second module other than the default.
     */
    @objid ("dc625382-b920-4d58-8211-f5ab079219f0")
    public Compressor(int pcmId) {
        initCompressor(pcmId);
    }

    /**
     * Create an instance of the Compressor
     * Makes a new instance of the compressor using the default PCM ID (0). Additional modules can be
     * supported by making a new instance and specifying the CAN ID
     */
    @objid ("6a78f052-6542-4bb9-a7a7-7144e4d6a91f")
    public Compressor() {
        initCompressor(getDefaultSolenoidModule());
    }

    @objid ("adc8dee1-18d6-4a97-8831-89ebefc3cdd9")
    private void initCompressor(int module) {
        m_table = null;
        
        m_pcm = CompressorJNI.initializeCompressor((byte)module);
    }

    /**
     * Start the compressor running in closed loop control mode
     * Use the method in cases where you would like to manually stop and start the compressor
     * for applications such as conserving battery or making sure that the compressor motor
     * doesn't start during critical operations.
     */
    @objid ("cd96f831-e422-4d0a-90d3-ff41d891e0fd")
    public void start() {
        setClosedLoopControl(true);
    }

    /**
     * Stop the compressor from running in closed loop control mode.
     * Use the method in cases where you would like to manually stop and start the compressor
     * for applications such as conserving battery or making sure that the compressor motor
     * doesn't start during critical operations.
     */
    @objid ("0a8e5a82-8928-4bb2-b8a9-a50da3269d9a")
    public void stop() {
        setClosedLoopControl(false);
    }

    /**
     * Get the enabled status of the compressor
     * @return true if the compressor is on
     */
    @objid ("b8d28d12-c30b-4d7c-86dc-c709648719bf")
    public boolean enabled() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        boolean on = CompressorJNI.getCompressor(m_pcm, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return on;
    }

    /**
     * Get the current pressure switch value
     * @return true if the pressure is low by reading the pressure switch that is plugged into the PCM
     */
    @objid ("bf284f85-65e1-4ac4-87d0-cb84ed18099e")
    public boolean getPressureSwitchValue() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        boolean on = CompressorJNI.getPressureSwitch(m_pcm, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return on;
    }

    /**
     * Get the current being used by the compressor
     * @return current consumed in amps for the compressor motor
     */
    @objid ("e4c69497-2611-4d4b-a0aa-17fb4cffc11f")
    public float getCompressorCurrent() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        float current = CompressorJNI.getCompressorCurrent(m_pcm, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return current;
    }

    /**
     * Set the PCM in closed loop control mode
     * @param on If true sets the compressor to be in closed loop control mode otherwise
     * normal operation of the compressor is disabled.
     */
    @objid ("b1a47b7b-4497-416d-90c1-08aefbae3659")
    public void setClosedLoopControl(boolean on) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        CompressorJNI.setClosedLoopControl(m_pcm, on, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Gets the current operating mode of the PCM
     * @return true if compressor is operating on closed-loop mode, otherwise return false.
     */
    @objid ("315ae98a-f444-4c36-8c4d-9696780754eb")
    public boolean getClosedLoopControl() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        boolean on = CompressorJNI.getClosedLoopControl(m_pcm, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return on;
    }

    /**
     * @return true if PCM is in fault state : Compressor Drive is
     * disabled due to compressor current being too high.
     */
    @objid ("d9ce0028-135e-43bf-9e5f-8da406c194d1")
    public boolean getCompressorCurrentTooHighFault() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        boolean retval = CompressorJNI.getCompressorCurrentTooHighFault(m_pcm, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retval;
    }

    /**
     * @return true if PCM sticky fault is set : Compressor Drive is
     * disabled due to compressor current being too high.
     */
    @objid ("02a69cff-6adc-4530-8dbb-775ce79d84d1")
    public boolean getCompressorCurrentTooHighStickyFault() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        boolean retval = CompressorJNI.getCompressorCurrentTooHighStickyFault(m_pcm, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retval;
    }

    /**
     * @return true if PCM sticky fault is set : Compressor output
     * appears to be shorted.
     */
    @objid ("7886ab64-c171-4f5c-b7f0-36574b875ab8")
    public boolean getCompressorShortedStickyFault() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        boolean retval = CompressorJNI.getCompressorShortedStickyFault(m_pcm, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retval;
    }

    /**
     * @return true if PCM is in fault state : Compressor output
     * appears to be shorted.
     */
    @objid ("7d24f89d-6503-4ab8-8ee9-a85b44838416")
    public boolean getCompressorShortedFault() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        boolean retval = CompressorJNI.getCompressorShortedFault(m_pcm, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retval;
    }

    /**
     * @return true if PCM sticky fault is set : Compressor does not
     * appear to be wired, i.e. compressor is
     * not drawing enough current.
     */
    @objid ("856e6c53-20bc-4b74-b616-13645f0566be")
    public boolean getCompressorNotConnectedStickyFault() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        boolean retval = CompressorJNI.getCompressorNotConnectedStickyFault(m_pcm, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retval;
    }

    /**
     * @return true if PCM is in fault state : Compressor does not
     * appear to be wired, i.e. compressor is
     * not drawing enough current.
     */
    @objid ("ab6f42a3-f074-46bd-a1b3-d161e050ef5a")
    public boolean getCompressorNotConnectedFault() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        boolean retval = CompressorJNI.getCompressorNotConnectedFault(m_pcm, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retval;
    }

    /**
     * Clear ALL sticky faults inside PCM that Compressor is wired to.
     * 
     * If a sticky fault is set, then it will be persistently cleared.  Compressor drive
     * maybe momentarily disable while flags are being cleared. Care should be
     * taken to not call this too frequently, otherwise normal compressor
     * functionality may be prevented.
     * 
     * If no sticky faults are set then this call will have no effect.
     */
    @objid ("c75fbc29-17f7-4689-b0c8-77a1bcc4338d")
    public void clearAllPCMStickyFaults() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        CompressorJNI.clearAllPCMStickyFaults(m_pcm, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    @objid ("7567ff4a-8c27-4f7c-b85c-a3410a912039")
    @Override
    public void startLiveWindowMode() {
    }

    @objid ("da253e6d-391c-44a2-984d-9cb5dbad2e48")
    @Override
    public void stopLiveWindowMode() {
    }

    @objid ("eb4cfa17-4145-4b6a-9c78-1cffc82787b0")
    @Override
    public String getSmartDashboardType() {
        return "Compressor";
    }

    @objid ("001d5290-75f8-44de-87d4-4e275cc52822")
    @Override
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    @objid ("19fbd5d9-82f9-4f0b-a95c-e2c83eced694")
    @Override
    public ITable getTable() {
        return m_table;
    }

    @objid ("712f6fb4-e574-4de6-bb7b-2410fae15283")
    @Override
    public void updateTable() {
        if (m_table != null) {
            m_table.putBoolean("Enabled", enabled());
            m_table.putBoolean("Pressure Switch", getPressureSwitchValue());
        }
    }

}
