package org.cg.util.http;

import static org.cg.base.Idiom.*;

import org.cg.ads.advalues.WithUrl;
import org.jsoup.nodes.Document;

public final class HttpResult<T> {

	private final WithUrl<T> input;
	private final Exception problem;
	private final Document document;

	public HttpResult(WithUrl<T> input, Exception problem, Document document) {
		this.input = input;
		this.problem = problem;
		this.document =  document;
	}

	public Document document() {
		return document;
	}

	public WithUrl<T> input() {
		return input;
	}

	public Exception exception() {
		return problem;
	}

	public boolean success() {
		return no(problem);
	}
}
