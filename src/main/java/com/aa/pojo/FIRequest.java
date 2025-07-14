package com.aa.pojo;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * POJO for FI Request
 * Represents the structure for Financial Information request
 */
public class FIRequest {
    
    @SerializedName("ver")
    private String version;
    
    @SerializedName("timestamp")
    private String timestamp;
    
    @SerializedName("txnid")
    private String transactionId;
    
    @SerializedName("FIDataRange")
    private FIDataRange fiDataRange;
    
    @SerializedName("Consent")
    private Consent consent;
    
    @SerializedName("KeyMaterial")
    private KeyMaterial keyMaterial;
    
    // Constructors
    public FIRequest() {}
    
    public FIRequest(String version, String timestamp, String transactionId, 
                    FIDataRange fiDataRange, Consent consent, KeyMaterial keyMaterial) {
        this.version = version;
        this.timestamp = timestamp;
        this.transactionId = transactionId;
        this.fiDataRange = fiDataRange;
        this.consent = consent;
        this.keyMaterial = keyMaterial;
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
    
    public FIDataRange getFiDataRange() {
        return fiDataRange;
    }
    
    public void setFiDataRange(FIDataRange fiDataRange) {
        this.fiDataRange = fiDataRange;
    }
    
    public Consent getConsent() {
        return consent;
    }
    
    public void setConsent(Consent consent) {
        this.consent = consent;
    }
    
    public KeyMaterial getKeyMaterial() {
        return keyMaterial;
    }
    
    public void setKeyMaterial(KeyMaterial keyMaterial) {
        this.keyMaterial = keyMaterial;
    }
    
    @Override
    public String toString() {
        return "FIRequest{" +
                "version='" + version + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", fiDataRange=" + fiDataRange +
                ", consent=" + consent +
                ", keyMaterial=" + keyMaterial +
                '}';
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
     * Inner class for Consent
     */
    public static class Consent {
        @SerializedName("id")
        private String id;
        
        @SerializedName("digitalSignature")
        private String digitalSignature;
        
        public Consent() {}
        
        public Consent(String id, String digitalSignature) {
            this.id = id;
            this.digitalSignature = digitalSignature;
        }
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getDigitalSignature() {
            return digitalSignature;
        }
        
        public void setDigitalSignature(String digitalSignature) {
            this.digitalSignature = digitalSignature;
        }
        
        @Override
        public String toString() {
            return "Consent{" +
                    "id='" + id + '\'' +
                    ", digitalSignature='" + digitalSignature + '\'' +
                    '}';
        }
    }
    
    /**
     * Inner class for KeyMaterial
     */
    public static class KeyMaterial {
        @SerializedName("cryptoAlg")
        private String cryptoAlg;
        
        @SerializedName("curve")
        private String curve;
        
        @SerializedName("params")
        private String params;
        
        @SerializedName("DHPublicKey")
        private DHPublicKey dhPublicKey;
        
        @SerializedName("Nonce")
        private String nonce;
        
        public KeyMaterial() {}
        
        public KeyMaterial(String cryptoAlg, String curve, String params, DHPublicKey dhPublicKey, String nonce) {
            this.cryptoAlg = cryptoAlg;
            this.curve = curve;
            this.params = params;
            this.dhPublicKey = dhPublicKey;
            this.nonce = nonce;
        }
        
        public String getCryptoAlg() {
            return cryptoAlg;
        }
        
        public void setCryptoAlg(String cryptoAlg) {
            this.cryptoAlg = cryptoAlg;
        }
        
        public String getCurve() {
            return curve;
        }
        
        public void setCurve(String curve) {
            this.curve = curve;
        }
        
        public String getParams() {
            return params;
        }
        
        public void setParams(String params) {
            this.params = params;
        }
        
        public DHPublicKey getDhPublicKey() {
            return dhPublicKey;
        }
        
        public void setDhPublicKey(DHPublicKey dhPublicKey) {
            this.dhPublicKey = dhPublicKey;
        }
        
        public String getNonce() {
            return nonce;
        }
        
        public void setNonce(String nonce) {
            this.nonce = nonce;
        }
        
        @Override
        public String toString() {
            return "KeyMaterial{" +
                    "cryptoAlg='" + cryptoAlg + '\'' +
                    ", curve='" + curve + '\'' +
                    ", params='" + params + '\'' +
                    ", dhPublicKey=" + dhPublicKey +
                    ", nonce='" + nonce + '\'' +
                    '}';
        }
    }
    
    /**
     * Inner class for DHPublicKey
     */
    public static class DHPublicKey {
        @SerializedName("expiry")
        private String expiry;
        
        @SerializedName("Parameters")
        private String parameters;
        
        @SerializedName("KeyValue")
        private String keyValue;
        
        public DHPublicKey() {}
        
        public DHPublicKey(String expiry, String parameters, String keyValue) {
            this.expiry = expiry;
            this.parameters = parameters;
            this.keyValue = keyValue;
        }
        
        public String getExpiry() {
            return expiry;
        }
        
        public void setExpiry(String expiry) {
            this.expiry = expiry;
        }
        
        public String getParameters() {
            return parameters;
        }
        
        public void setParameters(String parameters) {
            this.parameters = parameters;
        }
        
        public String getKeyValue() {
            return keyValue;
        }
        
        public void setKeyValue(String keyValue) {
            this.keyValue = keyValue;
        }
        
        @Override
        public String toString() {
            return "DHPublicKey{" +
                    "expiry='" + expiry + '\'' +
                    ", parameters='" + parameters + '\'' +
                    ", keyValue='" + keyValue + '\'' +
                    '}';
        }
    }
}