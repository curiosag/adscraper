package org.cg.common.io;

import org.cg.common.util.StringUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.function.Consumer;

public class DelegatingOutputStream extends ByteArrayOutputStream {

    private final PrintStream redirect;
    private final Consumer<String> delegate;

    public DelegatingOutputStream(PrintStream redirect, Consumer<String> delegate) {
        super();
        this.delegate = delegate;
        this.redirect = redirect;
    }

    @Override
    public void flush() throws IOException {

        synchronized (this) {
            super.flush();
            passOn(this.toString());
            reset();
        }
    }

    private void passOn(String s) {
        if (delegate == null || StringUtil.emptyOrNull(s) || s.equals(System.lineSeparator())) {
            return;
        }

        if (redirect != null) {
            redirect.print(s);
            redirect.flush();
        }

        delegate.accept(s);
    }

    public static PrintStream createPrintStream(PrintStream redirect, Consumer<String> delegate) {
        return new PrintStream(new DelegatingOutputStream(redirect, delegate), true);
    }
}