package edu.wpi.first.wpilibj.hal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("dec06f1d-bca3-400e-922e-593eadd2c3f4")
public class I2CJNI extends JNIWrapper {
    @objid ("ba87f318-f54d-4a68-a2cb-ccfe233ecc2a")
    public static native void i2CInitialize(byte port, IntBuffer status);

    @objid ("03a280ef-3d73-47bd-8b8c-2df64a6739c2")
    public static native byte i2CTransaction(byte port, byte address, ByteBuffer dataToSend, byte sendSize, ByteBuffer dataReceived, byte receiveSize);

    @objid ("31289302-63d7-4bbb-a846-0d4fd86f6507")
    public static native byte i2CWrite(byte port, byte address, ByteBuffer dataToSend, byte sendSize);

    @objid ("f956591b-e3a5-483d-8de2-996cfd9ac296")
    public static native byte i2CRead(byte port, byte address, ByteBuffer dataRecieved, byte receiveSize);

    @objid ("7248d7e4-7277-49b8-88ff-b0e057da3eec")
    public static native void i2CClose(byte port);

}
