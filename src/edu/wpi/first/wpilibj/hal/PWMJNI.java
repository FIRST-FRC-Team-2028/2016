package edu.wpi.first.wpilibj.hal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.SensorBase;

@objid ("4121c591-20e8-4ca1-aab8-0b50ba167b51")
public class PWMJNI extends DIOJNI {
    @objid ("4d6aa8d4-371f-4170-b2ff-c2601af010c6")
    public static native boolean allocatePWMChannel(ByteBuffer digital_port_pointer, IntBuffer status);

    @objid ("bbc0795b-c0b9-4c25-85bc-40dfe47c405d")
    public static native void freePWMChannel(ByteBuffer digital_port_pointer, IntBuffer status);

    @objid ("b6662767-afb7-4a89-95b7-b13f9bdd658b")
    public static native void setPWM(ByteBuffer digital_port_pointer, short value, IntBuffer status);

    @objid ("a09d7ebb-ba44-48d7-b54b-7572800ead64")
    public static native short getPWM(ByteBuffer digital_port_pointer, IntBuffer status);

    @objid ("e6945be1-91d0-4912-a6f7-ee74dc7b7e79")
    public static native void latchPWMZero(ByteBuffer digital_port_pointer, IntBuffer status);

    @objid ("241309f2-2a64-4acd-95c9-682b29707a74")
    public static native void setPWMPeriodScale(ByteBuffer digital_port_pointer, int squelchMask, IntBuffer status);

    @objid ("69d7e8d8-68bd-4b25-b451-9e84f8e8a8cc")
    public static native ByteBuffer allocatePWM(IntBuffer status);

    @objid ("39a187c0-63dd-4083-a428-55f2e285cd70")
    public static native void freePWM(ByteBuffer pwmGenerator, IntBuffer status);

    @objid ("48ec90bb-76d9-468c-966c-2d9c446a03dc")
    public static native void setPWMRate(double rate, IntBuffer status);

    @objid ("5108ea48-1c34-401f-994d-67bc704d159b")
    public static native void setPWMDutyCycle(ByteBuffer pwmGenerator, double dutyCycle, IntBuffer status);

    @objid ("3783cea7-fb85-403c-81a6-1904955d0fdf")
    public static native void setPWMOutputChannel(ByteBuffer pwmGenerator, int pin, IntBuffer status);

}
