package app;

import app.server.ScanRunner;
import org.cg.common.io.logging.DelegatingOutputStream;
import org.cg.common.io.logging.OnLineWritten;

import java.util.logging.Logger;

public class AdScraperApplication {
    private static final Logger LOG = Logger.getLogger(ScanRunner.class.getSimpleName());

    private boolean forkSystemOutput = true;

    public static void main(String[] args) {
        new AdScraperApplication().forkSystemOutput();
    }

    public void forkSystemOutput() {
        if (forkSystemOutput) {
            OnLineWritten redirect = LOG::info;

            System.setOut(DelegatingOutputStream.createPrintStream(System.out, redirect));
            System.setErr(DelegatingOutputStream.createPrintStream(System.err, redirect));
        }
    }

}