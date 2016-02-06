package edu.wpi.first.wpilibj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.hal.HALLibrary;
import edu.wpi.first.wpilibj.hal.HALUtil;
import edu.wpi.first.wpilibj.hal.SPIJNI;

/**
 * Represents a SPI bus port
 * 
 * @author koconnor
 */
@objid ("bc0e5c79-2793-4e2a-8fc0-298eb8dd08b3")
public class SPI extends SensorBase {
    @objid ("2a82c7b8-f571-4b53-9526-e0c5fae4a9fb")
    private static int devices = 0;

    @objid ("3e27b307-060c-47c9-8127-5ee0327a5c43")
    private byte m_port;

    @objid ("7af2b4fc-3945-4e13-95dd-c94b525c2a3c")
    private int bitOrder;

    @objid ("aeac6088-5316-4262-b602-fa62cabe9906")
    private int clockPolarity;

    @objid ("bcabe02d-ed1d-4254-b6f4-5746cdb47254")
    private int dataOnTrailing;

    /**
     * Constructor
     * @param port the physical SPI port
     */
    @objid ("fd6ec428-8c3f-4f08-80f1-3de31d45d10c")
    public SPI(Port port) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        m_port = (byte)port.getValue();
        devices++;
        
        SPIJNI.spiInitialize(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        
        UsageReporting.report(tResourceType.kResourceType_SPI, devices);
    }

    /**
     * Free the resources used by this object
     */
    @objid ("689e5472-38f8-4104-83db-e261a80c9fb6")
    public void free() {
        SPIJNI.spiClose(m_port);
    }

    /**
     * Configure the rate of the generated clock signal.
     * The default value is 500,000 Hz.
     * The maximum value is 4,000,000 Hz.
     * @param hz The clock rate in Hertz.
     */
    @objid ("9b6233da-31d0-4d7b-8eea-0f04710ab08a")
    public final void setClockRate(int hz) {
        SPIJNI.spiSetSpeed(m_port, hz);
    }

    /**
     * Configure the order that bits are sent and received on the wire
     * to be most significant bit first.
     */
    @objid ("23306e14-bd50-4fac-a5f1-e226f7c31563")
    public final void setMSBFirst() {
        this.bitOrder = 1;
        SPIJNI.spiSetOpts(m_port, this.bitOrder, this.dataOnTrailing, this.clockPolarity);
    }

    /**
     * Configure the order that bits are sent and received on the wire
     * to be least significant bit first.
     */
    @objid ("ed21cbe8-062f-45fd-b76f-450bfda92871")
    public final void setLSBFirst() {
        this.bitOrder = 0;
        SPIJNI.spiSetOpts(m_port, this.bitOrder, this.dataOnTrailing, this.clockPolarity);
    }

    /**
     * Configure the clock output line to be active low.
     * This is sometimes called clock polarity high or clock idle high.
     */
    @objid ("ecd91ed8-9441-427f-8a22-adf3858be141")
    public final void setClockActiveLow() {
        this.clockPolarity = 1;
        SPIJNI.spiSetOpts(m_port, this.bitOrder, this.dataOnTrailing, this.clockPolarity);
    }

    /**
     * Configure the clock output line to be active high.
     * This is sometimes called clock polarity low or clock idle low.
     */
    @objid ("0513c63b-bfc0-4b48-b322-173d14ce0d7d")
    public final void setClockActiveHigh() {
        this.clockPolarity = 0;
        SPIJNI.spiSetOpts(m_port, this.bitOrder, this.dataOnTrailing, this.clockPolarity);
    }

    /**
     * Configure that the data is stable on the falling edge and the data
     * changes on the rising edge.
     */
    @objid ("a11cd9b3-7815-4ea0-b403-d0130bb0c90c")
    public final void setSampleDataOnFalling() {
        this.dataOnTrailing = 1;
        SPIJNI.spiSetOpts(m_port, this.bitOrder, this.dataOnTrailing, this.clockPolarity);
    }

