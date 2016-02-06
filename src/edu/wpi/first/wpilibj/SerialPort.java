/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.hal.HALLibrary;
import edu.wpi.first.wpilibj.hal.HALUtil;
import edu.wpi.first.wpilibj.hal.SerialPortJNI;

/**
 * Driver for the RS-232 serial port on the RoboRIO.
 * 
 * The current implementation uses the VISA formatted I/O mode.  This means that
 * all traffic goes through the formatted buffers.  This allows the intermingled
 * use of print(), readString(), and the raw buffer accessors read() and write().
 * 
 * More information can be found in the NI-VISA User Manual here:
 * http://www.ni.com/pdf/manuals/370423a.pdf
 * and the NI-VISA Programmer's Reference Manual here:
 * http://www.ni.com/pdf/manuals/370132c.pdf
 */
@objid ("03d07dc4-8558-40af-b93a-9920edf5b027")
public class SerialPort {
    @objid ("c3901bcd-284d-4190-a392-834d696bdce7")
    private byte m_port;

    /**
     * Create an instance of a Serial Port class.
     * @param baudRate The baud rate to configure the serial port.
     * @param port The Serial port to use
     * @param dataBits The number of data bits per transfer.  Valid values are between 5 and 8 bits.
     * @param parity Select the type of parity checking to use.
     * @param stopBits The number of stop bits to use as defined by the enum StopBits.
     */
    @objid ("6a1efc68-8f72-4be8-b1b3-b9a7489a0ce7")
    public SerialPort(final int baudRate, Port port, final int dataBits, Parity parity, StopBits stopBits) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        m_port = (byte) port.getValue();
        
