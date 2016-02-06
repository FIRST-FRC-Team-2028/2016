/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.hal.AnalogJNI;
import edu.wpi.first.wpilibj.hal.DIOJNI;

/**
 * Base class for all sensors.
 * Stores most recent status information as well as containing utility functions for checking
 * channels and error processing.
 */
@objid ("56ea80da-f07d-48ca-b072-5764d54a7907")
public abstract class SensorBase {
// TODO: Refactor
// TODO: Move this to the HAL
    /**
     * Ticks per microsecond
     */
    @objid ("4991c7ad-c34c-449b-bef2-162817d46252")
    public static final int kSystemClockTicksPerMicrosecond = 40;

    /**
     * Number of digital channels per roboRIO
     */
    @objid ("7b41c8b3-4be2-4413-8abf-1f07bec9938f")
    public static final int kDigitalChannels = 26;

    /**
     * Number of analog input channels
     */
    @objid ("3356c60d-c8f5-4699-a20b-b3f8e891ee78")
    public static final int kAnalogInputChannels = 8;

    /**
     * Number of analog output channels
     */
    @objid ("4712ba32-3cf2-4076-af2f-ccc83ca5ba59")
    public static final int kAnalogOutputChannels = 2;

    /**
     * Number of solenoid channels per module
     */
    @objid ("9a5103b4-649a-4cb2-8ff6-22a424364f6e")
    public static final int kSolenoidChannels = 8;

    /**
     * Number of solenoid modules
     */
    @objid ("0c4fc618-b5bc-470d-a20c-3c325eb7ab0a")
    public static final int kSolenoidModules = 2;

    /**
     * Number of PWM channels per roboRIO
     */
    @objid ("2cacb919-7adb-4866-9d94-5646420c5a4a")
    public static final int kPwmChannels = 20;

    /**
     * Number of relay channels per roboRIO
     */
    @objid ("0d8ef5f8-0919-4d09-8bc2-b1eed7fccdd0")
    public static final int kRelayChannels = 4;

    /**
     * Number of power distribution channels
     */
    @objid ("18cb4c98-5b52-4105-841f-42ea1345ba22")
    public static final int kPDPChannels = 16;

    @objid ("1cbd92f6-dc12-4e8f-b2bc-cdcf2d9fb32a")
    private static int m_defaultSolenoidModule = 0;

    /**
     * Creates an instance of the sensor base and gets an FPGA handle
     */
    @objid ("15527150-467b-4596-8d4e-d58aa0279419")
    public SensorBase() {
    }

    /**
     * Set the default location for the Solenoid module.
     * @param moduleNumber The number of the solenoid module to use.
     */
    @objid ("3677df20-e755-4fb7-9f84-4b4f552aea6b")
    public static void setDefaultSolenoidModule(final int moduleNumber) {
        checkSolenoidModule(moduleNumber);
        SensorBase.m_defaultSolenoidModule = moduleNumber;
    }

    /**
     * Verify that the solenoid module is correct.
     * @param moduleNumber The solenoid module module number to check.
     */
    @objid ("fbb4ccb9-5311-41eb-b460-655c9cd1f83f")
    protected static void checkSolenoidModule(final int moduleNumber) {
        //        if(HALLibrary.checkSolenoidModule((byte) (moduleNumber - 1)) != 0) {
        //            System.err.println("Solenoid module " + moduleNumber + " is not present.");
        //        }
    }

    /**
     * Check that the digital channel number is valid.
     * Verify that the channel number is one of the legal channel numbers. Channel numbers are
     * 1-based.
     * @param channel The channel number to check.
     */
    @objid ("6d4f1c3b-4ed8-4a44-bf75-d55572fb18cd")
    protected static void checkDigitalChannel(final int channel) {
        if (channel < 0 || channel >= kDigitalChannels) {
            throw new IndexOutOfBoundsException("Requested digital channel number is out of range.");
        }
    }

    /**
     * Check that the digital channel number is valid.
     * Verify that the channel number is one of the legal channel numbers. Channel numbers are
     * 1-based.
     * @param channel The channel number to check.
     */
    @objid ("932d2579-6c78-4af9-afef-b8e667655b36")
    protected static void checkRelayChannel(final int channel) {
        if (channel < 0 || channel >= kRelayChannels) {
            throw new IndexOutOfBoundsException("Requested relay channel number is out of range.");
        }
    }

    /**
     * Check that the digital channel number is valid.
     * Verify that the channel number is one of the legal channel numbers. Channel numbers are
     * 1-based.
     * @param channel The channel number to check.
     */
    @objid ("591f1794-2779-49a3-9cb8-314e2a54fe97")
    protected static void checkPWMChannel(final int channel) {
        if (channel < 0 || channel >= kPwmChannels) {
            throw new IndexOutOfBoundsException("Requested PWM channel number is out of range.");
        }
    }

    /**
     * Check that the analog input number is value.
     * Verify that the analog input number is one of the legal channel numbers. Channel numbers
     * are 0-based.
     * @param channel The channel number to check.
     */
    @objid ("7952954a-196b-4af6-82b2-a4980a7f2903")
    protected static void checkAnalogInputChannel(final int channel) {
        if (channel < 0 || channel >= kAnalogInputChannels) {
            throw new IndexOutOfBoundsException("Requested analog input channel number is out of range.");
        }
    }

    /**
     * Check that the analog input number is value.
     * Verify that the analog input number is one of the legal channel numbers. Channel numbers
     * are 0-based.
     * @param channel The channel number to check.
     */
    @objid ("642f64ed-bf87-4af5-8fec-71bd200ee4b5")
    protected static void checkAnalogOutputChannel(final int channel) {
        if (channel < 0 || channel >= kAnalogOutputChannels) {
            throw new IndexOutOfBoundsException("Requested analog output channel number is out of range.");
        }
    }

    /**
     * Verify that the solenoid channel number is within limits.  Channel numbers
     * are 1-based.
     * @param channel The channel number to check.
     */
    @objid ("6928d5ab-c6f9-45bb-a236-ca3007567ca3")
    protected static void checkSolenoidChannel(final int channel) {
        if (channel < 0 || channel >= kSolenoidChannels) {
            throw new IndexOutOfBoundsException("Requested solenoid channel number is out of range.");
        }
    }

    /**
     * Verify that the power distribution channel number is within limits.
     * Channel numbers are 1-based.
     * @param channel The channel number to check.
     */
    @objid ("c2b72684-6a4a-41e6-b877-64b97fa1a6b3")
    protected static void checkPDPChannel(final int channel) {
        if (channel < 0 || channel >= kPDPChannels) {
            throw new IndexOutOfBoundsException("Requested PDP channel number is out of range.");
        }
    }

    /**
     * Get the number of the default solenoid module.
     * @return The number of the default solenoid module.
     */
    @objid ("357a73e2-bcc4-4415-8d88-2b7cb6fb6b04")
    public static int getDefaultSolenoidModule() {
        return SensorBase.m_defaultSolenoidModule;
    }

    /**
     * Free the resources used by this object
     */
    @objid ("3dfbe703-7056-4b4f-9c90-848203814663")
    public void free() {
    }

}
