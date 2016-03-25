package com.PhantomMentalists.Stronghold;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class FlashLight 
{
	CANTalon light;
	public FlashLight()
	{
		light = new CANTalon(Parameters.kFlashCANId);
		light.changeControlMode(TalonControlMode.Voltage);
		light.enable();
	}
	
	public void turnOn()
	{
		light.set(5);
	}
	
	public void turnOff()
	{
		light.set(0);
	}
}
