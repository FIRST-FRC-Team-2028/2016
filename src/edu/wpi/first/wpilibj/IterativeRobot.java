/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tInstances;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * IterativeRobot implements a specific type of Robot Program framework, extending the RobotBase class.
 * 
 * The IterativeRobot class is intended to be subclassed by a user creating a robot program.
 * 
 * This class is intended to implement the "old style" default code, by providing
 * the following functions which are called by the main loop, startCompetition(), at the appropriate times:
 * 
 * robotInit() -- provide for initialization at robot power-on
 * 
 * init() functions -- each of the following functions is called once when the
 * appropriate mode is entered:
 * - DisabledInit()   -- called only when first disabled
 * - AutonomousInit() -- called each and every time autonomous is entered from another mode
 * - TeleopInit()     -- called each and every time teleop is entered from another mode
 * - TestInit()       -- called each and every time test mode is entered from anothermode
 * 
 * Periodic() functions -- each of these functions is called iteratively at the
 * appropriate periodic rate (aka the "slow loop").  The period of
 * the iterative robot is synced to the driver station control packets,
 * giving a periodic frequency of about 50Hz (50 times per second).
 * - disabledPeriodic()
 * - autonomousPeriodic()
 * - teleopPeriodic()
 * - testPeriodoc()
 */
@objid ("bb0c3fd0-b7a5-40ef-85c4-99bed2cc668f")
public class IterativeRobot extends RobotBase {
    @objid ("15a251de-36bd-404a-82f0-dc465c0b81aa")
    private boolean m_disabledInitialized;

    @objid ("590dd1f1-e40c-480f-9bb8-8934fc90a5d1")
    private boolean m_autonomousInitialized;

    @objid ("62f2480c-425a-4c46-ba36-d8e527c0eb18")
    private boolean m_teleopInitialized;

    @objid ("7668b3fd-9344-4501-a7e4-49a67025b909")
    private boolean m_testInitialized;

/* ----------- Overridable periodic code -----------------*/
    @objid ("05da488d-ff67-49ad-a590-f722bd751de9")
    private boolean dpFirstRun = true;

    @objid ("85a5304d-e235-4314-b462-0b19e29f9ba0")
    private boolean apFirstRun = true;

    @objid ("fa442dc9-bcff-496b-94d9-52fe1c1745b2")
    private boolean tpFirstRun = true;

    @objid ("746aa2dd-9749-4bfc-9a9f-d77b43521a35")
    private boolean tmpFirstRun = true;

    /**
     * Constructor for RobotIterativeBase
     * 
     * The constructor initializes the instance variables for the robot to indicate
     * the status of initialization for disabled, autonomous, and teleop code.
     */
    @objid ("de53ba92-b0c4-4c3b-9496-2d4638b88aea")
    public IterativeRobot() {
        // set status for initialization of disabled, autonomous, and teleop code.
        m_disabledInitialized = false;
        m_autonomousInitialized = false;
        m_teleopInitialized = false;
        m_testInitialized = false;
    }

    @objid ("45d15341-16df-4a12-899f-84803571a31f")
    @Override
    protected void prestart() {
        // Don't immediately say that the robot's ready to be enabled.
        // See below.
    }

    /**
     * Provide an alternate "main loop" via startCompetition().
     */
    @objid ("5691c864-c49a-4630-8d4a-6382acfac794")
    public void startCompetition() {
        UsageReporting.report(tResourceType.kResourceType_Framework, tInstances.kFramework_Iterative);
        
        robotInit();
        
        // We call this now (not in prestart like default) so that the robot
        // won't enable until the initialization has finished. This is useful
        // because otherwise it's sometimes possible to enable the robot
        // before the code is ready. 
        FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationObserveUserProgramStarting();
        
        // loop forever, calling the appropriate mode-dependent function
        LiveWindow.setEnabled(false);
        while (true) {
            // Call the appropriate function depending upon the current robot mode
            if (isDisabled()) {
                // call DisabledInit() if we are now just entering disabled mode from
                // either a different mode or from power-on
                if (!m_disabledInitialized) {
                    LiveWindow.setEnabled(false);
                    disabledInit();
                    m_disabledInitialized = true;
                    // reset the initialization flags for the other modes
                    m_autonomousInitialized = false;
                    m_teleopInitialized = false;
                    m_testInitialized = false;
                }
                if (nextPeriodReady()) {
                    FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationObserveUserProgramDisabled();
                    disabledPeriodic();
                }
            } else if (isTest()) {
                // call TestInit() if we are now just entering test mode from either
                // a different mode or from power-on
                if (!m_testInitialized) {
                    LiveWindow.setEnabled(true);
                    testInit();
                    m_testInitialized = true;
                    m_autonomousInitialized = false;
                    m_teleopInitialized = false;
                    m_disabledInitialized = false;
                }
                if (nextPeriodReady()) {
                    FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationObserveUserProgramTest();
                    testPeriodic();
                }
            } else if (isAutonomous()) {
                // call Autonomous_Init() if this is the first time
                // we've entered autonomous_mode
                if (!m_autonomousInitialized) {
                    LiveWindow.setEnabled(false);
                    // KBS NOTE: old code reset all PWMs and relays to "safe values"
                    // whenever entering autonomous mode, before calling
                    // "Autonomous_Init()"
                    autonomousInit();
                    m_autonomousInitialized = true;
                    m_testInitialized = false;
                    m_teleopInitialized = false;
                    m_disabledInitialized = false;
                }
                if (nextPeriodReady()) {
                    FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationObserveUserProgramAutonomous();
                    autonomousPeriodic();
                }
            } else {
                // call Teleop_Init() if this is the first time
                // we've entered teleop_mode
                if (!m_teleopInitialized) {
                    LiveWindow.setEnabled(false);
                    teleopInit();
                    m_teleopInitialized = true;
                    m_testInitialized = false;
                    m_autonomousInitialized = false;
                    m_disabledInitialized = false;
                }
                if (nextPeriodReady()) {
                    FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationObserveUserProgramTeleop();
                    teleopPeriodic();
                }
            }
            m_ds.waitForData();
        }
    }

