package app.server;

public class HttpHeader {
    public int contentLength = -1;
    public String method;

    public HttpHeader method(String method) {
        this.method = method;
        return this;
    }

    public HttpHeader contentLength(int value){
        contentLength = value;
        return this;
    }
}
