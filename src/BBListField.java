package bbdev.keymaster;

import java.util.*;

import net.rim.device.api.ui.component.ListField;


class BBListField extends ListField {
    
    private Vector listData; //save data
    private byte bysort = -1;
    
    BBListField()
    {
        super();
        listData = new Vector(20);
    }
    BBListField(byte sort)
    {
        this();
        bysort = sort;
    }
    public void insert(int index,Object obj)
    {
        super.insert(index);
        listData.addElement(obj);
    }
    
    public void delete(int index)
    {
        super.delete(index);
        listData.removeElementAt(index);
    }
    
    public Object get(int index)
    {
        return listData.elementAt(index);
    }
    
    public Object getSelectObject()
    {
        if(this.getSelectedIndex() >= 0)
            return listData.elementAt(this.getSelectedIndex());
        else 
            return null;
    }
    
    public int getSize()
    {
        return listData.size();
    }

    public void sort()
    {
        int index = 0;
        byte sort  = Global.data.bySort;
        if(bysort != -1)
            sort = bysort;     
        if(sort == Const.SORT_BY_APPNAME){
            int size = this.getSize();
            for(int i=0;i<size;i++){
                for(int j=0;j<size-i-1;j++){
                    ModItem item = (ModItem)listData.elementAt(j);
                    ModItem item2 = (ModItem)listData.elementAt(j+1);
                    if(item2.firstChar < item.firstChar){
                        /*listData.removeElementAt(i);
                        listData.insertElementAt(item2,i);
                        listData.removeElementAt(j);
                        listData.insertElementAt(item,j);*/
                        listData.setElementAt(item2,j);
                        listData.setElementAt(item,j+1);
                    }
                }
            }
        }else if(sort == Const.SORT_BY_HOTKEY){
            int size = this.getSize();
            for(int i=0;i<size;i++){
                for(int j=0;j<size-i-1;j++){
                    ModItem item = (ModItem)listData.elementAt(j);
                    ModItem item2 = (ModItem)listData.elementAt(j+1);
                    int hotkey1= item.hotKey>0?item.hotKey:9999;
                    int hotkey2= item2.hotKey>0?item2.hotKey:9999;
                    if(hotkey2 < hotkey1){
                        listData.setElementAt(item2,j);
                        listData.setElementAt(item,j+1);
                    }
                }
            }            
        }
    }
    
    
} 
