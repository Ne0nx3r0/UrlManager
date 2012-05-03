package com.ne0nx3r0.UrlManager;

import java.util.HashMap;
import java.util.Map;

public class UrlCall{
    private String name;
    private String url;
    private String method;
    private String[] params;
    private Map<String,String> data;
    
    public UrlCall(String name,String url,String method){
        this.name = name;
        this.url = url;
        this.method = method;
    }
    
    public UrlCall(String name,String url,String method,String[] params){
        this.name = name;
        this.url = url;
        this.method = method;
        this.params = params;
    }
    
    public void addData(String param,String value){
        if(data == null){
            data = new HashMap<String,String>();
        }
        
        data.put(param, value);
    }
    
    public String getMethod(){
        return this.method;
    }
    
    public String[] getParams(){
        return this.params;
    }
    
    public Map<String,String> getDataParams(){
        return this.data;
    }

    String getUrl() {
        return this.url;
    }
}