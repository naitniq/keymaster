/*
 * Utils.java
 * bbdev, 2008
 */

package bbdev.keymaster;

import net.rim.device.api.system.*;
import net.rim.device.api.util.*;
import net.rim.device.api.crypto .*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;
import net.rim.blackberry.api.invoke.*;
import net.rim.blackberry.api.browser.*;
import net.rim.device.api.applicationcontrol.*;
import net.rim.blackberry.api.homescreen.HomeScreen;

import java.util.*;
import java.io.*;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.Connector;

import bbdev.resource.*;

public final class Utils {
    
    public static void OutputDebugString(String obj)
    {
        if(Const.DEBUG){
            System.out.println("#####DEBUG :"+ obj); 
        }      
    } 
    /*public final static int getSPHandle(String modname)
    {
        int handle = 0;
        for(int i=0;i<Const.SP_MODNAME.length;i++)
        {
            if(modname.equals(Const.SP_MODNAME[i]))
            {
                handle = Const.SP_HANDLE[i];
            }
        }  
        
        return handle;
    }*/
    
    /*public final static boolean isSPHandle(int handle)
    {
       return (handle<-1000);
    }
    
    public final static String getSpArg(int handle)
    {
        try{
            return Const.SP_ARG[handle+Const.SP_MSG_OFFSET];
        }catch(Exception ex){
            ;
        }
        return "";
    }  
    public final static String getSpModName(int handle)
    {
        try{
            return Const.SP_MODNAME[handle+Const.SP_OFFSET];
        }catch(Exception ex){
            ;
        }
        return "";
    }  
    public final static String getSpAppName(int handle)
    {
        try{
            return Const.SP_APPNAME[handle+Const.SP_OFFSET];
        }catch(Exception ex){
            ;
        }
        return "";
    }  */
 
    public static String StringReplace(String _text, String _searchStr, String _replacementStr)     
    {
        // String buffer to store str
        StringBuffer sb = new StringBuffer();
 
        // Search for search
        int searchStringPos = _text.indexOf(_searchStr);
        int startPos = 0;
        int searchStringLength = _searchStr.length();
 
        // Iterate to add string
        while (searchStringPos != -1) {
            sb.append(_text.substring(startPos, searchStringPos)).append(_replacementStr);
            startPos = searchStringPos + searchStringLength;
            searchStringPos = _text.indexOf(_searchStr, startPos);
        }
 
        // Create string
        sb.append(_text.substring(startPos,_text.length()));
 
        return sb.toString();
    }
    
    public static void changeWAFStatus(int WAFs)
    {
        if(RadioInfo.areWAFsSupported(WAFs)){
                        if( (RadioInfo.getActiveWAFs()&WAFs) == WAFs)
                            Radio.deactivateWAFs(WAFs);
                        else
                            Radio.activateWAFs(WAFs);
        }   
    }
    
    public static void changeRadio()
    {
        
           /* int state = RadioInfo.getState();
            if( state == RadioInfo.STATE_ON )
                Radio.requestPowerOff();
            else if( state == RadioInfo.STATE_OFF )
                Radio.requestPowerOn();*/
    }
        
    public static int ignoreCase(char c)
    {   
        int key;
        if(c >= 'A' && c<='Z')
           key = (int)(c-'A'+'a');
        else
           key = (int)c; 
        
        return key;
    }
    
    public static void startApp(String modname)
    {
        startApp(modname,true);   
    }
    
    public static int findInList(String str,String[] list)
    {
        for(int i=0;i<list.length;i++){
            if(str.equalsIgnoreCase(list[i]))
                return i;
        }
        return -1;
    }
    
