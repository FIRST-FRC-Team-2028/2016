/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.buttons;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * This class provides an easy way to link commands to inputs.
 * 
 * It is very easy to link a button to a command.  For instance, you could
 * link the trigger button of a joystick to a "score" command.
 * 
 * It is encouraged that teams write a subclass of Trigger if they want to have
 * something unusual (for instance, if they want to react to the user holding
 * a button while the robot is reading a certain sensor input).  For this, they
 * only have to write the {@link Trigger#get()} method to get the full functionality
 * of the Trigger class.
 * 
 * @author Joe Grinstead
 */
@objid ("fa8788d1-5a5a-422c-bf20-343a0a39378e")
public abstract class Trigger implements Sendable {
    @objid ("654ccbfc-d049-4510-885b-6bdb4dd0df02")
    private ITable table;

    /**
     * Returns whether or not the trigger is active
     * 
     * This method will be called repeatedly a command is linked to the Trigger.
     * @return whether or not the trigger condition is active.
     */
    @objid ("258b75f8-b33b-45b3-9356-14078790892b")
    public abstract boolean get();

    /**
     * Returns whether get() return true or the internal table for SmartDashboard use is pressed.
     * @return whether get() return true or the internal table for SmartDashboard use is pressed
     */
    @objid ("879ed5e6-3987-4754-9bef-f422b00def70")
    private boolean grab() {
        return get() || (table != null /*&& table.isConnected()*/ && table.getBoolean("pressed", false)); //FIXME make is connected work?
    }

    /**
     * Starts the given command whenever the trigger just becomes active.
     * @param command the command to start
     */
    @objid ("5abaf704-163e-4a86-8245-10436829fef2")
    public void whenActive(final Command command) {
        new ButtonScheduler() {
        
            boolean pressedLast = grab();
        
            public void execute() {
                if (grab()) {
                    if (!pressedLast) {
                        pressedLast = true;
                        command.start();
                    }
                } else {
                    pressedLast = false;
                }
            }
        } .start();
    }

    /**
     * Constantly starts the given command while the button is held.
     * 
     * {@link Command#start()} will be called repeatedly while the trigger is active,
     * and will be canceled when the trigger becomes inactive.
     * @param command the command to start
     */
    @objid ("78a40f19-b222-48d4-b608-789ecffcac24")
    public void whileActive(final Command command) {
        new ButtonScheduler() {
        
            boolean pressedLast = grab();
        
            public void execute() {
                if (grab()) {
                    pressedLast = true;
                    command.start();
                } else {
                    if (pressedLast) {
                        pressedLast = false;
                        command.cancel();
                    }
                }
            }
        } .start();
    }

    /**
     * Starts the command when the trigger becomes inactive
     * @param command the command to start
     */
    @objid ("ac31166a-36a0-4f6d-81bf-2bba82700c38")
    public void whenInactive(final Command command) {
        new ButtonScheduler() {
        
            boolean pressedLast = grab();
        
            public void execute() {
                if (grab()) {
                    pressedLast = true;
                } else {
                    if (pressedLast) {
                        pressedLast = false;
                        command.start();
                    }
                }
            }
        } .start();
    }

    /**
     * Toggles a command when the trigger becomes active
     * @param command the command to toggle
     */
    @objid ("6741ea73-7845-4448-8018-b887b1ae7a36")
    public void toggleWhenActive(final Command command) {
        new ButtonScheduler() {
        
            boolean pressedLast = grab();
        
            public void execute() {
                if (grab()) {
                    if (!pressedLast) {
                        pressedLast = true;
                        if (command.isRunning()) {
                            command.cancel();
                        } else {
                            command.start();
                        }
                    }
                } else {
                    pressedLast = false;
                }
            }
        } .start();
    }

    /**
     * Cancels a command when the trigger becomes active
     * @param command the command to cancel
     */
    @objid ("2b9e9969-7f1c-4ec7-a319-6f16d8405ba7")
    public void cancelWhenActive(final Command command) {
        new ButtonScheduler() {
        
            boolean pressedLast = grab();
        
            public void execute() {
                if (grab()) {
                    if (!pressedLast) {
                        pressedLast = true;
                        command.cancel();
                    }
                } else {
                    pressedLast = false;
                }
            }
        } .start();
    }

    /**
     * These methods continue to return the "Button" SmartDashboard type until we decided
     * to create a Trigger widget type for the dashboard.
     */
    @objid ("65ed9df6-969c-4352-a4d5-e1d91663ca4e")
    public String getSmartDashboardType() {
        return "Button";
    }

    @objid ("07273270-8ad4-46f4-baa2-71f6257c484e")
    public void initTable(ITable table) {
        this.table = table;
        if(table!=null) {
            table.putBoolean("pressed", get());
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("6f27f5da-c7ff-4577-8140-76db70008883")
    public ITable getTable() {
        return table;
    }

    /**
     * An internal class of {@link Trigger}.  The user should ignore this, it is
     * only public to interface between packages.
     */
    @objid ("a2114b58-6e96-49be-9c94-ec24c338770c")
    public abstract class ButtonScheduler {
        @objid ("dded2615-8614-4d8b-a21a-09a8331e49a8")
        public abstract void execute();

        @objid ("66335363-c5b7-4cb5-905b-e21510b04cf6")
        protected void start() {
            Scheduler.getInstance().addButton(this);
        }

    }

}
