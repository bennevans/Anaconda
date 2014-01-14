package modules;

import config.ArmConfig;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author Ankith Uppunda
 */
public class ArmModule extends Module implements PIDOutput {
    private PIDController controller;
    private Encoder e;
    private Victor roller1, roller2, arm1, arm2;
    public ArmModule(int v1, int v2, int v3, int v4, int edport1, int edport2, double s, double any){
       controller = new PIDController(ArmConfig.p, ArmConfig.i, ArmConfig.d,e,this);
       e = new Encoder(edport1, edport2);
       roller1 = new Victor(v1);
       roller2 = new Victor(v2);
       arm1 = new Victor(v3);
       arm2 = new Victor(v4);
    }
    public synchronized void setPositionA()
    {
        setPosition(0);
    }
    public synchronized void setPositionB()
    {
        controller.setSetpoint(180);
    }
    public synchronized void setPositionC()
    {
       controller.setSetpoint(45);
    }
    public synchronized void setPositionD()
    {
        controller.setSetpoint(90);
    }
    public synchronized void setPosition(double setposition)
    {
        //TODO calculate correct encoder tick numbers based on angle
        
       controller.setSetpoint(setposition);
    }
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
    public void reset(){
       controller.reset();
    }

    public void pidWrite(double d) {
       arm1.set(d);
       arm2.set(d);
    }
    public String toString(){
        return "Roller Value:" + roller1.get() + "Encoder Value:" + e.get() + "PID error:" + controller.getError();
        
//you dont need ^
    }
    
    
}
