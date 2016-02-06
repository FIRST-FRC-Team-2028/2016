/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.command;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.HLUsageReporting;
import edu.wpi.first.wpilibj.NamedSendable;
import edu.wpi.first.wpilibj.buttons.Trigger.ButtonScheduler;
import edu.wpi.first.wpilibj.networktables2.type.NumberArray;
import edu.wpi.first.wpilibj.networktables2.type.StringArray;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * The {@link Scheduler} is a singleton which holds the top-level running
 * commands. It is in charge of both calling the command's
 * {@link Command#run() run()} method and to make sure that there are no two
 * commands with conflicting requirements running.
 * 
 * <p>It is fine if teams wish to take control of the {@link Scheduler}
 * themselves, all that needs to be done is to call
 * {@link Scheduler#getInstance() Scheduler.getInstance()}.{@link Scheduler#run() run()}
 * often to have {@link Command Commands} function correctly. However, this is
 * already done for you if you use the CommandBased Robot template.</p>
 * 
 * @author Joe Grinstead
 * @see Command
 */
@objid ("21467194-2bce-47a2-aa82-1daf9e594f50")
public class Scheduler implements NamedSendable {
    /**
     * A hashtable of active {@link Command Commands} to their
     * {@link LinkedListElement}
     */
    @objid ("c1f46227-3d9a-4565-b08a-fcfc380d737b")
    private Hashtable commandTable = new Hashtable();

    /**
     * Whether or not we are currently adding a command
     */
    @objid ("8f483b0c-7c09-42c7-8023-f48835f1bd4c")
    private boolean adding = false;

    /**
     * A list of all {@link Command Commands} which need to be added
     */
    @objid ("c6bdd729-9ef9-411f-a3f7-a96c84b4ea79")
    private Vector additions = new Vector();

    /**
     * A list of all
     * {@link edu.wpi.first.wpilibj.buttons.Trigger.ButtonScheduler Buttons}. It
     * is created lazily.
     */
    @objid ("3c8cbbcb-9c06-4f7a-a95d-9ff562e28102")
    private Vector buttons;

    @objid ("a3485718-c847-48e9-b609-b00c2f9eb53f")
    private StringArray commands;

    @objid ("6c98f1d9-fb63-4f45-9f3a-e922a4a2cefd")
    private NumberArray toCancel;

    /**
     * Whether or not we are currently disabled
     */
    @objid ("c8fac606-cd60-4408-a42a-c807a0309eb2")
    private boolean disabled = false;

    @objid ("2c158449-df8b-484f-9b26-e91b093d7a1e")
    private boolean m_runningCommandsChanged;

    @objid ("2912a66c-bde8-4c4f-b551-eeb051f17755")
    private NumberArray ids;

    @objid ("f1f88c91-f057-4121-92fc-0537300f13a0")
    private ITable m_table;

    /**
     * The Singleton Instance
     */
    @objid ("4f8e407b-17cd-41dd-b94b-0317558fc991")
    private static Scheduler instance;

    /**
     * The first {@link Command} in the list
     */
    @objid ("1bb7abf5-652e-4435-8c69-9fe512f8f04c")
    private LinkedListElement firstCommand;

    /**
     * The last {@link Command} in the list
     */
    @objid ("1b1f6225-73c9-42ba-bf86-e71f5ba162c3")
    private LinkedListElement lastCommand;

    /**
     * The {@link Set} of all {@link Subsystem Subsystems}
     */
    @objid ("674b81de-ccd3-4fc8-bb10-1886390d5cf5")
    private Set subsystems = new Set();

    /**
     * Returns the {@link Scheduler}, creating it if one does not exist.
     * @return the {@link Scheduler}
     */
    @objid ("c554127e-7ac5-48fe-8478-3ce6b37b1657")
    public static synchronized Scheduler getInstance() {
        return instance == null ? instance = new Scheduler() : instance;
    }

    /**
     * Instantiates a {@link Scheduler}.
     */
    @objid ("0ce231ef-3737-486f-8f7e-89642d0e0d75")
    private Scheduler() {
        HLUsageReporting.reportScheduler();
    }

    /**
     * Adds the command to the {@link Scheduler}. This will not add the
     * {@link Command} immediately, but will instead wait for the proper time in
     * the {@link Scheduler#run()} loop before doing so. The command returns
     * immediately and does nothing if given null.
     * 
     * <p>Adding a {@link Command} to the {@link Scheduler} involves the
     * {@link Scheduler} removing any {@link Command} which has shared
     * requirements.</p>
     * @param command the command to add
     */
    @objid ("a66972b2-901f-4114-ac37-d0b3b1b1132f")
    public void add(Command command) {
        if (command != null) {
            additions.addElement(command);
        }
    }

    /**
     * Adds a button to the {@link Scheduler}. The {@link Scheduler} will poll
     * the button during its {@link Scheduler#run()}.
     * @param button the button to add
     */
    @objid ("0a26b616-e70b-4253-baa0-70d0baaf8325")
    public void addButton(ButtonScheduler button) {
        if (buttons == null) {
            buttons = new Vector();
        }
        buttons.addElement(button);
    }

    /**
     * Adds a command immediately to the {@link Scheduler}. This should only be
     * called in the {@link Scheduler#run()} loop. Any command with conflicting
     * requirements will be removed, unless it is uninterruptable. Giving
     * <code>null</code> does nothing.
     * @param command the {@link Command} to add
     */
    @objid ("082e504f-6567-4be3-b8bd-f564cb7a3b2d")
    private void _add(Command command) {
        if (command == null) {
            return;
        }
        
        // Check to make sure no adding during adding
        if (adding) {
            System.err.println("WARNING: Can not start command from cancel method.  Ignoring:" + command);
            return;
        }
        
        // Only add if not already in
        if (!commandTable.containsKey(command)) {
        
            // Check that the requirements can be had
            Enumeration requirements = command.getRequirements();
            while (requirements.hasMoreElements()) {
                Subsystem lock = (Subsystem) requirements.nextElement();
                if (lock.getCurrentCommand() != null && !lock.getCurrentCommand().isInterruptible()) {
                    return;
                }
            }
        
            // Give it the requirements
            adding = true;
            requirements = command.getRequirements();
            while (requirements.hasMoreElements()) {
                Subsystem lock = (Subsystem) requirements.nextElement();
                if (lock.getCurrentCommand() != null) {
                    lock.getCurrentCommand().cancel();
                    remove(lock.getCurrentCommand());
                }
                lock.setCurrentCommand(command);
            }
            adding = false;
        
            // Add it to the list
            LinkedListElement element = new LinkedListElement();
            element.setData(command);
            if (firstCommand == null) {
                firstCommand = lastCommand = element;
            } else {
                lastCommand.add(element);
                lastCommand = element;
            }
            commandTable.put(command, element);
        
            m_runningCommandsChanged = true;
        
            command.startRunning();
        }
    }

    /**
     * Runs a single iteration of the loop. This method should be called often
     * in order to have a functioning {@link Command} system. The loop has five
     * stages:
     * 
     * <ol> <li> Poll the Buttons </li> <li> Execute/Remove the Commands </li>
     * <li> Send values to SmartDashboard </li> <li> Add Commands </li> <li> Add
     * Defaults </li> </ol>
     */
    @objid ("b2a7d5bc-3a40-4b26-911c-3bfbb8a97e3d")
    public void run() {
        m_runningCommandsChanged = false;
        
        if (disabled) {
            return;
        } // Don't run when disabled
        
        // Get button input (going backwards preserves button priority)
        if (buttons != null) {
            for (int i = buttons.size() - 1; i >= 0; i--) {
                ((ButtonScheduler) buttons.elementAt(i)).execute();
            }
        }
        // Loop through the commands
        LinkedListElement e = firstCommand;
        while (e != null) {
            Command c = e.getData();
            e = e.getNext();
            if (!c.run()) {
                remove(c);
                m_runningCommandsChanged = true;
            }
        }
        
        // Add the new things
        for (int i = 0; i < additions.size(); i++) {
            _add((Command) additions.elementAt(i));
        }
        additions.removeAllElements();
        
        // Add in the defaults
        Enumeration locks = subsystems.getElements();
        while (locks.hasMoreElements()) {
            Subsystem lock = (Subsystem) locks.nextElement();
            if (lock.getCurrentCommand() == null) {
                _add(lock.getDefaultCommand());
            }
            lock.confirmCommand();
        }
        
        updateTable();
    }

    /**
     * Registers a {@link Subsystem} to this {@link Scheduler}, so that the
     * {@link Scheduler} might know if a default {@link Command} needs to be
     * run. All {@link Subsystem Subsystems} should call this.
     * @param system the system
     */
    @objid ("84f64e43-643f-43f7-bea8-9e7e7852da73")
    void registerSubsystem(Subsystem system) {
        if (system != null) {
            subsystems.add(system);
        }
    }

    /**
     * Removes the {@link Command} from the {@link Scheduler}.
     * @param command the command to remove
     */
    @objid ("fa2f5c0e-b4f3-4a9b-993d-19c1e606f820")
    void remove(Command command) {
        if (command == null || !commandTable.containsKey(command)) {
            return;
        }
        LinkedListElement e = (LinkedListElement) commandTable.get(command);
        commandTable.remove(command);
        
        if (e.equals(lastCommand)) {
            lastCommand = e.getPrevious();
        }
        if (e.equals(firstCommand)) {
            firstCommand = e.getNext();
        }
        e.remove();
        
        Enumeration requirements = command.getRequirements();
        while (requirements.hasMoreElements()) {
            ((Subsystem) requirements.nextElement()).setCurrentCommand(null);
        }
        
        command.removed();
    }

    /**
     * Removes all commands
     */
    @objid ("8d81b64a-b342-47c9-9c30-6f182d647964")
    public void removeAll() {
        // TODO: Confirm that this works with "uninteruptible" commands
        while (firstCommand != null) {
            remove(firstCommand.getData());
        }
    }

    /**
     * Disable the command scheduler.
     */
    @objid ("a791783b-f120-4c3a-8181-4f2b5c5c6d25")
    public void disable() {
        disabled = true;
    }

    /**
     * Enable the command scheduler.
     */
    @objid ("bb218264-7d30-480c-9ac8-7e9120152ae5")
    public void enable() {
        disabled = false;
    }

    @objid ("c0e854b0-c832-4042-8e99-cf55d96c5993")
    public String getName() {
        return "Scheduler";
    }

    @objid ("818b703c-af12-424e-9797-6ef5bbfc6bcf")
    public String getType() {
        return "Scheduler";
    }

    /**
     * {@inheritDoc}
     */
    @objid ("469d3337-4ebc-4a1b-8644-a66299a566cb")
    public void initTable(ITable subtable) {
        m_table = subtable;
        commands = new StringArray();
        ids = new NumberArray();
        toCancel = new NumberArray();
        
        m_table.putValue("Names", commands);
        m_table.putValue("Ids", ids);
        m_table.putValue("Cancel", toCancel);
    }

    @objid ("3b80c1d1-34e7-4a60-925f-539466ed402e")
    private void updateTable() {
        if (m_table != null) {
            // Get the commands to cancel
            m_table.retrieveValue("Cancel", toCancel);
            if (toCancel.size() > 0) {
                for (LinkedListElement e = firstCommand; e != null; e = e.getNext()) {
                    for (int i = 0; i < toCancel.size(); i++) {
                        if (e.getData().hashCode() == toCancel.get(i)) {
                            e.getData().cancel();
                        }
                    }
                }
                toCancel.setSize(0);
                m_table.putValue("Cancel", toCancel);
            }
        
            if (m_runningCommandsChanged) {
                commands.setSize(0);
                ids.setSize(0);
                // Set the the running commands
                for (LinkedListElement e = firstCommand; e != null; e = e.getNext()) {
                    commands.add(e.getData().getName());
                    ids.add(e.getData().hashCode());
                }
                m_table.putValue("Names", commands);
                m_table.putValue("Ids", ids);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("f8ec11aa-31f3-4007-bb30-5c6e7576585c")
    public ITable getTable() {
        return m_table;
    }

    @objid ("d2b6e115-3e7a-4462-87c1-239895e73f44")
    public String getSmartDashboardType() {
        return "Scheduler";
    }

}
