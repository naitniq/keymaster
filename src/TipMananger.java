package bbdev.keymaster;

import net.rim.device.api.system.*;
import net.rim.device.api.util.*;
import net.rim.device.api.util.*;
import net.rim.device.api.crypto .*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;

public class TipMananger extends VerticalFieldManager{
    
    String tipstr;
    int backcolor;
    int forecolor;
    TipMananger _this;
    TipMananger(String str,int forecolor,int backcolor) {  
        super(VerticalFieldManager.USE_ALL_WIDTH);
        tipstr = str;
        this.forecolor = forecolor;
        this.backcolor = backcolor;
        _this = this;
        LabelField tips = new LabelField(tipstr
                                , LabelField.NON_FOCUSABLE){
                            public void paint(Graphics graphics) {
                                graphics.setColor(_this.forecolor);
                                graphics.clear();
                                super.paint(graphics);  
                            }
        };
        this.add(tips);             
    }

    public void paint(Graphics graphics)
    {
        graphics.setBackgroundColor(backcolor);
        graphics.clear();
        super.paint(graphics);
    }
} 
