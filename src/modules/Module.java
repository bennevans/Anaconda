
package modules;

/**
 *
 * @author Robotics
 */
public class Module implements Runnable{
    
    protected Thread controlThread;
    
    public Module(){
        controlThread = new Thread(this);
    }
    
    public void start(){
        controlThread.start();
    }

    public void run(){
        System.err.println("run() method not overwritten!");
    }
    
}
