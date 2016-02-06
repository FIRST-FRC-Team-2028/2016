/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary;
import edu.wpi.first.wpilibj.communication.UsageReporting;

/**
 * Handle input from standard Joysticks connected to the Driver Station.
 * This class handles standard input that comes from the Driver Station. Each time a value is requested
 * the most recent value is returned. There is a single class instance for each joystick and the mapping
 * of ports to hardware buttons depends on the code in the driver station.
 */
@objid ("f6cf0bb5-9368-4445-befb-1dfb22acd825")
public class Joystick extends GenericHID {
    @objid ("d9834ef9-e820-4327-9fab-cdc3a2a6ff32")
     static final byte kDefaultXAxis = 0;

    @objid ("99314a2c-4bb2-4a3c-bda6-7ba35bf42a43")
     static final byte kDefaultYAxis = 1;

    @objid ("47a481b8-8420-4f31-b32d-35e969966cdd")
     static final byte kDefaultZAxis = 2;

    @objid ("4697d743-c98d-4112-8c2e-3260be51a478")
     static final byte kDefaultTwistAxis = 2;

    @objid ("6f600bce-1e9d-41b5-8801-f4b8b5cc140d")
     static final byte kDefaultThrottleAxis = 3;

    @objid ("944f4fbd-4612-430a-a904-ba92e05d8990")
     static final int kDefaultTriggerButton = 1;

    @objid ("26336ce3-8e50-41f5-8cce-638060f59cb7")
     static final int kDefaultTopButton = 2;

    @objid ("4f7a5170-53bb-4b12-970a-79e307269538")
    private final int m_port;

    @objid ("f0b9f7c1-6256-4fea-9c8b-5be9c2da0a9e")
    private final byte[] m_axes;

    @objid ("7b20d019-3409-48b7-8027-bd1cd9846741")
    private final byte[] m_buttons;

    @objid ("8e45ec63-fc6f-41d1-adba-f2a503e2d093")
    private int m_outputs;

    @objid ("0fa0608a-c8ed-4a5b-ac20-63485becf2e8")
    private short m_leftRumble;

    @objid ("0f731d1c-2102-46b8-b408-1a917f8dd66f")
    private short m_rightRumble;

    @objid ("902d3214-d6c2-4cbf-ae18-45fcb49f374c")
    private DriverStation m_ds;

    /**
     * Construct an instance of a joystick.
     * The joystick index is the usb port on the drivers station.
     * @param port The port on the driver station that the joystick is plugged into.
     */
    @objid ("c74c6bb4-a244-4155-8d6c-9b672e1774be")
    public Joystick(final int port) {
        this(port, AxisType.kNumAxis.value, ButtonType.kNumButton.value);
        
        m_axes[AxisType.kX.value] = kDefaultXAxis;
        m_axes[AxisType.kY.value] = kDefaultYAxis;
        m_axes[AxisType.kZ.value] = kDefaultZAxis;
        m_axes[AxisType.kTwist.value] = kDefaultTwistAxis;
        m_axes[AxisType.kThrottle.value] = kDefaultThrottleAxis;
        
        m_buttons[ButtonType.kTrigger.value] = kDefaultTriggerButton;
        m_buttons[ButtonType.kTop.value] = kDefaultTopButton;
        
        UsageReporting.report(tResourceType.kResourceType_Joystick, port);
    }

    /**
     * Protected version of the constructor to be called by sub-classes.
     * 
     * This constructor allows the subclass to configure the number of constants
     * for axes and buttons.
     * @param port The port on the driver station that the joystick is plugged into.
     * @param numAxisTypes The number of axis types in the enum.
     * @param numButtonTypes The number of button types in the enum.
     */
    @objid ("9e9c90e1-8188-4c74-9d75-e2c9aaf8c1d7")
    protected Joystick(int port, int numAxisTypes, int numButtonTypes) {
        m_ds = DriverStation.getInstance();
        m_axes = new byte[numAxisTypes];
        m_buttons = new byte[numButtonTypes];
        m_port = port;
    }

    /**
     * Get the X value of the joystick.
     * This depends on the mapping of the joystick connected to the current port.
     * @param hand Unused
     * @return The X value of the joystick.
     */
    @objid ("338aae2c-4480-4127-9310-771b3615e22e")
    public double getX(Hand hand) {
        return getRawAxis(m_axes[AxisType.kX.value]);
    }

    /**
     * Get the Y value of the joystick.
     * This depends on the mapping of the joystick connected to the current port.
     * @param hand Unused
     * @return The Y value of the joystick.
     */
    @objid ("8a25a0f4-241f-49f0-94a7-ee3e853f4e2f")
    public double getY(Hand hand) {
        return getRawAxis(m_axes[AxisType.kY.value]);
    }

