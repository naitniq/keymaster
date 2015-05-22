
package bbdev.keymaster;

import java.util.Vector;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.util.*;
import bbdev.resource.*;

final class SystemScreen extends MainScreen implements ListFieldCallback {
    
    private static ListField _list;
    private static BBListField _customlist;
    ///private static IntVector listTable;    //ProcessId
    //private static IntVector listMap;    //index -> key
    
    //private  Vector listMod  = new Vector(100); 
    //private  IntVector listHandle = new IntVector(100); 
    private static SystemScreen _this;
    
    SystemScreen(BBListField list)
    {
        //mainlist = lf;
        _this = this;
        _customlist = list;
        
        _list = new ListField();     
        _list.setCallback((ListFieldCallback)this);   
        
        /*int handles[] = CodeModuleManager.getModuleHandles();
        ApplicationDescriptor appdesc;
        ApplicationManager appman;
        String modname,appname;
        int index = 0;
        for(int i=handles.length-1;i>=0;i--)
        {
            if(CodeModuleManager.getModuleFlags(handles[i]) == CodeModuleManager.MODULE_FLAG_DELETE)
                continue;
            if(CodeModuleManager.isLibrary(handles[i]))// || !CodeModuleManager.isMidlet(handles[i]))
                continue;
                
            appdesc = CodeModuleManager.getApplicationDescriptors(handles[i])[0];
            //if(appdesc.getFlags() > 0)
            if( appdesc.getFlags() >0 && appdesc.getFlags() != ApplicationDescriptor.FLAG_SYSTEM)
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
            if(appname.equalsIgnoreCase("keymaster"))
                continue;
                
            appman = ApplicationManager.getApplicationManager();  
            //os 4.2.1  
            //if(appman.isConsoleDescriptor(appdesc))  
                //continue;
            if(findInBlockList(modname))
                continue;
            //if( appdesc.getFlags() == ApplicationDescriptor.FLAG_SYSTEM)    
                //modname = "Sys_"+modname;
            
            //os 4.2.1   
            appname =  appdesc.getLocalizedName();   
            listHandle.addElement(handles[i]);
            listMod.addElement(appname);
            _list.insert(index++);
        }*/
        refreshList();
        
        this.add(_list);
    }
    
    private boolean findInBlockList(String mod)
    {
        for(int i=0;i<Const.BLOCK_LIST.length;i++)
        {
            if(mod.equalsIgnoreCase(Const.BLOCK_LIST[i]))
                return true;
        }
        return false;
    }

    private void refreshList()
    {
        int modhandle;
        ApplicationDescriptor appdesc;
        ApplicationManager appman;
        String modname,appname;
        int index = 0;
        
        if(!listMod.isEmpty())
            listMod.removeAllElements();
        if(!listHandle.isEmpty())
            listHandle.removeAllElements();
        
        //add sp list
        /*int len  = Const.SP_HANDLE.length;
        for(int i=0;i<len;i++)
        {
                //modname = Const.SP_MODNAME[i];
                //modhandle = Const.SP_HANDLE[i];
                //appdesc = CodeModuleManager.getApplicationDescriptors(modhandle)[0];
                //appname = Const.SP_APPNAME[i];
                    
                //if(appname != null)
                //{
                    listHandle.addElement(Const.SP_HANDLE[i]);
                    listMod.addElement(Const.SP_APPNAME[i]);
                    _list.insert(index++);
                //}
        }*/
      
        //add custom module to list
        int len  = Global.listCustomMod.size();
        for(int i=0;i<len;i++)
        {
                modname = (String)Global.listCustomMod.elementAt(i);
                modhandle = CodeModuleManager.getModuleHandle(modname);
                if(modhandle == 0)
                {
                    Global.listCustomMod.removeElementAt(i);
                    i--;
                    len--;
                    continue;
                }
                appdesc = CodeModuleManager.getApplicationDescriptors(modhandle)[0];
                //appname = appdesc.getName();
                //os 4.2.1
                appname = appdesc.getLocalizedName();

                if(appname == null)
                    appname = CodeModuleManager.getModuleName(modhandle);
                    
                if(appname != null)
                {
                    listHandle.addElement(modhandle);
                    listMod.addElement(appname);
                    _list.insert(index++);
                }
                else
                {
                    Global.listCustomMod.removeElementAt(i);
                    i--;
                    len--;
                }
        }
        
        //add auto-detected module to list
        int handles[] = CodeModuleManager.getModuleHandles();
        for(int i=handles.length-1;i>=0;i--)
        {
            if(CodeModuleManager.getModuleFlags(handles[i]) == CodeModuleManager.MODULE_FLAG_DELETE)
                continue;
            if(CodeModuleManager.isLibrary(handles[i]))// || !CodeModuleManager.isMidlet(handles[i]))
                continue;
            if(listHandle.contains(handles[i]))  
                continue;
                
            appdesc = CodeModuleManager.getApplicationDescriptors(handles[i])[0];
            //if(appdesc.getFlags() > 0)
            if( appdesc.getFlags() >0 && appdesc.getFlags() != ApplicationDescriptor.FLAG_SYSTEM)
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
            if(appname.equalsIgnoreCase("keymaster"))
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
            listHandle.addElement(handles[i]);
            listMod.addElement(appname);
            _list.insert(index++);
        }
    }
    
