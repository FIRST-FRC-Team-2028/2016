/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary;
import edu.wpi.first.wpilibj.communication.HALAllianceStationID;
import edu.wpi.first.wpilibj.communication.HALControlWord;
import edu.wpi.first.wpilibj.hal.HALUtil;
import edu.wpi.first.wpilibj.hal.PowerJNI;

/**
 * Provide access to the network communication data to / from the Driver Station.
 */
@objid ("6db8d238-885a-42a8-8e85-a57a8050bed2")
public class DriverStation implements edu.wpi.first.wpilibj.RobotState.Interface {
    /**
     * Number of Joystick Ports
     */
    @objid ("14c5866d-45ea-4561-9af6-e3214316a2d6")
    public static final int kJoystickPorts = 6;

    @objid ("73206f9e-8087-45f9-a24f-b5369f5ff502")
    private static final double JOYSTICK_UNPLUGGED_MESSAGE_INTERVAL = 1.0;

    @objid ("9a34dc84-1bb7-427f-8359-12025c36e824")
    private double m_nextMessageTime = 0.0;

    @objid ("9b7ec0b3-a67b-4ecd-b48e-2b91240392ec")
    private short[][] m_joystickAxes = new short[kJoystickPorts][FRCNetworkCommunicationsLibrary.kMaxJoystickAxes];

    @objid ("b7971f3d-4406-4154-863e-ec872ec7fe87")
    private short[][] m_joystickPOVs = new short[kJoystickPorts][FRCNetworkCommunicationsLibrary.kMaxJoystickPOVs];

    @objid ("5a302c0c-dc9d-458c-a614-19630f8fea35")
    private Thread m_thread;

    @objid ("ca27fcda-066d-4283-a1c0-bde56441eca5")
    private final Object m_dataSem;

    @objid ("8dd9a821-cb70-49f1-9530-867626aa24cd")
    private volatile boolean m_thread_keepalive = true;

    @objid ("6b2a0eab-125b-48a2-820e-11f5aa327043")
    private boolean m_userInDisabled = false;

    @objid ("8c16f014-6ca3-4ae8-8fc6-ef5a8eae14e3")
    private boolean m_userInAutonomous = false;

    @objid ("65d2867d-f37f-41c7-997c-6e86686c4b0b")
    private boolean m_userInTeleop = false;

    @objid ("8ab95692-9daf-467e-90ab-f042552176e2")
    private boolean m_userInTest = false;

    @objid ("9f480b11-f0f6-4e7c-9616-77cfed1de8e8")
    private boolean m_newControlData;

    @objid ("f66f94de-72fa-43f9-a35a-e3e0cfd2ce3e")
    private final ByteBuffer m_packetDataAvailableMutex;

    @objid ("e95d8c18-8b8f-4d27-84ad-d81d82e2339c")
    private final ByteBuffer m_packetDataAvailableSem;

/* DriverStationTask */
    @objid ("3be8db4d-2383-4232-90b9-781273603f3a")
    private static DriverStation instance = new DriverStation();

    @objid ("5f4dfcf6-6e4f-4d47-b421-ee775cdb47d6")
    private HALJoystickButtons[] m_joystickButtons = new HALJoystickButtons[kJoystickPorts];

    /**
     * Gets an instance of the DriverStation
     * @return The DriverStation.
     */
    @objid ("65778487-e643-4c06-959e-3c0b2861d883")
    public static DriverStation getInstance() {
        return DriverStation.instance;
    }

    /**
     * DriverStation constructor.
     * 
     * The single DriverStation instance is created statically with the
     * instance static member variable.
     */
    @objid ("71915aa8-b134-458a-9821-9340ffc12241")
    protected DriverStation() {
        m_dataSem = new Object();
        for(int i=0; i<kJoystickPorts; i++)
        {
            m_joystickButtons[i] = new HALJoystickButtons();
        }
        
        m_packetDataAvailableMutex = HALUtil.initializeMutexNormal();
        m_packetDataAvailableSem = HALUtil.initializeMultiWait();
        FRCNetworkCommunicationsLibrary.setNewDataSem(m_packetDataAvailableSem);
        
        m_thread = new Thread(new DriverStationTask(this), "FRCDriverStation");
        m_thread.setPriority((Thread.NORM_PRIORITY + Thread.MAX_PRIORITY) / 2);
        
        m_thread.start();
    }