    /**
     * Configure that the data is stable on the rising edge and the data
     * changes on the falling edge.
     */
    @objid ("47cc67e1-c427-4cc2-8fde-39614ac7e845")
    public final void setSampleDataOnRising() {
        this.dataOnTrailing = 0;
        SPIJNI.spiSetOpts(m_port, this.bitOrder, this.dataOnTrailing, this.clockPolarity);
    }

    /**
     * Configure the chip select line to be active high.
     */
    @objid ("b384d3c9-9b45-461d-bf67-6b04eb4a71c8")
    public final void setChipSelectActiveHigh() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        SPIJNI.spiSetChipSelectActiveHigh(m_port, status.asIntBuffer());
            
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Configure the chip select line to be active low.
     */
    @objid ("b5b56eba-1caf-4d97-b01a-89fd12a8567b")
    public final void setChipSelectActiveLow() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        SPIJNI.spiSetChipSelectActiveLow(m_port, status.asIntBuffer());
            
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Write data to the slave device.  Blocks until there is space in the
     * output FIFO.
     * 
     * If not running in output only mode, also saves the data received
     * on the MISO input during the transfer into the receive FIFO.
     */
    @objid ("b308522c-226f-4e0a-b055-ebaa083a3727")
    public int write(byte[] dataToSend, int size) {
        int retVal = 0;
        ByteBuffer dataToSendBuffer = ByteBuffer.allocateDirect(size);
        dataToSendBuffer.put(dataToSend);
        retVal = SPIJNI.spiWrite(m_port, dataToSendBuffer, (byte) size);
        return retVal;
    }

    /**
     * Read a word from the receive FIFO.
     * 
     * Waits for the current transfer to complete if the receive FIFO is empty.
     * 
     * If the receive FIFO is empty, there is no active transfer, and initiate
     * is false, errors.
     * @param initiate If true, this function pushes "0" into the
     * transmit buffer and initiates a transfer.
     * If false, this function assumes that data is
     * already in the receive FIFO from a previous write.
     */
    @objid ("be892e96-bc2c-4bdb-9ab9-461ea677e0c1")
    public int read(boolean initiate, byte[] dataReceived, int size) {
        int retVal = 0;
        ByteBuffer dataReceivedBuffer = ByteBuffer.allocateDirect(size);
        ByteBuffer dataToSendBuffer = ByteBuffer.allocateDirect(size);
        if(initiate)
            retVal = SPIJNI.spiTransaction(m_port, dataToSendBuffer, dataReceivedBuffer, (byte) size);
        else
            retVal = SPIJNI.spiRead(m_port, dataReceivedBuffer, (byte) size);
        dataReceivedBuffer.get(dataReceived);
        return retVal;
    }

    /**
     * Perform a simultaneous read/write transaction with the device
     * @param dataToSend The data to be written out to the device
     * @param dataReceived Buffer to receive data from the device
     * @param size The length of the transaction, in bytes
     */
    @objid ("5b012b1f-e651-4d16-b4db-0f3d697aff30")
    public int transaction(byte[] dataToSend, byte[] dataReceived, int size) {
        int retVal = 0;
        ByteBuffer dataToSendBuffer = ByteBuffer.allocateDirect(size);
        dataToSendBuffer.put(dataToSend);
        ByteBuffer dataReceivedBuffer = ByteBuffer.allocateDirect(size);
        retVal = SPIJNI.spiTransaction(m_port, dataToSendBuffer, dataReceivedBuffer, (byte) size);
        dataReceivedBuffer.get(dataReceived);
        return retVal;
    }

    @objid ("3e4506e9-795e-4950-b4e8-a27215d5dee5")
    public enum Port {
        kOnboardCS0 (0),
        kOnboardCS1 (1),
        kOnboardCS2 (2),
        kOnboardCS3 (3),
        kMXP (4);

        @objid ("bac57df4-ed65-46dc-86e6-c4e1e8ddfe39")
        private int value;

        @objid ("821dcd04-fcab-4319-913b-84211e85d521")
        private Port(int value) {
            this.value = value;
        }

        @objid ("7047c340-85ef-42e4-be25-b557614e2f04")
        public int getValue() {
            return this.value;
        }

    }

}
