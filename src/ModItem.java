package bbdev.keymaster;

import net.rim.device.api.util.Persistable;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

final class ModItem implements Persistable {
    //event format "a|alt,b,c|shift"
    public String modName;
    public String aliasName;
    public int modAction; //1-8 bit:mod action id,9-16 bit:sub action id
    public int hotKey;
    public int firstChar; //ignorecase for sort
    
    public ModItem(String modname,int hotkey)
    {
        this(modname,modname,hotkey);
    }
    
    public ModItem(String modname,String aliasname,int hotkey)
    {
        this(modname,aliasname,hotkey,Const.MOD_ACTION_RUN);
    }

    public ModItem(String modname,String aliasname,int hotkey,int action)
    {
        set(modname,aliasname,hotkey,action);
        /*this.modName = modname;
        this.aliasName = aliasName;
        this.firstChar = Utils.ignoreCase(aliasName.charAt(0));
        this.hotKey = hotkey;
        this.modAction = action;*/
    }
    
    public void set(String modname,String aliasname,int hotkey,int action)
    {
        this.modName = modname;
        if(aliasname == null)
            aliasname = modname;
        this.aliasName = aliasname;
        this.firstChar = Utils.ignoreCase(aliasName.charAt(0));
        this.hotKey = hotkey;
        this.modAction = action;
    }
    
    public String getShowString()
    {
        if(hotKey>0)
            return this.aliasName + " ["+ (char)hotKey + "]";
        else
            return this.aliasName + " [none]";
    }
    
    /*public byte[] getBytes()
    {
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream(128);
            DataOutputStream dos = new DataOutputStream(bos);
            
            dos.writeInt(modAction);
            dos.writeInt(hotKey);
            dos.writeInt(firstChar);
            Utils.writeString2Dos(modName,dos);
            Utils.writeString2Dos(aliasName,dos);
            
            return bos.toByteArray();  
        } catch (Exception e) {
            ;
        }
        
        return null;
    }*/
} 
