package bbdev.keymaster;

import java.util.Vector;
import javax.microedition.pim.*;

import net.rim.blackberry.api.pdap.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.util.*;
import bbdev.resource.*;


final class EventScreen extends MainScreen{
    
    private static BBListField _customlist;
    private Vector _itemlist;
    private ModItem _item;
    
    private EditField _efname;
    private EditField _efsleep;
    
    private KeyChoiceField _cfinchar;
    
    
    EventScreen(BBListField list,ModItem item)
    {
        _customlist = list;
        String name = "";
        String number = "";
        String hotkey = "0";
        String tipstr = KeyMaster.getString(KeyMasterResource.KM_ADDEVENT);
        if(item!=null){
            tipstr = KeyMaster.getString(KeyMasterResource.KM_EDITEVENT);
        }
       
        VerticalFieldManager tipman =  new TipMananger(tipstr,Color.WHITE,Color.GRAY);
        /*VerticalFieldManager tipman = new VerticalFieldManager(VerticalFieldManager.USE_ALL_WIDTH) {
                    public void paint(Graphics graphics)
                    {
                        graphics.setBackgroundColor(Color.GRAY);
                        graphics.clear();
                        super.paint(graphics);
                    }
        };
        LabelField tips = new LabelField(tipstr
                                , LabelField.NON_FOCUSABLE){
                            public void paint(Graphics graphics) {
                                graphics.setColor(Color.WHITE);
                                graphics.clear();
                                super.paint(graphics);  
                            }
        };
        tipman.add(tips); */
       this.add(tipman); 
        
       this._item = item;
       if(_item != null){
            if( (_item.modAction&Const.MOD_ACTION_MASK) == Const.MOD_ACTION_EVENT ){
                name = _item.aliasName;
                //number = _item.modName;
                if(_item.hotKey > 0)
                    hotkey = ((char)_item.hotKey) + "";
            }
        }

        _efname = new EditField(KeyMaster.getString(KeyMasterResource.KM_EVENTNAME),name,32,EditField.EDITABLE|EditField.NO_NEWLINE);
        this.add(_efname);
        //_efnumber = new EditField(KeyMaster.getString(KeyMasterResource.KM_EVENTNAME),number,32,
                          //  EditField.EDITABLE|EditField.FILTER_PHONE);
        //this.add(_efnumber);
       
          _cfinchar = new KeyChoiceField(KeyMaster.getString(KeyMasterResource.KM_EVENTCHAR),hotkey.charAt(0));
         this.add(_cfinchar);          
    }
    
    protected void makeMenu(Menu menu, int instance) 
    {
        MenuItem okItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_SAVE), 99, 10) {
            public void run() {
                saveItem();
            }
        };   
        
        
        MenuItem closeItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_CANCEL), 100, 10) {
        public void run() {
               close();
            } 
        };
        
        menu.add(okItem);
        //menu.add(selPIMItem);
        menu.add(closeItem);
        //super.makeMenu(menu,instance);   
    }
    
    public void saveItem()
    {
                String name = _efname.getText();
                //String number = _efnumber.getText();
                //char hotkey = 0;
                int hotkey = (int)_cfinchar.getSelectedKey();
                //if(_efhotkey.getText().length() > 0)
                    //hotkey = _efhotkey.getText().charAt(0);
    
                if(hotkey >= 'A' && hotkey<='Z'){
                   
                }else if(hotkey >= 'a' && hotkey<='z'){
                    
                }else{
                    hotkey = 0;
                }
                
                   if(name == null || name.length() == 0){
                           // String tips = KeyMaster.getString(KeyMasterResource.KM_INPUTERROR);
                            String tips = KeyMaster.getString(KeyMasterResource.KM_PELINPUTNAME);//"Please input the Name properly.";
                            Dialog.alert(tips);
                            return;
                    }
                    
                    /*else if(hotkey == 0){
                           // String tips = KeyMaster.getString(KeyMasterResource.KM_INPUTERROR);
                            String tips = "Please input the character of Hotkey properly.";
                            Dialog.alert(tips);
                            return;
                    }*/
                //if(!Global.data.htModItem.containsKey(new Integer(hotkey))){
                if( hotkey >0 && (_item == null || hotkey!=_item.hotKey) ){
                    String tips = KeyMaster.getString(KeyMasterResource.KM_ALREADYEXIST);
                    tips = tips.replace('%',(char)hotkey);
                    Dialog.alert(tips);
                }else{
                        /*if(hotkey == 0){
                            hotkey =(int) --Global.data.lUID;
                        }
                    
                        if(name == null){
                            name = number;
                        }
                        int index = _customlist.getSize()>0?_customlist.getSize()-1:0;
                        if(_item != null){
                            Global.data.htModItem.remove(new Integer(_item.hotKey));
                            _item.set(number,name,hotkey,_item.modAction);
                            Global.data.htModItem.put(new Integer(hotkey),_item);
                            _customlist.sort();
                        }else{
                            _item = new ModItem(number,name,hotkey,Const.MOD_ACTION_PHONE);
                            _customlist.insert(index,_item);
                            _customlist.sort();
                            Global.data.htModItem.put(new Integer(hotkey),_item);
                        }*/
                        //if(Global.data.bNewbie)
                            //Global.data.bNewbie = false;
                        close();
              }
    }
    
    protected boolean keyChar(char c, int status, int time)
    {
        if(c == Characters.ESCAPE)
        {   
            saveItem();
            return true;
        }
        
        return super.keyChar(c,status,time);
    }

} 
