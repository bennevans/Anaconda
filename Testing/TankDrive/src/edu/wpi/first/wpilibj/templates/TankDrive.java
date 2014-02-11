/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.*;

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
    
    Joystick left, right;
    Victor left1, left2, right1, right2;
//    XBox xbox;
    Servo camera;
    
    int counter = 0;
    boolean last = false;
    
    protected void robotInit() {
        
        System.out.println("robotInit()");
        
        left = new Joystick(1);
        right = new Joystick(2);
        left1 = new Victor(1);
        left2 = new Victor(2);
        right1 = new Victor(3);
        right2 = new Victor(4);
        
        camera = new Servo(6);
    }
    
    
    
    public void autonomous() {   
    }
    
    /**
     * This function is called once each time the brobot enters operator control.
     */
    
    public void operatorControl() {
        System.out.println("operatorControl()");
        
        while(isOperatorControl() && isEnabled()){
            
            
            double leftpower = -left.getY();
            double rightpower = right.getY();
            
            
            leftpower = com.sun.squawk.util.MathUtils.pow(leftpower, 3);
            rightpower = com.sun.squawk.util.MathUtils.pow(rightpower, 3);
            
            
            if(right.getRawButton(3)){
                double tmp = rightpower;
                rightpower = leftpower;
                leftpower = tmp;
            }
                        
            left1.set(leftpower);
            left2.set(leftpower);
            right1.set(rightpower);
            right2.set(rightpower);
            
            camera.setAngle((right.getZ()+1) * 90);

            Timer.delay(0.1);
        }
    }

}
