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

/**
 * Texas Instruments / Vex Robotics Jaguar Speed Controller as a PWM device.
 * @see CANJaguar CANJaguar for CAN control
 */
@objid ("3ba48f40-8f4a-4906-b751-c6f3125f78b7")
public class Jaguar extends SafePWM implements SpeedController {
    /**
     * Common initialization code called by all constructors.
     */
    @objid ("b0e41c86-9f5d-4d35-b69f-e802f17358d3")
    private void initJaguar() {
        /*
         * Input profile defined by Luminary Micro.
         *
         * Full reverse ranges from 0.671325ms to 0.6972211ms
         * Proportional reverse ranges from 0.6972211ms to 1.4482078ms
         * Neutral ranges from 1.4482078ms to 1.5517922ms
         * Proportional forward ranges from 1.5517922ms to 2.3027789ms
         * Full forward ranges from 2.3027789ms to 2.328675ms
         */
        setBounds(2.31, 1.55, 1.507, 1.454, .697);
        setPeriodMultiplier(PeriodMultiplier.k1X);
        setRaw(m_centerPwm);
        setZeroLatch();
        
        UsageReporting.report(tResourceType.kResourceType_Jaguar, getChannel());
        LiveWindow.addActuator("Jaguar", getChannel(), this);
    }

    /**
     * Constructor.
     * @param channel The PWM channel that the Jaguar is attached to. 0-9 are on-board, 10-19 are on the MXP port
     */
    @objid ("c096a12d-a7e3-4b70-aba1-2b9cb1a44f15")
    public Jaguar(final int channel) {
        super(channel);
        initJaguar();
    }

    /**
     * Set the PWM value.
     * @deprecated For compatibility with CANJaguar
     * 
     * The PWM value is set using a range of -1.0 to 1.0, appropriately
     * scaling the value for the FPGA.
     * @param speed The speed to set.  Value should be between -1.0 and 1.0.
     * @param syncGroup The update group to add this Set() to, pending UpdateSyncGroup().  If 0, update immediately.
     */
    @objid ("13b38e88-bbcd-4971-a510-f1f2e4f47494")
    @Deprecated
    @Override
    public void set(double speed, byte syncGroup) {
        setSpeed(speed);
        Feed();
    }

    /**
     * Set the PWM value.
     * 
     * The PWM value is set using a range of -1.0 to 1.0, appropriately
     * scaling the value for the FPGA.
     * @param speed The speed value between -1.0 and 1.0 to set.
     */
    @objid ("e7316f19-414c-451d-938b-90529a78165b")
    @Override
    public void set(double speed) {
        setSpeed(speed);
        Feed();
    }

    /**
     * Get the recently set value of the PWM.
     * @return The most recently set value for the PWM between -1.0 and 1.0.
     */
    @objid ("8d706c2f-e1fd-40ca-95ea-a78877d17730")
    @Override
    public double get() {
        return getSpeed();
    }

    /**
     * Write out the PID value as seen in the PIDOutput base object.
     * @param output Write out the PWM value as was found in the PIDController
     */
    @objid ("5f6842d3-a0a3-4c2a-98b6-251b2dc84009")
    @Override
    public void pidWrite(double output) {
        set(output);
    }

}
