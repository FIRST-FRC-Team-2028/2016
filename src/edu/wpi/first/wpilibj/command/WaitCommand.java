/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.command;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * A {@link WaitCommand} will wait for a certain amount of time before finishing.
 * It is useful if you want a {@link CommandGroup} to pause for a moment.
 * @author Joe Grinstead
 * @see CommandGroup
 */
@objid ("efc0a42a-b917-4276-b3f7-cab34117d9fd")
public class WaitCommand extends Command {
    /**
     * Instantiates a {@link WaitCommand} with the given timeout.
     * @param timeout the time the command takes to run
     */
    @objid ("28038c8c-e0cc-4870-84b6-b4b302d2e1ba")
    public WaitCommand(double timeout) {
        this("Wait(" + timeout + ")", timeout);
    }

    /**
     * Instantiates a {@link WaitCommand} with the given timeout.
     * @param name the name of the command
     * @param timeout the time the command takes to run
     */
    @objid ("a7b9731b-b9a0-4e03-8091-60c927abb566")
    public WaitCommand(String name, double timeout) {
        super(name, timeout);
    }

    @objid ("49b75115-b921-4121-aadd-c9571a05fcb8")
    protected void initialize() {
    }

    @objid ("d5f7df0e-065b-4d6a-9140-09fdd55a83a9")
    protected void execute() {
    }

    @objid ("9a221954-4f55-4da9-b6ba-1b59f97e5010")
    protected boolean isFinished() {
        return isTimedOut();
    }

    @objid ("f32086b2-f9b5-41a9-adae-1985ddc9e4d4")
    protected void end() {
    }

    @objid ("fbb28731-3daa-415c-883c-5a13a9bd082f")
    protected void interrupted() {
    }

}
