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
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

/**
 * Standard hobby style servo.
 * 
 * The range parameters default to the appropriate values for the Hitec HS-322HD servo provided
 * in the FIRST Kit of Parts in 2008.
 */
@objid ("e80bd287-7043-4c52-b371-93bcfd40c1cc")
public class Servo extends PWM {
    @objid ("2b50f647-16f6-4c28-9db3-d9de04a1e987")
    private static final double kMaxServoAngle = 180.0;

    @objid ("00763167-c937-4801-8bb0-c0834bdf29c1")
    private static final double kMinServoAngle = 0.0;

    @objid ("5c763701-c41a-4697-ae72-a587deb22350")
    protected static final double kDefaultMaxServoPWM = 2.4;

    @objid ("67bc6158-e133-44c8-84e8-0e91ffa3a64f")
    protected static final double kDefaultMinServoPWM = .6;

    @objid ("1ac87f16-c8d0-4697-830b-4bd0fe9b6de5")
    private ITable m_table;

    @objid ("19d209e8-c146-48a3-8fd6-8c02822a4176")
    private ITableListener m_table_listener;

    /**
     * Common initialization code called by all constructors.
     * 
     * InitServo() assigns defaults for the period multiplier for the servo PWM control signal, as
     * well as the minimum and maximum PWM values supported by the servo.
     */
    @objid ("01445b57-9282-4b1e-a8d4-986b3dd7b0e1")
    private void initServo() {
        setBounds(kDefaultMaxServoPWM, 0, 0, 0, kDefaultMinServoPWM);
        setPeriodMultiplier(PeriodMultiplier.k4X);
        
        LiveWindow.addActuator("Servo", getChannel(), this);
        UsageReporting.report(tResourceType.kResourceType_Servo, getChannel());
    }

    /**
     * Constructor.<br>
     * 
     * By default {@value #kDefaultMaxServoPWM} ms is used as the maxPWM value<br>
     * By default {@value #kDefaultMinServoPWM} ms is used as the minPWM value<br>
     * @param channel The PWM channel to which the servo is attached. 0-9 are on-board, 10-19 are on the MXP port
     */
    @objid ("8530adb5-7f57-4f27-a9a7-a2e0cd950103")
    public Servo(final int channel) {
        super(channel);
        initServo();
    }

    /**
     * Set the servo position.
     * 
     * Servo values range from 0.0 to 1.0 corresponding to the range of full left to full right.
     * @param value Position from 0.0 to 1.0.
     */
    @objid ("7b2ca443-3076-4cf8-9316-6445a0b5d58e")
    public void set(double value) {
        setPosition(value);
    }

    /**
     * Get the servo position.
     * 
     * Servo values range from 0.0 to 1.0 corresponding to the range of full left to full right.
     * @return Position from 0.0 to 1.0.
     */
    @objid ("f7c5d240-8bdc-410e-96bb-9fe7d3b4daf5")
    public double get() {
        return getPosition();
    }

    /**
     * Set the servo angle.
     * 
     * Assume that the servo angle is linear with respect to the PWM value (big assumption, need to test).
     * 
     * Servo angles that are out of the supported range of the servo simply "saturate" in that direction
     * In other words, if the servo has a range of (X degrees to Y degrees) than angles of less than X
     * result in an angle of X being set and angles of more than Y degrees result in an angle of Y being set.
     * @param degrees The angle in degrees to set the servo.
     */
    @objid ("551b796c-273f-46b8-a4d9-6a43815a01cf")
    public void setAngle(double degrees) {
        if (degrees < kMinServoAngle) {
            degrees = kMinServoAngle;
        } else if (degrees > kMaxServoAngle) {
            degrees = kMaxServoAngle;
        }
        
        setPosition(((degrees - kMinServoAngle)) / getServoAngleRange());
    }

    /**
     * Get the servo angle.
     * 
     * Assume that the servo angle is linear with respect to the PWM value (big assumption, need to test).
     * @return The angle in degrees to which the servo is set.
     */
    @objid ("4ddb8075-ef28-487b-82ab-1fac8bc03e6a")
    public double getAngle() {
        return getPosition() * getServoAngleRange() + kMinServoAngle;
    }

    @objid ("52726304-d85c-4bfa-af02-c28cee8fdc4d")
    private double getServoAngleRange() {
        return kMaxServoAngle - kMinServoAngle;
    }

/*
     * Live Window code, only does anything if live window is activated.
     */
    @objid ("e5605054-852e-4e52-b75c-3ad9acf97bef")
    public String getSmartDashboardType() {
        return "Servo";
    }

    /**
     * {@inheritDoc}
     */
    @objid ("f162f525-a5f9-454c-8875-4a3b57676eb1")
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @objid ("5aa5ed54-b57f-4aab-94d7-916d4f9ef139")
    public void updateTable() {
        if (m_table != null) {
            m_table.putNumber("Value", get());
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("4d85987b-431f-48f2-a907-b24eeb27bb9a")
    public void startLiveWindowMode() {
        m_table_listener = new ITableListener() {
            public void valueChanged(ITable itable, String key, Object value, boolean bln) {
                set(((Double) value).doubleValue());
            }
        };
        m_table.addTableListener("Value", m_table_listener, true);
    }

    /**
     * {@inheritDoc}
     */
    @objid ("a1cab9d6-0651-4e12-abe4-b52bb67e1848")
    public void stopLiveWindowMode() {
        // TODO: Broken, should only remove the listener from "Value" only.
        m_table.removeTableListener(m_table_listener);
    }

}
