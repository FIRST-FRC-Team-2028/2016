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
 * This class defines a {@link Command} which interacts heavily with a PID loop.
 * 
 * <p>It provides some convenience methods to run an internal {@link PIDController}.
 * It will also start and stop said {@link PIDController} when the {@link PIDCommand} is
 * first initialized and ended/interrupted.</p>
 * 
 * @author Joe Grinstead
 */
@objid ("e9bb43be-1274-4cdf-96c6-4cfbe710cee6")
public abstract class PIDCommand extends Command implements Sendable {
    /**
     * The internal {@link PIDController}
     */
    @objid ("88b18e90-bb60-48c0-be80-9d717b971c4d")
    private PIDController controller;

    /**
     * An output which calls {@link PIDCommand#usePIDOutput(double)}
     */
    @objid ("47ab2c74-4546-430d-ad2d-e9b802ea785e")
    private PIDOutput output = new PIDOutput() {
        public void pidWrite(double output) {
            usePIDOutput(output);
        }
    };

    /**
     * A source which calls {@link PIDCommand#returnPIDInput()}
     */
    @objid ("2dae19c5-1e52-42bb-9d31-7682bd872016")
    private PIDSource source = new PIDSource() {
        public double pidGet() {
            return returnPIDInput();
        }
    };

    /**
     * Instantiates a {@link PIDCommand} that will use the given p, i and d values.
     * @param name the name of the command
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     */
    @objid ("7c4edf48-9f41-42a7-8de3-a978fc300567")
    public PIDCommand(String name, double p, double i, double d) {
        super(name);
        controller = new PIDController(p, i, d, source, output);
    }

    /**
     * Instantiates a {@link PIDCommand} that will use the given p, i and d values.  It will also space the time
     * between PID loop calculations to be equal to the given period.
     * @param name the name
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     * @param period the time (in seconds) between calculations
     */
    @objid ("bcc0eab2-bc69-4927-8952-41f706ab639d")
    public PIDCommand(String name, double p, double i, double d, double period) {
        super(name);
        controller = new PIDController(p, i, d, source, output, period);
    }

    /**
     * Instantiates a {@link PIDCommand} that will use the given p, i and d values.
     * It will use the class name as its name.
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     */
    @objid ("9a88b3b2-dfcb-4f24-9baf-384cafa9f807")
    public PIDCommand(double p, double i, double d) {
        controller = new PIDController(p, i, d, source, output);
    }

    /**
     * Instantiates a {@link PIDCommand} that will use the given p, i and d values.
     * It will use the class name as its name..
     * It will also space the time
     * between PID loop calculations to be equal to the given period.
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     * @param period the time (in seconds) between calculations
     */
    @objid ("869c8a4a-d14e-452a-addb-5065c2e743c1")
    public PIDCommand(double p, double i, double d, double period) {
        controller = new PIDController(p, i, d, source, output, period);
    }

    /**
     * Returns the {@link PIDController} used by this {@link PIDCommand}.
     * Use this if you would like to fine tune the pid loop.
     * @return the {@link PIDController} used by this {@link PIDCommand}
     */
    @objid ("bd4693ef-3496-4785-abcc-ed6f8d03ad84")
    protected PIDController getPIDController() {
        return controller;
    }

    @objid ("69e7f14d-4169-421f-931f-21a833f3ccf3")
    void _initialize() {
        controller.enable();
    }

    @objid ("f8cd55af-5e6f-420a-9b9f-9bdc64e4f2cf")
    void _end() {
        controller.disable();
    }

    @objid ("4a745e53-6dd3-4db4-9bc2-651a88254e90")
    void _interrupted() {
        _end();
    }

    /**
     * Adds the given value to the setpoint.
     * If {@link PIDCommand#setInputRange(double, double) setInputRange(...)} was used,
     * then the bounds will still be honored by this method.
     * @param deltaSetpoint the change in the setpoint
     */
    @objid ("4bf2d1f4-ea2d-4463-8032-9928cf01e21b")
    public void setSetpointRelative(double deltaSetpoint) {
        setSetpoint(getSetpoint() + deltaSetpoint);
    }

    /**
     * Sets the setpoint to the given value.  If {@link PIDCommand#setInputRange(double, double) setInputRange(...)}
     * was called,
     * then the given setpoint
     * will be trimmed to fit within the range.
     * @param setpoint the new setpoint
     */
    @objid ("151f6d27-df88-4e40-ae9b-f58eaf0d54d3")
    protected void setSetpoint(double setpoint) {
        controller.setSetpoint(setpoint);
    }

    /**
     * Returns the setpoint.
     * @return the setpoint
     */
    @objid ("504cbf46-54aa-4b2b-87ff-bf9f549cb4da")
    protected double getSetpoint() {
        return controller.getSetpoint();
    }

    /**
     * Returns the current position
     * @return the current position
     */
    @objid ("da325626-d043-4256-903a-c6028b30adb3")
    protected double getPosition() {
        return returnPIDInput();
    }

    /**
     * Sets the maximum and minimum values expected from the input and setpoint.
     * @param minimumInput the minimum value expected from the input and setpoint
     * @param maximumInput the maximum value expected from the input and setpoint
     */
    @objid ("038f3631-9921-4f74-9015-7732cd97e6ea")
    protected void setInputRange(double minimumInput, double maximumInput) {
        controller.setInputRange(minimumInput, maximumInput);
    }

    /**
     * Returns the input for the pid loop.
     * 
     * <p>It returns the input for the pid loop, so if this command was based
     * off of a gyro, then it should return the angle of the gyro</p>
     * 
     * <p>All subclasses of {@link PIDCommand} must override this method.</p>
     * 
     * <p>This method will be called in a different thread then the {@link Scheduler} thread.</p>
     * @return the value the pid loop should use as input
     */
    @objid ("8088ff9c-0ff9-4dda-8c87-6524f2dd2140")
    protected abstract double returnPIDInput();

    /**
     * Uses the value that the pid loop calculated.  The calculated value is the "output" parameter.
     * This method is a good time to set motor values, maybe something along the lines of <code>driveline.tankDrive(output, -output)</code>
     * 
     * <p>All subclasses of {@link PIDCommand} must override this method.</p>
     * 
     * <p>This method will be called in a different thread then the {@link Scheduler} thread.</p>
     * @param output the value the pid loop calculated
     */
    @objid ("0f8e90c8-fcd3-4781-833c-4d71d3c9a8f1")
    protected abstract void usePIDOutput(double output);

    @objid ("3e897c0d-26ea-4cfa-82b5-f803e813469a")
    public String getSmartDashboardType() {
        return "PIDCommand";
    }

    @objid ("0e51e6c9-e27d-40e4-8787-37dac9c0a898")
    public void initTable(ITable table) {
        controller.initTable(table);
        super.initTable(table);
    }

}
