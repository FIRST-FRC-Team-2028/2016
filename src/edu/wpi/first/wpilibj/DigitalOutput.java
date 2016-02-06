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
import edu.wpi.first.wpilibj.hal.PWMJNI;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

/**
 * Class to write digital outputs. This class will write digital outputs. Other
 * devices that are implemented elsewhere will automatically allocate digital
 * inputs and outputs as required.
 */
@objid ("bc61d1b0-a677-4b1e-b875-72a5770597f2")
public class DigitalOutput extends DigitalSource implements LiveWindowSendable {
    @objid ("8eb2854d-6612-426f-8b23-24d442a85853")
    private ByteBuffer m_pwmGenerator;

    @objid ("87685f85-aa05-46c1-99c3-255495e0d2da")
    private ITable m_table;

    @objid ("780d326f-534f-4045-936f-d755a602368a")
    private ITableListener m_table_listener;

    /**
     * Create an instance of a digital output. Create an instance of a digital
     * output given a channel.
     * @param channel the DIO channel to use for the digital output. 0-9 are on-board, 10-25 are on the MXP
     */
    @objid ("ef8dfc2b-8a95-44ea-8276-c041f611f9f8")
    public DigitalOutput(int channel) {
        initDigitalPort(channel, false);
        
        UsageReporting.report(tResourceType.kResourceType_DigitalOutput, channel);
    }

    /**
     * Free the resources associated with a digital output.
     */
    @objid ("61473bb9-09f2-419e-be63-90093da9f0a5")
    @Override
    public void free() {
        // disable the pwm only if we have allocated it
        if (m_pwmGenerator != null) {
            disablePWM();
        }
        super.free();
    }

    /**
     * Set the value of a digital output.
     * @param value true is on, off is false
     */
    @objid ("4e557820-c352-4e45-b3b0-db2e5a5d89a7")
    public void set(boolean value) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        DIOJNI.setDIO(m_port, (short) (value ? 1 : 0), status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * @return The GPIO channel number that this object represents.
     */
    @objid ("2692f499-9ca8-43b8-989b-e3ea3c502f09")
    public int getChannel() {
        return m_channel;
    }

    /**
     * Generate a single pulse. Write a pulse to the specified digital output
     * channel. There can only be a single pulse going at any time.
     * @param channel The channel to pulse.
     * @param pulseLength The length of the pulse.
     */
    @objid ("aa0b2564-20fb-45e8-a93e-6936c8018840")
    public void pulse(final int channel, final float pulseLength) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        DIOJNI.pulse(m_port, pulseLength, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * @deprecated Generate a single pulse. Write a pulse to the specified
     * digital output channel. There can only be a single pulse
     * going at any time.
     * @param channel The channel to pulse.
     * @param pulseLength The length of the pulse.
     */
    @objid ("fc602c39-c0de-494c-bae9-eaa8a87396a0")
    @Deprecated
    public void pulse(final int channel, final int pulseLength) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        float convertedPulse = (float) (pulseLength / 1.0e9 * (DIOJNI.getLoopTiming(status.asIntBuffer()) * 25));
        System.err
                .println("You should use the float version of pulse for portability.  This is deprecated");
        DIOJNI.pulse(m_port, convertedPulse, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Determine if the pulse is still going. Determine if a previously started
     * pulse is still going.
     * @return true if pulsing
     */
    @objid ("b60f9165-7a09-41cd-8849-bb7a97f8b6cd")
    public boolean isPulsing() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        boolean value = DIOJNI.isPulsing(m_port, status.asIntBuffer()) != 0;
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

    /**
     * Change the PWM frequency of the PWM output on a Digital Output line.
     * 
     * The valid range is from 0.6 Hz to 19 kHz. The frequency resolution is
     * logarithmic.
     * 
     * There is only one PWM frequency for all channnels.
     * @param rate The frequency to output all digital output PWM signals.
     */
    @objid ("80d08591-6023-437d-be21-18d4a9796b4d")
    public void setPWMRate(double rate) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        PWMJNI.setPWMRate(rate, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Enable a PWM Output on this line.
     * 
     * Allocate one of the 6 DO PWM generator resources.
     * 
     * Supply the initial duty-cycle to output so as to avoid a glitch when
     * first starting.
     * 
     * The resolution of the duty cycle is 8-bit for low frequencies (1kHz or
     * less) but is reduced the higher the frequency of the PWM signal is.
     * @param initialDutyCycle The duty-cycle to start generating. [0..1]
     */
    @objid ("d7f81323-4fdc-4faf-827c-a49d25d670b7")
    public void enablePWM(double initialDutyCycle) {
        if (m_pwmGenerator != null)
            return;
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        m_pwmGenerator = PWMJNI.allocatePWM(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        PWMJNI.setPWMDutyCycle(m_pwmGenerator, initialDutyCycle,
            status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        PWMJNI.setPWMOutputChannel(m_pwmGenerator, m_channel, status.asIntBuffer());
    }

    /**
     * Change this line from a PWM output back to a static Digital Output line.
     * 
     * Free up one of the 6 DO PWM generator resources that were in use.
     */
    @objid ("6c609cad-ddfd-4f30-a32c-ebe5cede052b")
    public void disablePWM() {
        if (m_pwmGenerator == null)
            return;
        // Disable the output by routing to a dead bit.
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        PWMJNI.setPWMOutputChannel(m_pwmGenerator, kDigitalChannels, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        PWMJNI.freePWM(m_pwmGenerator, status.asIntBuffer());
        m_pwmGenerator = null;
    }

    /**
     * Change the duty-cycle that is being generated on the line.
     * 
     * The resolution of the duty cycle is 8-bit for low frequencies (1kHz or
     * less) but is reduced the higher the frequency of the PWM signal is.
     * @param dutyCycle The duty-cycle to change to. [0..1]
     */
    @objid ("5436590d-d497-4061-96b1-39629a925963")
    public void updateDutyCycle(double dutyCycle) {
        if (m_pwmGenerator == null)
            return;
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        PWMJNI.setPWMDutyCycle(m_pwmGenerator, dutyCycle, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

/*
     * Live Window code, only does anything if live window is activated.
     */
    @objid ("781cd2e8-5219-4f7a-809e-994b9a3e487b")
    @Override
    public String getSmartDashboardType() {
        return "Digital Output";
    }

    /**
     * {@inheritDoc}
     */
    @objid ("9e2cb504-38b7-4412-86e9-d09af493c5e4")
    @Override
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @objid ("d78d84f1-0d1d-455d-9ce7-d4684a898ec8")
    @Override
    public ITable getTable() {
        return m_table;
    }

    /**
     * {@inheritDoc}
     */
    @objid ("a10dab9a-1ec9-4994-8215-0e5f82fdcb18")
    @Override
    public void updateTable() {
        // TODO: Put current value.
    }

    /**
     * {@inheritDoc}
     */
    @objid ("3ac13449-16be-4a00-9321-2608d078c7f6")
    @Override
    public void startLiveWindowMode() {
        m_table_listener = new ITableListener() {
            @Override
            public void valueChanged(ITable itable, String key, Object value,
                    boolean bln) {
                set(((Boolean) value).booleanValue());
            }
        };
        m_table.addTableListener("Value", m_table_listener, true);
    }

    /**
     * {@inheritDoc}
     */
    @objid ("d14eddf5-a5f3-4922-bfda-102cddd8237b")
    @Override
    public void stopLiveWindowMode() {
        // TODO: Broken, should only remove the listener from "Value" only.
        m_table.removeTableListener(m_table_listener);
    }

}
