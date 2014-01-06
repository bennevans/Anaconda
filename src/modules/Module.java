
package modules;

/**
 *
 * @author Robotics
 */
public class Module implements Runnable{
    
    protected Thread controlThread;
    
    public Module(){
        controlThread = new Thread(this);
        controlThread.start();
    }
    
    public void run(){
        System.out.println("run() not overloaded");
    }
}
