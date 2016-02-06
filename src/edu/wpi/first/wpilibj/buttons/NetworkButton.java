/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.buttons;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * @author Joe
 */
@objid ("950315f1-64ad-4769-8e68-d0c2bee21c55")
public class NetworkButton extends Button {
    @objid ("36a5df52-2088-43ae-a824-b0cf7d297b8b")
     NetworkTable table;

    @objid ("b4430169-de7f-4847-bf6c-edd8227bf0c6")
     String field;

    @objid ("608cb1b8-fa20-45c0-ae9c-2b7b3a482305")
    public NetworkButton(String table, String field) {
        this(NetworkTable.getTable(table), field);
    }

    @objid ("2950e404-68c0-43e7-aa41-ba2de114bc3c")
    public NetworkButton(NetworkTable table, String field) {
        this.table = table;
        this.field = field;
    }

    @objid ("369e1fef-15c3-4eba-a9c2-c78800015c23")
    public boolean get() {
        return table.isConnected() && table.getBoolean(field, false);
    }

}
