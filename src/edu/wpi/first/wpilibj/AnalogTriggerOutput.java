/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.hal.AnalogJNI;
import edu.wpi.first.wpilibj.hal.HALUtil;

/**
 * Class to represent a specific output from an analog trigger. This class is
 * used to get the current output value and also as a DigitalSource to provide
 * routing of an output to digital subsystems on the FPGA such as Counter,
 * Encoder, and Interrupt.
 * 
 * The TriggerState output indicates the primary output value of the trigger. If
 * the analog signal is less than the lower limit, the output is false. If the
 * analog value is greater than the upper limit, then the output is true. If the
 * analog value is in between, then the trigger output state maintains its most
 * recent value.
 * 
 * The InWindow output indicates whether or not the analog signal is inside the
 * range defined by the limits.
 * 
 * The RisingPulse and FallingPulse outputs detect an instantaneous transition
 * from above the upper limit to below the lower limit, and vise versa. These
 * pulses represent a rollover condition of a sensor and can be routed to an up
 * / down couter or to interrupts. Because the outputs generate a pulse, they
 * cannot be read directly. To help ensure that a rollover condition is not
 * missed, there is an average rejection filter available that operates on the
 * upper 8 bits of a 12 bit number and selects the nearest outlyer of 3 samples.
 * This will reject a sample that is (due to averaging or sampling) errantly
 * between the two limits. This filter will fail if more than one sample in a
 * row is errantly in between the two limits. You may see this problem if
 * attempting to use this feature with a mechanical rollover sensor, such as a
 * 360 degree no-stop potentiometer without signal conditioning, because the
 * rollover transition is not sharp / clean enough. Using the averaging engine
 * may help with this, but rotational speeds of the sensor will then be limited.
 */
@objid ("22cd4844-e99d-452d-b2af-92504752abd6")
public class AnalogTriggerOutput extends DigitalSource {
    @objid ("c3e483f6-b4f8-4f82-b30c-0d4c4e10e6b0")
    private final AnalogTriggerType m_outputType;

    @objid ("82aa5bb9-5c1a-4e05-a6f3-cd9a319af6c0")
    private final AnalogTrigger m_trigger;

    /**
     * Create an object that represents one of the four outputs from an analog
     * trigger.
     * 
     * Because this class derives from DigitalSource, it can be passed into
     * routing functions for Counter, Encoder, etc.
     * @param trigger The trigger for which this is an output.
     * @param outputType An enum that specifies the output on the trigger to represent.
     */
    @objid ("ddccb6bc-fe57-4b48-84db-1ede4560ec49")
    public AnalogTriggerOutput(AnalogTrigger trigger, final AnalogTriggerType outputType) {
        if (trigger == null)
            throw new NullPointerException("Analog Trigger given was null");
        if (outputType == null)
            throw new NullPointerException("Analog Trigger Type given was null");
        m_trigger = trigger;
        m_outputType = outputType;
        
        UsageReporting.report(tResourceType.kResourceType_AnalogTriggerOutput,
                trigger.getIndex(), outputType.value);
    }

    @objid ("e16dccc5-b288-40e4-ba1a-3d72bb7ef262")
    @Override
    public void free() {
    }

    /**
     * Get the state of the analog trigger output.
     * @return The state of the analog trigger output.
     */
    @objid ("dfa6fd27-7e64-458c-a9bf-f288f26b74ba")
    public boolean get() {
        IntBuffer status = IntBuffer.allocate(1);
        byte value = AnalogJNI.getAnalogTriggerOutput(m_trigger.m_port,
                m_outputType.value, status);
        HALUtil.checkStatus(status);
        return value != 0;
    }

    @objid ("c3dc8de0-23ec-4dda-bcd0-376ae45c7e0c")
    @Override
    public int getChannelForRouting() {
        return (m_trigger.m_index << 2) + m_outputType.value;
    }

    @objid ("dbea9ea4-17ea-4e34-8cb5-90403ff4297b")
    @Override
    public byte getModuleForRouting() {
        return (byte) (m_trigger.m_index >> 2);
    }

    @objid ("4c865428-fe1c-4618-8d0f-466800828eb1")
    @Override
    public boolean getAnalogTriggerForRouting() {
        return true;
    }

    /**
     * Exceptions dealing with improper operation of the Analog trigger output
     */
    @objid ("0f330903-32b6-4358-909c-0d46df71e523")
    public class AnalogTriggerOutputException extends RuntimeException {
        /**
         * Create a new exception with the given message
         * @param message the message to pass with the exception
         */
        @objid ("8229f5f0-0cde-4c8a-ae1b-340924ad19cf")
        public AnalogTriggerOutputException(String message) {
            super(message);
        }

    }

    /**
     * Defines the state in which the AnalogTrigger triggers
     * @author jonathanleitschuh
     */
    @objid ("394d0b96-4494-4687-9144-cf297e1b9d8d")
    public enum AnalogTriggerType {
        kInWindow (AnalogJNI.AnalogTriggerType.kInWindow),
        kState (AnalogJNI.AnalogTriggerType.kState),
        kRisingPulse (AnalogJNI.AnalogTriggerType.kRisingPulse),
        kFallingPulse (AnalogJNI.AnalogTriggerType.kFallingPulse);

        @objid ("9309375e-0a74-4756-8ab2-60cae63138ed")
        private final int value;

        @objid ("29e22e91-5de4-43ac-9e92-09a1e71638f8")
        private AnalogTriggerType(int value) {
            this.value = value;
        }

    }

}
