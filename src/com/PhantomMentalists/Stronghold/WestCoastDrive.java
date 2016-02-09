package com.PhantomMentalists.Stronghold;

import com.PhantomMentalists.Stronghold.DriveUnit.Gear;
import com.PhantomMentalists.Stronghold.DriveUnit.Placement;
import com.PhantomMentalists.Stronghold.Parameters;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.PIDCommand;

/**
* <p>Author: Zavion</p>
*/
@objid ("bfe07845-b9da-40e9-a047-c801ee86b209")
public class WestCoastDrive implements PIDOutput
{
	
   /**
    * <sets the turn setpoint>
    */
    @objid ("994a7329-7dde-4b3a-af65-88988500092f")
    public double turnSetpoint;
    
   /**
    * its a gear 
    */
    @objid ("28bc5bec-15fd-4924-b01c-5f725341e189")
    protected Gear gear;
   /**
    * left side
    */
    @objid ("b8a106fc-3334-47d0-b500-cf128b91b6c0")
	protected DriveUnit leftSide;
  
   /**
	* Right side
	*/
    @objid ("d0db5703-a6c1-4909-baf4-37eb81a88730")
    protected DriveUnit rightSide;
   /**
    * constructor
    */
    @objid ("06e23f53-7280-4c29-b319-50a651aca613")
    public WestCoastDrive() 
    {
    	  leftSide = new DriveUnit(Placement.Left);
    	  rightSide = new DriveUnit(Placement.Right);
    	  setGear(Gear.kLowGear);
    }

   /**
    * Set the output to the value calculated by PIDController
    * Instantiates a PIDCommand that will use the given p, i and d values. It will use the class name as its name. 
    * @param output the value calculated by PIDController
    */
    @objid ("fc5f1ae8-f7bf-4800-924d-f54e5ee2a661")
    public void pidWrite(double output) 
    {
              
    }
   /**
 	* sets the set point
 	* @param setPoint
 	*/
    @objid ("efa356b3-f44c-44ee-a418-1ba6be974be5")
    public void setSetpoint(double setPoint)
    {
    	leftSide.setSpeedSetpoint(setPoint);
    	rightSide.setSpeedSetpoint(setPoint);	
    }
   /**
 	* gets the set point
 	* @return
 	*/
     @objid ("ea714300-5ea7-4831-acb5-2503084baa47")
    public double getSetpoint()
    {
    	return 0;
    }
    
   /**
    * gets gear and return get value
    * @return 
    */
    @objid ("77501865-67e2-4889-b074-f047bd3616a6")
    public Gear getGear() 
    {
    	 return this.gear;
    }
   /**
    * This set the left and right gear based on the gear value
    * @param value
    */
    @objid ("3e825af2-8a08-4402-9058-dd7ae4aa2260")
    public void setGear(Gear value) 
    {
    	gear = value;
    	leftSide.setGear(gear);
    	rightSide.setGear(gear);
    }

}
