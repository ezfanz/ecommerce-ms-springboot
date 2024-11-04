package ecommerce.common.service.Util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.aop.support.AopUtils;

import java.io.IOException;

public class CustomProxySerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        try {
            if (AopUtils.isAopProxy(value)) {
                // Handle proxy serialization
                gen.writeStartObject();
                gen.writeStringField("proxyType", AopUtils.getTargetClass(value).getName());
                gen.writeEndObject();
            } else {
                // Default serialization for non-proxy objects
                serializers.defaultSerializeValue(value, gen);
            }
        } catch (StackOverflowError e) {
            // Handle StackOverflowError gracefully
            gen.writeStartObject();
            gen.writeStringField("error", "StackOverflowError occurred during serialization");
            gen.writeEndObject();
        }
    }
}