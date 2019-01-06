package app.server;

import java.util.Collection;
import java.util.Map;

public class HttpHeader {
    public int contentLength = -1;
    public String method;
    public Map<String, String> params;
    public Collection<String> path;
    
    public HttpHeader path(Collection<String> path) {
        this.path = path;
        return this;
    }
    
    public HttpHeader params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public HttpHeader method(String method) {
        this.method = method;
        return this;
    }

    public HttpHeader contentLength(int value){
        contentLength = value;
        return this;
    }
}