    /**
     * Kill the thread
     */
    @objid ("b8365783-2e1b-4148-983d-505485cd66ac")
    public void release() {
        m_thread_keepalive = false;
    }

    /**
     * Provides the service routine for the DS polling thread.
     */
    @objid ("8ab21332-bfd0-4250-a595-99044738400d")
    private void task() {
        int safetyCounter = 0;
        while (m_thread_keepalive) {
            HALUtil.takeMultiWait(m_packetDataAvailableSem, m_packetDataAvailableMutex, 0);
            synchronized (this) {
                getData();
            }
            synchronized (m_dataSem) {
                m_dataSem.notifyAll();
            }
            if (++safetyCounter >= 4) {
                MotorSafetyHelper.checkMotors();
                safetyCounter = 0;
            }
            if (m_userInDisabled) {
                FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationObserveUserProgramDisabled();
            }
            if (m_userInAutonomous) {
                FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationObserveUserProgramAutonomous();
            }
            if (m_userInTeleop) {
                FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationObserveUserProgramTeleop();
            }
            if (m_userInTest) {
                FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationObserveUserProgramTest();
            }
        }
    }

    /**
     * Wait for new data from the driver station.
     */
    @objid ("600b3bfa-a5f6-40da-a8e0-bf21317fa6ca")
    public void waitForData() {
        waitForData(0);
    }

    /**
     * Wait for new data or for timeout, which ever comes first.  If timeout is
     * 0, wait for new data only.
     * @param timeout The maximum time in milliseconds to wait.
     */
    @objid ("92e9b865-6ad7-4574-ac51-208578836ad3")
    public void waitForData(long timeout) {
        synchronized (m_dataSem) {
            try {
                m_dataSem.wait(timeout);
            } catch (InterruptedException ex) {
            }
        }
    }

    /**
     * Copy data from the DS task for the user.
     * If no new data exists, it will just be returned, otherwise
     * the data will be copied from the DS polling loop.
     */
    @objid ("fe4ee573-b84a-49d5-b330-71e186b0e225")
    protected synchronized void getData() {
        // Get the status of all of the joysticks
        for(byte stick = 0; stick < kJoystickPorts; stick++) {
            m_joystickAxes[stick] = FRCNetworkCommunicationsLibrary.HALGetJoystickAxes(stick);
            m_joystickPOVs[stick] = FRCNetworkCommunicationsLibrary.HALGetJoystickPOVs(stick);
            ByteBuffer countBuffer = ByteBuffer.allocateDirect(1);
            m_joystickButtons[stick].buttons = FRCNetworkCommunicationsLibrary.HALGetJoystickButtons((byte)stick, countBuffer);
            m_joystickButtons[stick].count = countBuffer.get();
        }
        
        m_newControlData = true;
    }

    /**
     * Read the battery voltage.
     * @return The battery voltage in Volts.
     */
    @objid ("8faaecd9-dbe2-41e5-be25-e41257bd8c57")
    public double getBatteryVoltage() {
        IntBuffer status = ByteBuffer.allocateDirect(4).asIntBuffer();
        float voltage = PowerJNI.getVinVoltage(status);
        HALUtil.checkStatus(status);
        return voltage;
    }

    /**
     * Reports errors related to unplugged joysticks
     * Throttles the errors so that they don't overwhelm the DS
     */
    @objid ("1627975b-94d2-4daa-ac84-c00060d76eed")
    private void reportJoystickUnpluggedError(String message) {
        double currentTime = Timer.getFPGATimestamp();
        if (currentTime > m_nextMessageTime) {
            reportError(message, false);
            m_nextMessageTime = currentTime + JOYSTICK_UNPLUGGED_MESSAGE_INTERVAL;
        }
    }

    /**
     * Get the value of the axis on a joystick.
     * This depends on the mapping of the joystick connected to the specified port.
     * @param stick The joystick to read.
     * @param axis The analog axis value to read from the joystick.
     * @return The value of the axis on the joystick.
     */
    @objid ("a8b0192b-9ba1-4290-a547-8e91b0c7b21d")
    public synchronized double getStickAxis(int stick, int axis) {
        if(stick < 0 || stick >= kJoystickPorts) {
            throw new RuntimeException("Joystick index is out of range, should be 0-5");
        }
        
        if (axis < 0 || axis >= FRCNetworkCommunicationsLibrary.kMaxJoystickAxes) {
            throw new RuntimeException("Joystick axis is out of range");
        }
        
        if (axis >= m_joystickAxes[stick].length) {
            reportJoystickUnpluggedError("WARNING: Joystick axis " + axis + " on port " + stick + " not available, check if controller is plugged in\n");
            return 0.0;
        }
        
        byte value = (byte)m_joystickAxes[stick][axis];
        
        if(value < 0) {
            return value / 128.0;
        } else {
            return value / 127.0;
        }
    }

