package com.aa.pojo;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * POJO for Consent Request
 * Represents the structure for Account Aggregator consent creation
 */
public class ConsentRequest {
    
    @SerializedName("ver")
    private String version;
    
    @SerializedName("timestamp")
    private String timestamp;
    
    @SerializedName("txnid")
    private String transactionId;
    
    @SerializedName("ConsentDetail")
    private ConsentDetail consentDetail;
    
    // Constructors
    public ConsentRequest() {}
    
    public ConsentRequest(String version, String timestamp, String transactionId, ConsentDetail consentDetail) {
        this.version = version;
        this.timestamp = timestamp;
        this.transactionId = transactionId;
        this.consentDetail = consentDetail;
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
    
    public ConsentDetail getConsentDetail() {
        return consentDetail;
    }
    
    public void setConsentDetail(ConsentDetail consentDetail) {
        this.consentDetail = consentDetail;
    }
    
    @Override
    public String toString() {
        return "ConsentRequest{" +
                "version='" + version + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", consentDetail=" + consentDetail +
                '}';
    }
    
    /**
     * Inner class for ConsentDetail
     */
    public static class ConsentDetail {
        
        @SerializedName("consentStart")
        private String consentStart;
        
        @SerializedName("consentExpiry")
        private String consentExpiry;
        
        @SerializedName("consentMode")
        private String consentMode;
        
        @SerializedName("fetchType")
        private String fetchType;
        
        @SerializedName("consentTypes")
        private List<String> consentTypes;
        
        @SerializedName("fiTypes")
        private List<String> fiTypes;
        
        @SerializedName("DataConsumer")
        private DataConsumer dataConsumer;
        
        @SerializedName("DataProvider")
        private DataProvider dataProvider;
        
        @SerializedName("Customer")
        private Customer customer;
        
        @SerializedName("Purpose")
        private Purpose purpose;
        
        @SerializedName("FIDataRange")
        private FIDataRange fiDataRange;
        
        @SerializedName("DataLife")
        private DataLife dataLife;
        
        @SerializedName("Frequency")
        private Frequency frequency;
        
        // Constructors
        public ConsentDetail() {}
        
        // Getters and Setters
        public String getConsentStart() {
            return consentStart;
        }
        
        public void setConsentStart(String consentStart) {
            this.consentStart = consentStart;
        }
        
        public String getConsentExpiry() {
            return consentExpiry;
        }
        
        public void setConsentExpiry(String consentExpiry) {
            this.consentExpiry = consentExpiry;
        }
        
        public String getConsentMode() {
            return consentMode;
        }
        
        public void setConsentMode(String consentMode) {
            this.consentMode = consentMode;
        }
        
        public String getFetchType() {
            return fetchType;
        }
        
        public void setFetchType(String fetchType) {
            this.fetchType = fetchType;
        }
        
        public List<String> getConsentTypes() {
            return consentTypes;
        }
        
        public void setConsentTypes(List<String> consentTypes) {
            this.consentTypes = consentTypes;
        }
        
        public List<String> getFiTypes() {
            return fiTypes;
        }
        
        public void setFiTypes(List<String> fiTypes) {
            this.fiTypes = fiTypes;
        }
        
        public DataConsumer getDataConsumer() {
            return dataConsumer;
        }
        
        public void setDataConsumer(DataConsumer dataConsumer) {
            this.dataConsumer = dataConsumer;
        }
        
        public DataProvider getDataProvider() {
            return dataProvider;
        }
        
        public void setDataProvider(DataProvider dataProvider) {
            this.dataProvider = dataProvider;
        }
        
        public Customer getCustomer() {
            return customer;
        }
        
        public void setCustomer(Customer customer) {
            this.customer = customer;
        }
        
        public Purpose getPurpose() {
            return purpose;
        }
        
        public void setPurpose(Purpose purpose) {
            this.purpose = purpose;
        }
        
        public FIDataRange getFiDataRange() {
            return fiDataRange;
        }
        
        public void setFiDataRange(FIDataRange fiDataRange) {
            this.fiDataRange = fiDataRange;
        }
        
        public DataLife getDataLife() {
            return dataLife;
        }
        
        public void setDataLife(DataLife dataLife) {
            this.dataLife = dataLife;
        }
        
        public Frequency getFrequency() {
            return frequency;
        }
        
        public void setFrequency(Frequency frequency) {
            this.frequency = frequency;
        }
        
        @Override
        public String toString() {
            return "ConsentDetail{" +
                    "consentStart='" + consentStart + '\'' +
                    ", consentExpiry='" + consentExpiry + '\'' +
                    ", consentMode='" + consentMode + '\'' +
                    ", fetchType='" + fetchType + '\'' +
                    ", consentTypes=" + consentTypes +
                    ", fiTypes=" + fiTypes +
                    ", dataConsumer=" + dataConsumer +
                    ", dataProvider=" + dataProvider +
                    ", customer=" + customer +
                    ", purpose=" + purpose +
                    ", fiDataRange=" + fiDataRange +
                    ", dataLife=" + dataLife +
                    ", frequency=" + frequency +
                    '}';
        }
    }
    
    /**
     * Inner class for DataConsumer
     */
    public static class DataConsumer {
        @SerializedName("id")
        private String id;
        
        @SerializedName("type")
        private String type;
        
        public DataConsumer() {}
        
        public DataConsumer(String id, String type) {
            this.id = id;
            this.type = type;
        }
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        @Override
        public String toString() {
            return "DataConsumer{" +
                    "id='" + id + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }
    
    /**
     * Inner class for DataProvider
     */
    public static class DataProvider {
        @SerializedName("id")
        private String id;
        
        @SerializedName("type")
        private String type;
        
        public DataProvider() {}
        
        public DataProvider(String id, String type) {
            this.id = id;
            this.type = type;
        }
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        @Override
        public String toString() {
            return "DataProvider{" +
                    "id='" + id + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }
    
    /**
     * Inner class for Customer
     */
    public static class Customer {
        @SerializedName("id")
        private String id;
        
        @SerializedName("Identifiers")
        private List<Identifier> identifiers;
        
        public Customer() {}
        
        public Customer(String id, List<Identifier> identifiers) {
            this.id = id;
            this.identifiers = identifiers;
        }
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public List<Identifier> getIdentifiers() {
            return identifiers;
        }
        
        public void setIdentifiers(List<Identifier> identifiers) {
            this.identifiers = identifiers;
        }
        
        @Override
        public String toString() {
            return "Customer{" +
                    "id='" + id + '\'' +
                    ", identifiers=" + identifiers +
                    '}';
        }
    }
    
    /**
     * Inner class for Identifier
     */
    public static class Identifier {
        @SerializedName("type")
        private String type;
        
        @SerializedName("value")
        private String value;
        
        public Identifier() {}
        
        public Identifier(String type, String value) {
            this.type = type;
            this.value = value;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        public String getValue() {
            return value;
        }
        
        public void setValue(String value) {
            this.value = value;
        }
        
        @Override
        public String toString() {
            return "Identifier{" +
                    "type='" + type + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
    
    /**
     * Inner class for Purpose
     */
    public static class Purpose {
        @SerializedName("code")
        private String code;
        
        @SerializedName("refUri")
        private String refUri;
        
        @SerializedName("text")
        private String text;
        
        @SerializedName("Category")
        private Category category;
        
        public Purpose() {}
        
        public Purpose(String code, String refUri, String text, Category category) {
            this.code = code;
            this.refUri = refUri;
            this.text = text;
            this.category = category;
        }
        
        public String getCode() {
            return code;
        }
        
        public void setCode(String code) {
            this.code = code;
        }
        
        public String getRefUri() {
            return refUri;
        }
        
        public void setRefUri(String refUri) {
            this.refUri = refUri;
        }
        
        public String getText() {
            return text;
        }
        
        public void setText(String text) {
            this.text = text;
        }
        
        public Category getCategory() {
            return category;
        }
        
        public void setCategory(Category category) {
            this.category = category;
        }
        
        @Override
        public String toString() {
            return "Purpose{" +
                    "code='" + code + '\'' +
                    ", refUri='" + refUri + '\'' +
                    ", text='" + text + '\'' +
                    ", category=" + category +
                    '}';
        }
    }
    
    /**
     * Inner class for Category
     */
    public static class Category {
        @SerializedName("type")
        private String type;
        
        public Category() {}
        
        public Category(String type) {
            this.type = type;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        @Override
        public String toString() {
            return "Category{" +
                    "type='" + type + '\'' +
                    '}';
        }
    }
    
    /**
     * Inner class for FIDataRange
     */
    public static class FIDataRange {
        @SerializedName("from")
        private String from;
        
        @SerializedName("to")
        private String to;
        
        public FIDataRange() {}
        
        public FIDataRange(String from, String to) {
            this.from = from;
            this.to = to;
        }
        
        public String getFrom() {
            return from;
        }
        
        public void setFrom(String from) {
            this.from = from;
        }
        
        public String getTo() {
            return to;
        }
        
        public void setTo(String to) {
            this.to = to;
        }
        
        @Override
        public String toString() {
            return "FIDataRange{" +
                    "from='" + from + '\'' +
                    ", to='" + to + '\'' +
                    '}';
        }
    }
    
    /**
     * Inner class for DataLife
     */
    public static class DataLife {
        @SerializedName("unit")
        private String unit;
        
        @SerializedName("value")
        private int value;
        
        public DataLife() {}
        
        public DataLife(String unit, int value) {
            this.unit = unit;
            this.value = value;
        }
        
        public String getUnit() {
            return unit;
        }
        
        public void setUnit(String unit) {
            this.unit = unit;
        }
        
        public int getValue() {
            return value;
        }
        
        public void setValue(int value) {
            this.value = value;
        }
        
        @Override
        public String toString() {
            return "DataLife{" +
                    "unit='" + unit + '\'' +
                    ", value=" + value +
                    '}';
        }
    }
    
    /**
     * Inner class for Frequency
     */
    public static class Frequency {
        @SerializedName("unit")
        private String unit;
        
        @SerializedName("value")
        private int value;
        
        public Frequency() {}
        
        public Frequency(String unit, int value) {
            this.unit = unit;
            this.value = value;
        }
        
        public String getUnit() {
            return unit;
        }
        
        public void setUnit(String unit) {
            this.unit = unit;
        }
        
        public int getValue() {
            return value;
        }
        
        public void setValue(int value) {
            this.value = value;
        }
        
        @Override
        public String toString() {
            return "Frequency{" +
                    "unit='" + unit + '\'' +
                    ", value=" + value +
                    '}';
        }
    }
}