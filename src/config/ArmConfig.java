package config;

/**
 *
 * @author Ankith Uppunda
 */
public class ArmConfig {
   public static final double p = 0.77;
   public static final double i = 0;
   public static final double d = 2.03;
   public static final int ROLLER_VICTOR = 6;
   public static final int ARM_VICTOR  = 10;//5;
   public static final int POT_PORT = 1;
   public static final int ROLLER_SPEED = 1;
   
   public static final boolean MOTOR_REVERSED = true;
   
   public static final double ARM_INPUT_MIN = 1.78, ARM_INPUT_MAX = 4.8;
   public static final double ARM_OUTPUT_MIN = -0.75, ARM_OUTPUT_MAX = 0.75;
   
   public static final double ARM_MED_CONST = 0.25;//0.35;
   public static final double ARM_TOBY_CONST = 0.3;
}