package bbdev.keymaster;

import java.util.Date;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;

public class FlashScreen extends FullScreen{
    
    FlashScreen() 
    {   
        getWorkerThread().start();
    }
    
    private final static int TOLERANCE = 8000;
    private long startOnTime = -1;
    private Thread workerThread;
    private boolean isWorkerRunning;
    
    public void on()
    {
        if (!isOn()) {
            //int bright = Backlight.getBrightness();
            //Backlight.setBrightness(bright);
            //Backlight.setTimeout(TOLERANCE/1000);
            Backlight.enable(true,16);
            startOnTime = System.currentTimeMillis();
            //System.out.println("*** turn on: " + new Date(startOnTime));
        }
    }
    
    private boolean isOn() 
    {
        boolean status = false;

        // This API call does not seem to work on the actual device (7230). Due
        // to this, pressing the actual light switch on the device can cause the
        // light to not work as expected
        //        status = Backlight.isEnabled();

        long now = System.currentTimeMillis();
        long diff = now - startOnTime;
        //System.out.println("*** isOn() now=" + now + " startOnTime="
                //+ startOnTime + " diff=" + diff);

        if (startOnTime >= 0) {
            if (diff < TOLERANCE) //JHH 2008-05-17
                status = true;
        }

        return status;
    }
    
    private Thread getWorkerThread() {
        // Worker thread to monitor certain options
        if (workerThread == null) {
            workerThread = new Thread(new Runnable() {
                public void run() {
                    isWorkerRunning = true;
                    while (isWorkerRunning) {
                        on();
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            });
        }

        return workerThread;
    }
    
    private void stopWorker() 
    {
        if(isWorkerRunning){
            isWorkerRunning = false;
            try {
                getWorkerThread().interrupt();
            } catch (Exception e) {
            }
            workerThread = null;
        }
    }
    
    public boolean onClose()
    {
        stopWorker();
        return true;
    }
        
    protected boolean keyChar(char c, int status, int time)
    {
        if(c == Characters.ESCAPE)
        {   
            stopWorker();
            this.close();
            return true;
        }
        
        return super.keyChar(c,status,time);
    }
} 
