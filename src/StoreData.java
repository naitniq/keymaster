/*
 * StoreData.java
 *
 * � BBDEV, 2009-2010
 * Confidential and proprietary.
 */

package bbdev.keymaster;

import net.rim.device.api.util.*;
import net.rim.device.api.system.*;
import java.util.Vector;
import java.util.Hashtable;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Enumeration;

public final class StoreData implements Persistable {

            //private static final long ID = 0x21c386b53dcd09e2L; // OEM
            public static final long ID = 0x21c386b53dcd0799L; 
            public static final int version = 2;
           // public IntVector listTable = new IntVector(60);    //ProcessId
            //public IntVector listMap = new IntVector(60);    //index -> key
            //public Vector listName = new Vector(60);    //name
            //public Vector listCustomMod = new Vector(20);   //custom module list
            //public Vector listPhoneNumber = new Vector(20); //custom phone list
            public Hashtable htModItem = new Hashtable(20);
            
            /*public boolean bSoftReset = true;
            public boolean bShutdown = true;*/
            public boolean bActiveCode = false;
            public boolean  bNewbie = true;
            
            public boolean bLedStatus = false;
            public boolean bHideApp = false;
            public boolean bFirstRun = true;
            public boolean bShiftMode = true;
            public byte bySort = Const.SORT_BY_APPNAME;
            public int iFontsize = Const.FONT_SIZE_MEDIUM;
            public long starttime = System.currentTimeMillis();
            public long lUID = 0;
            public char homehotkey = 'P';
            public String activeCode = "";
            public int captureIdx = 0;
            public int captureRandom = 0;
            public boolean bCapture = false;
            public boolean bSysmenu = false;
            public boolean bEventmenu = false;
            //public boolean bHaveSysMenu = false;
            
            private StoreData() {       
                if(htModItem.size() == 0){
                    for(int i=0;i<Const.SYSTEM_NAME.length;i++){
                        if(Const.SYSTEM_ID[i] == Const.SYS_WIFI && !Utils.isSupportWifi())
                            continue;
                        int action = ((Const.SYSTEM_ID[i]<<8)|Const.MOD_ACTION_SYSTEM);
                        htModItem.put(new Integer(0-i),new ModItem(Const.SYSTEM_NAME[i],Const.SYSTEM_NAME[i],0-i,action));
                    }
                }
                lUID-=200; //max 200 system function
                Utils.OutputDebugString("add system len:" + htModItem.size());
            }
            
            public byte[] getBytes()
            {
                try{
                    ByteArrayOutputStream bos = new ByteArrayOutputStream(128);
                    DataOutputStream dos = new DataOutputStream(bos);
                    
                    dos.writeLong(ID);
                    dos.writeInt(version);
                    byte byoption = (byte)(((bShiftMode?1:0)<<1)|(bHideApp?1:0));
                    dos.writeByte(byoption);
                    dos.writeByte(bySort);
                    dos.writeInt(iFontsize);
                    dos.writeLong(lUID);
                    dos.writeChar(homehotkey);
                    dos.writeInt(captureIdx);
                    dos.writeInt(captureRandom);
                    
                    Utils.writeString2Dos(activeCode,dos);
                    //dos.writeChars(activeCode);
                    dos.writeInt(htModItem.size());
                    
                    for (Enumeration em = htModItem.keys(); em.hasMoreElements();) {
                        Integer key = (Integer) em.nextElement();
                        ModItem item =  (ModItem) htModItem.get(key);
        
                        dos.writeInt(item.modAction);
                        dos.writeInt(item.hotKey);
                        //dos.writeInt(item.firstChar);
                        //dos.writeUTF(item.modName);
                        //dos.writeUTF(item.aliasName);
                        Utils.writeString2Dos(item.modName,dos);
                        Utils.writeString2Dos(item.aliasName,dos);
                    }
                    
                    return bos.toByteArray();  
                }catch (Exception e){
                    Utils.OutputDebugString(e.toString());
                }
                
                return null;
            }
            
            public void setFromStream(InputStream is)
            {
                try{
                    DataInputStream dis = new DataInputStream(is);
                    
                    long id = dis.readLong();
                    int ver = 0;
                    if(id != 0x21c386b53dcd0678L) //this id not include version code
                        ver = dis.readInt();
                    byte byoption = dis.readByte();
                    bShiftMode = (byoption&2) == 2;
                    bHideApp = (byoption&1) == 1;
                    bySort = dis.readByte();
                    iFontsize = dis.readInt();
                    lUID = dis.readLong();
                    homehotkey = dis.readChar();
                    if(ver > 0)
                        captureIdx = dis.readInt();
                    if(ver > 1)    
                        captureRandom = dis.readInt();
                    Utils.setHotkey(homehotkey);
                    
                    String actCode = Utils.readString4Dis(dis);
                    if(Utils.isActive(actCode))
                    {
                        bActiveCode = true;  
                        activeCode = actCode;
                    }
                    int size = dis.readInt();
                    if(size > 0)
                        htModItem.clear();
                    for(int i=0;i<size;i++){
                        int modAction = dis.readInt();
                        int hotKey = dis.readInt();
                        //int firstChar = dis.readInt();
                        String modName = Utils.readString4Dis(dis);
                        String aliasName = Utils.readString4Dis(dis);
                        htModItem.put(new Integer(hotKey),new ModItem(modName,aliasName,hotKey,modAction));
                    }
                }catch (Exception e){
                    Utils.OutputDebugString(e.toString());
                }
            }
            
            public void commit() 
            {
                PersistentObject.commit(this);
            }

            public static StoreData load() 
            {
                PersistentObject persist = PersistentStore.getPersistentObject( StoreData.ID );
                StoreData contents = null;
                
                try{
                    contents = (StoreData) persist.getContents();
                }catch(Exception ex){
                    contents = null;
                }
                
                synchronized( persist ) {
                        if( contents == null ) {
                                contents = new StoreData();
                                persist.setContents( contents );
                                persist.commit();
                        }
                }
            
                return contents;
            }
}      
