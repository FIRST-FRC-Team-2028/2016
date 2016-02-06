/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.command;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * This exception will be thrown if a command is used illegally.  There are
 * several ways for this to happen.
 * 
 * <p>Basically, a command becomes "locked" after it is first started or added
 * to a command group.</p>
 * 
 * <p>This exception should be thrown if (after a command has been locked) its requirements
 * change, it is put into multiple command groups,
 * it is started from outside its command group, or it adds a new child.</p>
 * 
 * @author Joe Grinstead
 */
@objid ("81a854ac-6ffd-40d6-a188-cd44c92bd16c")
public class IllegalUseOfCommandException extends RuntimeException {
    /**
     * Instantiates an {@link IllegalUseOfCommandException}.
     */
    @objid ("bd8b457f-4b85-44cd-abfc-3866e094b6cf")
    public IllegalUseOfCommandException() {
    }

    /**
     * Instantiates an {@link IllegalUseOfCommandException} with the given message.
     * @param message the message
     */
    @objid ("bd0e3a4e-0e27-47a3-aa1c-271e1e81662f")
    public IllegalUseOfCommandException(String message) {
        super(message);
    }

}
