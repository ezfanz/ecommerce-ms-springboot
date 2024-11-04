package ecommerce.common.service.Aop;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ecommerce.common.service.Util.AuditContextUtil;
import ecommerce.common.service.Util.JSONUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.time.ZonedDateTime;

/**

 @author Irfan Zulkefly
 */

/**

 Aspect for logging execution of service and REST controller Spring components.

 This advice intercepts the controller methods and logs the necessary details

 such as transaction ID, trace ID, span ID, and trace state for better traceability and observability.
 */
@Aspect
@Component
public class LogServiceAdvice {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.S");
    private final Logger logger = LoggerFactory.getLogger("auth.service.audit");

    private final ObjectMapper objectMapper;

    public LogServiceAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        configureObjectMapper(objectMapper);
    }


    /**

     Configure the ObjectMapper for JSON conversion.

     @param objectMapper the ObjectMapper to configure.
     */
    private void configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false);
        objectMapper.registerModule(new JodaModule());

        // Customize serialization for specific classes or properties if needed
        SimpleModule module = new SimpleModule();
        objectMapper.registerModule(module);
    }

    /**

     Pointcut that matches all controllers annotated with @RestController.
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {
    }

    /**

     Around advice that logs method entry, exit, and exception with trace and span details.

     @param joinPoint the join point representing the method execution.

     @return the result of the method execution.

     @throws Throwable if the method execution throws any exception.
     */
    @Around("controllerMethods()")
    public Object aroundControllerMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String transactionId = AuditContextUtil.generateTransactionId(ZonedDateTime.now());
        logMethodEntry(joinPoint, startTime, transactionId);
        Object result = null;
        try {
            result = joinPoint.proceed();
            logMethodExit(joinPoint, result, startTime, transactionId);
        } catch (Throwable ex) {
            logMethodException(joinPoint, ex, startTime, transactionId);
            throw ex;
        }
        return result;
    }

    /**

     Logs the method entry details including transaction ID, trace ID, span ID, and trace state.

     @param joinPoint the join point representing the method execution.

     @param startTime the start time of the method execution.

     @param transactionId the generated transaction ID.
     */
    private void logMethodEntry(JoinPoint joinPoint, long startTime, String transactionId) {
        String httpMethod = getHttpMethod(joinPoint);
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String requestParam = convertArgsToJson(args);

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = (authentication != null) ? authentication.getName() : "anonymous";

        logger.info("REQUEST: {},{},{},{},{},{},{},{},{},{},{}",
                DATE_FORMAT.print(startTime),
                transactionId,
                principal,
                httpMethod,
                joinPoint.getSignature().getDeclaringTypeName(),
                methodName,
                requestParam);
    }

    /**

     Logs the method exit details including transaction ID, trace ID, span ID, and trace state.

     @param joinPoint the join point representing the method execution.

     @param retVal the return value of the method.

     @param startTime the start time of the method execution.

     @param transactionId the generated transaction ID.

     */
    private void logMethodExit(JoinPoint joinPoint, Object retVal, long startTime, String transactionId) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        String httpMethod = getHttpMethod(joinPoint);
        String methodName = joinPoint.getSignature().getName();
        String successResponse = JSONUtil.toJSON(retVal);

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = (authentication != null) ? authentication.getName() : "anonymous";

        logger.info ("RESPONSE: {},{},{},{},{},{},{},{},{},{},{}",
                DATE_FORMAT.print(startTime),
                elapsedTime,
                transactionId,
                principal,
                httpMethod,
                joinPoint.getSignature().getDeclaringTypeName(),
                methodName,
                successResponse);
    }

    /**

     Logs the method exception details including transaction ID, trace ID, span ID, and trace state.

     @param joinPoint the join point representing the method execution.

     @param ex the exception thrown by the method.

     @param startTime the start time of the method execution.

     @param transactionId the generated transaction ID.

     */
    private void logMethodException(JoinPoint joinPoint, Throwable ex, long startTime, String transactionId) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        String httpMethod = getHttpMethod(joinPoint);
        String methodName = joinPoint.getSignature().getName();
        String endpoint = joinPoint.getTarget().getClass().getName() + "." + methodName;

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = (authentication != null) ? authentication.getName() : "anonymous";

        logger.error("ERROR RESPONSE: {},{},{},{},{},{},{},{},{},{},{},{}",
                DATE_FORMAT.print(startTime),
                elapsedTime,
                transactionId,
                principal,
                httpMethod,
                joinPoint.getSignature().getDeclaringTypeName(),
                endpoint,
                ex.getMessage());
    }

    /**

     Retrieves the HTTP method (GET, POST, etc.) of the intercepted method.

     @param joinPoint the join point representing the method execution.

     @return the HTTP method as a string.
     */
    private String getHttpMethod(JoinPoint joinPoint) {
        String httpMethod = "";
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        if (methodSignature.getMethod().isAnnotationPresent(PostMapping.class)) {
            httpMethod = "POST";
        } else if (methodSignature.getMethod().isAnnotationPresent(PutMapping.class)) {
            httpMethod = "PUT";
        } else if (methodSignature.getMethod().isAnnotationPresent(GetMapping.class)) {
            httpMethod = "GET";
        } else if (methodSignature.getMethod().isAnnotationPresent(DeleteMapping.class)) {
            httpMethod = "DELETE";
        }
        return httpMethod;
    }

    /**

     Converts method arguments to JSON string.

     @param args the method arguments.

     @return the JSON representation of the arguments.
     */
    private String convertArgsToJson(Object[] args) {
        try {
            return objectMapper.writeValueAsString(args);
        } catch (JsonProcessingException e) {
            logger.error("Error converting args to JSON: {}", e.getMessage());
            return "{}";
        }
    }

}