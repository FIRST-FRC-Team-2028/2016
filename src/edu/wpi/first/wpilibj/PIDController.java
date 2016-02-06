/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import java.util.TimerTask;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import edu.wpi.first.wpilibj.util.BoundaryException;

/**
 * Class implements a PID Control Loop.
 * 
 * Creates a separate thread which reads the given PIDSource and takes
 * care of the integral calculations, as well as writing the given
 * PIDOutput
 */
@objid ("66276281-5318-4641-b9b6-124f4e81bb21")
public class PIDController implements LiveWindowSendable, Controller {
    @objid ("0d553007-702a-4d65-9c7d-e68f5c9d37c6")
    public static final double kDefaultPeriod = .05;

    @objid ("b4e4939a-8b54-47b3-9052-7c9c67787fe8")
    private static int instances = 0;

    @objid ("1d611aa0-d685-4fdc-b908-d3e64e830cfc")
    private double m_P; // factor for "proportional" control

    @objid ("ec2fb225-6150-4d35-9072-44269d8b22b2")
    private double m_I; // factor for "integral" control

    @objid ("6b0e3efe-f004-4ad3-9eef-d0a23cfa0372")
    private double m_D; // factor for "derivative" control

    @objid ("9169963b-3fd2-4cc2-a502-6615e43036ae")
    private double m_F; // factor for feedforward term

    @objid ("70660e03-3425-4835-b06b-1bd6787aa750")
    private double m_maximumOutput = 1.0; // |maximum output|

    @objid ("4bb604f0-0e10-4dfd-9084-f0a2401255f4")
    private double m_minimumOutput = -1.0; // |minimum output|

    @objid ("369e1e23-5874-4d49-9761-e7543ca6f98e")
    private double m_maximumInput = 0.0; // maximum input - limit setpoint to this

    @objid ("157e8fd3-bb84-43f1-9a02-9e34453cf29e")
    private double m_minimumInput = 0.0; // minimum input - limit setpoint to this

    @objid ("ed867bac-ea1b-46c9-8fd5-cfd56577ab1a")
    private boolean m_continuous = false; // do the endpoints wrap around? eg. Absolute encoder

    @objid ("047b4fb8-8b51-49a7-9aa1-bb3385f95ed2")
    private boolean m_enabled = false; // is the pid controller enabled

    @objid ("d7c508e4-5109-4d05-8907-14fdb5e097dd")
    private double m_prevError = 0.0; // the prior sensor input (used to compute velocity)

    @objid ("90a3aa43-8d10-488a-a46a-bdc2bbd084e2")
    private double m_totalError = 0.0; // the sum of the errors for use in the integral calc

    @objid ("6899ce86-906c-459b-868a-9abe41adaf81")
    private double m_setpoint = 0.0;

    @objid ("bc8f120d-bfd9-4951-8e11-17d001123936")
    private double m_error = 0.0;

    @objid ("21c95fd1-6ea3-488c-be1e-415152042370")
    private double m_result = 0.0;

    @objid ("e43955d2-3b94-4af5-939c-c65f49ff91e8")
    private double m_period = kDefaultPeriod;

    @objid ("b0b36946-9b64-4434-a17e-7ee97765e873")
     java.util.Timer m_controlLoop;

    @objid ("04535fa7-0e27-490f-8b2f-1cc1658843ec")
    private boolean m_freed = false;

    @objid ("a4e8c5e1-3aa1-4ec5-9f72-b966e4ea93c8")
    private boolean m_usingPercentTolerance;

