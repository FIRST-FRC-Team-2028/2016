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
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * A simple robot base class that knows the standard FRC competition states (disabled, autonomous, or operator controlled).
 * 
 * You can build a simple robot program off of this by overriding the
 * robotinit(), disabled(), autonomous() and operatorControl() methods. The startCompetition() method will calls these methods
 * (sometimes repeatedly).
 * depending on the state of the competition.
 * 
 * Alternatively you can override the robotMain() method and manage all aspects of the robot yourself.
 */
@objid ("04d1ff33-d0c1-4b28-b8fb-65a345df1533")
public class SampleRobot extends RobotBase {
    @objid ("a56be65d-1929-490c-a362-bd82fc59dffe")
    private boolean m_robotMainOverridden;

    /**
     * Create a new SampleRobot
     */
    @objid ("bba61257-f12e-4dcc-98fe-a453d95f4aae")
    public SampleRobot() {
        super();
        m_robotMainOverridden = true;
    }

    /**
     * Robot-wide initialization code should go here.
     * 
     * Users should override this method for default Robot-wide initialization which will
     * be called when the robot is first powered on.
     * 
     * Called exactly 1 time when the competition starts.
     */
    @objid ("f15dd0a3-ffb5-4eb6-a513-966c569485fa")
    protected void robotInit() {
        System.out.println("Default robotInit() method running, consider providing your own");
    }

    /**
     * Disabled should go here.
     * Users should overload this method to run code that should run while the field is
     * disabled.
     * 
     * Called once each time the robot enters the disabled state.
     */
    @objid ("fd2e4382-99b9-4186-8dff-c3c50d364e33")
    protected void disabled() {
        System.out.println("Default disabled() method running, consider providing your own");
    }

    /**
     * Autonomous should go here.
     * Users should add autonomous code to this method that should run while the field is
     * in the autonomous period.
     * 
     * Called once each time the robot enters the autonomous state.
     */
    @objid ("6b9bd9cd-ffbd-45be-9953-c0f37e7650f2")
    public void autonomous() {
        System.out.println("Default autonomous() method running, consider providing your own");
    }

    /**
     * Operator control (tele-operated) code should go here.
     * Users should add Operator Control code to this method that should run while the field is
     * in the Operator Control (tele-operated) period.
     * 
     * Called once each time the robot enters the operator-controlled state.
     */
    @objid ("aafae3a6-8c42-461a-8cfc-4f94234befe9")
    public void operatorControl() {
        System.out.println("Default operatorControl() method running, consider providing your own");
    }

    /**
     * Test code should go here.
     * Users should add test code to this method that should run while the robot is in test mode.
     */
    @objid ("e6c4d00e-7010-4a22-9e21-1ff5863eaaf5")
    public void test() {
        System.out.println("Default test() method running, consider providing your own");
    }

    /**
     * Robot main program for free-form programs.
     * 
     * This should be overridden by user subclasses if the intent is to not use the autonomous() and
     * operatorControl() methods. In that case, the program is responsible for sensing when to run
     * the autonomous and operator control functions in their program.
     * 
     * This method will be called immediately after the constructor is called. If it has not been
     * overridden by a user subclass (i.e. the default version runs), then the robotInit(), disabled(), autonomous() and
     * operatorControl() methods will be called.
     */
    @objid ("a48cd10f-de01-4835-bcb7-8185382ec38b")
    public void robotMain() {
        m_robotMainOverridden = false;
    }

    /**
     * Start a competition.
     * This code tracks the order of the field starting to ensure that everything happens
     * in the right order. Repeatedly run the correct method, either Autonomous or OperatorControl
     * when the robot is enabled. After running the correct method, wait for some state to change,
     * either the other mode starts or the robot is disabled. Then go back and wait for the robot
     * to be enabled again.
     */
    @objid ("e7034121-54d7-49e4-b2eb-3ea5a6e85ff0")
    public void startCompetition() {
        UsageReporting.report(tResourceType.kResourceType_Framework, tInstances.kFramework_Simple);
        
        robotMain();
        if (!m_robotMainOverridden) {
            // first and one-time initialization
            LiveWindow.setEnabled(false);
            robotInit();
        
            while (true) {
                if (isDisabled()) {
                    m_ds.InDisabled(true);
                    disabled();
                    m_ds.InDisabled(false);
                    while (isDisabled()) {
                        Timer.delay(0.01);
                    }
                } else if (isAutonomous()) {
                    m_ds.InAutonomous(true);
                    autonomous();
                    m_ds.InAutonomous(false);
                    while (isAutonomous() && !isDisabled()) {
                        Timer.delay(0.01);
                    }
                } else if (isTest()) {
                    LiveWindow.setEnabled(true);
                    m_ds.InTest(true);
                    test();
                    m_ds.InTest(false);
                    while (isTest() && isEnabled())
                        Timer.delay(0.01);
                    LiveWindow.setEnabled(false);
                } else {
                    m_ds.InOperatorControl(true);
                    operatorControl();
                    m_ds.InOperatorControl(false);
                    while (isOperatorControl() && !isDisabled()) {
                        Timer.delay(0.01);
                    }
                }
            } /* while loop */
        }
    }

}
