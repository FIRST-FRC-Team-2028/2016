package edu.wpi.first.wpilibj.hal;

import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("e51ebc84-5621-4256-bc73-7a4437ba1605")
public class PowerJNI extends JNIWrapper {
    @objid ("c747cfe8-c117-4cf8-9e21-509310e7fffd")
    public static native float getVinVoltage(IntBuffer status);

    @objid ("b3bf621f-ca1c-43f1-84a7-e38b8b3ef727")
    public static native float getVinCurrent(IntBuffer status);

    @objid ("a0ba8e0d-cde7-4593-aab5-9f9d1ba84d85")
    public static native float getUserVoltage6V(IntBuffer status);

    @objid ("da712822-f9c4-4beb-8152-90b4d9e9526d")
    public static native float getUserCurrent6V(IntBuffer status);

    @objid ("e13b4597-3a22-45c6-a7c1-2243694fa511")
    public static native boolean getUserActive6V(IntBuffer status);

    @objid ("8d3dca17-1367-491c-b83d-a9194dcee482")
    public static native int getUserCurrentFaults6V(IntBuffer status);

    @objid ("265f977e-d547-4fc5-bd9d-1784c3c352b4")
    public static native float getUserVoltage5V(IntBuffer status);

    @objid ("b8bea6bb-1c0e-4186-9c79-9870fd4d51fc")
    public static native float getUserCurrent5V(IntBuffer status);

    @objid ("1a698449-ea1b-4f1b-8fd3-1afaa4a79e85")
    public static native boolean getUserActive5V(IntBuffer status);

    @objid ("58738136-f56f-4fd8-9b0b-36b67a91ae32")
    public static native int getUserCurrentFaults5V(IntBuffer status);

    @objid ("cfeb9854-0a37-411c-9823-8c113477503d")
    public static native float getUserVoltage3V3(IntBuffer status);

    @objid ("75cd322a-5488-4a1c-806f-ed4cdc796e3c")
    public static native float getUserCurrent3V3(IntBuffer status);

    @objid ("0ff43751-3297-45d1-9224-7e1f030951a0")
    public static native boolean getUserActive3V3(IntBuffer status);

    @objid ("c5eede3b-fc56-4360-b536-df20dcbf1a1b")
    public static native int getUserCurrentFaults3V3(IntBuffer status);

}
