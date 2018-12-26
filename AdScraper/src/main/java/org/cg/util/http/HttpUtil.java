package org.cg.util.http;

import org.apache.commons.io.IOUtils;
import org.cg.ads.advalues.WithUrl;
import org.cg.base.Check;
import org.cg.base.Const;
import org.cg.base.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.cg.base.Const.STACK_TRACE;
import static org.cg.base.Idiom.no;

public final class HttpUtil {

    public static String baseUrl(String url) {
        Check.notNull(url);

        URL u;
        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            Log.logException(e, !Const.ADD_STACK_TRACE);
            return null;
        }

        String port = u.getPort() == -1 ? "" : ":" + u.getPort();
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

    public static String getHtmlInputString(String url) {
        Check.notNull(url);

        DataInputStream s = getHtmlInputStream(url);
        if (s != null) {
            try {
                return IOUtils.toString(s);
            } catch (IOException e) {
                Log.logException(e, Const.ADD_STACK_TRACE);
                return null;
            }
        }
        return null;
    }

    public static Document getJsoupDoc(String url) {
        Check.notEmpty(url);

        try (DataInputStream s = getHtmlInputStream(url)) {
            if (s != null) {
                try {
                    return Jsoup.parse(s, "UTF-8", url);
                } catch (IOException e) {
                    Log.logException(e, Const.ADD_STACK_TRACE);
                }
            }
        } catch (IOException ignored) {
        }
        return null;
    }

    private static <T> HttpResult<T> getDoc(WithUrl<T> source) {
        try {
            return new HttpResult<T>(source, null, getJsoupDoc(source.url()));
        } catch (Exception e) {
            Log.logException(e, no(STACK_TRACE));
            return new HttpResult<T>(source, e, null);
        }
    }

    public final static <T, V extends WithUrl<T>> Collection<HttpResult<T>> getDocs(Collection<V> sources) {
        return sources.stream().parallel().map(HttpUtil::getDoc).collect(Collectors.toList());
    }

}
