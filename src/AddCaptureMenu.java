package bbdev.keymaster;

import net.rim.device.api.ui.component.Dialog.*;
import net.rim.blackberry.api.menuitem.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;
import net.rim.device.api.notification.*;
import net.rim.device.api.ui.component.Dialog;
import bbdev.resource.*;

public final class AddCaptureMenu extends ApplicationMenuItem{

    public static final long KM_RUN_ID = 0x3bf620d1886dd809L;
    public static AddCaptureMenu _asm; 
    
    public static void AddMenu()
    {
        RuntimeStore store = RuntimeStore.getRuntimeStore();
        _asm = (AddCaptureMenu)store.get(KM_RUN_ID);
        if(_asm!=null)
            return;
        _asm = new AddCaptureMenu();
        ApplicationMenuItemRepository amir = ApplicationMenuItemRepository.getInstance();
          //amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_SYSTEM, asm);   
        try{
            amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_SYSTEM, _asm);
       
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
            store.put( KM_RUN_ID, _asm);
        }catch(IllegalArgumentException e) {
                   //DxTools.OutputDebugString(e.toString());
        }
                
        //amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_SYSTEM, asm); 

    }
    
    public static void RemoveMenu()
    { 
        RuntimeStore store = RuntimeStore.getRuntimeStore();
        _asm = (AddCaptureMenu)store.get(KM_RUN_ID);
        if(_asm!=null){
            try{
                ApplicationMenuItemRepository amir = ApplicationMenuItemRepository.getInstance();
                if(amir.removeMenuItem(ApplicationMenuItemRepository.MENUITEM_SYSTEM, _asm)){
                    _asm = null;
                    store.remove(KM_RUN_ID);
                }
            }catch(Exception ex){
            }
        }
    } 
    
    public AddCaptureMenu() 
    {
        super(5);
    }

    public String toString()
    {
        return "Capture It";
    }
    //static String dlgstr;
    public Object run(Object context)
    {
        /*new Thread(new Runnable() {

        public void run() {
             
                   
               }
        }).start();*/
     
                    Global.data = StoreData.load(); 
                    Bitmap bitmap = new Bitmap(Display.getWidth(),Display.getHeight());
                    Display.screenshot(bitmap);
                    JPEGEncodedImage jpegImg = JPEGEncodedImage.encode(bitmap,80);
                    byte[] data = jpegImg.getData();
                    String dlgstr;
                    if(Utils.haveSDCard()){
                        String path = "file:///SDCard/BlackBerry/Pictures/";
                    //String path = "file:///SDCard/";
                        String name = "bbdev_" + Global.data.captureRandom + "_"  + Global.data.captureIdx + ".jpg";
                        Utils.saveData2File(path+name,data);
                        Global.data.captureIdx++;
                        dlgstr= KeyMaster.getString(KeyMasterResource.KM_SAVE_CAPTURE) + path +".";
                    }else{
                        dlgstr = KeyMaster.getString(KeyMasterResource.KM_PLS_INSERTSD);
                    }   
                    
                    synchronized(Application.getEventLock()){    
                        UiEngine ui = Ui.getUiEngine();
                        Screen screen = new Dialog(Dialog.D_OK, dlgstr, 
                            Dialog.OK, Bitmap.getPredefinedBitmap(Bitmap.EXCLAMATION),Manager.VERTICAL_SCROLL);
                        ui.pushGlobalScreen(screen, 1, UiEngine.GLOBAL_QUEUE);
                    }
                    /*UiApplication.getUiApplication().invokeLater(new Runnable() {
                                    public void run() {
                                        UiApplication.getUiApplication().pushGlobalScreen(new KMDialog(dlgstr),99,UiEngine.GLOBAL_MODAL);
                                    }
                    });*/
   

        //net.rim.device.api.ui.component.Dialog.alert(dlgstr);
        return null;      
    } 
} 


