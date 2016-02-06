package edu.wpi.first.wpilibj.hal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("89c050c0-6423-4216-8509-59929d99f1fe")
public class CompressorJNI extends JNIWrapper {
    @objid ("0cfc970f-5e0b-4c21-affc-829eee712fa2")
    public static native ByteBuffer initializeCompressor(byte module);

    @objid ("28cf5539-0730-4a60-b153-c0e3830bf6bc")
    public static native boolean checkCompressorModule(byte module);

    @objid ("ee3f9508-b786-4fc7-bfa3-7e3659fad153")
    public static native boolean getCompressor(ByteBuffer pcm_pointer, IntBuffer status);

    @objid ("a2b6e712-1863-4529-9485-0f4f761f2bde")
    public static native void setClosedLoopControl(ByteBuffer pcm_pointer, boolean value, IntBuffer status);

    @objid ("4ea20bd9-33b0-4cee-9054-18228b0f84f8")
    public static native boolean getClosedLoopControl(ByteBuffer pcm_pointer, IntBuffer status);

    @objid ("33f32748-1dd0-45c8-bafa-bc851e402675")
    public static native boolean getPressureSwitch(ByteBuffer pcm_pointer, IntBuffer status);

    @objid ("9317e319-6ac5-434f-af74-9135971385eb")
    public static native float getCompressorCurrent(ByteBuffer pcm_pointer, IntBuffer status);

    @objid ("44972c56-2ed6-4d46-956c-48131bb0a4a8")
    public static native boolean getCompressorCurrentTooHighFault(ByteBuffer pcm_pointer, IntBuffer status);

    @objid ("2a079c01-919b-4c91-80d7-2465f71cdfe3")
    public static native boolean getCompressorCurrentTooHighStickyFault(ByteBuffer pcm_pointer, IntBuffer status);

    @objid ("f0e4a172-f4fb-4b25-8e1d-b990b508b7e7")
    public static native boolean getCompressorShortedStickyFault(ByteBuffer pcm_pointer, IntBuffer status);

    @objid ("1f615626-3f1e-40b9-833d-10c4c5a642b9")
    public static native boolean getCompressorShortedFault(ByteBuffer pcm_pointer, IntBuffer status);

    @objid ("252c666d-19da-4448-826b-cb03237bcbc8")
    public static native boolean getCompressorNotConnectedStickyFault(ByteBuffer pcm_pointer, IntBuffer status);

    @objid ("e9913013-f24b-4649-8e22-1dd0876bf1e7")
    public static native boolean getCompressorNotConnectedFault(ByteBuffer pcm_pointer, IntBuffer status);

    @objid ("cbd8d5f9-9f2e-4fdc-a552-7535e09cf2e8")
    public static native void clearAllPCMStickyFaults(ByteBuffer pcm_pointer, IntBuffer status);

}
