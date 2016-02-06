/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.can.CANExceptionFactory;
import edu.wpi.first.wpilibj.can.CANJNI;
import edu.wpi.first.wpilibj.can.CANMessageNotFoundException;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import edu.wpi.first.wpilibj.util.AllocationException;
import edu.wpi.first.wpilibj.util.CheckedAllocationException;

/**
 * Texas Instruments Jaguar Speed Controller as a CAN device.
 * @author Thomas Clark
 */
@objid ("61d3c0e9-4180-40e7-8c64-12f1d4442372")
public class CANJaguar implements MotorSafety, PIDOutput, SpeedController, LiveWindowSendable {
    @objid ("55ababda-d020-463f-a8f3-f8de8fbd887d")
    public static final int kMaxMessageDataSize = 8;

// The internal PID control loop in the Jaguar runs at 1kHz.
    @objid ("7fc6a5aa-e373-4706-922b-6e70f604206e")
    public static final int kControllerRate = 1000;

    @objid ("6ee8c0ee-1665-4658-bf7d-d60b269a8958")
    public static final double kApproxBusVoltage = 12.0;

    @objid ("2fcc9e51-fd76-43fe-8a5a-e24c85a6a22e")
    private static final int kFullMessageIDMask = CANJNI.CAN_MSGID_API_M | CANJNI.CAN_MSGID_MFR_M | CANJNI.CAN_MSGID_DTYPE_M;

    @objid ("7e5ffb16-bb73-452c-a6fb-d0a9f87fa03c")
    private static final int kSendMessagePeriod = 20;

    @objid ("6642eacf-91d4-4f9c-a8e5-a434416c443a")
    public static final int kCurrentFault = 1;

    @objid ("52fa6b0f-af36-4a36-be18-313857135e34")
    public static final int kTemperatureFault = 2;

    @objid ("22bcc5e2-dd2e-4f4b-94e2-41fb7f983c7d")
    public static final int kBusVoltageFault = 4;

    @objid ("9ca17715-9382-45e6-a135-cd7e55c90cef")
    public static final int kGateDriverFault = 8;

    /**
     * Limit switch masks
     */
    @objid ("f0b7e49c-386c-4ece-b83e-a0410d38199f")
    public static final int kForwardLimit = 1;

    @objid ("25207c0b-1427-4165-98e9-85f20c216c4d")
    public static final int kReverseLimit = 2;

    @objid ("e9cb9229-95e0-4b08-af5a-9b4a7ec63f08")
     byte m_deviceNumber;

    @objid ("35cf1447-fa1a-4d26-a93a-ef046c2d7e78")
     double m_value = 0.0f;

// Parameters/configuration
    @objid ("2a62df5c-def2-474d-acc0-73b6aadb37c3")
     ControlMode m_controlMode;

    @objid ("97d708e3-9eb3-4f8b-b6c4-e5f81d99acd0")
     int m_speedReference = CANJNI.LM_REF_NONE;

    @objid ("6781a5ab-5317-4e00-9798-bad866f37a01")
     int m_positionReference = CANJNI.LM_REF_NONE;

    @objid ("2f8a556b-5e62-4300-ae51-96da0336f8c3")
     double m_p = 0.0;

    @objid ("22737a7e-5247-4d08-a6a6-badc65c37685")
     double m_i = 0.0;

    @objid ("a61483cc-71a7-4fe6-a952-6ae375229042")
     double m_d = 0.0;

    @objid ("ba2f661c-deed-44d7-8ea0-7d7a7ea7214a")
     NeutralMode m_neutralMode = NeutralMode.Jumper;

    @objid ("55761170-027d-4156-a4b6-f112198cb744")
     short m_encoderCodesPerRev = 0;

    @objid ("422207b2-e47a-4402-8410-0a9d08d5750f")
     short m_potentiometerTurns = 0;

    @objid ("64ee7877-aadc-40fd-83d5-a93a246b8c53")
     LimitMode m_limitMode = LimitMode.SwitchInputsOnly;

    @objid ("c3d8a36f-36f5-4dfa-8459-57dbbb4dc558")
     double m_forwardLimit = 0.0;

    @objid ("aff5390e-3011-4347-a463-bd841ba5fbae")
     double m_reverseLimit = 0.0;

    @objid ("c45a74c9-ba37-4c62-a485-cb1565a07e45")
     double m_maxOutputVoltage = kApproxBusVoltage;

    @objid ("fdbfa263-158d-4dfc-8dad-059b5bd9880f")
     double m_voltageRampRate = 0.0;

    @objid ("15b232c6-e1be-435d-bf6b-7bf9e2ad2c0b")
     float m_faultTime = 0.0f;

// Which parameters have been verified since they were last set?
    @objid ("6e1413a6-0229-4168-9a51-148ae2305bb8")
     boolean m_controlModeVerified = true;

    @objid ("0d4bd824-14ed-4662-9e62-9e9f1cc41cc9")
     boolean m_speedRefVerified = true;

    @objid ("e95ef476-fa90-482b-bd6c-60437e677b1d")
     boolean m_posRefVerified = true;

    @objid ("4b28439f-b72e-441f-83b8-6512880d821a")
     boolean m_pVerified = true;

    @objid ("5103eb11-4713-42e2-9322-6e131cd6769c")
     boolean m_iVerified = true;

    @objid ("312c914c-4cf5-4f56-823b-3a23184f9254")
     boolean m_dVerified = true;

    @objid ("26d80905-445a-4464-be60-912d90313ff3")
     boolean m_neutralModeVerified = true;

    @objid ("522585a4-b3e0-4524-8514-b823ef3e027c")
     boolean m_encoderCodesPerRevVerified = true;

    @objid ("3722656c-6e77-474b-9a7c-8ac48df23b9f")
     boolean m_potentiometerTurnsVerified = true;

    @objid ("f3249a7a-5d66-46aa-9122-b5a372b8d3dc")
     boolean m_forwardLimitVerified = true;

    @objid ("c47b6ba5-0964-49db-ad92-d2c719018132")
     boolean m_reverseLimitVerified = true;

    @objid ("e305696c-cc10-43d0-b07f-c1c27732eafd")
     boolean m_limitModeVerified = true;

    @objid ("e65819f5-58da-435d-9623-6921e2b1d79a")
     boolean m_maxOutputVoltageVerified = true;

    @objid ("3e705bce-baec-4ad8-bdf6-aeaee2603bea")
     boolean m_voltageRampRateVerified = true;

    @objid ("7041b79b-0cd4-4bb1-acf4-1f1639d87acc")
     boolean m_faultTimeVerified = true;

// Status data
    @objid ("025e2779-2dc0-4702-93c1-8c1a111212d5")
     double m_busVoltage = 0.0f;

    @objid ("1e5facbe-c070-47c8-be2b-7815212da75e")
     double m_outputVoltage = 0.0f;

    @objid ("dee84d8e-9ea6-4946-ad38-a938b84965c8")
     double m_outputCurrent = 0.0f;

    @objid ("8983425d-6dd6-4e5e-9cad-09b56523be2c")
     double m_temperature = 0.0f;

    @objid ("1d8c81e8-f438-4c40-969e-836036baf8e9")
     double m_position = 0.0;

    @objid ("090c063e-f656-40b5-91e7-b38cb6db7777")
     double m_speed = 0.0;

    @objid ("85013f98-920b-42c2-9039-6c5cf6b3212e")
     byte m_limits = (byte)0;

    @objid ("97fd4cd8-658b-40e8-bfbb-9dcddaf6f1dc")
     short m_faults = (short)0;

    @objid ("db030adc-a560-41fb-bf7e-822ba61732b5")
     int m_firmwareVersion = 0;

    @objid ("a8ddff28-6803-41aa-bc83-4d9b53664b5a")
     byte m_hardwareVersion = (byte)0;

// Which periodic status messages have we received at least once?
    @objid ("5b46fa26-0fb8-4435-9d16-98860a2801f1")
     boolean m_receivedStatusMessage0 = false;

    @objid ("0337d037-2dbf-4dd0-b6da-4d450bec3a90")
     boolean m_receivedStatusMessage1 = false;

    @objid ("cc347e32-fb9e-4e4d-aefd-ca691ae0cd3c")
     boolean m_receivedStatusMessage2 = false;

    @objid ("af2d6d5c-2c3b-4709-bd84-4bfe0d51fa23")
     static final int kReceiveStatusAttempts = 50;

    @objid ("085a72e8-e369-4ed9-93d0-420cd8acc58e")
     boolean m_controlEnabled = true;

    @objid ("cc0b8d19-c0dc-444b-adfe-d5af5360d2a7")
    private ITable m_table = null;

    @objid ("cbe565ef-3b75-4cc1-903f-c7a252ffafe7")
    private ITableListener m_table_listener = null;

    @objid ("d009a4ef-5dc3-44a8-b50c-9a234f8c1088")
    private MotorSafetyHelper m_safetyHelper;

    @objid ("3d79695c-6bc0-4c1d-9148-0dab241eaf53")
    private static final Resource allocated = new Resource(63);

    /**
     * Sets an encoder as the speed reference only. <br> Passed as the "tag" when setting the control mode.
     */
    @objid ("9eb183a6-1674-4870-aa6b-85dfa9366f65")
    public static final EncoderTag kEncoder = new EncoderTag();

    /**
     * Sets a quadrature encoder as the position and speed reference. <br> Passed as the "tag" when setting the control mode.
     */
    @objid ("b241f83a-8e0c-4e21-b2d3-e92212b6bcf3")
    public static final QuadEncoderTag kQuadEncoder = new QuadEncoderTag();

    /**
     * Sets a potentiometer as the position reference only. <br> Passed as the "tag" when setting the control mode.
     */
    @objid ("d3615926-aace-499c-ae5e-6d9ba1e720db")
    public static final PotentiometerTag kPotentiometer = new PotentiometerTag();

