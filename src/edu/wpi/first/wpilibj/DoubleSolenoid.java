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
import edu.wpi.first.wpilibj.tables.ITableListener;
import edu.wpi.first.wpilibj.util.AllocationException;
import edu.wpi.first.wpilibj.util.CheckedAllocationException;

/**
 * DoubleSolenoid class for running 2 channels of high voltage Digital Output.
 * 
 * The DoubleSolenoid class is typically used for pneumatics solenoids that
 * have two positions controlled by two separate channels.
 */
@objid ("7d689f62-e72a-49ed-817a-15848856be4b")
public class DoubleSolenoid extends SolenoidBase implements LiveWindowSendable {
    @objid ("e0a695d6-3a1a-466b-9922-94346fe1bcd0")
    private int m_forwardChannel; // /< The forward channel on the module to control.

    @objid ("47ef44c1-516a-44a1-85b7-158051065856")
    private int m_reverseChannel; // /< The reverse channel on the module to control.

    @objid ("ea9ff6b5-db7a-448a-94fc-d5bf8714324b")
    private byte m_forwardMask; // /< The mask for the forward channel.

    @objid ("1300247d-4724-42bd-b203-1c93305c6df4")
    private byte m_reverseMask; // /< The mask for the reverse channel.

    @objid ("4a41d32d-c9e7-4c7c-b024-5760bfdc6878")
    private ITable m_table;

    @objid ("0bbb7aa7-6b38-47ee-8233-c84d1e9cae76")
    private ITableListener m_table_listener;

    /**
     * Common function to implement constructor behavior.
     */
    @objid ("8eaa22be-03c2-44aa-9924-0aaf05e99449")
    private synchronized void initSolenoid() {
        checkSolenoidModule(m_moduleNumber);
        checkSolenoidChannel(m_forwardChannel);
        checkSolenoidChannel(m_reverseChannel);
        
        try {
            m_allocated.allocate(m_moduleNumber * kSolenoidChannels + m_forwardChannel);
        } catch (CheckedAllocationException e) {
            throw new AllocationException(
                "Solenoid channel " + m_forwardChannel + " on module " + m_moduleNumber + " is already allocated");
        }
        try {
            m_allocated.allocate(m_moduleNumber * kSolenoidChannels + m_reverseChannel);
        } catch (CheckedAllocationException e) {
            throw new AllocationException(
                "Solenoid channel " + m_reverseChannel + " on module " + m_moduleNumber + " is already allocated");
        }
        m_forwardMask = (byte) (1 << m_forwardChannel);
        m_reverseMask = (byte) (1 << m_reverseChannel);
        
        UsageReporting.report(tResourceType.kResourceType_Solenoid, m_forwardChannel, m_moduleNumber);
        UsageReporting.report(tResourceType.kResourceType_Solenoid, m_reverseChannel, m_moduleNumber);
        LiveWindow.addActuator("DoubleSolenoid", m_moduleNumber, m_forwardChannel, this);
    }

    /**
     * Constructor.
     * Uses the default PCM ID of 0
     * @param forwardChannel The forward channel number on the PCM (0..7).
     * @param reverseChannel The reverse channel number on the PCM (0..7).
     */
    @objid ("22855a02-fbd8-4ce2-9033-4c7db6e6f33a")
    public DoubleSolenoid(final int forwardChannel, final int reverseChannel) {
        super(getDefaultSolenoidModule());
        m_forwardChannel = forwardChannel;
        m_reverseChannel = reverseChannel;
        initSolenoid();
    }

    /**
     * Constructor.
     * @param moduleNumber The module number of the solenoid module to use.
     * @param forwardChannel The forward channel on the module to control (0..7).
     * @param reverseChannel The reverse channel on the module to control (0..7).
     */
    @objid ("b6e602f8-2507-43e3-ba16-a44084d5c60f")
    public DoubleSolenoid(final int moduleNumber, final int forwardChannel, final int reverseChannel) {
        super(moduleNumber);
        m_forwardChannel = forwardChannel;
        m_reverseChannel = reverseChannel;
        initSolenoid();
    }

    /**
     * Destructor.
     */
    @objid ("84d9fe92-d1a7-4455-9191-c6b41c1b9ae5")
    public synchronized void free() {
        m_allocated.free(m_moduleNumber * kSolenoidChannels + m_forwardChannel);
        m_allocated.free(m_moduleNumber * kSolenoidChannels + m_reverseChannel);
    }

