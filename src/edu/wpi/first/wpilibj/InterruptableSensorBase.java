/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.hal.HALUtil;
import edu.wpi.first.wpilibj.hal.InterruptJNI;
import edu.wpi.first.wpilibj.util.AllocationException;
import edu.wpi.first.wpilibj.util.CheckedAllocationException;

/**
 * Base for sensors to be used with interrupts
 */
@objid ("d720f023-498b-4778-9a8f-a9aef1962265")
public abstract class InterruptableSensorBase extends SensorBase {
    /**
     * The interrupt resource
     */
    @objid ("4afefe2a-12a0-46e9-87e7-2b0a3f2467b7")
    protected ByteBuffer m_interrupt = null;

    /**
     * Flags if the interrupt being allocated is synchronous
     */
    @objid ("cf14dfd1-f8bd-4640-91be-d22d1b5e05f3")
    protected boolean m_isSynchronousInterrupt = false;

    /**
     * The index of the interrupt
     */
    @objid ("c7b9f672-5504-4f85-9617-8c83f07a4e5f")
    protected int m_interruptIndex;

    /**
     * Resource manager
     */
    @objid ("14163d05-6926-4e06-9e6e-2cfc81c65eb2")
    protected static Resource interrupts = new Resource(8);

    /**
     * Create a new InterrupatableSensorBase
     */
    @objid ("d9c384d3-db33-4534-a606-030e421dc7c9")
    public InterruptableSensorBase() {
        m_interrupt = null;
    }

    /**
     * @return true if this is an analog trigger
     */
    @objid ("782717c0-abdd-4559-9e23-43ad7dcc7134")
    abstract boolean getAnalogTriggerForRouting();

    /**
     * @return channel routing number
     */
    @objid ("c629bef0-d229-4582-9552-fd3b60b93dab")
    abstract int getChannelForRouting();

    /**
     * @return module routing number
     */
    @objid ("68cf3911-ffff-4c03-87cd-94cceca9eb27")
    abstract byte getModuleForRouting();

