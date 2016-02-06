/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.hal.DIOJNI;
import edu.wpi.first.wpilibj.hal.HALUtil;
import edu.wpi.first.wpilibj.util.AllocationException;
import edu.wpi.first.wpilibj.util.CheckedAllocationException;

/**
 * DigitalSource Interface. The DigitalSource represents all the possible inputs
 * for a counter or a quadrature encoder. The source may be either a digital
 * input or an analog input. If the caller just provides a channel, then a
 * digital input will be constructed and freed when finished for the source. The
 * source can either be a digital input or analog trigger but not both.
 */
@objid ("f574ef54-686b-4f1a-81b2-d5d707f57e2f")
public abstract class DigitalSource extends InterruptableSensorBase {
    @objid ("6540c70c-cf7e-4621-973d-40edd0ad836f")
    protected ByteBuffer m_port;

    @objid ("eeb748cd-23c7-47d3-88b2-443057db269f")
    protected int m_channel;

    @objid ("aac62d17-55b7-4107-be9b-6d346409c7ea")
    protected static Resource channels = new Resource(kDigitalChannels);

    @objid ("059c3ec0-1f9a-4e76-8dff-d98f38b21178")
    protected void initDigitalPort(int channel, boolean input) {
        m_channel = channel;
        
        checkDigitalChannel(m_channel); // XXX: Replace with
                                        // HALLibrary.checkDigitalChannel when
                                        // implemented
        
        try {
            channels.allocate(m_channel);
        } catch (CheckedAllocationException ex) {
            throw new AllocationException("Digital input " + m_channel
                    + " is already allocated");
        }
        
        ByteBuffer port_pointer = DIOJNI.getPort((byte) channel);
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        m_port = DIOJNI.initializeDigitalPort(port_pointer, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        DIOJNI.allocateDIO(m_port, (byte) (input ? 1 : 0), status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    @objid ("4bd4d479-ab8d-4688-b503-8eb3d1d27f64")
    @Override
    public void free() {
        channels.free(m_channel);
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        DIOJNI.freeDIO(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        m_channel = 0;
    }

    /**
     * Get the channel routing number
     * @return channel routing number
     */
    @objid ("0f3b617c-8afd-4ad1-a705-b93207dac498")
    @Override
    public int getChannelForRouting() {
        return m_channel;
    }

    /**
     * Get the module routing number
     * @return 0
     */
    @objid ("6e3af4e5-758a-4641-ab23-dc90f8cc0387")
    @Override
    public byte getModuleForRouting() {
        return 0;
    }

    /**
     * Is this an analog trigger
     * @return true if this is an analog trigger
     */
    @objid ("b63b0d0a-9b0f-489d-a0c6-55ff7ba50821")
    @Override
    public boolean getAnalogTriggerForRouting() {
        return false;
    }

}
