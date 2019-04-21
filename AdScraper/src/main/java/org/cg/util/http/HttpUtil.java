package org.cg.util.http;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.cg.ads.advalues.WithUrl;
import org.cg.base.Check;
import org.cg.base.Const;
import org.cg.base.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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

    public static String getHtmlString(String url, boolean jsEnabled) {

        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.SEVERE);

        try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_60)) {
            webClient.getOptions().setJavaScriptEnabled(jsEnabled);
            webClient.getOptions().setCssEnabled(jsEnabled);

            webClient.waitForBackgroundJavaScript(60000);
            HtmlPage page = ((HtmlPage) webClient.getPage(url));
            return page.asXml();
        } catch (Exception e) {
            Log.logException(e, !Const.ADD_STACK_TRACE);
            return null;
        }
    }


    public static Document getJsoupDoc(String url, boolean jsEnabled) {
        Check.notEmpty(url);

        String s = getHtmlString(url, jsEnabled);
        if (s != null) {
            try {
                return Jsoup.parse(s, url);
            } catch (Exception e) {
                Log.logException(e, Const.ADD_STACK_TRACE);
            }
        }

        return null;
    }

    private static <T> HttpResult<T> getDoc(WithUrl<T> source, boolean jsEnabled) {
        try {
            return new HttpResult<T>(source, null, getJsoupDoc(source.url(), jsEnabled));
        } catch (Exception e) {
            Log.logException(e, no(STACK_TRACE));
            return new HttpResult<T>(source, e, null);
        }
    }

    public final static <T, V extends WithUrl<T>> Collection<HttpResult<T>> getDocs(Collection<V> sources, boolean jsEnabled) {
        return sources.stream().parallel().map(source -> HttpUtil.getDoc(source, jsEnabled)).collect(Collectors.toList());
    }

}