    /**
     * Constructor for the CANJaguar device.<br>
     * By default the device is configured in Percent mode.
     * The control mode can be changed by calling one of the control modes listed below.
     * @see CANJaguar#setCurrentMode(double, double, double)
     * @see CANJaguar#setCurrentMode(PotentiometerTag, double, double, double)
     * @see CANJaguar#setCurrentMode(EncoderTag, int, double, double, double)
     * @see CANJaguar#setCurrentMode(QuadEncoderTag, int, double, double, double)
     * @see CANJaguar#setPercentMode()
     * @see CANJaguar#setPercentMode(PotentiometerTag)
     * @see CANJaguar#setPercentMode(EncoderTag, int)
     * @see CANJaguar#setPercentMode(QuadEncoderTag, int)
     * @see CANJaguar#setPositionMode(PotentiometerTag, double, double, double)
     * @see CANJaguar#setPositionMode(QuadEncoderTag, int, double, double, double)
     * @see CANJaguar#setSpeedMode(EncoderTag, int, double, double, double)
     * @see CANJaguar#setSpeedMode(QuadEncoderTag, int, double, double, double)
     * @see CANJaguar#setVoltageMode()
     * @see CANJaguar#setVoltageMode(PotentiometerTag)
     * @see CANJaguar#setVoltageMode(EncoderTag, int)
     * @see CANJaguar#setVoltageMode(QuadEncoderTag, int)
     * @param deviceNumber The address of the Jaguar on the CAN bus.
     */
    @objid ("1df6bd8f-6cf8-4920-82a4-2f4f0be44490")
    public CANJaguar(int deviceNumber) {
        try {
            allocated.allocate(deviceNumber-1);
        } catch (CheckedAllocationException e1) {
            throw new AllocationException(
                    "CANJaguar device " + e1.getMessage() + "(increment index by one)");
        }
        
        m_deviceNumber = (byte)deviceNumber;
        m_controlMode = ControlMode.PercentVbus;
        
        m_safetyHelper = new MotorSafetyHelper(this);
        
        boolean receivedFirmwareVersion = false;
        byte[] data = new byte[8];
        
        // Request firmware and hardware version only once
        requestMessage(CANJNI.CAN_IS_FRAME_REMOTE | CANJNI.CAN_MSGID_API_FIRMVER);
        requestMessage(CANJNI.LM_API_HWVER);
        
        // Wait until we've gotten all of the status data at least once.
        for(int i = 0; i < kReceiveStatusAttempts; i++)
        {
            Timer.delay(0.001);
        
            setupPeriodicStatus();
            updatePeriodicStatus();
        
            if(!receivedFirmwareVersion) {
                try {
                    getMessage(CANJNI.CAN_MSGID_API_FIRMVER, CANJNI.CAN_MSGID_FULL_M, data);
                    m_firmwareVersion = unpackINT32(data);
                    receivedFirmwareVersion = true;
                } catch(CANMessageNotFoundException e) {}
            }
        
            if(m_receivedStatusMessage0 &&
            m_receivedStatusMessage1 &&
            m_receivedStatusMessage2 &&
            receivedFirmwareVersion) {
                break;
            }
        }
        
        if(!m_receivedStatusMessage0 ||
        !m_receivedStatusMessage1 ||
        !m_receivedStatusMessage2 ||
        !receivedFirmwareVersion) {
            /* Free the resource */
            free();
            throw new CANMessageNotFoundException();
        }
        
        try {
            getMessage(CANJNI.LM_API_HWVER, CANJNI.CAN_MSGID_FULL_M, data);
            m_hardwareVersion = data[0];
        } catch(CANMessageNotFoundException e) {
            // Not all Jaguar firmware reports a hardware version.
            m_hardwareVersion = 0;
        }
        
        // 3330 was the first shipping RDK firmware version for the Jaguar
        if (m_firmwareVersion >= 3330 || m_firmwareVersion < 108)
        {
            if (m_firmwareVersion < 3330)
            {
                DriverStation.reportError("Jag " + m_deviceNumber + " firmware " + m_firmwareVersion + " is too old (must be at least version 108 of the FIRST approved firmware)", false);
            }
            else
            {
                DriverStation.reportError("Jag" + m_deviceNumber + " firmware " + m_firmwareVersion +  " is not FIRST approved (must be at least version 108 of the FIRST approved firmware)", false);
            }
            return;
        }
    }

    /**
     * Cancel periodic messages to the Jaguar, effectively disabling it.
     * No other methods should be called after this is called.
     */
    @objid ("d27ca99b-7142-4d12-8fa1-fa7144b51dc2")
    public void free() {
        allocated.free(m_deviceNumber-1);
        m_safetyHelper = null;
        
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        status.asIntBuffer().put(0, 0);
        
        int messageID ;
        
        // Disable periodic setpoints
        switch(m_controlMode) {
        case PercentVbus:
            messageID = m_deviceNumber | CANJNI.LM_API_VOLT_T_SET;
            break;
        
        case Speed:
            messageID = m_deviceNumber | CANJNI.LM_API_SPD_T_SET;
            break;
        
        case Position:
            messageID = m_deviceNumber | CANJNI.LM_API_POS_T_SET;
            break;
        
        case Current:
            messageID = m_deviceNumber | CANJNI.LM_API_ICTRL_T_SET;
            break;
        
        case Voltage:
            messageID = m_deviceNumber | CANJNI.LM_API_VCOMP_T_SET;
            break;
        
        default:
            return;
        }
        
        CANJNI.FRCNetworkCommunicationCANSessionMuxSendMessage(messageID, null,
            CANJNI.CAN_SEND_PERIOD_STOP_REPEATING, status.asIntBuffer());
        
        configMaxOutputVoltage(kApproxBusVoltage);
    }

    /**
     * @return The CAN ID passed in the constructor
     */
    @objid ("7e0d5309-3267-4878-953d-32e4dd92679a")
    int getDeviceNumber() {
        return m_deviceNumber;
    }

    /**
     * Get the recently set outputValue set point.
     * 
     * The scale and the units depend on the mode the Jaguar is in.<br>
     * In percentVbus mode, the outputValue is from -1.0 to 1.0 (same as PWM Jaguar).<br>
     * In voltage mode, the outputValue is in volts.<br>
     * In current mode, the outputValue is in amps.<br>
     * In speed mode, the outputValue is in rotations/minute.<br>
     * In position mode, the outputValue is in rotations.<br>
     * @return The most recently set outputValue set point.
     */
    @objid ("67bdf0cd-bba5-4571-8263-537069a6cd1f")
    @Override
    public double get() {
        return m_value;
    }

    /**
     * Sets the output set-point value.
     * 
     * The scale and the units depend on the mode the Jaguar is in.<br>
     * In percentVbus Mode, the outputValue is from -1.0 to 1.0 (same as PWM Jaguar).<br>
     * In voltage Mode, the outputValue is in volts. <br>
     * In current Mode, the outputValue is in amps.<br>
     * In speed mode, the outputValue is in rotations/minute.<br>
     * In position Mode, the outputValue is in rotations.
     * @param outputValue The set-point to sent to the motor controller.
     * @param syncGroup The update group to add this set() to, pending UpdateSyncGroup().  If 0, update immediately.
     */
    @objid ("c52a41af-eb9c-4f68-b813-d8284c5a7982")
    @Override
    public void set(double outputValue, byte syncGroup) {
        int messageID;
        byte[] data = new byte[8];
        byte dataSize;
        
        if(m_controlEnabled) {
            switch(m_controlMode) {
            case PercentVbus:
                messageID = CANJNI.LM_API_VOLT_T_SET;
                dataSize = packPercentage(data, outputValue);
                break;
        
            case Speed:
                messageID = CANJNI.LM_API_SPD_T_SET;
                dataSize = packFXP16_16(data, outputValue);
                break;
        
            case Position:
                messageID = CANJNI.LM_API_POS_T_SET;
                dataSize = packFXP16_16(data, outputValue);
                break;
        
            case Current:
                messageID = CANJNI.LM_API_ICTRL_T_SET;
                dataSize = packFXP8_8(data, outputValue);
                break;
        
        
            case Voltage:
                messageID = CANJNI.LM_API_VCOMP_T_SET;
                dataSize = packFXP8_8(data, outputValue);
                break;
        
            default:
                return;
            }
        
            if(syncGroup != 0) {
                data[dataSize++] = syncGroup;
            }
        
            sendMessage(messageID, data, dataSize, kSendMessagePeriod);
        
            if(m_safetyHelper != null) m_safetyHelper.feed();
        }
        
        m_value = outputValue;
        
        verify();
    }

    /**
     * Sets the output set-point value.
     * 
     * The scale and the units depend on the mode the Jaguar is in.<br>
     * In percentVbus mode, the outputValue is from -1.0 to 1.0 (same as PWM Jaguar).<br>
     * In voltage mode, the outputValue is in volts. <br>
     * In current mode, the outputValue is in amps. <br>
     * In speed mode, the outputValue is in rotations/minute.<br>
     * In position mode, the outputValue is in rotations.
     * @param value The set-point to sent to the motor controller.
     */
    @objid ("31c02542-b6a1-4e65-8786-ab55f91d3db1")
    @Override
    public void set(double value) {
        set(value, (byte)0);
    }

