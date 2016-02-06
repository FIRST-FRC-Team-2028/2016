/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.smartdashboard;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables2.type.StringArray;
import edu.wpi.first.wpilibj.networktables2.util.List;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * The {@link SendableChooser} class is a useful tool for presenting a selection
 * of options to the {@link SmartDashboard}.
 * 
 * <p>For instance, you may wish to be able to select between multiple
 * autonomous modes. You can do this by putting every possible {@link Command}
 * you want to run as an autonomous into a {@link SendableChooser} and then put
 * it into the {@link SmartDashboard} to have a list of options appear on the
 * laptop. Once autonomous starts, simply ask the {@link SendableChooser} what
 * the selected value is.</p>
 * 
 * @author Joe Grinstead
 */
@objid ("0a712867-d864-4c2d-ad3b-ee243d610268")
public class SendableChooser implements Sendable {
    /**
     * The key for the default value
     */
    @objid ("98eab2b3-53a0-467f-b8ce-abd78e02fcd5")
    private static final String DEFAULT = "default";

    /**
     * The key for the selected option
     */
    @objid ("1dc27bc3-09ae-4f6a-9a91-c430dec871eb")
    private static final String SELECTED = "selected";

    /**
     * The key for the option array
     */
    @objid ("0fc67e3e-db61-47db-87a5-9073bb569eb4")
    private static final String OPTIONS = "options";

    /**
     * A table linking strings to the objects the represent
     */
    @objid ("110def7d-f645-41be-beb7-4f3aa84f03e9")
    private StringArray choices = new StringArray();

    @objid ("ab2214cd-d43f-4f01-b64e-8a71142521dc")
    private List values = new List();

    @objid ("7f83a8b4-baf6-44ba-a2bb-7bcbafdae76d")
    private String defaultChoice = null;

    @objid ("d70a60bd-d585-465b-a132-491d6b247cf6")
    private Object defaultValue = null;

    @objid ("73d36a1d-fa6d-4a4b-bd5a-f1627d43a6e6")
    private ITable table;

    /**
     * Instantiates a {@link SendableChooser}.
     */
    @objid ("c82a6e8b-6c4b-4d85-9ff2-ed10964bddec")
    public SendableChooser() {
    }

    /**
     * Adds the given object to the list of options. On the
     * {@link SmartDashboard} on the desktop, the object will appear as the
     * given name.
     * @param name the name of the option
     * @param object the option
     */
    @objid ("cdb4921e-c387-4df0-a0fd-7020471b7c24")
    public void addObject(String name, Object object) {
        //if we don't have a default, set the default automatically
        if (defaultChoice == null) {
            addDefault(name, object);
            return;
        }
        for (int i = 0; i < choices.size(); ++i) {
            if (choices.get(i).equals(name)) {
                choices.set(i, name);
                values.set(i, object);
                return;
            }
        }
        //not found
        choices.add(name);
        values.add(object);
        if (table != null) {
            table.putValue(OPTIONS, choices);
        }
    }

    /**
     * Add the given object to the list of options and marks it as the default.
     * Functionally, this is very close to
     * {@link SendableChooser#addObject(java.lang.String, java.lang.Object) addObject(...)}
     * except that it will use this as the default option if none other is
     * explicitly selected.
     * @param name the name of the option
     * @param object the option
     */
    @objid ("0fd456ce-c3fe-4d7c-a84a-6ae8f2aeed90")
    public void addDefault(String name, Object object) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        defaultChoice = name;
        defaultValue = object;
        if (table != null) {
            table.putString(DEFAULT, defaultChoice);
        }
        addObject(name, object);
    }

    /**
     * Returns the selected option. If there is none selected, it will return
     * the default. If there is none selected and no default, then it will
     * return {@code null}.
     * @return the option selected
     */
    @objid ("4c891351-e0db-423f-a290-c34dcfd2f6c3")
    public Object getSelected() {
        String selected = table.getString(SELECTED, null);
        for (int i = 0; i < values.size(); ++i) {
            if (choices.get(i).equals(selected)) {
                return values.get(i);
            }
        }
        return defaultValue;
    }

    @objid ("ace947ea-8131-490a-a562-e9b8e3338c0e")
    public String getSmartDashboardType() {
        return "String Chooser";
    }

    @objid ("1eabd68e-9e2e-4212-bc1f-b72e7d08d088")
    public void initTable(ITable table) {
        this.table = table;
        if (table != null) {
            table.putValue(OPTIONS, choices);
            if (defaultChoice != null) {
                table.putString(DEFAULT, defaultChoice);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("491550ff-07d5-48bb-864c-9ea9fa4cf245")
    public ITable getTable() {
        return table;
    }

}
