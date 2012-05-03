package com.ne0nx3r0.UrlManager.events;

import com.ne0nx3r0.UrlManager.UrlCall;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
 
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
    
    public Player getSender(){
        return this.p;
    }
    
    public String[] getParams(){
        return this.params;
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
}