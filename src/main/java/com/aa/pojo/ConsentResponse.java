package com.aa.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * POJO for Consent Response
 * Represents the structure for Account Aggregator consent creation response
 */
public class ConsentResponse {
    
    @SerializedName("ver")
    private String version;
    
    @SerializedName("timestamp")
    private String timestamp;
    
    @SerializedName("txnid")
    private String transactionId;
    
    @SerializedName("Customer")
    private Customer customer;
    
    @SerializedName("ConsentHandle")
    private String consentHandle;
    
    @SerializedName("ConsentStatus")
    private String consentStatus;
    
    // Constructors
    public ConsentResponse() {}
    
    public ConsentResponse(String version, String timestamp, String transactionId, 
                          Customer customer, String consentHandle, String consentStatus) {
        this.version = version;
        this.timestamp = timestamp;
        this.transactionId = transactionId;
        this.customer = customer;
        this.consentHandle = consentHandle;
        this.consentStatus = consentStatus;
    }
    
    // Getters and Setters
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public String getConsentHandle() {
        return consentHandle;
    }
    
    public void setConsentHandle(String consentHandle) {
        this.consentHandle = consentHandle;
    }
    
    public String getConsentStatus() {
        return consentStatus;
    }
    
    public void setConsentStatus(String consentStatus) {
        this.consentStatus = consentStatus;
    }
    
    @Override
    public String toString() {
        return "ConsentResponse{" +
                "version='" + version + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", customer=" + customer +
                ", consentHandle='" + consentHandle + '\'' +
                ", consentStatus='" + consentStatus + '\'' +
                '}';
    }
    
    /**
     * Inner class for Customer in response
     */
    public static class Customer {
        @SerializedName("id")
        private String id;
        
        public Customer() {}
        
        public Customer(String id) {
            this.id = id;
        }
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        @Override
        public String toString() {
            return "Customer{" +
                    "id='" + id + '\'' +
                    '}';
        }
    }
}