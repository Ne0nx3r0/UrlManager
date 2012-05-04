package com.ne0nx3r0.UrlManager.events;

import com.ne0nx3r0.UrlManager.UrlCall;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
 
public class UrlResponseEvent extends Event {    
    private static final HandlerList handlers = new HandlerList();
    
    private UrlCall uc;
    private Player p;
    private String[] params;
    private String result;
 
    public UrlResponseEvent(UrlCall uc,Player p,String[] params,String result){
        this.uc = uc;
        this.p = p;
        this.params = params;
        this.result = result;
    }
 
    public String getPlainTextResult(){
        return this.result;
    }
    
    public Map<String,String> getJSONResult(){
        JSONObject json;
        
        try {
            json = (JSONObject) new JSONParser().parse(this.result);
            
            return (Map<String,String>) json;
        } catch (ParseException ex) {
            Logger.getLogger(UrlResponseEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public Player getSender(){
        return this.p;
    }
    
    public String[] getParams(){
        return this.params;
    }
    
    public String getUrlCallName(){
        return uc.getName();
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
}