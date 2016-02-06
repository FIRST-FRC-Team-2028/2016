/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.command;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * This class is designed to handle the case where there is a {@link Subsystem}
 * which uses a single {@link PIDController} almost constantly (for instance,
 * an elevator which attempts to stay at a constant height).
 * 
 * <p>It provides some convenience methods to run an internal {@link PIDController}.
 * It also allows access to the internal {@link PIDController} in order to give total control
 * to the programmer.</p>
 * 
 * @author Joe Grinstead
 */
@objid ("e621bc4c-1c96-4a3f-ad19-26e57243fa5c")
public abstract class PIDSubsystem extends Subsystem implements Sendable {
    /**
     * The internal {@link PIDController}
     */
    @objid ("0abc99ad-6cdd-4efd-ab35-bd6abd050c3f")
    private PIDController controller;

    /**
     * An output which calls {@link PIDCommand#usePIDOutput(double)}
     */
    @objid ("a21c26fa-0e15-4143-af5c-7e84aabf6269")
    private PIDOutput output = new PIDOutput() {
        public void pidWrite(double output) {
            usePIDOutput(output);
        }
    };

    /**
     * A source which calls {@link PIDCommand#returnPIDInput()}
     */
    @objid ("83e726a4-daa0-4eb7-b939-de6a14406c25")
    private PIDSource source = new PIDSource() {
        public double pidGet() {
            return returnPIDInput();
        }
    };

    /**
     * Instantiates a {@link PIDSubsystem} that will use the given p, i and d values.
     * @param name the name
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     */
    @objid ("1b601852-3cde-4084-ac7f-94b8a534d782")
    public PIDSubsystem(String name, double p, double i, double d) {
        super(name);
        controller = new PIDController(p, i, d, source, output);
    }

    /**
     * Instantiates a {@link PIDSubsystem} that will use the given p, i and d values.
     * @param name the name
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     * @param f the feed forward value
     */
    @objid ("0f48ff69-9a48-44e0-8979-3f4e2cb2b75d")
    public PIDSubsystem(String name, double p, double i, double d, double f) {
        super(name);
        controller = new PIDController(p, i, d, f, source, output);
    }

    /**
     * Instantiates a {@link PIDSubsystem} that will use the given p, i and d values.  It will also space the time
     * between PID loop calculations to be equal to the given period.
     * @param name the name
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     * @param period the time (in seconds) between calculations
     */
    @objid ("8b4ef1b0-0543-40a7-bab0-8889abec50b1")
    public PIDSubsystem(String name, double p, double i, double d, double f, double period) {
        super(name);
        controller = new PIDController(p, i, d, f, source, output, period);
    }

    /**
     * Instantiates a {@link PIDSubsystem} that will use the given p, i and d values.
     * It will use the class name as its name.
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     */
    @objid ("82cc2d8e-10f7-4114-a562-fd8b46d11d46")
    public PIDSubsystem(double p, double i, double d) {
        controller = new PIDController(p, i, d, source, output);
    }

    /**
     * Instantiates a {@link PIDSubsystem} that will use the given p, i and d values.
     * It will use the class name as its name.
     * It will also space the time
     * between PID loop calculations to be equal to the given period.
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     * @param period the time (in seconds) between calculations
     * @param f the feed forward coefficient
     */
    @objid ("e4355902-0ebd-4997-9a12-b5c14e8d3578")
    public PIDSubsystem(double p, double i, double d, double period, double f) {
        controller = new PIDController(p, i, d, f, source, output, period);
    }

    /**
     * Instantiates a {@link PIDSubsystem} that will use the given p, i and d values.
     * It will use the class name as its name.
     * It will also space the time
     * between PID loop calculations to be equal to the given period.
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     * @param period the time (in seconds) between calculations
     */
    @objid ("37ebe248-0ae1-4eca-9b3d-5dbf03802d81")
    public PIDSubsystem(double p, double i, double d, double period) {
        controller = new PIDController(p, i, d, source, output, period);
    }

    /**
     * Returns the {@link PIDController} used by this {@link PIDSubsystem}.
     * Use this if you would like to fine tune the pid loop.
     * @return the {@link PIDController} used by this {@link PIDSubsystem}
     */
    @objid ("6f21cee1-ff90-43d2-8800-46fa06486d02")
    public PIDController getPIDController() {
        return controller;
    }

    /**
     * Adds the given value to the setpoint.
     * If {@link PIDSubsystem#setInputRange(double, double) setInputRange(...)} was used,
     * then the bounds will still be honored by this method.
     * @param deltaSetpoint the change in the setpoint
     */
    @objid ("7e6fa4f3-326e-4303-b364-346fe05c8059")
    public void setSetpointRelative(double deltaSetpoint) {
        setSetpoint(getPosition() + deltaSetpoint);
    }

