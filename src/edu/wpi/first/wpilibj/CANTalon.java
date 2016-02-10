/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.hal.CanTalonJNI;
import edu.wpi.first.wpilibj.hal.CanTalonSRX;
import edu.wpi.first.wpilibj.hal.SWIGTYPE_p_CTR_Code;
import edu.wpi.first.wpilibj.hal.SWIGTYPE_p_double;
import edu.wpi.first.wpilibj.hal.SWIGTYPE_p_int;
import edu.wpi.first.wpilibj.hal.SWIGTYPE_p_uint32_t;

@objid ("7caf682c-4a3b-4c93-bdec-38881cd9b4dc")
public class CANTalon implements MotorSafety, PIDOutput, SpeedController {
    @objid ("596aa360-d7bc-4d9e-9b01-64b0c1425102")
    private CanTalonSRX m_impl;

    @objid ("127c21ff-8414-4e85-8092-7468646c7e41")
     ControlMode m_controlMode;

    @objid ("7e0ea3b9-eef6-4011-9f72-1de876762706")
    private static double kDelayForSolicitedSignals = 0.004;

    @objid ("301e8667-ad8a-46d7-a135-2925c0ddb808")
     int m_deviceNumber;

    @objid ("0ee61e24-2e7e-4448-a5f8-394b9a8daa16")
     boolean m_controlEnabled;

    @objid ("28f34570-78b9-49ba-99e9-a3ae5c20ee15")
     int m_profile;

    @objid ("35c51f2f-c990-4829-950e-919afdf31566")
     double m_setPoint;

    @objid ("b2f90e8f-44ee-4cf1-a1fe-08e2f15d0783")
    private MotorSafetyHelper m_safetyHelper;

    @objid ("3d699ee5-e16e-44d4-9f01-42be7930cc81")
    public CANTalon(int deviceNumber) {
        m_deviceNumber = deviceNumber;
        m_impl = new CanTalonSRX(deviceNumber);
        m_safetyHelper = new MotorSafetyHelper(this);
        m_controlEnabled = true;
        m_profile = 0;
        m_setPoint = 0;
        setProfile(m_profile);
        applyControlMode(ControlMode.PercentVbus);
    }

    @objid ("777bc663-73cc-4cc9-91d9-c3173c5c6dc6")
    public CANTalon(int deviceNumber, int controlPeriodMs) {
        m_deviceNumber = deviceNumber;
        m_impl = new CanTalonSRX(deviceNumber,controlPeriodMs); /* bound period to be within [1 ms,95 ms] */
        m_safetyHelper = new MotorSafetyHelper(this);
        m_controlEnabled = true;
        m_profile = 0;
        m_setPoint = 0;
        setProfile(m_profile);
        applyControlMode(ControlMode.PercentVbus);
    }

    /**
     * Set the output to the value calculated by PIDController
     * @param output the value calculated by PIDController
     */
    @objid ("47736bb5-97d7-4d31-8e9d-f556aad18608")
    @Override
    public void pidWrite(double output) {
        if (getControlMode() == ControlMode.PercentVbus) {
          set(output);
        }
        else {
                throw new IllegalStateException("PID only supported in PercentVbus mode");
        }
    }

    @objid ("503cd2fb-49b3-4632-abad-635cb3705108")
    public void delete() {
        m_impl.delete();
    }

    /**
     * Common interface for setting the speed of a speed controller.
     * @param speed The speed to set.  Value should be between -1.0 and 1.0.
     */
    @objid ("e1c351d2-2f54-4945-8cda-9613c8f2c1b2")
    public void set(double speed) {
        /* feed safety helper since caller just updated our output */
        m_safetyHelper.feed();
        if (m_controlEnabled) {
          m_setPoint = outputValue;
          switch (m_controlMode) {
            case PercentVbus:
              m_impl.Set(outputValue);
              break;
            case Follower:
              m_impl.SetDemand((int)outputValue);
              break;
            case Voltage:
              // Voltage is an 8.8 fixed point number.
              int volts = (int)(outputValue * 256);
              m_impl.SetDemand(volts);
              break;
            case Speed:
              m_impl.SetDemand((int)outputValue);
              break;
            case Position:
              m_impl.SetDemand((int)outputValue);
              break;
            default:
              break;
          }
          m_impl.SetModeSelect(m_controlMode.value);
        }
    }

    /**
     * Common interface for setting the speed of a speed controller.
     * @param speed The speed to set.  Value should be between -1.0 and 1.0.
     * @param syncGroup The update group to add this Set() to, pending UpdateSyncGroup().  If 0, update immediately.
     */
    @objid ("59ecafec-418a-4a13-a778-b13188b11d07")
    @Override
    public void set(double speed, byte syncGroup) {
        set(outputValue);
    }

    /**
     * Flips the sign (multiplies by negative one) the sensor values going into
     * the talon.
     * 
     * This only affects position and velocity closed loop control. Allows for
     * situations where you may have a sensor flipped and going in the wrong
     * direction.
     * @param flip True if sensor input should be flipped; False if not.
     */
    @objid ("d044459e-2426-4c60-b697-a9fe2875a7d2")
    public void reverseSensor(boolean flip) {
        m_impl.SetRevFeedbackSensor(flip ? 1 : 0);
    }

