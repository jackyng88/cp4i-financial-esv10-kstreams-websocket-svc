package com.ibm.garage.cpat.domain;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;


public class EnrichedMessageDeserializer extends ObjectMapperDeserializer<EnrichedMessage>{
    public EnrichedMessageDeserializer(){
        // pass the class to the parent.
        super(EnrichedMessage.class);
    }
}