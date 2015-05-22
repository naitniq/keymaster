/*
 * SysmodScreen.java
 * bbdev, 2008
 */

package bbdev.keymaster;

import java.util.Vector;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.util.*;
import bbdev.resource.*;

final class SysmodScreen extends TabScreen implements ListFieldCallback {
    
    //private static BBListField _list;
    private static BBListField _customlist;
    
    public final static String[] TAB_STR  = {"A-G","H-N","O-T","U-Z","ALL"};
    public final static char[][] TAB_RANGE = { {'A','G'},{'H','N'},{'O','T'},{'U','Z'},{0,65535} };
    public final static char[][] TAB_RANGE_LOW = { {'a','g'},{'h','n'},{'o','t'},{'u','z'},{0,65535} };
    
    private BBListField[] tablist;
    
    ///private static IntVector listTable;    //ProcessId
    //private static IntVector listMap;    //index -> key
    
    //private  Vector listMod  = new Vector(100); 
    //private  IntVector listHandle = new IntVector(100); 
    private static SysmodScreen _this;
    
    SysmodScreen(BBListField list)
    {
        super(TAB_STR);

        //mainlist = lf;
        _this = this;
        _customlist = list;
        
        //_list = new BBListField(Const.SORT_BY_APPNAME);     
       // _list.setCallback((ListFieldCallback)this);   
        
        //refreshList();
        
        //this.add(_list);
    }

    protected void init(String[] tab)
    {
        VerticalFieldManager tipman =  new TipMananger(KeyMaster.getString(KeyMasterResource.KM_ADD_HOTKEY),
                                        Color.WHITE,Color.GRAY);
        this.add(tipman);
        tablist = new BBListField[tabCount];
        super.init(tab);
    }
    
    /*protected void onFocusNotify(boolean focus)
    {

    }*/
    
    protected VerticalFieldManager displayTab(int idx) 
    {
            if(tablist[idx] == null){
                    tablist[idx] = new BBListField(Const.SORT_BY_APPNAME);     
                    tablist[idx].setCallback((ListFieldCallback)this);
                    
                    resetList(idx);        
                    
                    manager[idx].add(tablist[idx]);
            }
            return manager[idx];
    }  
    
    private boolean findInBlockList(String mod)
    {
        for(int i=0;i<Const.BLOCK_LIST.length;i++){
            if(mod.equalsIgnoreCase(Const.BLOCK_LIST[i]))
                return true;
        }
        return false;
    }
    
