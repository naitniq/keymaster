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

/*final class AddModScreen extends PopupScreen{
    
    private final static int CUSTOM_SIZE_W   = ( Const.SCREEN_WIDTH >> 2 ) * 3;
    private final static int CUSTOM_SIZE_H   = 70;
    
    private EditField modedit;
    private SysmodScreen parentScreen;
        
    AddModScreen(SysmodScreen parent)
    {
        //super( new VerticalFieldManager( Manager.NO_VERTICAL_SCROLL | Manager.NO_VERTICAL_SCROLLBAR ) );
        super(new FlowFieldManager());
        
        parentScreen = parent;
        //super(new HorizontalFieldManager());
        
        modedit = new EditField(KeyMaster.getString(KeyMasterResource.KM_MOD_NAME),"",50,EditField.EDITABLE);
            
        ButtonField btnOk = new ButtonField(KeyMaster.getString(KeyMasterResource.KM_OK));
        btnOk.setChangeListener(btnlistener);
        ButtonField btnCancel = new ButtonField(KeyMaster.getString(KeyMasterResource.KM_CANCEL));
        btnCancel.setChangeListener(btnlistener); 
        ButtonField btnPaste = new ButtonField(KeyMaster.getString(KeyMasterResource.KM_PASTE));
        btnPaste.setChangeListener(btnlistener);     
        this.add(modedit);
        this.add(btnPaste);
        this.add(btnOk);
        this.add(btnCancel);
        
    }
    
    private void addModule(String text)
    {
        if(text.length() > 0)
        {
                    int modhandle = CodeModuleManager.getModuleHandle(text);
                    String moudlename;
                    if(modhandle > 0) 
                    {
                        //moudlename = CodeModuleManager.getApplicationDescriptors(modhandle)[0].getName();
                        //os 4.2.1
                        moudlename = CodeModuleManager.getApplicationDescriptors(modhandle)[0].getLocalizedName();
                        if(moudlename == null)
                            moudlename = CodeModuleManager.getModuleName(modhandle);
                            
                         Utils.OutputDebugString("Add Custom App:" + moudlename);  
                        
                        if( !Global.data.listCustomMod.contains(moudlename) )
                        {    
                            Global.data.listCustomMod.addElement(text);
                            Global.data.commit(); 
                            parentScreen.insertItem(text,0);
                        }

                        close();
                    }
                    else
                    {   
                        String info = Utils.StringReplace(KeyMaster.getString(KeyMasterResource.KM_MOD_ERROR),"%",text);
                        Dialog.alert(info);
                    }
        }
    }
    
    
    FieldChangeListener btnlistener = new FieldChangeListener() {
         public void fieldChanged(Field field, int context) {
            ButtonField buttonField = (ButtonField) field;
            if( buttonField.getLabel() == KeyMaster.getString(KeyMasterResource.KM_OK) )
            {
                String text = modedit.getText().trim();
                addModule(text);
            }
            else if(buttonField.getLabel() == KeyMaster.getString(KeyMasterResource.KM_PASTE))
            {
                String modname = (String)Clipboard.getClipboard().get();
                if(modname != null)
                {
                   modedit.setText(modname);
                }
            }
            else
            {
                close();
            }
         }
     };
    
    protected void sublayout( int width, int height ) 
    {
        int x = ( Const.SCREEN_WIDTH - CUSTOM_SIZE_W ) >> 1;
        int y = ( Const.SCREEN_HEIGHT - CUSTOM_SIZE_H ) >> 1;
        
        setExtent( CUSTOM_SIZE_W, CUSTOM_SIZE_H );
        setPosition( x, y );
        layoutDelegate( CUSTOM_SIZE_W, CUSTOM_SIZE_H );
    }
    
    protected boolean keyDown(int keycode, int time) 
    {
        int key = Keypad.key(keycode);
          
        if(key == Keypad.KEY_ESCAPE)
        { 
            close();
            return true;
        }
       
        
        return super.keyDown( keycode, time );
    }
} */

