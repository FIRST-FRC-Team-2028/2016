/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.command;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * A {@link PrintCommand} is a command which prints out a string when it is initialized, and then immediately finishes.
 * It is useful if you want a {@link CommandGroup} to print out a string when it reaches a certain point.
 * 
 * @author Joe Grinstead
 */
@objid ("c9f7e02d-137d-4442-9014-c93d2bd7f1d4")
public class PrintCommand extends Command {
    /**
     * The message to print out
     */
    @objid ("feb21ced-1514-4031-bee3-75aec2bd63ec")
    private String message;

    /**
     * Instantiates a {@link PrintCommand} which will print the given message when it is run.
     * @param message the message to print
     */
    @objid ("9b85be2d-50b3-4723-9c1b-df4862cd563d")
    public PrintCommand(String message) {
        super("Print(\"" + message + "\"");
        this.message = message;
    }

    @objid ("5be8eae4-0681-4b60-b20f-0a59985401ed")
    protected void initialize() {
        System.out.println(message);
    }

    @objid ("5baabe50-d03c-4bc0-b273-6ab91f600263")
    protected void execute() {
    }

    @objid ("e9d5b9ab-1afc-4455-88c4-45b64a5e2d00")
    protected boolean isFinished() {
        return true;
    }

    @objid ("bae4c938-5988-44db-9916-974ce1791cd9")
    protected void end() {
    }

    @objid ("4b11b7b4-0987-43c3-9c74-f5eb6b4a7725")
    protected void interrupted() {
    }

}
