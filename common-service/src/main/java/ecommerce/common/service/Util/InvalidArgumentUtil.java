package ecommerce.common.service.Util;


import org.apache.commons.lang3.StringUtils;

/**
 * @author Irfan Zulkefly
 */
public class InvalidArgumentUtil {
    public InvalidArgumentUtil() {
    }

    public static void validateNotNull(Object value, String name) {
        if (value == null) {
            throw new IllegalArgumentException("[" + name + "] cannot be null!");
        }
    }

    public static void validateNotBlank(String value, String name) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("[" + name + "] cannot be null or blank!");
        }
    }

    public static void validateRange(int value, String name, Integer minValue, Integer maxValue) {
        if (minValue != null && value < minValue) {
            throw new IllegalArgumentException("[" + name + "] value[" + value + "] cannot be less than minValue[" + minValue + "]!");
        } else if (maxValue != null && value > maxValue) {
            throw new IllegalArgumentException("[" + name + "] value[" + value + "] cannot be less than maxValue[" + maxValue + "]!");
        }
    }

    public static void validateRange(long value, String name, Long minValue, Long maxValue) {
        if (minValue != null && value < minValue) {
            throw new IllegalArgumentException("[" + name + "] value[" + value + "] cannot be less than minValue[" + minValue + "]!");
        } else if (maxValue != null && value > maxValue) {
            throw new IllegalArgumentException("[" + name + "] value[" + value + "] cannot be less than maxValue[" + maxValue + "]!");
        }
    }
}