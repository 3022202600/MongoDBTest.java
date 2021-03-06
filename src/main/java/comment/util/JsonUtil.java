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
     * ?????????ObjectMapper
     */
    static {
        /** ????????????????????????JSON?????????????????????Java??????????????????????????? **/
        OBJECTMAPPER
                .configure(
                        DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
                        false);
        /** ??????????????????????????????????????? **/
        OBJECTMAPPER.configure(
                SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        /** ?????????????????????????????? **/
        OBJECTMAPPER.getSerializationConfig().withDateFormat(new SimpleDateFormat(DATEFORMAT));
        /** ??????????????????????????? **/
        OBJECTMAPPER.getSerializationConfig().withSerializationInclusion(
                Inclusion.NON_NULL);
        /** ????????????????????????????????? **/
        OBJECTMAPPER.getDeserializationConfig().withDateFormat(
                new SimpleDateFormat(DATEFORMAT));
        /** ???????????????????????????????????? **/
        OBJECTMAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,
                true);
        /** ???????????????????????????????????? **/
        OBJECTMAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        /** ???????????????????????????????????? **/
        OBJECTMAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS,
                true);
        /** ?????????????????????????????? **/
        OBJECTMAPPER
                .configure(
                        JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER,
                        true);
        OBJECTMAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    }
}
