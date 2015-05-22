
package bbdev.keymaster;

import java.util.Vector;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.util.*;
import bbdev.resource.*;

final class EditModScreen extends MainScreen{
    
    private static BBListField _customlist;
    private ModItem _item;
    
    private EditField _efname;
    private EditField _efmodname;
    //private EditField _efhotkey;
    private KeyChoiceField _cfhotkey;
    
    EditModScreen(BBListField list,ModItem item)
    {
        _customlist = list;
        String name = "";
        String modname = "";
        String hotkey = "0";
        String tipstr = "Edit Hotkey";
       
        
       VerticalFieldManager tipman =  new TipMananger(tipstr,Color.WHITE,Color.GRAY);
       this.add(tipman);
        
       this._item = item;
       if(_item != null){
            if( (_item.modAction&Const.MOD_ACTION_MASK) == Const.MOD_ACTION_RUN 
            || (_item.modAction&Const.MOD_ACTION_MASK) == Const.MOD_ACTION_SYSTEM ){
                name = _item.aliasName;
                modname = _item.modName;
                if(_item.hotKey > 0 )
                    hotkey = ((char)_item.hotKey) + "";
            }
        }
        if( (_item.modAction&Const.MOD_ACTION_MASK) == Const.MOD_ACTION_RUN ){
            _efname = new EditField(KeyMaster.getString(KeyMasterResource.KM_NAME),name,32,EditField.EDITABLE|EditField.NO_NEWLINE);
            this.add(_efname);
            _efmodname = new EditField(KeyMaster.getString(KeyMasterResource.KM_MODNAME),modname,32,EditField.READONLY);
            this.add(_efmodname);
        }
            //_efhotkey = new EditField(KeyMaster.getString(KeyMasterResource.KM_HOTKEY),hotkey,1,EditField.EDITABLE);
          _cfhotkey = new KeyChoiceField(KeyMaster.getString(KeyMasterResource.KM_HOTKEY),hotkey.charAt(0));
         this.add(_cfhotkey);
         
         /*_efhotkey = new EditField(KeyMaster.getString(KeyMasterResource.KM_HOTKEY),hotkey,2,EditField.EDITABLE){
                protected boolean keyChar(char c, int status, int time)
                {
                    if(getTextLength()>0 && c != Characters.BACKSPACE && c != Characters.ESCAPE){
                        String tips = "Please input a single character for the hotkey.";
                        Dialog.alert(tips);
                        return true;
                    }
                    return super.keyChar(c,status,time);
                }
        };   
        this.add(_efhotkey);*/
              
    }
    
    public void save()
    {
                if(_item == null){
                    return;
                }
                String name = _item.aliasName;
                String modname = _item.modName;
                if( (_item.modAction&Const.MOD_ACTION_MASK) == Const.MOD_ACTION_RUN ){
                     name = _efname.getText();
                     modname = _efmodname.getText();
                }
               // char hotkey = 0;
                
                int hotkey = (int)_cfhotkey.getSelectedKey();
                
                //if(_efhotkey.getText().length() > 0)
                   // hotkey = _efhotkey.getText().charAt(0);
    
                if(hotkey >= 'A' && hotkey<='Z'){
                    if(!Global.data.bShiftMode)
                        hotkey = (char)(hotkey-'A'+'a');
                }else if(hotkey >= 'a' && hotkey<='z'){
                    
                }else{
                    hotkey = 0;
                }
                    if(modname == null || modname.length() == 0){
                        //String tips = KeyMaster.getString(KeyMasterResource.KM_INPUTERROR);
                        String tips = KeyMaster.getString(KeyMasterResource.KM_INPUTNAME);
                        //"Please input the name properly.";
                        Dialog.alert(tips);
                        return;
                    
                    }
                    /*else if(hotkey == 0){
                        //String tips = KeyMaster.getString(KeyMasterResource.KM_INPUTERROR);
                        int oldKey = _item.hotKey;
                        _item.set(modname,name,hotkey,_item.modAction);   
                        Global.data.htModItem.remove(new Integer(oldKey));
                        close();
                       // String tips = "Please input the character of Hotkey properly.";
                       // Dialog.alert(tips);
                        return;
                    }*/
                    
                if(Global.data.htModItem.containsKey(new Integer(hotkey)) &&  
                        hotkey!=_item.hotKey && hotkey >0){
                    String tips = KeyMaster.getString(KeyMasterResource.KM_ALREADYEXIST);
                    tips = tips.replace('%',(char)hotkey);
                    Dialog.alert(tips);
                }else{
                    if(hotkey == 0){
                        hotkey =(int) --Global.data.lUID;
                    }
                    
                    if(name == null){
                        name = modname;
                    }
                        
                        //int index = _customlist.getSize()>0?_customlist.getSize()-1:0;
                        int oldKey = _item.hotKey;
                        _item.set(modname,name,hotkey,_item.modAction);

                        _customlist.sort();
                        /*else{
                            _item = new ModItem(number,name,hotkey,Const.MOD_ACTION_RUN);
                            _customlist.insert(index,_item);
                        }*/
                        //if((_item.modAction&Const.MOD_ACTION_MASK) != Const.MOD_ACTION_SYSTEM)
                        Global.data.htModItem.remove(new Integer(oldKey));
                        Global.data.htModItem.put(new Integer(hotkey),_item);
                        //if(Global.data.bNewbie)
                            //Global.data.bNewbie = false;
                        close();
                }
    }
    
    protected void makeMenu(Menu menu, int instance) 
    {
        MenuItem okItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_SAVE), 99, 10) {
            public void run() {
                save();
            }
        };   
        
        MenuItem closeItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_CANCEL), 100, 10) {
        public void run() {
               close();
            } 
        };
        
        menu.add(okItem);
        menu.add(closeItem);
        //super.makeMenu(menu,instance);   
    }
    
    
    protected boolean keyChar(char c, int status, int time)
    {
        if(c == Characters.ESCAPE)
        {   
            save();
            return true;
        }
        
        return super.keyChar(c,status,time);
    }
    
} 
