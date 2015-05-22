package bbdev.keymaster;

import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.ObjectChoiceField;
import java.util.Vector;

public class KeyChoiceField extends ObjectChoiceField {

    public final static String[] KEY_NAMES_DEF = new String[] { 
            "NONE","a","b","c","d","e","f","g","h","i","j","k","l",
            "m","n","o","p","q","r","s","t","u", "v","w","x","y","z",
            "A","B","C","D","E","F","G","H","I","J","K","L",
            "M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
    };
    
    public final static char[] KEY_VALUES_DEF = new char[] { 
            '0','a','b','c','d','e','f','g','h','i','j','k','l',
            'm','n','o','p','q','r','s','t','u', 'v','w','x','y','z',
            'A','B','C','D','E','F','G','H','I','J','K','L',
            'M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
    };
    
    public String[] KEY_NAMES;
    public char[] KEY_VALUES;
    
    public static String[] key_names;
    public static Character[] key_values;
    public KeyChoiceField(String label, char selectedKey) 
    {
        this(label,selectedKey,KEY_NAMES_DEF,KEY_VALUES_DEF);
        /*super();
        
        setLabel(label);
        init(selectedKey);
        setChoices(key_names);
        //setSelectedIndex(selidx);
        for (int i = 0; i < key_values.length; i++) {
            if (selectedKey == key_values[i].charValue()) {
                setSelectedIndex(i);
                break;
            }
        }*/
    }
    public KeyChoiceField(String label, char selectedKey,String[] names,char[] values) 
    {
        super();
        
        KEY_NAMES = names;
        KEY_VALUES = values;
        
       /*if(mode == 1){
            if(selectedKey > 'A' && selectedKey<'Z')
                selectedKey +=32;
       }*/
        setLabel(label);
        init(selectedKey);
        setChoices(key_names);
        //setSelectedIndex(selidx);

       for (int i = 0; i < key_values.length; i++) {
                if (selectedKey == key_values[i].charValue()) {
                    setSelectedIndex(i);
                    break;
                }
            }
    }
    public void init(char selectedKey)
    {
        Vector vName = new Vector(54);
        Vector vValue = new Vector(54);
        int selidx = 0;
        int end = KEY_VALUES.length;
        for (int i = 0; i < end; i++) {
            if( selectedKey == 0 || selectedKey == KEY_VALUES[i] 
                || Global.data.htModItem.containsKey(new Integer(KEY_VALUES[i])) == false ){
                vName.addElement((String)KEY_NAMES[i]);
                vValue.addElement(new Character(KEY_VALUES[i]));
            }
        }   
        
        int size = vName.size();
        key_names = new String[size];
        key_values = new Character[size];
        vName.copyInto(key_names);
        vValue.copyInto(key_values);

    }
    
   /* protected boolean keyChar(char c, int status, int time)
    {
        if(c == '0'){
            setSelectedIndex(0);
            return true;
        }else if(c>='A' && c<='Z'){
            setSelectedIndex(c-'A'+1);
            return true; 
        }else if(c>='a' && c<='z'){
            setSelectedIndex(c-'a'+27);
            return true; 
        }
            
        return super.keyChar(c,status,time);
    }*/
    
    public char getSelectedKey() 
    {
        return key_values[getSelectedIndex()].charValue();
    }
    
    /*protected void paint(Graphics graphics) {
        graphics.setColor(getSelectedColor());
        super.paint(graphics);
    }*/
}
