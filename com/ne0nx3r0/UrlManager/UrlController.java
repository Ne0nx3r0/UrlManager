package com.ne0nx3r0.UrlManager;

import com.ne0nx3r0.UrlManager.UrlManager;
import com.ne0nx3r0.UrlManager.events.UrlResponseEvent;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class UrlController{
    private static UrlManager plugin;
    
    private static Map<String,UrlCall> urlCalls;
    
    public UrlController(UrlManager p){
        UrlController.plugin = p;
        
        urlCalls = new HashMap<String,UrlCall>();
    }
    
    public void addUrlCall(String name, String url, String method){
        urlCalls.put(name, new UrlCall(
            name,
            url,
            method
        ));
    }
    
    public void addUrlCall(
        String name,
        String url,
        String method,
        String[] params
    ){        
        urlCalls.put(name, new UrlCall(
            name,
            url,
            method,
            params
        ));
    }

    public void callUrl(String name, String[] params) {
        callUrl(null,name,params);
    }

    public void callUrl(Player p, String urlName, String[] paramValues){        
        //TODO: Add POST method support
        
        new Thread(new getUrlThenSendEvent(
                p,
                urlCalls.get(urlName),
                paramValues
        )).start();
    }

    UrlCall getUrlCall(String name) {
        return urlCalls.get(name);
    }

    private static class getUrlThenSendEvent implements Runnable{
        private final Player p;
        private final UrlCall uc;
        private final String[] params;

        public getUrlThenSendEvent(Player p,UrlCall uc,String[] paramValues){
            this.p = p;
            this.uc = uc;
            this.params = paramValues;
        }
        
        @Override
        public void run(){

//Create a map for parameters
        Map<String,String> paramMap = new HashMap<String,String>();
        String[] paramKeys = uc.getParams();
            
        if(paramKeys != null){
            for(int i=0;i<paramKeys.length;i++){
                if(paramKeys.length == i+1 //last run
                && this.params.length > i+1){//more values than keys

                    String sLastParam = "";

                    for(int j=i;j<this.params.length;j++){
                        sLastParam += " "+this.params[j];
                    }

                    paramMap.put(paramKeys[i], sLastParam.substring(1));

                    break;
                }
                else if(this.params.length > i){//in case they didn't specify all params
                    paramMap.put(paramKeys[i], this.params[i]);
                }
            }   
        }
            
//Append any static data to the parameters
        Map<String,String> paramDatas = uc.getDataParams();
        
        if(paramDatas != null){
            for(String sKey : paramDatas.keySet()){
                paramMap.put(sKey, paramDatas.get(sKey));
            }
        }
        
    String sParams = "";

    //Generate a parameter URL string
    if(!paramMap.isEmpty()){
        for(String sKey : paramMap.keySet()){
            try{
                sParams += "&"+URLEncoder.encode(sKey, "UTF-8")+"="+URLEncoder.encode(paramMap.get(sKey), "UTF-8");
            }catch(UnsupportedEncodingException ex){
                Logger.getLogger(UrlManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    //Convert the first & to ?
        sParams = "?" + sParams.substring(1);
    }    
//Now for the heavy lifting...
        String sReturn = null;
        URL url = null;
        try{
            url = new URL(uc.getUrl() + sParams);
        }catch(MalformedURLException ex){
            Logger.getLogger(UrlManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        InputStream is = null;
        try{
            is = url.openStream();
        } catch (IOException ex){
            Logger.getLogger(UrlManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder sb = new StringBuilder();

            String line;
            try{
                while ((line = br.readLine()) != null){
                        sb.append(line);
                }
            }catch (IOException ex){
                Logger.getLogger(UrlManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            sReturn = sb.toString();
        }finally{
            try{
                is.close();
            }catch(IOException ex){
                Logger.getLogger(UrlManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
// At this point sReturn contains the raw text response
        UrlManager.addUrlResponseEvent(new UrlResponseEvent(uc,p,this.params,sReturn));
        
        UrlManager.setUrlPending(true);
        }
    }
}
