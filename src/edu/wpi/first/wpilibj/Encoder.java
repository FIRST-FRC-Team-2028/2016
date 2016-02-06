/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tInstances;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.hal.EncoderJNI;
import edu.wpi.first.wpilibj.hal.HALUtil;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.util.BoundaryException;

/**
 * Class to read quad encoders. Quadrature encoders are devices that count shaft
 * rotation and can sense direction. The output of the QuadEncoder class is an
 * integer that can count either up or down, and can go negative for reverse
 * direction counting. When creating QuadEncoders, a direction is supplied that
 * changes the sense of the output to make code more readable if the encoder is
 * mounted such that forward movement generates negative values. Quadrature
 * encoders have two digital outputs, an A Channel and a B Channel that are out
 * of phase with each other to allow the FPGA to do direction sensing.
 * 
 * All encoders will immediately start counting - reset() them if you need them
 * to be zeroed before use.
 */
@objid ("ea5088e8-52bf-4abe-811f-3e9c23db39e4")
public class Encoder extends SensorBase implements CounterBase, PIDSource, LiveWindowSendable {
    @objid ("1de3651f-2fb8-4907-a96c-9f324e467071")
    private ByteBuffer m_encoder;

    @objid ("dda17658-f0dd-4b5b-930a-d67bdc842218")
    private int m_index;

    @objid ("556128f4-4bc6-4fe7-a7c9-f0dd3894060b")
    private double m_distancePerPulse; // distance of travel for each encoder

    @objid ("50d97834-c323-45f6-afdb-b075d3b264ce")
    private int m_encodingScale; // 1x, 2x, or 4x, per the encodingType

    @objid ("64903512-b49c-4316-a9e2-5be1689a2ebe")
    private boolean m_allocatedA;

    @objid ("a6721251-5271-4d8a-92fb-b772768e8a60")
    private boolean m_allocatedB;

    @objid ("8b01bf05-82bc-45e3-8d93-93dcbc08617e")
    private boolean m_allocatedI;

    @objid ("8b6dc405-eda4-483a-8b0d-04de9e6e4ed3")
    private ITable m_table;

    /**
     * The a source
     */
    @objid ("cf707eb2-353f-4263-b941-f2b3e20f5b37")
    protected DigitalSource m_aSource; // the A phase of the quad encoder

    /**
     * The b source
     */
    @objid ("0a984bd7-4108-4673-b4c5-89b8fa1d6508")
    protected DigitalSource m_bSource; // the B phase of the quad encoder

    /**
     * The index source
     */
    @objid ("c6bee418-66bf-4fa7-81a0-9e5ace5158e8")
    protected DigitalSource m_indexSource = null; // Index on some encoders

// tick
    @objid ("12fee5e3-f69b-42c1-bd2f-353f490f9181")
    private Counter m_counter; // Counter object for 1x and 2x encoding

    @objid ("601c364f-bf4c-4324-8ef8-64796dc4779b")
    private EncodingType m_encodingType = EncodingType.k4X;

    @objid ("5343b697-956b-453d-8bcd-feb800078691")
    private PIDSourceParameter m_pidSource;

    /**
     * Common initialization code for Encoders. This code allocates resources
     * for Encoders and is common to all constructors.
     * 
     * The encoder will start counting immediately.
     * @param encodingType
     * either k1X, k2X, or k4X to indicate 1X, 2X or 4X decoding. If
     * 4X is selected, then an encoder FPGA object is used and the
     * returned counts will be 4x the encoder spec'd value since all
     * rising and falling edges are counted. If 1X or 2X are selected
     * then a counter object will be used and the returned value will
     * either exactly match the spec'd count or be double (2x) the
     * spec'd count.
     * @param reverseDirection If true, counts down instead of up (this is all relative)
     */
    @objid ("3b538c99-bb21-481f-9263-3025bdd9cf51")
    private void initEncoder(boolean reverseDirection) {
        switch (m_encodingType.value) {
        case EncodingType.k4X_val:
            m_encodingScale = 4;
            ByteBuffer status = ByteBuffer.allocateDirect(4);
            // set the byte order
            status.order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer index = ByteBuffer.allocateDirect(4);
            // set the byte order
            index.order(ByteOrder.LITTLE_ENDIAN);
            m_encoder = EncoderJNI.initializeEncoder(
                    (byte) m_aSource.getModuleForRouting(),
                    m_aSource.getChannelForRouting(),
                    (byte) (m_aSource.getAnalogTriggerForRouting() ? 1 : 0),
                    (byte) m_bSource.getModuleForRouting(),
                    m_bSource.getChannelForRouting(),
                    (byte) (m_bSource.getAnalogTriggerForRouting() ? 1 : 0),
                    (byte) (reverseDirection ? 1 : 0), index.asIntBuffer(), status.asIntBuffer());
            HALUtil.checkStatus(status.asIntBuffer());
            m_index = index.asIntBuffer().get(0);
            m_counter = null;
            setMaxPeriod(.5);
            break;
        case EncodingType.k2X_val:
        case EncodingType.k1X_val:
            m_encodingScale = m_encodingType == EncodingType.k1X ? 1 : 2;
            m_counter = new Counter(m_encodingType, m_aSource, m_bSource,
                    reverseDirection);
            m_index = m_counter.getFPGAIndex();
            break;
        }
        m_distancePerPulse = 1.0;
        m_pidSource = PIDSourceParameter.kDistance;
        
        UsageReporting.report(tResourceType.kResourceType_Encoder,
                m_index, m_encodingType.value);
        LiveWindow.addSensor("Encoder", m_aSource.getChannelForRouting(), this);
    }