    /**
     * Returns the number of axes on a given joystick port
     * @param stick The joystick port number
     * @return The number of axes on the indicated joystick
     */
    @objid ("8e992da4-b1f8-4ecb-9a17-4bf0be2692f8")
    public synchronized int getStickAxisCount(int stick) {
        if(stick < 0 || stick >= kJoystickPorts) {
            throw new RuntimeException("Joystick index is out of range, should be 0-5");
        }
        return m_joystickAxes[stick].length;
    }

    /**
     * Get the state of a POV on the joystick.
     * @return the angle of the POV in degrees, or -1 if the POV is not pressed.
     */
    @objid ("9032d992-5402-4236-8f80-3cee3e0963b1")
    public synchronized int getStickPOV(int stick, int pov) {
        if(stick < 0 || stick >= kJoystickPorts) {
            throw new RuntimeException("Joystick index is out of range, should be 0-5");
        }
        
        if (pov < 0 || pov >= FRCNetworkCommunicationsLibrary.kMaxJoystickPOVs) {
            throw new RuntimeException("Joystick POV is out of range");
        }
        
        if (pov >= m_joystickPOVs[stick].length) {
            reportJoystickUnpluggedError("WARNING: Joystick POV " + pov + " on port " + stick + " not available, check if controller is plugged in\n");
            return -1;
        }
        return m_joystickPOVs[stick][pov];
    }

    /**
     * Returns the number of POVs on a given joystick port
     * @param stick The joystick port number
     * @return The number of POVs on the indicated joystick
     */
    @objid ("f48c6a87-6b7c-461f-9267-c4afc466a278")
    public synchronized int getStickPOVCount(int stick) {
        if(stick < 0 || stick >= kJoystickPorts) {
            throw new RuntimeException("Joystick index is out of range, should be 0-5");
        }
        return m_joystickPOVs[stick].length;
    }

    /**
     * The state of the buttons on the joystick.
     * @param stick The joystick to read.
     * @return The state of the buttons on the joystick.
     */
    @objid ("2a734b10-c1f4-4698-9d83-f2c9d78c17b3")
    public synchronized int getStickButtons(final int stick) {
        if(stick < 0 || stick >= kJoystickPorts) {
            throw new RuntimeException("Joystick index is out of range, should be 0-3");
        }
        return m_joystickButtons[stick].buttons;
    }

    /**
     * The state of one joystick button. Button indexes begin at 1.
     * @param stick The joystick to read.
     * @param button The button index, beginning at 1.
     * @return The state of the joystick button.
     */
    @objid ("2457092b-1e57-4333-bda3-0c59b1e609dd")
    public synchronized boolean getStickButton(final int stick, byte button) {
        if(stick < 0 || stick >= kJoystickPorts) {
            throw new RuntimeException("Joystick index is out of range, should be 0-3");
        }
        
        
        if(button > m_joystickButtons[stick].count) {
            reportJoystickUnpluggedError("WARNING: Joystick Button " + button + " on port " + stick + " not available, check if controller is plugged in\n");
            return false;
        }
        if(button <= 0)
        {
            reportJoystickUnpluggedError("ERROR: Button indexes begin at 1 in WPILib for C++ and Java\n");
            return false;
        }
        return ((0x1 << (button - 1)) & m_joystickButtons[stick].buttons) != 0;
    }

    /**
     * Gets the number of buttons on a joystick
     * @param  stick The joystick port number
     * @return The number of buttons on the indicated joystick
     */
    @objid ("4d6b23f5-6cb5-4488-bc9f-7459488540ee")
    public synchronized int getStickButtonCount(int stick) {
        if(stick < 0 || stick >= kJoystickPorts) {
            throw new RuntimeException("Joystick index is out of range, should be 0-5");
        }
        return m_joystickButtons[stick].count;
    }

    /**
     * Gets a value indicating whether the Driver Station requires the
     * robot to be enabled.
     * @return True if the robot is enabled, false otherwise.
     */
    @objid ("71ceacbd-5f42-4d3b-9336-6e971874eddf")
    public boolean isEnabled() {
        HALControlWord controlWord = FRCNetworkCommunicationsLibrary.HALGetControlWord();
        return controlWord.getEnabled() && controlWord.getDSAttached();
    }

