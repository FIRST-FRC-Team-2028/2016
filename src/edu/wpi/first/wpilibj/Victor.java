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
 * VEX Robotics Victor 888 Speed Controller
 * 
 * The Vex Robotics Victor 884 Speed Controller can also be used with this
 * class but may need to be calibrated per the Victor 884 user manual.
 */
@objid ("93779258-5dfd-4a7a-904b-061f9fa3391b")
public class Victor extends SafePWM implements SpeedController {
    /**
     * Common initialization code called by all constructors.
     * 
     * Note that the Victor uses the following bounds for PWM values.  These values were determined
     * empirically and optimized for the Victor 888. These values should work reasonably well for
     * Victor 884 controllers also but if users experience issues such as asymmetric behaviour around
     * the deadband or inability to saturate the controller in either direction, calibration is recommended.
     * The calibration procedure can be found in the Victor 884 User Manual available from VEX Robotics:
     * http://content.vexrobotics.com/docs/ifi-v884-users-manual-9-25-06.pdf
     * 
     * - 2.027ms = full "forward"
     * - 1.525ms = the "high end" of the deadband range
     * - 1.507ms = center of the deadband range (off)
     * - 1.49ms = the "low end" of the deadband range
     * - 1.026ms = full "reverse"
     */
    @objid ("d00ce129-3e1b-431f-a252-19394213ea86")
    private void initVictor() {
        setBounds(2.027, 1.525, 1.507, 1.49, 1.026);
        setPeriodMultiplier(PeriodMultiplier.k2X);
        setRaw(m_centerPwm);
        setZeroLatch();
        
        LiveWindow.addActuator("Victor", getChannel(), this);
        UsageReporting.report(tResourceType.kResourceType_Victor, getChannel());
    }

    /**
     * Constructor.
     * @param channel The PWM channel that the Victor is attached to. 0-9 are on-board, 10-19 are on the MXP port
     */
    @objid ("9f7b39ce-81ba-4af5-a2f2-c9550afa0335")
    public Victor(final int channel) {
        super(channel);
        initVictor();
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
    @objid ("c3f8d813-7502-4db4-8b22-b5661a9d84e3")
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
    @objid ("90bbd41e-0320-48dc-8766-5d2db5084ecd")
    public void set(double speed) {
        setSpeed(speed);
        Feed();
    }

    /**
     * Get the recently set value of the PWM.
     * @return The most recently set value for the PWM between -1.0 and 1.0.
     */
    @objid ("1c04a10c-aad6-40ce-a39d-0fb30f9b1217")
    public double get() {
        return getSpeed();
    }

    /**
     * Write out the PID value as seen in the PIDOutput base object.
     * @param output Write out the PWM value as was found in the PIDController
     */
    @objid ("2ce8c88f-ed47-42dd-abf9-2526d153761d")
    public void pidWrite(double output) {
        set(output);
    }

}
