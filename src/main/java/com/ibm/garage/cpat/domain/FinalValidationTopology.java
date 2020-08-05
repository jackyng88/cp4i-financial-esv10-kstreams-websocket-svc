package com.ibm.garage.cpat.domain;

import java.time.Instant;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.Stores;

import io.quarkus.kafka.client.serialization.JsonbSerde;
import io.quarkus.kafka.client.serialization.JsonbSerializer;

import com.ibm.garage.cpat.domain.*;


@ApplicationScoped
public class FinalValidationTopology {

    @ConfigProperty(name = "START_TOPIC_NAME")
    private String INCOMING_TOPIC;

    @ConfigProperty(name = "TARGET_TOPIC_NAME")
    private String OUTGOING_TOPIC;


    @Produces
    public Topology buildTopology() {

        StreamsBuilder builder = new StreamsBuilder();

        JsonbSerde<EnrichedMessage> enrichedMessageSerde = new JsonbSerde<>(EnrichedMessage.class);
        JsonbSerde<FinalizedMessage> finalizedMessageSerde = new JsonbSerde<>(FinalizedMessage.class);


        KStream<String, FinalizedMessage> enrichedStream = builder.stream(
            INCOMING_TOPIC,
            Consumed.with(Serdes.String(), enrichedMessageSerde)
        )
        .filter (
            (key, message) -> readyToPublishToWebSocketCheck(message)
        )
        .mapValues(
            (message) -> new FinalizedMessage(message)
        );

        enrichedStream.to (
            OUTGOING_TOPIC,
            Produced.with(Serdes.String(), finalizedMessageSerde)
        );  

        // builder.stream(
        //     INCOMING_TOPIC,
        //     Consumed.with(Serdes.Integer(), enrichedMessageSerde)
        // )
        // .filter (
        //     (key, message) -> readyToPublishToWebSocketCheck(message)
        // )
        
        
        return builder.build();
    }

    public boolean readyToPublishToWebSocketCheck (EnrichedMessage rawMessage) {
        // Returns a boolean based on whether all the current boolean flags and checks have completed.
        //System.out.println(rawMessage);
        return (!rawMessage.compliance_services && !rawMessage.technical_validation && !rawMessage.schema_validation
                && !rawMessage.trade_enrichment && !rawMessage.business_validation);
    }
}