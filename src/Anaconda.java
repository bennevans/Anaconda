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
        
        armModule.setPID(0.7, 0, 3.5);
        
        Watchdog.getInstance().setEnabled(false);
        Watchdog.getInstance().kill();
        
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
        System.out.println("teleopInit()");
        shooterModule.enable();
        shooterModule.setManual(true);
        driveModule.enable();
        armModule.enable();
        compressorModule.enable();
        
        driveModule.setAutoModeOff();
    }

    int infoCounter = 0;
    
    int shootCounter = 0;
    boolean lastShoot = false;
    
//    int dshootCounter = 0;
//    boolean dlastShoot = false;
    
    boolean gear = false;
    
    public void teleopPeriodic(){
        
        if(rJoy.getRawButton(5))
            gear = true;
        else if(rJoy.getRawButton(4))
            gear = false;
        
        driveModule.setGear(gear);
        
        driveModule.drive(-lJoy.getY(), -rJoy.getY());
        
        armModule.setRoller(xbox.getLY());
        shooterModule.setIntake(xbox.getBack());
                
        if(rJoy.getRawButton(9))
            shooterModule.setWinchPower(ShooterConfig.WINCH_POWER);
        else if(rJoy.getRawButton(8))
            shooterModule.setWinchPower(-ShooterConfig.WINCH_POWER/2.0);
        else
            shooterModule.setWinchPower(0);
         
        if(lastShoot != xbox.getRB())
            shootCounter++;
        lastShoot = xbox.getRB();
        
//        if(dlastShoot != rJoy.getTrigger())
//            dshootCounter++;
//        dlastShoot = rJoy.getTrigger();
        
        if(shootCounter % 4 == 0){
            shooterModule.setManual(true);
        }else if(shootCounter % 2 == 0){
            if(xbox.getLB() && !shooterModule.isShooting()){
                System.out.println("XBOX SHOOT");
                shooterModule.setManual(false);
                shooterModule.shoot();
            }                    
            shootCounter += 2;
        }
//        
//        if(dshootCounter % 4 == 0){
//            shooterModule.setManual(true);
//        }else if(dshootCounter % 2 == 0){
//            if(lJoy.getTrigger() && !shooterModule.isShooting()){
//                System.out.println("JOYSTICK SHOOT");
//                shooterModule.setManual(false);
//                shooterModule.shoot();
//            }                    
//            dshootCounter += 2;
//        }
        
        if(xbox.getB())
            armModule.setPosition((ArmConfig.ARM_MAX + ArmConfig.ARM_MIN) / 2.85);
        
        if(xbox.getA())
            armModule.setPosition(ArmConfig.ARM_MAX);
        
        if(xbox.getYb())
            armModule.setPosition(ArmConfig.ARM_MIN);
        
        if(infoCounter % 5 == 0){
            System.out.println(shooterModule.toString()+"\n"+armModule.toString());
        }
        infoCounter++;
                
        Timer.delay(0.05);
        

    }
    
    public void testInit(){
        armModule.enable();
        driveModule.enable();
        driveModule.setAutoModeOn();
        shooterModule.enable();
        compressorModule.enable();
        armModule.setPosition(ArmConfig.ARM_MIN);
    }
    
    int testCounter = 0;
    
    public void testPeriodic(){
        
//        if(rJoy.getTrigger()){
//            armModule.enable();
//            armModule.setPosition(1.96);
//        }else{
//            armModule.setPosition(3.9);
//            armModule.disable();
//            armModule.setArmPower(rJoy.getY());
//        }
        
        driveModule.setPID(rJoy.getZ()+1, 0, lJoy.getZ()+1);
        
        driveModule.setGear(false);
        
        if(lJoy.getTrigger())
            driveModule.reset();
        
        if(rJoy.getTrigger())
            driveModule.setSetpoint(5);
        else
            driveModule.setSetpoint(0);
            
        if(testCounter % 20 == 0)
            System.out.println(driveModule.toString());
        
//        armModule.setPID((rJoy.getZ()+1), 0.0075, (lJoy.getZ() + 1)*7);
        
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
