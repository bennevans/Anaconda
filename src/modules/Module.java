
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
    protected String moduleName;
    
    /**
     * The constructor which also defines an object of type thread.
     */    
    public Module(){
        controlThread = new Thread(this);
        moduleName = "Module";
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
     * Enables module
     */    
    public synchronized void enable(){
        enabled = true;
    }
    
    /**
     * disables module
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
    
    /**
     * logging method to be overridden
     * @return XML data as a String 
     */
    public synchronized String getLogData(){
        return "\t\t<error>"+getModuleName()+" getLogData() not overwritten</error>";
    }
    
    /**
     * the module name to be overridden
     * @return module name
     */
    public String getModuleName(){
        return moduleName;
    }
    
}