    /**
     * Determine if the appropriate next periodic function should be called.
     * Call the periodic functions whenever a packet is received from the Driver Station, or about every 20ms.
     */
    @objid ("6177974e-1efe-470e-8890-9b5d2b11b98f")
    private boolean nextPeriodReady() {
        return m_ds.isNewControlData();
    }

/* ----------- Overridable initialization code -----------------*/
    /**
     * Robot-wide initialization code should go here.
     * 
     * Users should override this method for default Robot-wide initialization which will
     * be called when the robot is first powered on.  It will be called exactly 1 time.
     */
    @objid ("3b7cc6b4-d501-40d5-a8a1-8f960d295c27")
    public void robotInit() {
        System.out.println("Default IterativeRobot.robotInit() method... Overload me!");
    }

    /**
     * Initialization code for disabled mode should go here.
     * 
     * Users should override this method for initialization code which will be called each time
     * the robot enters disabled mode.
     */
    @objid ("b7273d45-2a44-4b62-8465-1bc007b417da")
    public void disabledInit() {
        System.out.println("Default IterativeRobot.disabledInit() method... Overload me!");
    }

    /**
     * Initialization code for autonomous mode should go here.
     * 
     * Users should override this method for initialization code which will be called each time
     * the robot enters autonomous mode.
     */
    @objid ("6ebbbf40-0791-4cdc-a061-cc14adfa18f2")
    public void autonomousInit() {
        System.out.println("Default IterativeRobot.autonomousInit() method... Overload me!");
    }

    /**
     * Initialization code for teleop mode should go here.
     * 
     * Users should override this method for initialization code which will be called each time
     * the robot enters teleop mode.
     */
    @objid ("44a86079-dd69-41d9-89d2-626c40f8a31b")
    public void teleopInit() {
        System.out.println("Default IterativeRobot.teleopInit() method... Overload me!");
    }

    /**
     * Initialization code for test mode should go here.
     * 
     * Users should override this method for initialization code which will be called each time
     * the robot enters test mode.
     */
    @objid ("717f9f64-4a7d-4414-b71f-0b44b57f2c45")
    public void testInit() {
        System.out.println("Default IterativeRobot.testInit() method... Overload me!");
    }

    /**
     * Periodic code for disabled mode should go here.
     * 
     * Users should override this method for code which will be called periodically at a regular
     * rate while the robot is in disabled mode.
     */
    @objid ("7a2ccb42-2560-443d-aeba-9bd5cc9c5b72")
    public void disabledPeriodic() {
        if (dpFirstRun) {
            System.out.println("Default IterativeRobot.disabledPeriodic() method... Overload me!");
            dpFirstRun = false;
        }
        Timer.delay(0.001);
    }

    /**
     * Periodic code for autonomous mode should go here.
     * 
     * Users should override this method for code which will be called periodically at a regular
     * rate while the robot is in autonomous mode.
     */
    @objid ("4593c051-a6fa-4425-8578-986b2b8d5b2e")
    public void autonomousPeriodic() {
        if (apFirstRun) {
            System.out.println("Default IterativeRobot.autonomousPeriodic() method... Overload me!");
            apFirstRun = false;
        }
        Timer.delay(0.001);
    }

    /**
     * Periodic code for teleop mode should go here.
     * 
     * Users should override this method for code which will be called periodically at a regular
     * rate while the robot is in teleop mode.
     */
    @objid ("6fd27227-0717-4f5e-aa14-fb3f9c28eba1")
    public void teleopPeriodic() {
        if (tpFirstRun) {
            System.out.println("Default IterativeRobot.teleopPeriodic() method... Overload me!");
            tpFirstRun = false;
        }
        Timer.delay(0.001);
    }

    /**
     * Periodic code for test mode should go here
     * 
     * Users should override this method for code which will be called periodically at a regular rate
     * while the robot is in test mode.
     */
    @objid ("3ef21057-ce5e-484d-9e9e-9e7d2c9da633")
    public void testPeriodic() {
        if (tmpFirstRun) {
            System.out.println("Default IterativeRobot.testPeriodic() method... Overload me!");
            tmpFirstRun = false;
        }
    }

}
