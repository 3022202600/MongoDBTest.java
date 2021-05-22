package clicks.util;

import org.bson.Document;

import java.lang.reflect.Field;

public class ConvertUtil {
    public static Document convertDoc(Object o)
            throws IllegalArgumentException, IllegalAccessException {
        Document document = new Document();
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            document.append(field.getName(), field.get(o));
        }
        return document;
    }

}
