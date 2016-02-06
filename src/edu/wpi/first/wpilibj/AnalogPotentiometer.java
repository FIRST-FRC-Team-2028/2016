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
import edu.wpi.first.wpilibj.hal.PowerJNI;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * Class for reading analog potentiometers. Analog potentiometers read
 * in an analog voltage that corresponds to a position. The position is
 * in whichever units you choose, by way of the scaling and offset
 * constants passed to the constructor.
 * 
 * @author Alex Henning
 * @author Colby Skeggs (rail voltage)
 */
@objid ("d1151fd2-6016-43de-8ccc-78c97326351a")
public class AnalogPotentiometer implements Potentiometer, LiveWindowSendable {
    @objid ("071f5ade-4012-4e2a-be32-0436e8f3e6bb")
    private double m_fullRange;

    @objid ("c0ff0b4a-8983-4b64-85ae-7ed309953ac2")
    private double m_offset;

    @objid ("c7188976-7957-4e0b-9ff4-4e82ed828f93")
    private boolean m_init_analog_input;

    @objid ("7db8ea0c-794b-4450-86c5-68e597fcd2bc")
    private ITable m_table;

    @objid ("eed4d28a-2158-47ab-9d40-df4cbde0413a")
    private AnalogInput m_analog_input;

    /**
     * Common initialization code called by all constructors.
     * @param input The {@link AnalogInput} this potentiometer is plugged into.
     * @param fullRange The scaling to multiply the voltage by to get a meaningful unit.
     * @param offset The offset to add to the scaled value for controlling the zero value
     */
    @objid ("c749ddc8-7aa0-4fdf-a701-be5fbd81716a")
    private void initPot(final AnalogInput input, double fullRange, double offset) {
        this.m_fullRange = fullRange;
        this.m_offset = offset;
        m_analog_input = input;
    }

    /**
     * AnalogPotentiometer constructor.
     * 
     * Use the fullRange and offset values so that the output produces
     * meaningful values. I.E: you have a 270 degree potentiometer and
     * you want the output to be degrees with the halfway point as 0
     * degrees. The fullRange value is 270.0(degrees) and the offset is
     * -135.0 since the halfway point after scaling is 135 degrees.
     * 
     * This will calculate the result from the fullRange times the
     * fraction of the supply voltage, plus the offset.
     * @param channel The analog channel this potentiometer is plugged into.
     * @param fullRange The scaling to multiply the fraction by to get a meaningful unit.
     * @param offset The offset to add to the scaled value for controlling the zero value
     */
    @objid ("18030979-4d70-424c-a372-c1ee244c9887")
    public AnalogPotentiometer(final int channel, double fullRange, double offset) {
        AnalogInput input = new AnalogInput(channel);
        m_init_analog_input = true;
        initPot(input, fullRange, offset);
    }

    /**
     * AnalogPotentiometer constructor.
     * 
     * Use the fullRange and offset values so that the output produces
     * meaningful values. I.E: you have a 270 degree potentiometer and
     * you want the output to be degrees with the halfway point as 0
     * degrees. The fullRange value is 270.0(degrees) and the offset is
     * -135.0 since the halfway point after scaling is 135 degrees.
     * 
     * This will calculate the result from the fullRange times the
     * fraction of the supply voltage, plus the offset.
     * @param input The {@link AnalogInput} this potentiometer is plugged into.
     * @param fullRange The scaling to multiply the fraction by to get a meaningful unit.
     * @param offset The offset to add to the scaled value for controlling the zero value
     */
    @objid ("e766bf82-c1ee-43a1-913f-728f7534de9b")
    public AnalogPotentiometer(final AnalogInput input, double fullRange, double offset) {
        m_init_analog_input = false;
        initPot(input, fullRange, offset);
    }

