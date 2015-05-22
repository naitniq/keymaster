/*
 * AllModScreen.java
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

final class AllModScreen extends MainScreen implements ListFieldCallback {
    
    private static ListField _list;
    
    private  Vector listMod  = new Vector(100); 
    private  IntVector listHandle = new IntVector(100); 

    AllModScreen()
    {
        _list = new ListField();     
        _list.setCallback((ListFieldCallback)this);   
        
        int handles[] = CodeModuleManager.getModuleHandles();
        ApplicationDescriptor appdesc;
        ApplicationDescriptor[] appdescs;
        ApplicationManager appman;
        String modname,appname;
        int index = 0;
        for(int i=handles.length-1;i>=0;i--)
        {
            //if(CodeModuleManager.getModuleFlags(handles[i]) == CodeModuleManager.MODULE_FLAG_DELETE)
                //continue;
            //if(CodeModuleManager.isLibrary(handles[i]))
               // continue;
            appdescs = CodeModuleManager.getApplicationDescriptors(handles[i]);    
            if(appdescs!=null)
                appdesc = CodeModuleManager.getApplicationDescriptors(handles[i])[0];
            else
                appdesc = null;
                
            modname = CodeModuleManager.getModuleName(handles[i]); 
            
            if(appdesc!=null){
                //appname = appdesc.getName();
                appname = appdesc.getLocalizedName();
            }else{
                appname = modname;
            }

            if(appname == null)
                continue;
            if(appname.equalsIgnoreCase("keymaster"))
                continue;
                
            appman = ApplicationManager.getApplicationManager();  
            if(appname.indexOf("Playing") >0 || appname.indexOf("playing") > 0 ){
                Utils.OutputDebugString("modname:" + modname);
            }
            listHandle.addElement(handles[i]);
            listMod.addElement(appname);
            _list.insert(index++);
        }
        
        this.add(_list);
    }
    
    /*public static void viewDetailInfo(int handle)
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
    }*/
    
    protected boolean keyChar(char c, int status, int time)
    {
        if(c == 'd' || c == 'D')
        {   
            Utils.viewDetailInfo(listHandle.elementAt(_list.getSelectedIndex()));
            return true;
        }
        else
            return super.keyChar( c, status,time );
    }
    
    
    protected void makeMenu(Menu menu, int instance) 
    {
        MenuItem viewItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_VIEW_DETAIL), 99, 9) {
        public void run() {
               Utils.viewDetailInfo(listHandle.elementAt(_list.getSelectedIndex()));
            } 
        }; 
        
        MenuItem copyItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_MENU_COPYMOD), 100, 10) {
        public void run() {
                int handle = listHandle.elementAt(_list.getSelectedIndex());
                String ModuleName = CodeModuleManager.getModuleName(handle);
                Clipboard.getClipboard().put(ModuleName);
            } 
        }; 
      
        menu.add( viewItem );
        menu.add( copyItem );
        //menu.add(closeItem);
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