    /**
     * Flips the sign (multiplies by negative one) the throttle values going into
     * the motor on the talon in closed loop modes.
     * @param flip True if motor output should be flipped; False if not.
     */
    @objid ("8f017f95-493c-4da3-b1fd-ae1984a42865")
    public void reverseOutput(boolean flip) {
        m_impl.SetRevMotDuringCloseLoopEn(flip ? 1 : 0);
    }

    /**
     * Common interface for getting the current set speed of a speed controller.
     * @return The current set speed.  Value is between -1.0 and 1.0.
     */
    @objid ("4d082dae-3fe8-4376-8ee2-3ba0666ed136")
    public double get() {
        long valuep = CanTalonJNI.new_intp();
        SWIGTYPE_p_int swigp = new SWIGTYPE_p_int(valuep, true);
        switch (m_controlMode) {
          case Voltage:
            return getOutputVoltage();
          case Current:
            return getOutputCurrent();
          case Speed:
            m_impl.GetSensorVelocity(swigp);
            return (double)CanTalonJNI.intp_value(valuep);
          case Position:
            m_impl.GetSensorPosition(swigp);
            return (double)CanTalonJNI.intp_value(valuep);
          case PercentVbus:
          default:
            m_impl.GetAppliedThrottle(swigp);
            return (double)CanTalonJNI.intp_value(valuep) / 1023.0;
        }
    }

    /**
     * Get the current encoder position, regardless of whether it is the current feedback device.
     * @return The current position of the encoder.
     */
    @objid ("61f74f86-2af8-4442-87af-21f7059411b2")
    public int getEncPosition() {
        long valuep = CanTalonJNI.new_intp();
        SWIGTYPE_p_int swigp = new SWIGTYPE_p_int(valuep, true);
        m_impl.GetEncPosition(swigp);
        return CanTalonJNI.intp_value(valuep);
    }

    /**
     * Get the current encoder velocity, regardless of whether it is the current feedback device.
     * @return The current speed of the encoder.
     */
    @objid ("27cfd705-5742-403e-9b24-e5f042d984f3")
    public int getEncVelocity() {
        long valuep = CanTalonJNI.new_intp();
        SWIGTYPE_p_int swigp = new SWIGTYPE_p_int(valuep, true);
        m_impl.GetEncVel(swigp);
        return CanTalonJNI.intp_value(valuep);
    }

    /**
     * Get the number of of rising edges seen on the index pin.
     * @return number of rising edges on idx pin.
     */
    @objid ("48a92b39-57e9-4904-8f3d-4e0f647de87a")
    public int getNumberOfQuadIdxRises() {
        long valuep = CanTalonJNI.new_intp();
        SWIGTYPE_p_int swigp = new SWIGTYPE_p_int(valuep, true);
        m_impl.GetEncIndexRiseEvents(swigp);
        return CanTalonJNI.intp_value(valuep);
    }

    /**
     * @return IO level of QUADA pin.
     */
    @objid ("304d919b-62bc-4fd2-9ca7-d2c9ea67d761")
    public int getPinStateQuadA() {
        long valuep = CanTalonJNI.new_intp();
        SWIGTYPE_p_int swigp = new SWIGTYPE_p_int(valuep, true);
        m_impl.GetQuadApin(swigp);
        return CanTalonJNI.intp_value(valuep);
    }

    /**
     * @return IO level of QUADB pin.
     */
    @objid ("c0538109-e2d7-4276-8f80-30623ce6fad9")
    public int getPinStateQuadB() {
        long valuep = CanTalonJNI.new_intp();
        SWIGTYPE_p_int swigp = new SWIGTYPE_p_int(valuep, true);
        m_impl.GetQuadBpin(swigp);
        return CanTalonJNI.intp_value(valuep);
    }

    /**
     * @return IO level of QUAD Index pin.
     */
    @objid ("a256d63b-24db-4764-a7d8-44405f1b6bdf")
    public int getPinStateQuadIdx() {
        long valuep = CanTalonJNI.new_intp();
        SWIGTYPE_p_int swigp = new SWIGTYPE_p_int(valuep, true);
        m_impl.GetQuadIdxpin(swigp);
        return CanTalonJNI.intp_value(valuep);
    }

    /**
     * Get the current analog in position, regardless of whether it is the current
     * feedback device.
     * @return The 24bit analog position.  The bottom ten bits is the ADC (0 - 1023) on
     * the analog pin of the Talon. The upper 14 bits
     * tracks the overflows and underflows (continuous sensor).
     */
    @objid ("0f75aabd-9783-4066-a437-5f6f3fc7c949")
    public int getAnalogInPosition() {
        long valuep = CanTalonJNI.new_intp();
        SWIGTYPE_p_int swigp = new SWIGTYPE_p_int(valuep, true);
        m_impl.GetAnalogInWithOv(swigp);
        return CanTalonJNI.intp_value(valuep);
    }

    /**
     * Get the current analog in position, regardless of whether it is the current
     * feedback device.
     * @return The ADC (0 - 1023) on analog pin of the Talon.
     */
    @objid ("d6db7a15-b634-4ed5-8333-089cb1efb704")
    public int getAnalogInRaw() {
        return getAnalogInPosition() & 0x3FF;
    }

    /**
     * Get the current encoder velocity, regardless of whether it is the current
     * feedback device.
     * @return The current speed of the analog in device.
     */
    @objid ("9798c00c-0e2c-4b80-b589-57fed3089e8b")
    public int getAnalogInVelocity() {
        long valuep = CanTalonJNI.new_intp();
        SWIGTYPE_p_int swigp = new SWIGTYPE_p_int(valuep, true);
        m_impl.GetAnalogInVel(swigp);
        return CanTalonJNI.intp_value(valuep);
    }

