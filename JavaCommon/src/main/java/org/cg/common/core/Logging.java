package org.cg.common.core;

import java.util.Optional;

public interface Logging {
	void Info(String info);
	void Error(String error);
	
	Optional<String> lastInfo();
	Optional<String> lastError();
	
}

