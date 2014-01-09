
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
    
    public void enable(){
        enabled = true;
    }
    
    public void disable(){
        enabled = false;
    }

    public void setAutoModeOn(){
        autoMode = true;
    }
    
    public void setAutoModeOff(){
        autoMode = false;
    }
}
