

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
    private double distancePerPulse;
    private double leftPower = 0, rightPower = 0;
    private Solenoid gear;
    
    private PIDController lcont, rcont;
    double setpoint = 0;
    private double ks = 0, s = 0;
    private double error = 0;
         
    /**
     * constructor
     * @param lv1 left victor 1 port
     * @param lv2 left victor 2 port
     * @param rv1 right victor 1 port
     * @param rv2 right victor 2 port
     * @param lenca left encoder port a
     * @param lencb left encoder port b
     * @param renca right encoder port a
     * @param rencb right encoder port b
     * @param dist distance per encoder tick
     * @param solenoidPort solenoid port
     */    
    public DriveModule(int lv1, int lv2, int rv1, int rv2, int lenca, int lencb, int renca, int rencb, double dist, int solenoidPort){
        lVictor1 = new Victor(lv1);
        lVictor2 = new Victor(lv2);
        rVictor1 = new Victor(rv1);
        rVictor2 = new Victor(rv2);
        
        lEncoder = new Encoder(lenca, lencb);
        rEncoder = new Encoder(renca, rencb);
        distancePerPulse = dist;
        gear = new Solenoid(solenoidPort);
        setupEncoders();
        
        lcont = new PIDController(DriveConfig.KP, DriveConfig.KI, DriveConfig.KD, new PIDSource() {

            public double pidGet() {
                return lEncoder.getDistance();
            }
        }, new PIDOutput() {

            public void pidWrite(double d) {
                lVictor1.set(d + s);
                lVictor2.set(d + s);
            }
        });
        
        rcont = new PIDController(DriveConfig.KP, DriveConfig.KI, DriveConfig.KD, new PIDSource() {

            public double pidGet() {
                return rEncoder.getDistance();
            }
        }, new PIDOutput() {

            public void pidWrite(double d) {
                rVictor1.set(-d + s);
                lVictor2.set(-d + s);
            }
        });
        
        ks = DriveConfig.KS;
        
        lcont.setOutputRange(-0.5, 0.5);
        rcont.setOutputRange(-0.5, 0.5);
        
    }
    /**
     * starts encoder and sets distance
     */    
    private synchronized void setupEncoders(){
        lEncoder.start();
        rEncoder.start();
        lEncoder.setDistancePerPulse(distancePerPulse);
        rEncoder.setDistancePerPulse(distancePerPulse);
        rEncoder.setReverseDirection(true);
    }
    
    public synchronized void disablePID(){
        lcont.disable();
        rcont.disable();
    }
    
    /**
     * resets encoder, drive controller
     */    
    public synchronized void resetEncoders(){
        lEncoder.reset();
        rEncoder.reset();
    }
    /**
     * sets gear
     * @param High 
     */    
    public synchronized void setGear(boolean High){   
        gear.set(High);
    }
    /**
     * reset encoders
     */    
    public void reset(){
        resetEncoders();
        lcont.reset();
        rcont.reset();
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
    
    public synchronized void setSetpoint(double setpoint){
        this.setpoint = setpoint;
    }
    
    /**
     * returns power of left side
     * @return leftPower 
     */    
    public synchronized double getLeftPower(){
        return leftPower;
    }
    /**
     * return power of right side
     * @return rightPower
     */    
    public synchronized double getRightPower(){
        return rightPower;
    }
    
    /**
     * the log method for DriveModule
     * @return the log data as XML
     */
    
    public String getLogData(){
        String line1 = "\t\t<data name=\"leftPower\" value=\""+lVictor1.get()+"\">\n";
        String line2 = "\t\t<data name=\"rightPower\" value=\""+rVictor1.get()+"\">\n";
        String line3 = "\t\t<data name=\"leftEncoder\" value=\""+lEncoder.get()+"\">\n";
        String line4 = "\t\t<data name=\"rightEncoder\" value=\""+rEncoder.get()+"\">\n";
        String line5123445454 = "\t\t<data name=\"gear\" value=\""+(gear.get() ? "HIGH" : "LOW")+"\">";
        return line1+line2+line3+line4+line5123445454;
    }
    
    /**
     * implementation of run method for modules
     */    
    public void run(){
        while(true){
            if(enabled){
                
                if(autoMode){
                    setGear(false);
                    updateS();
                    lcont.enable();
                    rcont.enable();
                    lcont.setSetpoint(setpoint);
                    rcont.setSetpoint(setpoint);
                }else{
                    lcont.disable();
                    rcont.disable();
                    lcont.setSetpoint(0);
                    rcont.setSetpoint(0);
                    setPower(leftPower, rightPower);
                }
                
            }else{
                // reset here or main?
            }
                        
            Timer.delay(0.05);
        }
    }
        
    public synchronized void updateS(){
        error = lcont.get() - rcont.get();
        s = error * ks;
    }
    
    public synchronized void setStraightConstant(double ks){
        this.ks = ks;
    }
    
    public synchronized void setPID(double p, double i, double d){
        lcont.setPID(p, i, d);
        rcont.setPID(p, i, d);
    }
    
    public synchronized String toString(){
        return "Left: " + lVictor1.get() + " Right: " + rVictor1.get() + " Auto: " + autoMode + " LTicks: " + lEncoder.get() + " RTicks: " + rEncoder.get() 
                + " Distance: " + lEncoder.getDistance() + " Setpoint: " + rcont.getSetpoint() + " P: " + lcont.getP() + " I: " + lcont.getI() + " D: " + lcont.getD();
    }
}
