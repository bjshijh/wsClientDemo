package com.example.wsClientDemo.component;

import lombok.extern.slf4j.Slf4j;
import com.example.wsClientDemo.websocket.AgentWebsocketClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WsMonitor implements Runnable {
    public WsMonitor() {
    }
    
    static AgentConfigure agentConfigure;
    
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10000);
                agentConfigure= (AgentConfigure)SpringHelper.getBean("agentConfigure");
                AgentWebsocketClient client= agentConfigure.getAgentWebsocketClient();
                if (client!=null && !client.isClosed()){
                    client.send("ping");
                }else{
                    log.warn("client socket closed!");
                }
            }catch(Exception e){
                e.printStackTrace();
                log.error(e.getMessage());
            }
            
        }
    }
    
}
