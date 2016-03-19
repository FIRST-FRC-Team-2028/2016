package com.PhantomMentalists.Stronghold;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;

public class Gyro extends AnalogGyro
{
	
	public Gyro(int channel) {
		super(channel);
	}

	public double getRelativeAngle()
	{
		double angle = this.getAngle();
		int revs = (int)(angle/360);
		double newAngle = (angle/360)-revs;
		newAngle *= 360;
		if(newAngle < 0)
		{
			newAngle += 360;
		}
		return newAngle;
	}
	
	public double getAbsoluteAngleFromRelative(double relativeAngle)
	{
		double newangle = relativeAngle - getRelativeAngle();
		if(newangle >= 180)
		{
			newangle = -360+newangle;
		}
		else if (newangle <= -180)
		{
			newangle = 360+newangle;
		}
		newangle = getAngle()+newangle;
		return newangle;
	}
	
	
	public double getGyroAngle(double newAngle)
	{

	if(newAngle >= 180)
	{
		newAngle = -360+newAngle;
	}
	else if (newAngle <= -180)
	{
		newAngle = 360+newAngle;
	}
	newAngle = getAngle()+newAngle;
	return newAngle;
	}
}
