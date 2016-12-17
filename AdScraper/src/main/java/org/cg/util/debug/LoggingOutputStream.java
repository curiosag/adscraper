package org.cg.util.debug;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class LoggingOutputStream extends ByteArrayOutputStream {

	private String lineSeparator;

	public LoggingOutputStream() {
		super();
		lineSeparator = System.getProperty("line.separator");
	}

	@Override
	public void flush() throws IOException {
		String content;
		synchronized (this) {
			super.flush();
			content = this.toString();
			super.reset();

			if (!(content == null || content.length() > 0 || content.equals(lineSeparator)))
				java.util.logging.Logger.getGlobal().info(content);
		}
	}
}