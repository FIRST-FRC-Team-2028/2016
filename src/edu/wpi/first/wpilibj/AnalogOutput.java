/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.hal.AnalogJNI;
import edu.wpi.first.wpilibj.hal.HALUtil;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.util.AllocationException;
import edu.wpi.first.wpilibj.util.CheckedAllocationException;

/**
 * Analog output class.
 */
@objid ("ded0da35-a7d1-43c5-9634-17272a661a98")
public class AnalogOutput extends SensorBase implements LiveWindowSendable {
    @objid ("87e642c6-d54f-437c-96ea-403eaa8d8a95")
    private ByteBuffer m_port;

    @objid ("8708b43f-2a83-475d-914d-24d5542166ec")
    private int m_channel;

    @objid ("5bdacaef-80f4-402b-a9e2-4032ff3d6b30")
    private ITable m_table;

    @objid ("fc3f222e-8a21-474c-9276-f5d774964bf9")
    private static Resource channels = new Resource(kAnalogOutputChannels);

    /**
     * Construct an analog output on a specified MXP channel.
     * @param channel The channel number to represent.
     */
    @objid ("ad6610be-685f-444b-94dc-fad5917ad24d")
    public AnalogOutput(final int channel) {
        m_channel = channel;
        
        if (AnalogJNI.checkAnalogOutputChannel(channel) == 0) {
            throw new AllocationException("Analog output channel " + m_channel
                    + " cannot be allocated. Channel is not present.");
        }
        try {
            channels.allocate(channel);
        } catch (CheckedAllocationException e) {
            throw new AllocationException("Analog output channel " + m_channel
                    + " is already allocated");
        }
        
        ByteBuffer port_pointer = AnalogJNI.getPort((byte)channel);
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        m_port = AnalogJNI.initializeAnalogOutputPort(port_pointer, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        
        LiveWindow.addSensor("AnalogOutput", channel, this);
        UsageReporting.report(tResourceType.kResourceType_AnalogChannel, channel, 1);
    }

    /**
     * Channel destructor.
     */
    @objid ("c9640d75-91cf-436d-9127-02749ee66844")
    public void free() {
        channels.free(m_channel);
        m_channel = 0;
    }

    @objid ("5bed7582-8416-4c23-8173-49d1f3619953")
    public void setVoltage(double voltage) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        AnalogJNI.setAnalogOutput(m_port, voltage, status.asIntBuffer());
        
        HALUtil.checkStatus(status.asIntBuffer());
    }

    @objid ("2d09c4cf-dd62-48d2-99b5-77debe765824")
    public double getVoltage() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        double voltage = AnalogJNI.getAnalogOutput(m_port, status.asIntBuffer());
        
        HALUtil.checkStatus(status.asIntBuffer());
        return voltage;
    }

/*
     * Live Window code, only does anything if live window is activated.
     */
    @objid ("5d92d690-559e-4d80-88ac-1fb543c9a961")
    public String getSmartDashboardType() {
        return "Analog Output";
    }

    /**
     * {@inheritDoc}
     */
    @objid ("29655bd5-c074-4abd-a7c9-31b598d49cf5")
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @objid ("769f254e-0a05-4e55-92ff-123fd79f994e")
    public void updateTable() {
        if (m_table != null) {
            m_table.putNumber("Value", getVoltage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("9462b691-99e1-4ce1-8143-7c240988ee3b")
    public ITable getTable() {
        return m_table;
    }

    /**
     * Analog Channels don't have to do anything special when entering the
     * LiveWindow. {@inheritDoc}
     */
    @objid ("60c78467-3492-4023-a2f5-ace6cc33d8e2")
    public void startLiveWindowMode() {
    }

    /**
     * Analog Channels don't have to do anything special when exiting the
     * LiveWindow. {@inheritDoc}
     */
    @objid ("aaca762a-7256-4f9b-9a33-1ab5b539f51a")
    public void stopLiveWindowMode() {
    }

}
