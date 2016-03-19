package com.PhantomMentalists.Stronghold;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Vector;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;
import com.ni.vision.NIVision.RawData;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.AxisCamera;
import edu.wpi.first.wpilibj.vision.AxisCamera.Resolution;
import edu.wpi.first.wpilibj.vision.AxisCamera.Rotation;
import edu.wpi.first.wpilibj.vision.AxisCamera.WhiteBalance;

public class Camera {
	// Structure to represent the scores for the various tests used for target
	// identification
	public class Scores {
		double Area;
		double Aspect;
	};

	// Images
	Image frame;
	Image binaryFrame;
	int imaqError;

	// Constants
	NIVision.Range HUE_RANGE = new NIVision.Range(80, 135); // Default hue range
															// for yellow tote
	NIVision.Range SAT_RANGE = new NIVision.Range(30, 175); // Default
															// saturation range
															// for yellow tote
	NIVision.Range L_RANGE = new NIVision.Range(30, 150); // Default value range
															// for yellow tote
	double AREA_MINIMUM = 0.05; // Default Area minimum for particle as a
								// percentage of total image area
	double UPPER_LONG_RATIO = 1.8; // Tote long side = 26.9 / Tote height = 12.1
									// = 2.22
	double LOWER_LONG_RATIO = 1.33;
	double SHORT_RATIO = 1.4; // Tote short side = 16.9 / Tote height = 12.1 =
								// 1.4
	// double RATIO_DEAD = 0.1;
	double SCORE_MIN = 75.0; // Minimum score to be considered a tote
	double VIEW_ANGLE = 64; // View angle for camera, set to Axis m1011 by
							// default, 64 for m1013, 51.7 for 206, 52 for
							// HD3000 square, 60 for HD3000 640x480
	NIVision.ParticleFilterCriteria2 criteria[] = new NIVision.ParticleFilterCriteria2[1];
	NIVision.ParticleFilterOptions2 filterOptions = new NIVision.ParticleFilterOptions2(
			0, 0, 1, 1);
	Vector<ParticleReport> particles = new Vector<ParticleReport>();
	ParticleReport largest;
	ParticleReport smallest;
	ParticleReport best;
	double avglarg = 0;
	int accepted = 0;
	Scores scores = new Scores();
	AxisCamera cam;
	RawData table;
	Preferences prefs;
	File file = new File("/home/lvuser/test.csv");
	PrintWriter print;
	// Servo servox = new Servo(0);
	Servo servoy = new Servo(0);
	double posy = 0.25;
	double posx = 0.45;
	boolean followMode = false;
	boolean followInit = false;

	double distanceToTarget;

	public Camera(String loc) {
		// servox.set(posx);
		// servoy.set(posy);
		camInit();
		cam.writeRotation(Rotation.k0);
		// create images
		frame = NIVision.imaqCreateImage(ImageType.IMAGE_RGB, 0);
		binaryFrame = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);
		criteria[0] = new NIVision.ParticleFilterCriteria2(
				NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA, AREA_MINIMUM,
				100.0, 0, 0);
		table = new NIVision.RawData();
		prefs = Preferences.getInstance();
		prefs.putInt("Hue min", HUE_RANGE.minValue);
		prefs.putInt("Hue max", HUE_RANGE.maxValue);
		prefs.putInt("Sat min", SAT_RANGE.minValue);
		prefs.putInt("Sat max", SAT_RANGE.maxValue);
		prefs.putInt("Val min", L_RANGE.minValue);
		prefs.putInt("Val max", L_RANGE.maxValue);

