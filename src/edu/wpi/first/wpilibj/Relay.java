/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.hal.DIOJNI;
import edu.wpi.first.wpilibj.hal.HALUtil;
import edu.wpi.first.wpilibj.hal.RelayJNI;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import edu.wpi.first.wpilibj.util.AllocationException;
import edu.wpi.first.wpilibj.util.CheckedAllocationException;

/**
 * Class for VEX Robotics Spike style relay outputs. Relays are intended to be
 * connected to Spikes or similar relays. The relay channels controls a pair of
 * pins that are either both off, one on, the other on, or both on. This
 * translates into two Spike outputs at 0v, one at 12v and one at 0v, one at 0v
 * and the other at 12v, or two Spike outputs at 12V. This allows off, full
 * forward, or full reverse control of motors without variable speed. It also
 * allows the two channels (forward and reverse) to be used independently for
 * something that does not care about voltage polarity (like a solenoid).
 */
@objid ("a53c77b4-2aba-4033-b27c-e007e09b97ca")
public class Relay extends SensorBase implements LiveWindowSendable {
    @objid ("ac35ff3e-99d6-4f8b-8db8-7aca0adcabb5")
    private final int m_channel;

    @objid ("05308f03-28fd-42a6-9239-22315eceb8f5")
    private ByteBuffer m_port;

    @objid ("aa3a5067-4a1e-44f7-9658-989eeb3bc87e")
    private Direction m_direction;

    @objid ("7a2598ae-d258-49f2-83c1-ee16cf2e9442")
    private ITable m_table;

    @objid ("e19b0ed1-d5f4-460f-be67-cbfef5a7c270")
    private ITableListener m_table_listener;

    @objid ("c19d3d32-8c4c-4cbe-9668-00308c739ad9")
    private static Resource relayChannels = new Resource(kRelayChannels * 2);

