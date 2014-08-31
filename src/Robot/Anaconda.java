package Robot;

/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/




import util.XBox;
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
import edu.wpi.first.wpilibj.DriverStationLCD;
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
    
    static Anaconda instance;

    //Modules
    DriveModule driveModule;
    ShooterModule shooterModule;
    CompressorModule compressorModule;
    ArmModule armModule;
    
    DriverStationLCD driverStation;
    
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
                
        driverStation = DriverStationLCD.getInstance();
        driverStation.println(DriverStationLCD.Line.kUser1, 1, "Starting up robot!");
        driverStation.updateLCD();
        
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
        
        driveModule.setDriveExponent(DriveConfig.DRIVE_EXPONENT);
        
        instance = this;
    }
    
    public void disabledInit(){
        driveModule.disable();
        driveModule.reset();
        shooterModule.disable();
        compressorModule.disable();
        armModule.disable();
        armModule.setPID(ArmConfig.p, ArmConfig.i, ArmConfig.d);
        armModule.setPosition(ArmConfig.ARM_INPUT_MIN);
    }

    public void reset(){
        driveModule.reset();
        shooterModule.reset();
        compressorModule.reset();
        armModule.reset();
    }
    
    public void disabledPeriodic(){
        driveModule.reset();
        Timer.delay(0.1);
    }

    public void autonomousInit(){

        System.out.println("autonomousInit()");
        compressorModule.enable();
        armModule.enable();
        shooterModule.enable();
        shooterModule.setManual(false);
        driveModule.enable();
        driveModule.setGear(false);
        driveModule.setAutoModeOn();
        driveModule.reset();

        driveModule.setS(.95);
        
        
        if(true){
            armModule.setRoller(-0.3);

            System.out.println("Setting arm");
            armModule.setMedPosition();
            Timer.delay(1);
            //Move forward
            System.out.println("Moving");
            driveModule.setSetpoint(11.5/2+0.5);
            Timer.delay(5);
            armModule.setRoller(0.0);
            //shoot
            System.out.println("Shooting");
            shooterModule.shoot();
        }
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
        driveModule.disablePID();
        driveModule.setAutoModeOff();
    }

    int infoCounter = 0;
    boolean gear = false;
    int driveModeCounter = 0;
    boolean lastDriveMode = false;
    double armPercent = 0;
    
    public void teleopPeriodic(){
        
        if(rJoy.getRawButton(5))
            gear = true;
        else if(rJoy.getRawButton(4))
            gear = false;
        
        if(lJoy.getRawButton(7) != lastDriveMode)
            driveModeCounter++;
        lastDriveMode =lJoy.getRawButton(7);
        
        if(driveModeCounter % 4 == 0)
            driveModule.drive(-lJoy.getY(), -rJoy.getY());
        else
            driveModule.arcadeDrive(-lJoy.getY(), -rJoy.getX());
        
        driveModule.setGear(gear);
        
                
        armModule.setRoller(xbox.getLY());
        shooterModule.setIntake(xbox.getStart());
                
        if(rJoy.getRawButton(9))
            shooterModule.setWinchPower(ShooterConfig.WINCH_POWER);
        else if(rJoy.getRawButton(8))
            shooterModule.setWinchPower(-ShooterConfig.WINCH_POWER/2.0);
        else
            shooterModule.setWinchPower(0);
         
        
        if(xbox.getLB()){
            if(xbox.getRB()){
                shooterModule.setManual(false);
                shooterModule.shoot();
            }else if(xbox.getBack()){
                shooterModule.setManual(false);
                shooterModule.tobyShoot();
            }
        }
        
        if(rJoy.getTrigger() && lJoy.getTrigger()){
            shooterModule.setManual(false);
            shooterModule.shoot();
        }
        
        
        if(xbox.getB())
            armModule.setMedPosition();
        
        if(xbox.getA())
            armModule.setLowPosition();
        
        if(xbox.getYb())
            armModule.setHighPosition();
        
        if(xbox.getXb()){
            //armModule.setArmPercent((lJoy.getZ() + 1)/2.0);
            armModule.setTrussPostition();
        }
        
        if(xbox.getBack()){
            armPercent = armModule.getArmConstant();
            armPercent += xbox.getRY() / 100;
            armModule.setArmPercent(armPercent);
        }
        
        if(infoCounter % 5 == 0){
            System.out.println(armModule);
            driverStation.clear();
            driverStation.println(DriverStationLCD.Line.kUser1, 1, "Exp: " + driveModule.getDriveExponent() + " Per: " + armModule.getArmConstant());
            driverStation.println(DriverStationLCD.Line.kUser2, 1, "Compressed: " + !compressorModule.isPressureSwitchPressed());
            driverStation.println(DriverStationLCD.Line.kUser3, 1, "Winched: " + shooterModule.isButtonPressed());
            driverStation.println(DriverStationLCD.Line.kUser4, 1, "High Gear: " + driveModule.getGear());
            driverStation.println(DriverStationLCD.Line.kUser5, 1, "Drive Mode: " + (driveModeCounter % 4 == 0 ? "Tank" : "Arcade"));
            driverStation.updateLCD();
        }
        infoCounter++;
                
        Timer.delay(0.05);
        

    }
    
    public void testInit(){
        armModule.disable();
        driveModule.disable();
        driveModule.setAutoModeOn();
        shooterModule.disable(); 
        compressorModule.enable();
        armModule.setPosition(ArmConfig.ARM_INPUT_MIN);
    }
    
    int testCounter = 0;
    
    public void testPeriodic(){
                
        if(testCounter % 20 == 0){
            System.out.println(armModule);
        }
        
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
    
    public static Anaconda getInstance(){
        return instance;
    }
    
}