    /**
     * Check all unverified params and make sure they're equal to their local
     * cached versions. If a value isn't available, it gets requested.  If a value
     * doesn't match up, it gets set again.
     */
    @objid ("45f6d588-2ca3-4f8e-94b9-d105e6a8fbdf")
    protected void verify() {
        byte[] data = new byte[8];
        
        // If the Jaguar lost power, everything should be considered unverified
        try {
            getMessage(CANJNI.LM_API_STATUS_POWER, CANJNI.CAN_MSGID_FULL_M, data);
            boolean powerCycled = data[0] != 0;
        
            if(powerCycled) {
                // Clear the power cycled bit
                data[0] = 1;
                sendMessage(CANJNI.LM_API_STATUS_POWER, data, 1);
        
                // Mark everything as unverified
                m_controlModeVerified = false;
                m_speedRefVerified = false;
                m_posRefVerified = false;
                m_neutralModeVerified = false;
                m_encoderCodesPerRevVerified = false;
                m_potentiometerTurnsVerified = false;
                m_forwardLimitVerified = false;
                m_reverseLimitVerified = false;
                m_limitModeVerified = false;
                m_maxOutputVoltageVerified = false;
                m_faultTimeVerified = false;
        
                if(m_controlMode == ControlMode.PercentVbus || m_controlMode == ControlMode.Voltage) {
                    m_voltageRampRateVerified = false;
                }
                else {
                    m_pVerified = false;
                    m_iVerified = false;
                    m_dVerified = false;
                }
        
                // Verify periodic status messages again
                m_receivedStatusMessage0 = false;
                m_receivedStatusMessage1 = false;
                m_receivedStatusMessage2 = false;
        
                // Remove any old values from netcomms. Otherwise, parameters
                // are incorrectly marked as verified based on stale messages.
                int[] messages = new int[] {
                    CANJNI.LM_API_SPD_REF, CANJNI.LM_API_POS_REF,
                    CANJNI.LM_API_SPD_PC, CANJNI.LM_API_POS_PC,
                    CANJNI.LM_API_ICTRL_PC, CANJNI.LM_API_SPD_IC,
                    CANJNI.LM_API_POS_IC, CANJNI.LM_API_ICTRL_IC,
                    CANJNI.LM_API_SPD_DC, CANJNI.LM_API_POS_DC,
                    CANJNI.LM_API_ICTRL_DC, CANJNI.LM_API_CFG_ENC_LINES,
                    CANJNI.LM_API_CFG_POT_TURNS, CANJNI.LM_API_CFG_BRAKE_COAST,
                    CANJNI.LM_API_CFG_LIMIT_MODE, CANJNI.LM_API_CFG_LIMIT_REV,
                    CANJNI.LM_API_CFG_MAX_VOUT, CANJNI.LM_API_VOLT_SET_RAMP,
                    CANJNI.LM_API_VCOMP_COMP_RAMP, CANJNI.LM_API_CFG_FAULT_TIME,
                    CANJNI.LM_API_CFG_LIMIT_FWD
                };
        
                for(int message : messages) {
                    try {
                        getMessage(message, CANJNI.CAN_MSGID_FULL_M, data);
                    } catch (CANMessageNotFoundException e) {}
                }
            }
        } catch(CANMessageNotFoundException e) {
            requestMessage(CANJNI.LM_API_STATUS_POWER);
        }
        
        // Verify that any recently set parameters are correct
        if(!m_controlModeVerified && m_controlEnabled) {
            try {
                getMessage(CANJNI.LM_API_STATUS_CMODE, CANJNI.CAN_MSGID_FULL_M, data);
        
                ControlMode mode = ControlMode.values()[data[0]];
        
                if(m_controlMode == mode) {
                    m_controlModeVerified = true;
                } else {
                    // Enable control again to resend the control mode
                    enableControl();
                }
            } catch(CANMessageNotFoundException e) {
                // Verification is needed but not available - request it again.
                requestMessage(CANJNI.LM_API_STATUS_CMODE);
            }
        }
        
        if(!m_speedRefVerified) {
            try {
                getMessage(CANJNI.LM_API_SPD_REF, CANJNI.CAN_MSGID_FULL_M, data);
        
                int speedRef = data[0];
        
                if(m_speedReference == speedRef) {
                    m_speedRefVerified = true;
                } else {
                    // It's wrong - set it again
                    setSpeedReference(m_speedReference);
                }
            } catch(CANMessageNotFoundException e) {
                    // Verification is needed but not available - request it again.
                    requestMessage(CANJNI.LM_API_SPD_REF);
            }
        }
        
        if(!m_posRefVerified) {
            try {
                getMessage(CANJNI.LM_API_POS_REF, CANJNI.CAN_MSGID_FULL_M, data);
        
                int posRef = data[0];
        
                if(m_positionReference == posRef) {
                    m_posRefVerified = true;
                } else {
                    // It's wrong - set it again
                    setPositionReference(m_positionReference);
                }
            } catch(CANMessageNotFoundException e) {
                    // Verification is needed but not available - request it again.
                    requestMessage(CANJNI.LM_API_POS_REF);
            }
        }
        
        if(!m_pVerified) {
            int message = 0;
        
            switch(m_controlMode) {
                case Speed:
                    message = CANJNI.LM_API_SPD_PC;
                    break;
        
                case Position:
                    message = CANJNI.LM_API_POS_PC;
                    break;
        
                case Current:
                    message = CANJNI.LM_API_ICTRL_PC;
                    break;
        
                default:
                    break;
            }
        
            try {
                getMessage(message, CANJNI.CAN_MSGID_FULL_M, data);
        
                double p = unpackFXP16_16(data);
        
                if(FXP16_EQ(m_p, p)) {
                    m_pVerified = true;
                } else {
                    // It's wrong - set it again
                    setP(m_p);
                }
            } catch(CANMessageNotFoundException e) {
                    // Verification is needed but not available - request it again.
                    requestMessage(message);
            }
        }
        
        if(!m_iVerified) {
            int message = 0;
        
            switch(m_controlMode) {
                case Speed:
                    message = CANJNI.LM_API_SPD_IC;
                    break;
        
                case Position:
                    message = CANJNI.LM_API_POS_IC;
                    break;
        
                case Current:
                    message = CANJNI.LM_API_ICTRL_IC;
                    break;
        
                default:
                    break;
            }
        
            try {
                getMessage(message, CANJNI.CAN_MSGID_FULL_M, data);
        
                double i = unpackFXP16_16(data);
        
                if(FXP16_EQ(m_i, i)) {
                    m_iVerified = true;
                } else {
                    // It's wrong - set it again
                    setI(m_i);
                }
            } catch(CANMessageNotFoundException e) {
                    // Verification is needed but not available - request it again.
                    requestMessage(message);
            }
        }
        
        if(!m_dVerified) {
            int message = 0;
        
            switch(m_controlMode) {
                case Speed:
                    message = CANJNI.LM_API_SPD_DC;
                    break;
        
                case Position:
                    message = CANJNI.LM_API_POS_DC;
                    break;
        
                case Current:
                    message = CANJNI.LM_API_ICTRL_DC;
                    break;
        
                default:
                    break;
            }
        
            try {
                getMessage(message, CANJNI.CAN_MSGID_FULL_M, data);
        
                double d = unpackFXP16_16(data);
        
                if(FXP16_EQ(m_d, d)) {
                    m_dVerified = true;
                } else {
                    // It's wrong - set it again
                    setD(m_d);
                }
            } catch(CANMessageNotFoundException e) {
                    // Verification is needed but not available - request it again.
                    requestMessage(message);
            }
        }
        
        if(!m_neutralModeVerified) {
            try {
                getMessage(CANJNI.LM_API_CFG_BRAKE_COAST, CANJNI.CAN_MSGID_FULL_M, data);
        
                NeutralMode mode = NeutralMode.valueOf(data[0]);
        
                if(mode == m_neutralMode) {
                    m_neutralModeVerified = true;
                } else {
                    //It's wrong - set it again
                    configNeutralMode(m_neutralMode);
                }
            } catch(CANMessageNotFoundException e) {
                // Verification is needed but not available - request it again.
                requestMessage(CANJNI.LM_API_CFG_BRAKE_COAST);
            }
        }
        
        if(!m_encoderCodesPerRevVerified) {
            try {
                getMessage(CANJNI.LM_API_CFG_ENC_LINES, CANJNI.CAN_MSGID_FULL_M, data);
        
                short codes = unpackINT16(data);
        
                if(codes == m_encoderCodesPerRev) {
                    m_encoderCodesPerRevVerified = true;
                } else {
                    //It's wrong - set it again
                    configEncoderCodesPerRev(m_encoderCodesPerRev);
                }
            } catch(CANMessageNotFoundException e) {
                // Verification is needed but not available - request it again.
                requestMessage(CANJNI.LM_API_CFG_ENC_LINES);
            }
        }
        
        if(!m_potentiometerTurnsVerified) {
            try {
                getMessage(CANJNI.LM_API_CFG_POT_TURNS, CANJNI.CAN_MSGID_FULL_M, data);
        
                short turns = unpackINT16(data);
        
                if(turns == m_potentiometerTurns) {
                    m_potentiometerTurnsVerified = true;
                } else {
                    //It's wrong - set it again
                    configPotentiometerTurns(m_potentiometerTurns);
                }
            } catch(CANMessageNotFoundException e) {
                // Verification is needed but not available - request it again.
                requestMessage(CANJNI.LM_API_CFG_POT_TURNS);
            }
        }
        
        if(!m_limitModeVerified) {
            try {
                getMessage(CANJNI.LM_API_CFG_LIMIT_MODE, CANJNI.CAN_MSGID_FULL_M, data);
        
                LimitMode mode = LimitMode.valueOf(data[0]);
        
                if(mode == m_limitMode) {
                    m_limitModeVerified = true;
                } else {
                    //It's wrong - set it again
                    configLimitMode(m_limitMode);
                }
            } catch(CANMessageNotFoundException e) {
                // Verification is needed but not available - request it again.
                requestMessage(CANJNI.LM_API_CFG_LIMIT_MODE);
            }
        }
        
        if(!m_forwardLimitVerified) {
            try {
                getMessage(CANJNI.LM_API_CFG_LIMIT_FWD, CANJNI.CAN_MSGID_FULL_M, data);
        
                double limit = unpackFXP16_16(data);
        
                if(FXP16_EQ(limit, m_forwardLimit)) {
                    m_forwardLimitVerified = true;
                } else {
                    //It's wrong - set it again
                    configForwardLimit(m_forwardLimit);
                }
            } catch(CANMessageNotFoundException e) {
                // Verification is needed but not available - request it again.
                requestMessage(CANJNI.LM_API_CFG_LIMIT_FWD);
            }
        }
        
        if(!m_reverseLimitVerified) {
            try {
                getMessage(CANJNI.LM_API_CFG_LIMIT_REV, CANJNI.CAN_MSGID_FULL_M, data);
        
                double limit = unpackFXP16_16(data);
        
                if(FXP16_EQ(limit, m_reverseLimit)) {
                    m_reverseLimitVerified = true;
                } else {
                    //It's wrong - set it again
                    configReverseLimit(m_reverseLimit);
                }
            } catch(CANMessageNotFoundException e) {
                // Verification is needed but not available - request it again.
                requestMessage(CANJNI.LM_API_CFG_LIMIT_REV);
            }
        }
        
        if(!m_maxOutputVoltageVerified) {
            try {
                getMessage(CANJNI.LM_API_CFG_MAX_VOUT, CANJNI.CAN_MSGID_FULL_M, data);
        
                double voltage = unpackFXP8_8(data);
        
                // The returned max output voltage is sometimes slightly higher
                // or lower than what was sent.  This should not trigger
                // resending the message.
                if(Math.abs(voltage - m_maxOutputVoltage) < 0.1) {
                    m_maxOutputVoltageVerified = true;
                } else {
                    // It's wrong - set it again
                    configMaxOutputVoltage(m_maxOutputVoltage);
                }
        
            } catch(CANMessageNotFoundException e) {
                // Verification is needed but not available - request it again.
                requestMessage(CANJNI.LM_API_CFG_MAX_VOUT);
            }
        }
        
        if(!m_voltageRampRateVerified) {
            if(m_controlMode == ControlMode.PercentVbus) {
                try {
                    getMessage(CANJNI.LM_API_VOLT_SET_RAMP, CANJNI.CAN_MSGID_FULL_M, data);
        
                    double rate = unpackPercentage(data);
        
                    if(FXP16_EQ(rate, m_voltageRampRate)) {
                        m_voltageRampRateVerified = true;
                    } else {
                        // It's wrong - set it again
                        setVoltageRampRate(m_voltageRampRate);
                    }
        
                } catch(CANMessageNotFoundException e) {
                    // Verification is needed but not available - request it again.
                    requestMessage(CANJNI.LM_API_VOLT_SET_RAMP);
                }
            }
        } else if(m_controlMode == ControlMode.Voltage) {
            try {
                getMessage(CANJNI.LM_API_VCOMP_COMP_RAMP, CANJNI.CAN_MSGID_FULL_M, data);
        
                double rate = unpackFXP8_8(data);
        
                if(FXP8_EQ(rate, m_voltageRampRate)) {
                    m_voltageRampRateVerified = true;
                } else {
                    // It's wrong - set it again
                    setVoltageRampRate(m_voltageRampRate);
                }
        
            } catch(CANMessageNotFoundException e) {
                // Verification is needed but not available - request it again.
                requestMessage(CANJNI.LM_API_VCOMP_COMP_RAMP);
            }
        }
        
        if(!m_faultTimeVerified) {
            try {
                getMessage(CANJNI.LM_API_CFG_FAULT_TIME, CANJNI.CAN_MSGID_FULL_M, data);
        
                int faultTime = unpackINT16(data);
        
                if((int)(m_faultTime * 1000.0) == faultTime) {
                    m_faultTimeVerified = true;
                } else {
                    //It's wrong - set it again
                    configFaultTime(m_faultTime);
                }
            } catch(CANMessageNotFoundException e) {
                // Verification is needed but not available - request it again.
                requestMessage(CANJNI.LM_API_CFG_FAULT_TIME);
            }
        }
        
        if(!m_receivedStatusMessage0 ||
                !m_receivedStatusMessage1 ||
                !m_receivedStatusMessage2) {
            // If the periodic status messages haven't been verified as received,
            // request periodic status messages again and attempt to unpack any
            // available ones.
            setupPeriodicStatus();
            getTemperature();
            getPosition();
            getFaults();
        }
    }

