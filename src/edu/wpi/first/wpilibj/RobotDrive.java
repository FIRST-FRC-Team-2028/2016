/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tInstances;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;

/**
 * Utility class for handling Robot drive based on a definition of the motor configuration.
 * The robot drive class handles basic driving for a robot. Currently, 2 and 4 motor tank and
 * mecanum drive trains are supported. In the future other drive types like swerve might
 * be implemented. Motor channel numbers are supplied on creation of the class. Those are
 * used for either the drive function (intended for hand created drive code, such as autonomous)
 * or with the Tank/Arcade functions intended to be used for Operator Control driving.
 */
@objid ("ccf1c5d8-261a-45ba-a7f6-fea4567bc7ce")
public class RobotDrive implements MotorSafety {
    @objid ("a3a622b5-2014-4c67-9655-59c2d8ae3657")
    public static final double kDefaultExpirationTime = 0.1;

    @objid ("b8e8e524-9480-4afb-86f6-4ab1bfb4bc01")
    public static final double kDefaultSensitivity = 0.5;

    @objid ("102a2279-964a-41ea-bd6f-965c2098ded1")
    public static final double kDefaultMaxOutput = 1.0;

    @objid ("fe3a1e97-30d6-4797-8ef8-767856eedf08")
    protected static final int kMaxNumberOfMotors = 4;

    @objid ("1f213981-88ef-4715-96bd-f9d0f4890bc8")
    protected final int[] m_invertedMotors = new int[4];

    @objid ("fb912a7d-8ed7-473b-b15d-72fc9eeb23d9")
    protected double m_sensitivity;

    @objid ("f4572b51-3f2c-44c2-a465-ce15cc6a6874")
    protected double m_maxOutput;

    @objid ("76e705f5-681f-46d3-b48d-5c4bc0b0836f")
    protected boolean m_allocatedSpeedControllers;

    @objid ("6ae0f87c-993b-47df-962a-f08cfa420d4f")
    protected byte m_syncGroup = 0;

    @objid ("09f99aa9-9c16-414d-bc17-bcdd9e5413e9")
    protected static boolean kArcadeRatioCurve_Reported = false;

    @objid ("e1e008af-a37f-4a9c-bc17-fd0e5b6c0823")
    protected static boolean kTank_Reported = false;

    @objid ("d347f3b8-c46a-47b4-b742-49716c698e0d")
    protected static boolean kArcadeStandard_Reported = false;

    @objid ("d1bb08e8-0f74-444a-b4b4-3cc0bb8d82e9")
    protected static boolean kMecanumCartesian_Reported = false;

    @objid ("08caa86a-9cfd-43c0-8624-915d511e31dc")
    protected static boolean kMecanumPolar_Reported = false;

    @objid ("46629448-118c-4736-b257-c24433335352")
    protected MotorSafetyHelper m_safetyHelper;

    @objid ("101d22d9-2a0f-4de2-ab94-48ecbcb9febd")
    protected SpeedController m_frontLeftMotor;

    @objid ("794057d1-8b80-481c-aec1-eede4a8b8277")
    protected SpeedController m_frontRightMotor;

    @objid ("b9cacca8-545a-4a42-a3ad-0a13938a0ec9")
    protected SpeedController m_rearLeftMotor;

    @objid ("aa232af9-9cef-4ed0-9a90-304c1b155426")
    protected SpeedController m_rearRightMotor;

    /**
     * Constructor for RobotDrive with 2 motors specified with channel numbers.
     * Set up parameters for a two wheel drive system where the
     * left and right motor pwm channels are specified in the call.
     * This call assumes Talons for controlling the motors.
     * @param leftMotorChannel The PWM channel number that drives the left motor.
     * @param rightMotorChannel The PWM channel number that drives the right motor.
     */
    @objid ("19d41876-b727-4bfb-991f-58c9a24ce20d")
    public RobotDrive(final int leftMotorChannel, final int rightMotorChannel) {
        m_sensitivity = kDefaultSensitivity;
        m_maxOutput = kDefaultMaxOutput;
        m_frontLeftMotor = null;
        m_rearLeftMotor = new Talon(leftMotorChannel);
        m_frontRightMotor = null;
        m_rearRightMotor = new Talon(rightMotorChannel);
        for (int i = 0; i < kMaxNumberOfMotors; i++) {
            m_invertedMotors[i] = 1;
        }
        m_allocatedSpeedControllers = true;
        setupMotorSafety();
        drive(0, 0);
    }

