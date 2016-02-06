package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * The interface for sendable objects that gives the sendable a default name in the Smart Dashboard
 */
@objid ("53cff6b9-2e05-46e1-9729-f55af9f14904")
public interface NamedSendable extends Sendable {
    /**
     * @return the name of the subtable of SmartDashboard that the Sendable object will use
     */
    @objid ("77707d55-40f6-4369-9a3d-7cc7f4457ff4")
    String getName();

}