    /**
     * Get the Z value of the joystick.
     * This depends on the mapping of the joystick connected to the current port.
     * @param hand Unused
     * @return The Z value of the joystick.
     */
    @objid ("370e64f3-f58b-4b1e-9d21-28ff29124758")
    public double getZ(Hand hand) {
        return getRawAxis(m_axes[AxisType.kZ.value]);
    }

    /**
     * Get the twist value of the current joystick.
     * This depends on the mapping of the joystick connected to the current port.
     * @return The Twist value of the joystick.
     */
    @objid ("e0deb961-ae4b-4fa1-ab5b-ac825c8bffb7")
    public double getTwist() {
        return getRawAxis(m_axes[AxisType.kTwist.value]);
    }

    /**
     * Get the throttle value of the current joystick.
     * This depends on the mapping of the joystick connected to the current port.
     * @return The Throttle value of the joystick.
     */
    @objid ("8d20101c-e3c0-4e15-80c8-e7f6a2d14358")
    public double getThrottle() {
        return getRawAxis(m_axes[AxisType.kThrottle.value]);
    }

    /**
     * Get the value of the axis.
     * @param axis The axis to read, starting at 0.
     * @return The value of the axis.
     */
    @objid ("6344e1c4-791c-44e1-9f15-81b8295af62d")
    public double getRawAxis(final int axis) {
        return m_ds.getStickAxis(m_port, axis);
    }

    /**
     * For the current joystick, return the axis determined by the argument.
     * 
     * This is for cases where the joystick axis is returned programatically, otherwise one of the
     * previous functions would be preferable (for example getX()).
     * @param axis The axis to read.
     * @return The value of the axis.
     */
    @objid ("b7044e9b-6342-4944-8477-1c6690572e99")
    public double getAxis(final AxisType axis) {
        switch (axis.value) {
        case AxisType.kX_val:
            return getX();
        case AxisType.kY_val:
            return getY();
        case AxisType.kZ_val:
            return getZ();
        case AxisType.kTwist_val:
            return getTwist();
        case AxisType.kThrottle_val:
            return getThrottle();
        default:
            return 0.0;
        }
    }

    /**
     * For the current joystick, return the number of axis
     */
    @objid ("19496bfc-a878-40e9-a56d-d03bd25d78c0")
    public int getAxisCount() {
        return m_ds.getStickAxisCount(m_port);
    }

    /**
     * Read the state of the trigger on the joystick.
     * 
     * Look up which button has been assigned to the trigger and read its state.
     * @param hand This parameter is ignored for the Joystick class and is only here to complete the GenericHID interface.
     * @return The state of the trigger.
     */
    @objid ("dbbff919-0cf8-46ba-8695-bdfb53018b66")
    public boolean getTrigger(Hand hand) {
        return getRawButton(m_buttons[ButtonType.kTrigger.value]);
    }

    /**
     * Read the state of the top button on the joystick.
     * 
     * Look up which button has been assigned to the top and read its state.
     * @param hand This parameter is ignored for the Joystick class and is only here to complete the GenericHID interface.
     * @return The state of the top button.
     */
    @objid ("21b699eb-ba16-4273-8919-bb0c39b91f6d")
    public boolean getTop(Hand hand) {
        return getRawButton(m_buttons[ButtonType.kTop.value]);
    }

    /**
     * This is not supported for the Joystick.
     * This method is only here to complete the GenericHID interface.
     * @param hand This parameter is ignored for the Joystick class and is only here to complete the GenericHID interface.
     * @return The state of the bumper (always false)
     */
    @objid ("41ccb060-821e-4d5d-bf5c-28e966a4f6bc")
    public boolean getBumper(Hand hand) {
        return false;
    }

    /**
     * Get the button value (starting at button 1)
     * 
     * The appropriate button is returned as a boolean value.
     * @param button The button number to be read (starting at 1).
     * @return The state of the button.
     */
    @objid ("13412f79-ba59-4cb5-b86d-8784ffd01713")
    public boolean getRawButton(final int button) {
        return m_ds.getStickButton(m_port, (byte)button);
    }

    /**
     * For the current joystick, return the number of buttons
     */
    @objid ("b099c8f1-8b3d-42c1-addd-8ca8b8b26cef")
    public int getButtonCount() {
        return m_ds.getStickButtonCount(m_port);
    }

    /**
     * Get the state of a POV on the joystick.
     * @param pov The index of the POV to read (starting at 0)
     * @return the angle of the POV in degrees, or -1 if the POV is not pressed.
     */
    @objid ("091dd7ae-f30f-4e93-9ab7-5a35d1bc9d54")
    public int getPOV(int pov) {
        return m_ds.getStickPOV(m_port, pov);
    }

