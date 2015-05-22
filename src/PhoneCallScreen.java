
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


final class PhoneCallScreen extends MainScreen{
    
    private static BBListField _customlist;
    private ModItem _item;
    
    private EditField _efname;
    private EditField _efnumber;
    //private EditField _efhotkey;
    private KeyChoiceField _cfhotkey;
    
    PhoneCallScreen(BBListField list,ModItem item)
    {
        _customlist = list;
        String name = "";
        String number = "";
        String hotkey = "0";
        String tipstr = KeyMaster.getString(KeyMasterResource.KM_ADD_PHONECALL);//"Add Phone Call";
        if(item!=null){
            tipstr = KeyMaster.getString(KeyMasterResource.KM_EDIT_PHONECALL);//"Edit Phone Call";
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
            if( (_item.modAction&Const.MOD_ACTION_MASK) == Const.MOD_ACTION_PHONE ){
                name = _item.aliasName;
                number = _item.modName;
                if(_item.hotKey > 0)
                    hotkey = ((char)_item.hotKey) + "";
            }
        }

        _efname = new EditField(KeyMaster.getString(KeyMasterResource.KM_PHONENAME),name,32,EditField.EDITABLE|EditField.NO_NEWLINE);
        this.add(_efname);
        _efnumber = new EditField(KeyMaster.getString(KeyMasterResource.KM_PHONENUMBER),number,32,
                            EditField.EDITABLE|EditField.FILTER_PHONE);
        this.add(_efnumber);
       /* _efhotkey = new EditField(KeyMaster.getString(KeyMasterResource.KM_HOTKEY),hotkey,2,EditField.EDITABLE){
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
        this.add(_efhotkey); */
          _cfhotkey = new KeyChoiceField(KeyMaster.getString(KeyMasterResource.KM_HOTKEY),hotkey.charAt(0));
         this.add(_cfhotkey);          
    }
    
    protected void makeMenu(Menu menu, int instance) 
    {
        MenuItem okItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_SAVE), 99, 10) {
            public void run() {
                saveItem();
            }
        };   
        
        MenuItem selPIMItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_SEL_PIM), 100, 10) {
        public void run() {
               selPhoneNumFromPIM();
            } 
        };
        
        MenuItem closeItem = new MenuItem(KeyMaster.getString(KeyMasterResource.KM_CANCEL), 100, 10) {
        public void run() {
               close();
            } 
        };
        
        menu.add(okItem);
        menu.add(selPIMItem);
        menu.add(closeItem);
        //super.makeMenu(menu,instance);   
    }
    
    private void selPhoneNumFromPIM()
    {
        try{
            BlackBerryContactList contactList = 
                (BlackBerryContactList) PIM.getInstance().openPIMList(PIM.CONTACT_LIST,PIM.READ_ONLY);
            //Contact c = contactList.choose(null,BlackBerryContactList.AddressTypes.,false);
            PIMItem pitem = contactList.choose();
            //if (pitem == null)
           //{
                //System.out.println("no contact chosen ");
            //}
            if (pitem != null){
                Vector results = new Vector(5);
                BlackBerryContact contact = (BlackBerryContact) pitem;
                int numValues = contact.countValues(BlackBerryContact.TEL);
                for (int i = 0; i < numValues; i++) {
                    if (contact.getAttributes(BlackBerryContact.TEL, i) == Contact.ATTR_WORK) {
                            results.addElement(contact.getString(Contact.TEL,i));
                        //results.addElement(new DialogNumberChooseItem(resources.getString(WORK), contact.getString(BlackBerryContact.TEL, i)));
                        } else if (contact.getAttributes(BlackBerryContact.TEL, i) == BlackBerryContact.ATTR_HOME) {
                            results.addElement(contact.getString(Contact.TEL,i));
                        //results.addElement(new DialogNumberChooseItem(resources.getString(HOME), contact.getString(BlackBerryContact.TEL, i)));
                        } else if (contact.getAttributes(BlackBerryContact.TEL, i) == BlackBerryContact.ATTR_MOBILE) {
                            results.addElement(contact.getString(Contact.TEL,i));
                        //results.addElement(new DialogNumberChooseItem(resources.getString(MOBILE), contact.getString(BlackBerryContact.TEL, i)));
                        } else if (contact.getAttributes(BlackBerryContact.TEL, i) == BlackBerryContact.ATTR_OTHER) {
                            results.addElement(contact.getString(Contact.TEL,i));
                        //results.addElement(new DialogNumberChooseItem(resources.getString(OTHER), contact.getString(BlackBerryContact.TEL, i)));
                        }
                        /*else if (contact.getAttributes(BlackBerryContact.TEL, i) == BlackBerryContact.ATTR_HOME2) {
                            results.addElement(contact.getString(Contact.TEL,i));
                        //results.addElement(new DialogNumberChooseItem(resources.getString(MOBILE), contact.getString(BlackBerryContact.TEL, i)));
                        } else if (contact.getAttributes(BlackBerryContact.TEL, i) == BlackBerryContact.ATTR_WORK2) {
                            results.addElement(contact.getString(Contact.TEL,i));
                        //results.addElement(new DialogNumberChooseItem(resources.getString(OTHER), contact.getString(BlackBerryContact.TEL, i)));
                        }*/
                }
                
                int index = 0;
                if(results.size()>1){
                    String[] strs = new String[results.size()];
                    results.copyInto(strs);
                    index = Dialog.ask(KeyMaster.getString(KeyMasterResource.KM_CALLWN)+"?",strs, 0 );
                    //index = Dialog.ask("Call which number?",strs, 0);
                }
                if(results.size() > index){
                    String phonenumber = (String)results.elementAt(index);
                    _efnumber.setText(phonenumber);
                }
                //String phonenumber = pitem.getString(Contact.TEL,0);
                //_efnumber.setText(phonenumber);
                //String emailAddress = c.getString(Contact.TEL,0);
                //System.out.println(phonenumber);
            }
            
        }catch (Exception ee){
            System.err.println(ee.getMessage());
            ee.printStackTrace();
        }
    }
    
    public void saveItem()
    {
                String name = _efname.getText();
                String number = _efnumber.getText();
                //char hotkey = 0;
                int hotkey = (int)_cfhotkey.getSelectedKey();
                //if(_efhotkey.getText().length() > 0)
                    //hotkey = _efhotkey.getText().charAt(0);
    
                if(hotkey >= 'A' && hotkey<='Z'){
                    if(!Global.data.bShiftMode)
                        hotkey = (char)(hotkey-'A'+'a');
                }else if(hotkey >= 'a' && hotkey<='z'){
                    
                }else{
                    hotkey = 0;
                }
                
                    if(number == null || number.length() == 0){
                           // String tips = KeyMaster.getString(KeyMasterResource.KM_INPUTERROR);
                            String tips = KeyMaster.getString(KeyMasterResource.KM_PELINPUTNUMBER);//"Please input the Phone Number properly.";
                            Dialog.alert(tips);
                            return;
                    }else if(name == null || name.length() == 0){
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
                if( hotkey >0 && Global.data.htModItem.containsKey(new Integer(hotkey)) && (_item == null || hotkey!=_item.hotKey) ){
               // if(!Global.data.htModItem.containsKey(new Integer(hotkey)) || (_item != null && hotkey==_item.hotKey) ){    
                    String tips = KeyMaster.getString(KeyMasterResource.KM_ALREADYEXIST);
                    tips = tips.replace('%',(char)hotkey);
                    Dialog.alert(tips);
                }else{
                      if(hotkey == 0){
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
                        }
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
