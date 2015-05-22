/*
 * Const.java
 * bbdev, 2008
 */

package bbdev.keymaster;

import net.rim.device.api.system.*;
import net.rim.blackberry.api.invoke.*;
import bbdev.resource.*;

public final class Const {
    //Debug Switch
    public final static boolean DEBUG  = false;
    public final static boolean OEM = false;
    public final static boolean OEM_MOBIROO = false;
    public final static int SCREEN_WIDTH = Display.getWidth();
    public final static int SCREEN_HEIGHT = Display.getHeight();
    
    public final static String RES_FOLDER = "/res/";
     
    public final static String NET_ADD = "BBDEV.ORG";
    //public final static String NET_ADD = "eMobiStudio";
    
    public final static String[] BLOCK_LIST = { "net_rim_app_manager_console","net_rim_app_manager","net_rim_bis_launch",
                                                "net_rim_bis_client","net_rim_bb_mediacontenthandler","net_rim_bb_trust_application_manager",
                                                "net_rim_bb_dnslookup_app","net_rim_device_apps_games_wordmole","net_rim_event_log_viewer_app",
                                                "net_rim_bb_globalsearch_app","net_rim_bb_messaging_app","net_rim_bb_qm_yahoo","Reset",
                                                "net_rim_bb_file_explorer","net_rim_bb_ping_app","net_rim_bb_camera","net_rim_bb_profiles_app",
                                                "net_rim_bb_fileindexservice","net_rim_bb_geolocationagent","net_rim_plazmic_flint","net_rim_bb_gas",
                                                "net_rim_bb_safe_mode","RimGanInjector","net_rim_media_actions_daemon","net_rim_bb_mediaapp_launcher_app", 
                                                "net_rim_bb_app_center","net_rim_bb_application_updater","net_rim_bb_games_app", 
                                                "net_rim_bb_gmail","net_rim_bbgroup_calendar","net_rim_bbgroup_inbox","net_rim_bbgroup_lists",
                                                "net_rim_bbgroup_messaging","net_rim_bbgroup_photos","net_rim_bb_addressbook_simapp","net_rim_bb_supl_app",
                                                "net_rim_bb_ribbon_lib","net_rim_bb_addressbook_usimapp","net_rim_bb_otasl_app","net_rim_bb_diagnostic",
                                                "net_rim_bb_sitesurvey","net_rim_bb_diagnostics_ui",
                                              };
    
    public final static String[] WHITE_LIST = { "net_rim_bb_clock","net_rim_bb_qm_peer", };
    
    public final static String[][] FIX_NAME_LIST = { {"net_rim_bb_qm_peer","BlackBerry Messenger"},
                                                    {"net_rim_bb_lbs","BlackBerry Maps"},
                                                    {"net_rim_bb_medialibraryplayer","Now Playing..."},
                                                    };
                                                    
    public final static String [] NOT_RUN_LIST = {"net_rim_bb_medialibraryplayer"};
    public final static String [] NOT_RUN_MESSAGE = {"No media player is running."};
    
    public final static String [] NOT_CHECK_SUCCESS = {"net_rim_bb_manage_connections","net_rim_bb_mc_app","net_rim_bb_qm_peer"};
    
                                                    
                   //{"net_rim_bb_qm_yahoo","Yahoo! Messenger"} ,{"net_rim_bb_qm_google","GTalk"} };
       
   /* public final static int MESSAGE_APP = 0;
    public final static int OPTIONS_APP = 1;    
    public final static int CAMERA_APP = 2;          
    
    public final static int SP_MSG_OFFSET = 1005;
    public final static int SP_OFFSET = 1009;
    public final static int SP_MESSAGE = -1004; 
    public final static int SP_WIFI = -1005; 
    public final static int SP_LOCKSYSTEM = -1006; 
    public final static int SP_SHUTDOWN = -1007; 
    public final static int SP_SOFTRESET = -1008;    
    public final static int SP_SIGNAL = -1009; 
      
    public final static String[] SYSTEM_LIST = {"net_rim_bb_messaging_app","net_rim_bb_options_app","net_rim_bb_camera"};
    
    public final static String[] SP_ARG = {MessageArguments.ARG_NEW,MessageArguments.ARG_NEW_SMS,
                                                MessageArguments.ARG_NEW_MMS,MessageArguments.ARG_NEW_PIN};  
                                                
    public final static String[] SP_MODNAME = {"km_signal","km_softreset","km_shutdown","km_locksystem","km_wifi",
                        "km_new_email","km_new_sms","km_new_mms","km_new_pin"};                                         
    public final static int[] SP_HANDLE = {SP_SIGNAL,SP_SOFTRESET,SP_SHUTDOWN,SP_LOCKSYSTEM,SP_WIFI,-1004,-1003,-1002,-1001}; 
    
    public final static String[] SP_APPNAME = {
        KeyMaster.getString(KeyMasterResource.KM_SIGNAL),KeyMaster.getString(KeyMasterResource.KM_CAMERA)
        KeyMaster.getString(KeyMasterResource.KM_SOFTRESET),KeyMaster.getString(KeyMasterResource.KM_SHUTDOWN),
        KeyMaster.getString(KeyMasterResource.KM_LOCK_SYSTEM),KeyMaster.getString(KeyMasterResource.KM_WIFI),
        KeyMaster.getString(KeyMasterResource.KM_NEW_EMAIL),KeyMaster.getString(KeyMasterResource.KM_NEW_SMS),
        KeyMaster.getString(KeyMasterResource.KM_NEW_MMS),KeyMaster.getString(KeyMasterResource.KM_NEW_PIN)
    };   */
    