    /**
     * AnalogPotentiometer constructor.
     * 
     * Use the fullRange and offset values so that the output produces
     * meaningful values. I.E: you have a 270 degree potentiometer and
     * you want the output to be degrees with the halfway point as 0
     * degrees. The fullRange value is 270.0(degrees) and the offset is
     * -135.0 since the halfway point after scaling is 135 degrees.
     * @param channel The analog channel this potentiometer is plugged into.
     * @param scale The scaling to multiply the voltage by to get a meaningful unit.
     */
    @objid ("ae6b6a37-366d-4a12-98ee-fa8dba109544")
    public AnalogPotentiometer(final int channel, double scale) {
        this(channel, scale, 0);
    }

    /**
     * AnalogPotentiometer constructor.
     * 
     * Use the fullRange and offset values so that the output produces
     * meaningful values. I.E: you have a 270 degree potentiometer and
     * you want the output to be degrees with the halfway point as 0
     * degrees. The fullRange value is 270.0(degrees) and the offset is
     * -135.0 since the halfway point after scaling is 135 degrees.
     * @param input The {@link AnalogInput} this potentiometer is plugged into.
     * @param scale The scaling to multiply the voltage by to get a meaningful unit.
     */
    @objid ("a7d6c630-98dc-441b-880e-60a2b4a435e1")
    public AnalogPotentiometer(final AnalogInput input, double scale) {
        this(input, scale, 0);
    }

    /**
     * AnalogPotentiometer constructor.
     * @param channel The analog channel this potentiometer is plugged into.
     */
    @objid ("9ef765fa-fc9d-40b6-9b25-86b82371c277")
    public AnalogPotentiometer(final int channel) {
        this(channel, 1, 0);
    }

    /**
     * AnalogPotentiometer constructor.
     * @param input The {@link AnalogInput} this potentiometer is plugged into.
     */
    @objid ("358fc1b5-1cd2-43e2-8da1-edc01c28a1f5")
    public AnalogPotentiometer(final AnalogInput input) {
        this(input, 1, 0);
    }

    /**
     * Get the current reading of the potentiometer.
     * @return The current position of the potentiometer.
     */
    @objid ("2ca2074b-a8f4-48dd-b34c-60e91e47b8cf")
    public double get() {
        return (m_analog_input.getVoltage() / ControllerPower.getVoltage5V()) * m_fullRange + m_offset;
    }

    /**
     * Implement the PIDSource interface.
     * @return The current reading.
     */
    @objid ("70c81260-5f25-4290-b44a-7c88f7ae8c45")
    public double pidGet() {
        return get();
    }

    /**
     * Live Window code, only does anything if live window is activated.
     */
    @objid ("74d6a9de-3d58-4219-9e56-68cf622508ba")
    public String getSmartDashboardType() {
        return "Analog Input";
    }

    /**
     * {@inheritDoc}
     */
    @objid ("5fee02c0-7bde-4f2d-88e1-072354180e85")
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @objid ("801173d0-c5de-4d55-a40f-1cfaa2c185cc")
    public void updateTable() {
        if (m_table != null) {
            m_table.putNumber("Value", get());
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("277d0ae5-6c41-4fe8-aa84-2152970d7c42")
    public ITable getTable() {
        return m_table;
    }

    @objid ("8a085509-83e7-4bc6-89ff-df9911a5f1af")
    public void free() {
        if(m_init_analog_input){
            m_analog_input.free();
            m_analog_input = null;
            m_init_analog_input = false;
        }
    }

    /**
     * Analog Channels don't have to do anything special when entering the LiveWindow.
     * {@inheritDoc}
     */
    @objid ("015ade6d-4eea-4bbc-99af-4502e1c130cd")
    public void startLiveWindowMode() {
    }

    /**
     * Analog Channels don't have to do anything special when exiting the LiveWindow.
     * {@inheritDoc}
     */
    @objid ("75b2ff0d-d055-40dc-b63f-180c3c1b47f8")
    public void stopLiveWindowMode() {
    }

}
