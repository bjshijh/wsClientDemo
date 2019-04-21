package com.example.wsClientDemo.entity;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import com.google.gson.Gson;
public class TaskResult {
    public int status=0;
    public String errorMessage="";
    
    public TaskResult() {
        
    }
    public TaskResult(Exception e){
        status=e.hashCode();
        errorMessage =getExceptionStackTrace(e);
    }
    
    public static String getExceptionStackTrace(Exception e){
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        return result.toString();
    }
    
    public String toJsonString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
