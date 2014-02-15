package modules;


import edu.wpi.first.wpilibj.Compressor;



/**
 * This class handles the compressor on the robot
 * @author Ankith Uppunda
 * @author Ben Evans
 */

public class CompressorModule extends Module {
    private Compressor compressor;
    
    /**
     * constructor for CompressorModule
     * @param pressureSwitchChannel pressure switch channel
     * @param compressorRelayChannel compressor relay channel
     */
    
    public CompressorModule(int pressureSwitchChannel, int compressorRelayChannel){
        this.compressor = new Compressor(pressureSwitchChannel, compressorRelayChannel);
    }
    /**
     * starts compressor
     */
    public void run(){
        compressor.start();
    }
    /**
     * @return compressor state as string
     */    
    public String toString(){
        return "Pressure switch: " + compressor.getPressureSwitchValue();
    }
    
    /**
     * log function for CompressorModule
     * @return log data as XML
     */
    
    public String getLogData(){
        return "\t\t<data name = \"compressor\" value=\""+ ((!compressor.getPressureSwitchValue()) ? "ON" : "OFF" )+"\"/>";
    }
}