    /**
     * Common relay initialization method. This code is common to all Relay
     * constructors and initializes the relay and reserves all resources that
     * need to be locked. Initially the relay is set to both lines at 0v.
     */
    @objid ("ae4292af-50c7-4267-a562-c0cf7cd4e2b8")
    private void initRelay() {
        SensorBase.checkRelayChannel(m_channel);
        try {
            if (m_direction == Direction.kBoth
                    || m_direction == Direction.kForward) {
                relayChannels.allocate(m_channel * 2);
                UsageReporting.report(tResourceType.kResourceType_Relay, m_channel);
            }
            if (m_direction == Direction.kBoth
                    || m_direction == Direction.kReverse) {
                relayChannels.allocate(m_channel * 2 + 1);
                UsageReporting.report(tResourceType.kResourceType_Relay, m_channel + 128);
            }
        } catch (CheckedAllocationException e) {
            throw new AllocationException("Relay channel " + m_channel + " is already allocated");
        }
        
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        m_port = DIOJNI.initializeDigitalPort(DIOJNI.getPort((byte) m_channel), status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        
        LiveWindow.addActuator("Relay", m_channel, this);
    }

    /**
     * Relay constructor given a channel.
     * @param channel The channel number for this relay (0 - 3).
     * @param direction The direction that the Relay object will control.
     */
    @objid ("ed48bf74-f6d9-4d9c-8421-7d8081fa0cb2")
    public Relay(final int channel, Direction direction) {
        if (direction == null)
            throw new NullPointerException("Null Direction was given");
        m_channel = channel;
        m_direction = direction;
        initRelay();
        set(Value.kOff);
    }

    /**
     * Relay constructor given a channel, allowing both directions.
     * @param channel The channel number for this relay (0 - 3).
     */
    @objid ("93b82e23-d4fc-4bb1-a2d2-5bb066bf21e7")
    public Relay(final int channel) {
        this(channel, Direction.kBoth);
    }

    @objid ("ed38a2c5-a6cc-4615-ad1b-d61218f5da42")
    @Override
    public void free() {
        if (m_direction == Direction.kBoth || m_direction == Direction.kForward) {
            relayChannels.free(m_channel*2);
        }
        if (m_direction == Direction.kBoth || m_direction == Direction.kReverse) {
            relayChannels.free(m_channel*2 + 1);
        }
        
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        RelayJNI.setRelayForward(m_port, (byte) 0, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        RelayJNI.setRelayReverse(m_port, (byte) 0, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
        
        DIOJNI.freeDIO(m_port, status.asIntBuffer());
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Set the relay state.
     * 
     * Valid values depend on which directions of the relay are controlled by
     * the object.
     * 
     * When set to kBothDirections, the relay can be set to any of the four
     * states: 0v-0v, 12v-0v, 0v-12v, 12v-12v
     * 
     * When set to kForwardOnly or kReverseOnly, you can specify the constant
     * for the direction or you can simply specify kOff_val and kOn_val. Using
     * only kOff_val and kOn_val is recommended.
     * @param value The state to set the relay.
     */
    @objid ("cfe7854b-e8b7-49b5-b019-de8d4ade79aa")
    public void set(Value value) {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        switch (value) {
        case kOff:
            if (m_direction == Direction.kBoth
                    || m_direction == Direction.kForward) {
                RelayJNI.setRelayForward(m_port, (byte) 0, status.asIntBuffer());
            }
            if (m_direction == Direction.kBoth
                    || m_direction == Direction.kReverse) {
                RelayJNI.setRelayReverse(m_port, (byte) 0, status.asIntBuffer());
            }
            break;
        case kOn:
            if (m_direction == Direction.kBoth
                    || m_direction == Direction.kForward) {
                RelayJNI.setRelayForward(m_port, (byte) 1, status.asIntBuffer());
            }
            if (m_direction == Direction.kBoth
                    || m_direction == Direction.kReverse) {
                RelayJNI.setRelayReverse(m_port, (byte) 1, status.asIntBuffer());
            }
            break;
        case kForward:
            if (m_direction == Direction.kReverse)
                throw new InvalidValueException(
                        "A relay configured for reverse cannot be set to forward");
            if (m_direction == Direction.kBoth
                    || m_direction == Direction.kForward) {
                RelayJNI.setRelayForward(m_port, (byte) 1, status.asIntBuffer());
            }
            if (m_direction == Direction.kBoth) {
                RelayJNI.setRelayReverse(m_port, (byte) 0, status.asIntBuffer());
            }
            break;
        case kReverse:
            if (m_direction == Direction.kForward)
                throw new InvalidValueException(
                        "A relay configured for forward cannot be set to reverse");
            if (m_direction == Direction.kBoth) {
                RelayJNI.setRelayForward(m_port, (byte) 0, status.asIntBuffer());
            }
            if (m_direction == Direction.kBoth
                    || m_direction == Direction.kReverse) {
                RelayJNI.setRelayReverse(m_port, (byte) 1, status.asIntBuffer());
            }
            break;
        default:
            // Cannot hit this, limited by Value enum
        }
        
        HALUtil.checkStatus(status.asIntBuffer());
    }

    /**
     * Get the Relay State
     * 
     * Gets the current state of the relay.
     * 
     * When set to kForwardOnly or kReverseOnly, value is returned as kOn/kOff
     * not kForward/kReverse (per the recommendation in Set)
     * @return The current state of the relay as a Relay::Value
     */
    @objid ("0584bf65-2f18-4d17-9564-045e0244a36c")
    public Value get() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        
        if (RelayJNI.getRelayForward(m_port, status.asIntBuffer()) != 0) {
            if (RelayJNI.getRelayReverse(m_port, status.asIntBuffer()) != 0) {
                return Value.kOn;
            } else {
                if (m_direction == Direction.kForward) {
                    return Value.kOn;
                } else {
                    return Value.kForward;
                }
            }
        } else {
            if (RelayJNI.getRelayReverse(m_port, status.asIntBuffer()) != 0) {
                if (m_direction == Direction.kReverse) {
                    return Value.kOn;
                } else {
                    return Value.kReverse;
                }
            } else {
                return Value.kOff;
            }
        }
    }

    /**
     * Set the Relay Direction
     * 
     * Changes which values the relay can be set to depending on which direction
     * is used
     * 
     * Valid inputs are kBothDirections, kForwardOnly, and kReverseOnly
     * @param direction The direction for the relay to operate in
     */
    @objid ("fee302ae-b554-4a3a-89fc-bccf360f5ca3")
    public void setDirection(Direction direction) {
        if (direction == null)
            throw new NullPointerException("Null Direction was given");
        if (m_direction == direction) {
            return;
        }
        
        free();
        
        m_direction = direction;
        
        initRelay();
    }

/*
     * Live Window code, only does anything if live window is activated.
     */
    @objid ("fb83bfa3-c6e7-4790-bdd8-299fbb97c49d")
    @Override
    public String getSmartDashboardType() {
        return "Relay";
    }

    /**
     * {@inheritDoc}
     */
    @objid ("feae7303-b9c4-4dea-909a-20ef9b048418")
    @Override
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @objid ("e0bb63e6-ed40-4f17-8923-0dd2d669da09")
    @Override
    public ITable getTable() {
        return m_table;
    }

    /**
     * {@inheritDoc}
     */
    @objid ("df4fb9bf-89da-472e-9283-d479a63511fc")
    @Override
    public void updateTable() {
        if (m_table != null) {
            if (get() == Value.kOn) {
                m_table.putString("Value", "On");
            } else if (get() == Value.kForward) {
                m_table.putString("Value", "Forward");
            } else if (get() == Value.kReverse) {
                m_table.putString("Value", "Reverse");
            } else {
                m_table.putString("Value", "Off");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("43f04233-e8ae-48cc-b5f9-3d0117d44733")
    @Override
    public void startLiveWindowMode() {
        m_table_listener = new ITableListener() {
            @Override
            public void valueChanged(ITable itable, String key, Object value,
                    boolean bln) {
                String val = ((String) value);
                if (val.equals("Off")) {
                    set(Value.kOff);
                } else if (val.equals("On")) {
                    set(Value.kOn);
                } else if (val.equals("Forward")) {
                    set(Value.kForward);
                } else if (val.equals("Reverse")) {
                    set(Value.kReverse);
                }
            }
        };
        m_table.addTableListener("Value", m_table_listener, true);
    }

    /**
     * {@inheritDoc}
     */
    @objid ("d8955f78-d89a-4c52-9408-5864f8033c16")
    @Override
    public void stopLiveWindowMode() {
        // TODO: Broken, should only remove the listener from "Value" only.
        m_table.removeTableListener(m_table_listener);
    }

    /**
     * This class represents errors in trying to set relay values contradictory
     * to the direction to which the relay is set.
     */
    @objid ("ff51b560-989f-499f-8223-1ae59ae4d9e2")
    public class InvalidValueException extends RuntimeException {
        /**
         * Create a new exception with the given message
         * @param message the message to pass with the exception
         */
        @objid ("6192f4a5-d3dc-4584-8fe6-18e1a46ec666")
        public InvalidValueException(String message) {
            super(message);
        }

    }

    /**
     * The state to drive a Relay to.
     */
    @objid ("f6c3edeb-eb99-43c4-a08b-ddcba775ad85")
    public enum Value {
        /**
         * value: off
         */
        kOff (0),
        /**
         * value: on for relays with defined direction
         */
        kOn (1),
        /**
         * value: forward
         */
        kForward (2),
        /**
         * value: reverse
         */
        kReverse (3);

        /**
         * The integer value representing this enumeration
         */
        @objid ("fc5dbc46-45c6-49ac-b255-ae4df39b5704")
        public final int value;

        @objid ("c7c208b8-52c0-41b0-925b-b458b03566c6")
        private Value(int value) {
            this.value = value;
        }

    }

    /**
     * The Direction(s) that a relay is configured to operate in.
     */
    @objid ("3710fef6-f304-443a-8ad5-a465f4085454")
    public enum Direction {
        /**
         * direction: both directions are valid
         */
        kBoth (0),
        /**
         * direction: Only forward is valid
         */
        kForward (1),
        /**
         * direction: only reverse is valid
         */
        kReverse (2);

        /**
         * The integer value representing this enumeration
         */
        @objid ("8173e810-6b6b-4e95-8e36-12f2090d8204")
        public final int value;

        @objid ("0bd7e84a-3ccf-462d-a182-25b33acbeace")
        private Direction(int value) {
            this.value = value;
        }

    }

}
