/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.command;

import java.util.Enumeration;
import java.util.Vector;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.NamedSendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * This class defines a major component of the robot.
 * 
 * <p>A good example of a subsystem is the driveline, or a claw if the robot has one.</p>
 * 
 * <p>All motors should be a part of a subsystem. For instance, all the wheel motors should be
 * a part of some kind of "Driveline" subsystem.</p>
 * 
 * <p>Subsystems are used within the command system as requirements for {@link Command}.
 * Only one command which requires a subsystem can run at a time.  Also, subsystems
 * can have default commands which are started if there is no command running which
 * requires this subsystem.</p>
 * 
 * @author Joe Grinstead
 * @see Command
 */
@objid ("d34b7f0b-5b37-4097-b67b-bd4c4908293c")
public abstract class Subsystem implements NamedSendable {
    /**
     * Whether or not getDefaultCommand() was called
     */
    @objid ("0d819874-6437-4188-8596-b4ed68851ea2")
    private boolean initializedDefaultCommand = false;

    @objid ("02b32324-f1ef-421c-ae53-b808d268bab2")
    private boolean currentCommandChanged;

    /**
     * The name
     */
    @objid ("33bcfd38-fa33-4b84-8c7c-b3f7ffe9826f")
    private String name;

    /**
     * List of all subsystems created
     */
    @objid ("05ea0b1c-aa29-4db0-bcbb-963e29c577e3")
    private static Vector allSubsystems = new Vector();

    @objid ("01339eb2-c036-4077-8750-2d9061a80689")
    private ITable table;

    /**
     * The current command
     */
    @objid ("67e93649-c466-49c1-89eb-0d4e5e92296b")
    private Command currentCommand;

    /**
     * The default command
     */
    @objid ("bf55db74-4c1c-4b9a-a62e-16180c626830")
    private Command defaultCommand;

    /**
     * Creates a subsystem with the given name
     * @param name the name of the subsystem
     */
    @objid ("f195a7d8-c02c-47c9-b059-afd512565080")
    public Subsystem(String name) {
        this.name = name;
        Scheduler.getInstance().registerSubsystem(this);
    }

    /**
     * Creates a subsystem.  This will set the name to the name of the class.
     */
    @objid ("70a681ad-61bb-4cd1-949b-bcdad4e874ec")
    public Subsystem() {
        this.name = getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1);
        Scheduler.getInstance().registerSubsystem(this);
        currentCommandChanged = true;
    }

    /**
     * Initialize the default command for a subsystem
     * By default subsystems have no default command, but if they do, the default command is set
     * with this method. It is called on all Subsystems by CommandBase in the users program after
     * all the Subsystems are created.
     */
    @objid ("68b76c08-30e1-4b74-b052-598fd5b375e4")
    protected abstract void initDefaultCommand();

    /**
     * Sets the default command.  If this is not called or is called with null,
     * then there will be no default command for the subsystem.
     * 
     * <p><b>WARNING:</b> This should <b>NOT</b> be called in a constructor if the subsystem is a
     * singleton.</p>
     * @throws IllegalUseOfCommandException if the command does not require the subsystem
     * @param command the default command (or null if there should be none)
     */
    @objid ("47fff987-0332-40dc-8482-a089a01af9c8")
    protected void setDefaultCommand(Command command) {
        if (command == null) {
            defaultCommand = null;
        } else {
            boolean found = false;
            Enumeration requirements = command.getRequirements();
            while (requirements.hasMoreElements()) {
                if (requirements.nextElement().equals(this)) {
                    found = true;
        //            } else {
        //                throw new IllegalUseOfCommandException("A default command cannot require multiple subsystems");
                }
            }
            if (!found) {
                throw new IllegalUseOfCommandException("A default command must require the subsystem");
            }
            defaultCommand = command;
        }
        if (table != null) {
            if (defaultCommand != null) {
                table.putBoolean("hasDefault", true);
                table.putString("default", defaultCommand.getName());
            } else {
                table.putBoolean("hasDefault", false);
            }
        }
    }

    /**
     * Returns the default command (or null if there is none).
     * @return the default command
     */
    @objid ("d226777c-bb6f-4426-8cc2-c7ed759d13e2")
    protected Command getDefaultCommand() {
        if (!initializedDefaultCommand) {
            initializedDefaultCommand = true;
            initDefaultCommand();
        }
        return defaultCommand;
    }

    /**
     * Sets the current command
     * @param command the new current command
     */
    @objid ("b4abb1b4-6d16-4575-9b55-4b1f9ecbe824")
    void setCurrentCommand(Command command) {
        currentCommand = command;
        currentCommandChanged = true;
    }

    /**
     * Call this to alert Subsystem that the current command is actually the command.
     * Sometimes, the {@link Subsystem} is told that it has no command while the {@link Scheduler}
     * is going through the loop, only to be soon after given a new one.  This will avoid that situation.
     */
    @objid ("6c4540a1-7926-4281-a6ad-e0e292af5838")
    void confirmCommand() {
        if (currentCommandChanged) {
            if (table != null) {
                if (currentCommand != null) {
                    table.putBoolean("hasCommand", true);
                    table.putString("command", currentCommand.getName());
                } else {
                    table.putBoolean("hasCommand", false);
                }
            }
            currentCommandChanged = false;
        }
    }

    /**
     * Returns the command which currently claims this subsystem.
     * @return the command which currently claims this subsystem
     */
    @objid ("4077b8f6-c4a8-44a4-b19d-36836e6cdb0a")
    public Command getCurrentCommand() {
        return currentCommand;
    }

    @objid ("1a8a472b-7254-4828-be97-928f455a4ab0")
    public String toString() {
        return getName();
    }

    /**
     * Returns the name of this subsystem, which is by default the class name.
     * @return the name of this subsystem
     */
    @objid ("b0af6fa7-f1f8-453b-b397-ea24bc7f7111")
    public String getName() {
        return name;
    }

    @objid ("8fa45a25-453e-464f-bfb3-3f532288e575")
    public String getSmartDashboardType() {
        return "Subsystem";
    }

    @objid ("05ac7a4b-27d2-42ab-a8b4-9c1dfd0e2034")
    public void initTable(ITable table) {
        this.table = table;
        if(table!=null) {
            if (defaultCommand != null) {
                table.putBoolean("hasDefault", true);
                table.putString("default", defaultCommand.getName());
            } else {
                table.putBoolean("hasDefault", false);
            }
            if (currentCommand != null) {
                table.putBoolean("hasCommand", true);
                table.putString("command", currentCommand.getName());
            } else {
                table.putBoolean("hasCommand", false);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("0593e12a-075a-41ca-a4aa-6cf67579c374")
    public ITable getTable() {
        return table;
    }

}
