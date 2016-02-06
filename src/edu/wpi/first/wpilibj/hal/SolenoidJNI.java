package edu.wpi.first.wpilibj.hal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("9471e81a-3970-4adf-bd68-9789e39964e8")
public class SolenoidJNI extends JNIWrapper {
    @objid ("a57610e4-0b15-443f-be3e-42ee38fa71d5")
    public static native ByteBuffer initializeSolenoidPort(ByteBuffer portPointer, IntBuffer status);

    @objid ("9c40fc27-daba-44e9-befa-0b6174e77de7")
    public static native ByteBuffer getPortWithModule(byte module, byte channel);

    @objid ("a8673c9e-f70b-4a0b-b555-5d2ffb9b48af")
    public static native void setSolenoid(ByteBuffer port, byte on, IntBuffer status);

    @objid ("af59fa98-bceb-486d-81de-bd795d84cc87")
    public static native byte getSolenoid(ByteBuffer port, IntBuffer status);

    @objid ("72499e41-70c4-480c-a67e-1b1e732243f7")
    public static native byte getPCMSolenoidBlackList(ByteBuffer pcm_pointer, IntBuffer status);

    @objid ("9619172a-bf85-4e6b-89e2-334ccd638eff")
    public static native boolean getPCMSolenoidVoltageStickyFault(ByteBuffer pcm_pointer, IntBuffer status);

    @objid ("4090698a-4566-433d-a0f2-6e7b0956e488")
    public static native boolean getPCMSolenoidVoltageFault(ByteBuffer pcm_pointer, IntBuffer status);

    @objid ("6224af1a-3a63-4f4e-bf6b-8a0643aca994")
    public static native void clearAllPCMStickyFaults(ByteBuffer pcm_pointer, IntBuffer status);

}
