package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.util.BaseSystemNotInitializedException;

@objid ("97dad363-6a2c-47e4-9d7f-434af9a79d75")
public class Timer {
    @objid ("4810eb03-0a5a-4e2e-8a03-f2c02644c88f")
    private static StaticInterface impl;

    @objid ("676df0fb-bf3c-4445-a96e-3ef2ef0dbe99")
    private final Interface timer;

    @objid ("f5aca3ad-179c-4322-973c-6cf7fb5f0ecb")
    public static void SetImplementation(StaticInterface ti) {
        impl = ti;
    }

    /**
     * Return the system clock time in seconds. Return the time from the
     * FPGA hardware clock in seconds since the FPGA started.
     * @return Robot running time in seconds.
     */
    @objid ("9867235f-5c37-4f0d-b1c7-ee15dc8bbeae")
    public static double getFPGATimestamp() {
        if (impl != null) {
            return impl.getFPGATimestamp();
        } else {
            throw new BaseSystemNotInitializedException(StaticInterface.class, Timer.class);
        }
    }

    /**
     * Return the approximate match time
     * The FMS does not currently send the official match time to the robots
     * This returns the time since the enable signal sent from the Driver Station
     * At the beginning of autonomous, the time is reset to 0.0 seconds
     * At the beginning of teleop, the time is reset to +15.0 seconds
     * If the robot is disabled, this returns 0.0 seconds
     * Warning: This is not an official time (so it cannot be used to argue with referees)
     * @return Match time in seconds since the beginning of autonomous
     */
    @objid ("9939d690-2cb8-48cc-a959-2c8a8cad3b37")
    public static double getMatchTime() {
        if (impl != null) {
            return impl.getMatchTime();
        } else {
            throw new BaseSystemNotInitializedException(StaticInterface.class, Timer.class);
        }
    }

    /**
     * Pause the thread for a specified time. Pause the execution of the
     * thread for a specified period of time given in seconds. Motors will
     * continue to run at their last assigned values, and sensors will continue
     * to update. Only the task containing the wait will pause until the wait
     * time is expired.
     * @param seconds Length of time to pause
     */
    @objid ("234134f6-6740-44fa-866a-4d69c730ec78")
    public static void delay(final double seconds) {
        if (impl != null) {
            impl.delay(seconds);
        } else {
            throw new BaseSystemNotInitializedException(StaticInterface.class, Timer.class);
        }
    }

    @objid ("6ecd728a-6424-4370-b6af-1886878d0637")
    public Timer() {
        if(impl != null){
            timer = impl.newTimer();
        } else {
            throw new BaseSystemNotInitializedException(StaticInterface.class, Timer.class);
        }
    }

    /**
     * Get the current time from the timer. If the clock is running it is derived from
     * the current system clock the start time stored in the timer class. If the clock
     * is not running, then return the time when it was last stopped.
     * @return Current time value for this timer in seconds
     */
    @objid ("2171fbb2-c582-40f6-b9bd-60ca278dafec")
    public double get() {
        return timer.get();
    }

    /**
     * Reset the timer by setting the time to 0.
     * Make the timer startTime the current time so new requests will be relative now
     */
    @objid ("dc27c7b2-a47e-4dfa-a1b9-bd25c1411d6f")
    public void reset() {
        timer.reset();
    }

    /**
     * Start the timer running.
     * Just set the running flag to true indicating that all time requests should be
     * relative to the system clock.
     */
    @objid ("aaa518a1-be3c-409f-bf48-19d1db1a84b0")
    public void start() {
        timer.start();
    }

    /**
     * Stop the timer.
     * This computes the time as of now and clears the running flag, causing all
     * subsequent time requests to be read from the accumulated time rather than
     * looking at the system clock.
     */
    @objid ("3b0537b1-e5df-4334-a96a-2c1600654ae0")
    public void stop() {
        timer.stop();
    }

    /**
     * Check if the period specified has passed and if it has, advance the start
     * time by that period. This is useful to decide if it's time to do periodic
     * work without drifting later by the time it took to get around to checking.
     * @param period The period to check for (in seconds).
     * @return If the period has passed.
     */
    @objid ("237c3d77-ea9d-4f94-afbb-cc4622180f12")
    public boolean hasPeriodPassed(double period) {
        return timer.hasPeriodPassed(period);
    }

    @objid ("996dcb29-2aea-4cd2-b57c-b54c2cef5524")
    public interface StaticInterface {
        @objid ("d2498838-dc88-4685-9334-6d0a6e5d2d3d")
        double getFPGATimestamp();

        @objid ("060afea8-20e5-413d-84f8-2fd286901811")
        double getMatchTime();

        @objid ("0c471289-528f-472d-9898-2644395db797")
        void delay(final double seconds);

        @objid ("5fdf165d-db39-4bbb-a8ba-e8c305ed693b")
        Interface newTimer();

    }

    @objid ("897c90bf-a6cc-4dcb-9bba-aee7cfae3566")
    public interface Interface {
        /**
         * Get the current time from the timer. If the clock is running it is derived from
         * the current system clock the start time stored in the timer class. If the clock
         * is not running, then return the time when it was last stopped.
         * @return Current time value for this timer in seconds
         */
        @objid ("8678036a-8e33-48bc-8d4b-c759a8a46361")
        double get();

        /**
         * Reset the timer by setting the time to 0.
         * Make the timer startTime the current time so new requests will be relative now
         */
        @objid ("b9c53e2a-7b29-45a8-b695-8e190bbbca92")
        void reset();

        /**
         * Start the timer running.
         * Just set the running flag to true indicating that all time requests should be
         * relative to the system clock.
         */
        @objid ("c466c7eb-48a5-45b9-bcb6-1b7c6f0f440a")
        void start();

        /**
         * Stop the timer.
         * This computes the time as of now and clears the running flag, causing all
         * subsequent time requests to be read from the accumulated time rather than
         * looking at the system clock.
         */
        @objid ("1f1b5e39-fde9-428c-bfbb-84860607f4a0")
        void stop();

        /**
         * Check if the period specified has passed and if it has, advance the start
         * time by that period. This is useful to decide if it's time to do periodic
         * work without drifting later by the time it took to get around to checking.
         * @param period The period to check for (in seconds).
         * @return If the period has passed.
         */
        @objid ("a2b20cf6-4a26-472e-8370-2839e657ef93")
        boolean hasPeriodPassed(double period);

    }

}
