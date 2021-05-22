package user.util;

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
    public static Document convertDoc(Object o, Document doc)
            throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            doc.append(field.getName(), field.get(o));
        }
        return doc;
    }
}
