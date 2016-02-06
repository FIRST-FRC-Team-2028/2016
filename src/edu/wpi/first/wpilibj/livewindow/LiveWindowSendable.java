/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.livewindow;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.Sendable;

/**
 * Live Window Sendable is a special type of object sendable to the live window.
 * 
 * @author Alex Henning
 */
@objid ("e90f1959-8092-462a-8dca-4c14a43b9cdd")
public interface LiveWindowSendable extends Sendable {
    /**
     * Update the table for this sendable object with the latest
     * values.
     */
    @objid ("ce433526-2333-4c63-9845-ba3f9f90895d")
    void updateTable();

    /**
     * Start having this sendable object automatically respond to
     * value changes reflect the value on the table.
     */
    @objid ("a599feff-8661-4952-bc07-0b8ef6629063")
    void startLiveWindowMode();

    /**
     * Stop having this sendable object automatically respond to value
     * changes.
     */
    @objid ("32ac3b5d-e4be-4c1a-bd2b-37f22c040416")
    void stopLiveWindowMode();

}
