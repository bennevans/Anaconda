/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modules;

import config.ArmConfig;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.networktables2.util.Set;

/**
 *
 * @author Ankith Uppunda
 */
public class ArmModule extends Module implements PIDOutput {
    private PIDController controller;
    private Encoder e;
    private Victor roller1, roller2, arm1, arm2;
    double s;
    public ArmModule(int v1, int v2, int v3, int v4, int edport1, int edport2, double s){
       controller = new PIDController(ArmConfig.p, ArmConfig.i, ArmConfig.d,e,this);
       e = new Encoder(edport1, edport2);
       roller1 = new Victor(v1);
       roller2 = new Victor(v2);
       arm1 = new Victor(v3);
       arm2 = new Victor(v4);
       this.s = s;
    }
    public synchronized void setpositionA()
    {
        controller.setSetpoint(0);
    }
    public synchronized void setpositionB()
    {
        controller.setSetpoint(180);
    }
    public synchronized void setpositionC()
    {
       controller.setSetpoint(45);
    }
    public synchronized void setpositionD()
    {
        controller.setSetpoint(90);
    }
    public synchronized void anyposition()
    {
       
    }
    public void run(){
        while(true)
        {
            if(enabled)
            {
                s = e.get();
                controller.enable();
            }
            else
            {
                e.stop();
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
    
    
    
}
