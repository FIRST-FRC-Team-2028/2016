/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.util;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Thrown if there is an error caused by a basic system or setting
 * not being properly initialized before being used.
 * 
 * @author Jonathan Leitschuh
 */
@objid ("53daceba-7714-4725-80d5-f1a11c8f59d0")
public class BaseSystemNotInitializedException extends RuntimeException {
    /**
     * Create a new BaseSystemNotInitializedException
     * @param message the message to attach to the exception
     */
    @objid ("538d5b2e-b2b5-469a-8767-f9a3f7f042f6")
    public BaseSystemNotInitializedException(String message) {
        super(message);
    }

    /**
     * Create a new BaseSystemNotInitializedException using the offending class that was not set and the
     * class that was affected.
     * @param offender The class or interface that was not properly initialized.
     * @param affected The class that was was affected by this missing initialization.
     */
    @objid ("fe796775-0fdc-4a78-848d-f710f7737cee")
    public BaseSystemNotInitializedException(Class<?> offender, Class<?> affected) {
        super("The " + offender.getSimpleName() + " for the " + affected.getSimpleName() + " was never set.");
    }

}