    /**
     * Constructor for RobotDrive with 4 motors specified with channel numbers.
     * Set up parameters for a four wheel drive system where all four motor
     * pwm channels are specified in the call.
     * This call assumes Talons for controlling the motors.
     * @param frontLeftMotor Front left motor channel number
     * @param rearLeftMotor Rear Left motor channel number
     * @param frontRightMotor Front right motor channel number
     * @param rearRightMotor Rear Right motor channel number
     */
    @objid ("295d4452-f722-4be8-aa7a-037eb4ad1e75")
    public RobotDrive(final int frontLeftMotor, final int rearLeftMotor, final int frontRightMotor, final int rearRightMotor) {
        m_sensitivity = kDefaultSensitivity;
        m_maxOutput = kDefaultMaxOutput;
        m_rearLeftMotor = new Talon(rearLeftMotor);
        m_rearRightMotor = new Talon(rearRightMotor);
        m_frontLeftMotor = new Talon(frontLeftMotor);
        m_frontRightMotor = new Talon(frontRightMotor);
        for (int i = 0; i < kMaxNumberOfMotors; i++) {
            m_invertedMotors[i] = 1;
        }
        m_allocatedSpeedControllers = true;
        setupMotorSafety();
        drive(0, 0);
    }

    /**
     * Constructor for RobotDrive with 2 motors specified as SpeedController objects.
     * The SpeedController version of the constructor enables programs to use the RobotDrive classes with
     * subclasses of the SpeedController objects, for example, versions with ramping or reshaping of
     * the curve to suit motor bias or dead-band elimination.
     * @param leftMotor The left SpeedController object used to drive the robot.
     * @param rightMotor the right SpeedController object used to drive the robot.
     */
    @objid ("98a62a1e-fcb9-474c-b626-bde7931b6bcf")
    public RobotDrive(SpeedController leftMotor, SpeedController rightMotor) {
        if (leftMotor == null || rightMotor == null) {
            m_rearLeftMotor = m_rearRightMotor = null;
            throw new NullPointerException("Null motor provided");
        }
        m_frontLeftMotor = null;
        m_rearLeftMotor = leftMotor;
        m_frontRightMotor = null;
        m_rearRightMotor = rightMotor;
        m_sensitivity = kDefaultSensitivity;
        m_maxOutput = kDefaultMaxOutput;
        for (int i = 0; i < kMaxNumberOfMotors; i++) {
            m_invertedMotors[i] = 1;
        }
        m_allocatedSpeedControllers = false;
        setupMotorSafety();
        drive(0, 0);
    }

    /**
     * Constructor for RobotDrive with 4 motors specified as SpeedController objects.
     * Speed controller input version of RobotDrive (see previous comments).
     * @param frontLeftMotor The front left SpeedController object used to drive the robot
     * @param rearLeftMotor The back left SpeedController object used to drive the robot.
     * @param frontRightMotor The front right SpeedController object used to drive the robot.
     * @param rearRightMotor The back right SpeedController object used to drive the robot.
     */
    @objid ("b3bb09ad-e803-49d7-9d0c-b01b17b426d7")
    public RobotDrive(SpeedController frontLeftMotor, SpeedController rearLeftMotor, SpeedController frontRightMotor, SpeedController rearRightMotor) {
        if (frontLeftMotor == null || rearLeftMotor == null || frontRightMotor == null || rearRightMotor == null) {
            m_frontLeftMotor = m_rearLeftMotor = m_frontRightMotor = m_rearRightMotor = null;
            throw new NullPointerException("Null motor provided");
        }
        m_frontLeftMotor = frontLeftMotor;
        m_rearLeftMotor = rearLeftMotor;
        m_frontRightMotor = frontRightMotor;
        m_rearRightMotor = rearRightMotor;
        m_sensitivity = kDefaultSensitivity;
        m_maxOutput = kDefaultMaxOutput;
        for (int i = 0; i < kMaxNumberOfMotors; i++) {
            m_invertedMotors[i] = 1;
        }
        m_allocatedSpeedControllers = false;
        setupMotorSafety();
        drive(0, 0);
    }

