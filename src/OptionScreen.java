/*
 * OptionScreen.java
 * bbdev, 2008
 */

package bbdev.keymaster;

import java.util.*;
import javax.wireless.messaging.*;
import javax.microedition.io.Connector;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.util.*;
import net.rim.blackberry.api.invoke.*;
import bbdev.resource.*;

class OptionScreen extends MainScreen {
    
    ObjectChoiceField ocfShift;
    ObjectChoiceField ocfFontsize;
    ObjectChoiceField ocfSort;
    ObjectChoiceField ocfHide;
    /*ObjectChoiceField ocfShutdown;
    ObjectChoiceField ocfSoftReset;
    ObjectChoiceField ocfCloseSig;*/
    EditField _efactive;
    LabelField _lfact;
    KeyChoiceField _cfhotkey;
    static Font tipfont;
    CheckboxField _ckCapture;
    CheckboxField _ckSysmenu;

    public final static String[] KEY_NAMES_HOME_HOTKEY = new String[] { 
            "NONE","SPACE",
            "A","B","C","D","E","F","G","I","J","L",
            "M","N","P","Q","R","S","U","V","W","X","Y",
    };
    
    public final static char[] KEY_VALUES_HOME_HOTKEY = new char[] { 
            '0',' ',
            'A','B','C','D','E','F','G','I','J','L',
            'M','N','P','Q','R','S','U','V','W','X','Y',
    };
    
