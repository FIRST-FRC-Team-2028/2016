/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.AnalogTriggerOutput.AnalogTriggerType;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.hal.CounterJNI;
import edu.wpi.first.wpilibj.hal.HALUtil;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.util.BoundaryException;

/**
 * Class for counting the number of ticks on a digital input channel. This is a
 * general purpose class for counting repetitive events. It can return the
 * number of counts, the period of the most recent cycle, and detect when the
 * signal being counted has stopped by supplying a maximum cycle time.
 * 
 * All counters will immediately start counting - reset() them if you need them
 * to be zeroed before use.
 */
@objid ("720f49fc-1b38-4b4b-acab-f9fe5a0603f3")
public class Counter extends SensorBase implements CounterBase, LiveWindowSendable, PIDSource {
    @objid ("a274f64f-915d-479a-9d80-4b20413acc30")
    private boolean m_allocatedUpSource;

    @objid ("b434d1ca-cb7f-4482-89eb-b5dab54cae80")
    private boolean m_allocatedDownSource;

    @objid ("12a24128-47a4-47ae-bbfb-b3a94f02e557")
    private ByteBuffer m_counter; // /< The FPGA counter object.

    @objid ("20d24481-73e4-465a-901e-a5477925ed5d")
    private int m_index; // /< The index of this counter.

    @objid ("1458d450-fa19-4e0e-b847-85fbd99b7206")
    private double m_distancePerPulse; // distance of travel for each tick

    @objid ("5f81ba13-e7f7-42ae-a773-b8ebd20b2291")
    private ITable m_table;

    @objid ("04da041c-a855-4b6d-9939-9ff0eea4a957")
    private DigitalSource m_upSource; // /< What makes the counter count up.

    @objid ("71e227c6-9928-4ccd-91e8-41c36317d0e7")
    private DigitalSource m_downSource; // /< What makes the counter count down.

    @objid ("5b6ff8d8-58bb-471d-ab55-8dedec2ef8d3")
    private PIDSourceParameter m_pidSource;

    @objid ("60b5b4b4-f147-4d8c-9f9a-fb9d3a1d7567")
    private void initCounter(final Mode mode) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer index = ByteBuffer.allocateDirect(4);
        // set the byte order
        index.order(ByteOrder.LITTLE_ENDIAN);
        m_counter = CounterJNI.initializeCounter(mode.value, index.asIntBuffer(), status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        m_index = index.asIntBuffer().get(0);
        
        m_allocatedUpSource = false;
        m_allocatedDownSource = false;
        m_upSource = null;
        m_downSource = null;
        
        setMaxPeriod(.5);
        
        UsageReporting.report(tResourceType.kResourceType_Counter, m_index,
                mode.value);
    }

    /**
     * Create an instance of a counter where no sources are selected. Then they
     * all must be selected by calling functions to specify the upsource and the
     * downsource independently.
     * 
     * The counter will start counting immediately.
     */
    @objid ("fa0f7938-a4dc-491f-b960-9a456e24da36")
    public Counter() {
        initCounter(Mode.kTwoPulse);
    }

    /**
     * Create an instance of a counter from a Digital Input. This is used if an
     * existing digital input is to be shared by multiple other objects such as
     * encoders or if the Digital Source is not a DIO channel (such as an Analog Trigger)
     * 
     * The counter will start counting immediately.
     * @param source the digital source to count
     */
    @objid ("7979bb5e-1207-450e-a7fe-6f4f7021e7b1")
    public Counter(DigitalSource source) {
        if (source == null)
            throw new NullPointerException("Digital Source given was null");
        initCounter(Mode.kTwoPulse);
        setUpSource(source);
    }

    /**
     * Create an instance of a Counter object. Create an up-Counter instance
     * given a channel.
     * 
     * The counter will start counting immediately.
     * @param channel the DIO channel to use as the up source. 0-9 are on-board, 10-25 are on the MXP
     */
    @objid ("99cd0a6f-b4e9-4a7c-b310-6c4d415bb994")
    public Counter(int channel) {
        initCounter(Mode.kTwoPulse);
        setUpSource(channel);
    }

