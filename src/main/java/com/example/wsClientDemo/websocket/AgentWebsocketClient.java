package com.example.wsClientDemo.websocket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

import com.example.wsClientDemo.component.SpringHelper;
import com.example.wsClientDemo.entity.EdiAgent;
import com.example.wsClientDemo.entity.EdiDataSource;
import com.example.wsClientDemo.entity.TaskResult;
import com.example.wsClientDemo.component.DataWorker;

@Slf4j
public class AgentWebsocketClient extends WebSocketClient {
    
    DataWorker worker =(DataWorker)SpringHelper.getBean("dataWorker");

    public AgentWebsocketClient(String url) throws URISyntaxException {
        super(new URI(url));
    }
    
    @Override
    public void onOpen(ServerHandshake shake) {
        log.info("websocket handshake...");
        for(Iterator<String> it=shake.iterateHttpFields();it.hasNext();) {
            String key = it.next();
            log.info(key+":"+shake.getFieldValue(key));
        }
        EdiAgent agent= (EdiAgent)SpringHelper.getBean("ediAgent");
        agent.dataSource= (EdiDataSource)SpringHelper.getBean("ediDataSource");
        Gson gson = new Gson();
        this.send( "agentInfo=" + gson.toJson(agent));
    }
 
    @Override
    public void onMessage(String paramString) {
        log.info("Got message from server: "+paramString);
        try {
            if ( paramString.equals("sendDataToServer") ){
                worker.doJob();
                TaskResult taskres = new TaskResult();
                this.send(paramString + "taskResult=" + taskres.toJsonString());
            }
        }catch(Exception e){
            e.printStackTrace();
            TaskResult taskres = new TaskResult(e);
            this.send(paramString + "taskResult=" + taskres.toJsonString() );
        }
    }
 
    @Override
    public void onClose(int paramInt, String paramString, boolean paramBoolean) {
        log.info("Websocket closed...");
    }
 
    @Override
    public void onError(Exception e) {
        log.error( "Exception " +e);
    }
    
}
