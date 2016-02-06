/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Interface for counting the number of ticks on a digital input channel.
 * Encoders, Gear tooth sensors, and counters should all subclass this so it can be used to
 * build more advanced classes for control and driving.
 * 
 * All counters will immediately start counting - reset() them if you need them
 * to be zeroed before use.
 */
@objid ("1da6d43b-34c3-475f-adc7-213e6eb6493a")
public interface CounterBase {
    /**
     * Get the count
     * @return the count
     */
    @objid ("7c759375-3c76-4392-9a59-8eabc0778efd")
    int get();

    /**
     * Reset the count to zero
     */
    @objid ("792c7942-dc50-43da-be5e-9ee2195e009f")
    void reset();

    /**
     * Get the time between the last two edges counted
     * @return the time beteween the last two ticks in seconds
     */
    @objid ("e0448b7f-9d88-4dbe-bd1c-dbb2917496a1")
    double getPeriod();

    /**
     * Set the maximum time between edges to be considered stalled
     * @param maxPeriod the maximum period in seconds
     */
    @objid ("c5bc9e4e-00c8-4157-9b5e-fa61c4f00ee8")
    void setMaxPeriod(double maxPeriod);

    /**
     * Determine if the counter is not moving
     * @return true if the counter has not changed for the max period
     */
    @objid ("021f980b-1996-4fca-a715-83450ed90c7b")
    boolean getStopped();

    /**
     * Determine which direction the counter is going
     * @return true for one direction, false for the other
     */
    @objid ("54c2d68e-9a51-4946-803d-f683fba30b74")
    boolean getDirection();

    /**
     * The number of edges for the counterbase to increment or decrement on
     */
    @objid ("b7121b23-a866-497e-8134-3038e1100556")
    public static class EncodingType {
        /**
         * The integer value representing this enumeration
         */
        @objid ("78b98dde-c8fc-4230-9aa3-0b28a0b033b7")
        public final int value;

        @objid ("e3540736-f7aa-43b5-93a0-ed1b1a65283e")
         static final int k1X_val = 0;

        @objid ("d0b24efa-c252-49a6-b836-ca8404448217")
         static final int k2X_val = 1;

        @objid ("f048ed33-4769-460d-b5fe-8cfa076aa3f1")
         static final int k4X_val = 2;

        /**
         * Count only the rising edge
         */
        @objid ("d772abb1-ef93-443f-941a-50668a7a2366")
        public static final EncodingType k1X = new EncodingType(k1X_val);

        /**
         * Count both the rising and falling edge
         */
        @objid ("2592b1cb-53ca-48b3-9a08-d86d7838961b")
        public static final EncodingType k2X = new EncodingType(k2X_val);

        /**
         * Count rising and falling on both channels
         */
        @objid ("5b3a4e6b-aa30-4b17-9af6-aacea569010b")
        public static final EncodingType k4X = new EncodingType(k4X_val);

        @objid ("518e7e3a-c20a-4066-90b0-158543e3972c")
        private EncodingType(int value) {
            this.value = value;
        }

    }

}
