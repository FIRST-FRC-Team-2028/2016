package edu.wpi.first.wpilibj.internal;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.HLUsageReporting;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tInstances;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;

@objid ("7d975439-c753-43db-9847-8b1d28efdfa5")
public class HardwareHLUsageReporting implements edu.wpi.first.wpilibj.HLUsageReporting.Interface {
    @objid ("9cd20044-d3d9-4277-a7c2-87d7de5feede")
    @Override
    public void reportScheduler() {
        UsageReporting.report(tResourceType.kResourceType_Command, tInstances.kCommand_Scheduler);
    }

    @objid ("04c112a1-48eb-479e-8548-a345f3b66c4d")
    @Override
    public void reportPIDController(int num) {
        UsageReporting.report(tResourceType.kResourceType_PIDController, num);
    }

    @objid ("edd17454-0f45-4897-a37b-a22c99ddf2ca")
    @Override
    public void reportSmartDashboard() {
        UsageReporting.report(tResourceType.kResourceType_SmartDashboard, 0);
    }

}
