/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.hal.InterruptJNI.InterruptJNIHandlerFunction;

/**
 * It is recommended that you use this class in conjunction with classes from
 * {@link java.util.concurrent.atomic} as these objects are all thread safe.
 * 
 * @author Jonathan Leitschuh
 * 
 * @param <T> The type of the parameter that should be returned to the the
 * method {@link #interruptFired(int, Object)}
 */
@objid ("ff6a2f41-ae37-4f24-8dd0-187dc1da49d0")
public abstract class InterruptHandlerFunction<T> {
    @objid ("6adad4ee-52e3-4303-a8ae-0dddd76f390f")
     final Function function = new Function();

    /**
     * This method is run every time an interrupt is fired.
     * @param interruptAssertedMask
     * @param param The parameter provided by overriding the {@link #overridableParamater()}
     * method.
     */
    @objid ("4ec909c2-c922-4c23-b9d4-f6448c070f18")
    abstract void interruptFired(int interruptAssertedMask, T param);

    /**
     * Override this method if you would like to pass a specific
     * parameter to the {@link #interruptFired(int, Object)} when it is fired by the interrupt.
     * This method is called once when {@link InterruptableSensorBase#requestInterrupts(InterruptHandlerFunction)}
     * is run.
     * @return The object that should be passed to the interrupt when it runs
     */
    @objid ("a2a30870-28dd-41df-ae61-056bb326e1e9")
    public T overridableParamater() {
        return null;
    }

    /**
     * The entry point for the interrupt. When the interrupt fires the
     * {@link #apply(int, Object)} method is called.
     * The outer class is provided as an interface to allow the implementer to
     * pass a generic object to the interrupt fired method.
     * @author Jonathan Leitschuh
     */
    @objid ("0cfaf2cd-e5bb-4679-ad45-8bf2b6acc034")
    private class Function implements InterruptJNIHandlerFunction {
        @objid ("45f596dd-25bb-4de2-92c5-9014987463b7")
        @SuppressWarnings("unchecked")
        @Override
        public void apply(int interruptAssertedMask, Object param) {
            interruptFired(interruptAssertedMask, (T)param);
        }

    }

}
