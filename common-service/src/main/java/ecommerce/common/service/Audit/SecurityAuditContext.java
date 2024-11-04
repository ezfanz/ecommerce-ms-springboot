package ecommerce.common.service.Audit;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;

/**
 * @author Irfan Zulkefly
 */
public class SecurityAuditContext extends SecurityContextImpl implements SecurityContext {
    private AuditContext auditContext;

    public SecurityAuditContext() {
    }

    public SecurityAuditContext(AuditContext auditContext) {
        this.auditContext = auditContext;
    }

    public SecurityAuditContext(Authentication authentication, AuditContext auditContext) {
        super(authentication);
        this.auditContext = auditContext;
    }

    public AuditContext getAuditContext() {
        return this.auditContext;
    }

    public void setAuditContext(AuditContext auditContext) {
        this.auditContext = auditContext;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            if (!super.equals(o)) {
                return false;
            } else {
                SecurityAuditContext that = (SecurityAuditContext)o;
                return this.auditContext != null ? this.auditContext.equals(that.auditContext) : that.auditContext == null;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (this.auditContext != null ? this.auditContext.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "SecurityAuditContext{auditContext=" + this.auditContext + "}";
    }
}