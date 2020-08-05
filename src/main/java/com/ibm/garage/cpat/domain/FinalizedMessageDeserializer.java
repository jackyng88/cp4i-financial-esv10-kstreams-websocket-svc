package com.ibm.garage.cpat.domain;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;


public class FinalizedMessageDeserializer extends ObjectMapperDeserializer<FinalizedMessage>{
    public FinalizedMessageDeserializer(){
        // pass the class to the parent.
        super(FinalizedMessage.class);
    }
}