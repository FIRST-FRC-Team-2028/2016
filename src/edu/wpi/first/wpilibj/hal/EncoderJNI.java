package edu.wpi.first.wpilibj.hal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("03bc3181-95f4-4eb7-b073-5b963ce737dd")
public class EncoderJNI extends JNIWrapper {
    @objid ("cfbd3fbc-fee7-4910-ae2e-db46f233e2a5")
    public static native ByteBuffer initializeEncoder(byte port_a_module, int port_a_pin, byte port_a_analog_trigger, byte port_b_module, int port_b_pin, byte port_b_analog_trigger, byte reverseDirection, IntBuffer index, IntBuffer status);

    @objid ("f8b2dbea-4938-45fc-ac07-9c6ae4ebb3d7")
    public static native void freeEncoder(ByteBuffer encoder_pointer, IntBuffer status);

    @objid ("e2ee5870-3da1-4705-bf83-1e67885895ec")
    public static native void resetEncoder(ByteBuffer encoder_pointer, IntBuffer status);

    @objid ("baee4d76-27ac-43d3-a250-16bd9bcc6f53")
    public static native int getEncoder(ByteBuffer encoder_pointer, IntBuffer status);

    @objid ("c5f4aa77-2109-4696-9767-349c942f6bd1")
    public static native double getEncoderPeriod(ByteBuffer encoder_pointer, IntBuffer status);

    @objid ("8f8bcff2-88a8-468a-b485-92dc37a4e6bb")
    public static native void setEncoderMaxPeriod(ByteBuffer encoder_pointer, double maxPeriod, IntBuffer status);

    @objid ("4f2e3805-6a92-409f-b3f7-e5cbfecac4fa")
    public static native byte getEncoderStopped(ByteBuffer encoder_pointer, IntBuffer status);

    @objid ("a437b70d-de6e-443f-ae2f-a95fdc329c77")
    public static native byte getEncoderDirection(ByteBuffer encoder_pointer, IntBuffer status);

    @objid ("0634dd16-67e1-4f83-b232-d8a9c4c491cd")
    public static native void setEncoderReverseDirection(ByteBuffer encoder_pointer, byte reverseDirection, IntBuffer status);

    @objid ("01672a38-6d53-4a59-babb-a88d6e8e4e68")
    public static native void setEncoderSamplesToAverage(ByteBuffer encoder_pointer, int samplesToAverage, IntBuffer status);

    @objid ("ab8695d1-d366-4f86-a794-de8ab179165b")
    public static native int getEncoderSamplesToAverage(ByteBuffer encoder_pointer, IntBuffer status);

    @objid ("913cd78e-eb20-472b-afb6-541baa5cbd11")
    public static native void setEncoderIndexSource(ByteBuffer digital_port, int pin, boolean analogTrigger, boolean activeHigh, boolean edgeSensitive, IntBuffer status);

}
