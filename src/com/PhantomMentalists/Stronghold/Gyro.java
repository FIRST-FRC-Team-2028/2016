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
		return newAngle;
	}
}