    private boolean findInWhiteList(String mod)
    {
        for(int i=0;i<Const.WHITE_LIST.length;i++){
            if(mod.equalsIgnoreCase(Const.WHITE_LIST[i]))
                return true;
        }
        return false;
    }
    
    
    private String getFixedName(String modname,String appname)
    {
        for(int i=0;i<Const.FIX_NAME_LIST.length;i++){
            if(modname.equalsIgnoreCase(Const.FIX_NAME_LIST[i][0])){
                return Const.FIX_NAME_LIST[i][1];
            }
        }
        return appname;
    }
        
    
    private void resetList(int idx)
    {
        int modhandle = 0;
        ApplicationDescriptor appdesc  = null;
        ApplicationDescriptor[] appdescs  = null ;
        ApplicationManager appman = null;
        String modname = null;
        String appname  = null;
        String applocalname = null;
        int index = 0;
        
        //add auto-detected module to list
        int handles[] = CodeModuleManager.getModuleHandles();
        for(int i=handles.length-1;i>=0;i--){
            try{ 
                //if(listHandle.contains(handles[i]))  
                // continue;
                
                appdescs = CodeModuleManager.getApplicationDescriptors(handles[i]);
                if(appdescs!=null)
                    appdesc = appdescs[0];
                else 
                    appdesc = null;
                //if( appdesc.getFlags() >0 && appdesc.getFlags() != ApplicationDescriptor.FLAG_SYSTEM)
                //if(appdesc.getFlags() == ApplicationDescriptor.FLAG_AUTO_RESTART)
                    //continue;
                    
                modname = CodeModuleManager.getModuleName(handles[i]); 
                boolean bWhite = findInWhiteList(modname);
                
                if(!bWhite){
                    if(CodeModuleManager.getModuleFlags(handles[i]) == CodeModuleManager.MODULE_FLAG_DELETE)
                        continue;
                    if(CodeModuleManager.isLibrary(handles[i]))// || !CodeModuleManager.isMidlet(handles[i]))
                        continue;
                     if(findInBlockList(modname))
                        continue;     
                }
                
                //boolean bBlock = findInBlockList(modname);

                    
                if(appdesc!=null){
                    appname = appdesc.getName();
                    applocalname =  Utils.getLocalizedName(appdesc,appname);
                }else{
                    appname = modname;
                    applocalname = modname;
                }
                
                if(appname == null){
                    appname = applocalname;
                }else{
                    appname = getFixedName(modname,appname);
                    applocalname = getFixedName(modname,applocalname);
                }
                    
                    //continue;
                
                char appchar = appname.charAt(0);
                if(applocalname != null)
                    appchar = applocalname.charAt(0);
                
                if(appchar < TAB_RANGE[idx][0] || appchar > TAB_RANGE[idx][1]){
                    if(appchar < TAB_RANGE_LOW[idx][0] || appchar > TAB_RANGE_LOW[idx][1]){
                        continue;
                    }
                }
                                /*if(modname.indexOf("app") < 0)
                            continue;  
                        if(modname.indexOf("resource") > 0)
                            continue;
                        if(modname.indexOf("theme") > 0)
                            continue;   
                        if(modname.indexOf("bbapi") > 0)
                            continue;           
                        if(modname.indexOf("lib") > 0)
                            continue;  */
                            
                if(!bWhite){
                    if(appname.startsWith("net_rim")){
                        if(applocalname!=null && applocalname.startsWith("net_rim"))
                            continue; 
                    }
                }   
                
            // if(applocalname.startsWith("Now"))
                    //Utils.OutputDebugString("NP:" + modname);
                    
                if(appname.equalsIgnoreCase("hotkey manager"))
                        continue;
                        
                appman = ApplicationManager.getApplicationManager();  
                if(!bWhite){
                    if(appman.isConsoleDescriptor(appdesc))  
                        continue;
                    //if(findInBlockList(modname))
                        //continue;
                }
                //if( appdesc.getFlags() == ApplicationDescriptor.FLAG_SYSTEM)    
                    //modname = "Sys_"+modname;
                if(applocalname!=null)
                    appname =  applocalname;

                //listHandle.addElement(handles[i]);
            // listMod.addElement(appname);
                //if(handles[i] >= 0)
                tablist[idx].insert(index++,new ModItem(modname,appname,0));
            }catch(Exception ex){
                Utils.OutputDebugString(ex.toString());
                if(appname!=null)
                    Dialog.alert("Add " + appname + " error!");
                continue;
            }   
            //_list.insert(index++);
        }
        
        tablist[idx].sort();
    }

    /*private void refreshList()
    {
        int modhandle;
        ApplicationDescriptor appdesc;
        ApplicationManager appman;
        String modname,appname;
        int index = 0;
        
    
        
        //add auto-detected module to list
        int handles[] = CodeModuleManager.getModuleHandles();
        for(int i=handles.length-1;i>=0;i--)
        {
            if(CodeModuleManager.getModuleFlags(handles[i]) == CodeModuleManager.MODULE_FLAG_DELETE)
                continue;
            if(CodeModuleManager.isLibrary(handles[i]))// || !CodeModuleManager.isMidlet(handles[i]))
                continue;
            //if(listHandle.contains(handles[i]))  
               // continue;
                
            appdesc = CodeModuleManager.getApplicationDescriptors(handles[i])[0];
            //if( appdesc.getFlags() >0 && appdesc.getFlags() != ApplicationDescriptor.FLAG_SYSTEM)
            if(appdesc.getFlags() >0 && appdesc.getFlags() == ApplicationDescriptor.FLAG_AUTO_RESTART)
                continue;
                
            modname = CodeModuleManager.getModuleName(handles[i]); 
            appname = appdesc.getName();
            if(appname == null)
                continue;
                
            if(appname.startsWith("net_rim"))
            {
               continue; 
                //if(modname.indexOf("app") < 0)
                  //  continue;  
                //if(modname.indexOf("resource") > 0)
                 //   continue;
                //if(modname.indexOf("theme") > 0)
                   // continue;   
               // if(modname.indexOf("bbapi") > 0)
                   // continue;           
                //if(modname.indexOf("lib") > 0)
                   // continue;  
            }
            
            if(appname.equalsIgnoreCase("hotkey manager"))
                continue;
                
            appman = ApplicationManager.getApplicationManager();  
            //os 4.2.1 
            if(appman.isConsoleDescriptor(appdesc))  
                continue;
            if(findInBlockList(modname))
                continue;
            //if( appdesc.getFlags() == ApplicationDescriptor.FLAG_SYSTEM)    
                //modname = "Sys_"+modname;
            
            //os 4.2.1   
            appname = appdesc.getLocalizedName();   
            //listHandle.addElement(handles[i]);
           // listMod.addElement(appname);
            if(handles[i] >= 0)
                _list.insert(index++,new ModItem(modname,appname,0));
            //_list.insert(index++);
        }
        
        _list.sort();
    }*/
    
