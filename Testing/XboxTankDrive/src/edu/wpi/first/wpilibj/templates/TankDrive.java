/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class TankDrive extends SimpleRobot {
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    
    XBox xbox;
    Victor left1, left2, right1, right2;
    Servo camera;
    
    
    protected void robotInit() {
        
        System.out.println("robotInit()");
        
        xbox = new Joystick(1);
        left1 = new Victor(1);
        left2 = new Victor(2);
        right1 = new Victor(3);
        right2 = new Victor(4);
        
        camera = new Servo(6);
    }
    
    
    
    public void autonomous() {   
    }
    
    /**
     * This function is called once each time the robot enters operator control.
     */
    
    public void operatorControl() {
        System.out.println("operatorControl()");
        int counter = 0;
        while(isOperatorControl() && isEnabled()){
            
            Watchdog.getInstance().kill();
            
            double leftpower = -xbox.getlY();
            double rightpower = xbox.getrY();
            
            leftpower = com.sun.squawk.util.MathUtils.pow(leftpower, 3);
            rightpower = com.sun.squawk.util.MathUtils.pow(rightpower, 3);
            
            left1.set(leftpower);
            left2.set(leftpower);
            right1.set(rightpower);
            right2.set(rightpower);
            
            //camera.setAngle((.getZ()+1) * 90);
            if(counter % 50 == 0){
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser1, 1, "LeftJoy: " + xbox.getlY());
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "RightJoy: " + xbox.getrY());
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "LeftPower: " + leftpower);
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser4, 1, "RightPower: " + rightpower);
                //DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser5, 1, "Servo: " + camera.getAngle());
                DriverStationLCD.getInstance().updateLCD();
            }
            
            counter++;
            
            Timer.delay(0.05);
        }
    }

}
