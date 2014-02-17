
package modules;

import config.ShooterConfig;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

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
    
    private boolean manual = true;
    private double winchPower = 0;
    private boolean shifterGear = true, lifterState = false;
    boolean shoot = false;
    
    
/**
 * the constructor for this class
 * @param lift
 * @param shift
 * @param win1
 * @param win2
 * @param button 
 */    
    public ShooterModule(int lift, int shift, int win1, int win2, int button){
        lifter = new Solenoid(lift);
        shifter = new Solenoid(shift);
        winch1 = new Victor(win1);
        winch2 = new Victor(win2);
        winchSensor = new DigitalInput(button);
        
        mode = READY;
    }
/**
 * shoots, lifts Pneumatic
 */
    public synchronized void shoot(){
        
        lifter.set(true);
        Timer.delay(1);
        
        shifter.set(true);
        Timer.delay(1);
        
        shifter.set(false);
        Timer.delay(1);
        
        while(winchSensor.get()){
            winch1.set(ShooterConfig.WINCH_POWER);
            winch2.set(ShooterConfig.WINCH_POWER);
        }    
        
        winch1.set(0);
        winch2.set(0);
    }
/**
 * 
 * @return mode == Ready 
 */    

    public synchronized void setIntake(boolean up){
        lifterState = up;
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
    
/**
 * handles shooter state
 */ 
    public void run(){
        
        while(true){
            
            if(!winchSensor.get()){
                winch1.set(0);
                winch2.set(0);
            }

            if(enabled){

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
        return "Button: " + !winchSensor.get() + " State: " + getState() + " Winch: " + winch1.get() + " manual: " + manual + " shift: " + shifterGear + " lifter: " +lifterState + " shoot: " + shoot;
    }
    
    public String getLogData(){
        String line1 = "\t\t<data name=\"lifter\" value=\""+(lifter.get() ? "ON" : "OFF")+"\">\n";
        String line2 = "\t\t<data name=\"dog\" value=\""+(shifter.get() ? "ON" : "OFF")+"\">\n";
        String line3 = "\t\t<data name=\"winch\" value=\""+winch1.get()+"\">\n";
        String line4 = "\t\t<data name=\"state\" value=\""+getState()+"\">";
        return line1+line2+line3+line4;
    }
    
}
