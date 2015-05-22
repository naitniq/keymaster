/*
 * KeyMaster.java
 * bbdev, 2008
 */

package bbdev.keymaster;


import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.i18n.*;
import net.rim.blackberry.api.homescreen.*;
import bbdev.resource.*;
import net.rim.device.api.synchronization.*;
import net.rim.device.api.applicationcontrol.*;
import java.util.Random;
//import com.mobiroo.emobistudio.hotkeymanager.*; 

class KeyMaster extends UiApplication{
        public static KeyMaster _this;
        private static ResourceBundle _resources = ResourceBundle.getBundle( KeyMasterResource.BUNDLE_ID,KeyMasterResource.BUNDLE_NAME );    
        
        KeyMaster()
        {   
     
            _this = this;
            startKeyMaster(this); 
        }
        
        /*private MobirooSplash mobirooSplash;
        public void activate()
        {
            if(Const.OEM_MOBIROO){
                if (this.getActiveScreen() != mobirooSplash){ 
                    pushScreen(mobirooSplash); 
                    mobirooSplash.mobirooOnForeground(); 
                }
            }   
        }
        public void deactivate(){ 
            if(Const.OEM_MOBIROO){
                mobirooSplash.mobirooOnBackground(); 
            }
        } 
        public MobirooSplash getMobirooSplash(){ 
            return this.mobirooSplash; 
        }*/
        
        
        public static void startKeyMaster(UiApplication uiapp)
        {
                Utils.OutputDebugString(_resources.getString(KeyMasterResource.APPLICATION_TITLE));
                Utils.OutputDebugString(_resources.getString(KeyMasterResource.APPLICATION_DESCRIPTION));

                //load data
                Global.data = StoreData.load();
                
                long now = System.currentTimeMillis();
                if(!Const.DEBUG && !Utils.isActive(Global.data.activeCode) && now  - Global.data.starttime > 1*24*3600*1000){
                    uiapp.pushScreen(new OptionScreen());
                }else{
                    //_this = uiapp;
                    uiapp.pushScreen(new AppScreen());
                }
                
                Const.SoftVersion = DeviceInfo.getSoftwareVersion();
                Utils.setHotkey(Global.data.homehotkey);
                //if(Const.SoftVersion.charAt(0) != '5' && Global.data.bFirstRun){
                if(Global.data.bFirstRun){
                    /*try{
                        ApplicationPermissionsManager appPM = ApplicationPermissionsManager.getInstance();
                        ApplicationPermissions requestedPermissions = appPM.getApplicationPermissions();
                        //if(!requestedPermissions.containsPermissionKey(ApplicationPermissions.PERMISSION_EVENT_INJECTOR))
                        //requestedPermissions.addPermission(ApplicationPermissions.PERMISSION_SCREEN_CAPTURE):
                        requestedPermissions.addPermission(ApplicationPermissions.PERMISSION_CODE_MODULE_MANAGEMENT);
                        requestedPermissions.addPermission(ApplicationPermissions.PERMISSION_EVENT_INJECTOR);    
                        requestedPermissions.addPermission(ApplicationPermissions.PERMISSION_CHANGE_DEVICE_SETTINGS);                
                        appPM.invokePermissionsRequest(requestedPermissions);
                    }catch(Exception ex){
                         ;
                         
                    }*/
                    Utils.setPermissions();
                    Global.data.bFirstRun = false;
                }
                
                //Global.listTable = Global.data.listTable;
                //Global.listName = Global.data.listName;
               // Global.listMap = Global.data.listMap;
                //Global.listCustomMod =  Global.data.listCustomMod;
                //Global.listPhoneNumber =  Global.data.listPhoneNumber; 
                //Global.htModItem =  Global.data.htModItem;
            //MyScreen ms = new MyScreen();

                
                //ms.setTitle(new LabelField("KeyMaster"));
            
        }
    
        public static void main(String[] args) 
        {
            if (args != null && args.length >0 && args[0].equals("init")) 
            {     
                //enable app for synchronization
                SyncManager.getInstance().enableSynchronization(SyncData.getInstance());     
                Global.data = StoreData.load(); 
                Utils.setHotkey(Global.data.homehotkey);  
                if(Global.data.captureRandom == 0){
                    Random random = new Random();
                    Global.data.captureRandom = random.nextInt(9999);
                    Global.data.commit(); 
                }
                if(Global.data.bCapture){
                    AddCaptureMenu.AddMenu();
                }        
                if(Global.data.bSysmenu){
                    AddSystemMenu.AddMenu();
                }          
                if(Global.data.bEventmenu){
                    EventMenu.AddMenu();
                }          
        
            }
            else
                new KeyMaster().enterEventDispatcher();
            //else
            //    new KeyMaster(true).enterEventDispatcher();
        }
        
        public static String getString(int key) 
        {
            //_resources.getLocale();
            
            return _resources.getString(key);
        }
        /*
        public void RollerApplicationIcon()
        {
                //Setup the rollover icons.
                final Bitmap regIcon = Bitmap.getBitmapResource(Const.RES_FOLDER + "1.png");
                final Bitmap icon = Bitmap.getBitmapResource(Const.RES_FOLDER + "2.png");
        
                invokeLater(new Runnable()
                {
                    public void run()
                    {
                        ApplicationManager theApp =ApplicationManager.getApplicationManager();
                        boolean isStartup = true;
                      
                        while (isStartup)
                        {
                            //Check if the BlackBerry has completed its startup process.
                            if (theApp.inStartup())
                            {
                                //The BlackBerry is still starting up, sleep for 1 second.
                                try
                                {
                                    Thread.sleep(1000);
                                }
                                catch (Exception ex)
                                {
                                    Utils.OutputDebugString(ex.toString());
                                //Couldn't sleep, handle exception.
                                }
                            }
                            else
                            {
                                //The BlackBerry has finished its  startup process.
                                //Set the rollover icons.
                                try{
                                    HomeScreen.updateIcon(regIcon, 0);
                                    HomeScreen.setRolloverIcon(icon, 0);
                                    isStartup = false;
                                }catch (Exception ex){
                                    Utils.OutputDebugString( ex.toString() );
                                }
                                
                            }
                        }
                        
                        System.exit(0);
                    }
                });
        }
        */
} 