    /**
     * Request one of the 8 interrupts asynchronously on this digital input.
     * @param handler The {@link InterruptHandlerFunction} that contains the method
     * {@link InterruptHandlerFunction#interruptFired(int, Object)} that
     * will be called whenever there is an interrupt on this device.
     * Request interrupts in synchronous mode where the user program
     * interrupt handler will be called when an interrupt occurs. The
     * default is interrupt on rising edges only.
     */
    @objid ("b441980e-d7b8-42d5-9c82-d5fa223ba05a")
    public void requestInterrupts(InterruptHandlerFunction<?> handler) {
        if(m_interrupt != null){
            throw new AllocationException("The interrupt has already been allocated");
        }
        
        allocateInterrupts(false);
        
        assert (m_interrupt != null);
        
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        InterruptJNI.requestInterrupts(m_interrupt, getModuleForRouting(),
                getChannelForRouting(),
                (byte) (getAnalogTriggerForRouting() ? 1 : 0), status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        setUpSourceEdge(true, false);
        InterruptJNI.attachInterruptHandler(m_interrupt, handler.function, handler.overridableParamater(), status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Request one of the 8 interrupts synchronously on this digital input. Request
     * interrupts in synchronous mode where the user program will have to
     * explicitly wait for the interrupt to occur using {@link #waitForInterrupt}.
     * The default is interrupt on rising edges only.
     */
    @objid ("7bb48215-be1d-4488-8719-39e19f2f0fab")
    public void requestInterrupts() {
        if(m_interrupt != null){
            throw new AllocationException("The interrupt has already been allocated");
        }
        
        allocateInterrupts(true);
        
        assert (m_interrupt != null);
        
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        // set the byte order
        status.order(ByteOrder.LITTLE_ENDIAN);
        InterruptJNI.requestInterrupts(m_interrupt, getModuleForRouting(),
                getChannelForRouting(),
                (byte) (getAnalogTriggerForRouting() ? 1 : 0), status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        setUpSourceEdge(true, false);
    }

    /**
     * Allocate the interrupt
     * @param watcher true if the interrupt should be in synchronous mode where the user
     * program will have to explicitly wait for the interrupt to occur.
     */
    @objid ("4b40f96d-6fc2-45a3-b707-b29a6d8a8463")
    protected void allocateInterrupts(boolean watcher) {
        try {
            m_interruptIndex = interrupts.allocate();
        } catch (CheckedAllocationException e) {
            throw new AllocationException(
                    "No interrupts are left to be allocated");
        }
        m_isSynchronousInterrupt = watcher;
        
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        m_interrupt = InterruptJNI.initializeInterrupts(m_interruptIndex,
                (byte) (watcher ? 1 : 0), status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Cancel interrupts on this device. This deallocates all the chipobject
     * structures and disables any interrupts.
     */
    @objid ("ee26f7e8-cf93-4b0d-84df-46a3e8a0904c")
    public void cancelInterrupts() {
        if (m_interrupt == null) {
            throw new IllegalStateException("The interrupt is not allocated.");
        }
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        InterruptJNI.cleanInterrupts(m_interrupt, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        m_interrupt = null;
        interrupts.free(m_interruptIndex);
    }

    /**
     * In synchronous mode, wait for the defined interrupt to occur.
     * @param timeout Timeout in seconds
     * @param ignorePrevious If true, ignore interrupts that happened before
     * waitForInterrupt was called.
     */
    @objid ("93d47834-3524-43de-a0d0-946a7c65c2c5")
    public void waitForInterrupt(double timeout, boolean ignorePrevious) {
        if (m_interrupt == null) {
            throw new IllegalStateException("The interrupt is not allocated.");
        }
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        InterruptJNI.waitForInterrupt(m_interrupt, (float) timeout, ignorePrevious, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * In synchronous mode, wait for the defined interrupt to occur.
     * @param timeout Timeout in seconds
     */
    @objid ("9943f02b-fa1b-4af2-9f68-60b381058aaf")
    public void waitForInterrupt(double timeout) {
        waitForInterrupt(timeout, true);
    }

    /**
     * Enable interrupts to occur on this input. Interrupts are disabled when
     * the RequestInterrupt call is made. This gives time to do the setup of the
     * other options before starting to field interrupts.
     */
    @objid ("f97b658e-924e-4c0e-8d4c-97110d87babe")
    public void enableInterrupts() {
        if (m_interrupt == null) {
            throw new IllegalStateException("The interrupt is not allocated.");
        }
        if(m_isSynchronousInterrupt){
            throw new IllegalStateException("You do not need to enable synchronous interrupts");
        }
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        InterruptJNI.enableInterrupts(m_interrupt, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Disable Interrupts without without deallocating structures.
     */
    @objid ("ef303ced-4444-40f7-a770-43a616a2ddb6")
    public void disableInterrupts() {
        if (m_interrupt == null) {
            throw new IllegalStateException("The interrupt is not allocated.");
        }
        if(m_isSynchronousInterrupt){
            throw new IllegalStateException("You can not disable synchronous interrupts");
        }
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        InterruptJNI.disableInterrupts(m_interrupt, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Return the timestamp for the rising interrupt that occurred most
     * recently. This is in the same time domain as getClock().
     * The rising-edge interrupt should be enabled with
     * {@link #setUpSourceEdge}
     * @return Timestamp in seconds since boot.
     */
    @objid ("19f641dc-cffa-4301-9f4e-326b4f4a89ba")
    public double readRisingTimestamp() {
        if (m_interrupt == null) {
            throw new IllegalStateException("The interrupt is not allocated.");
        }
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        double timestamp = InterruptJNI.readRisingTimestamp(m_interrupt, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return timestamp;
    }

    /**
     * Return the timestamp for the falling interrupt that occurred most
     * recently. This is in the same time domain as getClock().
     * The falling-edge interrupt should be enabled with
     * {@link #setUpSourceEdge}
     * @return Timestamp in seconds since boot.
     */
    @objid ("dbc78462-5554-48ca-a25e-3d7e06021f8f")
    public double readFallingTimestamp() {
        if (m_interrupt == null) {
            throw new IllegalStateException("The interrupt is not allocated.");
        }
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        double timestamp = InterruptJNI.readFallingTimestamp(m_interrupt, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        return timestamp;
    }

    /**
     * Set which edge to trigger interrupts on
     * @param risingEdge true to interrupt on rising edge
     * @param fallingEdge true to interrupt on falling edge
     */
    @objid ("ca352e5e-c9a9-4ba2-9d65-37337a3b2d5d")
    public void setUpSourceEdge(boolean risingEdge, boolean fallingEdge) {
        if (m_interrupt != null) {
            ByteBuffer status = ByteBuffer.allocateDirect(4);
            // set the byte order
            status.order(ByteOrder.LITTLE_ENDIAN);
            InterruptJNI.setInterruptUpSourceEdge(m_interrupt,
                    (byte) (risingEdge ? 1 : 0), (byte) (fallingEdge ? 1 : 0),
                    status.asIntBuffer());
            HALUtil.checkStatus(status.asIntBuffer());
        } else {
            throw new IllegalArgumentException(
                    "You must call RequestInterrupts before setUpSourceEdge");
        }
    }


static{
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        InterruptJNI.initializeInterruptJVM(status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }
}
