package ecommerce.common.service.Util;

import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;

import ecommerce.common.service.Audit.AuditContext;
import ecommerce.common.service.Audit.SecurityAuditContext;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditContextUtil {
    private static final String TXN_PREFIX = "TXN";
    private static final String TOKEN_ENC_KEY = "jByyWlNghDsm";
    private static final DateTimeFormatter TXN_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    public AuditContextUtil() {
    }

    public static AuditContext getCurrentAuditContext() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return securityContext instanceof SecurityAuditContext ? ((SecurityAuditContext)securityContext).getAuditContext() : null;
    }

    public static String getCurrentTransactionId() {
        AuditContext currentAuditContext = getCurrentAuditContext();
        return currentAuditContext != null ? currentAuditContext.getTransactionId() : null;
    }

    public static String getCurrentAction() {
        AuditContext currentAuditContext = getCurrentAuditContext();
        return currentAuditContext != null ? currentAuditContext.getAction() : null;
    }

    public static String getCurrentRequestId() {
        AuditContext currentAuditContext = getCurrentAuditContext();
        return currentAuditContext != null ? currentAuditContext.getRequestId() : null;
    }

    public static String getCurrentAuthTokenId() {
        AuditContext currentAuditContext = getCurrentAuditContext();
        return currentAuditContext != null ? currentAuditContext.getAuthTokenId() : null;
    }

    public static String getCurrentIpAddress() {
        AuditContext currentAuditContext = getCurrentAuditContext();
        return currentAuditContext != null ? currentAuditContext.getIpAddress() : null;
    }

    public static String getCurrentChannel() {
        AuditContext currentAuditContext = getCurrentAuditContext();
        return currentAuditContext != null ? currentAuditContext.getChannel() : null;
    }

    public static String getCurrentToken() {
        AuditContext currentAuditContext = getCurrentAuditContext();
        return currentAuditContext != null ? currentAuditContext.getToken() : null;
    }

    public static String encryptToken(String plain, String authTokenId) {
        InvalidArgumentUtil.validateNotBlank(plain, "token");
        InvalidArgumentUtil.validateNotBlank(authTokenId, "authTokenId");
        return CrytoJsEncryptionUtil.encryptCryptoJsAES(plain, "jByyWlNghDsm" + authTokenId);
    }

    public static String decryptToken(String encrypted, String authTokenId) {
        InvalidArgumentUtil.validateNotBlank(encrypted, "token");
        InvalidArgumentUtil.validateNotBlank(authTokenId, "authTokenId");
        return CrytoJsEncryptionUtil.decryptCryptoJsAES(encrypted, "jByyWlNghDsm" + authTokenId);
    }

    public static String generateTransactionId(ZonedDateTime dateTime) {
        String var10000 = TXN_DATE_FORMAT.format(dateTime);
        return "TXN" + var10000 + RandomStringUtils.randomAlphanumeric(8);
    }
}
