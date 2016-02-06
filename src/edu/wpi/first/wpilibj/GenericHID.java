/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * GenericHID Interface
 */
@objid ("e1f36fdc-d529-4055-abef-13afcaa18d29")
public abstract class GenericHID {
    /**
     * Get the x position of the HID
     * @return the x position of the HID
     */
    @objid ("00f09cf3-8b40-4660-9c5b-370c88a4c478")
    public final double getX() {
        return getX(Hand.kRight);
    }

    /**
     * Get the x position of HID
     * @param hand which hand, left or right
     * @return the x position
     */
    @objid ("a99e2699-f052-454a-92d5-bc01683a5217")
    public abstract double getX(Hand hand);

    /**
     * Get the y position of the HID
     * @return the y position
     */
    @objid ("21e184bb-457d-4686-99ec-cc863eb86ba2")
    public final double getY() {
        return getY(Hand.kRight);
    }

    /**
     * Get the y position of the HID
     * @param hand which hand, left or right
     * @return the y position
     */
    @objid ("729a9205-6185-4843-bd0a-5d2956cf2c06")
    public abstract double getY(Hand hand);

    /**
     * Get the z position of the HID
     * @return the z position
     */
    @objid ("1bf07260-0cfd-49ca-bcd1-6b56ede3a622")
    public final double getZ() {
        return getZ(Hand.kRight);
    }

    /**
     * Get the z position of the HID
     * @param hand which hand, left or right
     * @return the z position
     */
    @objid ("6e19cf28-1fe1-4a7c-8ac6-09ab9e3563ac")
    public abstract double getZ(Hand hand);

    /**
     * Get the twist value
     * @return the twist value
     */
    @objid ("773d98aa-9479-448b-a6ad-2d3858d04e94")
    public abstract double getTwist();

    /**
     * Get the throttle
     * @return the throttle value
     */
    @objid ("20f55f74-e1af-491b-ad90-3b5c3fe6101c")
    public abstract double getThrottle();

    /**
     * Get the raw axis
     * @param which index of the axis
     * @return the raw value of the selected axis
     */
    @objid ("0f1fa571-7eb1-4d44-aef3-a3a1b11b6689")
    public abstract double getRawAxis(int which);

    /**
     * Is the trigger pressed
     * @return true if pressed
     */
    @objid ("891f5504-f55c-4924-992f-1180d07bd30d")
    public final boolean getTrigger() {
        return getTrigger(Hand.kRight);
    }

    /**
     * Is the trigger pressed
     * @param hand which hand
     * @return true if the trigger for the given hand is pressed
     */
    @objid ("0201d851-56c2-44b5-89a1-83297e31ce32")
    public abstract boolean getTrigger(Hand hand);

    /**
     * Is the top button pressed
     * @return true if the top button is pressed
     */
    @objid ("6a7b3fc2-887a-41d4-8a20-195db52c5ae1")
    public final boolean getTop() {
        return getTop(Hand.kRight);
    }

    /**
     * Is the top button pressed
     * @param hand which hand
     * @return true if hte top button for the given hand is pressed
     */
    @objid ("32e4f561-7e25-4c9e-aef5-5c183222b149")
    public abstract boolean getTop(Hand hand);

    /**
     * Is the bumper pressed
     * @return true if the bumper is pressed
     */
    @objid ("47744d4e-aed7-4fd1-bd18-499b466d0fe9")
    public final boolean getBumper() {
        return getBumper(Hand.kRight);
    }

    /**
     * Is the bumper pressed
     * @param hand which hand
     * @return true if hte bumper is pressed
     */
    @objid ("1e6c71c0-a24d-47f8-970d-ef7113cf0391")
    public abstract boolean getBumper(Hand hand);

    /**
     * Is the given button pressed
     * @param button which button number
     * @return true if the button is pressed
     */
    @objid ("e98cebb9-8fd4-480f-9cf7-1f301b02d83b")
    public abstract boolean getRawButton(int button);

    @objid ("e363a479-a0a3-48d4-953a-782f5a5bd0e7")
    public abstract int getPOV(int pov);

    @objid ("aad17817-8168-4d46-a71e-c3812eee0809")
    public int getPOV() {
        return getPOV(0);
    }

    /**
     * Which hand the Human Interface Device is associated with.
     */
    @objid ("11ceca68-85ef-4b68-b447-a312b4c0d9b1")
    public static class Hand {
        /**
         * The integer value representing this enumeration
         */
        @objid ("63ef039f-3d35-4755-9c1a-040735a32294")
        public final int value;

        @objid ("ac19c743-6261-476c-8573-5f14df55e68c")
         static final int kLeft_val = 0;

        @objid ("ce7415bf-a4d5-4fa8-b1a4-6017d5cf4515")
         static final int kRight_val = 1;

        /**
         * hand: left
         */
        @objid ("be3ea97c-c1f8-4b49-9bd7-9898af2fc2ae")
        public static final Hand kLeft = new Hand(kLeft_val);

        /**
         * hand: right
         */
        @objid ("c2ab6f01-777f-4502-8af3-4db225fa794e")
        public static final Hand kRight = new Hand(kRight_val);

        @objid ("eb9446c4-5354-40c0-9a5e-569e26929f55")
        private Hand(int value) {
            this.value = value;
        }

    }

}
