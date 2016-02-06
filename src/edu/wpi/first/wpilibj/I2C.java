/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.hal.HALLibrary;
import edu.wpi.first.wpilibj.hal.HALUtil;
import edu.wpi.first.wpilibj.hal.I2CJNI;
import edu.wpi.first.wpilibj.util.BoundaryException;

/**
 * I2C bus interface class.
 * 
 * This class is intended to be used by sensor (and other I2C device) drivers.
 * It probably should not be used directly.
 */
@objid ("e84af6df-9359-40e8-8132-eaaacc6fc9af")
public class I2C extends SensorBase {
    @objid ("3e7ad966-a81b-4711-b49a-c2bc420426c1")
    private Port m_port;

    @objid ("f8c1c1ec-ed41-474e-afb1-cac7c6cf8979")
    private int m_deviceAddress;

    /**
     * Constructor.
     * @param port The I2C port the device is connected to.
     * @param deviceAddress The address of the device on the I2C bus.
     */
    @objid ("20fa0f3d-1e8c-4b72-a453-4910b22b13bf")
    public I2C(Port port, int deviceAddress) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        m_port = port;
        m_deviceAddress = deviceAddress;
        
        I2CJNI.i2CInitialize((byte)m_port.getValue(), status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        
        UsageReporting.report(tResourceType.kResourceType_I2C, deviceAddress);
    }

    /**
     * Destructor.
     */
    @objid ("d401a67b-76c8-4c9a-88d9-f5291c89ebf3")
    public void free() {
    }

    /**
     * Generic transaction.
     * 
     * This is a lower-level interface to the I2C hardware giving you more
     * control over each transaction.
     * @param dataToSend Buffer of data to send as part of the transaction.
     * @param sendSize Number of bytes to send as part of the transaction. [0..6]
     * @param dataReceived Buffer to read data into.
     * @param receiveSize Number of bytes to read from the device. [0..7]
     * @return Transfer Aborted... false for success, true for aborted.
     */
    @objid ("dccbea0f-7961-4f2f-af3f-ec9bac070737")
    public synchronized boolean transaction(byte[] dataToSend, int sendSize, byte[] dataReceived, int receiveSize) {
        boolean aborted = true;
        
        ByteBuffer dataToSendBuffer = ByteBuffer.allocateDirect(sendSize);
        dataToSendBuffer.put(dataToSend);
        ByteBuffer dataReceivedBuffer = ByteBuffer.allocateDirect(receiveSize);
        
        aborted = I2CJNI
                .i2CTransaction((byte) m_port.getValue(), (byte) m_deviceAddress,
                    dataToSendBuffer, (byte) sendSize,
                    dataReceivedBuffer, (byte) receiveSize) != 0;
        /*if (status.get() == HALUtil.PARAMETER_OUT_OF_RANGE) {
            if (sendSize > 6) {
                throw new BoundaryException(BoundaryException.getMessage(
                        sendSize, 0, 6));
            } else if (receiveSize > 7) {
                throw new BoundaryException(BoundaryException.getMessage(
                        receiveSize, 0, 7));
            } else {
                throw new RuntimeException(
                        HALLibrary.PARAMETER_OUT_OF_RANGE_MESSAGE);
            }
        }
        HALUtil.checkStatus(status);*/
        if(receiveSize > 0 && dataReceived != null)
        {
            dataReceivedBuffer.get(dataReceived);
        }
        return aborted;
    }

    /**
     * Attempt to address a device on the I2C bus.
     * 
     * This allows you to figure out if there is a device on the I2C bus that
     * responds to the address specified in the constructor.
     * @return Transfer Aborted... false for success, true for aborted.
     */
    @objid ("39e97c1b-04e8-4bf4-aec3-8fe62112c563")
    public boolean addressOnly() {
        return transaction(null, (byte) 0, null, (byte) 0);
    }

    /**
     * Execute a write transaction with the device.
     * 
     * Write a single byte to a register on a device and wait until the
     * transaction is complete.
     * @param registerAddress The address of the register on the device to be written.
     * @param data The byte to write to the register on the device.
     */
    @objid ("40a76f56-8dfa-48b3-8644-1adb74a7c5db")
    public synchronized boolean write(int registerAddress, int data) {
        byte[] buffer = new byte[2];
        buffer[0] = (byte) registerAddress;
        buffer[1] = (byte) data;
        
        ByteBuffer dataToSendBuffer = ByteBuffer.allocateDirect(2);
        dataToSendBuffer.put(buffer);
        return I2CJNI.i2CWrite((byte)m_port.getValue(), (byte) m_deviceAddress, dataToSendBuffer, (byte)buffer.length) < 0;
    }