    /**
     * Create an instance of a Counter object. Create an instance of a simple
     * up-Counter given an analog trigger. Use the trigger state output from the
     * analog trigger.
     * 
     * The counter will start counting immediately.
     * @param encodingType which edges to count
     * @param upSource first source to count
     * @param downSource second source for direction
     * @param inverted true to invert the count
     */
    @objid ("5f5296f4-43e4-4034-b776-7b7fb9b806a7")
    public Counter(EncodingType encodingType, DigitalSource upSource, DigitalSource downSource, boolean inverted) {
        initCounter(Mode.kExternalDirection);
        if (encodingType != EncodingType.k1X
                && encodingType != EncodingType.k2X) {
            throw new RuntimeException(
                    "Counters only support 1X and 2X quadreature decoding!");
        }
        if (upSource == null)
            throw new NullPointerException("Up Source given was null");
        setUpSource(upSource);
        if (downSource == null)
            throw new NullPointerException("Down Source given was null");
        setDownSource(downSource);
        
        if (encodingType == null)
            throw new NullPointerException("Encoding type given was null");
        
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        if (encodingType == EncodingType.k1X) {
            setUpSourceEdge(true, false);
            CounterJNI.setCounterAverageSize(m_counter, 1, status.asIntBuffer());
        } else {
            setUpSourceEdge(true, true);
            CounterJNI.setCounterAverageSize(m_counter, 2, status.asIntBuffer());
        }
        
        HALUtil.checkStatus(status.asIntBuffer());
        setDownSourceEdge(inverted, true);
    }

    /**
     * Create an instance of a Counter object. Create an instance of a simple
     * up-Counter given an analog trigger. Use the trigger state output from the
     * analog trigger.
     * 
     * The counter will start counting immediately.
     * @param trigger the analog trigger to count
     */
    @objid ("f66560e6-5b92-4e44-8b1c-94b3390a89b6")
    public Counter(AnalogTrigger trigger) {
        if( trigger == null){
            throw new NullPointerException("The Analog Trigger given was null");
        }
        initCounter(Mode.kTwoPulse);
        setUpSource(trigger.createOutput(AnalogTriggerType.kState));
    }

