/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.command;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * This command will only finish if whatever {@link CommandGroup} it is in has no active children.
 * If it is not a part of a {@link CommandGroup}, then it will finish immediately.  If it is itself an
 * active child, then the {@link CommandGroup} will never end.
 * 
 * <p>This class is useful for the situation where you want to allow anything running in parallel to finish, before continuing
 * in the main {@link CommandGroup} sequence.</p>
 * @author Joe Grinstead
 */
@objid ("5587ca0b-fc8c-4f59-939c-f5d8fbf5ac46")
public class WaitForChildren extends Command {
    @objid ("f533fbed-4dc0-4e24-8695-d825e57adc0c")
    protected void initialize() {
    }

    @objid ("cfc85bae-b24c-479f-861c-be8b90154857")
    protected void execute() {
    }

    @objid ("818ebb90-5b41-4ebb-8d8f-a5b68e49f5b7")
    protected void end() {
    }

    @objid ("5e90d9e2-7ca9-47e8-9e52-39fd29df38f3")
    protected void interrupted() {
    }

    @objid ("b3b77ac6-83dd-456b-97cb-a144d087efd2")
    protected boolean isFinished() {
        return getGroup() == null || getGroup().m_children.isEmpty();
    }

}