    /**
     * For the current joystick, return the number of POVs
     */
    @objid ("5636ab3b-07f2-4cb2-a892-d856127aa350")
    public int getPOVCount() {
        return m_ds.getStickPOVCount(m_port);
    }

    /**
     * Get buttons based on an enumerated type.
     * 
     * The button type will be looked up in the list of buttons and then read.
     * @param button The type of button to read.
     * @return The state of the button.
     */
    @objid ("3dd41839-caec-4c36-ae42-9388e8a7ff8a")
    public boolean getButton(ButtonType button) {
        switch (button.value) {
        case ButtonType.kTrigger_val:
            return getTrigger();
        case ButtonType.kTop_val:
            return getTop();
        default:
            return false;
        }
    }

    /**
     * Get the magnitude of the direction vector formed by the joystick's
     * current position relative to its origin
     * @return The magnitude of the direction vector
     */
    @objid ("2d73b7b8-39df-45ec-b692-73f0510fa308")
    public double getMagnitude() {
        return Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2));
    }

    /**
     * Get the direction of the vector formed by the joystick and its origin
     * in radians
     * @return The direction of the vector in radians
     */
    @objid ("1368922a-e0c6-43f9-b5f2-063d8cfdaffe")
    public double getDirectionRadians() {
        return Math.atan2(getX(), -getY());
    }

    /**
     * Get the direction of the vector formed by the joystick and its origin
     * in degrees
     * 
     * uses acos(-1) to represent Pi due to absence of readily accessable Pi
     * constant in C++
     * @return The direction of the vector in degrees
     */
    @objid ("6e223160-d963-440c-a8d8-7e26cacc3c4b")
    public double getDirectionDegrees() {
        return Math.toDegrees(getDirectionRadians());
    }

    /**
     * Get the channel currently associated with the specified axis.
     * @param axis The axis to look up the channel for.
     * @return The channel fr the axis.
     */
    @objid ("114d440f-9ab9-4ee4-a8d6-35f4df84e476")
    public int getAxisChannel(AxisType axis) {
        return m_axes[axis.value];
    }

    /**
     * Set the channel associated with a specified axis.
     * @param axis The axis to set the channel for.
     * @param channel The channel to set the axis to.
     */
    @objid ("ad5c7cf1-2082-47fc-b4d9-e1c364f93dce")
    public void setAxisChannel(AxisType axis, int channel) {
        m_axes[axis.value] = (byte) channel;
    }

    /**
     * Set the rumble output for the joystick. The DS currently supports 2 rumble values,
     * left rumble and right rumble
     * @param type Which rumble value to set
     * @param value The normalized value (0 to 1) to set the rumble to
     */
    @objid ("bd4ef4c5-6ee5-4c7b-a1a2-7feb7e9be53b")
    public void setRumble(RumbleType type, float value) {
        if (value < 0)
            value = 0;
        else if (value > 1)
            value = 1;
        if (type.value == RumbleType.kLeftRumble_val)
            m_leftRumble = (short)(value*65535);
        else
            m_rightRumble = (short)(value*65535);
        FRCNetworkCommunicationsLibrary.HALSetJoystickOutputs((byte)m_port, m_outputs, m_leftRumble, m_rightRumble);
    }

    /**
     * Set a single HID output value for the joystick.
     * @param outputNumber The index of the output to set (1-32)
     * @param value The value to set the output to
     */
    @objid ("02b261e8-d80f-4560-a61a-f98b25d56c57")
    public void setOutput(int outputNumber, boolean value) {
        m_outputs = (m_outputs & ~(1 << (outputNumber-1))) | ((value?1:0) << (outputNumber-1));
        FRCNetworkCommunicationsLibrary.HALSetJoystickOutputs((byte)m_port, m_outputs, m_leftRumble, m_rightRumble);
    }

    /**
     * Set all HID output values for the joystick.
     * @param value The 32 bit output value (1 bit for each output)
     */
    @objid ("7853f386-17f3-414b-ac1e-f54a0e5be301")
    public void setOutputs(int value) {
        m_outputs = value;
        FRCNetworkCommunicationsLibrary.HALSetJoystickOutputs((byte)m_port, m_outputs, m_leftRumble, m_rightRumble);
    }

    /**
     * Represents an analog axis on a joystick.
     */
    @objid ("37582cae-2f51-4606-a24e-c6e6f6074d82")
    public static class AxisType {
        /**
         * The integer value representing this enumeration
         */
        @objid ("fd9ffe09-d0ca-4c9b-b3f1-231e78632567")
        public final int value;

        @objid ("79079323-e0c4-4da8-b79e-67f33ff5ff93")
         static final int kX_val = 0;

        @objid ("9b4a4dde-4e1a-44d9-a4f2-eef1769d58ac")
         static final int kY_val = 1;

        @objid ("534da2af-a74f-4048-9675-eeccaed31148")
         static final int kZ_val = 2;

        @objid ("b1e7f7f7-5007-45d4-9efc-1c82e5f9f992")
         static final int kTwist_val = 3;

        @objid ("9fb78430-6fb3-4fe6-b538-978a1b14fd3d")
         static final int kThrottle_val = 4;

        @objid ("f0c60ce7-73bb-4e45-95c0-19b276367b23")
         static final int kNumAxis_val = 5;

        /**
         * axis: x-axis
         */
        @objid ("d6fcd871-a8ba-42c7-a104-4b05ac5346cd")
        public static final AxisType kX = new AxisType(kX_val);

        /**
         * axis: y-axis
         */
        @objid ("a3d7cdb4-490a-4da0-af3e-68af8ec054b6")
        public static final AxisType kY = new AxisType(kY_val);

        /**
         * axis: z-axis
         */
        @objid ("2f0dae57-4aaf-4336-8bf2-d4bf632f4d53")
        public static final AxisType kZ = new AxisType(kZ_val);

        /**
         * axis: twist
         */
        @objid ("1c11ed3c-dba8-4134-96c8-3ac022d4e771")
        public static final AxisType kTwist = new AxisType(kTwist_val);

        /**
         * axis: throttle
         */
        @objid ("446d7a9e-cd0b-4ec4-8069-094bfee93b19")
        public static final AxisType kThrottle = new AxisType(kThrottle_val);

        /**
         * axis: number of axis
         */
        @objid ("75522bf3-addd-4408-8842-a6f2d2b4a6f6")
        public static final AxisType kNumAxis = new AxisType(kNumAxis_val);

        @objid ("e9da3cf6-571a-43fc-83b0-3ea2a8ec418f")
        private AxisType(int value) {
            this.value = value;
        }

    }

    /**
     * Represents a digital button on the JoyStick
     */
    @objid ("687f9d2b-1986-436c-a9df-7fd06336bc08")
    public static class ButtonType {
        /**
         * The integer value representing this enumeration
         */
        @objid ("17c592b0-945b-4fc6-b2ea-7c1df36597d4")
        public final int value;

        @objid ("326875da-114c-47ce-b541-531d551581a4")
         static final int kTrigger_val = 0;

        @objid ("adc1640b-066c-498d-a616-e09ed7ea32d1")
         static final int kTop_val = 1;

        @objid ("d7eb077c-ed61-4a52-827d-b0cf8d7e6e95")
         static final int kNumButton_val = 2;

        /**
         * button: trigger
         */
        @objid ("79170050-cbfb-4903-ba14-a0c79ebecb72")
        public static final ButtonType kTrigger = new ButtonType((kTrigger_val));

        /**
         * button: top button
         */
        @objid ("20da9589-66b1-45b1-899d-dcd429741fdc")
        public static final ButtonType kTop = new ButtonType(kTop_val);

        /**
         * button: num button types
         */
        @objid ("7cd6da50-959e-420c-b84d-cf7a5ba69fe1")
        public static final ButtonType kNumButton = new ButtonType((kNumButton_val));

        @objid ("57c1fdaa-27b7-43fc-9c73-b8a924eebf0d")
        private ButtonType(int value) {
            this.value = value;
        }

    }

    /**
     * Represents a rumble output on the JoyStick
     */
    @objid ("fe7116b5-ab99-49ae-a8db-f67411bc5373")
    public static class RumbleType {
        /**
         * The integer value representing this enumeration
         */
        @objid ("b1df7939-e69a-48e1-b0ce-557e4b57ebc5")
        public final int value;

        @objid ("e135e5db-c1cd-4bb3-b901-3fb1e1476d0a")
         static final int kLeftRumble_val = 0;

        @objid ("33777697-d275-49ec-84be-cc6756c6af52")
         static final int kRightRumble_val = 1;

        /**
         * Left Rumble
         */
        @objid ("75820b05-5eeb-4ce7-b7d9-b484e9971d2b")
        public static final RumbleType kLeftRumble = new RumbleType((kLeftRumble_val));

        /**
         * Right Rumble
         */
        @objid ("46272ac9-8592-426d-9f72-1e2cc9f859dc")
        public static final RumbleType kRightRumble = new RumbleType(kRightRumble_val);

        @objid ("8c83791f-784b-43db-ad35-e8a32941ebf6")
        private RumbleType(int value) {
            this.value = value;
        }

    }

}
