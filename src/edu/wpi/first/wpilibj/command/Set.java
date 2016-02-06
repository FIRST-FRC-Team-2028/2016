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
 * @author Greg
 */
@objid ("3b6b531a-192f-47fe-aa71-f53472f1ce9e")
class Set {
    @objid ("f2f50389-21b8-48f4-8040-4e723c1e2d19")
     Vector set = new Vector();

    @objid ("488520c5-2e84-4d55-8f86-cf329a5ae502")
    public Set() {
    }

    @objid ("4a32a4ed-6a30-4b1e-954f-309b0b914e93")
    public void add(Object o) {
        if(set.contains(o)) return;
        set.addElement(o);
    }

    @objid ("3f41e6b2-8c4c-4fa5-9153-ea8d5a028f47")
    public void add(Set s) {
        Enumeration stuff = s.getElements();
        for(Enumeration e = stuff; e.hasMoreElements();) {
            add(e.nextElement());
        }
    }

    @objid ("cb6b9db8-e052-4c16-8b3f-675f571a13ba")
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @objid ("f8b0c36e-a238-40a8-94b5-e7ad78c5b955")
    public Enumeration getElements() {
        return set.elements();
    }

}
