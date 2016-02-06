package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.util.BaseSystemNotInitializedException;

/**
 * <Enter note text here>
 */
@objid ("56a4c0b7-1055-47a3-98e1-15f77539e005")
public class RobotState {
    @objid ("533a057c-34ec-4aff-a063-ca1a583b323c")
    private static Interface impl;

    @objid ("0c040852-1229-4273-adf0-209295e3b0fa")
    public static void SetImplementation(Interface i) {
        impl = i;
    }

    @objid ("6f04e46f-77b1-453f-be5d-b1d8588919e8")
    public static boolean isDisabled() {
        if (impl != null) {
            return impl.isDisabled();
        } else {
            throw new BaseSystemNotInitializedException(Interface.class, RobotState.class);
        }
    }

    @objid ("26988e2a-f3c4-42e1-90dd-3e9fbe36e358")
    public static boolean isEnabled() {
        if (impl != null) {
            return impl.isEnabled();
        } else {
            throw new BaseSystemNotInitializedException(Interface.class, RobotState.class);
        }
    }

    @objid ("e4de1a8f-a9fa-4420-8ad8-ca60b2ad1446")
    public static boolean isOperatorControl() {
        if (impl != null) {
            return impl.isOperatorControl();
        } else {
            throw new BaseSystemNotInitializedException(Interface.class, RobotState.class);
        }
    }

    @objid ("69ee18b9-08ce-4230-9d85-90597460a419")
    public static boolean isAutonomous() {
        if (impl != null) {
            return impl.isAutonomous();
        } else {
            throw new BaseSystemNotInitializedException(Interface.class, RobotState.class);
        }
    }

    @objid ("6250d18c-a520-4b96-90a8-a9db27f04122")
    public static boolean isTest() {
        if (impl != null) {
            return impl.isTest();
        } else {
            throw new BaseSystemNotInitializedException(Interface.class, RobotState.class);
        }
    }

    @objid ("84a33278-1b90-4b3e-ada2-cbc13cfe3b98")
    interface Interface {
        @objid ("0022666c-0f4b-4a55-a312-e4b79df16b56")
        boolean isDisabled();

        @objid ("0de57e1e-9d22-48e2-a4f8-ebe8b739f347")
        boolean isEnabled();

        @objid ("bb5e32b1-254e-49a9-b1ad-f102b857b047")
        boolean isOperatorControl();

        @objid ("9f74150f-6c27-40c1-9ee2-80a2cadd30dc")
        boolean isAutonomous();

        @objid ("c12b2d3c-3022-4881-98ef-c2f179eb7e7c")
        boolean isTest();

    }

}
