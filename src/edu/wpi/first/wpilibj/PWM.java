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
import edu.wpi.first.wpilibj.util.AllocationException;
import edu.wpi.first.wpilibj.util.CheckedAllocationException;

/**
 * Class implements the PWM generation in the FPGA.
 * 
 * The values supplied as arguments for PWM outputs range from -1.0 to 1.0. They are mapped
 * to the hardware dependent values, in this case 0-2000 for the FPGA.
 * Changes are immediately sent to the FPGA, and the update occurs at the next
 * FPGA cycle. There is no delay.
 * 
 * As of revision 0.1.10 of the FPGA, the FPGA interprets the 0-2000 values as follows:
 * - 2000 = maximum pulse width
 * - 1999 to 1001 = linear scaling from "full forward" to "center"
 * - 1000 = center value
 * - 999 to 2 = linear scaling from "center" to "full reverse"
 * - 1 = minimum pulse width (currently .5ms)
 * - 0 = disabled (i.e. PWM output is held low)
 */
@objid ("ad55f7e7-4311-491e-b394-71514c08b15c")
public class PWM extends SensorBase implements LiveWindowSendable {
    @objid ("0435d9c7-46ef-42e8-a2b0-48e5ccd482a7")
    private int m_channel;

    @objid ("565df64e-b588-4b77-9b10-f074a6d30145")
    private ByteBuffer m_port;

    /**
     * kDefaultPwmPeriod is in ms
     * 
     * - 20ms periods (50 Hz) are the "safest" setting in that this works for all devices
     * - 20ms periods seem to be desirable for Vex Motors
     * - 20ms periods are the specified period for HS-322HD servos, but work reliably down
     * to 10.0 ms; starting at about 8.5ms, the servo sometimes hums and get hot;
     * by 5.0ms the hum is nearly continuous
     * - 10ms periods work well for Victor 884
     * - 5ms periods allows higher update rates for Luminary Micro Jaguar speed controllers.
     * Due to the shipping firmware on the Jaguar, we can't run the update period less
     * than 5.05 ms.
     * 
     * kDefaultPwmPeriod is the 1x period (5.05 ms).  In hardware, the period scaling is implemented as an
     * output squelch to get longer periods for old devices.
     */
    @objid ("d763e1cf-b094-4e8e-9e82-75e4ba6310d4")
    protected static final double kDefaultPwmPeriod = 5.05;

    /**
     * kDefaultPwmCenter is the PWM range center in ms
     */
    @objid ("d33943e5-47fb-4465-88db-613cef587e04")
    protected static final double kDefaultPwmCenter = 1.5;

    /**
     * kDefaultPWMStepsDown is the number of PWM steps below the centerpoint
     */
    @objid ("d3643df5-813f-4997-ad61-58510249e94c")
    protected static final int kDefaultPwmStepsDown = 1000;

    @objid ("e3c06f14-163c-4de6-8a1b-a375cc0064b0")
    public static final int kPwmDisabled = 0;

    @objid ("4b28ff90-1d11-46bc-b084-e13821ba1d99")
     boolean m_eliminateDeadband;

    @objid ("c70cab9d-dde8-4986-aa6e-b7cbeb370c5e")
     int m_maxPwm;

    @objid ("e92f66b4-6e70-43bd-98df-2f1f6281bc1f")
     int m_deadbandMaxPwm;

    @objid ("69fed5c4-4fd9-4c8b-8250-c5dba5c3ee59")
     int m_centerPwm;

    @objid ("50987b2c-c56e-45d9-a388-776bb9583995")
     int m_deadbandMinPwm;

    @objid ("f3458748-a85c-4cbf-80a5-847108ca75a1")
     int m_minPwm;

    @objid ("c698c947-bcc4-4830-af62-ae8904cbc137")
    private ITable m_table;

    @objid ("373adbcb-83b4-44e7-8641-4b707f288884")
    private ITableListener m_table_listener;