		try {
			if (!file.exists()) {
				file.createNewFile();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void camInit() {
		cam = new AxisCamera(Parameters.kCameraIpAddress);
		cam.writeCompression(10);
		cam.writeBrightness(50);
		cam.writeResolution(Resolution.k320x240);
		cam.writeExposurePriority(0);
		cam.writeWhiteBalance(WhiteBalance.kFixedIndoor);
	}

	public double getCameraAngle() {
		return ((1 - servoy.get()) * 69 / 0.382);

	}

	// public double getDistanceToTarget()
	// {
	// return distanceToTarget;
	// }

	public void process() {
		SmartDashboard.putNumber("Servo y Pos", servoy.get());
		SmartDashboard.putNumber("Servo angle",
				((1 - servoy.get()) * 69 / 0.382));

		// distanceToTarget = Math.tan(cameraAngle) * Parameters.kGoalHeight;

		// if(Math.abs(servoy.get()-posy) < 0.005)
		// {
		// servoy.
		// }
		// else
		// {
		// servoy.set(posy);
		// }
		// if(servoy.get() < 0.25 && cam.getRotation() == Rotation.k0)
		// {
		// cam.writeRotation(Rotation.k180);
		// }
		// else if(servoy.get() > 0.25 && cam.getRotation() == Rotation.k180)
		// {
		// cam.writeRotation(Rotation.k0);
		// }
	}

	public void setCam(double posx, double posy) {
		if (!followMode) {
			// this.posx = posx;
			// servox.set(this.posx);
			if (this.posy != posy) {
				this.posy = posy;
				servoy.set(this.posy);
			}
		} else {
			// servox.set(this.posx);
			servoy.set(this.posy);
		}
	}

	public void setKeepTarget(boolean follow) {
		// System.out.println("Keep target");
		followMode = follow;
		if (follow && !followInit) {
			followInit = true;
//			centerTarget();
		} else if (!follow) {
			followInit = false;
		}
	}

	public void getImage() {
		long start = System.nanoTime();
		// read file in from disk. For this example to run you need to copy
		// image.jpg from the SampleImages folder to the
		// directory shown below using FTP or SFTP:
		// http://wpilib.screenstepslive.com/s/4485/m/24166/l/282299-roborio-ftp
		// NIVision.imaqReadFile(frame, "/testImage.jpg");
		// cam.writeBrightness(50);
		try {
			print = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// print.println("First,Second,Third");
		avglarg = 0;
		accepted = 0;
		particles = new Vector<ParticleReport>();
		largest = null;
		for (int i = 0; i < 3; i++) {
			// System.out.println();
			// System.out.println("Sample: "+(i+1));
			long ims = System.nanoTime();
			cam.getImage(frame);
			// System.out.println("Image Capture Time: "+(System.nanoTime()-ims));
			NIVision.imaqWriteJPEGFile(frame, "/testing.jpg", 400, table);
			// System.out.println("1");
			// SmartDashboard.putNumber("Tote hue min",
			// TOTE_HUE_RANGE.minValue);
			// SmartDashboard.putNumber("Tote hue max",
			// TOTE_HUE_RANGE.maxValue);
			// SmartDashboard.putNumber("Tote sat min",
			// TOTE_SAT_RANGE.minValue);
			// SmartDashboard.putNumber("Tote sat max",
			// TOTE_SAT_RANGE.maxValue);
			// SmartDashboard.putNumber("Tote val min",
			// TOTE_VAL_RANGE.minValue);
			// SmartDashboard.putNumber("Tote val max",
			// TOTE_VAL_RANGE.maxValue);
			// Update threshold values from SmartDashboard. For performance
			// reasons it is recommended to remove this after calibration is
			// finished.
			// HUE_RANGE.minValue = (int)prefs.getInt("Hue min",
			// HUE_RANGE.minValue);
			// HUE_RANGE.maxValue = (int)prefs.getInt("Hue max",
			// HUE_RANGE.maxValue);
			// SAT_RANGE.minValue = (int)prefs.getInt("Sat min",
			// SAT_RANGE.minValue);
			// SAT_RANGE.maxValue = (int)prefs.getInt("Sat max",
			// SAT_RANGE.maxValue);
			// L_RANGE.minValue = (int)prefs.getInt("Val min",
			// L_RANGE.minValue);
			// L_RANGE.maxValue = (int)prefs.getInt("Val max",
			// L_RANGE.maxValue);
			// System.out.println(HUE_RANGE.minValue);
			// System.out.println(HUE_RANGE.maxValue);
			// System.out.println(SAT_RANGE.minValue);
			// System.out.println(SAT_RANGE.maxValue);
			// System.out.println(L_RANGE.minValue);
			// System.out.println(L_RANGE.maxValue);

			// Threshold the image looking for yellow (tote color)
			long maskings = System.nanoTime();
			NIVision.imaqColorThreshold(binaryFrame, frame, 255,
					NIVision.ColorMode.HSL, HUE_RANGE, SAT_RANGE, L_RANGE);
			// System.out.println("Image Masking Time: "+(System.nanoTime()-maskings));
			// System.out.println("2");

			// Send particle count to dashboard
			int numParticles = NIVision.imaqCountParticles(binaryFrame, 1);
			SmartDashboard.putNumber("Masked particles", numParticles);

			// Send masked image to dashboard to assist in tweaking mask.
			// CameraServer.getInstance().setImage(binaryFrame);
			// System.out.println("3");

			// filter out small particles
			// float areaMin = (float)SmartDashboard.getNumber("Area min %",
			// AREA_MINIMUM);
			long partfilts = System.nanoTime();
			criteria[0].lower = (float) AREA_MINIMUM;
			imaqError = NIVision.imaqParticleFilter4(binaryFrame, binaryFrame,
					criteria, filterOptions, null);
			// NIVision.imaq
			// System.out.println("Particle Filter Time: "+(System.nanoTime()-partfilts));
			// System.out.println("4");

			// Send particle count after filtering to dashboard
			numParticles = NIVision.imaqCountParticles(binaryFrame, 1);
			SmartDashboard.putNumber("Filtered particles", numParticles);

			if (numParticles > 0) {
				// Measure particles and sort by particle size
				long targets = System.nanoTime();
				// System.out.println("Total Targets found: "+numParticles);
				int lareaindex = 0;
				for (int particleIndex = 0; particleIndex < numParticles; particleIndex++) {
					ParticleReport par = new ParticleReport();
					// par.
					// par.Area = NIVision.imaqMeasureParticle(binaryFrame,
					// particleIndex, 0, NIVision.MeasurementType.MT_AREA);
					// NIVision.imaq

					par.BoundingRectTop = NIVision.imaqMeasureParticle(
							binaryFrame, particleIndex, 0,
							NIVision.MeasurementType.MT_BOUNDING_RECT_TOP);
					par.BoundingRectLeft = NIVision.imaqMeasureParticle(
							binaryFrame, particleIndex, 0,
							NIVision.MeasurementType.MT_BOUNDING_RECT_LEFT);
					par.BoundingRectBottom = NIVision.imaqMeasureParticle(
							binaryFrame, particleIndex, 0,
							NIVision.MeasurementType.MT_BOUNDING_RECT_BOTTOM);
					par.BoundingRectRight = NIVision.imaqMeasureParticle(
							binaryFrame, particleIndex, 0,
							NIVision.MeasurementType.MT_BOUNDING_RECT_RIGHT);

					par.Area = (par.BoundingRectRight - par.BoundingRectLeft)
							* (par.BoundingRectBottom - par.BoundingRectTop);
					par.PercentAreaToImageArea = par.Area / (320 * 240);
					par.length = par.BoundingRectRight - par.BoundingRectLeft;
					par.height = par.BoundingRectBottom - par.BoundingRectTop;
					System.out.println("Percent: " + par.PercentAreaToImageArea
							+ "\nArea: " + par.Area);
					double len = par.BoundingRectRight - par.BoundingRectLeft;
					double height = par.BoundingRectBottom
							- par.BoundingRectTop;
					double ratio = len / height;

					ArrayList<Boolean> filter = new ArrayList<Boolean>();
					filter.add(ratio <= (UPPER_LONG_RATIO));
					filter.add(ratio >= (LOWER_LONG_RATIO));
					filter.add(len >= 20);
					filter.add(height >= 12);
					filter.add(len <= 150);
					filter.add(height <= 85);
					filter.add(par.Area < 14000);
					filter.add(par.Area > 400);
					if (!filter.contains(false)) {
						// particles.add(par);
						// System.out.println();
						System.out.println("(Filter Accepted)");
						// System.out.println();
						if (largest != null) {
							if (par.Area > largest.Area) {
								largest = par;
							} else if (smallest != null) {
								if (par.Area < smallest.Area) {
									smallest = par;
								}
							} else {
								smallest = par;
							}
						} else {
							// System.out.println("Largest null");
							largest = par;
						}
						particles.add(par);
					} else {
						System.out.println("(Filter Rejected)");
					}
					// System.out.println("\nTarget,"+particleIndex+
					// "\nTop Left,"+par.BoundingRectLeft+","+par.BoundingRectTop+
					// "\nTop Right,"+par.BoundingRectRight+","+par.BoundingRectTop+
					// "\nBottom Left,"+par.BoundingRectLeft+","+par.BoundingRectBottom+
					// "\nBottom Right,"+par.BoundingRectRight+","+par.BoundingRectBottom+
					// "\nArea,"+par.Area+
					// "\nRatio,"+ratio+"\n");
					// print.println("Top Left,,Top Right,,Bottom Left,,Bottom Right");
					// print.println(par.BoundingRectLeft+","+par.BoundingRectTop+","+par.BoundingRectRight+","+par.BoundingRectTop+","+par.BoundingRectLeft+","+par.BoundingRectBottom+","+par.BoundingRectRight+","+par.BoundingRectBottom);

				}
				// if(largest != null)
				// {
				// avglarg += largest.Area;
				// accepted += 1;
				// double ratio =
				// (largest.BoundingRectRight-largest.BoundingRectLeft)/(largest.BoundingRectBottom-largest.BoundingRectTop);
				// // ParticleReport par = particles.get(lareaindex);
				// System.out.println("(Filter Largest Area) Target: "+" / Top Left       "+largest.BoundingRectLeft+":"+largest.BoundingRectTop+
				// " / Bottom Right   "+largest.BoundingRectRight+":"+largest.BoundingRectBottom
				// + " / Area: "+largest.Area + " / Ratio: "+ratio);
				// }
				particles.sort(null);
				// System.out.println("Find Target Time: "+(System.nanoTime()-targets));
				// This example only scores the largest particle. Extending to
				// score all particles and choosing the desired one is left as
				// an exercise
				// for the reader. Note that this scores and reports information
				// about a single particle (single L shaped target). To get
				// accurate information
				// about the location of the tote (not just the distance) you
				// will need to correlate two adjacent targets in order to find
				// the true center of the tote.
				// scores.Aspect = AspectScore(particles.elementAt(0));
				// SmartDashboard.putNumber("Aspect", scores.Aspect);
				// scores.Area = AreaScore(particles.elementAt(0));
				// SmartDashboard.putNumber("Area", scores.Area);
				// boolean isTote = scores.Aspect > SCORE_MIN && scores.Area >
				// SCORE_MIN;
			}
		}
		if (particles.size() > 2) {
			particles.remove(largest);
			particles.remove(smallest);
		}
		avglarg = 0;
		int num = 0;
		for (ParticleReport par : particles) {
			num++;
			double ratio = (par.BoundingRectRight - par.BoundingRectLeft)
					/ (par.BoundingRectBottom - par.BoundingRectTop);
			avglarg += par.Area;
			// System.out.println("\nTarget,"+num+
			// "\nTop Left,"+par.BoundingRectLeft+","+par.BoundingRectTop+
			// "\nTop Right,"+par.BoundingRectRight+","+par.BoundingRectTop+
			// "\nBottom Left,"+par.BoundingRectLeft+","+par.BoundingRectBottom+
			// "\nBottom Right,"+par.BoundingRectRight+","+par.BoundingRectBottom+
			// "\nArea,"+par.Area+
			// "\nRatio,"+ratio+"\n");
		}
		ArrayList<Integer> dup = new ArrayList<Integer>();
		for (int x = 0; x < particles.size(); x++) {
			dup.add(0);
		}
		for (ParticleReport par : particles) {
			for (ParticleReport pars : particles) {
				int dif = par.compareTo(pars);
				if (dif > -100 && dif < 100) {
					int index = particles.indexOf(par);
					dup.set(index, dup.get(index) + 1);
				}
				// System.out.println(par.compareTo(pars));
			}
		}
		if (dup.size() != 0) {
			int highest = 0;
			for (int y = 0; y < dup.size(); y++) {
				if (dup.get(y) > highest) {
					highest = dup.get(y);
				}
			}
			ParticleReport par = particles.get(dup.indexOf(highest));
			best = par;
			// System.out.println("\nTarget: Best,"+
			// "\nTop Left,"+par.BoundingRectLeft+","+par.BoundingRectTop+
			// "\nTop Right,"+par.BoundingRectRight+","+par.BoundingRectTop+
			// "\nBottom Left,"+par.BoundingRectLeft+","+par.BoundingRectBottom+
			// "\nBottom Right,"+par.BoundingRectRight+","+par.BoundingRectBottom+
			// "\nArea,"+par.Area+
			// /*"\nRatio,"+ratio+*/"\n");
		}
		avglarg = avglarg / particles.size();
		// System.out.println("Avg Largest Area: "+avglarg);
		long end = System.nanoTime() - start;
		// System.out.println("Total time: "+end);
		// print.close();
	}

	// public boolean driveAimStop()
	// {
	// getImage();
	// if (particles.get(0).Area >
	// }

	// public double getXOffCenter()
	// {
	// return servox.get()-0.5;
	// }

	public void centerTarget(double gyroAngle) {
		if (best != null) {
			
			double difx = 0;
			double dify = 0;
			double midxp = best.BoundingRectLeft
					+ ((best.BoundingRectRight - best.BoundingRectLeft) / 2);
			System.out.println(midxp);
			double midyp = best.BoundingRectTop
					+ ((best.BoundingRectBottom - best.BoundingRectTop) / 2);
			difx = midxp * (64 / 320);
			difx = (-difx) + 32;

			// difx = (320/2)-midxp;
			dify = (240 / 2) - midyp;
			// difx /= 320;
			dify /= 240;
			// System.out.println("Posx: "+posx + "\tPosy: "+posy);
			// System.out.println("X diff: "+difx +"\tY diff: "+dify);

			posx = difx;
			System.out.println("Angle Diff: " + posx);
			
			System.out.println("Gyro angle" + gyroAngle);
			
			posx = getDiffAngleX();
			posx += gyroAngle;
			
			posy -= dify * .25;			
			servoy.set(posy);
			// System.out.println("Posx: "+posx + "\tPosy: "+posy);
		}
		// else
		// {
		// System.out.println("No best target");
		// }
	}

	public double getDistanceToTarget() {
		double val = 0;
		double rads = Math.toRadians(getCameraAngle());
		val = (Parameters.kGoalHeight / Math.tan(rads));
		System.out.println("Get distance to target" + val);
		return val;
	}

	public double getDiffAngleX() {
		double angle = Math.atan((Parameters.kDistCamToShooterX)
				/ getDistanceToTarget());
		angle = Math.toDegrees(angle);
		System.out.println("Raw diff angle x" + angle);
		System.out.println("This should match target angle"+(posx + angle));
		return posx + angle;
	}
	
	public double getAngleToMoveFromCamera()
	{
		return posx;
	}

	public void changeBrightness(int bright) {
		if (cam.getBrightness() + bright > 100) {
			cam.writeBrightness(100);
		} else if (cam.getBrightness() + bright < 0) {
			cam.writeBrightness(0);
		} else {
			cam.writeBrightness(cam.getBrightness() + bright);
		}
	}

	private boolean isSimilar(ParticleReport par, ParticleReport par2) {
		// Rectangle rect = new Rectangle(par.BoundingRectLeft,par.)
		return false;
	}

	// A structure to hold measurements of a particle
	public class ParticleReport implements Comparator<ParticleReport>,
			Comparable<ParticleReport> {
		double PercentAreaToImageArea;
		double Area;
		double BoundingRectLeft;
		double BoundingRectTop;
		double BoundingRectRight;
		double BoundingRectBottom;
		double length;
		double height;

		public int compareTo(ParticleReport r) {
			return (int) (r.Area - this.Area);
		}

		public int compare(ParticleReport r1, ParticleReport r2) {
			return (int) (r1.Area - r2.Area);
		}
	};

	// Comparator function for sorting particles. Returns true if particle 1 is
	// larger
	static boolean CompareParticleSizes(ParticleReport particle1,
			ParticleReport particle2) {
		// we want descending sort order
		return particle1.PercentAreaToImageArea > particle2.PercentAreaToImageArea;
	}

	/**
	 * Converts a ratio with ideal value of 1 to a score. The resulting function
	 * is piecewise linear going from (0,0) to (1,100) to (2,0) and is 0 for all
	 * inputs outside the range 0-2
	 */
	double ratioToScore(double ratio) {
		return (Math.max(0, Math.min(100 * (1 - Math.abs(1 - ratio)), 100)));
	}

	double AreaScore(ParticleReport report) {
		double boundingArea = (report.BoundingRectBottom - report.BoundingRectTop)
				* (report.BoundingRectRight - report.BoundingRectLeft);
		// Tape is 7" edge so 49" bounding rect. With 2" wide tape it covers 24"
		// of the rect.
		return ratioToScore((49 / 24) * report.Area / boundingArea);
	}

	/**
	 * Method to score if the aspect ratio of the particle appears to match the
	 * retro-reflective target. Target is 7"x7" so aspect should be 1
	 */
	double AspectScore(ParticleReport report) {
		return ratioToScore(((report.BoundingRectRight - report.BoundingRectLeft) / (report.BoundingRectBottom - report.BoundingRectTop)));
	}

	/**
	 * Computes the estimated distance to a target using the width of the
	 * particle in the image. For more information and graphics showing the math
	 * behind this approach see the Vision Processing section of the
	 * ScreenStepsLive documentation.
	 *
	 * @param image
	 *            The image to use for measuring the particle estimated
	 *            rectangle
	 * @param report
	 *            The Particle Analysis Report for the particle
	 * @param isLong
	 *            Boolean indicating if the target is believed to be the long
	 *            side of a tote
	 * @return The estimated distance to the target in feet.
	 */
	double computeDistance(Image image, ParticleReport report) {
		double normalizedWidth, targetWidth;
		NIVision.GetImageSizeResult size;

		size = NIVision.imaqGetImageSize(image);
		normalizedWidth = 2
				* (report.BoundingRectRight - report.BoundingRectLeft)
				/ size.width;
		targetWidth = 7;

		return targetWidth
				/ (normalizedWidth * 12 * Math.tan(VIEW_ANGLE * Math.PI
						/ (180 * 2)));
	}
}
