

package modules;

import config.DriveConfig;
import edu.wpi.first.wpilibj.*;
/**
 *
 * @author Ben Evans
 * @author Ankith Uppunda
 */
public class DriveModule extends Module{
    
    private Victor lVictor1, lVictor2, rVictor1, rVictor2;
    private Encoder lEncoder, rEncoder;
    private PIDSource encoderSource;
    private double distancePerPulse;
    private PIDController driveController;
    private PIDOutput driveOutput;
    private double leftPower = 0, rightPower = 0;
    private Solenoid solenoid;
/**
 * constructor
 * @param lv1
 * @param lv2
 * @param rv1
 * @param rv2
 * @param lenca
 * @param lencb
 * @param renca
 * @param rencb
 * @param dist
 * @param solenoidPort 
 */    
    public DriveModule(int lv1, int lv2, int rv1, int rv2, int lenca, int lencb, int renca, int rencb, double dist, int solenoidPort){
        lVictor1 = new Victor(lv1);
        lVictor2 = new Victor(lv2);
        rVictor1 = new Victor(rv1);
        rVictor2 = new Victor(rv2);
        
        lEncoder = new Encoder(lenca, lencb);
        rEncoder = new Encoder(renca, rencb);
        encoderSource = new DualEncoder();
        distancePerPulse = dist;
        solenoid = new Solenoid(solenoidPort);
        setupEncoders();
        
        driveOutput = new DriveOutput();
        driveController = new PIDController(DriveConfig.ENCODER_P, DriveConfig.ENCODER_I, DriveConfig.ENCODER_D, encoderSource, driveOutput);
        driveController.setOutputRange(-1.0, 1.0);
        
    }
/**
 * starts encoder and sets distance
 */    
    private synchronized void setupEncoders(){
        lEncoder.start();
        rEncoder.start();
        lEncoder.setDistancePerPulse(distancePerPulse);
        rEncoder.setDistancePerPulse(distancePerPulse);
    }
/**
 * resets encoder, drive controller
 */    
    public synchronized void resetEncoders(){
        lEncoder.reset();
        rEncoder.reset();
        driveController.reset();
    }
 /**
  * sets gear
  * @param High 
  */    
    public synchronized void setGear(boolean High){   
        solenoid.set(High);
    }
/**
 * reset encoders
 */    
    public void reset(){
        resetEncoders();
    }
/**
 * sets victors 
 * @param left
 * @param right 
 */    
    private synchronized void setPower(double left, double right){
        lVictor1.set(left);
        lVictor2.set(left);
        rVictor1.set(-right);
        rVictor2.set(-right);
    }
/**
 * sets leftPower to left and rightPower to right
 * @param left
 * @param right 
 */
    public synchronized void drive(double left, double right){
        leftPower = left;
        rightPower = right;
    }
/**
 * 
 * @return leftPower 
 */    
    public synchronized double getLeftPower(){
        return leftPower;
    }
/**
 * 
 * @return rightPower
 */    
    public synchronized double getRightPower(){
        return rightPower;
    }
/**
 * set p,i,d constants for drive controller
 * @param p proportional constant
 * @param i integration
 * @param d derivative
 */    
    public synchronized void setPID(double p, double i, double d){
        driveController.setPID(p, i, d);
    }
/**
 * sets point to variable distance
 * @param distance 
 */    
    public synchronized void setDistance(double distance){
        driveController.setSetpoint(distance);
    }
    
/**
 * handles driving
 */    
    public void run(){
        while(true){
            if(enabled){
                setPower(leftPower, rightPower);
                
                if(autoMode){
                    driveController.enable();
                }else{
                    driveController.disable();
                }
                
            }else{
                // reset here or main?
            }
            
            Timer.delay(0.05);
        }
    }
/**
 * deals with PIDOutput interface
 * @author Ben Evans
 */    
     private class DriveOutput implements PIDOutput{
/**
 * sets drive to double d 
 * @param d 
 */
        public void pidWrite(double d) {
            //not sure if this will work
            drive(d, d);
        }
       
    }    
/**
 * deals with PIDSource interface
 * @author Ben Evans
 */    
    private class DualEncoder implements PIDSource{
/**
 * 
 * @return encoder distance
 */        
        public double pidGet(){
            return (lEncoder.getDistance() + rEncoder.getDistance()) / 2.0;
        }
    }
}
