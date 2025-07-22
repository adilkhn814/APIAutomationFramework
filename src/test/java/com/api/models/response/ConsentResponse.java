package com.api.models.response;

public class ConsentResponse {
    
    private String ver;
    private String timestamp;
    private String txnid;
    private Customer Customer;
    private String ConsentHandle;
    
    public ConsentResponse() {
        // Default constructor for deserialization
    }
    
    public ConsentResponse(String ver, String timestamp, String txnid, Customer customer, String consentHandle) {
        this.ver = ver;
        this.timestamp = timestamp;
        this.txnid = txnid;
        this.Customer = customer;
        this.ConsentHandle = consentHandle;
    }
    
    // Getters and Setters
    public String getVer() {
        return ver;
    }
    
    public void setVer(String ver) {
        this.ver = ver;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getTxnid() {
        return txnid;
    }
    
    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }
    
    public Customer getCustomer() {
        return Customer;
    }
    
    public void setCustomer(Customer customer) {
        this.Customer = customer;
    }
    
    public String getConsentHandle() {
        return ConsentHandle;
    }
    
    public void setConsentHandle(String consentHandle) {
        this.ConsentHandle = consentHandle;
    }
    
    // Nested Customer class
    public static class Customer {
        private String id;
        
        public Customer() {
            // Default constructor
        }
        
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
            return "Customer [id=" + id + "]";
        }
    }
    
    @Override
    public String toString() {
        return "ConsentResponse [ver=" + ver + ", timestamp=" + timestamp + ", txnid=" + txnid + 
               ", Customer=" + Customer + ", ConsentHandle=" + ConsentHandle + "]";
    }
}