    /*public void insertItem(String modname,int index)
    {
        int modhandle = CodeModuleManager.getModuleHandle(modname);
        if(modhandle > 0)
        {
            ApplicationDescriptor appdesc = CodeModuleManager.getApplicationDescriptors(modhandle)[0];
            //String appname = appdesc.getName();
            //os 4.2.1
            String appname = appdesc.getLocalizedName();
        
            if(appname == null)
                appname = CodeModuleManager.getModuleName(modhandle);
                            
            if( appname != null && !listHandle.contains(modhandle))
            {
                listHandle.insertElementAt(modhandle,index);
                listMod.insertElementAt(appname,index);
                _list.insert(index);
            }
            else
            {
                String tips = Utils.StringReplace(KeyMaster.getString(KeyMasterResource.KM_MOD_EXIST),"%",appname);
                Dialog.alert(tips);   
            }
        }
    }*/
    
    protected void makeMenu(Menu menu, int instance) 
    {
        /*MenuItem addexItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_EXADD), 99, 9) {
        public void run() {
                UiApplication.getUiApplication().pushScreen(new AddModScreen(_this));
            } 
        }; */
        MenuItem viewDetailItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_VIEW_DETAIL), 100, 7) {
            public void run() {
                    ModItem item = (ModItem)tablist[curTabIndex].getSelectObject();
                    int modhandle = CodeModuleManager.getModuleHandle(item.modName);
                    Utils.viewDetailInfo(modhandle);
                } 
        };        
        
        MenuItem closeItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_CANCEL), 100, 10) {
        public void run() {
               close();
            } 
        };
        
        menu.add(viewDetailItem);
        menu.add(closeItem);
    }
    
    protected boolean keyChar(char c, int status, int time)
    {
        //if(Const.DEBUG){
           /* if(c == 'd' || c == 'D')
            {   
                ModItem item = (ModItem)tablist[curTabIndex].getSelectObject();
                int modhandle = CodeModuleManager.getModuleHandle(item.modName);
                AllModScreen.viewDetailInfo(modhandle);
                return true;
            }*/
        //}

        
        int key = 0;
        if(c >= 'a' && c<='z'){
            key = (int)c;
        }else if(c >= 'A' && c<='Z'){
            if(Global.data.bShiftMode)
                key = (int)c;
            else
                key = (int)(c-'A'+'a');
        }else{
          return super.keyChar( c, status,time );
        }
        
        if(key > 0){
            if(!Global.data.htModItem.containsKey(new Integer(key)))
            //if(!Global.listMap.contains(key))
            {
                //int handle = listHandle.elementAt(_list.getSelectedIndex());
                //if((handle > 0 || Utils.isSPHandle(handle)) && !Global.listTable.contains(handle))
                /*if(handle > 0)
                {
                    String modname = CodeModuleManager.getModuleName(handle);
                    String appname = (String)listMod.elementAt(_list.getSelectedIndex());
                    
                    ModItem item = new ModItem(modname,appname,key);
                        
                    //Global.listMap.addElement(key);
                    //Global.listString.addElement((String)listMod.elementAt(_list.getSelectedIndex())+" ["+ (char)key + "]");
                    int index = _customlist.getSize()>0?_customlist.getSize()-1:0;
                    _customlist.insert(index,item);
                    Global.data.htModItem.put(new Integer(key),item);
                }*/
                

                ModItem item = (ModItem)tablist[curTabIndex].getSelectObject();
                item.hotKey = (int)key;
                int index = _customlist.getSize()>0?_customlist.getSize()-1:0;
                 _customlist.insert(index,item);
                 _customlist.sort();
                Global.data.htModItem.put(new Integer(key),item);
                
                if(Global.data.bNewbie)
                    Global.data.bNewbie = false;
                    
                UiApplication.getUiApplication().popScreen(this);
            }
            else
            {
                String tips = KeyMaster.getString(KeyMasterResource.KM_ALREADYEXIST);
                tips = tips.replace('%',(char)key);
                Dialog.alert(tips);
            }
            return true;
        }else
            return super.keyChar( c, status,time );
    }

    public void drawListRow(ListField list, Graphics g, int index, int y, int w) 
    {
        ModItem item = (ModItem)tablist[curTabIndex].get(index);         
        g.drawText(item.aliasName, 5, y, 0, w);        
    }
    public int getPreferredWidth(ListField list)
    {
        return Display.getWidth();
    }
    public Object get(ListField list, int index) 
    {
        return tablist[curTabIndex].get(index);
       // return listMod.elementAt(index);
    }
    public int indexOfList(ListField list, String p, int s) 
    {
        return 0;
        //return listMod.indexOf(p, s);
    }
    
    
} 