    /**
     * Encoder constructor. Construct a Encoder given a and b channels.
     * 
     * The encoder will start counting immediately.
     * @param aChannel The a channel DIO channel. 0-9 are on-board, 10-25 are on the MXP port
     * @param bChannel The b channel DIO channel. 0-9 are on-board, 10-25 are on the MXP port
     * @param reverseDirection represents the orientation of the encoder and inverts the
     * output values if necessary so forward represents positive
     * values.
     */
    @objid ("e4474362-2a15-4c50-b921-87289b5028d6")
    public Encoder(final int aChannel, final int bChannel, boolean reverseDirection) {
        m_allocatedA = true;
        m_allocatedB = true;
        m_allocatedI = false;
        m_aSource = new DigitalInput(aChannel);
        m_bSource = new DigitalInput(bChannel);
        initEncoder(reverseDirection);
    }

    /**
     * Encoder constructor. Construct a Encoder given a and b channels.
     * 
     * The encoder will start counting immediately.
     * @param aChannel The a channel digital input channel.
     * @param bChannel The b channel digital input channel.
     */
    @objid ("31b1b117-9a7b-4df5-aa59-a8f061e030fd")
    public Encoder(final int aChannel, final int bChannel) {
        this(aChannel, bChannel, false);
    }

    /**
     * Encoder constructor. Construct a Encoder given a and b channels.
     * 
     * The encoder will start counting immediately.
     * @param aChannel The a channel digital input channel.
     * @param bChannel The b channel digital input channel.
     * @param reverseDirection represents the orientation of the encoder and inverts the
     * output values if necessary so forward represents positive
     * values.
     * @param encodingType either k1X, k2X, or k4X to indicate 1X, 2X or 4X decoding. If
     * 4X is selected, then an encoder FPGA object is used and the
     * returned counts will be 4x the encoder spec'd value since all
     * rising and falling edges are counted. If 1X or 2X are selected
     * then a counter object will be used and the returned value will
     * either exactly match the spec'd count or be double (2x) the
     * spec'd count.
     */
    @objid ("9c0af49c-8412-4558-a66b-7c61ee0cfe22")
    public Encoder(final int aChannel, final int bChannel, boolean reverseDirection, final EncodingType encodingType) {
        m_allocatedA = true;
        m_allocatedB = true;
        m_allocatedI = false;
        if (encodingType == null)
            throw new NullPointerException("Given encoding type was null");
        m_encodingType = encodingType;
        m_aSource = new DigitalInput(aChannel);
        m_bSource = new DigitalInput(bChannel);
        initEncoder(reverseDirection);
    }

    /**
     * Encoder constructor. Construct a Encoder given a and b channels.
     * Using an index pulse forces 4x encoding
     * 
     * The encoder will start counting immediately.
     * @param aChannel The a channel digital input channel.
     * @param bChannel The b channel digital input channel.
     * @param indexChannel The index channel digital input channel.
     * @param reverseDirection represents the orientation of the encoder and inverts the
     * output values if necessary so forward represents positive
     * values.
     */
    @objid ("01185640-d214-464e-8814-33f704e68ca8")
    public Encoder(final int aChannel, final int bChannel, final int indexChannel, boolean reverseDirection) {
        m_allocatedA = true;
        m_allocatedB = true;
        m_allocatedI = true;
        m_aSource = new DigitalInput(aChannel);
        m_bSource = new DigitalInput(bChannel);
        m_indexSource = new DigitalInput(indexChannel);
        initEncoder(reverseDirection);
        setIndexSource(indexChannel);
    }