    @objid ("3e6e0a78-bb1e-4816-9737-c47601ded809")
    @Override
    public void free() {
        setUpdateWhenEmpty(true);
        
        clearUpSource();
        clearDownSource();
        
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        CounterJNI.freeCounter(m_counter, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        
        m_upSource = null;
        m_downSource = null;
        m_counter = null;
    }

    /**
     * @return the Counter's FPGA index
     */
    @objid ("758222d9-795a-4eb9-88b5-0c6a0c44b5d6")
    public int getFPGAIndex() {
        return m_index;
    }

    /**
     * Set the upsource for the counter as a digital input channel.
     * @param channel the DIO channel to count 0-9 are on-board, 10-25 are on the MXP
     */
    @objid ("1cdc36f1-4955-4b2a-8380-8718ba38eb8d")
    public void setUpSource(int channel) {
        setUpSource(new DigitalInput(channel));
        m_allocatedUpSource = true;
    }

    /**
     * Set the source object that causes the counter to count up. Set the up
     * counting DigitalSource.
     * @param source the digital source to count
     */
    @objid ("24c0f5a0-a182-42c9-b7fb-5321eba77309")
    public void setUpSource(DigitalSource source) {
        if (m_upSource != null && m_allocatedUpSource) {
            m_upSource.free();
            m_allocatedUpSource = false;
        }
        m_upSource = source;
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        CounterJNI.setCounterUpSource(m_counter,
                source.getChannelForRouting(),
                (byte) (source.getAnalogTriggerForRouting() ? 1 : 0), status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Set the up counting source to be an analog trigger.
     * @param analogTrigger The analog trigger object that is used for the Up Source
     * @param triggerType The analog trigger output that will trigger the counter.
     */
    @objid ("6efdc192-7453-44e3-b0eb-5a351c0888ce")
    public void setUpSource(AnalogTrigger analogTrigger, AnalogTriggerType triggerType) {
        if (analogTrigger == null){
            throw new NullPointerException("Analog Trigger given was null");
        }
        if (triggerType == null){
            throw new NullPointerException("Analog Trigger Type given was null");
        }
        setUpSource(analogTrigger.createOutput(triggerType));
        m_allocatedUpSource = true;
    }

    /**
     * Set the edge sensitivity on an up counting source. Set the up source to
     * either detect rising edges or falling edges.
     * @param risingEdge true to count rising edge
     * @param fallingEdge true to count falling edge
     */
    @objid ("900759a6-d3a2-427d-9368-98785f524f3f")
    public void setUpSourceEdge(boolean risingEdge, boolean fallingEdge) {
        if (m_upSource == null)
            throw new RuntimeException(
                    "Up Source must be set before setting the edge!");
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        CounterJNI.setCounterUpSourceEdge(m_counter,
                (byte) (risingEdge ? 1 : 0), (byte) (fallingEdge ? 1 : 0),
                status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Disable the up counting source to the counter.
     */
    @objid ("c0201393-962f-4ab7-9378-7d308fd1233b")
    public void clearUpSource() {
        if (m_upSource != null && m_allocatedUpSource) {
            m_upSource.free();
            m_allocatedUpSource = false;
        }
        m_upSource = null;
        
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        CounterJNI.clearCounterUpSource(m_counter, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Set the down counting source to be a digital input channel.
     * @param channel the DIO channel to count 0-9 are on-board, 10-25 are on the MXP
     */
    @objid ("6fe9766a-c980-44d0-b367-317953d4f7a8")
    public void setDownSource(int channel) {
        setDownSource(new DigitalInput(channel));
        m_allocatedDownSource = true;
    }

    /**
     * Set the source object that causes the counter to count down. Set the down
     * counting DigitalSource.
     * @param source the digital source to count
     */
    @objid ("9269b4bb-6d54-4c6c-a1b6-42f2d539e9c6")
    public void setDownSource(DigitalSource source) {
        if(source == null){
            throw new NullPointerException("The Digital Source given was null");
        }
        
        if (m_downSource != null && m_allocatedDownSource) {
            m_downSource.free();
            m_allocatedDownSource = false;
        }
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        CounterJNI.setCounterDownSource(m_counter,
                source.getChannelForRouting(),
                (byte) (source.getAnalogTriggerForRouting() ? 1 : 0), status.asIntBuffer());
        if (status.asIntBuffer().get(0) == HALUtil.PARAMETER_OUT_OF_RANGE) {
            throw new IllegalArgumentException(
                    "Counter only supports DownSource in TwoPulse and ExternalDirection modes.");
        }
        HALUtil.checkStatus(status.asIntBuffer());
        m_downSource = source;
    }

    /**
     * Set the down counting source to be an analog trigger.
     * @param analogTrigger The analog trigger object that is used for the Down Source
     * @param triggerType The analog trigger output that will trigger the counter.
     */
    @objid ("bb1e7594-5114-46e8-a07d-ed9a7d30a966")
    public void setDownSource(AnalogTrigger analogTrigger, AnalogTriggerType triggerType) {
        if (analogTrigger == null){
            throw new NullPointerException("Analog Trigger given was null");
        }
        if (triggerType == null){
            throw new NullPointerException("Analog Trigger Type given was null");
        }
        
        setDownSource(analogTrigger.createOutput(triggerType));
        m_allocatedDownSource = true;
    }

    /**
     * Set the edge sensitivity on a down counting source. Set the down source
     * to either detect rising edges or falling edges.
     * @param risingEdge true to count the rising edge
     * @param fallingEdge true to count the falling edge
     */
    @objid ("a4b256f8-e8bd-4268-a2b6-261252fcd6a2")
    public void setDownSourceEdge(boolean risingEdge, boolean fallingEdge) {
        if (m_downSource == null)
            throw new RuntimeException(
                    " Down Source must be set before setting the edge!");
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        CounterJNI.setCounterDownSourceEdge(m_counter, (byte) (risingEdge ? 1
                : 0), (byte) (fallingEdge ? 1 : 0), status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Disable the down counting source to the counter.
     */
    @objid ("c5dbceae-4989-450b-8deb-338e2fe1253c")
    public void clearDownSource() {
        if (m_downSource != null && m_allocatedDownSource) {
            m_downSource.free();
            m_allocatedDownSource = false;
        }
        m_downSource = null;
        
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        CounterJNI.clearCounterDownSource(m_counter, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Set standard up / down counting mode on this counter. Up and down counts
     * are sourced independently from two inputs.
     */
    @objid ("7a83e89a-c1b9-416e-8ac5-fd052aaecf18")
    public void setUpDownCounterMode() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        CounterJNI.setCounterUpDownMode(m_counter, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Set external direction mode on this counter. Counts are sourced on the Up
     * counter input. The Down counter input represents the direction to count.
     */
    @objid ("f1f58ef5-1b96-44ad-8a2c-e18b74cf29e4")
    public void setExternalDirectionMode() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        CounterJNI.setCounterExternalDirectionMode(m_counter, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Set Semi-period mode on this counter. Counts up on both rising and
     * falling edges.
     * @param highSemiPeriod true to count up on both rising and falling
     */
    @objid ("d519e9e1-c501-4ddc-8825-5d89ebf0e18e")
    public void setSemiPeriodMode(boolean highSemiPeriod) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        CounterJNI.setCounterSemiPeriodMode(m_counter,
                (byte) (highSemiPeriod ? 1 : 0), status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Configure the counter to count in up or down based on the length of the
     * input pulse. This mode is most useful for direction sensitive gear tooth
     * sensors.
     * @param threshold The pulse length beyond which the counter counts the opposite
     * direction. Units are seconds.
     */
    @objid ("d26870f6-b2ea-49b3-a848-0bfab77c583a")
    public void setPulseLengthMode(double threshold) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        CounterJNI.setCounterPulseLengthMode(m_counter, threshold,
                status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Read the current counter value. Read the value at this instant. It may
     * still be running, so it reflects the current value. Next time it is read,
     * it might have a different value.
     */
    @objid ("691a4021-a4de-429f-9052-e43952879eb3")
    @Override
    public int get() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        int value = CounterJNI.getCounter(m_counter, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

    /**
     * Read the current scaled counter value. Read the value at this instant,
     * scaled by the distance per pulse (defaults to 1).
     * @return The distance since the last reset
     */
    @objid ("3e9403aa-1aa7-4753-b4d6-c97b186f7b36")
    public double getDistance() {
        return get() * m_distancePerPulse;
    }

    /**
     * Reset the Counter to zero. Set the counter value to zero. This doesn't
     * effect the running state of the counter, just sets the current value to
     * zero.
     */
    @objid ("75fbc83a-2e63-482f-86a1-05fc3c7dcd1b")
    @Override
    public void reset() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        CounterJNI.resetCounter(m_counter, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Set the maximum period where the device is still considered "moving".
     * Sets the maximum period where the device is considered moving. This value
     * is used to determine the "stopped" state of the counter using the
     * GetStopped method.
     * @param maxPeriod The maximum period where the counted device is considered
     * moving in seconds.
     */
    @objid ("e3abfdac-ce60-433a-98a9-f7ab476ed6a2")
    @Override
    public void setMaxPeriod(double maxPeriod) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        CounterJNI.setCounterMaxPeriod(m_counter, maxPeriod, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Select whether you want to continue updating the event timer output when
     * there are no samples captured. The output of the event timer has a buffer
     * of periods that are averaged and posted to a register on the FPGA. When
     * the timer detects that the event source has stopped (based on the
     * MaxPeriod) the buffer of samples to be averaged is emptied. If you enable
     * the update when empty, you will be notified of the stopped source and the
     * event time will report 0 samples. If you disable update when empty, the
     * most recent average will remain on the output until a new sample is
     * acquired. You will never see 0 samples output (except when there have
     * been no events since an FPGA reset) and you will likely not see the
     * stopped bit become true (since it is updated at the end of an average and
     * there are no samples to average).
     * @param enabled true to continue updating
     */
    @objid ("73ac0581-00e1-4af6-89c0-577a72623770")
    public void setUpdateWhenEmpty(boolean enabled) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        CounterJNI.setCounterUpdateWhenEmpty(m_counter,
                (byte) (enabled ? 1 : 0), status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Determine if the clock is stopped. Determine if the clocked input is
     * stopped based on the MaxPeriod value set using the SetMaxPeriod method.
     * If the clock exceeds the MaxPeriod, then the device (and counter) are
     * assumed to be stopped and it returns true.
     * @return Returns true if the most recent counter period exceeds the
     * MaxPeriod value set by SetMaxPeriod.
     */
    @objid ("b195ceb6-d249-462f-b624-fdf61b8a59c1")
    @Override
    public boolean getStopped() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        boolean value = CounterJNI.getCounterStopped(m_counter, status.asIntBuffer()) != 0;
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

    /**
     * The last direction the counter value changed.
     * @return The last direction the counter value changed.
     */
    @objid ("80f64582-2657-462c-9b61-0a12ebba2153")
    @Override
    public boolean getDirection() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        boolean value = CounterJNI.getCounterDirection(m_counter, status.asIntBuffer()) != 0;
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

    /**
     * Set the Counter to return reversed sensing on the direction. This allows
     * counters to change the direction they are counting in the case of 1X and
     * 2X quadrature encoding only. Any other counter mode isn't supported.
     * @param reverseDirection true if the value counted should be negated.
     */
    @objid ("50b03806-dc9c-4e3d-835f-d86b9de28acf")
    public void setReverseDirection(boolean reverseDirection) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        CounterJNI.setCounterReverseDirection(m_counter,
                (byte) (reverseDirection ? 1 : 0), status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Get the Period of the most recent count. Returns the time interval of the
     * most recent count. This can be used for velocity calculations to
     * determine shaft speed.
     * @return The period of the last two pulses in units of seconds.
     */
    @objid ("ee0596ce-0769-4c3c-9eae-19869affb4af")
    @Override
    public double getPeriod() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        double value = CounterJNI.getCounterPeriod(m_counter, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

    /**
     * Get the current rate of the Counter. Read the current rate of the counter
     * accounting for the distance per pulse value. The default value for
     * distance per pulse (1) yields units of pulses per second.
     * @return The rate in units/sec
     */
    @objid ("c638122b-770a-4fb1-86b0-0f99088c3030")
    public double getRate() {
        return m_distancePerPulse / getPeriod();
    }

    /**
     * Set the Samples to Average which specifies the number of samples of the
     * timer to average when calculating the period. Perform averaging to
     * account for mechanical imperfections or as oversampling to increase
     * resolution.
     * @param samplesToAverage The number of samples to average from 1 to 127.
     */
    @objid ("b7a4364b-6cd7-4a7e-804b-d367536b477b")
    public void setSamplesToAverage(int samplesToAverage) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        CounterJNI.setCounterSamplesToAverage(m_counter, samplesToAverage,
                status.asIntBuffer());
        if (status.asIntBuffer().get(0) == HALUtil.PARAMETER_OUT_OF_RANGE) {
            throw new BoundaryException(BoundaryException.getMessage(
                    samplesToAverage, 1, 127));
        }
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Get the Samples to Average which specifies the number of samples of the
     * timer to average when calculating the period. Perform averaging to
     * account for mechanical imperfections or as oversampling to increase
     * resolution.
     * @return SamplesToAverage The number of samples being averaged (from 1 to
     * 127)
     */
    @objid ("2757d266-c3fa-4737-845b-adb7f9bc1059")
    public int getSamplesToAverage() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        int value = CounterJNI.getCounterSamplesToAverage(m_counter, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

    /**
     * Set the distance per pulse for this counter. This sets the multiplier
     * used to determine the distance driven based on the count value from the
     * encoder. Set this value based on the Pulses per Revolution and factor in
     * any gearing reductions. This distance can be in any units you like,
     * linear or angular.
     * @param distancePerPulse The scale factor that will be used to convert pulses to useful
     * units.
     */
    @objid ("d11e38c1-cf4d-42b0-9d7c-c0caae28fd28")
    public void setDistancePerPulse(double distancePerPulse) {
        m_distancePerPulse = distancePerPulse;
    }

    /**
     * Set which parameter of the encoder you are using as a process control
     * variable. The counter class supports the rate and distance parameters.
     * @param pidSource An enum to select the parameter.
     */
    @objid ("0d21268f-234d-40df-b22a-4322758cb66f")
    public void setPIDSourceParameter(PIDSourceParameter pidSource) {
        if(pidSource == null){
            throw new NullPointerException("PID Source Parameter given was null");
        }
        BoundaryException.assertWithinBounds(pidSource.value, 0, 1);
        m_pidSource = pidSource;
    }

    /**
     * Get the result to use in PIDController
     * @return the result to use in PIDController
     */
    @objid ("08df9ca8-01e9-4e19-9822-fba833fccbfc")
    @Override
    public double pidGet() {
        switch (m_pidSource.value) {
        case PIDSourceParameter.kDistance_val:
            return getDistance();
        case PIDSourceParameter.kRate_val:
            return getRate();
        default:
            return 0.0;
        }
    }

    /**
     * Live Window code, only does anything if live window is activated.
     */
    @objid ("324ce78b-6d49-45a6-8857-a49d64093ae1")
    @Override
    public String getSmartDashboardType() {
        return "Counter";
    }

    /**
     * {@inheritDoc}
     */
    @objid ("f6c69141-d8aa-42cb-927b-64a80b93bcb8")
    @Override
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @objid ("8fac4d14-bd17-49ec-958d-46ca561a19f7")
    @Override
    public ITable getTable() {
        return m_table;
    }

    /**
     * {@inheritDoc}
     */
    @objid ("a50a7e7d-393e-4a5a-b599-49cc7388d206")
    @Override
    public void updateTable() {
        if (m_table != null) {
            m_table.putNumber("Value", get());
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("f3a89b20-e3c0-49e8-8dac-f39dc48ecf84")
    @Override
    public void startLiveWindowMode() {
    }

    /**
     * {@inheritDoc}
     */
    @objid ("f98248bd-29b4-43ae-bbb2-95600032d910")
    @Override
    public void stopLiveWindowMode() {
    }

    /**
     * Mode determines how and what the counter counts
     */
    @objid ("1a1a5548-3704-47e2-8636-6797983635fa")
    public enum Mode {
        /**
         * mode: two pulse
         */
        kTwoPulse (0),
        /**
         * mode: semi period
         */
        kSemiperiod (1),
        /**
         * mode: pulse length
         */
        kPulseLength (2),
        /**
         * mode: external direction
         */
        kExternalDirection (3);

        /**
         * The integer value representing this enumeration
         */
        @objid ("6cc32d37-408f-47ac-bd09-59169815c673")
        public final int value;

        @objid ("378577a0-42d4-4a42-ac77-1f0b3a1fd6a9")
        private Mode(int value) {
            this.value = value;
        }

    }

}
