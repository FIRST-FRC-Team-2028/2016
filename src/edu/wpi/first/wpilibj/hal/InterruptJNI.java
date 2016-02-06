package edu.wpi.first.wpilibj.hal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("e37f30e9-28a9-423e-9f31-9b668503983a")
public class InterruptJNI extends JNIWrapper {
    @objid ("b0cfb3c8-3db4-4a5e-8798-65aed4c34d58")
    public static native void initializeInterruptJVM(IntBuffer status);

    @objid ("6af72acc-e490-4e03-bc4c-d9ae1add33ed")
    public static native ByteBuffer initializeInterrupts(int interruptIndex, byte watcher, IntBuffer status);

    @objid ("9de309d1-31ab-4fe5-9984-4bbeb5db631c")
    public static native void cleanInterrupts(ByteBuffer interrupt_pointer, IntBuffer status);

    @objid ("84adc154-90e9-499f-824f-dc04db9e1b8a")
    public static native int waitForInterrupt(ByteBuffer interrupt_pointer, double timeout, boolean ignorePrevious, IntBuffer status);

    @objid ("cff24a69-b4c9-4886-9267-bcb552ccceec")
    public static native void enableInterrupts(ByteBuffer interrupt_pointer, IntBuffer status);

    @objid ("45ab9dbe-325b-4bec-a85d-7167be28532d")
    public static native void disableInterrupts(ByteBuffer interrupt_pointer, IntBuffer status);

    @objid ("52c55185-6f7a-43ce-963b-366f380ed3d5")
    public static native double readRisingTimestamp(ByteBuffer interrupt_pointer, IntBuffer status);

    @objid ("6c7467fa-1cf8-4c42-ae0f-1e10062ede0b")
    public static native double readFallingTimestamp(ByteBuffer interrupt_pointer, IntBuffer status);

    @objid ("8f3f2117-38f0-487b-91ec-718c7d67e7e3")
    public static native void requestInterrupts(ByteBuffer interrupt_pointer, byte routing_module, int routing_pin, byte routing_analog_trigger, IntBuffer status);

    @objid ("a06234dd-cdc6-436c-8747-5f1cf6b780ed")
    public static native void attachInterruptHandler(ByteBuffer interrupt_pointer, InterruptJNIHandlerFunction handler, Object param, IntBuffer status);

    @objid ("fdb40ffe-b60a-4d23-b145-c38dba02b72a")
    public static native void setInterruptUpSourceEdge(ByteBuffer interrupt_pointer, byte risingEdge, byte fallingEdge, IntBuffer status);

    @objid ("b338267b-e9cd-4a6a-9cfd-debc8a1187f1")
    public interface InterruptJNIHandlerFunction {
        @objid ("1f3975d6-aa73-43d0-a33e-abc9cb49b95d")
        void apply(int interruptAssertedMask, Object param);

    }

}