    @objid ("93559282-ba0e-4583-89c5-622cd17b1f39")
    private final ITableListener listener = new ITableListener() {
        @Override
    public void valueChanged(ITable table, String key, Object value, boolean isNew) {
            if (key.equals("p") || key.equals("i") || key.equals("d") || key.equals("f")) {
                if (getP() != table.getNumber("p", 0.0) || getI() != table.getNumber("i", 0.0) ||
                        getD() != table.getNumber("d", 0.0) || getF() != table.getNumber("f", 0.0))
                    setPID(table.getNumber("p", 0.0), table.getNumber("i", 0.0), table.getNumber("d", 0.0), table.getNumber("f", 0.0));
            } else if (key.equals("setpoint")) {
                if (getSetpoint() != ((Double) value).doubleValue())
                    setSetpoint(((Double) value).doubleValue());
            } else if (key.equals("enabled")) {
                if (isEnable() != ((Boolean) value).booleanValue()) {
                    if (((Boolean) value).booleanValue()) {
                        enable();
                    } else {
                        disable();
                    }
                }
            }
        }
    };

    @objid ("a25f284a-1ac0-46fe-a335-e411a3a5d5bd")
    private ITable table;

    @objid ("1044cccc-2df3-425f-aeb5-165097d7c4d1")
    private Tolerance m_tolerance; // the tolerance object used to check if on target

    @objid ("feac7846-682c-46b2-ad4b-9feefe86eae1")
     PIDSource m_pidInput;

    @objid ("c93bf54c-30cc-42b6-aa4e-5b1dd95aaeed")
     PIDOutput m_pidOutput;

    /**
     * Allocate a PID object with the given constants for P, I, D, and F
     * @param Kp the proportional coefficient
     * @param Ki the integral coefficient
     * @param Kd the derivative coefficient
     * @param Kf the feed forward term
     * @param source The PIDSource object that is used to get values
     * @param output The PIDOutput object that is set to the output percentage
     * @param period the loop time for doing calculations. This particularly effects calculations of the
     * integral and differential terms. The default is 50ms.
     */
    @objid ("e3d7d073-89d2-4404-b4be-2cf969cb04ef")
    public PIDController(double Kp, double Ki, double Kd, double Kf, PIDSource source, PIDOutput output, double period) {
        if (source == null) {
            throw new NullPointerException("Null PIDSource was given");
        }
        if (output == null) {
            throw new NullPointerException("Null PIDOutput was given");
        }
        
        m_controlLoop = new java.util.Timer();
        
        
        m_P = Kp;
        m_I = Ki;
        m_D = Kd;
        m_F = Kf;
        
        m_pidInput = source;
        m_pidOutput = output;
        m_period = period;
        
        m_controlLoop.schedule(new PIDTask(this), 0L, (long) (m_period * 1000));
        
        instances++;
        HLUsageReporting.reportPIDController(instances);
        m_tolerance = new NullTolerance();
    }

    /**
     * Allocate a PID object with the given constants for P, I, D and period
     * @param Kp the proportional coefficient
     * @param Ki the integral coefficient
     * @param Kd the derivative coefficient
     * @param source the PIDSource object that is used to get values
     * @param output the PIDOutput object that is set to the output percentage
     * @param period the loop time for doing calculations. This particularly effects calculations of the
     * integral and differential terms. The default is 50ms.
     */
    @objid ("1706e601-abe9-40fd-b0cb-f09c94177e4c")
    public PIDController(double Kp, double Ki, double Kd, PIDSource source, PIDOutput output, double period) {
        this(Kp, Ki, Kd, 0.0, source, output, period);
    }

    /**
     * Allocate a PID object with the given constants for P, I, D, using a 50ms period.
     * @param Kp the proportional coefficient
     * @param Ki the integral coefficient
     * @param Kd the derivative coefficient
     * @param source The PIDSource object that is used to get values
     * @param output The PIDOutput object that is set to the output percentage
     */
    @objid ("4ea34b8a-cb5a-4721-9369-2eee4508b888")
    public PIDController(double Kp, double Ki, double Kd, PIDSource source, PIDOutput output) {
        this(Kp, Ki, Kd, source, output, kDefaultPeriod);
    }

