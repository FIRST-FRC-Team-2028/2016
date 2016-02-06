/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.command;

import java.util.Enumeration;
import java.util.Vector;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * A {@link CommandGroup} is a list of commands which are executed in sequence.
 * 
 * <p>Commands in a {@link CommandGroup} are added using the {@link CommandGroup#addSequential(Command) addSequential(...)} method
 * and are called sequentially.
 * {@link CommandGroup CommandGroups} are themselves {@link Command commands}
 * and can be given to other {@link CommandGroup CommandGroups}.</p>
 * 
 * <p>{@link CommandGroup CommandGroups} will carry all of the requirements of their {@link Command subcommands}.  Additional
 * requirements can be specified by calling {@link CommandGroup#requires(Subsystem) requires(...)}
 * normally in the constructor.</P>
 * 
 * <p>CommandGroups can also execute commands in parallel, simply by adding them
 * using {@link CommandGroup#addParallel(Command) addParallel(...)}.</p>
 * 
 * @author Brad Miller
 * @author Joe Grinstead
 * @see Command
 * @see Subsystem
 * @see IllegalUseOfCommandException
 */
@objid ("cfa2fd91-6d36-4c85-9706-6adb854be42c")
public class CommandGroup extends Command {
    /**
     * The commands in this group (stored in entries)
     */
    @objid ("bdcdf131-70b8-4344-ae50-7793406a5521")
     Vector m_commands = new Vector();

    /**
     * The active children in this group (stored in entries)
     */
    @objid ("f785cefb-2742-4dc7-9d90-a7e064048bdc")
     Vector m_children = new Vector();

    /**
     * The current command, -1 signifies that none have been run
     */
    @objid ("4bd63c67-f3ec-4105-a05e-2d6c07822659")
     int m_currentCommandIndex = -1;

    /**
     * Creates a new {@link CommandGroup CommandGroup}.
     * The name of this command will be set to its class name.
     */
    @objid ("bcd09a1a-839b-4b76-b7c4-1924418ea665")
    public CommandGroup() {
    }

    /**
     * Creates a new {@link CommandGroup CommandGroup} with the given name.
     * @throws IllegalArgumentException if name is null
     * @param name the name for this command group
     */
    @objid ("3a017124-74a5-4554-a1c6-f3b18cef908a")
    public CommandGroup(String name) {
        super(name);
    }

    /**
     * Adds a new {@link Command Command} to the group.  The {@link Command Command} will be started after
     * all the previously added {@link Command Commands}.
     * 
     * <p>Note that any requirements the given {@link Command Command} has will be added to the
     * group.  For this reason, a {@link Command Command's} requirements can not be changed after
     * being added to a group.</p>
     * 
     * <p>It is recommended that this method be called in the constructor.</p>
     * @throws IllegalUseOfCommandException if the group has been started before or been given to another group
     * @throws IllegalArgumentException if command is null
     * @param command The {@link Command Command} to be added
     */
    @objid ("e9279e67-d19d-4ee9-90bc-fbc33235b837")
    public final synchronized void addSequential(Command command) {
        validate("Can not add new command to command group");
        if (command == null) {
            throw new IllegalArgumentException("Given null command");
        }
        
        command.setParent(this);
        
        m_commands.addElement(new Entry(command, Entry.IN_SEQUENCE));
        for (Enumeration e = command.getRequirements(); e.hasMoreElements();) {
            requires((Subsystem) e.nextElement());
        }
    }

    /**
     * Adds a new {@link Command Command} to the group with a given timeout.
     * The {@link Command Command} will be started after all the previously added commands.
     * 
     * <p>Once the {@link Command Command} is started, it will be run until it finishes or the time
     * expires, whichever is sooner.  Note that the given {@link Command Command} will have no
     * knowledge that it is on a timer.</p>
     * 
     * <p>Note that any requirements the given {@link Command Command} has will be added to the
     * group.  For this reason, a {@link Command Command's} requirements can not be changed after
     * being added to a group.</p>
     * 
     * <p>It is recommended that this method be called in the constructor.</p>
     * @throws IllegalUseOfCommandException if the group has been started before or been given to another group or
     * if the {@link Command Command} has been started before or been given to another group
     * @throws IllegalArgumentException if command is null or timeout is negative
     * @param command The {@link Command Command} to be added
     * @param timeout The timeout (in seconds)
     */
    @objid ("dd4d85b1-a9d1-4f8e-afc5-b44440f14705")
    public final synchronized void addSequential(Command command, double timeout) {
        validate("Can not add new command to command group");
        if (command == null) {
            throw new IllegalArgumentException("Given null command");
        }
        if (timeout < 0) {
            throw new IllegalArgumentException("Can not be given a negative timeout");
        }
        
        command.setParent(this);
        
        m_commands.addElement(new Entry(command, Entry.IN_SEQUENCE, timeout));
        for (Enumeration e = command.getRequirements(); e.hasMoreElements();) {
            requires((Subsystem) e.nextElement());
        }
    }

    /**
     * Adds a new child {@link Command} to the group.  The {@link Command} will be started after
     * all the previously added {@link Command Commands}.
     * 
     * <p>Instead of waiting for the child to finish, a {@link CommandGroup} will have it
     * run at the same time as the subsequent {@link Command Commands}.  The child will run until either
     * it finishes, a new child with conflicting requirements is started, or
     * the main sequence runs a {@link Command} with conflicting requirements.  In the latter
     * two cases, the child will be canceled even if it says it can't be
     * interrupted.</p>
     * 
     * <p>Note that any requirements the given {@link Command Command} has will be added to the
     * group.  For this reason, a {@link Command Command's} requirements can not be changed after
     * being added to a group.</p>
     * 
     * <p>It is recommended that this method be called in the constructor.</p>
     * @throws IllegalUseOfCommandException if the group has been started before or been given to another command group
     * @throws IllegalArgumentException if command is null
     * @param command The command to be added
     */
    @objid ("e0dd84a5-768e-4956-8056-52ce9ae33b6e")
    public final synchronized void addParallel(Command command) {
        validate("Can not add new command to command group");
        if (command == null) {
            throw new NullPointerException("Given null command");
        }
        
        command.setParent(this);
        
        m_commands.addElement(new Entry(command, Entry.BRANCH_CHILD));
        for (Enumeration e = command.getRequirements(); e.hasMoreElements();) {
            requires((Subsystem) e.nextElement());
        }
    }

    /**
     * Adds a new child {@link Command} to the group with the given timeout.  The {@link Command} will be started after
     * all the previously added {@link Command Commands}.
     * 
     * <p>Once the {@link Command Command} is started, it will run until it finishes, is interrupted,
     * or the time expires, whichever is sooner.  Note that the given {@link Command Command} will have no
     * knowledge that it is on a timer.</p>
     * 
     * <p>Instead of waiting for the child to finish, a {@link CommandGroup} will have it
     * run at the same time as the subsequent {@link Command Commands}.  The child will run until either
     * it finishes, the timeout expires, a new child with conflicting requirements is started, or
     * the main sequence runs a {@link Command} with conflicting requirements.  In the latter
     * two cases, the child will be canceled even if it says it can't be
     * interrupted.</p>
     * 
     * <p>Note that any requirements the given {@link Command Command} has will be added to the
     * group.  For this reason, a {@link Command Command's} requirements can not be changed after
     * being added to a group.</p>
     * 
     * <p>It is recommended that this method be called in the constructor.</p>
     * @throws IllegalUseOfCommandException if the group has been started before or been given to another command group
     * @throws IllegalArgumentException if command is null
     * @param command The command to be added
     * @param timeout The timeout (in seconds)
     */
    @objid ("74adc401-c4d6-4fd5-b282-1eab1855050d")
    public final synchronized void addParallel(Command command, double timeout) {
        validate("Can not add new command to command group");
        if (command == null) {
            throw new NullPointerException("Given null command");
        }
        if (timeout < 0) {
            throw new IllegalArgumentException("Can not be given a negative timeout");
        }
        
        command.setParent(this);
        
        m_commands.addElement(new Entry(command, Entry.BRANCH_CHILD, timeout));
        for (Enumeration e = command.getRequirements(); e.hasMoreElements();) {
            requires((Subsystem) e.nextElement());
        }
    }

    @objid ("d306ddcf-9519-4ac2-9095-de5a39168b69")
    void _initialize() {
        m_currentCommandIndex = -1;
    }

    @objid ("c9ecd23c-9779-4256-8808-83a5597e229e")
    void _execute() {
        Entry entry = null;
        Command cmd = null;
        boolean firstRun = false;
        if (m_currentCommandIndex == -1) {
            firstRun = true;
            m_currentCommandIndex = 0;
        }
        
        while (m_currentCommandIndex < m_commands.size()) {
        
            if (cmd != null) {
                if (entry.isTimedOut()) {
                    cmd._cancel();
                }
                if (cmd.run()) {
                    break;
                } else {
                    cmd.removed();
                    m_currentCommandIndex++;
                    firstRun = true;
                    cmd = null;
                    continue;
                }
            }
        
            entry = (Entry) m_commands.elementAt(m_currentCommandIndex);
            cmd = null;
        
            switch (entry.state) {
            case Entry.IN_SEQUENCE:
                cmd = entry.command;
                if (firstRun) {
                    cmd.startRunning();
                    cancelConflicts(cmd);
                }
                firstRun = false;
                break;
            case Entry.BRANCH_PEER:
                m_currentCommandIndex++;
                entry.command.start();
                break;
            case Entry.BRANCH_CHILD:
                m_currentCommandIndex++;
                cancelConflicts(entry.command);
                entry.command.startRunning();
                m_children.addElement(entry);
                break;
            }
        }
        
        // Run Children
        for (int i = 0; i < m_children.size(); i++) {
            entry = (Entry) m_children.elementAt(i);
            Command child = entry.command;
            if (entry.isTimedOut()) {
                child._cancel();
            }
            if (!child.run()) {
                child.removed();
                m_children.removeElementAt(i--);
            }
        }
    }

    @objid ("fe5cbf15-ce59-4e8f-9e0e-56640d416787")
    void _end() {
        // Theoretically, we don't have to check this, but we do if teams override the isFinished method
        if (m_currentCommandIndex != -1 && m_currentCommandIndex < m_commands.size()) {
            Command cmd = ((Entry) m_commands.elementAt(m_currentCommandIndex)).command;
            cmd._cancel();
            cmd.removed();
        }
        
        Enumeration children = m_children.elements();
        while (children.hasMoreElements()) {
            Command cmd = ((Entry) children.nextElement()).command;
            cmd._cancel();
            cmd.removed();
        }
        m_children.removeAllElements();
    }

    @objid ("ec1f613c-bf97-4410-a43b-19abbb8833ef")
    void _interrupted() {
        _end();
    }

    /**
     * Returns true if all the {@link Command Commands} in this group
     * have been started and have finished.
     * 
     * <p>Teams may override this method, although they should probably
     * reference super.isFinished() if they do.</p>
     * @return whether this {@link CommandGroup} is finished
     */
    @objid ("b2ab1da4-d126-456d-8c23-7e02e4dc4fa3")
    protected boolean isFinished() {
        return m_currentCommandIndex >= m_commands.size() && m_children.isEmpty();
    }

// Can be overwritten by teams
    @objid ("f4b80387-7452-40dc-9f17-e9874d94b91e")
    protected void initialize() {
    }

// Can be overwritten by teams
    @objid ("89d8b0d6-c258-44a1-8e3a-ad045fc2830c")
    protected void execute() {
    }

// Can be overwritten by teams
    @objid ("45d0b6b7-7f54-48e4-b6c3-1ddd8ecfeffb")
    protected void end() {
    }

// Can be overwritten by teams
    @objid ("9c34c872-b64f-4ce8-a98f-838db88fdbff")
    protected void interrupted() {
    }

    /**
     * Returns whether or not this group is interruptible.
     * A command group will be uninterruptible if {@link CommandGroup#setInterruptible(boolean) setInterruptable(false)}
     * was called or if it is currently running an uninterruptible command
     * or child.
     * @return whether or not this {@link CommandGroup} is interruptible.
     */
    @objid ("ed241f05-09bf-4dc4-befe-7357f0d711d3")
    public synchronized boolean isInterruptible() {
        if (!super.isInterruptible()) {
            return false;
        }
        
        if (m_currentCommandIndex != -1 && m_currentCommandIndex < m_commands.size()) {
            Command cmd = ((Entry) m_commands.elementAt(m_currentCommandIndex)).command;
            if (!cmd.isInterruptible()) {
                return false;
            }
        }
        
        for (int i = 0; i < m_children.size(); i++) {
            if (!((Entry) m_children.elementAt(i)).command.isInterruptible()) {
                return false;
            }
        }
        return true;
    }

    @objid ("4e388225-c07d-461b-9564-a9497bc45a41")
    private void cancelConflicts(Command command) {
        for (int i = 0; i < m_children.size(); i++) {
            Command child = ((Entry) m_children.elementAt(i)).command;
        
            Enumeration requirements = command.getRequirements();
        
            while (requirements.hasMoreElements()) {
                Object requirement = requirements.nextElement();
                if (child.doesRequire((Subsystem) requirement)) {
                    child._cancel();
                    child.removed();
                    m_children.removeElementAt(i--);
                    break;
                }
            }
        }
    }

    @objid ("9521a417-595c-4ddb-8287-4a179d9280e3")
    private static class Entry {
        @objid ("cd404de2-3331-45bf-a5fa-7dda82d9d2d3")
        private static final int IN_SEQUENCE = 0;

        @objid ("a7996f7e-e9c5-45d9-a917-43a0afd6ce25")
        private static final int BRANCH_PEER = 1;

        @objid ("ce288e95-2f13-4139-9590-8d612f020021")
        private static final int BRANCH_CHILD = 2;

        @objid ("fbc73845-f783-41c4-8375-a44951f65b26")
         int state;

        @objid ("0cfb16bc-66f5-41b5-90fe-9ac33e5150a9")
         double timeout;

        @objid ("ab9b577a-7074-4c71-8b94-2e4c7fd07a52")
         Command command;

        @objid ("ef15c071-0b6f-4d44-a517-4a830e50232d")
        Entry(Command command, int state) {
            this.command = command;
            this.state = state;
            this.timeout = -1;
        }

        @objid ("9de5af49-827e-4ce4-b5cd-f047c0ecd595")
        Entry(Command command, int state, double timeout) {
            this.command = command;
            this.state = state;
            this.timeout = timeout;
        }

        @objid ("4790c693-c2d0-45ff-ba95-607063689e72")
        boolean isTimedOut() {
            if (timeout == -1) {
                return false;
            } else {
                double time = command.timeSinceInitialized();
                return time == 0 ? false : time >= timeout;
            }
        }

    }

}