    /**
     * Get the current difference between the setpoint and the sensor value.
     * @return The error, in whatever units are appropriate.
     */
    @objid ("86f75f95-245f-4431-a707-b8baa4477e54")
    public int getClosedLoopError() {
        long valuep = CanTalonJNI.new_intp();
        SWIGTYPE_p_int swigp = new SWIGTYPE_p_int(valuep, true);
        m_impl.GetCloseLoopErr(swigp);
        return CanTalonJNI.intp_value(valuep);
    }

// Returns true if limit switch is closed. false if open.
    @objid ("60475732-e2e9-4017-993a-1c601540e623")
    public boolean isFwdLimitSwitchClosed() {
        long valuep = CanTalonJNI.new_intp();
        SWIGTYPE_p_int swigp = new SWIGTYPE_p_int(valuep, true);
        m_impl.GetLimitSwitchClosedFor(swigp);
        return (CanTalonJNI.intp_value(valuep)==0) ? true : false;
    }

// Returns true if limit switch is closed. false if open.
    @objid ("65f86f39-477d-48f5-ab29-fc1707eca27a")
    public boolean isRevLimitSwitchClosed() {
        long valuep = CanTalonJNI.new_intp();
        SWIGTYPE_p_int swigp = new SWIGTYPE_p_int(valuep, true);
        m_impl.GetLimitSwitchClosedRev(swigp);
        return (CanTalonJNI.intp_value(valuep)==0) ? true : false;
    }

// Returns true if break is enabled during neutral. false if coast.
    @objid ("9ae2bcc1-5f67-43f9-b980-add3aadf960d")
    public boolean getBrakeEnableDuringNeutral() {
        long valuep = CanTalonJNI.new_intp();
        SWIGTYPE_p_int swigp = new SWIGTYPE_p_int(valuep, true);
        m_impl.GetBrakeIsEnabled(swigp);
        return (CanTalonJNI.intp_value(valuep)==0) ? false : true;
    }

    /**
     * Returns temperature of Talon, in degrees Celsius.
     */
    @objid ("3c5accee-ec6c-4701-a42e-cc7c734f6f84")
    public double getTemp() {
        long tempp = CanTalonJNI.new_doublep(); // Create a new swig pointer.
        m_impl.GetTemp(new SWIGTYPE_p_double(tempp, true));
        return CanTalonJNI.doublep_value(tempp);
    }

    /**
     * Returns the current going through the Talon, in Amperes.
     */
    @objid ("0b645ad2-fa3f-4710-984b-639a4bd981d7")
    public double getOutputCurrent() {
        long curp = CanTalonJNI.new_doublep(); // Create a new swig pointer.
        m_impl.GetCurrent(new SWIGTYPE_p_double(curp, true));
        return CanTalonJNI.doublep_value(curp);
    }

    /**
     * @return The voltage being output by the Talon, in Volts.
     */
    @objid ("f72ce745-f611-4164-9cb0-19f3e4d0234b")
    public double getOutputVoltage() {
        long throttlep = CanTalonJNI.new_intp();
        m_impl.GetAppliedThrottle(new SWIGTYPE_p_int(throttlep, true));
        double voltage = getBusVoltage() * (double)CanTalonJNI.intp_value(throttlep) / 1023.0;
        return voltage;
    }

    /**
     * @return The voltage at the battery terminals of the Talon, in Volts.
     */
    @objid ("413bfaa0-3202-4c19-b79e-77022f74e5b4")
    public double getBusVoltage() {
        long voltagep = CanTalonJNI.new_doublep();
        SWIGTYPE_p_CTR_Code status = m_impl.GetBatteryV(new SWIGTYPE_p_double(voltagep, true));
        /* Note: This section needs the JNI bindings regenerated with
         pointer_functions for CTR_Code included in order to be able to catch notice
         and throw errors.
         if (CanTalonJNI.CTR_Codep_value(status) != 0) {
           // TODO throw an error.
         }*/
        return CanTalonJNI.doublep_value(voltagep);
    }

    /**
     * TODO documentation (see CANJaguar.java)
     * @return The position of the sensor currently providing feedback.
     * When using analog sensors, 0 units corresponds to 0V, 1023 units corresponds to 3.3V
     * When using an analog encoder (wrapping around 1023 to 0 is possible) the units are still 3.3V per 1023 units.
     * When using quadrature, each unit is a quadrature edge (4X) mode.
     */
    @objid ("32f80245-dd77-42f4-8e47-0d94e1774439")
    public double getPosition() {
        long positionp = CanTalonJNI.new_intp();
        m_impl.GetSensorPosition(new SWIGTYPE_p_int(positionp, true));
        return CanTalonJNI.intp_value(positionp);
    }

    @objid ("c4e77193-2f5b-4a38-bba0-f4bd3ae0509e")
    public void setPosition(double pos) {
        m_impl.SetSensorPosition((int)pos);
    }