    /**
     * Allocate a PID object with the given constants for P, I, D, using a 50ms period.
     * @param Kp the proportional coefficient
     * @param Ki the integral coefficient
     * @param Kd the derivative coefficient
     * @param Kf the feed forward term
     * @param source The PIDSource object that is used to get values
     * @param output The PIDOutput object that is set to the output percentage
     */
    @objid ("df3ebf65-cdd9-4098-b3ef-2413a2181d39")
    public PIDController(double Kp, double Ki, double Kd, double Kf, PIDSource source, PIDOutput output) {
        this(Kp, Ki, Kd, Kf, source, output, kDefaultPeriod);
    }

    /**
     * Free the PID object
     */
    @objid ("9e841da5-aa3c-46a3-aaf4-6e62428ef2ef")
    public void free() {
        m_controlLoop.cancel();
        synchronized (this) {
          m_freed = true;
          m_pidOutput = null;
          m_pidInput = null;
          m_controlLoop = null;
        }
        if(this.table!=null) table.removeTableListener(listener);
    }

    /**
     * Read the input, calculate the output accordingly, and write to the output.
     * This should only be called by the PIDTask
     * and is created during initialization.
     */
    @objid ("2c0140a1-2dc0-4130-ad73-26b93da1f42b")
    private void calculate() {
        boolean enabled;
        PIDSource pidInput;
        
        synchronized (this) {
            if (m_pidInput == null) {
                return;
            }
            if (m_pidOutput == null) {
                return;
            }
            enabled = m_enabled; // take snapshot of these values...
            pidInput = m_pidInput;
        }
        
        if (enabled) {
          double input;
            double result;
            PIDOutput pidOutput = null;
            synchronized (this){
              input = pidInput.pidGet();
            }
            synchronized (this) {
                m_error = m_setpoint - input;
                if (m_continuous) {
                    if (Math.abs(m_error)
                            > (m_maximumInput - m_minimumInput) / 2) {
                        if (m_error > 0) {
                            m_error = m_error - m_maximumInput + m_minimumInput;
                        } else {
                            m_error = m_error
                                      + m_maximumInput - m_minimumInput;
                        }
                    }
                }
        
                if (m_I != 0) {
                    double potentialIGain = (m_totalError + m_error) * m_I;
                    if (potentialIGain < m_maximumOutput) {
                        if (potentialIGain > m_minimumOutput) {
                            m_totalError += m_error;
                        } else {
                            m_totalError = m_minimumOutput / m_I;
                        }
                    } else {
                        m_totalError = m_maximumOutput / m_I;
                    }
                }
        
                m_result = m_P * m_error + m_I * m_totalError + m_D * (m_error - m_prevError) + m_setpoint * m_F;
                m_prevError = m_error;
        
                if (m_result > m_maximumOutput) {
                    m_result = m_maximumOutput;
                } else if (m_result < m_minimumOutput) {
                    m_result = m_minimumOutput;
                }
                pidOutput = m_pidOutput;
                result = m_result;
            }
        
            pidOutput.pidWrite(result);
        }
    }

    /**
     * Set the PID Controller gain parameters.
     * Set the proportional, integral, and differential coefficients.
     * @param p Proportional coefficient
     * @param i Integral coefficient
     * @param d Differential coefficient
     */
    @objid ("7cfe5175-b761-4b00-a05d-4151a486bebc")
    public synchronized void setPID(double p, double i, double d) {
        m_P = p;
        m_I = i;
        m_D = d;
        
        if (table != null) {
            table.putNumber("p", p);
            table.putNumber("i", i);
            table.putNumber("d", d);
        }
    }

    /**
     * Set the PID Controller gain parameters.
     * Set the proportional, integral, and differential coefficients.
     * @param p Proportional coefficient
     * @param i Integral coefficient
     * @param d Differential coefficient
     * @param f Feed forward coefficient
     */
    @objid ("4490fa3e-9136-4917-8370-c2095a4f2180")
    public synchronized void setPID(double p, double i, double d, double f) {
        m_P = p;
        m_I = i;
        m_D = d;
        m_F = f;
        
        if (table != null) {
            table.putNumber("p", p);
            table.putNumber("i", i);
            table.putNumber("d", d);
            table.putNumber("f", f);
        }
    }

