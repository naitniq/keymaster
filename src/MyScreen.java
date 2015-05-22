/*
 * MyScreen.java
 * bbdev, 2008
 */

package bbdev.keymaster;

import java.util.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.util.*;
import net.rim.blackberry.api.invoke.*;
import bbdev.resource.*;

final class MyScreen extends MainScreen implements ListFieldCallback {
    
    private static ListField c;
    //private StoreData data;
    //private static IntVector listTable;    //ProcessId
    //private static IntVector listMap;    //index -> key
    
    //private Vector listString = new Vector(30);     //index -> display str
    private Font _font = Font.getDefault();
    
    MyScreen() 
    {    
        super.setTitle(KeyMaster.getString(KeyMasterResource.APPLICATION_TITLE));
        c = new ListField();     
        c.setCallback((ListFieldCallback)this);
        
        resetList();
        add(c);
        
        add(new SeparatorField()); 
            
        String poweredstr = "Powered By BBDev.org";
        LabelField lf = new LabelField(poweredstr);
        try{
            int s = Const.SCREEN_WIDTH - _font.getAdvance(poweredstr)- 5;
            if( s >= 0 && s < Const.SCREEN_WIDTH )
                lf.setPosition(s);
        }catch(Exception ex){
            Utils.OutputDebugString(ex.toString());
        }
        add(lf);
        //resetList();
    }
    

    /*protected boolean openProductionBackdoor(int backdoorCode) {
        switch( backdoorCode ) {
          // BACKDOOR ¨C converts four chars to an int via bit shifting and a
          // bitwise OR
          case ( 'A' << 24 ) | ( 'B' << 16 ) | ( 'C' << 8 ) | 'D':
            UiApplication.getUiApplication().invokeLater (new Runnable() {
                public void run()
                {
                   Dialog.inform("Backdoor sequence received");
                }
            });
            return true; // handled
        }
        return super.openProductionBackdoor(backdoorCode);
    }*/
    
    private void resetList()
    {
        if(!Global.listString.isEmpty())
        {
            Global.listString.removeAllElements();
        }
        if(!Global.listTable.isEmpty())
        {
            Global.listTable.removeAllElements();
        }        
        
        if(!Global.listName.isEmpty())
        {
            int len = Global.listName.size();
            
            int modhandle = 0;
            int index = 0;
            String moudlename;
            
            for(int i=0;i<len;i++)
            {
               // modhandle = tab[i];
                moudlename = (String)Global.listName.elementAt(i);
                modhandle = CodeModuleManager.getModuleHandle(moudlename);
                if(modhandle == 0)
                {
                    modhandle = Utils.getSPHandle(moudlename);
                    //Dialog.alert( Utils.StringReplace(KeyMaster.getString(KeyMasterResource.KM_MOD_ERROR),"%", moudlename) );
                    if(modhandle == 0)
                    {
                        Global.listName.removeElementAt(i);
                        Global.listMap.removeElementAt(i);
                        i--;
                        len--;
                        continue;
                    }
                    else
                    {
                        moudlename = Utils.getSpAppName(modhandle);
                    }
                }
                else
                {            
                    moudlename = CodeModuleManager.getApplicationDescriptors(modhandle)[0].getName();
                    //os 4.2.1
                    moudlename = CodeModuleManager.getApplicationDescriptors(modhandle)[0].getLocalizedName();   
                }

                if(moudlename == null)
                    moudlename = CodeModuleManager.getModuleName(modhandle);
                    
                //Utils.OutputDebugString("ModuleName:" + CodeModuleManager.getModuleName(modhandle));
                //Utils.OutputDebugString("moduleAliasName:" + CodeModuleManager.getModuleAliasName(modhandle,0));
                //Utils.OutputDebugString("moduleDescription:" +CodeModuleManager.getModuleDescription(modhandle));
                //Utils.OutputDebugString("ApplicationDescriptors:" +CodeModuleManager.getApplicationDescriptors(modhandle)[0].getLocalizedName());
                //Utils.OutputDebugString("ApplicationDescriptors:" +CodeModuleManager.getApplicationDescriptors(modhandle)[0].getName());
                //Utils.OutputDebugString("ApplicationDescriptors2:" +CodeModuleManager.getApplicationDescriptors(modhandle)[1]);
                
                if( moudlename != null )
                {
                    Global.listString.insertElementAt(moudlename+" ["+ (char)Global.listMap.elementAt(index) + "]", index);
                    Global.listTable.insertElementAt(modhandle,index);
                    c.insert(index++);
                }
                else
                {
                    Global.listName.removeElementAt(i);
                    Global.listMap.removeElementAt(i);
                    i--;
                    len--;
                }
            }
        }
    }
    
