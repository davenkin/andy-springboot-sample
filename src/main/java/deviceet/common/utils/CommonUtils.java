package deviceet.common.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.apache.commons.lang3.StringUtils.isBlank;

//todo:split
public class CommonUtils {
    private static final String SPRING_CGLIB_CLASS_SEPARATOR = "$$";

    public static String requireNonBlank(String str, String message) {
        if (isBlank(str)) {
            throw new IllegalArgumentException(message);
        }
        return str;
    }

    public static Class<?> singleParameterizedArgumentClassOf(Class<?> aClass) {
        // The aClass might be proxied by Spring CGlib, so we need to get the real targeted class
        Class<?> realClass = aClass.getName().contains(SPRING_CGLIB_CLASS_SEPARATOR) ? aClass.getSuperclass() : aClass;

        Type genericSuperclass = realClass.getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType)) {
            return null;
        }

        Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();

        if (actualTypeArguments.length != 1) {
            throw new RuntimeException("Expecting exactly one parameterized type argument for " + realClass);
        }

        Type actualTypeArgument = actualTypeArguments[0];
        if (actualTypeArgument instanceof Class) {
            return (Class<?>) actualTypeArgument;
        }
        return null;
    }

    public static String mongoConcatFields(String... fields) {
        return String.join(".", fields);
    }

}