    /**
     * Encoder constructor. Construct a Encoder given a and b channels.
     * Using an index pulse forces 4x encoding
     * 
     * The encoder will start counting immediately.
     * @param aChannel The a channel digital input channel.
     * @param bChannel The b channel digital input channel.
     * @param indexChannel The index channel digital input channel.
     */
    @objid ("286aef54-37d4-4f2d-825e-e23102752b6b")
    public Encoder(final int aChannel, final int bChannel, final int indexChannel) {
        this(aChannel, bChannel, indexChannel, false);
    }

    /**
     * Encoder constructor. Construct a Encoder given a and b channels as
     * digital inputs. This is used in the case where the digital inputs are
     * shared. The Encoder class will not allocate the digital inputs and assume
     * that they already are counted.
     * 
     * The encoder will start counting immediately.
     * @param aSource The source that should be used for the a channel.
     * @param bSource the source that should be used for the b channel.
     * @param reverseDirection represents the orientation of the encoder and inverts the
     * output values if necessary so forward represents positive
     * values.
     */
    @objid ("514e16f9-eada-4f85-98bc-b3e9e5ed0fc9")
    public Encoder(DigitalSource aSource, DigitalSource bSource, boolean reverseDirection) {
        m_allocatedA = false;
        m_allocatedB = false;
        m_allocatedI = false;
        if (aSource == null)
            throw new NullPointerException("Digital Source A was null");
        m_aSource = aSource;
        if (bSource == null)
            throw new NullPointerException("Digital Source B was null");
        m_bSource = bSource;
        initEncoder(reverseDirection);
    }

    /**
     * Encoder constructor. Construct a Encoder given a and b channels as
     * digital inputs. This is used in the case where the digital inputs are
     * shared. The Encoder class will not allocate the digital inputs and assume
     * that they already are counted.
     * 
     * The encoder will start counting immediately.
     * @param aSource The source that should be used for the a channel.
     * @param bSource the source that should be used for the b channel.
     */
    @objid ("06261de2-2a16-4f0d-ba6c-ac6a274b57b3")
    public Encoder(DigitalSource aSource, DigitalSource bSource) {
        this(aSource, bSource, false);
    }

    /**
     * Encoder constructor. Construct a Encoder given a and b channels as
     * digital inputs. This is used in the case where the digital inputs are
     * shared. The Encoder class will not allocate the digital inputs and assume
     * that they already are counted.
     * 
     * The encoder will start counting immediately.
     * @param aSource The source that should be used for the a channel.
     * @param bSource the source that should be used for the b channel.
     * @param reverseDirection represents the orientation of the encoder and inverts the
     * output values if necessary so forward represents positive
     * values.
     * @param encodingType either k1X, k2X, or k4X to indicate 1X, 2X or 4X decoding. If
     * 4X is selected, then an encoder FPGA object is used and the
     * returned counts will be 4x the encoder spec'd value since all
     * rising and falling edges are counted. If 1X or 2X are selected
     * then a counter object will be used and the returned value will
     * either exactly match the spec'd count or be double (2x) the
     * spec'd count.
     */
    @objid ("9ba1197e-8800-4780-ae5e-d2fd823fdd83")
    public Encoder(DigitalSource aSource, DigitalSource bSource, boolean reverseDirection, final EncodingType encodingType) {
        m_allocatedA = false;
        m_allocatedB = false;
        m_allocatedI = false;
        if (encodingType == null)
            throw new NullPointerException("Given encoding type was null");
        m_encodingType = encodingType;
        if (aSource == null)
            throw new NullPointerException("Digital Source A was null");
        m_aSource = aSource;
        if (bSource == null)
            throw new NullPointerException("Digital Source B was null");
        m_aSource = aSource;
        m_bSource = bSource;
        initEncoder(reverseDirection);
    }

