package edu.wpi.first.wpilibj.hal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("47827c74-a7a6-4f44-9c20-d9c7603393c3")
public class AnalogJNI extends JNIWrapper {
    @objid ("c7bc0526-1847-46f0-a06f-64e65b42d248")
    public static native ByteBuffer initializeAnalogInputPort(ByteBuffer port_pointer, IntBuffer status);

    @objid ("d75af6f6-731d-47f3-a147-5a80f7af147f")
    public static native ByteBuffer initializeAnalogOutputPort(ByteBuffer port_pointer, IntBuffer status);

    @objid ("06ce7a4e-d680-4403-bf39-02b9b51dfd62")
    public static native byte checkAnalogModule(byte module);

    @objid ("40b7b648-3147-4c10-9636-fd37a31eedad")
    public static native byte checkAnalogInputChannel(int pin);

    @objid ("658ef0ef-5f17-45f3-9f52-464eaf68ef36")
    public static native byte checkAnalogOutputChannel(int pin);

    @objid ("fd7d1f7d-66f3-418d-8e53-d39c722cf1c7")
    public static native void setAnalogOutput(ByteBuffer port_pointer, double voltage, IntBuffer status);

    @objid ("10026946-669a-448c-992a-9cc9e4df7932")
    public static native double getAnalogOutput(ByteBuffer port_pointer, IntBuffer status);

    @objid ("35df8e57-406e-4632-b50c-0fe18ca778d9")
    public static native void setAnalogSampleRate(double samplesPerSecond, IntBuffer status);

    @objid ("2bf67992-741a-4678-b5dc-746d0a3d5e94")
    public static native double getAnalogSampleRate(IntBuffer status);

    @objid ("9941a501-db9b-44eb-87ca-5b02716e4729")
    public static native void setAnalogAverageBits(ByteBuffer analog_port_pointer, int bits, IntBuffer status);

    @objid ("4fc18726-c1cb-4a11-8c07-feabaa5df1dd")
    public static native int getAnalogAverageBits(ByteBuffer analog_port_pointer, IntBuffer status);

    @objid ("708d2039-4268-4424-a6bc-ac991396ebb7")
    public static native void setAnalogOversampleBits(ByteBuffer analog_port_pointer, int bits, IntBuffer status);

    @objid ("eedf7c09-339a-418f-bf35-9ecad6500208")
    public static native int getAnalogOversampleBits(ByteBuffer analog_port_pointer, IntBuffer status);

    @objid ("e759a129-a18f-4ea7-8cc4-e746bdbbebc3")
    public static native short getAnalogValue(ByteBuffer analog_port_pointer, IntBuffer status);

    @objid ("beb18849-d04a-4d00-a2e0-6c7c985bec55")
    public static native int getAnalogAverageValue(ByteBuffer analog_port_pointer, IntBuffer status);

    @objid ("a8753e5f-0fdd-452a-942f-aef3b677aef2")
    public static native int getAnalogVoltsToValue(ByteBuffer analog_port_pointer, double voltage, IntBuffer status);

    @objid ("ab66f6a6-152c-4829-a730-22c772772195")
    public static native double getAnalogVoltage(ByteBuffer analog_port_pointer, IntBuffer status);

    @objid ("a057f972-0189-4d14-873f-d1f1b01faded")
    public static native double getAnalogAverageVoltage(ByteBuffer analog_port_pointer, IntBuffer status);

    @objid ("2d228c22-2a51-4e19-9bec-d274a2c4d84d")
    public static native int getAnalogLSBWeight(ByteBuffer analog_port_pointer, IntBuffer status);

    @objid ("253b9863-082e-46d6-a040-dc5c2c4970dd")
    public static native int getAnalogOffset(ByteBuffer analog_port_pointer, IntBuffer status);

    @objid ("2991b518-0205-4179-882f-a8030591f70b")
    public static native byte isAccumulatorChannel(ByteBuffer analog_port_pointer, IntBuffer status);

    @objid ("3f9b79c4-6857-452c-93ba-ec3d02f628cb")
    public static native void initAccumulator(ByteBuffer analog_port_pointer, IntBuffer status);

    @objid ("e54e2618-0d95-442a-b93b-bbb49ba5de67")
    public static native void resetAccumulator(ByteBuffer analog_port_pointer, IntBuffer status);