    /**
     * Get the Proportional coefficient
     * @return proportional coefficient
     */
    @objid ("b51cb562-7373-48e1-bb10-858986558a18")
    public synchronized double getP() {
        return m_P;
    }

    /**
     * Get the Integral coefficient
     * @return integral coefficient
     */
    @objid ("91b0390f-1b3b-4414-ae3a-af5fb1e6aae0")
    public synchronized double getI() {
        return m_I;
    }

    /**
     * Get the Differential coefficient
     * @return differential coefficient
     */
    @objid ("7e8ec712-2844-43cb-93d3-d8431152676f")
    public synchronized double getD() {
        return m_D;
    }

    /**
     * Get the Feed forward coefficient
     * @return feed forward coefficient
     */
    @objid ("307e8be8-d2e8-43aa-8310-33049333c627")
    public synchronized double getF() {
        return m_F;
    }

    /**
     * Return the current PID result
     * This is always centered on zero and constrained the the max and min outs
     * @return the latest calculated output
     */
    @objid ("dcee9cae-f312-41aa-85ed-018e10be4ea1")
    public synchronized double get() {
        return m_result;
    }

    /**
     * Set the PID controller to consider the input to be continuous,
     * Rather then using the max and min in as constraints, it considers them to
     * be the same point and automatically calculates the shortest route to
     * the setpoint.
     * @param continuous Set to true turns on continuous, false turns off continuous
     */
    @objid ("39670c28-7450-483c-95f2-c1fb3b2d9c47")
    public synchronized void setContinuous(boolean continuous) {
        m_continuous = continuous;
    }

    /**
     * Set the PID controller to consider the input to be continuous,
     * Rather then using the max and min in as constraints, it considers them to
     * be the same point and automatically calculates the shortest route to
     * the setpoint.
     */
    @objid ("a43dc739-0537-4a4f-9ed0-9703ccb9a04b")
    public synchronized void setContinuous() {
        this.setContinuous(true);
    }

    /**
     * Sets the maximum and minimum values expected from the input and setpoint.
     * @param minimumInput the minimum value expected from the input
     * @param maximumInput the maximum value expected from the input
     */
    @objid ("0538e057-9c01-4693-baba-f628348c0545")
    public synchronized void setInputRange(double minimumInput, double maximumInput) {
        if (minimumInput > maximumInput) {
            throw new BoundaryException("Lower bound is greater than upper bound");
        }
        m_minimumInput = minimumInput;
        m_maximumInput = maximumInput;
        setSetpoint(m_setpoint);
    }

    /**
     * Sets the minimum and maximum values to write.
     * @param minimumOutput the minimum percentage to write to the output
     * @param maximumOutput the maximum percentage to write to the output
     */
    @objid ("7b926588-06bc-42d5-96c3-f248ba1cc96c")
    public synchronized void setOutputRange(double minimumOutput, double maximumOutput) {
        if (minimumOutput > maximumOutput) {
            throw new BoundaryException("Lower bound is greater than upper bound");
        }
        m_minimumOutput = minimumOutput;
        m_maximumOutput = maximumOutput;
    }

    /**
     * Set the setpoint for the PIDController
     * @param setpoint the desired setpoint
     */
    @objid ("23547a8d-3384-486c-a367-c7719bd35fe7")
    public synchronized void setSetpoint(double setpoint) {
        if (m_maximumInput > m_minimumInput) {
            if (setpoint > m_maximumInput) {
                m_setpoint = m_maximumInput;
            } else if (setpoint < m_minimumInput) {
                m_setpoint = m_minimumInput;
            } else {
                m_setpoint = setpoint;
            }
        } else {
            m_setpoint = setpoint;
        }
        
        if (table != null)
            table.putNumber("setpoint", m_setpoint);
    }

