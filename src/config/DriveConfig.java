package config;

/**
 *
 * @author Ben Evans
 */
public class DriveConfig {
    //Input and output
    public static final int LEFT_VICTOR_ONE = 1;
    public static final int LEFT_VICTOR_TWO = 2;
    public static final int RIGHT_VICTOR_ONE = 3;
    public static final int RIGHT_VICTOR_TWO = 4;
    public static final double DISTANCE_PER_TICK = 0.00609756097560976;
    public static final int LEFT_ENCODER_A = 4, LEFT_ENCODER_B = 5;
    public static final int RIGHT_ENCODER_A = 2, RIGHT_ENCODER_B = 3;
    public static final double KP = 0.6, KI = 0, KD = 1, KS = 1;//0.575;
    public static final int SOLENOID_PORT = 1;
    public static final int ULTRASONIC_PORT = 7;
    public static final double DRIVE_EXPONENT = 1.64;
}
 