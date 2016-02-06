/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * @author brad
 */
@objid ("55d413ef-b768-434c-8ee0-2cd39a478327")
public class SafePWM extends PWM implements MotorSafety {
    @objid ("777aa01c-58d1-4d80-9b9c-57430e46ba43")
    private MotorSafetyHelper m_safetyHelper;

    /**
     * Initialize a SafePWM object by setting defaults
     */
    @objid ("e035cffa-38a6-4602-adef-65e57e7eaea2")
    void initSafePWM() {
        m_safetyHelper = new MotorSafetyHelper(this);
        m_safetyHelper.setExpiration(0.0);
        m_safetyHelper.setSafetyEnabled(false);
    }

    /**
     * Constructor for a SafePWM object taking a channel number
     * @param channel The channel number to be used for the underlying PWM object. 0-9 are on-board, 10-19 are on the MXP port.
     */
    @objid ("9dac3f4c-d91c-40c7-b749-0db9220512f9")
    public SafePWM(final int channel) {
        super(channel);
        initSafePWM();
    }

/*
     * Set the expiration time for the PWM object
     * @param timeout The timeout (in seconds) for this motor object
     */
    @objid ("2b898f4a-c7da-420f-955c-98d5faa300e2")
    public void setExpiration(double timeout) {
        m_safetyHelper.setExpiration(timeout);
    }

    /**
     * Return the expiration time for the PWM object.
     * @return The expiration time value.
     */
    @objid ("a2eba5b0-4c17-42d9-a52e-314a95abf7ce")
    public double getExpiration() {
        return m_safetyHelper.getExpiration();
    }

    /**
     * Check if the PWM object is currently alive or stopped due to a timeout.
     * @return a bool value that is true if the motor has NOT timed out and should still
     * be running.
     */
    @objid ("d2bade99-533a-4055-890d-9a292497bc26")
    public boolean isAlive() {
        return m_safetyHelper.isAlive();
    }

    /**
     * Stop the motor associated with this PWM object.
     * This is called by the MotorSafetyHelper object when it has a timeout for this PWM and needs to
     * stop it from running.
     */
    @objid ("af3ca886-e6ac-4494-b50b-c622d69529cf")
    public void stopMotor() {
        disable();
    }

    /**
     * Check if motor safety is enabled for this object
     * @return True if motor safety is enforced for this object
     */
    @objid ("daf70af9-65ab-48a1-ae74-f6e136b3bdd7")
    public boolean isSafetyEnabled() {
        return m_safetyHelper.isSafetyEnabled();
    }

    /**
     * Feed the MotorSafety timer.
     * This method is called by the subclass motor whenever it updates its speed, thereby reseting
     * the timeout value.
     */
    @objid ("e2c1127f-dfdb-410b-9f56-3c05e69d1cda")
    public void Feed() {
        m_safetyHelper.feed();
    }

    @objid ("3e6419b3-4d6e-4a44-8b66-90207ec1a3ab")
    public void setSafetyEnabled(boolean enabled) {
        m_safetyHelper.setSafetyEnabled(enabled);
    }

    @objid ("be881d5d-3b06-4b8b-af63-9c886f370ef9")
    public String getDescription() {
        return "PWM "+getChannel();
    }

    @objid ("91fb560c-3988-4bdf-8550-e2f26b0be20f")
    public void disable() {
        setRaw(kPwmDisabled);
    }

}