    /**
     * Common interface for disabling a motor.
     * @deprecated Call {@link #disableControl()} instead.
     */
    @objid ("9a5cefe4-57ba-4eca-bed0-e161a5b902e5")
    @Deprecated
    @Override
    public void disable() {
        disableControl();
    }

// PIDOutput interface
    /**
     * Set the output to the value calculated by PIDController
     * @param output the value calculated by PIDController
     */
    @objid ("bf255217-4aba-4a24-a462-7c752666f790")
    @Override
    public void pidWrite(double output) {
        if (m_controlMode == ControlMode.PercentVbus) {
            set(output);
        } else {
            throw new IllegalStateException("PID only supported in PercentVbus mode");
        }
    }

    /**
     * Set the reference source device for speed controller mode.
     * 
     * Choose encoder as the source of speed feedback when in speed control mode.
     * @param reference Specify a speed reference.
     */
    @objid ("ae0d6e6f-bfc9-42e5-9a1a-e9b832011bb0")
    private void setSpeedReference(int reference) {
        sendMessage(CANJNI.LM_API_SPD_REF, new byte[] { (byte)reference }, 1);
        
        m_speedReference = reference;
        m_speedRefVerified = false;
    }

    /**
     * Set the reference source device for position controller mode.
     * 
     * Choose between using and encoder and using a potentiometer
     * as the source of position feedback when in position control mode.
     * @param reference Specify a position reference.
     */
    @objid ("fe600bd8-4c3d-4b0e-9ced-eaadb2369586")
    private void setPositionReference(int reference) {
        sendMessage(CANJNI.LM_API_POS_REF, new byte[] { (byte)reference }, 1);
        
        m_positionReference = reference;
        m_posRefVerified = false;
    }

    /**
     * Set the P constant for the closed loop modes.
     * @param p The proportional gain of the Jaguar's PID controller.
     */
    @objid ("9b0ccb26-9213-4480-800c-90eb1ae7b2e4")
    public void setP(double p) {
        byte[] data = new byte[8];
        byte dataSize = packFXP16_16(data, p);
        
        switch(m_controlMode) {
        case Speed:
            sendMessage(CANJNI.LM_API_SPD_PC, data, dataSize);
            break;
        
        case Position:
            sendMessage(CANJNI.LM_API_POS_PC, data, dataSize);
            break;
        
        case Current:
            sendMessage(CANJNI.LM_API_ICTRL_PC, data, dataSize);
            break;
        
        default:
            throw new IllegalStateException("PID constants only apply in Speed, Position, and Current mode");
        }
        
        m_p = p;
        m_pVerified = false;
    }

    /**
     * Set the I constant for the closed loop modes.
     * @param i The integral gain of the Jaguar's PID controller.
     */
    @objid ("2e0d53aa-6141-4b83-b680-b606c3b4ba63")
    public void setI(double i) {
        byte[] data = new byte[8];
        byte dataSize = packFXP16_16(data, i);
        
        switch(m_controlMode) {
        case Speed:
            sendMessage(CANJNI.LM_API_SPD_IC, data, dataSize);
            break;
        
        case Position:
            sendMessage(CANJNI.LM_API_POS_IC, data, dataSize);
            break;
        
        case Current:
            sendMessage(CANJNI.LM_API_ICTRL_IC, data, dataSize);
            break;
        
        default:
            throw new IllegalStateException("PID constants only apply in Speed, Position, and Current mode");
        }
        
        m_i = i;
        m_iVerified = false;
    }

    /**
     * Set the D constant for the closed loop modes.
     * @param d The derivative gain of the Jaguar's PID controller.
     */
    @objid ("697f1e48-4e3e-4d7d-b975-64eb07a9d3e4")
    public void setD(double d) {
        byte[] data = new byte[8];
        byte dataSize = packFXP16_16(data, d);
        
        switch(m_controlMode) {
        case Speed:
            sendMessage(CANJNI.LM_API_SPD_DC, data, dataSize);
            break;
        
        case Position:
            sendMessage(CANJNI.LM_API_POS_DC, data, dataSize);
            break;
        
        case Current:
            sendMessage(CANJNI.LM_API_ICTRL_DC, data, dataSize);
            break;
        
        default:
            throw new IllegalStateException("PID constants only apply in Speed, Position, and Current mode");
        }
        
        m_d = d;
        m_dVerified = false;
    }

    /**
     * Set the P, I, and D constants for the closed loop modes.
     * @param p The proportional gain of the Jaguar's PID controller.
     * @param i The integral gain of the Jaguar's PID controller.
     * @param d The differential gain of the Jaguar's PID controller.
     */
    @objid ("93ed759f-84fb-477a-a687-72c441e0834a")
    public void setPID(double p, double i, double d) {
        setP(p);
        setI(i);
        setD(d);
    }

    /**
     * Get the Proportional gain of the controller.
     * @return The proportional gain.
     */
    @objid ("00e26d44-9054-4da6-a4cf-63eff8e1ff36")
    public double getP() {
        if(m_controlMode.equals(ControlMode.PercentVbus) || m_controlMode.equals(ControlMode.Voltage)){
            throw new IllegalStateException("PID does not apply in Percent or Voltage control modes");
        }
        return m_p;
    }

    /**
     * Get the Integral gain of the controller.
     * @return The integral gain.
     */
    @objid ("4b19a3b8-af08-4ce6-bb9d-b1f27799a69c")
    public double getI() {
        if(m_controlMode.equals(ControlMode.PercentVbus) || m_controlMode.equals(ControlMode.Voltage)){
            throw new IllegalStateException("PID does not apply in Percent or Voltage control modes");
        }
        return m_i;
    }

    /**
     * Get the Derivative gain of the controller.
     * @return The derivative gain.
     */
    @objid ("3ec14200-7bfc-49e2-958a-cd1fd15752bf")
    public double getD() {
        if(m_controlMode.equals(ControlMode.PercentVbus) || m_controlMode.equals(ControlMode.Voltage)){
            throw new IllegalStateException("PID does not apply in Percent or Voltage control modes");
        }
        return m_d;
    }

    /**
     * Enable the closed loop controller.
     * 
     * Start actually controlling the output based on the feedback.
     * If starting a position controller with an encoder reference,
     * use the encoderInitialPosition parameter to initialize the
     * encoder state.
     * @param encoderInitialPosition Encoder position to set if position with encoder reference.  Ignored otherwise.
     */
    @objid ("2a402c52-390d-4c88-b527-7176b00f5ddb")
    public void enableControl(double encoderInitialPosition) {
        switch(m_controlMode) {
        case PercentVbus:
            sendMessage(CANJNI.LM_API_VOLT_T_EN, new byte[0], 0);
            break;
        
        case Speed:
            sendMessage(CANJNI.LM_API_SPD_T_EN, new byte[0], 0);
            break;
        
        case Position:
            byte[] data = new byte[8];
            int dataSize = packFXP16_16(data, encoderInitialPosition);
            sendMessage(CANJNI.LM_API_POS_T_EN, data, dataSize);
            break;
        
        case Current:
            sendMessage(CANJNI.LM_API_ICTRL_T_EN, new byte[0], 0);
            break;
        
        case Voltage:
            sendMessage(CANJNI.LM_API_VCOMP_T_EN, new byte[0], 0);
            break;
        }
        
        m_controlEnabled = true;
    }

    /**
     * Enable the closed loop controller.
     * 
     * Start actually controlling the output based on the feedback.
     * This is the same as calling <code>CANJaguar.enableControl(double encoderInitialPosition)</code>
     * with <code>encoderInitialPosition</code> set to <code>0.0</code>
     */
    @objid ("e84d5edf-5681-46d8-a5f1-e991709add82")
    public void enableControl() {
        enableControl(0.0);
    }

    /**
     * Disable the closed loop controller.
     * 
     * Stop driving the output based on the feedback.
     */
    @objid ("701d4bac-3938-4051-b018-17b38287f8b9")
    public void disableControl() {
        // Disable all control modes.
        sendMessage(CANJNI.LM_API_VOLT_DIS, new byte[0], 0);
        sendMessage(CANJNI.LM_API_SPD_DIS, new byte[0], 0);
        sendMessage(CANJNI.LM_API_POS_DIS, new byte[0], 0);
        sendMessage(CANJNI.LM_API_ICTRL_DIS, new byte[0], 0);
        sendMessage(CANJNI.LM_API_VCOMP_DIS, new byte[0], 0);
        
        // Stop all periodic setpoints
        sendMessage(CANJNI.LM_API_VOLT_T_SET, new byte[0], 0, CANJNI.CAN_SEND_PERIOD_STOP_REPEATING);
        sendMessage(CANJNI.LM_API_SPD_T_SET, new byte[0], 0, CANJNI.CAN_SEND_PERIOD_STOP_REPEATING);
        sendMessage(CANJNI.LM_API_POS_T_SET, new byte[0], 0, CANJNI.CAN_SEND_PERIOD_STOP_REPEATING);
        sendMessage(CANJNI.LM_API_ICTRL_T_SET, new byte[0], 0, CANJNI.CAN_SEND_PERIOD_STOP_REPEATING);
        sendMessage(CANJNI.LM_API_VCOMP_T_SET, new byte[0], 0, CANJNI.CAN_SEND_PERIOD_STOP_REPEATING);
        
        m_controlEnabled = false;
    }

    /**
     * Enable controlling the motor voltage as a percentage of the bus voltage
     * without any position or speed feedback.<br>
     * After calling this you must call {@link CANJaguar#enableControl()} or {@link CANJaguar#enableControl(double)} to enable the device.
     */
    @objid ("bd21c0ac-046e-401e-a14f-9bd2f19f918f")
    public void setPercentMode() {
        changeControlMode(ControlMode.PercentVbus);
        setPositionReference(CANJNI.LM_REF_NONE);
        setSpeedReference(CANJNI.LM_REF_NONE);
    }

    /**
     * Enable controlling the motor voltage as a percentage of the bus voltage,
     * and enable speed sensing from a non-quadrature encoder.<br>
     * After calling this you must call {@link CANJaguar#enableControl()} or {@link CANJaguar#enableControl(double)} to enable the device.
     * @param tag The constant {@link CANJaguar#kEncoder}
     * @param codesPerRev The counts per revolution on the encoder
     */
    @objid ("8ee224e8-11b3-4582-8791-d76ac5627e4e")
    public void setPercentMode(EncoderTag tag, int codesPerRev) {
        changeControlMode(ControlMode.PercentVbus);
        setPositionReference(CANJNI.LM_REF_NONE);
        setSpeedReference(CANJNI.LM_REF_ENCODER);
        configEncoderCodesPerRev(codesPerRev);
    }

    /**
     * Enable controlling the motor voltage as a percentage of the bus voltage,
     * and enable position and speed sensing from a quadrature encoder.<br>
     * After calling this you must call {@link CANJaguar#enableControl()} or {@link CANJaguar#enableControl(double)} to enable the device.
     * @param tag The constant {@link CANJaguar#kQuadEncoder}
     * @param codesPerRev The counts per revolution on the encoder
     */
    @objid ("4fe131f4-4481-44f1-be85-4f7a5810c07d")
    public void setPercentMode(QuadEncoderTag tag, int codesPerRev) {
        changeControlMode(ControlMode.PercentVbus);
        setPositionReference(CANJNI.LM_REF_ENCODER);
        setSpeedReference(CANJNI.LM_REF_QUAD_ENCODER);
        configEncoderCodesPerRev(codesPerRev);
    }

