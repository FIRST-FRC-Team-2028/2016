/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.command;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.NamedSendable;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

/**
 * The Command class is at the very core of the entire command framework.
 * Every command can be started with a call to {@link Command#start() start()}.
 * Once a command is started it will call {@link Command#initialize() initialize()}, and then
 * will repeatedly call {@link Command#execute() execute()} until the {@link Command#isFinished() isFinished()}
 * returns true.  Once it does, {@link Command#end() end()} will be called.
 * 
 * <p>However, if at any point while it is running {@link Command#cancel() cancel()} is called, then
 * the command will be stopped and {@link Command#interrupted() interrupted()} will be called.</p>
 * 
 * <p>If a command uses a {@link Subsystem}, then it should specify that it does so by
 * calling the {@link Command#requires(Subsystem) requires(...)} method
 * in its constructor. Note that a Command may have multiple requirements, and
 * {@link Command#requires(Subsystem) requires(...)} should be
 * called for each one.</p>
 * 
 * <p>If a command is running and a new command with shared requirements is started,
 * then one of two things will happen.  If the active command is interruptible,
 * then {@link Command#cancel() cancel()} will be called and the command will be removed
 * to make way for the new one.  If the active command is not interruptible, the
 * other one will not even be started, and the active one will continue functioning.</p>
 * 
 * @author Brad Miller
 * @author Joe Grinstead
 * @see Subsystem
 * @see CommandGroup
 * @see IllegalUseOfCommandException
 */
@objid ("645db373-549e-4ffe-b4f4-850dbfa024f9")
public abstract class Command implements NamedSendable {
    /**
     * The name of this command
     */
    @objid ("0998fe4b-80f2-4a55-98ab-f73811ae8a22")
    private String m_name;

    /**
     * The time since this command was initialized
     */
    @objid ("32f2df5c-b6be-44a8-bc31-5ba3eec51e3e")
    private double m_startTime = -1;

    /**
     * The time (in seconds) before this command "times out" (or -1 if no timeout)
     */
    @objid ("ae2e6c50-30f9-4604-8a4d-eb3156dd914f")
    private double m_timeout = -1;

    /**
     * Whether or not this command has been initialized
     */
    @objid ("ad9f2dd9-1b5b-44ba-9cf9-050c0a655e89")
    private boolean m_initialized = false;

    /**
     * Whether or not it is running
     */
    @objid ("bf8119f5-9b98-44d9-af99-02a8d56ff6cd")
    private boolean m_running = false;

    /**
     * Whether or not it has been canceled
     */
    @objid ("61d3945c-99ad-4b50-bff6-d58b46567894")
    private boolean m_canceled = false;

    /**
     * Whether this command should run when the robot is disabled
     */
    @objid ("c889a745-dfda-4169-b12c-ef72ae9ddce8")
    private boolean m_runWhenDisabled = false;

    @objid ("6ee9b646-4f85-4f2a-98f3-0c4c2e1abefb")
    private ITableListener listener = new ITableListener() {
        public void valueChanged(ITable table, String key, Object value, boolean isNew) {
            if (((Boolean) value).booleanValue()) {
                start();
            } else {
                cancel();
            }
        }
    };

    /**
     * Whether or not it has been locked
     */
    @objid ("ac5dd5bb-f5f7-4cbb-b53b-2b333da02c64")
    private boolean m_locked = false;

    @objid ("768dfd46-cfee-4047-b8f9-38465f3189de")
    private ITable table;

    /**
     * An empty enumeration given whenever there are no requirements
     */
    @objid ("5bfd9d97-84c3-4469-9c09-d8f6f1bda4bd")
    private static Enumeration emptyEnumeration = new Enumeration() {
        public boolean hasMoreElements() {
            return false;
        }
        public Object nextElement() {
            throw new NoSuchElementException();
        }
    };

    /**
     * Whether or not it is interruptible
     */
    @objid ("22c3326b-a789-4573-bb39-fea6e3cd27ab")
    private boolean m_interruptible = true;

