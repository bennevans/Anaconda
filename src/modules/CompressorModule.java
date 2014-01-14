
import edu.wpi.first.wpilibj.Compressor;
import modules.Module;



/**
 *
 * @author Ankith Uppunda
 */
public class CompressorModule extends Module {
    private Compressor compressor;
    
    public CompressorModule(int pressureSwitchChannel, int compressorRelayChannel){
        this.compressor = new Compressor(pressureSwitchChannel, compressorRelayChannel);
    }
    public void run(){
        compressor.start();
        
        
    }
    public String toString(){
        return "Pressure switch: " + compressor.getPressureSwitchValue();
        
    }
    
}