    /**
     * Execute a write transaction with the device.
     * 
     * Write multiple bytes to a register on a device and wait until the
     * transaction is complete.
     * @param data The data to write to the device.
     */
    @objid ("68d30b7f-ad29-456d-9788-930f507068e0")
    public synchronized boolean writeBulk(byte[] data) {
        ByteBuffer dataToSendBuffer = ByteBuffer.allocateDirect(data.length);
        dataToSendBuffer.put(data);
        return I2CJNI.i2CWrite((byte)m_port.getValue(), (byte) m_deviceAddress, dataToSendBuffer, (byte)data.length) < 0;
    }

    /**
     * Execute a read transaction with the device.
     * 
     * Read 1 to 7 bytes from a device. Most I2C devices will auto-increment the
     * register pointer internally allowing you to read up to 7 consecutive
     * registers on a device in a single transaction.
     * @param registerAddress The register to read first in the transaction.
     * @param count The number of bytes to read in the transaction. [1..7]
     * @param buffer A pointer to the array of bytes to store the data read from
     * the device.
     * @return Transfer Aborted... false for success, true for aborted.
     */
    @objid ("28d01230-dce7-489c-a1e6-ecfebd50a5b8")
    public boolean read(int registerAddress, int count, byte[] buffer) {
        BoundaryException.assertWithinBounds(count, 1, 7);
        if (buffer == null) {
            throw new NullPointerException("Null return buffer was given");
        }
        byte[] registerAddressArray = new byte[1];
        registerAddressArray[0] = (byte) registerAddress;
        return transaction(registerAddressArray, registerAddressArray.length,
                        buffer, count);
    }

    /**
     * Execute a read only transaction with the device.
     * 
     * Read 1 to 7 bytes from a device. This method does not write any data to prompt
     * the device.
     * @param buffer A pointer to the array of bytes to store the data read from
     * the device.
     * @param count The number of bytes to read in the transaction. [1..7]
     * @return Transfer Aborted... false for success, true for aborted.
     */
    @objid ("5f552707-fb1a-4521-ba6b-79c49745a63a")
    public boolean readOnly(byte[] buffer, int count) {
        BoundaryException.assertWithinBounds(count, 1, 7);
        if (buffer == null) {
            throw new NullPointerException("Null return buffer was given");
        }
        
        ByteBuffer dataReceivedBuffer = ByteBuffer.allocateDirect(count);
        
        int retVal = I2CJNI.i2CRead((byte)m_port.getValue(), (byte) m_deviceAddress, dataReceivedBuffer, (byte)count);
        dataReceivedBuffer.get(buffer);
        return retVal < 0;
    }

    /**
     * Send a broadcast write to all devices on the I2C bus.
     * 
     * This is not currently implemented!
     * @param registerAddress The register to write on all devices on the bus.
     * @param data The value to write to the devices.
     */
    @objid ("d9b3a8c8-012a-4e97-abc5-01775a0c4bbd")
    public void broadcast(int registerAddress, int data) {
    }

    /**
     * Verify that a device's registers contain expected values.
     * 
     * Most devices will have a set of registers that contain a known value that
     * can be used to identify them. This allows an I2C device driver to easily
     * verify that the device contains the expected value.
     * @pre The device must support and be configured to use register
     * auto-increment.
     * @param registerAddress The base register to start reading from the device.
     * @param count The size of the field to be verified.
     * @param expected A buffer containing the values expected from the device.
     * @return true if the sensor was verified to be connected
     */
    @objid ("374e1974-e9c3-4f8f-8f0d-c5e546553300")
    public boolean verifySensor(int registerAddress, int count, byte[] expected) {
        // TODO: Make use of all 7 read bytes
        byte[] deviceData = new byte[4];
        for (int i = 0, curRegisterAddress = registerAddress; i < count; i += 4, curRegisterAddress += 4) {
            int toRead = count - i < 4 ? count - i : 4;
            // Read the chunk of data. Return false if the sensor does not
            // respond.
            if (read(curRegisterAddress, toRead, deviceData)) {
                return false;
            }
        
            for (byte j = 0; j < toRead; j++) {
                if (deviceData[j] != expected[i + j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @objid ("acd1e9e7-cb71-4968-8f7d-1e8932716307")
    public enum Port {
        kOnboard (0),
        kMXP (1);

        @objid ("7eac1474-2f2e-4c96-b7c7-837e137a84a8")
        private int value;

        @objid ("2ac8cbfb-0761-43b6-acdf-e09aac88656e")
        private Port(int value) {
            this.value = value;
        }

        @objid ("d4d231fb-a096-4c8c-8650-3a0c468b2c20")
        public int getValue() {
            return this.value;
        }

    }

}
