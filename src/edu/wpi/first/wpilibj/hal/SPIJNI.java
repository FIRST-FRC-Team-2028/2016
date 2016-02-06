package edu.wpi.first.wpilibj.hal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("31504312-85c8-4c28-8a43-9b3d946fc5cc")
public class SPIJNI extends JNIWrapper {
    @objid ("a6b94699-49c8-478c-a5d9-f614da8d446f")
    public static native void spiInitialize(byte port, IntBuffer status);

    @objid ("de9c5394-67c9-4e4f-a524-1d7a7d2be4f7")
    public static native int spiTransaction(byte port, ByteBuffer dataToSend, ByteBuffer dataReceived, byte size);

    @objid ("2100a220-1bc9-4875-81ff-6c872a49872b")
    public static native int spiWrite(byte port, ByteBuffer dataToSend, byte sendSize);

    @objid ("5d53fc8e-8d83-46ef-808f-9231bf774a82")
    public static native int spiRead(byte port, ByteBuffer dataReceived, byte size);

    @objid ("29ba2e07-bd9b-4d38-a2cb-f686cd88f2a5")
    public static native void spiClose(byte port);

    @objid ("cfec0c0a-3a48-444f-9606-9fb3a5a5b071")
    public static native void spiSetSpeed(byte port, int speed);

    @objid ("d94de1b5-a121-46c5-87f5-43f3135f064a")
    public static native void spiSetBitsPerWord(byte port, byte bpw);

    @objid ("e0db48e7-c11c-4ba5-b2cd-e514658c2a6a")
    public static native void spiSetOpts(byte port, int msb_first, int sample_on_trailing, int clk_idle_high);

    @objid ("cc5ba9d8-6bb4-465f-9de0-818ba59cd4f9")
    public static native void spiSetChipSelectActiveHigh(byte port, IntBuffer status);

    @objid ("b87986af-cc08-4a30-b4be-80975957c820")
    public static native void spiSetChipSelectActiveLow(byte port, IntBuffer status);

}