    /**
     * Sets the setpoint to the given value.  If {@link PIDSubsystem#setInputRange(double, double) setInputRange(...)}
     * was called,
     * then the given setpoint
     * will be trimmed to fit within the range.
     * @param setpoint the new setpoint
     */
    @objid ("ac68061f-6d95-4046-b9f9-6f01c614575f")
    public void setSetpoint(double setpoint) {
        controller.setSetpoint(setpoint);
    }

    /**
     * Returns the setpoint.
     * @return the setpoint
     */
    @objid ("41113e41-1ee9-4b3b-b6a4-5037570fa36e")
    public double getSetpoint() {
        return controller.getSetpoint();
    }

    /**
     * Returns the current position
     * @return the current position
     */
    @objid ("09d3300c-d437-45d0-8024-d2984d830c4c")
    public double getPosition() {
        return returnPIDInput();
    }

    /**
     * Sets the maximum and minimum values expected from the input.
     * @param minimumInput the minimum value expected from the input
     * @param maximumInput the maximum value expected from the output
     */
    @objid ("9de3cdcc-7b0c-466f-9106-abce3554fd70")
    public void setInputRange(double minimumInput, double maximumInput) {
        controller.setInputRange(minimumInput, maximumInput);
    }

    /**
     * Sets the maximum and minimum values to write.
     * @param minimumOutput the minimum value to write to the output
     * @param maximumOutput the maximum value to write to the output
     */
    @objid ("c72cedfa-b5b6-42fe-9ae4-8c15a7b9f395")
    public void setOutputRange(double minimumOutput, double maximumOutput) {
        controller.setOutputRange(minimumOutput, maximumOutput);
    }

    /**
     * Set the absolute error which is considered tolerable for use with
     * OnTarget. The value is in the same range as the PIDInput values.
     * @param t the absolute tolerance
     */
    @objid ("95816369-7059-49d3-8ca6-b1b9beea7a6b")
    public void setAbsoluteTolerance(double t) {
        controller.setAbsoluteTolerance(t);
    }

    /**
     * Set the percentage error which is considered tolerable for use with
     * OnTarget. (Value of 15.0 == 15 percent)
     * @param p the percent tolerance
     */
    @objid ("bcc80cda-cd72-4338-837f-1081bd0c44a2")
    public void setPercentTolerance(double p) {
        controller.setPercentTolerance(p);
    }

    /**
     * Return true if the error is within the percentage of the total input range,
     * determined by setTolerance. This assumes that the maximum and minimum input
     * were set using setInput.
     * @return true if the error is less than the tolerance
     */
    @objid ("7969c30b-608f-4dbe-8c2b-d1edc648fa9f")
    public boolean onTarget() {
        return controller.onTarget();
    }

    /**
     * Returns the input for the pid loop.
     * 
     * <p>It returns the input for the pid loop, so if this Subsystem was based
     * off of a gyro, then it should return the angle of the gyro</p>
     * 
     * <p>All subclasses of {@link PIDSubsystem} must override this method.</p>
     * @return the value the pid loop should use as input
     */
    @objid ("5d1aec78-90be-47db-a682-1c7b80e170f3")
    protected abstract double returnPIDInput();

    /**
     * Uses the value that the pid loop calculated.  The calculated value is the "output" parameter.
     * This method is a good time to set motor values, maybe something along the lines of <code>driveline.tankDrive(output, -output)</code>
     * 
     * <p>All subclasses of {@link PIDSubsystem} must override this method.</p>
     * @param output the value the pid loop calculated
     */
    @objid ("43e4c211-ee57-4d26-99b5-45da3757bfa8")
    protected abstract void usePIDOutput(double output);

    /**
     * Enables the internal {@link PIDController}
     */
    @objid ("95286f35-e501-4640-bfac-c5ea03c3f8f9")
    public void enable() {
        controller.enable();
    }

    /**
     * Disables the internal {@link PIDController}
     */
    @objid ("1a8c49fb-ce88-4903-a1b5-5c55631a6ab4")
    public void disable() {
        controller.disable();
    }

    @objid ("c4f0f143-0069-48f4-b9e5-6af93860d63c")
    public String getSmartDashboardType() {
        return "PIDSubsystem";
    }

    @objid ("775b5302-67b8-4dd7-91aa-e9a3c652c25d")
    public void initTable(ITable table) {
        controller.initTable(table);
        super.initTable(table);
    }

}
