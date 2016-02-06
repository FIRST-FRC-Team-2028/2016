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
 * VEX Robotics Victor SP Speed Controller
 */
@objid ("b401fc3f-157b-472b-994f-f60ac25715ca")
public class VictorSP extends SafePWM implements SpeedController {
    /**
     * Common initialization code called by all constructors.
     * 
     * Note that the VictorSP uses the following bounds for PWM values. These values should work reasonably well for
     * most controllers, but if users experience issues such as asymmetric behavior around
     * the deadband or inability to saturate the controller in either direction, calibration is recommended.
     * The calibration procedure can be found in the VictorSP User Manual available from CTRE.
     * 
     * - 2.004ms = full "forward"
     * - 1.52ms = the "high end" of the deadband range
     * - 1.50ms = center of the deadband range (off)
     * - 1.48ms = the "low end" of the deadband range
     * - .997ms = full "reverse"
     */
    @objid ("f71174bd-0e4d-4e0c-965a-3203fe3b70d3")
    protected void initVictorSP() {
        setBounds(2.004, 1.52, 1.50, 1.48, .997);
        setPeriodMultiplier(PeriodMultiplier.k1X);
        setRaw(m_centerPwm);
        setZeroLatch();
        
        LiveWindow.addActuator("VictorSP", getChannel(), this);
        UsageReporting.report(tResourceType.kResourceType_Talon, getChannel());
    }

    /**
     * Constructor.
     * @param channel The PWM channel that the VictorSP is attached to. 0-9 are on-board, 10-19 are on the MXP port
     */
    @objid ("e584560b-9f00-4079-a15b-36cbd7ac348d")
    public VictorSP(final int channel) {
        super(channel);
        initVictorSP();
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
    @objid ("c812239c-9bdc-4757-91ad-83d5d63f681a")
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
    @objid ("065a3480-19e0-4a2b-84a7-1e17a995a767")
    public void set(double speed) {
        setSpeed(speed);
        Feed();
    }

    /**
     * Get the recently set value of the PWM.
     * @return The most recently set value for the PWM between -1.0 and 1.0.
     */
    @objid ("ebc543b8-0146-4ec4-afcf-195325c6267c")
    public double get() {
        return getSpeed();
    }

    /**
     * Write out the PID value as seen in the PIDOutput base object.
     * @param output Write out the PWM value as was found in the PIDController
     */
    @objid ("9265901a-7676-40c4-aa00-323c39ae41a6")
    public void pidWrite(double output) {
        set(output);
    }

}
