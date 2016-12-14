package org.cg.hub;

import org.cg.history.History;
import org.gwtTests.base.Check;

import com.google.common.base.Predicate;

public class CheckIfNew implements Predicate<String> {

	String urlId;
	History history;

	public CheckIfNew(History history, String urlId) {
		Check.notNull(history);
		Check.notEmpty(urlId);

		this.urlId = urlId;
		this.history = history;
	}

	@Override
	public boolean apply(String url) {
		return !history.find(urlId, url);
	}

}
