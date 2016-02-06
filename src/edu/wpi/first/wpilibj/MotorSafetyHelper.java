/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.Timer;

/**
 * The MotorSafetyHelper object is constructed for every object that wants to implement the Motor
 * Safety protocol. The helper object has the code to actually do the timing and call the
 * motors Stop() method when the timeout expires. The motor object is expected to call the
 * Feed() method whenever the motors value is updated.
 * 
 * @author brad
 */
@objid ("77e86009-7e7c-4210-822a-0e2eadb0fe92")
public class MotorSafetyHelper {
    @objid ("84152599-292d-496e-be10-308776723d7b")
     double m_expiration;

    @objid ("b85ff7d2-4770-406f-9e44-f17803065779")
     boolean m_enabled;

    @objid ("9570a343-9967-445a-bf4c-b85d39c7497f")
     double m_stopTime;

    @objid ("a75ecf14-5824-4852-98f2-5451bf42c179")
     MotorSafety m_safeObject;

    @objid ("589bba15-dd34-4640-ab9a-2b38998d2c2c")
     MotorSafetyHelper m_nextHelper;

    @objid ("ff44b497-2884-4d69-834d-738c7c066d71")
     static MotorSafetyHelper m_headHelper = null;

    /**
     * The constructor for a MotorSafetyHelper object.
     * The helper object is constructed for every object that wants to implement the Motor
     * Safety protocol. The helper object has the code to actually do the timing and call the
     * motors Stop() method when the timeout expires. The motor object is expected to call the
     * Feed() method whenever the motors value is updated.
     * @param safeObject a pointer to the motor object implementing MotorSafety. This is used
     * to call the Stop() method on the motor.
     */
    @objid ("c541415a-5558-4e3a-983d-289f13a31ceb")
    public MotorSafetyHelper(MotorSafety safeObject) {
        m_safeObject = safeObject;
        m_enabled = false;
        m_expiration = MotorSafety.DEFAULT_SAFETY_EXPIRATION;
        m_stopTime = Timer.getFPGATimestamp();
        m_nextHelper = m_headHelper;
        m_headHelper = this;
    }

    /**
     * Feed the motor safety object.
     * Resets the timer on this object that is used to do the timeouts.
     */
    @objid ("4e65abd5-f178-4284-9557-8613298cd58f")
    public void feed() {
        m_stopTime = Timer.getFPGATimestamp() + m_expiration;
    }

    /**
     * Set the expiration time for the corresponding motor safety object.
     * @param expirationTime The timeout value in seconds.
     */
    @objid ("5b41aaad-f7c8-4191-84e9-7f12caf38432")
    public void setExpiration(double expirationTime) {
        m_expiration = expirationTime;
    }

    /**
     * Retrieve the timeout value for the corresponding motor safety object.
     * @return the timeout value in seconds.
     */
    @objid ("f7e53d76-c179-4686-8605-254f1f08f884")
    public double getExpiration() {
        return m_expiration;
    }

    /**
     * Determine of the motor is still operating or has timed out.
     * @return a true value if the motor is still operating normally and hasn't timed out.
     */
    @objid ("5e7bc653-827c-467a-83b1-02a89c13469a")
    public boolean isAlive() {
        return !m_enabled || m_stopTime > Timer.getFPGATimestamp();
    }

    /**
     * Check if this motor has exceeded its timeout.
     * This method is called periodically to determine if this motor has exceeded its timeout
     * value. If it has, the stop method is called, and the motor is shut down until its value is
     * updated again.
     */
    @objid ("e8b79405-1379-4832-99a6-d3e970ee2ea1")
    public void check() {
        if (!m_enabled || RobotState.isDisabled() || RobotState.isTest())
            return;
        if (m_stopTime < Timer.getFPGATimestamp()) {
            System.err.println(m_safeObject.getDescription() + "... Output not updated often enough.");
        
            m_safeObject.stopMotor();
        }
    }

    /**
     * Enable/disable motor safety for this device
     * Turn on and off the motor safety option for this PWM object.
     * @param enabled True if motor safety is enforced for this object
     */
    @objid ("bff5f4db-f4b0-4b27-96d9-fd837ad87265")
    public void setSafetyEnabled(boolean enabled) {
        m_enabled = enabled;
    }

    /**
     * Return the state of the motor safety enabled flag
     * Return if the motor safety is currently enabled for this devicce.
     * @return True if motor safety is enforced for this device
     */
    @objid ("b255c582-88b1-4d9e-8962-e69a7bb8baf2")
    public boolean isSafetyEnabled() {
        return m_enabled;
    }

//TODO: these should be synchronized with the setting methods in case it's called from a different thread
    /**
     * Check the motors to see if any have timed out.
     * This static  method is called periodically to poll all the motors and stop any that have
     * timed out.
     */
    @objid ("0613d5cb-df78-4e9f-b6b4-b39076d428a9")
    public static void checkMotors() {
        for (MotorSafetyHelper msh = m_headHelper; msh != null; msh = msh.m_nextHelper) {
            msh.check();
        }
    }

}
