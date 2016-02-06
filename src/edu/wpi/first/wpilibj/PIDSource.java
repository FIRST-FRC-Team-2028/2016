/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * This interface allows for PIDController to automatically read from this
 * object
 * @author dtjones
 */
@objid ("184a7b97-3f3a-4def-a4ef-7f96af3072d4")
public interface PIDSource {
    /**
     * Get the result to use in PIDController
     * @return the result to use in PIDController
     */
    @objid ("ca309188-662b-41b8-b7ac-ca0323b9c333")
    double pidGet();

    /**
     * A description for the type of output value to provide to a PIDController
     */
    @objid ("43b00997-6fe4-4bf4-a343-5f9e26ae1062")
    public static class PIDSourceParameter {
        @objid ("e8199b7a-8e2a-4c38-9462-a7a839c8bdb9")
        public final int value;

        @objid ("522041cb-86c0-4f59-8b7c-4a3a08c7d9c9")
         static final int kDistance_val = 0;

        @objid ("5d8b6548-9c4d-4b8d-80fe-4a7e5d297938")
         static final int kRate_val = 1;

        @objid ("ea084850-b1a2-46ff-93c9-a488f8f31da1")
         static final int kAngle_val = 2;

        @objid ("2275f4c6-ac6f-4483-a092-3f2ea0f394fc")
        public static final PIDSourceParameter kDistance = new PIDSourceParameter(kDistance_val);

        @objid ("9ae65461-e9e1-4c74-92e3-044d5529f086")
        public static final PIDSourceParameter kRate = new PIDSourceParameter(kRate_val);

        @objid ("c2e0a3aa-787d-46dd-a1c6-ef6bed0ba0f2")
        public static final PIDSourceParameter kAngle = new PIDSourceParameter(kAngle_val);

        @objid ("fbec72ac-2c21-48f4-a4d8-050f665b8822")
        private PIDSourceParameter(int value) {
            this.value = value;
        }

    }

}
