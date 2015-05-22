package bbdev.keymaster;

import java.io.InputStream;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.util.*;
import bbdev.resource.*;

class About extends MainScreen{
    
    About() 
    {    
        VerticalFieldManager tipman =  new TipMananger(KeyMaster.getString(KeyMasterResource.KM_MENU_ABOUT),
                                        Color.WHITE,Color.GRAY);
        this.add(tipman);    
        this.add(new BitmapField(getBitmap("/icon.png"),Field.FIELD_HCENTER));
        this.add(new LabelField(KeyMaster.getString(KeyMasterResource.KM_ABOUT_1)));       
        this.add(new LabelField(KeyMaster.getString(KeyMasterResource.KM_VER)));  
        this.add(new LabelField(KeyMaster.getString(KeyMasterResource.KM_ABOUT_2)));  
        this.add(new LabelField(KeyMaster.getString(KeyMasterResource.KM_ABOUT_HOME) + ": http://bbdev.org")); 
        //this.add(new LabelField(KeyMaster.getString(KeyMasterResource.KM_ABOUT_MOBILE) +": http://wap.emobistudio.com")); 
    }
    
    protected void makeMenu(Menu menu, int instance) 
    {
        /*MenuItem checkItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_CHECK_VERSION), 99, 10) {
            public void run() {
                Utils.launchBrowser("http://wap.emobistudio.com/update_hotkeymanager.php");
            }
        };   
        
        MenuItem moreappItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MORE_APP), 100, 10) {
            public void run() {
               Utils.launchBrowser("http://wap.emobistudio.com/products.php");
            } 
        };*/
        
        MenuItem closeItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_CLOSE), 100, 10) {
            public void run() {
               close();
            } 
        };
        
        //menu.add(checkItem);
        //menu.add(moreappItem);
        menu.add(closeItem);
        //super.makeMenu(menu,instance);   
    }
    
    public  Bitmap getBitmap(String path) 
    {
        Bitmap bitmap = null;
        try {
            //System.out.println("Load bitmap " + path);
            InputStream in = getClass().getResourceAsStream(path);
            byte[] data = new byte[10000];
            in.read(data);
            bitmap = Bitmap.createBitmapFromPNG(data, 0, data.length);
            in.close();
        } catch (Exception e) {
            //System.out.println("*** Exception in getBitmap: " + e.getMessage());
        }

        return bitmap;
    }
} 
