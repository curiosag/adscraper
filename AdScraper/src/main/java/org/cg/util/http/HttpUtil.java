package org.cg.util.http;

import org.jsoup.*;
import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import static org.cg.base.Const.STACK_TRACE;
import static org.cg.base.Idiom.*;

import java.io.*;

import org.apache.commons.io.*;
import org.cg.ads.advalues.WithUrl;
import org.cg.base.*;
import org.cg.util.parallel.Parallel;

;

public final class HttpUtil {

	public final static String baseUrl(String url) {
		Check.notNull(url);

		URL u;

		try {
			u = new URL(url);
		} catch (MalformedURLException e) {
			Log.logException(e, !Const.ADD_STACK_TRACE);
			return null;
		}

		String port;
		if (u.getPort() == -1)
			port = "";
		else
			port = ":" + u.getPort();

		return u.getProtocol() + "://" + u.getHost() + port;
	}

	// 15-01-13 appengine threw an exception after bazar's one redirect
	// it does not react to System.setProperty("http.maxRedirects", "1") either
	// so here's a hack to follow 301 redirects

	private static HttpURLConnection getHttpConnection(URL url) throws IOException {
		return (HttpURLConnection) url.openConnection();
	}

	private static DataInputStream _getHtmlInputStream(String url, int level) {
		if (level > 2) {
			Log.severe("redirect loop encountered");
			return null;
		} else
			try {
				URL url_ = new URL(url);
				String host = url_.getHost();

				HttpURLConnection conn = getHttpConnection(url_);
				conn.setInstanceFollowRedirects(false);
				conn.setConnectTimeout(Const.HTTP_TIMEOUT);

				conn.connect();

				if (conn.getResponseCode() != 301) {
					return new DataInputStream(new BufferedInputStream(conn.getInputStream()));
				} else {
					String location = conn.getHeaderField("Location");
					return _getHtmlInputStream("http://" + host + location, level + 1);
				}

			} catch (Exception e) {
				Log.logException(e, !Const.ADD_STACK_TRACE);
				return null;
			}
			}

	private static DataInputStream getHtmlInputStream(String url) {
		return _getHtmlInputStream(url, 0);
	}

	public final static String getHtmlInputString(String url) {
		Check.notNull(url);

		DataInputStream s = getHtmlInputStream(url);
		if (s != null) {
			try {
				return IOUtils.toString(s);
			} catch (IOException e) {
				Log.logException(e, Const.ADD_STACK_TRACE);
				return null;
			}
		} else {
			return null;
		}
	}
	
	public final static Document getJsoupDoc(String url) {
		Check.notEmpty(url);

		try {
			try (DataInputStream s = getHtmlInputStream(url)) {
				if (s != null) {
					try {
						return Jsoup.parse(s, "UTF-8", url);
					} catch (IOException e) {
						Log.logException(e, Const.ADD_STACK_TRACE);
						return null;
					}
				}
			}
		} catch (IOException ioe) {
		}
		return null;
	}

	public final static Document getJsoupFromFile(String filePath, String baseUrl) {
		return Jsoup.parse(filePath, "UTF-8");
	}

	private static <T> HttpResult<T> getDoc(WithUrl<T> source) {
		HttpResult<T> result;
		try {
			result = new HttpResult<T>(source, null, getJsoupDoc(source.url()));
		} catch (Exception e) {
			Log.logException(e, no(STACK_TRACE));
			result = new HttpResult<T>(source, e, null);
		}
		return result;
	}

	public final static <T, V extends WithUrl<T>> Collection<HttpResult<T>> getDocsAsync(Collection<V> sources) {

		Collection<HttpResult<T>> result;
		try {
			result = new Parallel.ForEach<V, HttpResult<T>>(sources)
					.apply(new Parallel.F<V, HttpResult<T>>() {
						public HttpResult<T> apply(V u) {
							return getDoc(u);
						}
					}).values();
		} catch (InterruptedException | ExecutionException e) {
			Log.logException(e, Const.ADD_STACK_TRACE);
			result = new LinkedList<HttpResult<T>>();

			for (WithUrl<T> source : sources)
				result.add(new HttpResult<T>(source, e, null));
		}
		return result;
	}

	public final static <T> Collection<Wrap<Wrap<T, String>, Document>> wrapDocAsync(Collection<Wrap<T, String>> sourceUrls) {
		return null;
	}
}
