package bbdev.keymaster;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;
import bbdev.resource.*;

class BackupScreen extends MainScreen {
    BackupScreen() { 
        super.setTitle(KeyMaster.getString(KeyMasterResource.KM_BARE));
        
        LabelField label = new LabelField(KeyMaster.getString(KeyMasterResource.KM_BACKTIP), LabelField.NON_FOCUSABLE);
        //LabelField label = new LabelField("HotkeyManager will backup your Activation Code, Hotkey Configuration and Options to hotkeymanager.dat on your device which can be restored anytime.\n\nBe sure to use this feature before upgrading your OS or uninstalling HotkeyManger in order to retrieve your settings later. ", LabelField.NON_FOCUSABLE);
        this.add(label); 
        
        ButtonField importField = new ButtonField(KeyMaster.getString(KeyMasterResource.KM_IMPORT),
                                                    Field.FIELD_HCENTER | Field.FOCUSABLE){
                        protected boolean trackwheelClick(int status, int time) {
                            if(Utils.haveSDCard())
                                Utils.importData("file:///SDCard/keymaster.dat");
                            else
                                Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_PLS_INSERTSD));
                            return true;
                        }
        };
        this.add(importField); 
        ButtonField exportField = new ButtonField(KeyMaster.getString(KeyMasterResource.KM_EXPORT),
                                                        Field.FIELD_HCENTER | Field.FOCUSABLE){
                            protected boolean trackwheelClick(int status, int time) {
                                if(Utils.haveSDCard())
                                    Utils.exportData("file:///SDCard/keymaster.dat");
                                else
                                    Dialog.alert(KeyMaster.getString(KeyMasterResource.KM_PLS_INSERTSD));
                                return true;
                            }
        };
        this.add(exportField); 
    }
    
    
} 