    /**
     * Encoder constructor. Construct a Encoder given a, b and index channels as
     * digital inputs. This is used in the case where the digital inputs are
     * shared. The Encoder class will not allocate the digital inputs and assume
     * that they already are counted.
     * 
     * The encoder will start counting immediately.
     * @param aSource The source that should be used for the a channel.
     * @param bSource the source that should be used for the b channel.
     * @param indexSource the source that should be used for the index channel.
     * @param reverseDirection represents the orientation of the encoder and inverts the
     * output values if necessary so forward represents positive
     * values.
     */
    @objid ("54eb54ff-78e1-418f-90ec-f8960fd2326b")
    public Encoder(DigitalSource aSource, DigitalSource bSource, DigitalSource indexSource, boolean reverseDirection) {
        m_allocatedA = false;
        m_allocatedB = false;
        m_allocatedI = false;
        if (aSource == null)
            throw new NullPointerException("Digital Source A was null");
        m_aSource = aSource;
        if (bSource == null)
            throw new NullPointerException("Digital Source B was null");
        m_aSource = aSource;
        m_bSource = bSource;
        m_indexSource = indexSource;
        initEncoder(reverseDirection);
        setIndexSource(indexSource);
    }

    /**
     * Encoder constructor. Construct a Encoder given a, b and index channels as
     * digital inputs. This is used in the case where the digital inputs are
     * shared. The Encoder class will not allocate the digital inputs and assume
     * that they already are counted.
     * 
     * The encoder will start counting immediately.
     * @param aSource The source that should be used for the a channel.
     * @param bSource the source that should be used for the b channel.
     * @param indexSource the source that should be used for the index channel.
     */
    @objid ("5a6c9b37-8ab7-44a6-90dd-d0448f78d1af")
    public Encoder(DigitalSource aSource, DigitalSource bSource, DigitalSource indexSource) {
        this(aSource, bSource, indexSource, false);
    }

    /**
     * @return the Encoder's FPGA index
     */
    @objid ("186c1d0d-2ba1-4a35-ad8e-1e8e467749e1")
    public int getFPGAIndex() {
        return m_index;
    }

    /**
     * @return the encoding scale factor 1x, 2x, or 4x, per the requested
     * encodingType. Used to divide raw edge counts down to spec'd counts.
     */
    @objid ("1aaea8c9-9a38-4c22-bfd4-4cf683b78b48")
    public int getEncodingScale() {
        return m_encodingScale;
    }

    @objid ("96518176-abb3-4ade-8005-a499123d28d2")
    public void free() {
        if (m_aSource != null && m_allocatedA) {
            m_aSource.free();
            m_allocatedA = false;
        }
        if (m_bSource != null && m_allocatedB) {
            m_bSource.free();
            m_allocatedB = false;
        }
        if (m_indexSource != null && m_allocatedI) {
            m_indexSource.free();
            m_allocatedI = false;
        }
        
        m_aSource = null;
        m_bSource = null;
        m_indexSource = null;
        if (m_counter != null) {
            m_counter.free();
            m_counter = null;
        } else {
            ByteBuffer status = ByteBuffer.allocateDirect(4);
            // set the byte order
            status.order(ByteOrder.LITTLE_ENDIAN);
            EncoderJNI.freeEncoder(m_encoder, status.asIntBuffer());
            HALUtil.checkStatus(status.asIntBuffer());
        }
    }

    /**
     * Gets the raw value from the encoder. The raw value is the actual count
     * unscaled by the 1x, 2x, or 4x scale factor.
     * @return Current raw count from the encoder
     */
    @objid ("051de63a-b350-4568-88a5-abe53ae624bd")
    public int getRaw() {
        int value;
        if (m_counter != null) {
            value = m_counter.get();
        } else {
            ByteBuffer status = ByteBuffer.allocateDirect(4);
            // set the byte order
            status.order(ByteOrder.LITTLE_ENDIAN);
            value = EncoderJNI.getEncoder(m_encoder, status.asIntBuffer());
            HALUtil.checkStatus(status.asIntBuffer());
        }
        return value;
    }

    /**
     * Gets the current count. Returns the current count on the Encoder. This
     * method compensates for the decoding type.
     * @return Current count from the Encoder adjusted for the 1x, 2x, or 4x
     * scale factor.
     */
    @objid ("a7c75e13-4a29-469b-b0e7-df54147c2a6c")
    public int get() {
        return (int) (getRaw() * decodingScaleFactor());
    }

