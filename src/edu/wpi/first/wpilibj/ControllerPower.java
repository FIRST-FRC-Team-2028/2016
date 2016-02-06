/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.hal.HALUtil;
import edu.wpi.first.wpilibj.hal.PowerJNI;

@objid ("79d03d6b-e748-44d1-b08d-2b83af3d85e7")
public class ControllerPower {
    /**
     * Get the input voltage to the robot controller
     * @return The controller input voltage value in Volts
     */
    @objid ("89c38324-447f-4319-b003-72e0c6e0a577")
    public static double getInputVoltage() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        double retVal = PowerJNI.getVinVoltage(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retVal;
    }

    /**
     * Get the input current to the robot controller
     * @return The controller input current value in Amps
     */
    @objid ("5f0ad4fb-9111-4778-b596-f09f0f073cd5")
    public static double getInputCurrent() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        double retVal = PowerJNI.getVinCurrent(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retVal;
    }

    /**
     * Get the voltage of the 3.3V rail
     * @return The controller 3.3V rail voltage value in Volts
     */
    @objid ("a64bfa7e-78fb-4b8b-99a6-70527711e42a")
    public static double getVoltage3V3() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        double retVal = PowerJNI.getUserVoltage3V3(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retVal;
    }

    /**
     * Get the current output of the 3.3V rail
     * @return The controller 3.3V rail output current value in Volts
     */
    @objid ("4fcf7525-b07e-4128-a13f-acc85ff39859")
    public static double getCurrent3V3() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        double retVal = PowerJNI.getUserCurrent3V3(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retVal;
    }

    /**
     * Get the enabled state of the 3.3V rail. The rail may be disabled due to a controller
     * brownout, a short circuit on the rail, or controller over-voltage
     * @return The controller 3.3V rail enabled value
     */
    @objid ("fc64e290-0e30-47a9-b3ef-5bbeeea88d91")
    public static boolean getEnabled3V3() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        boolean retVal = PowerJNI.getUserActive3V3(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retVal;
    }

    /**
     * Get the count of the total current faults on the 3.3V rail since the controller has booted
     * @return The number of faults
     */
    @objid ("ab79cc66-28d9-407f-bbe7-06597ebbb80f")
    public static int getFaultCount3V3() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        int retVal = PowerJNI.getUserCurrentFaults3V3(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retVal;
    }

    /**
     * Get the voltage of the 5V rail
     * @return The controller 5V rail voltage value in Volts
     */
    @objid ("b8470d35-a0d2-4357-94d2-a4c39a3d67bb")
    public static double getVoltage5V() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        double retVal = PowerJNI.getUserVoltage5V(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retVal;
    }

    /**
     * Get the current output of the 5V rail
     * @return The controller 5V rail output current value in Amps
     */
    @objid ("1efac830-67a4-46c6-8e8d-6ae108ae83e2")
    public static double getCurrent5V() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        double retVal = PowerJNI.getUserCurrent5V(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retVal;
    }

    /**
     * Get the enabled state of the 5V rail. The rail may be disabled due to a controller
     * brownout, a short circuit on the rail, or controller over-voltage
     * @return The controller 5V rail enabled value
     */
    @objid ("1fa559fc-c355-4a9c-9592-de1cfa1d413b")
    public static boolean getEnabled5V() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        boolean retVal = PowerJNI.getUserActive5V(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retVal;
    }

    /**
     * Get the count of the total current faults on the 5V rail since the controller has booted
     * @return The number of faults
     */
    @objid ("76e071ec-a522-46c4-9462-70cc9ca9be8e")
    public static int getFaultCount5V() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        int retVal = PowerJNI.getUserCurrentFaults5V(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retVal;
    }

    /**
     * Get the voltage of the 6V rail
     * @return The controller 6V rail voltage value in Volts
     */
    @objid ("8dbafeb5-383c-48ea-ba4f-b033a4aaa9ae")
    public static double getVoltage6V() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        double retVal = PowerJNI.getUserVoltage6V(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retVal;
    }

    /**
     * Get the current output of the 6V rail
     * @return The controller 6V rail output current value in Amps
     */
    @objid ("03baa2b3-1647-4b3f-a6df-497888ee6d83")
    public static double getCurrent6V() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        double retVal = PowerJNI.getUserCurrent6V(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retVal;
    }

    /**
     * Get the enabled state of the 6V rail. The rail may be disabled due to a controller
     * brownout, a short circuit on the rail, or controller over-voltage
     * @return The controller 6V rail enabled value
     */
    @objid ("f16fe9f0-92cf-4f38-9ca8-44026686abf4")
    public static boolean getEnabled6V() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        boolean retVal = PowerJNI.getUserActive6V(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retVal;
    }

    /**
     * Get the count of the total current faults on the 6V rail since the controller has booted
     * @return The number of faults
     */
    @objid ("3c39fb65-d2b1-4e6e-9ba4-f53ba777beba")
    public static int getFaultCount6V() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        int retVal = PowerJNI.getUserCurrentFaults6V(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retVal;
    }

}