    /**
     * TODO documentation (see CANJaguar.java)
     * @return The speed of the sensor currently providing feedback.
     * 
     * The speed units will be in the sensor's native ticks per 100ms.
     * 
     * For analog sensors, 3.3V corresponds to 1023 units.
     * So a speed of 200 equates to ~0.645 dV per 100ms or 6.451 dV per second.
     * If this is an analog encoder, that likely means 1.9548 rotations per sec.
     * For quadrature encoders, each unit corresponds a quadrature edge (4X).
     * So a 250 count encoder will produce 1000 edge events per rotation.
     * An example speed of 200 would then equate to 20% of a rotation per 100ms,
     * or 10 rotations per second.
     */
    @objid ("79a8a541-dc51-4b13-ba0c-015004beddf3")
    public double getSpeed() {
        long speedp = CanTalonJNI.new_intp();
        m_impl.GetSensorVelocity(new SWIGTYPE_p_int(speedp, true));
        return CanTalonJNI.intp_value(speedp);
    }

    @objid ("9c4a782e-cded-4ebc-8c92-cebf6205312f")
    public ControlMode getControlMode() {
        return m_controlMode;
    }

    /**
     * Fixup the m_controlMode so set() serializes the correct demand value.
     * Also fills the modeSelecet in the control frame to disabled.
     * @see #set
     * @param controlMode Control mode to ultimately enter once user calls set().
     */
    @objid ("7d354efa-047e-462d-af0a-ad87debc5b00")
    private void applyControlMode(ControlMode controlMode) {
        m_controlMode = controlMode;
        if (controlMode == ControlMode.Disabled)
          m_controlEnabled = false;
        // Disable until set() is called.
        m_impl.SetModeSelect(ControlMode.Disabled.value);
    }

    @objid ("309b8438-9f4d-4f8e-9732-0f84a76d0b6d")
    public void changeControlMode(ControlMode controlMode) {
        if(m_controlMode == controlMode){
          /* we already are in this mode, don't perform disable workaround */
        }else{
          applyControlMode(controlMode);
        }
    }

    @objid ("de3ed5a2-0962-4d1e-863f-cae001f7c2fc")
    public void setFeedbackDevice(FeedbackDevice device) {
        m_impl.SetFeedbackDeviceSelect(device.value);
    }

    @objid ("3f334456-cea2-4b1f-b2e1-cf79bb5a2814")
    public void setStatusFrameRateMs(StatusFrameRate stateFrame, int periodMs) {
        m_impl.SetStatusFrameRate(stateFrame.value,periodMs);
    }

    @objid ("86667739-0831-4b38-bcbc-92bd8149e2a2")
    public void enableControl() {
        changeControlMode(m_controlMode);
            m_controlEnabled = true;
    }

    @objid ("5d249b55-8471-4a25-bd87-513fb96d6ec7")
    public void disableControl() {
        m_impl.SetModeSelect(ControlMode.Disabled.value);
            m_controlEnabled = false;
    }

    @objid ("058b439c-40f4-4f4e-b2c6-a33d74de5dd2")
    public boolean isControlEnabled() {
        return m_controlEnabled;
    }

    /**
     * Get the current proportional constant.
     * @return double proportional constant for current profile.
     */
    @objid ("d2726a3b-a00a-4b19-b2d3-20ab0f1112a0")
    public double getP() {
        //if(!(m_controlMode.equals(ControlMode.Position) || m_controlMode.equals(ControlMode.Speed))) {
        //    throw new IllegalStateException("PID mode only applies in Position and Speed modes.");
        //}
        
            // Update the information that we have.
            if (m_profile == 0)
              m_impl.RequestParam(CanTalonSRX.param_t.eProfileParamSlot0_P);
            else
              m_impl.RequestParam(CanTalonSRX.param_t.eProfileParamSlot1_P);
        
            // Briefly wait for new values from the Talon.
            Timer.delay(kDelayForSolicitedSignals);
        
            long pp = CanTalonJNI.new_doublep();
            m_impl.GetPgain(m_profile, new SWIGTYPE_p_double(pp, true));
        return CanTalonJNI.doublep_value(pp);
    }

    @objid ("0c6d05cc-6439-4cd8-88fd-5c419625e328")
    public double getI() {
        //if(!(m_controlMode.equals(ControlMode.Position) || m_controlMode.equals(ControlMode.Speed))) {
        //    throw new IllegalStateException("PID mode only applies in Position and Speed modes.");
        //}
        
            // Update the information that we have.
            if (m_profile == 0)
              m_impl.RequestParam(CanTalonSRX.param_t.eProfileParamSlot0_I);
            else
              m_impl.RequestParam(CanTalonSRX.param_t.eProfileParamSlot1_I);
        
            // Briefly wait for new values from the Talon.
            Timer.delay(kDelayForSolicitedSignals);
        
            long ip = CanTalonJNI.new_doublep();
            m_impl.GetIgain(m_profile, new SWIGTYPE_p_double(ip, true));
        return CanTalonJNI.doublep_value(ip);
    }

    @objid ("c029982d-aa4b-4b11-a182-2f737f07c4f5")
    public double getD() {
        //if(!(m_controlMode.equals(ControlMode.Position) || m_controlMode.equals(ControlMode.Speed))) {
        //    throw new IllegalStateException("PID mode only applies in Position and Speed modes.");
        //}
        
            // Update the information that we have.
            if (m_profile == 0)
              m_impl.RequestParam(CanTalonSRX.param_t.eProfileParamSlot0_D);
            else
              m_impl.RequestParam(CanTalonSRX.param_t.eProfileParamSlot1_D);
        
            // Briefly wait for new values from the Talon.
            Timer.delay(kDelayForSolicitedSignals);
        
            long dp = CanTalonJNI.new_doublep();
            m_impl.GetDgain(m_profile, new SWIGTYPE_p_double(dp, true));
        return CanTalonJNI.doublep_value(dp);
    }

