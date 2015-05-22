package bbdev.keymaster;

import java.util.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.util.*;
import net.rim.blackberry.api.invoke.*;
import bbdev.resource.*;


public class AppScreen extends MainScreen implements FocusChangeListener,ListFieldCallback {

        public final static int IDX_APP = 0;
        //public final static int IDX_PHONE = 1;
        public final static int IDX_SYS = 1;
        //public final static int IDX_EVENT = 3;
        
        public final static int TAB_COUNT = 2;
        
        private LabelField tabApp;
        //private LabelField tabPhone;
        private LabelField tabSys;
        //private LabelField tabEvent;
        
        private LabelField spacer1;
        private LabelField spacer2;
        private LabelField spacer3;
        
        private VerticalFieldManager tabArea;
        
        private BBListField[] list = new BBListField[TAB_COUNT];
        private VerticalFieldManager[] manager = new VerticalFieldManager[TAB_COUNT];
        
        private int curTabIndex;
        
        private Font _font = Font.getDefault();
        private int fontsize = 0;      
        private byte bysort;
        private boolean isnewbie;
        
        //private LabelField tips;
        //private VerticalFieldManager tipman;
        private TipMananger tipman;
        
     
        
        public AppScreen() 
        {
            super.setTitle(KeyMaster.getString(KeyMasterResource.APPLICATION_TITLE));
            
            HorizontalFieldManager hManager = new HorizontalFieldManager();
            tabApp = new LabelField(KeyMaster.getString(KeyMasterResource.KM_TAB_APP), LabelField.FOCUSABLE | LabelField.HIGHLIGHT_SELECT);
            //tabPhone = new LabelField(KeyMaster.getString(KeyMasterResource.KM_TAB_PHONE), LabelField.FOCUSABLE | LabelField.HIGHLIGHT_SELECT);
            tabSys = new LabelField(KeyMaster.getString(KeyMasterResource.KM_TAB_SYSTEM), LabelField.FOCUSABLE | LabelField.HIGHLIGHT_SELECT);
            //tabEvent = new LabelField(KeyMaster.getString(KeyMasterResource.KM_TAB_EVENT), LabelField.FOCUSABLE | LabelField.HIGHLIGHT_SELECT);
            spacer1 = new LabelField(" | ", LabelField.NON_FOCUSABLE);
            //spacer2 = new LabelField(" | ", LabelField.NON_FOCUSABLE);
            //spacer3 = new LabelField(" | ", LabelField.NON_FOCUSABLE);
            
            isnewbie = Global.data.bNewbie;
            //try {
                //FontFamily ff = null;
                //ff = FontFamily.forName("Arial Black");
                Font black_font = Font.getDefault().derive(Font.BOLD, Const.FONT_SIZE[Const.FONT_SIZE_MEDIUM], Ui.UNITS_pt);
                //Font black_font = ff.getFont(Font.BOLD, Const.FONT_SIZE[Const.FONT_SIZE_MEDIUM],Ui.UNITS_pt);
                tabApp.setFont(black_font);
                //tabPhone.setFont(black_font);
                tabSys.setFont(black_font);
                //tabEvent.setFont(black_font);
            //} catch (ClassNotFoundException e) {
               //Utils.OutputDebugString("ClassNotFoundException");
            //}
            
            initMenu();
            
            tabApp.setFocusListener(this);
            //tabPhone.setFocusListener(this);
            tabSys.setFocusListener(this);
            //tabEvent.setFocusListener(this);
            hManager.add(tabApp);
            hManager.add(spacer1);
            //hManager.add(tabPhone);
            //hManager.add(spacer2);
            hManager.add(tabSys);
            //hManager.add(spacer3);
            //hManager.add(tabEvent);
            
            add(hManager);
            add(new SeparatorField());
            
             if(isnewbie){
                String str = KeyMaster.getString(KeyMasterResource.KM_NEWBIE_TIPS);
                //String str = "To add hotkey for application, please select the application and click on your favorite key.";
                //tipman = new TipMananger(str,Color.ALICEBLUE,Color.GREEN);
                /*tipman = new VerticalFieldManager() {
                    public void paint(Graphics graphics)
                    {
                        graphics.setBackgroundColor(Color.GREEN);
                        graphics.clear();
                        super.paint(graphics);
                    }
                };
                tips = new LabelField("To add hotkey for application, please select the application and click on your favorite key."
                                , LabelField.NON_FOCUSABLE){
                            public void paint(Graphics graphics) {
                                graphics.setColor(Color.WHITE);
                                //graphics.setBackgroundColor(Color.ALICEBLUE);
                                graphics.clear();
                                super.paint(graphics);  
                            }
                 };
                tipman.add(tips); */
                //add(tipman);               
            }

           
                /*tips = new LabelField("To add hotkey for application, please select the application and click on your favorite key."
                                , LabelField.NON_FOCUSABLE){
                            public void paint(Graphics graphics) {
                                graphics.setColor(Color.DIMGRAY);
                                graphics.setBackgroundColor(Color.ALICEBLUE);
                                graphics.clear();
                                super.paint(graphics);  
                            }
                }; */

                
            //}
       
             
            for(int i=0;i<TAB_COUNT;i++)
            {
                manager[i] = new VerticalFieldManager(); 
            }         
            //appManager = new VerticalFieldManager();
            //phoneManager = new VerticalFieldManager();
            //sysManager = new VerticalFieldManager();
            tabArea = displayTab(IDX_APP);
            add(tabArea);
            
            bysort = Global.data.bySort;
        }
        
