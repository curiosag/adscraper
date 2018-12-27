package app.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RudiHttpServer {

    private final Logger log = Logger.getLogger(RudiHttpServer.class.getSimpleName());
    private final int port;

    private boolean running = true;

    public RudiHttpServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000);
        while (running) {
            try (Socket clientSocket = serverSocket.accept()) {
                hdlRequest(clientSocket);
            } catch (SocketTimeoutException ex) {
            }
        }
    }

    private void hdlRequest(Socket clientSocket) throws IOException {
        try (BufferedReader in = getReader(clientSocket)) {
            try (PrintWriter out = getWriter(clientSocket)) {
                readHeaders(in);
                readPayload(in);
                reply(out);
            }
        }
    }

    private void readPayload(BufferedReader reader) throws IOException {
        StringBuilder payload = new StringBuilder();
        while (reader.ready()) {
            payload.append((char) reader.read());
        }
        System.out.println("Payload data is: " + payload.toString());
    }

    private void readHeaders(BufferedReader reader) throws IOException {
        String headerLine = null;
        while ((headerLine = reader.readLine()).length() != 0) {
            System.out.println(headerLine);
        }
    }

    private void reply(PrintWriter out) {
        out.write("HTTP/1.0 773 MU\r\n");
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

}