    public final static int MESSAGE_APP = 0;
    public final static int OPTIONS_APP = 1;    
    public final static int CAMERA_APP = 2;   
    public final static int MAP_APP = 3;   
    public final static String[] SYSTEM_LIST = { "net_rim_bb_messaging_app","net_rim_bb_options_app",
                                    "net_rim_bb_camera","net_rim_bb_lbs" };
    
    public final static byte SORT_NONE = 0;
    public final static byte SORT_BY_HOTKEY = 1;
    public final static byte SORT_BY_APPNAME = 2;
    
    public final static int FONT_SIZE_SMALL = 0;
    public final static int FONT_SIZE_MEDIUM = 1;
    public final static int FONT_SIZE_LARGE = 2;
    
    public final static int[] FONT_SIZE = {6,8,10};
    
    public static final byte MOD_ACTION_RUN = 1;
    public static final byte MOD_ACTION_SYSTEM = 2;
    public static final byte MOD_ACTION_PHONE = 3;
    public static final byte MOD_ACTION_EVENT = 4;
    
    //sys sub action
    public static final byte SYS_SIGNAL = 1;
    public static final byte SYS_CAMERA = 2;
    public static final byte SYS_SOFTRESET = SYS_CAMERA+1;   
    public static final byte SYS_SHUTDOWN = SYS_SOFTRESET+1; 
    public static final byte SYS_LOCK = SYS_SHUTDOWN+1; 
    public static final byte SYS_WIFI = SYS_LOCK+1;  
    public static final byte SYS_NEW_EMAIL = SYS_WIFI+1;  
    public static final byte SYS_NEW_SMS = SYS_NEW_EMAIL+1;   
    public static final byte SYS_NEW_MMS = SYS_NEW_SMS+1;   
    public static final byte SYS_NEW_PIN = SYS_NEW_MMS+1; 
    public static final byte SYS_MESSAGE = SYS_NEW_PIN+1; 
    public static final byte SYS_SEARCH = SYS_MESSAGE +1; 
    public static final byte SYS_BACKLIGHT = SYS_SEARCH +1;
    public static final byte SYS_LED = SYS_BACKLIGHT +1;
    public static final byte SYS_BLUETOOTH = SYS_LED +1;
   // public static final byte SYS_CAPTURE = SYS_BLUETOOTH +1;
    //public static final byte SYS_NEW_MAP = SYS_LED +1;
    
    public static final int MOD_ACTION_MASK = 0xFF;
    public static final int SUB_ACTION_MASK = 0xFF00;
    
    public final static byte[] SYSTEM_ID = {
        SYS_SIGNAL,SYS_CAMERA,
       // SYS_SOFTRESET,
        SYS_SHUTDOWN,
        SYS_LOCK,SYS_WIFI,
        SYS_NEW_EMAIL,SYS_NEW_SMS,
        SYS_NEW_MMS,SYS_NEW_PIN,
        SYS_MESSAGE,SYS_SEARCH,
        SYS_BACKLIGHT,SYS_LED,
        SYS_BLUETOOTH,
        //SYS_NEW_MAP,
    };
    
    public final static String[] SYSTEM_NAME = {
        KeyMaster.getString(KeyMasterResource.KM_SIGNAL),KeyMaster.getString(KeyMasterResource.KM_CAMERA),
        //KeyMaster.getString(KeyMasterResource.KM_SOFTRESET),
        KeyMaster.getString(KeyMasterResource.KM_SHUTDOWN),
        KeyMaster.getString(KeyMasterResource.KM_LOCK_SYSTEM),KeyMaster.getString(KeyMasterResource.KM_WIFI),
        KeyMaster.getString(KeyMasterResource.KM_NEW_EMAIL),KeyMaster.getString(KeyMasterResource.KM_NEW_SMS),
        KeyMaster.getString(KeyMasterResource.KM_NEW_MMS),KeyMaster.getString(KeyMasterResource.KM_NEW_PIN),
        KeyMaster.getString(KeyMasterResource.KM_MESSAGE),KeyMaster.getString(KeyMasterResource.KM_SEARCH),
        KeyMaster.getString(KeyMasterResource.KM_BACKLIGHT),KeyMaster.getString(KeyMasterResource.KM_LED),
        KeyMaster.getString(KeyMasterResource.KM_BLUETOOTH),
        //KeyMaster.getString(KeyMasterResource.KM_NEW_MAP),
    };     
    public final static char[] SYSTEM_HOTKEY = {
        's','c',
       // 'r',
        't',
        'l','w',
        'e','m',
        'm','p',
        'g','r',
    };  
   
    public static String SoftVersion="2.0";    
}
