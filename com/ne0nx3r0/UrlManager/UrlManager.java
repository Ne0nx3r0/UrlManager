package com.ne0nx3r0.UrlManager;

import com.ne0nx3r0.UrlManager.events.UrlResponseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class UrlManager extends JavaPlugin{
    private static List<UrlResponseEvent> urlResults;
    private static boolean urlPending = false;
    
    private static int replyCheckId;
    
    private static UrlController urlController;
    
    @Override
    public void onEnable(){
        urlResults = new ArrayList<UrlResponseEvent>();
        
        urlController = new UrlController(this);
        
        //Setup 
        replyCheckId = getServer().getScheduler().scheduleSyncRepeatingTask(
            this,
            replyCheck,
            20,
            20);
    }
    
    protected static void addUrlResponseEvent(UrlResponseEvent e){
        urlResults.add(e);
        setUrlPending(true);
    }
    
    protected static void setUrlPending(boolean pending){
        urlPending = pending;
    }
    
    //Scheduled save mechanism
    private Runnable replyCheck = new Runnable(){
        @Override
        public void run(){
            //Since this runs every second, we want to rule this out as fast as possible.
            if(urlPending){
                urlPending = false;
                                
                if(!urlResults.isEmpty()){
                    for(UrlResponseEvent ure : urlResults){
                        //try{
                            Bukkit.getServer().getPluginManager().callEvent(ure);
                        //}finally{
                            //this is awkward, but I prefer it
                            //to letting one faulty response clog up everything
                        //}
                    }
                    //Yah, it'll clear everything regardless of whether an error happened.
                    urlResults.clear();
                }
            }
        }
    };
    
//Generic wrappers for console messages
    protected void log(Level level,String sMessage){
        if(!sMessage.equals(""))
            getLogger().log(level,sMessage);
    }
    protected void log(String sMessage){
        log(Level.INFO,sMessage);
    }
    protected void error(String sMessage){
        log(Level.WARNING,sMessage);
    }
    
// Begin "API" 
    public void addUrlCall(
            String name,
            String url,
            String method
    ){
        urlController.addUrlCall(name, url, method);
    }
    
    public void addUrlCall(
            String name,
            String url,
            String method,
            String params
    ){
        urlController.addUrlCall(name, url, method, params.split(","));
    }
    
    public void addUrlCall(
            String name,
            String url,
            String method,
            String[] params
    ){
        urlController.addUrlCall(name, url, method, params);
    }
    
    public void addUrlCallData(String name, String key, String value){
        urlController.getUrlCall(name).addData(key, value);
    }
    
    public void callUrl(String name){
        urlController.callUrl(name,new String[]{});
    }
    
    public void callUrl(String name,String params){
        urlController.callUrl(name,params.split(","));
    }
    
    public void callUrl(String name,String[] params){
        urlController.callUrl(name,params);
    }
    
    public void callUrl(Player p,String name,String[] params){
        urlController.callUrl(p,name,params);
    }
// End "API"
}