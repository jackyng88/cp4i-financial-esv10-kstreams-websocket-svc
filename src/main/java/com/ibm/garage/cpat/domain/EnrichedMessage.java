package com.ibm.garage.cpat.domain;

public class EnrichedMessage {
    
    public String user_id;
    public String stock_symbol;
    public String exchange_id;
    public String trade_type;
    public String date_created;
    public String date_submitted;
    public int quantity;
    public double stock_price;
    public double total_cost;
    public int institution_id;
    public int country_id;
    public boolean compliance_services;
    public boolean technical_validation;
    public boolean schema_validation;
    public boolean business_validation;
    public boolean trade_enrichment;
    public String company_id;
    public String company_name;
    public String tckr;
    public String sector_cd;

    public EnrichedMessage() {

    }

    public EnrichedMessage(EnrichedMessage enrichedMessage) {

        this.user_id = enrichedMessage.user_id;
        this.stock_symbol = enrichedMessage.stock_symbol;
        this.exchange_id = enrichedMessage.exchange_id;
        this.trade_type = enrichedMessage.trade_type;
        this.date_created = enrichedMessage.date_created;
        this.date_submitted = enrichedMessage.date_submitted;
        this.quantity = enrichedMessage.quantity;
        this.stock_price = enrichedMessage.stock_price;
        this.total_cost = enrichedMessage.total_cost;
        this.institution_id = enrichedMessage.institution_id;
        this.country_id = enrichedMessage.country_id;
        this.compliance_services = enrichedMessage.compliance_services;
        this.technical_validation = enrichedMessage.technical_validation;
        this.schema_validation = enrichedMessage.schema_validation;
        this.business_validation = enrichedMessage.business_validation;
        this.trade_enrichment = enrichedMessage.trade_enrichment;
        this.company_id = enrichedMessage.company_id;
        this.company_name = enrichedMessage.company_name;
        this.tckr = enrichedMessage.tckr;
        this.sector_cd = enrichedMessage.sector_cd;
    }
}