package app.server;

import org.junit.Test;

public class RudiHttpServerTest {

    @Test
    public void start() {
        new RudiHttpServer(1781).start();
    }

}