package com.PhantomMentalists.Stronghold;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PIDSource;

@objid ("db4144c8-7be1-48b5-9d58-9bc308243305")
public class UltrasonicSensor implements PIDSource {
    /**
     * 
     * The UltrasonicSensor class gets eventually the distance from
     * the robot to the first obstacle that the ultrasonic sensors
     * find. It starts getting a voltage value, which is going to
     * be converted into distance in inches, which we will implement
     * in the pidGet method.
     * 
     * @author Diego
     */
    @objid ("d16268a3-74db-469d-91ee-69a025785033")
    protected AnalogInput input;

    /**
     * UltrasonicSensor constructor
     * 
     * Initialize the ultrasonic sensor, giving it the CAN id.
     */
    @objid ("9671a31a-9088-4031-b46c-f6c0fb27ad33")
    public UltrasonicSensor(int analogChannel) {
    	input = new AnalogInput(1);
    }

    /**
     * Converts the voltage from the analog input into inches.
     * 
     * The LV MaxSonar EZ produces a 0-5v signal with 512 divisions.
     * 
     * @return the distance to the target in inches
     */
    @objid ("da43f4b5-7392-45fa-aaa1-0636243b388e")
    public double getRangeInInches() {
    	double voltageValue = input.getVoltage();	    	
    	return (voltageValue / (5.0 / 512));
    }

    /**
     * Get the result to use in PIDController
     * @return the result to use in PIDController
     */
    @objid ("b343bf84-9bcd-43e5-bf15-329615ef6859")
    public double pidGet() {
		return getRangeInInches();
    }

}
