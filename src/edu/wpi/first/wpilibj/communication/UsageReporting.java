package edu.wpi.first.wpilibj.communication;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("a78ec866-5eb2-4cc3-aacb-b052f9893e1f")
public class UsageReporting {
    @objid ("a00c5b1f-18fa-42c8-8f3d-6104be45cce6")
    public static void report(int resource, int instanceNumber, int i) {
        report(resource, instanceNumber, i, "");
    }

    @objid ("f39db4e7-abe8-4e8f-b97d-d3425fe36885")
    public static void report(int resource, int instanceNumber) {
        report(resource, instanceNumber, 0, "");
    }

    @objid ("9cd2cc80-7d1a-455a-a77a-e932639fa693")
    public static void report(int resource, int instanceNumber, int i, String string) {
        FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationUsageReportingReport((byte)resource, (byte) instanceNumber, (byte) i, string);
    }

}
