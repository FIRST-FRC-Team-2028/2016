package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.util.BaseSystemNotInitializedException;

/**
 * Support for high level usage reporting.
 * 
 * @author alex
 */
@objid ("b7d2a7b5-e811-40e3-99d3-de2d432dfbc8")
public class HLUsageReporting {
    @objid ("eb76136b-ce95-4af9-a216-3d0c3162bd3b")
    private static Interface impl;

    @objid ("a4ac94af-9fcc-4cc7-94fc-e0390f0a3e11")
    public static void SetImplementation(Interface i) {
        impl = i;
    }

    @objid ("0479b96e-911c-4657-b10e-d49784b29ba4")
    public static void reportScheduler() {
        if (impl != null) {
            impl.reportScheduler();
        } else {
            throw new BaseSystemNotInitializedException(Interface.class, HLUsageReporting.class);
        }
    }

    @objid ("c0d31d8e-f876-4e5c-8612-4b9b626f8876")
    public static void reportPIDController(int num) {
        if (impl != null) {
            impl.reportPIDController(num);
        } else {
            throw new BaseSystemNotInitializedException(Interface.class, HLUsageReporting.class);
        }
    }

    @objid ("c38bc88a-35e8-4f3c-abaa-9a20f60482c8")
    public static void reportSmartDashboard() {
        if(impl != null) {
            impl.reportSmartDashboard();
        } else {
            throw new BaseSystemNotInitializedException(Interface.class, HLUsageReporting.class);
        }
    }

    @objid ("3052125d-9441-49b4-876c-36b772e459d8")
    public interface Interface {
        @objid ("da3f56cd-1098-415f-8f31-af7035faf393")
        void reportScheduler();

        @objid ("cedd4c84-9d0c-47d1-8184-ce4f30d0e70e")
        void reportPIDController(int num);

        @objid ("ae368c6d-b6c1-44a5-8a79-e30d1ad93be3")
        void reportSmartDashboard();

    }

    @objid ("3307e574-8f3a-49b8-9fe7-a3888b6f52c7")
    public static class Null implements Interface {
        @objid ("2b41d3ec-89e5-48c7-a990-1adb748922fe")
        public void reportScheduler() {
        }

        @objid ("ce53c1ff-3e22-4e7e-aee7-6c1d4ced0246")
        public void reportPIDController(int num) {
        }

        @objid ("515fdc3c-77cf-4d88-a371-538ec1dad7d3")
        public void reportSmartDashboard() {
        }

    }

}