    @objid ("05efb87d-ae1c-478f-bb4b-784aa4aa9957")
    public double getF() {
        //if(!(m_controlMode.equals(ControlMode.Position) || m_controlMode.equals(ControlMode.Speed))) {
        //    throw new IllegalStateException("PID mode only applies in Position and Speed modes.");
        //}
        
            // Update the information that we have.
            if (m_profile == 0)
              m_impl.RequestParam(CanTalonSRX.param_t.eProfileParamSlot0_F);
            else
              m_impl.RequestParam(CanTalonSRX.param_t.eProfileParamSlot1_F);
        
            // Briefly wait for new values from the Talon.
            Timer.delay(kDelayForSolicitedSignals);
        
            long fp = CanTalonJNI.new_doublep();
            m_impl.GetFgain(m_profile, new SWIGTYPE_p_double(fp, true));
        return CanTalonJNI.doublep_value(fp);
    }

    @objid ("a083e9b7-3b57-4315-862c-8d861013f0b6")
    public double getIZone() {
        //if(!(m_controlMode.equals(ControlMode.Position) || m_controlMode.equals(ControlMode.Speed))) {
        //    throw new IllegalStateException("PID mode only applies in Position and Speed modes.");
        //}
        
            // Update the information that we have.
            if (m_profile == 0)
              m_impl.RequestParam(CanTalonSRX.param_t.eProfileParamSlot0_IZone);
            else
              m_impl.RequestParam(CanTalonSRX.param_t.eProfileParamSlot1_IZone);
        
            // Briefly wait for new values from the Talon.
            Timer.delay(kDelayForSolicitedSignals);
        
            long fp = CanTalonJNI.new_intp();
            m_impl.GetIzone(m_profile, new SWIGTYPE_p_int(fp, true));
        return CanTalonJNI.intp_value(fp);
    }

    /**
     * Get the closed loop ramp rate for the current profile.
     * 
     * Limits the rate at which the throttle will change.
     * Only affects position and speed closed loop modes.
     * @see #setProfile For selecting a certain profile.
     * @return rampRate Maximum change in voltage, in volts / sec.
     */
    @objid ("c6a39ea9-70e9-487a-8a9d-77538704e3b9")
    public double getCloseLoopRampRate() {
        //    if(!(m_controlMode.equals(ControlMode.Position) || m_controlMode.equals(ControlMode.Speed))) {
        //        throw new IllegalStateException("PID mode only applies in Position and Speed modes.");
        //    }
        
        // Update the information that we have.
        if (m_profile == 0)
          m_impl.RequestParam(CanTalonSRX.param_t.eProfileParamSlot0_CloseLoopRampRate);
        else
          m_impl.RequestParam(CanTalonSRX.param_t.eProfileParamSlot1_CloseLoopRampRate);
        
        // Briefly wait for new values from the Talon.
        Timer.delay(kDelayForSolicitedSignals);
        
        long fp = CanTalonJNI.new_intp();
        m_impl.GetCloseLoopRampRate(m_profile, new SWIGTYPE_p_int(fp, true));
        double throttlePerMs = CanTalonJNI.intp_value(fp);
        return throttlePerMs / 1023.0 * 12.0 * 1000.0;
    }

    /**
     * @return The version of the firmware running on the Talon
     */
    @objid ("93720a29-d465-41f9-924e-587b08348ee7")
    public long GetFirmwareVersion() {
        // Update the information that we have.
        m_impl.RequestParam(CanTalonSRX.param_t.eFirmVers);
        
        // Briefly wait for new values from the Talon.
        Timer.delay(kDelayForSolicitedSignals);
        
        long fp = CanTalonJNI.new_intp();
        m_impl.GetParamResponseInt32(CanTalonSRX.param_t.eFirmVers, new SWIGTYPE_p_int(fp, true));
        return CanTalonJNI.intp_value(fp);
    }

    @objid ("0c0e7c83-7cc0-45bd-97dc-1609d2cadf7f")
    public long GetIaccum() {
        // Update the information that we have.
        m_impl.RequestParam(CanTalonSRX.param_t.ePidIaccum);
        
        // Briefly wait for new values from the Talon.
        Timer.delay(kDelayForSolicitedSignals);
        
        long fp = CanTalonJNI.new_intp();
        m_impl.GetParamResponseInt32(CanTalonSRX.param_t.ePidIaccum, new SWIGTYPE_p_int(fp, true));
        return CanTalonJNI.intp_value(fp);
    }

    /**
     * Clear the accumulator for I gain.
     */
    @objid ("cd5b16e6-7e3d-4b24-95c0-4e278662f724")
    public void ClearIaccum() {
        SWIGTYPE_p_CTR_Code status = m_impl.SetParam(CanTalonSRX.param_t.ePidIaccum, 0);
    }

    /**
     * Set the proportional value of the currently selected profile.
     * @see #setProfile For selecting a certain profile.
     * @param p Proportional constant for the currently selected PID profile.
     */
    @objid ("635ae0ec-3e66-407d-afbf-a29e59af9876")
    public void setP(double p) {
        m_impl.SetPgain(m_profile, p);
    }

    /**
     * Set the integration constant of the currently selected profile.
     * @see #setProfile For selecting a certain profile.
     * @param i Integration constant for the currently selected PID profile.
     */
    @objid ("05b50f28-5fa5-4827-997c-6ab9c1417d4a")
    public void setI(double i) {
        m_impl.SetIgain(m_profile, i);
    }

