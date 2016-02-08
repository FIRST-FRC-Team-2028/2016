package com.PhantomMentalists.Stronghold;

import com.PhantomMentalists.Stronghold.Parameters;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.CANTalon;

/**
 * <p>Author: Ricky</p>
 */
@objid ("899c26b9-be06-403c-b6de-a1d9d8a5322b")
public class ClimbingArm {
    @objid ("10bfa7f1-d560-4ed8-995e-1dbf6f3fefbd")
    protected ClimberPositions climberState;

    /**
     * <Enter note text here>
     */
    @objid ("66dd6e50-c2c1-49f7-8f19-a78ccab2976f")
    protected CANTalon extendRetractMotor;

    @objid ("585b27f8-06a7-403e-a648-3ca745308acc")
    protected CANTalon raiseLowerMotor;

    @objid ("71341c60-1263-46a0-9d0c-01aae0262ed7")
    protected CANTalon leftWenchMotor;

    @objid ("c01aa643-829c-4e75-aadb-53ff254d418b")
    protected CANTalon rightWenchMotor;

    @objid ("c215a8ac-a925-4c23-aab7-ec75ee354734")
    public ClimbingArm() {
    }

    /**
     * This method powers the tilt motor.  This method can only be used when autopilot is disabled and is intended for troubleshooting and debugging.
     * 
     * @param power Power to apply to the tilt motor as a percentage in the range of -1.0 .. 0 1.0 where -1.0 is 100% reverse and 1.0 is 100% forward.
     */
    @objid ("43dcda5e-dffa-4fab-9b4a-8aab3d753c53")
    public void manualTiltPower(double power) {
 
    }

    /**
     * This method powers the extend motor and applies power to both winch motors.
     * 
     * When run forward, the extend motor drives the extension and both winch motors are run slowly forward (with a constant power value that comes from Parameters) to spool out cable (slowly so it doesn't get slack and tangle).  When run in reverse (retract), the power setting is sent to both winch motors and the extend motor is run slowly in reverse (with a constant power setting that comes from Parameters).  This method can only be used when autopilot is disabled and is intended for troubleshooting and debugging.  
     * 
     * @param power Power to apply to the extend motor as a percentage in the range of -1.0 .. 0 .. 1.0 where -1.0 is 100% reverse, 1.0 is 100% forward and zero is stopped.  Note:  The power to apply to the two winch motors is a constant that comes from Parameters when extending.
     */
    @objid ("dab10ac8-66a4-4b7b-b086-486f55b3cfa2")
    public void manualExtendPower(double power) {
    }

    /**
     */
    @objid ("7e838403-f9e3-4d6a-a314-a1ddaee316e4")
    public void autoSetPosition(ClimberPositions setpoint) {
    }

    /**
     * This method returns if the climber is in a known position.
     * 
     * The climber uses an encoder to count ticks as both the tilt/angle motor and extend/retract motors move.  When the robot is initially powered on, the climber angle and extend/retract is in an unknown state until it reaches a limit switch at a known position.  If either motor is ever shutoff for an over current condition it is reset to an unknown position (since that should never happen).
     * 
     * @return true if both angle and extend/retract motors have reached the home limit switch since being powered on, false otherwise.
     */
    @objid ("167125bc-aa77-43c9-95ca-cbd5a5461e37")
    public boolean isKnownPosition() {
    	return false;
    }

    @objid ("93bc4b9e-0dd6-4d25-9cca-ff5ebcdd37bd")
    public boolean isPositionAtSetpoint() {
    	return false;
    }

    /**
     * <Enter note text here>
     */
    @objid ("c0ae318d-f340-4754-b8b7-cb333e3f0d81")
    public ClimberPositions getClimberState() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.climberState;
    }

    @objid ("523aa711-b676-4bf5-9a4c-66a33139a9d7")
    public void setClimberState(ClimberPositions value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.climberState = value;
    }

    /**
     * This enumeration provides easy to read values for the states of the climbing arm.
     */
    @objid ("c0a916c5-48f5-4fb3-806e-934e0e83cf0d")
    public enum ClimberPositions {
        /**
         * This setpoint returns both climbing arms to fully retracted with the tilt angle at the home position.
         */
        kHome,
        /**
         * This setpoint holds the climbing arms in the fully retracted position with the tilt angle completely down to clear the low bar.
         */
        kLowBar,
        /**
         * This state holds the climbing arms fully extended with the tilt angle set to reach out to the climbing bar on the tower.
         */
        kDeployHook,
        /**
         * This state retracts the climbing arms to lift the robot up the tower.
         */
        kClimb,
        /**
         * This state holds the climbing arms straight up.  It is used to avoid interfering with the shooter (when shooting) and to provide a neutral center of gravity when traversing defenses.
         */
        kRaised,
        /**
         * This state holds the climbing arm high enough to just clear the top of the draw bridge.  Once in position, the robot can change the state to kLowBar which will push down on the draw bridge as the robot backs up to pull down the draw bridge.
         */
        kDrawBridge,
        /**
         * This state represents that either the climbing arm tilt angle or the climbing arm extension is in an unknown position.  This is the initial state of the robot until it is homed.
         */
        kUnknown;
    }

}
