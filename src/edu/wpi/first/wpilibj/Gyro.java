/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.util.BoundaryException;

/**
 * Use a rate gyro to return the robots heading relative to a starting position.
 * The Gyro class tracks the robots heading based on the starting position. As
 * the robot rotates the new heading is computed by integrating the rate of
 * rotation returned by the sensor. When the class is instantiated, it does a
 * short calibration routine where it samples the gyro while at rest to
 * determine the default offset. This is subtracted from each sample to
 * determine the heading.
 */
@objid ("14daa41b-a294-474c-96b7-ef3711394377")
public class Gyro extends SensorBase implements PIDSource, LiveWindowSendable {
    @objid ("019e9c69-3eef-48ad-bfcb-17e090dcef5e")
     static final int kOversampleBits = 10;

    @objid ("b388d534-a1f3-42bf-9413-f8323658986a")
     static final int kAverageBits = 0;

    @objid ("de473ce7-562e-4955-92ae-1307be858ca7")
     static final double kSamplesPerSecond = 50.0;

    @objid ("4c3d4580-e66a-4279-8239-dd59f22f71a3")
     static final double kCalibrationSampleTime = 5.0;

    @objid ("7c596f78-4826-4f66-ae4b-c491fe6c2a9e")
     static final double kDefaultVoltsPerDegreePerSecond = 0.007;

    @objid ("e18629dd-89a2-4d77-922a-202a6ee2d2cc")
     double m_voltsPerDegreePerSecond;

    @objid ("68c5981d-5704-48dd-a4ca-124a6bc83a78")
     double m_offset;

    @objid ("7d18c639-add5-483d-9799-337da5325e41")
     int m_center;

    @objid ("333cf3c5-fa3b-4dbf-b005-0755e1cdce69")
     boolean m_channelAllocated = false;

    @objid ("68472fac-fcc2-4cff-91a3-eaaf06b4f22d")
    private ITable m_table;

    @objid ("6fe4147b-bfa4-4b2d-ab62-08f57016e842")
    protected AnalogInput m_analog;

    @objid ("733f63c8-cc85-4237-9ff8-6bcc157e586b")
     AccumulatorResult result;

    @objid ("924f250a-706c-476c-8313-7eb380095e2d")
    private PIDSourceParameter m_pidSource;

    /**
     * Initialize the gyro. Calibrate the gyro by running for a number of
     * samples and computing the center value. Then use the center
     * value as the Accumulator center value for subsequent measurements. It's
     * important to make sure that the robot is not moving while the centering
     * calculations are in progress, this is typically done when the robot is
     * first turned on while it's sitting at rest before the competition starts.
     */
    @objid ("15a9fe1b-c5ab-44c5-b57c-e044733b313c")
    public void initGyro() {
        result = new AccumulatorResult();
        if (m_analog == null) {
            System.out.println("Null m_analog");
        }
        m_voltsPerDegreePerSecond = kDefaultVoltsPerDegreePerSecond;
        m_analog.setAverageBits(kAverageBits);
        m_analog.setOversampleBits(kOversampleBits);
        double sampleRate = kSamplesPerSecond
                * (1 << (kAverageBits + kOversampleBits));
        AnalogInput.setGlobalSampleRate(sampleRate);
        Timer.delay(1.0);
        
        m_analog.initAccumulator();
        m_analog.resetAccumulator();
        
        Timer.delay(kCalibrationSampleTime);
        
        m_analog.getAccumulatorOutput(result);
        
        m_center = (int) ((double) result.value / (double) result.count + .5);
        
        m_offset = ((double) result.value / (double) result.count)
                - m_center;
        
        m_analog.setAccumulatorCenter(m_center);
        m_analog.resetAccumulator();
        
        setDeadband(0.0);
        
        setPIDSourceParameter(PIDSourceParameter.kAngle);
        
        UsageReporting.report(tResourceType.kResourceType_Gyro, m_analog.getChannel());
        LiveWindow.addSensor("Gyro", m_analog.getChannel(), this);
    }

    /**
     * Gyro constructor using the channel number
     * @param channel The analog channel the gyro is connected to. Gyros can only
     * be used on on-board channels 0-1.
     */
    @objid ("0aaa8e41-a4a2-4d37-b1eb-a4007426d28c")
    public Gyro(int channel) {
        this(new AnalogInput(channel));
        m_channelAllocated = true;
    }

    /**
     * Gyro constructor with a precreated analog channel object. Use this
     * constructor when the analog channel needs to be shared.
     * @param channel The AnalogInput object that the gyro is connected to. Gyros
     * can only be used on on-board channels 0-1.
     */
    @objid ("a56c075a-a06c-4217-a6fc-5b58fb9b8ac1")
    public Gyro(AnalogInput channel) {
        m_analog = channel;
        if (m_analog == null) {
            throw new NullPointerException("AnalogInput supplied to Gyro constructor is null");
        }
        initGyro();
    }

    /**
     * Reset the gyro. Resets the gyro to a heading of zero. This can be used if
     * there is significant drift in the gyro and it needs to be recalibrated
     * after it has been running.
     */
    @objid ("fb47b85c-bc95-49c2-8194-6aac65a02821")
    public void reset() {
        if (m_analog != null) {
            m_analog.resetAccumulator();
        }
    }