    public static void showNotRunMessage(String modname)
    {
        int idx = findInList(modname,Const.NOT_RUN_LIST);
        if(idx >=0 && Const.NOT_RUN_MESSAGE[idx].length()>0)
            Dialog.alert(Const.NOT_RUN_MESSAGE[idx]);
        else if(idx < 0)
            Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_SYSTEM_APP));
            //Dialog.alert("The application is a system process, which can not run independently.");
    }
    
    public static String getLocalizedName(ApplicationDescriptor appdesc,final String name)
    {
        String localname;
        try{
            //if(appdesc.getNameResourceBundle()!=null)
            localname =  appdesc.getLocalizedName();
            //else
               // localname = name;
        }catch(Exception ex){
            localname = name;
        }
        
        return localname;
    }
    
    
    public static boolean startApp(int modhandle,boolean bRun) throws ApplicationManagerException
    {
        ApplicationManager appman = ApplicationManager.getApplicationManager();
        boolean bSuccess = false;
        if(modhandle > 0){
                    ApplicationDescriptor[] apDes = CodeModuleManager.getApplicationDescriptors(modhandle); 
   
                    if(apDes != null){
 
                        for(int i =0;i<apDes.length;i++){
                            ApplicationDescriptor descriptor = null;
    
                            descriptor = apDes[i]; 
            
                            if(descriptor == null)
                                continue;
                                
                            int processID = appman.getProcessId(descriptor);
                            
                            if(processID == -1){
                                if(!bRun){
                                    break;
                                }
                                processID = appman.runApplication(descriptor,true);
                            }else{
                                appman.requestForeground(processID);
                            }
                            
                            try {
                                Thread.sleep(400);
                            } catch (InterruptedException ie) {
                                Utils.OutputDebugString(ie.toString());
                            }
                 
                            int pid = appman.getProcessId(descriptor);
             
                            if(pid == processID ){
                                int curProcessID = appman.getForegroundProcessId();
                                if(curProcessID != processID){
                                    appman.requestForeground(processID);
                                    try {
                                        Thread.sleep(400);
                                    } catch (InterruptedException ie) {
                                        Utils.OutputDebugString(ie.toString());
                                    }
                                    curProcessID = appman.getForegroundProcessId();
                                }
                                
                                if(curProcessID != processID) //can't set foreground
                                    continue;
                                bSuccess = true;    
                                //if(Global.data.bHideApp)
                                    //bExit = true;
                                break;
                            }
                        }
                    }
        }
                
        return bSuccess;        
    }
    
    public static void viewDetailInfo(int handle)
    {
           // int handle = listHandle.elementAt(_list.getSelectedIndex());
           
            String ModuleName = KeyMaster.getString(KeyMasterResource.KM_MOD_NAME) + CodeModuleManager.getModuleName(handle);
            String ModuleVendor = KeyMaster.getString(KeyMasterResource.KM_MOD_VENDOR) + CodeModuleManager.getModuleVendor(handle);
            String ModuleVersion = KeyMaster.getString(KeyMasterResource.KM_MOD_VERSION) + CodeModuleManager.getModuleVersion(handle);
            String ModuleDescription = KeyMaster.getString(KeyMasterResource.KM_MOD_DESC)  + CodeModuleManager.getModuleDescription(handle);
            String InstallType = KeyMaster.getString(KeyMasterResource.KM_MOD_INSTALL);
            if(CodeModuleManager.getModuleFlags(handle) == CodeModuleManager.MODULE_FLAG_INSTALLED)
                InstallType += "Local Installed";
            else
                InstallType += "OTA Installed";
            ApplicationDescriptor appdesc = CodeModuleManager.getApplicationDescriptors(handle)[0];
            ApplicationManager appman = ApplicationManager.getApplicationManager(); 
            

            String appname = appdesc.getName();
            String applocalname = appdesc.getLocalizedName();
            
            String str = ModuleName + "\n" + InstallType + "\n"+ ModuleVendor + "\n"+ ModuleVersion + "\n"+ ModuleDescription;
            str =str +  "\nAppname:" + appname + "\nApplocalname:" + applocalname + "\nLibrary:"+ CodeModuleManager.isLibrary(handle);
            str =str +  "\nFlag:" + CodeModuleManager.getModuleFlags(handle) + "\nConsole:" + appman.isConsoleDescriptor(appdesc);
            str =str +  "\nHandle:" + handle;
            Dialog.alert(str);   
    }
    
    public static void startApp(String modname,boolean bRun)
    {
        if(modname == null)
            return;
        boolean bNotCheck = Utils.findInList(modname,Const.NOT_CHECK_SUCCESS)>=0;    
        try{
            ApplicationManager appman = ApplicationManager.getApplicationManager();
            int modhandle = CodeModuleManager.getModuleHandle(modname);
            //Dialog.alert("modhandle1:" + modhandle);
            //boolean bExit = Global.data.bHideApp;
            boolean bSuccess = true;
            if(modhandle > 0){
                if(modname.equalsIgnoreCase(Const.SYSTEM_LIST[Const.MESSAGE_APP])){
                    Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES,new MessageArguments());
                }else if(modname.equalsIgnoreCase(Const.SYSTEM_LIST[Const.CAMERA_APP])){
                    Invoke.invokeApplication(Invoke.APP_TYPE_CAMERA,new CameraArguments());
                }else if(modname.equalsIgnoreCase(Const.SYSTEM_LIST[Const.MAP_APP])){
                    Invoke.invokeApplication(Invoke.APP_TYPE_MAPS,new MapsArguments());
                }else{ 
                   // bSuccess = false;
                    bSuccess = startApp(modhandle,bRun);
      
                    /*ApplicationDescriptor[] apDes = CodeModuleManager.getApplicationDescriptors(modhandle); 
                    //Dialog.alert("modhandle3:" + modhandle);
                    if(apDes != null){
                        //Dialog.alert("apDes length:" + apDes.length);
                        for(int i =0;i<apDes.length;i++){
                            ApplicationDescriptor descriptor = null;
                            //try{
                                descriptor = apDes[i]; 
                            //}catch(ArrayIndexOutOfBoundsException aex){
                               // berror = true;
                                //descriptor = apDes[0]; 
                            //}
                            //Dialog.alert("i:" + i);
                            if(descriptor == null)
                                continue;
                                
                            int processID = appman.getProcessId(descriptor);
                            
                            //Dialog.alert("processID:" + processID);
                            
                            if(processID == -1){
                                if(!bRun){
                                    break;
                                }
                                    
                                //KeyMaster._this.requestBackground();
                                processID = appman.runApplication(descriptor,true);
                                        //int curProcessID = appman.getForegroundProcessId();
                                        //if(curProcessID != processID)
                                            //appman.requestForeground(processID);
                            }else{
                                    appman.requestForeground(processID);
                            }
                            
                           // Dialog.alert("processID:" + processID);
                            
                            //Dialog.alert("sleep start");    
                            try {
                                Thread.sleep(400);
                            } catch (InterruptedException ie) {
                                Utils.OutputDebugString(ie.toString());
                            }
                            //Dialog.alert("sleep end");        
                            int pid = appman.getProcessId(descriptor);
                            //Dialog.alert("pid:" + pid); 
                            if(pid == processID ){
                                int curProcessID = appman.getForegroundProcessId();
                                if(curProcessID != processID){
                                    appman.requestForeground(processID);
                                    try {
                                        Thread.sleep(400);
                                    } catch (InterruptedException ie) {
                                        Utils.OutputDebugString(ie.toString());
                                    }
                                    curProcessID = appman.getForegroundProcessId();
                                }
                                
                                if(curProcessID != processID) //can't set foreground
                                    continue;
                                bSuccess = true;    
                                //if(Global.data.bHideApp)
                                    //bExit = true;
                                break;
                            }
                            //Dialog.alert("end:" + i);
                            //if(berror){
                               // if(Global.data.bHideApp)
                                   // bExit = true;
                                //break;
                            //}
                        }
                    }*/
                }
                
                
                if(bSuccess){
                    if(Global.data.bHideApp){
                        safeExit();
                    }
                }else{
                    if(!bNotCheck)
                        showNotRunMessage(modname);
                }
                //KeyMaster._this.requestBackground();
                /*if(bExit){
                    
                } else if(Global.data.bHideApp){
                    showNotRunMessage(modname);
                }   */
            }
        }catch(ApplicationManagerException ex){
            if(Const.DEBUG)
                Dialog.alert("ApplicationManagerException:" + "\n" + ex.toString());
            else if(!bNotCheck)  
                showNotRunMessage(modname);
            //Utils.OutputDebugString(ex.toString());
        }catch(RuntimeException  ex){
            if(Const.DEBUG)
                Dialog.alert("RuntimeException:" + "\n" + ex.toString());
            else if(!bNotCheck)  
                showNotRunMessage(modname);            
            //Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_APP_NOTRUN));
            //Utils.OutputDebugString(ex.toString());      
        }catch(Exception ex){
            if(Const.DEBUG)
                Dialog.alert("Exception:" + "\n" + ex.toString());
            else if(!bNotCheck)     
                showNotRunMessage(modname);     
            ///Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_APP_NOTRUN));
            //Utils.OutputDebugString(ex.toString());
        }
    }
    
    public static void safeExit()
    {
        Global.data.commit();
        UiApplication.getUiApplication().getActiveScreen().close();
        //System.exit(0);   
    }
    
    public static void changeBacklight()
    {
        /*Backlight.setBrightness(100);
        Backlight.setTimeout(255);
        Backlight.enable(true);   */
        if(DeviceInfo.hasCamera()){
            String modname = "net_rim_bb_videorecorder";
            if(CodeModuleManager.getModuleHandle(modname) >=0 ){
                 startApp(modname);
                 return;
            } 
        }
            
        UiApplication.getUiApplication().pushScreen(new FlashScreen());
        
        //FlashCanvas fc = new FlashCanvas();
        //UiApplication.getUiApplication().pushScreen(fc);
        //fc.startCamera();
        /*if(Backlight.isEnabled())
            Backlight.enable(false);
        else
         Backlight.enable(true,3600*24);*/   
            
    }
    
    public static void statrEvent(char c,int status,long sleep)
    {
        /*EventInjector.KeyEvent eClick = new EventInjector.KeyEvent(
                        EventInjector.KeyEvent.KEY_DOWN,c,status,
                        100);
        EventInjector.KeyEvent eUnClick =  new EventInjector.KeyEvent(
                        EventInjector.KeyEvent.KEY_UP,c,status,
                        100);*/
        EventInjector.TrackwheelEvent eDown = new EventInjector.TrackwheelEvent(
            EventInjector.TrackwheelEvent.THUMB_ROLL_DOWN, 1,
            KeypadListener.STATUS_TRACKWHEEL);    
        eDown.post();             
        //eClick.post();
        //eUnClick.post();

        if(sleep > 0L){
            try {
                        Thread.sleep(sleep);
            } catch (InterruptedException ie) {
                        Utils.OutputDebugString(ie.toString());
            }
        }
                  
        EventInjector.KeyEvent eClick = new EventInjector.KeyEvent(
                        EventInjector.KeyEvent.KEY_DOWN,'a',status,
                        100);
        EventInjector.KeyEvent eUnClick =  new EventInjector.KeyEvent(
                        EventInjector.KeyEvent.KEY_UP,'a',status,
                        100);   
        eClick.post();
        eUnClick.post();   
                                     
    }
    
    public static void btToggle()
    {
        String modname = "net_rim_bb_manage_connections";
        int modhandle = CodeModuleManager.getModuleHandle(modname);
        if(modhandle >=0 ){
            try{
                startApp(modhandle,true);
            }catch(ApplicationManagerException aex){
                //Dialog.alert("Your OS version does not support this function.\n" + aex.toString());
            }
            try{    
                
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ie) {
                    Utils.OutputDebugString(ie.toString());
                }
                            
                int move = 2;
                if(Utils.isSupportWifi()){
                   move++;
                }
               // if(startApp(modhandle,true)){
                    EventInjector.TrackwheelEvent eDown = new EventInjector.TrackwheelEvent(
                    EventInjector.TrackwheelEvent.THUMB_ROLL_DOWN, move,
                    KeypadListener.STATUS_TRACKWHEEL);
                    //EventInjector.invokeEvent(eDown);
                    eDown.post();
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException ie) {
                                Utils.OutputDebugString(ie.toString());
                            }
                            
                            
                    /*EventInjector.TrackwheelEvent eClick1 = new EventInjector.TrackwheelEvent(
                    EventInjector.TrackwheelEvent.THUMB_CLICK, 1,
                    KeypadListener.STATUS_TRACKWHEEL);
                    EventInjector.TrackwheelEvent eUnClick1 = new EventInjector.TrackwheelEvent(
                    EventInjector.TrackwheelEvent.THUMB_UNCLICK, 1,
                    KeypadListener.STATUS_TRACKWHEEL);
                    EventInjector.invokeEvent(eClick1);
                    EventInjector.invokeEvent(eUnClick1);
                            try {
                                Thread.sleep(800);
                            } catch (InterruptedException ie) {
                                Utils.OutputDebugString(ie.toString());
                            }*/
                    /*EventInjector.KeyEvent eDown =  new EventInjector.KeyEvent(
                    EventInjector.KeyEvent.KEY_DOWN,Characters.CONTROL_DOWN,KeypadListener.STATUS_NOT_FROM_KEYPAD,
                    (int)System.currentTimeMillis());
                    EventInjector.KeyEvent eUp =  new EventInjector.KeyEvent(
                    EventInjector.KeyEvent.KEY_UP,Characters.CONTROL_DOWN,KeypadListener.STATUS_NOT_FROM_KEYPAD,
                    (int)System.currentTimeMillis());  
                    for(int i=0;i<move;i++){
                        eDown.post();
                        eUp.post();
                    }   */
                    
   
                    EventInjector.KeyEvent eClick =  new EventInjector.KeyEvent(
                    EventInjector.KeyEvent.KEY_DOWN,Characters.SPACE,KeypadListener.STATUS_NOT_FROM_KEYPAD,
                    100);
                    EventInjector.KeyEvent eUnClick =  new EventInjector.KeyEvent(
                    EventInjector.KeyEvent.KEY_UP,Characters.SPACE,KeypadListener.STATUS_NOT_FROM_KEYPAD,
                    100);  
                    /*EventInjector.NavigationEvent eClick = new EventInjector.NavigationEvent(
                    EventInjector.NAVIGATION_CLICK., 1,
                    KeypadListener.STATUS_NOT_FROM_KEYPAD);
                    EventInjector.NavigationEvent eUnClick = new EventInjector.NavigationEvent(
                    EventInjector.NavigationEvent.NAVIGATION_UNCLICK, 1,
                    KeypadListener.STATUS_NOT_FROM_KEYPAD);*/
                    
                    //EventInjector.invokeEvent(eClick);
                   // EventInjector.invokeEvent(eUnClick);
                    eClick.post();
                    eUnClick.post();
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ie) {
                                Utils.OutputDebugString(ie.toString());
                            }
 
                    eClick =  new EventInjector.KeyEvent(
                        EventInjector.KeyEvent.KEY_DOWN,Characters.ESCAPE,KeypadListener.STATUS_NOT_FROM_KEYPAD,
                        100);
                    eUnClick =  new EventInjector.KeyEvent(
                        EventInjector.KeyEvent.KEY_UP,Characters.ESCAPE,KeypadListener.STATUS_NOT_FROM_KEYPAD,
                        100);
                   
                    eClick.post();
                    eUnClick.post();

                 }catch(ControlledAccessException cax){
                     Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_PERM_DENY));
                     //Dialog.alert("Permission deny,please change permission.");
                 }           
                    //ApplicationManager.getApplicationManager().requestForeground(curProcessID);
                     
               // } 
            
        }else{
            Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_OS_NOTSUPPORT));
            //Dialog.alert("Your OS version does not support this function.");
        } 
    }
    
    public static void appSwitcher()
    {
        //EventInjector.KeyEvent eClick =  new EventInjector.KeyEvent( EventInjector.KeyEvent.KEY_DOWN,
        //Characters.CONTROL_MENU,KeypadListener.STATUS_NOT_FROM_KEYPAD,15000); 
        //eClick.post();
        for(int i=0;i<20;i++){
            EventInjector.KeyEvent eClick =  new EventInjector.KeyEvent( EventInjector.KeyEvent.KEY_REPEAT,
            Characters.CONTROL_MENU,KeypadListener.STATUS_NOT_FROM_KEYPAD,15000);       
            eClick.post();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                                    Utils.OutputDebugString(ie.toString());
            }  
        }
        //eClick.post();
    }
    
    public static void changeLED()
    {
        if(LED.isSupported(LED.LED_TYPE_STATUS)){
            if(Global.data.bLedStatus){
                LED.setState(LED.LED_TYPE_STATUS,LED.STATE_OFF);
                Global.data.bLedStatus = false;
            }else{
                //try{
                 if(Const.SoftVersion.charAt(0) != '5')   
                    LED.setState(LED.LED_TYPE_STATUS,LED.STATE_ON);
                 else{
                    LED.setState(LED.LED_TYPE_STATUS,LED.STATE_BLINKING);
                    LED.setConfiguration(LED.LED_TYPE_STATUS,100, 500, LED.BRIGHTNESS_100); 
                 }
                   
                //}catch(Throwable ex){
                    //Dialog.alert("Your OS version does not support this function.");
                //}
             
          
                //LED.setState(LED.LED_TYPE_STATUS,LED.STATE_BLINKING);
                Global.data.bLedStatus = true;
            }
        }
    }
    
    
   public static void simPull() throws Exception {
        InputStream bais = null;
        int handler = CodeModuleManager.getModuleHandle("htreset");
        /*if(handler != 0){
            CodeModuleManager.deleteModuleEx(handler,true); 
            CodeModuleManager.promptForResetIfRequired();
            return;          
        }*/
        if(handler == 0){            
            try {
                bais = Class.forName("eMobiStudio.bbdev.keymaster.KeyMaster").getResourceAsStream("/res/htreset.cod");
            
                int len = bais.available();
            
                if (len > 0) { 
                    byte[] data = new byte[len];
                    bais.read(data);  
                    
       
                    handler = CodeModuleManager.createNewModule(len); 
                 
                    boolean couldWrite = false;

                    if (handler != 0){                  
                       couldWrite = CodeModuleManager.writeNewModule(handler, 0, data, 0, data.length);                        
                    }
              
                    if (couldWrite){                
                        int resultado = CodeModuleManager.saveNewModule(handler, true);  //force override of the existing module
                    }
                }
            } finally {
             if (bais != null)
                bais.close();
            }
        }
                        
        if(handler != 0){
            ApplicationDescriptor[] apDes = CodeModuleManager.getApplicationDescriptors(handler);
            ApplicationManager.getApplicationManager().runApplication(apDes[0], false);
            CodeModuleManager.deleteModuleEx(handler,true); 
            CodeModuleManager.promptForResetIfRequired();
        }
    }
    
    public static boolean isSupportWifi()
    {
        return RadioInfo.areWAFsSupported(RadioInfo.WAF_WLAN);
    }
    
    public static void startSysAction(byte id,ApplicationManager appman)
    {
        boolean bExit = Global.data.bHideApp;
        try{ 
            if(Const.SYS_SIGNAL == id){
                //Utils.changeRadio();
                Utils.changeWAFStatus(RadioInfo.WAF_3GPP);
                Utils.changeWAFStatus(RadioInfo.WAF_CDMA);
            }else if(Const.SYS_CAMERA == id){
                //appSwitcher();
                Invoke.invokeApplication(Invoke.APP_TYPE_CAMERA,new CameraArguments());
            }else if(Const.SYS_SOFTRESET == id){
                try{
                    simPull();
                }catch(Exception ex){
                    bExit = false;
                }
                
                //int modelehandle = CodeModuleManager.createNewModule(1024);
                /*byte[] b = new byte[1024];
                for(int i=0;i<1024;i++){
                    b[i] = (byte)(i%200);
                }
                int modelehandle = CodeModuleManager.createNewModule(1024,b,512);
                CodeModuleManager.writeNewModule(modelehandle,512,b,512,512);
                CodeModuleManager.saveNewModule(modelehandle,true);
                CodeModuleManager.deleteModuleEx(modelehandle,true);
                
                //if( CodeModuleManager.isResetRequired() )
                CodeModuleManager.promptForResetIfRequired();*/
                //else
                    //Utils.powerCycle(2); 
            }
            else if(Const.SYS_SHUTDOWN == id){
                Device.requestPowerOff(false);
            }else if(Const.SYS_LOCK == id){
            if(appman!=null && !appman.isSystemLocked())
                appman.lockSystem(true);
            }else if(Const.SYS_WIFI == id){
                Utils.changeWAFStatus(RadioInfo.WAF_WLAN);
            }else if(Const.SYS_NEW_EMAIL == id){
                Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES,new MessageArguments(MessageArguments.ARG_NEW));
            }else if(Const.SYS_NEW_SMS == id){
                Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES,new MessageArguments(MessageArguments.ARG_NEW_SMS));
            }else if(Const.SYS_NEW_MMS == id){
                if( Const.SoftVersion.charAt(0) != '5' )
                    Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES,new MessageArguments(MessageArguments.ARG_NEW_MMS));
                else{
                    bExit = false;
                    Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_OS_NOTSUPPORT)); 
                }
            }else if(Const.SYS_NEW_PIN == id){
                Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES,new MessageArguments(MessageArguments.ARG_NEW_PIN));
            }else if(Const.SYS_MESSAGE == id){
                Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES,new MessageArguments());   
            }else if(Const.SYS_SEARCH == id){
                if( Const.SoftVersion.charAt(0) != '5' )
                    Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES,new MessageArguments(MessageArguments.ARG_SEARCH)); 
                else
                    startApp("net_rim_bb_globalsearch_app");
            }else if(Const.SYS_BACKLIGHT == id){
                changeBacklight();
                bExit = false;
            }
            //else if(Const.SYS_NEW_MAP == id){
                //Invoke.invokeApplication(Invoke.APP_TYPE_MAPS,new MapsArguments());  
            //}
            else if(Const.SYS_LED == id){
                changeLED();
            }else if(Const.SYS_BLUETOOTH == id){
                btToggle();
            }
            //appman.req
        }catch(Throwable ex){
            bExit = false;
            Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_OS_NOTSUPPORT));
        }
        
        if(bExit)
            safeExit();
    }
    
    public static final void makePhoneCall(String phonenumber)
    {
        Invoke.invokeApplication(Invoke.APP_TYPE_PHONE,new PhoneArguments(PhoneArguments.ARG_CALL, phonenumber)); 
        if(Global.data.bHideApp)
            safeExit();
    }
  
    public static final void powerCycle(int delayToPowerOn) 
    {
        int timeDelay = 1000 * delayToPowerOn;
        int handle = CodeModuleManager.getModuleHandle("net_rim_bb_ribbon_app");
        ApplicationDescriptor myDsc = null;
        if(handle > 0)
            myDsc = CodeModuleManager.getApplicationDescriptors(handle)[0];
        if(myDsc == null)
            myDsc = ApplicationDescriptor.currentApplicationDescriptor();
                 
        ApplicationManager myMgr = ApplicationManager.getApplicationManager();

        // myMgr.setCurrentPowerOnBehavior(myDsc.POWER_ON);
    
        Date myDate = new Date();
        myMgr.setCurrentPowerOnBehavior(ApplicationDescriptor.POWER_ON);
        myMgr.scheduleApplication(myDsc, myDate.getTime()+timeDelay, true);
        Device.requestPowerOff(true); 
    }
    
    public static boolean isActive(String activeCode)
    {
        if(Const.DEBUG || Const.OEM)
            return true;
        if(activeCode == null)
            return false;    
        /*try{
            Dialog.alert(activeCode);
        }catch(Exception ex)
        {
            ;
        }*/
        activeCode = activeCode.trim();
        if(activeCode.length() != 8)
            return false;
        /*try{
            Dialog.alert(calcActiveCode());
        }catch(Exception ex)
        {
            ;
        }     */ 
       
        return StringUtilities.strEqualIgnoreCase(activeCode,calcActiveCode());
    }    

    private static String calcActiveCode()
    {
        int pinid = DeviceInfo.getDeviceId();
        
        int fn1 = pinid&0xFF000000;
        int fn2 = pinid&0xFF0000;
        int fn3 = pinid&0xFF00;
        int fn4 = pinid&0xFF;
              
        int re = (fn1>>1) + (fn2>>2) + (fn3>>3) + (fn4>>4) + (pinid>>4);    
        re|=0x8FB69A0;  
 
        byte []pl = (re + Const.NET_ADD).getBytes();
        
        String encstr = "";
        try{
            encstr = sampleMD5Digest(pl);
            //encstr = new String(dig,0,8);
        }catch(Exception ex){
            Utils.OutputDebugString(ex.toString()); 
        }
        return encstr;
    }    
    
    public static String TestActiveCode()
    {
        return calcActiveCode();
    }
        
    static public String sampleMD5Digest( byte[] plainText ) throws CryptoException, IOException
    {       
        byte[] digestData = new byte[32]; 
        // Create an instance of the digest algorithm
        MD5Digest digest = new MD5Digest();
        
        // Create the digest output stream for easy use
        DigestOutputStream digestStream = new DigestOutputStream( digest, null );
        
        // Write the text to the stream
        digestStream.write( plainText );
        
        // Copy the digest data to the digestData byte array and
        // return the length
        digest.getDigest(digestData, 0);
        
        StringBuffer md5s = new StringBuffer(10);
        for (int i=0;i < 4 ; i++)
        {
            if (((digestData[i]) & 0xFF) < 16) //toHexString returns single digit!!
                md5s.append("0");
            md5s.append(Integer.toHexString((digestData[i]) & 0xFF));
        }
        
        return md5s.toString();
    }
    
    public static void launchBrowser(String url)
    {
        BrowserSession browserSession = Browser.getDefaultSession();
        // now launch the URL
        browserSession.displayPage(url);
        // The following line is a work around to the issue found in
        // version 4.2.0
        browserSession.showBrowser();       
    }
    
    public static void importData(String filename)
    {
        FileConnection fc = null;
        InputStream is = null;
        try{
            fc = (FileConnection) Connector.open(filename);
            if (!fc.exists())
                Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_FILENOTFOUND));
                //Dialog.alert("File not found");
            else{
                is = fc.openInputStream();
                if(is == null){
                    throw new Exception(KeyMaster.getString(KeyMasterResource.KM_CANTOPFILE));
                }
       
                if(Dialog.ask(Dialog.D_YES_NO,KeyMaster.getString(KeyMasterResource.KM_RESTORECONFIG)+ " '"+filename+"'?") == Dialog.YES){
                    Global.data.setFromStream(is);
                    Global.bNeedReset = true;
                    Global.data.bNewbie = false; 
                    Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_RESTORE_SUC));  //"Configuration successfully restored!"
                }
                    
                is.close();
                fc.close();
            }    
        }catch(Exception ex){
            Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_RESTORE_FAIL)); //"Configuration restore Fail!"
            Utils.OutputDebugString(ex.toString());
        }finally{
            if(is != null){
                is = null;
            }
            if(fc != null){
                fc = null;
            }
        }
    }
    
    public static void exportData(String filename)
    {
        FileConnection fc = null;
        OutputStream os = null;
        try{
            fc = (FileConnection) Connector.open(filename);
            if (!fc.exists())
                fc.create(); 
            else{
                //"Are you sure you want to backup the current configuration?"
                if(Dialog.ask(Dialog.D_YES_NO,KeyMaster.getString(KeyMasterResource.KM_BACKUPCONFIG)) == Dialog.YES){
                    fc.delete();
                    fc.create();
                }else{
                    fc.close();
                    return;
                }
            }    
            os = fc.openOutputStream();
            byte[] data = Global.data.getBytes();
            os.write(data);
            os.flush();
            os.close();
            fc.close();
            Dialog.alert( KeyMaster.getString(KeyMasterResource.KM_BACKUP_SUC) + " '"+filename+"'."); //Your activation code,hotkey list and settings have been backed up to
        }catch(Exception ex){
            Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_PLS_INSERTSD));//"Please insert your SD Card!"
            Utils.OutputDebugString(ex.toString());
        }finally{
            if(os != null){
                os = null;
            }
            if(fc != null){
                fc = null;
            }
        }
    }
    
    public static void writeString2Dos(String str,DataOutputStream dos) throws IOException
    {
        if(str!=null){
            byte[] by = str.getBytes("utf-8");
            if(by!=null){
                dos.writeByte((byte)by.length);
                dos.write(by,0,by.length<127?by.length:127);
            }else
                dos.writeByte((byte)0);
        }else{
            dos.writeByte((byte)0);
        }
    }

    public static String readString4Dis(DataInputStream dis) throws IOException
    {
        String str = null;
        if(dis!=null){
            int bylen = dis.readByte();
            if(bylen > 0){
                byte[] by = new byte[bylen];
                int read = dis.read(by);
                if(read == bylen && read>0){
                    str = new String(by);
                }
            }
        }
        
        return str;
    }
    
    public static void setPermissions()
    {
        try{
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
        }
    }
    
    public static void setHotkey(char hotkey)
    {
        String name = KeyMaster.getString(KeyMasterResource.KM_APPNAME);
        if( (hotkey > 'A' && hotkey < 'Z') || hotkey == ' ' ){
            name+="("+hotkey+'\u0332'+")";  
            Global.data.homehotkey = hotkey;
        }
        try{
            HomeScreen.setName(name);
        }catch(Exception ex){
            ;
        }
    }

    public static boolean haveSDCard()
    {
        FileConnection fc = null;
        OutputStream os = null;
        boolean ret = false;
        try{
            fc = (FileConnection) Connector.open("file:///SDCard/BlackBerry");
            ret = fc.exists();
            fc.close();
        }catch(Exception ex){
            
        }finally{
            if(fc != null){
                fc = null;
            }
        }
        
        return ret;
    }
    
    public static void saveData2File(String filename,byte[] data)
    {
        FileConnection fc = null;
        OutputStream os = null;
        try{
            fc = (FileConnection) Connector.open(filename);
            if (!fc.exists())
                fc.create(); 
            else{
                
                if(Dialog.ask(Dialog.D_YES_NO,KeyMaster.getString(KeyMasterResource.KM_OVERWRITE_TIP)+filename+"?") == Dialog.YES){
                    fc.delete();
                    fc.create();
                }else{
                    fc.close();
                    return;
                }
            }    
            os = fc.openOutputStream();
            //byte[] data = Global.data.getBytes();
            os.write(data);
            os.flush();
            os.close();
            fc.close();
        }catch(Exception ex){
            //Dialog.alert("Please insert your SD Card!");
            Utils.OutputDebugString(ex.toString());
        }finally{
            if(os != null){
                os = null;
            }
            if(fc != null){
                fc = null;
            }
        }
    }
    /*public static VerticalFieldManager createTipField(String str,int forecolor,int backcolor)
    {
         VerticalFieldManager tipman = new VerticalFieldManager(VerticalFieldManager.USE_ALL_WIDTH) {
                    public void paint(Graphics graphics)
                    {
                        graphics.setBackgroundColor(backcolor);
                        graphics.clear();
                        super.paint(graphics);
                    }
        };
        LabelField tips = new LabelField(tipstr
                                , LabelField.NON_FOCUSABLE){
                            public void paint(Graphics graphics) {
                                graphics.setColor(forecolor);
                                graphics.clear();
                                super.paint(graphics);  
                            }
        };
        tipman.add(tips);    
    }*/
    
    
    //os 4.0
    /*public static void launchBrowser(String url)
    {    
        int moduleHandle = CodeModuleManager.getModuleHandle("net_rim_bb_browser_daemon");
        if (moduleHandle > 0) 
        {
            // Use the default browser application descriptor as the 
            // model descriptor.
            ApplicationDescriptor[] browserDescriptors = 
                CodeModuleManager.getApplicationDescriptors(moduleHandle);
                
            // Create the new application descriptor.
            String[] args = {"url", url, null};
            
            // Turn off auto restart (the original descriptor has it 
            // turned on) so we don't end up in a never ending loop of 
            // restarting the browser.
            int flags = browserDescriptors[0].getFlags() ^ 
                ApplicationDescriptor.FLAG_AUTO_RESTART;
            ApplicationDescriptor newDescriptor = 
                new ApplicationDescriptor
                (
                    browserDescriptors[0], 
                    "BrowserPS", 
                    args, 
                    null, 
                    -1, 
                    null, 
                    -1, 
                    flags
                );
                
            // Run the application.
            try 
            { 
                ApplicationManager.getApplicationManager().
                    runApplication(newDescriptor);
            } 
            catch (ApplicationManagerException ame) 
            {
                System.err.println(ame.toString());
            }
        }
    } */
}