    /**
     * Enable controlling the motor voltage as a percentage of the bus voltage,
     * and enable position sensing from a potentiometer and no speed feedback.<br>
     * After calling this you must call {@link CANJaguar#enableControl()} or {@link CANJaguar#enableControl(double)} to enable the device.
     * @param tag The constant {@link CANJaguar#kPotentiometer}
     */
    @objid ("6026cb8c-edb1-4fbf-b02c-b2e7d6f33870")
    public void setPercentMode(PotentiometerTag tag) {
        changeControlMode(ControlMode.PercentVbus);
        setPositionReference(CANJNI.LM_REF_POT);
        setSpeedReference(CANJNI.LM_REF_NONE);
        configPotentiometerTurns(1);
    }

    /**
     * Enable controlling the motor current with a PID loop.<br>
     * After calling this you must call {@link CANJaguar#enableControl()} or {@link CANJaguar#enableControl(double)} to enable the device.
     * @param p The proportional gain of the Jaguar's PID controller.
     * @param i The integral gain of the Jaguar's PID controller.
     * @param d The differential gain of the Jaguar's PID controller.
     */
    @objid ("a9ce517d-24cf-402c-b8af-6d7948bff0a7")
    public void setCurrentMode(double p, double i, double d) {
        changeControlMode(ControlMode.Current);
        setPositionReference(CANJNI.LM_REF_NONE);
        setSpeedReference(CANJNI.LM_REF_NONE);
        setPID(p, i, d);
    }

    /**
     * Enable controlling the motor current with a PID loop, and enable speed
     * sensing from a non-quadrature encoder.<br>
     * After calling this you must call {@link CANJaguar#enableControl()} or {@link CANJaguar#enableControl(double)} to enable the device.
     * @param tag The constant {@link CANJaguar#kEncoder}
     * @param p The proportional gain of the Jaguar's PID controller.
     * @param i The integral gain of the Jaguar's PID controller.
     * @param d The differential gain of the Jaguar's PID controller.
     */
    @objid ("473c9916-a3bc-4a5a-aaa1-71544c738f33")
    public void setCurrentMode(EncoderTag tag, int codesPerRev, double p, double i, double d) {
        changeControlMode(ControlMode.Current);
        setPositionReference(CANJNI.LM_REF_NONE);
        setSpeedReference(CANJNI.LM_REF_NONE);
        configEncoderCodesPerRev(codesPerRev);
        setPID(p, i, d);
    }

    /**
     * Enable controlling the motor current with a PID loop, and enable speed and
     * position sensing from a quadrature encoder.<br>
     * After calling this you must call {@link CANJaguar#enableControl()} or {@link CANJaguar#enableControl(double)} to enable the device.
     * @param tag The constant {@link CANJaguar#kQuadEncoder}
     * @param p The proportional gain of the Jaguar's PID controller.
     * @param i The integral gain of the Jaguar's PID controller.
     * @param d The differential gain of the Jaguar's PID controller.
     */
    @objid ("f0773641-efef-4b4a-ae73-269565a2d294")
    public void setCurrentMode(QuadEncoderTag tag, int codesPerRev, double p, double i, double d) {
        changeControlMode(ControlMode.Current);
        setPositionReference(CANJNI.LM_REF_ENCODER);
        setSpeedReference(CANJNI.LM_REF_QUAD_ENCODER);
        configEncoderCodesPerRev(codesPerRev);
        setPID(p, i, d);
    }

    /**
     * Enable controlling the motor current with a PID loop, and enable position
     * sensing from a potentiometer.<br>
     * After calling this you must call {@link CANJaguar#enableControl()} or {@link CANJaguar#enableControl(double)} to enable the device.
     * @param tag The constant {@link CANJaguar#kPotentiometer}
     * @param p The proportional gain of the Jaguar's PID controller.
     * @param i The integral gain of the Jaguar's PID controller.
     * @param d The differential gain of the Jaguar's PID controller.
     */
    @objid ("7e0ae885-d77b-4c0b-838d-3c849edefc38")
    public void setCurrentMode(PotentiometerTag tag, double p, double i, double d) {
        changeControlMode(ControlMode.Current);
        setPositionReference(CANJNI.LM_REF_POT);
        setSpeedReference(CANJNI.LM_REF_NONE);
        configPotentiometerTurns(1);
        setPID(p, i, d);
    }

    /**
     * Enable controlling the speed with a feedback loop from a non-quadrature
     * encoder.<br>
     * After calling this you must call {@link CANJaguar#enableControl()} or {@link CANJaguar#enableControl(double)} to enable the device.
     * @param tag The constant {@link CANJaguar#kEncoder}
     * @param codesPerRev The counts per revolution on the encoder
     * @param p The proportional gain of the Jaguar's PID controller.
     * @param i The integral gain of the Jaguar's PID controller.
     * @param d The differential gain of the Jaguar's PID controller.
     */
    @objid ("7e30bfc3-cc33-44da-a553-f9992878746a")
    public void setSpeedMode(EncoderTag tag, int codesPerRev, double p, double i, double d) {
        changeControlMode(ControlMode.Speed);
        setPositionReference(CANJNI.LM_REF_NONE);
        setSpeedReference(CANJNI.LM_REF_ENCODER);
        configEncoderCodesPerRev(codesPerRev);
        setPID(p, i, d);
    }

    /**
     * Enable controlling the speed with a feedback loop from a quadrature encoder.<br>
     * After calling this you must call {@link CANJaguar#enableControl()} or {@link CANJaguar#enableControl(double)} to enable the device.
     * @param tag The constant {@link CANJaguar#kQuadEncoder}
     * @param codesPerRev The counts per revolution on the encoder
     * @param p The proportional gain of the Jaguar's PID controller.
     * @param i The integral gain of the Jaguar's PID controller.
     * @param d The differential gain of the Jaguar's PID controller.
     */
    @objid ("c9645c4b-4617-48cf-9728-de3d9a255249")
    public void setSpeedMode(QuadEncoderTag tag, int codesPerRev, double p, double i, double d) {
        changeControlMode(ControlMode.Speed);
        setPositionReference(CANJNI.LM_REF_ENCODER);
        setSpeedReference(CANJNI.LM_REF_QUAD_ENCODER);
        configEncoderCodesPerRev(codesPerRev);
        setPID(p, i, d);
    }

    /**
     * Enable controlling the position with a feedback loop using an encoder.<br>
     * After calling this you must call {@link CANJaguar#enableControl()} or {@link CANJaguar#enableControl(double)} to enable the device.
     * @param tag The constant {@link CANJaguar#kQuadEncoder}
     * @param codesPerRev The counts per revolution on the encoder
     * @param p The proportional gain of the Jaguar's PID controller.
     * @param i The integral gain of the Jaguar's PID controller.
     * @param d The differential gain of the Jaguar's PID controller.
     */
    @objid ("387b29b6-1189-471b-bb65-f396c6fd50fd")
    public void setPositionMode(QuadEncoderTag tag, int codesPerRev, double p, double i, double d) {
        changeControlMode(ControlMode.Position);
        setPositionReference(CANJNI.LM_REF_ENCODER);
        configEncoderCodesPerRev(codesPerRev);
        setPID(p, i, d);
    }

    /**
     * Enable controlling the position with a feedback loop using a potentiometer.<br>
     * After calling this you must call {@link CANJaguar#enableControl()} or {@link CANJaguar#enableControl(double)} to enable the device.
     * @param tag The constant {@link CANJaguar#kPotentiometer}
     * @param p The proportional gain of the Jaguar's PID controller.
     * @param i The integral gain of the Jaguar's PID controller.
     * @param d The differential gain of the Jaguar's PID controller.
     */
    @objid ("34252bab-176e-42d6-b601-f6e5154127c8")
    public void setPositionMode(PotentiometerTag tag, double p, double i, double d) {
        changeControlMode(ControlMode.Position);
        setPositionReference(CANJNI.LM_REF_POT);
        configPotentiometerTurns(1);
        setPID(p, i, d);
    }

    /**
     * Enable controlling the motor voltage without any position or speed feedback.<br>
     * After calling this you must call {@link CANJaguar#enableControl()} or {@link CANJaguar#enableControl(double)} to enable the device.
     */
    @objid ("56a941aa-e715-4c14-ade1-dbb170164ed1")
    public void setVoltageMode() {
        changeControlMode(ControlMode.Voltage);
        setPositionReference(CANJNI.LM_REF_NONE);
        setSpeedReference(CANJNI.LM_REF_NONE);
    }

    /**
     * Enable controlling the motor voltage with speed feedback from a
     * non-quadrature encoder and no position feedback.<br>
     * After calling this you must call {@link CANJaguar#enableControl()} or {@link CANJaguar#enableControl(double)} to enable the device.
     * @param tag The constant {@link CANJaguar#kEncoder}
     * @param codesPerRev The counts per revolution on the encoder
     */
    @objid ("370bc50a-2db4-4443-9ace-c35cb2fdb1bd")
    public void setVoltageMode(EncoderTag tag, int codesPerRev) {
        changeControlMode(ControlMode.Voltage);
        setPositionReference(CANJNI.LM_REF_NONE);
        setSpeedReference(CANJNI.LM_REF_ENCODER);
        configEncoderCodesPerRev(codesPerRev);
    }

    /**
     * Enable controlling the motor voltage with position and speed feedback from a
     * quadrature encoder.<br>
     * After calling this you must call {@link CANJaguar#enableControl()} or {@link CANJaguar#enableControl(double)} to enable the device.
     * @param tag The constant {@link CANJaguar#kQuadEncoder}
     * @param codesPerRev The counts per revolution on the encoder
     */
    @objid ("b40d4509-182f-4682-bc4d-084af5c225d2")
    public void setVoltageMode(QuadEncoderTag tag, int codesPerRev) {
        changeControlMode(ControlMode.Voltage);
        setPositionReference(CANJNI.LM_REF_ENCODER);
        setSpeedReference(CANJNI.LM_REF_QUAD_ENCODER);
        configEncoderCodesPerRev(codesPerRev);
    }

    /**
     * Enable controlling the motor voltage with position feedback from a
     * potentiometer and no speed feedback.
     * @param tag The constant {@link CANJaguar#kPotentiometer}
     */
    @objid ("02cc4930-e16d-465d-9627-1e28cddb4110")
    public void setVoltageMode(PotentiometerTag tag) {
        changeControlMode(ControlMode.Voltage);
        setPositionReference(CANJNI.LM_REF_POT);
        setSpeedReference(CANJNI.LM_REF_NONE);
        configPotentiometerTurns(1);
    }

