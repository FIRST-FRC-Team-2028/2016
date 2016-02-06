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
 * Cross the Road Electronics (CTRE) Talon SRX Speed Controller with PWM control
 * @see CANTalon CANTalon for CAN control of Talon SRX
 */
@objid ("8d7b0969-7126-4c20-aa38-c0cabb3558bf")
public class TalonSRX extends SafePWM implements SpeedController {
    /**
     * Common initialization code called by all constructors.
     * 
     * Note that the TalonSRX uses the following bounds for PWM values. These values should work reasonably well for
     * most controllers, but if users experience issues such as asymmetric behavior around
     * the deadband or inability to saturate the controller in either direction, calibration is recommended.
     * The calibration procedure can be found in the TalonSRX User Manual available from CTRE.
     * 
     * - 2.0004ms = full "forward"
     * - 1.52ms = the "high end" of the deadband range
     * - 1.50ms = center of the deadband range (off)
     * - 1.48ms = the "low end" of the deadband range
     * - .997ms = full "reverse"
     */
    @objid ("ebb3e93a-29a9-4b2d-8604-96b59f5cd943")
    protected void initTalonSRX() {
        setBounds(2.004, 1.52, 1.50, 1.48, .997);
        setPeriodMultiplier(PeriodMultiplier.k1X);
        setRaw(m_centerPwm);
        setZeroLatch();
        
        LiveWindow.addActuator("TalonSRX", getChannel(), this);
        UsageReporting.report(tResourceType.kResourceType_Talon, getChannel());
    }

    /**
     * Constructor for a TalonSRX connected via PWM
     * @param channel The PWM channel that the TalonSRX is attached to. 0-9 are on-board, 10-19 are on the MXP port
     */
    @objid ("4541e605-7750-4fd0-976d-a108f3710cc4")
    public TalonSRX(final int channel) {
        super(channel);
        initTalonSRX();
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
    @objid ("4a5003f1-b766-4af3-8374-ed94c130c331")
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
    @objid ("5a59101b-6984-49ab-a519-adb0f42569ad")
    public void set(double speed) {
        setSpeed(speed);
        Feed();
    }

    /**
     * Get the recently set value of the PWM.
     * @return The most recently set value for the PWM between -1.0 and 1.0.
     */
    @objid ("3e3c2b22-d672-41f8-81f9-79d932ef6291")
    public double get() {
        return getSpeed();
    }

    /**
     * Write out the PID value as seen in the PIDOutput base object.
     * @param output Write out the PWM value as was found in the PIDController
     */
    @objid ("4e4d9b10-4ed4-4155-a9b9-165c368f6d00")
    public void pidWrite(double output) {
        set(output);
    }

}