        public boolean onClose()
        {


            Global.data.commit();
            if(KeyMaster._this == null)
                UiApplication.getUiApplication().popScreen(this);
            else    
                System.exit(0);
            return true;
        }   
        
        protected void onFocusNotify(boolean focus)
        {
            if(focus){
                //fix bug
                if(isnewbie && isnewbie!=Global.data.bNewbie){
                        isnewbie = Global.data.bNewbie;
                        //manager[IDX_APP].delete(tipman);
                }
                if(Global.bNeedReset){
                    Global.bNeedReset = false;
  
                    for(int i=0;i<TAB_COUNT;i++)
                    {
                        if(list[i]!=null){
                            this.manager[i].delete(list[i]);
                            this.list[i] = null; 
                        }
                    } 
                    if(tabArea!=null){
                        delete(tabArea);
                        tabArea = displayTab(curTabIndex);
                        add(tabArea); 
                    }
                           
                } 
                if(fontsize!=Const.FONT_SIZE[Global.data.iFontsize]){
                    fontsize = Const.FONT_SIZE[Global.data.iFontsize];
                    Font f = Font.getDefault().derive(Font.PLAIN, fontsize, Ui.UNITS_pt);
                    this.setFont(f);     
                }
                if(bysort!=Global.data.bySort){
                    bysort = Global.data.bySort;
                    this.resort();
                }
                /*if(isnewbie && isnewbie!=Global.data.bNewbie){
                    isnewbie = Global.data.bNewbie;
                    manager[IDX_APP].delete(tipman);
                }*/

            }
        }
        
        public void focusChanged(Field field, int eventType) 
        {
            if (tabArea != null) {
                if (eventType == FOCUS_GAINED) {
                    if (field == tabApp) {
                        delete(tabArea);
                        tabArea = displayTab(IDX_APP);
                        add(tabArea);
                    }
                    /* else if (field == tabPhone) {
                        delete(tabArea);
                        tabArea = displayTab(IDX_PHONE);
                        add(tabArea);
                    } */
                    else if (field == tabSys) {
                        delete(tabArea);
                        tabArea = displayTab(IDX_SYS);
                        add(tabArea);
                    }
                    /*else if (field == tabEvent) {
                        delete(tabArea);
                        tabArea = displayTab(IDX_EVENT);
                        add(tabArea);
                    }*/
                }
            }
        }

        public VerticalFieldManager displayTab(int idx) 
        {
            if(list[idx] == null){
                    list[idx] = new BBListField();     
                    list[idx].setCallback((ListFieldCallback)this);
                    
                    if(idx == IDX_APP){
                        //if(isnewbie)
                            //manager[idx].add(tipman);
                    }
                    
                    resetList(idx);        
                    
                    manager[idx].add(list[idx]);
            }
            curTabIndex = idx;
            makeCustomMenu(idx); 
            return manager[idx];
        }
        
