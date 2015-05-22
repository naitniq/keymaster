package bbdev.keymaster;

import net.rim.device.api.system.*;
import net.rim.device.api.util.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;

public class KMDialog extends PopupScreen{
    
    private final static int _CUSTOM_SIZE_W   = 100;
    private final static int _CUSTOM_SIZE_H   = 50;
    private final static int _SPACE  = 15;
    
    private final static Font _font = Font.getDefault();
    private String _str;
    private int _color = 0x000000;
    public KMDialog(String str) {   
        //super( new VerticalFieldManager( Manager.NO_VERTICAL_SCROLL | Manager.NO_VERTICAL_SCROLLBAR ) );
        super(new VerticalFieldManager());
       // _color = color;
        _str = str;
    }
    
    public int getPreferredWidth()
    {
        if(_str != null)
            return _font.getAdvance(_str) + (_SPACE<<1);
       return _CUSTOM_SIZE_W;
    }
    
    public int getPreferredHeight()
    {
        return  _font.getHeight() + (_SPACE<<1);
        //return h;    
    }   
    
    protected void sublayout( int width, int height ) 
    {
        int w = getPreferredWidth();
        int h = getPreferredHeight();
      
        setExtent(w, h);
        int x  = ( Display.getWidth()- w ) >> 1;
        int y  = ( Display.getHeight() - h ) >> 1;  
        
        setPosition( x, y );
        layoutDelegate( w, h );
    }
    
    protected void paint(Graphics g) 
    {
        XYRect myExtent = getExtent();
        //int color = g.getColor();
        //Font ft = g.getFont();
        //g.setColor(_color);
        
        //g.setFont(_font);
        if(_str!=null)
            g.drawText(_str,_SPACE,_SPACE,0,myExtent.width-(_SPACE<<1));
            
        //g.setColor(color);
        //g.setFont(ft);
    }
                      
    protected boolean keyDown(int keycode, int time) 
    {
         //DxTools.OutputDebugString("status:" + status);
        //int key = Keypad.key(keycode);
        this.close();
        
        return super.keyDown( keycode, time );
    }
} 