    /**
     * Gets a value indicating whether the Driver Station requires the
     * robot to be disabled.
     * @return True if the robot should be disabled, false otherwise.
     */
    @objid ("a4900cdc-56c8-425d-9310-d2456418d4eb")
    public boolean isDisabled() {
        return !isEnabled();
    }

    /**
     * Gets a value indicating whether the Driver Station requires the
     * robot to be running in autonomous mode.
     * @return True if autonomous mode should be enabled, false otherwise.
     */
    @objid ("ae2ab33a-1568-43f6-8676-8894d87abaac")
    public boolean isAutonomous() {
        HALControlWord controlWord = FRCNetworkCommunicationsLibrary.HALGetControlWord();
        return controlWord.getAutonomous();
    }

    /**
     * Gets a value indicating whether the Driver Station requires the
     * robot to be running in test mode.
     * @return True if test mode should be enabled, false otherwise.
     */
    @objid ("d1fd6ea2-9759-4220-bba8-b38416b9655d")
    public boolean isTest() {
        HALControlWord controlWord = FRCNetworkCommunicationsLibrary.HALGetControlWord();
        return controlWord.getTest();
    }

    /**
     * Gets a value indicating whether the Driver Station requires the
     * robot to be running in operator-controlled mode.
     * @return True if operator-controlled mode should be enabled, false otherwise.
     */
    @objid ("1e8b84a9-4f4e-4773-8130-5eefb19bb5da")
    public boolean isOperatorControl() {
        return !(isAutonomous() || isTest());
    }

    /**
     * Gets a value indicating whether the FPGA outputs are enabled. The outputs may be disabled
     * if the robot is disabled or e-stopped, the watdhog has expired, or if the roboRIO browns out.
     * @return True if the FPGA outputs are enabled.
     */
    @objid ("a12ed2eb-bc35-4b22-93e2-2870f0e88db9")
    public boolean isSysActive() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        boolean retVal = FRCNetworkCommunicationsLibrary.HALGetSystemActive(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retVal;
    }

    /**
     * Check if the system is browned out.
     * @return True if the system is browned out
     */
    @objid ("d12259af-4a65-415d-aa59-99c8e7171ef9")
    public boolean isBrownedOut() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        boolean retVal = FRCNetworkCommunicationsLibrary.HALGetBrownedOut(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retVal;
    }

    /**
     * Has a new control packet from the driver station arrived since the last time this function was called?
     * @return True if the control data has been updated since the last call.
     */
    @objid ("5a0bfb88-452f-4b29-931e-634866d30841")
    public synchronized boolean isNewControlData() {
        boolean result = m_newControlData;
        m_newControlData = false;
        return result;
    }

    /**
     * Get the current alliance from the FMS
     * @return the current alliance
     */
    @objid ("a2f69820-fe01-4f81-b247-3556fa00e80f")
    public Alliance getAlliance() {
        HALAllianceStationID allianceStationID = FRCNetworkCommunicationsLibrary.HALGetAllianceStation();
        if(allianceStationID == null) {
            return Alliance.Invalid;
        }
        
        switch (allianceStationID) {
            case Red1:
            case Red2:
            case Red3:
                return Alliance.Red;
        
            case Blue1:
            case Blue2:
            case Blue3:
                return Alliance.Blue;
        
            default:
                return Alliance.Invalid;
        }
    }

    /**
     * Gets the location of the team's driver station controls.
     * @return the location of the team's driver station controls: 1, 2, or 3
     */
    @objid ("ff371567-7feb-4768-9e96-6324b5ddca13")
    public int getLocation() {
        HALAllianceStationID allianceStationID = FRCNetworkCommunicationsLibrary.HALGetAllianceStation();
        if(allianceStationID == null) {
            return 0;
        }
        switch (allianceStationID) {
            case Red1:
            case Blue1:
                return 1;
        
            case Red2:
            case Blue2:
                return 2;
        
            case Blue3:
            case Red3:
                return 3;
        
            default:
                return 0;
        }
    }

    /**
     * Is the driver station attached to a Field Management System?
     * Note: This does not work with the Blue DS.
     * @return True if the robot is competing on a field being controlled by a Field Management System
     */
    @objid ("634da0d1-58a5-42bd-9be7-b231fcf3d884")
    public boolean isFMSAttached() {
        HALControlWord controlWord = FRCNetworkCommunicationsLibrary.HALGetControlWord();
        return controlWord.getFMSAttached();
    }

