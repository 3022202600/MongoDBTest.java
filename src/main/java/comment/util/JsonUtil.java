package comment.util;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

public class JsonUtil {
    private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final ObjectMapper OBJECTMAPPER = new ObjectMapper();

    public static String toJson(Object entity) {
        return toJson(entity, false);
    }

    public static String toJson(Object entity, boolean prettyPrint)
            throws RuntimeException {
        try {
            StringWriter sw = new StringWriter();
            JsonGenerator jg = OBJECTMAPPER.getJsonFactory()
                    .createJsonGenerator(sw);
            if (prettyPrint) {
                jg.useDefaultPrettyPrinter();
            }
            OBJECTMAPPER.writeValue(jg, entity);
            return sw.toString();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String str, Class<T> clazz)
            throws RuntimeException {
        try {
            return OBJECTMAPPER.readValue(str, clazz);
        } catch (JsonParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String str, TypeReference<T> t)
            throws RuntimeException {
        try {
            return OBJECTMAPPER.readValue(str, t);
        } catch (JsonParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static <T> T fromJson(String str, Class<?> collectionClass,
                                 Class<?> elementClasses) throws RuntimeException {
        try {
            JavaType type = OBJECTMAPPER.getTypeFactory()
                    .constructParametricType(collectionClass, elementClasses);
            return OBJECTMAPPER.readValue(str, type);
        } catch (JsonParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化ObjectMapper
     */
    static {
        /** 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性 **/
        OBJECTMAPPER
                .configure(
                        DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
                        false);
        /** 设置不使用默认日期类型格式 **/
        OBJECTMAPPER.configure(
                SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        /** 设置转换日期类型格式 **/
        OBJECTMAPPER.getSerializationConfig().withDateFormat(new SimpleDateFormat(DATEFORMAT));
        /** 设置转换时忽略空值 **/
        OBJECTMAPPER.getSerializationConfig().withSerializationInclusion(
                Inclusion.NON_NULL);
        /** 设置反转时日期类型格式 **/
        OBJECTMAPPER.getDeserializationConfig().withDateFormat(
                new SimpleDateFormat(DATEFORMAT));
        /** 设置键值可为非双引号形式 **/
        OBJECTMAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,
                true);
        /** 设置解析器支持解析单引号 **/
        OBJECTMAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        /** 设置解析器支持解析结束符 **/
        OBJECTMAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS,
                true);
        /** 设置可以带有转义字符 **/
        OBJECTMAPPER
                .configure(
                        JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER,
                        true);
        OBJECTMAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    }
}
