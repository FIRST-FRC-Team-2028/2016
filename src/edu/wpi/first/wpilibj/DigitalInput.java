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
import edu.wpi.first.wpilibj.hal.DIOJNI;
import edu.wpi.first.wpilibj.hal.HALUtil;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * Class to read a digital input. This class will read digital inputs and return
 * the current value on the channel. Other devices such as encoders, gear tooth
 * sensors, etc. that are implemented elsewhere will automatically allocate
 * digital inputs and outputs as required. This class is only for devices like
 * switches etc. that aren't implemented anywhere else.
 */
@objid ("69f18740-dc4b-4402-85f5-f112944bb351")
public class DigitalInput extends DigitalSource implements LiveWindowSendable {
    @objid ("0631ff9c-bbf2-41b2-a2d6-92c3913f59bb")
    private ITable m_table;

    /**
     * Create an instance of a Digital Input class. Creates a digital input
     * given a channel.
     * @param channel the DIO channel for the digital input 0-9 are on-board, 10-25 are on the MXP
     */
    @objid ("5de0e7b7-4983-4b16-bea4-b500ea76f1d0")
    public DigitalInput(int channel) {
        initDigitalPort(channel, true);
        
        LiveWindow.addSensor("DigitalInput", channel, this);
        UsageReporting.report(tResourceType.kResourceType_DigitalInput, channel);
    }

    /**
     * Get the value from a digital input channel. Retrieve the value of a
     * single digital input channel from the FPGA.
     * @return the status of the digital input
     */
    @objid ("365bb534-2b00-436e-9c6a-9d31ec27d01d")
    public boolean get() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        boolean value = DIOJNI.getDIO(m_port, status.asIntBuffer()) != 0;
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

    /**
     * Get the channel of the digital input
     * @return The GPIO channel number that this object represents.
     */
    @objid ("ef8bab8e-afca-4bd9-8845-454d9f009bf9")
    public int getChannel() {
        return m_channel;
    }

    @objid ("30c89972-c39e-46d2-a7b3-d9f492a2439e")
    @Override
    public boolean getAnalogTriggerForRouting() {
        return false;
    }

/*
     * Live Window code, only does anything if live window is activated.
     */
    @objid ("3efcd9f0-756c-464f-8cb6-7d2f8fa1f86d")
    @Override
    public String getSmartDashboardType() {
        return "Digital Input";
    }

    /**
     * {@inheritDoc}
     */
    @objid ("5aa9daaf-7526-4a37-bda4-b9054f8c3ef8")
    @Override
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @objid ("3e7f6ebb-dc67-41ca-bc73-b5ba6aeb2649")
    @Override
    public void updateTable() {
        if (m_table != null) {
            m_table.putBoolean("Value", get());
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("f8149ab3-fe11-4eb4-94b3-a6d4baa16740")
    @Override
    public ITable getTable() {
        return m_table;
    }

    /**
     * {@inheritDoc}
     */
    @objid ("50ad6710-f468-410d-ad57-ce8a1583c5e0")
    @Override
    public void startLiveWindowMode() {
    }

    /**
     * {@inheritDoc}
     */
    @objid ("80fa61a9-4e18-450e-b478-0172f0008352")
    @Override
    public void stopLiveWindowMode() {
    }

}