    /**
     * Reset the Encoder distance to zero. Resets the current count to zero on
     * the encoder.
     */
    @objid ("71a22a54-8337-4d6b-9da7-80e46befe1ea")
    public void reset() {
        if (m_counter != null) {
            m_counter.reset();
        } else {
            ByteBuffer status = ByteBuffer.allocateDirect(4);
            // set the byte order
            status.order(ByteOrder.LITTLE_ENDIAN);
            EncoderJNI.resetEncoder(m_encoder, status.asIntBuffer());
            HALUtil.checkStatus(status.asIntBuffer());
        }
    }

    /**
     * Returns the period of the most recent pulse. Returns the period of the
     * most recent Encoder pulse in seconds. This method compensates for the
     * decoding type.
     * @deprecated Use getRate() in favor of this method. This returns unscaled
     * periods and getRate() scales using value from
     * setDistancePerPulse().
     * @return Period in seconds of the most recent pulse.
     */
    @objid ("bbc5669e-9f59-4f48-83c2-37bb29f06e3e")
    public double getPeriod() {
        double measuredPeriod;
        if (m_counter != null) {
            measuredPeriod = m_counter.getPeriod() / decodingScaleFactor();
        } else {
            ByteBuffer status = ByteBuffer.allocateDirect(4);
            // set the byte order
            status.order(ByteOrder.LITTLE_ENDIAN);
            measuredPeriod = EncoderJNI.getEncoderPeriod(m_encoder, status.asIntBuffer());
            HALUtil.checkStatus(status.asIntBuffer());
        }
        return measuredPeriod;
    }

    /**
     * Sets the maximum period for stopped detection. Sets the value that
     * represents the maximum period of the Encoder before it will assume that
     * the attached device is stopped. This timeout allows users to determine if
     * the wheels or other shaft has stopped rotating. This method compensates
     * for the decoding type.
     * @param maxPeriod The maximum time between rising and falling edges before the
     * FPGA will report the device stopped. This is expressed in
     * seconds.
     */
    @objid ("a87f74bb-0c97-4539-a095-1f2dcb4dca1a")
    public void setMaxPeriod(double maxPeriod) {
        if (m_counter != null) {
            m_counter.setMaxPeriod(maxPeriod * decodingScaleFactor());
        } else {
            ByteBuffer status = ByteBuffer.allocateDirect(4);
            // set the byte order
            status.order(ByteOrder.LITTLE_ENDIAN);
            EncoderJNI.setEncoderMaxPeriod(m_encoder, maxPeriod, status.asIntBuffer());
            HALUtil.checkStatus(status.asIntBuffer());
        }
    }

    /**
     * Determine if the encoder is stopped. Using the MaxPeriod value, a boolean
     * is returned that is true if the encoder is considered stopped and false
     * if it is still moving. A stopped encoder is one where the most recent
     * pulse width exceeds the MaxPeriod.
     * @return True if the encoder is considered stopped.
     */
    @objid ("bd7b11d5-6839-4350-800c-93e830c519e0")
    public boolean getStopped() {
        if (m_counter != null) {
            return m_counter.getStopped();
        } else {
            ByteBuffer status = ByteBuffer.allocateDirect(4);
            // set the byte order
            status.order(ByteOrder.LITTLE_ENDIAN);
            boolean value = EncoderJNI.getEncoderStopped(m_encoder, status.asIntBuffer()) != 0;
            HALUtil.checkStatus(status.asIntBuffer());
            return value;
        }
    }

    /**
     * The last direction the encoder value changed.
     * @return The last direction the encoder value changed.
     */
    @objid ("d3675cc1-dfd8-49df-89f7-338da89fa7a0")
    public boolean getDirection() {
        if (m_counter != null) {
            return m_counter.getDirection();
        } else {
            ByteBuffer status = ByteBuffer.allocateDirect(4);
            // set the byte order
            status.order(ByteOrder.LITTLE_ENDIAN);
            boolean value = EncoderJNI.getEncoderDirection(m_encoder, status.asIntBuffer()) != 0;
            HALUtil.checkStatus(status.asIntBuffer());
            return value;
        }
    }

    /**
     * The scale needed to convert a raw counter value into a number of encoder
     * pulses.
     */
    @objid ("c00af542-e359-42d5-a499-2233b14a463c")
    private double decodingScaleFactor() {
        switch (m_encodingType.value) {
        case EncodingType.k1X_val:
            return 1.0;
        case EncodingType.k2X_val:
            return 0.5;
        case EncodingType.k4X_val:
            return 0.25;
        default:
            // This is never reached, EncodingType enum limits values
            return 0.0;
        }
    }

