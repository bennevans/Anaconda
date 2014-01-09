
package modules;

/**
 *
 * @author Ben Evans
 */
public class Module implements Runnable{
    
    protected Thread controlThread;
    protected boolean enabled = false;
    protected boolean autoMode = true;
    
    public Module(){
        controlThread = new Thread(this);
    }
    
    public void start(){
        controlThread.start();
    }

    public void run(){
        System.err.println("run() method not overwritten!");
    }
    
    public synchronized void enable(){
        enabled = true;
    }
    
    public synchronized void disable(){
        enabled = false;
    }

    public synchronized void setAutoModeOn(){
        autoMode = true;
    }
    
    public synchronized void setAutoModeOff(){
        autoMode = false;
    }
}
