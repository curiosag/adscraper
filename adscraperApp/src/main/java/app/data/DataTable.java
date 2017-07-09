package app.data;

import java.util.Collections;
import java.util.List;

/**
 * Created by ssmertnig on 3/26/17.
 */
public class DataTable {
    private List<String> headers;
    private StringListList rows;

    public DataTable(List<String> headers, StringListList rows){
        this.headers = Collections.unmodifiableList(headers);
        this.rows = rows;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public List<List<String>> getRows() {
        return rows.getRows();
    }
}
