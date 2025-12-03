package org.cg.base;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

public class Log {
	private static Logger instance;

	private Log() {
		super();
	}

	private static synchronized Logger getInstance() {
		if (instance == null) {
			instance = Logger.getLogger(Const.LOGKEY);
		}
		return instance;
	}

	public static Logger get() {
		return getInstance();
	}

	public static void info(String msg) {
		get().info(msg);
	}

	public static void warning(String msg) {
		get().warning(msg);
	}

	public static void error(String msg) {
		get().severe(msg);
	}
	
	public static void severe(String msg) {
		get().severe(msg);
	}

	public static void debug(String msg) {
		get().info("DEBUG " + msg);
	}

	public static void logException(Exception e, boolean addStackTrace) {
		Check.notNull(e);

		severe(String.format("Exception: %s Message: %s", e.getClass().getName(), e.getMessage()));
		if (addStackTrace) {
			StringWriter writer = new StringWriter();
			PrintWriter printWriter= new PrintWriter(writer);
			e.printStackTrace(printWriter);
			severe(writer.toString());
		}
	}

	public static void logStrings(Iterable<String> strings) {
		Check.notNull(strings);

		for (String s : strings)
			Log.info(s);
	}
}
