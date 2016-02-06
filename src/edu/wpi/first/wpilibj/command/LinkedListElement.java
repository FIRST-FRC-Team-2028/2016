/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.command;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * @author Greg
 */
@objid ("d5a4d9b6-27a0-4d54-b80a-371bd66ae6a1")
class LinkedListElement {
    @objid ("faa20359-3fa7-4fc8-8e57-3da61aff63a7")
    private LinkedListElement next;

    @objid ("72f3ceaa-0dae-4b6e-a3b7-87c3196912d1")
    private LinkedListElement previous;

    @objid ("eb816d09-cef7-4d9c-96a0-b52bc83c67ad")
    private Command data;

    @objid ("1434d72e-2249-41fc-afe7-97103b2a5b27")
    public LinkedListElement() {
    }

    @objid ("f11e029e-df20-4409-8f56-7f5a6930acbe")
    public void setData(Command newData) {
        data = newData;
    }

    @objid ("48ce5e58-99eb-4f91-bc8d-25241630dda0")
    public Command getData() {
        return data;
    }

    @objid ("3076fa9f-cabb-4e1e-8759-1da5d1219731")
    public LinkedListElement getNext() {
        return next;
    }

    @objid ("263f2996-d5c8-448c-ab39-588ec1e6cf77")
    public LinkedListElement getPrevious() {
        return previous;
    }

    @objid ("31cb09da-4b02-4cc7-b528-23a5ca0e8386")
    public void add(LinkedListElement l) {
        if(next == null) {
            next = l;
            next.previous = this;
        } else {
            next.previous = l;
            l.next = next;
            l.previous = this;
            next = l;
        }
    }

    @objid ("68b7369b-84b1-41a4-b029-f820e39f7004")
    public LinkedListElement remove() {
        if(previous == null && next == null) {
        
        } else if(next == null) {
            previous.next = null;
        } else if(previous == null) {
            next.previous = null;
        } else {
            next.previous = previous;
            previous.next = next;
        }
        LinkedListElement n = next;
        next = null;
        previous = null;
        return n;
    }

}
