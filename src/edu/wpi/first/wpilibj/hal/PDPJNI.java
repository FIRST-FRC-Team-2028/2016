package edu.wpi.first.wpilibj.hal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("1fdc289f-5da7-46dc-892e-507b3e3bd0d5")
public class PDPJNI extends JNIWrapper {
    @objid ("b8682495-2828-4e87-98a1-474a1cb399c3")
    public static native double getPDPTemperature(IntBuffer status);

    @objid ("d4392d5e-57d9-43a5-bbde-965bd006dfa3")
    public static native double getPDPVoltage(IntBuffer status);

    @objid ("1bf5264a-9990-48fa-b958-31d00fc14444")
    public static native double getPDPChannelCurrent(byte channel, IntBuffer status);

    @objid ("00a4bc8f-3465-4f4a-9a9a-96f906c99e7e")
    public static native double getPDPTotalCurrent(IntBuffer status);

    @objid ("e8c93f30-860d-4255-945e-b98fe3ed7d02")
    public static native double getPDPTotalPower(IntBuffer status);

    @objid ("913258b2-6c3a-4054-8418-3ef76874a2b4")
    public static native double getPDPTotalEnergy(IntBuffer status);

    @objid ("ca6251bd-518c-423c-9f4d-69da7deeaebc")
    public static native void resetPDPTotalEnergy(IntBuffer status);

    @objid ("2bf276e0-03f4-4821-b24b-5d26162403e9")
    public static native void clearPDPStickyFaults(IntBuffer status);

}