    /**
     * Returns the current setpoint of the PIDController
     * @return the current setpoint
     */
    @objid ("e750983b-14dd-4d73-92e9-0941e225eae6")
    public synchronized double getSetpoint() {
        return m_setpoint;
    }

    /**
     * Returns the current difference of the input from the setpoint
     * @return the current error
     */
    @objid ("7d47a370-eab0-4c7e-9fe6-b4978b059658")
    public synchronized double getError() {
        //return m_error;
        return getSetpoint() - m_pidInput.pidGet();
    }

    /**
     * Set the percentage error which is considered tolerable for use with
     * OnTarget. (Input of 15.0 = 15 percent)
     * @param percent error which is tolerable
     * @deprecated Use {@link #setPercentTolerance(double)} or {@link #setAbsoluteTolerance(double)} instead.
     */
    @objid ("72199f1f-6981-44b9-98cb-e91d79e40a74")
    @Deprecated
    public synchronized void setTolerance(double percent) {
        m_tolerance = new PercentageTolerance(percent);
    }

    /**
     * Set the PID tolerance using a Tolerance object.
     * Tolerance can be specified as a percentage of the range or as an absolute
     * value. The Tolerance object encapsulates those options in an object. Use it by
     * creating the type of tolerance that you want to use: setTolerance(new PIDController.AbsoluteTolerance(0.1))
     * @param tolerance a tolerance object of the right type, e.g. PercentTolerance
     * or AbsoluteTolerance
     */
    @objid ("9ebd926d-0732-47e5-a31b-d656e5805402")
    private synchronized void setTolerance(Tolerance tolerance) {
        m_tolerance = tolerance;
    }

    /**
     * Set the absolute error which is considered tolerable for use with
     * OnTarget.
     * @param absvalue absolute error which is tolerable in the units of the input object
     */
    @objid ("72b0cffb-0767-4f74-a8b0-26d2ff9d9691")
    public synchronized void setAbsoluteTolerance(double absvalue) {
        m_tolerance = new AbsoluteTolerance(absvalue);
    }

    /**
     * Set the percentage error which is considered tolerable for use with
     * OnTarget. (Input of 15.0 = 15 percent)
     * @param percentage percent error which is tolerable
     */
    @objid ("b03de9d1-2f0f-4461-a7c9-76eec2c88249")
    public synchronized void setPercentTolerance(double percentage) {
        m_tolerance = new PercentageTolerance(percentage);
    }

    /**
     * Return true if the error is within the percentage of the total input range,
     * determined by setTolerance. This assumes that the maximum and minimum input
     * were set using setInput.
     * @return true if the error is less than the tolerance
     */
    @objid ("003539d1-c194-42e8-909f-5a3393000d01")
    public synchronized boolean onTarget() {
        return m_tolerance.onTarget();
    }

    /**
     * Begin running the PIDController
     */
    @objid ("b2e6e9e3-c464-42e2-923c-245355bc7223")
    @Override
    public synchronized void enable() {
        m_enabled = true;
        
        if (table != null) {
            table.putBoolean("enabled", true);
        }
    }

    /**
     * Stop running the PIDController, this sets the output to zero before stopping.
     */
    @objid ("a026828e-faa3-4ea7-be64-c11bf26ce103")
    @Override
    public synchronized void disable() {
        m_pidOutput.pidWrite(0);
        m_enabled = false;
        
        if (table != null) {
            table.putBoolean("enabled", false);
        }
    }

    /**
     * Return true if PIDController is enabled.
     */
    @objid ("83c8ab7d-53c0-4e81-af01-009bbac1847d")
    public synchronized boolean isEnable() {
        return m_enabled;
    }

    /**
     * Reset the previous error,, the integral term, and disable the controller.
     */
    @objid ("4067b42b-0738-4360-a01b-cd91c8275b4c")
    public synchronized void reset() {
        disable();
        m_prevError = 0;
        m_totalError = 0;
        m_result = 0;
    }

    @objid ("06d1463b-569b-4057-88ed-73109e321e7b")
    @Override
    public String getSmartDashboardType() {
        return "PIDController";
    }

