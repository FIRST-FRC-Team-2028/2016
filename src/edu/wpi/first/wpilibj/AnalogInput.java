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
 * Analog channel class.
 * 
 * Each analog channel is read from hardware as a 12-bit number representing
 * 0V to 5V.
 * 
 * Connected to each analog channel is an averaging and oversampling engine.
 * This engine accumulates the specified ( by setAverageBits() and
 * setOversampleBits() ) number of samples before returning a new value. This is
 * not a sliding window average. The only difference between the oversampled
 * samples and the averaged samples is that the oversampled samples are simply
 * accumulated effectively increasing the resolution, while the averaged samples
 * are divided by the number of samples to retain the resolution, but get more
 * stable values.
 */
@objid ("e412622d-21c0-4a3c-ad5d-bdca8dae2e00")
public class AnalogInput extends SensorBase implements PIDSource, LiveWindowSendable {
    @objid ("880b9f22-3e51-419a-9e2a-55ca3cf0ec12")
    private static final int kAccumulatorSlot = 1;

    @objid ("2ea09209-3e7b-4622-bdea-9361a889ab0e")
    private ByteBuffer m_port;

    @objid ("b498d9f6-68cc-40f1-bc5b-dd5a40eaa57f")
    private int m_channel;

    @objid ("584340f5-a7ea-40bf-a767-a0d369061f3d")
    private static final int[] kAccumulatorChannels = { 0, 1 };

    @objid ("f91e75b1-923f-4d6e-ad35-79e7d39e691c")
    private long m_accumulatorOffset;

    @objid ("9cefe10e-c1c4-40cd-822f-3529367168dd")
    private ITable m_table;

    @objid ("461df4bd-bac0-4169-a99c-60ec6038e519")
    private static Resource channels = new Resource(kAnalogInputChannels);