        SerialPortJNI.serialInitializePort(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        SerialPortJNI.serialSetBaudRate(m_port, baudRate, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        SerialPortJNI.serialSetDataBits(m_port, (byte) dataBits, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        SerialPortJNI.serialSetParity(m_port, (byte) parity.value, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        SerialPortJNI.serialSetStopBits(m_port, (byte) stopBits.value, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        
        // Set the default read buffer size to 1 to return bytes immediately
        setReadBufferSize(1);
        
        // Set the default timeout to 5 seconds.
        setTimeout(5.0f);
        
        // Don't wait until the buffer is full to transmit.
        setWriteBufferMode(WriteBufferMode.kFlushOnAccess);
        
        disableTermination();
        
        UsageReporting.report(tResourceType.kResourceType_SerialPort,0);
    }

    /**
     * Create an instance of a Serial Port class. Defaults to one stop bit.
     * @param baudRate The baud rate to configure the serial port.
     * @param dataBits The number of data bits per transfer.  Valid values are between 5 and 8 bits.
     * @param parity Select the type of parity checking to use.
     */
    @objid ("c9251401-9953-4ce0-81b7-94fba620cc6f")
    public SerialPort(final int baudRate, Port port, final int dataBits, Parity parity) {
        this(baudRate, port, dataBits, parity, StopBits.kOne);
    }

    /**
     * Create an instance of a Serial Port class. Defaults to no parity and one
     * stop bit.
     * @param baudRate The baud rate to configure the serial port.
     * @param dataBits The number of data bits per transfer.  Valid values are between 5 and 8 bits.
     */
    @objid ("76cf1aba-94b8-4e5e-93b3-5944950783f0")
    public SerialPort(final int baudRate, Port port, final int dataBits) {
        this(baudRate, port, dataBits, Parity.kNone, StopBits.kOne);
    }

    /**
     * Create an instance of a Serial Port class. Defaults to 8 databits,
     * no parity, and one stop bit.
     * @param baudRate The baud rate to configure the serial port.
     */
    @objid ("c8d90a6f-6e3e-4bb7-a25b-da83a923dd59")
    public SerialPort(final int baudRate, Port port) {
        this(baudRate, port, 8, Parity.kNone, StopBits.kOne);
    }

    /**
     * Destructor.
     */
    @objid ("e0505c42-e268-4ac2-996a-f87bb2b6945b")
    public void free() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        SerialPortJNI.serialClose(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Set the type of flow control to enable on this port.
     * 
     * By default, flow control is disabled.
     * @param flowControl the FlowControl value to use
     */
    @objid ("5a474885-8c14-4fa3-8f96-6676dbe639a1")
    public void setFlowControl(FlowControl flowControl) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        SerialPortJNI.serialSetFlowControl(m_port, (byte) flowControl.value, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Enable termination and specify the termination character.
     * 
     * Termination is currently only implemented for receive.
     * When the the terminator is received, the read() or readString() will return
     * fewer bytes than requested, stopping after the terminator.
     * @param terminator The character to use for termination.
     */
    @objid ("8e72fafd-88a4-4407-b259-92e022640e06")
    public void enableTermination(char terminator) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        SerialPortJNI.serialEnableTermination(m_port, terminator, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Enable termination with the default terminator '\n'
     * 
     * Termination is currently only implemented for receive.
     * When the the terminator is received, the read() or readString() will return
     * fewer bytes than requested, stopping after the terminator.
     * 
     * The default terminator is '\n'
     */
    @objid ("8aca1a3b-0c08-450d-b7dc-333cedc42b73")
    public void enableTermination() {
        this.enableTermination('\n');
    }

    /**
     * Disable termination behavior.
     */
    @objid ("052138eb-81bf-4e44-bb87-2e27d50e9009")
    public void disableTermination() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        SerialPortJNI.serialDisableTermination(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Get the number of bytes currently available to read from the serial port.
     * @return The number of bytes available to read.
     */
    @objid ("4855ac6e-3546-48de-a01e-abdc42182591")
    public int getBytesReceived() {
        int retVal = 0;
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        retVal = SerialPortJNI.serialGetBytesRecieved(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retVal;
    }

    /**
     * Read a string out of the buffer. Reads the entire contents of the buffer
     * @return The read string
     */
    @objid ("f32b4fc6-088b-4150-a1e0-7f001367da43")
    public String readString() {
        return readString(getBytesReceived());
    }

    /**
     * Read a string out of the buffer. Reads the entire contents of the buffer
     * @param count the number of characters to read into the string
     * @return The read string
     */
    @objid ("5deb15e1-14c2-4960-a541-512efaeefe44")
    public String readString(int count) {
        byte[] out = read(count);
        try {
            return new String(out, 0, count, "US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return new String();
        }
    }

    /**
     * Read raw bytes out of the buffer.
     * @param count The maximum number of bytes to read.
     * @return An array of the read bytes
     */
    @objid ("38afe9f0-7b13-450b-b01b-30958aa1ee60")
    public byte[] read(final int count) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer dataReceivedBuffer = ByteBuffer.allocateDirect(count);
        int gotten = SerialPortJNI.serialRead(m_port, dataReceivedBuffer, count, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        byte[] retVal = new byte[gotten];
        dataReceivedBuffer.get(retVal);
        return retVal;
    }

    /**
     * Write raw bytes to the serial port.
     * @param buffer The buffer of bytes to write.
     * @param count The maximum number of bytes to write.
     * @return The number of bytes actually written into the port.
     */
    @objid ("950ff2c0-7c24-4a8f-b551-8b84892d446b")
    public int write(byte[] buffer, int count) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer dataToSendBuffer = ByteBuffer.allocateDirect(count);
        dataToSendBuffer.put(buffer, 0, count);
        int retVal = SerialPortJNI.serialWrite(m_port, dataToSendBuffer, count, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return retVal;
    }

    /**
     * Write a string to the serial port
     * @param data The string to write to the serial port.
     * @return The number of bytes actually written into the port.
     */
    @objid ("5192658a-8265-40d5-ae4e-34e8491ea2d3")
    public int writeString(String data) {
        return write(data.getBytes(), data.length());
    }

    /**
     * Configure the timeout of the serial port.
     * 
     * This defines the timeout for transactions with the hardware.
     * It will affect reads if less bytes are available than the
     * read buffer size (defaults to 1) and very large writes.
     * @param timeout The number of seconds to to wait for I/O.
     */
    @objid ("debb4b6d-e4f9-4d52-b985-ea90ef16948b")
    public void setTimeout(double timeout) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        SerialPortJNI.serialSetTimeout(m_port, (float)timeout, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Specify the size of the input buffer.
     * 
     * Specify the amount of data that can be stored before data
     * from the device is returned to Read.  If you want
     * data that is received to be returned immediately, set this to 1.
     * 
     * It the buffer is not filled before the read timeout expires, all
     * data that has been received so far will be returned.
     * @param size The read buffer size.
     */
    @objid ("293ef122-1f71-4bcc-b9fa-b0c92b4e46ae")
    public void setReadBufferSize(int size) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        SerialPortJNI.serialSetReadBufferSize(m_port, size, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Specify the size of the output buffer.
     * 
     * Specify the amount of data that can be stored before being
     * transmitted to the device.
     * @param size The write buffer size.
     */
    @objid ("7717629a-cb1b-4fc0-a385-eb009d712b5a")
    public void setWriteBufferSize(int size) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        SerialPortJNI.serialSetWriteBufferSize(m_port, size, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Specify the flushing behavior of the output buffer.
     * 
     * When set to kFlushOnAccess, data is synchronously written to the serial port
     * after each call to either print() or write().
     * 
     * When set to kFlushWhenFull, data will only be written to the serial port when
     * the buffer is full or when flush() is called.
     * @param mode The write buffer mode.
     */
    @objid ("c4ae3c93-fb4f-42b2-8d73-ac1977c3cd3e")
    public void setWriteBufferMode(WriteBufferMode mode) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        SerialPortJNI.serialSetWriteMode(m_port, (byte)mode.value, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Force the output buffer to be written to the port.
     * 
     * This is used when setWriteBufferMode() is set to kFlushWhenFull to force a
     * flush before the buffer is full.
     */
    @objid ("2f95ff51-0888-45fb-b051-669d9a6d4cca")
    public void flush() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        SerialPortJNI.serialFlush(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Reset the serial port driver to a known state.
     * 
     * Empty the transmit and receive buffers in the device and formatted I/O.
     */
    @objid ("e84fc4d1-3821-45da-a6cd-0082e622898b")
    public void reset() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        SerialPortJNI.serialClear(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Represents the parity to use for serial communications
     */
    @objid ("1b81b458-74e2-4e24-a0f2-03167cb1bb60")
    public static class Parity {
        /**
         * The integer value representing this enumeration
         */
        @objid ("b2e1dc06-e9ca-404c-93f4-c5d96e4653a7")
        public final int value;

        @objid ("584f9fcc-ac2f-4e60-941f-026ff5f657b2")
         static final int kNone_val = 0;

        @objid ("c687a4ad-ab96-4ba9-9cce-ca9f4de44b39")
         static final int kOdd_val = 1;

        @objid ("7630263c-0475-4784-b226-b62de1678509")
         static final int kEven_val = 2;

        @objid ("6b037db5-3f8e-4c9e-9747-3552b4ba49a3")
         static final int kMark_val = 3;

        @objid ("2465f16d-ccc3-4348-8467-7fb288c1215e")
         static final int kSpace_val = 4;

        /**
         * parity: Use no parity
         */
        @objid ("a93711ca-9c3c-483a-8f17-7deb3a14da41")
        public static final Parity kNone = new Parity(kNone_val);

        /**
         * parity: Use odd parity
         */
        @objid ("16ea0919-251a-4d79-892e-c8e85addf032")
        public static final Parity kOdd = new Parity(kOdd_val);

        /**
         * parity: Use even parity
         */
        @objid ("1fd4d5b3-6a2c-4dc9-a630-9ea05aeb87e2")
        public static final Parity kEven = new Parity(kEven_val);

        /**
         * parity: Use mark parity
         */
        @objid ("09f3ba68-4456-4823-bae3-f54048690f80")
        public static final Parity kMark = new Parity(kMark_val);

        /**
         * parity: Use space parity
         */
        @objid ("86b6ee49-0fb4-4d52-ad02-42d6968c717e")
        public static final Parity kSpace = new Parity((kSpace_val));

        @objid ("a8b5cd98-c1fd-4664-b95e-124f299c0be6")
        private Parity(int value) {
            this.value = value;
        }

    }

    /**
     * Represents the number of stop bits to use for Serial Communication
     */
    @objid ("6f3cdf4a-ad92-450b-9805-0e4656ec70c2")
    public static class StopBits {
        /**
         * The integer value representing this enumeration
         */
        @objid ("21549621-fbfd-4d87-8ca1-0b0e39f0f5f8")
        public final int value;

        @objid ("ec5ab600-dce5-4822-990f-2dcd102396dc")
         static final int kOne_val = 10;

        @objid ("9a69a649-0ab5-4434-add0-bf8f689f8a32")
         static final int kOnePointFive_val = 15;

        @objid ("1e594dc3-a3d3-4c6c-86f0-24876aa3a861")
         static final int kTwo_val = 20;

        /**
         * stopBits: use 1
         */
        @objid ("e4381f86-9bf6-451e-8c4b-2c1a610551b7")
        public static final StopBits kOne = new StopBits(kOne_val);

        /**
         * stopBits: use 1.5
         */
        @objid ("41d7f4b7-c63a-4a98-a3ff-6dd382a72c7a")
        public static final StopBits kOnePointFive = new StopBits(kOnePointFive_val);

        /**
         * stopBits: use 2
         */
        @objid ("c351c726-856b-48ea-9aed-4306a0bd46df")
        public static final StopBits kTwo = new StopBits(kTwo_val);

        @objid ("a62c7079-1e89-4b0f-8141-4fbcba5df438")
        private StopBits(int value) {
            this.value = value;
        }

    }

    /**
     * Represents what type of flow control to use for serial communication
     */
    @objid ("b2b3ee18-5a62-42ef-bd0d-0cd5e7b5845f")
    public static class FlowControl {
        /**
         * The integer value representing this enumeration
         */
        @objid ("3fc5eecb-f458-4cd2-80e6-0c6e2e2ef1c6")
        public final int value;

        @objid ("a87683bd-ba65-4a36-983b-d019f8d1284d")
         static final int kNone_val = 0;

        @objid ("11708ce9-2d14-47f6-84fa-1d3250c38860")
         static final int kXonXoff_val = 1;

        @objid ("cfcf00ad-400b-43a2-9e42-234927e2dd52")
         static final int kRtsCts_val = 2;

        @objid ("9347e381-f375-4542-80a6-dcc3de0f0ba8")
         static final int kDtrDsr_val = 4;

        /**
         * flowControl: use none
         */
        @objid ("196819a6-f376-4b38-b475-0c415c0d49f1")
        public static final FlowControl kNone = new FlowControl(kNone_val);

        /**
         * flowcontrol: use on/off
         */
        @objid ("03477e3d-a852-4af4-b400-870a14d3f755")
        public static final FlowControl kXonXoff = new FlowControl(kXonXoff_val);

        /**
         * flowcontrol: use rts cts
         */
        @objid ("7c794395-c0de-4402-af5d-33fe49372fd3")
        public static final FlowControl kRtsCts = new FlowControl(kRtsCts_val);

        /**
         * flowcontrol: use dts dsr
         */
        @objid ("daad2bce-077b-417f-85b2-325f05bdc561")
        public static final FlowControl kDtrDsr = new FlowControl(kDtrDsr_val);

        @objid ("620365a6-460d-43bd-91d8-e496273dd179")
        private FlowControl(int value) {
            this.value = value;
        }

    }

    /**
     * Represents which type of buffer mode to use when writing to a serial port
     */
    @objid ("8afc11bb-ff0f-4696-9636-0ae50a358e15")
    public static class WriteBufferMode {
        /**
         * The integer value representing this enumeration
         */
        @objid ("a18a280c-37bd-4542-b7ff-e5691f2ccc85")
        public final int value;

        @objid ("c4b0a661-d322-4b37-9970-280d3b648d3f")
         static final int kFlushOnAccess_val = 1;

        @objid ("0e40a6d0-f104-4d3f-b293-8d904354cc2f")
         static final int kFlushWhenFull_val = 2;

        /**
         * Flush on access
         */
        @objid ("6e7835bd-6b22-43e5-98f6-fe952fe3b30a")
        public static final WriteBufferMode kFlushOnAccess = new WriteBufferMode(kFlushOnAccess_val);

        /**
         * Flush when full
         */
        @objid ("5c7b0db8-fa1d-4eb7-8603-53742934e8e8")
        public static final WriteBufferMode kFlushWhenFull = new WriteBufferMode(kFlushWhenFull_val);

        @objid ("ff7e58d6-61c1-4312-9f18-4f6853fc73b6")
        private WriteBufferMode(int value) {
            this.value = value;
        }

    }

    @objid ("9fe45fd9-6465-4adf-a875-aa1248917407")
    public enum Port {
        kOnboard (0),
        kMXP (1),
        kUSB (2);

        @objid ("a01ce5bf-a1d1-4bba-8ddd-cebb44e251c2")
        private int value;

        @objid ("95a7a28a-1332-4330-96b1-705985481eca")
        private Port(int value) {
            this.value = value;
        }

        @objid ("45222a5d-3475-4735-8ac3-c203045d3565")
        public int getValue() {
            return this.value;
        }

    }

}
