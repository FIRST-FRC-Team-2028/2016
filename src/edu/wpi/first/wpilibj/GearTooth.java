/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * Alias for counter class.
 * Implement the gear tooth sensor supplied by FIRST. Currently there is no reverse sensing on
 * the gear tooth sensor, but in future versions we might implement the necessary timing in the
 * FPGA to sense direction.
 */
@objid ("c676b455-3c80-4d70-9ce0-93bd5a409a3c")
public class GearTooth extends Counter {
    @objid ("8b7f4b5c-8dab-4acb-bcf7-ea6ffee221b3")
    private static final double kGearToothThreshold = 55e-6;

    /**
     * Common code called by the constructors.
     */
    @objid ("f8162fd0-da89-41ef-9595-a47b545efbd4")
    public void enableDirectionSensing(boolean directionSensitive) {
        if (directionSensitive) {
            setPulseLengthMode(kGearToothThreshold);
        }
    }

    /**
     * Construct a GearTooth sensor given a channel.
     * 
     * No direction sensing is assumed.
     * @param channel The GPIO channel that the sensor is connected to.
     */
    @objid ("0d6a4f11-a035-4d9e-beef-f157b29da5a5")
    public GearTooth(final int channel) {
        this(channel,false);
    }

    /**
     * Construct a GearTooth sensor given a channel.
     * @param channel The DIO channel that the sensor is connected to. 0-9 are on-board, 10-25 are on the MXP port
     * @param directionSensitive True to enable the pulse length decoding in hardware to specify count direction.
     */
    @objid ("088660fe-a02c-477c-99c4-19bae4e30de4")
    public GearTooth(final int channel, boolean directionSensitive) {
        super(channel);
        enableDirectionSensing(directionSensitive);
        if(directionSensitive) {
            UsageReporting.report(tResourceType.kResourceType_GearTooth, channel, 0, "D");
        } else {
            UsageReporting.report(tResourceType.kResourceType_GearTooth, channel, 0);
        }
        LiveWindow.addSensor("GearTooth", channel, this);
    }

    /**
     * Construct a GearTooth sensor given a digital input.
     * This should be used when sharing digital inputs.
     * @param source An existing DigitalSource object (such as a DigitalInput)
     * @param directionSensitive True to enable the pulse length decoding in hardware to specify count direction.
     */
    @objid ("ea32204d-cef0-42d7-aa5c-da0c4c3845e9")
    public GearTooth(DigitalSource source, boolean directionSensitive) {
        super(source);
        enableDirectionSensing(directionSensitive);
    }

    /**
     * Construct a GearTooth sensor given a digital input.
     * This should be used when sharing digial inputs.
     * 
     * No direction sensing is assumed.
     * @param source An object that fully descibes the input that the sensor is connected to.
     */
    @objid ("f56f2325-ba94-4f9a-8d9b-4045cedaac36")
    public GearTooth(DigitalSource source) {
        this(source,false);
    }

/*
     * Live Window code, only does anything if live window is activated.
     */
    @objid ("42417b22-0a62-4ac3-9085-a9a5d7f4e0d7")
    public String getSmartDashboardType() {
        return "Gear Tooth";
    }

}