    /**
     * Drive the motors at "speed" and "curve".
     * 
     * The speed and curve are -1.0 to +1.0 values where 0.0 represents stopped and
     * not turning. The algorithm for adding in the direction attempts to provide a constant
     * turn radius for differing speeds.
     * 
     * This function will most likely be used in an autonomous routine.
     * @param outputMagnitude The forward component of the output magnitude to send to the motors.
     * @param curve The rate of turn, constant for different forward speeds.
     */
    @objid ("06e426bf-fd02-4320-a1f1-37ec3afbdf4f")
    public void drive(double outputMagnitude, double curve) {
        double leftOutput, rightOutput;
        
        if(!kArcadeRatioCurve_Reported) {
            UsageReporting.report(tResourceType.kResourceType_RobotDrive, getNumMotors(), tInstances.kRobotDrive_ArcadeRatioCurve);
            kArcadeRatioCurve_Reported = true;
        }
        if (curve < 0) {
            double value = Math.log(-curve);
            double ratio = (value - m_sensitivity) / (value + m_sensitivity);
            if (ratio == 0) {
                ratio = .0000000001;
            }
            leftOutput = outputMagnitude / ratio;
            rightOutput = outputMagnitude;
        } else if (curve > 0) {
            double value = Math.log(curve);
            double ratio = (value - m_sensitivity) / (value + m_sensitivity);
            if (ratio == 0) {
                ratio = .0000000001;
            }
            leftOutput = outputMagnitude;
            rightOutput = outputMagnitude / ratio;
        } else {
            leftOutput = outputMagnitude;
            rightOutput = outputMagnitude;
        }
        setLeftRightMotorOutputs(leftOutput, rightOutput);
    }

    /**
     * Provide tank steering using the stored robot configuration.
     * drive the robot using two joystick inputs. The Y-axis will be selected from
     * each Joystick object.
     * @param leftStick The joystick to control the left side of the robot.
     * @param rightStick The joystick to control the right side of the robot.
     */
    @objid ("500ebd17-b08b-48cb-89f3-be3025bd9af6")
    public void tankDrive(GenericHID leftStick, GenericHID rightStick) {
        if (leftStick == null || rightStick == null) {
            throw new NullPointerException("Null HID provided");
        }
        tankDrive(leftStick.getY(), rightStick.getY(), true);
    }

    /**
     * Provide tank steering using the stored robot configuration.
     * drive the robot using two joystick inputs. The Y-axis will be selected from
     * each Joystick object.
     * @param leftStick The joystick to control the left side of the robot.
     * @param rightStick The joystick to control the right side of the robot.
     * @param squaredInputs Setting this parameter to true decreases the sensitivity at lower speeds
     */
    @objid ("11120ad9-88af-402c-8574-d15e9f8fa1d9")
    public void tankDrive(GenericHID leftStick, GenericHID rightStick, boolean squaredInputs) {
        if (leftStick == null || rightStick == null) {
            throw new NullPointerException("Null HID provided");
        }
        tankDrive(leftStick.getY(), rightStick.getY(), squaredInputs);
    }

    /**
     * Provide tank steering using the stored robot configuration.
     * This function lets you pick the axis to be used on each Joystick object for the left
     * and right sides of the robot.
     * @param leftStick The Joystick object to use for the left side of the robot.
     * @param leftAxis The axis to select on the left side Joystick object.
     * @param rightStick The Joystick object to use for the right side of the robot.
     * @param rightAxis The axis to select on the right side Joystick object.
     */
    @objid ("45990e25-c7fe-46e8-b802-5434df412181")
    public void tankDrive(GenericHID leftStick, final int leftAxis, GenericHID rightStick, final int rightAxis) {
        if (leftStick == null || rightStick == null) {
            throw new NullPointerException("Null HID provided");
        }
        tankDrive(leftStick.getRawAxis(leftAxis), rightStick.getRawAxis(rightAxis), true);
    }

    /**
     * Provide tank steering using the stored robot configuration.
     * This function lets you pick the axis to be used on each Joystick object for the left
     * and right sides of the robot.
     * @param leftStick The Joystick object to use for the left side of the robot.
     * @param leftAxis The axis to select on the left side Joystick object.
     * @param rightStick The Joystick object to use for the right side of the robot.
     * @param rightAxis The axis to select on the right side Joystick object.
     * @param squaredInputs Setting this parameter to true decreases the sensitivity at lower speeds
     */
    @objid ("41fcd159-42bb-4689-8730-83542a3fa6d3")
    public void tankDrive(GenericHID leftStick, final int leftAxis, GenericHID rightStick, final int rightAxis, boolean squaredInputs) {
        if (leftStick == null || rightStick == null) {
            throw new NullPointerException("Null HID provided");
        }
        tankDrive(leftStick.getRawAxis(leftAxis), rightStick.getRawAxis(rightAxis), squaredInputs);
    }

