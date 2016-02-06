package edu.wpi.first.wpilibj.hal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("373f097a-58db-4b30-9206-f50792c840a4")
public class RelayJNI extends DIOJNI {
    @objid ("2a71a80c-4132-4c21-b9ec-47a9bc1fcad2")
    public static native void setRelayForward(ByteBuffer digital_port_pointer, byte on, IntBuffer status);

    @objid ("e5165e32-ffcd-4d83-8d30-35439320853d")
    public static native void setRelayReverse(ByteBuffer digital_port_pointer, byte on, IntBuffer status);

    @objid ("9a749a0e-4172-4bab-8651-181a72abd959")
    public static native byte getRelayForward(ByteBuffer digital_port_pointer, IntBuffer status);

    @objid ("6027425b-10d2-43a6-8c54-1702321820ba")
    public static native byte getRelayReverse(ByteBuffer digital_port_pointer, IntBuffer status);

}
