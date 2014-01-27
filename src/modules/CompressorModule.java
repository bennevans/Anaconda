package modules;


import edu.wpi.first.wpilibj.Compressor;



/**
 *
 * @author Ankith Uppunda
 */
public class CompressorModule extends Module {
    private Compressor compressor;
    
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
 * @return values for switch values
 */    
    public String toString(){
        return "Pressure switch: " + compressor.getPressureSwitchValue();
    }
    
    public String getLogData(){
        return "\t\t<data name = \"compressor\" value=\""+ ((!compressor.getPressureSwitchValue()) ? "ON" : "OFF" )+"\"/>";
    }
}
