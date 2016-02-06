package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * The base interface for objects that can be sent over the network
 * through network tables.
 */
@objid ("4371a6ba-f243-4259-9990-5209254267b0")
public interface Sendable {
    /**
     * Initializes a table for this sendable object.
     * @param subtable The table to put the values in.
     */
    @objid ("c6f18f6e-8183-41ba-a447-9abfa83f60ed")
    void initTable(ITable subtable);

    /**
     * @return the table that is currently associated with the sendable
     */
    @objid ("5bc2487d-56c2-44ba-8f4e-7e806c5cbaa1")
    ITable getTable();

    /**
     * @return the string representation of the named data type that will be used by the smart dashboard for this sendable
     */
    @objid ("f6a230b0-8cf7-429c-9d0c-60144b98a180")
    String getSmartDashboardType();

}
