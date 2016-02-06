package edu.wpi.first.wpilibj.hal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("f0cda342-aa2a-4fd0-87d1-05c6cb32070a")
public class SerialPortJNI extends JNIWrapper {
    @objid ("4adee988-96b8-495e-a367-9644b0fe4556")
    public static native void serialInitializePort(byte port, IntBuffer status);

    @objid ("d9c24356-91da-4f88-bb92-f6d523e008be")
    public static native void serialSetBaudRate(byte port, int baud, IntBuffer status);

    @objid ("03ba52c4-611d-4aaa-8b03-8ac28d7800b3")
    public static native void serialSetDataBits(byte port, byte bits, IntBuffer status);

    @objid ("7d2dd7db-0f59-4f5a-b487-8e7c1cf1c127")
    public static native void serialSetParity(byte port, byte parity, IntBuffer status);

    @objid ("715e536e-111a-4411-b75f-2daa5b82bf35")
    public static native void serialSetStopBits(byte port, byte stopBits, IntBuffer status);

    @objid ("f7f96337-4914-4583-9dff-ff2fc0bf2849")
    public static native void serialSetWriteMode(byte port, byte mode, IntBuffer status);

    @objid ("a1cbe27e-4909-4e29-a04f-2e69cc1c4793")
    public static native void serialSetFlowControl(byte port, byte flow, IntBuffer status);

    @objid ("54591c1d-3166-4d1e-8dd7-ad1762723419")
    public static native void serialSetTimeout(byte port, float timeout, IntBuffer status);

    @objid ("8bde0cf7-2596-4cd2-a5f4-7d906d4cd652")
    public static native void serialEnableTermination(byte port, char terminator, IntBuffer status);

    @objid ("2b85cb06-c11d-45a7-be57-9b980eb47465")
    public static native void serialDisableTermination(byte port, IntBuffer status);

    @objid ("bc00dde7-a6e2-4a29-83d2-e621f57144eb")
    public static native void serialSetReadBufferSize(byte port, int size, IntBuffer status);

    @objid ("c956feaa-a5fd-4105-b47a-4242a53fa000")
    public static native void serialSetWriteBufferSize(byte port, int size, IntBuffer status);

    @objid ("de27d693-b035-4068-a4ff-5af6418857e8")
    public static native int serialGetBytesRecieved(byte port, IntBuffer status);

    @objid ("2d632a19-dbf4-4cd2-b106-3719272b169e")
    public static native int serialRead(byte port, ByteBuffer buffer, int count, IntBuffer status);

    @objid ("c1b99235-667c-46c5-947e-adfa702d860e")
    public static native int serialWrite(byte port, ByteBuffer buffer, int count, IntBuffer status);

    @objid ("8cdb1f77-607a-43a1-b56f-278815a02654")
    public static native void serialFlush(byte port, IntBuffer status);

    @objid ("cdcfb1f5-1821-434d-8c2f-8a4b1c50a513")
    public static native void serialClear(byte port, IntBuffer status);

    @objid ("8923f96b-92e3-4509-9117-e3155c20d08a")
    public static native void serialClose(byte port, IntBuffer status);

}
