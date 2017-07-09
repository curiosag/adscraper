package app.data;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ssmertnig on 3/26/17.
 */
public class StringListList {
    private LinkedList<List<String>> rows = new LinkedList<List<String>>();

    public void addRow(List<String> row){
        rows.add(Collections.unmodifiableList(row));
    }

    public List<List<String>> getRows(){
        return Collections.unmodifiableList(rows);
    }
};
