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
    
/**
* Add a URL Call with no parameters
*
* @param name - Name for the URL Call
* @param url - URL (without a querystring) EX: http://www.google.com/
* @param method - "GET" in most cases
*/
    public void addUrlCall(
            String name,
            String url,
            String method
    ){
        urlController.addUrlCall(name, url, method);
    }
    
    
/**
* Add a URL Call with parameters as a string
*
* @param name - Name for the URL Call
* @param url - URL (without a querystring) EX: http://www.google.com/
* @param method - "GET" in most cases
* @param params - comma seperated string values "param" or "param1,param2,param3,etc"
*/
    public void addUrlCall(
            String name,
            String url,
            String method,
            String params
    ){
        urlController.addUrlCall(name, url, method, params.split(","));
    }
    
/**
* Add a URL Call with parameters as an array
*
* @param name - Name for the URL Call
* @param url - URL (without a querystring) EX: http://www.google.com/
* @param method - "GET" in most cases
* @param params - String array of parameters
*/
    public void addUrlCall(
            String name,
            String url,
            String method,
            String[] params
    ){
        urlController.addUrlCall(name, url, method, params);
    }
    
    
/**
* Add static data to be sent in a URL Call's querystring
*
* @param name - Name for the URL Call
* @param key - querystring name (&this=)
* @param value - querystring value (=this)
*/
    public void addUrlCallData(String name, String key, String value){
        urlController.getUrlCall(name).addData(key, value);
    }

/**
* Call a URL Call by name with no parameters
*
* @param name - Name for the URL Call
*/
    public void callUrl(String name){
        urlController.callUrl(name,new String[]{});
    }
    
/**
* Call a URL Call by name with parameters, params specified as a string
*
* @param name - Name for the URL Call
* @param params - comma seperated string values "paramValue" or "paramValue1,paramValue2,paramValue3,etc"
*/
    public void callUrl(String name,String params){
        urlController.callUrl(name,params.split(","));
    }
    
/**
* Call a URL Call by name with parameters, params as an array
*
* @param name - Name for the URL Call
* @param params - array of parameter values
*/
    public void callUrl(String name,String[] params){
        urlController.callUrl(name,params);
    }
    
/**
* Call a URL Call by name with parameters, include the player, with params
*
* @param name - URL Call name
* @param p - Player object
*/ 
    public void callUrl(String name,Player p){
        urlController.callUrl(name,p);
    }
    
/**
* Call a URL Call by name with parameters, include the player, with params
*
* @param name - Name for the URL Call
* @param params - array of parameter values
* @param p - Player object
*/ 
    public void callUrl(String name,String[] params,Player p){
        urlController.callUrl(p,name,params);
    }
// End "API"
}