    /**
     * Construct an analog channel.
     * @param channel The channel number to represent. 0-3 are on-board 4-7 are on the MXP port.
     */
    @objid ("f7d7a476-4176-479e-873c-e03607754987")
    public AnalogInput(final int channel) {
        m_channel = channel;
        
        if (AnalogJNI.checkAnalogInputChannel(channel) == 0) {
            throw new AllocationException("Analog input channel " + m_channel
                    + " cannot be allocated. Channel is not present.");
        }
        try {
            channels.allocate(channel);
        } catch (CheckedAllocationException e) {
            throw new AllocationException("Analog input channel " + m_channel
                     + " is already allocated");
        }
        
        ByteBuffer port_pointer = AnalogJNI.getPort((byte) channel);
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        m_port = AnalogJNI.initializeAnalogInputPort(port_pointer, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        
        LiveWindow.addSensor("AnalogInput", channel, this);
        UsageReporting.report(tResourceType.kResourceType_AnalogChannel, channel);
    }

    /**
     * Channel destructor.
     */
    @objid ("13a61c4a-4ec9-4db5-b1f5-69be1c2b7792")
    public void free() {
        channels.free(m_channel);
        m_channel = 0;
        m_accumulatorOffset = 0;
    }

    /**
     * Get a sample straight from this channel. The sample is a 12-bit value
     * representing the 0V to 5V range of the A/D converter. The units are in
     * A/D converter codes. Use GetVoltage() to get the analog value in
     * calibrated units.
     * @return A sample straight from this channel.
     */
    @objid ("0af19c37-7fa3-4cc8-905d-03d6c62ee46a")
    public int getValue() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        int value = AnalogJNI.getAnalogValue(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

    /**
     * Get a sample from the output of the oversample and average engine for
     * this channel. The sample is 12-bit + the bits configured in
     * SetOversampleBits(). The value configured in setAverageBits() will cause
     * this value to be averaged 2^bits number of samples. This is not a
     * sliding window. The sample will not change until 2^(OversampleBits +
     * AverageBits) samples have been acquired from this channel. Use
     * getAverageVoltage() to get the analog value in calibrated units.
     * @return A sample from the oversample and average engine for this channel.
     */
    @objid ("9762856d-3ade-45cf-bc85-bc2dd0531037")
    public int getAverageValue() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        int value = AnalogJNI.getAnalogAverageValue(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

    /**
     * Get a scaled sample straight from this channel. The value is scaled to
     * units of Volts using the calibrated scaling data from getLSBWeight() and
     * getOffset().
     * @return A scaled sample straight from this channel.
     */
    @objid ("836d02e2-b427-464b-83bf-681388a95274")
    public double getVoltage() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        double value = AnalogJNI.getAnalogVoltage(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

    /**
     * Get a scaled sample from the output of the oversample and average engine
     * for this channel. The value is scaled to units of Volts using the
     * calibrated scaling data from getLSBWeight() and getOffset(). Using
     * oversampling will cause this value to be higher resolution, but it will
     * update more slowly. Using averaging will cause this value to be more
     * stable, but it will update more slowly.
     * @return A scaled sample from the output of the oversample and average
     * engine for this channel.
     */
    @objid ("8d06f2dd-3cd7-4a6f-bcb6-1fbc8f478243")
    public double getAverageVoltage() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        double value = AnalogJNI.getAnalogAverageVoltage(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

    /**
     * Get the factory scaling least significant bit weight constant. The least
     * significant bit weight constant for the channel that was calibrated in
     * manufacturing and stored in an eeprom.
     * 
     * Volts = ((LSB_Weight * 1e-9) * raw) - (Offset * 1e-9)
     * @return Least significant bit weight.
     */
    @objid ("dcc2fa3b-7b7f-49c7-b972-7d04643f4ccd")
    public long getLSBWeight() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        long value = AnalogJNI.getAnalogLSBWeight(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

    /**
     * Get the factory scaling offset constant. The offset constant for the
     * channel that was calibrated in manufacturing and stored in an eeprom.
     * 
     * Volts = ((LSB_Weight * 1e-9) * raw) - (Offset * 1e-9)
     * @return Offset constant.
     */
    @objid ("ec10eaef-2ab5-42bd-86b9-056382d302ee")
    public int getOffset() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        int value = AnalogJNI.getAnalogOffset(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

    /**
     * Get the channel number.
     * @return The channel number.
     */
    @objid ("f521069c-cc36-483b-acce-b68d7cff694d")
    public int getChannel() {
        return m_channel;
    }

    /**
     * Set the number of averaging bits. This sets the number of averaging bits.
     * The actual number of averaged samples is 2^bits. The averaging is done
     * automatically in the FPGA.
     * @param bits The number of averaging bits.
     */
    @objid ("58e23fb9-23a5-41e3-bb6e-097232a0e055")
    public void setAverageBits(final int bits) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        AnalogJNI.setAnalogAverageBits(m_port, bits, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Get the number of averaging bits. This gets the number of averaging bits
     * from the FPGA. The actual number of averaged samples is 2^bits. The
     * averaging is done automatically in the FPGA.
     * @return The number of averaging bits.
     */
    @objid ("9ea9e7d7-3f1a-4745-ac72-bdb88e6f7dfb")
    public int getAverageBits() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        int value = AnalogJNI.getAnalogAverageBits(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

    /**
     * Set the number of oversample bits. This sets the number of oversample
     * bits. The actual number of oversampled values is 2^bits. The
     * oversampling is done automatically in the FPGA.
     * @param bits The number of oversample bits.
     */
    @objid ("133b9cc4-8135-4bf1-8382-58f133da54a6")
    public void setOversampleBits(final int bits) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        AnalogJNI.setAnalogOversampleBits(m_port, bits, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Get the number of oversample bits. This gets the number of oversample
     * bits from the FPGA. The actual number of oversampled values is 2^bits.
     * The oversampling is done automatically in the FPGA.
     * @return The number of oversample bits.
     */
    @objid ("f6571e02-1847-46df-8f9f-7c5a2f9f0cbb")
    public int getOversampleBits() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        int value = AnalogJNI.getAnalogOversampleBits(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

    /**
     * Initialize the accumulator.
     */
    @objid ("3a7f6a17-e612-4c5d-8f69-f534d473a1cf")
    public void initAccumulator() {
        if (!isAccumulatorChannel()) {
            throw new AllocationException(
                    "Accumulators are only available on slot "
                            + kAccumulatorSlot + " on channels "
                            + kAccumulatorChannels[0] + ","
                            + kAccumulatorChannels[1]);
        }
        m_accumulatorOffset = 0;
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        AnalogJNI.initAccumulator(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Set an initial value for the accumulator.
     * 
     * This will be added to all values returned to the user.
     * @param initialValue The value that the accumulator should start from when reset.
     */
    @objid ("233e3ffa-1fda-4f22-8534-a0e6ddf1fdc1")
    public void setAccumulatorInitialValue(long initialValue) {
        m_accumulatorOffset = initialValue;
    }

    /**
     * Resets the accumulator to the initial value.
     */
    @objid ("86c404be-0a14-40d6-954a-d0928ab14b45")
    public void resetAccumulator() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        AnalogJNI.resetAccumulator(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        
        // Wait until the next sample, so the next call to getAccumulator*()
        // won't have old values.
        final double sampleTime = 1.0 / getGlobalSampleRate();
        final double overSamples = 1 << getOversampleBits();
        final double averageSamples = 1 << getAverageBits();
        Timer.delay(sampleTime * overSamples * averageSamples);
    }

    /**
     * Set the center value of the accumulator.
     * 
     * The center value is subtracted from each A/D value before it is added to
     * the accumulator. This is used for the center value of devices like gyros
     * and accelerometers to take the device offset
     * into account when integrating.
     * 
     * This center value is based on the output of the oversampled and averaged
     * source the accumulator channel. Because of this, any non-zero oversample bits will
     * affect the size of the value for this field.
     */
    @objid ("540cd08b-da04-4c33-aaa4-5dd58b6b8135")
    public void setAccumulatorCenter(int center) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        AnalogJNI.setAccumulatorCenter(m_port, center, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Set the accumulator's deadband.
     * @param deadband The deadband size in ADC codes (12-bit value)
     */
    @objid ("aec17c65-9d2f-4d10-823a-c17c80d07e1e")
    public void setAccumulatorDeadband(int deadband) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        AnalogJNI.setAccumulatorDeadband(m_port, deadband, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Read the accumulated value.
     * 
     * Read the value that has been accumulating. The accumulator
     * is attached after the oversample and average engine.
     * @return The 64-bit value accumulated since the last Reset().
     */
    @objid ("a25a86da-814c-45b3-98b7-e325b8e14d68")
    public long getAccumulatorValue() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        long value = AnalogJNI.getAccumulatorValue(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return value + m_accumulatorOffset;
    }

    /**
     * Read the number of accumulated values.
     * 
     * Read the count of the accumulated values since the accumulator was last
     * Reset().
     * @return The number of times samples from the channel were accumulated.
     */
    @objid ("625813c4-aff1-4d02-90b9-7d2f76e1b0c8")
    public long getAccumulatorCount() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        long value = AnalogJNI.getAccumulatorCount(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

    /**
     * Read the accumulated value and the number of accumulated values
     * atomically.
     * 
     * This function reads the value and count from the FPGA atomically. This
     * can be used for averaging.
     * @param result AccumulatorResult object to store the results in.
     */
    @objid ("e7b3111a-b2f3-40b0-aea2-59309a3a8f65")
    public void getAccumulatorOutput(AccumulatorResult result) {
        if (result == null) {
            throw new IllegalArgumentException("Null parameter `result'");
        }
        if (!isAccumulatorChannel()) {
            throw new IllegalArgumentException("Channel " + m_channel
                    + " is not an accumulator channel.");
        }
        ByteBuffer value = ByteBuffer.allocateDirect(8);
        // set the byte order
        value.order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer count = ByteBuffer.allocateDirect(4);
        // set the byte order
        count.order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        AnalogJNI.getAccumulatorOutput(m_port, value.asLongBuffer(), count.asIntBuffer(), status.asIntBuffer());
        result.value = value.asLongBuffer().get(0) + m_accumulatorOffset;
        result.count = count.asIntBuffer().get(0);
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Is the channel attached to an accumulator.
     * @return The analog channel is attached to an accumulator.
     */
    @objid ("0956c744-e5d5-47e4-9ec1-a1ab5d983df1")
    public boolean isAccumulatorChannel() {
        for (int i = 0; i < kAccumulatorChannels.length; i++) {
            if (m_channel == kAccumulatorChannels[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Set the sample rate per channel.
     * 
     * This is a global setting for all channels.
     * The maximum rate is 500kS/s divided by the number of channels in use.
     * This is 62500 samples/s per channel if all 8 channels are used.
     * @param samplesPerSecond The number of samples per second.
     */
    @objid ("222d72a1-2a4c-4c5f-a42e-0ba64510ad5c")
    public static void setGlobalSampleRate(final double samplesPerSecond) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        AnalogJNI.setAnalogSampleRate((float)samplesPerSecond, status.asIntBuffer());
        
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Get the current sample rate.
     * 
     * This assumes one entry in the scan list. This is a global setting for
     * all channels.
     * @return Sample rate.
     */
    @objid ("c2d005d0-65dc-42e1-9cb4-eb190a96cf77")
    public static double getGlobalSampleRate() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        double value = AnalogJNI.getAnalogSampleRate(status.asIntBuffer());
        
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

    /**
     * Get the result to use in PIDController
     * @return the result to use in PIDController
     */
    @objid ("f243c876-eb35-4529-869e-0ff99ca5fce9")
    public double pidGet() {
        return getAverageVoltage();
    }

    /**
     * Live Window code, only does anything if live window is activated.
     */
    @objid ("bfa6e49c-a98c-49e7-b7e5-13eb8725a23a")
    public String getSmartDashboardType() {
        return "Analog Input";
    }

    /**
     * {@inheritDoc}
     */
    @objid ("f368acbf-52ee-4dfa-b32e-f87c0c6807fb")
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @objid ("a7105f46-9f80-44e1-9954-357102b0ee00")
    public void updateTable() {
        if (m_table != null) {
            m_table.putNumber("Value", getAverageVoltage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("13f75031-bdfa-4ca3-b58c-3dd5cc4640fc")
    public ITable getTable() {
        return m_table;
    }

    /**
     * Analog Channels don't have to do anything special when entering the
     * LiveWindow. {@inheritDoc}
     */
    @objid ("94607a3b-70dd-45a0-abec-a134a12317de")
    public void startLiveWindowMode() {
    }

    /**
     * Analog Channels don't have to do anything special when exiting the
     * LiveWindow. {@inheritDoc}
     */
    @objid ("b5a862ad-8c33-4c7d-9c8f-2e4a147a2c2d")
    public void stopLiveWindowMode() {
    }

}
