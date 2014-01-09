/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modules;

import config.DriveConfig;
import edu.wpi.first.wpilibj.*;
/**
 *
 * @author Ben Evans
 * 
 */
public class DriveModule extends Module{
    
    private Victor lVictor1, lVictor2, rVictor1, rVictor2;
    private Encoder lEncoder, rEncoder;
    private PIDSource encoderSource;
    private double distancePerPulse;
    private Gyro gyro;
    private PIDController angleController, driveController;
    private PIDOutput angleOutput, driveOutput;
    private double leftPower = 0, rightPower = 0;
    
    public DriveModule(int lv1, int lv2, int rv1, int rv2, int lenca, int lencb, int renca, int rencb, double dist, int gyroc){
        lVictor1 = new Victor(lv1);
        lVictor2 = new Victor(lv2);
        rVictor1 = new Victor(rv1);
        rVictor2 = new Victor(rv2);
        
        lEncoder = new Encoder(lenca, lencb);
        rEncoder = new Encoder(renca, rencb);
        encoderSource = new DualEncoder();
        distancePerPulse = dist;
        gyro = new Gyro(gyroc);
        
        angleOutput = new AngleOutput();
        angleController = new PIDController(DriveConfig.GYRO_P, DriveConfig.GYRO_I, DriveConfig.GYRO_D, gyro, angleOutput);
        
        setupEncoders();
        
        driveOutput = new DriveOutput();
        driveController = new PIDController(DriveConfig.ENCODER_P, DriveConfig.ENCODER_I, DriveConfig.ENCODER_D, encoderSource, driveOutput);
        
    }
    
    private synchronized void setupEncoders(){
        lEncoder.start();
        rEncoder.start();
        lEncoder.setDistancePerPulse(distancePerPulse);
        rEncoder.setDistancePerPulse(distancePerPulse);
    }
    
    public synchronized void resetEncoders(){
        lEncoder.reset();
        rEncoder.reset();
        driveController.reset();
    }
    
    public synchronized void resetGyro(){
        gyro.reset();
        angleController.reset();
    }
        
    public void reset(){
        resetGyro();
        resetEncoders();
    }
    
    private synchronized void setPower(double left, double right){
        lVictor1.set(left);
        lVictor2.set(left);
        rVictor1.set(-right);
        rVictor2.set(-right);
    }

    public synchronized void drive(double left, double right){
        leftPower = left;
        rightPower = right;
    }
    public synchronized double getLeftPower(){
        return leftPower;
    }
    
    public synchronized double getRightPower(){
        return rightPower;
    }
    
    public void run(){
        while(true){
            if(enabled){
                setPower(leftPower, rightPower);
                
                if(autoMode){
                    angleController.enable();
                    driveController.enable();
                }else{
                    
                }
                
            }else{
                // reset here or main?
            }
            
            Timer.delay(0.05);
        }
    }
    
    //TODO add methods to change pid values
 
    private class AngleOutput implements PIDOutput{

        public void pidWrite(double d) {
            //not sure if this will work
            drive(d, -d);
        }
       
    }    
    
    private class DriveOutput implements PIDOutput{

        public void pidWrite(double d) {
            //not sure if this will work
            drive(d, d);
        }
       
    }    
    
    private class DualEncoder implements PIDSource{
        public double pidGet(){
            return (lEncoder.pidGet() + rEncoder.pidGet()) / 2.0;
        }
    }
}