    OptionScreen() 
    {   
        super.setTitle(KeyMaster.getString(KeyMasterResource.KM_MENU_OPTION));
         tipfont = Font.getDefault().derive(Font.ITALIC, Const.FONT_SIZE[Const.FONT_SIZE_SMALL], Ui.UNITS_pt); 
            if(!Const.OEM){
                String actstr = KeyMaster.getString(KeyMasterResource.KM_ACTIVE_OFF);
                if( Utils.isActive( Global.data.activeCode ) )
                    actstr = KeyMaster.getString(KeyMasterResource.KM_ACTIVE_ON);
                        
                _lfact = new LabelField(actstr);
                this.add(_lfact);       
                LabelField lfpin = new LabelField(KeyMaster.getString(KeyMasterResource.KM_PIN_CODE) +
                                                Integer.toHexString(DeviceInfo.getDeviceId()).toUpperCase());
                this.add(lfpin);
                
                _efactive = new EditField(KeyMaster.getString(KeyMasterResource.KM_ACTIVATION_CODE),Global.data.activeCode,16,EditField.EDITABLE);
                this.add(_efactive);
                //screen.add(new LabelField("    "));
                LabelField lblField= new LabelField(KeyMaster.getString(KeyMasterResource.KM_VISITWEB),LabelField.NON_FOCUSABLE);
                lblField.setFont(tipfont);
         
                this.add(lblField);
                
                this.add(new SeparatorField()); 
            }
                

            String open = KeyMaster.getString(KeyMasterResource.KM_OPEN);
            String close = KeyMaster.getString(KeyMasterResource.KM_CLOSE);
            
            String[] choices = {open,close};
            
            ocfShift = new ObjectChoiceField(KeyMaster.getString(KeyMasterResource.KM_OPT_SHIFT), choices, Global.data.bShiftMode?0:1);
            this.add(ocfShift);
            
            //String small = KeyMaster.getString(KeyMasterResource.KM_SMALL);
            //String medium = KeyMaster.getString(KeyMasterResource.KM_MEDIUM); 
            //String large = KeyMaster.getString(KeyMasterResource.KM_LARGE);   
            String[] fschoices = {KeyMaster.getString(KeyMasterResource.KM_SMALL),KeyMaster.getString(KeyMasterResource.KM_MEDIUM),
                                KeyMaster.getString(KeyMasterResource.KM_LARGE)};
               
            ocfFontsize = new ObjectChoiceField(KeyMaster.getString(KeyMasterResource.KM_FONTSIZE), fschoices, Global.data.iFontsize);
            this.add(ocfFontsize);
            
            String[] sortchoices = {KeyMaster.getString(KeyMasterResource.KM_SORT_NONE),KeyMaster.getString(KeyMasterResource.KM_SORT_BY_HOTKEY),
                                KeyMaster.getString(KeyMasterResource.KM_SORT_BY_APPNAME)};          
            ocfSort = new ObjectChoiceField(KeyMaster.getString(KeyMasterResource.KM_SORT), sortchoices, Global.data.bySort);
            this.add(ocfSort);         
            
            String[] hidechoices = {KeyMaster.getString(KeyMasterResource.KM_YES),KeyMaster.getString(KeyMasterResource.KM_NO)};
            ocfHide = new ObjectChoiceField(KeyMaster.getString(KeyMasterResource.KM_HIDE_APP), hidechoices, Global.data.bHideApp?0:1);
            this.add(ocfHide);     
            
            //add(new SeparatorField()); 
            _cfhotkey = new KeyChoiceField(KeyMaster.getString(KeyMasterResource.KM_HOME_HOTKEY),
                                    Global.data.homehotkey,KEY_NAMES_HOME_HOTKEY,KEY_VALUES_HOME_HOTKEY);
            this.add(_cfhotkey);

            _ckSysmenu = new CheckboxField(KeyMaster.getString(KeyMasterResource.KM_ADD_SYSTEMMENU),Global.data.bSysmenu);
            //this.add(_ckSysmenu);

            add(new SeparatorField());  
            _ckCapture = new CheckboxField(KeyMaster.getString(KeyMasterResource.KM_CAPTURE),Global.data.bCapture);
            this.add(_ckCapture);
            LabelField lblCaptureField= new LabelField(KeyMaster.getString(KeyMasterResource.KM_CAPTURE_TIPS),LabelField.NON_FOCUSABLE);
            lblCaptureField.setFont(tipfont);
            this.add(lblCaptureField); 

            //ocfCloseSig = new ObjectChoiceField(KeyMaster.getString(KeyMasterResource.KM_OPT_CLOSESIGNAL), choices, Global.data.bCloseSig?0:1);
            //this.add(ocfCloseSig);   
              
            //ocfSoftReset = new ObjectChoiceField(KeyMaster.getString(KeyMasterResource.KM_OPT_SOFTRESET), choices, Global.data.bSoftReset?0:1);
            //this.add(ocfSoftReset); 
                  
            //ocfShutdown = new ObjectChoiceField(KeyMaster.getString(KeyMasterResource.KM_OPT_SHUTDOWN), choices, Global.data.bShutdown?0:1);
            //this.add(ocfShutdown);
  
            //_ef = new EditField("Õð¶¯Ê±¼ä: ",Integer.toString(_data.getTime()),5, BasicEditField.FILTER_INTEGER);
            //screen.add(_ef);
            //screen.add(new LabelField("    "));
            
     /*FieldChangeListener listener = new FieldChangeListener() {
         public void fieldChanged(Field field, int context) {
             ButtonField buttonField = (ButtonField) field;
             //System.out.println("Button pressed: " + buttonField.getLabel());
         }
     };*/

    add(new SeparatorField());  
    ButtonField setPermField = new ButtonField(KeyMaster.getString(KeyMasterResource.KM_SETAPP_PERM),
                                                    Field.FIELD_HCENTER | Field.FOCUSABLE){
                        protected boolean trackwheelClick(int status, int time) {
                            Utils.setPermissions();
                            return true;
                        }
    };
    this.add(setPermField); 
    LabelField lblPermField= new LabelField(KeyMaster.getString(KeyMasterResource.KM_SETPERM),LabelField.NON_FOCUSABLE);
    //LabelField lblPermField= new LabelField("Please set listed application permissions to 'Allow' in order to ensure HotkeyManager work properly.",
                                //LabelField.NON_FOCUSABLE);
    lblPermField.setFont(tipfont);
    this.add(lblPermField); 
    
    //add(new SeparatorField());  
    
    
    add(new SeparatorField()); 
    ButtonField backupField = new ButtonField(KeyMaster.getString(KeyMasterResource.KM_BARE),
                                                    Field.FIELD_HCENTER | Field.FOCUSABLE){
                        protected boolean trackwheelClick(int status, int time) {
                            UiApplication.getUiApplication().popScreen();
                            UiApplication.getUiApplication().pushScreen(new BackupScreen());
                            return true;
                        }
    };
    this.add(backupField); 
    
    this.add(new SeparatorField()); 
    HorizontalFieldManager hm = new HorizontalFieldManager(Field.FIELD_HCENTER);
    /*if(!Const.OEM){
        
        if(!Utils.isActive(Global.data.activeCode)){
            ButtonField buttonField = new ButtonField(KeyMaster.getString(KeyMasterResource.KM_ORDER),
                                                    Field.FIELD_LEFT | Field.FOCUSABLE){
                        protected boolean trackwheelClick(int status, int time) {
                            Utils.launchBrowser("http://wap.emobistudio.com/hotkeymanager.php");
                            return true;
                        }
            };
            hm.add(buttonField); 
        }else{
            ButtonField buttonField = new ButtonField(KeyMaster.getString(KeyMasterResource.KM_CHECK_UPDATE),
                                                    Field.FIELD_LEFT | Field.FOCUSABLE){
                        protected boolean trackwheelClick(int status, int time) {
                            Utils.launchBrowser("http://wap.emobistudio.com/update_hotkeymanager.php");
                            return true;
                        }
            };
            hm.add(buttonField); 
        }
    }else{
            ButtonField buttonField = new ButtonField(KeyMaster.getString(KeyMasterResource.KM_CHECK_UPDATE),
                                                    Field.FIELD_LEFT | Field.FOCUSABLE){
                        protected boolean trackwheelClick(int status, int time) {
                            Utils.launchBrowser("http://wap.emobistudio.com/update_hotkeymanager.php");
                            return true;
                        }
            };
            hm.add(buttonField); 
    }*/
    
    /*ButtonField firendField = new ButtonField(KeyMaster.getString(KeyMasterResource.KM_TELL_FRIEND),
                                                    Field.FIELD_RIGHT | Field.FOCUSABLE){
                        protected boolean trackwheelClick(int status, int time) {
                            String subject = KeyMaster.getString(KeyMasterResource.KM_TELL_FRIEND_SUBJECT);
                            String body = KeyMaster.getString(KeyMasterResource.KM_TELL_FRIEND_BODY);
                            String strs[] = {"SMS","EMAIL"};
                            int index = Dialog.ask(KeyMaster.getString(KeyMasterResource.KM_PLSEL) + "?",strs, 0);
                            if(index == 0){
                                try{
                                    MessageConnection mc = (MessageConnection)Connector.open( "sms://" );
                                    TextMessage m = (TextMessage)mc.newMessage( MessageConnection.TEXT_MESSAGE );
                                    //m.setAddress( "sms://5558888" );
                                    m.setPayloadText(body);
                                    Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES,
                                        new MessageArguments(m));
                                }catch(Exception ex){
                                    ;
                                }
                            }else if(index == 1){
                                Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES,
                                    new MessageArguments(MessageArguments.ARG_NEW,null,subject,body));
                            }
                            return true;
                        }
    };
    hm.add(firendField); 
    LabelField lblField= new LabelField(KeyMaster.getString(KeyMasterResource.KM_VISITWEB),LabelField.NON_FOCUSABLE);
    //LabelField lblField= new LabelField("For more detailed information,please visit eMobiStudio's website at http://www.emobistudio.com.",
                                //LabelField.NON_FOCUSABLE);
    lblField.setFont(tipfont);
    this.add(hm);
    this.add(lblField); */
    
     //buttonField.setChangeListener(listener);
            
            
            /*this.add(new SeparatorField()); 
            LabelField orderField = new LabelField(KeyMaster.getString(KeyMasterResource.KM_ORDER),
                                            Field.FIELD_HCENTER | Field.FOCUSABLE) {
                protected boolean trackwheelClick(int status, int time) {
                    Utils.launchBrowser("http://wap.emobistudio.com/hotkeymanager.php");
                    return true;
                }
            };

            this.add(orderField);   */       
            /*String poweredstr = "Powered By BBDev.org";
            LabelField lf = new LabelField(poweredstr);
            try{
                int s = Const.SCREEN_WIDTH - _font.getAdvance(poweredstr)- 5;
                if( s >= 0 && s < Const.SCREEN_WIDTH )
                    lf.setPosition(s);
            }catch(Exception ex){
                   Utils.OutputDebugString(ex.toString());
            }
            screen.add(lf);*/
    }
    
