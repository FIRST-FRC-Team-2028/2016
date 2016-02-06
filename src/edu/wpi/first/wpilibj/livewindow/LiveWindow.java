/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.livewindow;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindowComponent;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * The LiveWindow class is the public interface for putting sensors and
 * actuators on the LiveWindow.
 * 
 * @author Alex Henning
 */
@objid ("33e2b28e-cee6-4785-8680-7764a5b46130")
public class LiveWindow {
    @objid ("9c7c8863-5e8b-4986-be6a-e8893b5ba32f")
    private static Vector sensors = new Vector();

//    private static Vector actuators = new Vector();
    @objid ("53b14bae-358c-438c-80c5-ebf0be548d1a")
    private static Hashtable components = new Hashtable();

    @objid ("5f66b187-23dc-4337-99aa-3ebfeb969732")
    private static ITable livewindowTable;

    @objid ("cc83507b-25f1-44d8-9128-a178527d6e4d")
    private static ITable statusTable;

    @objid ("45377fba-8573-4edf-a889-ef582b995a5e")
    private static boolean liveWindowEnabled = false;

    @objid ("9d610cb5-5886-49f7-bbfb-2c4ed5485152")
    private static boolean firstTime = true;

    /**
     * Initialize all the LiveWindow elements the first time we enter LiveWindow
     * mode. By holding off creating the NetworkTable entries, it allows them to
     * be redefined before the first time in LiveWindow mode. This allows
     * default sensor and actuator values to be created that are replaced with
     * the custom names from users calling addActuator and addSensor.
     */
    @objid ("26a82d3d-849e-4597-9a79-bd3dd3dbd2a7")
    private static void initializeLiveWindowComponents() {
        System.out.println("Initializing the components first time");
        livewindowTable = NetworkTable.getTable("LiveWindow");
        statusTable = livewindowTable.getSubTable("~STATUS~");
        for (Enumeration e = components.keys(); e.hasMoreElements();) {
            LiveWindowSendable component = (LiveWindowSendable) e.nextElement();
            LiveWindowComponent c = (LiveWindowComponent) components.get(component);
            String subsystem = c.getSubsystem();
            String name = c.getName();
            System.out.println("Initializing table for '" + subsystem + "' '" + name + "'");
            livewindowTable.getSubTable(subsystem).putString("~TYPE~", "LW Subsystem");
            ITable table = livewindowTable.getSubTable(subsystem).getSubTable(name);
            table.putString("~TYPE~", component.getSmartDashboardType());
            table.putString("Name", name);
            table.putString("Subsystem", subsystem);
            component.initTable(table);
            if (c.isSensor()) {
                sensors.addElement(component);
            }
        }
    }

    /**
     * Set the enabled state of LiveWindow. If it's being enabled, turn off the
     * scheduler and remove all the commands from the queue and enable all the
     * components registered for LiveWindow. If it's being disabled, stop all
     * the registered components and reenable the scheduler. TODO: add code to
     * disable PID loops when enabling LiveWindow. The commands should reenable
     * the PID loops themselves when they get rescheduled. This prevents arms
     * from starting to move around, etc. after a period of adjusting them in
     * LiveWindow mode.
     */
    @objid ("d557fea6-a001-4faa-8144-6742083b5319")
    public static void setEnabled(boolean enabled) {
        if (liveWindowEnabled != enabled) {
            if (enabled) {
                System.out.println("Starting live window mode.");
                if (firstTime) {
                    initializeLiveWindowComponents();
                    firstTime = false;
                }
                Scheduler.getInstance().disable();
                Scheduler.getInstance().removeAll();
                for (Enumeration e = components.keys(); e.hasMoreElements();) {
                    LiveWindowSendable component = (LiveWindowSendable) e.nextElement();
                    component.startLiveWindowMode();
                }
            } else {
                System.out.println("stopping live window mode.");
                for (Enumeration e = components.keys(); e.hasMoreElements();) {
                    LiveWindowSendable component = (LiveWindowSendable) e.nextElement();
                    component.stopLiveWindowMode();
                }
                Scheduler.getInstance().enable();
            }
            liveWindowEnabled = enabled;
            statusTable.putBoolean("LW Enabled", enabled);
        }
    }

    /**
     * The run method is called repeatedly to keep the values refreshed on the screen in
     * test mode.
     */
    @objid ("240c027c-0374-4f7c-8c54-ee80a8fe72c2")
    public static void run() {
        updateValues();
    }

