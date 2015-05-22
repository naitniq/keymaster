package bbdev.keymaster;

import net.rim.device.api.ui.component.Dialog.*;
import net.rim.blackberry.api.menuitem.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;
import net.rim.device.api.notification.*;
import net.rim.device.api.ui.component.Dialog;
import bbdev.resource.*;

public final class EventMenu extends ApplicationMenuItem{

    public static final long KM_EM_ID = 0x806b74bfa5a5c5d9L;
    public static EventMenu _em; 
    
    public static void AddMenu()
    {
        RuntimeStore store = RuntimeStore.getRuntimeStore();
        _em = (EventMenu)store.get(KM_EM_ID);
        if(_em!=null)
            return;
        _em = new EventMenu();
        ApplicationMenuItemRepository amir = ApplicationMenuItemRepository.getInstance();
        try{
            amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_SYSTEM, _em);
            /*amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_SEARCH,_asm); 
            amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_BROWSER,_asm); 
            amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_MMS_EDIT, _asm); //4.3.0
            amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_SMS_EDIT, _asm);   //4.2.1
            amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_EMAIL_EDIT, _asm);          
            amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_ADDRESSCARD_EDIT, _asm);      
            amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_TASK_EDIT, _asm);     
            amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_MEMO_EDIT, _asm);      
            amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_GROUPADDRESS_EDIT, _asm); //4.2.1
            amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_CALENDAR_EVENT, _asm);*/

        }catch(Exception ex){
            ;
        }
        
        //RuntimeStore store = RuntimeStore.getRuntimeStore();
        try {
            store.put( KM_EM_ID, _em);
        }catch(IllegalArgumentException e) {
                   //DxTools.OutputDebugString(e.toString());
        }
                
        //amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_SYSTEM, asm); 

    }
    
    public static void RemoveMenu()
    { 
        RuntimeStore store = RuntimeStore.getRuntimeStore();
        _em = (EventMenu)store.get(KM_EM_ID);
        if(_em!=null){
            try{
                ApplicationMenuItemRepository amir = ApplicationMenuItemRepository.getInstance();
                if(amir.removeMenuItem(ApplicationMenuItemRepository.MENUITEM_SYSTEM, _em)){
                    _em = null;
                    store.remove(KM_EM_ID);
                }
            }catch(Exception ex){
            }
        }
    } 
    
    public EventMenu() 
    {
        super(5);
    }

    public String toString()
    {
        return "Start Event";
    }
    //static String dlgstr;
    public Object run(Object context)
    {
        Utils.statrEvent(Characters.TAB,KeypadListener.STATUS_NOT_FROM_KEYPAD,200);
        
                    /*Global.data = StoreData.load(); 
                    String dlgstr = ;
             
                    synchronized(Application.getEventLock()){    
                        UiEngine ui = Ui.getUiEngine();
                        Screen screen = new Dialog(Dialog.D_OK, dlgstr, 
                            Dialog.OK, Bitmap.getPredefinedBitmap(Bitmap.EXCLAMATION),Manager.VERTICAL_SCROLL);
                        ui.pushGlobalScreen(screen, 1, UiEngine.GLOBAL_QUEUE);
                    }*/
        return null;      
    } 
} 


