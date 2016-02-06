package edu.wpi.first.wpilibj.hal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.DriverStation;

@objid ("2fedb0cd-3b87-4377-aef8-023d9794cfd5")
public class HALUtil extends JNIWrapper {
    @objid ("7ca4c043-e982-40cb-b2ad-52a22f3a7655")
    public static final int NULL_PARAMETER = -1005;

    @objid ("eaa4145a-9256-4519-bc4f-b5c56aeb0264")
    public static final int SAMPLE_RATE_TOO_HIGH = 1001;

    @objid ("d01cbd11-a685-4b2e-ac9b-fc44ceafa5fa")
    public static final int VOLTAGE_OUT_OF_RANGE = 1002;

    @objid ("e3292fe1-c9db-4a83-8516-060b6382e6b6")
    public static final int LOOP_TIMING_ERROR = 1004;

    @objid ("4981cf12-8298-402e-89b2-b444582b29d4")
    public static final int INCOMPATIBLE_STATE = 1015;

    @objid ("69fff371-8646-4372-bb51-c36de0a0e4e0")
    public static final int ANALOG_TRIGGER_PULSE_OUTPUT_ERROR = -1011;

    @objid ("dbf1da56-a800-4b31-871e-16764803aaca")
    public static final int NO_AVAILABLE_RESOURCES = -104;

    @objid ("18936b3a-4765-438e-8397-cf3f809bed8e")
    public static final int PARAMETER_OUT_OF_RANGE = -1028;

//public static final int SEMAPHORE_WAIT_FOREVER = -1;
//public static final int SEMAPHORE_Q_PRIORITY = 0x01;
    @objid ("28d51f53-0340-4ac7-999f-1a8404b70b33")
    public static native ByteBuffer initializeMutexNormal();

    @objid ("27cd8812-34e3-456f-b2b5-f961480454dd")
    public static native void deleteMutex(ByteBuffer sem);

    @objid ("443cfee1-f6a1-40e3-8def-3a1c6c9f1e20")
    public static native byte takeMutex(ByteBuffer sem);

//public static native ByteBuffer initializeSemaphore(int initialValue);
//public static native void deleteSemaphore(ByteBuffer sem);
//public static native byte takeSemaphore(ByteBuffer sem, int timeout);
    @objid ("1ad9b922-a7b5-41cd-9ca2-c5f8aa220539")
    public static native ByteBuffer initializeMultiWait();

    @objid ("005c0a44-53d8-48b3-a864-aff87de3bd86")
    public static native void deleteMultiWait(ByteBuffer sem);

    @objid ("8bd6680e-5e71-40e3-9dbd-31272e37eed9")
    public static native byte takeMultiWait(ByteBuffer sem, ByteBuffer m, int timeOut);

    @objid ("6cf8567c-0ef6-44f8-bb81-6c9b9ffb53dc")
    public static native short getFPGAVersion(IntBuffer status);

    @objid ("c1018d8e-4830-464c-b149-70643479a68c")
    public static native int getFPGARevision(IntBuffer status);

    @objid ("b20746d4-adff-4dc4-8c85-88431aff422e")
    public static native long getFPGATime(IntBuffer status);

    @objid ("5812b497-5798-45df-b795-8dd05cd1a8b6")
    public static native boolean getFPGAButton(IntBuffer status);

    @objid ("9aceca35-421c-40dd-bc31-08ef87e52516")
    public static native String getHALErrorMessage(int code);

    @objid ("feb7d76f-fcb3-4d55-8da0-06f85cf1d936")
    public static native int getHALErrno();

    @objid ("24a01d6a-342e-47db-a434-48318ba15830")
    public static native String getHALstrerror(int errno);

    @objid ("b24542c9-e3d8-440e-8a56-9a7782e0f48c")
    public static String getHALstrerror() {
        return getHALstrerror(getHALErrno());
    }

    @objid ("60953087-591d-4372-94e4-b0a9501b637c")
    public static void checkStatus(IntBuffer status) {
        int s = status.get(0);
        if (s < 0)
        {
            String message = getHALErrorMessage(s);
            throw new RuntimeException(" Code: " + s + ". " + message);
        } else if (s > 0) {
            String message = getHALErrorMessage(s);
            DriverStation.reportError(message, true);
        }
    }

}
