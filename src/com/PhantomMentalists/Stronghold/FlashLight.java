package com.PhantomMentalists.Stronghold;

import edu.wpi.first.wpilibj.DigitalOutput;

public class FlashLight 
{
	DigitalOutput light;
	public FlashLight()
	{
		light = new DigitalOutput(Parameters.kFlashCANId);
	}
	
	public void turnOn()
	{
		light.set(true);
	}
	
	public void turnOff()
	{
		light.set(false);
	}
}