    public void insertItem(String modname,int index)
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
    }
    
    protected void makeMenu(Menu menu, int instance) 
    {
        MenuItem addexItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_EXADD), 99, 9) {
        public void run() {
                UiApplication.getUiApplication().pushScreen(new AddModScreen(_this));
            } 
        }; 
        /*MenuItem closeItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_CLOSE), 100, 10) {
        public void run() {
               this.close();
            } 
        };*/
        
        menu.add(addexItem);
        //menu.add(closeItem);
    }
    
    protected boolean keyChar(char c, int status, int time)
    {
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
        else
        {
          return super.keyChar( c, status,time );
        }
        
        if(key > 0)
        {
            if(!Global.htModItem.containsKey(new Integer(key)))
            //if(!Global.listMap.contains(key))
            {
                int handle = listHandle.elementAt(_list.getSelectedIndex());
                //if((handle > 0 || Utils.isSPHandle(handle)) && !Global.listTable.contains(handle))
                if(handle > 0)
                {
                    String modname = CodeModuleManager.getModuleName(handle);
                    ModItem item = new ModItem(modname,key);
                    /*Global.listTable.addElement(handle);
                    if(Utils.isSPHandle(handle))
                        Global.listName.addElement(Utils.getSpModName(handle));
                    else
                        Global.listName.addElement(CodeModuleManager.getModuleName(handle));*/
                        
                    //Global.listMap.addElement(key);
                    //Global.listString.addElement((String)listMod.elementAt(_list.getSelectedIndex())+" ["+ (char)key + "]");
                    int index = _customlist.getSize()>0?_customlist.getSize()-1:0;
                    _customlist.insert(index,item);
                    Global.htModItem.put(new Integer(key),item);
                }
                
                UiApplication.getUiApplication().popScreen(this);
            }
            else
            {
                String tips = KeyMaster.getString(KeyMasterResource.KM_ALREADYEXIST);
                tips = tips.replace('%',(char)key);
                Dialog.alert(tips);
            }
            return true;
        }
        else
            return super.keyChar( c, status,time );
    }

    public void drawListRow(ListField list, Graphics g, int index, int y, int w) 
    {
        String text = (String)listMod.elementAt(index);         
        g.drawText(text, 5, y, 0, w);
    }
    public int getPreferredWidth(ListField list)
    {
        return Display.getWidth();
    }
    public Object get(ListField list, int index) 
    {
        return listMod.elementAt(index);
    }
    public int indexOfList(ListField list, String p, int s) 
    {
        return listMod.indexOf(p, s);
    }
    
    
} 