    @objid ("b7a59991-47e5-4c45-8a19-e1623872079d")
    @Override
    public void initTable(ITable table) {
        if(this.table!=null)
            this.table.removeTableListener(listener);
        this.table = table;
        if(table!=null) {
            table.putNumber("p", getP());
            table.putNumber("i", getI());
            table.putNumber("d", getD());
            table.putNumber("f", getF());
            table.putNumber("setpoint", getSetpoint());
            table.putBoolean("enabled", isEnable());
            table.addTableListener(listener, false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @objid ("895a1348-efa5-4a29-8e43-fbdf3ba72939")
    @Override
    public ITable getTable() {
        return table;
    }

    /**
     * {@inheritDoc}
     */
    @objid ("054367a9-267d-47ac-a25e-bfb60f6161b7")
    @Override
    public void updateTable() {
    }

    /**
     * {@inheritDoc}
     */
    @objid ("131abf98-24dc-44e0-bcc0-63a50bea9943")
    @Override
    public void startLiveWindowMode() {
        disable();
    }

    /**
     * {@inheritDoc}
     */
    @objid ("17a613a7-0db1-4669-9b33-52818259d476")
    @Override
    public void stopLiveWindowMode() {
    }

    /**
     * Tolerance is the type of tolerance used to specify if the PID controller is on target.
     * The various implementations of this class such as PercentageTolerance and AbsoluteTolerance
     * specify types of tolerance specifications to use.
     */
    @objid ("d88710fe-c0be-43be-81b1-60b552f6436b")
    public interface Tolerance {
        @objid ("85e0597b-434f-45fa-aa00-6d2545fb22f2")
        boolean onTarget();

    }

    @objid ("c5308f25-cd5f-49ab-baa7-946d326b0727")
    public class PercentageTolerance implements Tolerance {
        @objid ("fc04ff2c-d59d-4eea-9b93-5885b8a4a895")
         double percentage;

        @objid ("f371385e-83d7-4f34-981d-4f28f3e6d1f1")
        PercentageTolerance(double value) {
            percentage = value;
        }

        @objid ("e16116d0-cd7e-4ca5-a081-038b6e3f56ab")
        @Override
        public boolean onTarget() {
            return (Math.abs(getError()) < percentage / 100
                                * (m_maximumInput - m_minimumInput));
        }

    }

    @objid ("a13ede2a-f561-4736-9b52-dbed4b86e412")
    public class AbsoluteTolerance implements Tolerance {
        @objid ("c2d9d153-5547-4a24-a16f-fa39ad017a36")
         double value;

        @objid ("f5b403a7-2249-4c8a-9d89-87013b74e750")
        AbsoluteTolerance(double value) {
            this.value = value;
        }

        @objid ("0e9e996e-fbcb-4e55-810b-1e95668e492d")
        @Override
        public boolean onTarget() {
            return Math.abs(getError()) < value;
        }

    }

    @objid ("bb31f6df-605e-4561-a779-cd059c5c1a72")
    public class NullTolerance implements Tolerance {
        @objid ("2e82f690-3fd5-4830-9b9f-cfa3284f958c")
        @Override
        public boolean onTarget() {
            throw new RuntimeException("No tolerance value set when using PIDController.onTarget()");
        }

    }

    @objid ("c95b7513-b976-4227-95d0-a9a969c94851")
    private class PIDTask extends TimerTask {
        @objid ("467458de-77c6-4a89-8523-7926726fc0ab")
        private PIDController m_controller;

        @objid ("235fadb0-f002-47d4-9b8c-a403c550c06e")
        public PIDTask(PIDController controller) {
            if (controller == null) {
                throw new NullPointerException("Given PIDController was null");
            }
            m_controller = controller;
        }

        @objid ("c7068ae6-414f-4fd9-8a6e-1304fe96c737")
        @Override
        public void run() {
            m_controller.calculate();
        }

    }

}