    /**
     * Add a Sensor associated with the subsystem and with call it by the given
     * name.
     * @param subsystem The subsystem this component is part of.
     * @param name The name of this component.
     * @param component A LiveWindowSendable component that represents a sensor.
     */
    @objid ("dc8d77e1-97b0-476b-beac-e267eb6467aa")
    public static void addSensor(String subsystem, String name, LiveWindowSendable component) {
        components.put(component, new LiveWindowComponent(subsystem, name, true));
    }

    /**
     * Add an Actuator associated with the subsystem and with call it by the
     * given name.
     * @param subsystem The subsystem this component is part of.
     * @param name The name of this component.
     * @param component A LiveWindowSendable component that represents a
     * actuator.
     */
    @objid ("5bfc4159-3b52-4d5b-834a-83db2254b54b")
    public static void addActuator(String subsystem, String name, LiveWindowSendable component) {
        components.put(component, new LiveWindowComponent(subsystem, name, false));
    }

    /**
     * Puts all sensor values on the live window.
     */
    @objid ("a2c4320c-3004-4406-99f6-cdf281d9fafc")
    private static void updateValues() {
        //TODO: gross - needs to be sped up
        for (int i = 0; i < sensors.size(); i++) {
            LiveWindowSendable lws = (LiveWindowSendable) sensors.elementAt(i);
            lws.updateTable();
        }
        // TODO: Add actuators?
        // TODO: Add better rate limiting.
    }

    /**
     * Add Sensor to LiveWindow. The components are shown with the type and
     * channel like this: Gyro[1] for a gyro object connected to the first
     * analog channel.
     * @param moduleType A string indicating the type of the module used in the
     * naming (above)
     * @param channel The channel number the device is connected to
     * @param component A reference to the object being added
     */
    @objid ("1547d7a9-9ed1-446c-8f00-4cc75baa86d6")
    public static void addSensor(String moduleType, int channel, LiveWindowSendable component) {
        addSensor("Ungrouped", moduleType + "[" + channel + "]", component);
        if (sensors.contains(component)) {
            sensors.removeElement(component);
        }
        sensors.addElement(component);
    }

    /**
     * Add Actuator to LiveWindow. The components are shown with the module
     * type, slot and channel like this: Servo[1,2] for a servo object connected
     * to the first digital module and PWM port 2.
     * @param moduleType A string that defines the module name in the label for
     * the value
     * @param channel The channel number the device is plugged into (usually
     * PWM)
     * @param component The reference to the object being added
     */
    @objid ("cecf956f-e452-4d13-8fc9-608684950632")
    public static void addActuator(String moduleType, int channel, LiveWindowSendable component) {
        addActuator("Ungrouped", moduleType + "[" + channel + "]", component);
    }

    /**
     * Add Actuator to LiveWindow. The components are shown with the module
     * type, slot and channel like this: Servo[1,2] for a servo object connected
     * to the first digital module and PWM port 2.
     * @param moduleType A string that defines the module name in the label for
     * the value
     * @param moduleNumber The number of the particular module type
     * @param channel The channel number the device is plugged into (usually
     * PWM)
     * @param component The reference to the object being added
     */
    @objid ("478478ce-d721-4565-a243-439354102bfa")
    public static void addActuator(String moduleType, int moduleNumber, int channel, LiveWindowSendable component) {
        addActuator("Ungrouped", moduleType + "[" + moduleNumber + "," + channel + "]", component);
    }

}

/**
 * A LiveWindow component is a device (sensor or actuator) that should be added to the
 * SmartDashboard in test mode. The components are cached until the first time the robot
 * enters Test mode. This allows the components to be inserted, then renamed.
 * @author brad
 */
@objid ("52731024-c1cb-4382-864d-9342a7f346dd")
class LiveWindowComponent {
    @objid ("77421a91-5ad6-4005-99d3-193affe1aba6")
     String m_subsystem;

    @objid ("e380c93e-1d01-416e-94cc-e1bfee23a886")
     String m_name;

    @objid ("7c23f0d9-3e1f-42a1-bb14-398ef8406c4e")
     boolean m_isSensor;

    @objid ("deff0975-372d-4f4a-92e0-de3dcee49378")
    public LiveWindowComponent(String subsystem, String name, boolean isSensor) {
        m_subsystem = subsystem;
        m_name = name;
        m_isSensor = isSensor;
    }

    @objid ("bd10ee40-8c82-4359-9a69-d9a4a0f01e53")
    public String getName() {
        return m_name;
    }

    @objid ("0c773821-4bed-4a5a-8bba-3572049bc0e1")
    public String getSubsystem() {
        return m_subsystem;
    }

    @objid ("9b5df7f7-213f-400d-8cc7-53d91a6c419c")
    public boolean isSensor() {
        return m_isSensor;
    }

}