    /**
     * Used internally. In order to set the control mode see the methods listed below.
     * 
     * Change the control mode of this Jaguar object.
     * 
     * After changing modes, configure any PID constants or other settings needed
     * and then EnableControl() to actually change the mode on the Jaguar.
     * @see CANJaguar#setCurrentMode(double, double, double)
     * @see CANJaguar#setCurrentMode(PotentiometerTag, double, double, double)
     * @see CANJaguar#setCurrentMode(EncoderTag, int, double, double, double)
     * @see CANJaguar#setCurrentMode(QuadEncoderTag, int, double, double, double)
     * @see CANJaguar#setPercentMode()
     * @see CANJaguar#setPercentMode(PotentiometerTag)
     * @see CANJaguar#setPercentMode(EncoderTag, int)
     * @see CANJaguar#setPercentMode(QuadEncoderTag, int)
     * @see CANJaguar#setPositionMode(PotentiometerTag, double, double, double)
     * @see CANJaguar#setPositionMode(QuadEncoderTag, int, double, double, double)
     * @see CANJaguar#setSpeedMode(EncoderTag, int, double, double, double)
     * @see CANJaguar#setSpeedMode(QuadEncoderTag, int, double, double, double)
     * @see CANJaguar#setVoltageMode()
     * @see CANJaguar#setVoltageMode(PotentiometerTag)
     * @see CANJaguar#setVoltageMode(EncoderTag, int)
     * @see CANJaguar#setVoltageMode(QuadEncoderTag, int)
     * @param controlMode The new mode.
     */
    @objid ("8fae3d2b-b07c-438c-83e2-54133bed92b5")
    private void changeControlMode(ControlMode controlMode) {
        // Disable the previous mode
        disableControl();
        
        // Update the local mode
        m_controlMode = controlMode;
        m_controlModeVerified = false;
    }

    /**
     * Get the active control mode from the Jaguar.
     * 
     * Ask the Jagaur what mode it is in.
     * @return ControlMode that the Jag is in.
     */
    @objid ("29e42d3e-b7cd-479a-b4a9-dbe3508155fd")
    public ControlMode getControlMode() {
        return m_controlMode;
    }

    /**
     * Get the voltage at the battery input terminals of the Jaguar.
     * @return The bus voltage in Volts.
     */
    @objid ("e9a10265-2ad4-42e8-84c5-9837e1b53e2f")
    public double getBusVoltage() {
        updatePeriodicStatus();
        return m_busVoltage;
    }

    /**
     * Get the voltage being output from the motor terminals of the Jaguar.
     * @return The output voltage in Volts.
     */
    @objid ("8cb0208b-40df-4a17-b390-99ad46ffe0ca")
    public double getOutputVoltage() {
        updatePeriodicStatus();
        return m_outputVoltage;
    }

    /**
     * Get the current through the motor terminals of the Jaguar.
     * @return The output current in Amps.
     */
    @objid ("e5c392c8-6613-4f81-8432-4c8137ece412")
    public double getOutputCurrent() {
        updatePeriodicStatus();
        return m_outputCurrent;
    }

    /**
     * Get the internal temperature of the Jaguar.
     * @return The temperature of the Jaguar in degrees Celsius.
     */
    @objid ("df40017e-39f8-491d-b1f3-cbc23c1db540")
    public double getTemperature() {
        updatePeriodicStatus();
        return m_temperature;
    }

    /**
     * Get the position of the encoder or potentiometer.
     * @see CANJaguar#configPotentiometerTurns(int)
     * @see CANJaguar#configEncoderCodesPerRev(int)
     * @return The position of the motor in rotations based on the configured feedback.
     */
    @objid ("8b7e68bc-70d2-414e-bef0-39a65af2aca3")
    public double getPosition() {
        updatePeriodicStatus();
        return m_position;
    }

    /**
     * Get the speed of the encoder.
     * @return The speed of the motor in RPM based on the configured feedback.
     */
    @objid ("a218b430-9a23-46b4-99dd-e522254cd6e0")
    public double getSpeed() {
        updatePeriodicStatus();
        return m_speed;
    }

    /**
     * Get the status of the forward limit switch.
     * @return true if the motor is allowed to turn in the forward direction.
     */
    @objid ("2b0bb5be-8751-4648-b9b2-6b3c63131368")
    public boolean getForwardLimitOK() {
        updatePeriodicStatus();
        return (m_limits & kForwardLimit) != 0;
    }

    /**
     * Get the status of the reverse limit switch.
     * @return true if the motor is allowed to turn in the reverse direction.
     */
    @objid ("d8c991b6-8766-4e3a-962f-8d92e9b89238")
    public boolean getReverseLimitOK() {
        updatePeriodicStatus();
        return (m_limits & kReverseLimit) != 0;
    }

    /**
     * Get the status of any faults the Jaguar has detected.
     * @see #kCurrentFault
     * @see #kBusVoltageFault
     * @see #kTemperatureFault
     * @see #kGateDriverFault
     * @return A bit-mask of faults defined by the "Faults" constants.
     */
    @objid ("0dad2a58-d589-492c-bac3-29cf570cd72e")
    public short getFaults() {
        updatePeriodicStatus();
        return m_faults;
    }

    /**
     * set the maximum voltage change rate.
     * 
     * When in PercentVbus or Voltage output mode, the rate at which the voltage changes can
     * be limited to reduce current spikes.  set this to 0.0 to disable rate limiting.
     * @param rampRate The maximum rate of voltage change in Percent Voltage mode in V/s.
     */
    @objid ("a3857dc3-78ee-4b9d-849e-925326d8c332")
    public void setVoltageRampRate(double rampRate) {
        byte[] data = new byte[8];
        int dataSize;
        int message;
        
        switch(m_controlMode) {
        case PercentVbus:
            dataSize = packPercentage(data, rampRate / (m_maxOutputVoltage * kControllerRate));
            message = CANJNI.LM_API_VOLT_SET_RAMP;
            break;
        case Voltage:
            dataSize = packFXP8_8(data, rampRate / kControllerRate);
            message = CANJNI.LM_API_VCOMP_COMP_RAMP;
            break;
        default:
            throw new IllegalStateException("Voltage ramp rate only applies in Percentage and Voltage modes");
        }
        
        sendMessage(message, data, dataSize);
    }

    /**
     * Get the version of the firmware running on the Jaguar.
     * @return The firmware version.  0 if the device did not respond.
     */
    @objid ("bd18c7df-3175-4e1d-b0b3-ef62b51bb894")
    public int getFirmwareVersion() {
        return m_firmwareVersion;
    }

    /**
     * Get the version of the Jaguar hardware.
     * @return The hardware version. 1: Jaguar,  2: Black Jaguar
     */
    @objid ("d4ff0ec1-5286-4cc8-859c-afa700aeab2d")
    public byte getHardwareVersion() {
        return m_hardwareVersion;
    }

    /**
     * Configure what the controller does to the H-Bridge when neutral (not driving the output).
     * 
     * This allows you to override the jumper configuration for brake or coast.
     * @param mode Select to use the jumper setting or to override it to coast or brake.
     */
    @objid ("64dacb1a-5646-41c1-93dc-6f6307f62735")
    public void configNeutralMode(NeutralMode mode) {
        sendMessage(CANJNI.LM_API_CFG_BRAKE_COAST, new byte[] { mode.value }, 1);
        
        m_neutralMode = mode;
        m_neutralModeVerified = false;
    }

    /**
     * Configure how many codes per revolution are generated by your encoder.
     * @param codesPerRev The number of counts per revolution in 1X mode.
     */
    @objid ("faa00f05-996b-4779-b158-3be67a3d345a")
    public void configEncoderCodesPerRev(int codesPerRev) {
        byte[] data = new byte[8];
        
        int dataSize = packINT16(data, (short)codesPerRev);
        sendMessage(CANJNI.LM_API_CFG_ENC_LINES, data, dataSize);
        
        m_encoderCodesPerRev = (short)codesPerRev;
        m_encoderCodesPerRevVerified = false;
    }

    /**
     * Configure the number of turns on the potentiometer.
     * 
     * There is no special support for continuous turn potentiometers.
     * Only integer numbers of turns are supported.
     * @param turns The number of turns of the potentiometer
     */
    @objid ("a073c9de-38d4-4acf-82d6-e3f813c93da9")
    public void configPotentiometerTurns(int turns) {
        byte[] data = new byte[8];
        
        int dataSize = packINT16(data, (short)turns);
        sendMessage(CANJNI.LM_API_CFG_POT_TURNS, data, dataSize);
        
        m_potentiometerTurns = (short)turns;
        m_potentiometerTurnsVerified = false;
    }

    /**
     * Configure Soft Position Limits when in Position Controller mode.<br>
     * 
     * When controlling position, you can add additional limits on top of the limit switch inputs
     * that are based on the position feedback.  If the position limit is reached or the
     * switch is opened, that direction will be disabled.
     * @param forwardLimitPosition The position that, if exceeded, will disable the forward direction.
     * @param reverseLimitPosition The position that, if exceeded, will disable the reverse direction.
     */
    @objid ("2addc1da-9f51-45a3-9042-d76635d850c9")
    public void configSoftPositionLimits(double forwardLimitPosition, double reverseLimitPosition) {
        configLimitMode(LimitMode.SoftPositionLimits);
        configForwardLimit(forwardLimitPosition);
        configReverseLimit(reverseLimitPosition);
    }

    /**
     * Disable Soft Position Limits if previously enabled.<br>
     * 
     * Soft Position Limits are disabled by default.
     */
    @objid ("87cb0743-a331-4655-b7ea-0e1dc2e1c80f")
    public void disableSoftPositionLimits() {
        configLimitMode(LimitMode.SwitchInputsOnly);
    }

    /**
     * Set the limit mode for position control mode.<br>
     * 
     * Use {@link #configSoftPositionLimits(double, double)} or {@link #disableSoftPositionLimits()} to set this
     * automatically.
     * @see LimitMode#SwitchInputsOnly
     * @see LimitMode#SoftPositionLimits
     * @param mode The {@link LimitMode} to use to limit the rotation of the device.
     */
    @objid ("e4dd5d2e-a287-455e-9789-177b93576198")
    public void configLimitMode(LimitMode mode) {
        sendMessage(CANJNI.LM_API_CFG_LIMIT_MODE, new byte[] { mode.value }, 1);
    }

    /**
     * Set the position that, if exceeded, will disable the forward direction.
     * 
     * Use {@link #configSoftPositionLimits(double, double)} to set this and the {@link LimitMode} automatically.
     * @param forwardLimitPosition The position that, if exceeded, will disable the forward direction.
     */
    @objid ("831a5800-61f8-4d93-be22-835645be69d2")
    public void configForwardLimit(double forwardLimitPosition) {
        byte[] data = new byte[8];
        
        int dataSize = packFXP16_16(data, forwardLimitPosition);
        data[dataSize++] = 1;
        sendMessage(CANJNI.LM_API_CFG_LIMIT_FWD, data, dataSize);
        
        m_forwardLimit = forwardLimitPosition;
        m_forwardLimitVerified = false;
    }

    /**
     * Set the position that, if exceeded, will disable the reverse direction.
     * 
     * Use {@link #configSoftPositionLimits(double, double)} to set this and the {@link LimitMode} automatically.
     * @param reverseLimitPosition The position that, if exceeded, will disable the reverse direction.
     */
    @objid ("84221e31-bf2e-4d1e-ad88-7b386a30421d")
    public void configReverseLimit(double reverseLimitPosition) {
        byte[] data = new byte[8];
        
        int dataSize = packFXP16_16(data, reverseLimitPosition);
        data[dataSize++] = 1;
        sendMessage(CANJNI.LM_API_CFG_LIMIT_REV, data, dataSize);
        
        m_reverseLimit = reverseLimitPosition;
        m_reverseLimitVerified = false;
    }

