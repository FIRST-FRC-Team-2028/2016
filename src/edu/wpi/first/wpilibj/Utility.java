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
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary;
import edu.wpi.first.wpilibj.hal.HALLibrary;
import edu.wpi.first.wpilibj.hal.HALUtil;

/**
 * Contains global utility functions
 */
@objid ("7111317c-8f40-4ae3-9a9c-c863ce56cad2")
public class Utility {
    @objid ("e9e74dcb-e9f0-4749-893d-04c702b30383")
    private Utility() {
    }

    /**
     * Return the FPGA Version number. For now, expect this to be 2009.
     * @return FPGA Version number.
     */
    @objid ("149aa9b0-0a4a-4265-8c78-46c7e07e4456")
    int getFPGAVersion() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        int value = HALUtil.getFPGAVersion(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

    /**
     * Return the FPGA Revision number. The format of the revision is 3 numbers.
     * The 12 most significant bits are the Major Revision. the next 8 bits are
     * the Minor Revision. The 12 least significant bits are the Build Number.
     * @return FPGA Revision number.
     */
    @objid ("6299330a-230b-465e-be48-d1349492f862")
    long getFPGARevision() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        int value = HALUtil.getFPGARevision(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return (long) value;
    }

    /**
     * Read the microsecond timer from the FPGA.
     * @return The current time in microseconds according to the FPGA.
     */
    @objid ("07ad551b-ec61-4e5c-a1d1-4a9d0720433c")
    public static long getFPGATime() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        long value = HALUtil.getFPGATime(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

    /**
     * Get the state of the "USER" button on the RoboRIO
     * @return true if the button is currently pressed down
     */
    @objid ("d7ee628c-db1b-4707-839e-4dba12f1cbed")
    public static boolean getUserButton() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        boolean value = HALUtil.getFPGAButton(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return value;
    }

}