    @objid ("bd70f6cd-6e8c-4404-8e74-a7dd46375cc4")
    public static native void setAccumulatorCenter(ByteBuffer analog_port_pointer, int center, IntBuffer status);

    @objid ("dcc279b5-771a-4f6b-8d2d-83515ae3e2be")
    public static native void setAccumulatorDeadband(ByteBuffer analog_port_pointer, int deadband, IntBuffer status);

    @objid ("afb37197-7eb9-460b-b32e-f72d3c679ba6")
    public static native long getAccumulatorValue(ByteBuffer analog_port_pointer, IntBuffer status);

    @objid ("c24bf817-d20d-4803-9250-d89a5df90bbb")
    public static native int getAccumulatorCount(ByteBuffer analog_port_pointer, IntBuffer status);

    @objid ("c84be332-5b1d-4ffe-a1a5-279f9bbda5bd")
    public static native void getAccumulatorOutput(ByteBuffer analog_port_pointer, LongBuffer value, IntBuffer count, IntBuffer status);

    @objid ("53bfa811-ed7f-4df9-8013-13e3be3413ff")
    public static native ByteBuffer initializeAnalogTrigger(ByteBuffer port_pointer, IntBuffer index, IntBuffer status);

    @objid ("0a824f23-afb2-4b31-ba39-cb27b8349705")
    public static native void cleanAnalogTrigger(ByteBuffer analog_trigger_pointer, IntBuffer status);

    @objid ("446afd25-98e4-449e-9e78-4400441f4244")
    public static native void setAnalogTriggerLimitsRaw(ByteBuffer analog_trigger_pointer, int lower, int upper, IntBuffer status);

    @objid ("f8ed0315-b1f1-4487-89db-fb06734f42ef")
    public static native void setAnalogTriggerLimitsVoltage(ByteBuffer analog_trigger_pointer, double lower, double upper, IntBuffer status);

    @objid ("f2d2ae78-5d02-45eb-bcd4-bcd4ee18246a")
    public static native void setAnalogTriggerAveraged(ByteBuffer analog_trigger_pointer, byte useAveragedValue, IntBuffer status);

    @objid ("d3d5e0ba-2b04-4cf8-89e1-f77102f22fbe")
    public static native void setAnalogTriggerFiltered(ByteBuffer analog_trigger_pointer, byte useFilteredValue, IntBuffer status);

    @objid ("2034d6ec-b08a-493f-9c0e-ca40d584c215")
    public static native byte getAnalogTriggerInWindow(ByteBuffer analog_trigger_pointer, IntBuffer status);

    @objid ("6b88654b-0ef0-4192-bf33-8a017911a40f")
    public static native byte getAnalogTriggerTriggerState(ByteBuffer analog_trigger_pointer, IntBuffer status);

    @objid ("561493e6-f901-490a-bab2-6827c0fb14d8")
    public static native byte getAnalogTriggerOutput(ByteBuffer analog_trigger_pointer, int type, IntBuffer status);

    /**
     * <i>native declaration : AthenaJava\target\native\include\HAL\Analog.h:58</i><br>
     * enum values
     */
    @objid ("76a7e654-90a5-4a55-87ec-a5b3cf4c7007")
    public interface AnalogTriggerType {
        /**
         * <i>native declaration : AthenaJava\target\native\include\HAL\Analog.h:54</i>
         */
        @objid ("ac20c493-1125-4c18-9212-1c587d90ea9c")
        public static final int kInWindow = 0;

        /**
         * <i>native declaration : AthenaJava\target\native\include\HAL\Analog.h:55</i>
         */
        @objid ("b43dee4a-8aa1-4433-9461-37ae4e97f7ca")
        public static final int kState = 1;

        /**
         * <i>native declaration : AthenaJava\target\native\include\HAL\Analog.h:56</i>
         */
        @objid ("6b8498c7-4a9c-4cfc-bf74-1a84fe871906")
        public static final int kRisingPulse = 2;

        /**
         * <i>native declaration : AthenaJava\target\native\include\HAL\Analog.h:57</i>
         */
        @objid ("37780d0a-f3a2-46da-b594-07948915db44")
        public static final int kFallingPulse = 3;

    }

}