    protected void makeMenu(Menu menu, int instance) 
    {
        if(!Const.OEM && !Utils.isActive(Global.data.activeCode)){
            MenuItem regItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_REG), 99, 9) {
            public void run() {
                    String actCode = _efactive.getText();
                    if(Utils.isActive(actCode)) {
                        Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_REG_VALID));
                        Global.data.bActiveCode = true;  
                        Global.data.activeCode = actCode;
                        _lfact.setText(KeyMaster.getString(KeyMasterResource.KM_ACTIVE_ON));
                        _efactive.setText(Global.data.activeCode);
                    }else{
                        Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_REG_INVALID));
                    }
                } 
            }; 
            menu.add(regItem);
        }else{
            /*MenuItem exportItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_EXPORT), 98, 8) {
                public void run() {
                    Utils.exportData("file:///SDCard/hotkeymanager.dat");
                } 
            };  
            MenuItem importItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_IMPORT), 99, 8) {
                public void run() {
                    Utils.importData("file:///SDCard/hotkeymanager.dat");
                } 
            };  
            
            menu.add(exportItem);
            menu.add(importItem);*/
            super.makeMenu(menu,instance);
        }
    }
    
    private void saveChange()
    {
                if(ocfShift.getSelectedIndex() == 1)
                    Global.data.bShiftMode  = false;
                else
                    Global.data.bShiftMode  = true;
                
                Global.data.iFontsize = ocfFontsize.getSelectedIndex();
                Global.data.bySort = (byte)ocfSort.getSelectedIndex();
                
                Global.data.bHideApp = ocfHide.getSelectedIndex()==0;
                
                char hotkey = _cfhotkey.getSelectedKey();
                //if(hotkey>'a' && hotkey<'z')
                   // hotkey -=32;
                if(hotkey != Global.data.homehotkey){
                    Utils.setHotkey(hotkey);
                }
                
                if(_ckCapture.getChecked() != Global.data.bCapture)
                {
                    Global.data.bCapture = _ckCapture.getChecked();
                    if(Global.data.bCapture){
                        AddCaptureMenu.AddMenu();
                    }else{
                        AddCaptureMenu.RemoveMenu();
                    }
                }
     
                if(_ckSysmenu.getChecked() != Global.data.bSysmenu)
                {
                    Global.data.bSysmenu = _ckSysmenu.getChecked();
                    if(Global.data.bSysmenu){
                        AddSystemMenu.AddMenu();
                    }else{
                        AddSystemMenu.RemoveMenu();
                    }
                }                
                /*if(ocfSoftReset.getSelectedIndex() == 1)
                    Global.data.bSoftReset  = false;
                else
                    Global.data.bSoftReset  = true;   
                     
                if(ocfShutdown.getSelectedIndex() == 1)
                    Global.data.bShutdown  = false;
                else
                    Global.data.bShutdown  = true;  
                    
                if(ocfCloseSig.getSelectedIndex() == 1)
                    Global.data.bCloseSig  = false;
                else
                    Global.data.bCloseSig  = true; */                   
            if(!Const.OEM){
                    String actCode = _efactive.getText();
                    if(Utils.isActive(actCode))
                    {
                        Global.data.bActiveCode = true;  
                        Global.data.activeCode = actCode;
                    }
            }
                    
            Global.data.commit();       
    }
    
    public boolean onClose()
    {
        saveChange();
        this.close();
        return true;
    }
   
    /*protected boolean onSavePrompt()
    {
        if(Dialog.ask(Dialog.D_SAVE,KeyMaster.getString(KeyMasterResource.KM_OPT_SAVE)) == Dialog.SAVE)
        {
            saveChange();
        }
            
        return true;
    }*/

} 