    /**
     * Set the value of a solenoid.
     * @param value The value to set (Off, Forward, Reverse)
     */
    @objid ("5f58b1c3-dfbc-4893-89e2-73e25fedbda8")
    public void set(final Value value) {
        byte rawValue = 0;
        
        switch (value.value) {
        case Value.kOff_val:
            rawValue = 0x00;
            break;
        case Value.kForward_val:
            rawValue = m_forwardMask;
            break;
        case Value.kReverse_val:
            rawValue = m_reverseMask;
            break;
        }
        
        set(rawValue, m_forwardMask | m_reverseMask);
    }

    /**
     * Read the current value of the solenoid.
     * @return The current value of the solenoid.
     */
    @objid ("1058d672-c98b-4f76-999d-5e4e292094ab")
    public Value get() {
        byte value = getAll();
        
        if ((value & m_forwardMask) != 0) return Value.kForward;
        if ((value & m_reverseMask) != 0) return Value.kReverse;
        return Value.kOff;
    }

    /**
     * Check if the forward solenoid is blacklisted.
     * If a solenoid is shorted, it is added to the blacklist and
     * disabled until power cycle, or until faults are cleared.
     * @see #clearAllPCMStickyFaults()
     * @return If solenoid is disabled due to short.
     */
    @objid ("dd38c432-0317-40a0-ad74-63e0360ae9c4")
    public boolean isFwdSolenoidBlackListed() {
        int blackList = getPCMSolenoidBlackList();
        return ((blackList & m_forwardMask) != 0);
    }

    /**
     * Check if the reverse solenoid is blacklisted.
     * If a solenoid is shorted, it is added to the blacklist and
     * disabled until power cycle, or until faults are cleared.
     * @see #clearAllPCMStickyFaults()
     * @return If solenoid is disabled due to short.
     */
    @objid ("11501211-d50d-4509-addd-53a731c2935d")
    public boolean isRevSolenoidBlackListed() {
        int blackList = getPCMSolenoidBlackList();
        return ((blackList & m_reverseMask) != 0);
    }

/*
     * Live Window code, only does anything if live window is activated.
     */
    @objid ("2107a0ff-dd69-45bf-bb04-342069a046c8")
    public String getSmartDashboardType() {
        return "Double Solenoid";
    }

    /**
     * {@inheritDoc}
     */
    @objid ("2867900d-6df3-4a4a-8d8e-3117fb2bdb32")
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @objid ("c8f255f3-4492-4bc1-b914-e6d461581ade")
    public ITable getTable() {
        return m_table;
    }

    /**
     * {@inheritDoc}
     */
    @objid ("1415e46c-2010-4874-b878-6e5b870b2768")
    public void updateTable() {
        if (m_table != null) {
            //TODO: this is bad
            m_table.putString("Value", (get() == Value.kForward ? "Forward" : (get() == Value.kReverse ? "Reverse" : "Off")));
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("55981e85-35aa-4f6d-a808-9976e724d682")
    public void startLiveWindowMode() {
        set(Value.kOff); // Stop for safety
        m_table_listener = new ITableListener() {
            public void valueChanged(ITable itable, String key, Object value, boolean bln) {
                //TODO: this is bad also
                if (value.toString().equals("Reverse"))
                    set(Value.kReverse);
                else if (value.toString().equals("Forward"))
                    set(Value.kForward);
                else
                    set(Value.kOff);
            }
        };
        m_table.addTableListener("Value", m_table_listener, true);
    }

    /**
     * {@inheritDoc}
     */
    @objid ("ef9f4c01-ac8d-4b61-acc7-0c711514f856")
    public void stopLiveWindowMode() {
        set(Value.kOff); // Stop for safety
        // TODO: Broken, should only remove the listener from "Value" only.
        m_table.removeTableListener(m_table_listener);
    }

    /**
     * Possible values for a DoubleSolenoid
     */
    @objid ("8687e50c-8017-4411-ba5c-ae42a008eb8d")
    public static class Value {
        @objid ("691407b3-5e7b-4999-83a9-a6ad39c14a46")
        public final int value;

        @objid ("0c972ec9-96fb-4847-9c86-40322418314d")
        public static final int kOff_val = 0;

        @objid ("aafa4234-bc23-4579-b7b4-05fb3b0cff55")
        public static final int kForward_val = 1;

        @objid ("6335df14-0e0f-4b3e-8ecc-9fa3aa885c66")
        public static final int kReverse_val = 2;

        @objid ("4828eb9a-4993-4c3a-8fc6-3f6fe18dd5b5")
        public static final Value kOff = new Value(kOff_val);

        @objid ("aa3c3d89-b9cb-4350-aa14-9a8891a345d1")
        public static final Value kForward = new Value(kForward_val);

        @objid ("e18143d8-708b-494e-9ab3-3a5e667ef478")
        public static final Value kReverse = new Value(kReverse_val);

        @objid ("9835f5fb-cace-459c-833c-82b4be3c9cea")
        private Value(int value) {
            this.value = value;
        }

    }

}
