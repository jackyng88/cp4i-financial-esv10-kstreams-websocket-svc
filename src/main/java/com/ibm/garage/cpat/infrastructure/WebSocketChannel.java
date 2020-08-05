package com.ibm.garage.cpat.infrastructure;

import io.reactivex.Flowable;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

import com.ibm.garage.cpat.domain.*;


/*
A bit of a hacky way to work the connection to a WebSocket service. This 'bean' reads from a channel (pre-websocket)
that will have the final messages produced onto as a Kafka Topic by Kafka Streams. This will create channels bi-directionally
for incoming and outgoing. The outgoing sends to a (websocket-ready) channel to be injected into a Flowable object 
within the WebSocketServer class.
*/
@ApplicationScoped
public class WebSocketChannel {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketChannel.class);  

    @Incoming("pre-websocket")
    @Outgoing("websocket-ready")
    @Broadcast
    public Flowable<EnrichedMessage> primeChannelForWebsocket(EnrichedMessage EnrichedMessage) {

        //EnrichedMessage receivedMessage = EnrichedMessage;

        LOGGER.info("Message {} is being sent to the WebSocket Service", EnrichedMessage);
    
        return Flowable.just(EnrichedMessage);
        
    }
}