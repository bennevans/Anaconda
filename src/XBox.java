
import edu.wpi.first.wpilibj.Joystick;


/**
 *
 * @author Ben Evans
 */
public class XBox extends Joystick{

    public XBox(int port) {
        super(port);
    }
    
    public boolean getA(){
        return getRawButton(1);
    }
    
    
}
