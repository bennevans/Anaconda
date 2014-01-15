/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/




import config.ArmConfig;
import config.CompressorConfig;
import config.DriveConfig;
import config.ShooterConfig;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import modules.DriveModule;
import modules.ArmModule;
import modules.ShooterModule;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Anaconda extends IterativeRobot {

    //Modules
    DriveModule driveModule;
    ShooterModule shooterModule;
    CompressorModule compressorModule;
    ArmModule armModule;

    //Driver controllers
    Joystick lJoy, rJoy;
    XBox xbox;
    
    
    public void robotInit(){
        driveModule = new DriveModule(DriveConfig.LEFT_VICTOR_ONE, DriveConfig.LEFT_VICTOR_TWO, DriveConfig.RIGHT_VICTOR_ONE, DriveConfig.RIGHT_VICTOR_TWO, DriveConfig.LEFT_ENCODER_A, DriveConfig.LEFT_ENCODER_B, DriveConfig.RIGHT_ENCODER_A, DriveConfig.RIGHT_ENCODER_B, DriveConfig.DISTANCE_PER_TICK);
        shooterModule = new ShooterModule(ShooterConfig.LIFTER, ShooterConfig.ROLLER, ShooterConfig.SHIFTER, ShooterConfig.WINCH1, ShooterConfig.WINCH2, ShooterConfig.TOUCH_SENSOR);
        compressorModule = new CompressorModule(CompressorConfig.COMPRESSOR_RELAY_CHANNEL, CompressorConfig.PRESSURE_SWITCH_CHANNEL);
        armModule = new ArmModule(ArmConfig.ARM_VICTOR_ONE, ArmConfig.ARM_VICTOR_TWO, ArmConfig.ROLLER_VICTOR_ONE, ArmConfig.ROLLER_VICTOR_TWO, ArmConfig.Encoder_port1, ArmConfig.Encoder_port2);
        
        lJoy = new Joystick(1);
        rJoy = new Joystick(2);
        xbox = new XBox(3);
        
        
    }

    public void startCompetition(){
        driveModule.start();
        shooterModule.start();
        compressorModule.start();
        armModule.start();
    }

    public void disabledInit(){
        driveModule.disable();
        shooterModule.disable();
        compressorModule.disable();
        armModule.disable();
        driveModule.reset();
        shooterModule.reset();
        compressorModule.reset();
        armModule.reset();
    }

    public void disabledPeriodic(){

    }

    public void autonomousInit(){
        shooterModule.enable();
        driveModule.enable();
        compressorModule.enable();
        armModule.enable();
        
        //Move forward
        driveModule.setDistance(0.0);
        Timer.delay(0.0);
        //shoot
        shooterModule.shoot();
        
    }

    public void autonomousPeriodic(){
        
    }

    public void teleopInit(){
        shooterModule.enable();
        driveModule.enable();
        compressorModule.enable();
        armModule.enable();
    }

    public void teleopPeriodic(){
        
        double leftPower = lJoy.getY();
        double rightPower = rJoy.getX();
        
        
    }
    
}
