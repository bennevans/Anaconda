package modules;

import config.ArmConfig;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Timer;
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
    
    private double armPower = 0, setPoint;
    
    /**
     *  Constructor for ArmModule
     * @param rollerPort roller port
     * @param armPort arm port
     * @param potPort potentiometer port
     */
    
    public ArmModule(int armPort, int rollerPort, int potPort){
       pot = new AnalogPotentiometer(potPort); 
       controller = new PIDController(ArmConfig.p, ArmConfig.i, ArmConfig.d,pot,this);
       roller = new Victor(rollerPort);
       arm = new Victor(armPort);
       
       controller.setInputRange(ArmConfig.ARM_MIN, ArmConfig.ARM_MAX);
       controller.setOutputRange(-1, 1);
    }

    public synchronized void setRoller(double value){
        roller.set(value);
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
       this.setPoint = setposition;
    }
    
    public synchronized void setLowPosition(){
        //TODO: fix this
        setPosition(ArmConfig.ARM_MAX);
    }
    
    public synchronized void setHighPosition(){
        setPosition(ArmConfig.ARM_MIN);
    }
    
    public synchronized void setMedPosition(){
        setPosition((ArmConfig.ARM_MAX + ArmConfig.ARM_MIN) / 2.75);
    }
    
    public synchronized void setLowGoalPosition(){
        setPosition((ArmConfig.ARM_MIN + ArmConfig.ARM_MAX)/ 3.0);
    }
    
    public synchronized void setArmPower(double armPower){
        this.armPower = armPower;
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
                controller.setSetpoint(setPoint);
            }
            else
            {
                controller.disable();
                controller.reset();
                arm.set(armPower);
            }
            
            
            Timer.delay(0.05);
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
    
    public double getPotValue(){
        return pot.get();
    }
    
    /**
     * toString for ArmModule 
     * @return current field values as a string
     */
    public String toString(){
        return "P: " + controller.getP() + " D: " + controller.getD() + " Arm: " + arm.get() + " Setpoint: " + controller.getSetpoint() + " Roller Value: " + roller.get() + " Pot Value: " + pot.get() + " PID error: " + controller.getError();
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
