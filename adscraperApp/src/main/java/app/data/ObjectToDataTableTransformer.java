package app.data;

import java.lang.reflect.Field;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class ObjectToDataTableTransformer {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");

    private List<String> headers;
    private StringListList rows;

    public ObjectToDataTableTransformer(List l) {
        assert (l != null);
        try {
            headers = transformHeader(l);
            rows = transformRows(l);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DataTable getDataTable() {
        return headers != null ? new DataTable(headers, rows) : null;
    }

    private List<String> transformRow(Object o) throws IllegalArgumentException, IllegalAccessException {
        LinkedList<String> result = new LinkedList<String>();
        for (Field f : o.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            String value = f.getType().getSimpleName();
            if (f.getType().equals(String.class))
                value = (String) f.get(o);
            else if (f.getType().equals(Integer.TYPE))
                value = String.valueOf(f.getInt(o));
            else if (f.getType().equals(Date.class))
                value = dateFormat.format((Date) f.get(o));
            result.add(value);

        }
        return result;
    }

    private List<String> transformHeader(List l) throws IllegalArgumentException, IllegalAccessException {
        if (l.size() == 0)
            return null;

        LinkedList<String> result = new LinkedList<String>();
        Object o = l.get(0);
        for (Field f : o.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            result.add(f.getName());
        }

        return result;
    }

    public StringListList transformRows(@SuppressWarnings("rawtypes") List l) throws IllegalAccessException {
        if (l.size() == 0)
            return null;

        StringListList result = new StringListList();

        for (Object o : l)
            result.addRow(transformRow(o));

        return result;
    }

}
