package app.server;

import app.data.DataTable;

/**
 * Created by ssmertnig on 3/26/17.
 */
public class WebConsoleFormData {
    private String cmd;
    private String reply;
    private DataTable dataTable;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public void setElement(Object o){
        if (o instanceof String)
            reply = (String)o;

    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }
}
