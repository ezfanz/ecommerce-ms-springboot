package ecommerce.common.service.Audit;

import java.util.Objects;

/**
 * @author Irfan Zulkefly
 */
public class AuditContext {
    private String channel;
    private String action;
    private String transactionId;
    private String requestId;
    private String authTokenId;
    private String ipAddress;
    private String token;

    public AuditContext() {
    }

    public String getChannel() {
        return this.channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAuthTokenId() {
        return this.authTokenId;
    }

    public void setAuthTokenId(String authTokenId) {
        this.authTokenId = authTokenId;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AuditContext that = (AuditContext)o;
            return Objects.equals(this.channel, that.channel) && Objects.equals(this.action, that.action) && Objects.equals(this.transactionId, that.transactionId) && Objects.equals(this.requestId, that.requestId) && Objects.equals(this.authTokenId, that.authTokenId) && Objects.equals(this.ipAddress, that.ipAddress) && Objects.equals(this.token, that.token);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.channel, this.action, this.transactionId, this.requestId, this.authTokenId, this.ipAddress, this.token});
    }

    public String toString() {
        return "AuditContext{action='" + this.action + "', transactionId='" + this.transactionId + "', requestId='" + this.requestId + "', authTokenId='" + this.authTokenId + "', ipAddress='" + this.ipAddress + "'}";
    }
}