    /**
     * Get the distance the robot has driven since the last reset.
     * @return The distance driven since the last reset as scaled by the value
     * from setDistancePerPulse().
     */
    @objid ("bace3753-d161-40b9-848a-d713953f91d7")
    public double getDistance() {
        return getRaw() * decodingScaleFactor() * m_distancePerPulse;
    }

    /**
     * Get the current rate of the encoder. Units are distance per second as
     * scaled by the value from setDistancePerPulse().
     * @return The current rate of the encoder.
     */
    @objid ("35338051-fb50-475c-bdc8-3409b6456df6")
    public double getRate() {
        return m_distancePerPulse / getPeriod();
    }

    /**
     * Set the minimum rate of the device before the hardware reports it
     * stopped.
     * @param minRate The minimum rate. The units are in distance per second as
     * scaled by the value from setDistancePerPulse().
     */
    @objid ("31cd4de3-a9df-4b56-bc88-705cd80657f4")
    public void setMinRate(double minRate) {
        setMaxPeriod(m_distancePerPulse / minRate);
    }

    /**
     * Set the distance per pulse for this encoder. This sets the multiplier
     * used to determine the distance driven based on the count value from the
     * encoder. Do not include the decoding type in this scale. The library
     * already compensates for the decoding type. Set this value based on the
     * encoder's rated Pulses per Revolution and factor in gearing reductions
     * following the encoder shaft. This distance can be in any units you like,
     * linear or angular.
     * @param distancePerPulse The scale factor that will be used to convert pulses to useful
     * units.
     */
    @objid ("8a444171-de70-4a61-98e7-a942b3206cc9")
    public void setDistancePerPulse(double distancePerPulse) {
        m_distancePerPulse = distancePerPulse;
    }

    /**
     * Set the direction sensing for this encoder. This sets the direction
     * sensing on the encoder so that it could count in the correct software
     * direction regardless of the mounting.
     * @param reverseDirection true if the encoder direction should be reversed
     */
    @objid ("4aa8f21f-3869-4068-b5fd-1af315bc3195")
    public void setReverseDirection(boolean reverseDirection) {
        if (m_counter != null) {
            m_counter.setReverseDirection(reverseDirection);
        } else {
        
        }
    }

    /**
     * Set the Samples to Average which specifies the number of samples of the
     * timer to average when calculating the period. Perform averaging to
     * account for mechanical imperfections or as oversampling to increase
     * resolution.
     * 
     * TODO: Should this throw a checked exception, so that the user has to deal
     * with giving an incorrect value?
     * @param samplesToAverage The number of samples to average from 1 to 127.
     */
    @objid ("f4bc7808-4202-43cf-aeff-e06baf3f6b22")
    public void setSamplesToAverage(int samplesToAverage) {
        switch (m_encodingType.value) {
        case EncodingType.k4X_val:
            ByteBuffer status = ByteBuffer.allocateDirect(4);
            // set the byte order
            status.order(ByteOrder.LITTLE_ENDIAN);
            EncoderJNI.setEncoderSamplesToAverage(m_encoder, samplesToAverage,
                    status.asIntBuffer());
            if (status.duplicate().get() == HALUtil.PARAMETER_OUT_OF_RANGE) {
                throw new BoundaryException(BoundaryException.getMessage(
                        samplesToAverage, 1, 127));
            }
            HALUtil.checkStatus(status.asIntBuffer());
            break;
        case EncodingType.k1X_val:
        case EncodingType.k2X_val:
            m_counter.setSamplesToAverage(samplesToAverage);
        }
    }

    /**
     * Get the Samples to Average which specifies the number of samples of the
     * timer to average when calculating the period. Perform averaging to
     * account for mechanical imperfections or as oversampling to increase
     * resolution.
     * @return SamplesToAverage The number of samples being averaged (from 1 to
     * 127)
     */
    @objid ("06f7315c-2c85-4ff2-9728-87b980491eba")
    public int getSamplesToAverage() {
        switch (m_encodingType.value) {
        case EncodingType.k4X_val:
            ByteBuffer status = ByteBuffer.allocateDirect(4);
            // set the byte order
            status.order(ByteOrder.LITTLE_ENDIAN);
            int value = EncoderJNI.getEncoderSamplesToAverage(m_encoder, status.asIntBuffer());
            HALUtil.checkStatus(status.asIntBuffer());
            return value;
        case EncodingType.k1X_val:
        case EncodingType.k2X_val:
            return m_counter.getSamplesToAverage();
        }
        return 1;
    }

