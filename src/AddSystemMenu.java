package bbdev.keymaster;

import net.rim.device.api.ui.component.Dialog.*;
import net.rim.blackberry.api.menuitem.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;
import net.rim.device.api.notification.*;
import net.rim.device.api.ui.component.Dialog;
import bbdev.resource.*;

public final class AddSystemMenu extends ApplicationMenuItem{

    public static final long KM_SYSMENU_ID = 0x6ff7577edda89580L;
    public static AddSystemMenu _asm; 
    
    public static void AddMenu()
    {
        RuntimeStore store = RuntimeStore.getRuntimeStore();
        _asm = (AddSystemMenu)store.get(KM_SYSMENU_ID);
        if(_asm!=null)
            return;
        _asm = new AddSystemMenu();
        ApplicationMenuItemRepository amir = ApplicationMenuItemRepository.getInstance();

        try{
            amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_SYSTEM, _asm);
        }catch(Exception ex){
            ;
        }
        
        //RuntimeStore store = RuntimeStore.getRuntimeStore();
        try {
            store.put( KM_SYSMENU_ID, _asm);
        }catch(IllegalArgumentException e) {
                   //DxTools.OutputDebugString(e.toString());
        }
                
        //amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_SYSTEM, asm); 

    }
    
    public static void RemoveMenu()
    { 
        RuntimeStore store = RuntimeStore.getRuntimeStore();
        _asm = (AddSystemMenu)store.get(KM_SYSMENU_ID);
        if(_asm!=null){
            try{
                ApplicationMenuItemRepository amir = ApplicationMenuItemRepository.getInstance();
                if(amir.removeMenuItem(ApplicationMenuItemRepository.MENUITEM_SYSTEM, _asm)){
                    _asm = null;
                    store.remove(KM_SYSMENU_ID);
                }
            }catch(Exception ex){
            }
        }
    } 
    
    public AddSystemMenu() 
    {
        super(5);
    }

    public String toString()
    {
        return KeyMaster.getString(KeyMasterResource.KM_APPNAME);
    }

    public Object run(Object context)
    {
        synchronized(Application.getEventLock()){
            //new KeyMaster().enterEventDispatcher();   
            KeyMaster.startKeyMaster(UiApplication.getUiApplication());
            //UiApplication.getUiApplication().pushScreen(new );
        
        }
        return null;      
    } 
} 


