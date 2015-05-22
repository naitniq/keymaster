package bbdev.keymaster;

import java.util.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.util.*;
import net.rim.blackberry.api.invoke.*;
import bbdev.resource.*;

public class TabScreen extends MainScreen implements FocusChangeListener {

        protected int tabCount;
        protected LabelField []tabLabel;

        protected VerticalFieldManager tabArea;
        
        //protected BBListField[] list;
        protected VerticalFieldManager[] manager;
        
        protected int curTabIndex;

        public TabScreen(String[] tab) 
        {
            tabCount = tab.length;
            tabLabel = new LabelField[tabCount];
            manager = new VerticalFieldManager[tabCount];
            
            init(tab);
        }
        
        public int getTabIndex()
        {
            return curTabIndex;  
        }
        
        protected void init(String[] tab)
        {
            HorizontalFieldManager hManager = new HorizontalFieldManager();
            Font black_font = Font.getDefault().derive(Font.BOLD, Const.FONT_SIZE[Const.FONT_SIZE_MEDIUM], Ui.UNITS_pt);
            //LabelField spacer = new LabelField(" | ", LabelField.NON_FOCUSABLE);
            for(int i=0;i<tabCount;i++){
                tabLabel[i] = new LabelField(tab[i],LabelField.FOCUSABLE|LabelField.HIGHLIGHT_SELECT);
                tabLabel[i].setFont(black_font);
                tabLabel[i].setFocusListener(this);
                hManager.add(tabLabel[i]);
                if(i<tabCount-1){
                    hManager.add(new LabelField(" | ", LabelField.NON_FOCUSABLE));
                }
                
                manager[i] = new VerticalFieldManager(); 
            }

            add(hManager);
            add(new SeparatorField());

            tabArea = displayTab(0);
            add(tabArea);   
        }
        
        
         
        protected void onFocusNotify(boolean focus)
        {

        }
        
        public void focusChanged(Field field, int eventType) 
        {
            if (tabArea != null) {
                if (eventType == FOCUS_GAINED) {
                    for(int i=0;i<tabCount;i++){
                        if(field == tabLabel[i]){
                            delete(tabArea);
                            tabArea = displayTab(i);
                            add(tabArea);
                            curTabIndex = i;
                            break;
                        }
                    }
                }
            }
        }

        protected VerticalFieldManager displayTab(int idx) 
        {
            return manager[idx];
        }  
   }
