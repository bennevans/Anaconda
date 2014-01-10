
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
    public void run(){
        compressor.start();
        
        
    }
    
}