    /**
     * Set which parameter of the encoder you are using as a process control
     * variable. The encoder class supports the rate and distance parameters.
     * @param pidSource An enum to select the parameter.
     */
    @objid ("31fbbfed-ac9d-405e-ac3c-94aeaed1d048")
    public void setPIDSourceParameter(PIDSourceParameter pidSource) {
        BoundaryException.assertWithinBounds(pidSource.value, 0, 1);
        m_pidSource = pidSource;
    }

    /**
     * Get the result to use in PIDController
     * @return the result to use in PIDController
     */
    @objid ("03257d3e-41cc-46f3-93ef-d88f5d13806a")
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
     * Set the index source for the encoder.  When this source rises, the encoder count automatically resets.
     * @param channel A DIO channel to set as the encoder index
     * @param type The state that will cause the encoder to reset
     */
    @objid ("a402ab5b-14ed-4664-b7bf-428c93c45d71")
    public void setIndexSource(int channel, IndexingType type) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
            
        boolean activeHigh = (type == IndexingType.kResetWhileHigh) || (type == IndexingType.kResetOnRisingEdge);
        boolean edgeSensitive = (type == IndexingType.kResetOnFallingEdge) || (type == IndexingType.kResetOnRisingEdge);
        
        EncoderJNI.setEncoderIndexSource(m_encoder, channel, false, activeHigh, edgeSensitive, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Set the index source for the encoder.  When this source is activated, the encoder count automatically resets.
     * @param channel A DIO channel to set as the encoder index
     */
    @objid ("e7212644-4497-4a43-a6f2-23f5b4899f7f")
    public void setIndexSource(int channel) {
        this.setIndexSource(channel, IndexingType.kResetOnRisingEdge);
    }

    /**
     * Set the index source for the encoder.  When this source rises, the encoder count automatically resets.
     * @param source A digital source to set as the encoder index
     * @param type The state that will cause the encoder to reset
     */
    @objid ("a959bc58-87b9-4340-bdc7-3c708db10dfc")
    public void setIndexSource(DigitalSource source, IndexingType type) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
            
        boolean activeHigh = (type == IndexingType.kResetWhileHigh) || (type == IndexingType.kResetOnRisingEdge);
        boolean edgeSensitive = (type == IndexingType.kResetOnFallingEdge) || (type == IndexingType.kResetOnRisingEdge);
        
        EncoderJNI.setEncoderIndexSource(m_encoder, source.getChannelForRouting(), source.getAnalogTriggerForRouting(),
                activeHigh, edgeSensitive, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Set the index source for the encoder.  When this source is activated, the encoder count automatically resets.
     * @param source A digital source to set as the encoder index
     */
    @objid ("04fa1ac4-8320-40a7-b0d0-e41623e5f01d")
    public void setIndexSource(DigitalSource source) {
        this.setIndexSource(source, IndexingType.kResetOnRisingEdge);
    }

/*
     * Live Window code, only does anything if live window is activated.
     */
    @objid ("d7f650af-f514-4d85-8058-0562325c48f6")
    public String getSmartDashboardType() {
        switch (m_encodingType.value) {
        case EncodingType.k4X_val:
            return "Quadrature Encoder";
        default:
            return "Encoder";
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("17e22a9a-b0f2-4250-8003-b39ac281435b")
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @objid ("6bfc16f4-644a-484a-ad38-0d0501d68ab2")
    public ITable getTable() {
        return m_table;
    }

    /**
     * {@inheritDoc}
     */
    @objid ("0efdff55-af29-416b-bcec-6c964e006388")
    public void updateTable() {
        if (m_table != null) {
            m_table.putNumber("Speed", getRate());
            m_table.putNumber("Distance", getDistance());
            m_table.putNumber("Distance per Tick", m_distancePerPulse);
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("7880d589-46a0-49fb-934e-5086fed2c0a2")
    public void startLiveWindowMode() {
    }

    /**
     * {@inheritDoc}
     */
    @objid ("95b95500-ef28-46b3-8795-9b0cd53f4d08")
    public void stopLiveWindowMode() {
    }

    @objid ("78889392-17a0-44bf-baeb-67035025647b")
    public enum IndexingType {
        kResetWhileHigh,
        kResetWhileLow,
        kResetOnFallingEdge,
        kResetOnRisingEdge;
    }

}
