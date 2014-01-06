/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modules;

import edu.wpi.first.wpilibj.*;

/**
 *
 * @author Ben Evans
 * 
 */
public class DriveModule extends Module{
    
    private Victor lVictor1, lVictor2, rVictor1, rVictor2;
    private Encoder lEncoder, rEncoder;
    private double distancePerPulse;
    private Gyro gyro;
    private PIDSource pidSource;
    private PIDOutput pidOutput;
    private PIDController pid;
    
    public DriveModule(int lv1, int lv2, int rv1, int rv2, int lenca, int lencb, int renca, int rencb, double dist, int gyroc){
        lVictor1 = new Victor(lv1);
        lVictor2 = new Victor(lv2);
        rVictor1 = new Victor(rv1);
        rVictor2 = new Victor(rv2);
        
        lEncoder = new Encoder(lenca, lencb);
        rEncoder = new Encoder(renca, rencb);
        distancePerPulse = dist;
        gyro = new Gyro(gyroc);
        
        setupEncoders();
//        pidSource = new PIDSource();
    }
    
    private synchronized void setupEncoders(){
        lEncoder.start();
        rEncoder.start();
        lEncoder.setDistancePerPulse(distancePerPulse);
        rEncoder.setDistancePerPulse(distancePerPulse);
    }
    
    public synchronized void resetEncoders(){
        lEncoder.reset();
        rEncoder.reset();
    }
    
    public synchronized void resetGyro(){
        gyro.reset();
    }
    
    public void run(){
        
    }
}
