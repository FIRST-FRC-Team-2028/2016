/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.command;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.Timer;

/**
 * WaitUntilCommand - waits until an absolute game time.
 * This will wait until the game clock reaches some value, then continue to the next command.
 * @author brad
 */
@objid ("12b76233-15a2-4427-905a-aa4ce279a076")
public class WaitUntilCommand extends Command {
    @objid ("1139fb3f-7670-4049-b071-2ca914bd6288")
    private double m_time;

    @objid ("310d48e2-8a45-4ff6-ab89-e3b0093e1357")
    public WaitUntilCommand(double time) {
        super("WaitUntil(" + time + ")");
        m_time = time;
    }

    @objid ("3a78106e-e30a-4956-ad89-be4ba2e9c7fb")
    public void initialize() {
    }

    @objid ("d8c34201-6a57-4b42-a90f-e40f75293884")
    public void execute() {
    }

    /**
     * Check if we've reached the actual finish time.
     */
    @objid ("a49eb11e-2a4e-46e9-8741-5c8cdef2132c")
    public boolean isFinished() {
        return Timer.getMatchTime() >= m_time;
    }

    @objid ("4cb41d7e-acb2-4ddc-8261-a91f53bed5e0")
    public void end() {
    }

    @objid ("7763c0ff-56f6-4a2b-b9f2-4647d1fd1b24")
    public void interrupted() {
    }

}
