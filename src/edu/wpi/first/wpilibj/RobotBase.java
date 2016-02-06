/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.Manifest;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.Utility;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tInstances;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.internal.HardwareHLUsageReporting;
import edu.wpi.first.wpilibj.internal.HardwareTimer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * Implement a Robot Program framework.
 * The RobotBase class is intended to be subclassed by a user creating a robot program.
 * Overridden autonomous() and operatorControl() methods are called at the appropriate time
 * as the match proceeds. In the current implementation, the Autonomous code will run to
 * completion before the OperatorControl code could start. In the future the Autonomous code
 * might be spawned as a task, then killed at the end of the Autonomous period.
 */
@objid ("341b13f9-249b-483a-95ff-a679cb435c7c")
public abstract class RobotBase {
    /**
     * The VxWorks priority that robot code should work at (so Java code should run at)
     */
    @objid ("3b042cff-9bf8-47a5-b713-72f24d08eb5c")
    public static final int ROBOT_TASK_PRIORITY = 101;

    @objid ("f9558b52-17be-4dcf-a6b1-15668980b797")
    protected final DriverStation m_ds;

    /**
     * Constructor for a generic robot program.
     * User code should be placed in the constructor that runs before the Autonomous or Operator
     * Control period starts. The constructor will run to completion before Autonomous is entered.
     * 
     * This must be used to ensure that the communications code starts. In the future it would be
     * nice to put this code into it's own task that loads on boot so ensure that it runs.
     */
    @objid ("8f5835d2-75ef-4ab9-babd-30fad17e8548")
    protected RobotBase() {
        // TODO: StartCAPI();
        // TODO: See if the next line is necessary
        // Resource.RestartProgram();
        
        NetworkTable.setServerMode();//must be before b
        m_ds = DriverStation.getInstance();
        NetworkTable.getTable("");  // forces network tables to initialize
        NetworkTable.getTable("LiveWindow").getSubTable("~STATUS~").putBoolean("LW Enabled", false);
    }

    /**
     * Free the resources for a RobotBase class.
     */
    @objid ("aee1c237-267a-4303-aa43-b9f5aeda20d7")
    public void free() {
    }

    /**
     * @return If the robot is running in simulation.
     */
    @objid ("b70a49ca-f6d0-4a18-b8cc-649caeaf03fa")
    public static boolean isSimulation() {
        return false;
    }

    /**
     * @return If the robot is running in the real world.
     */
    @objid ("26748904-2b63-4e04-9c95-51bdd3012347")
    public static boolean isReal() {
        return true;
    }

    /**
     * Determine if the Robot is currently disabled.
     * @return True if the Robot is currently disabled by the field controls.
     */
    @objid ("dca19f82-3ba2-4553-82d8-13d124d1355e")
    public boolean isDisabled() {
        return m_ds.isDisabled();
    }

    /**
     * Determine if the Robot is currently enabled.
     * @return True if the Robot is currently enabled by the field controls.
     */
    @objid ("82cb26d7-0077-4f84-ad9d-03276e1177c6")
    public boolean isEnabled() {
        return m_ds.isEnabled();
    }

    /**
     * Determine if the robot is currently in Autonomous mode.
     * @return True if the robot is currently operating Autonomously as determined by the field controls.
     */
    @objid ("a74b0a7e-2cb0-4731-8093-60b22a08c1a3")
    public boolean isAutonomous() {
        return m_ds.isAutonomous();
    }

    /**
     * Determine if the robot is currently in Test mode
     * @return True if the robot is currently operating in Test mode as determined by the driver station.
     */
    @objid ("2f87bf7f-ade3-4f26-9be2-a57ad0eaedb2")
    public boolean isTest() {
        return m_ds.isTest();
    }

    /**
     * Determine if the robot is currently in Operator Control mode.
     * @return True if the robot is currently operating in Tele-Op mode as determined by the field controls.
     */
    @objid ("1b383e7f-c4a3-4608-97e2-e9e982fb0294")
    public boolean isOperatorControl() {
        return m_ds.isOperatorControl();
    }