    @objid ("0fae8d42-02bf-4195-8825-1b7520847136")
    public boolean isDSAttached() {
        HALControlWord controlWord = FRCNetworkCommunicationsLibrary.HALGetControlWord();
        return controlWord.getDSAttached();
    }

    /**
     * Return the approximate match time
     * The FMS does not send an official match time to the robots, but does send an approximate match time.
     * The value will count down the time remaining in the current period (auto or teleop).
     * Warning: This is not an official time (so it cannot be used to dispute ref calls or guarantee that a function
     * will trigger before the match ends)
     * The Practice Match function of the DS approximates the behaviour seen on the field.
     * @return Time remaining in current match period (auto or teleop) in seconds
     */
    @objid ("858aca20-a14e-4181-b02f-99475ab7803b")
    public double getMatchTime() {
        return FRCNetworkCommunicationsLibrary.HALGetMatchTime();
    }

    /**
     * Report error to Driver Station.
     * Also prints error to System.err
     * Optionally appends Stack trace to error message
     * @param printTrace If true, append stack trace to error string
     */
    @objid ("af98880a-f33a-42da-b639-f966c959b655")
    public static void reportError(String error, boolean printTrace) {
        String errorString = error;
        if(printTrace) {
            errorString += " at ";
            StackTraceElement[] traces = Thread.currentThread().getStackTrace();
            for (int i=2; i<traces.length; i++)
            {
                errorString += traces[i].toString() + "\n";
            }
        }
        System.err.println(errorString);
        HALControlWord controlWord = FRCNetworkCommunicationsLibrary.HALGetControlWord();
        if(controlWord.getDSAttached()) {
            FRCNetworkCommunicationsLibrary.HALSetErrorData(errorString);
        }
    }

    /**
     * Only to be used to tell the Driver Station what code you claim to be executing
     * for diagnostic purposes only
     * @param entering If true, starting disabled code; if false, leaving disabled code
     */
    @objid ("36496188-ff59-4b63-ad6d-2c71fe9bbe9c")
    public void InDisabled(boolean entering) {
        m_userInDisabled=entering;
    }

    /**
     * Only to be used to tell the Driver Station what code you claim to be executing
     * for diagnostic purposes only
     * @param entering If true, starting autonomous code; if false, leaving autonomous code
     */
    @objid ("8449113f-5f3a-44f8-b139-f9f487ec45a1")
    public void InAutonomous(boolean entering) {
        m_userInAutonomous=entering;
    }

    /**
     * Only to be used to tell the Driver Station what code you claim to be executing
     * for diagnostic purposes only
     * @param entering If true, starting teleop code; if false, leaving teleop code
     */
    @objid ("92b7c639-de6e-43b2-802a-91049f61f223")
    public void InOperatorControl(boolean entering) {
        m_userInTeleop=entering;
    }

    /**
     * Only to be used to tell the Driver Station what code you claim to be executing
     * for diagnostic purposes only
     * @param entering If true, starting test code; if false, leaving test code
     */
    @objid ("f5ad40ac-e688-4676-b4e3-c4de64873a22")
    public void InTest(boolean entering) {
        m_userInTest = entering;
    }

    @objid ("cf83e769-eba7-4961-889e-43cd5fa5ff99")
    private class HALJoystickButtons {
        @objid ("536ff32c-f738-4284-887e-7f265b51bbc5")
        public int buttons;

        @objid ("539f3436-9501-4dc3-8e78-39586152e516")
        public byte count;

    }

    @objid ("27dd7302-2bc3-45f4-822b-583d9b7ed0d7")
    private static class DriverStationTask implements Runnable {
        @objid ("cccf6762-08ab-412e-862c-e910052453e6")
        private DriverStation m_ds;

        @objid ("4ba47f84-9520-48b8-8eb1-d9b617bf88c6")
        DriverStationTask(DriverStation ds) {
            m_ds = ds;
        }

        @objid ("c198eed9-565d-4563-8da2-05d5ad71cc74")
        public void run() {
            m_ds.task();
        }

    }

    /**
     * The robot alliance that the robot is a part of
     */
    @objid ("071b35ce-441e-4fb9-bb22-2e68beaaf908")
    public enum Alliance {
        Red,
        Blue,
        Invalid;
    }

}
