package com.PhantomMentalists.Stronghold;

import com.PhantomMentalists.Stronghold.Parameters;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

@objid ("db4144c8-7be1-48b5-9d58-9bc308243305")
public class UltrasonicSensor implements PIDSource {
    /**
     * <Enter note text here>
     */
    @objid ("d16268a3-74db-469d-91ee-69a025785033")
    protected AnalogInput input;

    /**
     * <Enter note text here>
     */
    @objid ("9671a31a-9088-4031-b46c-f6c0fb27ad33")
    public UltrasonicSensor() {
    }

    @objid ("da43f4b5-7392-45fa-aaa1-0636243b388e")
    public double getRangeInInches() {
    	return 0;
    }

    /**
     * Get the result to use in PIDController
     * @return the result to use in PIDController
     */
    @objid ("b343bf84-9bcd-43e5-bf15-329615ef6859")
    public double pidGet() {
    	return 0;
    }

    /**
     * This method is responsible for reporting the distance in inches to the smart dashboard.
     */
    @objid ("898baa49-95e8-40a6-881a-2aae5fcdef3c")
    public void process() {
    }

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		// TODO Auto-generated method stub
		return null;
	}

}