    /**
     * Provide tank steering using the stored robot configuration.
     * This function lets you directly provide joystick values from any source.
     * @param leftValue The value of the left stick.
     * @param rightValue The value of the right stick.
     * @param squaredInputs Setting this parameter to true decreases the sensitivity at lower speeds
     */
    @objid ("b698c0de-2f2c-42f5-b74a-58974fb228f7")
    public void tankDrive(double leftValue, double rightValue, boolean squaredInputs) {
        if(!kTank_Reported) {
            UsageReporting.report(tResourceType.kResourceType_RobotDrive, getNumMotors(), tInstances.kRobotDrive_Tank);
            kTank_Reported = true;
        }
        
        // square the inputs (while preserving the sign) to increase fine control while permitting full power
        leftValue = limit(leftValue);
        rightValue = limit(rightValue);
        if(squaredInputs) {
            if (leftValue >= 0.0) {
                leftValue = (leftValue * leftValue);
            } else {
                leftValue = -(leftValue * leftValue);
            }
            if (rightValue >= 0.0) {
                rightValue = (rightValue * rightValue);
            } else {
                rightValue = -(rightValue * rightValue);
            }
        }
        setLeftRightMotorOutputs(leftValue, rightValue);
    }

    /**
     * Provide tank steering using the stored robot configuration.
     * This function lets you directly provide joystick values from any source.
     * @param leftValue The value of the left stick.
     * @param rightValue The value of the right stick.
     */
    @objid ("b993fd79-bdb5-4734-ad71-d3af2be9ac47")
    public void tankDrive(double leftValue, double rightValue) {
        tankDrive(leftValue, rightValue, true);
    }

    /**
     * Arcade drive implements single stick driving.
     * Given a single Joystick, the class assumes the Y axis for the move value and the X axis
     * for the rotate value.
     * (Should add more information here regarding the way that arcade drive works.)
     * @param stick The joystick to use for Arcade single-stick driving. The Y-axis will be selected
     * for forwards/backwards and the X-axis will be selected for rotation rate.
     * @param squaredInputs If true, the sensitivity will be decreased for small values
     */
    @objid ("5ce92ecc-ec93-4c5f-a816-53bf9d9fe4a2")
    public void arcadeDrive(GenericHID stick, boolean squaredInputs) {
        // simply call the full-featured arcadeDrive with the appropriate values
        arcadeDrive(stick.getY(), stick.getX(), squaredInputs);
    }

    /**
     * Arcade drive implements single stick driving.
     * Given a single Joystick, the class assumes the Y axis for the move value and the X axis
     * for the rotate value.
     * (Should add more information here regarding the way that arcade drive works.)
     * @param stick The joystick to use for Arcade single-stick driving. The Y-axis will be selected
     * for forwards/backwards and the X-axis will be selected for rotation rate.
     */
    @objid ("a4294f15-2fc5-41cf-bb46-27b9b001a94b")
    public void arcadeDrive(GenericHID stick) {
        this.arcadeDrive(stick, true);
    }

    /**
     * Arcade drive implements single stick driving.
     * Given two joystick instances and two axis, compute the values to send to either two
     * or four motors.
     * @param moveStick The Joystick object that represents the forward/backward direction
     * @param moveAxis The axis on the moveStick object to use for forwards/backwards (typically Y_AXIS)
     * @param rotateStick The Joystick object that represents the rotation value
     * @param rotateAxis The axis on the rotation object to use for the rotate right/left (typically X_AXIS)
     * @param squaredInputs Setting this parameter to true decreases the sensitivity at lower speeds
     */
    @objid ("e2a0fd20-cb64-40ea-9a2b-1f8e76b5f3c4")
    public void arcadeDrive(GenericHID moveStick, final int moveAxis, GenericHID rotateStick, final int rotateAxis, boolean squaredInputs) {
        double moveValue = moveStick.getRawAxis(moveAxis);
        double rotateValue = rotateStick.getRawAxis(rotateAxis);
        
        arcadeDrive(moveValue, rotateValue, squaredInputs);
    }

    /**
     * Arcade drive implements single stick driving.
     * Given two joystick instances and two axis, compute the values to send to either two
     * or four motors.
     * @param moveStick The Joystick object that represents the forward/backward direction
     * @param moveAxis The axis on the moveStick object to use for forwards/backwards (typically Y_AXIS)
     * @param rotateStick The Joystick object that represents the rotation value
     * @param rotateAxis The axis on the rotation object to use for the rotate right/left (typically X_AXIS)
     */
    @objid ("02c91e3f-76e2-4e83-99f0-49c89759bab1")
    public void arcadeDrive(GenericHID moveStick, final int moveAxis, GenericHID rotateStick, final int rotateAxis) {
        this.arcadeDrive(moveStick, moveAxis, rotateStick, rotateAxis, true);
    }

