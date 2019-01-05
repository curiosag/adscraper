package app.server;

import org.cg.base.Check;
import org.cg.common.util.Exc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class RudiHttpServer implements Runnable {

    private final Logger LOG = Logger.getLogger(RudiHttpServer.class.getSimpleName());
    private final int port;

    private boolean running;

    public RudiHttpServer(int port) {
        this.port = port;
    }

    private Supplier<String> get = () -> "nope";
    private Runnable del = () -> {
    };
    private Function<InputStream, Boolean> put = i -> false;

    public synchronized void start() {
        if (running) {
            throw new IllegalStateException("already running");
        }
        LOG.info("rudi on " + port);
        running = true;
        new Thread(this).start();
    }

    @Override
    public synchronized void run() {
        if (!running) {
            throw new IllegalStateException("Rudi won't run sans start.");
        }
        try (ServerSocket serverSocket = new ServerSocket(port)){
            serverSocket.setSoTimeout(1000);
            while (running && !Thread.interrupted()) {
                try (Socket clientSocket = serverSocket.accept()) {
                    clientSocket.setSoTimeout(3000);
                    hdlRequest(clientSocket);
                } catch (SocketTimeoutException ex) {
                }
            }
        } catch (Exception e) {
            LOG.severe("Rudi died from " + e.getMessage() + "\r\n" + Exc.getStackTrace(e));
        }
    }

    private void hdlRequest(Socket clientSocket) throws IOException {
        try (BufferedReader in = getReader(clientSocket)) {
            try (PrintWriter out = getWriter(clientSocket)) {
                try {
                    HttpHeader header = readHeader(in);
                    if ("POST".equals(header.method))
                        hdlPut(header.contentLength, in, out);
                    else if ("GET".equals(header.method)) {
                        hdlGet(out);
                    } else if ("DELETE".equals(header.method)) {
                        del.run();
                        replyStatus(true, out);
                    } else {
                        out.write("HTTP/1.0 773 DOPE\r\n");
                    }
                } catch (SocketTimeoutException ex) {
                    out.write("timeout");
                }
            }
        }
    }

    private void hdlGet(PrintWriter out) {
        String reply = get.get();
        replyStatus(reply != null, out);
        if (reply != null) {
            out.write(reply);
        }
    }

    private void replyStatus(boolean positive, PrintWriter out) {
        if (!positive)
            out.write("HTTP/1.0 144 NOPE\r\n");
        else {
            out.write("HTTP/1.0 200 OK\r\n");
        }
        out.write("\r\n");
    }

    private void hdlPut(int contentLength, BufferedReader in, PrintWriter out) throws IOException {
        if (contentLength <= 0) {
            replyStatus(false, out);
            return;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        String content = "";
        int read = 0;
        while (buffer.size() < contentLength) {
            read = in.read();
            content = content + String.valueOf((char) read);
            buffer.write(read);
        }
        buffer.flush();
        replyStatus(put.apply(new ByteArrayInputStream(buffer.toByteArray())), out);
        out.write(read);
    }

    private HttpHeader readHeader(BufferedReader reader) throws IOException {
        HttpHeader result = new HttpHeader();
        String headerLine = reader.readLine();
        LOG.info(headerLine);
        result.method(getMethod(headerLine));
        while (!empty(headerLine)) {
            headerLine = reader.readLine();
            LOG.info(headerLine);
            if (empty(headerLine)) {
                return result;
            }
            if (headerLine.toLowerCase().startsWith("content-length:")) {
                result.contentLength(decodeInt(headerLine.substring(15).trim()));
            }
        }
        return result;
    }

    private String getMethod(String headerLine) {
        if (headerLine == null || !headerLine.contains(" ")) {
            return null;
        }
        return headerLine.substring(0, headerLine.indexOf(" ")).toUpperCase();
    }

    private int decodeInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
        }
        return -1;
    }

    private boolean empty(String headerLine) {
        return headerLine == null || headerLine.length() == 0;
    }

    private PrintWriter getWriter(Socket clientSocket) throws IOException {
        return new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
    }

    private BufferedReader getReader(Socket clientSocket) throws IOException {
        return new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public synchronized void stop() {
        this.running = false;
    }

    public synchronized void setGet(Supplier<String> get) {
        Check.notNull(get);
        this.get = get;
    }

    public synchronized void setPut(Function<InputStream, Boolean> put) {
        Check.notNull(put);
        this.put = put;
    }

    public void setDel(Runnable del) {
        Check.notNull(del);
        this.del = del;
    }
}
