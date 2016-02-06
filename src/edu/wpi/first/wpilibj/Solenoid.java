/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.hal.HALUtil;
import edu.wpi.first.wpilibj.hal.SolenoidJNI;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import edu.wpi.first.wpilibj.util.AllocationException;
import edu.wpi.first.wpilibj.util.CheckedAllocationException;

/**
 * Solenoid class for running high voltage Digital Output.
 * 
 * The Solenoid class is typically used for pneumatics solenoids, but could be used
 * for any device within the current spec of the PCM.
 */
@objid ("16083147-9c1a-4de2-a1d3-764d06b27c9e")
public class Solenoid extends SolenoidBase implements LiveWindowSendable {
    @objid ("7d33a5a9-ea62-4281-8e44-29c9691eac23")
    private int m_channel; // /< The channel to control.

    @objid ("6a7a5055-a817-4500-9f0d-e28b736d0e06")
    private ByteBuffer m_solenoid_port;

    @objid ("50251c58-1ce2-4c3f-800c-a139cf71be41")
    private ITable m_table;

    @objid ("80ed3d08-1594-4cb9-ba3c-5d8ffe8de4e4")
    private ITableListener m_table_listener;

    /**
     * Common function to implement constructor behavior.
     */
    @objid ("c4e1ad66-baac-41e2-b6ff-0711c6f18e91")
    private synchronized void initSolenoid() {
        checkSolenoidModule(m_moduleNumber);
        checkSolenoidChannel(m_channel);
        
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        ByteBuffer port = SolenoidJNI.getPortWithModule((byte) m_moduleNumber, (byte) m_channel);
        m_solenoid_port = SolenoidJNI.initializeSolenoidPort(port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        
        LiveWindow.addActuator("Solenoid", m_moduleNumber, m_channel, this);
        UsageReporting.report(tResourceType.kResourceType_Solenoid, m_channel, m_moduleNumber);
    }

    /**
     * Constructor using the default PCM ID (0)
     * @param channel The channel on the PCM to control (0..7).
     */
    @objid ("006c36ee-b88c-49af-a071-435154146933")
    public Solenoid(final int channel) {
        super(getDefaultSolenoidModule());
        m_channel = channel;
        initSolenoid();
    }

    /**
     * Constructor.
     * @param moduleNumber The CAN ID of the PCM the solenoid is attached to.
     * @param channel The channel on the PCM to control (0..7).
     */
    @objid ("da7817aa-f027-4c49-9982-e475e82e145b")
    public Solenoid(final int moduleNumber, final int channel) {
        super(moduleNumber);
        m_channel = channel;
        initSolenoid();
    }

    /**
     * Destructor.
     */
    @objid ("f031715c-b0f8-4fa4-9dad-c05dcfce27e5")
    public synchronized void free() {
        //      m_allocated.free((m_moduleNumber - 1) * kSolenoidChannels + m_channel - 1);
    }

    /**
     * Set the value of a solenoid.
     * @param on Turn the solenoid output off or on.
     */
    @objid ("69c47fb3-9703-4634-838a-62e4d337bc48")
    public void set(boolean on) {
        byte value = (byte) (on ? 0xFF : 0x00);
        byte mask = (byte) (1 << m_channel);
        
        set(value, mask);
    }

    /**
     * Read the current value of the solenoid.
     * @return The current value of the solenoid.
     */
    @objid ("db87dd04-54f3-4d7d-ab24-7faebf802e6b")
    public boolean get() {
        int value = getAll() & ( 1 << m_channel);
        return (value != 0);
    }

    /**
     * Check if solenoid is blacklisted.
     * If a solenoid is shorted, it is added to the blacklist and
     * disabled until power cycle, or until faults are cleared.
     * @see clearAllPCMStickyFaults()
     * @return If solenoid is disabled due to short.
     */
    @objid ("c73d4bac-0ffd-42c0-b510-d92967137a3d")
    public boolean isBlackListed() {
        int value = getPCMSolenoidBlackList() & ( 1 << m_channel);
        return (value != 0);
    }

/*
     * Live Window code, only does anything if live window is activated.
     */
    @objid ("8e7d2342-91a4-4f32-8e0e-2f0550efaa48")
    public String getSmartDashboardType() {
        return "Solenoid";
    }

    /**
     * {@inheritDoc}
     */
    @objid ("cce62006-f461-4cba-a6f7-ba720e4d912f")
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @objid ("f3a4f289-a540-442b-859c-a61d4b4b6781")
    public ITable getTable() {
        return m_table;
    }

    /**
     * {@inheritDoc}
     */
    @objid ("1c8c6e39-423c-436d-b209-581edbc1e408")
    public void updateTable() {
        if (m_table != null) {
            m_table.putBoolean("Value", get());
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("c5de8389-c95a-4f53-9247-e69246683c20")
    public void startLiveWindowMode() {
        set(false); // Stop for safety
        m_table_listener = new ITableListener() {
            public void valueChanged(ITable itable, String key, Object value, boolean bln) {
                set(((Boolean) value).booleanValue());
            }
        };
        m_table.addTableListener("Value", m_table_listener, true);
    }

    /**
     * {@inheritDoc}
     */
    @objid ("e6a7e907-a0d5-44ad-a933-0ac220460b1e")
    public void stopLiveWindowMode() {
        set(false); // Stop for safety
        // TODO: Broken, should only remove the listener from "Value" only.
        m_table.removeTableListener(m_table_listener);
    }

}