    /**
     * Configure the maximum voltage that the Jaguar will ever output.
     * 
     * This can be used to limit the maximum output voltage in all modes so that
     * motors which cannot withstand full bus voltage can be used safely.
     * @param voltage The maximum voltage output by the Jaguar.
     */
    @objid ("e621f892-274c-449e-89ac-c57569bf560a")
    public void configMaxOutputVoltage(double voltage) {
        byte[] data = new byte[8];
        
        int dataSize = packFXP8_8(data, voltage);
        sendMessage(CANJNI.LM_API_CFG_MAX_VOUT, data, dataSize);
        
        m_maxOutputVoltage = voltage;
        m_maxOutputVoltageVerified = false;
    }

    /**
     * Configure how long the Jaguar waits in the case of a fault before resuming operation.
     * 
     * Faults include over temerature, over current, and bus under voltage.
     * The default is 3.0 seconds, but can be reduced to as low as 0.5 seconds.
     * @param faultTime The time to wait before resuming operation, in seconds.
     */
    @objid ("7e3a6b4b-e60d-4ed9-a45a-2f9ff8b6fa2c")
    public void configFaultTime(float faultTime) {
        byte[] data = new byte[8];
        
        if(faultTime < 0.5f) faultTime = 0.5f;
        else if(faultTime > 3.0f) faultTime = 3.0f;
        
        int dataSize = packINT16(data, (short)(faultTime * 1000.0));
        sendMessage(CANJNI.LM_API_CFG_FAULT_TIME, data, dataSize);
        
        m_faultTime = faultTime;
        m_faultTimeVerified = false;
    }

    @objid ("f87f5fca-115a-4799-9c7b-ffc33c3b3f0f")
    static void sendMessageHelper(int messageID, byte[] data, int dataSize, int period) throws CANMessageNotFoundException {
        final int[] kTrustedMessages = {
                CANJNI.LM_API_VOLT_T_EN, CANJNI.LM_API_VOLT_T_SET, CANJNI.LM_API_SPD_T_EN, CANJNI.LM_API_SPD_T_SET,
                CANJNI.LM_API_VCOMP_T_EN, CANJNI.LM_API_VCOMP_T_SET, CANJNI.LM_API_POS_T_EN, CANJNI.LM_API_POS_T_SET,
                CANJNI.LM_API_ICTRL_T_EN, CANJNI.LM_API_ICTRL_T_SET
        };
        
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        status.asIntBuffer().put(0, 0);
        
        for(byte i = 0; i < kTrustedMessages.length; i++) {
            if((kFullMessageIDMask & messageID) == kTrustedMessages[i])
            {
                // Make sure the data will still fit after adjusting for the token.
                if (dataSize > kMaxMessageDataSize - 2) {
                    throw new RuntimeException("CAN message has too much data.");
                }
        
                ByteBuffer trustedBuffer = ByteBuffer.allocateDirect(dataSize+2);
                trustedBuffer.put(0, (byte)0);
                trustedBuffer.put(1, (byte)0);
        
                for(byte j = 0; j < dataSize; j++) {
                    trustedBuffer.put(j+2, data[j]);
                }
        
                CANJNI.FRCNetworkCommunicationCANSessionMuxSendMessage(messageID, trustedBuffer, period, status.asIntBuffer());
                int statusCode = status.asIntBuffer().get(0);
                if(statusCode < 0) {
                    CANExceptionFactory.checkStatus(statusCode, messageID);
                }
        
                return;
            }
        }
        
        // Use a null pointer for the data buffer if the given array is null
        ByteBuffer buffer;
        if(data != null) {
            buffer = ByteBuffer.allocateDirect(dataSize);
            for(byte i = 0; i < dataSize; i++) {
                buffer.put(i, data[i]);
            }
        } else {
            buffer = null;
        }
        
        CANJNI.FRCNetworkCommunicationCANSessionMuxSendMessage(messageID, buffer, period, status.asIntBuffer());
        
        int statusCode = status.asIntBuffer().get(0);
        if(statusCode < 0) {
            CANExceptionFactory.checkStatus(statusCode, messageID);
        }
    }

    /**
     * Send a message to the Jaguar.
     * @param messageID The messageID to be used on the CAN bus (device number
     * is added internally)
     * @param data The up to 8 bytes of data to be sent with the message
     * @param dataSize Specify how much of the data in "data" to send
     * @param period If positive, tell Network Communications to send the
     * message every "period" milliseconds.
     */
    @objid ("c85203fc-a32b-4385-af40-91ecdc6da717")
    protected void sendMessage(int messageID, byte[] data, int dataSize, int period) {
        sendMessageHelper(messageID | m_deviceNumber, data, dataSize, period);
    }

    /**
     * Send a message to the Jaguar, non-periodically
     * @param messageID The messageID to be used on the CAN bus (device number
     * is added internally)
     * @param data The up to 8 bytes of data to be sent with the message
     * @param dataSize Specify how much of the data in "data" to send
     */
    @objid ("6554c6c7-d241-4f3e-b509-03044dfba09d")
    protected void sendMessage(int messageID, byte[] data, int dataSize) {
        sendMessage(messageID, data, dataSize, CANJNI.CAN_SEND_PERIOD_NO_REPEAT);
    }

    /**
     * Request a message from the Jaguar, but don't wait for it to arrive.
     * @param messageID The message to request
     * @param period If positive, tell Network Communications to request the
     * message every "period" milliseconds.
     */
    @objid ("e54e446f-20af-4b3c-a106-61094f6cbed0")
    protected void requestMessage(int messageID, int period) {
        sendMessageHelper(messageID | m_deviceNumber, null, 0, period);
    }

    /**
     * Request a message from the Jaguar, but don't wait for it to arrive.
     * @param messageID The message to request
     */
    @objid ("13334be9-7762-4dd2-b5c0-04347904042c")
    protected void requestMessage(int messageID) {
        requestMessage(messageID, CANJNI.CAN_SEND_PERIOD_NO_REPEAT);
    }

    /**
     * Get a previously requested message.
     * 
     * Jaguar always generates a message with the same message ID when replying.
     * @param messageID The messageID to read from the CAN bus (device number is added internally)
     * @param data The up to 8 bytes of data that was received with the message
     */
    @objid ("12483ea6-ac6b-42fb-ba73-7cd0aa3ec27a")
    protected void getMessage(int messageID, int messageMask, byte[] data) throws CANMessageNotFoundException {
        messageID |= m_deviceNumber;
        messageID &= CANJNI.CAN_MSGID_FULL_M;
        
        ByteBuffer targetedMessageID = ByteBuffer.allocateDirect(4);
        targetedMessageID.order(ByteOrder.LITTLE_ENDIAN);
        targetedMessageID.asIntBuffer().put(0, messageID);
        
        ByteBuffer timeStamp = ByteBuffer.allocateDirect(4);
        
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        status.asIntBuffer().put(0, 0);
        
        // Get the data.
        ByteBuffer dataBuffer = CANJNI.FRCNetworkCommunicationCANSessionMuxReceiveMessage(
            targetedMessageID.asIntBuffer(),
            messageMask,
            timeStamp,
            status.asIntBuffer());
        
        if(data != null) {
            for(int i = 0; i < dataBuffer.capacity(); i++) {
                data[i] = dataBuffer.get(i);
            }
        }
        
        int statusCode = status.asIntBuffer().get(0);
        if(statusCode < 0) {
            CANExceptionFactory.checkStatus(statusCode, messageID);
        }
    }

    /**
     * Enables periodic status updates from the Jaguar
     */
    @objid ("ab3e2b73-e950-4b5b-b753-60fd5be97c2d")
    protected void setupPeriodicStatus() {
        byte[] data = new byte[8];
        int dataSize;
        
        // Message 0 returns bus voltage, output voltage, output current, and
        // temperature.
        final byte[] kMessage0Data = new byte[] {
            CANJNI.LM_PSTAT_VOLTBUS_B0, CANJNI.LM_PSTAT_VOLTBUS_B1,
            CANJNI.LM_PSTAT_VOLTOUT_B0, CANJNI.LM_PSTAT_VOLTOUT_B1,
            CANJNI.LM_PSTAT_CURRENT_B0, CANJNI.LM_PSTAT_CURRENT_B1,
            CANJNI.LM_PSTAT_TEMP_B0, CANJNI.LM_PSTAT_TEMP_B1
        };
        
        // Message 1 returns position and speed
        final byte[] kMessage1Data = new byte[] {
            CANJNI.LM_PSTAT_POS_B0, CANJNI.LM_PSTAT_POS_B1, CANJNI.LM_PSTAT_POS_B2, CANJNI.LM_PSTAT_POS_B3,
            CANJNI.LM_PSTAT_SPD_B0, CANJNI.LM_PSTAT_SPD_B1, CANJNI.LM_PSTAT_SPD_B2, CANJNI.LM_PSTAT_SPD_B3
        };
        
        // Message 2 returns limits and faults
        final byte[] kMessage2Data = new byte[] {
            CANJNI.LM_PSTAT_LIMIT_CLR,
            CANJNI.LM_PSTAT_FAULT,
            CANJNI.LM_PSTAT_END,
            (byte)0,
            (byte)0,
            (byte)0,
            (byte)0,
            (byte)0,
        };
        
        dataSize = packINT16(data, (short)(kSendMessagePeriod));
        sendMessage(CANJNI.LM_API_PSTAT_PER_EN_S0, data, dataSize);
        sendMessage(CANJNI.LM_API_PSTAT_PER_EN_S1, data, dataSize);
        sendMessage(CANJNI.LM_API_PSTAT_PER_EN_S2, data, dataSize);
        
        dataSize = 8;
        sendMessage(CANJNI.LM_API_PSTAT_CFG_S0, kMessage0Data, dataSize);
        sendMessage(CANJNI.LM_API_PSTAT_CFG_S1, kMessage1Data, dataSize);
        sendMessage(CANJNI.LM_API_PSTAT_CFG_S2, kMessage2Data, dataSize);
    }

    /**
     * Check for new periodic status updates and unpack them into local variables.
     */
    @objid ("a864f8c4-4ab0-4673-9666-9ea85aa11c46")
    protected void updatePeriodicStatus() {
        byte[] data = new byte[8];
        int dataSize;
        
        // Check if a new bus voltage/output voltage/current/temperature message
        // has arrived and unpack the values into the cached member variables
        try {
            getMessage(CANJNI.LM_API_PSTAT_DATA_S0, CANJNI.CAN_MSGID_FULL_M, data);
        
            m_busVoltage    = unpackFXP8_8(new byte[] { data[0], data[1] });
            m_outputVoltage = unpackPercentage(new byte[] { data[2], data[3] }) * m_busVoltage;
            m_outputCurrent = unpackFXP8_8(new byte[] { data[4], data[5] });
            m_temperature   = unpackFXP8_8(new byte[] { data[6], data[7] });
        
            m_receivedStatusMessage0 = true;
        } catch(CANMessageNotFoundException e) {}
        
        // Check if a new position/speed message has arrived and do the same
        try {
            getMessage(CANJNI.LM_API_PSTAT_DATA_S1, CANJNI.CAN_MSGID_FULL_M, data);
        
            m_position = unpackFXP16_16(new byte[] { data[0], data[1], data[2], data[3] });
            m_speed    = unpackFXP16_16(new byte[] { data[4], data[5], data[6], data[7] });
        
            m_receivedStatusMessage1 = true;
        } catch(CANMessageNotFoundException e) {}
        
        // Check if a new limits/faults message has arrived and do the same
        try {
            getMessage(CANJNI.LM_API_PSTAT_DATA_S2, CANJNI.CAN_MSGID_FULL_M, data);
            m_limits = data[0];
            m_faults = data[1];
        
            m_receivedStatusMessage2 = true;
        } catch(CANMessageNotFoundException e) {}
    }