    /**
     * Indicates if new data is available from the driver station.
     * @return Has new data arrived over the network since the last time this function was called?
     */
    @objid ("bf794f3b-daaa-440f-b980-6dc072a45cac")
    public boolean isNewDataAvailable() {
        return m_ds.isNewControlData();
    }

    /**
     * Provide an alternate "main loop" via startCompetition().
     */
    @objid ("f35366d6-76bb-4237-91bb-60baa81bdf38")
    public abstract void startCompetition();

    /**
     * This hook is called right before startCompetition(). By default, tell the
     * DS that the robot is now ready to be enabled. If you don't want for the
     * robot to be enabled yet, you can override this method to do nothing.
     * If you do so, you will need to call
     * FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationOvserveUserProgramStarting() from
     * your code when you are ready for the robot to be enabled.
     */
    @objid ("b97982de-4c6a-42bb-856b-895a5f5ebc35")
    protected void prestart() {
        FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationObserveUserProgramStarting();
    }

    @objid ("3a805b1b-354c-4e56-a21d-23b420d48542")
    public static boolean getBooleanProperty(String name, boolean defaultValue) {
        String propVal = System.getProperty(name);
        if (propVal == null) {
            return defaultValue;
        }
        if (propVal.equalsIgnoreCase("false")) {
            return false;
        } else if (propVal.equalsIgnoreCase("true")) {
            return true;
        } else {
            throw new IllegalStateException(propVal);
        }
    }

    /**
     * Common initialization for all robot programs.
     */
    @objid ("52f91d02-488d-4a6e-8c24-9d2b3df481ad")
    public static void initializeHardwareConfiguration() {
        FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationReserve();
        
        // Set some implementations so that the static methods work properly
        Timer.SetImplementation(new HardwareTimer());
        HLUsageReporting.SetImplementation(new HardwareHLUsageReporting());
        RobotState.SetImplementation(DriverStation.getInstance());
    }

    /**
     * Starting point for the applications.
     */
    @objid ("a2741619-1a6d-400c-89ac-e435f08ac2f0")
    public static void main(String[] args) {
        initializeHardwareConfiguration();
        
        UsageReporting.report(tResourceType.kResourceType_Language, tInstances.kLanguage_Java);
        
        String robotName = "";
        Enumeration<URL> resources = null;
        try {
            resources = RobotBase.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
        } catch (IOException e) {e.printStackTrace();}
        while (resources != null && resources.hasMoreElements()) {
            try {
                Manifest manifest = new Manifest(resources.nextElement().openStream());
                robotName = manifest.getMainAttributes().getValue("Robot-Class");
            } catch (IOException e) {e.printStackTrace();}
        }
        
        RobotBase robot;
        try {
            robot = (RobotBase) Class.forName(robotName).newInstance();
            robot.prestart();
        } catch (Throwable t) {
            DriverStation.reportError("ERROR Unhandled exception instantiating robot " + robotName + " " + t.toString() + " at " + Arrays.toString(t.getStackTrace()), false);
            System.err.println("WARNING: Robots don't quit!");
            System.err.println("ERROR: Could not instantiate robot " + robotName + "!");
            System.exit(1);
            return;
        }
        
        File file = null;
            FileOutputStream output = null;
            try {
                file = new File("/tmp/frc_versions/FRC_Lib_Version.ini");
        
                if (file.exists())
                    file.delete();
        
                file.createNewFile();
        
                output = new FileOutputStream(file);
        
                output.write("2015 Java 1.2.0".getBytes());
        
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException ex) {
                    }
                }
            }
        
        boolean errorOnExit = false;
        try {
            robot.startCompetition();
        } catch (Throwable t) {
            DriverStation.reportError("ERROR Unhandled exception: " + t.toString() + " at " + Arrays.toString(t.getStackTrace()), false);
            errorOnExit = true;
        } finally {
            // startCompetition never returns unless exception occurs....
            System.err.println("WARNING: Robots don't quit!");
            if (errorOnExit) {
                System.err.println("---> The startCompetition() method (or methods called by it) should have handled the exception above.");
            } else {
                System.err.println("---> Unexpected return from startCompetition() method.");
            }
        }
        System.exit(1);
    }

}
