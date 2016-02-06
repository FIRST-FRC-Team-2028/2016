/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.util;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * This exception represents an error in which a lower limit was set as higher
 * than an upper limit.
 * 
 * @author dtjones
 */
@objid ("ad2cb5cd-d062-42d0-aee9-5b55e059afcf")
public class BoundaryException extends RuntimeException {
    /**
     * Create a new exception with the given message
     * @param message the message to attach to the exception
     */
    @objid ("c582c9fc-9dfe-4b9b-8b96-d059cf35f731")
    public BoundaryException(String message) {
        super(message);
    }

    /**
     * Make sure that the given value is between the upper and lower bounds, and
     * throw an exception if they are not.
     * @param value The value to check.
     * @param lower The minimum acceptable value.
     * @param upper The maximum acceptable value.
     */
    @objid ("98f87e6d-ee42-4771-87e3-58eeefd7f554")
    public static void assertWithinBounds(double value, double lower, double upper) {
        if (value < lower || value > upper)
            throw new BoundaryException("Value must be between " + lower
                    + " and " + upper + ", " + value + " given");
    }

    /**
     * Returns the message for a boundary exception. Used to keep the message
     * consistent across all boundary exceptions.
     * @param value The given value
     * @param lower The lower limit
     * @param upper The upper limit
     * @return the message for a boundary exception
     */
    @objid ("627fab26-5de3-48e8-bd20-9c1b6b55850b")
    public static String getMessage(double value, double lower, double upper) {
        return "Value must be between " + lower + " and " + upper + ", "
                        + value + " given";
    }

}
