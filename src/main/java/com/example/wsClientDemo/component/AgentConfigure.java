package com.example.wsClientDemo.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.java_websocket.enums.ReadyState;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.wsClientDemo.websocket.AgentWebsocketClient;
import com.example.wsClientDemo.entity.EdiAgent;

@Slf4j
@Configuration
@Component
public class AgentConfigure {

    @Value("${server.websocketUrl}")
    public String websocketServerUrl;
    
    @Autowired
    EdiAgent agent = null;
    
    /*
    @Bean
    public EdiAgent ediAgent() {
        if (agent == null) {
            agent = new EdiAgent();
            agent.dataSource = new EdiDataSource();
        }
        return agent;
    }
    */

        
    static AgentWebsocketClient clientSocket = null;
    public AgentWebsocketClient getAgentWebsocketClient() throws Exception {
        try {
            if (clientSocket != null && clientSocket.getReadyState()==ReadyState.OPEN ) {
            }else{
                log.info("Trying connect to Socket Server " + websocketServerUrl);
                clientSocket=null;
                clientSocket = new AgentWebsocketClient(websocketServerUrl);
                clientSocket.connect();
            }
            return clientSocket;
        } catch (Exception e) {
            throw e;
        }
    }
}