    /**
     * Delete (free) the accumulator and the analog components used for the
     * gyro.
     */
    @objid ("a6cc77e8-f460-4c92-84cc-a0827ebfc9ff")
    @Override
    public void free() {
        if (m_analog != null && m_channelAllocated) {
            m_analog.free();
        }
        m_analog = null;
    }

    /**
     * Return the actual angle in degrees that the robot is currently facing.
     * 
     * The angle is based on the current accumulator value corrected by the
     * oversampling rate, the gyro type and the A/D calibration values. The
     * angle is continuous, that is it will continue from 360 to 361 degrees. This allows
     * algorithms that wouldn't want to see a discontinuity in the gyro output
     * as it sweeps past from 360 to 0 on the second time around.
     * @return the current heading of the robot in degrees. This heading is
     * based on integration of the returned rate from the gyro.
     */
    @objid ("4d6bbe11-a1b3-4e5c-8b52-0275b463a04b")
    public double getAngle() {
        if (m_analog == null) {
            return 0.0;
        } else {
            m_analog.getAccumulatorOutput(result);
        
            long value = result.value - (long) (result.count * m_offset);
        
            double scaledValue = value
                    * 1e-9
                    * m_analog.getLSBWeight()
                    * (1 << m_analog.getAverageBits())
                    / (AnalogInput.getGlobalSampleRate() * m_voltsPerDegreePerSecond);
        
            return scaledValue;
        }
    }

    /**
     * Return the rate of rotation of the gyro
     * 
     * The rate is based on the most recent reading of the gyro analog value
     * @return the current rate in degrees per second
     */
    @objid ("2a59d20d-023b-4ea0-95f4-dc485342e82a")
    public double getRate() {
        if (m_analog == null) {
            return 0.0;
        } else {
            return (m_analog.getAverageValue() - (m_center + m_offset))
                    * 1e-9
                    * m_analog.getLSBWeight()
                    / ((1 << m_analog.getOversampleBits()) * m_voltsPerDegreePerSecond);
        }
    }

    /**
     * Set the gyro sensitivity. This takes the number of
     * volts/degree/second sensitivity of the gyro and uses it in subsequent
     * calculations to allow the code to work with multiple gyros. This value
     * is typically found in the gyro datasheet.
     * @param voltsPerDegreePerSecond The sensitivity in Volts/degree/second.
     */
    @objid ("09ce9e34-4511-407f-a995-68e3396273d6")
    public void setSensitivity(double voltsPerDegreePerSecond) {
        m_voltsPerDegreePerSecond = voltsPerDegreePerSecond;
    }

    /**
     * Set the size of the neutral zone.  Any voltage from the gyro less than
     * this amount from the center is considered stationary.  Setting a
     * deadband will decrease the amount of drift when the gyro isn't rotating,
     * but will make it less accurate.
     * @param volts The size of the deadband in volts
     */
    @objid ("b209fb61-4057-41d7-adad-f478723428af")
    void setDeadband(double volts) {
        int deadband = (int)(volts * 1e9 / m_analog.getLSBWeight() * (1 << m_analog.getOversampleBits()));
        m_analog.setAccumulatorDeadband(deadband);
    }

    /**
     * Set which parameter of the gyro you are using as a process control
     * variable. The Gyro class supports the rate and angle parameters
     * @param pidSource An enum to select the parameter.
     */
    @objid ("af7fef18-2514-4ca5-8b99-8c9588488481")
    public void setPIDSourceParameter(PIDSourceParameter pidSource) {
        BoundaryException.assertWithinBounds(pidSource.value, 1, 2);
        m_pidSource = pidSource;
    }

    /**
     * Get the result to use in PIDController
     * @return the result to use in PIDController
     */
    @objid ("b820f94d-f4e4-4ffd-be9a-a771a40f0abd")
    @Override
    public double pidGet() {
        switch (m_pidSource.value) {
        case PIDSourceParameter.kRate_val:
            return getRate();
        case PIDSourceParameter.kAngle_val:
            return getAngle();
        default:
            return 0.0;
        }
    }

/*
     * Live Window code, only does anything if live window is activated.
     */
    @objid ("e68a570b-ef90-4f6f-8362-e79feaa36574")
    @Override
    public String getSmartDashboardType() {
        return "Gyro";
    }

    /**
     * {@inheritDoc}
     */
    @objid ("66215e36-c623-466d-83ba-a559ebbe3771")
    @Override
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @objid ("0d00abf1-df40-4c67-9dea-de1236a947fa")
    @Override
    public ITable getTable() {
        return m_table;
    }

    /**
     * {@inheritDoc}
     */
    @objid ("d226eddf-5a4f-44b9-8754-609cd03b4a0e")
    @Override
    public void updateTable() {
        if (m_table != null) {
            m_table.putNumber("Value", getAngle());
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("aaaea14a-fd34-4c6d-872f-efa2739d0a4b")
    @Override
    public void startLiveWindowMode() {
    }

    /**
     * {@inheritDoc}
     */
    @objid ("bd84a12e-1862-43a0-8a91-7969c2047afd")
    @Override
    public void stopLiveWindowMode() {
    }

}
