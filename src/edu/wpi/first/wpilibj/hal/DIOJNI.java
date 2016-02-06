package edu.wpi.first.wpilibj.hal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("5281f511-89e5-4304-b97a-f6256cfcd588")
public class DIOJNI extends JNIWrapper {
    @objid ("4e14634d-0d28-446a-a775-c80602766406")
    public static native ByteBuffer initializeDigitalPort(ByteBuffer port_pointer, IntBuffer status);

    @objid ("5ce4d2cf-ddd2-4b0b-9ce4-799056779dc1")
    public static native byte allocateDIO(ByteBuffer digital_port_pointer, byte input, IntBuffer status);

    @objid ("87e97185-f33a-4186-b1c8-c61f4db3917d")
    public static native void freeDIO(ByteBuffer digital_port_pointer, IntBuffer status);

    @objid ("eb026d8b-f8da-4501-ba89-656c76213943")
    public static native void setDIO(ByteBuffer digital_port_pointer, short value, IntBuffer status);

    @objid ("c5a5fa4c-ad84-4baa-aff9-9fd433df73b8")
    public static native byte getDIO(ByteBuffer digital_port_pointer, IntBuffer status);

    @objid ("28cae848-80f1-4737-8336-2397d235e2fc")
    public static native byte getDIODirection(ByteBuffer digital_port_pointer, IntBuffer status);

    @objid ("c615628a-cfee-4ea5-ab64-9fdd6e82c228")
    public static native void pulse(ByteBuffer digital_port_pointer, double pulseLength, IntBuffer status);

    @objid ("557ea3af-8aea-404a-b9a5-4cf9895ef244")
    public static native byte isPulsing(ByteBuffer digital_port_pointer, IntBuffer status);

    @objid ("a0c16766-f56e-47fd-a5f3-0a1672535385")
    public static native byte isAnyPulsing(IntBuffer status);

    @objid ("e1606371-78df-45e7-9f1e-63f2c4f3e3d0")
    public static native short getLoopTiming(IntBuffer status);

}
