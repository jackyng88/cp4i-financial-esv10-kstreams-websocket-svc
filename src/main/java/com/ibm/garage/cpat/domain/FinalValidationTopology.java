package com.ibm.garage.cpat.domain;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import io.quarkus.kafka.client.serialization.JsonbSerde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.garage.cpat.infrastructure.*;


@ApplicationScoped
public class FinalValidationTopology {

    @ConfigProperty(name = "START_TOPIC_NAME")
    private String INCOMING_TOPIC;

    @ConfigProperty(name = "TARGET_TOPIC_NAME")
    private String OUTGOING_TOPIC;

    @Inject
    WebSocketEndpoint websocketEndPoint;


    @Produces
    public Topology buildTopology() {

        StreamsBuilder builder = new StreamsBuilder();

        JsonbSerde<EnrichedMessage> enrichedMessageSerde = new JsonbSerde<>(EnrichedMessage.class);
        JsonbSerde<FinalizedMessage> finalizedMessageSerde = new JsonbSerde<>(FinalizedMessage.class);

        ObjectMapper obj = new ObjectMapper();

        // Filters only the messages that are ready to publish [false, false, false, false, false]
        // it then creates a new FinalizedMessage object with a websocket_ready = true field.
        // Next, it invokes peek() and writes to the WebSocket session and then to a topic for
        // persistence.
        KStream<String, FinalizedMessage> enrichedStream = builder.stream(
            INCOMING_TOPIC,
            Consumed.with(Serdes.String(), enrichedMessageSerde)
        )
        .filter (
            (key, message) -> readyToPublishToWebSocketCheck(message)
        )
        .mapValues(
            (message) -> new FinalizedMessage(message)
        )
        .peek((key, message) -> {
            websocketEndPoint.getSessions().forEach(s -> {
                try {
                    s.getBasicRemote().sendText(obj.writeValueAsString(message));
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });

        enrichedStream.to (
            OUTGOING_TOPIC,
            Produced.with(Serdes.String(), finalizedMessageSerde)
        );  
        
        return builder.build();
    }

    public boolean readyToPublishToWebSocketCheck (EnrichedMessage rawMessage) {
        // Returns a boolean based on whether all the current boolean flags and checks have completed.
        //System.out.println(rawMessage);
        return (!rawMessage.compliance_services && !rawMessage.technical_validation && !rawMessage.schema_validation
                && !rawMessage.trade_enrichment && !rawMessage.business_validation);
    }
}