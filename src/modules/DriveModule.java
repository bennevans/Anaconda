

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
    private PIDController driveController;
    private PIDOutput driveOutput;
    private double leftPower = 0, rightPower = 0;
    private Solenoid solenoid;
    
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
    public synchronized void setGear(boolean High){   
        solenoid.set(High);
    }
    public void reset(){
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
    
    public synchronized void setPID(double p, double i, double d){
        driveController.setPID(p, i, d);
    }
    
    public synchronized void setDistance(double distance){
        driveController.setSetpoint(distance);
    }
    
    
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
     private class DriveOutput implements PIDOutput{

        public void pidWrite(double d) {
            //not sure if this will work
            drive(d, d);
        }
       
    }    
    
    private class DualEncoder implements PIDSource{
        public double pidGet(){
            return (lEncoder.getDistance() + rEncoder.getDistance()) / 2.0;
        }
    }
}
