package edu.wpi.first.wpilibj.hal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("10c8b4b0-b60f-4df5-90fe-4dd1a752813b")
public class CounterJNI extends JNIWrapper {
    @objid ("858378a7-ddc1-4d18-a638-1761781efeb8")
    public static native ByteBuffer initializeCounter(int mode, IntBuffer index, IntBuffer status);

    @objid ("9021af69-5a0e-4f85-946c-e48de0cbaf23")
    public static native void freeCounter(ByteBuffer counter_pointer, IntBuffer status);

    @objid ("092209d3-0d99-447b-83c5-2f2cdd0c845a")
    public static native void setCounterAverageSize(ByteBuffer counter_pointer, int size, IntBuffer status);

    @objid ("68599e90-b141-4303-a8f3-3cbcfd45ad33")
    public static native void setCounterUpSource(ByteBuffer counter_pointer, int pin, byte analogTrigger, IntBuffer status);

    @objid ("817ac209-f15d-490e-85be-f669acc20f27")
    public static native void setCounterUpSourceEdge(ByteBuffer counter_pointer, byte risingEdge, byte fallingEdge, IntBuffer status);

    @objid ("a041e147-c15b-4878-a6e4-7afc3b8258d6")
    public static native void clearCounterUpSource(ByteBuffer counter_pointer, IntBuffer status);

    @objid ("03268ef4-6a4c-44a3-beeb-3d6b5d4c2ac1")
    public static native void setCounterDownSource(ByteBuffer counter_pointer, int pin, byte analogTrigger, IntBuffer status);

    @objid ("22e3626a-731e-49f7-942b-fde75e0b2cf3")
    public static native void setCounterDownSourceEdge(ByteBuffer counter_pointer, byte risingEdge, byte fallingEdge, IntBuffer status);

    @objid ("21dd770e-e39f-4180-a4de-31794e696e40")
    public static native void clearCounterDownSource(ByteBuffer counter_pointer, IntBuffer status);

    @objid ("938491f3-e052-4c1c-b35a-94a84f552d0e")
    public static native void setCounterUpDownMode(ByteBuffer counter_pointer, IntBuffer status);

    @objid ("6b935f60-a59f-4162-9607-3539d366cb12")
    public static native void setCounterExternalDirectionMode(ByteBuffer counter_pointer, IntBuffer status);

    @objid ("545ca4e5-beb7-4297-a09d-e4cc316f6bcb")
    public static native void setCounterSemiPeriodMode(ByteBuffer counter_pointer, byte highSemiPeriod, IntBuffer status);

    @objid ("4dc22dd3-afcc-4d7f-a91c-71b3ec9e1978")
    public static native void setCounterPulseLengthMode(ByteBuffer counter_pointer, double threshold, IntBuffer status);

    @objid ("081e32a9-8363-4e51-adf8-31f8303f30a5")
    public static native int getCounterSamplesToAverage(ByteBuffer counter_pointer, IntBuffer status);

    @objid ("01c49cbb-d793-49a4-aa25-175e015ef7c7")
    public static native void setCounterSamplesToAverage(ByteBuffer counter_pointer, int samplesToAverage, IntBuffer status);

    @objid ("914b19b5-2380-4ef3-9fbd-69d7ea5458cf")
    public static native void resetCounter(ByteBuffer counter_pointer, IntBuffer status);

    @objid ("7316355b-1987-47f6-8ab7-f6cfed349d14")
    public static native int getCounter(ByteBuffer counter_pointer, IntBuffer status);

    @objid ("7909eaea-5e13-4709-9564-f9e8aa9761db")
    public static native double getCounterPeriod(ByteBuffer counter_pointer, IntBuffer status);

    @objid ("2fa0d375-1aba-4bae-851e-8dc75c49e0fe")
    public static native void setCounterMaxPeriod(ByteBuffer counter_pointer, double maxPeriod, IntBuffer status);

    @objid ("ed39f893-8662-42ef-a193-30494896002d")
    public static native void setCounterUpdateWhenEmpty(ByteBuffer counter_pointer, byte enabled, IntBuffer status);

    @objid ("d6921f95-7664-4d33-8493-3fe0bb0eb6f7")
    public static native byte getCounterStopped(ByteBuffer counter_pointer, IntBuffer status);

    @objid ("cffa17a6-b545-47b9-a64e-767ec5dc74c4")
    public static native byte getCounterDirection(ByteBuffer counter_pointer, IntBuffer status);

    @objid ("4b278817-1403-4a2d-949b-87142f94f011")
    public static native void setCounterReverseDirection(ByteBuffer counter_pointer, byte reverseDirection, IntBuffer status);

}
