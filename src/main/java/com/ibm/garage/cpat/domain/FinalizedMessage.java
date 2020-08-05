package com.ibm.garage.cpat.domain;

public class FinalizedMessage extends EnrichedMessage {
    public boolean websocket_ready;

    public FinalizedMessage() {

    }

    public FinalizedMessage(EnrichedMessage enrichedMessage) {

        super(enrichedMessage);
        this.websocket_ready = true;

    }
}