    /**
     * Arcade drive implements single stick driving.
     * This function lets you directly provide joystick values from any source.
     * @param moveValue The value to use for forwards/backwards
     * @param rotateValue The value to use for the rotate right/left
     * @param squaredInputs If set, decreases the sensitivity at low speeds
     */
    @objid ("12f004ae-3369-47e2-93e5-7621d12cf1b1")
    public void arcadeDrive(double moveValue, double rotateValue, boolean squaredInputs) {
        // local variables to hold the computed PWM values for the motors
        if(!kArcadeStandard_Reported) {
            UsageReporting.report(tResourceType.kResourceType_RobotDrive, getNumMotors(), tInstances.kRobotDrive_ArcadeStandard);
            kArcadeStandard_Reported = true;
        }
        
        double leftMotorSpeed;
        double rightMotorSpeed;
        
        moveValue = limit(moveValue);
        rotateValue = limit(rotateValue);
        
        if (squaredInputs) {
            // square the inputs (while preserving the sign) to increase fine control while permitting full power
            if (moveValue >= 0.0) {
                moveValue = (moveValue * moveValue);
            } else {
                moveValue = -(moveValue * moveValue);
            }
            if (rotateValue >= 0.0) {
                rotateValue = (rotateValue * rotateValue);
            } else {
                rotateValue = -(rotateValue * rotateValue);
            }
        }
        
        if (moveValue > 0.0) {
            if (rotateValue > 0.0) {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = Math.max(moveValue, rotateValue);
            } else {
                leftMotorSpeed = Math.max(moveValue, -rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            }
        } else {
            if (rotateValue > 0.0) {
                leftMotorSpeed = -Math.max(-moveValue, rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            } else {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
            }
        }
        
        setLeftRightMotorOutputs(leftMotorSpeed, rightMotorSpeed);
    }

    /**
     * Arcade drive implements single stick driving.
     * This function lets you directly provide joystick values from any source.
     * @param moveValue The value to use for fowards/backwards
     * @param rotateValue The value to use for the rotate right/left
     */
    @objid ("302381e4-3555-4ade-a3e8-5a9c3fa6d2d6")
    public void arcadeDrive(double moveValue, double rotateValue) {
        this.arcadeDrive(moveValue, rotateValue, true);
    }

    /**
     * Drive method for Mecanum wheeled robots.
     * 
     * A method for driving with Mecanum wheeled robots. There are 4 wheels
     * on the robot, arranged so that the front and back wheels are toed in 45 degrees.
     * When looking at the wheels from the top, the roller axles should form an X across the robot.
     * 
     * This is designed to be directly driven by joystick axes.
     * @param x The speed that the robot should drive in the X direction. [-1.0..1.0]
     * @param y The speed that the robot should drive in the Y direction.
     * This input is inverted to match the forward == -1.0 that joysticks produce. [-1.0..1.0]
     * @param rotation The rate of rotation for the robot that is completely independent of
     * the translation. [-1.0..1.0]
     * @param gyroAngle The current angle reading from the gyro.  Use this to implement field-oriented controls.
     */
    @objid ("0dc4fc34-28b9-49a0-b0db-8859ef051851")
    public void mecanumDrive_Cartesian(double x, double y, double rotation, double gyroAngle) {
        if(!kMecanumCartesian_Reported) {
            UsageReporting.report(tResourceType.kResourceType_RobotDrive, getNumMotors(), tInstances.kRobotDrive_MecanumCartesian);
            kMecanumCartesian_Reported = true;
        }
        double xIn = x;
        double yIn = y;
        // Negate y for the joystick.
        yIn = -yIn;
        // Compenstate for gyro angle.
        double rotated[] = rotateVector(xIn, yIn, gyroAngle);
        xIn = rotated[0];
        yIn = rotated[1];
        
        double wheelSpeeds[] = new double[kMaxNumberOfMotors];
        wheelSpeeds[MotorType.kFrontLeft_val] = xIn + yIn + rotation;
        wheelSpeeds[MotorType.kFrontRight_val] = -xIn + yIn - rotation;
        wheelSpeeds[MotorType.kRearLeft_val] = -xIn + yIn + rotation;
        wheelSpeeds[MotorType.kRearRight_val] = xIn + yIn - rotation;
        
        normalize(wheelSpeeds);
        m_frontLeftMotor.set(wheelSpeeds[MotorType.kFrontLeft_val] * m_invertedMotors[MotorType.kFrontLeft_val] * m_maxOutput, m_syncGroup);
        m_frontRightMotor.set(wheelSpeeds[MotorType.kFrontRight_val] * m_invertedMotors[MotorType.kFrontRight_val] * m_maxOutput, m_syncGroup);
        m_rearLeftMotor.set(wheelSpeeds[MotorType.kRearLeft_val] * m_invertedMotors[MotorType.kRearLeft_val] * m_maxOutput, m_syncGroup);
        m_rearRightMotor.set(wheelSpeeds[MotorType.kRearRight_val] * m_invertedMotors[MotorType.kRearRight_val] * m_maxOutput, m_syncGroup);
        
        if (m_syncGroup != 0) {
            CANJaguar.updateSyncGroup(m_syncGroup);
        }
        
        if (m_safetyHelper != null) m_safetyHelper.feed();
    }

    /**
     * Drive method for Mecanum wheeled robots.
     * 
     * A method for driving with Mecanum wheeled robots. There are 4 wheels
     * on the robot, arranged so that the front and back wheels are toed in 45 degrees.
     * When looking at the wheels from the top, the roller axles should form an X across the robot.
     * @param magnitude The speed that the robot should drive in a given direction.
     * @param direction The direction the robot should drive in degrees. The direction and maginitute are
     * independent of the rotation rate.
     * @param rotation The rate of rotation for the robot that is completely independent of
     * the magnitute or direction. [-1.0..1.0]
     */
    @objid ("25db1088-695e-484c-b8cc-1322aede8792")
    public void mecanumDrive_Polar(double magnitude, double direction, double rotation) {
        if(!kMecanumPolar_Reported) {
            UsageReporting.report(tResourceType.kResourceType_RobotDrive, getNumMotors(), tInstances.kRobotDrive_MecanumPolar);
            kMecanumPolar_Reported = true;
        }
        double frontLeftSpeed, rearLeftSpeed, frontRightSpeed, rearRightSpeed;
        // Normalized for full power along the Cartesian axes.
        magnitude = limit(magnitude) * Math.sqrt(2.0);
        // The rollers are at 45 degree angles.
        double dirInRad = (direction + 45.0) * 3.14159 / 180.0;
        double cosD = Math.cos(dirInRad);
        double sinD = Math.sin(dirInRad);
        
        double wheelSpeeds[] = new double[kMaxNumberOfMotors];
        wheelSpeeds[MotorType.kFrontLeft_val] = (sinD * magnitude + rotation);
        wheelSpeeds[MotorType.kFrontRight_val] = (cosD * magnitude - rotation);
        wheelSpeeds[MotorType.kRearLeft_val] = (cosD * magnitude + rotation);
        wheelSpeeds[MotorType.kRearRight_val] = (sinD * magnitude - rotation);
        
        normalize(wheelSpeeds);
        
        m_frontLeftMotor.set(wheelSpeeds[MotorType.kFrontLeft_val] * m_invertedMotors[MotorType.kFrontLeft_val] * m_maxOutput, m_syncGroup);
        m_frontRightMotor.set(wheelSpeeds[MotorType.kFrontRight_val] * m_invertedMotors[MotorType.kFrontRight_val] * m_maxOutput, m_syncGroup);
        m_rearLeftMotor.set(wheelSpeeds[MotorType.kRearLeft_val] * m_invertedMotors[MotorType.kRearLeft_val] * m_maxOutput, m_syncGroup);
        m_rearRightMotor.set(wheelSpeeds[MotorType.kRearRight_val] * m_invertedMotors[MotorType.kRearRight_val] * m_maxOutput, m_syncGroup);
        
        if (this.m_syncGroup != 0) {
            CANJaguar.updateSyncGroup(m_syncGroup);
        }
        
        if (m_safetyHelper != null) m_safetyHelper.feed();
    }

    /**
     * Holonomic Drive method for Mecanum wheeled robots.
     * 
     * This is an alias to mecanumDrive_Polar() for backward compatability
     * @param magnitude The speed that the robot should drive in a given direction.  [-1.0..1.0]
     * @param direction The direction the robot should drive. The direction and maginitute are
     * independent of the rotation rate.
     * @param rotation The rate of rotation for the robot that is completely independent of
     * the magnitute or direction.  [-1.0..1.0]
     */
    @objid ("b2e2ce95-afca-40d7-9c46-7c7520b3d3a8")
    void holonomicDrive(float magnitude, float direction, float rotation) {
        mecanumDrive_Polar(magnitude, direction, rotation);
    }

    /**
     * Set the speed of the right and left motors.
     * This is used once an appropriate drive setup function is called such as
     * twoWheelDrive(). The motors are set to "leftSpeed" and "rightSpeed"
     * and includes flipping the direction of one side for opposing motors.
     * @param leftOutput The speed to send to the left side of the robot.
     * @param rightOutput The speed to send to the right side of the robot.
     */
    @objid ("3e980e46-efa7-405d-b372-b62e3fd892bd")
    public void setLeftRightMotorOutputs(double leftOutput, double rightOutput) {
        if (m_rearLeftMotor == null || m_rearRightMotor == null) {
            throw new NullPointerException("Null motor provided");
        }
        
        if (m_frontLeftMotor != null) {
            m_frontLeftMotor.set(limit(leftOutput) * m_invertedMotors[MotorType.kFrontLeft_val] * m_maxOutput, m_syncGroup);
        }
        m_rearLeftMotor.set(limit(leftOutput) * m_invertedMotors[MotorType.kRearLeft_val] * m_maxOutput, m_syncGroup);
        
        if (m_frontRightMotor != null) {
            m_frontRightMotor.set(-limit(rightOutput) * m_invertedMotors[MotorType.kFrontRight_val] * m_maxOutput, m_syncGroup);
        }
        m_rearRightMotor.set(-limit(rightOutput) * m_invertedMotors[MotorType.kRearRight_val] * m_maxOutput, m_syncGroup);
        
        if (this.m_syncGroup != 0) {
            CANJaguar.updateSyncGroup(m_syncGroup);
        }
        
        if (m_safetyHelper != null) m_safetyHelper.feed();
    }

    /**
     * Limit motor values to the -1.0 to +1.0 range.
     */
    @objid ("9f08153c-fcb5-4fab-9424-3ecde4bb2a90")
    protected static double limit(double num) {
        if (num > 1.0) {
            return 1.0;
        }
        if (num < -1.0) {
            return -1.0;
        }
        return num;
    }

    /**
     * Normalize all wheel speeds if the magnitude of any wheel is greater than 1.0.
     */
    @objid ("0cd95ba2-eb6c-40df-8f48-0a32768ea4bb")
    protected static void normalize(double[] wheelSpeeds) {
        double maxMagnitude = Math.abs(wheelSpeeds[0]);
        int i;
        for (i=1; i<kMaxNumberOfMotors; i++) {
            double temp = Math.abs(wheelSpeeds[i]);
            if (maxMagnitude < temp) maxMagnitude = temp;
        }
        if (maxMagnitude > 1.0) {
            for (i=0; i<kMaxNumberOfMotors; i++) {
                wheelSpeeds[i] = wheelSpeeds[i] / maxMagnitude;
            }
        }
    }

    /**
     * Rotate a vector in Cartesian space.
     */
    @objid ("1dc87879-3b9d-4e5f-828e-1662d98ebbad")
    protected static double[] rotateVector(double x, double y, double angle) {
        double cosA = Math.cos(angle * (3.14159 / 180.0));
        double sinA = Math.sin(angle * (3.14159 / 180.0));
        double out[] = new double[2];
        out[0] = x * cosA - y * sinA;
        out[1] = x * sinA + y * cosA;
        return out;
    }

    /**
     * Invert a motor direction.
     * This is used when a motor should run in the opposite direction as the drive
     * code would normally run it. Motors that are direct drive would be inverted, the
     * drive code assumes that the motors are geared with one reversal.
     * @param motor The motor index to invert.
     * @param isInverted True if the motor should be inverted when operated.
     */
    @objid ("a7e243b1-0098-40df-84d6-d1ec3a3d28ad")
    public void setInvertedMotor(MotorType motor, boolean isInverted) {
        m_invertedMotors[motor.value] = isInverted ? -1 : 1;
    }

    /**
     * Set the turning sensitivity.
     * 
     * This only impacts the drive() entry-point.
     * @param sensitivity Effectively sets the turning sensitivity (or turn radius for a given value)
     */
    @objid ("64381eb0-8056-4fdc-a238-263710f36b6a")
    public void setSensitivity(double sensitivity) {
        m_sensitivity = sensitivity;
    }

    /**
     * Configure the scaling factor for using RobotDrive with motor controllers in a mode other than PercentVbus.
     * @param maxOutput Multiplied with the output percentage computed by the drive functions.
     */
    @objid ("497efcc2-9436-43e9-a036-1276be9ab09c")
    public void setMaxOutput(double maxOutput) {
        m_maxOutput = maxOutput;
    }

    /**
     * Set the number of the sync group for the motor controllers.  If the motor controllers are {@link CANJaguar}s,
     * then they will all be added to this sync group, causing them to update their values at the same time.
     * @param syncGroup the update group to add the motor controllers to
     */
    @objid ("85ae1d83-5f9c-457d-957f-aced39f7da59")
    public void setCANJaguarSyncGroup(byte syncGroup) {
        m_syncGroup = syncGroup;
    }

    /**
     * Free the speed controllers if they were allocated locally
     */
    @objid ("6c3657f0-5919-468f-9894-3945865124cf")
    public void free() {
        if (m_allocatedSpeedControllers) {
            if (m_frontLeftMotor != null) {
                ((PWM) m_frontLeftMotor).free();
            }
            if (m_frontRightMotor != null) {
                ((PWM) m_frontRightMotor).free();
            }
            if (m_rearLeftMotor != null) {
                ((PWM) m_rearLeftMotor).free();
            }
            if (m_rearRightMotor != null) {
                ((PWM) m_rearRightMotor).free();
            }
        }
    }

    @objid ("9938407f-e088-4b7b-bde9-727cfb93c7a2")
    public void setExpiration(double timeout) {
        m_safetyHelper.setExpiration(timeout);
    }

    @objid ("e02d880a-a465-48c0-acfe-66007f2d1277")
    public double getExpiration() {
        return m_safetyHelper.getExpiration();
    }

    @objid ("d2ccd155-1b3f-4b26-bcdb-a30be2e1f089")
    public boolean isAlive() {
        return m_safetyHelper.isAlive();
    }

    @objid ("e49a898f-4bb9-432a-96a2-6a4f08ea124c")
    public boolean isSafetyEnabled() {
        return m_safetyHelper.isSafetyEnabled();
    }

    @objid ("c8cdbb2f-df58-4478-a028-263bc803f042")
    public void setSafetyEnabled(boolean enabled) {
        m_safetyHelper.setSafetyEnabled(enabled);
    }

    @objid ("a0e19d53-caf2-49d3-b7c5-f85a963c9350")
    public String getDescription() {
        return "Robot Drive";
    }

    @objid ("bc223e6c-4e62-4efa-ab6b-cbf22a549301")
    public void stopMotor() {
        if (m_frontLeftMotor != null) {
            m_frontLeftMotor.set(0.0);
        }
        if (m_frontRightMotor != null) {
            m_frontRightMotor.set(0.0);
        }
        if (m_rearLeftMotor != null) {
            m_rearLeftMotor.set(0.0);
        }
        if (m_rearRightMotor != null) {
            m_rearRightMotor.set(0.0);
        }
        if (m_safetyHelper != null) m_safetyHelper.feed();
    }

    @objid ("2176b29e-671e-4911-8275-ac0869098fd2")
    private void setupMotorSafety() {
        m_safetyHelper = new MotorSafetyHelper(this);
        m_safetyHelper.setExpiration(kDefaultExpirationTime);
        m_safetyHelper.setSafetyEnabled(true);
    }

    @objid ("5c380f29-6087-4a8f-a947-8576637fe8f1")
    protected int getNumMotors() {
        int motors = 0;
        if (m_frontLeftMotor != null) motors++;
        if (m_frontRightMotor != null) motors++;
        if (m_rearLeftMotor != null) motors++;
        if (m_rearRightMotor != null) motors++;
        return motors;
    }

    /**
     * The location of a motor on the robot for the purpose of driving
     */
    @objid ("64853e0d-2afe-4174-bd48-1345e878c9ce")
    public static class MotorType {
        /**
         * The integer value representing this enumeration
         */
        @objid ("4c5b6c3a-3de9-4471-a543-a3db2109318e")
        public final int value;

        @objid ("d3a90661-f156-41d9-8a4b-c00846e32c1a")
         static final int kFrontLeft_val = 0;

        @objid ("5a58374f-2324-4a54-a804-44db26a4ebda")
         static final int kFrontRight_val = 1;

        @objid ("e7660af8-08b2-4497-b52f-dc4cb832c857")
         static final int kRearLeft_val = 2;

        @objid ("8d721d4e-3d18-45b5-bca5-e4c492497bc7")
         static final int kRearRight_val = 3;

        /**
         * motortype: front left
         */
        @objid ("f6772b88-cfee-4fb6-9687-ae8244c8302c")
        public static final MotorType kFrontLeft = new MotorType(kFrontLeft_val);

        /**
         * motortype: front right
         */
        @objid ("262ea413-9520-4d5e-a4bc-5e4fede4e6f1")
        public static final MotorType kFrontRight = new MotorType(kFrontRight_val);

        /**
         * motortype: rear left
         */
        @objid ("6acf3d2e-7cfb-438b-be95-18a1034f4b27")
        public static final MotorType kRearLeft = new MotorType(kRearLeft_val);

        /**
         * motortype: rear right
         */
        @objid ("6b3f6cec-a26c-4ece-acbe-b81d91470dad")
        public static final MotorType kRearRight = new MotorType(kRearRight_val);

        @objid ("e9aaecfa-20ca-4b7a-b2ce-7eff9b18ede6")
        private MotorType(int value) {
            this.value = value;
        }

    }

}
