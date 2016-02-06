/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.hal.HALUtil;
import edu.wpi.first.wpilibj.hal.SolenoidJNI;

/**
 * SolenoidBase class is the common base class for the Solenoid and
 * DoubleSolenoid classes.
 */
@objid ("5cba2cb5-d4f3-4426-b199-48111fb72b32")
public abstract class SolenoidBase extends SensorBase {
    @objid ("a80bb933-166e-442e-b1dd-8454546d2f2c")
    private ByteBuffer[] m_ports;

    @objid ("89e6bd1c-dda5-4992-9d1a-5864f57c1b30")
    protected int m_moduleNumber; // /< The number of the solenoid module being used.

    @objid ("e9713d9c-a199-4c0a-8f5e-45f60aa4028d")
    protected Resource m_allocated = new Resource(63* SensorBase.kSolenoidChannels);

    /**
     * Constructor.
     * @param moduleNumber The PCM CAN ID
     */
    @objid ("66cb384b-fdc0-4d71-a3e9-94ed64766777")
    public SolenoidBase(final int moduleNumber) {
        m_moduleNumber = moduleNumber;
        m_ports = new ByteBuffer[SensorBase.kSolenoidChannels];
        for (int i = 0; i < SensorBase.kSolenoidChannels; i++) {
            ByteBuffer port = SolenoidJNI.getPortWithModule((byte) moduleNumber, (byte) i);
            IntBuffer status = IntBuffer.allocate(1);
            m_ports[i] = SolenoidJNI.initializeSolenoidPort(port, status);
            HALUtil.checkStatus(status);
        }
    }

    /**
     * Set the value of a solenoid.
     * @param value The value you want to set on the module.
     * @param mask The channels you want to be affected.
     */
    @objid ("d877486f-ff82-4d93-9136-042b57408af4")
    protected synchronized void set(int value, int mask) {
        IntBuffer status = ByteBuffer.allocateDirect(4).asIntBuffer();
        for (int i = 0; i < SensorBase.kSolenoidChannels; i++) {
            int local_mask = 1 << i;
            if ((mask & local_mask) != 0)
                SolenoidJNI.setSolenoid(m_ports[i], (byte) (value & local_mask), status);
        }
        HALUtil.checkStatus(status);
    }

    /**
     * Read all 8 solenoids from the module used by this solenoid as a single byte
     * @return The current value of all 8 solenoids on this module.
     */
    @objid ("9c969407-09b6-448f-a6e4-e786f8606377")
    public byte getAll() {
        byte value = 0;
        IntBuffer status = ByteBuffer.allocateDirect(4).asIntBuffer();
        for (int i = 0; i < SensorBase.kSolenoidChannels; i++) {
            value |= SolenoidJNI.getSolenoid(m_ports[i], status) << i;
        }
        HALUtil.checkStatus(status);
        return value;
    }

    /**
     * Reads complete solenoid blacklist for all 8 solenoids as a single byte.
     * 
     * If a solenoid is shorted, it is added to the blacklist and
     * disabled until power cycle, or until faults are cleared.
     * @see #clearAllPCMStickyFaults()
     * @return The solenoid blacklist of all 8 solenoids on the module.
     */
    @objid ("1b92981f-6dd7-489f-a97e-0f33be3147a1")
    public byte getPCMSolenoidBlackList() {
        IntBuffer status = ByteBuffer.allocateDirect(4).asIntBuffer();
        
        byte retval = SolenoidJNI.getPCMSolenoidBlackList(m_ports[0], status);
        HALUtil.checkStatus(status);
        return retval;
    }

    /**
     * @return true if PCM sticky fault is set : The common
     * highside solenoid voltage rail is too low,
     * most likely a solenoid channel is shorted.
     */
    @objid ("46eb752c-dda7-4647-9342-64bdf209f5b3")
    public boolean getPCMSolenoidVoltageStickyFault() {
        IntBuffer status = ByteBuffer.allocateDirect(4).asIntBuffer();
        
        boolean retval = SolenoidJNI.getPCMSolenoidVoltageStickyFault(m_ports[0], status);
        HALUtil.checkStatus(status);
        return retval;
    }

    /**
     * @return true if PCM is in fault state : The common
     * highside solenoid voltage rail is too low,
     * most likely a solenoid channel is shorted.
     */
    @objid ("9db7a601-23f9-48ac-b13b-2611ee915299")
    public boolean getPCMSolenoidVoltageFault() {
        IntBuffer status = ByteBuffer.allocateDirect(4).asIntBuffer();
        
        boolean retval = SolenoidJNI.getPCMSolenoidVoltageFault(m_ports[0], status);
        HALUtil.checkStatus(status);
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
    @objid ("27282254-0199-486d-aeb3-2c186de9ebd7")
    public void clearAllPCMStickyFaults() {
        IntBuffer status = ByteBuffer.allocateDirect(4).asIntBuffer();
        
        SolenoidJNI.clearAllPCMStickyFaults(m_ports[0], status);
        HALUtil.checkStatus(status);
    }

}
