/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.internal;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Utility;

/**
 * Timer objects measure accumulated time in milliseconds.
 * The timer object functions like a stopwatch. It can be started, stopped, and cleared. When the
 * timer is running its value counts up in milliseconds. When stopped, the timer holds the current
 * value. The implementation simply records the time when started and subtracts the current time
 * whenever the value is requested.
 */
@objid ("bc6baf84-59ed-4997-95b6-aeeb52ef0793")
public class HardwareTimer implements edu.wpi.first.wpilibj.Timer.StaticInterface {
    /**
     * Pause the thread for a specified time. Pause the execution of the
     * thread for a specified period of time given in seconds. Motors will
     * continue to run at their last assigned values, and sensors will continue
     * to update. Only the task containing the wait will pause until the wait
     * time is expired.
     * @param seconds Length of time to pause
     */
    @objid ("a02ae862-acdb-4925-81ce-f897221dd7df")
    @Override
    public void delay(final double seconds) {
        try {
            Thread.sleep((long) (seconds * 1e3));
        } catch (final InterruptedException e) {
        }
    }

    /**
     * Return the system clock time in seconds. Return the time from the
     * FPGA hardware clock in seconds since the FPGA started.
     * @return Robot running time in seconds.
     */
    @objid ("ed34b760-2cf9-4eef-ae89-934b1f5ecddb")
    @Override
    public double getFPGATimestamp() {
        return Utility.getFPGATime() / 1000000.0;
    }

    @objid ("21110ebb-361b-4c4d-bafd-a4f43cc89c8f")
    @Override
    public double getMatchTime() {
        return DriverStation.getInstance().getMatchTime();
    }

    @objid ("6c5be0ca-6f28-48c0-80e4-b86d96cd5c0f")
    @Override
    public edu.wpi.first.wpilibj.Timer.Interface newTimer() {
        return new TimerImpl();
    }

    @objid ("5481d0fa-ffac-4427-b560-3ac5e81176e7")
    class TimerImpl implements edu.wpi.first.wpilibj.Timer.Interface {
        @objid ("7d14a838-8fc5-4226-98ec-9743383f223d")
        private long m_startTime;

        @objid ("0d3ce546-7485-4076-857a-08fedbd626b9")
        private double m_accumulatedTime;

        @objid ("6715d844-a879-4140-a06b-1f82139fb79f")
        private boolean m_running;

        /**
         * Create a new timer object.
         * Create a new timer object and reset the time to zero. The timer is initially not running and
         * must be started.
         */
        @objid ("f7f02672-2360-4d33-b1de-af64f7e83108")
        public TimerImpl() {
            reset();
        }

        @objid ("136e79fc-9ee5-42f8-a3fe-86c15e98a674")
        private long getMsClock() {
            return Utility.getFPGATime() / 1000;
        }

        /**
         * Get the current time from the timer. If the clock is running it is derived from
         * the current system clock the start time stored in the timer class. If the clock
         * is not running, then return the time when it was last stopped.
         * @return Current time value for this timer in seconds
         */
        @objid ("c7423e1c-9ff1-4c16-b365-ba8c54b527a4")
        public synchronized double get() {
            if (m_running) {
                return ((double) ((getMsClock() - m_startTime) + m_accumulatedTime)) / 1000.0;
            } else {
                return m_accumulatedTime;
            }
        }

        /**
         * Reset the timer by setting the time to 0.
         * Make the timer startTime the current time so new requests will be relative now
         */
        @objid ("0a1a91e9-dace-48d3-93b2-fbd080469a6f")
        public synchronized void reset() {
            m_accumulatedTime = 0;
            m_startTime = getMsClock();
        }

        /**
         * Start the timer running.
         * Just set the running flag to true indicating that all time requests should be
         * relative to the system clock.
         */
        @objid ("1e066714-e473-46f2-affd-b5a472085dd7")
        public synchronized void start() {
            m_startTime = getMsClock();
            m_running = true;
        }

        /**
         * Stop the timer.
         * This computes the time as of now and clears the running flag, causing all
         * subsequent time requests to be read from the accumulated time rather than
         * looking at the system clock.
         */
        @objid ("c2c3086f-1ad3-4603-89e2-bf309dc8229a")
        public synchronized void stop() {
            final double temp = get();
            m_accumulatedTime = temp;
            m_running = false;
        }

        /**
         * Check if the period specified has passed and if it has, advance the start
         * time by that period. This is useful to decide if it's time to do periodic
         * work without drifting later by the time it took to get around to checking.
         * @param period The period to check for (in seconds).
         * @return If the period has passed.
         */
        @objid ("3bf27b9a-b2bb-467d-94f4-c3e325be4c06")
        public synchronized boolean hasPeriodPassed(double period) {
            if (get() > period) {
                // Advance the start time by the period.
                // Don't set it to the current time... we want to avoid drift.
                m_startTime += period * 1000;
                return true;
            }
            return false;
        }

    }

}
