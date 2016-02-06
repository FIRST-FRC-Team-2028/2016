/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.hal.HALUtil;
import edu.wpi.first.wpilibj.hal.PDPJNI;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * Class for getting voltage, current, temperature, power and energy from the CAN PDP.
 * The PDP must be at CAN Address 0.
 * @author Thomas Clark
 */
@objid ("50fc8142-14a5-4b7b-8485-74eef25dc598")
public class PowerDistributionPanel extends SensorBase implements LiveWindowSendable {
/*
     * Live Window code, only does anything if live window is activated.
     */
    @objid ("920944cb-c114-4e21-80a8-9db733d8fdd8")
    private ITable m_table;

    @objid ("5f26b743-344f-416a-93fd-fcdcc635e15a")
    public PowerDistributionPanel() {
    }

    /**
     * Query the input voltage of the PDP
     * @return The voltage of the PDP in volts
     */
    @objid ("fcae5b94-df8f-48b5-ac17-91f6bab88bc9")
    public double getVoltage() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        double voltage = PDPJNI.getPDPVoltage(status.asIntBuffer());
        return voltage;
    }

    /**
     * Query the temperature of the PDP
     * @return The temperature of the PDP in degrees Celsius
     */
    @objid ("1f2cb9d7-c9fa-4b96-a8bb-df4bfb411081")
    public double getTemperature() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        double temperature = PDPJNI.getPDPTemperature(status.asIntBuffer());
        return temperature;
    }

    /**
     * Query the current of a single channel of the PDP
     * @return The current of one of the PDP channels (channels 0-15) in Amperes
     */
    @objid ("ea5ebd19-148e-44bc-ab30-142e6c2c7c8f")
    public double getCurrent(int channel) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        double current = PDPJNI.getPDPChannelCurrent((byte)channel, status.asIntBuffer());
        
        checkPDPChannel(channel);
        return current;
    }

    /**
     * Query the current of all monitored PDP channels (0-15)
     * @return The current of all the channels in Amperes
     */
    @objid ("cf5776d2-c931-4404-9d44-d3d5a1ea6d20")
    public double getTotalCurrent() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        double current = PDPJNI.getPDPTotalCurrent(status.asIntBuffer());
        return current;
    }

    /**
     * Query the total power drawn from the monitored PDP channels
     * @return the total power in Watts
     */
    @objid ("61632b6a-ac76-461b-8862-a4e94639ec98")
    public double getTotalPower() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        double power = PDPJNI.getPDPTotalPower(status.asIntBuffer());
        return power;
    }

    /**
     * Query the total energy drawn from the monitored PDP channels
     * @return the total energy in Joules
     */
    @objid ("863f5312-d85b-4412-bdbc-8d94ebe13a65")
    public double getTotalEnergy() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        double energy = PDPJNI.getPDPTotalEnergy(status.asIntBuffer());
        return energy;
    }

    /**
     * Reset the total energy to 0
     */
    @objid ("2a2d28ec-3b2e-42d7-8e68-778272ebdc2c")
    public void resetTotalEnergy() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        PDPJNI.resetPDPTotalEnergy(status.asIntBuffer());
    }

    /**
     * Clear all PDP sticky faults
     */
    @objid ("6a462222-db7d-48ae-8600-d4deb518700a")
    public void clearStickyFaults() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        PDPJNI.clearPDPStickyFaults(status.asIntBuffer());
    }

    @objid ("338218c4-7e29-4ae9-ad82-a2575a443fc0")
    public String getSmartDashboardType() {
        return "PowerDistributionPanel";
    }

    /**
     * {@inheritDoc}
     */
    @objid ("f1d417f6-f06e-4a5f-90c4-8e9edb55024d")
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @objid ("ea1f7e2e-3754-4c1f-82cb-3e546117b7b8")
    public ITable getTable() {
        return m_table;
    }

    /**
     * {@inheritDoc}
     */
    @objid ("b90e592f-69ad-4255-90f1-9f6a63ce105c")
    public void updateTable() {
        if (m_table != null) {
            m_table.putNumber("Chan0", getCurrent(0));
            m_table.putNumber("Chan1", getCurrent(1));
            m_table.putNumber("Chan2", getCurrent(2));
            m_table.putNumber("Chan3", getCurrent(3));
            m_table.putNumber("Chan4", getCurrent(4));
            m_table.putNumber("Chan5", getCurrent(5));
            m_table.putNumber("Chan6", getCurrent(6));
            m_table.putNumber("Chan7", getCurrent(7));
            m_table.putNumber("Chan8", getCurrent(8));
            m_table.putNumber("Chan9", getCurrent(9));
            m_table.putNumber("Chan10", getCurrent(10));
            m_table.putNumber("Chan11", getCurrent(11));
            m_table.putNumber("Chan12", getCurrent(12));
            m_table.putNumber("Chan13", getCurrent(13));
            m_table.putNumber("Chan14", getCurrent(14));
            m_table.putNumber("Chan15", getCurrent(15));
            m_table.putNumber("Voltage", getVoltage());
            m_table.putNumber("TotalCurrent", getTotalCurrent());
        }
    }

    /**
     * PDP doesn't have to do anything special when entering the LiveWindow.
     * {@inheritDoc}
     */
    @objid ("f50afdbc-4da6-4487-963c-e114cf019ebe")
    public void startLiveWindowMode() {
    }

    /**
     * PDP doesn't have to do anything special when exiting the LiveWindow.
     * {@inheritDoc}
     */
    @objid ("ae76a99e-a1e0-4226-9d02-b0c51b8250ed")
    public void stopLiveWindowMode() {
    }

}
