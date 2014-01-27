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
 */
public class ArmModule extends Module implements PIDOutput {
    private PIDController controller;
    private Potentiometer pot;
    private Victor roller1, roller2, arm1, arm2;
    public ArmModule(int v1, int v2, int v3, int v4, int potPort){
       pot = new AnalogPotentiometer(potPort);
       controller = new PIDController(ArmConfig.p, ArmConfig.i, ArmConfig.d,pot,this);
       roller1 = new Victor(v1);
       roller2 = new Victor(v2);
       arm1 = new Victor(v3);
       arm2 = new Victor(v4);
    }
/**
 * turn roller on or off
 * @param on
 */
    public synchronized void setRoller(boolean on){
        if(on)
        {
            roller1.set(ArmConfig.ROLLER_SPEED);
            roller2.set(-ArmConfig.ROLLER_SPEED);
        }

    }

 /**
  * sets the position of the arm to the left
  */
    public synchronized void setPosition_left()
    {
        setPosition(10);
    }
/**
 * sets the position of the arm to the angled
 */
    public synchronized void setPosition_right()
    {
        setPosition(85);
    }
/**
 * sets the position of the arm to an angled position
 */
    public synchronized void setPosition_angled()
    {
       setPosition(45);
    }
/**
 * sets position of arm to slanted
 */
    public synchronized void setPosition_up()
    {
        setPosition(70);
    }
/**
 * sets p,i,d constants
 * @param p proportional constant
 * @param i integral
 * @param d derivative
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
        //TODO calculate correct encoder tick numbers based on angle
       double setPositionB = (200.0 * setposition)/360 ;
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
 * makes specific value for victors
 * @param d
 * sets the victor value to double d
 */
    public void pidWrite(double d) {
       arm1.set(d);
       arm2.set(d);
    }
/**
 * makes a string
 * @return roller victor values, encoder values, controller errors
 */
    public String toString(){
        return "Roller Value:" + roller1.get() + "Pot Value:" + pot.get() + "PID error:" + controller.getError();
    }

    public String getLogData(){
        String line1 = "\t\t<data name=\"roller\" value=\""+roller1.get()+"\"/>\n";
        String line2 = "\t\t<data name=\"pot\" value=\""+pot.get()+"\"/>\n";
        String line3 = "\t\t<data name=\"arm\" value=\""+arm1.get()+"\"/>";
        return line1 + line2 + line3;
    }
    
}
