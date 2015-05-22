/*
 * SyncData.java
 *
 * bbdev, 2003-2008
 * Confidential and proprietary.
 */
package bbdev.keymaster;

import net.rim.device.api.synchronization.*;
import net.rim.device.api.system.*;
import net.rim.device.api.i18n.*;
import net.rim.device.api.util.*;

class SyncData extends SyncItem{
    
    private static final int TYPE_BOOL              = 1;
    private static final int TYPE_INT_MAP           = 2;
    private static final int TYPE_STR_NAME          = 3;
    private static final int TYPE_STR_CNAME         = 4;
    private static final int TYPE_STR_ACTIVECODE    = 5;
    private static SyncData _syncData;
    
    SyncData() 
    {   
    
    
    }
    
    public static SyncData getInstance()
    {
        if (_syncData == null) {
            _syncData = new SyncData();
        }
        return _syncData;
    }
    
    public int getSyncVersion()
    {
        return 1;
    }
    
    public String getSyncName()
    {
        return "KeyMaster";
    }
    
    public String getSyncName(Locale locale)
    {
        return null;
    }
    
    public boolean getSyncData(DataBuffer buffer, int version)
    {
        try{
            StoreData data = StoreData.load();
            
            /*ConverterUtilities.writeByteArray(buffer,TYPE_BOOL,data.getBoolList());
            ConverterUtilities.writeIntArray(buffer,TYPE_INT_MAP,data.getMapList());
            ConverterUtilities.writeString(buffer,TYPE_STR_NAME,data.getNameList());
            ConverterUtilities.writeString(buffer,TYPE_STR_CNAME,data.getCNameList());*/
            ConverterUtilities.writeString(buffer,TYPE_STR_ACTIVECODE,data.activeCode);
            return true;
        }catch(Exception ex){
            return false;
        }

    }
    
    public boolean setSyncData(DataBuffer buffer, int version)
    {
        try{
            StoreData data = StoreData.load();
            
            while(buffer.available() > 0)
            {
                if(ConverterUtilities.isType(buffer,TYPE_BOOL))
                {
                    //data.setBooolList(ConverterUtilities.readByteArray(buffer));
                }
                else if(ConverterUtilities.isType(buffer,TYPE_INT_MAP))
                {
                    //contact.setLast(new String(ConverterUtilities.readByteArray(data)).trim());
                }
                else if(ConverterUtilities.isType(buffer,TYPE_STR_NAME))
                {
                    //contact.setLast(new String(ConverterUtilities.readByteArray(data)).trim());
                }
                else if(ConverterUtilities.isType(buffer,TYPE_STR_CNAME))
                {
                    //contact.setLast(new String(ConverterUtilities.readByteArray(data)).trim());
                }
                else if(ConverterUtilities.isType(buffer,TYPE_STR_ACTIVECODE))
                {
                    data.activeCode = new String(ConverterUtilities.readByteArray(buffer)).trim();
                }
            }
            
            data.commit();
            return true;
        }catch(Exception ex){
            return false;
        }
    }
} 
