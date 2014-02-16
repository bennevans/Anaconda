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
import edu.wpi.first.wpilibj.Watchdog;
import java.io.PrintStream;
import javax.microedition.io.Connector;
import modules.CompressorModule;

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
        System.out.println("robotInit()");
        driveModule = new DriveModule(DriveConfig.LEFT_VICTOR_ONE, DriveConfig.LEFT_VICTOR_TWO, DriveConfig.RIGHT_VICTOR_ONE, DriveConfig.RIGHT_VICTOR_TWO, DriveConfig.LEFT_ENCODER_A, DriveConfig.LEFT_ENCODER_B, DriveConfig.RIGHT_ENCODER_A, DriveConfig.RIGHT_ENCODER_B, DriveConfig.DISTANCE_PER_TICK, DriveConfig.SOLENOID_PORT);
        shooterModule = new ShooterModule(ShooterConfig.LIFTER, ShooterConfig.WINCH_SHIFTER, ShooterConfig.WINCH_VICTOR1, ShooterConfig.WINCH_VICTOR2, ShooterConfig.TOUCH_SENSOR);
        compressorModule = new CompressorModule(CompressorConfig.COMPRESSOR_RELAY_CHANNEL, CompressorConfig.PRESSURE_SWITCH_CHANNEL);
        armModule = new ArmModule(ArmConfig.ARM_VICTOR, ArmConfig.ROLLER_VICTOR, ArmConfig.POT_PORT);
        
        lJoy = new Joystick(1);
        rJoy = new Joystick(2);
        xbox = new XBox(3);
        
        systemTime = new Timer();
        
        if(RobotConfig.LOGGING){
            try {
                logFile = (FileConnection) Connector.open("file:///logfile.xml", Connector.WRITE);
                logps = new PrintStream(logFile.openOutputStream());
                logps.println("<?xml version=\"1.0\"?>");
            } catch (Exception ex) {
                System.err.println("Error Opening logfile!");
                logFile = null;
                logps = null;
            }
        }
        
        driveModule.start();
        shooterModule.start();
        compressorModule.start();
        armModule.start();
        systemTime.start();
        
        Watchdog.getInstance().setEnabled(false);
        Watchdog.getInstance().kill();
        
        System.out.println("robotInit() done");
    }
    
    public void disabledInit(){
        driveModule.disable();
        shooterModule.disable();
        compressorModule.disable();
        armModule.disable();
    }

    public void reset(){
        driveModule.reset();
        shooterModule.reset();
        compressorModule.reset();
        armModule.reset();
    }
    
    public void disabledPeriodic(){
        System.out.println("disabled...");
        Timer.delay(0.5);
    }

    public void autonomousInit(){
        shooterModule.enable();
        driveModule.enable();
        compressorModule.enable();
        armModule.enable();
        
        //Move forward
        driveModule.setSetpoint(0.0);
        Timer.delay(0.0);
        //shoot
        shooterModule.shoot();
        
    }

    public void autonomousPeriodic(){
        
    }
    
    public void teleopInit(){
        shooterModule.enable();
        shooterModule.setManual(true);
        driveModule.enable();
        armModule.enable();
        compressorModule.enable();
        
        driveModule.setAutoModeOff();
    }

    int testShiftCounter = 0;
    boolean lastTestShiftState = false;
    
    int infoCounter = 0;
    
    public void teleopPeriodic(){
        
        if(rJoy.getRawButton(RobotConfig.SHIFT_BUTTON) != lastTestShiftState)
            testShiftCounter++;
        lastTestShiftState = rJoy.getRawButton(RobotConfig.SHIFT_BUTTON);
        
        if(testShiftCounter % 4 == 0)
            driveModule.setGear(true);
        else if(testShiftCounter %2 == 0)
            driveModule.setGear(false);
        
        driveModule.drive(-lJoy.getY(), -rJoy.getY());
        
        armModule.setRoller(xbox.getLY());
        shooterModule.setIntake(xbox.getBack());
        
        shooterModule.setWinchPower(xbox.getRY());
        shooterModule.setGear(xbox.getRB());
        
        if(infoCounter % 10 == 0){
            System.out.println(shooterModule.toString());
            System.out.println(Watchdog.getInstance().getEnabled());
        }
        infoCounter++;
                
        Timer.delay(0.05);
        

    }
    
    public void testInit(){
        
    }
    
    int testCounter = 0;
    
    public void testPeriodic(){
        
        if(rJoy.getTrigger()){
            armModule.enable();
            armModule.setPosition(1.96);
        }else{
            armModule.setPosition(3.9);
            armModule.disable();
            armModule.setArmPower(rJoy.getY());
        }
        
        
        if(testCounter % 10 == 0)
            System.out.println(armModule);
        
        armModule.setPID((rJoy.getZ()+1), 0.01, (lJoy.getZ() + 1)*2);
        
        testCounter++;
        
        Timer.delay(0.05);
        
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