    /**
     * Set the derivative constant of the currently selected profile.
     * @see #setProfile For selecting a certain profile.
     * @param d Derivative constant for the currently selected PID profile.
     */
    @objid ("e4d7a5a8-7911-4dbe-82e8-022d8b5c4642")
    public void setD(double d) {
        m_impl.SetDgain(m_profile, d);
    }

    /**
     * Set the feedforward value of the currently selected profile.
     * @see #setProfile For selecting a certain profile.
     * @param f Feedforward constant for the currently selected PID profile.
     */
    @objid ("d11ac0ab-5531-46d7-89d9-a510425e5c9f")
    public void setF(double f) {
        m_impl.SetFgain(m_profile, f);
    }

    /**
     * Set the integration zone of the current Closed Loop profile.
     * 
     * Whenever the error is larger than the izone value, the accumulated
     * integration error is cleared so that high errors aren't racked up when at
     * high errors.
     * An izone value of 0 means no difference from a standard PIDF loop.
     * @see #setProfile For selecting a certain profile.
     * @param izone Width of the integration zone.
     */
    @objid ("ea56baec-f6a1-4a7f-80a8-7b473cca8994")
    public void setIZone(int izone) {
        m_impl.SetIzone(m_profile, izone);
    }

    /**
     * Set the closed loop ramp rate for the current profile.
     * 
     * Limits the rate at which the throttle will change.
     * Only affects position and speed closed loop modes.
     * @see #setProfile For selecting a certain profile.
     * @param rampRate Maximum change in voltage, in volts / sec.
     */
    @objid ("86024612-b587-42e7-8fcc-2e41b8efcac4")
    public void setCloseLoopRampRate(double rampRate) {
        // CanTalonSRX takes units of Throttle (0 - 1023) / 1ms.
        int rate = (int) (rampRate * 1023.0 / 12.0 / 1000.0);
        m_impl.SetCloseLoopRampRate(m_profile, rate);
    }

    /**
     * Set the voltage ramp rate for the current profile.
     * 
     * Limits the rate at which the throttle will change.
     * Affects all modes.
     * @param rampRate Maximum change in voltage, in volts / sec.
     */
    @objid ("280030e4-72ce-47c3-a93c-eafa9ce8f971")
    public void setVoltageRampRate(double rampRate) {
        // CanTalonSRX takes units of Throttle (0 - 1023) / 10ms.
        int rate = (int) (rampRate * 1023.0 / 12.0 /100.0);
        m_impl.SetRampThrottle(rate);
    }

    /**
     * Sets control values for closed loop control.
     * @param p Proportional constant.
     * @param i Integration constant.
     * @param d Differential constant.
     * @param f Feedforward constant.
     * @param izone Integration zone -- prevents accumulation of integration error
     * with large errors. Setting this to zero will ignore any izone stuff.
     * @param closeLoopRampRate Closed loop ramp rate. Maximum change in voltage, in volts / sec.
     * @param profile which profile to set the pid constants for. You can have two
     * profiles, with values of 0 or 1, allowing you to keep a second set of values
     * on hand in the talon. In order to switch profiles without recalling setPID,
     * you must call setProfile().
     */
    @objid ("d178d75a-048a-4a38-abcc-8545b7d8537e")
    public void setPID(double p, double i, double d, double f, int izone, double closeLoopRampRate, int profile) {
        if (profile != 0 && profile != 1)
          throw new IllegalArgumentException("Talon PID profile must be 0 or 1.");
        m_profile = profile;
        setProfile(profile);
        setP(p);
        setI(i);
        setD(d);
        setF(f);
        setIZone(izone);
        setCloseLoopRampRate(closeLoopRampRate);
    }

    @objid ("ce7cac06-342f-4c27-a669-9876b95149f2")
    public void setPID(double p, double i, double d) {
        setPID(p, i, d, 0, 0, 0, m_profile);
    }

    /**
     * @return The latest value set using set().
     */
    @objid ("122d7bcf-b422-4efe-8e44-38963fdcd6d4")
    public double getSetpoint() {
        return m_setPoint;
    }

    /**
     * Select which closed loop profile to use, and uses whatever PIDF gains and
     * the such that are already there.
     */
    @objid ("49fe0b3d-be1a-43e2-b0ef-a7b4e0e0a3f4")
    public void setProfile(int profile) {
        if (profile != 0 && profile != 1)
          throw new IllegalArgumentException("Talon PID profile must be 0 or 1.");
        m_profile = profile;
        m_impl.SetProfileSlotSelect(m_profile);
    }

    /**
     * Common interface for stopping a motor.
     * @deprecated Use disableControl instead.
     */
    @objid ("051d049e-9457-46a7-b4dc-7b27141450c7")
    @Override
    @Deprecated
    public void stopMotor() {
        disableControl();
    }

    /**
     * Disable the speed controller
     */
    @objid ("fe0a83d6-49d7-415d-b709-ec6d686d35a8")
    @Override
    public void disable() {
        disableControl();
    }

    @objid ("482358c6-34e8-49fa-adcd-0cbe56d4c8f8")
    public int getDeviceID() {
        return m_deviceNumber;
    }

// TODO: Documentation for all these accessors/setters for misc. stuff.
    @objid ("89784c3d-458b-4813-8571-60f8f008df8b")
    public void setForwardSoftLimit(int forwardLimit) {
        m_impl.SetForwardSoftLimit(forwardLimit);
    }

