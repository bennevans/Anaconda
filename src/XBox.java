
import edu.wpi.first.wpilibj.Joystick;


/**
 *
 * @author Ben Evans & Michael Chin
 */
public class XBox extends Joystick{

    public XBox(int port) {
        super(port);
    }
    
    public boolean getA(){
        return getRawButton(1);
    }
    public boolean getB(){
        return getRawButton(2);
    }
    public boolean getXb(){
        return getRawButton(3);
    }
    public boolean getYb(){
        return getRawButton(4);
    }
    public boolean getRB(){
        return getRawButton(6);
    }
    public boolean getLB(){
        return getRawButton(5);
    }
    public boolean getR3(){
        return getRawButton(10);
    }
    public boolean getL3(){
        return getRawButton(9);
    }
    public boolean getStart(){
        return getRawButton(8);
    }
    public boolean getBack(){
        return getRawButton(7);
    }
    public double rT(){
        return getRawAxis(3);
    }
    public double lT(){
        return getRawAxis(3);
    }
    public double rX(){
        return getRawAxis(4);
    }
    public double rY(){
        return getRawAxis(5);
    }
    public double lY(){
        return getRawAxis(2);
    }
    public double lX(){
        return getRawAxis(1);
    }
    public double pX(){
        return getRawAxis(6);
    }
//    public double pY(){
//       return getRawAxis();
//    }
}
   