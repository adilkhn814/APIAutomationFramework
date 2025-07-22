package com.api.models.request;

import java.util.List;

public class ConsentRequest {
    
    private String ver;
    private String timestamp;
    private String txnid;
    private ConsentDetail ConsentDetail;
    
    private ConsentRequest(String ver, String timestamp, String txnid, ConsentDetail consentDetail) {
        this.ver = ver;
        this.timestamp = timestamp;
        this.txnid = txnid;
        this.ConsentDetail = consentDetail;
    }
    
    // Getters and Setters
    public String getVer() { return ver; }
    public void setVer(String ver) { this.ver = ver; }
    
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    
    public String getTxnid() { return txnid; }
    public void setTxnid(String txnid) { this.txnid = txnid; }
    
    public ConsentDetail getConsentDetail() { return ConsentDetail; }
    public void setConsentDetail(ConsentDetail consentDetail) { this.ConsentDetail = consentDetail; }
    
    // Nested Classes
    public static class ConsentDetail {
        private String consentStart;
        private String consentExpiry;
        private String consentMode;
        private String fetchType;
        private List<String> consentTypes;
        private List<String> fiTypes;
        private DataConsumer DataConsumer;
        private Customer Customer;
        private Purpose Purpose;
        private FIDataRange FIDataRange;
        private DataLife DataLife;
        private Frequency Frequency;
        private List<DataFilter> DataFilter;
        
        // Getters and Setters
        public String getConsentStart() { return consentStart; }
        public void setConsentStart(String consentStart) { this.consentStart = consentStart; }
        
        public String getConsentExpiry() { return consentExpiry; }
        public void setConsentExpiry(String consentExpiry) { this.consentExpiry = consentExpiry; }
        
        public String getConsentMode() { return consentMode; }
        public void setConsentMode(String consentMode) { this.consentMode = consentMode; }
        
        public String getFetchType() { return fetchType; }
        public void setFetchType(String fetchType) { this.fetchType = fetchType; }
        
        public List<String> getConsentTypes() { return consentTypes; }
        public void setConsentTypes(List<String> consentTypes) { this.consentTypes = consentTypes; }
        
        public List<String> getFiTypes() { return fiTypes; }
        public void setFiTypes(List<String> fiTypes) { this.fiTypes = fiTypes; }
        
        public DataConsumer getDataConsumer() { return DataConsumer; }
        public void setDataConsumer(DataConsumer dataConsumer) { this.DataConsumer = dataConsumer; }
        
        public Customer getCustomer() { return Customer; }
        public void setCustomer(Customer customer) { this.Customer = customer; }
        
        public Purpose getPurpose() { return Purpose; }
        public void setPurpose(Purpose purpose) { this.Purpose = purpose; }
        
        public FIDataRange getFIDataRange() { return FIDataRange; }
        public void setFIDataRange(FIDataRange fIDataRange) { this.FIDataRange = fIDataRange; }
        
        public DataLife getDataLife() { return DataLife; }
        public void setDataLife(DataLife dataLife) { this.DataLife = dataLife; }
        
        public Frequency getFrequency() { return Frequency; }
        public void setFrequency(Frequency frequency) { this.Frequency = frequency; }
        
        public List<DataFilter> getDataFilter() { return DataFilter; }
        public void setDataFilter(List<DataFilter> dataFilter) { this.DataFilter = dataFilter; }
    }
    
    public static class DataConsumer {
        private String id;
        private String type;
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }
    
    public static class Customer {
        private String id;
        private List<Identifier> Identifiers;
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public List<Identifier> getIdentifiers() { return Identifiers; }
        public void setIdentifiers(List<Identifier> identifiers) { this.Identifiers = identifiers; }
    }
    
    public static class Identifier {
        private String type;
        private String value;
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }
    
    public static class Purpose {
        private String code;
        private String refUri;
        private String text;
        private Category Category;
        
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        
        public String getRefUri() { return refUri; }
        public void setRefUri(String refUri) { this.refUri = refUri; }
        
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        
        public Category getCategory() { return Category; }
        public void setCategory(Category category) { this.Category = category; }
    }
    
    public static class Category {
        private String type;
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }
    
    public static class FIDataRange {
        private String from;
        private String to;
        
        public String getFrom() { return from; }
        public void setFrom(String from) { this.from = from; }
        
        public String getTo() { return to; }
        public void setTo(String to) { this.to = to; }
    }
    
    public static class DataLife {
        private String unit;
        private int value;
        
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        
        public int getValue() { return value; }
        public void setValue(int value) { this.value = value; }
    }
    
    public static class Frequency {
        private String unit;
        private int value;
        
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        
        public int getValue() { return value; }
        public void setValue(int value) { this.value = value; }
    }
    
    public static class DataFilter {
        private String type;
        private String operator;
        private String value;
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public String getOperator() { return operator; }
        public void setOperator(String operator) { this.operator = operator; }
        
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }
    
    // Builder Pattern
    public static class Builder {
        private String ver;
        private String timestamp;
        private String txnid;
        private ConsentDetail consentDetail;
        
        public Builder ver(String ver) {
            this.ver = ver;
            return this;
        }
        
        public Builder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public Builder txnid(String txnid) {
            this.txnid = txnid;
            return this;
        }
        
        public Builder consentDetail(ConsentDetail consentDetail) {
            this.consentDetail = consentDetail;
            return this;
        }
        
        public ConsentRequest build() {
            return new ConsentRequest(ver, timestamp, txnid, consentDetail);
        }
    }
    
    @Override
    public String toString() {
        return "ConsentRequest [ver=" + ver + ", timestamp=" + timestamp + ", txnid=" + txnid + ", ConsentDetail=" + ConsentDetail + "]";
    }
}