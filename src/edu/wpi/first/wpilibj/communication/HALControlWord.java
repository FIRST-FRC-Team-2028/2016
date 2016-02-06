package edu.wpi.first.wpilibj.communication;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * A wrapper for the HALControlWord bitfield
 */
@objid ("3457b57a-a092-4003-88cf-76ccd333f981")
public class HALControlWord {
    @objid ("eae4e6a3-c385-41c1-8d8c-05cc6bbdd1dd")
    private boolean m_enabled;

    @objid ("4c63095d-cd31-4ebc-beb1-15f2b4ae1bd5")
    private boolean m_autonomous;

    @objid ("7a0a722f-288a-4510-94fe-bc33b7947238")
    private boolean m_test;

    @objid ("3e0b03a2-763b-4161-a22d-14a2bf300707")
    private boolean m_eStop;

    @objid ("0f398d57-f78d-4243-a063-7d339edea2d4")
    private boolean m_fmsAttached;

    @objid ("1ae6dab0-cfc4-48aa-8523-182803d41d48")
    private boolean m_dsAttached;

    @objid ("2d657427-cea7-4ede-9283-de75f36f8d1d")
    protected HALControlWord(boolean enabled, boolean autonomous, boolean test, boolean eStop, boolean fmsAttached, boolean dsAttached) {
        m_enabled = enabled;
        m_autonomous = autonomous;
        m_test = test;
        m_eStop = eStop;
        m_fmsAttached = fmsAttached;
        m_dsAttached = dsAttached;
    }

    @objid ("daa5d559-d8cb-4c5e-a330-dd163b2bfcda")
    public boolean getEnabled() {
        return m_enabled;
    }

    @objid ("dc7e36e0-a3e4-4bf7-bb89-7d69c0f36a67")
    public boolean getAutonomous() {
        return m_autonomous;
    }

    @objid ("95920e3f-a8f3-4164-bc67-41fd9dd072c2")
    public boolean getTest() {
        return m_test;
    }

    @objid ("60d6d14c-6854-4a91-bb66-081caceab134")
    public boolean getEStop() {
        return m_eStop;
    }

    @objid ("0fe672fc-59f2-4762-bbdc-62ee69edc700")
    public boolean getFMSAttached() {
        return m_fmsAttached;
    }

    @objid ("409b6478-4d12-4e85-9d63-cb3b8f5c4203")
    public boolean getDSAttached() {
        return m_dsAttached;
    }

}