    @objid ("272b9e1a-9813-462c-9008-ea18cd5c4433")
    public void enableForwardSoftLimit(boolean enable) {
        m_impl.SetForwardSoftEnable(enable ? 1 : 0);
    }

    @objid ("aec97c64-8a8b-4d3f-877c-6c5bee2b9642")
    public void setReverseSoftLimit(int reverseLimit) {
        m_impl.SetReverseSoftLimit(reverseLimit);
    }

    @objid ("6fb3d37c-3d4b-432f-a639-7fe39d6cdf87")
    public void enableReverseSoftLimit(boolean enable) {
        m_impl.SetReverseSoftEnable(enable ? 1 : 0);
    }

    @objid ("28027f6c-97ff-4b96-92f4-8c90809ee0b2")
    public void clearStickyFaults() {
        m_impl.ClearStickyFaults();
    }

    @objid ("6fb64939-4d2c-4170-8864-365493fd18ff")
    public void enableLimitSwitch(boolean forward, boolean reverse) {
        int mask = 4 + (forward ? 1 : 0) * 2 + (reverse ? 1 : 0);
        m_impl.SetOverrideLimitSwitchEn(mask);
    }

    /**
     * Configure the fwd limit switch to be normally open or normally closed.
     * Talon will disable momentarilly if the Talon's current setting
     * is dissimilar to the caller's requested setting.
     * 
     * Since Talon saves setting to flash this should only affect
     * a given Talon initially during robot install.
     * @param normallyOpen true for normally open.  false for normally closed.
     */
    @objid ("39760b52-2d8b-4b12-92bd-b80bfa4a01b8")
    public void ConfigFwdLimitSwitchNormallyOpen(boolean normallyOpen) {
        SWIGTYPE_p_CTR_Code status = m_impl.SetParam(CanTalonSRX.param_t.eOnBoot_LimitSwitch_Forward_NormallyClosed,normallyOpen ? 0 : 1);
    }

    /**
     * Configure the rev limit switch to be normally open or normally closed.
     * Talon will disable momentarilly if the Talon's current setting
     * is dissimilar to the caller's requested setting.
     * 
     * Since Talon saves setting to flash this should only affect
     * a given Talon initially during robot install.
     * @param normallyOpen true for normally open.  false for normally closed.
     */
    @objid ("b1bd857f-d235-4a3f-aa4d-25d46e393554")
    public void ConfigRevLimitSwitchNormallyOpen(boolean normallyOpen) {
        SWIGTYPE_p_CTR_Code status = m_impl.SetParam(CanTalonSRX.param_t.eOnBoot_LimitSwitch_Reverse_NormallyClosed,normallyOpen ? 0 : 1);
    }

    @objid ("9ad1eab1-0be2-4265-a42e-a2c361e7be8f")
    public void enableBrakeMode(boolean brake) {
        m_impl.SetOverrideBrakeType(brake ? 2 : 1);
    }

    @objid ("ec6ade2e-d694-47dd-a6be-af89266cf060")
    public int getFaultOverTemp() {
        long valuep = CanTalonJNI.new_intp();
        m_impl.GetFault_OverTemp(new SWIGTYPE_p_int(valuep, true));
        return CanTalonJNI.intp_value(valuep);
    }

    @objid ("83d44742-51d7-4487-8e2b-80856d38cd1b")
    public int getFaultUnderVoltage() {
        long valuep = CanTalonJNI.new_intp();
        m_impl.GetFault_UnderVoltage(new SWIGTYPE_p_int(valuep, true));
        return CanTalonJNI.intp_value(valuep);
    }

    @objid ("092be32f-e9be-4e07-acae-74cb964e3b1e")
    public int getFaultForLim() {
        long valuep = CanTalonJNI.new_intp();
        m_impl.GetFault_ForLim(new SWIGTYPE_p_int(valuep, true));
        return CanTalonJNI.intp_value(valuep);
    }

    @objid ("9c93529b-c524-40ee-aef5-1a9b847933e2")
    public int getFaultRevLim() {
        long valuep = CanTalonJNI.new_intp();
        m_impl.GetFault_RevLim(new SWIGTYPE_p_int(valuep, true));
        return CanTalonJNI.intp_value(valuep);
    }

    @objid ("68a2584f-7dbc-4137-aae4-fd3e9cddd1d9")
    public int getFaultHardwareFailure() {
        long valuep = CanTalonJNI.new_intp();
        m_impl.GetFault_HardwareFailure(new SWIGTYPE_p_int(valuep, true));
        return CanTalonJNI.intp_value(valuep);
    }

    @objid ("1ebcc0de-9dbf-4da5-b67b-c6003a4e9623")
    public int getFaultForSoftLim() {
        long valuep = CanTalonJNI.new_intp();
        m_impl.GetFault_ForSoftLim(new SWIGTYPE_p_int(valuep, true));
        return CanTalonJNI.intp_value(valuep);
    }

    @objid ("6cf417bb-3dbe-470e-9026-42507df93ffe")
    public int getFaultRevSoftLim() {
        long valuep = CanTalonJNI.new_intp();
        m_impl.GetFault_RevSoftLim(new SWIGTYPE_p_int(valuep, true));
        return CanTalonJNI.intp_value(valuep);
    }

    @objid ("5eec2ff1-4082-4068-9f73-5a13d6de6466")
    public int getStickyFaultOverTemp() {
        long valuep = CanTalonJNI.new_intp();
        m_impl.GetStckyFault_OverTemp(new SWIGTYPE_p_int(valuep, true));
        return CanTalonJNI.intp_value(valuep);
    }