    protected void makeMenu(Menu menu, int instance) 
    {
        MenuItem runItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_RUN), 87, 6) {
        public void run() {
                if(Global.listTable.size() > 0)
                    runApp(Global.listTable.elementAt(c.getSelectedIndex()));
            } 
        };
        MenuItem addItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_ADD), 88, 7) {
        public void run() {
                UiApplication.getUiApplication().pushScreen(new SysmodScreen(c));
            } 
        };
        MenuItem delItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_DELETE), 89, 8) {
        public void run() {
               delete();
            } 
        };
        MenuItem optItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_OPTION), 89, 8) {
        public void run() {
               UiApplication.getUiApplication().pushScreen(new OptionScreen());
            } 
        };
        MenuItem modItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_VIEWMOD), 98, 8) {
        public void run() {
                UiApplication.getUiApplication().pushScreen(new AllModScreen());
                //String modname = CodeModuleManager.getModuleName(Global.listTable.elementAt(c.getSelectedIndex()));
                //Dialog.alert(modname);
            } 
        }; 
        MenuItem aboutItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_ABOUT), 99, 9) {
        public void run() {
              Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_ABOUT_STR));
            } 
        };
        MenuItem closeItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_CLOSE), 100, 10) {
        public void run() {
                Global.data.commit();
                System.exit(0);
            } 
        };
        
        menu.add(runItem);
        menu.add(addItem);
        menu.add(delItem);
        menu.add(optItem);
        menu.add(modItem);
        menu.add(aboutItem);
        menu.add(closeItem);
    }
    
    //protected void onFocus(int direction)
    //{
    //    resetList();
    //}
    
    //softreset
    public static final void powerCycle (int delayToPowerOn) 
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
    
        myMgr.scheduleApplication(myDsc, myDate.getTime()+timeDelay, true);
        Device.requestPowerOff(true); 
    }
    
    private void exitApp()
    {
        Global.data.commit();
        System.exit(0);    
    }

    protected boolean keyChar(char c, int status, int time)
    {
        if(!Global.data.bActiveCode && !Const.DEBUG)
            return super.keyChar( c, status,time );
             
        int key = 0;
        if(c >= 'a' && c<='z')
        {
            key = (int)c;
        }      
        else if(c >= 'A' && c<='Z')
        {
            if(Global.data.bShiftMode)
                key = (int)c;
            else
                key = (int)(c-'A'+'a');
        }
        //else if(c == Characters.SPACE && status == KeypadListener.STATUS_ALT)
       // {
       //     Device.requestPowerOff(true);
       // }
        /*else if( c == Characters.SPACE && Global.data.bCloseSig )
        {
            int state = RadioInfo.getState();
            if( state == RadioInfo.STATE_ON )
                Radio.requestPowerOff();
            else if( state == RadioInfo.STATE_OFF )
                Radio.requestPowerOn();
            exitApp();
        }*/
        else
            return super.keyChar( c, status,time );
            
        int index = Global.listMap.indexOf(key); 
           
        if(index >= 0)
            runApp(Global.listTable.elementAt(index));
            
        return true;    
    }
    
    
    protected boolean keyDown(int keycode, int time) 
    {
       //Utils.OutputDebugString( "keyDown:" + keycode );
         //DxTools.OutputDebugString("status:" + status);

        int key = Keypad.key( keycode );
        
        if( key == Keypad.KEY_DELETE || key == Keypad.KEY_BACKSPACE )
        { 
            delete();
            return true;
        }
        /*else if(key == Keypad.KEY_SEND &&  Global.data.bSoftReset )
        {
            //if( CodeModuleManager.isResetRequired() )
               // CodeModuleManager.promptForResetIfRequired();
           // else
                powerCycle(2); 
            exitApp();       
            return true;
        }
        else if(key == Keypad.KEY_END &&  Global.data.bShutdown )
        {
            Device.requestPowerOff(false);
            exitApp();
            return true;
        }*/
        else
            return super.keyDown( keycode, time );
    }
   
    /*private void insert(String toInsert, int index) 
    {
        listString.insertElementAt(toInsert, index);
    }*/
    private void delete() 
    {
        int index = c.getSelectedIndex();
        if(index>=0)
        {
            if(Dialog.ask(Dialog.D_YES_NO,KeyMaster.getString(KeyMasterResource.KM_DELETE_CONFIRM)) == Dialog.YES)
            {
                c.delete(index);
                Global.listTable.removeElementAt(index);
                Global.listMap.removeElementAt(index); 
                Global.listName.removeElementAt(index);
                Global.listString.removeElementAt(index);
            }
        }
    }
    
    public void drawListRow(ListField list, Graphics g, int index, int y, int w) 
    {
        String text = (String)Global.listString.elementAt(index);         
        g.drawText(text, 5, y, 0, w);
    }
    public int getPreferredWidth(ListField list)
    {
        return Display.getWidth();
    }
    public Object get(ListField list, int index) 
    {
        return Global.listString.elementAt(index);
    }
    public int indexOfList(ListField list, String p, int s) 
    {
        return Global.listString.indexOf(p, s);
    }
    
    private void runApp(int modHandle)
    {
        try{
            
            ApplicationManager appman = ApplicationManager.getApplicationManager();

            if(modHandle > 0)
            {
                ApplicationDescriptor[] apDes = CodeModuleManager.getApplicationDescriptors(modHandle); 
                ApplicationDescriptor descriptor = apDes[0]; 
                String modname = descriptor.getModuleName();
                
                    //ApplicationDescriptor[] visibleAppDes = appman.getVisibleApplications();
                int processID = appman.getProcessId(descriptor);
                
                Global.data.commit();
    
                //Dialog.alert(modname);
                if(modname.equalsIgnoreCase(Const.SYSTEM_LIST[Const.MESSAGE_APP]))
                {
                    Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES,new MessageArguments());
                }
                else if(modname.equalsIgnoreCase(Const.SYSTEM_LIST[Const.CAMERA_APP]))
                {
                    Invoke.invokeApplication(Invoke.APP_TYPE_CAMERA,new CameraArguments());
                }
                else
                {    
                    if(processID == -1)
                    {
                        processID = appman.runApplication(descriptor,true);
                        //int curProcessID = appman.getForegroundProcessId();
                        //if(curProcessID != processID)
                            //appman.requestForeground(processID);
                    }
                    else
                        appman.requestForeground(processID);
                    //else
                    //{
                        //appman.getForegroundProcessId();
                    //   appman.requestForeground(processID);
                        
                    //}
                }
                
                try {
                    Thread.sleep(800);
                } catch (InterruptedException ie) {
                    Utils.OutputDebugString(ie.toString());
                }
                    
                int curProcessID = appman.getForegroundProcessId();
                if(curProcessID != processID)
                    appman.requestForeground(processID);
            }
            else if(Utils.isSPHandle(modHandle))
            {
                if(modHandle > Const.SP_MESSAGE){
                    Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES,new MessageArguments(Utils.getSpArg(modHandle)));
                }else if(modHandle == Const.SP_WIFI){
                    Utils.changeWAFStatus(RadioInfo.WAF_WLAN);
                }else if(modHandle == Const.SP_LOCKSYSTEM){
                    if(!appman.isSystemLocked())
                        appman.lockSystem(true);
                }else if(modHandle == Const.SP_SHUTDOWN){
                    Device.requestPowerOff(false);
                }else if(modHandle == Const.SP_SOFTRESET){
                    if( CodeModuleManager.isResetRequired() )
                        CodeModuleManager.promptForResetIfRequired();
                    else
                        powerCycle(2); 
                }else if(modHandle == Const.SP_SIGNAL){
                    Utils.changeRadio();
                }
            }
            else
                return;
                
            System.exit(0); 
        }catch(ApplicationManagerException ex){
            //Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_APP_NOTRUN));
            Utils.OutputDebugString(ex.toString());
        }catch(RuntimeException  ex){
            //Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_APP_NOTRUN));
            Utils.OutputDebugString(ex.toString());      
        }catch(Exception ex){
            //Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_APP_NOTRUN));
            Utils.OutputDebugString(ex.toString());
        }
        
    }
    
} 
