/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/





import config.DriveConfig;
import config.ShooterConfig;
import edu.wpi.first.wpilibj.IterativeRobot;
import modules.DriveModule;
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
    
    public void robotInit(){
        driveModule = new DriveModule(DriveConfig.LEFT_VICTOR_ONE, DriveConfig.LEFT_VICTOR_TWO, DriveConfig.RIGHT_VICTOR_ONE, DriveConfig.RIGHT_VICTOR_TWO, DriveConfig.LEFT_ENCODER_A, DriveConfig.LEFT_ENCODER_B, DriveConfig.RIGHT_ENCODER_A, DriveConfig.RIGHT_ENCODER_B, DriveConfig.DISTANCE_PER_TICK, DriveConfig.GYRO);
        shooterModule = new ShooterModule(ShooterConfig.LIFTER, ShooterConfig.ROLLER, ShooterConfig.SHIFTER, ShooterConfig.WINCH1, ShooterConfig.WINCH2, ShooterConfig.TOUCH_SENSOR);
        
    }
    
    public void startCompetition(){
        driveModule.start();
        shooterModule.start();
    }
    
    public void disabledInit(){
        driveModule.disable();
        shooterModule.disable();
        driveModule.reset();
        shooterModule.reset();
    }
    
    public void disabledPeriodic(){
        
    }
    
    public void autonomousInit(){
        shooterModule.enable();
        driveModule.enable();
    }
    
    public void autonomousPeriodic(){
        
    }
    
    public void teleopInit(){
        shooterModule.enable();
        driveModule.enable();
    }
    
    public void teleopPeriodic(){
        
    }
    
}
