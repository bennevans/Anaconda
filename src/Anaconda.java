/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/




import config.ArmConfig;
import config.CompressorConfig;
import config.DriveConfig;
import config.RobotConfig;
import config.ShooterConfig;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import modules.DriveModule;
import modules.ArmModule;
import modules.ShooterModule;
import com.sun.squawk.microedition.io.FileConnection;
import java.io.IOException;
import java.io.PrintStream;
import javax.microedition.io.Connector;

/**
 * It is the mother of all classes that starts everything
 * @author Ben Evans
 * @author Ankith Uppundda
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
    
    //toggle switch variables
    int reverseButtonCounter = 0;
    boolean reverseButtonLastState = false;
    
    int shifterButtonCounter = 0;
    boolean shiftButtonLastState = false;
    
    //logging
    FileConnection logFile;
    PrintStream logps;
    Timer systemTime;
    
    
    public void robotInit(){
        driveModule = new DriveModule(DriveConfig.LEFT_VICTOR_ONE, DriveConfig.LEFT_VICTOR_TWO, DriveConfig.RIGHT_VICTOR_ONE, DriveConfig.RIGHT_VICTOR_TWO, DriveConfig.LEFT_ENCODER_A, DriveConfig.LEFT_ENCODER_B, DriveConfig.RIGHT_ENCODER_A, DriveConfig.RIGHT_ENCODER_B, DriveConfig.DISTANCE_PER_TICK, DriveConfig.SOLENOID_PORT);
        shooterModule = new ShooterModule(ShooterConfig.LIFTER, ShooterConfig.ROLLER, ShooterConfig.SHIFTER, ShooterConfig.WINCH1, ShooterConfig.WINCH2, ShooterConfig.TOUCH_SENSOR);
        compressorModule = new CompressorModule(CompressorConfig.COMPRESSOR_RELAY_CHANNEL, CompressorConfig.PRESSURE_SWITCH_CHANNEL);
        armModule = new ArmModule(ArmConfig.ARM_VICTOR_ONE, ArmConfig.ARM_VICTOR_TWO, ArmConfig.ROLLER_VICTOR_ONE, ArmConfig.ROLLER_VICTOR_TWO, ArmConfig.POT_PORT);
        
        lJoy = new Joystick(1);
        rJoy = new Joystick(2);
        xbox = new XBox(3);
        
        systemTime = new Timer();
        
        try {
            logFile = (FileConnection) Connector.open("logfile.xml", Connector.WRITE);
            logps = new PrintStream(logFile.openOutputStream());
            logps.println("<?xml version=\"1.0\"?>");
        } catch (IOException ex) {
            System.err.println("Error Opening logfile!");
            logFile = null;
            logps = null;
        }
        
        
    }

    public void startCompetition(){
        driveModule.start();
        shooterModule.start();
        compressorModule.start();
        armModule.start();
        systemTime.start();
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
        double rightPower = rJoy.getY();
        
        if(rJoy.getRawButton(RobotConfig.REVERSE_BUTTON) != reverseButtonLastState)
            reverseButtonCounter++;
        reverseButtonLastState = rJoy.getRawButton(RobotConfig.REVERSE_BUTTON);
        
        if(reverseButtonCounter % 4 == 0)
            driveModule.drive(leftPower, rightPower);
        else if(reverseButtonCounter % 2 == 0)
            driveModule.drive(-rightPower,-leftPower);
        
        if(rJoy.getRawButton(RobotConfig.SHIFT_BUTTON) != shiftButtonLastState)
            shifterButtonCounter++;
        shiftButtonLastState = rJoy.getRawButton(RobotConfig.SHIFT_BUTTON);
        
        if(shifterButtonCounter % 4 == 0)
            driveModule.setGear(true);
        else if(shifterButtonCounter % 2 == 0)
            driveModule.setGear(false);
        
        if((rJoy.getTrigger() || xbox.getRB()) && shooterModule.isReady())
            shooterModule.shoot();
        
        if(lJoy.getRawButton(RobotConfig.ROLLER_BUTTON))
            armModule.setRoller(true);
        else
            armModule.setRoller(false);
        
        log();

    }
    
    
    
    public void log(){
        if(logFile == null || logps == null)
            return;
        
        logps.println("<block time= \""+ systemTime.get() +"\">");
        logps.println("\t<module name=\"drive\">");
        logps.println(driveModule.getLogData());
        logps.println("\t</module>");
        logps.println("\t<module name=\"shooter\">");
        logps.println(shooterModule.getLogData());
        logps.println("\t</module>");
        logps.println("\t<module name=\"arm\">");
        logps.println(armModule.getLogData());
        logps.println("\t</module>");
        logps.println("\t<module name=\"compressor\">");
        logps.println(compressorModule.getLogData());
        logps.println("\t</module>");
        logps.println("</block>");
        
    }
    
}