    @objid ("ceceaf9e-684c-4850-88d6-b8d2f2a0d4f5")
    public int getStickyFaultUnderVoltage() {
        long valuep = CanTalonJNI.new_intp();
        m_impl.GetStckyFault_UnderVoltage(new SWIGTYPE_p_int(valuep, true));
        return CanTalonJNI.intp_value(valuep);
    }

    @objid ("1ceaf9f9-59ea-43f1-9268-596caf1011bf")
    public int getStickyFaultForLim() {
        long valuep = CanTalonJNI.new_intp();
        m_impl.GetStckyFault_ForLim(new SWIGTYPE_p_int(valuep, true));
        return CanTalonJNI.intp_value(valuep);
    }

    @objid ("c4546010-8675-4d41-b221-a5c278bb408c")
    public int getStickyFaultRevLim() {
        long valuep = CanTalonJNI.new_intp();
        m_impl.GetStckyFault_RevLim(new SWIGTYPE_p_int(valuep, true));
        return CanTalonJNI.intp_value(valuep);
    }

    @objid ("742a9c0b-a594-4a10-98d1-718ca6fc7231")
    public int getStickyFaultForSoftLim() {
        long valuep = CanTalonJNI.new_intp();
        m_impl.GetStckyFault_ForSoftLim(new SWIGTYPE_p_int(valuep, true));
        return CanTalonJNI.intp_value(valuep);
    }

    @objid ("f9e03917-afb0-44d9-b815-b7456610ff5b")
    public int getStickyFaultRevSoftLim() {
        long valuep = CanTalonJNI.new_intp();
        m_impl.GetStckyFault_RevSoftLim(new SWIGTYPE_p_int(valuep, true));
        return CanTalonJNI.intp_value(valuep);
    }

    @objid ("79f6b9e1-e697-46a1-9514-4259cd69caaf")
    @Override
    public void setExpiration(double timeout) {
        m_safetyHelper.setExpiration(timeout);
    }

    @objid ("3112269c-a892-4398-828a-0bbcfc39b0bd")
    @Override
    public double getExpiration() {
        return m_safetyHelper.getExpiration();
    }

    @objid ("ac7e8a93-17eb-4bcb-876a-9141f4f699d7")
    @Override
    public boolean isAlive() {
        return m_safetyHelper.isAlive();
    }

    @objid ("faa199dc-d343-44cc-9b26-02886f0700cd")
    @Override
    public boolean isSafetyEnabled() {
        return m_safetyHelper.isSafetyEnabled();
    }

    @objid ("f5dd0587-d60f-490d-bc53-643f14cc7ec3")
    @Override
    public void setSafetyEnabled(boolean enabled) {
        m_safetyHelper.setSafetyEnabled(enabled);
    }

    @objid ("6d07d4d6-3457-4660-a269-73dfb596eca5")
    @Override
    public String getDescription() {
        return "CANJaguar ID "+m_deviceNumber;
    }

    @objid ("f05ae2db-9b1b-4712-9146-3f72b9850261")
    public String getDescription() {
    }

    @objid ("cce881a7-c0e7-4286-be36-04da46fad6da")
    public enum FeedbackDevice {
        QuadEncoder (0),
        AnalogPot (2),
        AnalogEncoder (3),
        EncRising (4),
        EncFalling (5);

        @objid ("bd0e7026-2001-4034-a050-a4030528a8db")
        public int value;

        @objid ("a5737b3d-cd56-44bd-bb08-498d9a246111")
        public static FeedbackDevice valueOf(int value) {
            for(FeedbackDevice mode : values()) {
                if(mode.value == value) {
                    return mode;
                }
            }
            return null;
        }

        @objid ("a2af62f0-2d55-44b9-98f1-c703c35770d5")
        private FeedbackDevice(int value) {
            this.value = value;
        }

    }

    /**
     * enumerated types for frame rate ms
     */
    @objid ("c8e684dc-529a-4ff4-a451-41a1ee2af624")
    public enum StatusFrameRate {
        General (0),
        Feedback (1),
        QuadEncoder (2),
        AnalogTempVbat (3);

        @objid ("b0fac64a-8263-4f26-a8e1-931cad796368")
        public int value;

        @objid ("35922d6d-ee6c-4546-ba48-a562d34bb925")
        public static StatusFrameRate valueOf(int value) {
            for(StatusFrameRate mode : values()) {
                if(mode.value == value) {
                    return mode;
                }
            }
            return null;
        }

        @objid ("e8bdce0c-a8f5-4499-86ad-df696656c900")
        private StatusFrameRate(int value) {
            this.value = value;
        }

    }

    @objid ("1158c2f9-943f-433e-a180-e6fe47365ec9")
    public enum ControlMode {
        PercentVbus (0),
        Follower (5),
        Voltage (4),
        Position (1),
        Speed (2),
        Current (3),
        Disabled (15);

        @objid ("b1c5acad-6ae0-48be-9178-1a1ea45b7d07")
        public int value;

        @objid ("3978ffb5-75f0-46c2-965a-2437bab01d02")
        public static ControlMode valueOf(int value) {
            for(ControlMode mode : values()) {
                if(mode.value == value) {
                    return mode;
                }
            }
            return null;
        }

        @objid ("a57b14b9-eb50-46ff-a200-9f2da0bd77ce")
        private ControlMode(int value) {
            this.value = value;
        }

    }

}