    /**
     * Initialize PWMs given a channel.
     * 
     * This method is private and is the common path for all the constructors
     * for creating PWM instances. Checks channel value ranges and allocates
     * the appropriate channel. The allocation is only done to help users
     * ensure that they don't double assign channels.
     * @param channel The PWM channel number. 0-9 are on-board, 10-19 are on the MXP port
     */
    @objid ("6fb88e02-faa2-4ae7-bc62-6cfdd8db7d6c")
    private void initPWM(final int channel) {
        checkPWMChannel(channel);
        m_channel = channel;
        
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        m_port = DIOJNI.initializeDigitalPort(DIOJNI.getPort((byte) m_channel), status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        
        if (!PWMJNI.allocatePWMChannel(m_port, status.asIntBuffer()))
        {
            throw new AllocationException(
                "PWM channel " + channel  + " is already allocated");
        }
        HALUtil.checkStatus(status.asIntBuffer());
        
        PWMJNI.setPWM(m_port, (short) 0, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        
        m_eliminateDeadband = false;
        
        UsageReporting.report(tResourceType.kResourceType_PWM, channel);
    }

    /**
     * Allocate a PWM given a channel.
     * @param channel The PWM channel.
     */
    @objid ("981e0338-4f7c-4085-a599-6c9b0b54abce")
    public PWM(final int channel) {
        initPWM(channel);
    }

    /**
     * Free the PWM channel.
     * 
     * Free the resource associated with the PWM channel and set the value to 0.
     */
    @objid ("ef3965eb-ca43-4f1b-8f9f-8b2d5048d073")
    public void free() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        PWMJNI.setPWM(m_port, (short) 0, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        
        PWMJNI.freePWMChannel(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        
        PWMJNI.freeDIO(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Optionally eliminate the deadband from a speed controller.
     * @param eliminateDeadband If true, set the motor curve on the Jaguar to eliminate
     * the deadband in the middle of the range. Otherwise, keep the full range without
     * modifying any values.
     */
    @objid ("6f8153e6-d090-41b4-bcc9-e573338dd98e")
    public void enableDeadbandElimination(boolean eliminateDeadband) {
        m_eliminateDeadband = eliminateDeadband;
    }

    /**
     * Set the bounds on the PWM values.
     * This sets the bounds on the PWM values for a particular each type of controller. The values
     * determine the upper and lower speeds as well as the deadband bracket.
     * @deprecated Recommended to set bounds in ms using {@link #setBounds(double, double, double, double, double)}
     * @param max The Minimum pwm value
     * @param deadbandMax The high end of the deadband range
     * @param center The center speed (off)
     * @param deadbandMin The low end of the deadband range
     * @param min The minimum pwm value
     */
    @objid ("c4868528-3b58-4673-97f4-2d14bc62328b")
    public void setBounds(final int max, final int deadbandMax, final int center, final int deadbandMin, final int min) {
        m_maxPwm = max;
        m_deadbandMaxPwm = deadbandMax;
        m_centerPwm = center;
        m_deadbandMinPwm = deadbandMin;
        m_minPwm = min;
    }

    /**
     * Set the bounds on the PWM pulse widths.
     * This sets the bounds on the PWM values for a particular type of controller. The values
     * determine the upper and lower speeds as well as the deadband bracket.
     * @param max The max PWM pulse width in ms
     * @param deadbandMax The high end of the deadband range pulse width in ms
     * @param center The center (off) pulse width in ms
     * @param deadbandMin The low end of the deadband pulse width in ms
     * @param min The minimum pulse width in ms
     */
    @objid ("70741799-5124-4d57-8922-e1a1b8a2288a")
    protected void setBounds(double max, double deadbandMax, double center, double deadbandMin, double min) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        double loopTime = DIOJNI.getLoopTiming(status.asIntBuffer())/(kSystemClockTicksPerMicrosecond*1e3);
        
        m_maxPwm = (int)((max-kDefaultPwmCenter)/loopTime+kDefaultPwmStepsDown-1);
        m_deadbandMaxPwm = (int)((deadbandMax-kDefaultPwmCenter)/loopTime+kDefaultPwmStepsDown-1);
        m_centerPwm = (int)((center-kDefaultPwmCenter)/loopTime+kDefaultPwmStepsDown-1);
        m_deadbandMinPwm = (int)((deadbandMin-kDefaultPwmCenter)/loopTime+kDefaultPwmStepsDown-1);
        m_minPwm = (int)((min-kDefaultPwmCenter)/loopTime+kDefaultPwmStepsDown-1);
    }

    /**
     * Gets the channel number associated with the PWM Object.
     * @return The channel number.
     */
    @objid ("1177120a-cc33-4213-b9b6-1a3b2e6b8716")
    public int getChannel() {
        return m_channel;
    }

    /**
     * Set the PWM value based on a position.
     * 
     * This is intended to be used by servos.
     * @pre SetMaxPositivePwm() called.
     * @pre SetMinNegativePwm() called.
     * @param pos The position to set the servo between 0.0 and 1.0.
     */
    @objid ("6e24ce77-f91b-4e61-9fd0-418db1845d18")
    public void setPosition(double pos) {
        if (pos < 0.0) {
            pos = 0.0;
        } else if (pos > 1.0) {
            pos = 1.0;
        }
        
        int rawValue;
        // note, need to perform the multiplication below as floating point before converting to int
        rawValue = (int) ((pos * (double)getFullRangeScaleFactor()) + getMinNegativePwm());
        
        // send the computed pwm value to the FPGA
        setRaw(rawValue);
    }

    /**
     * Get the PWM value in terms of a position.
     * 
     * This is intended to be used by servos.
     * @pre SetMaxPositivePwm() called.
     * @pre SetMinNegativePwm() called.
     * @return The position the servo is set to between 0.0 and 1.0.
     */
    @objid ("36d4cad1-847a-40b7-a7d5-0f8149d73e56")
    public double getPosition() {
        int value = getRaw();
        if (value < getMinNegativePwm()) {
            return 0.0;
        } else if (value > getMaxPositivePwm()) {
            return 1.0;
        } else {
            return (double)(value - getMinNegativePwm()) / (double)getFullRangeScaleFactor();
        }
    }

    /**
     * Set the PWM value based on a speed.
     * 
     * This is intended to be used by speed controllers.
     * @pre SetMaxPositivePwm() called.
     * @pre SetMinPositivePwm() called.
     * @pre SetCenterPwm() called.
     * @pre SetMaxNegativePwm() called.
     * @pre SetMinNegativePwm() called.
     * @param speed The speed to set the speed controller between -1.0 and 1.0.
     */
    @objid ("23d67cb2-0aaa-40ca-9cc5-f3a63c7202de")
    final void setSpeed(double speed) {
        // clamp speed to be in the range 1.0 >= speed >= -1.0
        if (speed < -1.0) {
            speed = -1.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        
        // calculate the desired output pwm value by scaling the speed appropriately
        int rawValue;
        if (speed == 0.0) {
            rawValue = getCenterPwm();
        } else if (speed > 0.0) {
            rawValue = (int) (speed * ((double)getPositiveScaleFactor()) +
                              ((double)getMinPositivePwm()) + 0.5);
        } else {
            rawValue = (int) (speed * ((double)getNegativeScaleFactor()) +
                              ((double)getMaxNegativePwm()) + 0.5);
        }
        
        // send the computed pwm value to the FPGA
        setRaw(rawValue);
    }

    /**
     * Get the PWM value in terms of speed.
     * 
     * This is intended to be used by speed controllers.
     * @pre SetMaxPositivePwm() called.
     * @pre SetMinPositivePwm() called.
     * @pre SetMaxNegativePwm() called.
     * @pre SetMinNegativePwm() called.
     * @return The most recently set speed between -1.0 and 1.0.
     */
    @objid ("188aed3a-0da0-4c0b-b8a6-7b558337ad34")
    public double getSpeed() {
        int value = getRaw();
        if (value > getMaxPositivePwm()) {
            return 1.0;
        } else if (value < getMinNegativePwm()) {
            return -1.0;
        } else if (value > getMinPositivePwm()) {
            return (double) (value - getMinPositivePwm()) / (double)getPositiveScaleFactor();
        } else if (value < getMaxNegativePwm()) {
            return (double) (value - getMaxNegativePwm()) / (double)getNegativeScaleFactor();
        } else {
            return 0.0;
        }
    }

    /**
     * Set the PWM value directly to the hardware.
     * 
     * Write a raw value to a PWM channel.
     * @param value Raw PWM value.  Range 0 - 255.
     */
    @objid ("1306d59b-e0c6-4578-bcfc-446698c61b3c")
    public void setRaw(int value) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        PWMJNI.setPWM(m_port, (short) value, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Get the PWM value directly from the hardware.
     * 
     * Read a raw value from a PWM channel.
     * @return Raw PWM control value.  Range: 0 - 255.
     */
    @objid ("d15d6da9-c6fc-417d-9361-50d321ea7b66")
    public int getRaw() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        int value = PWMJNI.getPWM(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

    /**
     * Slow down the PWM signal for old devices.
     * @param mult The period multiplier to apply to this channel
     */
    @objid ("064462e8-159e-4390-b5fc-e1b0a99907c0")
    public void setPeriodMultiplier(PeriodMultiplier mult) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        switch (mult.value) {
        case PeriodMultiplier.k4X_val:
            // Squelch 3 out of 4 outputs
            PWMJNI.setPWMPeriodScale(m_port, 3, status.asIntBuffer());
            break;
        case PeriodMultiplier.k2X_val:
            // Squelch 1 out of 2 outputs
            PWMJNI.setPWMPeriodScale(m_port, 1, status.asIntBuffer());
            break;
        case PeriodMultiplier.k1X_val:
            // Don't squelch any outputs
            PWMJNI.setPWMPeriodScale(m_port, 0, status.asIntBuffer());
            break;
        default:
            //Cannot hit this, limited by PeriodMultiplier enum
        }
        
        HALUtil.checkStatus(status.asIntBuffer());
    }

    @objid ("0edc14cf-035e-4ed6-9a74-a5be920b4396")
    protected void setZeroLatch() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        PWMJNI.latchPWMZero(m_port, status.asIntBuffer());
        
        HALUtil.checkStatus(status.asIntBuffer());
    }

    @objid ("89a079ed-8a54-4dc7-b8a6-5062ea3488b7")
    private int getMaxPositivePwm() {
        return m_maxPwm;
    }

    @objid ("c42540a1-a1db-42ac-b08a-368c94bb7e44")
    private int getMinPositivePwm() {
        return m_eliminateDeadband ? m_deadbandMaxPwm : m_centerPwm + 1;
    }

    @objid ("68aa5da7-e669-4bff-9844-deaa40ca5e4b")
    private int getCenterPwm() {
        return m_centerPwm;
    }

    @objid ("a11ab1fe-14ed-4b5c-80ff-e1f24243cbec")
    private int getMaxNegativePwm() {
        return m_eliminateDeadband ? m_deadbandMinPwm : m_centerPwm - 1;
    }

    @objid ("70a48f7f-d67a-49b0-9043-faeff3fb44c5")
    private int getMinNegativePwm() {
        return m_minPwm;
    }

    @objid ("bd79e0b1-d6e3-4fdc-b54f-61ba9480278b")
    private int getPositiveScaleFactor() {
        return getMaxPositivePwm() - getMinPositivePwm();
    }

///< The scale for positive speeds.
    @objid ("13490d37-74a7-4843-a0e3-1ae426245fdb")
    private int getNegativeScaleFactor() {
        return getMaxNegativePwm() - getMinNegativePwm();
    }

///< The scale for negative speeds.
    @objid ("86abc25b-e957-47f2-be35-9c3c052f2997")
    private int getFullRangeScaleFactor() {
        return getMaxPositivePwm() - getMinNegativePwm();
    }

///< The scale for positions.
/*
     * Live Window code, only does anything if live window is activated.
     */
    @objid ("0c49d5c6-8d23-46ee-ac7e-3e2a15dcec88")
    public String getSmartDashboardType() {
        return "Speed Controller";
    }

    /**
     * {@inheritDoc}
     */
    @objid ("d5fcd09c-a799-4d73-bdf9-b6d6f84a00df")
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @objid ("b369b6da-a9d1-46d4-bd2e-07020488fc9a")
    public void updateTable() {
        if (m_table != null) {
            m_table.putNumber("Value", getSpeed());
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("cd3dc3b8-7a80-4a23-b2d9-bc04fca8ccd4")
    public ITable getTable() {
        return m_table;
    }

    /**
     * {@inheritDoc}
     */
    @objid ("72ab8022-8551-41a1-95db-1d4fac61b542")
    public void startLiveWindowMode() {
        setSpeed(0); // Stop for safety
        m_table_listener = new ITableListener() {
            public void valueChanged(ITable itable, String key, Object value, boolean bln) {
                setSpeed(((Double) value).doubleValue());
            }
        };
        m_table.addTableListener("Value", m_table_listener, true);
    }

    /**
     * {@inheritDoc}
     */
    @objid ("bee56b25-00b4-419f-a646-1e5084eaa32a")
    public void stopLiveWindowMode() {
        setSpeed(0); // Stop for safety
        // TODO: Broken, should only remove the listener from "Value" only.
        m_table.removeTableListener(m_table_listener);
    }

    /**
     * Represents the amount to multiply the minimum servo-pulse pwm period by.
     */
    @objid ("6c37913d-13d8-415c-9f58-bafde942e932")
    public static class PeriodMultiplier {
        /**
         * The integer value representing this enumeration
         */
        @objid ("a7be5fcf-c8bc-4d04-9a73-438afb5789d4")
        public final int value;

        @objid ("4f15b864-1538-4b12-a3b3-c473f36d41ba")
         static final int k1X_val = 1;

        @objid ("703af794-c6ee-4083-b90b-255a1f8d78e2")
         static final int k2X_val = 2;

        @objid ("77a8b6c3-5ff3-4452-96f5-bacf27826093")
         static final int k4X_val = 4;

        /**
         * Period Multiplier: don't skip pulses
         */
        @objid ("8b51e789-8031-4092-a93c-b2962d744286")
        public static final PeriodMultiplier k1X = new PeriodMultiplier(k1X_val);

        /**
         * Period Multiplier: skip every other pulse
         */
        @objid ("32749fc3-4247-48a9-9dd7-7014d77cec8e")
        public static final PeriodMultiplier k2X = new PeriodMultiplier(k2X_val);

        /**
         * Period Multiplier: skip three out of four pulses
         */
        @objid ("720b7f8e-1b66-4dac-89dd-b64795c1d1d5")
        public static final PeriodMultiplier k4X = new PeriodMultiplier(k4X_val);

        @objid ("6233cf51-ba13-465a-bb9b-3a0cc538eb94")
        private PeriodMultiplier(int value) {
            this.value = value;
        }

    }

}
