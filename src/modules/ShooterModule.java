
package modules;

import config.ShooterConfig;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

/**
 *controls the shooter
 * @author Ben Evans 
 * @author Michael Chin
 */
public class ShooterModule extends Module{
    
    private Solenoid lifter;
    private Solenoid shifter;
    private Victor roller;
    private Victor winch1, winch2;
    private DigitalInput winchSensor;
    
    //TODO enums?
    public static int READY = 0;
    public static int LIFTING_PNEUMATIC = 1;
    public static int SHOOTING = 2;
    public static int RELOADING = 3;
    
    private int mode;
    
/**
 * the constructor for this class
 * @param lift
 * @param roll
 * @param shift
 * @param win1
 * @param win2
 * @param button 
 */    
    public ShooterModule(int lift, int roll, int shift, int win1, int win2, int button){
        lifter = new Solenoid(lift);
        roller = new Victor(roll);
        shifter = new Solenoid(shift);
        winch1 = new Victor(win1);
        winch2 = new Victor(win2);
        winchSensor = new DigitalInput(button);
        mode = READY;
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
/**
 * handles shooter state
 */ 
    public void run(){
        long stateTimer = 0;
        
        while(true){
            if(enabled){
                //state machine
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
                        shifter.set(true);
                        mode = RELOADING;
                        stateTimer = System.currentTimeMillis();
                    }
                }else if(mode == RELOADING){
                    lifter.set(false);
                    winch1.set(ShooterConfig.WINCH_SPEED);
                    winch2.set(ShooterConfig.WINCH_SPEED);
                    if(winchSensor.get())
                        mode = READY;
                }else{
                    //problem
                }

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
        return "Winch Charge Status:" + winchSensor.get() + "State:" + getState();
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
