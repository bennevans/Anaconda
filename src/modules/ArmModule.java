package modules;

import config.ArmConfig;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;

/**
 * ArmModule, the class, defines through various methods what the arm on our robot
 * will be able to do. It has methods that sets the arm in a specific position or in any
 * position.
 * @author Ankith Uppunda
 * @author Ben Evans
 */
public class ArmModule extends Module implements PIDOutput {
    private PIDController controller;
    private Potentiometer pot;
    private Victor roller, arm;
    
    /**
     *  Constructor for ArmModule
     * @param rollerPort roller port
     * @param armPort arm port
     * @param potPort potentiometer port
     */
    
    public ArmModule(int rollerPort, int armPort, int potPort){
       pot = new AnalogPotentiometer(potPort);
       controller = new PIDController(ArmConfig.p, ArmConfig.i, ArmConfig.d,pot,this);
       roller = new Victor(rollerPort);
       arm = new Victor(armPort);
    }
    /**
     * sets the roller on or off
     * @param on state of the roller
     */
    public synchronized void setRoller(boolean on){
        if(on){
            roller.set(ArmConfig.ROLLER_SPEED);
        }else{
            roller.set(0);
        }
    }
    
    /**
     * sets p,i,d constants
     * @param p proportional constant
     * @param i integral constant
     * @param d derivative constant
     *
     */
    public synchronized void setPID(double p, double i, double d){
        controller.setPID(p, i, d);
    }
    /**
     * sets position of the arm
     * @param setposition
     */
    public synchronized void setPosition(double setposition)
    {
       double setPositionB = setposition;
       controller.setSetpoint(setPositionB);
    }
    /**
     * handles arm controller
     */
    public void run(){
        while(true)
        {
            if(enabled)
            {
                controller.enable();
            }
            else
            {
                controller.disable();
                controller.reset();
            }
        }
    }
    /**
     * resets arm
     */
    public void reset(){
       controller.reset();
    }
    /**
     * implementation of pidOutput
     * @param d pid output value
     */
    public void pidWrite(double d) {
       arm.set(d);
    }
    /**
     * toString for ArmModule 
     * @return current field values as a string
     */
    public String toString(){
        return "Roller Value: " + roller.get() + " Pot Value: " + pot.get() + " PID error: " + controller.getError();
    }

    /**
     * the log function for ArmModule
     * @return log data as XML
     */
    
    public String getLogData(){
        String line1 = "\t\t<data name=\"roller\" value=\""+roller.get()+"\"/>\n";
        String line2 = "\t\t<data name=\"pot\" value=\""+pot.get()+"\"/>\n";
        String line3 = "\t\t<data name=\"arm\" value=\""+arm.get()+"\"/>";
        return line1 + line2 + line3;
    }
    
}
