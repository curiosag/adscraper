package org.cg.util.http;

import org.apache.commons.io.IOUtils;
import org.cg.ads.advalues.WithUrl;
import org.cg.base.Check;
import org.cg.base.Const;
import org.cg.base.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.cg.base.Const.STACK_TRACE;
import static org.cg.base.Idiom.no;


public final class HttpUtil {

    WebDriver driver = null;

    private WebDriver getDriver() {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("headless");
            driver = new ChromeDriver(options);
        }
        return driver;
    }

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

    public Document getJsoupDoc(String url, boolean jsEnabled) {
        Check.notEmpty(url);

        try {
            String s = jsEnabled ? getBySelenium(url) : getByUrlConnection(url);
            if (s != null) {
                return Jsoup.parse(s, url);
            }
        } catch (Exception e) {
            Log.logException(e, Const.ADD_STACK_TRACE);
        }

        return null;
    }

    public String getByUrlConnection(String url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(Const.HTTP_TIMEOUT);
            conn.connect();
            return IOUtils.toString(conn.getInputStream(), StandardCharsets.UTF_8.name());
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private synchronized String getBySelenium(String url) {
        getDriver().get(url);
        return getDriver().getPageSource();
    }

    private <T> HttpResult<T> getDoc(WithUrl<T> source, boolean jsEnabled) {
        try {
            return new HttpResult<T>(source, null, getJsoupDoc(source.url(), jsEnabled));
        } catch (Exception e) {
            Log.logException(e, no(STACK_TRACE));
            return new HttpResult<T>(source, e, null);
        }
    }

    public <T, V extends WithUrl<T>> Collection<HttpResult<T>> getDocs(Collection<V> sources, boolean jsEnabled) {
        return sources.stream().parallel().map(source -> getDoc(source, jsEnabled)).collect(Collectors.toList());
    }

}
