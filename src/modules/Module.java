
package modules;

/**
 * This class is the super class for all other modules.
 * There are run, start, enable, disable, and reset methods. The run and reset methods
 * can be overwritten by our other modules.
 * @author Ben Evans
 */

public class Module implements Runnable{
    
    protected Thread controlThread;
    protected boolean enabled = false;
    protected boolean autoMode = true;
/**
 * The constructor which also defines an object of type thread.
 */    
    public Module(){
        controlThread = new Thread(this);
    }
/**
 * starts the modules using the Thread, controlThread
 */    
    public void start(){
        controlThread.start();
    }
/**
 *  The run method is the method that can be overwritten by other module classes
 */
    public void run(){
        System.err.println("run() method not overwritten!");
    }
/**
 * Enables robot
 */    
    public synchronized void enable(){
        enabled = true;
    }
/**
 * disables robot
 */    
    public synchronized void disable(){
        enabled = false;
    }
/**
 * The reset method is overwritten by all other module classes. 
 */    
    public void reset(){
        System.err.println("reset() not overwritten!");
    }
/**
 * sets autonomous on
 */
    public synchronized void setAutoModeOn(){
        autoMode = true;
    }
/** 
 * sets autonomous off
 */    
    public synchronized void setAutoModeOff(){
        autoMode = false;
    }
}