        private void resetList(int idx)
        {
            /*if(listData[idx] == null || !listData[idx].isEmpty()){
                listData[idx] = new Vector(20);
            }*/
            
            int action = 0;
            if(IDX_APP == idx)
                action = Const.MOD_ACTION_RUN;
            //else if(IDX_PHONE == idx)
                //action = Const.MOD_ACTION_PHONE;          
            else if(IDX_SYS == idx)
                action = Const.MOD_ACTION_SYSTEM;      
            //else if(IDX_EVENT == idx)
                //action = Const.MOD_ACTION_EVENT;       

            ModItem item;
            if(Global.data.htModItem!=null){
                Enumeration e1 = Global.data.htModItem.elements(); 
                int index = 0;
                while (e1.hasMoreElements()) { 
                    item =  (ModItem)e1.nextElement();
                    if(action == (item.modAction&Const.MOD_ACTION_MASK)){
                        //listData[idx].addElement(item);
                        list[idx].insert(index++,item);
                    }
                }
                //if(IDX_APP == idx)
                 list[idx].sort();   
            }
        }        
        
   
        
        MenuItem makeCallItem,addPCItem,editPCItem,delPCItem;
        MenuItem addHKItem,editHKItem,delHKItem;     
        MenuItem runItem,optItem,aboutItem,closeItem; 
        MenuItem startEventItem,addEventItem,editEventItem,delEventItem;
        MenuItem viewallItem;
        private void initMenu()
        {
            runItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_LAUNCH), 87, 6) {
            public void run() {
                    if(list[curTabIndex].getSize() >0){
                        Object obj = list[curTabIndex].getSelectObject();
                        if(obj!=null)
                            runApp((ModItem)obj);
                    }
                    //if(Global.listTable.size() > 0)
                       // runApp(Global.listTable.elementAt(c.getSelectedIndex()));
                } 
            };
            
            addHKItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_ADD_HOTKEY), 88, 7) {
            public void run() {
                    UiApplication.getUiApplication().pushScreen(new SysmodScreen(list[curTabIndex]));
                } 
            };
            
            editHKItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_EDIT_HOTKEY), 89, 7) {
            public void run() {
                    if(list[curTabIndex].getSize() > 0){
                        Object obj = list[curTabIndex].getSelectObject();
                        if(obj!=null){
                            UiApplication.getUiApplication().pushScreen(new EditModScreen(list[curTabIndex],
                                (ModItem)obj));
                        }
                    }
                } 
            };        
            
            delHKItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_DEL_HOTKEY), 90, 7) {
            public void run() {
                    delete();
                    //UiApplication.getUiApplication().pushScreen(new SysmodScreen(list[curTabIndex]));
                } 
            };           

            makeCallItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MAKECALL), 88, 7) {
            public void run() {
                    if(list[curTabIndex].getSize() > 0){
                        Object obj = list[curTabIndex].getSelectObject();
                        ModItem item = (ModItem)obj;
                        Utils.makePhoneCall(item.modName);
                    }
                } 
            };
            
            /*addPCItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_ADD_PHONECALL), 88, 7) {
            public void run() {
                    if(Utils.isActive(Global.data.activeCode)){
                        UiApplication.getUiApplication().pushScreen(new PhoneCallScreen(list[curTabIndex],null));
                    }else
                        Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_REG_ERROR));
                } 
            };*/
            
            /*editPCItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_EDIT_PHONECALL), 89, 7) {
            public void run() {
                    if(list[curTabIndex].getSize() > 0){
                        Object obj = list[curTabIndex].getSelectObject();
                        if(obj!=null){
                            UiApplication.getUiApplication().pushScreen(new PhoneCallScreen(list[curTabIndex]
                                ,(ModItem)obj));
                        }
                    }
                } 
            };*/        
            
            delPCItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_DEL_PHONECALL), 90, 7) {
            public void run() {
                    delete();
                } 
            };  
                
            optItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_OPTION), 98, 8) {
            public void run() {
                    UiApplication.getUiApplication().pushScreen(new OptionScreen());
                } 
            }; 
            
            aboutItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_ABOUT), 99, 9) {
            public void run() {
                    UiApplication.getUiApplication().pushScreen(new About());
                } 
            };
            
            if(Const.DEBUG){
                viewallItem = new MenuItem("View All App", 98, 8) {
                public void run(){
                        UiApplication.getUiApplication().pushScreen(new AllModScreen());
                    } 
                }; 
            }

            /*startEventItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_STRATEVENT), 87, 6) {
                public void run(){
                    UiApplication.getUiApplication().requestBackground();
                    Utils.statrEvent(Characters.SPACE,KeypadListener.STATUS_NOT_FROM_KEYPAD|KeypadListener.STATUS_ALT,200);
                } 
            };     */ 
            addEventItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_ADDEVENT), 90, 8) {
                public void run() {
                    UiApplication.getUiApplication().pushScreen(new EventScreen(list[curTabIndex],null));
                } 
            };    
            editEventItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_EDITEVENT), 90, 8) {
                public void run() {
        
                }
            };  
            delEventItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_DELEVENT), 90, 8) {
                public void run() {
                

                } 
            };       
            /*closeItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_CLOSE), 100, 10) {
            public void run() {
                    Global.data.commit();
                    System.exit(0);
                } 
            };*/
        }
        
      

        private void delete() 
        {
            ModItem item = (ModItem)list[curTabIndex].getSelectObject();
            if(item!=null)
            {
                if(Dialog.ask(Dialog.D_YES_NO,KeyMaster.getString(KeyMasterResource.KM_DELETE_CONFIRM)) == Dialog.YES)
                {
                    list[curTabIndex].delete(list[curTabIndex].getSelectedIndex());
                    Global.data.htModItem.remove(new Integer(item.hotKey));
                    //resort();
                }
            }
        }
        
        private void resort()
        {
            for(int i=0;i<TAB_COUNT;i++)
            {
                if(list[i]!=null)
                    list[i].sort(); 
            }         
            //list[curTabIndex].sort();   
        }
        
        
        
        /*protected void makeMenu(Menu menu, int instance) 
        {
            runItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_RUN), 87, 6) {
            public void run() {
                    if(list[curTabIndex].getSize() >0){
                        runApp((ModItem)list[curTabIndex].getSelectObject());
                    }
                    //if(Global.listTable.size() > 0)
                       // runApp(Global.listTable.elementAt(c.getSelectedIndex()));
                } 
            };
            
            addHKItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_ADD_HOTKEY), 88, 7) {
            public void run() {
                    UiApplication.getUiApplication().pushScreen(new SysmodScreen(list[curTabIndex]));
                } 
            };
            
            editHKItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_EDIT_HOTKEY), 89, 7) {
            public void run() {
                    UiApplication.getUiApplication().pushScreen(new SysmodScreen(list[curTabIndex]));
                } 
            };        
            
            delHKItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_DEL_HOTKEY), 90, 7) {
            public void run() {
                    UiApplication.getUiApplication().pushScreen(new SysmodScreen(list[curTabIndex]));
                } 
            };           
            
            addPCItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_ADD_PHONECALL), 88, 7) {
            public void run() {
                    UiApplication.getUiApplication().pushScreen(new SysmodScreen(list[curTabIndex]));
                } 
            };
            
            editPCItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_EDIT_PHONECALL), 89, 7) {
            public void run() {
                    UiApplication.getUiApplication().pushScreen(new SysmodScreen(list[curTabIndex]));
                } 
            };        
            
            delPCItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_DEL_PHONECALL), 90, 7) {
            public void run() {
                    UiApplication.getUiApplication().pushScreen(new SysmodScreen(list[curTabIndex]));
                } 
            };     
           
            optItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_OPTION), 98, 8) {
            public void run() {
                //UiApplication.getUiApplication().pushScreen(new OptionScreen());
                } 
            }; 
            aboutItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_ABOUT), 99, 9) {
            public void run() {
                    Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_ABOUT_STR));
                } 
            };
            closeItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_CLOSE), 100, 10) {
            public void run() {
                    Global.data.commit();
                    System.exit(0);
                } 
            };
            
            removeAllMenuItems();
            menu.add(runItem);
            menu.add(addHKItem);
            menu.add(editHKItem);
            menu.add(delHKItem);
            menu.add(optItem);
            menu.add(aboutItem);
            menu.add(closeItem);
            
            super.makeMenu(menu,instance);
        }*/
        
        private void makeCustomMenu(int index)
        {
            if(IDX_APP == index){
                this.removeAllMenuItems();
                this.addMenuItem(runItem);
                this.addMenuItem(addHKItem);
                this.addMenuItem(editHKItem);
                this.addMenuItem(delHKItem);
                this.addMenuItem(optItem);
                this.addMenuItem(aboutItem);
                if(Const.DEBUG)
                    this.addMenuItem(viewallItem);
                //this.addMenuItem(closeItem);
            }
            /*else if(IDX_PHONE == index){
                this.removeAllMenuItems();
                this.addMenuItem(makeCallItem);
                this.addMenuItem(addPCItem);
                this.addMenuItem(editPCItem);
                this.addMenuItem(delPCItem);
                this.addMenuItem(optItem);
                this.addMenuItem(aboutItem);
                if(Const.DEBUG)
                    this.addMenuItem(viewallItem);
                //this.addMenuItem(closeItem);
            }*/
            else if(IDX_SYS == index){
                this.removeAllMenuItems();
                this.addMenuItem(runItem);
                this.addMenuItem(editHKItem);
                this.addMenuItem(optItem);
                this.addMenuItem(aboutItem);
                if(Const.DEBUG)
                    this.addMenuItem(viewallItem);
               // this.addMenuItem(closeItem); 
            }
            /*else if(IDX_EVENT == index){
                this.removeAllMenuItems();
                this.addMenuItem(startEventItem);
                this.addMenuItem(addEventItem);
                this.addMenuItem(editEventItem);
                this.addMenuItem(delEventItem);
                this.addMenuItem(optItem);
                this.addMenuItem(aboutItem);
               // this.addMenuItem(closeItem); 
            }*/
            
            
            
        }
        
        /*private void exitApp()
        {
            Global.data.commit();
            System.exit(0);    
        }*/
        
        protected boolean keyChar(char c, int status, int time)
        {
            //if(!Global.data.bActiveCode && !Const.DEBUG)
                //return super.keyChar( c, status,time );
                
            int key = 0;
            if(c >= 'a' && c<='z'){
                key = (int)c;
            }else if(c >= 'A' && c<='Z'){
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
            
            ModItem item = (ModItem)Global.data.htModItem.get(new Integer(key));
            if(item!=null){
                runApp(item);
            }    
           /* int index = Global.listMap.indexOf(key); 
            
            if(index >= 0)
                runApp(Global.listTable.elementAt(index));*/
                
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

        public void drawListRow(ListField list, Graphics g, int index, int y, int w) 
        {
            ModItem item = (ModItem)this.list[curTabIndex].get(index);         
            g.drawText(item.getShowString(), 5, y, 0, w);
        }
        public int getPreferredWidth(ListField list)
        {
            return Display.getWidth();
        }
        public Object get(ListField list, int index) 
        {
            return this.list[curTabIndex].get(index);
            //return Global.listString.elementAt(index);
        }
        public int indexOfList(ListField list, String p, int s) 
        {
            return 0;
           // return Global.listString.indexOf(p, s);
        }
        
        private void runApp(ModItem item)
        {
            if((item.modAction&Const.MOD_ACTION_MASK) == Const.MOD_ACTION_PHONE){
                Utils.makePhoneCall(item.modName);
            }else if((item.modAction&Const.MOD_ACTION_MASK) == Const.MOD_ACTION_SYSTEM){
                Utils.startSysAction((byte)((item.modAction&Const.SUB_ACTION_MASK)>>8),
                                ApplicationManager.getApplicationManager());
            }else{
                if(Utils.findInList(item.modName,Const.NOT_RUN_LIST) >= 0)
                    Utils.startApp(item.modName,false);
                else
                    Utils.startApp(item.modName,true);
            }
            /*try{
                
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
                    //}
                    
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
            }*/
            
        }
   }
