package ecommerce.common.service.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**

 @author Irfan Zulkefly
 */
public class JSONUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Object.class, new CustomProxySerializer()); // Register custom serializer
        mapper.registerModule(module);
    }

    public static String toJSON(Object obj) {
        String result = "";
        if (obj != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                mapper.writeValue(out, obj);
                result = out.toString("UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}