    /**
     * The {@link CommandGroup} this is in
     */
    @objid ("8d8b548d-80d3-47e5-8ff0-4bdd0d2d1b61")
    private CommandGroup m_parent;

    /**
     * The requirements (or null if no requirements)
     */
    @objid ("5107ec19-6b73-41ad-bed8-b6f79467db40")
    private Set m_requirements;

    /**
     * Creates a new command.
     * The name of this command will be set to its class name.
     */
    @objid ("3c6d6f68-72b3-4cc9-b64c-0eda036f23e9")
    public Command() {
        m_name = getClass().getName();
        m_name = m_name.substring(m_name.lastIndexOf('.') + 1);
    }

    /**
     * Creates a new command with the given name.
     * @throws IllegalArgumentException if name is null
     * @param name the name for this command
     */
    @objid ("082375e3-102c-4b36-bd1c-a15730854331")
    public Command(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null.");
        }
        m_name = name;
    }

    /**
     * Creates a new command with the given timeout and a default name.
     * The default name is the name of the class.
     * @throws IllegalArgumentException if given a negative timeout
     * @see Command#isTimedOut() isTimedOut()
     * @param timeout the time (in seconds) before this command "times out"
     */
    @objid ("ac8819e4-4293-4111-82e3-f89c2596aa41")
    public Command(double timeout) {
        this();
        if (timeout < 0) {
            throw new IllegalArgumentException("Timeout must not be negative.  Given:" + timeout);
        }
        m_timeout = timeout;
    }

    /**
     * Creates a new command with the given name and timeout.
     * @throws IllegalArgumentException if given a negative timeout or name was null.
     * @see Command#isTimedOut() isTimedOut()
     * @param name the name of the command
     * @param timeout the time (in seconds) before this command "times out"
     */
    @objid ("aca449fc-f007-438e-a669-9d8948f92028")
    public Command(String name, double timeout) {
        this(name);
        if (timeout < 0) {
            throw new IllegalArgumentException("Timeout must not be negative.  Given:" + timeout);
        }
        m_timeout = timeout;
    }

    /**
     * Returns the name of this command.
     * If no name was specified in the constructor,
     * then the default is the name of the class.
     * @return the name of this command
     */
    @objid ("a25fff7f-8e12-4111-b879-5561d176692f")
    public String getName() {
        return m_name;
    }

    /**
     * Sets the timeout of this command.
     * @throws IllegalArgumentException if seconds is negative
     * @see Command#isTimedOut() isTimedOut()
     * @param seconds the timeout (in seconds)
     */
    @objid ("f17bae7c-a864-4bdc-9454-ea7104fd007b")
    protected final synchronized void setTimeout(double seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("Seconds must be positive.  Given:" + seconds);
        }
        m_timeout = seconds;
    }

    /**
     * Returns the time since this command was initialized (in seconds).
     * This function will work even if there is no specified timeout.
     * @return the time since this command was initialized (in seconds).
     */
    @objid ("5bcb2379-1786-4326-baa1-cee114ba5c44")
    public final synchronized double timeSinceInitialized() {
        return m_startTime < 0 ? 0 : Timer.getFPGATimestamp() - m_startTime;
    }

    /**
     * This method specifies that the given {@link Subsystem} is used by this command.
     * This method is crucial to the functioning of the Command System in general.
     * 
     * <p>Note that the recommended way to call this method is in the constructor.</p>
     * @throws IllegalArgumentException if subsystem is null
     * @throws IllegalUseOfCommandException if this command has started before or if it has been given to a {@link CommandGroup}
     * @see Subsystem
     * @param subsystem the {@link Subsystem} required
     */
    @objid ("4d5feb0d-0f94-476b-a013-7f62a98b3ce6")
    protected synchronized void requires(Subsystem subsystem) {
        validate("Can not add new requirement to command");
        if (subsystem != null) {
            if (m_requirements == null) {
                m_requirements = new Set();
            }
            m_requirements.add(subsystem);
        } else {
            throw new IllegalArgumentException("Subsystem must not be null.");
        }
    }

    /**
     * Called when the command has been removed.
     * This will call {@link Command#interrupted() interrupted()} or {@link Command#end() end()}.
     */
    @objid ("17c6908e-1181-4b7e-89cb-28d2397fe997")
    synchronized void removed() {
        if (m_initialized) {
            if (isCanceled()) {
                interrupted();
                _interrupted();
            } else {
                end();
                _end();
            }
        }
        m_initialized = false;
        m_canceled = false;
        m_running = false;
        if (table != null) {
            table.putBoolean("running", false);
        }
    }

    /**
     * The run method is used internally to actually run the commands.
     * @return whether or not the command should stay within the {@link Scheduler}.
     */
    @objid ("ee474dad-6fa2-4154-9cd6-da0e9f1bd08e")
    synchronized boolean run() {
        if (!m_runWhenDisabled && m_parent == null && RobotState.isDisabled()) {
            cancel();
        }
        if (isCanceled()) {
            return false;
        }
        if (!m_initialized) {
            m_initialized = true;
            startTiming();
            _initialize();
            initialize();
        }
        _execute();
        execute();
        return !isFinished();
    }

    /**
     * The initialize method is called the first time this Command is run after
     * being started.
     */
    @objid ("1f4bf063-0aa5-476b-8ce8-d6329c0a870b")
    protected abstract void initialize();

    /**
     * A shadow method called before {@link Command#initialize() initialize()}
     */
    @objid ("4f194bf8-edd1-4f0d-b0df-e18888277172")
    void _initialize() {
    }

    /**
     * The execute method is called repeatedly until this Command either finishes
     * or is canceled.
     */
    @objid ("ebbefae1-a968-48c4-9682-1ead75a0fcea")
    protected abstract void execute();

    /**
     * A shadow method called before {@link Command#execute() execute()}
     */
    @objid ("9296240c-0b58-4116-8e99-6a23e9d99189")
    void _execute() {
    }

    /**
     * Returns whether this command is finished.
     * If it is, then the command will be removed
     * and {@link Command#end() end()} will be called.
     * 
     * <p>It may be useful for a team to reference the {@link Command#isTimedOut() isTimedOut()} method
     * for time-sensitive commands.</p>
     * @see Command#isTimedOut() isTimedOut()
     * @return whether this command is finished.
     */
    @objid ("3b785e1f-3b0a-4920-9301-5e7f2a0217d1")
    protected abstract boolean isFinished();

    /**
     * Called when the command ended peacefully.  This is where you may want
     * to wrap up loose ends, like shutting off a motor that was being used
     * in the command.
     */
    @objid ("fded913e-4eb5-499a-ac89-f34f37b01807")
    protected abstract void end();

    /**
     * A shadow method called after {@link Command#end() end()}.
     */
    @objid ("a3e9225f-921a-4d04-b4e2-ffe536259bd6")
    void _end() {
    }

    /**
     * Called when the command ends because somebody called {@link Command#cancel() cancel()}
     * or another command shared the same requirements as this one, and booted
     * it out.
     * 
     * <p>This is where you may want
     * to wrap up loose ends, like shutting off a motor that was being used
     * in the command.</p>
     * 
     * <p>Generally, it is useful to simply call the {@link Command#end() end()} method
     * within this method</p>
     */
    @objid ("0df37b9e-489c-4ce6-a35d-302905da441a")
    protected abstract void interrupted();

    /**
     * A shadow method called after {@link Command#interrupted() interrupted()}.
     */
    @objid ("19a8878d-3083-4f1f-9de5-fed1602dc268")
    void _interrupted() {
    }

    /**
     * Called to indicate that the timer should start.
     * This is called right before {@link Command#initialize() initialize()} is, inside the
     * {@link Command#run() run()} method.
     */
    @objid ("badf8075-d61e-49b2-8587-29d1a8419112")
    private void startTiming() {
        m_startTime = Timer.getFPGATimestamp();
    }

    /**
     * Returns whether or not the {@link Command#timeSinceInitialized() timeSinceInitialized()}
     * method returns a number which is greater than or equal to the timeout for the command.
     * If there is no timeout, this will always return false.
     * @return whether the time has expired
     */
    @objid ("8f2335c9-05d7-43ec-87f6-7603f6216c5c")
    protected synchronized boolean isTimedOut() {
        return m_timeout != -1 && timeSinceInitialized() >= m_timeout;
    }

    /**
     * Returns the requirements (as an {@link Enumeration Enumeration} of {@link Subsystem Subsystems}) of this command
     * @return the requirements (as an {@link Enumeration Enumeration} of {@link Subsystem Subsystems}) of this command
     */
    @objid ("624fe371-c872-4408-8fb0-6d5473af3b5c")
    synchronized Enumeration getRequirements() {
        return m_requirements == null ? emptyEnumeration : m_requirements.getElements();
    }

    /**
     * Prevents further changes from being made
     */
    @objid ("0ea58cf7-b6d2-4f9d-a7c6-a5268c96e40d")
    synchronized void lockChanges() {
        m_locked = true;
    }

    /**
     * If changes are locked, then this will throw an {@link IllegalUseOfCommandException}.
     * @param message the message to say (it is appended by a default message)
     */
    @objid ("0073a27c-ba2d-4b74-aba9-1efc04b35712")
    synchronized void validate(String message) {
        if (m_locked) {
            throw new IllegalUseOfCommandException(message + " after being started or being added to a command group");
        }
    }

    /**
     * Sets the parent of this command.  No actual change is made to the group.
     * @throws IllegalUseOfCommandException if this {@link Command} already is already in a group
     * @param parent the parent
     */
    @objid ("8af095ff-d37d-45e7-98e5-57960b824269")
    synchronized void setParent(CommandGroup parent) {
        if (this.m_parent != null) {
            throw new IllegalUseOfCommandException("Can not give command to a command group after already being put in a command group");
        }
        lockChanges();
        this.m_parent = parent;
        if (table != null) {
            table.putBoolean("isParented", true);
        }
    }

    /**
     * Starts up the command.  Gets the command ready to start.
     * <p>Note that the command will eventually start, however it will not necessarily
     * do so immediately, and may in fact be canceled before initialize is even called.</p>
     * @throws IllegalUseOfCommandException if the command is a part of a CommandGroup
     */
    @objid ("ef87ef4c-7226-49ff-93e8-1b3153fa121a")
    public synchronized void start() {
        lockChanges();
        if (m_parent != null) {
            throw new IllegalUseOfCommandException("Can not start a command that is a part of a command group");
        }
        Scheduler.getInstance().add(this);
    }

    /**
     * This is used internally to mark that the command has been started.
     * The lifecycle of a command is:
     * 
     * startRunning() is called.
     * run() is called (multiple times potentially)
     * removed() is called
     * 
     * It is very important that startRunning and removed be called in order or some assumptions
     * of the code will be broken.
     */
    @objid ("c7fcf5f6-b028-4b04-9e0d-bd64a39ba8d3")
    synchronized void startRunning() {
        m_running = true;
        m_startTime = -1;
        if (table != null) {
            table.putBoolean("running", true);
        }
    }

    /**
     * Returns whether or not the command is running.
     * This may return true even if the command has just been canceled, as it may
     * not have yet called {@link Command#interrupted()}.
     * @return whether or not the command is running
     */
    @objid ("9e203135-ad6b-49e8-9cbb-c46afdf98e37")
    public synchronized boolean isRunning() {
        return m_running;
    }

    /**
     * This will cancel the current command.
     * <p>This will cancel the current command eventually.  It can be called multiple times.
     * And it can be called when the command is not running.  If the command is running though,
     * then the command will be marked as canceled and eventually removed.</p>
     * <p>A command can not be canceled
     * if it is a part of a command group, you must cancel the command group instead.</p>
     * @throws IllegalUseOfCommandException if this command is a part of a command group
     */
    @objid ("4c68a23d-7912-4e13-b95d-14340c184688")
    public synchronized void cancel() {
        if (m_parent != null) {
            throw new IllegalUseOfCommandException("Can not manually cancel a command in a command group");
        }
        _cancel();
    }

    /**
     * This works like cancel(), except that it doesn't throw an exception if it is a part
     * of a command group.  Should only be called by the parent command group.
     */
    @objid ("7866d4ad-8b64-4072-b201-199ce0454b8c")
    synchronized void _cancel() {
        if (isRunning()) {
            m_canceled = true;
        }
    }

    /**
     * Returns whether or not this has been canceled.
     * @return whether or not this has been canceled
     */
    @objid ("13a54197-9da2-44fe-a80a-9aad91524e65")
    public synchronized boolean isCanceled() {
        return m_canceled;
    }

    /**
     * Returns whether or not this command can be interrupted.
     * @return whether or not this command can be interrupted
     */
    @objid ("e84aac14-0d49-4e3c-bcee-0a73de4077dc")
    public synchronized boolean isInterruptible() {
        return m_interruptible;
    }

    /**
     * Sets whether or not this command can be interrupted.
     * @param interruptible whether or not this command can be interrupted
     */
    @objid ("fae677dc-a858-478e-9ae1-db7306ee16e1")
    protected synchronized void setInterruptible(boolean interruptible) {
        this.m_interruptible = interruptible;
    }

    /**
     * Checks if the command requires the given {@link Subsystem}.
     * @param system the system
     * @return whether or not the subsystem is required, or false if given null
     */
    @objid ("3fc26bfc-e564-49f5-b5c7-36033153288d")
    public synchronized boolean doesRequire(Subsystem system) {
        return m_requirements != null && m_requirements.contains(system);
    }

    /**
     * Returns the {@link CommandGroup} that this command is a part of.
     * Will return null if this {@link Command} is not in a group.
     * @return the {@link CommandGroup} that this command is a part of (or null if not in group)
     */
    @objid ("14f17383-1372-4574-b16c-df5dd056a1dd")
    public synchronized CommandGroup getGroup() {
        return m_parent;
    }

    /**
     * Sets whether or not this {@link Command} should run when the robot is disabled.
     * 
     * <p>By default a command will not run when the robot is disabled, and will in fact be canceled.</p>
     * @param run whether or not this command should run when the robot is disabled
     */
    @objid ("971b16d4-7289-42fa-bca5-9b460d82635c")
    public void setRunWhenDisabled(boolean run) {
        m_runWhenDisabled = run;
    }

    /**
     * Returns whether or not this {@link Command} will run when the robot is disabled, or if it will cancel itself.
     * @return whether or not this {@link Command} will run when the robot is disabled, or if it will cancel itself
     */
    @objid ("95531126-9774-454d-9198-b78b2411e2f1")
    public boolean willRunWhenDisabled() {
        return m_runWhenDisabled;
    }

    /**
     * The string representation for a {@link Command} is by default its name.
     * @return the string representation of this object
     */
    @objid ("27c82c8a-0a7a-4218-9637-49c22f12c32b")
    public String toString() {
        return getName();
    }

    @objid ("9a8073f6-7594-4ff3-bd68-6669277d249e")
    public String getSmartDashboardType() {
        return "Command";
    }

    @objid ("2db47581-a7c3-415c-a0dd-2743020e903e")
    public void initTable(ITable table) {
        if(this.table!=null)
            this.table.removeTableListener(listener);
        this.table = table;
        if(table!=null) {
            table.putString("name", getName());
            table.putBoolean("running", isRunning());
            table.putBoolean("isParented", m_parent != null);
            table.addTableListener("running", listener, false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("deedb7c1-1b65-4a44-a5e7-5193f84133cc")
    public ITable getTable() {
        return table;
    }

}
