
package modules;

import config.ShooterConfig;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Watchdog;

/**
 * controls the shooter
 * @author Ben Evans 
 * @author Michael Chin
 */
public class ShooterModule extends Module{
    
    private Solenoid lifter;
    private Solenoid shifter;
    private Victor winch1, winch2;
    private DigitalInput winchSensor;
    private Encoder winchEncoder;
    private PIDController winchController;
    
    
    //TODO enums?
    public static int READY = 0;
    public static int LIFTING_PNEUMATIC = 1;
    public static int SHOOTING = 2;
    public static int RELOADING = 3;
    
    private int mode;
    private boolean manual = true;
    private double winchPower = 0;
    private boolean shifterGear = true;
    
    
/**
 * the constructor for this class
 * @param lift
 * @param shift
 * @param win1
 * @param win2
 * @param button 
 */    
    public ShooterModule(int lift, int shift, int win1, int win2, int button, int encA, int encB){
        lifter = new Solenoid(lift);
        shifter = new Solenoid(shift);
        winch1 = new Victor(win1);
        winch2 = new Victor(win2);
        winchSensor = new DigitalInput(button);
        winchEncoder = new Encoder(encA, encB);
        
        mode = READY;
        
        winchController = new PIDController(ShooterConfig.KP, ShooterConfig.KI, ShooterConfig.KD, winchEncoder, new PIDOutput() {

            public void pidWrite(double d) {
                winch1.set(d);
                winch2.set(d);
            }
        });
     
        winchController.setOutputRange(0, 1);
        
    }
/**
 * shoots, lifts Pneumatic
 */
    public void shoot(){
       if(mode == READY)
           mode = LIFTING_PNEUMATIC;
    }
 /**
  * 
  * @return mode 
  */   
    public int getMode(){
        return mode;
    }
/**
 * 
 * @return mode == Ready 
 */    
    public boolean isReady(){
        return mode == READY;
    }
    
    public void setIntake(boolean up){
        lifter.set(up);
    }
    
    public synchronized void setManual(boolean man){
        this.manual = man;
    }
    
    public synchronized void setWinchPower(double power){
        this.winchPower = power;
    }
    
    public synchronized void setGear(boolean engaged){
        this.shifterGear = engaged;
    }
    
    public void setPID(double p, double i, double d){
        winchController.setPID(p, i, d);
    }
    
/**
 * handles shooter state
 */ 
    public void run(){
        long stateTimer = 0;
        
        while(true){
            if(enabled){
                if(manual){
                    winchController.disable();
                }else{
                //state machine
                    winchController.enable();
                    
                    if(mode == READY){
                        stateTimer = System.currentTimeMillis();
                    }else if(mode == LIFTING_PNEUMATIC){
                        lifter.set(true);
                        if((System.currentTimeMillis() - stateTimer) > 1000){
                            mode = SHOOTING;
                            stateTimer = System.currentTimeMillis();
                        }
                    }else if(mode == SHOOTING){
                        shifter.set(false);
                        if((System.currentTimeMillis() - stateTimer) > 1000){
                            winchController.setSetpoint(0);
                            shifter.set(true);
                            mode = RELOADING;
                            stateTimer = System.currentTimeMillis();
                        }
                    }else if(mode == RELOADING){
                        lifter.set(false);
                        winchController.setSetpoint(ShooterConfig.LOADED_TICKS);
                        if(winchSensor.get())
                            mode = READY;
                    }else{
                        //problem
                    }
                }
                
                if(winchSensor.get()){
                    winchPower = 0;
                }
                
                winch1.set(winchPower);
                winch2.set(winchPower);
                shifter.set(shifterGear);
            }
                        
            Timer.delay(0.05);
        }
    }
/**
 * 
 * @return winch charge status and state 
 */    
    public String toString()
    {
        return "Winch Charge Status: " + winchSensor.get() + " State: " + getState() + " Winch: " + winchPower;
    }
    
    public String getLogData(){
        String line1 = "\t\t<data name=\"lifter\" value=\""+(lifter.get() ? "ON" : "OFF")+"\">\n";
        String line2 = "\t\t<data name=\"dog\" value=\""+(shifter.get() ? "ON" : "OFF")+"\">\n";
        String line3 = "\t\t<data name=\"winch\" value=\""+winch1.get()+"\">\n";
        String line4 = "\t\t<data name=\"state\" value=\""+getState()+"\">";
        return line1+line2+line3+line4;
    }
    
/**
 * 
 * @return state
 */    
    public String getState(){
        if (mode == READY){
            return "SHOOTER_READY";
        }else if(mode == LIFTING_PNEUMATIC){
            return "LIFTING_PNEUMATIC";
        }else if(mode == SHOOTING){
            return "FIRING_BALLZ";
        }else if(mode == RELOADING){
            return "RELOADING";
        }else{
            return "NOT_READY";
        }
    }
}