    /**
     * Update all the motors that have pending sets in the syncGroup.
     * @param syncGroup A bitmask of groups to generate synchronous output.
     */
    @objid ("ace2b80c-c1d6-49b3-b296-2244ab0ae8e1")
    public static void updateSyncGroup(byte syncGroup) {
        byte[] data = new byte[8];
        
        data[0] = syncGroup;
        
        sendMessageHelper(CANJNI.CAN_MSGID_API_SYNC, data, 1, CANJNI.CAN_SEND_PERIOD_NO_REPEAT);
    }

/* we are on ARM-LE now, not Freescale so no need to swap */
    @objid ("e207f142-7f1e-4047-bc42-6ebee31969fe")
    private static final void swap16(int x, byte[] buffer) {
        buffer[0] = (byte)(x & 0xff);
        buffer[1] = (byte)((x>>8) & 0xff);
    }

    @objid ("58080ffa-cf3b-48cd-89a4-1f0498442921")
    private static final void swap32(int x, byte[] buffer) {
        buffer[0] = (byte)(x & 0xff);
        buffer[1] = (byte)((x>>8) & 0xff);
        buffer[2] = (byte)((x>>16) & 0xff);
        buffer[3] = (byte)((x>>24) & 0xff);
    }

    @objid ("eb118b09-03fb-4105-821f-5f8b20fa2828")
    private static final byte packPercentage(byte[] buffer, double value) {
        if(value < -1.0) value = -1.0;
        if(value > 1.0) value = 1.0;
        short intValue = (short) (value * 32767.0);
        swap16(intValue, buffer);
        return 2;
    }

    @objid ("201e8ca3-0b42-4178-81cc-c74c490d3602")
    private static final byte packFXP8_8(byte[] buffer, double value) {
        short intValue = (short) (value * 256.0);
        swap16(intValue, buffer);
        return 2;
    }

    @objid ("bde240ea-8a01-441f-920b-cf285676254f")
    private static final byte packFXP16_16(byte[] buffer, double value) {
        int intValue = (int) (value * 65536.0);
        swap32(intValue, buffer);
        return 4;
    }

    @objid ("1036a96e-f1ed-4870-b648-12fbd02bddf3")
    private static final byte packINT16(byte[] buffer, short value) {
        swap16(value, buffer);
        return 2;
    }

    @objid ("6633cec5-4b8b-4a50-9574-0148e8587b7d")
    private static final byte packINT32(byte[] buffer, int value) {
        swap32(value, buffer);
        return 4;
    }

    /**
     * Unpack 16-bit data from a buffer in little-endian byte order
     * @param buffer The buffer to unpack from
     * @param offset The offset into he buffer to unpack
     * @return The data that was unpacked
     */
    @objid ("223fe1fa-cf70-4134-a674-04247e0c0c74")
    private static final short unpack16(byte[] buffer, int offset) {
        return (short) ((buffer[offset] & 0xFF) | (short) ((buffer[offset + 1] << 8)) & 0xFF00);
    }

    /**
     * Unpack 32-bit data from a buffer in little-endian byte order
     * @param buffer The buffer to unpack from
     * @param offset The offset into he buffer to unpack
     * @return The data that was unpacked
     */
    @objid ("1cca11a1-7eb0-4170-a143-2661503da18e")
    private static final int unpack32(byte[] buffer, int offset) {
        return (buffer[offset] & 0xFF) | ((buffer[offset + 1] << 8) & 0xFF00) |
                    ((buffer[offset + 2] << 16) & 0xFF0000) | ((buffer[offset + 3] << 24) & 0xFF000000);
    }

    @objid ("0bae6ad9-01d3-4e12-8681-a55f1d1b4ff5")
    private static final double unpackPercentage(byte[] buffer) {
        return unpack16(buffer,0) / 32767.0;
    }

    @objid ("a19b88ba-721c-4b3f-8837-dd23f8d70ccb")
    private static final double unpackFXP8_8(byte[] buffer) {
        return unpack16(buffer,0) / 256.0;
    }

    @objid ("8e9c473e-c684-44c8-8e0b-c672a3354061")
    private static final double unpackFXP16_16(byte[] buffer) {
        return unpack32(buffer,0) / 65536.0;
    }

    @objid ("2d99f551-f497-4cf7-a440-dd450816b299")
    private static final short unpackINT16(byte[] buffer) {
        return unpack16(buffer,0);
    }

    @objid ("7d0c3d03-e3c4-43e9-b780-11cea6226c32")
    private static final int unpackINT32(byte[] buffer) {
        return unpack32(buffer,0);
    }

/* Compare floats for equality as fixed point numbers */
    @objid ("7886f2c1-ae31-4821-8d7a-ace4d3130bb1")
    public boolean FXP8_EQ(double a, double b) {
        return (int)(a * 256.0) == (int)(b * 256.0);
    }

/* Compare floats for equality as fixed point numbers */
    @objid ("0ead5456-03d3-40be-a753-c8da1d73116f")
    public boolean FXP16_EQ(double a, double b) {
        return (int)(a * 65536.0) == (int)(b * 65536.0);
    }

    @objid ("375a34fe-1929-4845-8b91-535587a14237")
    @Override
    public void setExpiration(double timeout) {
        m_safetyHelper.setExpiration(timeout);
    }

    @objid ("dfac972c-4df6-4d9c-8b87-f617a1172d4e")
    @Override
    public double getExpiration() {
        return m_safetyHelper.getExpiration();
    }

    @objid ("7905a63a-16a5-40fe-a4ad-1709bcac1b57")
    @Override
    public boolean isAlive() {
        return m_safetyHelper.isAlive();
    }

    @objid ("798e5473-f5c7-44c2-9815-86e7d03a8ff2")
    @Override
    public boolean isSafetyEnabled() {
        return m_safetyHelper.isSafetyEnabled();
    }

    @objid ("88f448a1-37b3-41d3-b17c-4f1620a09a4f")
    @Override
    public void setSafetyEnabled(boolean enabled) {
        m_safetyHelper.setSafetyEnabled(enabled);
    }

    @objid ("7b8d50f6-c1c5-492e-91b1-bb75f824ff14")
    @Override
    public String getDescription() {
        return "CANJaguar ID "+m_deviceNumber;
    }

    @objid ("a1758670-59a0-4c7e-be4d-6e4ea46fbc55")
    public int getDeviceID() {
        return (int)m_deviceNumber;
    }

    /**
     * Common interface for stopping a motor.
     * @deprecated Use disableControl instead.
     */
    @objid ("1b2d514f-7efb-44a3-913c-c30f44e32099")
    @Override
    @Deprecated
    public void stopMotor() {
        disableControl();
    }

/*
    * Live Window code, only does anything if live window is activated.
    */
    @objid ("d07ba90a-a9f4-486d-a7ea-5db4fb2cee55")
    @Override
    public String getSmartDashboardType() {
        return "Speed Controller";
    }

    /**
     * {@inheritDoc}
     */
    @objid ("d66d7d52-4727-44b1-bd5e-b2956bf04016")
    @Override
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @objid ("c51670c8-0e73-4f7b-9f37-0f824958be46")
    @Override
    public void updateTable() {
        if (m_table != null) {
            m_table.putNumber("Value", get());
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("a20bcc87-28eb-4048-b51d-46ee463b3dca")
    @Override
    public ITable getTable() {
        return m_table;
    }

    /**
     * {@inheritDoc}
     */
    @objid ("e6625cdd-5744-42a2-9dd2-f96fcc647c0c")
    @Override
    public void startLiveWindowMode() {
        set(0); // Stop for safety
        m_table_listener = new ITableListener() {
            @Override
            public void valueChanged(ITable itable, String key, Object value, boolean bln) {
                set(((Double) value).doubleValue());
            }
        };
        m_table.addTableListener("Value", m_table_listener, true);
    }

    /**
     * {@inheritDoc}
     */
    @objid ("e2856a44-6c1a-4d72-84c0-a6632ef90224")
    @Override
    public void stopLiveWindowMode() {
        set(0); // Stop for safety
        // TODO: Broken, should only remove the listener from "Value" only.
        m_table.removeTableListener(m_table_listener);
    }

// Control Mode tags
    @objid ("24de06c7-6049-454b-af78-21b84d96c759")
    private static class EncoderTag {
    }

    @objid ("121990b9-48e3-405a-9c60-9f7d31ef9569")
    private static class QuadEncoderTag {
    }

    @objid ("f96f3172-4546-4898-bb1e-a3a4dcb51460")
    private static class PotentiometerTag {
    }

    /**
     * Mode determines how the Jaguar is controlled, used internally.
     */
    @objid ("f25331ac-ab4d-4885-a825-2f56fead7ed7")
    public enum ControlMode {
        PercentVbus,
        Current,
        Speed,
        Position,
        Voltage;
    }

    /**
     * Determines how the Jaguar behaves when sending a zero signal.
     */
    @objid ("56c888cd-04aa-4d30-b3b9-5d214ab9928e")
    public enum NeutralMode {
        /**
         * Use the NeutralMode that is set by the jumper wire on the CAN device
         */
        Jumper ((byte)0),
        /**
         * Stop the motor's rotation by applying a force.
         */
        Brake ((byte)1),
        /**
         * Do not attempt to stop the motor. Instead allow it to coast to a stop without applying resistance.
         */
        Coast ((byte)2);

        @objid ("6dba565d-69d5-47cb-b359-bd9b519ec917")
        public byte value;

        @objid ("a3526cd5-6145-49ec-8ff0-e4ee8fe30aac")
        public static NeutralMode valueOf(byte value) {
            for(NeutralMode mode : values()) {
                if(mode.value == value) {
                    return mode;
                }
            }
            return null;
        }

        @objid ("c5ca57b4-b70a-4cb6-a7b8-2170fd1d20e7")
        private NeutralMode(byte value) {
            this.value = value;
        }

    }

    /**
     * Determines which sensor to use for position reference.
     * Limit switches will always be used to limit the rotation. This can not be disabled.
     */
    @objid ("b45341b5-9313-4961-b64f-cf067eb4a9c3")
    public enum LimitMode {
        /**
         * Disables the soft position limits and only uses the limit switches to limit rotation.
         * @see CANJaguar#getForwardLimitOK()
         * @see CANJaguar#getReverseLimitOK()
         */
        SwitchInputsOnly ((byte)0),
        /**
         * Enables the soft position limits on the Jaguar.
         * These will be used in addition to the limit switches. This does not disable the behavior
         * of the limit switch input.
         * @see CANJaguar#configSoftPositionLimits(double, double)
         */
        SoftPositionLimits ((byte)1);

        @objid ("6bff394e-681e-4de8-b8e2-e28d04ba3b77")
        public byte value;

        @objid ("4c6e779f-8560-42fe-95af-c7dcbd62b8f1")
        public static LimitMode valueOf(byte value) {
            for(LimitMode mode : values()) {
                if(mode.value == value) {
                    return mode;
                }
            }
            return null;
        }

        @objid ("d964d7b2-e3f6-440e-b79d-0bf3f78076aa")
        private LimitMode(byte value) {
            this.value = value;
        }

    }

}
